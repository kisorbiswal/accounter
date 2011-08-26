package com.vimukti.accounter.core;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.vimukti.accounter.web.client.exception.AccounterException;

public class ServerCompany implements IAccounterServerCore {
	private long id;
	private String companyName;
	private Date createdDate;
	private int companyType;
	private String serverAddress;
	private boolean isConfigured;
	private Set<Client> clients = new HashSet<Client>();
	private int version;
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getCompanyType() {
		return companyType;
	}

	public void setCompanyType(int companyType) {
		this.companyType = companyType;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * @return the isConfigured
	 */
	public boolean isConfigured() {
		return isConfigured;
	}

	/**
	 * @param isConfigured
	 *            the isConfigured to set
	 */
	public void setConfigured(boolean isConfigured) {
		this.isConfigured = isConfigured;
	}

	/**
	 * @return the clients
	 */
	public Set<Client> getClients() {
		return clients;
	}

	/**
	 * @param clients
	 *            the clients to set
	 */
	public void setClients(Set<Client> clients) {
		this.clients = clients;
	}

	public long getId() {
		return id;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version=version;
		
	}

}
