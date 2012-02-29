package com.vimukti.accounter.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Subscription;
import com.vimukti.accounter.mail.UsersMailSendar;
import com.vimukti.accounter.utils.HibernateUtil;

public class SubscryptionTool extends Thread {
	@Override
	public void run() {
		Session session = HibernateUtil.openSession();
		try {
			List<Client> clients = getClients();
			for (Client c : clients) {
				if (isExpire(c)) {
					sendMail(c);
					doExpireSubscription(c);
				}
			}
		} catch (Exception e) {
		} finally {
			if (session.isOpen()) {
				session.close();
			}
		}
	}

	private void doExpireSubscription(Client c) {
		c.getClientSubscription().setSubscription(
				Subscription.getInstance(Subscription.FREE_CLIENT));
		HibernateUtil.getCurrentSession().saveOrUpdate(
				c.getClientSubscription());
		try {
			UsersMailSendar.sendMailToSubscriptionExpiredUser(c);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendMail(Client c) {
		// TODO Auto-generated method stub

	}

	private boolean isExpire(Client c) {
		Date expiredDate = c.getClientSubscription().getExpiredDate();
		return expiredDate == null ? false : expiredDate.before(new Date());
	}

	private List<Client> getClients() {
		Session session = HibernateUtil.getCurrentSession();
		List list = session.getNamedQuery("get.all.clients").list();
		return list;
	}

}
