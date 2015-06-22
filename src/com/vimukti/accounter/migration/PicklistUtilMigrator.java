package com.vimukti.accounter.migration;

public class PicklistUtilMigrator {
	static String depreciationForIdentity(int type) {
		switch (type) {
		case 1:
			return "All";
		case 2:
			return "Single";
		}
		return null;
	}

	static String depreciationMethodIdentity(int type) {
		switch (type) {
		case 1:
			return "StraightLine";
		case 2:
			return "ReducingBalance";
		}
		return null;
	}

	static String depreciationStatusIdentity(int type) {
		switch (type) {
		case 1:
			return "Approve";
		case 2:
			return "Rollback";
		}
		return null;
	}

	static String getAccountTypeIdentity(int type) {
		switch (type) {
		case 1:
			return "Cash";
		case 2:
			return "Bank";
		case 3:
			return "AccountReceivable";
		case 4:
			return "InventoryAsset";
		case 5:
			return "OtherCurrentAsset";
		case 6:
			return "FixedAsset";
		case 7:
			return "OtherAsset";
		case 8:
			return "AccountPayable";
		case 9:
			return "OtherCurrentLiability";
		case 10:
			return "CreditCard";
		case 11:
			return "OtherCurrentLiability";
		case 12:
			return "LongTermLiability";
		case 13:
			return "Equity";
		case 14:
			return "Income";
		case 15:
			return "CostOfGoodsSold";
		case 16:
			return "Expense";
		case 17:
			return "OtherIncome";
		case 18:
			return "OtherExpense";
		case 19:
			return "OtherCurrentLiability";
		case 20:
			return "OtherAsset";
		case 21:
			return "Paypal";
		}
		return null;
	}

	static String getFixedAssetStatusIdentifier(int i) {
		switch (i) {
		case 1:
			return "Pending";
		case 2:
			return "Register";
		case 3:
			return "Dispose";
		}
		return null;
	}
}
