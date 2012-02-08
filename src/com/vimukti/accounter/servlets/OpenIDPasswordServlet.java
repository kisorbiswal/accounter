package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.Security;

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
		HttpSession session = req.getSession();
		if (session != null && session.getAttribute(EMAIL_ID) != null) {
			String emailId = (String) session.getAttribute(EMAIL_ID);
			String password = req.getParameter("password");
			Client client = getClient(emailId);
			if (client != null) {
				password = HexUtil.bytesToHex(Security.makeHash(emailId
						+ password.trim()));
				if (!client.getPassword().equals(password)) {
					req.setAttribute("error",
							"Wrong password. Please enter you accounter passowrd");
					dispatch(req, resp, VIEW);
					return;
				}
				try {
					NewLoginServlet.createD2(req, emailId, password);
				} catch (Exception e) {
					e.printStackTrace();
				}
				redirectExternal(req, resp, LOGIN_URL);
			}
		}
		resp.sendRedirect(LOGIN_URL);
	}

}
