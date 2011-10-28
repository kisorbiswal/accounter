package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.UIUtils;

public abstract class NewAbstractTransactionCommand extends NewAbstractCommand {

	public void updateTotals(Context context, ClientTransaction transaction,
			boolean isSales) {
		List<ClientTransactionItem> allrecords = transaction
				.getTransactionItems();
		double[] result = getTgransactionTotal(context,
				transaction.isAmountsIncludeVAT(), allrecords, isSales);
		double grandTotal = result[0] + result[1];
		transaction.setTotal(grandTotal);
		transaction.setNetAmount(result[0]);
	}

	public double[] getTgransactionTotal(Context context,
			boolean isAmountsIncludeVAT,
			List<ClientTransactionItem> allrecords, boolean isSales) {
		double lineTotal = 0.0;
		double totalTax = 0.0;

		for (ClientTransactionItem record : allrecords) {

			int type = record.getType();

			if (type == 0)
				continue;

			Double lineTotalAmt = record.getLineTotal();
			lineTotal += lineTotalAmt;

			if (record != null && record.isTaxable()) {
				double taxAmount = getVATAmount(context, isAmountsIncludeVAT,
						record.getTaxCode(), record, isSales);
				if (isAmountsIncludeVAT) {
					lineTotal -= taxAmount;
				}
				record.setVATfraction(taxAmount);
				totalTax += record.getVATfraction();
			}
		}

		double[] result = new double[2];
		result[0] = lineTotal;
		result[1] = totalTax;
		return result;
	}

	public double getVATAmount(Context context, boolean isAmountsIncludeVAT,
			long TAXCodeID, ClientTransactionItem record, boolean isSales) {

		double vatRate = 0.0;
		try {
			if (TAXCodeID != 0) {
				// Checking the selected object is VATItem or VATGroup.
				// If it is VATItem,the we should get 'VATRate',otherwise
				// 'GroupRate
				ClientTAXCode taxCode = context.getClientCompany().getTAXCode(
						TAXCodeID);
				if (!taxCode.getName().equals("EGS")
						&& !taxCode.getName().equals("EGZ")
						&& !taxCode.getName().equals("RC")) {
					ClientTAXItemGroup vatItemGroup = context
							.getClientCompany()
							.getTAXItemGroup(
									isSales ? taxCode.getTAXItemGrpForSales()
											: taxCode
													.getTAXItemGrpForPurchases());
					if (vatItemGroup != null) {
						if (vatItemGroup instanceof ClientTAXItem) {
							return ((ClientTAXItem) vatItemGroup).getTaxRate();
						}
						vatRate = ((ClientTAXGroup) vatItemGroup)
								.getGroupRate();
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Double vat = 0.0;
		if (isAmountsIncludeVAT) {
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
