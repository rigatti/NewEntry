function checkDate(strDate, dateType) {
	var c;
	var d, m, y;
	var sepCount	= 0;
	var sepPos		= new Array();
	var strTmp		= strtrim(strDate);
	var len			= strTmp.length;
	var pos			= 0;

	if (dateType == 2)	strResult  = "00000000";
	else				strResult  = "0000";

	if (len == 0)		return 0;

	// Check for date separators
	while ((pos < len) && (sepCount <= dateType)) {
		c = strTmp.charAt(pos);

		if ((c == ' ') || (c == '-') || (c == '/')) {
			sepPos[sepCount] = pos;
			sepCount++;
		} else if ((c < '0') || (c > '9')) {
			return -1;
		}

		pos++;
	}

	// Extract day, month, year
	if (sepCount == 0) {
		if  ((dateType == 2) && ((len == 6) || (len == 8))) {
			d = parseFloat(strTmp.substring(0, 2));
			m = parseFloat(strTmp.substring(2, 4));
			y = parseFloat(strTmp.substring(4, 8));

			if ((len == 8) && (y < 100)) {
				return -1;
			}
		} else if (len == 4) {
			d = parseFloat(strTmp.substring(0, 2));
			m = parseFloat(strTmp.substring(2, 4));

			var dt = new Date();
			y = dt.getYear();
		} else {
			return -1;
		}
	} else if ((sepCount == 1) && (len <= 5)) {
		d = parseFloat(strTmp.substring(0, sepPos[0]));
		m = parseFloat(strTmp.substring(sepPos[0] + 1, 5));

		var dt = new Date();
		y = dt.getYear();
	} else if ((dateType == 2) && (sepCount == 2) && (len <= 10)) {
		d = parseFloat(strTmp.substring(0, sepPos[0]));
		m = parseFloat(strTmp.substring(sepPos[0] + 1, sepPos[1]));

		var yLen = strTmp.substring(sepPos[1] + 1, 10).length;

		if (yLen == 0) {
			return -1;
		}

		y = parseFloat(strTmp.substring(sepPos[1] + 1, 10));

		if ((yLen > 2) && (y < 100)) {
			return -1;
		}
	} else {
		return -1;
	}

	if ((y >= 30) && (y <= 130)) {
		y += 1900;
	} else if (y < 30) {
		y += 2000;
	}

	if ((y < 1900) || (y > 2200) || (m < 1) || (m > 12)) {
		return -1;
	}

	var days = new Array(31,28,31,30,31,30,31,31,30,31,30,31);

	days[1] = ((y % 4 || !(y % 100)) && (y % 400)) ? 28 : 29;

	if ((d < 1) || (d > days[m - 1])) {
		return -1;
	}

	if (dateType == 2) {
		strResult = new String(y) + new String((m < 10) ? "0" + m : m) + new String((d < 10) ? "0" + d : d);
	} else {
		strResult = new String((m < 10) ? "0" + m : m) + new String((d < 10) ? "0" + d : d);
	}

	return 1;
}

//-----------------------------------------------------------------------------

function computeDate(strDate, dType, dValue) {
	var day		= parseFloat(strDate.substring(6,8));
	var month	= parseFloat(strDate.substring(4,6));
	var year	= parseFloat(strDate.substring(0,4));

	if (dType == "D") {
		var dateUTC = parseFloat(Date.UTC(year, month - 1, day, 0, 0, 0)) + (dValue * 86400000.0);
		var dateRes = new Date(dateUTC);

		day = dateRes.getDate();
		month = dateRes.getMonth() + 1;
		year = dateRes.getYear();

		if (year < 100) {
			year += 1900;
		}
	} else if (dType == "M") {
		var nMonth = dValue % 12;				// Number of months
		var nYear = ((dValue - nMonth) / 12);	// Number of years

		if (dValue < 0) {
			if (month + nMonth <= 0) {
				month += 12;
				year--;
			}
		} else {
			if (month + nMonth > 12) {
				month -= 12;
				year++;
			}
		}

		month += nMonth;
		year += nYear;

		// Adjust eventualy the day
		var days	= new Array(31,28,31,30,31,30,31,31,30,31,30,31);
		days[1]		= ((year % 4 || !(year % 100)) && (year % 400)) ? 28 : 29;

		if (day > days[month - 1]) {
			day = days[month - 1];
		}
	} else if (dType == "Y") {
		year += dValue;
	}
	strResult = new String((day < 10) ? "0" + day : day) + "-" + new String((month < 10) ? "0" + month : month) + "-" + new String(year);
	return (new String(year) + new String((month < 10) ? "0" + month : month) + new String((day < 10) ? "0" + day : day));
}

//-----------------------------------------------------------------------------

function checkTime(strTime, timeType) {
	var c;
	var h, m, s;
	var sepCount	= 0;
	var sepPos		= new Array();
	var strTmp		= strtrim(strTime);
	var len			= strTmp.length;
	var pos			= 0;

	if (timeType == 2)	strResult  = "000000";  //hhmmss
	else				strResult  = "0000";    //hhmm

	if (len == 0)		return 0;

	// Check for time separators
	while ((pos < len) && (sepCount <= timeType)) {
		c = strTmp.charAt(pos);

		if ((c == ' ') || (c == ':')) {
			sepPos[sepCount] = pos;
			sepCount++;
		} else if ((c < '0') || (c > '9')) {
			return -1;
		}

		pos++;
	}

	// Extract hours, minutes, seconds
	if (sepCount == 0) {
		if  ((timeType == 2) && (len == 6)) {
			h = parseFloat(strTmp.substring(0, 2));
			m = parseFloat(strTmp.substring(2, 4));
			s = parseFloat(strTmp.substring(4, 8));

		} else if (len == 4) {
			h = parseFloat(strTmp.substring(0, 2));
			m = parseFloat(strTmp.substring(2, 4));
			s = 0;

		} else {
			return -1;
		}
	} else if ((sepCount == 1) && (len <= 5)) {
		h = parseFloat(strTmp.substring(0, sepPos[0]));
		m = parseFloat(strTmp.substring(sepPos[0] + 1, 5));
		s = 0;
		
	} else if ((timeType == 2) && (sepCount == 2) && (len <= 8)) {
		h = parseFloat(strTmp.substring(0, sepPos[0]));
		m = parseFloat(strTmp.substring(sepPos[0] + 1, sepPos[1]));

		var sLen = strTmp.substring(sepPos[1] + 1, 8).length;

		if (sLen == 0) {
			return -1;
		}

		s = parseFloat(strTmp.substring(sepPos[1] + 1, 8));

	} else {
		return -1;
	}

	if ((h < 0) || (h > 23) || (m < 0) || (m > 59) || (s < 0) || (s > 59)) {
		return -1;
	}

	if (timeType == 2) {
		strResult = new String((h < 10) ? "0" + h : h) + new String((m < 10) ? "0" + m : m) + new String((s < 10) ? "0" + s : s);
	} else {
		strResult = new String((h < 10) ? "0" + h : h) + new String((m < 10) ? "0" + m : m);
	}

	return 1;
}
