package com.vimukti.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class CustomFieldValue extends CreatableObject implements
		IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String value;

	CustomField customField;

	Payee payee;

	private int version;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public CustomField getCustomField() {
		return customField;
	}

	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		return true;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public int getVersion() {

		return this.version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;

	}

	@Override
	public boolean onSave(Session s) throws CallbackException {

		return true;
	}

	@Override
	public boolean onUpdate(Session s) throws CallbackException {

		return true;
	}

	@Override
	public boolean onDelete(Session s) throws CallbackException {

		return true;
	}

	@Override
	public void onLoad(Session s, Serializable id) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getID() {

		return this.id;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setId(long id) {

		this.id = id;
	}

	public Payee getPayee() {
		return payee;
	}

	public void setPayee(Payee payee) {
		this.payee = payee;
	}

}
