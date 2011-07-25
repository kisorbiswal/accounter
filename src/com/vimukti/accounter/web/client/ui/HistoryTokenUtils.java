/**
 * 
 */
package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerCreditMemo;
import com.vimukti.accounter.web.client.core.ClientCustomerPrePayment;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.CompanyActionFactory;
import com.vimukti.accounter.web.client.ui.core.History;

/**
 * @author K.Sandeep Sagar
 * 
 */
public class HistoryTokenUtils {

	public List<Object> getObject(String string) {
		int split1 = string.lastIndexOf('?');
		int split2 = string.lastIndexOf(':');
		List<Object> list = new ArrayList<Object>();
		if (!(split1 <= 0) && !(split2 <= 0)) {
			list.add(string.substring(0, split1));
			String temp = string.substring(split1 + 1, split2);
			if (temp.equalsIgnoreCase("customer")) {
				ClientCustomer clientCustomer = new ClientCustomer();
				list.add(clientCustomer.getObjectType());
			} else if (temp.equalsIgnoreCase("vendor")) {
				ClientVendor clientVendor = new ClientVendor();
				list.add(clientVendor.getObjectType());
			} else if (temp.equalsIgnoreCase("item")) {
				ClientItem clientItem = new ClientItem();
				list.add(clientItem.getObjectType());
			} else if (temp.equalsIgnoreCase("account")) {
				ClientAccount clientAccount = new ClientAccount();
				list.add(clientAccount.getObjectType());
			} else if (temp.equalsIgnoreCase("journelentry")) {
				ClientJournalEntry clientJournalEntry = new ClientJournalEntry();
				list.add(clientJournalEntry.getObjectType());
			} else if (temp.equalsIgnoreCase("taxitem")) {
				ClientTAXItem clientTAXItem = new ClientTAXItem();
				list.add(clientTAXItem.getObjectType());
			} else if (temp.equalsIgnoreCase("taxcode")) {
				ClientTAXCode clientTAXCode = new ClientTAXCode();
				list.add(clientTAXCode.getObjectType());
			} else if (temp.equalsIgnoreCase("invoice")) {
				ClientInvoice clientInvoice = new ClientInvoice();
				list.add(clientInvoice.getObjectType());
			} else if (temp.equalsIgnoreCase("cashsale")) {
				ClientCashSales clientCashSales = new ClientCashSales();
				list.add(clientCashSales.getObjectType());
			} else if (temp.equalsIgnoreCase("estimate")) {
				ClientEstimate clientEstimate = new ClientEstimate();
				list.add(clientEstimate.getObjectType());
			} else if (temp.equalsIgnoreCase("customercreditmemo")) {
				ClientCustomerCreditMemo clientCustomerCreditMemo = new ClientCustomerCreditMemo();
				list.add(clientCustomerCreditMemo.getObjectType());
			} else if (temp.equalsIgnoreCase("receivepayment")) {
				ClientReceivePayment clientReceivePayment = new ClientReceivePayment();
				list.add(clientReceivePayment.getObjectType());
			} else if (temp.equalsIgnoreCase("customerrefund")) {
				ClientCustomerRefund clientCustomerRefund = new ClientCustomerRefund();
				list.add(clientCustomerRefund.getObjectType());
			} else if (temp.equalsIgnoreCase("customerprepayment")) {
				ClientCustomerPrePayment clientCustomerPrePayment = new ClientCustomerPrePayment();
				list.add(clientCustomerPrePayment.getObjectType());
			} else if (temp.equalsIgnoreCase("enterbill")) {
				ClientEnterBill clientEnterBill = new ClientEnterBill();
				list.add(clientEnterBill.getObjectType());
			} else if (temp.equalsIgnoreCase("paybill")) {
				ClientPayBill clientPayBill = new ClientPayBill();
				list.add(clientPayBill.getObjectType());
			} else if (temp.equalsIgnoreCase("cashpurchase")) {
				ClientCashPurchase clientCashPurchase = new ClientCashPurchase();
				list.add(clientCashPurchase.getObjectType());
			} else if (temp.equalsIgnoreCase("vendorcreditmemo")) {
				ClientVendorCreditMemo clientVendorCreditMemo = new ClientVendorCreditMemo();
				list.add(clientVendorCreditMemo.getObjectType());
			}
			list.add(string.substring(split2 + 1));
		} else
			list.add(string);
		return list;
	}

	public String getObjectNameWithID(Object object) {
		StringBuilder temp = new StringBuilder();// object.getClass().getName().replaceFirst("Client",
		// "");
		if (object instanceof ClientCustomer) {
			temp.append("customer");
			temp.append(":");
			temp.append(((ClientCustomer) object).getID());

		} else if (object instanceof ClientVendor) {
			temp.append("vendor");
			temp.append(":");
			temp.append(((ClientVendor) object).getID());

		} else if (object instanceof ClientItem) {
			temp.append("item");
			temp.append(":");
			temp.append(((ClientItem) object).getID());

		} else if (object instanceof ClientAccount) {
			temp.append("account");
			temp.append(":");
			temp.append(((ClientAccount) object).getID());

		} else if (object instanceof ClientJournalEntry) {
			temp.append("journalentry");
			temp.append(":");
			temp.append(((ClientJournalEntry) object).getID());

		} else if (object instanceof ClientTAXItem) {
			temp.append("taxitem");
			temp.append(":");
			temp.append(((ClientTAXItem) object).getID());

		} else if (object instanceof ClientTAXCode) {
			temp.append("taxcode");
			temp.append(":");
			temp.append(((ClientTAXCode) object).getID());

		} else if (object instanceof ClientInvoice) {
			temp.append("invoice");
			temp.append(":");
			temp.append(((ClientInvoice) object).getID());

		} else if (object instanceof ClientCashSales) {
			temp.append("cashsales");
			temp.append(":");
			temp.append(((ClientCashSales) object).getID());

		} else if (object instanceof ClientEstimate) {
			temp.append("estimate");
			temp.append(":");
			temp.append(((ClientEstimate) object).getID());

		} else if (object instanceof ClientCustomerCreditMemo) {
			temp.append("customercreditmemo");
			temp.append(":");
			temp.append(((ClientCustomerCreditMemo) object).getID());

		} else if (object instanceof ClientReceivePayment) {
			temp.append("receivepayment");
			temp.append(":");
			temp.append(((ClientReceivePayment) object).getID());

		} else if (object instanceof ClientCustomerRefund) {
			temp.append("customerrefund");
			temp.append(":");
			temp.append(((ClientCustomerRefund) object).getID());

		} else if (object instanceof ClientCustomerPrePayment) {
			temp.append("customerprepayment");
			temp.append(":");
			temp.append(((ClientCustomerPrePayment) object).getID());

		} else if (object instanceof ClientEnterBill) {
			temp.append("enterbill");
			temp.append(":");
			temp.append(((ClientEnterBill) object).getID());

		} else if (object instanceof ClientPayBill) {
			temp.append("paybill");
			temp.append(":");
			temp.append(((ClientPayBill) object).getID());

		} else if (object instanceof ClientCashPurchase) {
			temp.append("cashpurchase");
			temp.append(":");
			temp.append(((ClientCashPurchase) object).getID());

		} else if (object instanceof ClientVendorCreditMemo) {
			temp.append("vendorcreditmemo");
			temp.append(":");
			temp.append(((ClientVendorCreditMemo) object).getID());
		}
		return temp.toString();
	}

	public void setPreviousToken() {
		History history = MainFinanceWindow.getViewManager().getTempHistory();
		if (history != null) {
			setPresentToken(history.getAction(), null);
		} else {
			setPresentToken(CompanyActionFactory.getCompanyHomeAction(), null);
		}
	}

	public String getTokenWithID(String historyToken, Object object) {
		String token = historyToken;
		if (object != null) {
			token = token + "?" + getObjectNameWithID(object);
		}
		return token;
	}

	public static void setPresentToken(Action action, Object obj) {
		MainFinanceWindow.shouldExecuteRun = false;
		MainFinanceWindow.oldToken = com.google.gwt.user.client.History
				.getToken();
		com.google.gwt.user.client.History.newItem(getTokenWithID(
				action.getHistoryToken(), obj));
	}
}
