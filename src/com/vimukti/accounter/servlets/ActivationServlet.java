package com.vimukti.accounter.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.vimukti.accounter.core.Activation;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.Security;

public class ActivationServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// get the token
		String token = req.getParameter("code");
		// get activation record
		Activation activation = getActivation(token);
		// If it is null
		if (activation == null) {
			// set Error "Token has expired"
			req.setAttribute("error", "Token has expired.");
		} else {
			// otherwise
			// set token in session.
			HttpSession session = req.getSession(true);
			session.setAttribute("activationToken", token);
			req.setAttribute("emailId", activation.getEmailId());
		}
		// redirect To ActivationPage.
		redirect(req, resp, "");

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		if (session == null) {
			dispatchMessage("Token has expired.", req, resp);
			return;
		}

		// get token from session
		String token = (String) session.getAttribute("activationToken");
		// get activation record from table
		Activation activation = getActivation(token);
		// if it is null
		if (activation == null) {
			// dispatch withr "Token has expired".
			dispatchMessage("Token has expired.", req, resp);
			return;
		}
		// otherwise
		// getPasswords from request
		String password = req.getParameter("password");
		String confirm = req.getParameter("confirm");

		// compare if not equal send error message
		// otherwise
		if (!password.equals(confirm)) {
			dispatchMessage("Passwords are not matched.", req, resp);
			return;
		}

		// getClient record with activation emailId
		Client client = getClient(activation.getEmailId());

		// update password and set isActive true
		client.setPassword(HexUtil.bytesToHex(Security.makeHash(activation
				.getEmailId() + password.trim())));
		// and save Client, delete activation record
		saveEntry(client);
		deleteEntry(activation);

		// Send to login page with emailId
		redirect(req, resp, "");
	}

	private Activation getActivation(String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
