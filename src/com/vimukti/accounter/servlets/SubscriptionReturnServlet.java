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

import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.main.ServerConfiguration;

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
				saveDetailsInDB(params, emailId);
				upgradeClient(params, emailId);
				req.getRequestDispatcher(VIEW).forward(req, resp);
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
		if (type.equals("")) {
			paymentType = Subscription.ONE_USER_MONTHLY_SUBSCRIPTION;
		} else if (type.equals("")) {
			paymentType = Subscription.ONE_USER_YEARLY_SUBSCRIPTION;
		} else if (type.equals("")) {
			paymentType = Subscription.ONE_USER_MONTHLY_SUBSCRIPTION;
		} else if (type.equals("")) {
			paymentType = Subscription.ONE_USER_MONTHLY_SUBSCRIPTION;
		} else if (type.equals("")) {
			paymentType = Subscription.ONE_USER_MONTHLY_SUBSCRIPTION;
		} else if (type.equals("")) {
			paymentType = Subscription.ONE_USER_MONTHLY_SUBSCRIPTION;
		} else if (type.equals("")) {
			paymentType = Subscription.ONE_USER_MONTHLY_SUBSCRIPTION;
		} else if (type.equals("")) {
			paymentType = Subscription.ONE_USER_MONTHLY_SUBSCRIPTION;
		} else if (type.equals("")) {
			paymentType = Subscription.ONE_USER_MONTHLY_SUBSCRIPTION;
		} else if (type.equals("")) {
			paymentType = Subscription.ONE_USER_MONTHLY_SUBSCRIPTION;
		} else if (type.equals("")) {
			paymentType = Subscription.ONE_USER_MONTHLY_SUBSCRIPTION;
		} else if (type.equals("")) {
			paymentType = Subscription.ONE_USER_MONTHLY_SUBSCRIPTION;
		}
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
			int executeMethod = client.executeMethod(method);
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
