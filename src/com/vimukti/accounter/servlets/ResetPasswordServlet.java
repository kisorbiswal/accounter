package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.ResetPasswordToken;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HibernateUtil;

public class ResetPasswordServlet extends BaseServlet {

	private static final long serialVersionUID = 1L;

	String view = "/WEB-INF/resetpassword.jsp";
	String tokenIdCompany;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		checkSession(req, resp);

		tokenIdCompany = req.getParameter("token");

		if (tokenIdCompany != null) {
			String tokenId = tokenIdCompany.substring(0, 40);
			String company = tokenIdCompany.substring(40);
			String errorMessage = null;
			Session session = HibernateUtil.openSession(company);
			if (session == null) {
				errorMessage = "Reset Password request was expired";
			} else {
				ResetPasswordToken token = (ResetPasswordToken) session
						.getNamedQuery("gettoken.by.id")
						.setParameter(0, tokenId).uniqueResult();
				if (token == null) {
					errorMessage = "Reset Password request was expired";
				}
			}
			session.close();
			req.setAttribute("errorMessage", errorMessage);
		}
		dispatch(req, resp, view);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Session session = null;
		try {
			checkSession(req, resp);

			String tokenId = tokenIdCompany.substring(0, 40);
			String company = tokenIdCompany.substring(40);
			session = HibernateUtil.openSession(company);

			Transaction tx = session.beginTransaction();

			ResetPasswordToken token = (ResetPasswordToken) session
					.getNamedQuery("gettoken.by.id").setParameter(0, tokenId)
					.uniqueResult();
			User user = (User) session.getNamedQuery("getIDentity.from.id")
					.setParameter("id", token.getUserId()).uniqueResult();
			String password = req.getParameter("newPassword");
			user.setPasswordSha1Hash(password);

			session.saveOrUpdate(user);
			session.delete(token);

			try {
				tx.commit();
			} finally {
				tx.rollback();
			}

			redirectExternal(req, resp, LOGIN_URL);

		} catch (Exception e) {
			req.setAttribute("errorMessage",
					"Password reset failed please try again");
			dispatch(req, resp, view);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	/**
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void checkSession(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		HttpSession httpSession = req.getSession();
		if (httpSession == null) {
			String destination = req.getParameter(DESTINATION);
			if (destination == null) {
				redirectExternal(req, resp, LOGIN_URL);
			} else {
				redirectExternal(req, resp, destination);
			}
		}
	}

}
