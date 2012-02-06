package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GoPremiumServlet extends BaseServlet {

	/**
	 * 
	 */
	private String view = "/WEB-INF/subscription.jsp";
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userid = (String) req.getSession().getAttribute(EMAIL_ID);
		if (userid != null) {
			dispatch(req, resp, view);
		} else {
			req.setAttribute(PARAM_DESTINATION, "/site/gopremium");
			this.getServletContext()
			.getRequestDispatcher("/main/login")
			.forward(req, resp);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
}
