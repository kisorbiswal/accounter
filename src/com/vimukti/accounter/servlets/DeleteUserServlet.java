package com.vimukti.accounter.servlets;

import java.io.IOException;

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
import com.vimukti.accounter.utils.HibernateUtil;

public class DeleteUserServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Session openSession = HibernateUtil.openSession(Server.LOCAL_DATABASE);
		Transaction beginTransaction = openSession.beginTransaction();

		String senderEmail = req.getParameter("senderEmailId");
		if (senderEmail == null || senderEmail.isEmpty()) {
			req.setAttribute("message", "Sender email id is null");
			return;
		}

		String deletableUserEmail = req.getParameter("deletableUserEmail");
		if (deletableUserEmail == null || deletableUserEmail.isEmpty()) {
			req.setAttribute("message", "Deletable client email id is null");
			return;
		}
		Client deletableClient = getClient(deletableUserEmail);

		String serverCompanyId = req.getParameter("serverCompanyId");
		ServerCompany serverCompany = null;
		try {
			serverCompany = (ServerCompany) openSession.load(
					ServerCompany.class, Long.valueOf(serverCompanyId));
			serverCompany.getClients().remove(deletableClient);
			openSession.saveOrUpdate(serverCompany);
			deletableClient.getCompanies().remove(serverCompany);
			openSession.saveOrUpdate(deletableClient);
			UsersMailSendar.sendDeletedInviteUserMail(senderEmail,
					deletableClient, serverCompany.getCompanyName());
			beginTransaction.commit();
		} catch (HibernateException e) {
			beginTransaction.rollback();
			req.setAttribute("message", "Save or update failed");
			return;
		} catch (Exception e) {
			beginTransaction.rollback();
			req.setAttribute("message", "Save or update failed");
			return;
		} finally {
			if (openSession != null && openSession.isOpen()) {
				openSession.close();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
