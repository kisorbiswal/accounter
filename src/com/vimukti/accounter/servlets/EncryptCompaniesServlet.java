package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.EU;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.encryption.Encrypter;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;

public class EncryptCompaniesServlet extends BaseServlet {

	private static final String ENCRYPT_VIEW = "/WEB-INF/encryptcompany.jsp";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!ServerConfiguration.isEnableEncryption()) {
			resp.sendRedirect(COMPANIES_URL);
			return;
		}
		HttpSession session = req.getSession();
		if (session != null && session.getAttribute(EMAIL_ID) != null) {
			String emailId = (String) session.getAttribute(EMAIL_ID);
			Client client = getClient(emailId);
			if (client == null) {
				resp.sendRedirect(LOGIN_URL);
				return;
			}
			Session currentSession = HibernateUtil.getCurrentSession();
			List<Long> userIds = new ArrayList<Long>();
			for (User user : client.getUsers()) {
				if (!user.isDeleted()) {
					userIds.add(user.getID());
				}
			}
			if (!userIds.isEmpty()) {
				List<Object[]> list = currentSession
						.getNamedQuery(
								"get.NonEncrypted.CompanyNames.by.client")
						.setParameterList("userIds", userIds).list();
				req.setAttribute("companeyList", list);
			}
			dispatch(req, resp, ENCRYPT_VIEW);
		} else {
			resp.sendRedirect(LOGIN_URL);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!ServerConfiguration.isEnableEncryption()) {
			resp.sendRedirect(COMPANIES_URL);
			return;
		}

		HttpSession session = req.getSession();
		if (session != null && session.getAttribute(EMAIL_ID) != null) {
			String emailId = (String) session.getAttribute(EMAIL_ID);
			String companyId = req.getParameter("companyname");
			Session session2 = HibernateUtil.getCurrentSession();
			EU.removeCipher();
			Company company = (Company) session2.get(Company.class,
					Long.parseLong(companyId));
			if (company != null) {
				User user = company.getUserByUserEmail(emailId);
				if (user == null) {
					dispatch(req, resp, ENCRYPT_VIEW);
					return;
				}
				String password = req.getParameter("password");
				company.setLocked(true);
				Session currentSession = HibernateUtil.getCurrentSession();
				Transaction beginTransaction = currentSession
						.beginTransaction();
				currentSession.saveOrUpdate(company);

				try {
					new Encrypter(company.getId(), password, getD2(req),
							emailId, session.getId()).start();
				} catch (Exception e) {
					company.setLocked(false);
				}
				beginTransaction.commit();
			} else {
				dispatch(req, resp, ENCRYPT_VIEW);
				return;
			}
		}
		resp.sendRedirect(LOGIN_URL);
	}

	private Company getCompany(String companyName) {
		Session session = HibernateUtil.getCurrentSession();
		Object uniqueResult = session
				.getNamedQuery("get.company.by.tradingname")
				.setParameter("tradingName", companyName).uniqueResult();
		return (Company) uniqueResult;
	}
}
