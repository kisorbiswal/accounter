package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.exception.AccounterException;
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
			accounterReportServiceImpl.getAgedCreditors(clientFinanceStartDate,
					clientFinanceEndDate);

		} else if (methodName.equals("checkdetails")) {
			Long paymentmethod = (Long) req.getAttribute("PaymentMethod");
			accounterReportServiceImpl.getCheckDetailReport(paymentmethod,
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("depositdetail")) {
			accounterReportServiceImpl.getDepositDetail(clientFinanceStartDate,
					clientFinanceEndDate);

		} else if (methodName.equals("expensereports")) {
			int status = (Integer) req.getAttribute("Status");
			accounterReportServiceImpl.getExpenseReportByType(status,
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("creditors")) {
			try {
				accounterReportServiceImpl.getCreditors(clientFinanceStartDate,
						clientFinanceEndDate);
			} catch (AccounterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (methodName.equals("ageddebtors")) {
			accounterReportServiceImpl.getAgedDebtors(clientFinanceStartDate,
					clientFinanceEndDate);

		} else if (methodName.equals("amountsduetovendor")) {
			accounterReportServiceImpl.getAmountsDueToVendor(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("customertransactionhistory")) {
			accounterReportServiceImpl.getCustomerTransactionHistory(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("debitorslist")) {
			try {
				accounterReportServiceImpl.getDebitors(clientFinanceStartDate,
						clientFinanceEndDate);
			} catch (AccounterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (methodName.equals("mostprofitablecustomers")) {
			accounterReportServiceImpl.getMostProfitableCustomers(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("reversechargelist")) {
			String payeeName = (String) req.getAttribute("Name");
			accounterReportServiceImpl.getReverseChargeListDetailReport(
					payeeName, clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("purchasesbyitemdetail")) {
			accounterReportServiceImpl.getPurchasesByItemDetail(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("reversechargelistdetails")) {
			String payeeName = (String) req.getAttribute("Name");
			accounterReportServiceImpl.getReverseChargeListDetailReport(
					payeeName, clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("ecsaleslistdetails")) {
			String payeeName = (String) req.getAttribute("Name");
			accounterReportServiceImpl.getECSalesListDetailReport(payeeName,
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("purchasesbyitemsummary")) {
			accounterReportServiceImpl.getPurchasesByItemSummary(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("purchasesbyvendordetail")) {
			accounterReportServiceImpl.getPurchasesByVendorDetail(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("ecsaleslist")) {
			accounterReportServiceImpl.getECSalesListReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("purchasesbyvendorsummary")) {
			accounterReportServiceImpl.getPurchasesByVendorSummary(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("salesbycustomerdetal")) {
			accounterReportServiceImpl.getSalesByCustomerDetailReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("salesbycustomerdetalbyname")) {
			String customerName = (String) req.getAttribute("Name");
			accounterReportServiceImpl.getSalesByCustomerDetailReport(
					customerName, clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("salesbyitemdetail")) {
			accounterReportServiceImpl.getSalesByItemDetail(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("salesbyitemsummary")) {
			accounterReportServiceImpl.getSalesByItemSummary(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("transactiondetailbytaxitem")) {
			accounterReportServiceImpl.getTransactionDetailByTaxItem(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("trailbalance")) {
			accounterReportServiceImpl.getTrialBalance(clientFinanceStartDate,
					clientFinanceEndDate);

		} else if (methodName.equals("vendortransactionhistory")) {
			accounterReportServiceImpl.getVendorTransactionHistory(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("purchasereportitems")) {
			accounterReportServiceImpl.getPurchaseReportItems(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("salesreportitems")) {
			accounterReportServiceImpl.getSalesReportItems(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("transactionhistorycustomers")) {
			accounterReportServiceImpl.getTransactionHistoryCustomers(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("vatitemdetails")) {
			String vatItemName = (String) req.getAttribute("Name");
			accounterReportServiceImpl.getVATItemDetailReport(vatItemName,
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("vatitemsummary")) {
			accounterReportServiceImpl.getVATItemSummaryReport(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("transactionhistoryvendors")) {
			accounterReportServiceImpl.getTransactionHistoryVendors(
					clientFinanceStartDate, clientFinanceEndDate);

		} else if (methodName.equals("uncategorisedamounts")) {

		} else if (methodName.equals("salestaxliability")) {

		} else if (methodName.equals("vat100report")) {

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
