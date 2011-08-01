package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.Lists.DummyDebitor;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.client.core.reports.AmountsDueToVendor;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.core.reports.DepositDetail;
import com.vimukti.accounter.web.client.core.reports.ECSalesList;
import com.vimukti.accounter.web.client.core.reports.ECSalesListDetail;
import com.vimukti.accounter.web.client.core.reports.ExpenseList;
import com.vimukti.accounter.web.client.core.reports.MostProfitableCustomers;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeListDetail;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.core.reports.SalesTaxLiability;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.core.reports.UncategorisedAmountsReport;
import com.vimukti.accounter.web.client.core.reports.VATDetail;
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

		} else if (methodName.equals("salesbycustomerdetail")) {
			List<SalesByCustomerDetail> salesByCustomerDetailReport = accounterReportServiceImpl
					.getSalesByCustomerDetailReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("salesbycustomerdetailbyname")) {
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
			List<TransactionDetailByAccount> transactionDetailByAccount = accounterReportServiceImpl
					.getTransactionDetailByAccount(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("priorreturnvatsummary")) {
			Long taxAgency = (Long) req.getAttribute("TaxAgency");
			List<VATSummary> priorReturnVATSummary = accounterReportServiceImpl
					.getPriorReturnVATSummary(taxAgency, clientFinanceEndDate);

		} else if (methodName.equals("purchasesbyitemdetailname")) {
			String itemName = (String) req.getAttribute("Name");
			List<SalesByCustomerDetail> purchasesByItemDetail = accounterReportServiceImpl
					.getPurchasesByItemDetail(itemName, clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("priorvatreturnreport")) {
			Long taxAgency = (Long) req.getAttribute("TaxAgency");
			List<VATDetail> priorVATReturnReport = accounterReportServiceImpl
					.getPriorVATReturnReport(taxAgency, clientFinanceEndDate);

		} else if (methodName.equals("purchasesbyvendordetailbyname")) {
			String vendorName = (String) req.getAttribute("Name");
			List<SalesByCustomerDetail> purchasesByVendorDetail = accounterReportServiceImpl
					.getPurchasesByVendorDetail(vendorName,
							clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("priorvatreturnvatdetailreport")) {
			List<VATDetail> priorVATReturnVATDetailReport = accounterReportServiceImpl
					.getPriorVATReturnVATDetailReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("salesclosedorderlist")) {
			List<OpenAndClosedOrders> salesClosedOrderReport = accounterReportServiceImpl
					.getSalesClosedOrderReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("salescancelledorderreport")) {
			List<OpenAndClosedOrders> salesCancelledOrderReport = accounterReportServiceImpl
					.getSalesCancelledOrderReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("salesbyitemdetailbyname")) {
			String itemName = (String) req.getAttribute("Name");
			List<SalesByCustomerDetail> salesByItemDetail = accounterReportServiceImpl
					.getSalesByItemDetail(itemName, clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("salesordelist")) {
			List<OpenAndClosedOrders> salesOrderReport = accounterReportServiceImpl
					.getSalesOrderReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("salescompletedorder")) {
			List<OpenAndClosedOrders> salesCompletedOrderReport = accounterReportServiceImpl
					.getSalesCompletedOrderReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("ageddebtorsbydebitorname")) {
			String Name = (String) req.getAttribute("Name");
			List<AgedDebtors> agedDebtors = accounterReportServiceImpl
					.getAgedDebtors(Name, clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("salesopenorder")) {
			List<OpenAndClosedOrders> salesOpenOrderReport = accounterReportServiceImpl
					.getSalesOpenOrderReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("purchaseclosedorder")) {
			List<OpenAndClosedOrders> purchaseClosedOrderReport = accounterReportServiceImpl
					.getPurchaseClosedOrderReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("transactiondetailbyaccountname")) {
			String accountName = (String) req.getAttribute("Name");
			List<TransactionDetailByAccount> transactionDetailByAccount = accounterReportServiceImpl
					.getTransactionDetailByAccount(accountName,
							clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("minimumandmaximumtransactiondate")) {
			List<ClientFinanceDate> minimumAndMaximumTransactionDate = accounterReportServiceImpl
					.getMinimumAndMaximumTransactionDate();

		} else if (methodName.equals("purchaseorder")) {
			List<OpenAndClosedOrders> purchaseOrderReport = accounterReportServiceImpl
					.getPurchaseOrderReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("purchasecancelledorder")) {
			List<OpenAndClosedOrders> purchaseCancelledOrderReport = accounterReportServiceImpl
					.getPurchaseCancelledOrderReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("transactiondetailbytaxitemname")) {
			String taxItemName = (String) req.getAttribute("Name");
			List<TransactionDetailByTaxItem> transactionDetailByTaxItem = accounterReportServiceImpl
					.getTransactionDetailByTaxItem(taxItemName,
							clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("balancesheetreport")) {
			List<TrialBalance> balanceSheetReport = accounterReportServiceImpl
					.getBalanceSheetReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("purchasecompletedlist")) {
			List<OpenAndClosedOrders> purchaseCompletedOrderReport = accounterReportServiceImpl
					.getPurchaseCompletedOrderReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("cashflowreport")) {
			List<TrialBalance> cashFlowReport = accounterReportServiceImpl
					.getCashFlowReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("purchaseopenorderlist")) {
			List<OpenAndClosedOrders> purchaseOpenOrderReport = accounterReportServiceImpl
					.getPurchaseOpenOrderReport(clientFinanceStartDate,
							clientFinanceEndDate);

		} else if (methodName.equals("profitandlossreport")) {
			List<TrialBalance> profitAndLossReport = accounterReportServiceImpl
					.getProfitAndLossReport(clientFinanceStartDate,
							clientFinanceEndDate);
		}

	}

	public <T> void sendResult(HttpServletRequest req,
			HttpServletResponse resp, List<? extends BaseReport> list) {
		XmlSerializationFactory factory = XmlSerializationFactory.getInstance();
		String string = factory.serializeReportsList(list);
		ServletOutputStream outputStream;
		try {
			outputStream = resp.getOutputStream();
			outputStream.write(string.getBytes());
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
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
