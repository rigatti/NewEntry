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

import java.util.Vector;

import org.belex.entry.Entry;
import org.belex.product.Product;
import org.belex.product.Product.Unit;
import org.belex.supplier.Supplier;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.webflow.action.FormAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ScopeType;

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
		
		if (productsFound == null || productsFound.size() == 0) {
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
	

}