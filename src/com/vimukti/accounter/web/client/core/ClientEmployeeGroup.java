package com.vimukti.accounter.web.client.core;

import java.util.List;

public class ClientEmployeeGroup implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private List<ClientEmployee> employees;

	private ClientPayStructure payStructure;

	public List<ClientEmployee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<ClientEmployee> employees) {
		this.employees = employees;
	}

	public ClientPayStructure getPayStructure() {
		return payStructure;
	}

	public void setPayStructure(ClientPayStructure payStructure) {
		this.payStructure = payStructure;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setID(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}

}
