package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SubscribtionComplitionServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5081127451630771127L;
	private String view = "/WEB-INF/subscribtioncomplition.jsp";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String emailId = (String) req.getSession().getAttribute(EMAIL_ID);
		if (emailId == null) {
			resp.sendRedirect(LOGIN_URL);
			return;
		}
		dispatch(req, resp, view);
	}
}
