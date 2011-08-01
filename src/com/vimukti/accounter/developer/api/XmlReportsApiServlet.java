package com.vimukti.accounter.developer.api;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

		if (methodName.equals("salesbycustomersummary")) {

		} else if (methodName.equals("payeestatements")) {

		} else if (methodName.equals("agedcreditors")) {

		} else if (methodName.equals("checkdetails")) {

		} else if (methodName.equals("depositdetail")) {

		} else if (methodName.equals("expensereports")) {

		} else if (methodName.equals("creditors")) {

		} else if (methodName.equals("debtorswidsamename")) {

		} else if (methodName.equals("ageddebtors")) {

		} else if (methodName.equals("amountsduetovendor")) {

		} else if (methodName.equals("customertransactionhistory")) {

		} else if (methodName.equals("debitorslist")) {

		} else if (methodName.equals("mostprofitablecustomers")) {

		} else if (methodName.equals("reversechargelist")) {

		} else if (methodName.equals("purchasesbyitemdetail")) {

		} else if (methodName.equals("reversechargelistdetails")) {

		} else if (methodName.equals("ecsaleslistdetails")) {

		} else if (methodName.equals("purchasesbyitemsummary")) {

		} else if (methodName.equals("purchasesbyvendordetail")) {

		} else if (methodName.equals("ecsaleslist")) {

		} else if (methodName.equals("purchasesbyvendorsummary")) {

		} else if (methodName.equals("salesbycustomerdetalreport")) {

		} else if (methodName.equals("salesbyitemdetail")) {

		} else if (methodName.equals("salesbyitemsummary")) {

		} else if (methodName.equals("transactiondetailbytaxitem")) {

		} else if (methodName.equals("trailbalance")) {

		} else if (methodName.equals("vendortransactionhistory")) {

		} else if (methodName.equals("purchasereportitems")) {

		} else if (methodName.equals("salesreportitems")) {

		} else if (methodName.equals("transactionhistorycustomers")) {

		} else if (methodName.equals("vatitemdetails")) {

		} else if (methodName.equals("vatitemsummary")) {

		} else if (methodName.equals("transactionhistoryvendors")) {

		} else if (methodName.equals("uncategorisedamounts")) {

		} else if (methodName.equals("salestaxliabilityreport")) {

		} else if (methodName.equals("vat100report")) {

		} else if (methodName.equals("transactiondetailbyaccount")) {

		} else if (methodName.equals("priorreturnvatsummary")) {

		} else if (methodName.equals("purchasesbyitemdetail")) {

		} else if (methodName.equals("priorvatreturnreport")) {

		} else if (methodName.equals("purchasesbyvendordetail")) {

		} else if (methodName.equals("priorvatreturnvatdetailreport")) {

		} else if (methodName.equals("salesclosedorderlist")) {

		} else if (methodName.equals("salesbycustomerdetailreport")) {

		} else if (methodName.equals("salescancelledorderreport")) {

		} else if (methodName.equals("salesbyitemdetail")) {

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

		} else if (methodName.equals("transactiondetailbytaxitem")) {

		} else if (methodName.equals("balancesheetreport")) {

		} else if (methodName.equals("purchasecompletedlist")) {

		} else if (methodName.equals("cashflowreport")) {

		} else if (methodName.equals("purchaseopenorderlist")) {

		} else if (methodName.equals("profitandlossreport")) {

		}

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
