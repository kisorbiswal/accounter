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

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.ClientSubscription;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.OperationContext;

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
		List<String> existedUsers = getExistedUsers(oldMembers);
		Set<String> members = getMembers(string);
		try {
			mergeUsers(client, oldMembers, existedUsers, members);
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		if (!checkTotalMembers(oldMembers, client.getClientSubscription()
				.getPremiumType())) {
			req.setAttribute("info", "No of users should be limit");
			dispatch(req, resp, view);
			return;
		}
		clientSubscription.setMembers(members);

		saveEntry(clientSubscription);
		transaction.commit();

		redirectExternal(req, resp, "/main/subscription/thankyou");
	}

	private void mergeUsers(Client client, Set<String> oldMembers,
			List<String> existedUsers, Set<String> members)
			throws AccounterException {
		for (String o : oldMembers) {
			if (!members.contains(o) && existedUsers.contains(o)) {
				deleteUser(client, o);
			}
		}
	}

	private void deleteUser(Client client, String emailId)
			throws AccounterException {
		Set<User> users = client.getUsers();
		User user = (User) HibernateUtil.getCurrentSession()
				.getNamedQuery("getUser.by.mailId")
				.setParameter("emailId", emailId).uniqueResult();
		boolean canDelete = false;
		for (User u : users) {
			if (u.getID() == user.getID()) {
				canDelete = true;
			}
		}
		if (!canDelete) {
			return;
		}

		ClientUser coreUser = new ClientConvertUtil().toClientObject(user,
				ClientUser.class);
		String clientClassSimpleName = coreUser.getObjectType()
				.getClientClassSimpleName();
		FinanceTool financeTool = new FinanceTool();
		OperationContext context = new OperationContext(user.getCompany()
				.getId(), coreUser, emailId, String.valueOf(coreUser.getID()),
				clientClassSimpleName);
		financeTool.delete(context);
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
			req.setAttribute("expiredDate", client.getClientSubscription()
					.getExpiredDateAsString());

			String finalString = "";
			Set<String> members = client.getClientSubscription().getMembers();
			List<String> createdUsers = getExistedUsers(members);
			boolean isFirst = true;
			if (!members.isEmpty()) {
				finalString = "[";
				for (String string2 : members) {
					if (isFirst) {
						finalString += getDict(string2,
								createdUsers.contains(string2));
						isFirst = false;
					} else {
						finalString += ","
								+ getDict(string2,
										createdUsers.contains(string2));
					}
				}
				finalString += "]";
			}
			req.setAttribute("userIdsList", finalString);
			dispatch(req, resp, view);
		} else {
			resp.sendRedirect(LOGIN_URL);
			return;
		}
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
