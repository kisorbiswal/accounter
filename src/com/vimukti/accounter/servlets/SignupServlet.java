package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.core.Activation;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ServerCompany;
import com.vimukti.accounter.utils.SecureUtils;

public class SignupServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Take userName from request
		String emailId = req.getParameter("emailId").trim().toLowerCase();
		String firstName = req.getParameter("firstName").trim().toLowerCase();
		String lastName = req.getParameter("lastName").trim().toLowerCase();
		if (!isValidInputs(NAME, firstName, lastName)
				|| !isValidInputs(MAIL_ID, emailId)) {
			dispatchMessage("Given Inputs are wrong.", req, resp, "");
			return;
		}
		// Have to check UserExistence
		if (getClient(emailId) != null) {
			// If Exists then send to login password with username
			redirect(req, resp, "/sites/login.jsp");
		} else {
			// else
			// Generate Token and create Activation and save. then send
			String tocken = SecureUtils.createID();
			Activation activation = new Activation();
			activation.setEmailId(emailId);
			activation.setTocken(tocken);
			activation.setSignUpDate(new Date());
			saveEntry(activation);

			// Create Client and Save
			Client client = new Client();
			client.setActive(false);
			client.setCompanies(new HashSet<ServerCompany>());
			client.setEmailId(emailId);
			client.setFirstName(firstName);
			client.setLastName(lastName);
			saveEntry(client);

			// Email to that user.
			sendActivationEmail(emailId, tocken);
			// Send to SignUp Success View
			redirect(req, resp, "");
		}

	}

	private void sendActivationEmail(String emailId, String tocken) {
		// TODO Auto-generated method stub

	}
}
