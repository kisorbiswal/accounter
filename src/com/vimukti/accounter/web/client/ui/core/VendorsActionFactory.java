package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.banking.BuyChecksAndFormsAction;
import com.vimukti.accounter.web.client.ui.banking.ServicesOverviewAction;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.company.PurchaseItemsAction;
import com.vimukti.accounter.web.client.ui.vendors.AwaitingAuthorisationAction;
import com.vimukti.accounter.web.client.ui.vendors.BillsAction;
import com.vimukti.accounter.web.client.ui.vendors.CashExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.CreditCardExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.EmployeeExpenseAction;
import com.vimukti.accounter.web.client.ui.vendors.EnterBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.ExpenseClaimsAction;
import com.vimukti.accounter.web.client.ui.vendors.ExpensesAction;
import com.vimukti.accounter.web.client.ui.vendors.IssuePaymentsAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCashPurchaseAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCheckAction;
import com.vimukti.accounter.web.client.ui.vendors.NewCreditMemoAction;
import com.vimukti.accounter.web.client.ui.vendors.NewItemReceiptAction;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorAction;
import com.vimukti.accounter.web.client.ui.vendors.NewVendorItemAction;
import com.vimukti.accounter.web.client.ui.vendors.PayBillsAction;
import com.vimukti.accounter.web.client.ui.vendors.PreviousClaimAction;
import com.vimukti.accounter.web.client.ui.vendors.PurchaseOrderAction;
import com.vimukti.accounter.web.client.ui.vendors.PurchaseOrderListAction;
import com.vimukti.accounter.web.client.ui.vendors.RecordExpensesAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorPaymentsListAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorsHomeAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorsListAction;

/**
 * VendorsActionFactory contains static methods, each method returns appropriate
 * Action instance from Vendors Section, use to Get All Vendors Actions
 * instance.
 * 
 * @author kumar kasimala
 * 
 */
public class VendorsActionFactory extends AbstractActionFactory {

	public static VendorsHomeAction getVendorsHomeAction() {
		return new VendorsHomeAction(UIUtils.getVendorString(FinanceApplication
				.getVendorsMessages().supplierhome(), FinanceApplication
				.getVendorsMessages().vendorHome()),
				"/images/icons/vendors/vendor_home.png");
	}

	public static NewVendorAction getNewVendorAction() {
		return new NewVendorAction(UIUtils.getVendorString(FinanceApplication
				.getVendorsMessages().newSupplier(), FinanceApplication
				.getVendorsMessages().newVendor()),
				"/images/icons/vendors/new_vendor.png");
	}

	public static NewVendorAction getNewVendorAction(ClientVendor vendor,
			AsyncCallback<Object> callback) {
		return new NewVendorAction(UIUtils.getVendorString(FinanceApplication
				.getVendorsMessages().newSupplier(), FinanceApplication
				.getVendorsMessages().newVendor()),
				"/images/icons/vendors/new_vendor.png", vendor, callback);
	}

	public static PurchaseItemsAction getItemsAction() {
		return new PurchaseItemsAction(actionsConstants.items(),
				"/images/icons/customers/items.png", UIUtils.getVendorString(
						FinanceApplication.getVendorsMessages().supplier(),
						FinanceApplication.getVendorsMessages().vendor()));
	}

	// public static NewItemAction getNewItemAction() {
	// return new NewItemAction(actionsConstants.newItem(),
	// "/images/icons/vendors/new_item.png", null);
	// }
	//
	// public static NewItemAction getNewItemAction(Item item,
	// AsyncCallback<Object> callback, AbstractBaseView view) {
	// return new NewItemAction(actionsConstants.newItem(),
	// "/images/icons/vendors/new_item.png", view, item, callback);
	// }

	public static NewCashPurchaseAction getNewCashPurchaseAction() {
		return new NewCashPurchaseAction(actionsConstants.newCashPurchase(),
				"/images/icons/vendors/new_cash_purchase.png");
	}

	public static NewCashPurchaseAction getNewCashPurchaseAction(
			ClientCashPurchase cashPurchase, AsyncCallback<Object> callback) {
		return new NewCashPurchaseAction(actionsConstants.newCashPurchase(),
				"/images/icons/vendors/new_cash_purchase.png", cashPurchase,
				callback);
	}

	public static NewCreditMemoAction getNewCreditMemoAction() {
		return new NewCreditMemoAction(UIUtils.getVendorString(
				FinanceApplication.getVendorsMessages().supplierCredit(),
				FinanceApplication.getVendorsMessages().vendorCredit()),
				"/images/icons/vendors/new_credit_memo.png");
	}

	public static NewItemAction getNewItemAction() {
		return new NewVendorItemAction(actionsConstants.newItem(),
				"/images/icons/customers/new_item.png");
	}

	public static NewCreditMemoAction getNewCreditMemoAction(
			ClientVendorCreditMemo vendorCreditMemo,
			AsyncCallback<Object> callBack) {
		return new NewCreditMemoAction(actionsConstants.newCreditMemo(),
				"/images/icons/vendors/new_credit_memo.png", vendorCreditMemo,
				callBack);
	}

	public static NewCheckAction getNewCheckAction() {
		return new NewCheckAction(actionsConstants.newCheck(),
				"/images/icons/vendors/new_check.png");
	}

	public static EnterBillsAction getEnterBillsAction() {
		return new EnterBillsAction(actionsConstants.enterBills(),
				"/images/icons/vendors/enter_bills.png");
	}

	public static PayBillsAction getPayBillsAction() {
		return new PayBillsAction(actionsConstants.payBills(),
				"/images/icons/vendors/pay_bills.png");
	}

	public static IssuePaymentsAction getIssuePaymentsAction() {
		return new IssuePaymentsAction(actionsConstants.issuePayments(),
				"/images/icons/vendors/issue_payment.png");
	}

	public static VendorPaymentsAction getNewVendorPaymentAction() {
		return new VendorPaymentsAction(UIUtils.getVendorString(
				FinanceApplication.getVendorsMessages().supplierPrePayment(),
				FinanceApplication.getVendorsMessages().vendorPrePayment()),
				"/images/icons/vendors/vendor_payments.png");
	}

	public static RecordExpensesAction getRecordExpensesAction() {
		return new RecordExpensesAction(actionsConstants.recordExpenses(),
				"/images/icons/vendors/record_expenses.png");
	}

	public static ServicesOverviewAction getServicesOverviewAction() {
		return new ServicesOverviewAction(actionsConstants.servicesOverview());
	}

	public static BuyChecksAndFormsAction getBuyChecksAndFormsAction() {
		return new BuyChecksAndFormsAction(
				actionsConstants.buyChecksAndForms(), "");
	}

	public static VendorsListAction getVendorsAction() {
		return new VendorsListAction(UIUtils.getVendorString(FinanceApplication
				.getVendorsMessages().suppliers(), FinanceApplication
				.getVendorsMessages().vendors()),
				"/images/icons/vendors/vendors.png");
	}

	// public static Item getItemAction() {
	// return new ItemListAction(actionsConstants.items());
	// }

	public static BillsAction getBillsAction() {
		return new BillsAction(actionsConstants.billsAndItemReceipts(),
				"/images/icons/vendors/bills.png");
	}

	public static ExpensesAction getExpensesAction(String viewType) {
		return new ExpensesAction(actionsConstants.recordExpenses(),
				"/images/icons/vendors/record_expenses.png", viewType);
	}

	public static VendorPaymentsListAction getVendorPaymentsAction() {

		return new VendorPaymentsListAction(UIUtils.getVendorString(
				FinanceApplication.getVendorsMessages().supplierPayments(),
				FinanceApplication.getVendorsMessages().vendorPayments()),
				"/images/icons/vendors/vendor_payments.png");
	}

	public static PurchaseOrderAction getPurchaseOrderAction() {
		return new PurchaseOrderAction(FinanceApplication.getVendorsMessages()
				.purchaseOrder(), "/images/icons/vendors/vendor_payments.png");
	}

	public static PurchaseOrderListAction getPurchaseOrderListAction() {
		return new PurchaseOrderListAction(FinanceApplication
				.getVendorsMessages().purchaseOrderList(),
				"/images/icons/vendors/vendor_payments.png");
	}

	public static NewItemReceiptAction getItemReceiptAction() {
		return new NewItemReceiptAction(FinanceApplication.getVendorsMessages()
				.ItemReceipt(), "/images/icons/vendors/vendor_payments.png");
	}

	public static CashExpenseAction CashExpenseAction() {
		return new CashExpenseAction(FinanceApplication.getVendorsMessages()
				.cash(), "/images/icons/vendors/new_cash_purchase.png");
	}

	public static EmployeeExpenseAction EmployeeExpenseAction() {
		return new EmployeeExpenseAction(FinanceApplication
				.getVendorsMessages().employee(),
				"/images/icons/vendors/new_cash_purchase.png");
	}

	public static CreditCardExpenseAction CreditCardExpenseAction() {
		return new CreditCardExpenseAction(FinanceApplication
				.getVendorsMessages().CreditCard(),
				"/images/icons/vendors/new_cash_purchase.png");
	}

	public static AwaitingAuthorisationAction getAwaitingAuthorisationAction() {
		return new AwaitingAuthorisationAction(FinanceApplication
				.getVendorsMessages().awaitingAuthorisation());

	}

	public static PreviousClaimAction getPreviousClaimAction() {
		return new PreviousClaimAction(FinanceApplication.getVendorsMessages()
				.previousClaim());

	}

	public static ExpenseClaimsAction getExpenseClaimsAction(int selectedTab) {
		return new ExpenseClaimsAction(FinanceApplication.getVendorsMessages()
				.expenseClaims(), "/images/icons/vendors/record_expense.png", selectedTab);
	}
}
