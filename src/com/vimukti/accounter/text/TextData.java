package com.vimukti.accounter.text;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Quantity;

public class TextData implements ITextData {

	private String type;
	private String data;

	TextData(String type, String data) {
		this.type = type;
		this.data = data;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String nextString(String defVal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long nextNumber() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FinanceDate nextDate(FinanceDate defVal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Address nextAddress(Address defVal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Quantity nextQuantity(Quantity defVal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double nextDouble(double defVal) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDouble() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Double nextDouble() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isQuantity() {
		// TODO Auto-generated method stub
		return false;
	}

}
