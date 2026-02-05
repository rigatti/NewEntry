var strResult;   // Global variable
var newFlowExecutionKey = ""; // springWebflow execution key
//---------------------------------

function confirmAction() {
	return confirm("Etes-vous sûr?");
}

function getSelectedValue(selObj) {
	var idx = selObj.selectedIndex;

	if (idx >= 0) {
		return selObj.options[idx].value;
	}

	return "";
}

function changeCheckBox(id) {
	var checkbox = window.document.getElementById(id);
	checkbox.checked = (!checkbox.checked);
}

function strtrim(inString) {
	var startPos = 0;
	var endPos   = inString.length - 1;

	while ((startPos < inString.length) && (inString.charAt(startPos) == ' '))
		startPos++;

	while ((endPos > startPos) && (inString.charAt(endPos) == ' '))
		endPos--;

	return (inString.substring(startPos, endPos + 1));
}

function errormessage(errString, errObj) {
	alert(errString);

	if (errObj != null) {
		errObj.focus();
	}

	return false;
}

function replace(srcString, searchString, replaceString) {
	var i = srcString.indexOf(searchString);

	if (i > -1)
		return (srcString.substring(0, i) + replaceString + srcString.substring(i + searchString.length, srcString.length));
	else
		return srcString;
}

function replaceAll(srcString, searchString, replaceString) {
	var i = srcString.indexOf(searchString);
	var newString = srcString;

	while (i > -1) {
		newString = newString.substring(0, i) + replaceString + newString.substring(i + searchString.length, newString.length);
		i = newString.indexOf(searchString);
	}
	return newString;
}

function getRadioValue(formObj, radioName) {
	for (i = 0; i < formObj.length; i++) {
		if (formObj.elements[i].type == "radio") {
			if ((formObj.elements[i].name == radioName) && (formObj.elements[i].checked == true))
				return (formObj.elements[i].value);
		}
	}

	return "";
}

function setRadioState(formObj, radioName, idx, chkState) {
	var curIdx = 0;
	var oldState = false;

	for (i = 0; i < formObj.length; i++) {
		if ((formObj.elements[i].type == "radio") && (formObj.elements[i].name == radioName)) {
			if (curIdx == idx) {
				oldState = (formObj.elements[i].checked != chkState);
				formObj.elements[i].checked = chkState;
				return oldState;
			} else {
				curIdx++;
			}
		}
	}

	return oldState;
}


function IsNumeric(strString) {
   //  check for valid numeric strings	
   var strValidChars = "0123456789.";
   var strChar;
   var blnResult = true;

   if (strString.length == 0) return false;

   //  test strString consists of valid characters listed above
   for (i = 0; i < strString.length && blnResult == true; i++) {
      strChar = strString.charAt(i);
      if (strValidChars.indexOf(strChar) == -1) {
         blnResult = false;
      }
   }
   return blnResult;
}

function switchDisplay(id) {
	var obj = window.document.getElementById(id);
	if (obj.style.visibility == "hidden"){
		obj.style.visibility = "visible";
		obj.style.display = "block";
	} else {
		obj.style.visibility = "hidden";
		obj.style.display = "none";
	}
}

function switchCheckBoxValue(id) {
	var obj = window.document.getElementById(id);
	if (obj.checked == true){
		obj.checked = false;
	} else {
		obj.checked = true;
	}
}
