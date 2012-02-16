package com.vimukti.accounter.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientPaypalDetails;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.utils.HibernateUtil;

public class PayPalIPINServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// read post from PayPal
		// system and add 'cmd'
		Enumeration en = req.getParameterNames();
		String str = "cmd=_notify-validate";
		Map<String, String> params = new HashMap<String, String>();
		while (en.hasMoreElements()) {
			String paramName = (String) en.nextElement();
			String paramValue = req.getParameter(paramName);
			params.put(paramName, paramValue);
			str = str + "&" + paramName + "=" + URLEncoder.encode(paramValue);
		}

		String paymentStatus = req.getParameter("payment_status");
		String emailId = req.getParameter("custom");
		String txnId = req.getParameter("txn_id");
		String txnType = req.getParameter("txn_type");

		// post back to PayPal system to validate
		// NOTE: change http: to https: in the following URL to verify using SSL
		// (for increased security).
		// using HTTPS requires either Java 1.4 or greater, or Java Secure
		// Socket Extension (JSSE)
		// and configured for older versions.
		URL u = new URL("https://www.sandbox.paypal.com/cgi-bin/webscr");
		URLConnection uc = u.openConnection();
		uc.setDoOutput(true);
		uc.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		PrintWriter pw = new PrintWriter(uc.getOutputStream());
		pw.println(str);
		pw.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				uc.getInputStream()));
		String res = in.readLine();
		in.close();

		if (txnType.equals("subscr_cancel")) {
			removeClientSubscription(emailId);
		}

		if (res.equals("VERIFIED")) {
			// check that paymentStatus=Completed
			// check that txnId has not been previously processed
			// check that receiverEmail is your Primary PayPal email
			// check that paymentAmount/paymentCurrency are correct
			// process payment

			if (emailId != null && txnId != null) {
				if (!paymentStatus.equals("Completed")) {
					sendInfo("Your process is not completed", req, resp);
					return;
				}
				Session openSession = HibernateUtil.openSession();
				try {
					saveDetailsInDB(params, emailId);
					upgradeClient(params, emailId);
				} catch (Exception e) {
				} finally {
					if (openSession.isOpen()) {
						openSession.close();
					}
				}
			} else {
			}

		} else if (res.equals("INVALID")) {
			// log for investigation
		} else {
			// error
		}
	}

	private void removeClientSubscription(String emailId) {
		Client client = getClient(emailId);
		client.getClientSubscription().getSubscription().setType(0);
		Session session = HibernateUtil.getCurrentSession();
		Transaction beginTransaction = session.beginTransaction();
		session.saveOrUpdate(client);
		beginTransaction.commit();

	}

	private void upgradeClient(Map<String, String> params, String emailId) {
		String type = params.get("option_selection1");
		int paymentType = 0;
		if (type.equals("One user monthly")) {
			paymentType = Subscription.ONE_USER_MONTHLY_SUBSCRIPTION;
		} else if (type.equals("One user yearly")) {
			paymentType = Subscription.ONE_USER_YEARLY_SUBSCRIPTION;
		} else if (type.equals("2 users monthly")) {
			paymentType = Subscription.TWO_USERS_MONTHLY_SUBSCRIPTION;
		} else if (type.equals("2 users yearly")) {
			paymentType = Subscription.TWO_USERS_YEARLY_SUBSCRIPTION;
		} else if (type.equals("5 users monthly")) {
			paymentType = Subscription.FIVE_USERS_MONTHLY_SUBSCRIPTION;
		} else if (type.equals("5 users yearly")) {
			paymentType = Subscription.FIVE_USERS_YEARLY_SUBSCRIPTION;
		} else if (type.equals("Unlimited Users monthly")) {
			paymentType = Subscription.UNLIMITED_USERS_MONTHLY_SUBSCRIPTION;
		} else if (type.equals("Unlimited Users yearly")) {
			paymentType = Subscription.UNLIMITED_USERS_YEARLY_SUBSCRIPTION;
		}
		Client client = getClient(emailId);
		client.getClientSubscription().getSubscription().setType(paymentType);
		Session session = HibernateUtil.getCurrentSession();
		Transaction beginTransaction = session.beginTransaction();
		session.saveOrUpdate(client);
		beginTransaction.commit();
	}

	private void sendInfo(String string, HttpServletRequest req,
			HttpServletResponse resp) {
		req.setAttribute("info", string);
		try {
			RequestDispatcher reqDispatcher = getServletContext()
					.getRequestDispatcher(GoPremiumServlet.view);
			reqDispatcher.forward(req, resp);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
	}

	private void saveDetailsInDB(Map<String, String> params, String emailId) {
		ClientPaypalDetails details = new ClientPaypalDetails();
		details.setFirstname(params.get("first_name"));
		details.setLastname(params.get("last_name"));
		details.setAddressCountry(params.get("address_country"));
		details.setPayerEmail(params.get("payer_email"));
		String string = params.get("mc_gross");
		details.setPaymentGross(Double.parseDouble(string));
		details.setMcCurrency(params.get("mc_currency"));
		details.setPaymentStatus(params.get("payment_status"));
		details.setClinetEmailId(emailId);

	}

}
