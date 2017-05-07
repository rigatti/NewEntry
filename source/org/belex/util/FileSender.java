package org.belex.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

public class FileSender {
	
	private static final Category log = Logger.getLogger(FileSender.class);
	public static void main(String[] args) {
		new FileSender().uploadImage("c:\\\\wamp\\www\\p\\img\\BIGARA.jpg");
	}
	public FileSender() {
	}
	public String uploadImage(String reference) {
		String path = null;
    	String targetPath = null;
    	String httpPath = null;
    	String result = null;
    	FTPClient client = null;
    	FileInputStream fis = null;
    	
		try {
			String localhostname = java.net.InetAddress.getLocalHost().getHostName();
			boolean isLocal = false;
	    	if ( ! StringUtils.containsIgnoreCase(localhostname, "CARGO") ) {
	    		isLocal = true;
	    	}
	    	
	    	if (isLocal) {
	    		path = Constants.IMG_PATH_LOCAL;
	    		targetPath = Constants.IMG_TARGET_PATH_LOCAL;
	    		if ( Constants.SEND_TO_SITE_FROM_LOCAL ) {
	    			httpPath = Constants.IMG_HTTP_PATH;
	    		} else {
	    			httpPath = Constants.IMG_HTTP_PATH_LOCAL;
	    		}
	    	} else {
	    		path = Constants.IMG_PATH;
	    		targetPath = Constants.IMG_TARGET_PATH;
	    		httpPath = Constants.IMG_HTTP_PATH;
	    	}

	    	String filename = reference;
            File f = new File(path + reference + ".jpg");
            result = filename + ".jpg";
            if ( ! f.exists()) {
            	f = new File(path + reference + ".png");
                result = filename + ".png";
            }
            if ( ! f.exists()) {
            	f = new File(path + reference + ".bmp");
                result = filename + ".bmp";
            }
            if ( ! f.exists()) {
            	f = new File(path + reference + ".gif");
                result = filename + ".gif";
            }
            
            if (f.exists()) {
            	filename = result;
            	// files are all already on the ftp server in local, no need to send it again
            	// Due to corrupted img transfer, do it manually to the ftp server belexcargo.com
            	if (false) {
            	//if ( ! isLocal ) {
	            	client = new FTPClient();
	                client.connect(Constants.FTPHOST);
	                client.login(Constants.FTPUSERNAME, Constants.FTPPWD);
	     
	                fis = new FileInputStream(path + filename);
	                client.storeFile(targetPath + filename, fis);
	
		            if (client.getReplyCode() != 226 && client.getReplyCode() != 200) {
		            	log.error("FileSender, path: " + path + " and reference: " + reference +" and resp: " + client.getReplyString());
		            	result = null;
		            } else {
		            	log.info("FileSender: " + client.getReplyString());
		            	result = httpPath + filename;
		            }
		            client.logout();
            	} else {
            		// local : file should already be in place 
            		result = httpPath + filename;
            	}
            } else {
                result = null;
            }
		} catch (UnknownHostException e1) {
			log.error("FileSender catch, path:" + path + " and reference:" + reference ,e1);
	    	result = null;
	    } catch (IOException e) {
	    	log.error("FileSender catch, path:" + path + " and reference:" + reference ,e);
	    	result = null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (client != null) {
                	client.disconnect();
                }
            } catch (IOException e) {
    	    	log.error("FileSender finally block, path:" + path + " and reference:" + reference ,e);
    	    	result = null;
            }
        }
        return result;
    }
}
