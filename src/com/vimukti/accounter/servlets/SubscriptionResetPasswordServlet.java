package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;

/**
 * @author Sai Karthik K
 * 
 */
public class SubscriptionResetPasswordServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger LOG = Logger
			.getLogger(SubscriptionResetPasswordServlet.class);

	private static final int UPDATE_PROCESS_SUCCESSFUL = 101;
	private static final int UPDATE_PROCESS_UNSUCCESSFUL = 102;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		switch (processRequest(req, resp)) {
		case UPDATE_PROCESS_SUCCESSFUL:
			dispatchView(req, resp, UPDATE_PROCESS_SUCCESSFUL);
			break;
		case UPDATE_PROCESS_UNSUCCESSFUL:
			dispatchView(req, resp, UPDATE_PROCESS_UNSUCCESSFUL);
			break;
		}

	}

	private int processRequest(HttpServletRequest req, HttpServletResponse resp) {

		String emailId = null;
		String password = null;
		String domainName = null;
		String adminName = null;
		try {
			JSONObject jsonObject = new JSONObject(req
					.getParameter("jsonObject"));
			emailId = jsonObject.getString("emailId");
			password = HexUtil.getRandomString();
			domainName = jsonObject.getString("domainName");
			adminName = jsonObject.getString("adminName");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		User user = getUserByUserId(emailId, domainName);
		if (user == null) {
			LOG.info("We don't  have user:");
			return UPDATE_PROCESS_UNSUCCESSFUL;
		}

		ResetIDentityPasswordEvent event = new ResetIDentityPasswordEvent(user
				.getUserid(), domainName, password, adminName, true);
		Server.getInstance().process(user.getUserid(), event);
		return UPDATE_PROCESS_SUCCESSFUL;
	}

	public User getUserByUserId(String userID, String domainName) {
		Session session = HibernateUtil.openSession(domainName);
		User note = (User) session.getNamedQuery("user.by.emailid")
				.setParameter("emailID", userID).uniqueResult();
		session.close();
		return note;
	}

	public void dispatchView(HttpServletRequest request,
			HttpServletResponse response, int view) {
		try {
			response.getWriter().write(String.valueOf(view));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
