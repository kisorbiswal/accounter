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
	private static final String SUCCESS = "Your company is created successfully";
	private static final String CREATING = "Creating the company...";
	private static final String FAIL = "Company creation failed";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session == null) {
			req.setAttribute("successmessage", FAIL);
		} else {
			String status = (String) session.getAttribute("COM_STATUS");
			if (status == null) {
				req.setAttribute("successmessage", FAIL);
			} else {
				if (status.equals("Creating")) {
					req.setAttribute("successmessage", CREATING);
				} else if (status.equals("Success")) {
					req.setAttribute("successmessage", SUCCESS);
				} else {
					req.setAttribute("successmessage", FAIL);
				}
			}
		}
		req.getRequestDispatcher("/WEB-INF/refresh.jsp").forward(req, resp);
	}
}
