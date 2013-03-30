package com.vimukti.accounter.text;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Quantity;

public interface ITextData {

	public String nextString(String defVal);

	public Long nextNumber();

	public FinanceDate nextDate(FinanceDate defVal);

	public Address nextAddress(Address defVal);

	public Quantity nextQuantity();

	public boolean isDate();

	public double nextDouble(double defVal);

	public boolean hasNext();

	public boolean isDouble();
}
