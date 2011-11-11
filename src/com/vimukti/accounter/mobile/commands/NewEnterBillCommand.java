package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Contact;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
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
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
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

		return getMessages().create(getConstants().enterBill());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().enterBill());
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
		get(CURRENCY).setDefaultValue(null);
		get(CURRENCY_FACTOR).setDefaultValue(1.0);
		get(IS_VAT_INCLUSIVE).setDefaultValue(false);

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().enterBill());
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new VendorRequirement(VENDOR, getMessages().pleaseSelect(
				getConstants().Vendor()), getConstants().vendor(), false, true,
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
				getMessages().selectTypeOfThis(getConstants().purchaseOrder()),
				getConstants().purchaseOrderList(), true, true, null) {

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
		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getConstants().currency()), getConstants().currency(), true,
				true, null) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getPreferences().isEnableMultiCurrency()) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected List<Currency> getLists(Context context) {
				return new ArrayList<Currency>(context.getCompany()
						.getCurrencies());
			}
		});

		list.add(new AmountRequirement(CURRENCY_FACTOR, getMessages()
				.pleaseSelect(getConstants().currency()), getConstants()
				.currency(), false, true) {
			@Override
			protected String getDisplayValue(Double value) {
				ClientCurrency primaryCurrency = getPreferences()
						.getPrimaryCurrency();
				Currency selc = get(CURRENCY).getValue();
				return "1 " + selc.getFormalName() + " = " + value + " "
						+ primaryCurrency.getFormalName();
			}

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (get(CURRENCY).getValue() != null) {
					if (getPreferences().isEnableMultiCurrency()
							&& !((Currency) get(CURRENCY).getValue())
									.equals(getPreferences()
											.getPrimaryCurrency())) {
						return super.run(context, makeResult, list, actions);
					}
				}
				return null;

			}
		});

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().billNo()), getConstants().billNo(), true, true));

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().transactionDate()), getConstants()
				.transactionDate(), true, true));

		list.add(new DateRequirement(DUE_DATE, getMessages().pleaseEnter(
				getConstants().dueDate()), getConstants().dueDate(), true, true));

		list.add(new DateRequirement(DELIVERY_DATE, getMessages().pleaseEnter(
				getConstants().deliveryDate()), getConstants().deliveryDate(),
				true, true));

		list.add(new PaymentTermRequirement(PAYMENT_TERMS, getMessages()
				.pleaseSelect(getConstants().paymentTerm()), getConstants()
				.paymentTerms(), true, true, null) {

			@Override
			protected List<PaymentTerms> getLists(Context context) {
				return new ArrayList<PaymentTerms>(context.getCompany()
						.getPaymentTerms());
			}
		});

		list.add(new TransactionAccountTableRequirement(ACCOUNTS,
				"please select account Items", getConstants().Account(), true,
				true) {

			@Override
			protected List<Account> getAccounts(Context context) {
				return Utility.filteredList(new ListFilter<Account>() {

					@Override
					public boolean filter(Account account) {
						if (account.getType() != Account.TYPE_CASH
								&& account.getType() != Account.TYPE_BANK
								&& account.getType() != Account.TYPE_INVENTORY_ASSET
								&& account.getType() != Account.TYPE_ACCOUNT_RECEIVABLE
								&& account.getType() != Account.TYPE_ACCOUNT_PAYABLE
								&& account.getType() != Account.TYPE_INCOME
								&& account.getType() != Account.TYPE_OTHER_INCOME
								&& account.getType() != Account.TYPE_OTHER_CURRENT_ASSET
								&& account.getType() != Account.TYPE_OTHER_CURRENT_LIABILITY
								&& account.getType() != Account.TYPE_OTHER_ASSET
								&& account.getType() != Account.TYPE_EQUITY
								&& account.getType() != Account.TYPE_LONG_TERM_LIABILITY) {
							return true;
						} else {
							return false;
						}
					}
				}, new ArrayList<Account>(context.getCompany().getAccounts()));
			}
		});

		list.add(new TransactionItemTableRequirement(ITEMS,
				"Please Enter Item Name or number", getConstants().items(),
				true, true, false) {

			@Override
			public List<Item> getItems(Context context) {
				Set<Item> items2 = context.getCompany().getItems();
				List<Item> items = new ArrayList<Item>();
				for (Item item : items2) {
					if (item.getType() != Item.TYPE_SERVICE) {
						items.add(item);
					}
				}
				return items;
			}

		});

		list.add(new ContactRequirement(CONTACT, "Please Enter contact name",
				"Contact", true, true, null) {

			@Override
			protected List<Contact> getLists(Context context) {
				return new ArrayList<Contact>(
						((Customer) NewEnterBillCommand.this.get(VENDOR)
								.getValue()).getContacts());
			}

			@Override
			protected String getContactHolderName() {
				return ((Vendor) get(VENDOR).getValue()).getName();
			}
		});

		list.add(new NumberRequirement(PHONE, getMessages().pleaseEnter(
				getConstants().phoneNumber()), getConstants().phoneNumber(),
				true, true));

		list.add(new TaxCodeRequirement(TAXCODE, getMessages().pleaseSelect(
				getConstants().taxCode()), getConstants().taxCode(), false,
				true, null) {

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
				getConstants().memo()), getConstants().memo(), true, true));
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
				cct = getpurchaseOrders(e.getTransactionId(), context);
				addpurchaseOrders(cct, items);
			}
		}
		if (preferences.isEnableMultiCurrency()) {
			Currency currency = get(CURRENCY).getValue();
			if (currency != null) {
				enterBill.setCurrency(currency.getID());
			}

			double factor = get(CURRENCY_FACTOR).getValue();
			enterBill.setCurrencyFactor(factor);
		}
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

	private void addpurchaseOrders(ClientPurchaseOrder cct,
			List<ClientTransactionItem> items) {
		for (ClientTransactionItem cst : cct.getTransactionItems()) {
			ClientTransactionItem clientItem = new ClientTransactionItem();
			if (cst.getLineTotal() != 0.0) {
				clientItem.setDescription(cst.getDescription());
				clientItem.setType(cst.getType());
				clientItem.setAccount(cst.getAccount());
				clientItem.setItem(cst.getItem());
				clientItem.setVATfraction(cst.getVATfraction());
				clientItem.setTaxCode(cst.getTaxCode());
				clientItem.setDescription(cst.getDescription());
				clientItem.setQuantity(cst.getQuantity());
				clientItem.setUnitPrice(cst.getUnitPrice());
				clientItem.setDiscount(cst.getDiscount());
				clientItem.setLineTotal(cst.getLineTotal() - cst.getInvoiced());
				clientItem.setTaxable(cst.isTaxable());
				clientItem.setReferringTransactionItem(cst.getID());

				items.add(clientItem);
			}
		}

	}

	private ClientPurchaseOrder getpurchaseOrders(long transactionId,
			Context context) {
		ClientPurchaseOrder cPurchaseOrder = null;
		try {
			cPurchaseOrder = new FinanceTool().getManager().getObjectById(
					AccounterCoreType.PURCHASEORDER, transactionId,
					context.getCompany().getID());
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
}