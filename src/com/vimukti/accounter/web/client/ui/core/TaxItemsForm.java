package com.vimukti.accounter.web.client.ui.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;

public class TaxItemsForm extends DynamicForm {

	Map<ClientTAXItem, Double> codeValues;
	ClientTransaction transaction;
	double totalTax;

	public TaxItemsForm() {
		this.setNumCols(2);
		this.setCellSpacing(5);
	}

	public void setTransaction(ClientTransaction transaction) {
		this.transaction = transaction;
		updateForm();
	}

	private void updateForm() {
		ClientCompany company = Accounter.getCompany();

		this.removeAllRows();
		codeValues = new HashMap<ClientTAXItem, Double>();
		int category = getTransactionCategory(transaction.getObjectType());

		List<ClientTransactionItem> transactionItems = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem clientTransactionItem : transaction
				.getTransactionItems()) {
			if (clientTransactionItem.getReferringTransactionItem() == 0) {
				transactionItems.add(clientTransactionItem);
			}
		}
		if (transaction instanceof ClientInvoice) {
			List<ClientEstimate> estimates = ((ClientInvoice) transaction)
					.getEstimates();
			for (ClientEstimate clientEstimate : estimates) {
				transactionItems.addAll(clientEstimate.getTransactionItems());
			}

			List<ClientSalesOrder> salesOrders = ((ClientInvoice) transaction)
					.getSalesOrders();
			for (ClientSalesOrder clientSalesOrder : salesOrders) {
				transactionItems.addAll(clientSalesOrder.getTransactionItems());
			}
		}

		for (ClientTransactionItem transactionItem : transactionItems) {

			ClientTAXCode taxCode = company.getTAXCode(transactionItem
					.getTaxCode());
			if (taxCode == null) {
				continue;
			}

			long taxItemGroupId = 0;
			if (category == Transaction.CATEGORY_CUSTOMER) {
				taxItemGroupId = taxCode.getTAXItemGrpForSales();
			} else if (category == Transaction.CATEGORY_VENDOR) {
				taxItemGroupId = taxCode.getTAXItemGrpForPurchases();
			}
			ClientTAXItemGroup taxItemGroup = company
					.getTAXItemGroup(taxItemGroupId);
			if (taxItemGroup == null) {
				continue;
			}
			List<ClientTAXItem> taxItems = new ArrayList<ClientTAXItem>();

			if (taxItemGroup instanceof ClientTAXItem) {
				ClientTAXItem taxItem = (ClientTAXItem) taxItemGroup;
				taxItems.add(taxItem);

			} else if (taxItemGroup instanceof ClientTAXGroup) {
				taxItems = ((ClientTAXGroup) taxItemGroup).getTaxItems();
			}

			double value = transactionItem.getLineTotal() != null ? transactionItem
					.getLineTotal() : 0;

			for (ClientTAXItem clientTAXItem : taxItems) {
				double taxRate = clientTAXItem.getTaxRate();
				double amount = value;
				if (codeValues.containsKey(clientTAXItem)) {
					amount += codeValues.get(clientTAXItem);
				}
				if (clientTAXItem != null && taxRate != 0 && value != 0) {
					codeValues.put(clientTAXItem, amount);
				}
			}

		}

		FormItem[] items = new FormItem[codeValues.size()];
		totalTax = 0;
		int i = 0;

		for (Entry<ClientTAXItem, Double> entry : codeValues.entrySet()) {
			ClientTAXItem taxCode = entry.getKey();
			Double value = entry.getValue();

			if (taxCode != null) {
				double taxRate = taxCode.getTaxRate();

				double taxAmount = (value / 100) * taxRate;
				totalTax += taxAmount;

				if (taxRate != 0) {
					AmountLabel amountLabel = new AmountLabel(Accounter
							.messages().taxAtOnValue(
									DataUtils.getAmountAsString(taxRate),
									DataUtils.getAmountAsString(value)));
					amountLabel.setAmount(taxAmount);

					items[i] = amountLabel;
					i++;
				}
			}

		}

		this.setFields(items);
	}

	public double getTotalTax() {
		return totalTax;
	}

	public static int getTransactionCategory(AccounterCoreType accounterCoreType) {
		switch (accounterCoreType) {
		case CASHSALES:
		case CUSTOMERCREDITMEMO:
		case CUSTOMERREFUND:
		case ESTIMATE:
		case INVOICE:
		case RECEIVEPAYMENT:
		case SALESORDER:
		case CUSTOMERPREPAYMENT:
			return ClientTransaction.CATEGORY_CUSTOMER;

		case CASHPURCHASE:
		case CREDITCARDCHARGE:
		case ENTERBILL:
		case ISSUEPAYMENT:
		case PAYBILL:
		case VENDORCREDITMEMO:
		case PURCHASEORDER:
		case ITEMRECEIPT:
		case EXPENSE:
			return ClientTransaction.CATEGORY_VENDOR;

		case MAKEDEPOSIT:
		case TRANSFERFUND:
			return ClientTransaction.CATEGORY_BANKING;

		default:
			break;
		}
		return 0;
	}

}
