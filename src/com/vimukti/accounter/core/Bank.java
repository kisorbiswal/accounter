package com.vimukti.accounter.core;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.InvalidOperationException;

public class Bank implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8192082953624184711L;

	long id;

	String name;

	int version;

	public String stringID;

	transient boolean isImported;

	public Bank() {
	}

	public int getVersion() {
		return version;
	}

	// public void setVersion(int version) {
	// this.version = version;
	// }

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	// public void setId(long id) {
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
	public String getStringID() {
		// TODO Auto-generated method stub
		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;

	}

	@Override
	public void setImported(boolean isImported) {
		this.isImported = isImported;

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		Session session = HibernateUtil.getCurrentSession();
		Bank bank = (Bank) clientObject;
		Query query = session.createQuery(
				"from com.vimukti.accounter.core.Bank B where B.name=?")
				.setParameter(0, bank.name);
		List list = query.list();
		if (list != null && list.size() > 0) {
			Bank newBank = (Bank) list.get(0);
			if (bank.id != newBank.id) {
				throw new InvalidOperationException(
						"Bank name is alreay in use Please enter Unique name");

			}
		}

		return true;

	}

}
