package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;

public class MaintananceServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1163973484857078003L;
	private static final String MAINTANANCE_VIEW = "/WEB-INF/serverMaintain.jsp";
	private static final String UNDER_CONSTRUCTION_VIEW = "/WEB-INF/maintananceInfo.jsp";
	private static final String LOGIN_VIEW = "/WEB-INF/login.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dispatch(req, resp, MAINTANANCE_VIEW);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Session openSession = HibernateUtil.openSession(LOCAL_DATABASE);
		String paswdString = req.getParameter("adminPasswordBox");
		if (paswdString != null
				&& paswdString == ServerConfiguration.getAdminPassword()) {
			if (req.getParameter("option1") != null
					&& req.getParameter("option1").equals("on")) {
				ServerConfiguration.setUnderMaintainance(true);

				// TODO updateTable();
			}
		}

		if (ServerConfiguration.isUnderMaintainance()) {
			dispatch(req, resp, UNDER_CONSTRUCTION_VIEW);
		} else {
			dispatch(req, resp, LOGIN_VIEW);
		}

	}

}
