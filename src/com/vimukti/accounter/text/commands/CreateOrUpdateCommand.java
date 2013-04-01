package com.vimukti.accounter.text.commands;

import com.vimukti.accounter.core.Company;

public abstract class CreateOrUpdateCommand implements ITextCommand {

	protected Company getCompany() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Calculating the Line Total With Tax.
	 * 
	 * @param itemTotal
	 * @param tax
	 * @return {@link Double} Line Total
	 */
	protected Double getLineTotal(Double itemTotal, String tax) {
		double lineTotal = itemTotal;
		if (tax != null) {
			double taxTotal = 0;
			// checking Tax is Percentage Value..
			if (tax.contains("%")) {
				double taxRate = Double.valueOf(tax.replace("%", ""));
				// calculating the Tax Total On Item Total
				taxTotal = (itemTotal / 100) * taxRate;
			} else {
				double taxAmount = Double.valueOf(tax);
				taxTotal = taxAmount;
			}
			// adding Tax Value To Item Total
			lineTotal += taxTotal;
		}
		return lineTotal;
	}
}
