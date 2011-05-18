package com.vimukti.accounter.core;

import java.text.SimpleDateFormat;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.data.InvalidSessionException;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;
import com.vimukti.accounter.web.client.ui.serverreports.APAgingDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.APAgingSummaryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ARAgingDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ARAgingSummaryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.AbstractFinaneReport;
import com.vimukti.accounter.web.client.ui.serverreports.AmountsDueToVendorServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.BalanceSheetServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.CashFlowStatementServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.CustomerTransactionHistoryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.CustomerTransactionHistoryServerReport1;
import com.vimukti.accounter.web.client.ui.serverreports.ECSalesListDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ECSalesListServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ExpenseServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.MostProfitableCustomerServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.PriorVATReturnsServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ProfitAndLossServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseByItemDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseByItemSummaryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseByVendorDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseByVendorSummaryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseClosedOrderServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.PurchaseOpenOrderServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.ReverseChargeListDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReverseChargeListServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByCustomerDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByCustomerSummaryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByItemDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByItemSummaryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.SalesClosedOrderServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.SalesOpenOrderServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.SalesTaxLiabilityServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.StatementServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.TransactionDetailByAccountServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.TransactionDetailByTaxItemServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.TrialBalanceServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.VAT100ServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.VATDetailServerReportView;
import com.vimukti.accounter.web.client.ui.serverreports.VATItemDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.VATItemSummaryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.VATUncategorisedAmountsServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.VendorTransactionHistoryServerReport;
import com.vimukti.accounter.web.server.AccounterReportServiceImpl;
import com.vimukti.accounter.workspace.tool.FinanceTool;
import com.vimukti.accounter.workspace.tool.IFinanceTool;

public class ReportsGenerator {

	public final static int REPORT_TYPE_PROFITANDLOSS = 111;
	public final static int REPORT_TYPE_BALANCESHEET = 112;
	public final static int REPORT_TYPE_TRIALBALANCE = 113;
	public final static int REPORT_TYPE_TRANSACTIONDETAILBYTAXITEM = 114;
	// public final static int REPORT_TYPE_TRANSACTIONDETAILBYTAXCODE = 114;
	public final static int REPORT_TYPE_TRANSACTIONDETAILBYACCOUNT = 115;
	public final static int REPORT_TYPE_EXPENSE = 116;
	public final static int REPORT_TYPE_AR_AGEINGSUMMARY = 117;
	public final static int REPORT_TYPE_AR_AGEINGDETAIL = 118;
	public final static int REPORT_TYPE_CUSTOMERTRANSACTIONHISTORY = 119;
	public final static int REPORT_TYPE_CUSTOMERTRANSACTIONHISTORY1 = 120;
	public final static int REPORT_TYPE_SALESBYCUSTOMERSUMMARY = 121;
	public final static int REPORT_TYPE_SALESBYCUSTOMERDETAIL = 122;
	public final static int REPORT_TYPE_SALESBYITEMSUMMARY = 123;
	public final static int REPORT_TYPE_SALESBYITEMDETAIL = 124;
	public final static int REPORT_TYPE_SALESORDER_OPEN = 125;
	public final static int REPORT_TYPE_SALESORDER_CLOSE = 126;
	public final static int REPORT_TYPE_AP_AGEINGSUMMARY = 127;
	public final static int REPORT_TYPE_AP_AGEINGDETAIL = 128;
	public final static int REPORT_TYPE_VENDORTRANSACTIONHISTORY = 129;
	public final static int REPORT_TYPE_PURCHASEBYVENDORSUMMARY = 130;
	public final static int REPORT_TYPE_PURCHASEBYVENDORDETAIL = 131;
	public final static int REPORT_TYPE_PURCHASEBYITEMSUMMARY = 132;
	public final static int REPORT_TYPE_PURCHASEBYITEMDETAIL = 133;
	public final static int REPORT_TYPE_PURCHASEORDER_OPEN = 134;
	public final static int REPORT_TYPE_PURCHASEORDER_CLOSE = 135;
	public final static int REPORT_TYPE_PRIORVATRETURNS = 136;
	public final static int REPORT_TYPE_VAT100 = 137;
	public final static int REPORT_TYPE_VATDETAIL = 138;
	public final static int REPORT_TYPE_VATITEMDETAIL = 139;
	public final static int REPORT_TYPE_UNCATEGORISEDVATAMOUNT = 140;
	public final static int REPORT_TYPE_VATITEMSUMMARY = 141;
	public final static int REPORT_TYPE_ECSCALESLIST = 142;
	public final static int REPORT_TYPE_ECSCALESLISTDETAIL = 143;
	public final static int REPORT_TYPE_SALESTAXLIABILITY = 144;
	public final static int REPORT_TYPE_REVERSECHARGELIST = 145;
	public final static int REPORT_TYPE_REVERSECHARGELISTDETAIL = 146;
	public final static int REPORT_TYPE_MOSTPROFITABLECUSTOMER = 147;
	public final static int REPORT_TYPE_CASHFLOWSTATEMENT = 148;
	public final static int REPORT_TYPE_AMOUNTSDUETOVENDOR = 149;
	public final static int REPORT_TYPE_CUSTOMERSTATEMENT = 150;

	private static int companyType;

	private int reportType;
	private long startDate;
	private long endDate;
	private String navigateObjectName;
	private String status;

	public static final int GENERATIONTYPEPDF = 1001;
	public static final int GENERATIONTYPECSV = 1002;

	public ReportsGenerator(int reportType, long starDate, long endDate,
			String navigateObjectName, int generationType) {
		this.reportType = reportType;
		this.startDate = starDate;
		this.endDate = endDate;
		this.navigateObjectName = navigateObjectName;
	}

	public ReportsGenerator(int reportType, long starDate, long endDate,
			String navigateObjectName, int generationType, String status) {
		this.reportType = reportType;
		this.startDate = starDate;
		this.endDate = endDate;
		this.navigateObjectName = navigateObjectName;
		this.status = status;
	}

	public String generate(FinanceTool financeTool, int generationType) {
		ReportGridTemplate.generationType = generationType;
		ReportGridTemplate<?> gridTemplate = getReportGridTemplate(financeTool,
				generationType);
		return gridTemplate.getBody();
	}

	@SuppressWarnings("serial")
	public ReportGridTemplate<?> getReportGridTemplate(
			final FinanceTool finaTool, int generationType1) {

		AccounterReportServiceImpl reportsSerivce = new AccounterReportServiceImpl() {
			@Override
			protected IFinanceTool getFinanceTool()
					throws InvalidSessionException {
				return finaTool;
			}
		};
		switch (reportType) {
		case REPORT_TYPE_PROFITANDLOSS:

			ProfitAndLossServerReport profitAndLossServerReport = new ProfitAndLossServerReport(
					startDate, endDate, generationType1) {

				@Override
				public ClientFinanceDate getCurrentFiscalYearEndDate() {
					return Utility_R.getCurrentFiscalYearEndDate();
				}

				@Override
				public ClientFinanceDate getCurrentFiscalYearStartDate() {
					return Utility_R.getCurrentFiscalYearStartDate();
				}

				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(profitAndLossServerReport, finaTool);
			profitAndLossServerReport.resetVariables();
			try {
				profitAndLossServerReport.onSuccess(reportsSerivce
						.getProfitAndLossReport(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return profitAndLossServerReport.getGridTemplate();
		case REPORT_TYPE_BALANCESHEET:
			BalanceSheetServerReport balanceSheetServerReport = new BalanceSheetServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(balanceSheetServerReport, finaTool);
			balanceSheetServerReport.resetVariables();
			try {
				balanceSheetServerReport.onSuccess(reportsSerivce
						.getBalanceSheetReport(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return balanceSheetServerReport.getGridTemplate();
		case REPORT_TYPE_TRIALBALANCE:
			TrialBalanceServerReport trialBalanceServerReport = new TrialBalanceServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(trialBalanceServerReport, finaTool);
			trialBalanceServerReport.resetVariables();
			try {
				trialBalanceServerReport.onSuccess(reportsSerivce
						.getTrialBalance(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return trialBalanceServerReport.getGridTemplate();
		case REPORT_TYPE_TRANSACTIONDETAILBYTAXITEM:
			TransactionDetailByTaxItemServerReport transactionDetailByTaxItemServerReport = new TransactionDetailByTaxItemServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(transactionDetailByTaxItemServerReport, finaTool);
			transactionDetailByTaxItemServerReport.resetVariables();
			try {
				transactionDetailByTaxItemServerReport.onSuccess(finaTool
						.getTransactionDetailByTaxItem(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return transactionDetailByTaxItemServerReport.getGridTemplate();
		case REPORT_TYPE_TRANSACTIONDETAILBYACCOUNT:
			TransactionDetailByAccountServerReport transactionDetailByAccountServerReport = new TransactionDetailByAccountServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(transactionDetailByAccountServerReport, finaTool);
			transactionDetailByAccountServerReport.resetVariables();
			try {
				transactionDetailByAccountServerReport.onSuccess(reportsSerivce
						.getTransactionDetailByAccount(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return transactionDetailByAccountServerReport.getGridTemplate();
		case REPORT_TYPE_EXPENSE:
			ExpenseServerReport expenseServerReport = new ExpenseServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(expenseServerReport, finaTool);
			expenseServerReport.resetVariables();
			try {
				expenseServerReport.onSuccess(reportsSerivce
						.getExpenseReportByType(Integer.parseInt(status),
								startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return expenseServerReport.getGridTemplate();
		case REPORT_TYPE_AR_AGEINGSUMMARY:
			ARAgingSummaryServerReport arAgingSummaryServerReport = new ARAgingSummaryServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(arAgingSummaryServerReport, finaTool);
			arAgingSummaryServerReport.resetVariables();
			try {
				arAgingSummaryServerReport.onSuccess(reportsSerivce
						.getDebitors(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return arAgingSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_AR_AGEINGDETAIL:
			ARAgingDetailServerReport arAgingDetailServerReport = new ARAgingDetailServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(arAgingDetailServerReport, finaTool);
			arAgingDetailServerReport.resetVariables();
			try {
				arAgingDetailServerReport.onSuccess(finaTool.getAgedDebtors(
						startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return arAgingDetailServerReport.getGridTemplate();
		case REPORT_TYPE_CUSTOMERTRANSACTIONHISTORY:
			CustomerTransactionHistoryServerReport customerTransactionHistoryServerReport = new CustomerTransactionHistoryServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(customerTransactionHistoryServerReport, finaTool);
			customerTransactionHistoryServerReport.resetVariables();
			try {
				customerTransactionHistoryServerReport.onSuccess(finaTool
						.getCustomerTransactionHistory(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return customerTransactionHistoryServerReport.getGridTemplate();
		case REPORT_TYPE_CUSTOMERTRANSACTIONHISTORY1:
			CustomerTransactionHistoryServerReport1 customerTransactionHistoryServerReport1 = new CustomerTransactionHistoryServerReport1(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(customerTransactionHistoryServerReport1, finaTool);
			customerTransactionHistoryServerReport1.resetVariables();
			try {
				customerTransactionHistoryServerReport1.onSuccess(finaTool
						.getCustomerTransactionHistory(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return customerTransactionHistoryServerReport1.getGridTemplate();
		case REPORT_TYPE_SALESBYCUSTOMERSUMMARY:
			SalesByCustomerSummaryServerReport salesByCustomerSummaryServerReport = new SalesByCustomerSummaryServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(salesByCustomerSummaryServerReport, finaTool);
			salesByCustomerSummaryServerReport.resetVariables();
			try {
				salesByCustomerSummaryServerReport.onSuccess(finaTool
						.getSalesByCustomerSummary(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesByCustomerSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_SALESBYCUSTOMERDETAIL:
			SalesByCustomerDetailServerReport salesByCustomerDetailServerReport = new SalesByCustomerDetailServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(salesByCustomerDetailServerReport, finaTool);
			salesByCustomerDetailServerReport.resetVariables();
			try {
				salesByCustomerDetailServerReport.onSuccess(finaTool
						.getSalesByCustomerDetailReport(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesByCustomerDetailServerReport.getGridTemplate();
		case REPORT_TYPE_SALESBYITEMSUMMARY:
			SalesByItemSummaryServerReport salesByItemSummaryServerReport = new SalesByItemSummaryServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(salesByItemSummaryServerReport, finaTool);
			salesByItemSummaryServerReport.resetVariables();
			try {
				salesByItemSummaryServerReport.onSuccess(finaTool
						.getSalesByItemSummary(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesByItemSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_SALESBYITEMDETAIL:
			SalesByItemDetailServerReport salesByItemDetailServerReport = new SalesByItemDetailServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(salesByItemDetailServerReport, finaTool);
			salesByItemDetailServerReport.resetVariables();
			try {
				if (status == null) {
					salesByItemDetailServerReport.onSuccess(finaTool
							.getSalesByItemDetail(startDate, endDate));
				} else {
					salesByItemDetailServerReport.onSuccess(finaTool
							.getSalesByItemDetail(status, startDate, endDate));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesByItemDetailServerReport.getGridTemplate();
		case REPORT_TYPE_SALESORDER_OPEN:
			SalesOpenOrderServerReport salesOpenOrderServerReport = new SalesOpenOrderServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(salesOpenOrderServerReport, finaTool);
			salesOpenOrderServerReport.resetVariables();
			try {
				if (Integer.parseInt(status) == 1) {
					salesOpenOrderServerReport.onSuccess(reportsSerivce
							.getSalesOpenOrderReport(startDate, endDate));
				} else if (Integer.parseInt(status) == 2) {
					salesOpenOrderServerReport.onSuccess(reportsSerivce
							.getSalesCompletedOrderReport(startDate, endDate));
				} else if (Integer.parseInt(status) == 3) {
					salesOpenOrderServerReport.onSuccess(reportsSerivce
							.getSalesCancelledOrderReport(startDate, endDate));
				} else {
					salesOpenOrderServerReport.onSuccess(reportsSerivce
							.getSalesOrderReport(startDate, endDate));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesOpenOrderServerReport.getGridTemplate();
		case REPORT_TYPE_SALESORDER_CLOSE:
			SalesClosedOrderServerReport salesClosedOrderServerReport = new SalesClosedOrderServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(salesClosedOrderServerReport, finaTool);
			salesClosedOrderServerReport.resetVariables();
			try {
				salesClosedOrderServerReport.onSuccess(finaTool
						.getClosedSalesOrders(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesClosedOrderServerReport.getGridTemplate();
		case REPORT_TYPE_AP_AGEINGSUMMARY:
			APAgingSummaryServerReport apAgingSummaryServerReport = new APAgingSummaryServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(apAgingSummaryServerReport, finaTool);
			apAgingSummaryServerReport.resetVariables();
			try {
				apAgingSummaryServerReport.onSuccess(reportsSerivce
						.getCreditors(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return apAgingSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_AP_AGEINGDETAIL:
			APAgingDetailServerReport apAgingDetailServerReport = new APAgingDetailServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(apAgingDetailServerReport, finaTool);
			apAgingDetailServerReport.resetVariables();
			try {
				if (status == null) {
					apAgingDetailServerReport.onSuccess(finaTool
							.getAgedCreditors(startDate, endDate));
				} else {
					apAgingDetailServerReport.onSuccess(reportsSerivce
							.getAgedCreditors(status, startDate, endDate));
				}
				// apAgingDetailServerReport.onSuccess(finaTool.getAgedCreditors(
				// startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return apAgingDetailServerReport.getGridTemplate();
		case REPORT_TYPE_VENDORTRANSACTIONHISTORY:
			VendorTransactionHistoryServerReport vendorTransactionHistoryServerReport = new VendorTransactionHistoryServerReport(
					this.startDate, this.endDate, generationType1) {

				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(vendorTransactionHistoryServerReport, finaTool);
			vendorTransactionHistoryServerReport.resetVariables();
			try {
				vendorTransactionHistoryServerReport.onSuccess(finaTool
						.getVendorTransactionHistory(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vendorTransactionHistoryServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEBYVENDORSUMMARY:
			PurchaseByVendorSummaryServerReport purchaseByVendorSummaryServerReport = new PurchaseByVendorSummaryServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(purchaseByVendorSummaryServerReport, finaTool);
			purchaseByVendorSummaryServerReport.resetVariables();
			try {
				purchaseByVendorSummaryServerReport.onSuccess(finaTool
						.getPurchasesByVendorSummary(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseByVendorSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEBYVENDORDETAIL:
			PurchaseByVendorDetailServerReport purchaseByVendorDetailServerReport = new PurchaseByVendorDetailServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(purchaseByVendorDetailServerReport, finaTool);
			purchaseByVendorDetailServerReport.resetVariables();
			try {
				if (status == null) {

					purchaseByVendorDetailServerReport.onSuccess(finaTool
							.getPurchasesByVendorDetail(startDate, endDate));
				} else {
					purchaseByVendorDetailServerReport.onSuccess(finaTool
							.getPurchasesByVendorDetail(status, startDate,
									endDate));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseByVendorDetailServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEBYITEMSUMMARY:
			PurchaseByItemSummaryServerReport purchaseByItemSummaryServerReport = new PurchaseByItemSummaryServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(purchaseByItemSummaryServerReport, finaTool);
			purchaseByItemSummaryServerReport.resetVariables();
			try {

				purchaseByItemSummaryServerReport.onSuccess(finaTool
						.getPurchasesByItemSummary(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseByItemSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEBYITEMDETAIL:
			PurchaseByItemDetailServerReport purchaseByItemDetailServerReport = new PurchaseByItemDetailServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(purchaseByItemDetailServerReport, finaTool);
			purchaseByItemDetailServerReport.resetVariables();
			try {
				if (status == null) {
					purchaseByItemDetailServerReport.onSuccess(finaTool
							.getPurchasesByItemDetail(startDate, endDate));
				} else {
					purchaseByItemDetailServerReport.onSuccess(finaTool
							.getPurchasesByItemDetail(status, startDate,
									endDate));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseByItemDetailServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEORDER_OPEN:
			PurchaseOpenOrderServerReport purchaseOpenOrderServerReport = new PurchaseOpenOrderServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(purchaseOpenOrderServerReport, finaTool);
			purchaseOpenOrderServerReport.resetVariables();
			try {
				if (Integer.parseInt(status) == 1) {
					purchaseOpenOrderServerReport.onSuccess(reportsSerivce
							.getPurchaseOpenOrderReport(startDate, endDate));
				} else if (Integer.parseInt(status) == 2) {
					purchaseOpenOrderServerReport
							.onSuccess(reportsSerivce
									.getPurchaseCompletedOrderReport(startDate,
											endDate));
				} else if (Integer.parseInt(status) == 3) {
					purchaseOpenOrderServerReport
							.onSuccess(reportsSerivce
									.getPurchaseCancelledOrderReport(startDate,
											endDate));
				} else {
					purchaseOpenOrderServerReport.onSuccess(reportsSerivce
							.getPurchaseOrderReport(startDate, endDate));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseOpenOrderServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEORDER_CLOSE:
			PurchaseClosedOrderServerReport purchaseClosedOrderServerReport = new PurchaseClosedOrderServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(purchaseClosedOrderServerReport, finaTool);
			purchaseClosedOrderServerReport.resetVariables();
			try {
				purchaseClosedOrderServerReport.onSuccess(finaTool
						.getClosedPurchaseOrders(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseClosedOrderServerReport.getGridTemplate();
		case REPORT_TYPE_PRIORVATRETURNS:
			PriorVATReturnsServerReport priorVATReturnsServerReport = new PriorVATReturnsServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(priorVATReturnsServerReport, finaTool);
			priorVATReturnsServerReport.resetVariables();
			try {
				priorVATReturnsServerReport.onSuccess(reportsSerivce
						.getPriorReturnVATSummary(status, endDate));

			} catch (Exception e) {
				e.printStackTrace();
			}
			return priorVATReturnsServerReport.getGridTemplate();
		case REPORT_TYPE_VAT100:
			VAT100ServerReport vat100ServerReport = new VAT100ServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(vat100ServerReport, finaTool);
			vat100ServerReport.resetVariables();
			try {
				vat100ServerReport.onSuccess(reportsSerivce.getVAT100Report(
						status, startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vat100ServerReport.getGridTemplate();
		case REPORT_TYPE_VATDETAIL:
			VATDetailServerReportView vatDetailServerReportView = new VATDetailServerReportView(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(vatDetailServerReportView, finaTool);
			vatDetailServerReportView.resetVariables();
			try {
				vatDetailServerReportView.onSuccess(finaTool
						.getVATDetailReport(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vatDetailServerReportView.getGridTemplate();
		case REPORT_TYPE_VATITEMDETAIL:
			VATItemDetailServerReport vatItemDetailServerReport = new VATItemDetailServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(vatItemDetailServerReport, finaTool);
			vatItemDetailServerReport.resetVariables();
			try {
				vatItemDetailServerReport.onSuccess(finaTool
						.getVATItemDetailReport(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vatItemDetailServerReport.getGridTemplate();
		case REPORT_TYPE_UNCATEGORISEDVATAMOUNT:
			VATUncategorisedAmountsServerReport vatUncategorisedAmountsServerReport = new VATUncategorisedAmountsServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(vatUncategorisedAmountsServerReport, finaTool);
			vatUncategorisedAmountsServerReport.resetVariables();
			try {
				vatUncategorisedAmountsServerReport.onSuccess(finaTool
						.getUncategorisedAmountsReport(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vatUncategorisedAmountsServerReport.getGridTemplate();
		case REPORT_TYPE_VATITEMSUMMARY:
			VATItemSummaryServerReport vatItemSummaryServerReport = new VATItemSummaryServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(vatItemSummaryServerReport, finaTool);
			vatItemSummaryServerReport.resetVariables();
			try {
				vatItemSummaryServerReport.onSuccess(finaTool
						.getVATItemSummaryReport(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vatItemSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_ECSCALESLIST:
			ECSalesListServerReport ecSalesListServerReport = new ECSalesListServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String dateFormat(ClientFinanceDate date) {
					try {
						if (date == null)
							return "";
						SimpleDateFormat dateFormatter = new SimpleDateFormat(
								"dd/MM/yyyy");
						String format = dateFormatter.format(date
								.getDateAsObject());
						return format;

					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}

				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(ecSalesListServerReport, finaTool);
			ecSalesListServerReport.resetVariables();
			try {
				ecSalesListServerReport.onSuccess(finaTool
						.getECSalesListReport(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ecSalesListServerReport.getGridTemplate();
		case REPORT_TYPE_ECSCALESLISTDETAIL:
			ECSalesListDetailServerReport ecSalesListDetailServerReport = new ECSalesListDetailServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(ecSalesListDetailServerReport, finaTool);
			ecSalesListDetailServerReport.resetVariables();
			try {
				ecSalesListDetailServerReport
						.onSuccess(finaTool.getECSalesListDetailReport(status,
								startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ecSalesListDetailServerReport.getGridTemplate();
		case REPORT_TYPE_SALESTAXLIABILITY:
			SalesTaxLiabilityServerReport salesTaxLiabilityServerReport = new SalesTaxLiabilityServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(salesTaxLiabilityServerReport, finaTool);
			salesTaxLiabilityServerReport.resetVariables();
			try {
				salesTaxLiabilityServerReport.onSuccess(finaTool
						.getSalesTaxLiabilityReport(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesTaxLiabilityServerReport.getGridTemplate();
		case REPORT_TYPE_REVERSECHARGELIST:
			ReverseChargeListServerReport reverseChargeListServerReport = new ReverseChargeListServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(reverseChargeListServerReport, finaTool);
			reverseChargeListServerReport.resetVariables();
			try {
				reverseChargeListServerReport.onSuccess(finaTool
						.getReverseChargeListReport(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return reverseChargeListServerReport.getGridTemplate();
		case REPORT_TYPE_REVERSECHARGELISTDETAIL:
			ReverseChargeListDetailServerReport reverseChargeListDetailServerReport = new ReverseChargeListDetailServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(reverseChargeListDetailServerReport, finaTool);
			reverseChargeListDetailServerReport.resetVariables();
			try {
				reverseChargeListDetailServerReport.onSuccess(finaTool
						.getReverseChargeListDetailReport(status, startDate,
								endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return reverseChargeListDetailServerReport.getGridTemplate();
		case REPORT_TYPE_MOSTPROFITABLECUSTOMER:
			MostProfitableCustomerServerReport mostProfitableCustomerServerReport = new MostProfitableCustomerServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(mostProfitableCustomerServerReport, finaTool);
			mostProfitableCustomerServerReport.resetVariables();
			try {
				mostProfitableCustomerServerReport.onSuccess(finaTool
						.getMostProfitableCustomers(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mostProfitableCustomerServerReport.getGridTemplate();
		case REPORT_TYPE_CASHFLOWSTATEMENT:
			CashFlowStatementServerReport cashFlowStatementServerReport = new CashFlowStatementServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(cashFlowStatementServerReport, finaTool);
			cashFlowStatementServerReport.resetVariables();
			try {
				cashFlowStatementServerReport.onSuccess(reportsSerivce
						.getCashFlowReport(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return cashFlowStatementServerReport.getGridTemplate();
		case REPORT_TYPE_AMOUNTSDUETOVENDOR:
			AmountsDueToVendorServerReport amountsDueToVendorServerReport = new AmountsDueToVendorServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(amountsDueToVendorServerReport, finaTool);
			amountsDueToVendorServerReport.resetVariables();
			try {
				amountsDueToVendorServerReport.onSuccess(finaTool
						.getAmountsDueToVendor(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return amountsDueToVendorServerReport.getGridTemplate();
		case REPORT_TYPE_CUSTOMERSTATEMENT:
			StatementServerReport statementReport = new StatementServerReport(
					this.startDate, this.endDate, generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = Company.getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(statementReport, finaTool);
			statementReport.resetVariables();
			try {
				statementReport.onSuccess(finaTool.getPayeeStatementsList(
						status, new ClientFinanceDate().getTime(), startDate,
						endDate, 0, false, false, 0.00, false, false));

			} catch (Exception e) {
				e.printStackTrace();
			}
			return statementReport.getGridTemplate();
		default:
			break;
		}
		return null;
	}

	private void updateReport(AbstractFinaneReport<?> abstractFinaneReport,
			FinanceTool financeTool) {
		abstractFinaneReport.setNavigatedObjectName(navigateObjectName);
		// abstractFinaneReport.setFinaceTool(financeTool);
		abstractFinaneReport.setStartAndEndDates(startDate, endDate);
		abstractFinaneReport.makeReportRequest(startDate, endDate);
		abstractFinaneReport
				.setCompanyType(Company.getCompany().accountingType);
		abstractFinaneReport.setCurrentFiscalYearStartDate(Utility_R
				.getCurrentFiscalYearStartDate());
		abstractFinaneReport.setCurrentFiscalYearEndDate(Utility_R
				.getCurrentFiscalYearEndDate());
	}

	public static String getReportHtml() {
		return "";
	}

	public static String getReportNameByType(int reportType) {
		switch (reportType) {
		case REPORT_TYPE_PROFITANDLOSS:
			return "Profit And Loss Report";
		case REPORT_TYPE_BALANCESHEET:
			return "Balance Sheet Report";
		case REPORT_TYPE_TRIALBALANCE:
			return "Trial Balance Report";
		case REPORT_TYPE_TRANSACTIONDETAILBYTAXITEM:
			return "Transaction Detail By Tax Item Report";
		case REPORT_TYPE_TRANSACTIONDETAILBYACCOUNT:
			return "Transaction Detail By Account Report";
		case REPORT_TYPE_EXPENSE:
			return "Expense Report";
		case REPORT_TYPE_AR_AGEINGSUMMARY:
			return "AR Ageing Summary Report";
		case REPORT_TYPE_AR_AGEINGDETAIL:
			return "AR Ageing Detail Report";
		case REPORT_TYPE_CUSTOMERTRANSACTIONHISTORY:
			return "Customer Transaction History Report";
		case REPORT_TYPE_CUSTOMERTRANSACTIONHISTORY1:
			return "Customer Transaction History1 Report";
		case REPORT_TYPE_SALESBYCUSTOMERSUMMARY:
			return "Sales By Customer Summary Report";
		case REPORT_TYPE_SALESBYCUSTOMERDETAIL:
			return "Sales By Customer Detail Report";
		case REPORT_TYPE_SALESBYITEMSUMMARY:
			return "Sales By Item Summary Report";
		case REPORT_TYPE_SALESBYITEMDETAIL:
			return "Sales By Item Detail Report";
		case REPORT_TYPE_SALESORDER_OPEN:
			return "Sales Open Order Report";
		case REPORT_TYPE_SALESORDER_CLOSE:
			return "Sales Closed Order Report";
		case REPORT_TYPE_AP_AGEINGSUMMARY:
			return "AP Ageing Summary Report";
		case REPORT_TYPE_AP_AGEINGDETAIL:
			return "AP Ageing Detail Report";
		case REPORT_TYPE_VENDORTRANSACTIONHISTORY:
			return "Supplier Transaction History Report";
		case REPORT_TYPE_PURCHASEBYVENDORSUMMARY:
			return "Purchase By Supplier Summary Report";
		case REPORT_TYPE_PURCHASEBYVENDORDETAIL:
			return "Purchase By Supplier Detail Report";
		case REPORT_TYPE_PURCHASEBYITEMSUMMARY:
			return "Purchase By Item Summary Report";
		case REPORT_TYPE_PURCHASEBYITEMDETAIL:
			return "Purchase By Item Detail Report";
		case REPORT_TYPE_PURCHASEORDER_OPEN:
			return "Purchase Open Order Report";
		case REPORT_TYPE_PURCHASEORDER_CLOSE:
			return "Purchase Closed Order Report";
		case REPORT_TYPE_PRIORVATRETURNS:
			return "Prior VAT Return Report";
		case REPORT_TYPE_VAT100:
			return "VAT 100 Report";
		case REPORT_TYPE_VATDETAIL:
			return "VAT Detail Report";
		case REPORT_TYPE_VATITEMDETAIL:
			return "VAT Item Detail Report";
		case REPORT_TYPE_UNCATEGORISEDVATAMOUNT:
			return "Uncategorised VAT Amount Report";
		case REPORT_TYPE_VATITEMSUMMARY:
			return "VAT Item Summary Report";
		case REPORT_TYPE_ECSCALESLIST:
			return "EC Scales List Report";
		case REPORT_TYPE_ECSCALESLISTDETAIL:
			return "EC Scales List Detail Report";
		case REPORT_TYPE_SALESTAXLIABILITY:
			return "Sales Tax Liability Report";
		case REPORT_TYPE_REVERSECHARGELIST:
			return "Reverse Charge List Report";
		case REPORT_TYPE_REVERSECHARGELISTDETAIL:
			return "Reverse Charge List Detail Report";
		case REPORT_TYPE_MOSTPROFITABLECUSTOMER:
			return "Most Profitable Customer Report";
		case REPORT_TYPE_CASHFLOWSTATEMENT:
			return "Cash Flow Statement Report";
		case REPORT_TYPE_AMOUNTSDUETOVENDOR:
			return "Amount Due To Vendor Report";
		case REPORT_TYPE_CUSTOMERSTATEMENT:
			return "Customer Statement";
		default:
			break;
		}
		return "";
	}

	public static String getDateByCompanyType(ClientFinanceDate date) {
		if (companyType == Company.ACCOUNTING_TYPE_UK) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
			String format = dateFormatter.format(date.getDateAsObject());
			return format;
		} else if (companyType == Company.ACCOUNTING_TYPE_US) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
			String format = dateFormatter.format(date.getDateAsObject());
			return format;
		} else {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
			String format = dateFormatter.format(date.getDateAsObject());
			return format;
		}
	}

}
