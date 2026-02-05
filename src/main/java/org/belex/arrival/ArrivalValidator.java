package org.belex.arrival;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ArrivalValidator implements Validator {

	public boolean supports(Class clazz) {
		return clazz.equals(Arrival.class);
	}
	public void fillContext(Arrival arrival, Errors errors) {
		String s = "";
	}
	public void validate(Object obj, Errors errors) {
		Arrival arrival = (Arrival)obj;
	}

	public void validateSupplierSelection(Arrival arrival, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "supplierCode", "noSupplierCode", "Veuillez introduire un nom de fournisseur.");
	}
	
	public void validateEntryForm(Arrival arrival, Errors errors) {
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "codeInput", "noProductCode", "Veuillez introduire code produit.");
	}

	public void validateCompleteEntry(Arrival arrival, Errors errors){
		// pull the date from the model
		String s = "";
	}
}