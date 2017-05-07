package org.belex.application;

import java.io.File;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class InitializerServlet extends HttpServlet {

public void init() throws ServletException {
	// clean jsptmp directory
	HashSet<String> paths = (HashSet<String>) getServletContext().getResourcePaths("/jsptmp/");
	if (paths != null) {
		for (String filePath : paths) {
			File f = new File(getServletContext().getRealPath(filePath));

			if (f != null && f.exists()) {
				f.delete();
			}
		}
	}
	super.init();
	
}
}
