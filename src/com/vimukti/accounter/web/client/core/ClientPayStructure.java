package com.vimukti.accounter.web.client.core;

import java.util.List;

public class ClientPayStructure implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ClientEmployee employee;

	private ClientEmployeeGroup employeeGroup;

	private ClientFinanceDate effectiveFrom;

	private List<ClientPayStructureItem> items;

	public ClientEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(ClientEmployee employee) {
		this.employee = employee;
	}

	public ClientEmployeeGroup getEmployeeGroup() {
		return employeeGroup;
	}

	public void setEmployeeGroup(ClientEmployeeGroup employeeGroup) {
		this.employeeGroup = employeeGroup;
	}

	public ClientFinanceDate getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(ClientFinanceDate effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public List<ClientPayStructureItem> getItems() {
		return items;
	}

	public void setItems(List<ClientPayStructureItem> items) {
		this.items = items;
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
		// TODO Auto-generated method stub
		return null;
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
