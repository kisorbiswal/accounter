package com.vimukti.accounter.admin.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.servlets.BaseServlet;

public class AdminLoginServlet extends BaseServlet {
	public static final String ADMIN_HOME = "adminhome";
	public static final String ADMIN_LOGIN_VIEW = "/WEB-INF/AdminLogin.jsp";
	/**
	 * 
	 */
	private static final long serialVersionUID = 5166788117640750514L;

	/**
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		dispatch(req, resp, ADMIN_LOGIN_VIEW);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String email = req.getParameter("emailId");
		String passwd = req.getParameter("password");
		if (email.equals(ServerConfiguration.getAdminID())
				&& passwd.equals(ServerConfiguration.getAdminPassword())) {
			redirectExternal(req, resp, ADMIN_HOME);
		} else {
			req.setAttribute("errorMsg", "error");
			dispatch(req, resp, ADMIN_LOGIN_VIEW);
		}

	}
}
