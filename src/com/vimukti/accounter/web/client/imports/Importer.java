package com.vimukti.accounter.web.client.imports;

import java.util.Map;

import com.vimukti.accounter.web.client.core.IAccounterCore;

/**
 * @author Prasanna Kumar G
 * 
 * @param <T>
 */
public interface Importer<T extends IAccounterCore> {

	/**
	 * Returns Output Object that is Generated after Matching
	 * 
	 * @return
	 */
	public T getData();

	/**
	 * Returns Data that is imported from File
	 * 
	 * @return
	 */
	public Map<String, String> getImportedData();

	/**
	 * 
	 * Loads the Data(ColumnName,ColumnValue) from File
	 * 
	 * @param data
	 */
	public void loadData(Map<String, String> data);

}
