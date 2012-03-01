package com.vimukti.accounter.core;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.utils.HibernateUtil;

public class Subscription implements IsSerializable {
	public static final int BEFORE_PAID_FETURE = 1;
	public static final int FREE_CLIENT = 2;
	public static final int PREMIUM_USER = 3;

	private long id;
	private Set<String> features = new HashSet<String>();
	private int type;

	public Subscription() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Set<String> getFeatures() {
		return features;
	}

	public void setFeatures(Set<String> features) {
		this.features = features;
	}

	public static int getStringToType(String type) {
		if (type.equals("One User Monthly Subscription")) {
			return 0;
		} else if (type.equals("One User Yearly Subscription")) {
			return 1;
		} else if (type.equals("Two Users Monthly Subscription")) {
			return 2;
		} else if (type.equals("Two Users Yearly Subscription")) {
			return 3;
		} else if (type.equals("Five Users Monthly Subscription")) {
			return 4;
		} else if (type.equals("Five Users Yearly Subscription")) {
			return 5;
		} else if (type.equals("Unlimited Users Monthly Subscription")) {
			return 6;
		} else if (type.equals("Unlimited Users Yearly Subscription")) {
			return 7;
		}
		return 1;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isPaidUser() {
		return type == PREMIUM_USER;
	}

	public static Subscription getInstance(int subscriptionType) {
		Session currentSession = HibernateUtil.getCurrentSession();
		Subscription object = (Subscription) currentSession
				.getNamedQuery("get.subscription")
				.setParameter("type", subscriptionType).uniqueResult();
		return object;
	}
}
