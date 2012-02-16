package com.vimukti.accounter.web.client.imports;

/**
 * @author Prasanna Kumar G
 * 
 * @param <T>
 */
public interface Importer<T> {

	enum Type {
		INVOICE, CUSTOMER, VENDOR
	}

	public Type getType();

	public T getImportedData();
}
