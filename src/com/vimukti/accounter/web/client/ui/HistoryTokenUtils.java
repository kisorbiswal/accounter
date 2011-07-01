/**
 * 
 */
package com.vimukti.accounter.web.client.ui;

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

	public static Object getObject(String string) {
		String[] strings = string.split(":");
		if (strings.length > 1) {
			String temp = strings[0];
			if (temp.equalsIgnoreCase("customer")) {
				ClientCustomer clientCustomer = new ClientCustomer();
				return clientCustomer.getObjectType();
			} else if (temp.equalsIgnoreCase("vendor")) {
				ClientVendor clientVendor = new ClientVendor();
				return clientVendor.getObjectType();
			} else if (temp.equalsIgnoreCase("item")) {
				ClientItem clientItem = new ClientItem();
				return clientItem.getObjectType();
			} else if (temp.equalsIgnoreCase("account")) {
				ClientAccount clientAccount = new ClientAccount();
				return clientAccount.getObjectType();
			} else if (temp.equalsIgnoreCase("journelentry")) {
				ClientJournalEntry clientJournalEntry = new ClientJournalEntry();
				return clientJournalEntry.getObjectType();
			} else if (temp.equalsIgnoreCase("taxitem")) {
				ClientTAXItem clientTAXItem = new ClientTAXItem();
				return clientTAXItem.getObjectType();
			} else if (temp.equalsIgnoreCase("taxcode")) {
				ClientTAXCode clientTAXCode = new ClientTAXCode();
				return clientTAXCode.getObjectType();
			} else if (temp.equalsIgnoreCase("invoice")) {
				ClientInvoice clientInvoice = new ClientInvoice();
				return clientInvoice.getObjectType();
			} else if (temp.equalsIgnoreCase("cashsale")) {
				ClientCashSales clientCashSales = new ClientCashSales();
				return clientCashSales.getObjectType();
			} else if (temp.equalsIgnoreCase("estimate")) {
				ClientEstimate clientEstimate = new ClientEstimate();
				return clientEstimate.getObjectType();
			} else if (temp.equalsIgnoreCase("customercreditmemo")) {
				ClientCustomerCreditMemo clientCustomerCreditMemo = new ClientCustomerCreditMemo();
				return clientCustomerCreditMemo.getObjectType();
			} else if (temp.equalsIgnoreCase("receivepayment")) {
				ClientReceivePayment clientReceivePayment = new ClientReceivePayment();
				return clientReceivePayment.getObjectType();
			} else if (temp.equalsIgnoreCase("customerrefund")) {
				ClientCustomerRefund clientCustomerRefund = new ClientCustomerRefund();
				return clientCustomerRefund.getObjectType();
			} else if (temp.equalsIgnoreCase("customerprepayment")) {
				ClientCustomerPrePayment clientCustomerPrePayment = new ClientCustomerPrePayment();
				return clientCustomerPrePayment.getObjectType();
			} else if (temp.equalsIgnoreCase("enterbill")) {
				ClientEnterBill clientEnterBill = new ClientEnterBill();
				return clientEnterBill.getObjectType();
			} else if (temp.equalsIgnoreCase("paybill")) {
				ClientPayBill clientPayBill = new ClientPayBill();
				return clientPayBill.getObjectType();
			} else if (temp.equalsIgnoreCase("cashpurchase")) {
				ClientCashPurchase clientCashPurchase = new ClientCashPurchase();
				return clientCashPurchase.getObjectType();
			} else if (temp.equalsIgnoreCase("vendorcreditmemo")) {
				ClientVendorCreditMemo clientVendorCreditMemo = new ClientVendorCreditMemo();
				return clientVendorCreditMemo.getObjectType();
			}
		}
		return null;
	}

	public static String getObjectNameWithID(Object object) {
		StringBuilder temp = new StringBuilder();// object.getClass().getName().replaceFirst("Client",
		// "");
		if (object instanceof ClientCustomer) {
			temp.append("customer");
			temp.append(":");
			temp.append(((ClientCustomer) object).getStringID());

		} else if (object instanceof ClientVendor) {
			temp.append("vendor");
			temp.append(":");
			temp.append(((ClientVendor) object).getStringID());

		} else if (object instanceof ClientItem) {
			temp.append("item");
			temp.append(":");
			temp.append(((ClientItem) object).getStringID());

		} else if (object instanceof ClientAccount) {
			temp.append("account");
			temp.append(":");
			temp.append(((ClientAccount) object).getStringID());

		} else if (object instanceof ClientJournalEntry) {
			temp.append("journalentry");
			temp.append(":");
			temp.append(((ClientJournalEntry) object).getStringID());

		} else if (object instanceof ClientTAXItem) {
			temp.append("taxitem");
			temp.append(":");
			temp.append(((ClientTAXItem) object).getStringID());

		} else if (object instanceof ClientTAXCode) {
			temp.append("taxcode");
			temp.append(":");
			temp.append(((ClientTAXCode) object).getStringID());

		} else if (object instanceof ClientInvoice) {
			temp.append("invoice");
			temp.append(":");
			temp.append(((ClientInvoice) object).getStringID());

		} else if (object instanceof ClientCashSales) {
			temp.append("cashsales");
			temp.append(":");
			temp.append(((ClientCashSales) object).getStringID());

		} else if (object instanceof ClientEstimate) {
			temp.append("estimate");
			temp.append(":");
			temp.append(((ClientEstimate) object).getStringID());

		} else if (object instanceof ClientCustomerCreditMemo) {
			temp.append("customercreditmemo");
			temp.append(":");
			temp.append(((ClientCustomerCreditMemo) object).getStringID());

		} else if (object instanceof ClientReceivePayment) {
			temp.append("receivepayment");
			temp.append(":");
			temp.append(((ClientReceivePayment) object).getStringID());

		} else if (object instanceof ClientCustomerRefund) {
			temp.append("customerrefund");
			temp.append(":");
			temp.append(((ClientCustomerRefund) object).getStringID());

		} else if (object instanceof ClientCustomerPrePayment) {
			temp.append("customerprepayment");
			temp.append(":");
			temp.append(((ClientCustomerPrePayment) object).getStringID());

		} else if (object instanceof ClientEnterBill) {
			temp.append("enterbill");
			temp.append(":");
			temp.append(((ClientEnterBill) object).getStringID());

		} else if (object instanceof ClientPayBill) {
			temp.append("paybill");
			temp.append(":");
			temp.append(((ClientPayBill) object).getStringID());

		} else if (object instanceof ClientCashPurchase) {
			temp.append("cashpurchase");
			temp.append(":");
			temp.append(((ClientCashPurchase) object).getStringID());

		} else if (object instanceof ClientVendorCreditMemo) {
			temp.append("vendorcreditmemo");
			temp.append(":");
			temp.append(((ClientVendorCreditMemo) object).getStringID());
		}
		return temp.toString();
	}

	public static void setPreviousToken() {
		History history = MainFinanceWindow.getViewManager().getTempHistory();
		if (history != null) {
			setPresentToken(history.getAction(), null);
		} else {
			setPresentToken(CompanyActionFactory.getCompanyHomeAction(), null);
		}
	}

	public static String getTokenWithID(String historyToken, Object object) {
		String token = historyToken;
		if (object != null) {
			token = token + "?" + getObjectNameWithID(object);
		}
		return token;
	}

	public static void setPresentToken(Action action, Object obj) {
		MainFinanceWindow.shouldExecuteRun = false;
		com.google.gwt.user.client.History.newItem(getTokenWithID(action
				.getHistoryToken(), obj));
	}
}
