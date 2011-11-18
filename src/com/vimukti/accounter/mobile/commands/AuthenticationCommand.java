/**
 * 
 */
package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Activation;
import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.IMActivation;
import com.vimukti.accounter.core.IMUser;
import com.vimukti.accounter.core.MobileCookie;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.mobile.AccounterChatServer;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.RequirementType;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.UserCommand;
import com.vimukti.accounter.utils.HexUtil;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;
import com.vimukti.accounter.utils.Security;

/**
 * @author Prasanna Kumar G
 * 
 */
public class AuthenticationCommand extends Command {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
	}

	@Override
	public Result run(Context context) {
		int networkType = context.getNetworkType();
		// MOBILE
		if (networkType == AccounterChatServer.NETWORK_TYPE_MOBILE) {
			Result makeResult = context.makeResult();
			String string = context.getString();
			if (string.isEmpty()) {
				string = (String) context.getLast(RequirementType.STRING);
			}
			Object attribute = context.getAttribute("input");
			Client client = null;

			if (attribute == null || string == null) {
				if (string != null) {
					MobileCookie mobileCookie = getMobileCookie(string);
					if (mobileCookie != null) {
						client = mobileCookie.getClient();
						markDone();
						attribute = "finish";
					}
				}
				if (!isDone()) {
					context.setAttribute("input", "userName");
					makeResult.add("Please Enter Username. Or press Signup");
					CommandList commandList = new CommandList();
					commandList.add("Signup");
					makeResult.add(commandList);
					return makeResult;
				}
			}

			if (attribute.equals("activation")) {
				String userName = (String) context.getAttribute("userName");
				Session currentSession = HibernateUtil.getCurrentSession();
				Transaction beginTransaction = currentSession
						.beginTransaction();

				client = getClient(userName);
				client.setActive(true);
				beginTransaction.commit();
				markDone();
			}

			if (attribute.equals("userName")) {
				context.setAttribute("userName", string);
				client = getClient(string);
				if (client != null && !client.isActive()) {
					context.setAttribute("input", "activation");
					makeResult.add("Please Enter Activation Code");
					return makeResult;
				}
				context.setAttribute("input", "password");
				makeResult.add("Please Enter password");
				return makeResult;
			}

			if (attribute.equals("password")) {
				String userName = (String) context.getAttribute("userName");
				String password = HexUtil.bytesToHex(Security.makeHash(userName
						+ string));
				client = getClient(userName);
				if (client == null || !client.getPassword().equals(password)) {
					context.setAttribute("userName", null);
					makeResult
							.add("There is no account found with given Email Id and Password");
					makeResult
							.add("Please Enter valid Accounter Email Id. Or press Signup");
					CommandList commandList = new CommandList();
					commandList.add("Signup");
					makeResult.add(commandList);
					return makeResult;
				}
				String cookie = SecureUtils.createID(64);
				createMobileCookie(cookie, client);
				makeResult.setCookie(cookie);
				markDone();
			}

			if (isDone()) {
				makeResult.add("Your Successfully Logged.");
				makeResult.setNextCommand("Select Company");
				context.getIOSession().setClient(client);
				context.getIOSession().setAuthentication(true);
				return makeResult;
			}
		}

		// CHATTING AND CONSOLE
		IMUser imUser = getIMUser(context.getNetworkId(),
				context.getNetworkType());
		Result makeResult = context.makeResult();
		if (imUser == null) {
			IMActivation activation = getImActivationByTocken(context
					.getString());
			if (activation == null) {
				List<IMActivation> activationList = getImActivationByNetworkId(context
						.getNetworkId());
				if (activationList == null || activationList.size() == 0) {
					Client client = getClient(context.getNetworkId());
					if (client == null) {
						client = getClient(context.getString());
					}

					if (client != null) {
						sendActivationMail(context.getNetworkId(),
								client.getEmailId());
						makeResult
								.add("Activation code has been sent to your email Id. Enter Activation code");
					} else {
						if (!context.getString().isEmpty()) {
							makeResult
									.add("There is no account found with given Email Id");
						}
						makeResult
								.add("Enter valid Accounter Email Id. Or Signup");
						CommandList commandList = new CommandList();
						commandList.add("Signup");
						commandList.add(new UserCommand("Signup",
								"Signup with " + context.getNetworkId(),
								context.getNetworkId()));
						makeResult.add(commandList);
					}

				} else {
					if (!context.getString().isEmpty()) {
						makeResult.add("Wrong Activation code");
					}
					makeResult.add("Please Enter Activation code");

				}

			} else {
				imUser = createIMUser(context.getNetworkType(),
						activation.getNetworkId(),
						getClient(activation.getEmailId()));
				makeResult.add("Activation Success");
			}
		}
		if (imUser != null) {
			Client client = imUser.getClient();
			if (client.isActive()) {
				markDone();
			} else {
				if (context.getString().isEmpty()) {
					makeResult.add("Please Enter Activation code");
				} else {
					Activation activation = getActivation(context.getString());
					if (activation == null) {
						makeResult.add("Wrong activation code");
						makeResult.add("Please enter Activation code");
					} else {
						Session currentSession = HibernateUtil
								.getCurrentSession();
						Transaction beginTransaction = currentSession
								.beginTransaction();
						client.setActive(true);
						beginTransaction.commit();

						makeResult.add("Activation Success");
						markDone();
					}
				}
			}
		}
		if (isDone()) {
			makeResult.setNextCommand("Select Company");
			context.getIOSession().setClient(imUser.getClient());
			context.getIOSession().setAuthentication(true);
		}
		return makeResult;
	}

	private void createMobileCookie(String cookie, Client client) {
		Session session = HibernateUtil.getCurrentSession();
		Transaction beginTransaction = session.beginTransaction();
		MobileCookie mobileCookie = new MobileCookie();
		mobileCookie.setCookie(cookie);
		mobileCookie.setClient(client);
		session.save(mobileCookie);
		beginTransaction.commit();
	}

	private MobileCookie getMobileCookie(String string) {
		Session session = HibernateUtil.getCurrentSession();
		return (MobileCookie) session.get(MobileCookie.class, string);
	}

	private Activation getActivation(String string) {
		Session session = HibernateUtil.getCurrentSession();
		Activation val = (Activation) session
				.getNamedQuery("get.activation.by.token")
				.setString("token", string).uniqueResult();
		return val;
	}

	private IMUser createIMUser(int networkType, String networkId, Client client) {
		Session currentSession = HibernateUtil.getCurrentSession();
		IMUser imUser = new IMUser();
		imUser.setClient(client);
		imUser.setNetworkId(networkId);
		imUser.setNetworkType(networkType);
		Transaction beginTransaction = currentSession.beginTransaction();
		currentSession.save(imUser);
		List<IMActivation> imActivationByNetworkId = getImActivationByNetworkId(networkId);
		for (IMActivation activation : imActivationByNetworkId) {
			currentSession.delete(activation);
		}
		beginTransaction.commit();
		return imUser;
	}

	private void sendActivationMail(String networkId, String emailId) {
		String activationCode = SecureUtils.createID(16);
		System.out.println("NetWorkID: " + networkId);
		System.out.println("EmailId: " + emailId);
		System.out.println("Activation Code: " + activationCode);

		UsersMailSendar.sendMobileActivationMail(activationCode, emailId);

		Session currentSession = HibernateUtil.getCurrentSession();
		IMActivation activation = new IMActivation();
		activation.setEmailId(emailId);
		activation.setNetworkId(networkId);
		activation.setTocken(activationCode);
		Transaction beginTransaction = currentSession.beginTransaction();
		currentSession.save(activation);
		beginTransaction.commit();
	}

	private List<IMActivation> getImActivationByNetworkId(String networkId) {
		Session session = HibernateUtil.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<IMActivation> activationList = (List<IMActivation>) session
				.getNamedQuery("activation.by.networkId")
				.setString("networkId", networkId).list();
		return activationList;
	}

	private IMActivation getImActivationByTocken(String string) {
		Session session = HibernateUtil.getCurrentSession();
		IMActivation activation = (IMActivation) session
				.getNamedQuery("activation.by.tocken")
				.setString("tocken", string).uniqueResult();
		return activation;
	}

	private IMUser getIMUser(String networkId, int networkType) {
		Session session = HibernateUtil.getCurrentSession();
		IMUser user = (IMUser) session.getNamedQuery("imuser.by.networkId")
				.setString("networkId", networkId)
				.setInteger("networkType", networkType).uniqueResult();
		return user;
	}

	private Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter("emailId", emailId.toLowerCase());
		Client client = (Client) namedQuery.uniqueResult();
		return client;
	}
}
