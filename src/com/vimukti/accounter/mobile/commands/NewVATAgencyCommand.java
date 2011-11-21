package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.CustomerContactRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ListFilter;

public class NewVATAgencyCommand extends NewAbstractCommand {

	private static final String PAYMENT_TERM = "paymentTerm";
	private static final String SALES_ACCOUNT = "salesLiabilityAccount";
	private static final String PURCHASE_ACCOUNT = "purchaseLiabilityAccount";
	private static final String PHONE = "phone";
	private static final String FAX = "fax";
	private static final String EMAIL = "email";
	private static final String WEBSITE = "webPageAddress";
	protected static final String CONTACT_NAME = "contactName";
	protected static final String TITLE = "title";
	protected static final String BUSINESS_PHONE = "businessPhone";
	protected static final String CONTACT_EMAIL = "contactEmail";
	private static final String IS_ACTIVE = "isActive";

	private static final String TAX_AGENCY_NAME = "Tax Agency Name";
	private static final String VAT_RETURN = "Vat Return";
	private static final String ADREESS = "address";
	private static final String CONTACTS = "contact";
	ClientTAXAgency taxAgency;

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new StringRequirement(TAX_AGENCY_NAME, getMessages()
				.pleaseEnter(getConstants().taxAgency()), "Tax Agency", false,
				true));

		list.add(new BooleanRequirement(IS_ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return getConstants().active();
			}

			@Override
			protected String getFalseString() {
				return getConstants().inActive();
			}
		});

		list.add(new PaymentTermRequirement(PAYMENT_TERM, getMessages()
				.pleaseSelect(getConstants().paymentTerm()), getConstants()
				.paymentTerm(), false, true, new ChangeListner<PaymentTerms>() {

			@Override
			public void onSelection(PaymentTerms value) {
				// TODO Auto-generated method stub

			}
		}) {

			@Override
			protected List<PaymentTerms> getLists(Context context) {
				return new ArrayList<PaymentTerms>(context.getCompany()
						.getPaymentTerms());
			}
		});

		list.add(new StringListRequirement(VAT_RETURN, getMessages()
				.pleaseSelect(getConstants().vatReturn()), "Vat Return", false,
				true, new ChangeListner<String>() {

					@Override
					public void onSelection(String value) {
						// TODO Auto-generated method stub

					}
				}) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getConstants().vatReturn());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getVatReturns();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new AccountRequirement(SALES_ACCOUNT, getMessages()
				.pleaseSelect(
						getConstants().sales() + getConstants().liability()
								+ getConstants().account()), getConstants()
				.sales() + getConstants().account(), false, true,
				new ChangeListner<Account>() {

					@Override
					public void onSelection(Account value) {
						// TODO Auto-generated method stub

					}
				}) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected List<Account> getLists(Context context) {
				return getSalesLiabilityAccounts(context,
						new ListFilter<Account>() {

							@Override
							public boolean filter(Account account) {
								return account.getIsActive()
										&& Arrays
												.asList(Account.TYPE_INCOME,
														Account.TYPE_EXPENSE,
														Account.TYPE_OTHER_CURRENT_LIABILITY,
														Account.TYPE_OTHER_CURRENT_ASSET,
														Account.TYPE_FIXED_ASSET)
												.contains(account.getType());
							}
						});
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new AccountRequirement(PURCHASE_ACCOUNT, getMessages()
				.pleaseSelect(
						getConstants().purchase() + getConstants().liability()
								+ getConstants().account()), getConstants()
				.purchase() + getConstants().account(), false, true,
				new ChangeListner<Account>() {

					@Override
					public void onSelection(Account value) {
						// TODO Auto-generated method stub

					}
				}) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected List<Account> getLists(Context context) {
				return getSalesLiabilityAccounts(context,
						new ListFilter<Account>() {

							@Override
							public boolean filter(Account account) {
								return account.getIsActive()
										&& Arrays
												.asList(Account.TYPE_INCOME,
														Account.TYPE_EXPENSE,
														Account.TYPE_OTHER_CURRENT_LIABILITY,
														Account.TYPE_OTHER_CURRENT_ASSET,
														Account.TYPE_FIXED_ASSET)
												.contains(account.getType());
							}
						});
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new AddressRequirement(ADREESS, getMessages().pleaseEnter(
				getConstants().address()), getConstants().address(), true, true));

		list.add(new StringRequirement(PHONE, getMessages().pleaseEnter(
				getConstants().phoneNumber()), getConstants().phoneNumber(),
				true, true));

		list.add(new StringRequirement(FAX, getMessages().pleaseEnter(
				getConstants().faxNumber()), getConstants().faxNumber(), true,
				true));

		list.add(new StringRequirement(EMAIL, getMessages().pleaseEnter(
				getConstants().email()), getConstants().email(), true, true));

		list.add(new StringRequirement(WEBSITE, getMessages().pleaseEnter(
				getConstants().webSite()), getConstants().webSite(), true, true));

		list.add(new CustomerContactRequirement(CONTACTS, getMessages()
				.pleaseSelect(getConstants().contact()), CONTACTS, true, true) {

			@Override
			protected List<ClientContact> getList() {
				List<ClientContact> contacts = getAgencyContacts();
				return new ArrayList<ClientContact>(contacts);
			}
		});

	}

	protected List<ClientContact> getAgencyContacts() {
		if (taxAgency.getID() != 0) {
			new ArrayList<ClientContact>(taxAgency.getContacts());
		}
		return null;
	}

	protected List<Account> getSalesLiabilityAccounts(Context context,
			ListFilter<Account> filter) {
		List<Account> filteredList = new ArrayList<Account>();
		for (Account obj : context.getCompany().getAccounts()) {
			if (filter.filter(obj)) {
				filteredList.add(obj);
			}
		}
		return filteredList;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		String string = context.getString();
		if (isUpdate) {
			if (string.isEmpty()) {
				addFirstMessage(context, "Select a Tax Agency to update.");
				return "vendors";
			}
			ClientPayee vendorByName = CommandUtils.getPayeeByName(
					context.getCompany(), string);
			if (vendorByName == null) {
				addFirstMessage(context, "Select a Tax Agency to update.");
				return "vendors " + string;
			}
			taxAgency = (ClientTAXAgency) vendorByName;
			setValues();
		} else {
			taxAgency = new ClientTAXAgency();
			if (string.isEmpty()) {
				get(TAX_AGENCY_NAME).setValue(string);
			}
		}
		return null;
	}

	private void setValues() {
		get(TAX_AGENCY_NAME).setValue(taxAgency.getName());
		get(PAYMENT_TERM).setValue(
				CommandUtils.getServerObjectById(taxAgency.getPaymentTerm(),
						AccounterCoreType.PAYMENT_TERM));
		get(SALES_ACCOUNT).setValue(
				CommandUtils.getServerObjectById(
						taxAgency.getSalesLiabilityAccount(),
						AccounterCoreType.ACCOUNT));
		get(PHONE).setValue(taxAgency.getPhoneNo());
		get(FAX).setValue(taxAgency.getFaxNo());
		get(EMAIL).setValue(taxAgency.getEmail());
		get(WEBSITE).setValue(taxAgency.getWebPageAddress());
		get(ADREESS).setValue(
				new ArrayList<ClientAddress>(taxAgency.getAddress()));
		get(CONTACTS).setValue(
				new ArrayList<ClientContact>(taxAgency.getContacts()));
		get(PURCHASE_ACCOUNT).setValue(
				CommandUtils.getServerObjectById(
						taxAgency.getPurchaseLiabilityAccount(),
						AccounterCoreType.ACCOUNT));
		int vatReturn = taxAgency.getVATReturn();
		if (vatReturn == ClientTAXAgency.RETURN_TYPE_NONE) {
			get(VAT_RETURN).setValue("");
		} else if (vatReturn == ClientTAXAgency.RETURN_TYPE_UK_VAT) {
			get(VAT_RETURN).setValue("UK VAT");
		} else if (vatReturn == ClientTAXAgency.RETURN_TYPE_IRELAND_VAT) {
			get(VAT_RETURN).setValue(null);
		}
	}

	@Override
	protected String getWelcomeMessage() {
		return taxAgency.getID() == 0 ? "NewVatAgency commond is activated"
				: "Update VAT Agency command is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return taxAgency.getID() == 0 ? "NewVatAgencyCommond is ready to create with the following values"
				: "VAT Agency is ready to update with following details";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(IS_ACTIVE).setDefaultValue(Boolean.TRUE);
	}

	@Override
	public String getSuccessMessage() {
		return taxAgency.getID() == 0 ? "New vat commond is created successfully"
				: "VAT Agency is updated successfully";
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		String name = get(TAX_AGENCY_NAME).getValue();
		PaymentTerms paymentTerm = get(PAYMENT_TERM).getValue();
		Account salesAccount = get(SALES_ACCOUNT).getValue();

		// ClientAddress address = (ClientAddress) get(VAT_AGENCY_ADDRESS)
		// .getValue();

		String phone = (String) get(PHONE).getValue();
		String fax = (String) get(FAX).getValue();
		String email = (String) get(EMAIL).getValue();
		String website = (String) get(WEBSITE).getValue();

		// Set<ClientContact> contacts = get(VAT_AGENCY_CONTACT).getValue();
		//
		// HashSet<ClientAddress> addresses = new HashSet<ClientAddress>();
		// if (address != null) {
		// addresses.add(address);
		// }

		taxAgency.setName(name);
		taxAgency.setPaymentTerm(paymentTerm.getID());
		taxAgency.setSalesLiabilityAccount(salesAccount.getID());
		// taxAgency.setAddress(addresses);
		taxAgency.setPhoneNo(phone);
		taxAgency.setFaxNo(fax);
		taxAgency.setEmail(email);
		taxAgency.setWebPageAddress(website);
		ClientAddress address = get(ADREESS).getValue();
		Set<ClientAddress> listAddress = new HashSet<ClientAddress>();
		if (address != null) {
			listAddress.add(address);
		}
		taxAgency.setAddress(listAddress);
		List<ClientContact> contactList = get(CONTACTS).getValue();
		taxAgency.setContacts(new HashSet<ClientContact>(contactList));
		if (context.getPreferences().isTrackPaidTax()) {
			Account purchaseAccount = get(PURCHASE_ACCOUNT).getValue();
			String vatReturn = get(VAT_RETURN).getValue();
			taxAgency.setPurchaseLiabilityAccount(purchaseAccount.getID());
			if (vatReturn == "") {
				taxAgency.setVATReturn(ClientTAXAgency.RETURN_TYPE_NONE);
			} else if (vatReturn == "UK VAT") {
				taxAgency.setVATReturn(ClientTAXAgency.RETURN_TYPE_UK_VAT);
			} else {
				taxAgency.setVATReturn(ClientTAXAgency.RETURN_TYPE_IRELAND_VAT);
			}
		}

		create(taxAgency, context);

		markDone();
		return null;
	}

	private List<String> getVatReturns() {

		ArrayList<String> vatReturnList = new ArrayList<String>();
		vatReturnList.add("UK VAT");
		vatReturnList.add("VAT 3(Ireland)");

		return vatReturnList;
	}
}
