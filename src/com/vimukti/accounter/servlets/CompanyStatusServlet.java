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
	private String view = "/WEB-INF/refresh.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();

		String status = (String) session.getAttribute(COMPANY_CREATION_STATUS);
		if (status != null) {
			if (status.equals("Creating")) {
				req.setAttribute("successmessage", CREATING);
			} else if (status.equals("Success")) {
				redirectExternal(req, resp, COMPANIES_URL);
				return;
			} else if (status.equals("Fail")) {
				dispatch(req, resp, "/WEB-INF/CreateCompany.jsp");
				return;
			}

		}else{
			req.setAttribute("successmessage", "Initializing company creation process....");
		}
		req.getRequestDispatcher(view).forward(req, resp);
	}
}
