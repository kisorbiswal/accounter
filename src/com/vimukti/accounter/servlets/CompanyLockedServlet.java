package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CompanyLockedServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/companylocked.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session == null) {
			resp.sendRedirect(BaseServlet.LOGIN_URL);
			return;
		}
		Integer reasonType = (Integer) session
				.getAttribute(BaseServlet.LOCAK_REASON_TYPE);
		if (reasonType == null) {
			reasonType = 0;
		}
		try {
			req.setAttribute(BaseServlet.LOCAK_REASON_TYPE, reasonType);
			RequestDispatcher reqDispatcher = getServletContext()
					.getRequestDispatcher(VIEW);
			reqDispatcher.forward(req, resp);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}
}
