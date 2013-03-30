package com.vimukti.accounter.text;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Quantity;

public interface ITextData {

	public String nextString();

	public Long nextNumber();

	public FinanceDate nextDate();

	public Address nextAddress();

	public Quantity nextQuantity();

	public boolean isDate();

	public Double nextDouble();
}
