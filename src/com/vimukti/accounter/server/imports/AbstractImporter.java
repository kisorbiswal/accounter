package com.vimukti.accounter.server.imports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ImportField;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.imports.BooleanField;
import com.vimukti.accounter.web.client.imports.DoubleField;
import com.vimukti.accounter.web.client.imports.FinanceDateField;
import com.vimukti.accounter.web.client.imports.Integer2Field;
import com.vimukti.accounter.web.client.imports.LongField;
import com.vimukti.accounter.web.client.imports.StringField;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Prasanna Kumar G
 * 
 * @param <T>
 */
public abstract class AbstractImporter<T extends IAccounterCore> implements
		Importer<T> {

	private List<ImportField> fields = new ArrayList<ImportField>();

	private Map<String, String> importedData = new HashMap<String, String>();

	AccounterMessages messages = Global.get().messages();

	public AbstractImporter() {
		fields = getAllFields();
	}

	public abstract List<ImportField> getAllFields();

	protected ImportField getFieldByName(String fieldName) {
		for (ImportField field : getFields()) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		return null;
	}

	public void validate() throws AccounterException {
		for (ImportField field : getFields()) {
			if (!field.isValid()) {
				throw new AccounterException();
			}
		}
	}

	@Override
	public void loadData(Map<String, String> data) {
		for (ImportField field : fields) {
			String value = data.get(field.getColumnName());
			if (value != null) {
				field.validate(value);
			}
		}
	}

	protected String getString(String fieldName) {
		return ((StringField) getFieldByName(fieldName)).getValue();
	}

	protected ClientFinanceDate getFinanceDate(String fieldName) {
		return ((FinanceDateField) getFieldByName(fieldName)).getValue();
	}

	protected Long getLong(String fieldName) {
		return ((LongField) getFieldByName(fieldName)).getValue();
	}

	protected Double getDouble(String fieldName) {
		return ((DoubleField) getFieldByName(fieldName)).getValue();
	}

	protected Set<ClientAddress> getSetAddressList(String fieldName) {
		HashSet<ClientAddress> contactSet = new HashSet<ClientAddress>();
		contactSet.add(getAddresData());
		return contactSet;
	}

	protected Set<ClientContact> getSetContactList(String fieldName) {
		HashSet<ClientContact> contactSet = new HashSet<ClientContact>();
		contactSet.add(getContactData());
		return contactSet;
	}

	protected boolean getBoolean(String fieldName) {
		return ((BooleanField) getFieldByName(fieldName)).getValue();
	}

	protected int getInteger(String fieldName) {
		return ((Integer2Field) getFieldByName(fieldName)).getValue();
	}

	protected ClientQuantity getClientQty(String quantity) {
		ClientQuantity qty = new ClientQuantity();
		qty.setValue(getDouble(quantity));
		if (getLong("measurement") != null) {
			qty.setUnit(getLong("measurement"));
		} else if (getDouble(quantity) != null) {
			long defaultMeasurement = Accounter.getCompany()
					.getDefaultMeasurement();
			qty.setUnit(defaultMeasurement);
		}
		return qty;
	}

	protected long getPayeeByName(String fieldName) {
		// TODO
		return 0;
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(List<ImportField> fields) {
		this.fields = fields;
	}

	/**
	 * @return the fields
	 */
	public List<ImportField> getFields() {
		return fields;
	}

	@Override
	public void updateFields(Map<String, String> importMap) {
		for (ImportField field : fields) {
			String string = importMap.get(field.getName());
			field.setColumnName(string);
		}
	}

	protected ClientContact getContactData() {
		ClientContact contact = new ClientContact();
		contact.setName(getString(messages.contactName()));
		contact.setTitle(getString(messages.title()));
		contact.setBusinessPhone(getString(messages.businessPhone()));
		contact.setEmail(getString(messages.email()));
		return contact;
	}

	protected ClientAddress getAddresData() {
		ClientAddress address = new ClientAddress();
		address.setAddress1(getString(messages.streetAddress1()));
		address.setStreet(getString(messages.address2()));
		address.setCity(getString(messages.city()));
		address.setCountryOrRegion(getString(messages.countryRegion()));
		address.setStateOrProvinence(getString(messages.stateOrProvince()));
		return address;
	}

	protected long getCustomerByName(String customerName) {
		return 0;
	}

}
