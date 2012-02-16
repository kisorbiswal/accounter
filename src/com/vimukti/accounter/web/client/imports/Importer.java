package com.vimukti.accounter.web.client.imports;

import java.util.Map;

/**
 * @author Prasanna Kumar G
 * 
 * @param <T>
 */
public interface Importer<T> {

	enum Type {
		INVOICE, CUSTOMER, VENDOR
	}

	/**
	 * Returns the Type of the Importer
	 * 
	 * @return
	 */
	public Type getType();

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

}
