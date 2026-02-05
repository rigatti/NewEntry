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
package org.belex.export;

import org.springframework.webflow.action.FormAction;
import org.springframework.webflow.execution.ScopeType;

public class ExportFormAction extends FormAction {

	public ExportFormAction() {
		// tell the superclass about the form object and validator we want to use
		// you could also do this in the application context XML ofcourse
		setFormObjectName("export");
		setFormObjectClass(Export.class);
		setFormObjectScope(ScopeType.FLOW);
	}
}