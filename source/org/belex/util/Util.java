package org.belex.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.belex.entry.Entry;
import org.belex.product.Product;

public class Util {

	public static String formatForDB(String originalString) {
		return originalString.replace("'", "''").replace("&", " ").replace("%", " ");
	}

	public static String formatForDB(String originalString, int maxLenght) {
		
		String strTmp = formatForDB(originalString);
		
		if (strTmp.length() > maxLenght) {
			// do not double the sigle cote but replace it by space to keep max size 
			strTmp = originalString.replace("'", " ").replace("&", " ").replace("%", " ");
		}

		// be safe and limit to the max size 
		if (strTmp.length() > maxLenght) {
			strTmp = strTmp.substring(0, maxLenght - 1);
		}

		return strTmp;
	}

	public static boolean contains(StringTokenizer inItems, String item) {
		boolean result = false;
		while (inItems.hasMoreTokens()) {
			if (inItems.nextToken().equals(item)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public static boolean containsExactEntry(List<Entry> entries, Entry entry) {
		boolean result = false;
		try {
			UtilityBean utb = new UtilityBean();
			
			int numberOfProduct = entry.getNumberOfProduct();
			Product.Unit unit = utb.getSelectedUnit(entry.getProduct());
			int numberOfUnit = unit.getNumber();
			int total = numberOfUnit * numberOfProduct;
			
			int tempTotal = 0;
	
			for (int i = 0 ; i < entries.size() ; i++ ){
				Entry tempEntry = entries.get(i);
	
				if (tempEntry.getProduct().getProductCode().equals(entry.getProduct().getProductCode())) {
					
					Product.Unit tempUnit = utb.getSelectedUnit(tempEntry.getProduct());

					if (tempUnit.getConditionnement().equals(unit.getConditionnement())) {
						int tempNumberOfProduct = tempEntry.getNumberOfProduct();
						int tempNumberOfUnit = tempUnit.getNumber();
						tempTotal += (tempNumberOfProduct * tempNumberOfUnit);
					}
				}
			}
			if (total == tempTotal) {
				result = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}


	/**
	 * @param entries
	 * @param entry
	 * @return the number of conditionnement stored for the similar entry in Vector of entries. 
	 */
	public static int containsSameUnit(List<Entry> entries, Entry entry) {
		int result = 0;
		try {
			UtilityBean utb = new UtilityBean();

			Product.Unit unit = utb.getSelectedUnit(entry.getProduct());
			
			for (int i = 0 ; i < entries.size() ; i++ ){
				Entry tempEntry = entries.get(i);
	
				if (tempEntry.getProduct().getProductCode().equals(entry.getProduct().getProductCode())) {
					
					Product.Unit tempUnit = utb.getSelectedUnit(tempEntry.getProduct());

					if (tempUnit.getConditionnement().equals(unit.getConditionnement())) {
						int tempNumberOfProduct = tempEntry.getNumberOfProduct();
						int tempNumberOfUnit = tempUnit.getNumber();
						result += (tempNumberOfProduct * tempNumberOfUnit);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = 0;
		}

		return result;
	}

	public static String displayContitionnement(int numberOfUnit, String unitConditionnement) {
		return displayContitionnement(numberOfUnit, unitConditionnement, false);
	}

	public static String displayContitionnement(int numberOfUnit, String unitConditionnement, boolean addBraquets) {
		String result = "";
		if (numberOfUnit == 1) {
			result = unitConditionnement;
		}
		if (result.equals("")) {
			result = numberOfUnit + " X " + unitConditionnement;
		}
		if (addBraquets && numberOfUnit > 1) {
			result = "(" + result + ")";
		}
		return result;
	}

	public static String getNowFormated(String outPattern){
		return formatDate(null, null, outPattern);
	}

	public static String formatDate(String originDate, String inPattern, String outPattern){
		String result = "";
		if (outPattern != null) {
			try {
	
				SimpleDateFormat outFormat = new SimpleDateFormat(outPattern);
	
				if (originDate == null || originDate.trim().equals("")) {
	
					result = outFormat.format(Calendar.getInstance(new Locale("fr", "BE")).getTime());
	
				} else {
	
					SimpleDateFormat inFormat = new SimpleDateFormat(inPattern);
	
					Calendar cal = Calendar.getInstance();
			        cal.setTime(inFormat.parse(originDate));
			        result = outFormat.format(cal.getTime());
				}
	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;

	}
	public static String computeDate(int typeOfInstance, String originDate, int step, String datePattern) { 

        String retValue = null; 
        
        try { 
                SimpleDateFormat format = new SimpleDateFormat(datePattern); 
                switch (typeOfInstance) {
				case 1:
					typeOfInstance = Calendar.DAY_OF_MONTH;
					break;
				case 2:
					typeOfInstance = Calendar.MONTH;
					break;
				case 3:
					typeOfInstance = Calendar.YEAR;
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(format.parse(originDate));
                cal.add(typeOfInstance, step);
                retValue = format.format(cal.getTime());
        
        } catch ( Exception e ) { 
                retValue = null; 
        } 
        
        return retValue; 
} 
	
/**
 * <p>
 * This method will check if the content of small is contained in the big. 
 * The returned string contains all elements from the small NOT contained in big.
 * Note : we consider big, small and result as StringTokenizer delim by the delimiter param.
 * </p>
 * @param big
 * @param small
 * @param delimiter
 * @return
 */
public static String containsAll(String big, String small, String delimiter) {
	String result = "";
	
	StringTokenizer smallToken = new StringTokenizer(small, delimiter);

	while (smallToken.hasMoreTokens()) {
		String currentSmall = smallToken.nextToken();
		boolean found = false;
		StringTokenizer bigToken = new StringTokenizer(big, delimiter);

		while (bigToken.hasMoreTokens() && !found) {
			if (currentSmall.equals(bigToken.nextToken())) {
				found = true;
			}
		}

		if (!found) {
			result += currentSmall + delimiter;
		}
	}
	if (result.length() > 0) {
		// remove the last delimiter
		result = result.substring(0,result.length() - 1);
	}
	return result;
}

public static String getShortDisplayable(String longString, int maxLength) {
	String result = longString;
	if (longString.length() > maxLength) {
		result = longString.substring(0,maxLength - 4) + "...";
	}
	return result;
}

public static String getShortDisplayable(String longString) {
	return getShortDisplayable(longString, 30);
}

}
