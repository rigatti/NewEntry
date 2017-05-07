package org.belex.requestparams;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class RequestParamsValidator implements Validator {

	public boolean supports(Class clazz) {
		return clazz.equals(RequestParams.class);
	}

	public void validate(Object obj, Errors errors) {
	}
}