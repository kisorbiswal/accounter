package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.User;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;

public class CompanyPasswordServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/companypassword.jsp";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String password = (String) req.getParameter("password");
		Long companyId = (Long) req.getSession().getAttribute(COMPANY_ID);
		String emailId = (String) req.getSession().getAttribute(EMAIL_ID);
		if (password != null && companyId != null) {
			byte[] comSecret = getCompanySecretFromDB(companyId);
			try {
				User user = getUser(emailId, companyId);
				Session session = HibernateUtil.getCurrentSession();
				Transaction beginTransaction = session.beginTransaction();
				session.saveOrUpdate(user);
				beginTransaction.commit();
				resp.sendRedirect(ServerConfiguration.isDebugMode ? ACCOUNTER_URL
						: ACCOUNTER_OLD_URL);
				return;
			} catch (Exception e) {
				e.printStackTrace();
				req.setAttribute("error", "Wrong password.");
				dispatch(req, resp, VIEW);
			}
		} else {
			dispatch(req, resp, VIEW);
		}
	}
}
