
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.belex.util.Util;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import db.entry.treated.TreatedEntryDAO;
import db.product.ProductDAO;
import db.supplier.Supplier;
import db.supplier.SupplierDAO;

public class Test {

	public static void main(String[] args) {
		/*Util.getNowFormated("yyyyMMdd");
		
		Util.containsAll("25,10,40","40,85,10", ",");
		
		//Chargement du contexte
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("TESTflowlauncher-servlet.xml");

		//((TreatedEntryDAO) ctx.getBean("treatedEntryDAO")).test();
		
		insertTreatedEntry(((TreatedEntryDAO) ctx.getBean("treatedEntryDAO")));
		//String result = ((ProductDAO) ctx.getBean("ProductDAO")).save(new Product("002", "BEL"));
		
		// find airports
		//ArrayList<Airport> airports = ((AirportDAO) ctx.getBean("AirportDAO")).find("002", "BEL");
		
		//complete product
		String description = ((ProductDAO) ctx.getBean("ProductDAO")).getDescription("COROLIE");

		List<Supplier> suppliers = ((SupplierDAO) ctx.getBean("NewSupplierDAO")).getAll();

		//get supplierOrderDetail
		//ArrayList<SupplierOrderDetail> supplierOrderDetails = ((SupplierOrderDetailDAO) ctx.getBean("SupplierOrderDetailDAO")).get("COROLIE", new StringTokenizer("21"));

		//int to = ((PackagingDetailDAO) ctx.getBean("PackagingDetailDAO")).getToPackageNumber("20", 100);
		*/


System.out.println("INSERT INTO `ps_group_lang` (`id_group`, `id_lang`, `name`) VALUES");
for (int i = 1002; i < 1999; i++) {
	if (i != 1010 && i != 1100 && i != 1225) {
		String istr = String.valueOf(i);
		System.out.println("(" + i + ", 1, 'client Pro " + StringUtils.substring(istr, 1, 3) + "." + StringUtils.substring(istr, 3) + "'),");
		System.out.println("(" + i + ", 2, 'client Pro " + StringUtils.substring(istr, 1, 3) + "." + StringUtils.substring(istr, 3) + "'),");
		System.out.println("(" + i + ", 3, 'client Pro " + StringUtils.substring(istr, 1, 3) + "." + StringUtils.substring(istr, 3) + "'),");
	}
}
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

	}

	public static boolean insertTreatedEntry(TreatedEntryDAO dao) {
		/*
		boolean result = true;

		TreatedEntry treatedEntry = new TreatedEntry();
		treatedEntry.setValidityDate("validityDate");
		treatedEntry.setProductCode("productCode");
		treatedEntry.setEan("ean");
		treatedEntry.setLotNumber("lotNumber");
		treatedEntry.setSupplierCode("supplierCode");
		
		dao.insert(treatedEntry);
		
		TreatedEntryDetail treatedEntryDetail = new TreatedEntryDetail();
		treatedEntryDetail.setTreatedEntryId(treatedEntry.getTreatedEntryId());

		treatedEntryDetail.setSupplierCommandeNumber(123);
		treatedEntryDetail.setArrivalDate("arrivalDate");
		treatedEntryDetail.setArrivalTime("arrivalTime");
		treatedEntryDetail.setAdditionalData("additionalData");
		treatedEntryDetail.setDescription("description");
		treatedEntryDetail.setUnitConditionnement("unitConditionnement");
		treatedEntryDetail.setNumberOfUnit(456);
		treatedEntryDetail.setNumberOfProduct(789);
		
		for (int i = 0; i < 2; i++) {

			TreatedEntryDetailDestination treatedEntryDetailDestination = new TreatedEntryDetailDestination();
			treatedEntryDetailDestination.setTreatedEntryDetailId(treatedEntryDetail.getTreatedEntryDetailId());

			treatedEntryDetailDestination.setCustomerCode("clients.get(i).getCode()");
			treatedEntryDetailDestination.setAirportCode("airportCode");
			treatedEntryDetailDestination.setFlight("flight");
			treatedEntryDetailDestination.setLta("lta");
			treatedEntryDetail.getTreatedEntryDetailDestinations().add(treatedEntryDetailDestination);
		}

		/*
			sql = 
				"insert into TreatedEntryDetailDestination(" +
											"IDTreatedEntryDetail, " +
											"clientCode, " +
											"airportCode, " +
											"flight, " +
											"lta" +
									  	") " +
						"values(" + 
									IDTreatedEntryDetail + ", " +
									"'" + clients.get(i).getCode() + "', " +
									"'" + airportCode + "', " +
									"'" + flight + "', " +
									"'" + lta + "'" +
								"); ";
			
			stTreatedEntry.execute(sql);
			*/

		/*
		treatedEntry.getTreatedEntryDetails().add(treatedEntryDetail);
		
		dao.saveOrUpdate(treatedEntry);
		
		return result;
		*/
		return false;
	}
}
