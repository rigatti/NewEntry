package org.belex.requestparams;

import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.webflow.action.FormAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ScopeType;

public class RequestParamsFormAction extends FormAction {

	public RequestParamsFormAction() {
		// tell the superclass about the form object and validator we want to use
		// you could also do this in the application context XML ofcourse
		setFormObjectName("requestParams");
		setFormObjectClass(RequestParams.class);
		setFormObjectScope(ScopeType.FLOW);
		setValidator(new RequestParamsValidator());
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
}