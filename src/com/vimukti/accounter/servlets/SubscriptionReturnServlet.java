package com.vimukti.accounter.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.vimukti.accounter.main.ServerConfiguration;

public class SubscriptionReturnServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
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
					// Transaction Fail send this message to subscription
				}
				saveDetailsInDB(params, emailId);
			} else {
				// Transaction Fail send this message to subscription
			}
		} else {
			resp.sendRedirect(BaseServlet.LOGIN_URL);
			return;
		}
	}

	private void saveDetailsInDB(Map<String, String> params, String emailId) {
		// TODO Auto-generated method stub

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
