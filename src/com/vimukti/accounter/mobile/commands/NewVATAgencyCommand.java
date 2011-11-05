package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;

public class NewVATAgencyCommand extends NewAbstractCommand {

	private static final String PAYMENT_TERM = "paymentTerm";
	private static final String SALES_ACCOUNT = "salesLiabilityAccount";
	private static final String PURCHASE_ACCOUNT = "purchaseLiabilityAccount";
	private static final String PHONE = "phone";
	private static final String FAX = "fax";
	private static final String EMAIL = "email";
	private static final String WEBSITE = "webPageAddress";
	private static final String VAT_AGENCY_CONTACT = "vatAgencyContact";
	protected static final String CONTACT_NAME = "contactName";
	protected static final String TITLE = "title";
	protected static final String BUSINESS_PHONE = "businessPhone";
	protected static final String CONTACT_EMAIL = "contactEmail";
	private static final String IS_ACTIVE = "isActive";

	private static final String VAT_AGENCY_ADDRESS = "vatAgencyAddress";
	private static final String TAX_AGENCY_NAME = "Tax Agency Name";
	private static final String VAT_RETURN = "Vat Return";
	private static final String ADREESS = "address";
	private static final String CONTACTS = "contact";
	protected boolean isUpdate;

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
				.paymentTerm(), false, true,
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
				new ChangeListner<ClientAccount>() {

					@Override
					public void onSelection(ClientAccount value) {
						// TODO Auto-generated method stub

					}
				}) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected List<ClientAccount> getLists(Context context) {
				return getSalesLiabilityAccounts(context,
						new ListFilter<ClientAccount>() {

							@Override
							public boolean filter(ClientAccount account) {
								return account.getIsActive()
										&& Arrays
												.asList(ClientAccount.TYPE_INCOME,
														ClientAccount.TYPE_EXPENSE,
														ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
														ClientAccount.TYPE_OTHER_CURRENT_ASSET,
														ClientAccount.TYPE_FIXED_ASSET)
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
				new ChangeListner<ClientAccount>() {

					@Override
					public void onSelection(ClientAccount value) {
						// TODO Auto-generated method stub

					}
				}) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected List<ClientAccount> getLists(Context context) {
				return getSalesLiabilityAccounts(context,
						new ListFilter<ClientAccount>() {

							@Override
							public boolean filter(ClientAccount account) {
								return account.getIsActive()
										&& Arrays
												.asList(ClientAccount.TYPE_INCOME,
														ClientAccount.TYPE_EXPENSE,
														ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
														ClientAccount.TYPE_OTHER_CURRENT_ASSET,
														ClientAccount.TYPE_FIXED_ASSET)
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

			@Override
			protected String getEmptyString() {
				return isUpdate ? getMessages().youDontHaveAny(
						getConstants().contacts()) : "";
			}
		});

	}

	protected List<ClientContact> getAgencyContacts() {
		// TODO Auto-generated method stub
		return null;
	}

	protected List<ClientContact> getContactList(Context context) {
		return null;
	}

	protected List<ClientAccount> getSalesLiabilityAccounts(Context context,
			ListFilter<ClientAccount> filter) {
		return Utility.filteredList(filter, context.getClientCompany()
				.getAccounts());
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		this.isUpdate = isUpdate;
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "NewVatAgency commond is activated";
	}

	@Override
	protected String getDetailsMessage() {
		return "NewVatAgencyCommond is ready to create with the following values";
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(IS_ACTIVE).setDefaultValue(Boolean.TRUE);
	}

	@Override
	public String getSuccessMessage() {
		return "New vat commond is created successfully";
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {

		ClientTAXAgency taxAgency = new ClientTAXAgency();
		String name = get(TAX_AGENCY_NAME).getValue();
		ClientPaymentTerms paymentTerm = get(PAYMENT_TERM).getValue();
		ClientAccount salesAccount = get(SALES_ACCOUNT).getValue();

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
		Set<ClientContact> contactsList = new HashSet<ClientContact>();
		if (!contactList.isEmpty()) {
			for (ClientContact contact : contactList) {
				contactsList.add(contact);
			}
			taxAgency.setContacts(contactsList);
		}

		if (context.getClientCompany().getPreferences().isTrackPaidTax()) {
			ClientAccount purchaseAccount = get(PURCHASE_ACCOUNT).getValue();
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

	//
	// private static final String PAYMENT_TERM = "paymentTerm";
	// private static final String SALES_ACCOUNT = "salesLiabilityAccount";
	// private static final String PURCHASE_ACCOUNT =
	// "purchaseLiabilityAccount";
	// private static final String PHONE = "phone";
	// private static final String FAX = "fax";
	// private static final String EMAIL = "email";
	// private static final String WEBSITE = "webPageAddress";
	// private static final String VAT_AGENCY_CONTACT = "vatAgencyContact";
	// protected static final String CONTACT_NAME = "contactName";
	// protected static final String TITLE = "title";
	// protected static final String BUSINESS_PHONE = "businessPhone";
	// protected static final String CONTACT_EMAIL = "contactEmail";
	// private static final String IS_ACTIVE = "isActive";
	//
	// private static final String VAT_AGENCY_ADDRESS = "vatAgencyAddress";
	//
	// @Override
	// public String getId() {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// protected void addRequirements(List<Requirement> list) {
	// list.add(new Requirement(NAME, false, true));
	// list.add(new Requirement(PAYMENT_TERM, false, true));
	// list.add(new Requirement(SALES_ACCOUNT, false, true));
	// // if (getCompanyType(c) != 0) {
	// list.add(new Requirement(VAT_RETURN, false, true));
	// list.add(new Requirement(PURCHASE_ACCOUNT, false, true));
	// // }
	// list.add(new Requirement(VAT_AGENCY_ADDRESS, true, true));
	// list.add(new Requirement(PHONE, true, true));
	// list.add(new Requirement(FAX, true, true));
	// list.add(new Requirement(EMAIL, true, true));
	// list.add(new Requirement(WEBSITE, true, true));
	// list.add(new ObjectListRequirement(VAT_AGENCY_CONTACT, true, true) {
	//
	// @Override
	// public void addRequirements(List<Requirement> list) {
	// list.add(new Requirement(CONTACT_NAME, true, true));
	// list.add(new Requirement(TITLE, true, true));
	// list.add(new Requirement(BUSINESS_PHONE, true, true));
	// list.add(new Requirement(CONTACT_EMAIL, true, true));
	// }
	// });
	// list.add(new Requirement(IS_ACTIVE, true, true));
	// }
	//
	// @Override
	// public Result run(Context context) {
	// Object attribute = context.getAttribute(INPUT_ATTR);
	// if (attribute == null) {
	// context.setAttribute(INPUT_ATTR, "optional");
	// }
	// Result result = context.makeResult();
	//
	// String process = (String) context.getAttribute(PROCESS_ATTR);
	// if (process != null) {
	// if (process.equals(CONTACT_PROCESS)) {
	// result = contactProcess(context);
	// if (result != null) {
	// return result;
	// }
	// }
	// if (process.equals(ADDRESS_PROCESS)) {
	// result = addressProcess(context);
	// if (result != null) {
	// return result;
	// }
	// }
	// }
	// Result makeResult = context.makeResult();
	// makeResult.add(getMessages().readyToCreate(getConstants().vatAgency()));
	// ResultList list = new ResultList("values");
	// makeResult.add(list);
	// ResultList actions = new ResultList(ACTIONS);
	// makeResult.add(actions);
	//
	// setOptionalValues();
	//
	// result = nameRequirement(context, list, NAME, getConstants()
	// .vatAgencyName(),
	// getMessages().pleaseEnter(getConstants().vatAgencyName()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = paymentTermsRequirement(context, list, PAYMENT_TERM,
	// getMessages().pleaseSelect(getConstants().paymentTerm()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = accountRequirement(context, list, SALES_ACCOUNT, getMessages()
	// .salesLiabilityAccount(Global.get().Account()),
	// new ListFilter<ClientAccount>() {
	//
	// @Override
	// public boolean filter(ClientAccount account) {
	// return account.getIsActive()
	// && Arrays
	// .asList(ClientAccount.TYPE_INCOME,
	// ClientAccount.TYPE_EXPENSE,
	// ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
	// ClientAccount.TYPE_OTHER_CURRENT_ASSET,
	// ClientAccount.TYPE_FIXED_ASSET)
	// .contains(account.getType());
	// }
	// });
	// if (result != null) {
	// return result;
	// }
	//
	// if (getClientCompany().getPreferences().isTrackPaidTax()) {
	// result = accountRequirement(
	// context,
	// list,
	// PURCHASE_ACCOUNT,
	// getMessages().purchaseLiabilityAccount(
	// Global.get().Account()),
	// new ListFilter<ClientAccount>() {
	//
	// @Override
	// public boolean filter(ClientAccount account) {
	// return account.getIsActive()
	// && Arrays
	// .asList(ClientAccount.TYPE_INCOME,
	// ClientAccount.TYPE_EXPENSE,
	// ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
	// ClientAccount.TYPE_OTHER_CURRENT_ASSET,
	// ClientAccount.TYPE_FIXED_ASSET)
	// .contains(account.getType());
	// }
	// });
	// if (result != null) {
	// return result;
	// }
	// result = vatReturnRequirement(context, list, VAT_RETURN);
	// if (result != null) {
	// return result;
	// }
	// }
	//
	// result = createOptionalResult(context, list, actions, makeResult);
	// if (result != null) {
	// return result;
	// }
	//
	// return createVatAgency(context);
	// }
	//
	// private void setOptionalValues() {
	// Requirement isActiveReq = get(IS_ACTIVE);
	// if (isActiveReq.getDefaultValue() == null) {
	// isActiveReq.setDefaultValue(true);
	// }
	// Requirement contactsReq = get(VAT_AGENCY_CONTACT);
	// if (contactsReq.getDefaultValue() == null) {
	// contactsReq.setDefaultValue(new HashSet<ClientContact>());
	// }
	// Requirement addressReq = get(VAT_AGENCY_ADDRESS);
	// if (addressReq.getDefaultValue() == null) {
	// addressReq.setDefaultValue(new ClientAddress());
	// }
	// Requirement phoneReq = get(PHONE);
	// if (phoneReq.getDefaultValue() == null) {
	// phoneReq.setDefaultValue(new String());
	// }
	// Requirement faxReq = get(FAX);
	// if (faxReq.getDefaultValue() == null) {
	// faxReq.setDefaultValue(new String());
	// }
	// Requirement emailReq = get(EMAIL);
	// if (emailReq.getDefaultValue() == null) {
	// emailReq.setDefaultValue(new String());
	// }
	// Requirement websiteReq = get(WEBSITE);
	// if (websiteReq.getDefaultValue() == null) {
	// websiteReq.setDefaultValue(new String());
	// }
	// }
	//
	// private Result createVatAgency(Context context) {
	// ClientTAXAgency taxAgency = new ClientTAXAgency();
	// String name = get(NAME).getValue();
	// ClientPaymentTerms paymentTerm = get(PAYMENT_TERM).getValue();
	// ClientAccount salesAccount = get(SALES_ACCOUNT).getValue();
	// ClientAddress address = (ClientAddress) get(VAT_AGENCY_ADDRESS)
	// .getValue();
	// String phone = (String) get(PHONE).getValue();
	// String fax = (String) get(FAX).getValue();
	// String email = (String) get(EMAIL).getValue();
	// String website = (String) get(WEBSITE).getValue();
	// Set<ClientContact> contacts = get(VAT_AGENCY_CONTACT).getValue();
	//
	// HashSet<ClientAddress> addresses = new HashSet<ClientAddress>();
	// if (address != null) {
	// addresses.add(address);
	// }
	//
	// taxAgency.setName(name);
	// taxAgency.setPaymentTerm(paymentTerm.getID());
	// taxAgency.setSalesLiabilityAccount(salesAccount.getID());
	// taxAgency.setAddress(addresses);
	// taxAgency.setPhoneNo(phone);
	// taxAgency.setFaxNo(fax);
	// taxAgency.setEmail(email);
	// taxAgency.setWebPageAddress(website);
	// taxAgency.setContacts(contacts);
	// if (getClientCompany().getPreferences().isTrackPaidTax()) {
	// ClientAccount purchaseAccount = get(PURCHASE_ACCOUNT).getValue();
	// String vatReturn = get(VAT_RETURN).getValue();
	// taxAgency.setPurchaseLiabilityAccount(purchaseAccount.getID());
	// if (vatReturn == "") {
	// taxAgency.setVATReturn(ClientTAXAgency.RETURN_TYPE_NONE);
	// } else if (vatReturn == "UK VAT") {
	// taxAgency.setVATReturn(ClientTAXAgency.RETURN_TYPE_UK_VAT);
	// } else {
	// taxAgency.setVATReturn(ClientTAXAgency.RETURN_TYPE_IRELAND_VAT);
	// }
	// }
	//
	// create(taxAgency, context);
	//
	// markDone();
	// Result result = new Result();
	// result.add(getMessages().createSuccessfully(getConstants().taxAgency()));
	//
	// return result;
	// }
	//
	// private Result createOptionalResult(Context context, ResultList list,
	// ResultList actions, Result makeResult) {
	// // context.setAttribute(INPUT_ATTR, "optional");
	//
	// Object selection = context.getSelection(ACTIONS);
	// if (selection != null) {
	// ActionNames actionName = (ActionNames) selection;
	// switch (actionName) {
	// case FINISH:
	// context.removeAttribute(INPUT_ATTR);
	// return null;
	// default:
	// break;
	// }
	// }
	//
	// selection = context.getSelection("values");
	//
	// Result result = numberOptionalRequirement(context, list, selection,
	// PHONE, getConstants().phoneNumber(),
	// getMessages().pleaseEnter(getConstants().phoneNumber()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = numberOptionalRequirement(context, list, selection, FAX,
	// getConstants().fax(),
	// getMessages().pleaseEnter(getConstants().fax()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = stringOptionalRequirement(context, list, selection, EMAIL,
	// getConstants().email(),
	// getMessages().pleaseEnter(getConstants().email()));
	// if (result != null) {
	// return result;
	// }
	//
	// result = stringOptionalRequirement(context, list, selection, WEBSITE,
	// getConstants().webSite(),
	// getMessages().pleaseEnter(getConstants().webPageAddress()));
	// if (result != null) {
	// return result;
	// }
	// booleanOptionalRequirement(context, selection, list, IS_ACTIVE,
	// getMessages().active(getConstants().vatAgency()), getMessages()
	// .inActive(getConstants().vatAgency()));
	//
	// Record finish = new Record(ActionNames.FINISH);
	// finish.add("", getMessages().finishToCreate(getConstants().vatAgency()));
	// actions.add(finish);
	// return makeResult;
	// }
}
