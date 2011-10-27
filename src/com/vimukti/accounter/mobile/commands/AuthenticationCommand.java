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
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.mobile.Command;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.utils.SecureUtils;

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
					Client client = getClient(context.getUserId());
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
									.add("There is no account found with given Email Id.");
						}
						makeResult.add("Enter valid Accounter Email Id");
						CommandList commandList = new CommandList();
						commandList.add("Signup");
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
			CommandList commandList = new CommandList();
			makeResult.setNextCommand("Select Company");
			makeResult.add(commandList);
			context.getIOSession().setClient(imUser.getClient());
			context.getIOSession().setAuthentication(true);
		}
		return makeResult;
	}

	private Activation getActivation(String string) {
		Session session = HibernateUtil.getCurrentSession();
		if (session != null) {
			Activation val = (Activation) session
					.getNamedQuery("get.activation.by.token")
					.setString("token", string).uniqueResult();
			return val;

		}
		return null;
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
		namedQuery.setParameter("emailId", emailId);
		Client client = (Client) namedQuery.uniqueResult();
		return client;
	}
}
