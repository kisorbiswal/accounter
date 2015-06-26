package com.vimukti.accounter.migration;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Utility;

public class AccountMigrator implements IMigrator<Account> {

	String[] names = new String[] { "Account Receivable", "Account Payable",
			"Central Sales Tax Payable", "Sales Liability Account",
			"Tax Filed", "Opening Balances", "Exchange Loss or Gain",
			"Salaris Payable", "Inventory Assets", "Pending Item Receipts",
			"Rounding", "TDS Tax Payable", "Service Tax Payable",
			"Retained Earnings", "Owner a Share Capital",
			"Opening Balance Offset", "Ordinary Shares",
			"P&L Brought Forward/YTD", "Dividends", "Capitation Fees",
			"Fee for Service Income", "Nonmedical Income", "Refunds",
			"Discounts Given", "Other Cash Income", "Cash Discount taken",
			"Ask my Accountant", "Advertising & Promotion",
			"Automobile Expense", "Bank Service Charges",
			"Computer & Internet Expenses", "Continuing Education",
			"Depreciation Expenses", "Insurance expense", "Interest Expense",
			"Janitorial Expenses", "Laboratory Fees", "Meals & Entertainment",
			"Medical Records & Supplies", "Office Supplies",
			"Professoinal Fees", "Reference Materials", "Rent Expenses",
			"Saloon Supplies", " Linens", " Laundry", "Repairs & Maintenance",
			"Salaries and Wages", "Small Medical Equipment",
			"Telephone Expenses", "Travel Expenses", "Uniform",
			"Utilities Expenses", "Other Cash Expense", "Deferred Tax",
			"TDS Deducted by Others", "Vaccinnes & Medicines" };

	@Override
	public JSONObject migrate(Account account, MigratorContext context)
			throws JSONException {
		List<String> asList = Arrays.asList(names);
		if (asList.contains(account.getName())) {
			return null;
		}
		JSONObject jsonObject = new JSONObject();
		CommonFieldsMigrator.migrateCommonFields(account, jsonObject, context);
		jsonObject.put("name", account.getName());
		FinanceDate asOf = account.getAsOf();
		if (asOf != null) {
			jsonObject.put("asOf", asOf.getAsDateObject().getTime());
		} else {
			jsonObject.put("asOf", account.getCreatedDate().getTime());
		}
		if (account.getParent() != null) {
			jsonObject.put("subAccountOf",
					context.get("Account", account.getParent().getID()));
		}
		JSONObject accountTypeJSON = new JSONObject();
		accountTypeJSON.put("identity",
				PicklistUtilMigrator.getAccountTypeIdentity(account.getType()));
		jsonObject.put("type", accountTypeJSON);
		JSONObject currencyJson = new JSONObject();
		currencyJson.put("identity", account.getCurrency().getFormalName());
		jsonObject.put("currency", currencyJson);
		jsonObject.put("number", context.getNextAccountNumber());
		jsonObject.put("inactive", !account.getIsActive());
		jsonObject.put("description", account.getComment());
		jsonObject.put("openingBalance", account.getOpeningBalance());
		jsonObject.put("payPalEmail", account.getPaypalEmail());
		jsonObject.put("cashFlowCategory",
				Utility.getCashFlowCategoryName(account.getCashFlowCategory()));
		jsonObject.put("currencyFactor", account.getCurrencyFactor());
		jsonObject.put("isIncrease", account.isIncrease());
		return jsonObject;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		criteria.add(Restrictions.ne("type", 2));
	}
}