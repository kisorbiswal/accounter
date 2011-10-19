package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.VendorGroup;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.ObjectListRequirement;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;
import com.vimukti.accounter.web.client.util.ICountryPreferences;

public class NewVendorCommand extends AbstractTransactionCommand {

	private static final String INPUT_ATTR = "input";

	private static final int VENDORGROUP_TO_SHOW = 5;

	protected static final String ACCOUNT = "Account";
	protected static final String CREDIT_LIMIT = "Credit Limit";
	private static final String CST_NUM = "CST number";
	private static final String SERVICE_TAX_NUM = "Service tax registration no";
	private static final String TIN_NUM = "Taxpayer identification number";
	private static final String PREFERRED_SHIPPING_METHOD = "Preferred Shipping Method";
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
	private static final String ADDRESS = "address";
	private static final String PHONE = "Phone";
	private static final String FAX = "Fax";
	private static final String EMAIL = "E-mail";
	private static final String WEB_PAGE_ADDRESS = "Web Page Address";
	private static final String CONTACTS = "Contacts";
	private static final String CONTACT_NAME = "Contact Name";
	private static final String TITLE = "Title";
	private static final String BUSINESS_PHONE = "Business Phone";

	private int accountingType;

	protected static final String PRIMARY = "Primary";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement(VENDOR_NAME, false, true));

		list.add(new Requirement(VENDOR_NUMBER, false, true));

		list.add(new Requirement(ACTIVE, true, true));
		list.add(new Requirement(VENDOR_SINCE, true, true));
		list.add(new Requirement(BALANCE, true, true));
		list.add(new Requirement(BALANCE_AS_OF, true, true));
		list.add(new Requirement(BILL_TO, true, true));
		list.add(new Requirement(PHONE, true, true));
		list.add(new Requirement(FAX, true, true));
		list.add(new Requirement(EMAIL, true, true));
		list.add(new Requirement(WEB_PAGE_ADDRESS, true, true));

		list.add(new Requirement(TRACK_PAYMENTS_FOR_1099, true, true));

		list.add(new ObjectListRequirement(CONTACTS, true, true) {
			@Override
			public void addRequirements(List<Requirement> list) {

				list.add(new Requirement(PRIMARY, true, true));
				list.add(new Requirement(CONTACT_NAME, false, true));
				list.add(new Requirement(TITLE, true, true));
				list.add(new Requirement(BUSINESS_PHONE, true, true));
				list.add(new Requirement(EMAIL, true, true));

			}
		});

		list.add(new Requirement(ACCOUNT, true, true));
		list.add(new Requirement(CREDIT_LIMIT, true, true));
		list.add(new Requirement(PREFERRED_SHIPPING_METHOD, true, true));
		list.add(new Requirement(PAYMENT_METHOD, true, true));
		list.add(new Requirement(PAYMENT_TERMS, true, true));
		list.add(new Requirement(ACCOUNT_NO, true, true));
		list.add(new Requirement(BANK_NAME, true, true));
		list.add(new Requirement(BANK_BRANCH, true, true));
		list.add(new Requirement(VENDOR_GROUP, true, true));
		list.add(new Requirement(VAT_REGISTRATION_NUMBER, true, true));
		list.add(new Requirement(VENDOR_VAT_CODE, true, true));

	}

	@Override
	public Result run(Context context) {
		accountingType = getClientCompany().getAccountingType();
		Object attribute = context.getAttribute(INPUT_ATTR);
		if (attribute == null) {
			context.setAttribute(INPUT_ATTR, "optional");
		}
		String process = (String) context.getAttribute(PROCESS_ATTR);
		Result result = null;
		if (process != null) {
			if (process.equals(ADDRESS_PROCESS)) {
				result = addressProcess(context);
				if (result != null) {
					return result;
				}
			}
			if (process.equals(CONTACT_PROCESS)) {
				result = contactProcess(context);
				if (result != null) {
					return result;
				}
			}
		}

		Result makeResult = context.makeResult();
		makeResult.add(" Vendor is ready to create with following values.");
		ResultList list = new ResultList("values");
		makeResult.add(list);
		ResultList actions = new ResultList(ACTIONS);
		makeResult.add(actions);

		result = nameRequirement(context, list, VENDOR_NAME,
				"Please enter the  Vendor Name");
		if (result != null) {
			return result;
		}

		if (context.getCompany().getPreferences().getUseCustomerId()) {
			result = numberRequirement(context, list, NUMBER,
					"Please enter the  Vendor Number");
			if (result != null) {
				return result;
			}
		}
		setDefaultValues();
		result = optionalRequirements(context, list, actions, makeResult);
		if (result != null) {
			return result;
		}

		createVendorObject(context);
		markDone();
		return result;
	}

	private void setDefaultValues() {
		get(ACTIVE).setDefaultValue(true);
		get(VENDOR_SINCE).setDefaultValue(new Date());
		get(BALANCE).setDefaultValue(Double.valueOf(0.0D));
		get(BALANCE_AS_OF).setDefaultValue(new Date());
		get(BILL_TO).setDefaultValue(new ClientAddress());

	}

	private Result createVendorObject(Context context) {

		CompanyPreferences preferences = context.getCompany().getPreferences();

		ClientVendor vendor = new ClientVendor();
		String name = get(VENDOR_NAME).getValue();
		String number = null;
		if (preferences.getUseVendorId()) {
			number = get(VENDOR_NUMBER).getValue().toString();
		}
		Set<ClientContact> contacts = get(CONTACTS).getValue();
		boolean isActive = (Boolean) get(ACTIVE).getValue();
		Date balancedate = get(BALANCE_AS_OF).getValue();
		double balance = get(BALANCE).getValue();
		ClientAddress adress = get(BILL_TO).getValue();
		ClientAccount account = get(ACCOUNT).getValue();
		String phoneNum = get(PHONE).getValue();
		String faxNum = get(FAX).getValue();
		String emailId = get(EMAIL).getValue();
		String webaddress = get(WEB_PAGE_ADDRESS).getValue();
		double creditLimit = (Double) (get(CREDIT_LIMIT).getValue() == null ? 0.0
				: get(CREDIT_LIMIT).getValue().toString());
		String bankName = get(BANK_NAME).getValue();
		String bankAccountNum = get(ACCOUNT_NO).getValue();
		String bankBranch = get(BANK_BRANCH).getValue();
		ClientShippingMethod shippingMethod = get(PREFERRED_SHIPPING_METHOD)
				.getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		ClientPaymentTerms paymentTerms = get(PAYMENT_TERMS).getValue();

		String vatRegistredNum = get(VAT_REGISTRATION_NUMBER).getValue();

		HashSet<ClientAddress> addresses = new HashSet<ClientAddress>();
		if (adress != null) {
			addresses.add(adress);
		}

		vendor.setName(name);
		if (preferences.getUseVendorId())
			vendor.setVendorNumber(number);

		if (balancedate != null) {
			vendor.setBalanceAsOf(balancedate.getTime());
		}
		vendor.setContacts(contacts);
		vendor.setBalance(balance);
		vendor.setAddress(addresses);
		vendor.setPhoneNo(phoneNum);
		vendor.setFaxNo(faxNum);
		vendor.setWebPageAddress(webaddress);
		vendor.setBankAccountNo(bankAccountNum);
		vendor.setBankBranch(bankBranch);
		vendor.setBankName(bankName);
		vendor.setEmail(emailId);

		if (accountingType == Company.ACCOUNTING_TYPE_US) {
			boolean isTrackPaymentsFor1099 = get(TRACK_PAYMENTS_FOR_1099)
					.getValue();
			vendor.setTrackPaymentsFor1099(isTrackPaymentsFor1099);
		}
		vendor.setExpenseAccount(account);
		vendor.setCreditLimit(creditLimit);
		if (preferences.isDoProductShipMents() && shippingMethod != null)
			vendor.setShippingMethod(shippingMethod);
		vendor.setPaymentMethod(paymentMethod);
		if (paymentTerms != null)
			vendor.setPaymentTerms(paymentTerms);

		ClientVendorGroup value = get(VENDOR_GROUP).getValue();
		if (value != null)
			vendor.setVendorGroup(value.getID());
		vendor.setActive(isActive);
		ClientTAXCode code = get(VENDOR_VAT_CODE).getValue();
		if (code != null)
			vendor.setTAXCode(code.getID());
		vendor.setVATRegistrationNumber(vatRegistredNum);

		create(vendor, context);
		markDone();

		Result result = new Result();
		result.add(" Vendor was created successfully.");

		return result;

	}

	private Result optionalRequirements(Context context, ResultList list,
			ResultList actions, Result makeResult) {
		// context.setAttribute(INPUT_ATTR, "optional");
		Object selection = context.getSelection(ACTIONS);
		ICountryPreferences countryPreferences = getClientCompany()
				.getCountryPreferences();
		ClientCompanyPreferences preferences = getClientCompany()
				.getPreferences();
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case FINISH:
				context.removeAttribute(INPUT_ATTR);
				return null;
			default:
				break;
			}
		}

		selection = context.getSelection("values");

		booleanOptionalRequirement(context, selection, list, ACTIVE,
				"This vendor is Active", "This Vendor is InActive");

		if (accountingType == Company.ACCOUNTING_TYPE_US) {
			booleanOptionalRequirement(context, selection, list,
					TRACK_PAYMENTS_FOR_1099,
					"Track payments for 1099 is Active",
					"Track payments for 1099 is InActive");
		}
		Result result = dateOptionalRequirement(context, list, VENDOR_SINCE,
				VENDOR_SINCE, selection);

		if (result != null) {
			return result;
		}

		result = amountOptionalRequirement(context, list, selection, BALANCE,
				"Enter Balance");
		if (result != null) {
			return result;
		}
		result = dateOptionalRequirement(context, list, BALANCE_AS_OF,
				BALANCE_AS_OF, selection);
		if (result != null) {
			return result;
		}

		result = billToRequirement(context, list, selection);
		if (result != null) {
			return result;
		}

		result = numberOptionalRequirement(context, list, selection, PHONE,
				"Enter Phone Number");
		if (result != null) {
			return result;
		}
		result = numberOptionalRequirement(context, list, selection, FAX,
				"Enter Fax Number");
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, EMAIL,
				"Enter Email Id");
		if (result != null) {
			return result;
		}

		result = stringOptionalRequirement(context, list, selection,
				WEB_PAGE_ADDRESS, "Enter Web page address");
		if (result != null) {
			return result;
		}

		result = amountOptionalRequirement(context, list, selection,
				CREDIT_LIMIT, "Enter Credit Limit ");
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection, BANK_NAME,
				"Enter Bank name");
		if (result != null) {
			return result;
		}
		result = numberOptionalRequirement(context, list, selection,
				ACCOUNT_NO, "Enter Account Number ");
		if (result != null) {
			return result;
		}
		result = stringOptionalRequirement(context, list, selection,
				BANK_BRANCH, "Enter Bank branch name");
		if (result != null) {
			return result;
		}
		result = accountsOptionalRequirement(context, list, selection, ACCOUNT);
		if (result != null) {
			return result;
		}
		result = paymentTermRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		result = paymentMethodOptionalRequirement(context, list,
				(String) selection);
		if (result != null) {
			return result;
		}
		if (preferences.isDoProductShipMents()) {
			result = preferredShippingMethodRequirement(context, list,
					selection);
			if (result != null) {
				return result;
			}
		}

		result = vendorGroupRequirement(context, list, selection);
		if (result != null) {
			return result;
		}
		if (preferences.isTrackTax()) {
			if (countryPreferences.isVatAvailable()) {
				result = numberOptionalRequirement(context, list, selection,
						VAT_REGISTRATION_NUMBER,
						"Enter vat Registeration Number");
				if (result != null) {
					return result;
				}
				result = VatCodeRequirement(context, list, selection);
				if (result != null) {
					return result;
				}
			}
		}
		if (preferences.isTrackTax()) {
			// result = stringOptionalRequirement(context, list, selection,
			// PAN_NUM, "Enter Personal Ledger number");
			// if (result != null) {
			// return result;
			// }
			if (countryPreferences.isSalesTaxAvailable()) {
				result = numberOptionalRequirement(context, list, selection,
						CST_NUM, "Enter CST Number ");
				if (result != null) {
					return result;
				}
			}
			if (countryPreferences.isServiceTaxAvailable()) {
				result = numberOptionalRequirement(context, list, selection,
						SERVICE_TAX_NUM,
						"Enter Service tax registration Number ");
				if (result != null) {
					return result;
				}
			}
			if (countryPreferences.isTDSAvailable()) {
				result = numberOptionalRequirement(context, list, selection,
						TIN_NUM, "Enter Taxpayer identification number");
				if (result != null) {
					return result;
				}
			}
		}

		Record finish = new Record(ActionNames.FINISH);
		finish.add("", "Finish to create Vendor.");
		actions.add(finish);
		return makeResult;
	}

	private Result VatCodeRequirement(Context context, ResultList list,
			Object selection) {
		Object customerVatCodeObj = context.getSelection(TAXCODE);
		Requirement customerVatCodeReq = get(VENDOR_VAT_CODE);
		ClientTAXCode vatCode = (ClientTAXCode) customerVatCodeReq.getValue();

		if (customerVatCodeObj != null) {
			vatCode = (ClientTAXCode) customerVatCodeObj;
			customerVatCodeReq.setValue(vatCode);
		}
		if (selection != null) {
			if (selection.equals("vatCode")) {
				context.setAttribute(INPUT_ATTR, VENDOR_VAT_CODE);
				return taxCode(context, vatCode);
			}
		}

		Record customerVatCodeRecord = new Record("vatCode");
		customerVatCodeRecord.add("Name", "vatCode");
		customerVatCodeRecord.add(
				"Value",
				vatCode == null ? "" : vatCode.getName() + "-"
						+ vatCode.getSalesTaxRate());
		list.add(customerVatCodeRecord);

		Result result = new Result();
		result.add(list);
		return null;
	}

	private Result vendorGroupRequirement(Context context, ResultList list,
			Object selection) {

		Object vendorGroupObj = context.getSelection(VENDOR_GROUP);
		Requirement vendorGroupReq = get(VENDOR_GROUP);
		String vendorGRP = (String) vendorGroupReq.getValue();

		if (vendorGroupObj != null) {
			vendorGRP = (String) vendorGroupObj;
			vendorGroupReq.setValue(vendorGRP);
		}
		if (selection != null)
			if (selection == VENDOR_GROUP) {
				context.setAttribute(INPUT_ATTR, VENDOR_GROUP);
				return vendorGroups(context, vendorGRP);
			}
		Record customerGroupRecord = new Record(VENDOR_GROUP);
		customerGroupRecord.add("Name", VENDOR_GROUP);
		customerGroupRecord.add("Value", vendorGRP);
		list.add(customerGroupRecord);

		Result result = new Result();
		result.add(list);
		return null;
	}

	private Record createVendorGroupRecord(String oldVenodrGroup) {
		Record record = new Record(oldVenodrGroup);
		record.add("Name", VENDOR_GROUP);
		record.add("value", oldVenodrGroup);
		return record;
	}

	private Result vendorGroups(Context context, String oldVendorGroup) {
		Set<VendorGroup> vendorGroups = getVendorGroupsList(context);
		Result result = context.makeResult();
		result.add("Select Vendor Group");

		ResultList list = new ResultList(VENDOR_GROUP);
		int num = 0;
		if (oldVendorGroup != null) {
			list.add(createVendorGroupRecord(oldVendorGroup));
			num++;
		}
		for (VendorGroup vendor : vendorGroups) {
			if (vendor.getName() != oldVendorGroup) {
				list.add(createVendorGroupRecord(vendor.getName()));
				num++;
			}
			if (num == VENDORGROUP_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create Vendor Group");
		result.add(commandList);

		return result;
	}

	private Set<VendorGroup> getVendorGroupsList(Context context) {
		Set<VendorGroup> vendorGroups = context.getCompany().getVendorGroups();
		return vendorGroups;
	}

}
