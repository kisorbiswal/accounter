package com.vimukti.accounter.web.client.ui.core;


public class AccounterWarningType {

	public static final String AMOUNTEXCEEDS = "Amount Range Should be in: 0 t0 1000";
	public static final String INVALIDAMOUNT = "Invalid Amount";
	public static final String INVALIDENTRY = "";
	public static final String AMOUNTPOSITIVEEXCEEDS = "Amount Range Should be in:";
	public static final String on_CreditCardSave = "It is recommended that you setup a supplier record for the company that you make "
			+ "credit card or line of credit card payments. Do you want to setup this supplier record now?";
	public static final String Create_FiscalYear = "This will create all necessary fiscal years to include the selected start date./n "
			+ "Do you want to continue?";
	public static final String default_IncomeAccount = "Would you like to set this to be the default Income Finance Category for service item?";
	public static final String default_ExpenseAccount = "Would you like to set this to be the default Expense Finance Category for service item?";
	public static final String default_IncomeAccountNonInventory = "Would you like to set this to be the default Income Finance Category for Non-Inventory item?";
	public static final String default_ExpenseAccountNonInventory = "Would you like to set this to be the default Expense Finance Category for Non-Inventory item?";
	public static final String different_IncomeAccountType = "A sale is generally linked to an income Finance Category, but you have selected "
			+ "different finance category type. Do you want to continue? ";
	public static final String different_ExpenseAccountType = "A purchase is generally linked to an expense Finance Category, but you have selected"
			+ " different finance category type. Do you want to continue? ";
	public static final String sales_price_zero = "The Sales Price is £0.00, do you want to continue?";
	public static final String purchase_price_zero = "The purchase price is zero. Do you want to continue?";
	public static final String different_CurrentLiabilityAccount = "A Tax Agency is generally linked to a current liability Finance Category,"
			+ " but you have selected different finance category type. Do you want to continue? ";
	public static final String void_Transaction = "Are you sure you want to void this transaction";
	public static final String saveOrClose = "Do you want to save your changes?";
	public static final String distributePaymentToOutstandingInvoices = "Do you want to distribute this payment to the outstanding invoices";
	public static final String recievePayment = "The full payment has not been applied to the invoices. Are you sure want to save this payment?";
	public static final String total_Exceeds_BankBalance = "The total amount exceeds the bank balance. Do you want to continue";
	public static final String transferFromAccount = "The selected transfer from Finance Category contains insufficient funds to support this transaction. Do you want to contiue? ";
	public static final String INVALID_CUSTOMERREFUND_AMOUNT = "The total Amount exceeds the bank balance.Do you want to continue ";
	public static final String RECORDSEMPTY = "No Records to show";
	public static final String NOT_YET_IMPLEMENTED = "Not yet implemeted...";
	public static final String CLOSED_FISCALYEAR = "The Financial year for your Company has been closed.You can enter Opening balances when a Financial year"
			+ "hasn't been closed.To re-open Finnacial year,select Manage FiscalYear on the Company menu";
	public static final String RECEIVEPAYMENT_EDITING = "Editing this receive payment will void this transaction and create a new receive payment. Do you want to continue?";
	public static final String PAYBILL_EDITING = "Editing this Pay Bill will void this transaction and create a new paybill. Do you want to continue?";
	public static final String prior_CustomerSinceDate = "Customer Since date is prior to the company start date. Do you want to continue?";
	public static final String PAYSALESTAX_EDITING = "Editing this Pay Sales Tax will void this transaction and create a new Pay Sales Tax. Do you want to continue?";
	public static final String PAYVAT_EDITING = "Editing this Pay VAT will void this transaction and create a new Pay VAT. Do you want to continue?";
	public static final String EMPTY_CLASS = "You did n't selected class for this transaction.Do you want to continue?";
}
