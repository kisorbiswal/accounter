package com.vimukti.accounter.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.ClientSubscription;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.PortletConfiguration;
import com.vimukti.accounter.core.PortletPageConfiguration;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.main.ServerLocal;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.portlet.PortletFactory;
import com.vimukti.accounter.web.client.portlet.PortletPage;

public class SubscryptionTool extends Thread {
	Logger log = Logger.getLogger(SubscryptionTool.class);

	public SubscryptionTool() {
		super("Subsciprion Tool");
	}

	public void run() {
		log.info("Started checking of subscriptions");
		ServerLocal.set(Locale.ENGLISH);
		Session session = HibernateUtil.openSession();
		try {
			Transaction transaction = session.beginTransaction();
			List<Client> clients = getClients();
			for (Client c : clients) {
				ClientSubscription subscription = c.getClientSubscription();
				if (subscription.isTracePeriodExpired()) {
					doExpireSubscription(c);
					continue;
				}
				if (subscription.isExpired()) {
					Date tracePeriodDate = subscription.getGracePeriodDate();
					if (tracePeriodDate == null) {
						subscription.setSubscription(Subscription
								.getInstance(Subscription.FREE_CLIENT));
						subscription.setPremiumType(0);
						subscription.setDurationType(0);
						subscription.setGracePeriodDate(getGracePeriodDate());
					}
				}
			}
			transaction.commit();
			log.info("Completed checking of subscriptions");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	public static Date getGracePeriodDate() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, ServerConfiguration.getGracePeriod());
		return c.getTime();
	}

	private void doExpireSubscription(Client c) throws AccounterException {
		ClientSubscription subscription = c.getClientSubscription();
		subscription.setGracePeriodDate(null);
		subscription.setExpiredDate(null);
		Set<String> members = subscription.getMembers();
		int premiumType = subscription.getPremiumType();
		Set<String> deletedMembers = getDeletedMembers(members, c.getEmailId(),
				premiumType);
		deleteUsers(deletedMembers, c);
		members.removeAll(deletedMembers);
		subscription.setMembers(members);
		if (subscription.getSubscription().getType() == Subscription.FREE_CLIENT) {

			List<Company> companies = getAllCompaniesByClient(c);
			List<String> defPortlets = new ArrayList<String>();
			defPortlets.add(PortletFactory.BANKING);
			defPortlets.add(PortletFactory.EXPENSES_CLAIM);
			defPortlets.add(PortletFactory.MONEY_COMING);
			defPortlets.add(PortletFactory.MONEY_GOING);

			for (Company company : companies) {
				CompanyPreferences preferences = company.getPreferences();
				preferences.setInventoryEnabled(false);
				preferences.setEnableMultiCurrency(false);
				preferences.setSalesOrderEnabled(false);
				preferences.setPurchaseOrderEnabled(false);

				preferences.setClassTrackingEnabled(false);// CLASS
				preferences.setLocationTrackingEnabled(false);// LOCATION
				preferences
						.setProductandSerivesTrackingByCustomerEnabled(false);// BILLABLE_EXPENSE
				preferences.setBillableExpsesEnbldForProductandServices(false);// BILLABLE_EXPENSE
				preferences.setDelayedchargesEnabled(false);// CREDITS_CHARGES

				Set<User> users = company.getUsers();
				for (User user : users) {
					Set<PortletPageConfiguration> portletPages = user
							.getPortletPages();
					for (PortletPageConfiguration pg : portletPages) {
						if (pg.getPageName().equals(PortletPage.DASHBOARD)) {
							List<PortletConfiguration> def = new ArrayList<PortletConfiguration>();
							List<PortletConfiguration> portlets = pg
									.getPortlets();

							for (PortletConfiguration pc : portlets) {
								if (defPortlets.contains(pc.getPortletName())) {
									def.add(pc);
									if (def.size() == defPortlets.size()) {
										break;
									}
								}
							}
							pg.setPortlets(def);
							break;
						}
					}
					HibernateUtil.getCurrentSession().saveOrUpdate(user);
				}
				HibernateUtil.getCurrentSession().saveOrUpdate(company);
			}
		}
		HibernateUtil.getCurrentSession().saveOrUpdate(
				c.getClientSubscription());
		log.info("Complted..:" + c.getEmailId());
		try {
			UsersMailSendar.sendMailToSubscriptionExpiredUser(c);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private List<Company> getAllCompaniesByClient(Client c) {
		Session session = HibernateUtil.getCurrentSession();
		Query namedQuery = session
				.getNamedQuery("get.client.created.companies");
		((SQLQuery) namedQuery).addEntity(Company.class);
		List<Company> list = namedQuery.setParameter("clientId", c.getID())
				.list();
		return list;
	}

	public static Set<String> getDeletedMembers(Set<String> members,
			String emailId, int premiumType) {
		Set<String> deletedMems = new HashSet<String>();

		List<String> newMembers = new ArrayList<String>(members);
		newMembers.remove(emailId);
		int noOfUsers = 0;
		switch (premiumType) {
		case ClientSubscription.ONE_USER:
			noOfUsers = 1;
			break;
		case ClientSubscription.TWO_USERS:
			noOfUsers = 2;
			break;
		case ClientSubscription.FIVE_USERS:
			noOfUsers = 5;
			break;
		default:
			break;
		}
		noOfUsers = newMembers.size() - noOfUsers + 1;
		for (int i = 0; i < noOfUsers && i < newMembers.size(); i++) {
			deletedMems.add(newMembers.get(i));
		}
		return deletedMems;

	}

	private void deleteUsers(Set<String> members, Client client)
			throws AccounterException {
		for (String s : members) {
			log.info("deleteUser:" + s);
			deleteUser(client, s);
		}
	}

	public static void deleteUser(Client client, String emailId)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getUserIds.invited.by.client");

		((SQLQuery) query).addEntity(User.class);

		List<User> users = (List<User>) query.setParameter("emailId", emailId)
				.setParameter("clientId", client.getID()).list();
		for (User user : users) {
			user.setDeleted(true);
			session.saveOrUpdate(user);
		}
	}

	private List<Client> getClients() {
		Session session = HibernateUtil.getCurrentSession();
		List list = session.getNamedQuery("get.all.clients").list();
		return list;
	}

}
