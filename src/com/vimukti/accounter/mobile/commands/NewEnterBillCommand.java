package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.PurchaseOrderListRequirements;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionAccountTableRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemTableRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewEnterBillCommand extends NewAbstractTransactionCommand {

	private static String PURCHASE_ORDER = "purchaseOrder";

	@Override
	protected String getWelcomeMessage() {

		return getMessages().create(getMessages().enterBill());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().enterBill());
	}

	@Override
	protected void setDefaultValues(Context context) {

		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(NUMBER)
				.setDefaultValue(
						NumberUtils.getNextTransactionNumber(
								ClientTransaction.TYPE_ENTER_BILL,
								context.getCompany()));
		get(PHONE).setDefaultValue("");
		get(CONTACT).setDefaultValue(null);
		Set<PaymentTerms> paymentTerms = context.getCompany().getPaymentTerms();
		for (PaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}

		get(DUE_DATE).setDefaultValue(new ClientFinanceDate());
		get(DELIVERY_DATE).setDefaultValue(new ClientFinanceDate());

		Vendor v = (Vendor) get(VENDOR).getValue();
		if (v != null) {
			Set<Contact> contacts2 = v.getContacts();
			if (contacts2 != null)
				for (Contact c : contacts2) {
					get(CONTACT).setDefaultValue(c);
				}
		}
		get(MEMO).setDefaultValue("");
		/*
		 * get(CURRENCY).setDefaultValue(null);
		 * get(CURRENCY_FACTOR).setDefaultValue(1.0);
		 */
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().enterBill());
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new VendorRequirement(VENDOR, getMessages().pleaseSelect(
				getMessages().Vendor()), getMessages().vendor(), false, true,
				null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(Global.get().Vendor());
			}

			@Override
			protected List<Vendor> getLists(Context context) {
				return new ArrayList<Vendor>(context.getCompany().getVendors());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Vendor());
			}

			@Override
			protected boolean filter(Vendor e, String name) {
				return e.getName().startsWith(name);
			}
		});
		list.add(new PurchaseOrderListRequirements(PURCHASE_ORDER,
				getMessages().selectTypeOfThis(getMessages().purchaseOrder()),
				getMessages().purchaseOrderList(), true, true,
				new ChangeListner<PurchaseOrdersList>() {

					@Override
					public void onSelection(PurchaseOrdersList e) {
						ClientPurchaseOrder cct = null;
						if (e != null) {
							if (e.getType() == ClientTransaction.TYPE_PURCHASE_ORDER) {
								cct = getpurchaseOrders(e.getTransactionId());
								selectedPurchaseOrder(cct);
							}
						}
					}
				}) {

			@Override
			protected List<PurchaseOrdersList> getLists(Context context) {
				try {
					return new FinanceTool()
							.getPurchageManager()
							.getPurchaseOrdersList(context.getCompany().getID());
				} catch (DAOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected boolean filter(PurchaseOrdersList e, String name) {
				return e.getVendorName().contains(name);
			}
		});
		/*
		 * list.add(new CurrencyRequirement(CURRENCY,
		 * getMessages().pleaseSelect( getConstants().currency()),
		 * getConstants().currency(), true, true, null) {
		 * 
		 * @Override public Result run(Context context, Result makeResult,
		 * ResultList list, ResultList actions) { if
		 * (getPreferences().isEnableMultiCurrency()) { return
		 * super.run(context, makeResult, list, actions); } else { return null;
		 * } }
		 * 
		 * @Override protected List<Currency> getLists(Context context) { return
		 * new ArrayList<Currency>(context.getCompany() .getCurrencies()); } });
		 * 
		 * list.add(new AmountRequirement(CURRENCY_FACTOR, getMessages()
		 * .pleaseSelect(getConstants().currency()), getConstants() .currency(),
		 * false, true) {
		 * 
		 * @Override protected String getDisplayValue(Double value) {
		 * ClientCurrency primaryCurrency = getPreferences()
		 * .getPrimaryCurrency(); Currency selc = get(CURRENCY).getValue();
		 * return "1 " + selc.getFormalName() + " = " + value + " " +
		 * primaryCurrency.getFormalName(); }
		 * 
		 * @Override public Result run(Context context, Result makeResult,
		 * ResultList list, ResultList actions) { if (get(CURRENCY).getValue()
		 * != null) { if (getPreferences().isEnableMultiCurrency() &&
		 * !((Currency) get(CURRENCY).getValue()) .equals(getPreferences()
		 * .getPrimaryCurrency())) { return super.run(context, makeResult, list,
		 * actions); } } return null;
		 * 
		 * } });
		 */

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getMessages().billNo()), getMessages().billNo(), true, true));

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getMessages().transactionDate()), getMessages()
				.transactionDate(), true, true));

		list.add(new DateRequirement(DUE_DATE, getMessages().pleaseEnter(
				getMessages().dueDate()), getMessages().dueDate(), true, true));

		list.add(new DateRequirement(DELIVERY_DATE, getMessages().pleaseEnter(
				getMessages().deliveryDate()), getMessages().deliveryDate(),
				true, true));

		list.add(new PaymentTermRequirement(PAYMENT_TERMS, getMessages()
				.pleaseSelect(getMessages().paymentTerm()), getMessages()
				.paymentTerms(), true, true, null) {

			@Override
			protected List<PaymentTerms> getLists(Context context) {
				return new ArrayList<PaymentTerms>(context.getCompany()
						.getPaymentTerms());
			}
		});

		list.add(new TransactionAccountTableRequirement(ACCOUNTS,
				"please select account Items", getMessages().Account(), true,
				true) {

			@Override
			protected List<Account> getAccounts(Context context) {
				return Utility.filteredList(new ListFilter<Account>() {

					@Override
					public boolean filter(Account account) {
						if (account.getType() == Account.TYPE_EXPENSE
								|| account.getType() == Account.TYPE_COST_OF_GOODS_SOLD
								|| account.getType() == Account.TYPE_OTHER_EXPENSE) {
							return true;
						} else {
							return false;
						}
						// return Arrays.asList(Account.TYPE_EXPENSE,
						// Account.TYPE_COST_OF_GOODS_SOLD,
						// Account.TYPE_OTHER_EXPENSE).contains(
						// account.getType());
					}
				}, new ArrayList<Account>(context.getCompany().getAccounts()));
			}
		});

		list.add(new TransactionItemTableRequirement(ITEMS,
				"Please Enter Item Name or number", getMessages().items(),
				true, true) {

			@Override
			public List<Item> getItems(Context context) {
				Set<Item> items2 = context.getCompany().getItems();
				List<Item> items = new ArrayList<Item>();
				for (Item item : items2) {
					if (item.isIBuyThisItem()) {
						items.add(item);
					}
				}
				return items;
			}

			@Override
			public boolean isSales() {
				return false;
			}

		});

		list.add(new ContactRequirement(CONTACT, "Please Enter contact name",
				"Contact", true, true, null) {

			@Override
			protected List<Contact> getLists(Context context) {
				return new ArrayList<Contact>(
						((Vendor) NewEnterBillCommand.this.get(VENDOR)
								.getValue()).getContacts());
			}

			@Override
			protected String getContactHolderName() {
				return ((Vendor) get(VENDOR).getValue()).getName();
			}
		});

		list.add(new NumberRequirement(PHONE, getMessages().pleaseEnter(
				getMessages().phoneNumber()), getMessages().phoneNumber(),
				true, true));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getMessages().taxCode()), getMessages().taxCode(), false, true,
				null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isTrackTax()
						&& !getPreferences().isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<TAXCode> getLists(Context context) {
				return new ArrayList<TAXCode>(context.getCompany()
						.getTaxCodes());
			}

			@Override
			protected boolean filter(TAXCode e, String name) {
				return e.getName().contains(name);
			}
		});

		list.add(new BooleanRequirement(IS_VAT_INCLUSIVE, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				ClientCompanyPreferences preferences = context.getPreferences();
				if (preferences.isTrackTax()
						&& !preferences.isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return "Include VAT with Amount enabled";
			}

			@Override
			protected String getFalseString() {
				return "Include VAT with Amount disabled";
			}
		});

		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getMessages().memo()), getMessages().memo(), true, true));
	}

	@Override
	public void beforeFinishing(Context context, Result makeResult) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			addFirstMessage(
					context,
					"Transaction total can not zero or less than zero.So you can't finish this command");
		}
		super.beforeFinishing(context, makeResult);
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		if (items.isEmpty() && accounts.isEmpty()) {
			return new Result();
		}

		ClientEnterBill enterBill = new ClientEnterBill();

		Vendor vendor = (Vendor) get(VENDOR).getValue();
		enterBill.setVendor(vendor.getID());
		ClientFinanceDate date = get(DATE).getValue();
		if (date != null) {
			enterBill.setDate(date.getDate());
		} else {
			enterBill.setDate(System.currentTimeMillis());
		}

		enterBill.setType(ClientTransaction.TYPE_ENTER_BILL);

		items.addAll(accounts);

		Boolean isVatInclusive = get(IS_VAT_INCLUSIVE).getValue();
		ClientCompanyPreferences preferences = context.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			enterBill.setAmountsIncludeVAT(isVatInclusive);
			TAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : accounts) {
				item.setTaxCode(taxCode.getID());
			}
		}
		PurchaseOrdersList e = get(PURCHASE_ORDER).getValue();
		ClientPurchaseOrder cct = null;
		if (e != null) {
			if (e.getType() == ClientTransaction.TYPE_PURCHASE_ORDER) {
				cct = getpurchaseOrders(e.getTransactionId());
				enterBill.setPurchaseOrder(cct.getID());
			}
		}

		/*
		 * if (preferences.isEnableMultiCurrency()) { Currency currency =
		 * get(CURRENCY).getValue(); if (currency != null) {
		 * enterBill.setCurrency(currency.getID()); }
		 * 
		 * double factor = get(CURRENCY_FACTOR).getValue();
		 * enterBill.setCurrencyFactor(factor); }
		 */
		enterBill.setTransactionItems(items);
		updateTotals(context, enterBill, false);

		ClientFinanceDate dueDate = get(DUE_DATE).getValue();
		enterBill.setDueDate(dueDate.getDate());

		ClientFinanceDate deliveryDate = get(DELIVERY_DATE).getValue();
		enterBill.setDeliveryDate(deliveryDate);

		Contact contact = get(CONTACT).getValue();
		enterBill.setContact(toClientContact(contact));

		PaymentTerms paymentTerm = get("paymentTerms").getValue();
		enterBill.setPaymentTerm(paymentTerm.getID());

		String phone = get(PHONE).getValue();
		enterBill.setPhone(phone);

		create(enterBill, context);
		return null;
	}

	/**
	 * get the purchase Order object by id
	 * 
	 * @param transactionId
	 * @return
	 */
	private ClientPurchaseOrder getpurchaseOrders(long transactionId) {
		ClientPurchaseOrder cPurchaseOrder = null;
		try {
			cPurchaseOrder = new FinanceTool().getManager().getObjectById(
					AccounterCoreType.PURCHASEORDER, transactionId,
					getCompanyId());
		} catch (DAOException e) {
			e.printStackTrace();
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return cPurchaseOrder;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param purchaseOrder
	 */
	public void selectedPurchaseOrder(ClientPurchaseOrder purchaseOrder) {
		if (purchaseOrder == null) {
			return;
		}
		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();

		for (ClientTransactionItem record : accounts) {
			for (ClientTransactionItem salesRecord : purchaseOrder
					.getTransactionItems())
				if (record.getReferringTransactionItem() == salesRecord.getID()) {
					// vendorAccountTransactionTable.delete(record);;
				}
		}
		List<ClientTransactionItem> items = get(ITEMS).getValue();
		for (ClientTransactionItem item : items) {
			for (ClientTransactionItem salesRecord : purchaseOrder
					.getTransactionItems())
				if (item.getReferringTransactionItem() == salesRecord.getID()) {
					// vendorItemTransactionTable.delete(record);}
				}
		}

		// if (selectedOrdersAndItemReceipts != null)
		// selectedOrdersAndItemReceipts.add(purchaseOrder);

		List<ClientTransactionItem> itemsList = new ArrayList<ClientTransactionItem>();
		// selectedOrdersAndItemReceipts.add(purchaseOrder);

		for (ClientTransactionItem transactionitem : purchaseOrder
				.getTransactionItems()) {
			if (transactionitem.getLineTotal() - transactionitem.getInvoiced() <= 0) {
				continue;
			}
			ClientTransactionItem clientItem = new ClientTransactionItem();
			clientItem.setType(transactionitem.getType());
			clientItem.setDescription(transactionitem.getDescription());
			clientItem.setTaxCode(transactionitem.getTaxCode());
			clientItem.setReferringTransactionItem(transactionitem.getID());
			clientItem.setAccount(transactionitem.getAccount());
			clientItem.setItem(transactionitem.getItem());
			clientItem.setQuantity(transactionitem.getQuantity());
			clientItem.setUnitPrice(transactionitem.getUnitPrice());
			clientItem.setDiscount(transactionitem.getDiscount());
			clientItem.setLineTotal(transactionitem.getLineTotal()
					- transactionitem.getInvoiced());
			clientItem.setVATfraction(transactionitem.getVATfraction());
			clientItem.setVatItem(transactionitem.getVatItem());
			clientItem.setTaxable(transactionitem.isTaxable());

			itemsList.add(clientItem);

		}

		get(ACCOUNTS).setValue(getAccountTransactionItems(itemsList));
		get(ITEMS).setValue(getItemTransactionItems(itemsList));
	}

	public List<ClientTransactionItem> getAccountTransactionItems(
			List<ClientTransactionItem> transactionItems) {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem item : transactionItems) {
			if (item.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
				list.add(item);
			}
		}
		return list;
	}

	public List<ClientTransactionItem> getItemTransactionItems(
			List<ClientTransactionItem> transactionItems) {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem item : transactionItems) {
			if (item.getType() == ClientTransactionItem.TYPE_ITEM) {
				list.add(item);
			}
		}
		return list;
	}
}
