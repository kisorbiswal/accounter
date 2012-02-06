package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientSubscription;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.Security;
import com.vimukti.accounter.web.client.Global;

public class SignupServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String view = "/WEB-INF/signup.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		dispatch(req, resp, view);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Take userName from request
		String emailId = req.getParameter("emailId").trim();
		String firstName = req.getParameter("firstName").trim();
		String lastName = req.getParameter("lastName").trim();
		String password = req.getParameter("password").trim();
		String phoneNumber = req.getParameter("phoneNumber").trim();
		String country = req.getParameter("country").trim();
		boolean isSubscribedToNewsLetter = false;
		if (req.getParameter("newsletter") != null
				&& req.getParameter("newsletter").equals("on"))
			isSubscribedToNewsLetter = true;

		boolean isAgreed = false;
		if (req.getParameter("agree") != null
				&& req.getParameter("agree").equals("on"))
			isAgreed = true;
		if (!isAgreed) {
			dispatchMessage("Please accept Terms of use", req, resp, view);
		}

		if (!isValidInputs(NAME, firstName, lastName, country)
				|| !isValidInputs(MAIL_ID, emailId)) {
			dispatchMessage("Given Inputs are wrong.", req, resp, view);
			return;
		}
		emailId = emailId.toLowerCase();
		String passwordWithHash = HexUtil.bytesToHex(Security.makeHash(emailId
				+ password));
		HttpSession session = req.getSession(true);

		Session hibernateSession = HibernateUtil.openSession();
		Transaction transaction = null;
		try {
			transaction = hibernateSession.beginTransaction();
			// Have to check UserExistence
			if (getClient(emailId) != null) {
				// If Exists then send to login password with username
				// TODO::: in login.jsp check for email id in the request if it
				// is available set this email id in the email id field of login
				// page
				// redirectExternal(req, resp, LOGIN_URL);
				Client client = getClient(emailId);
				if (client.isDeleted()) {
					String token = createActivation(emailId);
					client.setActive(false);
					client.setUsers(new HashSet<User>());
					client.setEmailId(emailId);
					client.setFirstName(firstName);
					client.setLastName(lastName);
					client.setFullName(Global.get().messages()
							.fullName(firstName, lastName));
					client.setPassword(passwordWithHash);
					client.setPhoneNo(phoneNumber);
					client.setCountry(country);
					client.setSubscribedToNewsLetters(isSubscribedToNewsLetter);
					client.setDeleted(false);
					saveEntry(client);
					session.setAttribute(EMAIL_ID, emailId);

					// Email to that user.
					sendActivationEmail(token, client);
					// Send to SignUp Success View
					String message = "?message=" + ACT_FROM_SIGNUP;
					redirectExternal(req, resp, ACTIVATION_URL + message);
					transaction.commit();
				} else {
					req.setAttribute(
							"errormessage",
							"This Email ID is already registered with Accounter, try to signup with another Email ID. If you are the registered user click <a href=\"/main/login\">here</a> to login.");
					dispatch(req, resp, view);
					return;
				}
			} else {
				// else
				// Generate Token and create Activation and save. then send
				String token = createActivation(emailId);

				// Create Client and Save
				Client client = new Client();
				client.setActive(false);
				client.setUsers(new HashSet<User>());
				client.setEmailId(emailId);
				client.setFirstName(firstName);
				client.setLastName(lastName);
				client.setFullName(firstName + " " + lastName);
				client.setPassword(passwordWithHash);
				client.setPhoneNo(phoneNumber);
				client.setCountry(country);
				client.setSubscribedToNewsLetters(isSubscribedToNewsLetter);

				// clientSubscription.setCreatedDate(new Date(System
				// .currentTimeMillis()));
				// Set<String> members = new HashSet<String>();
				// members.add(emailId);
				// clientSubscription.setMembers(members);
				// Subscription subscription = new Subscription();
				// subscription.setName("");
				// Set<String> features = new HashSet<String>();
				// subscription.setFeatures(features);
				// clientSubscription.setSubscription(subscription);

				ClientSubscription clientSubscription = new ClientSubscription();
				clientSubscription.setSubscription(new Subscription());
				saveEntry(clientSubscription);

				client.setClientSubscription(clientSubscription);
				client.setDeleted(false);

				saveEntry(client);
				session.setAttribute(EMAIL_ID, emailId);

				// Email to that user.
				sendActivationEmail(token, client);
				// Send to SignUp Success View
				String message = "?message=" + ACT_FROM_SIGNUP;

				redirectExternal(req, resp, ACTIVATION_URL + message);
				transaction.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (hibernateSession.isOpen())
				hibernateSession.close();
		}

		return;
	}

}
