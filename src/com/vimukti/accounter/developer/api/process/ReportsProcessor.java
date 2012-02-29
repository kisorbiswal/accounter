package com.vimukti.accounter.developer.api.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.vimukti.accounter.core.Client;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.IAccounterReportService;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class ReportsProcessor extends ApiProcessor {

	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		try {
			String methodName = req.getParameter("reporttype");
			if (methodName == null) {
				sendFail("reporttype should be present");
				return;
			}
			ClientFinanceDate clientFinanceEndDate = null;
			ClientFinanceDate clientFinanceStartDate = null;
			IAccounterReportService accounterReportServiceImpl = getS2sSyncProxy(
					req, "/do/accounter/report/rpc/service",
					IAccounterReportService.class);
			if (!methodName.equals("companyids")) {
				clientFinanceStartDate = getClientFinanceDate(req
						.getParameter("StartDate"));
				clientFinanceEndDate = getClientFinanceDate(req
						.getParameter("EndDate"));
				if (clientFinanceEndDate == null
						|| clientFinanceStartDate == null) {
					resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
							"Wrong date formate");
					return;
				}
			}
			List<? extends BaseReport> result = null;

			if (methodName.equals("salesbycustomersummary")) {
				result = accounterReportServiceImpl.getSalesByCustomerSummary(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("payeestatements")) {
				boolean isVendor = (Boolean) req.getAttribute("IsVendor");
				int id = (Integer) req.getAttribute("Id");
				long transactionDate = (Long) req
						.getAttribute("TransactionDate");
				ClientFinanceDate fromDate = (ClientFinanceDate) req
						.getAttribute("FromDate");
				ClientFinanceDate toDate = (ClientFinanceDate) req
						.getAttribute("ToDate");
				int noOfDays = (Integer) req.getAttribute("NoOfDays");
				boolean isEnabledOfZeroBalBox = (Boolean) req
						.getAttribute("IsEnabledOfZeroBalBox");
				boolean isEnabledOfLessthanZeroBalBox = (Boolean) req
						.getAttribute("IsEnabledOfLessthanZeroBalBox");
				double lessThanZeroBalanceValue = (Double) req
						.getAttribute("LessThanZeroBalanceValue");
				boolean isEnabledOfNoAccountActivity = (Boolean) req
						.getAttribute("IsEnabledOfNoAccountActivity");
				boolean isEnabledOfInactiveCustomer = (Boolean) req
						.getAttribute("IsEnabledOfInactiveCustomer");

				accounterReportServiceImpl.getStatements(isVendor, id, 0,
						fromDate, toDate);

			} else if (methodName.equals("agedcreditors")) {
				result = accounterReportServiceImpl.getAgedCreditors(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("checkdetails")) {
				Long paymentmethod = (Long) req.getAttribute("PaymentMethod");
				result = accounterReportServiceImpl.getCheckDetailReport(
						paymentmethod, clientFinanceStartDate,
						clientFinanceEndDate);

			} else if (methodName.equals("depositdetail")) {
				result = accounterReportServiceImpl.getDepositDetail(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("expensereports")) {
				int status = (Integer) req.getAttribute("Status");
				result = accounterReportServiceImpl.getExpenseReportByType(
						status, clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("creditors")) {
				try {
					result = accounterReportServiceImpl.getCreditors(
							clientFinanceStartDate, clientFinanceEndDate);
				} catch (AccounterException e) {
					sendFail(e.getMessage());
					return;
				}

			} else if (methodName.equals("ageddebtors")) {
				result = accounterReportServiceImpl.getAgedDebtors(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("amountsduetovendor")) {
				result = accounterReportServiceImpl.getAmountsDueToVendor(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("salesbycustomerdetailbyname")) {
				String customerName = (String) req.getAttribute("Name");
				result = accounterReportServiceImpl
						.getSalesByCustomerDetailReport(customerName,
								clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("customertransactionhistory")) {
				result = accounterReportServiceImpl
						.getCustomerTransactionHistory(clientFinanceStartDate,
								clientFinanceEndDate);

			} else if (methodName.equals("debitorslist")) {
				try {
					result = accounterReportServiceImpl.getDebitors(
							clientFinanceStartDate, clientFinanceEndDate);
				} catch (AccounterException e) {
					resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
							e.getMessage());
					return;
				}

			} else if (methodName.equals("mostprofitablecustomers")) {
				result = accounterReportServiceImpl.getMostProfitableCustomers(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("reversechargelist")) {
				String payeeName = (String) req.getAttribute("Name");
				result = accounterReportServiceImpl
						.getReverseChargeListDetailReport(payeeName,
								clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("purchasesbyitemdetail")) {
				result = accounterReportServiceImpl.getPurchasesByItemDetail(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("reversechargelistdetails")) {
				String payeeName = (String) req.getAttribute("Name");
				result = accounterReportServiceImpl
						.getReverseChargeListDetailReport(payeeName,
								clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("ecsaleslistdetails")) {
				String payeeName = (String) req.getAttribute("Name");
				result = accounterReportServiceImpl
						.getECSalesListDetailReport(payeeName,
								clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("purchasesbyitemsummary")) {
				result = accounterReportServiceImpl.getPurchasesByItemSummary(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("purchasesbyvendordetail")) {
				result = accounterReportServiceImpl.getPurchasesByVendorDetail(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("ecsaleslist")) {
				result = accounterReportServiceImpl.getECSalesListReport(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("purchasesbyvendorsummary")) {
				result = accounterReportServiceImpl
						.getPurchasesByVendorSummary(clientFinanceStartDate,
								clientFinanceEndDate);

			} else if (methodName.equals("salesbycustomerdetail")) {
				result = accounterReportServiceImpl
						.getSalesByCustomerDetailReport(clientFinanceStartDate,
								clientFinanceEndDate);

			} else if (methodName.equals("salesbyitemdetail")) {
				result = accounterReportServiceImpl.getSalesByItemDetail(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("salesbyitemsummary")) {
				result = accounterReportServiceImpl.getSalesByItemSummary(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("transactiondetailbytaxitem")) {
				result = accounterReportServiceImpl
						.getTransactionDetailByTaxItem(clientFinanceStartDate,
								clientFinanceEndDate);

			} else if (methodName.equals("trailbalance")) {
				result = accounterReportServiceImpl.getTrialBalance(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("vendortransactionhistory")) {
				result = accounterReportServiceImpl
						.getVendorTransactionHistory(clientFinanceStartDate,
								clientFinanceEndDate);

			} else if (methodName.equals("purchasereportitems")) {
				result = accounterReportServiceImpl.getPurchaseReportItems(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("salesreportitems")) {
				result = accounterReportServiceImpl.getSalesReportItems(
						clientFinanceStartDate, clientFinanceEndDate);
				/*
				 * till here classes are created
				 */

			} else if (methodName.equals("transactionhistorycustomers")) {
				List<ClientCustomer> transactionHistoryCustomers = accounterReportServiceImpl
						.getTransactionHistoryCustomers(clientFinanceStartDate,
								clientFinanceEndDate);
				sendResult(transactionHistoryCustomers);
				return;

			} else if (methodName.equals("vatitemdetails")) {
				String vatItemName = (String) req.getAttribute("Name");
				result = accounterReportServiceImpl.getVATItemDetailReport(
						vatItemName, clientFinanceStartDate,
						clientFinanceEndDate);

			} else if (methodName.equals("vatitemsummary")) {
				result = accounterReportServiceImpl.getVATItemSummaryReport(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("transactionhistoryvendors")) {
				List<ClientVendor> transactionHistoryVendors = accounterReportServiceImpl
						.getTransactionHistoryVendors(clientFinanceStartDate,
								clientFinanceEndDate);
				sendResult(transactionHistoryVendors);
				return;

			} else if (methodName.equals("uncategorisedamounts")) {
				result = accounterReportServiceImpl
						.getUncategorisedAmountsReport(clientFinanceStartDate,
								clientFinanceEndDate);

			} else if (methodName.equals("salestaxliability")) {
				result = accounterReportServiceImpl.getSalesTaxLiabilityReport(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("vat100report")) {
				Long taxAgency = (Long) req.getAttribute("TaxAgency");
				result = accounterReportServiceImpl.getVAT100Report(taxAgency,
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("transactiondetailbyaccount")) {
				result = accounterReportServiceImpl
						.getTransactionDetailByAccount(clientFinanceStartDate,
								clientFinanceEndDate);

			} else if (methodName.equals("priorreturnvatsummary")) {
				Long taxAgency = (Long) req.getAttribute("TaxAgency");
				result = accounterReportServiceImpl.getPriorReturnVATSummary(
						taxAgency, clientFinanceEndDate);

			} else if (methodName.equals("purchasesbyitemdetailname")) {
				String itemName = (String) req.getAttribute("Name");
				result = accounterReportServiceImpl.getPurchasesByItemDetail(
						itemName, clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("priorvatreturnreport")) {
				Long taxAgency = (Long) req.getAttribute("TaxAgency");
				result = accounterReportServiceImpl.getPriorVATReturnReport(
						taxAgency, clientFinanceEndDate);

			} else if (methodName.equals("purchasesbyvendordetailbyname")) {
				String vendorName = (String) req.getAttribute("Name");
				result = accounterReportServiceImpl.getPurchasesByVendorDetail(
						vendorName, clientFinanceStartDate,
						clientFinanceEndDate);

			} else if (methodName.equals("priorvatreturnvatdetailreport")) {
				result = accounterReportServiceImpl
						.getPriorVATReturnVATDetailReport(
								clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("salesclosedorderlist")) {
				result = accounterReportServiceImpl.getSalesOrderReport(4,
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("salescancelledorderreport")) {
				result = accounterReportServiceImpl.getSalesOrderReport(103,
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("salesbyitemdetailbyname")) {
				String itemName = (String) req.getAttribute("Name");
				result = accounterReportServiceImpl.getSalesByItemDetail(
						itemName, clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("salesordelist")) {
				result = accounterReportServiceImpl.getSalesOrderReport(-1,
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("salescompletedorder")) {
				result = accounterReportServiceImpl.getSalesOrderReport(102,
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("ageddebtorsbydebitorname")) {
				String Name = (String) req.getAttribute("Name");
				result = accounterReportServiceImpl.getAgedDebtors(Name,
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("salesopenorder")) {
				result = accounterReportServiceImpl.getSalesOrderReport(0,
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("agedCreditorsbydebitorname")) {
				String name = (String) req.getAttribute("Name");
				result = accounterReportServiceImpl.getAgedCreditors(name,
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("purchaseclosedorder")) {
				result = accounterReportServiceImpl.getPurchaseOrderReport(4,
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("transactiondetailbyaccountname")) {
				String accountName = (String) req.getAttribute("Name");
				result = accounterReportServiceImpl
						.getTransactionDetailByAccount(accountName,
								clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("minimumandmaximumtransactiondate")) {
				List<ClientFinanceDate> minimumAndMaximumTransactionDate = accounterReportServiceImpl
						.getMinimumAndMaximumTransactionDate();
				sendResult(minimumAndMaximumTransactionDate);
				return;
			} else if (methodName.equals("purchaseorder")) {
				result = accounterReportServiceImpl.getPurchaseOrderReport(-1,
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("purchasecancelledorder")) {
				result = accounterReportServiceImpl.getPurchaseOrderReport(103,
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("transactiondetailbytaxitemname")) {
				String taxItemName = (String) req.getAttribute("Name");
				result = accounterReportServiceImpl
						.getTransactionDetailByTaxItem(taxItemName,
								clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("balancesheetreport")) {
				result = accounterReportServiceImpl.getBalanceSheetReport(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("purchasecompletedlist")) {
				result = accounterReportServiceImpl.getPurchaseOrderReport(102,
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("cashflowreport")) {
				result = accounterReportServiceImpl.getCashFlowReport(
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("purchaseopenorderlist")) {
				result = accounterReportServiceImpl.getPurchaseOrderReport(1,
						clientFinanceStartDate, clientFinanceEndDate);

			} else if (methodName.equals("profitandlossreport")) {
				result = accounterReportServiceImpl.getProfitAndLossReport(
						clientFinanceStartDate, clientFinanceEndDate);
			} else if (methodName.equals("companyids")) {
				sendCompanyIds(req, resp);
				return;
			}
			sendResult(result);
		} catch (Exception e) {
			sendFail(e.getMessage());
		}
	}

	private void sendCompanyIds(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {
		Map<String, Long> companyIds = new HashMap<String, Long>();
		String emailId = (String) req.getAttribute("emailId");
		Client client = getClient(emailId);
		Set<User> users = client.getUsers();
		for (User user : users) {
			Company company = user.getCompany();
			companyIds.put(company.getTradingName(), company.getID());
		}
		sendResult(companyIds);
	}

	protected Client getClient(String emailId) {
		Session session = HibernateUtil.getCurrentSession();
		Client client = (Client) session.getNamedQuery("getClient.by.mailId")
				.setString("emailId", emailId).uniqueResult();
		return client;
	}
}
