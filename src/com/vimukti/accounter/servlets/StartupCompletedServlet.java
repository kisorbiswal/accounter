package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.vimukti.accounter.core.Property;
import com.vimukti.accounter.setup.server.DatabaseManager;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * It will be used for only Desktop Application
 * 
 * @author Prasanna Kumar G
 * 
 */
public class StartupCompletedServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String SETUP_URL = "/desk/setup";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String redirectURL = null;
		if (DatabaseManager.isDBConfigured()) {
			Session session = HibernateUtil.openSession();
			Property prop = (Property) session.get(Property.class,
					Property.SETUP_PAGE);
			if (prop == null) {
				redirectURL = SETUP_URL;
			} else {
				String value = prop.getValue();
				if (value.equals("3")) {
					redirectURL = "/login";
				} else {
					redirectURL = SETUP_URL + "?page=" + value;
				}
			}

		} else {
			redirectURL = SETUP_URL;
		}
		resp.sendRedirect(redirectURL);
	}
}
