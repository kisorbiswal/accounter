package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.Lists.DummyDebitor;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.client.core.reports.AmountsDueToVendor;
import com.vimukti.accounter.web.client.core.reports.DepositDetail;
import com.vimukti.accounter.web.client.core.reports.ECSalesList;
import com.vimukti.accounter.web.client.core.reports.ECSalesListDetail;
import com.vimukti.accounter.web.client.core.reports.ExpenseList;
import com.vimukti.accounter.web.client.core.reports.MostProfitableCustomers;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeListDetail;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.core.reports.SalesTaxLiability;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.core.reports.UncategorisedAmountsReport;
import com.vimukti.accounter.web.client.core.reports.VATItemDetail;
import com.vimukti.accounter.web.client.core.reports.VATItemSummary;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.reports.CheckDetailReport;
import com.vimukti.accounter.web.server.AccounterReportServiceImpl;

public class XmlReportsApiServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String methodName = getMethodName(req);
		Date startDate = getDate(req.getParameter("StartDate"));
		Date endDate = getDate(req.getParameter("EndDate"));

		AccounterReportServiceImpl accounterReportServiceImpl = getAccounterReportServiceImpl();

		ClientFinanceDate clientFinanceStartDate = new ClientFinanceDate(
				startDate);
		ClientFinanceDate clientFinanceEndDate = new ClientFinanceDate(endDate);

		if (methodName.equals("salesbycustomersummary")) {
			accounterReportServiceImpl.getSalesByCustomerSummary(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("payeestatements")) {

		} else if (methodName.equals("agedcreditors")) {
			List<AgedDebtors> agedCreditors = accounterReportServiceImpl
					.getAgedCreditors(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("checkdetails")) {
			Long paymentmethod = (Long) req.getAttribute("PaymentMethod");
			List<CheckDetailReport> checkDetailReport = accounterReportServiceImpl
					.getCheckDetailReport(paymentmethod,
							clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("depositdetail")) {
			List<DepositDetail> depositDetail = accounterReportServiceImpl
					.getDepositDetail(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("expensereports")) {
			int status = (Integer) req.getAttribute("Status");
			List<ExpenseList> expenseReportByType = accounterReportServiceImpl
					.getExpenseReportByType(status, clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("creditors")) {
			try {
				List<DummyDebitor> creditors = accounterReportServiceImpl
						.getCreditors(clientFinanceStartDate,
								clientFinanceEndDate);
			} catch (AccounterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (methodName.equals("ageddebtors")) {
			List<AgedDebtors> agedDebtors = accounterReportServiceImpl
					.getAgedDebtors(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("amountsduetovendor")) {
			List<AmountsDueToVendor> amountsDueToVendor = accounterReportServiceImpl
					.getAmountsDueToVendor(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("customertransactionhistory")) {
			List<TransactionHistory> customerTransactionHistory = accounterReportServiceImpl
					.getCustomerTransactionHistory(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("debitorslist")) {
			try {
				List<DummyDebitor> debitors = accounterReportServiceImpl
						.getDebitors(clientFinanceStartDate,
								clientFinanceEndDate);
			} catch (AccounterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (methodName.equals("mostprofitablecustomers")) {
			List<MostProfitableCustomers> mostProfitableCustomers = accounterReportServiceImpl
					.getMostProfitableCustomers(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("reversechargelist")) {
			String payeeName = (String) req.getAttribute("Name");
			List<ReverseChargeListDetail> reverseChargeListDetailReport = accounterReportServiceImpl
					.getReverseChargeListDetailReport(payeeName,
							clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("purchasesbyitemdetail")) {
			List<SalesByCustomerDetail> purchasesByItemDetail = accounterReportServiceImpl
					.getPurchasesByItemDetail(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("reversechargelistdetails")) {
			String payeeName = (String) req.getAttribute("Name");
			List<ReverseChargeListDetail> reverseChargeListDetailReport = accounterReportServiceImpl
					.getReverseChargeListDetailReport(payeeName,
							clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("ecsaleslistdetails")) {
			String payeeName = (String) req.getAttribute("Name");
			List<ECSalesListDetail> ecSalesListDetailReport = accounterReportServiceImpl
					.getECSalesListDetailReport(payeeName,
							clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("purchasesbyitemsummary")) {
			List<SalesByCustomerDetail> purchasesByItemSummary = accounterReportServiceImpl
					.getPurchasesByItemSummary(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("purchasesbyvendordetail")) {
			List<SalesByCustomerDetail> purchasesByVendorDetail = accounterReportServiceImpl
					.getPurchasesByVendorDetail(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("ecsaleslist")) {
			List<ECSalesList> ecSalesListReport = accounterReportServiceImpl
					.getECSalesListReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("purchasesbyvendorsummary")) {
			List<SalesByCustomerDetail> purchasesByVendorSummary = accounterReportServiceImpl
					.getPurchasesByVendorSummary(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("salesbycustomerdetal")) {
			List<SalesByCustomerDetail> salesByCustomerDetailReport = accounterReportServiceImpl
					.getSalesByCustomerDetailReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("salesbycustomerdetalbyname")) {
			String customerName = (String) req.getAttribute("Name");
			List<SalesByCustomerDetail> salesByCustomerDetailReport = accounterReportServiceImpl
					.getSalesByCustomerDetailReport(customerName,
							clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("salesbyitemdetail")) {
			List<SalesByCustomerDetail> salesByItemDetail = accounterReportServiceImpl
					.getSalesByItemDetail(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("salesbyitemsummary")) {
			List<SalesByCustomerDetail> salesByItemSummary = accounterReportServiceImpl
					.getSalesByItemSummary(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("transactiondetailbytaxitem")) {
			List<TransactionDetailByTaxItem> transactionDetailByTaxItem = accounterReportServiceImpl
					.getTransactionDetailByTaxItem(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("trailbalance")) {
			List<TrialBalance> trialBalance = accounterReportServiceImpl
					.getTrialBalance(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("vendortransactionhistory")) {
			List<TransactionHistory> vendorTransactionHistory = accounterReportServiceImpl
					.getVendorTransactionHistory(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("purchasereportitems")) {
			List<ClientItem> purchaseReportItems = accounterReportServiceImpl
					.getPurchaseReportItems(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("salesreportitems")) {
			List<ClientItem> salesReportItems = accounterReportServiceImpl
					.getSalesReportItems(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("transactionhistorycustomers")) {
			List<ClientCustomer> transactionHistoryCustomers = accounterReportServiceImpl
					.getTransactionHistoryCustomers(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("vatitemdetails")) {
			String vatItemName = (String) req.getAttribute("Name");
			List<VATItemDetail> vatItemDetailReport = accounterReportServiceImpl
					.getVATItemDetailReport(vatItemName,
							clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("vatitemsummary")) {
			List<VATItemSummary> vatItemSummaryReport = accounterReportServiceImpl
					.getVATItemSummaryReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("transactionhistoryvendors")) {
			List<ClientVendor> transactionHistoryVendors = accounterReportServiceImpl
					.getTransactionHistoryVendors(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("uncategorisedamounts")) {
			List<UncategorisedAmountsReport> uncategorisedAmountsReport = accounterReportServiceImpl
					.getUncategorisedAmountsReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("salestaxliability")) {
			List<SalesTaxLiability> salesTaxLiabilityReport = accounterReportServiceImpl
					.getSalesTaxLiabilityReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("vat100report")) {
			Long taxAgency = (Long) req.getAttribute("TaxAgency");
			List<VATSummary> vat100Report = accounterReportServiceImpl
					.getVAT100Report(taxAgency, clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("transactiondetailbyaccount")) {

		} else if (methodName.equals("priorreturnvatsummary")) {

		} else if (methodName.equals("purchasesbyitemdetail")) {

		} else if (methodName.equals("priorvatreturnreport")) {

		} else if (methodName.equals("purchasesbyvendordetailbyname")) {

		} else if (methodName.equals("priorvatreturnvatdetailreport")) {

		} else if (methodName.equals("salesclosedorderlist")) {

		} else if (methodName.equals("salesbycustomerdetailreport")) {

		} else if (methodName.equals("salescancelledorderreport")) {

		} else if (methodName.equals("salesbyitemdetailbyname")) {

		} else if (methodName.equals("salesordelist")) {

		} else if (methodName.equals("salescompletedrder")) {

		} else if (methodName.equals("ageddebtorsbydebitorname")) {

		} else if (methodName.equals("salescompletedorder")) {

		} else if (methodName.equals("salesopenorder")) {

		} else if (methodName.equals("agedCreditorsbydebitorname")) {

		} else if (methodName.equals("purchaseclosedorder")) {

		} else if (methodName.equals("transactiondetailbyaccount")) {

		} else if (methodName.equals("minimumandmaximumtransactiondate")) {

		} else if (methodName.equals("purchaseorder")) {

		} else if (methodName.equals("purchasecancelledorder")) {

		} else if (methodName.equals("transactiondetailbytaxitemname")) {

		} else if (methodName.equals("balancesheetreport")) {

		} else if (methodName.equals("purchasecompletedlist")) {

		} else if (methodName.equals("cashflowreport")) {

		} else if (methodName.equals("purchaseopenorderlist")) {

		} else if (methodName.equals("profitandlossreport")) {

		}

	}

	private AccounterReportServiceImpl getAccounterReportServiceImpl() {
		// TODO
		return null;
	}

	private Date getDate(String startDateStr) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getMethodName(HttpServletRequest req) {
		StringBuffer requestURL = req.getRequestURL();
		String url = requestURL.toString();
		String[] urlParts = url.split("/");
		String last = urlParts[urlParts.length - 1];
		return last.split("?")[0];
	}
}
