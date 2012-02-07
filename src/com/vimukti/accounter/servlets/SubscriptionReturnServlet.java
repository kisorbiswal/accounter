package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientPaypalDetails;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.utils.HibernateUtil;

public class SubscriptionReturnServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String VIEW = "/WEB-INF/paymentdone.jsp";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String emailId = (String) req.getSession().getAttribute(
				BaseServlet.EMAIL_ID);
		String transactionID = req.getParameter("tx");
		if (emailId != null && transactionID != null) {
			String requestToPayPal = requestToPayPal(transactionID);
			String[] split = requestToPayPal.split("\n");
			if (split[0].equals("SUCCESS")) {
				Map<String, String> params = new HashMap<String, String>();
				for (int i = 1; i < split.length; i++) {
					String[] split2 = split[i].split("=");
					params.put(split2[0], split2[1]);
				}
				String status = params.get("payment_status");
				if (!status.equals("Completed")) {
					sendInfo("Your process is not completed", req, resp);
					return;
				}
				Session openSession = HibernateUtil.openSession();
				try {
					saveDetailsInDB(params, emailId);
					upgradeClient(params, emailId);
					req.getRequestDispatcher(VIEW).forward(req, resp);
				} catch (Exception e) {
				} finally {
					if (openSession.isOpen()) {
						openSession.close();
					}
				}
			} else {
				sendInfo("Your transaction is fail", req, resp);
			}
		} else {
			resp.sendRedirect(BaseServlet.LOGIN_URL);
		}
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

	private Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter("emailId", emailId);
		Client client = (Client) namedQuery.uniqueResult();
		return client;
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
		details.setFirstname(params.get("firstname"));
		details.setLastname(params.get("lastname"));
		details.setAddressCountry(params.get("addressCountry"));
		details.setPayerEmail(params.get("payerEmail"));
		details.setPaymentGross(Double.parseDouble(params.get("paymentGross")));
		details.setMcCurrency(params.get("mcCurrency"));
		details.setPaymentStatus(params.get("paymentStatus"));
		details.setClinetEmailId(emailId);
		 
	}

	private String requestToPayPal(String transactionID) {
		PostMethod method = null;

		String urlString = "https://www.paypal.com/cgi-bin/webscr";

		try {
			// Creating the GetMethod instance
			method = new PostMethod(urlString);

			// Retries to establish a successful connection the specified number
			// of times if the initial attempts are not successful.
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler(1, false));

			method.getParams().setParameter("http.socket.timeout",
					new Integer(5000));

			NameValuePair[] data = {
					new NameValuePair("cmd", "_notify-synch"),
					new NameValuePair("tx", transactionID),
					new NameValuePair("at",
							ServerConfiguration.getPaypalIdentityId()),
					new NameValuePair("submit", "PDT") };
			method.setRequestBody(data);
			HttpClient client = new HttpClient();
			client.executeMethod(method);
			String responseBodyAsString = method.getResponseBodyAsString();
			return responseBodyAsString;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return null;

	}
}
