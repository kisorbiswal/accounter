package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.vimukti.accounter.core.Activation;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ServerCompany;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;

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

		Session hibernateSession = HibernateUtil.openSession(LOCAL_DATABASE);
		try {
			// Have to check UserExistence
			if (getClient(emailId) != null) {
				// If Exists then send to login password with username
				// TODO::: in login.jsp check for email id in the request if it
				// is available set this email id in the email id field of login
				// page
				redirect(req, resp, "/WEB-INF/login");

			} else {
				// else
				// Generate Token and create Activation and save. then send
				String token = SecureUtils.createID();
				Activation activation = new Activation();
				activation.setEmailId(emailId);
				activation.setToken(token);
				activation.setSignUpDate(new Date());
				saveEntry(activation);

				// Create Client and Save
				Client client = new Client();
				client.setActive(false);
				client.setCompanies(new HashSet<ServerCompany>());
				client.setEmailId(emailId);
				client.setFirstName(firstName);
				client.setLastName(lastName);
				client.setPhoneNo(phoneNumber);
				client.setCountry(country);
				client.setSubscribedToNewsLetters(isSubscribedToNewsLetter);
				saveEntry(client);

				// Email to that user.
				sendActivationEmail(token, client);
				// Send to SignUp Success View
				req.setAttribute("successmessage",
						"Thanks for registering with Accounter!");
				dispatch(req, resp, view);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (hibernateSession != null) {
				if (hibernateSession.isOpen())
					hibernateSession.close();
			}
		}
	}

	private void sendActivationEmail(String token, Client client) {
		UsersMailSendar.sendActivationMail(token, client);
	}
}
