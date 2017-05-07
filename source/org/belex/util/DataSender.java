package org.belex.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

public class DataSender {

	private static final Category log = Logger.getLogger(FileSender.class);
	
	public DataSender() {
	}
	public boolean sendProductUpdate(String data) {
		boolean result = false;
		try {
			String localhostname = java.net.InetAddress.getLocalHost().getHostName();
			URL url = null;
			if ( ! (StringUtils.containsIgnoreCase(localhostname, "CARGO") || Constants.SEND_TO_SITE_FROM_LOCAL )) {
				url = new URL(Constants.URL_UPDATE_PRODUCT_LOCAL + "?" + data + "&validation=" + Constants.ACCESS_KEY_LOCAL);
			} else {
				url = new URL(Constants.URL_UPDATE_PRODUCT + "?" + data + "&validation=" + Constants.ACCESS_KEY);
			}
			result = send(url, data);
		} catch (Exception e) {
			log.error("DataSender data:" + data, e);
		}
    	return result;
		
	}
	public boolean sendCustomerUpdate(String data) {
		boolean result = false;
		try {
			String localhostname = java.net.InetAddress.getLocalHost().getHostName();
			URL url = null;
			if ( ! (StringUtils.containsIgnoreCase(localhostname, "CARGO") || Constants.SEND_TO_SITE_FROM_LOCAL )) {
				url = new URL(Constants.URL_UPDATE_USER_LOCAL + "?" + data + "&validation=" + Constants.ACCESS_KEY_LOCAL);
			} else {
				url = new URL(Constants.URL_UPDATE_USER + "?" + data + "&validation=" + Constants.ACCESS_KEY);
			}
			result = send(url, data);
		} catch (Exception e) {
			log.error("DataSender data:" + data, e);
		}
    	return result;
	}
	private boolean send(URL url, String data) {
	    boolean result = false;
	
	    try {
	       
	        if (StringUtils.isNotBlank(data)) {
	        	log.info(url);

	        	URLConnection urlConnection = url.openConnection();
	        	urlConnection.connect();
	        	InputStream in = urlConnection.getInputStream();

	        	log.info(DataSender.convertStreamToString(in));

	        	in.close();
	        	urlConnection = null;
	        	result = true;
	        }
	    } catch (IOException e) {
	    	result = false;
	    	log.error("DataSender data:" + data, e);
	    }
	    return result;
	}
	
	public static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	
	public static void getIp() {
		try {
			Enumeration e=NetworkInterface.getNetworkInterfaces();
		    while(e.hasMoreElements())
		    {
		        NetworkInterface n=(NetworkInterface) e.nextElement();
		        Enumeration ee = n.getInetAddresses();
		        while(ee.hasMoreElements())
		        {
		            InetAddress i= (InetAddress) ee.nextElement();
		            System.out.println(i.getHostAddress());
		        }
		    }
		} catch (Exception e) {
			log.error("getting ip", e);
		}
	}
}
