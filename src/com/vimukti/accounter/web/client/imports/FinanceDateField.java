package com.vimukti.accounter.web.client.imports;

import java.util.Date;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ImportField;

public class FinanceDateField extends ImportField {

	private ClientFinanceDate financeDate;

	public FinanceDateField() {
		super();
	}

	public FinanceDateField(String name, String displayName) {
		super(name, displayName);
	}

	public FinanceDateField(String name, String displayName, boolean isRequired) {
		super(name, displayName, isRequired);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ClientFinanceDate getValue() {
		return financeDate;
	}

	public void setValue(ClientFinanceDate value) {
		this.financeDate = value;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean validate(String value) {
		try {
			Date date = new Date(value);
			setValue(new ClientFinanceDate(date));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public String getValueAsString() {
		if (getValue() == null) {
			return null;
		}
		return getValue().toString();
	}

}
