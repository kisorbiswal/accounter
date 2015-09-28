package com.vimukti.accounter.migration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.vimukti.accounter.core.Company;

public class MigratorContext {

	private Map<String, Long> ids = new HashMap<String, Long>();

	private PickListTypeContext pickListTypeContext;
	private Company company;
	private Map<String, List<Long>> childrenMap;
	private int lastAccNumber = 1055;
	private int taxAgencyNo = 1;
	private TransactionMigrationContext currentTrasMigrationContext;
	// CompanySetting type for present transactions
	// (with tax,without discount etc)
	private String companySettingsType;
	private long contactNo = 1L;

	public void put(String name, Map<Long, Long> migrateAccounts) {
		for (Entry<Long, Long> oldId : migrateAccounts.entrySet()) {
			ids.put(name + oldId.getKey(), oldId.getValue());
		}
	}

	public void put(String name, Long oldId, Long newID) {
		ids.put(name + oldId, newID);
	}

	public Long get(String name, long id) {
		return ids.get(name + id);
	}

	public PickListTypeContext getPickListContext() {
		return pickListTypeContext;
	}

	public void setPickListContext(PickListTypeContext pickListTypeContext) {
		this.pickListTypeContext = pickListTypeContext;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public void setAdmin(String user, long userId) {
		ids.put(user, userId);
	}

	public Long getAdmin() {
		return ids.get("Admin");
	}

	public void setChildrenMap(Map<String, List<Long>> childrenMap) {
		this.childrenMap = childrenMap;
	}

	public Map<String, List<Long>> getChildrenMap() {
		return childrenMap;
	}

	public int getNextAccountNumber() {
		return ++lastAccNumber;
	}

	public String getNextTaxAgencyNumber() {
		return String.valueOf(taxAgencyNo++);
	}

	public TransactionMigrationContext getCurrentTrasMigrationContext() {
		return currentTrasMigrationContext;
	}

	public void setCurrentTrasMigrationContext(
			TransactionMigrationContext currentTrasMigrationContext) {
		this.currentTrasMigrationContext = currentTrasMigrationContext;
	}

	public String getCompanySettingsType() {
		return companySettingsType;
	}

	public void setCompanySettingsType(String companySettingsType) {
		this.companySettingsType = companySettingsType;
	}

	public long getNextContactNo() {
		return contactNo++;
	}

}
