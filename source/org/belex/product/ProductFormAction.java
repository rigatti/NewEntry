package org.belex.product;

import java.util.Vector;

import org.belex.arrival.Arrival;
import org.belex.product.Product.Unit;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.webflow.action.FormAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ScopeType;

public class ProductFormAction extends FormAction {

	public ProductFormAction() {
		// tell the superclass about the form object and validator we want to use
		// you could also do this in the application context XML ofcourse
		setFormObjectName("product");
		setFormObjectClass(Product.class);
		setFormObjectScope(ScopeType.FLOW);
		setValidator(new ProductValidator());
	}

	protected void registerPropertyEditors(PropertyEditorRegistry registry) {
		// register a custom property editor to handle the date input
		//SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
		//registry.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	public Event validateEntryForm(RequestContext context) throws Exception {
		getFormObject(context);
		//String s = context.getExternalContext().getRequestParameterMap().get("searchCodeInput");
		return new Event("","");
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
}