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

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dispatch(req, resp, MAINTANANCE_VIEW);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Session openSession = HibernateUtil.openSession(LOCAL_DATABASE);
		ServerConfiguration.setUnderMaintainance(true);
		// TODO updateTable(maintanance table query);

		if (ServerConfiguration.isUnderMaintainance()) {
			dispatch(req, resp, UNDER_CONSTRUCTION_VIEW);
		}

	}

}
