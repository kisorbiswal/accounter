package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XmlReportsApiServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String methodName = getMethodName(req);
		Date startDate = getDate(req.getParameter("StartDate"));
		Date endDate = getDate(req.getParameter("EndDate"));
		
	}

	private Date getDate(String startDateStr) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getMethodName(HttpServletRequest req) {
		StringBuffer requestURL = req.getRequestURL();
		String url = requestURL.toString();
		String[] urlParts = url.split("/");
		String last = urlParts[urlParts.length - 1];
		return last.split("?")[0];
	}
}
