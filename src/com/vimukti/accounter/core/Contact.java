package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.vimukti.accounter.web.client.InvalidOperationException;

@SuppressWarnings("serial")
public class Contact implements IAccounterServerCore, Lifecycle {

	/**
	 * This will specify which Contact is Primary among all the Contacts of a
	 * {@link Payee}
	 */
	boolean isPrimary = false;
	/**
	 * This is Contact Name
	 */
	String name = "";
	/**
	 * This is Contact Title
	 */
	String title = "";
	/**
	 * This is Contact Business Phone Number.
	 */
	String businessPhone = "";
	/**
	 * This is Contact Email Address
	 */
	String email = "";

	transient boolean isImported;
	transient private boolean isOnSaveProccessed;

	public Contact() {

	}

	/**
	 * @return the isPrimary
	 */
	public boolean isPrimary() {
		return isPrimary;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the businessPhone
	 */
	public String getBusinessPhone() {
		return businessPhone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean onDelete(Session s) throws CallbackException {
		return false;
	}

	@Override
	public void onLoad(Session s, Serializable id) {

	}

	@Override
	public boolean onSave(Session s) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		this.isOnSaveProccessed = true;
		return false;
	}

	@Override
	public boolean onUpdate(Session s) throws CallbackException {
		return false;
	}

	@Override
	public long getID(){

		return null;
	}

	@Override
	public void setID(long id){

	}

	@Override
	public void setImported(boolean isImported) {
		this.isImported = isImported;

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws InvalidOperationException {
		// TODO Auto-generated method stub
		return true;
	}

}
