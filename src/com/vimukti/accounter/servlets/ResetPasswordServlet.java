package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.vimukti.accounter.core.ResetPasswordToken;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HibernateUtil;

public class ResetPasswordServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String view = "/sites/resetpassword.jsp";
	String tokenIdCompany;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
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
			disptchView(req, resp, view);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			String successMessage;
			String tokenId = tokenIdCompany.substring(0, 40);
			String company = tokenIdCompany.substring(40);
			Session session = HibernateUtil.openSession(company);

			org.hibernate.Transaction tx = session.beginTransaction();
			ResetPasswordToken token = (ResetPasswordToken) session
					.getNamedQuery("gettoken.by.id").setParameter(0, tokenId)
					.uniqueResult();
			User user = (User) session
					.getNamedQuery("getIDentity.from.id")
					.setParameter("id", token.getUserId()).uniqueResult();
			String password = req.getParameter("newPassword");
			user.setPasswordSha1Hash(password);
			session.saveOrUpdate(user);
			successMessage = "Your Password successfully changed";
			req.setAttribute("successMessage", successMessage);
			session.delete(token);
			tx.commit();
			session.close();

			disptchView(req, resp, view);
		} catch (Exception e) {
			req.setAttribute("errorMessage",
					"Password reset failed please try again");
			disptchView(req, resp, view);
		}
	}

	void disptchView(HttpServletRequest req, HttpServletResponse resp,
			String view) {
		try {
			RequestDispatcher dispatcher = getServletContext()
					.getRequestDispatcher(view);

			dispatcher.forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
