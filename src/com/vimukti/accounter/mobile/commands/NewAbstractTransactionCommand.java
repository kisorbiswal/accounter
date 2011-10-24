package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.UIUtils;

public abstract class NewAbstractTransactionCommand extends NewAbstractCommand {

	public void updateTotals(ClientTransaction transaction, boolean isSales) {
		List<ClientTransactionItem> allrecords = transaction
				.getTransactionItems();
		double lineTotal = 0.0;
		double totalTax = 0.0;

		for (ClientTransactionItem record : allrecords) {

			int type = record.getType();

			if (type == 0)
				continue;

			Double lineTotalAmt = record.getLineTotal();
			lineTotal += lineTotalAmt;

			if (record != null && record.isTaxable()) {
				double taxAmount = getVATAmount(transaction,
						record.getTaxCode(), record, isSales);
				if (transaction.isAmountsIncludeVAT()) {
					lineTotal -= taxAmount;
				}
				record.setVATfraction(taxAmount);
				totalTax += record.getVATfraction();
			}
		}

		double grandTotal = totalTax + lineTotal;

		transaction.setTotal(grandTotal);
		transaction.setNetAmount(lineTotal);
	}

	public double getVATAmount(ClientTransaction transaction, long TAXCodeID,
			ClientTransactionItem record, boolean isSales) {

		double vatRate = 0.0;
		try {
			if (TAXCodeID != 0) {
				// Checking the selected object is VATItem or VATGroup.
				// If it is VATItem,the we should get 'VATRate',otherwise
				// 'GroupRate
				vatRate = UIUtils.getVATItemRate(
						getClientCompany().getTAXCode(TAXCodeID), isSales);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Double vat = 0.0;
		if (transaction.isAmountsIncludeVAT()) {
			vat = ((ClientTransactionItem) record).getLineTotal()
					- (100 * (((ClientTransactionItem) record).getLineTotal() / (100 + vatRate)));
		} else {
			vat = ((ClientTransactionItem) record).getLineTotal() * vatRate
					/ 100;
		}
		vat = UIUtils.getRoundValue(vat);
		return vat.doubleValue();
	}

}
