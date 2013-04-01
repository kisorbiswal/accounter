package com.vimukti.accounter.text.commands;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.core.ActivityType;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CreatableObject;
import com.vimukti.accounter.core.IAccounterServerCore;
import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.web.client.exception.AccounterException;

public abstract class CreateOrUpdateCommand extends AbstractTextCommand {

	protected void saveOrUpdate(IAccounterServerCore serverObj)
			throws AccounterException {
		Company company = getCompany();
		if (serverObj instanceof CreatableObject) {
			((CreatableObject) serverObj).setCompany(company);
		}
		serverObj.selfValidate();
		Session session = getSession();
		boolean isAdd = serverObj.getID() == 0;
		Transaction transaction = session.beginTransaction();
		try {
			// Save or Update Object
			session.saveOrUpdate(serverObj);

			// Create Activity
			Activity activity = new Activity(company, getUser(),
					(isAdd ? ActivityType.ADD : ActivityType.EDIT), serverObj);
			session.save(activity);

			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			throw new AccounterException("Error while saving.");
		}
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
