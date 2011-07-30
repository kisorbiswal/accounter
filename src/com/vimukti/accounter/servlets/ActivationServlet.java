package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import com.vimukti.accounter.core.Activation;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;

public class ActivationServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/resetpassword.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// get the token
		String token = req.getParameter("code");
		// get activation record

		Session hibernateSession = HibernateUtil.openSession(LOCAL_DATABASE);
		try {
			Activation activation = getActivation(token);
			// If it is null
			if (activation == null) {
				// set Error "Token has expired"
				req.setAttribute("message", "Token has expired.");
			} else {
				// otherwise
				// set token in session.
				HttpSession session = req.getSession(true);
				session.setAttribute("activationToken", token);
				req.setAttribute("emailId", activation.getEmailId());
			}
			// redirect To ActivationPage.
			dispatch(req, resp, VIEW);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (hibernateSession != null) {
				hibernateSession.close();
			}
		}

	}

	/**
	 * Comes here after user posts the form where he posts the new password
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session == null) {
			dispatchMessage("Token has expired.", req, resp, VIEW);
			return;
		}

		// get token from session
		String token = (String) session.getAttribute("activationToken");
		if (token.isEmpty()) {
			dispatchMessage("Token has expired.", req, resp, VIEW);
			return;
		}
		// get activation record from table
		Session hibernateSession = HibernateUtil.openSession(LOCAL_DATABASE);
		try {
			Activation activation = getActivation(token);
			// if it is null
			if (activation == null) {
				// dispatch withr "Token has expired".
				dispatchMessage("Token has expired.", req, resp, VIEW);
				return;
			}
			// otherwise
			// getPasswords from request
			String password = req.getParameter("password");
			String confirm = req.getParameter("confirm");

			// compare if not equal send error message
			// otherwise
			if (!password.equals(confirm)) {
				dispatchMessage(
						"Password mismatch. Please reenter the password", req,
						resp, VIEW);
				return;
			}

			// getClient record with activation emailId
			Client client = getClient(activation.getEmailId());

			// update password and set isActive true
			client.setPassword(HexUtil.bytesToHex(Security.makeHash(activation
					.getEmailId() + password.trim())));
			client.setActive(true);
			// and save Client, delete activation record
			saveEntry(client);

			deleteActivationTokens(activation.getEmailId());

			// Send to login page with emailId
			session.setAttribute("emailId", activation.getEmailId());
			dispatch(req, resp, "/site/companysetup");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (hibernateSession != null) {
				hibernateSession.close();
			}
		}
	}

	private void deleteActivationTokens(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		session.getNamedQuery("delete.activation.by.emailId")
				.setString("emailId", emailId).executeUpdate();
	}

	private Activation getActivation(String token) {
		Session session = HibernateUtil.getCurrentSession();
		if (session != null) {
			Activation val = (Activation) session
					.getNamedQuery("get.activation.by.token")
					.setString("token", token).uniqueResult();
			return val;

		}
		return null;
	}
}
