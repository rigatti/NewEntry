/*
 * Copyright 2004-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.belex.arrival;

import lombok.extern.slf4j.Slf4j;
import org.belex.entry.Entry;
import org.belex.product.Product;
import org.belex.product.Product.Unit;
import org.belex.requestparams.RequestParams;
import org.belex.supplier.Supplier;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.stereotype.Component;
import org.springframework.webflow.action.FormAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ScopeType;

import java.util.Vector;

@Component("arrivalFormAction")
@Slf4j
public class ArrivalFormAction extends FormAction {

	public ArrivalFormAction() {
		// tell the superclass about the form object and validator we want to use
		// you could also do this in the application context XML ofcourse
		setFormObjectName("arrival");
		setFormObjectClass(Arrival.class);
		setFormObjectScope(ScopeType.FLOW);
		setValidator(new ArrivalValidator());
	}

	protected void registerPropertyEditors(PropertyEditorRegistry registry) {
		// register a custom property editor to handle the date input
		//SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
		//registry.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	public Event bindSearchSupplierDate(RequestContext context) throws Exception {
		Arrival arrival = (Arrival) getFormObject(context);
		String searchSupplierDate = context.getExternalContext().getRequestParameterMap().get("searchSupplierDate");
		
		if (searchSupplierDate != null && !searchSupplierDate.isEmpty()) {
			arrival.setSearchSupplierDate(searchSupplierDate);
		}
		
		return success();
	}

	public Event resetNewEntry(RequestContext context) throws Exception {
		// pull the date from the model
		Arrival arrival = (Arrival)getFormObject(context);
		arrival.prepareNewEntry();
		//entry.prepareNewProduct();

		return success();
	}

	public Event checkProductsFound(RequestContext context) throws Exception {
		// pull the date from the model
		Vector<Product> productsFound = (Vector<Product>) context.getFlowScope().get("products");
		
		if (productsFound == null || productsFound.isEmpty()) {
			return new Event("completeProducts", "none");			
		}
		
		if (productsFound.size() > 1) {
			return new Event("completeProducts", "multiple");			
		}

		// multiple conditionnement detected
		if (productsFound.get(0).getUnits().size() > 1) {
			return new Event("completeProducts", "multiple");			
		}
		
		Arrival arrival = (Arrival)getFormObject(context);
		Product productFound = productsFound.get(0);
		Vector<Unit> units = productFound.getUnits();
		if (units.size() == 1) {
			Unit unit = units.get(0);
			unit.setSelected(true);
			units.set(0, unit);
			productFound.setUnits(units);
		}
		arrival.getEntry().setProduct(productFound);
		
		return new Event("completeProductsAct", "single");
	}

	public Event saveTempEntry(RequestContext context) throws Exception {
		Arrival arrival = (Arrival)getFormObject(context);
		Vector<Entry> savedEntries = arrival.getSavedEntries();
		if (savedEntries == null) {
			savedEntries = new Vector<Entry>();
		}
		savedEntries.add(arrival.getEntry());
		arrival.setSavedEntries(savedEntries);

		return success();
	}
	public Event updateTempEntry(RequestContext context) throws Exception {
		// check all saved entries and update the one corresponding with the current treatment.
		// remove the last element of the saved entries (= current entry in trt)
		Arrival arrival = (Arrival)getFormObject(context);
		Vector<Entry> savedEntries = arrival.getSavedEntries();
		
		Entry currentEntry = arrival.getEntry();

		for (int i = 0; i < savedEntries.size(); i++) {
			Entry currentSavedEntry = savedEntries.get(i);
			if (currentEntry.getProduct().getProductCode().equals(currentSavedEntry.getProduct().getProductCode()) &&
				currentEntry.getProduct().getUnits().get(0).getConditionnement().equals(currentSavedEntry.getProduct().getUnits().get(0).getConditionnement()) &&
				currentEntry.getProduct().getUnits().get(0).getNumber() == currentSavedEntry.getProduct().getUnits().get(0).getNumber()
				) {
				currentSavedEntry.setNumberOfProduct(currentEntry.getNumberOfProduct() + currentSavedEntry.getNumberOfProduct());
				savedEntries.set(i, currentSavedEntry);
				if (i < (savedEntries.size() - 1)) {
					savedEntries.remove(savedEntries.size() - 1);
				}
				break;
			}
		}

		arrival.setSavedEntries(savedEntries);

		return success();
	}
	
	public Event sortSuppliers(RequestContext context) throws Exception {
		Arrival arrival = (Arrival)getFormObject(context);
		Vector<Supplier> suppliers = arrival.getSuppliers();
		
		// TODO sort 
		return success();
	}

	public Event bindRequestParams(RequestContext context) throws Exception {
		Arrival arrival = (Arrival) getFormObject(context);

		// Récupérer tous les paramètres POST
		var requestParams = context.getExternalContext().getRequestParameterMap();

		// Créer un objet RequestParams et le remplir
		RequestParams params = new RequestParams();

		// Récupérer les paramètres pertinents
		String entryId = requestParams.get("entryId");
		String removeEntryId = requestParams.get("removeEntryId");
		String entryNumberOfProduct = requestParams.get("entryNumberOfProduct");
		String entryValidityDate = requestParams.get("entryValidityDate");
		String entryLotNumber = requestParams.get("entryLotNumber");

		if (entryId != null && !entryId.isEmpty()) {
			params.setEntryId(Integer.parseInt(entryId));
		}
		if (removeEntryId != null && !removeEntryId.isEmpty()) {
			params.setEntryId(Integer.parseInt(removeEntryId));
		}
		if (entryNumberOfProduct != null && !entryNumberOfProduct.isEmpty()) {
			params.setEntryNumberOfProduct(Integer.parseInt(entryNumberOfProduct));
		}
		if (entryValidityDate != null) {
			params.setEntryValidityDate(entryValidityDate);
		}
		if (entryLotNumber != null) {
			params.setEntryLotNumber(entryLotNumber);
		}

		// Stocker dans flowScope
		context.getFlowScope().put("requestParams", params);

		log.debug("bindRequestParams: entryId=" + params.getEntryId());

		return success();
	}

	public Event bindSupplierDocument(RequestContext context) throws Exception {
		Arrival arrival = (Arrival)getFormObject(context);
		
		// Get parameters from the form
		String supplierDocumentType = context.getExternalContext().getRequestParameterMap().get("supplierDocumentType");
		String supplierDocumentDescription = context.getExternalContext().getRequestParameterMap().get("supplierDocumentDescription");
		String supplierEntryProductIntegrity = context.getExternalContext().getRequestParameterMap().get("supplierEntryProductIntegrity");
		String supplierEntryPackagingIntegrity = context.getExternalContext().getRequestParameterMap().get("supplierEntryPackagingIntegrity");
		String supplierEntryDlcDdmValidity = context.getExternalContext().getRequestParameterMap().get("supplierEntryDlcDdmValidity");
		String supplierEntryTemperatureValidity = context.getExternalContext().getRequestParameterMap().get("supplierEntryTemperatureValidity");
		String supplierEntryCommentOnQuality = context.getExternalContext().getRequestParameterMap().get("supplierEntryCommentOnQuality");
		
		// Bind to arrival object
		if (supplierDocumentType != null && !supplierDocumentType.isEmpty()) {
			arrival.setSupplierDocumentType(Integer.parseInt(supplierDocumentType));
		}
		if (supplierDocumentDescription != null) {
			arrival.setSupplierDocumentDescription(supplierDocumentDescription);
		}
		if (supplierEntryProductIntegrity != null && !supplierEntryProductIntegrity.isEmpty()) {
			arrival.setSupplierEntryProductIntegrity(Integer.parseInt(supplierEntryProductIntegrity));
		}
		if (supplierEntryPackagingIntegrity != null && !supplierEntryPackagingIntegrity.isEmpty()) {
			arrival.setSupplierEntryPackagingIntegrity(Integer.parseInt(supplierEntryPackagingIntegrity));
		}
		if (supplierEntryDlcDdmValidity != null && !supplierEntryDlcDdmValidity.isEmpty()) {
			arrival.setSupplierEntryDlcDdmValidity(Integer.parseInt(supplierEntryDlcDdmValidity));
		}
		if (supplierEntryTemperatureValidity != null && !supplierEntryTemperatureValidity.isEmpty()) {
			arrival.setSupplierEntryTemperatureValidity(Integer.parseInt(supplierEntryTemperatureValidity));
		}
		if (supplierEntryCommentOnQuality != null) {
			arrival.setSupplierEntryCommentOnQuality(supplierEntryCommentOnQuality);
		}
		
		log.debug("bindSupplierDocument: documentType=" + arrival.getSupplierDocumentType() + 
				  ", description=" + arrival.getSupplierDocumentDescription() + 
				  ", productIntegrity=" + arrival.getSupplierEntryProductIntegrity());
		
		return success();
	}

}