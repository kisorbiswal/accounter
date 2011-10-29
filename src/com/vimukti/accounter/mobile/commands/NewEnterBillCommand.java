package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.ContactRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemAccountsRequirement;
import com.vimukti.accounter.mobile.requirements.TransactionItemItemsRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewEnterBillCommand extends NewAbstractTransactionCommand {

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
		ArrayList<ClientPaymentTerms> paymentTerms = context.getClientCompany()
				.getPaymentsTerms();
		for (ClientPaymentTerms p : paymentTerms) {
			if (p.getName().equals("Due on Receipt")) {
				get(PAYMENT_TERMS).setDefaultValue(p);
			}
		}

		get(DUE_DATE).setDefaultValue(new ClientFinanceDate());
		get(DELIVERY_DATE).setDefaultValue(new ClientFinanceDate());

		ClientVendor v = (ClientVendor) get(VENDOR).getValue();
		if (v != null) {
			Set<ClientContact> contacts2 = v.getContacts();
			if (contacts2 != null)
				for (ClientContact c : contacts2) {
					get(CONTACT).setDefaultValue(c);
				}
		}
		get(MEMO).setDefaultValue("");

		get(CURRENCY).setDefaultValue(null);

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
		list.add(new VendorRequirement(VENDOR, getMessages()
				.pleaseSelectVendor(getConstants().Vendor()), getConstants()
				.vendor(), false, true, new ChangeListner<ClientVendor>() {

			@Override
			public void onSelection(ClientVendor value) {
				// TODO Auto-generated method stub

			}
		})

		{

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected List<ClientVendor> getLists(Context context) {
				return context.getClientCompany().getVendors();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			protected boolean filter(ClientVendor e, String name) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getConstants().currency()), getConstants().currency(), true,
				true, new ChangeListner<ClientCurrency>() {

					@Override
					public void onSelection(ClientCurrency value) {

					}
				}) {

			@Override
			protected List<ClientCurrency> getLists(Context context) {
				return context.getClientCompany().getCurrencies();
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
				.paymentTerms(), true, true,
				new ChangeListner<ClientPaymentTerms>() {

					@Override
					public void onSelection(ClientPaymentTerms value) {
						// TODO Auto-generated method stub

					}
				}) {

			@Override
			protected List<ClientPaymentTerms> getLists(Context context) {
				return context.getClientCompany().getPaymentsTerms();
			}
		});

		list.add(new TransactionItemAccountsRequirement(ACCOUNTS,
				"please select accountItems", getConstants().Account(), false,
				true) {

			@Override
			protected List<ClientAccount> getLists(Context context) {
				return getClientCompany().getAccounts();
			}
		});
		list.add(new TransactionItemItemsRequirement(ITEMS,
				"Please Enter Item Name or number", getConstants().items(),
				false, true, true) {

			@Override
			protected List<ClientItem> getLists(Context context) {
				return getClientCompany().getItems();
			}
		});

		list.add(new ContactRequirement(CONTACT, "Enter contact name",
				"Contact", true, true, null) {

			@Override
			protected List<ClientContact> getLists(Context context) {
				return new ArrayList<ClientContact>(
						((ClientCustomer) NewEnterBillCommand.this.get(VENDOR)
								.getValue()).getContacts());
			}

			@Override
			protected String getContactHolderName() {
				return ((ClientVendor) get(VENDOR).getValue()).getDisplayName();
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
				if (getClientCompany().getPreferences().isTrackTax()
						&& !getClientCompany().getPreferences()
								.isTaxPerDetailLine()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected List<ClientTAXCode> getLists(Context context) {
				return getClientCompany().getTaxCodes();
			}

			@Override
			protected boolean filter(ClientTAXCode e, String name) {
				return e.getName().contains(name);
			}
		});
		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientEnterBill enterBill = new ClientEnterBill();

		ClientVendor vendor = (ClientVendor) get(VENDOR).getValue();
		enterBill.setVendor(vendor);
		ClientFinanceDate date = get(DATE).getValue();
		if (date != null) {
			enterBill.setDate(date.getDate());
		} else {
			enterBill.setDate(System.currentTimeMillis());
		}

		enterBill.setType(ClientTransaction.TYPE_ENTER_BILL);

		List<ClientTransactionItem> items = get(ITEMS).getValue();

		List<ClientTransactionItem> accounts = get(ACCOUNTS).getValue();
		items.addAll(accounts);

		ClientCompanyPreferences preferences = context.getClientCompany()
				.getPreferences();
		if (preferences.isTrackTax() && !preferences.isTaxPerDetailLine()) {
			ClientTAXCode taxCode = get(TAXCODE).getValue();
			for (ClientTransactionItem item : items) {
				item.setTaxCode(taxCode.getID());
			}
		}

		enterBill.setTransactionItems(items);
		updateTotals(context, enterBill, false);

		ClientFinanceDate dueDate = get(DUE_DATE).getValue();
		enterBill.setDueDate(dueDate.getDate());

		ClientFinanceDate deliveryDate = get(DELIVERY_DATE).getValue();
		enterBill.setDeliveryDate(deliveryDate);

		ClientContact contact = get(CONTACT).getValue();
		enterBill.setContact(contact);

		ClientPaymentTerms paymentTerm = get("paymentTerms").getValue();
		enterBill.setPaymentTerm(paymentTerm);

		String phone = get(PHONE).getValue();
		enterBill.setPhone(phone);

		create(enterBill, context);
		return null;
	}
}