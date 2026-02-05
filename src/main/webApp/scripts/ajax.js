	function generateUrl(obj, changeFlowExecutionKey) {
		var url = obj.action + "?";
		var formElements = obj.elements
		for (i=0; i < formElements.length; i++) {
			if (formElements[i].name != "") {
				if (changeFlowExecutionKey) {
					if (formElements[i].name == "_flowExecutionKey") {
						if (window.document.frmNewFlowExecutionKey) {
							newFlowExecutionKey = window.document.frmNewFlowExecutionKey.newKey.value;
						}
						if (newFlowExecutionKey != "") {
							formElements[i].value = newFlowExecutionKey;
						}
					}
				}
				url += formElements[i].name + "=" + formElements[i].value + "&";
			}
		}
		return url;
	}

    function makeRequest(formObj, changeFlowExecutionKey) {
		
		var url = generateUrl(formObj, changeFlowExecutionKey);
        var httpRequest = false;
        if (window.XMLHttpRequest) { // Mozilla, Safari,...
            httpRequest = new XMLHttpRequest();
            if (httpRequest.overrideMimeType) {
                httpRequest.overrideMimeType('text/xml');
            }
        }
        else if (window.ActiveXObject) { // IE
            try {
                httpRequest = new ActiveXObject("Msxml2.XMLHTTP");
            }
            catch (e) {
                try {
                    httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
                }
                catch (e) {}
            }
        }
        if (!httpRequest) {
            alert('Abandon :( Impossible de créer une instance XMLHTTP');
            return false;
        }
        httpRequest.onreadystatechange = function() { analyseAjaxResponse(httpRequest); };
        httpRequest.open('GET', url, true);
        httpRequest.send(null);

    }
	function analyseAjaxResponse(httpRequest) {
        try {
            if (httpRequest.readyState == 4) {
                if (httpRequest.status == 200) {
                	// defined in each jsp using this ajax script
                    treatAjaxResponse(httpRequest);
                } else {
                    alert('Un problème est survenu au cours de la requête.');
                }
            }
        }
        catch( e ) {
            alert("Une exception s'est produite : " + e.description);
        }
	
	}
