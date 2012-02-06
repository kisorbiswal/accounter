package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.core.SubscriptionManagementData;
import com.vimukti.accounter.core.User;

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
		Client client;
		SubscriptionManagementData managementData = new SubscriptionManagementData();
		String emailId = req.getSession().getAttribute(EMAIL_ID).toString();
		if (emailId != null) {
			client = getClient(emailId);
			managementData.setAdminMailId(emailId);
			managementData.setSubscriptionDate(String.valueOf(client
					.getClientSubscription().getCreatedDate()));
		}

		managementData.setUserMailds(req.getParameter("userMailds").toString());
		managementData.setSubscriptionType(Subscription.getStringToType(req
				.getParameter("subscriptionType").toString()));

	}

	private void showSubscriptionManagementDetails(HttpServletRequest req,
			HttpServletResponse resp) {
		SubscriptionManagementData managementData = new SubscriptionManagementData();
		String emailId = req.getSession().getAttribute(EMAIL_ID).toString();
		if (emailId != null) {
			Client client = getClient(emailId);
			managementData.setUserMailds(getUsersMailIds(client));
			managementData.setAdminMailId(emailId);
			managementData.setSubscriptionDate(String.valueOf(client
					.getClientSubscription().getCreatedDate()));
			managementData.setSubscriptionType(client.getClientSubscription()
					.getSubscription().getType());
			req.setAttribute("managementData", managementData);
			dispatch(req, resp, view);
		}
		return;
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
