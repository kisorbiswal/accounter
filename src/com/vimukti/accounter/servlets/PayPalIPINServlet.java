package com.vimukti.accounter.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientPaypalDetails;
import com.vimukti.accounter.core.ClientSubscription;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.services.SubscryptionTool;
import com.vimukti.accounter.utils.HibernateUtil;

public class PayPalIPINServlet extends BaseServlet {
	Logger log = Logger.getLogger(PayPalIPINServlet.class);
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
		log.info("--------------------- PAYPAL PAYMENT STARTED ------------------------");
		// read post from PayPal
		// system and add 'cmd'
		Enumeration en = req.getParameterNames();
		String verifyParam = "cmd=_notify-validate";
		Map<String, String> params = new HashMap<String, String>();
		while (en.hasMoreElements()) {
			String paramName = (String) en.nextElement();
			String paramValue = req.getParameter(paramName);
			params.put(paramName, paramValue);
			verifyParam = verifyParam + "&" + paramName + "="
					+ URLEncoder.encode(paramValue, "utf-8");
		}

		String paymentStatus = req.getParameter("payment_status");
		String emailId = req.getParameter("custom");
		String transactionID = req.getParameter("txn_id");
		String transactionType = req.getParameter("txn_type");
		log.info("Request Params #### PaymentStatus - " + paymentStatus
				+ ";Email ID - " + emailId + ";TransactionID - "
				+ transactionID + ";Transaction Type - " + transactionType);

		saveDetailsInDB(params, emailId);

		String verificationResp = verifyRequest(verifyParam);

		if (verificationResp.equals("VERIFIED")) {
			log.info("Paypal Request VERIFIED.");

			// Check paymentStatus=Completed
			// Check txnId has not been previously processed
			// Check receiverEmail is your Primary PayPal email
			// Check paymentAmount/paymentCurrency are correct

			if (emailId != null && transactionID != null) {
				if (!(paymentStatus.equals("Completed")
						|| paymentStatus.equals("Processed") || paymentStatus
							.equals("Pending"))) {
					log.info("Paypal not completed.");
					sendInfo("Your process is not completed", req, resp);
				} else {
					if (transactionType.equals("subscr_cancel")) {
						removeClientSubscription(emailId);
					} else {
						log.info("Payment completed with Status - "
								+ paymentStatus);
						upgradeClient(params, emailId);
					}
				}
			} else {
				log.info("emailId != null && txnId != null):" + emailId
						+ ",txnId:" + transactionID);
			}

		} else if (verificationResp.equals("INVALID")) {
			log.info("Paypal Request INVALID.");
		} else {
			log.info("Paypal Request ERROR.");
		}

		log.info("---------------------- PAYPAL PAYMENT ENDED -------------------------");
	}

	/**
	 * Verifies the Paypal Request
	 * 
	 * @param verifyParam
	 * @return
	 */
	private String verifyRequest(String verifyParam) {
		log.info("Verification Started.");
		String verificationResp = "VERIFIED";
		try {
			URL verifyUrl = null;
			if (ServerConfiguration.isSandBoxPaypal()) {
				verifyUrl = new URL(
						"https://www.sandbox.paypal.com/cgi-bin/webscr");
			} else {
				verifyUrl = new URL("https://www.paypal.com/cgi-bin/webscr");
			}
			log.info("Opening Paypal Verify Connection with URL " + verifyUrl);
			URLConnection verifyConnection = verifyUrl.openConnection();
			verifyConnection.setDoOutput(true);
			verifyConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			PrintWriter pw = new PrintWriter(verifyConnection.getOutputStream());
			log.info("Writting Params to 'Verify Connection' - " + verifyParam);
			pw.println(verifyParam);
			pw.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					verifyConnection.getInputStream()));
			verificationResp = in.readLine();
			in.close();

			log.info("Response of Paypal Verification - " + verificationResp);

		} catch (Exception e) {
			log.error("Exception while Verifying : ", e);
		}
		log.info("Verification Completed !!");
		return verificationResp;
	}

	private void removeClientSubscription(String emailId) {
		log.info("Removing Client Subscription for" + emailId);
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			Client client = getClient(emailId);
			client.getClientSubscription().setSubscription(
					Subscription.getInstance(Subscription.FREE_CLIENT));
			client.getClientSubscription().setPremiumType(0);
			session.saveOrUpdate(client);
			transaction.commit();
		} catch (Exception e) {
			log.error("Exception while Removing Subscription : ", e);
			transaction.rollback();
		}
		log.info("Removed Client Subscription !!");
	}

	/**
	 * Upgrades the Client to Subscription
	 * 
	 * @param params
	 * @param emailId
	 */
	private void upgradeClient(Map<String, String> params, String emailId) {
		log.info("Upgrading Client, Email - " + emailId + ". Params - "
				+ params);
		String type = params.get("option_selection1");
		log.info("Subscription type:" + type);
		int paymentType = 0;
		int durationType = 4;
		Date expiredDate = new Date();
		if (type.equals("One user monthly")) {
			paymentType = ClientSubscription.ONE_USER;
			durationType = ClientSubscription.MONTHLY_USER;
			expiredDate = getNextMonthDate(1);
		} else if (type.equals("One user yearly")) {
			paymentType = ClientSubscription.ONE_USER;
			durationType = ClientSubscription.YEARLY_USER;
			expiredDate = getNextMonthDate(12);
		} else if (type.equals("2 users monthly")) {
			paymentType = ClientSubscription.TWO_USERS;
			durationType = ClientSubscription.MONTHLY_USER;
			expiredDate = getNextMonthDate(1);
		} else if (type.equals("2 users yearly")) {
			paymentType = ClientSubscription.TWO_USERS;
			durationType = ClientSubscription.YEARLY_USER;
			expiredDate = getNextMonthDate(12);
		} else if (type.equals("5 users monthly")) {
			paymentType = ClientSubscription.FIVE_USERS;
			durationType = ClientSubscription.MONTHLY_USER;
			expiredDate = getNextMonthDate(1);
		} else if (type.equals("5 users yearly")) {
			paymentType = ClientSubscription.FIVE_USERS;
			durationType = ClientSubscription.YEARLY_USER;
			expiredDate = getNextMonthDate(12);
		} else if (type.equals("Unlimited Users monthly")) {
			paymentType = ClientSubscription.UNLIMITED_USERS;
			durationType = ClientSubscription.MONTHLY_USER;
			expiredDate = getNextMonthDate(1);
		} else if (type.equals("Unlimited Users yearly")) {
			paymentType = ClientSubscription.UNLIMITED_USERS;
			durationType = ClientSubscription.YEARLY_USER;
			expiredDate = getNextMonthDate(12);
		}
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		Client client = getClient(emailId);
		try {
			ClientSubscription clientSubscription = client
					.getClientSubscription();
			clientSubscription.getMembers().add(emailId);
			if (clientSubscription.getPremiumType() > paymentType) {
				clientSubscription.setGracePeriodDate(SubscryptionTool
						.getGracePeriodDate());
			} else {
				clientSubscription.setGracePeriodDate(null);
			}
			clientSubscription.setPremiumType(paymentType);
			clientSubscription.setDurationType(durationType);
			clientSubscription.setExpiredDate(expiredDate);
			clientSubscription.setSubscription(Subscription
					.getInstance(Subscription.PREMIUM_USER));

			session.saveOrUpdate(clientSubscription);
			transaction.commit();
		} catch (Exception e) {
			log.error("Exception While Upgrading the Client : " + e);
			transaction.rollback();
		}
		log.info("Client Upgraded Successfully !!");
		try {
			UsersMailSendar.sendMailToSubscribedUser(client);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Date getNextMonthDate(int months) {
		Calendar instance = Calendar.getInstance();
		instance.add(Calendar.MONTH, months);
		return instance.getTime();
	}

	/**
	 * Sends the Information to Paypal.
	 * 
	 * @param information
	 * @param req
	 * @param resp
	 */
	private void sendInfo(String information, HttpServletRequest req,
			HttpServletResponse resp) {
		log.info("Sending Info - " + information);
		req.setAttribute("info", information);
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

	/**
	 * Saves Request Details to Database.
	 * 
	 * @param params
	 * @param emailId
	 */
	private void saveDetailsInDB(Map<String, String> params, String emailId) {
		log.info("Saving Request with Parameters - " + params
				+ " And Email ID - " + emailId);
		Session session = HibernateUtil.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		try {
			ClientPaypalDetails details = new ClientPaypalDetails();
			details.setFirstname(params.get("first_name"));
			details.setLastname(params.get("last_name"));
			details.setAddressCountry(params.get("address_country"));
			details.setPayerEmail(params.get("payer_email"));
			String string = params.get("mc_gross");
			if (string != null) {
				details.setPaymentGross(Double.parseDouble(string));
			}
			details.setMcCurrency(params.get("mc_currency"));
			details.setPaymentStatus(params.get("payment_status"));
			details.setClinetEmailId(emailId);
			transaction.commit();
		} catch (Exception e) {
			log.error("Error While Saving Request : ", e);
			transaction.rollback();
		}
	}

}
