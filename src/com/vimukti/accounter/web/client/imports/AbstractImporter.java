package com.vimukti.accounter.web.client.imports;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar G
 * 
 * @param <T>
 */
public abstract class AbstractImporter<T extends IAccounterCore> implements
		Importer<T> {

	private List<Field<?>> fields = new ArrayList<Field<?>>();

	public AbstractImporter() {
		fields = getAllFields();
	}

	public abstract List<Field<?>> getAllFields();

	protected Field<?> getFieldByName(String fieldName) {
		for (Field<?> field : getFields()) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		return null;
	}

	public void validate() throws AccounterException {
		for (Field<?> field : getFields()) {
			if (!field.isValid()) {
				throw new AccounterException();
			}
		}
	}

	@Override
	public void loadData(Map<String, String> data) {
		for (Field<?> field : fields) {
			String value = data.get(field.getColumnName());
			field.validate(value);
		}
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(List<Field<?>> fields) {
		this.fields = fields;
	}

	/**
	 * @return the fields
	 */
	public List<Field<?>> getFields() {
		return fields;
	}

	@Override
	public void updateFields(Map<String, String> importMap) {
		for (Field<?> field : fields) {
			String string = importMap.get(field.getName());
			field.setColumnName(string);
		}
	}

	protected long getCustomerByName(String customerName) {
		return 0;
	}

}
