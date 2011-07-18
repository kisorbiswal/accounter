package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONException;
import org.json.JSONObject;

import com.bizantra.server.internal.core.BizantraCompany;
import com.bizantra.server.main.Server;
import com.bizantra.server.storage.HibernateUtil;

/**
 * 
 * @author Sai Karthik K
 * 
 */
public class SubscriptionUpdateCompanyServlet extends HttpServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final int EXTENSION_CREATED = 0;
	private static final int EXTENSION_REMOVED = 1;
	private static final int EXTENSION_NOT_CREATED = 2;

	private static final int UPDATE_PROCESS_SUCCESSFUL = 101;
	private static final int REMOVE_PROCESS_SUCCESSFUL = 102;
	private static final int UPDATE_PROCESS_UNSUCCESSFUL = 103;

	private String view = "/CreateCompany.jsp";

	private String ADD_ACTION = "Add Extension";

	private String REMOVE_ACTION = "Remove Extension";

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {

			switch (updateCompany(request)) {
			case EXTENSION_CREATED:
				dispatchView(request, response, UPDATE_PROCESS_SUCCESSFUL);
				break;
			case EXTENSION_REMOVED:
				dispatchView(request, response, REMOVE_PROCESS_SUCCESSFUL);
				break;
			case EXTENSION_NOT_CREATED:
				dispatchView(request, response, UPDATE_PROCESS_UNSUCCESSFUL);
				break;
			}

		} catch (JSONException e) {

			e.printStackTrace();
		}
	}

	public void dispatchView(HttpServletRequest request,
			HttpServletResponse response, int view) {
		try {
			response.getWriter().write(String.valueOf(view));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//
	private int updateCompany(HttpServletRequest request) throws JSONException {
		JSONObject jsonObject = new JSONObject(request
				.getParameter("jsonObject"));
		// TODO need to check for pending
		// String emailID = jsonObject.getString("option_selection3");// user
		// email
		String userCompany = jsonObject.getString("domainName");
		// String subscriptionType = jsonObject.getString("option_selection1")
		// .split(":")[0].trim();
		// IF in case there are multiple subscriptions..
		// if (!(subscriptionType.equalsIgnoreCase("type-1"))) {
		//			
		// }
		int subscriptionTotalUsers = Integer.parseInt(jsonObject
				.getString("subscriptionTotalUsers"));
		int subscriptionTotalLiteUsers = Integer.parseInt(jsonObject
				.getString("subscriptionTotalLiteUsers"));
		long subscriptionTotalSize = Long.parseLong(jsonObject
				.getString("subscriptionTotalSize"));

		if (jsonObject.getString("ACTION").equals(ADD_ACTION)) {
			Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
			Query query = session.getNamedQuery("get.company.by.name");
			query.setString("name", userCompany);
			if (query.list().size() > 0) {
				session.close();
				session = HibernateUtil.openSession(userCompany);
				query = session.getNamedQuery("get.company.by.name");
				query.setString("name", userCompany);

				BizantraCompany company = (BizantraCompany) query.list().get(0);

				// Log.info("Request came for renewing company..");
				company.updateCompanyWithExtensions(subscriptionTotalUsers,
						subscriptionTotalLiteUsers, subscriptionTotalSize);
				updateCompany(session, company);
				session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
				company = (BizantraCompany) session.getNamedQuery(
						"get.company.by.name").setString("name", userCompany)
						.list().get(0);
				company.updateCompanyWithExtensions(subscriptionTotalUsers,
						subscriptionTotalLiteUsers, subscriptionTotalSize);
				updateCompany(session, company);
				// Log.info("isCompanyUpdate made true");
				session.close();
				return EXTENSION_CREATED;
			}
			session.close();
			return EXTENSION_NOT_CREATED;
		} else if (jsonObject.getString("ACTION").equals(REMOVE_ACTION)) {

			Session session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
			Query query = session.getNamedQuery("get.company.by.name");
			query.setString("name", userCompany);
			if (query.list().size() > 0) {
				session.close();
				session = HibernateUtil.openSession(userCompany);
				query = session.getNamedQuery("get.company.by.name");
				query.setString("name", userCompany);

				BizantraCompany company = (BizantraCompany) query.list().get(0);

				// Log.info("Request came for renewing company..");
				company.updateCompanyWithxtensionsRemoval(
						subscriptionTotalUsers, subscriptionTotalLiteUsers,
						subscriptionTotalSize);
				updateCompany(session, company);
				session = HibernateUtil.openSession(Server.LOCAL_DATABASE);
				company = (BizantraCompany) session.getNamedQuery(
						"get.company.by.name").setString("name", userCompany)
						.list().get(0);
				company.updateCompanyWithxtensionsRemoval(
						subscriptionTotalUsers, subscriptionTotalLiteUsers,
						subscriptionTotalSize);
				updateCompany(session, company);
				session.close();
				// Log.info("isCompanyUpdate made true");
				return EXTENSION_REMOVED;
			}
			session.close();
			return EXTENSION_NOT_CREATED;
		}
		return EXTENSION_NOT_CREATED;

	}

	private void updateCompany(Session session, BizantraCompany company) {
		Transaction tx = session.beginTransaction();
		session.update(company);
		tx.commit();
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(view);
	}
}
