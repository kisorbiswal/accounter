package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Server;
import com.vimukti.accounter.core.ServerCompany;
import com.vimukti.accounter.services.IS2SService;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * @author Prasanna Kumar G
 * 
 */

public class DeleteCompanyServlet extends BaseServlet {

	private static final String IS_DELETE_ALL_USERS = "deleteAllUsers";

	private String deleteCompanyView = "/WEB-INF/deleteCompany.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		String emailID = (String) session.getAttribute(EMAIL_ID);
		if (emailID == null) {
			redirectExternal(req, resp, LOGIN_URL);
			return;
		}

		req.getSession().setAttribute(COMPANY_ID, req.getParameter(COMPANY_ID));
		dispatch(req, resp, deleteCompanyView);
	}

	/**
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void deleteComapny(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		final String email = (String) req.getSession().getAttribute(EMAIL_ID);

		String message = "";

		final String companyID = (String) req.getSession().getAttribute(
				COMPANY_ID);
		final boolean deleteAllUsers = req.getParameter("delete").equals(
				"deleteAllUsers");
		final HttpSession httpSession = req.getSession();
		new Thread(new Runnable() {

			@Override
			public void run() {
				httpSession.setAttribute(COMPANY_DELETION_STATUS,
						COMPANY_DELETING);
				Session session = HibernateUtil.openSession();
				Transaction transaction = session.beginTransaction();
				try {

					Client client = getClient(email);
					ServerCompany serverCompany = null;
					if (companyID == null) {
						httpSession.setAttribute(COMPANY_DELETION_STATUS,
								"Fail");
						httpSession.setAttribute("DeletionFailureMessage",
								"Internal Error.");
						return;
					}
					for (ServerCompany cmpny : client.getCompanies()) {
						if (cmpny.getID() == Long.parseLong(companyID)) {
							serverCompany = cmpny;
						}
					}

					if (serverCompany == null) {
						httpSession.setAttribute(COMPANY_DELETION_STATUS,
								"Fail");
						httpSession.setAttribute("DeletionFailureMessage",
								"Company Doesnot Exist");
						return;
					}

					String schemaName = Server.COMPANY + companyID;

					IS2SService s2sService = getS2sSyncProxy(serverCompany
							.getServer().getAddress());

					boolean isAdmin = s2sService.isAdmin(
							Long.parseLong(companyID), email);

					if (!isAdmin) {
						httpSession.setAttribute(COMPANY_DELETION_STATUS,
								"Fail");
						httpSession
								.setAttribute("DeletionFailureMessage",
										"You Don't have Permissions to Delete this Company");
						// req.setAttribute("message",
						// "You don't have permisssions to to Delete this Company");
						// dispatch(req, resp, deleteCompanyView);
						return;
					}

					if (deleteAllUsers
							|| serverCompany.getClients().size() == 1) {
						// Deleting ServerCompany
						Set<Client> clients = serverCompany.getClients();
						for (Client clnt : clients) {
							clnt.getCompanies().remove(serverCompany);
							session.saveOrUpdate(clnt);
						}
						session.delete(serverCompany);

						// Deleting Company
						IS2SService s2sSyncProxy = getS2sSyncProxy(serverCompany
								.getServer().getAddress());
						s2sSyncProxy.deleteCompany(serverCompany.getId());

					} else {

						// Deleting Client from ServerCompany
						client.getCompanies().remove(serverCompany);
						session.saveOrUpdate(client);

						s2sService.deleteUserFromCompany(
								Long.parseLong(companyID), email);
					}
					transaction.commit();
					httpSession
							.setAttribute(COMPANY_DELETION_STATUS, "Success");
					updateServers(serverCompany.getServer(), false);
				} catch (Exception e) {
					e.printStackTrace();
					transaction.rollback();
					httpSession.setAttribute(COMPANY_DELETION_STATUS, "Fail");
					httpSession.setAttribute("DeletionFailureMessage",
							"Internal Error Occured");
				} finally {
					httpSession.removeAttribute(COMPANY_ID);
					session.close();
				}
			}
		}).start();

		if (message.isEmpty()) {
			message = "Success";
		}
		redirectExternal(req, resp, COMPANY_STATUS_URL);
		// req.setAttribute("message", message);
		// dispatch(req, resp, deleteCompanyView);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String emailID = (String) req.getSession().getAttribute(EMAIL_ID);
		if (emailID == null) {
			req.setAttribute("message",
					"Company deletion failed because of invalide session.");
			dispatch(req, resp, deleteCompanyView);
			return;
		}

		deleteComapny(req, resp);
	}
}
