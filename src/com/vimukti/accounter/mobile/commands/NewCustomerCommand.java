package com.vimukti.accounter.mobile.commands;

import java.util.Date;
import java.util.List;

import com.vimukti.accounter.core.Address;
import com.vimukti.accounter.core.CreditRating;
import com.vimukti.accounter.core.CustomerGroup;
import com.vimukti.accounter.core.PaymentTerms;
import com.vimukti.accounter.core.PriceLevel;
import com.vimukti.accounter.core.SalesPerson;
import com.vimukti.accounter.core.TAXCode;
import com.vimukti.accounter.mobile.ActionNames;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.web.client.ui.Accounter;

public class NewCustomerCommand extends AbstractTransactionCommand {

	private static final int SALESPERSON_TO_SHOW = 5;
	private static final int PRICELEVEL_TO_SHOW = 5;
	private static final int CREDITRATING_TO_SHOW = 5;
	private static final int CUSTOMERGROUP_TO_SHOW = 5;
	private int VATCODE_TO_SHOW;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new Requirement("customerName", false, true));
		list.add(new Requirement("customerContact", true, true));
		list.add(new Requirement("isactive", true, true));
		list.add(new Requirement("customerSinceDate", true, true));
		list.add(new Requirement("balance", true, true));
		list.add(new Requirement("balanceAsOfDate", true, true));
		list.add(new Requirement("address", true, true));
		list.add(new Requirement("phone", true, true));
		list.add(new Requirement("fax", true, true));
		list.add(new Requirement("email", true, true));
		list.add(new Requirement("webPageAdress", true, true));
		list.add(new Requirement("salesPerson", true, true));
		list.add(new Requirement("priceLevel", true, true));
		list.add(new Requirement("creditRating", true, true));
		list.add(new Requirement("bankName", true, true));
		list.add(new Requirement("bankAccountNum", true, true));
		list.add(new Requirement("bankBranch", true, true));
		list.add(new Requirement("paymentMethod", true, true));
		list.add(new Requirement("paymentTerms", true, true));
		list.add(new Requirement("cusomerGroup", true, true));
		list.add(new Requirement("vatRegisterationNum", true, true));
		list.add(new Requirement("customerVatCode", true, true));

	}

	@Override
	public Result run(Context context) {
		Result customerNameRequirement = customerNameRequirement(context);
		if (customerNameRequirement == null) {
			// TODO
		}
		Result optionalRequirements = optionalRequirements(context);
		if (optionalRequirements == null) {
			// TODO
		}
		return createCustomerObject(context);
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result createCustomerObject(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	private Result optionalRequirements(Context context) {
		Result result = null;
		Object selection = context.getSelection("actions");
		if (selection != null) {
			ActionNames actionName = (ActionNames) selection;
			switch (actionName) {
			case ADD_MORE_CUSTOMERS:
				return customerNameresult(context);
			case FINISH:
				return null;
			default:
				break;
			}

			result = context.makeResult();
			result.add("Customer is ready to create with following values.");
			ResultList list = new ResultList("values");

			String customerName = (String) get("customerName").getValue();
			Record nameRecord = new Record(customerName);
			nameRecord.add("Name", "customerName");
			nameRecord.add("Value", customerName);
			list.add(nameRecord);

			String customerContact = (String) get("customerContact").getValue();
			Record customerContactRecord = new Record(customerContact);
			customerContactRecord.add("Name", "customerContact");
			customerContactRecord.add("Value", customerContact);
			list.add(customerContactRecord);

			boolean isActive = (Boolean) get("isactive").getDefaultValue();
			Record isActiveRecord = new Record(isActive);
			isActiveRecord.add("Name", "Is Active");
			isActiveRecord.add("Value", isActive);
			list.add(isActiveRecord);

			result = customerSinceDateRequirement(context, list, selection);
			if (result != null) {
				return result;
			}

			String customerBalance = (String) get("Balance").getValue();
			Record balanceRecord = new Record(customerBalance);
			balanceRecord.add("Name", "Customer Balanace");
			balanceRecord.add("Value", customerBalance);
			list.add(balanceRecord);

			result = balanceAsOfDateRequirement(context, list, selection);
			if (result != null) {
				return result;
			}

			result = billToRequirement(context, list, selection);
			if (result != null) {
				return result;
			}
			String phoneNum = (String) get("phone").getValue();
			Record phoneRecord = new Record(phoneNum);
			phoneRecord.add("Name", "Phone");
			phoneRecord.add("Value", phoneNum);
			list.add(phoneRecord);

			String faxNum = (String) get("fax").getValue();
			Record faxRecord = new Record(faxNum);
			faxRecord.add("Name", "Fax");
			faxRecord.add("Value", faxNum);
			list.add(faxRecord);

			String email = (String) get("email").getValue();
			Record emailRecord = new Record(email);
			emailRecord.add("Name", "Email");
			emailRecord.add("Value", email);
			list.add(emailRecord);

			String webPageAdress = (String) get("webPageAdress").getValue();
			Record webPageAdressRecord = new Record(webPageAdress);
			webPageAdressRecord.add("Name", "WebPage Adress");
			webPageAdressRecord.add("Value", webPageAdress);
			list.add(webPageAdressRecord);

			result = salesPersonRequirement(context, list, selection);
			if (result != null) {
				return result;
			}

			result = priceLevelRequirement(context, list, selection);
			if (result != null) {
				return result;
			}

			result = creditRatingRequirement(context, list, selection);
			if (result != null) {
				return result;
			}

			String bankName = (String) get("bankName").getValue();
			Record bankRecord = new Record("bankName");
			bankRecord.add("Name", "Bank Name");
			bankRecord.add("Value", bankName);
			list.add(bankRecord);

			String bankAccountNum = (String) get("bankAccountNum").getValue();
			Record bankAccountNumRecord = new Record("bankAccountNum");
			bankAccountNumRecord.add("Name", "Bank AccountNum");
			bankAccountNumRecord.add("Value", bankAccountNum);
			list.add(bankAccountNumRecord);

			String bankBranchName = (String) get("bankBranch").getValue();
			Record bankBranchRecord = new Record("bankBranch");
			bankBranchRecord.add("Name", "Bank Branch");
			bankBranchRecord.add("Value", bankBranchName);
			list.add(bankBranchRecord);

			result = paymentMethodRequirement(context, list, selection);
			if (result != null) {
				return result;
			}
			result = paymentTermRequirement(context, list, selection);
			if (result != null) {
				return result;
			}
			result = customerGroupRequirement(context, list, selection);
			if (result != null) {
				return result;
			}

			String vatRegisterationNum = (String) get("vatRegisterationNum")
					.getValue();
			Record vatRegisterationNumRecord = new Record("vatRegisterationNum");
			vatRegisterationNumRecord.add("Name", "Vat Registeration Number");
			vatRegisterationNumRecord.add("Value", vatRegisterationNum);
			list.add(vatRegisterationNumRecord);

			result = customerVatCodeRequirement(context, list, selection);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result customerVatCodeRequirement(Context context, ResultList list,
			Object selection) {

		Object customerVatCodeObj = context.getSelection("customerVatCode");
		Requirement customerVatCodeReq = get("customerVatCode");
		TAXCode vatCode = (TAXCode) customerVatCodeReq.getValue();

		if (selection == vatCode) {
			return vatCodes(context, vatCode);
		}

		if (customerVatCodeObj != null) {
			vatCode = (TAXCode) customerVatCodeObj;
			customerVatCodeReq.setDefaultValue(vatCode);
		}

		Record customerVatCodeRecord = new Record(vatCode);
		customerVatCodeRecord.add("Name", "Customer VatCode");
		customerVatCodeRecord.add("Value", vatCode.getName());
		list.add(customerVatCodeRecord);

		return null;
	}

	/**
	 * 
	 * @param context
	 * @param string
	 * @return
	 */
	private Result vatCodes(Context context, TAXCode oldTaxCode) {
		List<TAXCode> taxCodes = getTaxCodesList();
		Result result = context.makeResult();
		result.add("Select VatCode");

		ResultList list = new ResultList("customerVatCode");
		int num = 0;
		if (oldTaxCode != null) {
			list.add(createTAXCodeRecord(oldTaxCode));
			num++;
		}
		for (TAXCode taxCode : taxCodes) {
			if (taxCode != oldTaxCode) {
				list.add(createTAXCodeRecord(taxCode));
				num++;
			}
			if (num == VATCODE_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create VatCode");
		result.add(commandList);

		return result;
	}

	/**
	 * 
	 * @param oldTaxCode
	 * @return
	 */
	private Record createTAXCodeRecord(TAXCode oldTaxCode) {
		Record record = new Record(oldTaxCode);
		record.add("Name", oldTaxCode.getName());
		return record;
	}

	/**
	 * CustomerGroup
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return {@link CustomerGroupResult}
	 */
	private Result customerGroupRequirement(Context context, ResultList list,
			Object selection) {

		Object customerGroupObj = context.getSelection("cusomerGroup");
		Requirement customerGroupReq = get("cusomerGroup");
		CustomerGroup customerGroup = (CustomerGroup) customerGroupReq
				.getValue();

		if (selection == customerGroup) {
			return customerGroups(context, customerGroup);
		}

		if (customerGroupObj != null) {
			customerGroup = (CustomerGroup) customerGroupObj;
			customerGroupReq.setDefaultValue(customerGroup);
		}

		Record customerGroupRecord = new Record(customerGroup);
		customerGroupRecord.add("Name", "Cusomer Group");
		customerGroupRecord.add("Value", customerGroup.getName());
		list.add(customerGroupRecord);

		return null;
	}

	/**
	 * 
	 * @param context
	 * @param string
	 * @return
	 */
	private Result customerGroups(Context context,
			CustomerGroup oldCustomerGroup) {
		List<CustomerGroup> customerGroups = getCustomerGroupsList();
		Result result = context.makeResult();
		result.add("Select CustomerGroup");

		ResultList list = new ResultList("customerGroup");
		int num = 0;
		if (oldCustomerGroup != null) {
			list.add(createTAXCodeRecord(oldCustomerGroup));
			num++;
		}
		for (CustomerGroup customerGroup : customerGroups) {
			if (customerGroup != oldCustomerGroup) {
				list.add(createTAXCodeRecord(customerGroup));
				num++;
			}
			if (num == CUSTOMERGROUP_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create CustomerGroup");
		result.add(commandList);

		return result;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result paymentMethodRequirement(Context context, ResultList list,
			Object selection) {

		Result result = context.makeResult();

		result.add("Select PaymentMethod");

		ResultList paymethodResultList = new ResultList("paymentmethod");

		paymethodResultList.add(createPayMentMethodRecord(Accounter.constants()
				.cash()));
		paymethodResultList.add(createPayMentMethodRecord(Accounter.constants()
				.check()));
		paymethodResultList.add(createPayMentMethodRecord(Accounter.constants()
				.creditCard()));
		paymethodResultList.add(createPayMentMethodRecord(Accounter.constants()
				.directDebit()));
		paymethodResultList.add(createPayMentMethodRecord(Accounter.constants()
				.masterCard()));
		paymethodResultList.add(createPayMentMethodRecord(Accounter.constants()
				.standingOrder()));
		paymethodResultList.add(createPayMentMethodRecord(Accounter.constants()
				.onlineBanking()));
		paymethodResultList.add(createPayMentMethodRecord(Accounter.constants()
				.switchMaestro()));

		result.add(paymethodResultList);

		return result;

	}

	private Record createPayMentMethodRecord(String paymentMethod) {
		Record record = new Record(paymentMethod);
		record.add("Name", "Payment Method");
		record.add("value", paymentMethod);
		return record;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result creditRatingRequirement(Context context, ResultList list,
			Object selection) {

		Object crediRatingObj = context.getSelection("creditRating");
		Requirement creditRatingReq = get("creditRating");
		CreditRating creditRating = (CreditRating) creditRatingReq.getValue();

		if (selection == creditRating) {
			return creditRatings(context, creditRating);
		}

		if (crediRatingObj != null) {
			creditRating = (CreditRating) crediRatingObj;
			creditRatingReq.setDefaultValue(creditRating);
		}

		Record priceLevelRecord = new Record(creditRating);
		priceLevelRecord.add("Name", "Credit Rating");
		priceLevelRecord.add("Value", creditRating.getName());
		list.add(priceLevelRecord);

		return null;
	}

	/**
	 * 
	 * @param context
	 * @param string
	 * @return
	 */
	private Result creditRatings(Context context, CreditRating oldCreditRating) {

		List<CreditRating> creditRatings = getCreditRatingsList();
		Result result = context.makeResult();
		result.add("Select CreditRating");

		ResultList list = new ResultList("creditRating");
		int num = 0;
		if (oldCreditRating != null) {
			list.add(createCreditRatingRecord(oldCreditRating));
			num++;
		}
		for (CreditRating priceLevel : creditRatings) {
			if (priceLevel != oldCreditRating) {
				list.add(createCreditRatingRecord(priceLevel));
				num++;
			}
			if (num == CREDITRATING_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create creditRating");
		result.add(commandList);
		return result;
	}

	/**
	 * 
	 * @param oldCreditRating
	 * @return
	 */
	private Record createCreditRatingRecord(CreditRating oldCreditRating) {
		Record record = new Record(oldCreditRating);
		record.add("Name", oldCreditRating.getName());
		return record;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return
	 */
	private Result priceLevelRequirement(Context context, ResultList list,
			Object selection) {

		Object priceLevelObj = context.getSelection("priceLevel");
		Requirement priceLevelReq = get("priceLevel");
		PriceLevel priceLevel = (PriceLevel) priceLevelReq.getValue();

		if (selection == priceLevel) {
			return priceLevels(context, priceLevel);
		}

		if (priceLevelObj != null) {
			priceLevel = (PriceLevel) priceLevelObj;
			priceLevelReq.setDefaultValue(priceLevel);
		}

		Record priceLevelRecord = new Record(priceLevel);
		priceLevelRecord.add("Name", "Price Level");
		priceLevelRecord.add("Value", priceLevel.getName());
		list.add(priceLevelRecord);

		return null;
	}

	/**
	 * 
	 * @param context
	 * @param string
	 * @return
	 */
	private Result priceLevels(Context context, PriceLevel oldPriceLevel) {

		List<PriceLevel> priceLevels = getPriceLevelsList();
		Result result = context.makeResult();
		result.add("Select PriceLevel");

		ResultList list = new ResultList("priceLevel");
		int num = 0;
		if (oldPriceLevel != null) {
			list.add(createCreditRatingRecord(oldPriceLevel));
			num++;
		}
		for (PriceLevel priceLevel : priceLevels) {
			if (priceLevel != oldPriceLevel) {
				list.add(createCreditRatingRecord(priceLevel));
				num++;
			}
			if (num == PRICELEVEL_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create priceLevel");
		result.add(commandList);
		return result;
	}

	/**
	 * 
	 * @param oldPriceLevel
	 * @return
	 */
	private Record createCreditRatingRecord(PriceLevel oldPriceLevel) {
		Record record = new Record(oldPriceLevel);
		record.add("Name", oldPriceLevel.getName());
		return record;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return {@link Result}
	 */
	private Result salesPersonRequirement(Context context, ResultList list,
			Object selection) {

		Object salesPersonObj = context.getSelection("salesPerson");
		Requirement salesPersonReq = get("salesPerson");
		SalesPerson salesPerson = (SalesPerson) salesPersonReq.getValue();

		if (selection == salesPerson) {
			return salesPersons(context, salesPerson);
		}
		if (salesPersonObj != null) {
			salesPerson = (SalesPerson) salesPersonObj;
			salesPersonReq.setDefaultValue(salesPerson);
		}

		Record salesPersonRecord = new Record(salesPerson);
		salesPersonRecord.add("Name", "sales Person");
		salesPersonRecord.add("Value", salesPerson.getName());
		list.add(salesPersonRecord);

		return null;
	}

	/**
	 * Bill To Address
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return {@link Result}
	 */
	private Result billToRequirement(Context context, ResultList list,
			Object selection) {
		Requirement req = get("billTo");
		Address billTo = (Address) req.getValue();

		String attribute = (String) context.getAttribute("input");
		if (attribute.equals("billTo")) {
			Address input = context.getSelection("address");
			if (input == null) {
				input = context.getAddress();
			}
			billTo = input;
			req.setDefaultValue(billTo);
		}

		if (selection == billTo) {
			context.setAttribute("input", "billTo");
			return address(context, billTo);
		}

		Record billToRecord = new Record(billTo);
		billToRecord.add("Name", "Bill To");
		billToRecord.add("Value", billTo.toString());
		list.add(billToRecord);
		return null;
	}

	/**
	 * balanceAsOfDate
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return {@link Result}
	 */
	private Result balanceAsOfDateRequirement(Context context, ResultList list,
			Object selection) {
		Requirement dateReq = get("date");
		Date balanceAsofdate = (Date) dateReq.getDefaultValue();
		String attribute = (String) context.getAttribute("input");
		if (attribute.equals("balanceAsOfDate")) {
			Date date = context.getSelection("date");
			if (date == null) {
				date = context.getDate();
			}
			balanceAsofdate = date;
			dateReq.setDefaultValue(balanceAsofdate);
		}
		if (selection == balanceAsofdate) {
			context.setAttribute("input", "balanceAsOfDate");
			return date(context, "Enter BalanceAsOf  Date", balanceAsofdate);
		}

		Record transDateRecord = new Record(balanceAsofdate);
		transDateRecord.add("Name", "Balance AsOf Date");
		transDateRecord.add("Value", balanceAsofdate.toString());
		list.add(transDateRecord);
		return null;
	}

	/**
	 * customerSinceDate
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return {@link Result}
	 */
	private Result customerSinceDateRequirement(Context context,
			ResultList list, Object selection) {
		Requirement dateReq = get("date");
		Date customerSincedate = (Date) dateReq.getDefaultValue();
		String attribute = (String) context.getAttribute("input");
		if (attribute.equals("customerSinceDate")) {
			Date date = context.getSelection("date");
			if (date == null) {
				date = context.getDate();
			}
			customerSincedate = date;
			dateReq.setDefaultValue(customerSincedate);
		}
		if (selection == customerSincedate) {
			context.setAttribute("input", "customerSinceDate");
			return date(context, "Enter Customer Since Date", customerSincedate);
		}

		Record transDateRecord = new Record(customerSincedate);
		transDateRecord.add("Name", "Customer SinceDate");
		transDateRecord.add("Value", customerSincedate.toString());
		list.add(transDateRecord);
		return null;
	}

	/**
	 * 
	 * @param context
	 * @return {@link Result}
	 */
	private Result customerNameRequirement(Context context) {
		Requirement requirement = get("customerName");
		if (!requirement.isDone()) {
			String customerName = context.getSelection("customerName");
			if (customerName != null) {
				requirement.setValue(customerName);
			} else {
				return customerNameresult(context);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param context
	 * @return {@link Result}
	 */
	private Result customerNameresult(Context context) {
		Result result = context.makeResult();
		result.add("Please enter the  Customer Name");
		CommandList commands = new CommandList();
		commands.add("Create New customer");
		return result;
	}

	/**
	 * paymentTerms
	 * 
	 * @param context
	 * @param list
	 * @param selection
	 * @return {@link PaymentTerms Result}
	 */
	private Result paymentTermRequirement(Context context, ResultList list,
			Object selection) {
		Object payamentObj = context.getSelection(PAYMENT_TERMS);
		Requirement paymentReq = get("paymentTerms");
		PaymentTerms paymentTerm = (PaymentTerms) paymentReq.getValue();

		if (selection == paymentTerm) {
			return paymentTerms(context, paymentTerm);

		}
		if (payamentObj != null) {
			paymentTerm = (PaymentTerms) payamentObj;
			paymentReq.setDefaultValue(paymentTerm);
		}

		Record paymentTermRecord = new Record(paymentTerm);
		paymentTermRecord.add("Name", "Payment Terms");
		paymentTermRecord.add("Value", paymentTerm.getName());
		list.add(paymentTermRecord);
		return null;
	}

	/**
	 * SalesPerson
	 * 
	 * @param context
	 * 
	 * @param string
	 * @return {@link SalesPerson Result}
	 */
	protected Result salesPersons(Context context, SalesPerson oldsalesPerson) {
		List<SalesPerson> salesPersons = getsalePersonsList();
		Result result = context.makeResult();
		result.add("Select SalesPerson");

		ResultList list = new ResultList("salesPerson");
		int num = 0;
		if (oldsalesPerson != null) {
			list.add(createSalesPersonRecord(oldsalesPerson));
			num++;
		}
		for (SalesPerson salesPerson : salesPersons) {
			if (salesPerson != oldsalesPerson) {
				list.add(createSalesPersonRecord(salesPerson));
				num++;
			}
			if (num == SALESPERSON_TO_SHOW) {
				break;
			}
		}
		result.add(list);

		CommandList commandList = new CommandList();
		commandList.add("Create SalesPerson");
		result.add(commandList);
		return result;
	}

	/**
	 * 
	 * @param oldsalesPerson
	 * @return {@link Record}
	 */
	private Record createSalesPersonRecord(SalesPerson oldsalesPerson) {
		Record record = new Record(oldsalesPerson);
		record.add("Name", oldsalesPerson.getName());
		return record;
	}

	private List<SalesPerson> getsalePersonsList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private List<TAXCode> getTaxCodesList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private List<CustomerGroup> getCustomerGroupsList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param oldCustomerGroup
	 * @return
	 */
	private Record createTAXCodeRecord(CustomerGroup oldCustomerGroup) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private List<CreditRating> getCreditRatingsList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @return
	 */
	private List<PriceLevel> getPriceLevelsList() {
		// TODO Auto-generated method stub
		return null;
	}
}
