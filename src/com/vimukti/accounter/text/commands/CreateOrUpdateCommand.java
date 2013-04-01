package com.vimukti.accounter.text.commands;

import java.util.ArrayList;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.TransactionItem;

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

	/**
	 * Calculating the Transaction Total
	 * 
	 * @param transactionItems
	 *            Transaction Items
	 * @return {@link Double} Transaction Total
	 */
	protected double getTransactionTotal(
			ArrayList<TransactionItem> transactionItems) {
		double total = 0.0;
		for (TransactionItem transactionItem : transactionItems) {
			total += transactionItem.getLineTotal();
		}
		return total;
	}

	public class TransctionItem {

		private String item;
		private Double unitPrice;
		private Quantity quantity;
		private String tax;
		private String description;
		private Double amount;
		private String account;

		public String getDescription() {
			return description;
		}

		public String getTax() {
			return this.tax;
		}

		public Quantity getQuantity() {
			return this.quantity;
		}

		public Double getUnitPrice() {
			return this.unitPrice;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public void setTax(String tax) {
			this.tax = tax;
		}

		public void setQuantity(Quantity quantity) {
			this.quantity = quantity;
		}

		public void setUnitPrice(Double unitPrice) {
			this.unitPrice = unitPrice;
		}

		public Double getAmount() {
			return amount;
		}

		public void setAmount(Double amount) {
			this.amount = amount;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public String getItem() {
			return item;
		}

		public void setItem(String item) {
			this.item = item;
		}

	}
}
