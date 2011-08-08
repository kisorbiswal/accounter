package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CompanyStatusServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String CREATING = "Creating the company...";
	private String REFRESH_VIEW = "/WEB-INF/refresh.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session != null) {
			String status = (String) session
					.getAttribute(COMPANY_CREATION_STATUS);
			if (status != null) {
				if (status.equals("Creating")) {
					req.setAttribute("successmessage", CREATING);
					session.removeAttribute(COMPANY_CREATION_STATUS);
					req.getRequestDispatcher(REFRESH_VIEW).forward(req, resp);
					return;
				}
			}
		}
		redirectExternal(req, resp, COMPANIES_URL);
	}
}
