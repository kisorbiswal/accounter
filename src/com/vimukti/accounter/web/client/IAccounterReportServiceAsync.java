package com.vimukti.accounter.web.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.DummyDebitor;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.client.core.reports.AmountsDueToVendor;
import com.vimukti.accounter.web.client.core.reports.BankDepositDetail;
import com.vimukti.accounter.web.client.core.reports.BudgetActuals;
import com.vimukti.accounter.web.client.core.reports.ClientBudgetList;
import com.vimukti.accounter.web.client.core.reports.DepositDetail;
import com.vimukti.accounter.web.client.core.reports.DepreciationShedule;
import com.vimukti.accounter.web.client.core.reports.ECSalesList;
import com.vimukti.accounter.web.client.core.reports.ECSalesListDetail;
import com.vimukti.accounter.web.client.core.reports.EstimatesByJob;
import com.vimukti.accounter.web.client.core.reports.ExpenseList;
import com.vimukti.accounter.web.client.core.reports.InventoryStockStatusDetail;
import com.vimukti.accounter.web.client.core.reports.InventoryValutionDetail;
import com.vimukti.accounter.web.client.core.reports.InventoryValutionSummary;
import com.vimukti.accounter.web.client.core.reports.JobActualCostDetail;
import com.vimukti.accounter.web.client.core.reports.MISC1099TransactionDetail;
import com.vimukti.accounter.web.client.core.reports.MostProfitableCustomers;
import com.vimukti.accounter.web.client.core.reports.ProfitAndLossByLocation;
import com.vimukti.accounter.web.client.core.reports.RealisedExchangeLossOrGain;
import com.vimukti.accounter.web.client.core.reports.ReconcilationItemList;
import com.vimukti.accounter.web.client.core.reports.Reconciliation;
import com.vimukti.accounter.web.client.core.reports.ReconciliationDiscrepancy;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeList;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeListDetail;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationDetails;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationSummary;
import com.vimukti.accounter.web.client.core.reports.SalesTaxLiability;
import com.vimukti.accounter.web.client.core.reports.TDSAcknowledgmentsReport;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.core.reports.UnRealisedLossOrGain;
import com.vimukti.accounter.web.client.core.reports.UncategorisedAmountsReport;
import com.vimukti.accounter.web.client.core.reports.VATDetail;
import com.vimukti.accounter.web.client.core.reports.VATItemDetail;
import com.vimukti.accounter.web.client.core.reports.VATItemSummary;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.client.ui.reports.CheckDetailReport;
import com.vimukti.accounter.web.client.ui.reports.CurrencyExchangeRate;
import com.vimukti.accounter.web.client.ui.reports.TAXItemDetail;

public interface IAccounterReportServiceAsync {

	public void getSalesByCustomerSummary(final ClientFinanceDate startDate,
			final ClientFinanceDate endDate,
			AsyncCallback<ArrayList<SalesByCustomerDetail>> callBackResult);

	public void getTrialBalance(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<TrialBalance>> callBackResult);

	public void getAgedDebtors(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<AgedDebtors>> callBackResult);

	public void getAgedCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<AgedDebtors>> callBackResult);

	public void getSalesByCustomerDetailReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<SalesByCustomerDetail>> callBackResult);

	public void getSalesByItemDetail(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<SalesByCustomerDetail>> callBackResult);

	public void getSalesByItemSummary(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<SalesByCustomerDetail>> callBackResult);

	public void getCustomerTransactionHistory(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<TransactionHistory>> callBackResult);

	public void getPurchasesByVendorDetail(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<SalesByCustomerDetail>> callBackResult);

	public void getPurchasesByVendorSummary(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<SalesByCustomerDetail>> callBackResult);

	public void getPurchasesByItemDetail(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<SalesByCustomerDetail>> callBackResult);

	public void getPurchasesByItemSummary(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<SalesByCustomerDetail>> callBackResult);

	public void getVendorTransactionHistory(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<TransactionHistory>> callBackResult);

	public void getAmountsDueToVendor(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<AmountsDueToVendor>> callBackResult);

	public void getMostProfitableCustomers(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<MostProfitableCustomers>> callBackResult);

	public void getTransactionDetailByTaxItem(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<TransactionDetailByTaxItem>> callBackResult);

	public void getRegister(long accountId,
			AsyncCallback<ArrayList<ClientTransaction>> callBackResult);

	public void getAccountRegister(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long accountId,
			AsyncCallback<ArrayList<AccountRegister>> callBackResult);

	public void getTransactionHistoryCustomers(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<ClientCustomer>> callBackResult);

	public void getTransactionHistoryVendors(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<ClientVendor>> callBackResult);

	public void getSalesReportItems(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<ClientItem>> callBackResult);

	public void getPurchaseReportItems(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<ClientItem>> callBackResult);

	public void getTransactionDetailByAccount(
			final ClientFinanceDate startDate, final ClientFinanceDate endDate,
			AsyncCallback<ArrayList<TransactionDetailByAccount>> callBackResult);

	public void getAutomaticTransactions(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<TransactionDetailByAccount>> callBack);

	public void getSalesTaxLiabilityReport(final ClientFinanceDate startDate,
			final ClientFinanceDate endDate,
			AsyncCallback<ArrayList<SalesTaxLiability>> callBack);

	public void getSalesByCustomerDetailReport(String customerName,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			AsyncCallback<ArrayList<SalesByCustomerDetail>> callBack);

	public void getSalesByItemDetail(String itemName,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			AsyncCallback<ArrayList<SalesByCustomerDetail>> callBack);

	public void getPurchasesByVendorDetail(String vendorName,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			AsyncCallback<ArrayList<SalesByCustomerDetail>> callBack);

	public void getPurchasesByItemDetail(String itemName,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			AsyncCallback<ArrayList<SalesByCustomerDetail>> callBack);

	public void getTransactionDetailByAccount(String accountName,
			final ClientFinanceDate startDate, final ClientFinanceDate endDate,
			AsyncCallback<ArrayList<TransactionDetailByAccount>> callBack);

	void getMinimumAndMaximumTransactionDate(
			AsyncCallback<ArrayList<ClientFinanceDate>> callBack);

	public void getTransactionDetailByTaxItem(String taxItemname,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			AsyncCallback<ArrayList<TransactionDetailByTaxItem>> callBackResult);

	public void getBalanceSheetReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<TrialBalance>> callBack);

	public void getDepreciationSheduleReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate, int status, long comapnyId,
			AsyncCallback<ArrayList<DepreciationShedule>> callBack);

	public void getProfitAndLossReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<TrialBalance>> callBack);

	public void getProfitAndLossByLocationReport(boolean isLocation,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			AsyncCallback<ArrayList<ProfitAndLossByLocation>> callBack);

	public void getSalesByLocationDetailsReport(boolean isLocation,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			AsyncCallback<ArrayList<SalesByLocationDetails>> callBack);

	public void getSalesByLocationDetailsForLocation(boolean isLocation,
			String locationName, ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<SalesByLocationDetails>> callBack);

	public void getSalesByLocationSummaryReport(boolean isLocation,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			AsyncCallback<ArrayList<SalesByLocationSummary>> callBack);

	public void getCashFlowReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<TrialBalance>> callBack);

	public void getPurchaseOpenOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<OpenAndClosedOrders>> callBack);

	public void getPurchaseCompletedOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<OpenAndClosedOrders>> callBack);

	public void getPurchaseCancelledOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<OpenAndClosedOrders>> callBack);

	public void getPurchaseOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<OpenAndClosedOrders>> callBack);

	public void getPurchaseClosedOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<OpenAndClosedOrders>> callBack);

	public void getSalesOpenOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<OpenAndClosedOrders>> callBack);

	public void getSalesCompletedOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<OpenAndClosedOrders>> callBack);

	public void getSalesOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<OpenAndClosedOrders>> callBack);

	public void getSalesCancelledOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<OpenAndClosedOrders>> callBack);

	public void getSalesClosedOrderReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<OpenAndClosedOrders>> callBack);

	void getPriorVATReturnVATDetailReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<VATDetail>> callback);

	void getPriorVATReturnReport(long vatAgancy, ClientFinanceDate endDate,
			AsyncCallback<ArrayList<VATDetail>> callback);

	public void getPriorReturnVATSummary(long vatAgency,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<VATSummary>> callback);

	public void getVAT100Report(long vatAgncy, ClientFinanceDate fromDate,
			ClientFinanceDate toDate,
			AsyncCallback<ArrayList<VATSummary>> callback);

	public void getUncategorisedAmountsReport(ClientFinanceDate fromDate,
			ClientFinanceDate toDate,
			AsyncCallback<ArrayList<UncategorisedAmountsReport>> callback);

	public void getECSalesListReport(ClientFinanceDate fromDate,
			ClientFinanceDate toDate,
			AsyncCallback<ArrayList<ECSalesList>> callback);

	public void getECSalesListReport(ClientFinanceDate fromDate,
			ClientFinanceDate toDate, long companyId,
			AsyncCallback<ArrayList<ECSalesList>> callback);

	public void getECSalesListDetailReport(String payeeName,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			AsyncCallback<ArrayList<ECSalesListDetail>> callback);

	public void getReverseChargeListDetailReport(String payeeName,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			AsyncCallback<ArrayList<ReverseChargeListDetail>> callback);

	public void getReverseChargeListReport(ClientFinanceDate fromDate,
			ClientFinanceDate toDate,
			AsyncCallback<ArrayList<ReverseChargeList>> callback);

	public void getDebitors(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<DummyDebitor>> callback);

	public void getCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<DummyDebitor>> callback);

	public void getAgedDebtors(String Name, ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<AgedDebtors>> callback);

	public void getAgedCreditors(String debitorName,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			AsyncCallback<ArrayList<AgedDebtors>> callback);

	public void getVATItemSummaryReport(ClientFinanceDate fromDate,
			ClientFinanceDate toDate,
			AsyncCallback<ArrayList<VATItemSummary>> callback);

	public void getVATItemDetailReport(String vatItemName,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			AsyncCallback<ArrayList<VATItemDetail>> callback);

	public void getExpenseReportByType(int status, ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<ExpenseList>> callback);

	public void getDepositDetail(final ClientFinanceDate startDate,
			final ClientFinanceDate endDate,
			AsyncCallback<ArrayList<DepositDetail>> callBackResult);

	public void getCheckDetailReport(long paymentmethod,
			final ClientFinanceDate startDate, final ClientFinanceDate endDate,
			AsyncCallback<ArrayList<CheckDetailReport>> callBackResult);

	public void getStatements(boolean isVendor, long id, int viewType,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			AsyncCallback<ArrayList<PayeeStatementsList>> callBack);

	void getCustomerStatement(long customer, long fromDate, long toDate,
			AsyncCallback<ArrayList<PayeeStatementsList>> callBack);

	public void getMISC1099TransactionDetailReport(long VendorId, int boxNo,
			ClientFinanceDate fromDate, ClientFinanceDate toDate,
			AsyncCallback<ArrayList<MISC1099TransactionDetail>> callback);

	public void getBudgetItemsList(long budgetID,
			AsyncCallback<ArrayList<ClientBudgetList>> callback);

	void getTAXItemDetailReport(long taxAgency, long startDate, long endDate,
			AsyncCallback<ArrayList<TAXItemDetail>> callback);

	public void getVATExceptionDetailReport(ClientFinanceDate start,
			ClientFinanceDate end, long taxRetunId,
			AsyncCallback<ArrayList<VATDetail>> callback);

	public void getTAXItemExceptionDetailReport(long taxAgency, long startDate,
			long endDate, AsyncCallback<ArrayList<TAXItemDetail>> callback);

	public void getTDSAcknowledgments(ClientFinanceDate startDate,
			ClientFinanceDate endDate,
			AsyncCallback<ArrayList<TDSAcknowledgmentsReport>> callback);

	public void getAllReconciliations(ClientFinanceDate start,
			ClientFinanceDate end, long companyId,
			AsyncCallback<ArrayList<Reconciliation>> callback);

	public void getCustomerTransactionsList(long id, int transactionType,
			int transactionStatusType, ClientFinanceDate startDate,
			ClientFinanceDate endDate, int start, int length,
			AsyncCallback<PaginationList<TransactionHistory>> callback);

	void getReconciliationItemByBankAccountID(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long bankAccountId, long comapnyId,
			AsyncCallback<ArrayList<ReconcilationItemList>> callback);

	public void getVendorTransactionsList(long id, int transactionType,
			int transactionStatusType, ClientFinanceDate startDate,
			ClientFinanceDate endDate, int start, int length,
			AsyncCallback<PaginationList<TransactionHistory>> callback);

	public void getBudgetvsAcualReportData(long id, ClientFinanceDate start,
			ClientFinanceDate end, int type,
			AsyncCallback<ArrayList<BudgetActuals>> callback);

	public void getRealisedExchangeLossesAndGains(ClientFinanceDate start,
			ClientFinanceDate end,
			AsyncCallback<ArrayList<RealisedExchangeLossOrGain>> callback);

	public void getUnRealisedExchangeLossesAndGains(
			long enteredDate,
			Map<Long, Double> exchangeRates,
			AsyncCallback<ArrayList<UnRealisedLossOrGain>> unRealisedExchangeLossedAndGains);

	void getExchangeRatesOfDate(long date,
			AsyncCallback<List<CurrencyExchangeRate>> callback);

	public void getInventoryStockStatusByVendor(ClientFinanceDate start,
			ClientFinanceDate end,
			AsyncCallback<ArrayList<InventoryStockStatusDetail>> callback);

	public void getInventoryStockStatusByItem(ClientFinanceDate start,
			ClientFinanceDate end,
			AsyncCallback<ArrayList<InventoryStockStatusDetail>> callback);

	public void getInventoryValutionSummary(ClientFinanceDate start,
			ClientFinanceDate end,
			AsyncCallback<ArrayList<InventoryValutionSummary>> callback);

	public void getInventoryValutionDetail(long id, ClientFinanceDate start,
			ClientFinanceDate end,
			AsyncCallback<ArrayList<InventoryValutionDetail>> callback);

	public void getBankingDepositDetils(ClientFinanceDate start,
			ClientFinanceDate end,
			AsyncCallback<ArrayList<BankDepositDetail>> callback);

	public void getMissingCheckDetils(long accountId, ClientFinanceDate start,
			ClientFinanceDate end,
			AsyncCallback<ArrayList<TransactionDetailByAccount>> callback);

	public void getReconciliationDiscrepancy(ClientFinanceDate start,
			ClientFinanceDate end,
			AsyncCallback<ArrayList<ReconciliationDiscrepancy>> callback);

	public void getEstimatesByJob(ClientFinanceDate start,
			ClientFinanceDate end,
			AsyncCallback<ArrayList<EstimatesByJob>> callBack);

	public void getJobActualCostOrRevenueDetails(ClientFinanceDate start,
			ClientFinanceDate end, boolean isActualcostDetail,
			long transactionId, long jobId,
			AsyncCallback<ArrayList<JobActualCostDetail>> callBack);

}