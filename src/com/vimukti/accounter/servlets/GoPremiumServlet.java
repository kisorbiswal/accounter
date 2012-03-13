package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.main.ServerConfiguration;

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
			emailId = req.getParameter("emailId");
			if (getClient(emailId) == null) {
				emailId = null;
			}
		}

		if (emailId != null) {
			boolean sandBoxPaypal = ServerConfiguration.isSandBoxPaypal();
			req.setAttribute("isSandBoxPaypal", sandBoxPaypal);
			req.setAttribute(EMAIL_ID, emailId);
			dispatch(req, resp, view);
		} else {
			req.setAttribute("paypalButtonId", ServerConfiguration.getPaypalButtonId());
			req.setAttribute(PARAM_DESTINATION, "/site/gopremium");
			resp.sendRedirect(LOGIN_URL + "?destination=/site/gopremium");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
}
