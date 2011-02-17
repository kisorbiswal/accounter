package com.vimukti.accounter.core;

public class BalanceSheetReport {

	int transactionType;

	double amount;

	boolean flag;

	int baseType;

	int subBaseType;

	int status;

	String accountName;

	public static final int BASETYPE_ASSET = 1;
	public static final int BASETYPE_LIABILITY = 2;
	public static final int BASETYPE_EQUITY = 3;

	// public static final int BASETYPE_ORDINARY_INCOME_OR_EXPENSE = 4;
	// public static final int BASETYPE_OTHER_INCOME_OR_EXPENSE = 5;

	public static final int SUBBASETYPE_CURRENT_ASSET = 1;
	public static final int SUBBASETYPE_FIXED_ASSET = 2;
	public static final int SUBBASETYPE_OTHER_ASSET = 3;
	public static final int SUBBASETYPE_CURRENT_LIABILITY = 4;
	public static final int SUBBASETYPE_LONG_TERM_LIABILITY = 5;

	// public static final int GROUPTYPE_CASH = 1;

}
