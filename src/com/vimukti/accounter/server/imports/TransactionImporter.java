/**
 * 
 */
package com.vimukti.accounter.server.imports;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientAttachment;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.imports.Field;

/**
 * @author vimukti23
 * 
 */
public abstract class TransactionImporter<T> extends
		AbstractImporter<IAccounterCore> {
	protected void getTransactionData(ClientTransaction transaction) {
		transaction.setLocation(getLong(messages.location()));
		transaction.setTransactionDate(getFinanceDate(
				messages.transactionDate()).getDate());
		transaction.setNumber(getString(messages.number()));
		transaction.setPaymentMethod(getString(messages.paymentMethod()));
		transaction.setAccounterClass(getAccounterClass(messages.className()));
		transaction.setAllLineTotal(getDouble(messages.lineTotal()));
		transaction.setAllNonTaxableLineTotal(getDouble(messages.nonTaxable()));
		transaction.setAllTaxableLineTotal(getDouble(messages.taxable()));
		transaction.setAttachments(getAttachments(messages.attachments()));
		transaction.setMemo(getString(messages.memo()));
		transaction.setNetAmount(getDouble(messages.netAmount()));
		transaction.setRecurringTransaction(getLong(messages
				.recurringTransactions()));
		transaction.setReference(getString(messages.reference()));
		transaction.setTotal(getDouble(messages.total()));
		transaction.setTransactionItems(getTransactionItem(messages
				.transactionItem()));

	}

	private List<ClientTransactionItem> getTransactionItem(
			String transactionItem) {
		List<ClientTransactionItem> items = new ArrayList<ClientTransactionItem>();
		ClientTransactionItem item = new ClientTransactionItem();
		item.setAccount(getLong(messages.account()));
		item.setItem(getLong(messages.itemName()));
		item.setDescription(getString(messages.description()));
		item.setQuantity(getClientQty(messages.quantity()));
		item.setUnitPrice(getDouble(messages.unitPrice()));
		item.setTaxCode(getLong(messages.taxCode()));
		item.setDiscount(getDouble(messages.discount()));
		item.setVATfraction(getDouble(messages.vat()));
		item.setCustomer(getLong(messages.customer()));
		items.add(item);
		return items;
	}

	private ClientQuantity getClientQty(String quantity) {
		// TODO Auto-generated method stub
		return null;
	}

	private Set<ClientAttachment> getAttachments(String attachments) {
		// TODO Auto-generated method stub
		return null;
	}

	protected ClientContact getContactData() {
		ClientContact contact = new ClientContact();
		contact.setName(getString(messages.contactName()));
		contact.setTitle(getString(messages.title()));
		contact.setBusinessPhone(getString(messages.businessPhone()));
		contact.setEmail(getString(messages.email()));
		return contact;
	}

	@Override
	public List<Field<?>> getAllFields() {
		List<Field<?>> fields = new ArrayList<Field<?>>();
		fields.add(new Field<Long>(messages.location(), messages
				.locationName(Global.get().Location())));
		fields.add(new Field<Long>(messages.transactionDate(), messages
				.transactionDate()));
		fields.add(new Field<String>(messages.number(), messages.number()));
		fields.add(new Field<String>(messages.paymentMethod(), messages
				.paymentMethod()));
		fields.add(new Field<String>(messages.className(), messages.className()));
		fields.add(new Field<Double>(messages.lineTotal(), messages.lineTotal()));
		fields.add(new Field<Double>(messages.lineTotal(), messages.lineTotal()));
		fields.add(new Field<Double>(messages.nonTaxable(), messages
				.nonTaxable()));
		fields.add(new Field<Double>(messages.taxable(), messages.taxable()));
		fields.add(new Field<Double>(messages.taxable(), messages.taxable()));
		fields.add(new Field<String>(messages.memo(), messages.memo()));
		fields.add(new Field<Double>(messages.netAmount(), messages.netAmount()));
		fields.add(new Field<Long>(messages.receivedTransactions(), messages
				.receivedTransactions()));
		fields.add(new Field<Double>(messages.receivedTransactions(), messages
				.receivedTransactions()));
		fields.add(new Field<Double>(messages.total(), messages.total()));
		fields.add(new Field<String>(messages.account(), messages.name(), true));
		fields.add(new Field<String>(messages.itemName(), messages.name(), true));
		fields.add(new Field<String>(messages.description(), messages
				.description()));
		fields.add(new Field<Long>(messages.quantity(), messages.quantity(),
				true));
		fields.add(new Field<Long>(messages.unit(), messages.unit(), true));
		fields.add(new Field<Long>(messages.taxCode(), messages.taxCode(), true));
		fields.add(new Field<Double>(messages.discount(), messages.discount()));
		fields.add(new Field<Double>(messages.vat(), messages.tax()));
		fields.add(new Field<String>(messages.customer(), Global.get()
				.Customer()));
		return fields;
	}

	private ClientAccounterClass getAccounterClass(String className) {
		// TODO Auto-generated method stub
		return null;
	}

}
