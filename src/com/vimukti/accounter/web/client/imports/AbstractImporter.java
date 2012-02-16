package com.vimukti.accounter.web.client.imports;

import java.util.List;

/**
 * @author Prasanna Kumar G
 * 
 * @param <T>
 */
public abstract class AbstractImporter<T> implements Importer<T> {

	@Override
	public T getImportedData() {
		// TODO
		return null;
	}

	protected abstract List<Field<?>> getFields();

	protected Field<?> getFieldByName(String fieldName) {
		for (Field<?> field : getFields()) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		return null;
	}

}
