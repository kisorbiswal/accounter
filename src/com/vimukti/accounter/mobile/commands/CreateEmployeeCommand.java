package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.CustomerContactRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.EmailRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientEmployee;

public class CreateEmployeeCommand extends AbstractCommand {

	private static final String EMPLOYEE_NAME = "employeeName";
	private static final String DATE_OF_BIRTH = "dateOfBirth";
	private static final String GENDER = "gender";
	private static final String ACTIVE = "active";
	private static final String CONTACT = "contact";
	private static final String EMAIL = "email";
	private static final String ADDRESS = "address";
	private static final String BANK_NAME = "bankName";
	private static final String BANK_ACCOUNT_NUM = "bankAccountNum";
	private static final String BANK_BRANCH = "bankBranch";
	private static final String EMPLOYEE_ID = "employeeId";
	private static final String DATE_OF_HIRE = "dateOfHire";
	private static final String EMP_GROUP = "empGroup";
	private static final String DESIGNATION = "designation";
	private static final String WORKING_LOCATION = "workingLocation";

	private ClientEmployee employee;

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {

		list.add(new NameRequirement(EMPLOYEE_NAME, getMessages().pleaseEnter(
				getMessages().employeeName()), getMessages().employeeName(),
				false, true) {
			@Override
			public void setValue(Object value) {
				if (CreateEmployeeCommand.this.isEmpExists((String) value)) {
					addFirstMessage(getMessages().alreadyExist());
					addFirstMessage(getMessages().pleaseEnter(
							getMessages().employeeName()));
					return;
				}
				super.setValue(value);
			}
		});

		list.add(new DateRequirement(DATE_OF_BIRTH, getMessages().pleaseEnter(
				getMessages().dateofBirth()), getMessages().pleaseEnter(
				getMessages().dateofBirth()), true, true));

		list.add(new StringListRequirement(GENDER, getMessages().pleaseEnter(
				getMessages().gender()), getMessages().gender(), true, true,
				null) {

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().gender());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getGenders();
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().gender());
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new BooleanRequirement(ACTIVE, true) {

			@Override
			protected String getTrueString() {
				return "This Customer is Active";
			}

			@Override
			protected String getFalseString() {
				return "This Customer is In-Active";
			}
		});

		list.add(new CustomerContactRequirement(CONTACT, getMessages()
				.pleaseSelect(getMessages().contact()), CONTACT, true, true) {

			@Override
			protected List<ClientContact> getList() {
				if (employee.getID() != 0) {
					return new ArrayList<ClientContact>(employee.getContacts());
				}
				return null;
			}

		});

		list.add(new EmailRequirement(EMAIL, getMessages().pleaseEnter(
				getMessages().email()), getMessages().email(), true, true));

		list.add(new AddressRequirement(ADDRESS, getMessages().pleaseEnter(
				getMessages().address()), getMessages().address(), true, true));
		list.add(new StringRequirement(BANK_NAME, getMessages().pleaseEnter(
				getMessages().bankName()), getMessages().bankName(), true, true));

		list.add(new StringRequirement(BANK_BRANCH, getMessages().pleaseEnter(
				getMessages().bankBranch()), getMessages().bankBranch(), true,
				true));

		list.add(new StringRequirement(BANK_ACCOUNT_NUM, getMessages()
				.pleaseEnter(getMessages().bankAccountNumber()), getMessages()
				.bankAccountNumber(), true, true));
		list.add(new StringRequirement(EMPLOYEE_ID, getMessages().pleaseEnter(
				getMessages().employeeID()), getMessages().employeeID(), true,

		true));

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	protected boolean isEmpExists(String value) {
		if (this.employee.getID() != 0
				&& this.employee.getName().equalsIgnoreCase(value)) {
			return false;
		}
		return CommandUtils.isCustomerExistsWithSameName(getCompany()
				.getCustomers(), value);
	}

	private List<String> getGenders() {
		ArrayList<String> list = new ArrayList<String>();
		list.add(getMessages().unspecified());
		list.add(getMessages().male());
		list.add(getMessages().female());
		return list;
	}
}
