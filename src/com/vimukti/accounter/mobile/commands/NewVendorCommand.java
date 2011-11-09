package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.ShippingMethod;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.core.VendorGroup;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerContactRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PaymentTermRequirement;
import com.vimukti.accounter.mobile.requirements.ShippingMethodRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.TaxCodeRequirement;
import com.vimukti.accounter.mobile.requirements.VendorGroupRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;
import com.vimukti.accounter.web.client.util.CountryPreferenceFactory;
import com.vimukti.accounter.web.client.util.ICountryPreferences;

public class NewVendorCommand extends NewAbstractCommand {

	protected static final String ACCOUNT = "Account";
	protected static final String CREDIT_LIMIT = "Credit Limit";
	private static final String CST_NUM = "CST number";
	private static final String SERVICE_TAX_NUM = "Service tax registration no";
	private static final String TIN_NUM = "Taxpayer identification number";
	private static final String PAYMENT_METHOD = "paymentMethod";
	private static final String PAYMENT_TERMS = "paymentTerms";
	private static final String ACCOUNT_NO = "Account No";
	private static final String BANK_NAME = "Bank Name";
	private static final String BANK_BRANCH = "Bank Branch";
	private static final String VENDOR_GROUP = "Vendor Group";
	private static final String VAT_REGISTRATION_NUMBER = "Vat Registration Number";
	private static final String VENDOR_VAT_CODE = "Vendor Vat Code";
	private static final String VENDOR_NAME = "Vendor Name";
	private static final String VENDOR_NUMBER = "Vendor Number";
	private static final String VENDOR_SINCE = "Vendor Since";
	private static final String ACTIVE = "Active";
	private static final String TRACK_PAYMENTS_FOR_1099 = "Track payments for 1099";
	private static final String BALANCE = "Balance";
	private static final String BALANCE_AS_OF = "Balance As Of";
	private static final String PHONE = "Phone";
	private static final String FAX = "Fax";
	private static final String EMAIL = "E-mail";
	private static final String WEB_PAGE_ADDRESS = "Web Page Address";
	private static final String CONTACTS = "contact";

	protected static final String PRIMARY = "Primary";

	private static final String BILL_TO = "billTo";

	private static final String SHIPPING_METHODS = "shippingMethod";
	private static final String SHIP_TO = "shipTo";
	private boolean isUpdate;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new NameRequirement(VENDOR_NAME, getMessages().pleaseEnter(
				getMessages().payeeName(Global.get().Vendor())), getMessages()
				.payeeName(Global.get().Vendor()), false, true));

		list.add(new NumberRequirement(VENDOR_NUMBER, getMessages()
				.pleaseEnter(getMessages().payeeNumber(Global.get().Vendor())),
				getMessages().payeeNumber(Global.get().Vendor()), false, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getClientCompany().getPreferences()
						.getUseCustomerId()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new BooleanRequirement(ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return getConstants().active();
			}

			@Override
			protected String getFalseString() {
				return getConstants().inActive();
			}
		});

		list.add(new DateRequirement(VENDOR_SINCE, getMessages().pleaseEnter(
				getMessages().payeeSince(Global.get().Vendor())), getMessages()
				.payeeSince(Global.get().Vendor()), true, true));

		list.add(new AmountRequirement(BALANCE, getMessages().pleaseEnter(
				getMessages().payeeBalance(Global.get().Vendor())),
				getMessages().payeeBalance(Global.get().Vendor()), true, true));

		list.add(new DateRequirement(BALANCE_AS_OF, getMessages().pleaseEnter(
				getConstants().balanceAsOfDate()), getConstants()
				.balanceAsOfDate(), true, true));

		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getConstants().billTo()), getConstants().billTo(), true, true));

		list.add(new AddressRequirement(SHIP_TO, getMessages().pleaseEnter(
				getConstants().shipTo()), getConstants().shipTo(), true, true));

		list.add(new NumberRequirement(PHONE, getMessages().pleaseEnter(
				getConstants().phoneNumber()), getConstants().phoneNumber(),
				true, true));

		list.add(new NumberRequirement(FAX, getMessages().pleaseEnter(
				getConstants().fax()), getConstants().fax(), true, true));

		list.add(new NameRequirement(EMAIL, getMessages().pleaseEnter(
				getConstants().email()), getConstants().email(), true, true));

		list.add(new NameRequirement(WEB_PAGE_ADDRESS, getMessages()
				.pleaseEnter(getConstants().webPageAddress()), getConstants()
				.webPageAddress(), true, true));

		list.add(new BooleanRequirement(TRACK_PAYMENTS_FOR_1099, true) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (context.getClientCompany().getCountry()
						.equals(CountryPreferenceFactory.UNITED_STATES)) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}

			@Override
			protected String getTrueString() {
				return getConstants().trackPaymentsFor1099();
			}

			@Override
			protected String getFalseString() {
				return getConstants().dontTrackPaymentsFor1099();
			}
		});

		list.add(new CustomerContactRequirement(CONTACTS, getMessages()
				.pleaseSelect(getConstants().contact()), CONTACTS, true, true) {

			@Override
			protected List<ClientContact> getList() {
				List<ClientContact> contacts = getVendorContacts();
				return new ArrayList<ClientContact>(contacts);
			}

			@Override
			protected String getEmptyString() {
				return isUpdate ? getMessages().youDontHaveAny(
						getConstants().contacts()) : "";
			}
		});

		list.add(new AccountRequirement(ACCOUNT, getMessages().pleaseSelect(
				Global.get().Account()), Global.get().Account(), true, true,
				null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(Global.get().Account());
			}

			@Override
			protected List<Account> getLists(Context context) {
				return new ArrayList<Account>(context.getCompany()
						.getAccounts());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Accounts());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().startsWith(name)
						|| e.getNumber().equals(name);
			}
		});

		list.add(new AmountRequirement(CREDIT_LIMIT, getMessages().pleaseEnter(
				getConstants().creditLimit()), getConstants().creditLimit(),
				true, true));

		list.add(new ShippingMethodRequirement(SHIPPING_METHODS, getMessages()
				.pleaseEnter(getConstants().shippingMethod()), getConstants()
				.shippingMethod(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getConstants().shippingMethod());
			}

			@Override
			protected List<ShippingMethod> getLists(Context context) {
				return new ArrayList<ShippingMethod>(context.getCompany()
						.getShippingMethods());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getConstants().shippingMethod());
			}

			@Override
			protected boolean filter(ShippingMethod e, String name) {
				return e.getName().equals(name);
			}
		});

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseEnterName(getConstants().paymentMethod()),
				getConstants().paymentMethod(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getConstants().paymentMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getConstants().paymentMethod());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getPaymentMethods();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getConstants().paymentMethod());
			}
		});

		list.add(new PaymentTermRequirement(PAYMENT_TERMS, getMessages()
				.pleaseSelect(getConstants().paymentTerm()), getConstants()
				.paymentTerm(), true, true, null) {

			@Override
			protected List<PaymentTerms> getLists(Context context) {
				return new ArrayList<PaymentTerms>(context.getCompany()
						.getPaymentTerms());
			}
		});

		list.add(new NumberRequirement(ACCOUNT_NO, getMessages().pleaseEnter(
				getMessages().payeeNumber(Global.get().Account())),
				getMessages().payeeNumber(Global.get().Account()), true, true));

		list.add(new NameRequirement(BANK_NAME, getMessages().pleaseEnter(
				getConstants().bankName()), getConstants().bankName(), true,
				true));

		list.add(new NameRequirement(BANK_BRANCH, getMessages().pleaseEnter(
				getConstants().bankBranch()), getConstants().bankBranch(),
				true, true));

		list.add(new VendorGroupRequirement(VENDOR_GROUP, getMessages()
				.pleaseEnterName(
						getMessages().payeeGroup(Global.get().Vendor())),
				getMessages().payeeGroup(Global.get().Vendor()), true, true,
				null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().payeeGroup(Global.get().Vendor()));
			}

			@Override
			protected List<VendorGroup> getLists(Context context) {
				return new ArrayList<VendorGroup>(context.getCompany()
						.getVendorGroups());
			}

			@Override
			protected boolean filter(VendorGroup e, String name) {
				return e.getName().startsWith(name);
			}
		});

		list.add(new NumberRequirement(VAT_REGISTRATION_NUMBER, getMessages()
				.pleaseEnter(getConstants().vatRegistrationNumber()),
				getConstants().vatRegistrationNumber(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getClientCompany().getPreferences().isTrackTax()
						&& getClientCompany().getCountryPreferences()
								.isVatAvailable()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new TaxCodeRequirement(VENDOR_VAT_CODE,
				"Please enter the tax code name", getConstants().taxCode(),
				true, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getClientCompany().getPreferences().isTrackTax()) {
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
				return e.getName().startsWith(name);
			}
		});

		list.add(new NumberRequirement(CST_NUM, getMessages().pleaseEnter(
				getMessages().payeeNumber(Global.get().Customer())),
				getMessages().payeeNumber(Global.get().Customer()), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getClientCompany().getPreferences().isTrackTax()
						&& getClientCompany().getCountryPreferences()
								.isSalesTaxAvailable()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new NumberRequirement(SERVICE_TAX_NUM, getMessages()
				.pleaseEnter(getConstants().serviceTax()), getConstants()
				.serviceTax(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getClientCompany().getPreferences().isTrackTax()
						&& getClientCompany().getCountryPreferences()
								.isServiceTaxAvailable()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

		list.add(new NumberRequirement(TIN_NUM, getMessages().pleaseEnter(
				getConstants().tinNumber()), getConstants().tinNumber(), true,
				true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if (getClientCompany().getPreferences().isTrackTax()
						&& getClientCompany().getCountryPreferences()
								.isTDSAvailable()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;
			}
		});

	}

	protected List<ClientContact> getVendorContacts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {

		CompanyPreferences preferences = context.getCompany().getPreferences();
		ICountryPreferences countryPreferences = context.getClientCompany()
				.getCountryPreferences();

		ClientVendor vendor = new ClientVendor();
		String name = get(VENDOR_NAME).getValue();
		String number = null;
		if (preferences.getUseVendorId()) {
			number = get(VENDOR_NUMBER).getValue().toString();
		}
		List<ClientContact> contact = get(CONTACTS).getValue();
		boolean isActive = (Boolean) get(ACTIVE).getValue();
		ClientFinanceDate balancedate = get(BALANCE_AS_OF).getValue();
		double balance = get(BALANCE).getValue();
		ClientAddress billTo = get(BILL_TO).getValue();
		ClientAddress shipTo = get(SHIP_TO).getValue();
		ClientAccount account = get(ACCOUNT).getValue();
		String phoneNum = get(PHONE).getValue();
		String faxNum = get(FAX).getValue();
		String emailId = get(EMAIL).getValue();
		String webaddress = get(WEB_PAGE_ADDRESS).getValue();
		double creditLimit = Double
				.parseDouble((get(CREDIT_LIMIT).getValue() == null ? 0.0 : get(
						CREDIT_LIMIT).getValue().toString()).toString());
		String bankName = get(BANK_NAME).getValue();
		String bankAccountNum = get(ACCOUNT_NO).getValue();
		String bankBranch = get(BANK_BRANCH).getValue();
		ClientShippingMethod shippingMethod = get(SHIPPING_METHODS).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		ClientPaymentTerms paymentTerms = get(PAYMENT_TERMS).getValue();

		String vatRegistredNum = get(VAT_REGISTRATION_NUMBER).getValue();

		String cstNum = get(CST_NUM).getValue();
		String serviceTaxNum = get(SERVICE_TAX_NUM).getValue();
		String tinNum = get(TIN_NUM).getValue();

		HashSet<ClientAddress> addresses = new HashSet<ClientAddress>();
		if (billTo != null && billTo.getAddress1() != "") {
			billTo.setType(ClientAddress.TYPE_BILL_TO);
			addresses.add(billTo);
		}
		if (shipTo != null && shipTo.getAddress1() != "") {
			shipTo.setType(ClientAddress.TYPE_SHIP_TO);
			addresses.add(shipTo);

		}
		HashSet<ClientContact> contacts = new HashSet<ClientContact>();
		if (contact != null) {
			contacts.addAll(contact);
		}
		vendor.setName(name);
		if (preferences.getUseVendorId())
			vendor.setVendorNumber(number);

		if (balancedate != null) {
			vendor.setBalanceAsOf(balancedate.getDate());
		}
		// vendor.set
		vendor.setContacts(contacts);
		vendor.setBalance(balance);
		if (!addresses.isEmpty())
			vendor.setAddress(addresses);
		vendor.setPhoneNo(phoneNum);
		vendor.setFaxNo(faxNum);
		vendor.setWebPageAddress(webaddress);
		vendor.setBankAccountNo(bankAccountNum);
		vendor.setBankBranch(bankBranch);
		vendor.setBankName(bankName);
		vendor.setEmail(emailId);

		if (context.getClientCompany().getCountry()
				.equals(CountryPreferenceFactory.UNITED_STATES)) {
			boolean isTrackPaymentsFor1099 = get(TRACK_PAYMENTS_FOR_1099)
					.getValue();
			vendor.setTrackPaymentsFor1099(isTrackPaymentsFor1099);
		}
		if (account != null)
			vendor.setExpenseAccount(account);
		vendor.setCreditLimit(creditLimit);
		if (preferences.isDoProductShipMents() && shippingMethod != null)
			vendor.setShippingMethod(shippingMethod.getID());
		vendor.setPaymentMethod(paymentMethod);
		if (paymentTerms != null)
			vendor.setPaymentTerms(paymentTerms.getID());

		ClientVendorGroup value = get(VENDOR_GROUP).getValue();
		if (value != null)
			vendor.setVendorGroup(value.getID());
		vendor.setActive(isActive);
		ClientTAXCode code = get(VENDOR_VAT_CODE).getValue();
		if (code != null)
			vendor.setTAXCode(code.getID());
		vendor.setVATRegistrationNumber(vatRegistredNum);

		if (preferences.isTrackTax() && code != null) {
			if (countryPreferences.isVatAvailable()) {
				vendor.setTAXCode(code.getID());
				vendor.setVATRegistrationNumber(vatRegistredNum);
			}
			if (countryPreferences.isSalesTaxAvailable()) {
				vendor.setCstNumber(cstNum);
			}
			if (countryPreferences.isServiceTaxAvailable()) {
				vendor.setServiceTaxRegistrationNumber(serviceTaxNum);
			}
			if (countryPreferences.isTDSAvailable()) {
				vendor.setTinNumber(tinNum);
			}
			// customer.setPANno(panNum);
		}
		create(vendor, context);

		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		this.isUpdate = isUpdate;
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(Global.get().Vendor());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(Global.get().Vendor());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(ACTIVE).setDefaultValue(true);
		get(VENDOR_SINCE).setDefaultValue(new ClientFinanceDate());
		get(BALANCE).setDefaultValue(Double.valueOf(0.0D));
		get(BALANCE_AS_OF).setDefaultValue(new ClientFinanceDate());
		get(BILL_TO).setDefaultValue(new ClientAddress());
		get(PAYMENT_METHOD).setDefaultValue(getConstants().cash());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(Global.get().Vendor());
	}

}
