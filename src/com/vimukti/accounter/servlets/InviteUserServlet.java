package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ServerCompany;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.main.Server;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;

public class InviteUserServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String companyID = req.getParameter("serverCompanyId");
		Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		String senderEmailId = req.getParameter("senderEmailId");
		if (senderEmailId == null) {
			req.setAttribute("message",
					"Inivitation sender Email Id should n't be null");
			return;
		}

		Client inviter = getClient(senderEmailId);

		String invitedemailID = req.getParameter("invitedUserEmail");
		if (invitedemailID == null) {
			req.setAttribute("message", "Email Id can't be null");
			return;
		}
		String firstName = req.getParameter("invitedUserFirstName");
		if (firstName == null) {
			req.setAttribute("message", "First name can't be null");
			return;
		}

		String lastName = req.getParameter("invitedUserLastName");

		if (lastName == null) {
			req.setAttribute("message", "Last name can't be null");
			return;
		}
		ServerCompany serverCompany = null;
		try {
			serverCompany = (ServerCompany) session.load(ServerCompany.class,
					Long.valueOf(companyID));
		} catch (HibernateException e) {
			req.setAttribute("message", "Server Company null");
			return;
		}
		Transaction transaction = session.beginTransaction();
		Client invitedClient = getClient(invitedemailID);
		boolean isExistedUser = true;
		if (invitedClient == null) {
			isExistedUser = false;
			invitedClient = new Client();
			invitedClient.setActive(true);
			Set<ServerCompany> servercompanies = new HashSet<ServerCompany>();
			servercompanies.add(serverCompany);
			invitedClient.setCompanies(servercompanies);
			invitedClient.setCountry(inviter.getCountry());
			invitedClient.setEmailId(invitedemailID);
			invitedClient.setFirstName(firstName);
			invitedClient.setLastName(lastName);
			invitedClient.setPassword(HexUtil.bytesToHex(Security
					.makeHash(invitedemailID + "***REMOVED***")));
			// invitedClient.setRequirePasswordReset(true);
		} else {
			Set<ServerCompany> invitedClientCompanies = invitedClient
					.getCompanies();
			for (ServerCompany invitedClientCompany : invitedClientCompanies) {
				if (serverCompany == invitedClientCompany) {
					req.setAttribute("message",
							"Invited user already exists in your company.");
					return;
				}
			}
			invitedClient.getCompanies().add(serverCompany);
		}

		try {
			session.save(invitedClient);
			if (isExistedUser) {
				UsersMailSendar.sendMailToOtherCompanyUser(invitedClient,
						serverCompany.getCompanyName(), inviter);
			} else {
				sendMailToInvitedUser(invitedClient,
						"***REMOVED***",
						serverCompany.getCompanyName());
			}
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			req.setAttribute("message", "Invited Client save process failed");
			return;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		req.setAttribute("message", "");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
