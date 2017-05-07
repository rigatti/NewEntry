package org.belex.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.belex.product.Product;
import org.belex.product.Product.Unit;
import org.belex.supplier.Supplier;

public class UtilityBean {
	private HttpServletRequest request;
	public void initialise(HttpServletRequest request){
		this.request = request;
	}
	public String getStringValue(String varName) {
		String result = "";

		try 
		{
			Object obj = null;
			StringTokenizer st = new StringTokenizer(varName, ".");
			while (st.hasMoreElements()) {
				String element = (String) st.nextElement();
				if (obj == null) {
					obj = request.getAttribute(element);
				} else {
					Method method = obj.getClass().getMethod("get" + element.substring(0,1).toUpperCase() + element.substring(1), new Class[]{});
					obj = method.invoke(obj, new Object[]{});
				}
			}
			if (obj instanceof String) {
				result = (String) obj;
			}
		} 
		catch (SecurityException e) 
		{
		e.printStackTrace();
		} 
		catch (NoSuchMethodException e) 
		{
		e.printStackTrace();
		} 
		catch (IllegalArgumentException e) 
		{
		e.printStackTrace();
		} 
		catch (IllegalAccessException e) 
		{
		e.printStackTrace();
		} 
		catch (InvocationTargetException e) 
		{
		e.printStackTrace();
		}

		return result;
	}
	public Unit getSelectedUnit(Product product) {
		Unit unit = product. new Unit();
		List<Unit> units = product.getUnits();

		if (units.size() == 1) {

			unit = units.get(0);
			
		} else {

			for (int i = 0; i < units.size(); i++) {
				if (units.get(i).isSelected()) {
					unit = units.get(i);
					break;
				}
			}
		}
		return unit;
	}

	public String getSupplierName(List<Supplier> suppliers, String supplierCode){
		String result = "";
		for (Supplier s : suppliers) {
			if (s.getSupplierCode().equals(supplierCode)) {
				result = s.getSupplierName();
			}
		}
		return result;
	}
}
