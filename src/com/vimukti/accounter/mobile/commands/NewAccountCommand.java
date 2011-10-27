package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewAccountCommand extends NewAbstractCommand {

	private static final String ACCOUNT_NAME = "Account Name";
	private static final String ACCOUNT_NUMBER = "Account Number";
	private static final String OPENINGBALANCE = "Opening Balance";
	private static final String ACCOUNT_TYPE = "Account Type";
	private static final String ACTIVE = "Active";

	private static final String ASOF = "AsOf";
	private static final String COMMENTS = "Comments";
	private static final String CONSIDER_AS_CASH_ACCOUNT = "Consider As Cash Account";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringListRequirement(ACCOUNT_TYPE,
				"Please Enter Account Type", "Account Type", true, true, null) {

			@Override
			protected String getSetMessage() {
				return "Account type has been selected";
			}

			@Override
			protected String getSelectString() {
				return "Select Account type";
			}

			@Override
			protected List<String> getLists(Context context) {
				return getAccountTypes();
			}
		});
		
		list.add(new NameRequirement(ACCOUNT_NAME, "Please Enter Account Name",
				"Name", false, true));

		list.add(new NumberRequirement(ACCOUNT_NUMBER,
				"Please Enter Account number", "Account Number", false, true));

		list.add(new NumberRequirement(OPENINGBALANCE,
				"Please Enter Opening balece", "Opening balence", true, true));

		list.add(new BooleanRequirement(ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return "This account is Active";
			}

			@Override
			protected String getFalseString() {
				return "This account is InActive";
			}
		});

		list.add(new DateRequirement(ASOF, "Please Enter As Of Date",
				"As Of Date", true, true));

		list.add(new StringRequirement(COMMENTS, "Please Enter Comment",
				"Comment", true, true));

		list.add(new BooleanRequirement(CONSIDER_AS_CASH_ACCOUNT, true) {

			@Override
			protected String getTrueString() {
				return "This account is cash account";
			}

			@Override
			protected String getFalseString() {
				return "This account is not a cash account";
			}
		});

	}

	protected List<String> getAccountTypes() {
		List<String> list = new ArrayList<String>();
		list.add("Income");
		list.add("OtherIncome");
		list.add("Expense");
		list.add("OtherExpense");
		list.add("CostOfGoodSold");
		list.add("Cash");
		list.add("OtherAssets");

		list.add("CreditCard");
		list.add("FixedAssets");
		return list;
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().account());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(ACCOUNT_TYPE).setDefaultValue("Income");
		get(ACTIVE).setDefaultValue(Boolean.TRUE);
		get(CONSIDER_AS_CASH_ACCOUNT).setDefaultValue(Boolean.TRUE);
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().account());
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientAccount account = new ClientAccount();

		String accType = (String) get(ACCOUNT_TYPE).getValue();
		String accname = (String) get(ACCOUNT_NAME).getValue();
		String accountNum = (String) get(ACCOUNT_NUMBER).getValue();
		double openingBal = (Double) get(OPENINGBALANCE).getValue();
		boolean isActive = (Boolean) get(ACTIVE).getValue();
		boolean isCashAcount = (Boolean) get(CONSIDER_AS_CASH_ACCOUNT)
				.getValue();
		ClientFinanceDate asOf = get(ASOF).getValue();
		String comment = get(COMMENTS).getValue();

		account.setDefault(true);
		account.setType(getAccountTypes().indexOf(accType) + 1);
		account.setName(accname);
		account.setNumber(String.valueOf(accountNum));
		account.setOpeningBalance(openingBal);
		account.setIsActive(isActive);
		account.setAsOf(asOf.getDate());
		account.setConsiderAsCashAccount(isCashAcount);
		account.setComment(comment);
		create(account, context);
		return null;
	}
}
