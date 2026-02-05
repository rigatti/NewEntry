var strDefDateFmt = "dd-MM-yyyy"
var sortModeAsc = new Array(true, true, true, true, true, true, true, true, true, true);

function trimSpacesAndTabs(inString, trimLeading, trimTrailing) {
	var startPos = 0;
	var endPos   = inString.length - 1;

	if (trimLeading) {
		while ((startPos < inString.length) && (inString.charCodeAt(startPos) <= 32))
			startPos++;
	}

	if (trimTrailing) {
		while ((endPos > startPos) && (inString.charCodeAt(endPos) <= 32))
			endPos--;
	}

	return (inString.substring(startPos, endPos + 1));
}

function _isInteger(val) {
	var digits = "1234567890";
	for (var i=0; i < val.length; i++) {
		if (digits.indexOf(val.charAt(i)) == -1) { return false; }
		}
	return true;
}

function getIntFromString(str, startIdx, minlength, maxlength) {
	for (var i = maxlength; i >= minlength; i--) {
		var token = str.substring(startIdx, startIdx + i);

		if (_isInteger(token)) {
			return token;
		}
	}

	return null;
}

function unformatDate(formattedDate) {
	var MONTH_NAMES = new Array('jan','feb','mar','apr','may','jun','jul','aug','sep','oct','nov','dec');

	var i_formattedDate = 0;
	var i_format = 0;
	var token = "";
	var year = 0;
	var month = 0;
	var date  = 0;

	// Check  if the date received corresponds to the standard format
	if (strDefDateFmt.length != formattedDate.length) {
		return null;
	}

	formattedDate = formattedDate + "";

	while (i_format < strDefDateFmt.length) {
		// Get next token from format string
		var c = strDefDateFmt.charAt(i_format);
		token = "";

		while ((strDefDateFmt.charAt(i_format) == c) && (i_format < strDefDateFmt.length)) {
			token += strDefDateFmt.charAt(i_format);
			i_format++;
		}

		// Extract contents of value based on format token
		if ((token == "yyyy") || (token == "yy") || (token == "y")) {
			if (token == "yyyy")	year = getIntFromString(formattedDate, i_formattedDate, 4, 4);
			else if (token == "yy")	year = getIntFromString(formattedDate, i_formattedDate, 2, 2);
			else if (token == "y")	year = getIntFromString(formattedDate, i_formattedDate, 2, 4);

			if (year == null)	return null;

			i_formattedDate += year.length;
		} else if (token=="MMM") {
			for (var i = 0; i < MONTH_NAMES.length; i++) {
				if (formattedDate.substring(i_formattedDate,i_formattedDate + MONTH_NAMES[i].length).toLowerCase() == MONTH_NAMES[i]) {
					month = i + 1;
					i_formattedDate += 3;
					break;
				}
			}
		} else if ((token == "MM") || (token == "M")) {
			month = getIntFromString(formattedDate, i_formattedDate, token.length, 2);

			if (month == null)	return null;

			i_formattedDate += month.length;
		} else if ((token=="dd") || (token=="d")) {
			date = getIntFromString(formattedDate, i_formattedDate, token.length, 2);

			if (date == null)	return null;

			i_formattedDate += date.length;
		}
		else {
			i_formattedDate += token.length;
		}
	}

	var lYear = parseInt(year);

	if (lYear <= 20) {
		year = 2000 + lYear;
	} else if ((lYear >= 21) && (lYear <= 99)) {
		year = 1900 + lYear;
	}

	return parseFloat(Date.UTC(year, month - 1, date));
}

function unformatNumber(formattedNumber) {
	// Search the decimal point
	var idx1 = formattedNumber.lastIndexOf(",");
	var idx2 = formattedNumber.lastIndexOf(".");
	var decimalSeparator = ',';

	if ((idx1 != -1) && (idx2 != -1) && (idx2 > idx1)) {
		decimalSeparator = '.'
	}

	var unformattedNumber = "";
	var negative = false;

	for (var i = formattedNumber.length - 1; i >= 0; i--) {

		var c = formattedNumber.charAt(i);

		if ((c < "0") || (c > "9")) {
			if ((c != "-") && (c != ".") && (c != ",")) {
				return null;
			} else if (c == decimalSeparator) {
				unformattedNumber = "." + unformattedNumber;
			} else if (c == "-") {
				negative = true;
			}
		} else {
			unformattedNumber = c + unformattedNumber;
		}
	}

	return (parseFloat(unformattedNumber) * (negative ? -1 : 1));
}

function getInnerText(cell) {
	var result = "";

	var cn = cell.childNodes;
	var cnLength = cn.length;

	for (var i = 0; i < cnLength; i++) {
		switch (cn[i].nodeType) {
			case 1: // element
				result += getInnerText(cn[i]);
				break;

			case 3:	// text
				result += cn[i].nodeValue;
				break;
		}
	}

	return result;
}

function getRowValue(row, colId, colType) {
	if ((colId + 1) > row.cells.length) {
		return null;
	}

	var cell = row.cells[colId];
	var value = null;

	if (typeof cell.innerText != "undefined") {
		value = cell.innerText;
	} else {
		value = getInnerText(cell);
	}

	if ((colType == "Number") || (colType == "Amount")) { 
		value = trimSpacesAndTabs(value, true, true);

		if ((value == "") || (value == "-"))
			return (parseFloat(-999999999999999));

		if ((colType == "Amount") && (value.length > 4))  {
			return unformatNumber(value.substring(0, value.length - 4));
		} 

		return unformatNumber(value);
	} else if (colType == "Percent") {
		value = trimSpacesAndTabs(value, true, true);
		var length = value.length;

		if (length > 2) {
			return unformatNumber(value.substring(0, length - 2));
		}
	} else if (colType == "Date") {
		return unformatDate(trimSpacesAndTabs(value, true, true));
	} else if (colType == "Period") {
		var formattedDateStart = trimSpacesAndTabs(value, true, false).substring(0, strDefDateFmt.length);

		return unformatDate(formattedDateStart);
	} else if (colType == "String") {
		return trimSpacesAndTabs(value, true, true);
	}

	return null;
}

function sCompare(o1, o2) {
	if (o1.value < o2.value)
		return -1;

	if (o2.value < o1.value)
		return 1;

	return 0;
}

function sortTable(tableToSort, colId, colType, selectedTitleBgColor, unselectedTitleBgColor, evenBgColor, oddBgColor) {
	var tHead = tableToSort.tHead;
	var tBody = tableToSort.tBodies[0];

	// Create an array that contains the value on which the sort has to be made, and the complete row
	var rows = tBody.rows;
	var rowCount = rows.length;
	var arrayToSort = new Array(rowCount);
	var sortNeeded = false;

	if (rowCount > 0) {
		if (rowCount > 1000) {
			if (confirm(sortWarnMsg) == false) {
				return;
			}
		}

		var valReference = getRowValue(rows[0], colId, colType);

		for (var i = 0; i < rowCount; i++) {
			var row = rows[i];
			var val = getRowValue(row, colId, colType);

			if (val != valReference) {
				sortNeeded = true;
			}

			arrayToSort[i] = { value: val, element: row };
		}
	}

	if (selectedTitleBgColor != null) {
		for (var i = 0; i < tHead.rows[0].cells.length; i++) {
			tHead.rows[0].cells[i].bgColor = ((i == colId) ? selectedTitleBgColor : unselectedTitleBgColor);
		}
	}

	if (sortNeeded) {
		arrayToSort.sort(sCompare);

		// Check the sorting mode
		if (sortModeAsc[colId] == false) {
			arrayToSort.reverse();
		}

		for (var i = 0; i < sortModeAsc.length; i++) {
			sortModeAsc[i] = ((i == colId) ? !sortModeAsc[i] : true);
		}

		for (var i = 0; i < rowCount; i++) {
			if (evenBgColor != null) {
				arrayToSort[i].element.bgColor = ((i % 2) != 0) ? oddBgColor : evenBgColor;
			}

			tBody.appendChild(arrayToSort[i].element);
		}
	}
}
