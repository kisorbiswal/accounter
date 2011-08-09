package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.ui.UIUtils;

public interface AccounterErrorType {

//	public static final String AMOUNTEXCEEDS = "The amount you have entered is too large";
//	public static final String INVALIDAMOUNT = "Invalid Amount";
//	public static final String INVALIDENTRY = "Data which is entered is not valid";
//	public static final String INVALID_NEGATIVE_AMOUNT = "Amount should not be zero or less than zero";

	// error messages for Transactions
//	public static final String InvalidTransactionDate = "The transaction date must be within an open Fiscal Year. "
//			+ "To create a new Fiscal Year or edit an existing one click Manage Fiscal Year in the Company drop down menu";
//	public static final String InvalidDate = "You must enter a date that falls after the Prevent Posting Before date";
//	public static final String blankTransaction = "Please enter a Transaction Item";
//	public static final String customer_payment_or_bill_payment_blankTransaction = "Please select a Transaction";
//	public static final String fiscal_Year_Closed = "The Fiscal Year for your company has been closed. You can enter only opening balances"
//			+ "when a fiscal year hasn't been closed. To re-open Fiscal years, select Manage Fiscal Year on the company menu. "
//			+ "If you need assistance contact your administrator";
//	public static final String duplicate_ItemName = "This Product name is already in use. Please use a unique name";
//	public static final String Item_BuyOrSell = "You must mark this itfem as an Product you buy or sell";
//	public static final String isItemSoldTrue = "Is sold must be true since the Product is part of a sales's document.";
//	public static final String isItemPurchaseTrue = "Is purchase  must be true since the Product is part of a purchase document.";
//	public static final String taxAgency_Discount_PaymentTerm = "The discount for the payment term will not be applied for a tax agency.";
//	public static final String transactionDateWithinFiscalYear = "The transaction date must be within an open fiscal year."
//			+ "To create a new fiscal year, on the company menu Click Manage Fiscal Year.";
//	public static final String taxAgency_FinanceAcount = ": This financial Account is specified as the "
//			+ "liability Finance Category of a tax agency and can only be used for tax related transactions.";
//	public static final String canVoidOrEdit = "This document cannot be edited or voided because a cash discount "
//			+ "or a write off has been applied to the payment ";
//	public static final String cannotUsePurchaseItem = "Cannot sell a Product that does not have a sales financial Account.";
//	public static final String cannotUseSalesItem = "Cannot purchase a Product that does not have a purchase financial Account.";
//	public static final String unitPrice = "You cannot enter a negative Unit Price";
//	public static final String discountAmount = "Discount Amount cannot be negative";
//	public static final String amount = "Amount should not be zero or less than zero";
//	public static final String lineTotal = "You cannot enter a negative Total Amount ";
//	public static final String quantity = "You cannot enter a negative Quantity ";
//	public static final String lineTotalAmount = "The Value for Line Total Field must be positive.";
//	public static final String invoice_DueDate = "The due date cannot be earlier than the date of the invoice";
//	public static final String recievePayment_TotalAmount = "The total Payments entered cannot exceed the Amount Received";
//	public static final String makeDeposit_CashBackAccount = "An active cash-back Finance Category must be specified.";
//	public static final String makeDeposit_CashBackAmount = "The cash-back amount cannot be greater than the total amount deposited.";
//	public static final String transferFunds = "You must transfer funds from one Finance Category to a different Finance Category.";
//	public static final String distributePayments = "Do you want to distribute this payment to outstanding invoice";
//	public static final String ALREADYEXIST = "Name is already in use. Please enter a unique Name";
//	public static final String GNALREADYEXIST = "Group Name/Number is already in use. Please use a unique Name";
//	public static final String VCALREADYEXIST = "VAT Code is already in use. Please use a unique Name";
//	public static final String VAALREADYEXIST = "VAT Agency is already in use. Please use a unique Name";
//	public static final String makedepositAccountValidation = "We cannot use Deposit/ Transfer to in grid";

//	public static final String RECEIVEPAYMENT_AMOUNT_DUE = "sum of CashDiscount, WriteOff, and AppliedCredits should not exceed the AmountDue";
//
//	public static final String RECEIVEPAYMENT_PAYMENT_EXCESS = "The sum of payments, creditsApplied, writeoff and cashdisount must not exceed the amount due or zero value";
//
//	public static final String REQUIRED_FIELDS = "Details highlighted in red must be entered";
//
//	public static final String RECEIVEPAYMET_INVALID_PAYMENT_AMOUNT = "invalid payment amount...";
//
//	public static final String RECEIVEPAYMET_APPLIED_CREDITS_AMOUNT = "Amount Cannot Be Negative or More than the Balance.";
//
//	public static final String FAILEDREQUEST = "Failed Request";
//	public static final String INCORRECTINFORMATION = "Incorrect Information Entered!";
//	public static final String SHOULD_NOT_EMPTY = "The fields should not be Empty";
//	public static final String SHOULD_EQUAL = "For Journal Entry Credit & Debit Both Should be equal. ";
//	public static final String SHOULD_SELECT_RADIO = "Select Any Depreciation Type";
//	public static final String CHECK_ANYONE = "Check  Sell or Buy Product";
//	public static final String TOTAL_MUSTBE_SAME = "The Debit total and Credit totals must be same";
//	public static final String FAILED_GET_JOURNALENTRIES = "Failed to load journaln entries";
//	public static final String ZERO_AMOUNT = "The Sales Price is £0.00, do you want to continue?";
//	public static final String INVALID_EMAIL = "Invalid Characters Given in Email";
//
//	public static final String PURHASEDATESUDBEWITHINFISCALYEARRANGE = "Purchase Date Should within Opened Fiscal Year";
//
//	public static final String invalidPurchaseDate = "The Purchase Date must be with in an Open "
//			+ "Fiscal Year. To create a new fiscal year, On the Company menu, click Manage FiscalYear";
//
//	public static final String writeCheck_TotalAmount = "The amount on the check has to match the total line amount";
//
//	public static final String journalEntryCustomer = "Only one customer may be used per voucher number on a journal entry";
//
//	public static final String journalEntryAccount = "Same Account should not be used";

//	public static final String journalEntryVendor = "Only one "
//			+ UIUtils.getVendorString("supplier", "vendor")
//			+ "may be used per voucher number on a journal entry";
//	public static final String InvalidTransactionAmount = "You cannot enter a negative Unit Price ";
//	public static final String prior_asOfDate = "Balance as of date is prior to the company start date";
//	public static final String SELECT_VATAGENCY = "Please select VAT Agency and Dates to File VAT";
//	public static final String selectTransaction = "Please select a Transaction Item";
//	public static final String INVALIDNUMBER = "Invalid Account Number. Please enter valid number";
//	public static final String INVALIDSALESORDERNUMBER = "Invalid Sales Order Number. Please enter valid number";
//	public static final String INVALIDACCOUNTNUMBER = UIUtils.getVendorString(
//			"supplier", "vendor")
//			+ " Account Number is Invalid. Please enter valid Account Number";
//	public static final String SALESORDERNUMBERPOSITIVE = "A Sales Order Number should be positive";
//	public static final String SALESORDERNUMBER = "A Sales Order Number should be a number";
//	public static final String SALESORDERNUMBERGREATER0 = "A Sales Order Number should be greater than 0";
}
