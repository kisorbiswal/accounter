package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		try {
			Client client = null;

			SubscriptionManagementData managementData = new SubscriptionManagementData();
			String emailId = req.getSession().getAttribute(EMAIL_ID).toString();
			if (emailId != null) {
				client = getClient(emailId);
				managementData.setAdminMailId(emailId);
				managementData.setSubscriptionDate(String.valueOf(client
						.getClientSubscription().getCreatedDate()));
			}

			managementData.setUserMailds(req.getParameter("userMailds")
					.toString());
			managementData.setSubscriptionType(Subscription.getStringToType(req
					.getParameter("subscriptionType").toString()));
			client.setSubscriptionManagementData(managementData);
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

	private Set<String> setMembers(String string) {
		String[] mailsString = string.split("\n");
		Set<String> mailsSet = new HashSet<String>();
		for (String string2 : mailsString) {
			mailsSet.add(string2);
		}
		return mailsSet;
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
		if (emailId != null) {
			Client client = getClient(emailId);
			managementData.setUserMailds(client.getSubscriptionManagementData()
					.getUserMailds().toString());
			managementData.setAdminMailId(emailId);
			if (client.getClientSubscription().getCreatedDate() != null) {
				managementData.setSubscriptionDate(String.valueOf(client
						.getClientSubscription().getCreatedDate()));
			} else {
				managementData.setSubscriptionDate(" ");
			}
			managementData.setSubscriptionType(client.getClientSubscription()
					.getSubscription().getType());
			req.setAttribute("managementData", managementData);

			String string = client.getSubscriptionManagementData()
					.getUserMailds().toString();
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
