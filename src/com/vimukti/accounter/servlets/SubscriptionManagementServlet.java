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
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientSubscription;
import com.vimukti.accounter.services.SubscryptionTool;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class SubscriptionManagementServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2578211747595543714L;
	private String view = "/WEB-INF/subscriptionmanagement.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
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
			ClientSubscription clientSubscription = client
					.getClientSubscription();
			if (clientSubscription.getPremiumType() == 0) {
				req.setAttribute("error", "Your not premium user");
			}
			showSubscriptionManagementDetails(client,
					clientSubscription.getMembers(), req, resp);
		} else {
			resp.sendRedirect(LOGIN_URL);
			return;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		HttpSession session2 = req.getSession();
		if (session2 == null) {
			resp.sendRedirect(LOGIN_URL);
			return;
		}
		String emailId = (String) req.getSession().getAttribute(EMAIL_ID);
		Client client = getClient(emailId);
		if (client == null) {
			resp.sendRedirect(LOGIN_URL);
			return;
		}
		String string = req.getParameter("userMailds");
		ClientSubscription clientSubscription = client.getClientSubscription();
		if (clientSubscription.getPremiumType() == 0) {
			req.setAttribute("error", "Your not premium user");
			showSubscriptionManagementDetails(client,
					clientSubscription.getMembers(), req, resp);
			return;
		}
		Set<String> oldMembers = client.getClientSubscription().getMembers();
		List<String> existedUsers = getExistedUsers(oldMembers);
		Set<String> members = getMembers(string);
		members.add(emailId);

		if (!checkTotalMembers(members, client.getClientSubscription()
				.getPremiumType())) {
			req.setAttribute("error", "No of users should be limit");
			showSubscriptionManagementDetails(client, members, req, resp);
			return;
		}
		clientSubscription.setMembers(members);

		saveEntry(clientSubscription);

		try {
			int deletedUsers = mergeUsers(client, oldMembers, existedUsers,
					members);
			if (deletedUsers == 0) {
				transaction.commit();
			}

		} catch (AccounterException e) {
			e.printStackTrace();
		}
		resp.sendRedirect(COMPANIES_URL);
	}

	private int mergeUsers(Client client, Set<String> oldMembers,
			List<String> existedUsers, Set<String> members)
			throws AccounterException {
		int deletedUsers = 0;
		for (String o : oldMembers) {
			if (!members.contains(o) && existedUsers.contains(o)) {
				SubscryptionTool.deleteUser(client, o);
				deletedUsers++;
			}
		}
		return deletedUsers;
	}

	private boolean checkTotalMembers(Set<String> members, int type) {
		switch (type) {
		case ClientSubscription.ONE_USER:
			return members.size() <= 1;
		case ClientSubscription.TWO_USERS:
			return members.size() <= 2;
		case ClientSubscription.FIVE_USERS:
			return members.size() <= 5;
		default:
			return true;
		}
	}

	private Set<String> getMembers(String string) {
		Set<String> emailIds = new HashSet<String>();
		String[] stringArray = string.split("\r\n");
		for (String string2 : stringArray) {
			if (!string2.isEmpty()) {
				emailIds.add(string2);
			}
		}
		return emailIds;
	}

	private void showSubscriptionManagementDetails(Client client,
			Set<String> members, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		members = new HashSet<String>(members);
		members.remove(client.getEmailId());
		req.setAttribute("emailId", client.getEmailId());

		req.setAttribute("premiumType", client.getClientSubscription()
				.getPremiumType());
		req.setAttribute("expiredDate", client.getClientSubscription()
				.getExpiredDateAsString());

		String finalString = "";
		List<String> createdUsers = getExistedUsers(members);
		boolean isFirst = true;
		finalString = "[";
		for (String string2 : members) {
			if (string2.isEmpty()) {
				continue;
			}
			if (isFirst) {
				finalString += getDict(string2, createdUsers.contains(string2));
				isFirst = false;
			} else {
				finalString += ","
						+ getDict(string2, createdUsers.contains(string2));
			}
		}
		finalString += "]";
		req.setAttribute("userIdsList", finalString);
		dispatch(req, resp, view);
	}

	@SuppressWarnings("unchecked")
	private List<String> getExistedUsers(Set<String> members) {
		if (members.isEmpty()) {
			return new ArrayList<String>();
		}
		return HibernateUtil.getCurrentSession()
				.getNamedQuery("get.created.users")
				.setParameterList("users", members).list();
	}

	private String getDict(String string2, boolean contains) {
		return "{emailId:'" + string2 + "',isCreated:"
				+ (contains ? "true" : "false") + "}";
	}
}
