package org.belex.product;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ProductValidator implements Validator {

	public boolean supports(Class clazz) {
		return clazz.equals(Product.class);
	}

	public void validate(Object obj, Errors errors) {
	}
}