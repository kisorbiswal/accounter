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

	public static String getString(Object object) {
		if (object instanceof ClientCustomer) {
			return "customer";
		} else if (object instanceof ClientVendor) {
			return "vendor";
		} else if (object instanceof ClientItem) {
			return "item";
		} else if (object instanceof ClientAccount) {
			return "account";
		} else if (object instanceof ClientJournalEntry) {
			return "journalentry";
		} else if (object instanceof ClientTAXItem) {
			return "taxitem";
		} else if (object instanceof ClientTAXCode) {
			return "taxcode";
		} else if (object instanceof ClientInvoice) {
			return "invoice";
		} else if (object instanceof ClientCashSales) {
			return "cashsale";
		} else if (object instanceof ClientEstimate) {
			return "estimate";
		} else if (object instanceof ClientCustomerCreditMemo) {
			return "customercreditmemo";
		} else if (object instanceof ClientReceivePayment) {
			return "receivepayment";
		} else if (object instanceof ClientCustomerRefund) {
			return "customerrefund";
		} else if (object instanceof ClientCustomerPrePayment) {
			return "customerprepayment";
		} else if (object instanceof ClientEnterBill) {
			return "enterbill";
		} else if (object instanceof ClientPayBill) {
			return "paybill";
		} else if (object instanceof ClientCashPurchase) {
			return "cashpurchase";
		} else if (object instanceof ClientVendorCreditMemo) {
			return "vendorcreditmemo";
		}
		return null;
	}
}
