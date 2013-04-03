package com.vimukti.accounter.text.commands.transaction;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.text.commands.CreateOrUpdateCommand;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

public abstract class AbstractTransactionCommand extends CreateOrUpdateCommand {
	// parse transaction Items
	public ArrayList<TransctionItem> items = new ArrayList<TransctionItem>();
	// Transaction Date
	public FinanceDate transactionDate;

	/**
	 * Calculating the Line Total With Tax.
	 * 
	 * @param itemTotal
	 * @param tax
	 * @return {@link Double} Line Total
	 */
	private Double getLineTotal(Double itemTotal, String tax) {
		double lineTotal = itemTotal;
		if (tax != null && !tax.isEmpty()) {
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

	/**
	 * parse the Transaction Date
	 * 
	 * @param data
	 * @param respnse
	 * @return
	 */
	protected boolean parseTransactionDate(ITextData data, ITextResponse respnse) {
		// Transaction Date
		if (!data.isDate()) {
			respnse.addError("Invalid Date format for date field");
			return false;
		}
		transactionDate = data.nextDate(new FinanceDate());
		return true;
	}

	/**
	 * Parsing the Transaction Item And Creating Transaction Items List
	 * 
	 * @param data
	 * @param respnse
	 */
	protected boolean parseTransactionItem(ITextData data, ITextResponse respnse) {
		TransctionItem transactionItem = new TransctionItem();
		String itemName = data.nextString("");
		if (itemName != null) {
			// Item Name
			transactionItem.setItem(itemName);
			// Description
			transactionItem.setDescription(data.nextString(null));
			// Unit Price
			if (!data.isDouble()) {
				respnse.addError("Invalid Double for Unit Price field");
				return false;
			}
			transactionItem.setUnitPrice(data.nextDouble(0));
			// Quantity
			if (!data.isQuantity()) {
				respnse.addError("Invalid Quantity format for quantity field");
				return false;
			}
			transactionItem.setQuantity(data.nextQuantity(null));
			// Tax
			transactionItem.setTax(data.nextString(null));
		} else {
			// forwarding the Positions
			data.forward(4);
			String account = data.nextString("");
			transactionItem.setAccount(account);
			if (!data.isDouble()) {
				respnse.addError("Invalid Double for Unit Price field");
				return false;
			}
			transactionItem.setDescription(data.nextString(null));
			transactionItem.setAmount(data.nextDouble(0));
			transactionItem.setTax(data.nextString(null));
		}
		items.add(transactionItem);
		return true;
	}

	/**
	 * 
	 * @return
	 * @throws AccounterException
	 */
	protected ArrayList<TransactionItem> processCustomerTransactionItem()
			throws AccounterException {
		return processTransactionItem(true);

	}

	/**
	 * 
	 * @return
	 * @throws AccounterException
	 */
	protected ArrayList<TransactionItem> processVendorTransactionItem()
			throws AccounterException {
		return processTransactionItem(false);

	}

	/**
	 * 
	 * @param respnse
	 * @return
	 * @throws AccounterException
	 */
	protected ArrayList<TransactionItem> processTransactionItem(
			boolean isCustomer) throws AccounterException {
		ArrayList<TransactionItem> transactionItems = new ArrayList<TransactionItem>();
		for (TransctionItem titem : items) {
			String itemName = titem.getItem();
			if (itemName != null) {
				transactionItems.add(processTransactionItem(titem, isCustomer));
			} else {
				transactionItems.add(processAccountTransactionItem(titem));
			}
		}
		return transactionItems;
	}

	/**
	 * 
	 * @param session
	 * @param titem
	 * @return
	 */
	private TransactionItem processAccountTransactionItem(TransctionItem titem) {
		Session session = HibernateUtil.getCurrentSession();

		TransactionItem transcItem = new TransactionItem();
		String accountName = titem.getAccount();
		Criteria accountQuery = session.createCriteria(Account.class);
		accountQuery.add(Restrictions.eq("company", getCompany()));
		accountQuery.add(Restrictions.eq("name", accountName));
		Account account = (Account) accountQuery.uniqueResult();
		if (account == null) {
			account = new Account();
			account.setName(accountName);
			session.save(account);
		}
		transcItem.setAccount(account);
		transcItem.setUnitPrice(titem.getAmount());
		// getting Line Total
		Double lineTotal = getLineTotal(titem.getAmount(), titem.getTax());
		transcItem.setLineTotal(lineTotal);
		String desc = titem.getDescription();
		if (desc != null) {
			transcItem.setDescription(desc);
		}
		return transcItem;

	}

	/**
	 * Process The Transaction Item
	 * 
	 * @param session
	 * @param titem
	 * @return
	 * @throws AccounterException
	 */
	private TransactionItem processTransactionItem(TransctionItem titem,
			boolean isCustomer) throws AccounterException {
		TransactionItem transcItem = new TransactionItem();
		String itemName = titem.getItem();
		Item item = getObject(Item.class, "name", itemName);
		if (item == null) {
			item = new Item();
			item.setName(itemName);
			item.setActive(true);
			// if customer true the Item type is sell this item
			if (isCustomer) {
				item.setISellThisItem(true);
				item.setType(Item.TYPE_SERVICE);
				Account incomeAccount = getObject(Account.class, "name",
						"Sales Software");
				item.setIncomeAccount(incomeAccount);
			} else {
				item.setIBuyThisItem(false);
				item.setType(Item.TYPE_NON_INVENTORY_PART);
				Account expenseAccount = getObject(Account.class, "name",
						"Sales Software");
				item.setExpenseAccount(expenseAccount);
			}
			saveOrUpdate(item);
		}
		transcItem.setItem(item);

		transcItem.setUnitPrice(titem.getUnitPrice());
		transcItem.setQuantity(titem.getQuantity());

		Double itemTotal = transcItem.getUnitPrice()
				* transcItem.getQuantity().getValue();
		String tax = titem.getTax();
		// getting Line Total
		Double lineTotal = getLineTotal(itemTotal, tax);
		transcItem.setLineTotal(lineTotal);

		String desc = titem.getDescription();
		if (desc != null) {
			transcItem.setDescription(desc);
		}
		return transcItem;
	}
}
