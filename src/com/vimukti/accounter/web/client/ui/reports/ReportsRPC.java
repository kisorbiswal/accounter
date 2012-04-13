package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientBankAccount;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientBuildAssembly;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientCreditRating;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientCustomerGroup;
import com.vimukti.accounter.web.client.core.ClientCustomerPrePayment;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.core.ClientEmployee;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.ClientInventoryAssembly;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientIssuePayment;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ClientItemReceipt;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.ClientLocation;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ClientPayTAX;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPayrollUnit;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientReceiveVAT;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientStockAdjustment;
import com.vimukti.accounter.web.client.core.ClientStockTransfer;
import com.vimukti.accounter.web.client.core.ClientTAXAdjustment;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;
import com.vimukti.accounter.web.client.core.ClientVendorPayment;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;

public class ReportsRPC {

	protected static <T extends IAccounterCore, A extends Action> void initCallBack(
			T c, final A a, final long id) {
		AccounterAsyncCallback<T> callback = new AccounterAsyncCallback<T>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showMessage(Global.get().messages().sessionExpired());
			}

			@Override
			public void onResultSuccess(T result) {
				if (result != null) {
					UIUtils.runAction(result, a);
				}
			}

		};
		Accounter.createGETService().getObjectById(c.getObjectType(), id,
				callback);
	}

	public static void getTaxAgency(long id) {
		UIUtils.runAction(Accounter.getCompany().getTaxAgency(id),
				ActionFactory.getNewTAXAgencyAction());

	}

	public static void getVendor(String name) {
		UIUtils.runAction(Accounter.getCompany().getVendorByName(name),
				ActionFactory.getNewVendorAction());
	}

	public static void openTransactionView(int transactionType,
			long transactionId) {
		if (Accounter.getUser().getUserRole()
				.equalsIgnoreCase(RolePermissions.READ_ONLY)) {
			return;
		}
		switch (transactionType) {

		case ClientTransaction.TYPE_PAY_BILL:
			initCallBack(new ClientPayBill(),
					ActionFactory.getPayBillsAction(), transactionId);
			break;
		case ClientTransaction.TYPE_VENDOR_PAYMENT:
			initCallBack(new ClientVendorPayment(),
					ActionFactory.getNewVendorPaymentAction(), transactionId);
			break;
		case ClientTransaction.TYPE_TRANSFER_FUND:
			initCallBack(new ClientTransferFund(),
					ActionFactory.getMakeDepositAction(), transactionId);
			break;
		case ClientTransaction.TYPE_ENTER_BILL:
			initCallBack(new ClientEnterBill(),
					ActionFactory.getEnterBillsAction(), transactionId);
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
			initCallBack(new ClientCashPurchase(),
					ActionFactory.getNewCashPurchaseAction(), transactionId);
			break;
		case ClientTransaction.TYPE_CASH_SALES:
			initCallBack(new ClientCashSales(),
					ActionFactory.getNewCashSaleAction(), transactionId);
		case IAccounterCore.RECURING_TRANSACTION:
			initCallBack(new ClientRecurringTransaction(),
					ActionFactory.getRecurringTransactionDialogAction(),
					transactionId);
			break;
		case ClientTransaction.TYPE_WRITE_CHECK:
			initCallBack(new ClientWriteCheck(),
					ActionFactory.getWriteChecksAction(), transactionId);
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			initCallBack(new ClientCustomerRefund(),
					ActionFactory.getCustomerRefundAction(), transactionId);
			break;
		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			initCallBack(new ClientCustomerCreditMemo(),
					ActionFactory.getNewCreditsAndRefundsAction(),
					transactionId);
			break;
		case ClientTransaction.TYPE_RECEIVE_PAYMENT:
			initCallBack(new ClientReceivePayment(),
					ActionFactory.getReceivePaymentAction(), transactionId);
			break;
		case ClientTransaction.TYPE_INVOICE:
			initCallBack(new ClientInvoice(),
					ActionFactory.getNewInvoiceAction(), transactionId);
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			initCallBack(new ClientCreditCardCharge(),
					ActionFactory.getCreditCardChargeAction(), transactionId);
			break;
		case ClientTransaction.TYPE_ESTIMATE:
			initCallBack(new ClientEstimate(),
					ActionFactory.getNewQuoteAction(0), transactionId);
			break;
		case ClientTransaction.TYPE_ISSUE_PAYMENT:
			initCallBack(new ClientIssuePayment(),
					ActionFactory.getIssuePaymentsAction(), transactionId);
			break;
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			initCallBack(new ClientVendorCreditMemo(),
					ActionFactory.getNewCreditMemoAction(), transactionId);
			break;
		case ClientTransaction.TYPE_PAY_TAX:

			initCallBack(new ClientPayTAX(), ActionFactory.getpayTAXAction(),
					transactionId);
			break;
		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			initCallBack(new ClientJournalEntry(),
					ActionFactory.getNewJournalEntryAction(), transactionId);
			break;

		case ClientTransaction.TYPE_PURCHASE_ORDER:
			initCallBack(new ClientPurchaseOrder(),
					ActionFactory.getPurchaseOrderAction(), transactionId);
			break;

		case ClientTransaction.TYPE_ITEM_RECEIPT:
			initCallBack(new ClientItemReceipt(),
					ActionFactory.getItemReceiptAction(), transactionId);
			break;

		case ClientTransaction.TYPE_CASH_EXPENSE:
			initCallBack(new ClientCashPurchase(),
					ActionFactory.CashExpenseAction(), transactionId);
			break;

		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			initCallBack(new ClientCreditCardCharge(),
					ActionFactory.CreditCardExpenseAction(), transactionId);
			break;

		case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
			initCallBack(new ClientCashPurchase(),
					ActionFactory.EmployeeExpenseAction(), transactionId);
			break;

		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
			initCallBack(new ClientCustomerPrePayment(),
					ActionFactory.getNewCustomerPaymentAction(), transactionId);
			break;
		case ClientTransaction.TYPE_RECEIVE_TAX:
			initCallBack(new ClientReceiveVAT(),
					ActionFactory.getreceiveVATAction(), transactionId);
			break;
		case ClientTransaction.TYPE_ADJUST_SALES_TAX:
		case ClientTransaction.TYPE_ADJUST_VAT_RETURN:
			initCallBack(new ClientTAXAdjustment(),
					ActionFactory.getAdjustTaxAction(), transactionId);
			break;
		case ClientTransaction.TYPE_TDS_CHALLAN:
			initCallBack(new ClientTDSChalanDetail(),
					ActionFactory.getTDSChalanDetailsView(), transactionId);
			break;
		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			initCallBack(new ClientMakeDeposit(),
					ActionFactory.getDepositAction(), transactionId);
			break;

		// These cases were included to open the views other than transactions.
		case IAccounterCore.ACCOUNT:
			initCallBack(new ClientAccount(),
					ActionFactory.getNewAccountAction(), transactionId);
			break;
		case IAccounterCore.TAXGROUP:
			initCallBack(new ClientTAXGroup(),
					ActionFactory.getManageSalesTaxGroupsAction(),
					transactionId);
			break;
		case IAccounterCore.TAXITEM:
			initCallBack(new ClientTAXItem(),
					ActionFactory.getNewVatItemAction(), transactionId);
			break;
		case IAccounterCore.TAXAGENCY:
			initCallBack(new ClientTAXAgency(),
					ActionFactory.getNewTAXAgencyAction(), transactionId);
			break;
		case IAccounterCore.CUSTOMER_GROUP:
			initCallBack(new ClientCustomerGroup(),
					ActionFactory.getCustomerGroupListAction(), transactionId);
			break;
		case IAccounterCore.VENDOR_GROUP:
			initCallBack(new ClientVendorGroup(),
					ActionFactory.getVendorGroupListAction(), transactionId);
			break;
		case IAccounterCore.PAYMENT_TERMS:
			initCallBack(new ClientPaymentTerms(),
					ActionFactory.getPaymentTermListAction(), transactionId);
			break;
		case IAccounterCore.SHIPPING_METHOD:
			initCallBack(new ClientShippingMethod(),
					ActionFactory.getShippingMethodListAction(), transactionId);
			break;
		case IAccounterCore.SHIPPING_TERMS:
			initCallBack(new ClientShippingTerms(),
					ActionFactory.getShippingTermListAction(), transactionId);
			break;
		case IAccounterCore.ITEM_GROUP:
			initCallBack(new ClientItemGroup(),
					ActionFactory.getItemGroupListAction(), transactionId);
			break;
		case IAccounterCore.CREDIT_RATING:
			initCallBack(new ClientCreditRating(),
					ActionFactory.getCreditRatingListAction(), transactionId);
			break;
		case IAccounterCore.CURRENCY:
			initCallBack(new ClientCurrency(),
					ActionFactory.getCurrencyGroupListAction(), transactionId);
			break;
		case IAccounterCore.ITEM:
			initCallBack(new ClientItem(),
					ActionFactory.getNewItemAction(true), transactionId);
			break;
		case IAccounterCore.ASSEMBLY:
			initCallBack(new ClientInventoryAssembly(),
					ActionFactory.getInventoryAssemblyAction(), transactionId);
			break;
		case IAccounterCore.VENDOR:
			initCallBack(new ClientVendor(),
					ActionFactory.getNewVendorAction(), transactionId);
			break;
		case IAccounterCore.CUSTOMER:
			initCallBack(new ClientCustomer(),
					ActionFactory.getNewCustomerAction(), transactionId);
			break;
		case IAccounterCore.SALES_PERSON:
			initCallBack(new ClientSalesPerson(),
					ActionFactory.getNewSalesperSonAction(), transactionId);
			break;
		case IAccounterCore.TAXCODE:
			initCallBack(new ClientTAXCode(),
					ActionFactory.getNewTAXCodeAction(), transactionId);
			break;
		case IAccounterCore.STOCK_ADJUSTMENT:
			initCallBack(new ClientStockAdjustment(),
					ActionFactory.getStockAdjustmentAction(), transactionId);
			break;
		case IAccounterCore.WAREHOUSE:
			initCallBack(new ClientWarehouse(),
					ActionFactory.getWareHouseViewAction(), transactionId);
			break;
		case IAccounterCore.STOCK_TRANSFER:
			initCallBack(new ClientStockTransfer(),
					ActionFactory.getWareHouseTransferAction(), transactionId);
			break;
		case IAccounterCore.MEASUREMENT:
			initCallBack(new ClientMeasurement(),
					ActionFactory.getAddMeasurementAction(), transactionId);
			break;
		case IAccounterCore.USER:
			initCallBack(new ClientUser(), ActionFactory.getInviteUserAction(),
					transactionId);
			break;
		case IAccounterCore.BRANDING_THEME:
			initCallBack(new ClientBrandingTheme(),
					ActionFactory.getNewBrandThemeAction(), transactionId);
			break;
		case IAccounterCore.LOCATION:
			initCallBack(new ClientLocation(),
					ActionFactory.getLocationGroupListAction(), transactionId);
			break;
		case IAccounterCore.ACCOUNTER_CLASS:
			initCallBack(new ClientAccounterClass(),
					ActionFactory.getAccounterClassGroupListAction(),
					transactionId);
			break;
		case IAccounterCore.BANK_ACCOUNT:
			initCallBack(new ClientBankAccount(),
					ActionFactory.getNewBankAccountAction(), transactionId);
			break;
		case IAccounterCore.FIXED_ASSET:
			initCallBack(new ClientFixedAsset(),
					ActionFactory.getNewFixedAssetAction(), transactionId);
			break;
		case IAccounterCore.BUDGET:
			initCallBack(new ClientBudget(),
					ActionFactory.getNewBudgetAction(), transactionId);
			break;
		case IAccounterCore.RECONCILIATION:
			initCallBack(new ClientReconciliation(),
					ActionFactory.getNewReconciliationAction(), transactionId);
			break;
		case IAccounterCore.TDSCHALANDETAIL:
			initCallBack(new ClientTDSChalanDetail(),
					ActionFactory.getTDSChalanDetailsView(), transactionId);
			break;
		case IAccounterCore.EMPLOYEE:
			initCallBack(new ClientEmployee(),
					ActionFactory.getNewEmployeeAction(), transactionId);
			break;
		case ClientTransaction.TYPE_STOCK_ADJUSTMENT:
			initCallBack(new ClientStockAdjustment(),
					ActionFactory.getStockAdjustmentAction(), transactionId);
			break;
		case ClientTransaction.TYPE_BUILD_ASSEMBLY:
			initCallBack(new ClientBuildAssembly(),
					ActionFactory.getBuildAssemblyAction(), transactionId);
			break;
		case IAccounterCore.PAYROLL_UNIT:
			initCallBack(new ClientPayrollUnit(),
					ActionFactory.getNewPayrollUnitAction(), transactionId);
			break;
		case IAccounterCore.PAY_HEAD:
			initCallBack(new ClientPayHead(),
					ActionFactory.getNewPayHeadAction(), transactionId);
			break;

		}

	}

	public static void openTransactionView(ClientTransaction transaction) {
		if (Accounter.getUser().getUserRole().equals(RolePermissions.READ_ONLY)) {
			return;
		}
		switch (transaction.getType()) {

		case ClientTransaction.TYPE_TRANSFER_FUND:
			ActionFactory.getMakeDepositAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_ENTER_BILL:
			ActionFactory.getEnterBillsAction().run(
					(ClientEnterBill) transaction, false);
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
			ActionFactory.getNewCashPurchaseAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_CASH_SALES:
			ActionFactory.getNewCashSaleAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_WRITE_CHECK:
			ActionFactory.getWriteChecksAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO:
			ActionFactory.getNewCreditsAndRefundsAction().run(transaction,
					false);
			break;
		case ClientTransaction.TYPE_INVOICE:
			ActionFactory.getNewInvoiceAction().run(
					(ClientInvoice) transaction, false);
			break;
		case ClientTransaction.TYPE_ESTIMATE:
			ActionFactory.getNewQuoteAction(0).run(transaction, false);
			break;
		case ClientTransaction.TYPE_VENDOR_CREDIT_MEMO:
			ActionFactory.getNewCreditMemoAction().run(transaction, false);
			break;

		case ClientTransaction.TYPE_CASH_EXPENSE:
			ActionFactory.CashExpenseAction().run(transaction, false);
			break;

		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			ActionFactory.CreditCardExpenseAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_JOURNAL_ENTRY:
			ActionFactory.getNewJournalEntryAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			ActionFactory.getCustomerRefundAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_MAKE_DEPOSIT:
			ActionFactory.getDepositAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_PURCHASE_ORDER:
			ActionFactory.getPurchaseOrderAction().run(transaction, false);
			break;
		case ClientTransaction.TYPE_BUILD_ASSEMBLY:
			ActionFactory.getBuildAssemblyAction().run(transaction, false);
			break;
		}
	}
}
