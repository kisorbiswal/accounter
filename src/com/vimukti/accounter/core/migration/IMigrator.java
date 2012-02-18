package com.vimukti.accounter.core.migration;

import com.vimukti.accounter.core.Company;

/**
 * @author Prasanna Kumar G
 * 
 */
public interface IMigrator {

	/**
	 * Migrates the given company
	 * 
	 * @param company
	 */
	public void migrate(Company company);

	/**
	 * Returns the Migration Version
	 * 
	 * @return
	 */
	public int getVersion();
}
