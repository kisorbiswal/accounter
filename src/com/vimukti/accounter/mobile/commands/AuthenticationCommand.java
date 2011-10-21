/**
 * 
 */
package com.vimukti.accounter.mobile.commands;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.IMActivation;
import com.vimukti.accounter.core.IMUser;
import com.vimukti.accounter.mail.UsersMailSendar;
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
public class AuthenticationCommand extends AbstractCommand {

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
	}

	@Override
	public Result run(Context context) {

		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "");
		}

		IMUser imUser = getIMUser(context.getNetworkId(),
				context.getNetworkType());
		Result makeResult = context.makeResult();
		if (imUser == null) {
			Client client = getClient(context.getUserId());
			if (client == null) {
				IMActivation activation = getImActivationByTocken(context
						.getString());
				if (activation == null) {
					List<IMActivation> activationList = getImActivationByNetworkId(context
							.getNetworkId());
					if (activationList == null || activationList.size() == 0) {
						client = getClient(context.getString());
						if (client == null) {
							CommandList commandList = new CommandList();
							commandList.add("Signup");
							makeResult.add("Enter valid Accounter emailId");
							makeResult.add(commandList);
						}
					} else {
						makeResult.add("Wrong Activation code");
					}

				} else {
					imUser = createIMUser(context.getNetworkType(),
							activation.getNetworkId(),
							getClient(activation.getEmailId()));
					makeResult.add("Activation Success");
				}
			}
			if (client != null) {
				sendActivationMail(context.getNetworkId(), client.getEmailId());
				makeResult
						.add("Activation code has sent to your email Id. Enter Activation code");
			}
		}
		if (imUser != null) {
			context.getIOSession().setClient(imUser.getClient());
			markDone();
		}
		return makeResult;
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

	// private Result companyNameRequirement(Context context, IMUser imUser) {
	// Requirement companyReq = get(COMPANY_NAME);
	// Company selection = context.getSelection(COMPANY_NAME);
	// if (selection != null) {
	// companyReq.setValue(selection);
	// }
	// if (companyReq.isDone()) {
	// Company value = companyReq.getValue();
	// context.selectCompany(value, imUser.getClient());
	// markDone();
	// return null;
	// }
	//
	// Result result = context.makeResult();
	// result.add("Select a company");
	//
	// ResultList companyList = new ResultList(COMPANY_NAME);
	//
	// Set<User> users = imUser.getClient().getUsers();
	// List<Company> companies = new ArrayList<Company>();
	// for (User user : users) {
	// if (!user.isDeleted()) {
	// companies.add(user.getCompany());
	// }
	// }
	//
	// for (Company company : companies) {
	// Record record = new Record(company);
	// record.add("", company.getDisplayName());
	// record.add("", company.getCountry());
	// companyList.add(record);
	// }
	// result.add(companyList);
	// return result;
	// }

	private Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session.getNamedQuery("getClient.by.mailId");
		namedQuery.setParameter("emailId", emailId);
		Client client = (Client) namedQuery.uniqueResult();
		return client;
	}
}
