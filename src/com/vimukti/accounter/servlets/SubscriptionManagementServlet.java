package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.core.SubscriptionManagementData;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HibernateUtil;

public class SubscriptionManagementServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2578211747595543714L;
	private String view = "/WEB-INF/subscriptionmanagement.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		showSubscriptionManagementDetails(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		String emailId = req.getSession().getAttribute(EMAIL_ID).toString();
		Client client = null;
		client = getClient(emailId);
		try {
			SubscriptionManagementData subscriptionManagementData = null;
			Session currentSession = HibernateUtil.getCurrentSession();
			Query query = currentSession.getNamedQuery(
					"get.subscription.mangementdata").setParameter("emailId",
					client.getEmailId());
			subscriptionManagementData = (SubscriptionManagementData) query
					.uniqueResult();

			if (subscriptionManagementData == null) {
				subscriptionManagementData = new SubscriptionManagementData();
			}

			if (emailId != null) {

				subscriptionManagementData.setAdminMailId(emailId);
				subscriptionManagementData.setSubscriptionDate(String
						.valueOf(client.getClientSubscription()
								.getCreatedDate()));
			}

			subscriptionManagementData.setUserMailds(req.getParameter(
					"userMailds").toString());
			subscriptionManagementData.setSubscriptionType(Subscription
					.getStringToType(req.getParameter("subscriptionType")
							.toString()));
			client.setSubscriptionManagementData(subscriptionManagementData);
			saveEntry(client.getClientSubscription());
			saveEntry(client.getSubscriptionManagementData());
			saveEntry(client);
			transaction.commit();

		} catch (Exception e) {
			e.printStackTrace();
			transaction.rollback();
		}
		redirectExternal(req, resp, "/main/subscription/thankyou");
	}

	private void showSubscriptionManagementDetails(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		SubscriptionManagementData managementData = new SubscriptionManagementData();
		HttpSession session = req.getSession();
		if (session == null) {
			resp.sendRedirect(LOGIN_URL);
			return;
		}
		String emailId = (String) req.getSession().getAttribute(EMAIL_ID);
		String string = "";
		if (emailId != null) {
			Client client = getClient(emailId);
			if (client.getSubscriptionManagementData() != null) {
				if (client.getSubscriptionManagementData().getUserMailds() != null) {
					managementData.setUserMailds(client
							.getSubscriptionManagementData().getUserMailds()
							.toString());
				}
			}
			managementData.setAdminMailId(emailId);
			if (client.getClientSubscription().getCreatedDate() != null) {
				managementData.setSubscriptionDate(String.valueOf(client
						.getClientSubscription().getCreatedDate()));
			} else {
				managementData.setSubscriptionDate(" ");
			}
			managementData.setSubscriptionType(client.getClientSubscription()
					.getPremiumType());
			req.setAttribute("managementData", managementData);

			if (client.getSubscriptionManagementData() != null) {
				string = client.getSubscriptionManagementData().getUserMailds()
						.toString();
			}
			String[] stringArray = string.split("\r\n");
			String finalString = "";
			for (String string2 : stringArray) {
				finalString = finalString + "," + string2;
			}
			finalString = finalString.substring(1);
			req.setAttribute("userIdsList", finalString);
			dispatch(req, resp, view);
		} else {
			resp.sendRedirect(LOGIN_URL);
			return;
		}
	}

	private String getUsersMailIds(Client client) {
		try {
			Set<User> list = (Set<User>) client.getUsers();
			List<String> mailIds = new ArrayList<String>();
			for (User user : list) {
				mailIds.add(user.getClientUser().getEmail());
			}
			XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
			xstream.setMode(XStream.NO_REFERENCES);
			xstream.alias("usersMailIds", String.class);

			return xstream.toXML(mailIds);
		} finally {
		}
	}
}
