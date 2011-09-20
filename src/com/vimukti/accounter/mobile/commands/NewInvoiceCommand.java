package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Invoice;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.TAXGroup;
import com.vimukti.accounter.core.TAXItem;
import com.vimukti.accounter.core.TAXItemGroup;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class NewInvoiceCommand extends AbstractTransactionCommand {

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new Requirement("customer", false, true));
		list.add(new ObjectListRequirement("items", false, true) {

			@Override
			public void addRequirements(List<Requirement> list) {
				list.add(new Requirement("name", false, true));
				list.add(new Requirement("desc", true, true));
				list.add(new Requirement("quantity", true, true));
				list.add(new Requirement("price", true, true));
				list.add(new Requirement("vatCode", true, true));
			}
		});

		list.add(new Requirement(DATE, true, true));
		list.add(new Requirement("number", true, false));
		list.add(new Requirement("paymentTerms", true, true));
		list.add(new Requirement("contact", true, true));
		list.add(new Requirement("billTo", true, true));
		list.add(new Requirement("shipTo", true, true));
		list.add(new Requirement("due", true, true));
		list.add(new Requirement(ORDER_NO, true, true));
		list.add(new Requirement(MEMO, true, true));
		Company company = getCompany();
		if (company.getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			list.add(new Requirement("tax", false, true));
		}
	}

	@Override
	public Result run(Context context) {
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = null;
		if (process != null) {
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			} else if (process.equals(TRANSACTION_ITEM_PROCESS)) {
				result = transactionItemProcess(context);
				if (result != null) {
					return result;
				}
			}
		}

		result = customerRequirement(context);
		if (result != null) {
			return result;
		}

		result = itemsRequirement(context);
		if (result != null) {
			return result;
		}

		Company company = getCompany();
		if (company.getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			Requirement taxReq = get("tax");
			TAXCode taxcode = context.getSelection(TAXCODE);
			if (!taxReq.isDone()) {
				if (taxcode != null) {
					taxReq.setValue(taxcode);
				} else {
					return taxCode(context, null);
				}
			}
			if (taxcode != null) {
				taxReq.setValue(taxcode);
			}
		}

		result = createOptionalResult(context);
		if (result != null) {
			return result;
		}
		completeProcess(context);
		markDone();
		return null;
	}

	private void completeProcess(Context context) {
		Company company = getCompany();
		Invoice invoice = new Invoice();

		Date date = get(DATE).getValue();
		invoice.setDate(new FinanceDate(date));

		invoice.setType(Transaction.TYPE_INVOICE);

		String number = get("number").getValue();
		invoice.setNumber(number);

		List<TransactionItem> items = get("items").getValue();
		invoice.setTransactionItems(items);

		// TODO Location
		// TODO Class

		if (company.getAccountingType() == Company.ACCOUNTING_TYPE_US) {
			TAXCode taxCode = get("tax").getValue();
			for (TransactionItem item : items) {
				item.setTaxCode(taxCode);
			}
			// TODO if (getCompany().getPreferences().isChargeSalesTax()) {
			// if (taxCode != null) {
			// for (TransactionItem record : items) {
			// record.setTaxItemGroup(taxCode.getTAXItemGrpForSales());
			// }
			// }
			// transaction.setSalesTaxAmount(this.salesTax);
			// }
		}

		Customer customer = get("customer").getValue();
		invoice.setCustomer(customer);

		Address shipTp = get("shipTo").getValue();
		Set<Address> address = new HashSet<Address>();
		address.add(shipTp);
		customer.setAddress(address);
		invoice.setShippingAdress(shipTp);

		Date dueDate = get("due").getValue();
		invoice.setDueDate(new FinanceDate(dueDate));

		Contact contact = get("contact").getValue();
		invoice.setContact(contact);

		Address billTo = get("billTo").getValue();
		invoice.setBillingAddress(billTo);

		PaymentTerms paymentTerm = get("paymentTerms").getValue();
		invoice.setPaymentTerm(paymentTerm);

		String orderNo = get(ORDER_NO).getValue();
		invoice.setOrderNum(orderNo);

		invoice.setTotal(getTransactionTotal(items, company));

		// TODO Payments

		String memo = get(MEMO).getValue();
		invoice.setMemo(memo);

		// TODO Discount Date
		// TODO Estimates
		// TODO sales Order
		create(invoice, context);
	}

	private double getTransactionTotal(List<TransactionItem> items,
			Company company) {

		int totaldiscount = 0;
		double totallinetotal = 0.0;
		double taxableTotal = 0.0;
		double totalVat = 0.0;
		double grandTotal = 0.0;
		double totalValue = 0.0;
		int accountType = getCompany().getAccountingType();
		for (TransactionItem citem : items) {
			totaldiscount += citem.getDiscount();

			Double lineTotalAmt = citem.getLineTotal();
			totallinetotal += lineTotalAmt;

			if (citem != null && citem.isTaxable()) {
				// ClientTAXItem taxItem = getCompany().getTAXItem(
				// citem.getTaxCode());
				// if (taxItem != null) {
				// totalVat += taxItem.getTaxRate() / 100 * lineTotalAmt;
				// }
				taxableTotal += lineTotalAmt;
			}

			citem.setVATfraction(getVATAmount(citem.getTaxCode(), citem,
					company));
			totalVat += citem.getVATfraction();
			// totalVat += citem.getVATfraction();
		}

		if (getCompany().getPreferences().isChargeSalesTax()) {
			grandTotal = totalVat + totallinetotal;
		} else {
			grandTotal = totallinetotal;
			totalValue = grandTotal;
		}
		if (getCompany().getPreferences().isRegisteredForVAT()) {
			// if (transactionView.vatinclusiveCheck != null
			// && (Boolean) transactionView.vatinclusiveCheck.getValue()) {
			// grandTotal = totallinetotal - totalVat;
			// setTotalValue(totallinetotal);
			//
			// } else {
			grandTotal = totallinetotal;
			totalValue = grandTotal + totalVat;
			// }
		} else {
			grandTotal = totallinetotal;
			totalValue = grandTotal;
		}
		return totallinetotal;
	}

	public double getVATAmount(TAXCode taxCode, TransactionItem record,
			Company company) {

		double vatRate = 0.0;
		if (taxCode != null) {
			// Checking the selected object is VATItem or VATGroup.
			// If it is VATItem,the we should get 'VATRate',otherwise 'GroupRate
			try {

				TAXItemGroup item = taxCode.getTAXItemGrpForSales();
				if (item == null) {
					vatRate = 0.0;
				} else if (item instanceof TAXItem) {
					// The selected one is VATItem,so get 'VATRate' from
					// 'VATItem'
					vatRate = ((TAXItem) item).getTaxRate();
				} else {
					// The selected one is VATGroup,so get 'GroupRate' from
					// 'VATGroup'
					vatRate = ((TAXGroup) item).getGroupRate();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Double vat = 0.0;
		vat = record.getLineTotal() * vatRate / 100;
		vat = UIUtils.getRoundValue(vat);
		return vat.doubleValue();
	}

	private Result createOptionalResult(Context context) {
		context.setAttribute(INPUT_ATTR, "optional");

		Object selection = context.getSelection(ACTIONS);
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_ITEMS:
				return items(context);
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		Requirement itemsReq = get("items");
		List<TransactionItem> transItems = itemsReq.getValue();

		selection = context.getSelection("transactionItems");
		if (selection != null) {
			Result result = transactionItem(context,
					(TransactionItem) selection);
			if (result != null) {
				return result;
			}
		}

		selection = context.getSelection("values");
		ResultList list = new ResultList("values");

		Requirement custmerReq = get("customer");
		Customer customer = (Customer) custmerReq.getValue();
		Record custRecord = new Record(customer);
		custRecord.add("Name", "Customer");
		custRecord.add("Value", customer.getName());

		list.add(custRecord);

		Result result = dateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = contactRequirement(context, list, selection, customer);
		if (result != null) {
			return result;
		}

		result = paymentTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = invoiceNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = billToRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = shipToRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = dueDateRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = orderNoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = memoRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = context.makeResult();
		result.add("Invoice is ready to create with following values.");
		result.add(list);

		result.add("Items:-");
		ResultList items = new ResultList("transactionItems");
		for (TransactionItem item : transItems) {
			Record itemRec = new Record(item);
			itemRec.add("Name", item.getItem().getName());
			itemRec.add("Total", item.getLineTotal());
			itemRec.add("VatCode", item.getVATfraction());
		}
		result.add(items);

		ResultList actions = new ResultList(ACTIONS);
		Record moreItems = new Record(ActionNames.ADD_MORE_ITEMS);
		moreItems.add("", "Add more items");
		actions.add(moreItems);
		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Invoice.");
		actions.add(finish);
		result.add(actions);

		return result;
	}

	private Result dueDateRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("due");
		Date dueDate = (Date) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals("dueDate")) {
			Date date = context.getSelection(DATE);
			if (date == null) {
				date = context.getDate();
			}
			dueDate = date;
			req.setValue(dueDate);
		}
		if (selection == dueDate) {
			context.setAttribute(INPUT_ATTR, "dueDate");
			return date(context, "Due", dueDate);
		}

		Record dueDateRecord = new Record(dueDate);
		dueDateRecord.add("Name", "Due Date");
		dueDateRecord.add("Value", dueDate.toString());
		list.add(dueDateRecord);
		return null;
	}

	private Result invoiceNoRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("number");
		String invoiceNo = (String) req.getValue();

		String attribute = (String) context.getAttribute(INPUT_ATTR);
		if (attribute.equals(ORDER_NO)) {
			String order = context.getSelection(NUMBER);
			if (order == null) {
				order = context.getString();
			}
			invoiceNo = order;
			req.setValue(invoiceNo);
		}

		if (selection == invoiceNo) {
			context.setAttribute(INPUT_ATTR, ORDER_NO);
			return number(context, "Enter Invoice number", invoiceNo);
		}

		Record invoiceNoRec = new Record(invoiceNo);
		invoiceNoRec.add("Name", "Invoice Number");
		invoiceNoRec.add("Value", invoiceNo);
		list.add(invoiceNoRec);
		return null;
	}

}