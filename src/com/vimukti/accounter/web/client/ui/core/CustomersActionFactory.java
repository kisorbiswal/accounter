package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.company.AddEditSalesTaxCodeAction;
import com.vimukti.accounter.web.client.ui.company.CustomersAction;
import com.vimukti.accounter.web.client.ui.company.ItemsAction;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.company.NewSalesperSonAction;
import com.vimukti.accounter.web.client.ui.customers.CreateStatementAction;
import com.vimukti.accounter.web.client.ui.customers.CustomerPaymentsAction;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundAction;
import com.vimukti.accounter.web.client.ui.customers.CustomerRefundsAction;
import com.vimukti.accounter.web.client.ui.customers.CustomersHomeAction;
import com.vimukti.accounter.web.client.ui.customers.InvoicesAction;
import com.vimukti.accounter.web.client.ui.customers.NewCashSaleAction;
import com.vimukti.accounter.web.client.ui.customers.NewCreditsAndRefundsAction;
import com.vimukti.accounter.web.client.ui.customers.NewCustomerAction;
import com.vimukti.accounter.web.client.ui.customers.NewInvoiceAction;
import com.vimukti.accounter.web.client.ui.customers.NewQuoteAction;
import com.vimukti.accounter.web.client.ui.customers.QuotesAction;
import com.vimukti.accounter.web.client.ui.customers.ReceivePaymentAction;
import com.vimukti.accounter.web.client.ui.customers.ReceivedPaymentsAction;
import com.vimukti.accounter.web.client.ui.customers.SalesOrderAction;
import com.vimukti.accounter.web.client.ui.customers.SalesOrderListAction;
import com.vimukti.accounter.web.client.ui.customers.SalesPersonAction;
import com.vimukti.accounter.web.client.ui.customers.TaxDialogAction;
import com.vimukti.accounter.web.client.ui.vendors.RecordExpensesAction;

/**
 * CustomersActionFactory contains all static methods, each method returns
 * appropriate Action instance from Customer Section, use to Get All Customer
 * Actions instance.
 * 
 * @author kumar kasimala
 * 
 */
public class CustomersActionFactory extends AbstractActionFactory {
	public static NewCustomerAction getNewCustomerAction() {
		return new NewCustomerAction(actionsConstants.newCustomer(),
				"/images/icons/customers/new_customer.png");
	}

	public static NewCustomerAction getNewCustomerAction(
			ClientCustomer customer, AsyncCallback<Object> callBackObject) {
		return new NewCustomerAction(actionsConstants.newCustomer(),
				"/images/icons/customers/new_customer.png", customer,
				callBackObject);
	}

	public static CustomersHomeAction getCustomersHomeAction() {
		return new CustomersHomeAction(actionsConstants.customersHome(),
				"/images/icons/customers/customers_home.png");
	}

	public static AddEditSalesTaxCodeAction getAddEditSalesTaxCodeAction() {
		String constant = null;
		if (FinanceApplication.getCompany().getAccountingType() == 1)
			constant = actionsConstants.newVatCode();
		else
			constant = actionsConstants.newTaxCode();
		return new AddEditSalesTaxCodeAction(constant);
	}

	public static NewQuoteAction getNewQuoteAction() {
		return new NewQuoteAction(actionsConstants.newQuote(),
				"/images/icons/customers/new_quote.png");
	}

	public static NewQuoteAction getNewQuoteAction(ClientEstimate quote,
			AsyncCallback<Object> callBackObject) {
		return new NewQuoteAction(actionsConstants.newQuote(),
				"/images/icons/customers/new_quote.png", quote, callBackObject);
	}

	public static NewItemAction getNewItemAction() {
		return new NewItemAction(actionsConstants.newItem(),
				"/images/icons/customers/new_item.png", true);
	}

	// public static NewItemAction getNewItemAction(ClientItem item,
	// AsyncCallback<Object> callBackObject) {
	// return new NewItemAction(actionsConstants.newItem(),
	// "/images/icons/customers/new_item.png", item, callBackObject);
	// }

	public static TaxDialogAction getTaxAction() {
		return new TaxDialogAction(actionsConstants.tax(),
				"/images/icons/vendors/record_expenses.png");
	}

	public static NewInvoiceAction getNewInvoiceAction() {
		return new NewInvoiceAction(actionsConstants.newInvoice(),
				"/images/icons/customers/new_invoice.png");
	}

	public static NewInvoiceAction getNewInvoiceAction(ClientInvoice invoice,
			AsyncCallback<Object> callBackObject) {
		return new NewInvoiceAction(actionsConstants.newInvoice(),
				"/images/icons/customers/new_invoice.png", invoice,
				callBackObject);
	}

	public static NewCashSaleAction getNewCashSaleAction() {
		return new NewCashSaleAction(actionsConstants.newCashSale(),
				"/images/icons/customers/new_cash_sale.png");
	}

	public static NewCashSaleAction getNewCashSaleAction(
			ClientCashSales cashSales, AsyncCallback<Object> callBackObject) {
		return new NewCashSaleAction(actionsConstants.newCashSale(),
				"/images/icons/customers/new_cash_sale.png", cashSales,
				callBackObject);
	}

	public static NewCreditsAndRefundsAction getNewCreditsAndRefundsAction() {
		return new NewCreditsAndRefundsAction(
				actionsConstants.newCreditNotes(),
				"/images/icons/customers/new_credit_and_refunds.png");
	}

	public static NewCreditsAndRefundsAction getNewCreditsAndRefundsAction(
			ClientCustomerCreditMemo creditMemo,
			AsyncCallback<Object> callBackObject) {
		return new NewCreditsAndRefundsAction(actionsConstants
				.newCreditsAndRefunds(),
				"/images/icons/customers/new_credits_and_refunds.png",
				creditMemo, callBackObject);
	}

	public static ReceivePaymentAction getReceivePaymentAction() {
		return new ReceivePaymentAction("Receive Payment",
				"/images/icons/customers/recive_payments.png");
	}

	public static ReceivePaymentAction getReceivePaymentAction(
			ClientReceivePayment receivePayment,
			AsyncCallback<Object> callBackObject) {
		return new ReceivePaymentAction("Receive Payment",
				"/images/icons/customers/recive_payments.png", receivePayment,
				callBackObject);
	}

	public static CustomerRefundAction getCustomerRefundAction() {
		return new CustomerRefundAction(actionsConstants.customerRefund(),
				"/images/icons/customers/customer_refunds.png");
	}

	// public static CustomerRefundAction getCustomerRefundAction(
	// ClientCustomerRefund customerRefund,
	// AsyncCallback<Object> callBackObject) {
	// return new CustomerRefundAction(actionsConstants.customerRefund(),
	// "/images/icons/customers/customer_refunds.png", customerRefund,
	// callBackObject);
	// }

	public static CreateStatementAction getCreateStatementAction() {
		return new CreateStatementAction(actionsConstants.createStatement(),
				"/images/icons/customers/create_statement.png");
	}

	public static CustomersAction getCustomersAction() {
		return new CustomersAction(actionsConstants.customers(),
				"/images/icons/customers/customers.png");
	}

	public static ItemsAction getItemsAction() {
		return new ItemsAction(actionsConstants.items(),
				"/images/icons/customers/items.png", FinanceApplication
						.getCustomersMessages().customer());
	}

	public static QuotesAction getQuotesAction() {
		return new QuotesAction(actionsConstants.quotes(),
				"/images/icons/customers/quotes.png");
	}

	public static ReceivedPaymentsAction getReceivedPaymentsAction() {
		return new ReceivedPaymentsAction(actionsConstants.receivedPayments(),
				"/images/icons/customers/recived_payment_list.png");
	}

	public static InvoicesAction getInvoicesAction() {
		return new InvoicesAction(actionsConstants.invoices(),
				"/images/icons/customers/invoices.png");
	}

	public static CustomerRefundsAction getCustomerRefundsAction() {
		return new CustomerRefundsAction(actionsConstants.customerRefunds(),
				"/images/icons/customers/customer_refunds.png");
	}

	public static SalesOrderAction getSalesOrderAction() {
		return new SalesOrderAction(actionsConstants.newSalesOrder(),
				"/images/icons/customers/invoices.png");
	}

	public static SalesOrderListAction getSalesOrderListAction() {
		return new SalesOrderListAction(actionsConstants.salesOrderList(),
				"/images/icons/customers/invoices.png");
	}

	public static NewSalesperSonAction getNewSalesperSonAction() {
		return new NewSalesperSonAction(actionsConstants.newSalesperson(),
				"/images/icons/customers/new_salesPerson.png");
	}

	public static SalesPersonAction getSalesPersonAction() {
		return new SalesPersonAction(actionsConstants.salesPersons(),
				"/images/icons/customers/customers.png");
	}

	public static CustomerPaymentsAction getNewCustomerPaymentAction() {
		return new CustomerPaymentsAction(
				actionsConstants.customerPrePayment(),
				"/images/icons/vendors/vendor_payments.png");
	}

}
