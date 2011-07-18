package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.bizantra.server.events.CometServerManager;
import com.bizantra.server.main.Server;
import com.bizantra.server.storage.HibernateUtil;
import com.vimukti.accounter.web.client.data.ClientCometData;

/**
 * 
 * @author P.Praneeth
 * 
 *         This servlet is used to delete attachments from space(Rackspace or
 *         localspace) and then drops DB.
 * 
 */

public class DeleteCompanyServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String DELETED_SUCCESSFULLY = "101";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String domainName = req.getParameter("domainName");
		if (domainName == null) {
			return;
		}
		deleteCompany(domainName);
		resp.getWriter().write(DELETED_SUCCESSFULLY);

	}

	// @Override
	// protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	// throws ServletException, IOException {
	// String domainName = req.getParameter("domainName");
	// if (domainName == null) {
	// return;
	// }
	// deleteCompany(domainName);
	// }

	private void deleteCompany(String domainName) {
		deleteAttachments(domainName);
		try {
			Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
			Transaction tx = session.beginTransaction();
			session.createSQLQuery("drop schema " + domainName).executeUpdate();
			session.createSQLQuery(
					"delete from BIZANTRA_COMPANY where NAME =:companyName")
					.setString("companyName", domainName).executeUpdate();
			tx.commit();
			session.close();

			// LiveServer.getInstance().removeCompany(domainName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void deleteAttachments(String domainName) {
		try {
			Session session = HibernateUtil.openSession(domainName);
			Transaction tx = session.beginTransaction();
			List<String> attachmentsList = session.getNamedQuery(
					"get.all.attachments").list();
			for (String attId : attachmentsList) {
				// session.delete(att);
				// Server.getInstance().deleteAttachment(attId);
			}
			ArrayList<Object> list = (ArrayList<Object>) session
					.getNamedQuery("get.identities.by.company")
					.setString("companyName", domainName).list();
			for (Object userId : list) {
				String collaberId = (String) userId;
				ClientCometData cometData = new ClientCometData(
						ClientCometData.IDENTITY,
						ClientCometData.DELETE_ACTION, null, null, collaberId);
				CometServerManager
						.putIdentityDataToQuees(collaberId, cometData);
				Server.getInstance().invalidateAllSessionIds(collaberId);
			}
			tx.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
