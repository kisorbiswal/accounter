package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.vimukti.accounter.core.ResetPasswordToken;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;

@SuppressWarnings("serial")
public class ForgetPasswordServlet extends BaseServlet {

	private String view = "/sites/forgotpassword.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dispatchServlet(req, resp, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @param getting from httpservlet request object are emailId and company
	 * name form url.
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String errorMessage;
		String successMessage;
		String emailId = req.getParameter("emailId");
		String companyName = req.getParameter("companyName");
		req.setAttribute("emailId", emailId);
		req.setAttribute("companyName", companyName);
		if (companyName == null || !isCompanyExits(companyName)) {
			errorMessage = "We couldn't find any company with this name. please enter a valid company name.";
			req.setAttribute("errorMessage", errorMessage);
			dispatchServlet(req, resp, view);
			return;
		} else {
			Session session = HibernateUtil.openSession(companyName);
			org.hibernate.Transaction tx = session.beginTransaction();
			try {
				User user = (User) session.getNamedQuery("unique.emailId.User")
						.setParameter(0, emailId).uniqueResult();

				if (user == null) {
					errorMessage = "we couldn't find any user with the given Email ID. please enter a valid email";
					req.setAttribute("errorMessage", errorMessage);
				} else {
					sendLinkToUser(session, user, companyName);
					successMessage = "Reset Password link has been sent to the given emailId, Kindly check your Mail box.";
					req.setAttribute("successMessage", successMessage);
				}
				dispatchServlet(req, resp, view);
			} finally {
				tx.commit();
				session.close();
			}
		}
	}

	/*
	 * Reset the password and send mail to user
	 */
	@SuppressWarnings("unchecked")
	private void sendLinkToUser(Session session, User user, String companyName) {

		List<ResetPasswordToken> existtokens = session
				.getNamedQuery("gettokens.by.userid")
				.setParameter(0, user.getID()).list();
		for (ResetPasswordToken t : existtokens) {
			session.delete(t);
		}

		String randomAttr = SecureUtils.createID();
		ResetPasswordToken token = new ResetPasswordToken(randomAttr,
				user.getID());
		session.save(token);

		String link = "https://www.accounterlive.com/site/resetpassword?token="
				+ randomAttr + companyName;

		UsersMailSendar.sendResetPasswordLinkToUser(link, user.getEmail());

		// ResetIDentityPasswordEvent event = new
		// ResetIDentityPasswordEvent(user
		// .getUserid(), companyName, HexUtil.getRandomString());
		// Server.getInstance().process(user.getUserid(), event);
	}

	/*
	 * Dispatch the servlet to view
	 */

	private void dispatchServlet(HttpServletRequest request,
			HttpServletResponse response, String view) {
		try {
			RequestDispatcher dispatcher = getServletContext()
					.getRequestDispatcher(view);
			dispatcher.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
