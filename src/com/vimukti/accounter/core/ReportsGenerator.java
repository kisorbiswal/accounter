package com.vimukti.accounter.core;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;
import com.vimukti.accounter.web.client.ui.reports.BudgetServerReport;
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
import com.vimukti.accounter.web.client.ui.serverreports.MISC1099TransactionDetailServerReport;
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
import com.vimukti.accounter.web.client.ui.serverreports.SalesByLocationDetailsServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByLocationsummaryServerReport;
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
import com.vimukti.accounter.web.server.FinanceTool;

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
	public final static int REPORT_TYPE_SALESBYLOCATIONDETAIL = 151;
	public final static int REPORT_TYPE_SALESBYLOCATIONDETAILFORLOCATION = 152;
	public final static int REPORT_TYPE_PROFITANDLOSSBYLOCATION = 153;
	public final static int REPORT_TYPE_BUDGET = 154;
	private static final int REPORT_TYPE_1099TRANSACTIONDETAIL = 158;
	public final static int REPORT_TYPE_SALESBYCLASSDETAIL = 159;
	public final static int REPORT_TYPE_SALESBYCLASSDETAILFORCLASS = 160;
	public final static int REPORT_TYPE_PROFITANDLOSSBYCLASS = 161;

	private static int companyType;
	private ClientCompanyPreferences preferences = Global.get().preferences();

	private int reportType;
	private FinanceDate startDate;
	private FinanceDate endDate;
	private String navigateObjectName;
	private String status;
	private int boxNo;
	private long vendorId;
	private Company company;

	public static final int GENERATIONTYPEPDF = 1001;
	public static final int GENERATIONTYPECSV = 1002;

	public ReportsGenerator(int reportType, long starDate, long endDate,
			String navigateObjectName, int generationType, int companyType,
			Company company) {
		ReportsGenerator.companyType = companyType;
		this.reportType = reportType;
		this.startDate = new FinanceDate(starDate);
		this.endDate = new FinanceDate(endDate);
		this.navigateObjectName = navigateObjectName;
		this.company = company;
	}

	public ReportsGenerator(int reportType, long starDate, long endDate,
			String navigateObjectName, int generationType, String status,
			int companyType, Company company) {
		ReportsGenerator.companyType = companyType;
		this.reportType = reportType;
		this.startDate = new FinanceDate(starDate);
		this.endDate = new FinanceDate(endDate);
		this.navigateObjectName = navigateObjectName;
		this.status = status;
		this.company = company;
	}

	public ReportsGenerator(int reportType, long startDate, long endDate,
			String navigateObjectName, int generationtypecsv, long vendorId,
			int boxNo, int companyType, Company company) {
		ReportsGenerator.companyType = companyType;
		this.reportType = reportType;
		this.startDate = new FinanceDate(startDate);
		this.endDate = new FinanceDate(endDate);
		this.navigateObjectName = navigateObjectName;
		this.vendorId = vendorId;
		this.boxNo = boxNo;
		this.company = company;
	}

	public String generate(FinanceTool financeTool, int generationType)
			throws IOException {
		ReportGridTemplate.generationType = generationType;

		ReportGridTemplate<?> gridTemplate = getReportGridTemplate(financeTool,
				generationType);
		return gridTemplate.getBody(Global.get().constants());
	}

	public ReportGridTemplate<?> getReportGridTemplate(
			final FinanceTool finaTool, int generationType1) throws IOException {

		AccounterReportServiceImpl reportsSerivce = new AccounterReportServiceImpl() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected FinanceTool getFinanceTool() {
				return finaTool;
			}

		};
		switch (reportType) {
		case REPORT_TYPE_PROFITANDLOSS:

			ProfitAndLossServerReport profitAndLossServerReport = new ProfitAndLossServerReport(
					startDate.getDate(), endDate.getDate(), generationType1) {

				@Override
				public ClientFinanceDate getCurrentFiscalYearEndDate() {
					return Utility_R.getCurrentFiscalYearEndDate(company);
				}

				@Override
				public ClientFinanceDate getCurrentFiscalYearStartDate() {
					return Utility_R.getCurrentFiscalYearStartDate();
				}

				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(profitAndLossServerReport, finaTool);
			profitAndLossServerReport.resetVariables();
			try {
				profitAndLossServerReport.onResultSuccess(reportsSerivce
						.getProfitAndLossReport(
								startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return profitAndLossServerReport.getGridTemplate();
		case REPORT_TYPE_SALESBYCLASSDETAILFORCLASS:
			return generateSalesByClassorLocationTemplate(reportsSerivce,
					false, generationType1, finaTool);

		case REPORT_TYPE_SALESBYLOCATIONDETAILFORLOCATION:
			return generateSalesByClassorLocationTemplate(reportsSerivce, true,
					generationType1, finaTool);
		case REPORT_TYPE_SALESBYCLASSDETAIL:
			return generateSalesByLocationorClassDetailReport(false,
					generationType1, finaTool, reportsSerivce);
		case REPORT_TYPE_SALESBYLOCATIONDETAIL:
			return generateSalesByLocationorClassDetailReport(true,
					generationType1, finaTool, reportsSerivce);
		case REPORT_TYPE_BALANCESHEET:
			BalanceSheetServerReport balanceSheetServerReport = new BalanceSheetServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(balanceSheetServerReport, finaTool);
			balanceSheetServerReport.resetVariables();
			try {
				balanceSheetServerReport.onResultSuccess(reportsSerivce
						.getBalanceSheetReport(startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return balanceSheetServerReport.getGridTemplate();
		case REPORT_TYPE_TRIALBALANCE:
			TrialBalanceServerReport trialBalanceServerReport = new TrialBalanceServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(trialBalanceServerReport, finaTool);
			trialBalanceServerReport.resetVariables();
			try {
				trialBalanceServerReport.onResultSuccess(reportsSerivce
						.getTrialBalance(startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return trialBalanceServerReport.getGridTemplate();
		case REPORT_TYPE_TRANSACTIONDETAILBYTAXITEM:
			TransactionDetailByTaxItemServerReport transactionDetailByTaxItemServerReport = new TransactionDetailByTaxItemServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(transactionDetailByTaxItemServerReport, finaTool);
			transactionDetailByTaxItemServerReport.resetVariables();
			try {
				if (status == null || status.isEmpty()) {
					transactionDetailByTaxItemServerReport
							.onResultSuccess(finaTool
									.getTransactionDetailByTaxItem(startDate,
											endDate));
				} else {
					transactionDetailByTaxItemServerReport
							.onResultSuccess(finaTool
									.getTransactionDetailByTaxItem(status,
											startDate, endDate));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return transactionDetailByTaxItemServerReport.getGridTemplate();
		case REPORT_TYPE_TRANSACTIONDETAILBYACCOUNT:
			TransactionDetailByAccountServerReport transactionDetailByAccountServerReport = new TransactionDetailByAccountServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(transactionDetailByAccountServerReport, finaTool);
			transactionDetailByAccountServerReport.resetVariables();
			try {
				if (status == null || status.isEmpty()) {
					transactionDetailByAccountServerReport
							.onResultSuccess(reportsSerivce
									.getTransactionDetailByAccount(
											startDate.toClientFinanceDate(),
											endDate.toClientFinanceDate()));
				} else {
					transactionDetailByAccountServerReport
							.onResultSuccess(reportsSerivce
									.getTransactionDetailByAccount(status,
											startDate.toClientFinanceDate(),
											endDate.toClientFinanceDate()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return transactionDetailByAccountServerReport.getGridTemplate();
		case REPORT_TYPE_EXPENSE:
			ExpenseServerReport expenseServerReport = new ExpenseServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(expenseServerReport, finaTool);
			expenseServerReport.resetVariables();
			try {
				expenseServerReport.onResultSuccess(reportsSerivce
						.getExpenseReportByType(Integer.parseInt(status),
								startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return expenseServerReport.getGridTemplate();
		case REPORT_TYPE_AR_AGEINGSUMMARY:
			ARAgingSummaryServerReport arAgingSummaryServerReport = new ARAgingSummaryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(arAgingSummaryServerReport, finaTool);
			arAgingSummaryServerReport.resetVariables();
			try {
				arAgingSummaryServerReport.onResultSuccess(reportsSerivce
						.getDebitors(startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return arAgingSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_AR_AGEINGDETAIL:
			ARAgingDetailServerReport arAgingDetailServerReport = new ARAgingDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(arAgingDetailServerReport, finaTool);
			arAgingDetailServerReport.resetVariables();
			try {
				if (status == null || status.isEmpty()) {
					arAgingDetailServerReport.onResultSuccess(reportsSerivce
							.getAgedDebtors(startDate.toClientFinanceDate(),
									endDate.toClientFinanceDate()));
				} else {
					arAgingDetailServerReport.onResultSuccess(reportsSerivce
							.getAgedDebtors(status,
									startDate.toClientFinanceDate(),
									endDate.toClientFinanceDate()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return arAgingDetailServerReport.getGridTemplate();
		case REPORT_TYPE_CUSTOMERTRANSACTIONHISTORY:
			CustomerTransactionHistoryServerReport customerTransactionHistoryServerReport = new CustomerTransactionHistoryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(customerTransactionHistoryServerReport, finaTool);
			customerTransactionHistoryServerReport.resetVariables();
			try {
				customerTransactionHistoryServerReport.onResultSuccess(finaTool
						.getCustomerTransactionHistory(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return customerTransactionHistoryServerReport.getGridTemplate();
		case REPORT_TYPE_CUSTOMERTRANSACTIONHISTORY1:
			CustomerTransactionHistoryServerReport1 customerTransactionHistoryServerReport1 = new CustomerTransactionHistoryServerReport1(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(customerTransactionHistoryServerReport1, finaTool);
			customerTransactionHistoryServerReport1.resetVariables();
			try {
				customerTransactionHistoryServerReport1
						.onResultSuccess(finaTool
								.getCustomerTransactionHistory(startDate,
										endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return customerTransactionHistoryServerReport1.getGridTemplate();
		case REPORT_TYPE_SALESBYCUSTOMERSUMMARY:
			SalesByCustomerSummaryServerReport salesByCustomerSummaryServerReport = new SalesByCustomerSummaryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(salesByCustomerSummaryServerReport, finaTool);
			salesByCustomerSummaryServerReport.resetVariables();
			try {
				salesByCustomerSummaryServerReport.onResultSuccess(finaTool
						.getSalesByCustomerSummary(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesByCustomerSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_SALESBYCUSTOMERDETAIL:
			SalesByCustomerDetailServerReport salesByCustomerDetailServerReport = new SalesByCustomerDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(salesByCustomerDetailServerReport, finaTool);
			salesByCustomerDetailServerReport.resetVariables();
			try {
				if (status == null || status.isEmpty()) {
					salesByCustomerDetailServerReport
							.onResultSuccess(finaTool
									.getSalesByCustomerDetailReport(startDate,
											endDate));
				} else {
					salesByCustomerDetailServerReport.onResultSuccess(finaTool
							.getSalesByCustomerDetailReport(status, startDate,
									endDate));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesByCustomerDetailServerReport.getGridTemplate();
		case REPORT_TYPE_SALESBYITEMSUMMARY:
			SalesByItemSummaryServerReport salesByItemSummaryServerReport = new SalesByItemSummaryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(salesByItemSummaryServerReport, finaTool);
			salesByItemSummaryServerReport.resetVariables();
			try {
				salesByItemSummaryServerReport.onResultSuccess(finaTool
						.getSalesByItemSummary(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesByItemSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_SALESBYITEMDETAIL:
			SalesByItemDetailServerReport salesByItemDetailServerReport = new SalesByItemDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(salesByItemDetailServerReport, finaTool);
			salesByItemDetailServerReport.resetVariables();
			try {
				if (status == null || status.isEmpty()) {
					salesByItemDetailServerReport.onResultSuccess(finaTool
							.getSalesByItemDetail(startDate, endDate));
				} else {
					salesByItemDetailServerReport.onResultSuccess(finaTool
							.getSalesByItemDetail(status, startDate, endDate));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesByItemDetailServerReport.getGridTemplate();
		case REPORT_TYPE_SALESORDER_OPEN:
			SalesOpenOrderServerReport salesOpenOrderServerReport = new SalesOpenOrderServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(salesOpenOrderServerReport, finaTool);
			salesOpenOrderServerReport.resetVariables();
			try {
				if (Integer.parseInt(status) == 1) {
					salesOpenOrderServerReport.onResultSuccess(reportsSerivce
							.getSalesOpenOrderReport(
									startDate.toClientFinanceDate(),
									endDate.toClientFinanceDate()));
				} else if (Integer.parseInt(status) == 2) {
					salesOpenOrderServerReport.onResultSuccess(reportsSerivce
							.getSalesCompletedOrderReport(
									startDate.toClientFinanceDate(),
									endDate.toClientFinanceDate()));
				} else if (Integer.parseInt(status) == 3) {
					salesOpenOrderServerReport.onResultSuccess(reportsSerivce
							.getSalesCancelledOrderReport(
									startDate.toClientFinanceDate(),
									endDate.toClientFinanceDate()));
				} else {
					salesOpenOrderServerReport.onResultSuccess(reportsSerivce
							.getSalesOrderReport(
									startDate.toClientFinanceDate(),
									endDate.toClientFinanceDate()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesOpenOrderServerReport.getGridTemplate();
		case REPORT_TYPE_SALESORDER_CLOSE:
			SalesClosedOrderServerReport salesClosedOrderServerReport = new SalesClosedOrderServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(salesClosedOrderServerReport, finaTool);
			salesClosedOrderServerReport.resetVariables();
			try {
				salesClosedOrderServerReport.onResultSuccess(finaTool
						.getClosedSalesOrders(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesClosedOrderServerReport.getGridTemplate();
		case REPORT_TYPE_AP_AGEINGSUMMARY:
			APAgingSummaryServerReport apAgingSummaryServerReport = new APAgingSummaryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(apAgingSummaryServerReport, finaTool);
			apAgingSummaryServerReport.resetVariables();
			try {
				apAgingSummaryServerReport.onResultSuccess(reportsSerivce
						.getCreditors(startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return apAgingSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_AP_AGEINGDETAIL:
			APAgingDetailServerReport apAgingDetailServerReport = new APAgingDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(apAgingDetailServerReport, finaTool);
			apAgingDetailServerReport.resetVariables();
			try {
				if (status == null || status.isEmpty()) {
					apAgingDetailServerReport.onResultSuccess(finaTool
							.getAgedCreditors(startDate, endDate, company));
				} else {
					apAgingDetailServerReport.onResultSuccess(reportsSerivce
							.getAgedCreditors(status,
									startDate.toClientFinanceDate(),
									endDate.toClientFinanceDate()));
				}
				// apAgingDetailServerReport.onSuccess(finaTool.getAgedCreditors(
				// startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return apAgingDetailServerReport.getGridTemplate();
		case REPORT_TYPE_VENDORTRANSACTIONHISTORY:
			VendorTransactionHistoryServerReport vendorTransactionHistoryServerReport = new VendorTransactionHistoryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {

				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(vendorTransactionHistoryServerReport, finaTool);
			vendorTransactionHistoryServerReport.resetVariables();
			try {
				vendorTransactionHistoryServerReport.onResultSuccess(finaTool
						.getVendorTransactionHistory(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vendorTransactionHistoryServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEBYVENDORSUMMARY:
			PurchaseByVendorSummaryServerReport purchaseByVendorSummaryServerReport = new PurchaseByVendorSummaryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(purchaseByVendorSummaryServerReport, finaTool);
			purchaseByVendorSummaryServerReport.resetVariables();
			try {
				purchaseByVendorSummaryServerReport.onResultSuccess(finaTool
						.getPurchasesByVendorSummary(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseByVendorSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEBYVENDORDETAIL:
			PurchaseByVendorDetailServerReport purchaseByVendorDetailServerReport = new PurchaseByVendorDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(purchaseByVendorDetailServerReport, finaTool);
			purchaseByVendorDetailServerReport.resetVariables();
			try {
				if (status == null || status.isEmpty()) {

					purchaseByVendorDetailServerReport.onResultSuccess(finaTool
							.getPurchasesByVendorDetail(startDate, endDate));
				} else {
					purchaseByVendorDetailServerReport.onResultSuccess(finaTool
							.getPurchasesByVendorDetail(status, startDate,
									endDate));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseByVendorDetailServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEBYITEMSUMMARY:
			PurchaseByItemSummaryServerReport purchaseByItemSummaryServerReport = new PurchaseByItemSummaryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(purchaseByItemSummaryServerReport, finaTool);
			purchaseByItemSummaryServerReport.resetVariables();
			try {

				purchaseByItemSummaryServerReport.onResultSuccess(finaTool
						.getPurchasesByItemSummary(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseByItemSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEBYITEMDETAIL:
			PurchaseByItemDetailServerReport purchaseByItemDetailServerReport = new PurchaseByItemDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(purchaseByItemDetailServerReport, finaTool);
			purchaseByItemDetailServerReport.resetVariables();
			try {
				if (status == null || status.isEmpty()) {
					purchaseByItemDetailServerReport.onResultSuccess(finaTool
							.getPurchasesByItemDetail(startDate, endDate));
				} else {
					purchaseByItemDetailServerReport.onResultSuccess(finaTool
							.getPurchasesByItemDetail(status, startDate,
									endDate));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseByItemDetailServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEORDER_OPEN:
			PurchaseOpenOrderServerReport purchaseOpenOrderServerReport = new PurchaseOpenOrderServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(purchaseOpenOrderServerReport, finaTool);
			purchaseOpenOrderServerReport.resetVariables();
			try {
				if (Integer.parseInt(status) == 1) {
					purchaseOpenOrderServerReport
							.onResultSuccess(reportsSerivce
									.getPurchaseOpenOrderReport(
											startDate.toClientFinanceDate(),
											endDate.toClientFinanceDate()));
				} else if (Integer.parseInt(status) == 2) {
					purchaseOpenOrderServerReport
							.onResultSuccess(reportsSerivce
									.getPurchaseCompletedOrderReport(
											startDate.toClientFinanceDate(),
											endDate.toClientFinanceDate()));
				} else if (Integer.parseInt(status) == 3) {
					purchaseOpenOrderServerReport
							.onResultSuccess(reportsSerivce
									.getPurchaseCancelledOrderReport(
											startDate.toClientFinanceDate(),
											endDate.toClientFinanceDate()));
				} else {
					purchaseOpenOrderServerReport
							.onResultSuccess(reportsSerivce
									.getPurchaseOrderReport(
											startDate.toClientFinanceDate(),
											endDate.toClientFinanceDate()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseOpenOrderServerReport.getGridTemplate();
		case REPORT_TYPE_PURCHASEORDER_CLOSE:
			PurchaseClosedOrderServerReport purchaseClosedOrderServerReport = new PurchaseClosedOrderServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(purchaseClosedOrderServerReport, finaTool);
			purchaseClosedOrderServerReport.resetVariables();
			try {
				purchaseClosedOrderServerReport.onResultSuccess(finaTool
						.getClosedPurchaseOrders(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return purchaseClosedOrderServerReport.getGridTemplate();
		case REPORT_TYPE_PRIORVATRETURNS:
			PriorVATReturnsServerReport priorVATReturnsServerReport = new PriorVATReturnsServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(priorVATReturnsServerReport, finaTool);
			priorVATReturnsServerReport.resetVariables();
			try {
				priorVATReturnsServerReport.onResultSuccess(reportsSerivce
						.getPriorReturnVATSummary(Long.parseLong(status),
								endDate.toClientFinanceDate()));

			} catch (Exception e) {
				e.printStackTrace();
			}
			return priorVATReturnsServerReport.getGridTemplate();
		case REPORT_TYPE_VAT100:
			VAT100ServerReport vat100ServerReport = new VAT100ServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(vat100ServerReport, finaTool);
			vat100ServerReport.resetVariables();
			try {
				vat100ServerReport.onResultSuccess(reportsSerivce
						.getVAT100Report(Long.parseLong(status),
								startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vat100ServerReport.getGridTemplate();
		case REPORT_TYPE_VATDETAIL:
			VATDetailServerReportView vatDetailServerReportView = new VATDetailServerReportView(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(vatDetailServerReportView, finaTool);
			vatDetailServerReportView.resetVariables();
			try {
				vatDetailServerReportView.onResultSuccess(finaTool
						.getVATDetailReport(startDate, endDate, company));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vatDetailServerReportView.getGridTemplate();
		case REPORT_TYPE_VATITEMDETAIL:
			VATItemDetailServerReport vatItemDetailServerReport = new VATItemDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(vatItemDetailServerReport, finaTool);
			vatItemDetailServerReport.resetVariables();
			try {
				vatItemDetailServerReport.onResultSuccess(finaTool
						.getVATItemDetailReport(status, startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vatItemDetailServerReport.getGridTemplate();
		case REPORT_TYPE_UNCATEGORISEDVATAMOUNT:
			VATUncategorisedAmountsServerReport vatUncategorisedAmountsServerReport = new VATUncategorisedAmountsServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(vatUncategorisedAmountsServerReport, finaTool);
			vatUncategorisedAmountsServerReport.resetVariables();
			try {
				vatUncategorisedAmountsServerReport.onResultSuccess(finaTool
						.getUncategorisedAmountsReport(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vatUncategorisedAmountsServerReport.getGridTemplate();
		case REPORT_TYPE_VATITEMSUMMARY:
			VATItemSummaryServerReport vatItemSummaryServerReport = new VATItemSummaryServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(vatItemSummaryServerReport, finaTool);
			vatItemSummaryServerReport.resetVariables();
			try {
				vatItemSummaryServerReport.onResultSuccess(finaTool
						.getVATItemSummaryReport(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return vatItemSummaryServerReport.getGridTemplate();
		case REPORT_TYPE_ECSCALESLIST:
			ECSalesListServerReport ecSalesListServerReport = new ECSalesListServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
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
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);

				}
			};
			updateReport(ecSalesListServerReport, finaTool);
			ecSalesListServerReport.resetVariables();
			try {
				ecSalesListServerReport.onResultSuccess(finaTool
						.getECSalesListReport(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ecSalesListServerReport.getGridTemplate();
		case REPORT_TYPE_ECSCALESLISTDETAIL:
			ECSalesListDetailServerReport ecSalesListDetailServerReport = new ECSalesListDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(ecSalesListDetailServerReport, finaTool);
			ecSalesListDetailServerReport.resetVariables();
			try {
				ecSalesListDetailServerReport
						.onResultSuccess(finaTool.getECSalesListDetailReport(
								status, startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return ecSalesListDetailServerReport.getGridTemplate();
		case REPORT_TYPE_SALESTAXLIABILITY:
			SalesTaxLiabilityServerReport salesTaxLiabilityServerReport = new SalesTaxLiabilityServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(salesTaxLiabilityServerReport, finaTool);
			salesTaxLiabilityServerReport.resetVariables();
			try {
				salesTaxLiabilityServerReport.onResultSuccess(finaTool
						.getSalesTaxLiabilityReport(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return salesTaxLiabilityServerReport.getGridTemplate();
		case REPORT_TYPE_REVERSECHARGELIST:
			ReverseChargeListServerReport reverseChargeListServerReport = new ReverseChargeListServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(reverseChargeListServerReport, finaTool);
			reverseChargeListServerReport.resetVariables();
			try {
				reverseChargeListServerReport.onResultSuccess(finaTool
						.getReverseChargeListReport(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return reverseChargeListServerReport.getGridTemplate();
		case REPORT_TYPE_REVERSECHARGELISTDETAIL:
			ReverseChargeListDetailServerReport reverseChargeListDetailServerReport = new ReverseChargeListDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(reverseChargeListDetailServerReport, finaTool);
			reverseChargeListDetailServerReport.resetVariables();
			try {
				reverseChargeListDetailServerReport.onResultSuccess(finaTool
						.getReverseChargeListDetailReport(status, startDate,
								endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return reverseChargeListDetailServerReport.getGridTemplate();
		case REPORT_TYPE_MOSTPROFITABLECUSTOMER:
			MostProfitableCustomerServerReport mostProfitableCustomerServerReport = new MostProfitableCustomerServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(mostProfitableCustomerServerReport, finaTool);
			mostProfitableCustomerServerReport.resetVariables();
			try {
				mostProfitableCustomerServerReport.onResultSuccess(finaTool
						.getMostProfitableCustomers(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mostProfitableCustomerServerReport.getGridTemplate();
		case REPORT_TYPE_CASHFLOWSTATEMENT:
			CashFlowStatementServerReport cashFlowStatementServerReport = new CashFlowStatementServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(cashFlowStatementServerReport, finaTool);
			cashFlowStatementServerReport.resetVariables();
			try {
				cashFlowStatementServerReport.onResultSuccess(reportsSerivce
						.getCashFlowReport(startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return cashFlowStatementServerReport.getGridTemplate();
		case REPORT_TYPE_AMOUNTSDUETOVENDOR:
			AmountsDueToVendorServerReport amountsDueToVendorServerReport = new AmountsDueToVendorServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(amountsDueToVendorServerReport, finaTool);
			amountsDueToVendorServerReport.resetVariables();
			try {
				amountsDueToVendorServerReport.onResultSuccess(finaTool
						.getAmountsDueToVendor(startDate, endDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return amountsDueToVendorServerReport.getGridTemplate();
		case REPORT_TYPE_CUSTOMERSTATEMENT:
			StatementServerReport statementReport = new StatementServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(statementReport, finaTool);
			statementReport.resetVariables();
			try {
				statementReport.onResultSuccess(finaTool
						.getPayeeStatementsList(Long.parseLong(status),
								startDate, endDate, company));

			} catch (Exception e) {
				e.printStackTrace();
			}
			return statementReport.getGridTemplate();
		case REPORT_TYPE_PROFITANDLOSSBYCLASS:
			return generateProfitandLossByLocationorClass(false,
					generationType1, finaTool, reportsSerivce);
		case REPORT_TYPE_PROFITANDLOSSBYLOCATION:
			return generateProfitandLossByLocationorClass(true,
					generationType1, finaTool, reportsSerivce);

		case REPORT_TYPE_1099TRANSACTIONDETAIL:
			MISC1099TransactionDetailServerReport misc1099TransactionDetailServerReport = new MISC1099TransactionDetailServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {
				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}
			};
			updateReport(misc1099TransactionDetailServerReport, finaTool);
			misc1099TransactionDetailServerReport.resetVariables();
			try {
				misc1099TransactionDetailServerReport.onResultSuccess(finaTool
						.getPaybillsByVendorAndBoxNumber(this.startDate,
								this.endDate, this.vendorId, this.boxNo));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return misc1099TransactionDetailServerReport.getGridTemplate();
		case REPORT_TYPE_BUDGET:
			BudgetServerReport budgetServerReport = new BudgetServerReport(
					this.startDate.getDate(), this.endDate.getDate(),
					generationType1) {

				@Override
				public String getDateByCompanyType(ClientFinanceDate date) {
					ReportsGenerator.companyType = getCompany().accountingType;
					ReportUtility.companyType = companyType;
					return ReportsGenerator.getDateByCompanyType(date);
				}

			};
			updateReport(budgetServerReport, finaTool);
			budgetServerReport.resetVariables();
			try {
				budgetServerReport
						.onResultSuccess(reportsSerivce.getBudgetItemsList(
								Integer.parseInt(status),
								startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate(), generationType1));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return budgetServerReport.getGridTemplate();
		default:
			break;
		}

		return null;
	}

	private Company getCompany() {
		return company;
	}

	private ReportGridTemplate<?> generateProfitandLossByLocationorClass(
			boolean isLocation, int generationType1, FinanceTool finaTool,
			AccounterReportServiceImpl reportsSerivce) {
		ProfitAndLossServerReport profitAndLossBylocationServerReport = new ProfitAndLossServerReport(
				startDate.getDate(), endDate.getDate(), generationType1) {

			@Override
			public ClientFinanceDate getCurrentFiscalYearEndDate() {
				return Utility_R.getCurrentFiscalYearEndDate(company);
			}

			@Override
			public ClientFinanceDate getCurrentFiscalYearStartDate() {
				return Utility_R.getCurrentFiscalYearStartDate();
			}

			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				ReportsGenerator.companyType = getCompany().accountingType;
				ReportUtility.companyType = companyType;
				ReportUtility.companyType = companyType;
				return ReportsGenerator.getDateByCompanyType(date);
			}
		};
		updateReport(profitAndLossBylocationServerReport, finaTool);
		profitAndLossBylocationServerReport.resetVariables();
		try {
			profitAndLossBylocationServerReport.onResultSuccess(reportsSerivce
					.getProfitAndLossReport(startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return profitAndLossBylocationServerReport.getGridTemplate();
	}

	private ReportGridTemplate<?> generateSalesByLocationorClassDetailReport(
			boolean isLocation, int generationType1, FinanceTool finaTool,
			AccounterReportServiceImpl reportsSerivce) {
		SalesByLocationDetailsServerReport salesByLocationDetailsServerReport = new SalesByLocationDetailsServerReport(
				startDate.getDate(), endDate.getDate(), generationType1) {

			@Override
			public ClientFinanceDate getCurrentFiscalYearEndDate() {
				return Utility_R.getCurrentFiscalYearEndDate(company);
			}

			@Override
			public ClientFinanceDate getCurrentFiscalYearStartDate() {
				return Utility_R.getCurrentFiscalYearStartDate();
			}

			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				ReportsGenerator.companyType = getCompany().accountingType;
				ReportUtility.companyType = companyType;
				ReportUtility.companyType = companyType;
				return ReportsGenerator.getDateByCompanyType(date);
			}
		};
		updateReport(salesByLocationDetailsServerReport, finaTool);
		salesByLocationDetailsServerReport.resetVariables();
		try {
			salesByLocationDetailsServerReport.onResultSuccess(reportsSerivce
					.getSalesByLocationDetailsReport(isLocation,
							startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesByLocationDetailsServerReport.getGridTemplate();
	}

	private ReportGridTemplate<?> generateSalesByClassorLocationTemplate(
			AccounterReportServiceImpl reportsSerivce, boolean isLocation,
			int generationType1, FinanceTool finaTool) {
		SalesByLocationsummaryServerReport salesByLocationsummaryServerReport = new SalesByLocationsummaryServerReport(
				startDate.getDate(), endDate.getDate(), generationType1) {

			@Override
			public ClientFinanceDate getCurrentFiscalYearEndDate() {
				return Utility_R.getCurrentFiscalYearEndDate(company);
			}

			@Override
			public ClientFinanceDate getCurrentFiscalYearStartDate() {
				return Utility_R.getCurrentFiscalYearStartDate();
			}

			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				ReportsGenerator.companyType = getCompany().accountingType;
				ReportUtility.companyType = companyType;
				ReportUtility.companyType = companyType;
				return ReportsGenerator.getDateByCompanyType(date);
			}
		};
		updateReport(salesByLocationsummaryServerReport, finaTool);
		salesByLocationsummaryServerReport.resetVariables();
		try {
			salesByLocationsummaryServerReport.onResultSuccess(reportsSerivce
					.getSalesByLocationSummaryReport(isLocation,
							startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesByLocationsummaryServerReport.getGridTemplate();
	}

	private void updateReport(AbstractFinaneReport<?> abstractFinaneReport,
			FinanceTool financeTool) {
		abstractFinaneReport.setNavigatedObjectName(navigateObjectName);
		// abstractFinaneReport.setFinaceTool(financeTool);
		abstractFinaneReport.setStartAndEndDates(
				startDate.toClientFinanceDate(), endDate.toClientFinanceDate());
		abstractFinaneReport.makeReportRequest(startDate.getDate(),
				endDate.getDate());
		abstractFinaneReport.setCompanyType(getCompany().accountingType);
		abstractFinaneReport.setCurrentFiscalYearStartDate(Utility_R
				.getCurrentFiscalYearStartDate());
		abstractFinaneReport.setCurrentFiscalYearEndDate(Utility_R
				.getCurrentFiscalYearEndDate(company));
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
			if (ReportsGenerator.companyType == Company.ACCOUNTING_TYPE_UK)
				return "Supplier Transaction History Report";
			if (ReportsGenerator.companyType == Company.ACCOUNTING_TYPE_US)
				return "Vendor Transaction History Report";
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
		case REPORT_TYPE_SALESBYLOCATIONDETAIL:
			return "Sales By Location Details Report";
		case REPORT_TYPE_SALESBYLOCATIONDETAILFORLOCATION:
			return "Sales By Location Detail For Location";
		case REPORT_TYPE_PROFITANDLOSSBYLOCATION:
			return "Profit and Loss by Location";
		case REPORT_TYPE_BUDGET:
			return "Budget Report";
		case REPORT_TYPE_1099TRANSACTIONDETAIL:
			return "1099 Transaction Detail By Vendor";
		case REPORT_TYPE_SALESBYCLASSDETAIL:
			return "Sales By Class Details Report";
		case REPORT_TYPE_SALESBYCLASSDETAILFORCLASS:
			return "Sales By Class Detail For Class";
		case REPORT_TYPE_PROFITANDLOSSBYCLASS:
			return "Profit and Loss by Class";
		default:
			break;
		}
		return "";
	}

	public static String getDateByCompanyType(ClientFinanceDate date) {
		if (date == null) {
			return "";
		}
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
