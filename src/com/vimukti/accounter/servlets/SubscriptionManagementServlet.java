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
import com.vimukti.accounter.core.ClientSubscription;
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
		Client client = getClient(emailId);
		if (client == null) {
			resp.sendRedirect(LOGIN_URL);
			return;
		}
		String string = req.getParameter("userMailds");
		ClientSubscription clientSubscription = client.getClientSubscription();
		Set<String> oldMembers = client.getClientSubscription().getMembers();
		Set<String> members = getMembers(string);
		oldMembers.addAll(members);
		if (!checkTotalMembers(oldMembers, client.getClientSubscription()
				.getPremiumType())) {
			req.setAttribute("info", "No of users should be limit");
			dispatch(req, resp, view);
			return;
		}
		clientSubscription.setMembers(getMembers(string));

		saveEntry(clientSubscription);
		transaction.commit();

		redirectExternal(req, resp, "/main/subscription/thankyou");
	}

	private boolean checkTotalMembers(Set<String> oldMembers, int type) {
		switch (type) {
		case ClientSubscription.ONE_USER:
			return oldMembers.size() == 1;
		case ClientSubscription.TWO_USERS:
			return oldMembers.size() == 2;
		case ClientSubscription.FIVE_USERS:
			return oldMembers.size() == 5;
		default:
			return true;
		}
	}

	private Set<String> getMembers(String string) {
		Set<String> emailIds = new HashSet<String>();
		String[] stringArray = string.split("\r\n");
		for (String string2 : stringArray) {
			emailIds.add(string2);
		}
		return emailIds;
	}

	private void showSubscriptionManagementDetails(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession();
		if (session == null) {
			resp.sendRedirect(LOGIN_URL);
			return;
		}
		String emailId = (String) req.getSession().getAttribute(EMAIL_ID);
		if (emailId != null) {
			Client client = getClient(emailId);
			if (client == null) {
				resp.sendRedirect(LOGIN_URL);
				return;
			}
			req.setAttribute("premiumType", client.getClientSubscription()
					.getPremiumType());
			req.setAttribute("ExpiredDate", client.getClientSubscription()
					.getExpiredDate());

			String finalString = "";
			for (String string2 : client.getClientSubscription().getMembers()) {
				finalString = finalString + "\n" + string2;
			}
			if (!finalString.isEmpty()) {
				finalString = finalString.substring(1);
			}

			if (!finalString.isEmpty()) {
				finalString = finalString.substring(1);
			}

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
