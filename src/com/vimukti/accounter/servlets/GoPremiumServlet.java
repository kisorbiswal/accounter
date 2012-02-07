package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.EU;

public class GoPremiumServlet extends BaseServlet {

	/**
	 * 
	 */
	public static String view = "/WEB-INF/gopremium.jsp";
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String emailId = (String) req.getSession().getAttribute(EMAIL_ID);
		if (emailId == null) {
			String parameter = req.getParameter("email_enc");
			emailId = EU.decryptAccounter(parameter);
			req.getSession(true).setAttribute(EMAIL_ID, emailId);
		}
		if (emailId != null) {
			dispatch(req, resp, view);
		} else {
			req.setAttribute(PARAM_DESTINATION, "/site/gopremium");
			this.getServletContext().getRequestDispatcher("/main/login")
					.forward(req, resp);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
}
