package com.vimukti.accounter.core;

import java.util.Date;

import org.json.JSONException;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class License implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Client client;

	private String serverId;

	private String organisation;

	private Date expiresOn;

	private String licenseText;

	private Date createdOn = new Date();

	private Date purchasedOn = new Date();

	private boolean isActive;

	private long id;

	private int version;

	/**
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * @return the serverId
	 */
	public String getServerId() {
		return serverId;
	}

	/**
	 * @param serverId
	 *            the serverId to set
	 */
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	/**
	 * @return the oranisationName
	 */
	public String getOrganisation() {
		return organisation;
	}

	/**
	 * @param organisationName
	 *            the oranisationName to set
	 */
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	/**
	 * @return the expiresOn
	 */
	public Date getExpiresOn() {
		return expiresOn;
	}

	/**
	 * @param expiresOn
	 *            the expiresOn to set
	 */
	public void setExpiresOn(Date expiresOn) {
		this.expiresOn = expiresOn;
	}

	/**
	 * @return the licenseText
	 */
	public String getLicenseText() {
		return licenseText;
	}

	/**
	 * @param licenseText
	 *            the licenseText to set
	 */
	public void setLicenseText(String licenseText) {
		this.licenseText = licenseText;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn
	 *            the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the purchasedOn
	 */
	public Date getPurchasedOn() {
		return purchasedOn;
	}

	/**
	 * @param purchasedOn
	 *            the purchasedOn to set
	 */
	public void setPurchasedOn(Date purchasedOn) {
		this.purchasedOn = purchasedOn;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub

	}

}
