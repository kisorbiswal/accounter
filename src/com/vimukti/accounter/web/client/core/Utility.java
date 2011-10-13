package com.vimukti.accounter.web.client.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Calendar;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.util.ChangeType;
import com.vimukti.accounter.web.client.util.CoreEvent;

public class Utility implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int version;
	static boolean isDelete;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public static String getTransactionName(int transactionType) {

		String transactionName = null;
		switch (transactionType) {
		case 0:
			transactionName = AccounterClientConstants.MEMO_OPENING_BALANCE;
			break;
		case ClientTransaction.TYPE_CASH_SALES:
			transactionName = AccounterClientConstants.TYPE_CASH_SALES;
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
			transactionName = AccounterClientConstants.TYPE_CASH_PURCHASE;
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			transactionName = AccounterClientConstants.TYPE_CREDIT_CARD_CHARGE;
			break;
		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			transactionName = AccounterClientConstants.TYPE_CUSTOMER_CREDIT_MEMO;
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			transactionName = AccounterClientConstants.TYPE_CUSTOMER_REFUNDS;
			break;
		case ClientTransaction.TYPE_ENTER_BILL:
			transactionName = AccounterClientConstants.TYPE_ENTER_BILL;
			break;
		case ClientTransaction.TYPE_ESTIMATE:
			transactionName = AccounterClientConstants.TYPE_ESTIMATE;
			break;
		case ClientTransaction.TYPE_INVOICE:
			transactionName = AccounterClientConstants.TYPE_INVOICE;
			break;
		case ClientTransaction.TYPE_ISSUE_PAYMENT:
			transactionName = AccounterClientConstants.TYPE_ISSUE_PAYMENT;
			break;
		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			transactionName = AccounterClientConstants.TYPE_MAKE_DEPOSIT;
			break;
		case ClientTransaction.TYPE_PAY_BILL:
			transactionName = AccounterClientConstants.TYPE_PAY_BILL;
			break;
		case ClientTransaction.TYPE_VENDOR_PAYMENT:
			transactionName = AccounterClientConstants.TYPE_VENDOR_PAYMENT;
			break;
		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
			transactionName = AccounterClientConstants.TYPE_RECEIVE_PAYMENT;
			break;
		case ClientTransaction.TYPE_TRANSFER_FUND:
			transactionName = AccounterClientConstants.TYPE_TRANSFER_FUND;
			break;
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			transactionName = AccounterClientConstants.TYPE_VENDOR_CREDIT_MEMO;
			break;
		case ClientTransaction.TYPE_WRITE_CHECK:
			transactionName = AccounterClientConstants.TYPE_WRITE_CHECK;
			break;
		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			transactionName = AccounterClientConstants.TYPE_JOURNAL_ENTRY;
			break;
		case ClientTransaction.TYPE_PAY_SALES_TAX:
			transactionName = AccounterClientConstants.TYPE_PAY_SALES_TAX;
			break;
		case ClientTransaction.TYPE_RECEIVE_VAT:
			transactionName = AccounterClientConstants.TYPE_RECEIVE_VAT;
			break;
		case ClientTransaction.TYPE_SALES_ORDER:
			transactionName = AccounterClientConstants.TYPE_SALES_ORDER;
			break;
		case ClientTransaction.TYPE_PURCHASE_ORDER:
			transactionName = AccounterClientConstants.TYPE_PURCHASE_ORDER;
			break;
		case ClientTransaction.TYPE_ITEM_RECEIPT:
			transactionName = AccounterClientConstants.TYPE_ITEM_RECEIPT;
			break;
		case ClientTransaction.TYPE_CASH_EXPENSE:
			transactionName = AccounterClientConstants.TYPE_CASH_EXPENSE;
			break;
		case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
			transactionName = AccounterClientConstants.TYPE_EMPLOYEE_EXPENSE;
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			transactionName = AccounterClientConstants.TYPE_CREDIT_CARD_EXPENSE;
			break;
		case ClientTransaction.TYPE_VAT_RETURN:
			transactionName = AccounterClientConstants.TYPE_VAT_RETURN;
			break;
		case ClientTransaction.TYPE_PAY_VAT:
			transactionName = AccounterClientConstants.TYPE_PAY_VAT;
			break;
		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
			transactionName = AccounterClientConstants.TYPE_CUSTOMER_PREPAYMENT;
		}
		return transactionName;
	}

	public static String getItemType(int itemType) {
		String itemTypeName = null;
		switch (itemType) {
		case ClientTransactionItem.TYPE_ITEM:
			itemTypeName = Accounter.constants().productOrServiceItem();
			break;
		case ClientTransactionItem.TYPE_ACCOUNT:
			itemTypeName = Global.get().Account();
			break;
		default:
			break;
		}
		return itemTypeName;
	}

	public static String getAccountTypeString(int accountType) {

		String accountTypeName = null;
		switch (accountType) {
		case ClientAccount.TYPE_INCOME:
			accountTypeName = AccounterClientConstants.TYPE_INCOME;
			break;
		case ClientAccount.TYPE_OTHER_INCOME:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_INCOME;
			break;
		case ClientAccount.TYPE_EXPENSE:
			accountTypeName = AccounterClientConstants.TYPE_EXPENSE;
			break;
		case ClientAccount.TYPE_OTHER_EXPENSE:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_EXPENSE;
			break;
		case ClientAccount.TYPE_COST_OF_GOODS_SOLD:
			accountTypeName = AccounterClientConstants.TYPE_COST_OF_GOODS_SOLD;
			break;
		case ClientAccount.TYPE_CASH:
			accountTypeName = AccounterClientConstants.TYPE_CASH;
			break;
		case ClientAccount.TYPE_BANK:
			accountTypeName = AccounterClientConstants.TYPE_BANK;
			break;
		case ClientAccount.TYPE_OTHER_CURRENT_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_CURRENT_ASSET;
			break;
		case ClientAccount.TYPE_INVENTORY_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_INVENTORY_ASSET;
			break;
		case ClientAccount.TYPE_OTHER_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_ASSET;
			break;
		case ClientAccount.TYPE_FIXED_ASSET:
			accountTypeName = AccounterClientConstants.TYPE_FIXED_ASSET;
			break;
		case ClientAccount.TYPE_CREDIT_CARD:
			accountTypeName = AccounterClientConstants.TYPE_CREDIT_CARD;
			break;
		case ClientAccount.TYPE_PAYPAL:
			accountTypeName = AccounterClientConstants.TYPE_PAYPAL;
			break;
		case ClientAccount.TYPE_PAYROLL_LIABILITY:
			accountTypeName = AccounterClientConstants.TYPE_PAYROLL_LIABILITY;
			break;
		case ClientAccount.TYPE_OTHER_CURRENT_LIABILITY:
			accountTypeName = AccounterClientConstants.TYPE_OTHER_CURRENT_LIABILITY;
			break;
		case ClientAccount.TYPE_LONG_TERM_LIABILITY:
			accountTypeName = AccounterClientConstants.TYPE_LONG_TERM_LIABILITY;
			break;
		case ClientAccount.TYPE_EQUITY:
			accountTypeName = AccounterClientConstants.TYPE_EQUITY;
			break;
		case ClientAccount.TYPE_ACCOUNT_RECEIVABLE:
			accountTypeName = AccounterClientConstants.TYPE_ACCOUNT_RECEIVABLE;
			break;
		case ClientAccount.TYPE_ACCOUNT_PAYABLE:
			accountTypeName = AccounterClientConstants.TYPE_ACCOUNT_PAYABLE;
			break;

		}
		return accountTypeName;
	}

	public static String getCashFlowCategoryName(int type) {

		String CashFlowCategoryName = null;
		switch (type) {
		case ClientAccount.CASH_FLOW_CATEGORY_FINANCING:
			CashFlowCategoryName = AccounterClientConstants.CASH_FLOW_CATEGORY_FINANCING;
			break;
		case ClientAccount.CASH_FLOW_CATEGORY_INVESTING:
			CashFlowCategoryName = AccounterClientConstants.CASH_FLOW_CATEGORY_INVESTING;
			break;
		case ClientAccount.CASH_FLOW_CATEGORY_OPERATING:
			CashFlowCategoryName = AccounterClientConstants.CASH_FLOW_CATEGORY_OPERATING;
			break;
		}
		return CashFlowCategoryName;
	}

	public static String getBankAccountType(int type) {

		String bankAccountName = null;
		switch (type) {
		case ClientAccount.BANK_ACCCOUNT_TYPE_NONE:
			bankAccountName = AccounterClientConstants.BANK_ACCCOUNT_TYPE_NONE;
			break;
		case ClientAccount.BANK_ACCCOUNT_TYPE_CHECKING:
			bankAccountName = AccounterClientConstants.BANK_ACCCOUNT_TYPE_CHECKING;
			break;
		case ClientAccount.BANK_ACCCOUNT_TYPE_MONEY_MARKET:
			bankAccountName = AccounterClientConstants.BANK_ACCCOUNT_TYPE_MONEY_MARKET;
			break;
		case ClientAccount.BANK_ACCCOUNT_TYPE_SAVING:
			bankAccountName = AccounterClientConstants.BANK_ACCCOUNT_TYPE_SAVING;
			break;
		}
		return bankAccountName;
	}

	public static String getHierarchy(ClientAccount account) {

		if (account == null)
			return "";

		return getHierarchy(Accounter.getCompany().getAccount(
				account.getParent()))
				+ account.getName() + " > ";
	}

	public static ClientFinanceDate getCalculatedDiscountDate(
			ClientFinanceDate transactionDate, ClientPaymentTerms paymentTerm) {

		// try {
		//
		// Date discountDate = new Date();
		// discountDate = transactionDate;
		//
		// if (paymentTerm != null) {
		//
		// int ifPaidWithIn = 0;
		// if (paymentTerm.getIfPaidWithIn() > 0)
		// ifPaidWithIn = paymentTerm.getIfPaidWithIn();
		// Calendar cal = Calendar.getInstance();
		// cal.setTime(discountDate);
		//
		// cal.add(Calendar.DAY_OF_MONTH, ifPaidWithIn);
		//
		// discountDate = cal.getTime();
		// return discountDate;
		// } else
		// throw (new DAOException(DAOException.INVALID_REQUEST_EXCEPTION,
		// null));
		// } catch (DataAccessException e) {
		// throw (new DAOException(DAOException.DATABASE_EXCEPTION, e));
		// }
		return transactionDate;
	}

	// public static String getCalculatedDiscountDate(String transactionDate,
	// PaymentTerms paymentTerm){
	//
	// String discountDate = "";
	// if (paymentTerm != null) {
	//
	// int ifPaidWithIn = 0;
	// if (paymentTerm.getIfPaidWithIn() > 0)
	// ifPaidWithIn = paymentTerm.getIfPaidWithIn();
	//
	// discountDate = calculateDiscountDate(transactionDate, ifPaidWithIn);
	// }
	//
	// }

	// private final static native String calculateDiscountDate(String
	// transactionDate,
	// int ifPaidWithIn) /*-{
	//
	// Date discountDate = new Date(transactionDate);
	// discountDate = discountDate+ifPaidWithIn;
	// $wnd.alert(" discountDate:"+discountDate);
	// return "";
	//
	//
	//
	//
	// }-*/;

	public static ClientFinanceDate getPaymentTermsDate(
			ClientPaymentTerms paymentTerm) {
		ClientFinanceDate dueDate = new ClientFinanceDate(new Date());

		int dueDays = paymentTerm.getDueDays();
		int day = dueDate.getDay();
		int month = dueDate.getMonth();
		int year = dueDate.getYear();

		int month_days;
		if (dueDays == 0) {
			return new ClientFinanceDate(new Date());
		} else {

			if (month == 1 || month == 3 || month == 5 || month == 7
					|| month == 8 || month == 10 || month == 12) {
				month_days = 31;
				day += dueDays;
				if (day > month_days) {
					do {

						day = day - month_days;
						month += 1;
						month_days = getNumberOfDays(month, year);
						if (month > 12) {
							year += 1;
							month = 1;
						}
					} while (day > month_days);

				}
			} else if (month == 4 || month == 6 || month == 9 || month == 11) {
				month_days = 30;
				day += dueDays;
				if (day > month_days) {
					do {
						day = day - month_days;
						month += 1;
						month_days = getNumberOfDays(month, year);
						if (month > 12) {
							year += 1;
							month = 1;
						}
					} while (day > month_days);

				}
			} else if (month == 2) {
				int no_of_days;
				if (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)) {
					month_days = 29;

				} else {
					month_days = 28;
				}
				day += dueDays;
				if (day > month_days) {
					do {

						day = day - month_days;
						month += 1;
						month_days = getNumberOfDays(month, year);
						if (month > 12) {
							year += 1;
							month = 1;
						}
					} while (day > month_days);

				}
			}
			System.err.println("mon" + month + "day" + day + "year" + year);

			dueDate = new ClientFinanceDate();
			dueDate.setDay(day - 1);
			dueDate.setMonth(month);
			dueDate.setYear(year);
			return dueDate;
		}

	}

	private static int getNumberOfDays(int month, int year) {

		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			return 31;
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		} else if (month == 2
				&& (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0))) {
			return 29;

		} else {
			return 28;
		}

	}

	public static ClientFinanceDate getCalculatedDueDate(
			ClientFinanceDate transactionDate, ClientPaymentTerms paymentTerm) {
		try {

			ClientFinanceDate dueDate = new ClientFinanceDate();
			dueDate = transactionDate;

			if (paymentTerm != null) {

				int due = 0;
				int dueDays = 0;
				int month = 0;
				due = paymentTerm.getDue();
				if (paymentTerm.getDueDays() > 0)
					dueDays = (Integer) paymentTerm.getDueDays();
				Calendar cal = Calendar.getInstance();
				cal.setTime(dueDate.getDateAsObject());

				cal.add(Calendar.DAY_OF_MONTH, dueDays);
				// switch (due) {
				// case ClientPaymentTerms.DUE_NONE:
				// if (dueDays > 0) {
				//
				// cal.add(Calendar.DAY_OF_MONTH, dueDays);
				// }
				// break;
				// case ClientPaymentTerms.DUE_CURRENT_MONTH:
				// cal.set(Calendar.DAY_OF_MONTH, 01);
				// cal.add(Calendar.MONTH, 1);
				// cal.set(Calendar.DAY_OF_MONTH, dueDays);
				// break;
				// case ClientPaymentTerms.DUE_CURRENT_QUARTER:
				// cal.set(Calendar.DAY_OF_MONTH, 01);
				// month = cal.get(Calendar.MONTH);
				// month++;
				//
				// if (month == 1 || month == 2 || month == 3) {
				// cal.set(Calendar.MONTH, 3);
				// cal.set(Calendar.DAY_OF_MONTH, dueDays);
				// } else if (month == 4 || month == 5 || month == 6) {
				// cal.set(Calendar.MONTH, 6);
				// cal.set(Calendar.DAY_OF_MONTH, dueDays);
				//
				// } else if (month == 7 || month == 8 || month == 9) {
				// cal.set(Calendar.MONTH, 9);
				// cal.set(Calendar.DAY_OF_MONTH, dueDays);
				// } else {
				// cal.add(Calendar.YEAR, 1);
				// cal.set(Calendar.DAY_OF_MONTH, dueDays);
				// }
				// break;
				// case ClientPaymentTerms.DUE_CURRENT_HALF_YEAR:
				// cal.set(Calendar.DAY_OF_MONTH, 01);
				// if (month == 1 || month == 2 || month == 3 || month == 4
				// || month == 5 || month == 6) {
				// cal.set(Calendar.MONTH, 7);
				// cal.set(Calendar.DAY_OF_MONTH, dueDays);
				// } else {
				// cal.add(Calendar.YEAR, 1);
				// cal.set(Calendar.DAY_OF_MONTH, dueDays);
				// }
				// break;
				// case ClientPaymentTerms.DUE_CURRENT_YEAR:
				// cal.set(Calendar.DAY_OF_MONTH, 01);
				// cal.add(Calendar.YEAR, 1);
				// cal.set(Calendar.DAY_OF_MONTH, dueDays);
				// break;
				//
				// }
				dueDate = new ClientFinanceDate(cal.getTime());
				return dueDate;
			} else
				throw (new Exception());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionDate;
	}

	public static double getCalculatedSalesTax(
			ClientFinanceDate transactionDate, double taxableLineTotal,
			ClientTAXItemGroup taxItemGroup) {

		// Query query = session.createSQLQuery(new
		// StringBuilder().append(
		// "SELECT SUM(TR.RATE) FROM TAXRATES TR JOIN TAXGROUP_TAXCODE TGTC ON TR.TAXCODE_ID = TGTC.TAXCODE_ID JOIN TAXGROUP TG ON TGTC.TAXGROUP_ID = TG.ID JOIN TAXCODE TC ON TC.ID=TR.TAXCODE_ID WHERE TG.ID = "
		// ).append(taxGroupID).append(
		// " AND TR.AS_OF IN (SELECT MAX(TR1.AS_OF) FROM TAXRATES TR1 WHERE TR1.ID IN (SELECT (TR2.ID) FROM TAXRATES TR2 JOIN TAXGROUP_TAXCODE TGTC ON TR2.TAXCODE_ID = TGTC.TAXCODE_ID JOIN TAXGROUP TG ON TGTC.TAXGROUP_ID = TG.ID) AND TR1.AS_OF <= '"
		// ).append(transactionDate).append(
		// "' GROUP BY TR1.TAXCODE_ID) and TR.COMPANY_ID ="
		// ).append(company.getID()).toString());

		Double salesTaxAmount = 0D;
		Double calculatedTaxRate = 0D;

		if (taxItemGroup == null || taxItemGroup.equals("None")) {
			return salesTaxAmount;
		}

		if (taxItemGroup instanceof ClientTAXItem)
			calculatedTaxRate += ((ClientTAXItem) taxItemGroup).getTaxRate();
		if (taxItemGroup instanceof ClientTAXGroup)
			calculatedTaxRate += ((ClientTAXGroup) taxItemGroup).getGroupRate();

		if (DecimalUtil.isGreaterThan(calculatedTaxRate, 0.0)) {
			salesTaxAmount = (taxableLineTotal * calculatedTaxRate) / 100;
		}

		return salesTaxAmount;
	}

	// @SuppressWarnings( { "unchecked", "unused" })
	// private static Double getLatestTaxRate(ClientTaxCode taxCode,
	// ClientFinanceDate transactionDate) {
	//
	// Set<ClientTaxRates> taxRates = taxCode.getTaxRates();
	// int numberOfTaxRates = taxRates.size();
	// Iterator taxRateIterator = taxRates.iterator();
	// ClientFinanceDate latestDate = null;
	//
	// ClientFinanceDate taxDate[] = new ClientFinanceDate[numberOfTaxRates];
	// int index = 0;
	// while (taxRateIterator.hasNext()) {
	// ClientTaxRates taxRate = (ClientTaxRates) taxRateIterator.next();
	// taxDate[index] = new ClientFinanceDate(taxRate.getAsOf());
	// index++;
	// }
	// latestDate = taxDate[0];
	// for (ClientFinanceDate date : taxDate) {
	// if (date.after(latestDate)) {
	// latestDate = date;
	// }
	//
	// }
	// taxRateIterator = taxRates.iterator();
	// while (taxRateIterator.hasNext()) {
	// ClientTaxRates taxRate = (ClientTaxRates) taxRateIterator.next();
	// if (taxRate == null)
	// continue;
	//
	// ClientFinanceDate getAsOf = new ClientFinanceDate(taxRate.getAsOf());
	//
	// if (getAsOf == null)
	// continue;
	//
	// if (latestDate.equals(getAsOf)) {
	// return taxRate.getRate();
	// }
	// }
	// return null;

	// Double rate=null;
	//
	// if(taxCode.getTaxRates()!=null) {
	// Set <TaxRates> taxrates=taxCode.getTaxRates();
	// Iterator<TaxRates> i3=taxrates.iterator();
	//
	// Date date;
	// if(i3.hasNext()) {
	// TaxRates tr=(TaxRates) i3.next();
	// date=(Date)tr.getAsOf();
	// rate=tr.getRate();
	//
	// while(i3.hasNext()) {
	//
	// if(date.after(tr.getAsOf())) {
	// date=tr.getAsOf();
	// rate=tr.getRate();
	// }
	// tr=(TaxRates)i3.next();
	// }
	// }
	// }
	// return rate;
	// }

	// To display the PayFrom Account combo box of Creating Customer Refund,
	// PayBill, Vendor Payment, Cash Purchase, Credit Card Charge,Write Check
	// and Issue Payment

	// public static List<ClientAccount> getPayFromAccounts(ClientCompany
	// company) {
	//
	// List<ClientAccount> payFromAccounts = new ArrayList<ClientAccount>();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// if (account.type == ClientAccount.TYPE_CASH
	// || account.type == ClientAccount.TYPE_BANK
	// || account.type == ClientAccount.TYPE_CREDIT_CARD
	// || account.type == ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
	// || account.type == ClientAccount.TYPE_LONG_TERM_LIABILITY) {
	// payFromAccounts.add(account);
	// }
	// }
	// return payFromAccounts;
	//
	// }
	//
	// // To display the DepositIn Account combo box of Creating Cash Sale,
	// // ReceivePayment
	// public static List<ClientAccount> getDepositInAccounts(ClientCompany
	// company) {
	//
	// List<ClientAccount> depositInAccounts = new ArrayList();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// if (account.type == ClientAccount.TYPE_CASH
	// || account.type == ClientAccount.TYPE_BANK
	// || account.type == ClientAccount.TYPE_CREDIT_CARD) {
	// depositInAccounts.add(account);
	// }
	// }
	// return depositInAccounts;
	//
	// }
	//
	// // To Display the Account Combo box of Transaction Item Lines.
	// public static List<ClientAccount> getGridAccounts(ClientCompany company)
	// {
	// List<ClientAccount> gridAccounts = new ArrayList();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// if (account.type != ClientAccount.TYPE_CASH
	// && account.type != ClientAccount.TYPE_BANK
	// && account.type != ClientAccount.TYPE_INVENTORY_ASSET
	// && account.type != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
	// && account.type != ClientAccount.TYPE_ACCOUNT_PAYABLE) {
	// gridAccounts.add(account);
	// }
	// }
	// return gridAccounts;
	// }
	//
	// public static List<ClientAccount> getCashBackAccounts(ClientCompany
	// company) {
	// List<ClientAccount> cashBackAccounts = new ArrayList();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// if (account.type != ClientAccount.TYPE_INVENTORY_ASSET
	// && account.type != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
	// && account.type != ClientAccount.TYPE_ACCOUNT_PAYABLE) {
	// cashBackAccounts.add(account);
	// }
	// }
	// return cashBackAccounts;
	// }
	//
	// public static List<ClientAccount> getIncomeAndExpenseAccounts(
	// ClientCompany company) {
	// List<ClientAccount> incomeAndExpenseAccounts = new ArrayList();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// if (account.type != ClientAccount.TYPE_INVENTORY_ASSET
	// && account.type != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
	// && account.type != ClientAccount.TYPE_ACCOUNT_PAYABLE) {
	// incomeAndExpenseAccounts.add(account);
	// }
	// }
	// return incomeAndExpenseAccounts;
	// }
	//
	// // To display the Bank Account combo box of Creating Make Deposit
	// public static List<ClientAccount> getBankAccounts(ClientCompany company)
	// {
	//
	// List<ClientAccount> bankAccounts = new ArrayList();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// if (account.type == ClientAccount.TYPE_BANK) {
	// bankAccounts.add(account);
	// }
	// }
	// return bankAccounts;
	//
	// }
	//
	// public static List<ClientAccount> getBankingAccountSummary(
	// ClientCompany company) {
	//
	// List<ClientAccount> bankingAccountSummary = new ArrayList();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// if (account.type == ClientAccount.TYPE_BANK
	// && (account.bankAccountType == ClientAccount.BANK_ACCCOUNT_TYPE_CHECKING
	// || account.bankAccountType ==
	// ClientAccount.BANK_ACCCOUNT_TYPE_MONEY_MARKET || account.bankAccountType
	// == ClientAccount.BANK_ACCCOUNT_TYPE_SAVING)) {
	// bankingAccountSummary.add(account);
	// }
	// }
	// return bankingAccountSummary;
	//
	// }

	// public static ClientAccount getAccount(ClientCompany company, Long id) {
	//
	// ClientAccount account = null;
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// account = (ClientAccount) iterator.next();
	//
	// if (account.id == id) {
	// return account;
	// }
	// }
	// return null;
	//
	// }
	//
	// public static List<ClientAccount> getAccounts(ClientCompany company,
	// int type) {
	//
	// List<ClientAccount> accounts = new ArrayList();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// if (account.type == type) {
	// accounts.add(account);
	// }
	// }
	//
	// return accounts;
	//
	// }
	//
	// public static List<ClientAccount> getAccounts(ClientCompany company) {
	//
	// List<ClientAccount> accounts = new ArrayList();
	//
	// Iterator iterator = company.getAccounts().iterator();
	//
	// while (iterator.hasNext()) {
	// ClientAccount account = (ClientAccount) iterator.next();
	// accounts.add(account);
	// }
	// return accounts;
	//
	// }

	public static double getCalculatedItemUnitPriceForPriceLevel(
			ClientItem item, ClientPriceLevel priceLevel,
			boolean isVendorTransaction) {

		double amt;

		if (item == null)
			return 0.00D;

		if (isVendorTransaction) {

			amt = item.getPurchasePrice();

		} else {
			amt = item.getSalesPrice();
		}

		if (priceLevel == null)
			return amt;

		try {

			double percentage = priceLevel.getPercentage()
					* (priceLevel.isPriceLevelDecreaseByThisPercentage() ? -1
							: 1);

			amt += amt * (percentage / 100);

			return amt;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return amt;
	}

	public static String getNumberInWords(Object object) {

		// int n = 1234;
		// int s = 0;
		// int r = 0;
		// while(n>0){
		// r = n%10;
		// s = (s*10)+r;
		// n = n/10;
		// }
		// System.out.println(s);

		String numberInWords = "";
		String lhs = "";
		String rhs = "";
		String subStringArray[];
		// Double amountEntered = number;
		String amount = object.toString();
		// StringTokenizer amt = new StringTokenizer(amount, ".");
		int i = 0;
		char c[] = new char[amount.length()];
		amount.getChars(0, amount.length(), c, 0);
		Boolean isDecimal = Boolean.FALSE;
		for (i = 0; i < c.length; i++) {
			if (c[i] == '.') {
				isDecimal = Boolean.TRUE;
				break;
			}
		}
		// i-=1;

		if (!isDecimal) {
			lhs = amount;
		}
		if (i < amount.length() && isDecimal) {

			lhs = amount.substring(0, i);
			rhs = amount.substring(i + 1, amount.length());
			if (rhs.length() >= 2)
				rhs = rhs.substring(0, 2);

		}
		// String amount = "123456789123456.89745";

		// StringTokenizer amt = new StringTokenizer(amount, ".");

		// if (amt.countTokens() == 1){
		// lhs = amount;
		// }
		// if (amt.countTokens() >= 2){
		// if (amt.hasMoreTokens()) {
		// lhs = amt.nextToken();
		// rhs = amt.nextToken();
		// rhs = rhs.substring(0, 2);
		//
		// }
		// }
		int length = lhs.length();
		int subStrCount = (length % 3 == 0 ? length / 3 : (length / 3) + 1);
		subStringArray = new String[subStrCount];
		i = 0;
		int j = 0;
		for (i = length - 1, j = subStrCount - 1; i > 0 && j > 0; i -= 3, j--) {
			subStringArray[j] = lhs.substring(i - 2, i + 1);

		}
		subStringArray[j] = lhs.substring(0, i + 1);
		String placeValue = "";

		// String faceValue = "";
		//
		// String subPlaceValue = "";
		//
		// String subFaceValue = "";
		if (subStringArray.length > 1) {
			for (i = 0; i < subStringArray.length; i++) {
				String subString = subStringArray[i];
				if (subString.equals("000"))
					continue;
				placeValue = getPlaceValue(subStringArray.length - i);
				numberInWords += readNumber(subString);
				numberInWords += " ";
				numberInWords += placeValue;
				numberInWords += " ";

			}
		} else {
			String subString = subStringArray[0];
			numberInWords += readNumber(subString);
		}
		if (!rhs.equals(""))
			numberInWords = new StringBuilder().append(numberInWords)
					.append(" and ").append(rhs).append("/100 DOLLARS")
					.toString();
		System.out.println(numberInWords);

		return numberInWords;

	}

	public static String getPlaceValue(int i) {

		String pos = "";
		if (i > 0) {

			switch (i) {
			// case 1 : pos = "HUNDRED"; break;
			case 2:
				pos = "THOUSAND";
				break;
			case 3:
				pos = "MILLION";
				break;
			case 4:
				pos = "BILLION";
				break;
			case 5:
				pos = "TRILLION";
				break;
			}
		}
		return pos;

	}

	public static String readNumber(String subString) {

		String faceValue = "";
		String numberInWords = "";

		int length = subString.length();
		Boolean mid = false;

		if (length == 0)
			return "";
		if (length >= 1) {
			for (int i = 0; i < length; i++) {
				Object object[] = getFaceValue(subString.substring(i, length),
						mid);
				faceValue = object[0].toString();
				if (subString.substring(i, length).length() > 2) {
					object = getFaceValue("" + subString.charAt(i), mid);
					numberInWords += " " + object[0].toString() + " "
							+ faceValue;

				} else
					numberInWords += " " + faceValue;
				mid = (Boolean) object[1];
				// System.out.println(numberInWords);

			}
		}
		return numberInWords;

	}

	public static Object[] getFaceValue(String lhs, Boolean mid) {

		String start = "";
		int length = 0;
		if (lhs != null) {
			length = lhs.length();
		}
		if (length == 1 && !mid) {
			char c = lhs.charAt(0);
			switch (c) {
			case '0':
				start = "ZERO";
				break;
			case '1':
				start = "ONE";
				break;
			case '2':
				start = "TWO";
				break;
			case '3':
				start = "THREE";
				break;
			case '4':
				start = "FOUR";
				break;
			case '5':
				start = "FIVE";
				break;
			case '6':
				start = "SIX";
				break;
			case '7':
				start = "SEVEN";
				break;
			case '8':
				start = "EIGHT";
				break;
			case '9':
				start = "NINE";
				break;
			}
			return new Object[] { start, mid };
		}
		if (length > 1) {
			if (length == 2) {
				String modLhs = "";
				if (Integer.parseInt(lhs) >= 10 && Integer.parseInt(lhs) < 20) {
					modLhs = lhs;
					mid = true;
				} else {
					Integer it = Integer.parseInt(lhs)
							- ((Integer.parseInt(lhs)) % 10);
					modLhs = it.toString();
				}
				if (modLhs.equals("0")) {
					mid = true;
					return new Object[] { start, mid };
				}

				if (modLhs.equals("10"))
					start = "TEN";
				else if (modLhs.equals("11"))
					start = "ELEVEN";
				else if (modLhs.equals("12"))
					start = "TWELVE";
				else if (modLhs.equals("13"))
					start = "THIRTEEN";
				else if (modLhs.equals("14"))
					start = "TOURTEEN";
				else if (modLhs.equals("15"))
					start = "TIFTEEN";
				else if (modLhs.equals("16"))
					start = "TIXTEEN";
				else if (modLhs.equals("17"))
					start = "SEVENTEEN";
				else if (modLhs.equals("18"))
					start = "EIGHTEEN";
				else if (modLhs.equals("19"))
					start = "NINETEEN";

				else if (modLhs.equals("20"))
					start = "TWENTY -";
				else if (modLhs.equals("30"))
					start = "THIRTY -";
				else if (modLhs.equals("40"))
					start = "FORTY -";
				else if (modLhs.equals("50"))
					start = "FIFTY -";
				else if (modLhs.equals("60"))
					start = "SIXTY -";
				else if (modLhs.equals("70"))
					start = "SEVENTY -";
				else if (modLhs.equals("80"))
					start = "EIGHTY -";
				else if (modLhs.equals("90"))
					start = "NINTY -";

				return new Object[] { start, mid };
			} else if (length > 2) {

				switch (length) {
				case 3:
					start = "HUNDRED";
					break;
				}
				return new Object[] { start, mid };

			}
		}

		return new Object[] { "", mid };
	}

	public static String getStatus(int transactionType, int status) {
		StringBuffer buffer = new StringBuffer();
		String statusString = getTransactionStatus(transactionType, status);

		if (statusString.trim().length() > 1) {
			buffer.append(statusString);
		} else
			buffer.append(statusString);

		return buffer.toString();
	}

	public static String getTransactionStatus(int transactionType, int status) {

		StringBuffer buffer = new StringBuffer();

		switch (transactionType) {

		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_UN_APPLIED);
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(AccounterClientConstants.STATUS_PARTIALLY_APPLIED);
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_APPLIED);
				break;
			}
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_NOT_ISSUED);
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_ISSUED);
				break;
			}
			break;
		case ClientTransaction.TYPE_ENTER_BILL:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_NOT_PAID);
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(AccounterClientConstants.STATUS_PARTIALLY_PAID);
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_PAID);
				break;
			}
			break;
		case ClientTransaction.TYPE_ESTIMATE:
			switch (status) {
			case ClientEstimate.STATUS_ACCECPTED:
				buffer.append(AccounterClientConstants.STATUS_ACCEPTED);
				break;
			case ClientEstimate.STATUS_REJECTED:
				buffer.append(AccounterClientConstants.STATUS_REJECTED);
				break;
			case ClientEstimate.STATUS_OPEN:
				buffer.append(AccounterClientConstants.STATUS_OPEN);
				break;

			}
			break;
		case ClientTransaction.TYPE_INVOICE:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_NOT_PAID);
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(AccounterClientConstants.STATUS_PARTIALLY_PAID);
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_PAID);
				break;
			}
			break;

		case ClientTransaction.TYPE_PAY_BILL:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_NOT_ISSUED);
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_ISSUED);
				break;
			}
			break;
		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_UN_APPLIED);
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(AccounterClientConstants.STATUS_PARTIALLY_APPLIED);
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_APPLIED);
				break;
			}
			break;

		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_UN_APPLIED);
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(AccounterClientConstants.STATUS_PARTIALLY_APPLIED);
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_APPLIED);
				break;
			}
			break;

		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_NEW);
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_POSTED);
				break;
			}
			break;
		case ClientTransaction.TYPE_CASH_SALES:
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_NOT_ISSUED);
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_ISSUED);
				break;
			}
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_NOT_ISSUED);
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_ISSUED);
				break;
			}
			break;
		case ClientTransaction.TYPE_ISSUE_PAYMENT:
			break;
		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			break;
		case ClientTransaction.TYPE_TRANSFER_FUND:
			break;
		case ClientTransaction.TYPE_SALES_ORDER:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_NOT_INVOICED);
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(AccounterClientConstants.STATUS_PARTIALLY_INVOICED);
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_INVOICED);
				break;
			}
			break;
		case ClientTransaction.TYPE_PURCHASE_ORDER:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_NOT_RECEIVED);
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				buffer.append(AccounterClientConstants.STATUS_PARTIALLY_RECEIVED);
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_RECEIVED);
				break;
			}
			break;
		case ClientTransaction.TYPE_WRITE_CHECK:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_NOT_ISSUED);
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_ISSUED);
				break;
			}
			break;
		case ClientTransaction.TYPE_PAY_SALES_TAX:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_NOT_ISSUED);
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_ISSUED);
				break;
			}
			break;
		case ClientTransaction.TYPE_PAY_VAT:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_NOT_ISSUED);
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_ISSUED);
				break;
			}
			break;

		case ClientTransaction.TYPE_RECEIVE_VAT:
			switch (status) {
			case ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_NOT_ISSUED);
				break;
			case ClientTransaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED:
				break;
			case ClientTransaction.STATUS_PAID_OR_APPLIED_OR_ISSUED:
				buffer.append(AccounterClientConstants.STATUS_ISSUED);
				break;
			}
			break;
		}

		return buffer.toString();

	}

	public static String dateToString(ClientFinanceDate date) {
		try {
			DateTimeFormat dateFormatter = DateTimeFormat
					.getFormat("yyyy-MM-dd");
			String format = dateFormatter.format(date.getDateAsObject());
			return format;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T extends IAccounterCore> long getID(T source) {

		return source != null ? source.getID() : 0;

	}

	public static long getTime(ClientFinanceDate date) {

		return date != null ? date.getDate() : 0l;
	}

	public static <T extends IAccounterCore> void updateClientList(
			T objectInList, List<T> objectsList) {

		if (objectInList == null || objectsList == null)
			return;
		boolean change = false;
		T existObj = getObject(objectsList, objectInList.getID());
		if (existObj != null) {
			objectsList.remove(existObj);
			if (isDelete)
				return;
			change = true;
		}
		if (objectInList.getName() != null)
			UIUtils.updateComboItemsInSortedOrder(objectInList, objectsList);
		else {
			objectsList.add(objectInList);
		}
		if (change) {
			Accounter.getEventBus().fireEvent(
					new CoreEvent<T>(ChangeType.CHANGE, objectInList));
		} else {
			Accounter.getEventBus().fireEvent(
					new CoreEvent<T>(ChangeType.ADD, objectInList));
		}
	}

	public static <T> ArrayList<T> filteredList(ListFilter<T> filter,
			List<T> objectsList) {
		ArrayList<T> filteredList = new ArrayList<T>();
		for (T obj : objectsList) {
			if (filter.filter(obj)) {
				filteredList.add(obj);
			}
		}

		return filteredList;
	}

	public static <S extends IAccounterCore> S getObject(List<S> list, long id) {
		if (list == null)
			return null;
		for (S s : list) {
			if (s != null && s.getID() == id) {
				return s;
			}
		}
		return null;
	}

	public static <S extends IsSerializable> S getObjectFromList(List<S> list,
			long id) {
		if (list == null)
			return null;
		for (S s : list) {
			if (s != null && ((IAccounterCore) s).getID() != id) {
				return s;
			}
		}
		return null;
	}

	public static <S extends IAccounterCore> S getObjectByName(List<S> list,
			String name) {
		if (list == null)
			return null;
		for (S s : list)
			if (s.getName().equalsIgnoreCase(name))
				return s;
		return null;
	}

	public static <S extends IAccounterCore> boolean isNumberCorrect(
			IAccounterCore iAccounterCore) {
		try {
			if (iAccounterCore instanceof ClientAccount) {
				if (checkIfNotNumber(((ClientAccount) iAccounterCore)
						.getNumber())) {
					throw new InvalidEntryException(
							"A Account Number shouble be a number");
				}
				if (Integer.parseInt(((ClientAccount) iAccounterCore)
						.getNumber()) < 1) {
					throw new InvalidEntryException(
							"A Account Number shouble be positive");

				}
			} else if (iAccounterCore instanceof ClientVendor) {
				if (((ClientVendor) iAccounterCore).getVendorNumber()
						.equals("")) {
					((ClientVendor) iAccounterCore).setVendorNumber(String
							.valueOf(0));
				}
				if (checkIfNotNumber(((ClientVendor) iAccounterCore)
						.getVendorNumber())) {
					throw new InvalidEntryException(
							"A Supplier Account Number shouble be a number");
				}
				if (Integer.parseInt(((ClientVendor) iAccounterCore)
						.getVendorNumber()) < 1) {
					throw new InvalidEntryException(
							"A Supplier Account Number shouble be positive");
				}

			}
		} catch (InvalidEntryException e) {
			return true;
		}
		return false;
	}

	public static <S extends IAccounterCore> boolean isObjectExist(
			List<S> list, String name) {
		if (list == null || list.isEmpty())
			return false;
		for (S s : list) {
			if (s.getName() != null
					&& s.getName().toLowerCase().equals(name.toLowerCase())) {
				return true;
			}

		}
		return false;
	}

	public static boolean checkIfNotNumber(String in) {
		try {
			Integer.parseInt(in);

		} catch (NumberFormatException ex) {
			return true;
		}
		return false;
	}

	public static <S extends IAccounterCore> S getObject(Set<S> set, long id) {
		if (set == null)
			return null;

		for (S s : set)
			if (s.getID() == id)
				return s;
		return null;
	}

	public static String getItemTypeText(ClientItem item) {

		String itemText = "";

		if (item == null)
			return itemText;

		switch (item.getType()) {

		case ClientItem.TYPE_SERVICE:

			itemText = "Service Item";
			break;

		case ClientItem.TYPE_INVENTORY_PART:

			itemText = "Inventory Part";

			break;

		case ClientItem.TYPE_DISCOUNT:

			itemText = "Discount";
			break;

		case ClientItem.TYPE_GROUP:

			itemText = "Group";
			break;

		case ClientItem.TYPE_INVENTORY_ASSEMBLY:

			itemText = "Inventory Assembly";
			break;

		case ClientItem.TYPE_NON_INVENTORY_PART:

			itemText = "Non Inventory";
			break;

		case ClientItem.TYPE_PAYMENT:

			itemText = "Payment";

			break;

		case ClientItem.TYPE_SALES_TAX_GROUP:

			itemText = "Sales Tax Group";

			break;

		case ClientItem.TYPE_SALES_TAX_ITEM:

			itemText = "Sales Tax Item";

			break;

		case ClientItem.TYPE_SUBTOTAL:

			itemText = "SubTotal";

			break;
		default:
			break;
		}

		return itemText;

	}

	public static <L> List<L> getArrayList(List<L> list) {
		if (list == null)
			return new ArrayList<L>();
		return list;
	}

	public static String getDescription(int boxType) {

		String description = null;
		switch (boxType) {
		case 1:
			description = AccounterClientConstants.Box1_Description;
			break;
		case 2:
			description = AccounterClientConstants.Box2_Description;
			break;

		}
		return description;
	}
}