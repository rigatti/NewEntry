package org.belex.export;

import db.customer.ICustomerDAO;
import db.editiontype.EditionType;
import db.editiontype.IEditionTypeDAO;
import db.family.FamilyLevel1;
import db.family.FamilyLevel3;
import db.product.Conditioning;
import db.product.IConditioningDAO;
import db.product.IProductDAO;
import db.product.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.belex.util.DataSender;
import org.belex.util.FileSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ExportBusiness implements IExportBusiness {

	private final IProductDAO productDAO;
	private final IConditioningDAO conditioningDAO;
	private final ICustomerDAO customerDAO;
	private final IEditionTypeDAO editionTypeDAO;

	@Override
	public Export productExport(Export export, String productReference, String productExportWithImage) {
		if (export == null) {
			export = new Export();
		}

		log.info("Product export for reference {}", productReference);

		String imagePath = "";
		if ("1".equals(productExportWithImage)) {
			imagePath = new FileSender().uploadImage(StringUtils.upperCase(productReference));
		}

		if (StringUtils.isBlank(imagePath)) {
			export.setErrorMessage("Image non transférée sur le site");
			log.error("Image not detected for reference {}", productReference);
			return export;
		}

		List<?> resultFromDb = productDAO.getProductsToExportByReference(productReference);
		extractData(export, resultFromDb);

		List<ProductToExport> products = export.getProducts();
		log.info("Number of products to export: {}", products == null ? 0 : products.size());

		String impactedRefs = buildImpactedReferences(products);

		for (ProductToExport product : products) {
			sendProduct(product, impactedRefs, imagePath);
		}

		return export;
	}

	private void extractData(Export export, List<?> resultFromDb) {
		for (Object row : resultFromDb) {
			Conditioning conditioning = null;
			Product product = null;
			EditionType editionType = null;
			FamilyLevel1 family1 = null;
			FamilyLevel3 family3 = null;

			for (Object obj : (Object[]) row) {
				if (obj instanceof Conditioning c) conditioning = c;
				if (obj instanceof Product p) product = p;
				if (obj instanceof EditionType e) editionType = e;
				if (obj instanceof FamilyLevel1 f1) family1 = f1;
				if (obj instanceof FamilyLevel3 f3) family3 = f3;
			}

			if (conditioning != null && product != null && editionType != null && family1 != null) {
				export.addProduct(buildResult(product, conditioning, editionType, family1));
			}
		}
	}

	private ProductToExport buildResult(
			Product product,
			Conditioning conditioning,
			EditionType editionType,
			FamilyLevel1 familyLevel1) {

		ProductToExport result = new ProductToExport();

		result.setProductCode(product.getProductCode());
		result.setDescription_fr(product.getDescription());
		result.setFamilyCode(product.getFamilyCode() + 1000);
		result.setSortingOrder(product.getSortingOrder());

		result.setEditionTypeLabel(editionType.getDescription());
		result.setEditionTypeId("10" + editionType.getId());

		if (StringUtils.isNotBlank(conditioning.getEditionTypeAdditionalId())) {
			EditionType add = editionTypeDAO.get(conditioning.getEditionTypeAdditionalId());
			if (add != null) {
				result.setEditionTypeAdditionalLabel(add.getDescription());
				result.setEditionTypeAdditionalId("10" + add.getId());
			}
		}

		result.setPro(
				"PRO".equalsIgnoreCase(result.getEditionTypeLabel()) ||
						"PRO".equalsIgnoreCase(result.getEditionTypeAdditionalLabel())
		);

		result.setPlaneMandatory(product.isPlaneMandatory());
		result.setUnitConditioning(conditioning.getUnit());
		result.setUnitNumber(conditioning.getNumberOfUnit());
		result.setUnitPrice(conditioning.getUnitPrice());
		result.setTotalConditioning(conditioning.getUnitLargeScale());
		result.setDateLastUpdate(StringUtils.remove(conditioning.getDateLastModification(), ".000"));
		result.setMarginForStandardCustomer(familyLevel1.getMargin() * -1);

		BigDecimal price = BigDecimal
				.valueOf(conditioning.getNumberOfUnit() * conditioning.getUnitPrice())
				.setScale(2, RoundingMode.UP);

		result.setTotalPrice(price.floatValue());

		return result;
	}

	private void sendProduct(ProductToExport product, String impactedRefs, String imagePath) {
		StringBuilder data = new StringBuilder();

		data.append("dateLastModif=").append(encode(product.getDateLastUpdate())).append("&");
		data.append("impactedReferences=").append(impactedRefs).append("&");
		data.append("referenceRoot=").append(encode(product.getProductCode())).append("&");
		data.append("reference=").append(encode(product.getProductCode())).append("&");
		data.append("isPlane=").append(product.isPlaneMandatory() ? 1 : 0).append("&");
		data.append("name=").append(encode(product.getDescription_fr())).append("&");
		data.append("description=").append(encode(product.getTotalConditioning())).append("&");
		data.append("price=").append(product.getTotalPrice()).append("&");
		data.append("category=").append(product.getFamilyCode()).append("&");
		data.append("sortingOrder=").append(product.getSortingOrder()).append("&");
		data.append("reduction_percent=").append(product.getMarginForStandardCustomer()).append("&");
		data.append("assignment_group=").append(product.getEditionTypeId()).append("&");
		data.append("newimage=1&image=").append(encode(imagePath));

		new DataSender().sendProductUpdate(data.toString());
	}

	private String buildImpactedReferences(List<ProductToExport> products) {
		return products.stream()
				.map(p -> encode(p.getProductCode()))
				.reduce((a, b) -> a + "," + b)
				.orElse("");
	}

	private String encode(Object data) {
		try {
			return data == null ? "" : URLEncoder.encode(String.valueOf(data), StandardCharsets.UTF_8);
		} catch (Exception e) {
			log.error("Encoding error", e);
			return "";
		}
	}
}
