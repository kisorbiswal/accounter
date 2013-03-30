package com.vimukti.accounter.text;

import java.util.Date;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Quantity;

public interface ITextData {

	public String nextString();

	public Long nextNumber();

	public Date nextDate();

	public Address nextAddress();

	public Quantity nextQuantity();
}
