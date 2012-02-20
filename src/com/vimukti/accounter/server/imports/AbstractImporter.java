package com.vimukti.accounter.server.imports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.imports.Field;

/**
 * @author Prasanna Kumar G
 * 
 * @param <T>
 */
public abstract class AbstractImporter<T extends IAccounterCore> implements
		Importer<T> {

	private List<Field<?>> fields = new ArrayList<Field<?>>();

	private Map<String, String> importedData = new HashMap<String, String>();

	AccounterMessages messages = Global.get().messages();

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

	protected String getString(String fieldName) {
		return (String) getFieldByName(fieldName).getValue();
	}

	protected ClientFinanceDate getFinanceDate(String fieldName) {
		return (ClientFinanceDate) getFieldByName(fieldName).getValue();
	}

	protected Long getLong(String fieldName) {
		return (Long) getFieldByName(fieldName).getValue();
	}

	protected Double getDouble(String fieldName) {
		return (Double) getFieldByName(fieldName).getValue();
	}

	protected Set<ClientAddress> getSetAddressList(String fieldName) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Set<ClientContact> getSetContactList(String fieldName) {
		// TODO Auto-generated method stub
		return null;
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
