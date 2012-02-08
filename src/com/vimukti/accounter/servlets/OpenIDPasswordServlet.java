package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class OpenIDPasswordServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8974718380835687273L;
	private static final String VIEW = "/WEB-INF/openidpassword.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session != null && session.getAttribute(EMAIL_ID) != null) {
			dispatch(req, resp, VIEW);
		} else {
			resp.sendRedirect(LOGIN_URL);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

	}

}
