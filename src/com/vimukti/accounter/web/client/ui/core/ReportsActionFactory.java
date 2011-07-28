package com.vimukti.accounter.web.client.ui.core;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.APAgingDetailAction;
import com.vimukti.accounter.web.client.ui.reports.APAgingSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.ARAgingDetailAction;
import com.vimukti.accounter.web.client.ui.reports.ARAgingSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.BalanceSheetAction;
import com.vimukti.accounter.web.client.ui.reports.CashFlowStatementAction;
import com.vimukti.accounter.web.client.ui.reports.CheckDetailReportAction;
import com.vimukti.accounter.web.client.ui.reports.CustomerTransactionHistoryAction;
import com.vimukti.accounter.web.client.ui.reports.ECSalesListAction;
import com.vimukti.accounter.web.client.ui.reports.ECSalesListDetailAction;
import com.vimukti.accounter.web.client.ui.reports.ExpenseReportAction;
import com.vimukti.accounter.web.client.ui.reports.GLReportAction;
import com.vimukti.accounter.web.client.ui.reports.MostProfitableCustomersAction;
import com.vimukti.accounter.web.client.ui.reports.ProfitAndLossAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByItemDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByItemSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByVendorDetailsAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseByVendorSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseClosedOrderAction;
import com.vimukti.accounter.web.client.ui.reports.PurchaseOpenOrderAction;
import com.vimukti.accounter.web.client.ui.reports.ReportsHomeAction;
import com.vimukti.accounter.web.client.ui.reports.ReverseChargeListAction;
import com.vimukti.accounter.web.client.ui.reports.ReverseChargeListDetailAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByCustomerDetailAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByCustomerSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByItemDetailAction;
import com.vimukti.accounter.web.client.ui.reports.SalesByItemSummaryAction;
import com.vimukti.accounter.web.client.ui.reports.SalesClosedOrderAction;
import com.vimukti.accounter.web.client.ui.reports.SalesOpenOrderAction;
import com.vimukti.accounter.web.client.ui.reports.SalesTaxLiabilityAction;
import com.vimukti.accounter.web.client.ui.reports.StatementReportAction;
import com.vimukti.accounter.web.client.ui.reports.TransactionDetailByAccountAction;
import com.vimukti.accounter.web.client.ui.reports.TransactionDetailByTaxItemAction;
import com.vimukti.accounter.web.client.ui.reports.TrialBalanceAction;
import com.vimukti.accounter.web.client.ui.reports.VAT100ReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATDetailsReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATItemSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATSummaryReportAction;
import com.vimukti.accounter.web.client.ui.reports.VATUncategorisedAmountsReportAction;
import com.vimukti.accounter.web.client.ui.reports.VaTItemDetailAction;
import com.vimukti.accounter.web.client.ui.reports.VendorTransactionHistoryAction;
import com.vimukti.accounter.web.client.ui.vendors.VendorsListAction;

/**
 * This factory is to get All Reports Actions Object
 * 
 * @author vimukti3
 * 
 */
public class ReportsActionFactory extends AbstractActionFactory {

	public static ReportsHomeAction getReportsHomeAction() {
		return new ReportsHomeAction(actionsConstants.reportsHome(),
				"/images/icons/report/report_home.png");
	}

	public static VendorsListAction getVendorListAction() {
		return new VendorsListAction(UIUtils.getVendorString(Accounter
				.constants().suppliersList(), Accounter
				.constants().vendorsList()),
				"/images/icons/report/reports.png");
	}

	public static BalanceSheetAction getBalanceSheetAction() {
		return new BalanceSheetAction(Accounter.constants()
				.balanceSheet(), "/images/icons/report/reports.png");
	}

	public static CashFlowStatementAction getCashFlowStatementAction() {
		return new CashFlowStatementAction(Accounter
				.constants().cashFlowReport(),
				"/images/icons/report/reports.png");
	}

	public static TrialBalanceAction getTrialBalanceAction() {
		return new TrialBalanceAction(Accounter.constants()
				.trialBalance(), "/images/icons/report/reports.png");
	}

	public static TransactionDetailByAccountAction getTransactionDetailByAccountAction() {
		return new TransactionDetailByAccountAction(Accounter
				.constants().transactionDetailByAccount(),
				"/images/icons/report/reports.png");
	}

	public static GLReportAction getGlReportAction() {
		return new GLReportAction(Accounter.constants()
				.generalLedgerReport(), "/images/icons/report/reports.png");
	}

	public static SalesTaxLiabilityAction getSalesTaxLiabilityAction() {
		return new SalesTaxLiabilityAction(Accounter
				.constants().salesTaxLiability(),
				"/images/icons/report/reports.png");
	}

	public static TransactionDetailByTaxItemAction getTransactionDetailByTaxItemAction() {
		return new TransactionDetailByTaxItemAction(Accounter
				.constants().transactionDetailByTaxItem(),
				"/images/icons/report/reports.png");
	}

	// public static YtdProfitComparedToLastYearAction
	// getYtdProfitComparedToLastYearAction() {
	// return new YtdProfitComparedToLastYearAction(
	// "Ytd Profit Compared To Last Year","/images/icons/report/reports.png");
	// }

	public static ARAgingDetailAction getArAgingDetailAction() {
		return new ARAgingDetailAction(Accounter.constants()
				.ARAgeingDetail(), "/images/icons/report/reports.png");
	}

	public static CustomerTransactionHistoryAction getCustomerTransactionHistoryAction() {
		return new CustomerTransactionHistoryAction(Accounter
				.constants().customerTransactionHistory(),
				"/images/icons/report/reports.png");
	}

	public static MostProfitableCustomersAction getMostProfitableCustomersAction() {
		return new MostProfitableCustomersAction(Accounter
				.constants().mostProfitableCustomers(),
				"/images/icons/report/reports.png");
	}

	public static SalesByCustomerSummaryAction getSalesByCustomerSummaryAction() {
		return new SalesByCustomerSummaryAction(Accounter
				.constants().salesByCustomerSumary(),
				"/images/icons/report/reports.png");
	}

	public static SalesByCustomerDetailAction getSalesByCustomerDetailAction() {
		return new SalesByCustomerDetailAction(Accounter
				.constants().salesByCustomerDetail(),
				"/images/icons/report/reports.png");
	}

	public static SalesByItemSummaryAction getSalesByItemSummmaryAction() {
		return new SalesByItemSummaryAction(Accounter
				.constants().SalesByItemSummary(),
				"/images/icons/report/reports.png");
	}

	public static SalesByItemDetailAction getSalesByItemDetailAction() {
		return new SalesByItemDetailAction(Accounter
				.constants().SalesByItemDetail(),
				"/images/icons/report/reports.png");
	}

	// public static YtdSalesComparedToLastYearAction
	// getYtdSalesComparedToLastYearAction() {
	// return new YtdSalesComparedToLastYearAction(
	// "Ytd Sales Compared To Last Year","/images/icons/report/reports.png");
	// }

	public static APAgingDetailAction getAorpAgingDetailAction() {
		return new APAgingDetailAction(Accounter.constants()
				.APAgeingDetail(), "/images/icons/report/reports.png");
	}

	public static VendorTransactionHistoryAction getVendorTransactionHistoryAction() {
		return new VendorTransactionHistoryAction(UIUtils.getVendorString(
				Accounter.constants()
						.supplierTransactionHistory(), Accounter
						.constants().vendorTransactionHistory()),
				"/images/icons/report/reports.png");
	}

	// public static AmountsDueToVendorsAction getAmountsDueToVendorsAction() {
	// return new AmountsDueToVendorsAction("Amounts Due To"
	// + UIUtils.getVendorString("Suppliers", "Vendors"),
	// "/images/icons/report/reports.png");
	// }

	public static ProfitAndLossAction getProfitAndLossAction() {
		return new ProfitAndLossAction(actionsConstants.profitAndLoss(),
				"/images/icons/report/reports.png");
	}

	public static SalesByItemSummaryAction getSalesByItemSummaryAction() {
		return new SalesByItemSummaryAction(Accounter
				.constants().SalesByItemSummary(),
				"/images/icons/report/reports.png");
	}

	public static PurchaseByVendorSummaryAction getPurchaseByVendorSummaryAction() {
		return new PurchaseByVendorSummaryAction(UIUtils.getVendorString(
				Accounter.constants()
						.purchaseBySupplierSummary(), Accounter
						.constants().purchaseByVendorSummary()),
				"/images/icons/report/reports.png");
	}

	public static PurchaseByVendorDetailsAction getPurchaseByVendorDetailAction() {
		return new PurchaseByVendorDetailsAction(UIUtils.getVendorString(
				Accounter.constants()
						.purchaseBySupplierDetail(), Accounter
						.constants().purchaseByVendorDetail()),
				"/images/icons/report/reports.png");
	}

	public static PurchaseByItemSummaryAction getPurchaseByItemSummaryAction() {
		return new PurchaseByItemSummaryAction(Accounter
				.constants().purchaseByItemSummary(),
				"/images/icons/report/reports.png");
	}

	public static PurchaseByItemDetailsAction getPurchaseByItemAction() {
		return new PurchaseByItemDetailsAction(Accounter
				.constants().purchaseByItemDetail(),
				"/images/icons/report/reports.png");
	}

	public static PurchaseOpenOrderAction getPurchaseOpenOrderAction() {
		return new PurchaseOpenOrderAction(Accounter
				.constants().purchaseOrderReport(),
				"/images/icons/report/reports.png");
	}

	public static PurchaseClosedOrderAction getPurchaseClosedOrderAction() {
		return new PurchaseClosedOrderAction(Accounter
				.constants().purchaseClosedOrder(),
				"/images/icons/report/reports.png");
	}

	public static SalesOpenOrderAction getSalesOpenOrderAction() {
		return new SalesOpenOrderAction(Accounter
				.constants().salesOrderReport(),
				"/images/icons/report/reports.png");
	}

	public static SalesClosedOrderAction getSalesCloseOrderAction() {
		return new SalesClosedOrderAction(Accounter
				.constants().salesCloseOrder(),
				"/images/icons/report/reports.png");
	}

	public static VATDetailsReportAction getVATDetailsReportAction() {
		return new VATDetailsReportAction(Accounter
				.constants().vatDetail());
	}

	public static VATSummaryReportAction getVATSummaryReportAction() {
		return new VATSummaryReportAction(Accounter
				.constants().priorVATReturns(),
				"/images/icons/report/reports.png");
	}

	public static VAT100ReportAction getVAT100ReportAction() {
		return new VAT100ReportAction(Accounter.constants()
				.VAT100(), "/images/icons/report/reports.png");
	}

	public static VATUncategorisedAmountsReportAction getVATUncategorisedAmountsReportAction() {
		return new VATUncategorisedAmountsReportAction(Accounter
				.constants().uncategorisedVATAmounts(),
				"/images/icons/report/reports.png");
	}

	public static VATItemSummaryReportAction getVATItemSummaryReportAction() {
		return new VATItemSummaryReportAction(Accounter
				.constants().VATItemSummary(),
				"/images/icons/report/reports.png");
	}

	public static ECSalesListAction getECSalesListAction() {
		return new ECSalesListAction(Accounter.constants()
				.ECSalesList(), "/images/icons/report/reports.png");
	}

	public static ECSalesListDetailAction getECSalesListDetailAction() {
		return new ECSalesListDetailAction(Accounter
				.constants().ECSalesListDetailReport(),
				"/images/icons/report/reports.png");
	}

	public static ReverseChargeListAction getReverseChargeListAction() {
		return new ReverseChargeListAction(Accounter
				.constants().reverseChargeList(),
				"/images/icons/report/reports.png");
	}

	public static ReverseChargeListDetailAction getReverseChargeListDetailAction() {
		return new ReverseChargeListDetailAction(Accounter
				.constants().reverseChargeListDetailReport(),
				"/images/icons/report/reports.png");
	}

	public static VaTItemDetailAction getVaTItemDetailAction() {
		return new VaTItemDetailAction(Accounter.constants()
				.VATItemDetailReport(), "/images/icons/report/reports.png");
	}

	public static ARAgingSummaryReportAction getArAgingSummaryReportAction() {
		return new ARAgingSummaryReportAction(Accounter
				.constants().ARAgeingSummary(),
				"/images/icons/report/reports.png");
	}

	public static Action getAorpAgingSummaryReportAction() {
		return new APAgingSummaryReportAction(Accounter
				.constants().APAgeingSummary(),
				"/images/icons/report/reports.png");
	}

	public static ExpenseReportAction getExpenseReportAction() {
		return new ExpenseReportAction(Accounter.constants()
				.expenseReport(), "/images/icons/report/reports.png");
	}

	public static DepositDetailAction getDetailReportAction() {
		return new DepositDetailAction(Accounter.constants()
				.depositDetail(), "");
	}

	public static CheckDetailReportAction getCheckDetailReport() {
		return new CheckDetailReportAction(Accounter
				.constants().checkDetail(), "");
	}

	public static Action getStatementReport() {
		return new StatementReportAction(Accounter
				.constants().customerStatement(),
				"/images/icons/report/reports.png");
	}
}
