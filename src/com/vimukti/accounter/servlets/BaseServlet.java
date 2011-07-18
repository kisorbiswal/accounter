package com.vimukti.accounter.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.bizantra.server.internal.core.BizantraCompany;
import com.bizantra.server.main.Server;
import com.bizantra.server.storage.HibernateUtil;
import com.vimukti.accounter.workspace.tool.FinanceTool;

public class BaseServlet extends HttpServlet {
	public static final String USER_ID = "userID";
	public static final String OUR_COOKIE = "_accounter_01_infinity_2711";

	public static final String COMPANY_NAME = "company";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String getCompanyName(HttpServletRequest req) {
		return null;

	}

	protected boolean isCompanyExits(String companyName) {
		if (companyName == null) {
			return false;
		}
		Session openSession = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		try {
			BizantraCompany uniqueResult = (BizantraCompany) openSession
					.getNamedQuery("getCompanyName.is.unique")
					.setParameter("companyName", companyName).uniqueResult();
			return uniqueResult == null ? false : true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			openSession.close();
		}
	}

	protected FinanceTool getFinanceTool(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	protected String getCookie(HttpServletRequest request, String ourCookie) {
		// TODO Auto-generated method stub
		return null;
	}

}
