package com.vimukti.accounter.core;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class Bank implements IAccounterServerCore, INamedObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8192082953624184711L;

	long id;

	String name;

	int version;

	public Bank() {
	}

	public int getVersion() {
		return version;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	// public void setID(long id){
	// this.id = id;
	// }
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	// public void setName(String name) {
	// this.name = name;
	// }
	@Override
	public String toString() {

		return this.name;
	}

	public boolean equals(Bank bank) {

		if (bank.id == this.id && this.version == bank.version
				&& this.name.equals(bank.name))
			return true;
		return false;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Bank bank = (Bank) clientObject;
		Query query = session.getNamedQuery("getNameofBank.from.Bank")
				.setParameter(0, bank.name);
		List list = query.list();
		if (list != null && list.size() > 0) {
			Bank newBank = (Bank) list.get(0);
			if (bank.id != newBank.id) {
				throw new AccounterException(
						AccounterException.ERROR_NAME_CONFLICT);
				// "Bank name is alreay in use Please enter Unique name");

			}
		}

		return true;

	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}
