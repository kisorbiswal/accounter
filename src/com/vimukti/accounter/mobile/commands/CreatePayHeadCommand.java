package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.core.ClientPayHead;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;

public class CreatePayHeadCommand extends AbstractCommand {

	private static final String PAY_HEAD_NAME = "payHeadName";
	private static final String PAY_HEAD_TYPE = "payHeadType";
	private static final String PAY_HEAD_SLIPNAME = "payHeadSlipName";
	private static final String EXPENSE_ACCOUNT = "expenseAccount";
	private static final String AFFECT_NET_SALARY = "affectNetSalary";
	private static final String CALCULATION_TYPE = "calculationType";
	private static final String EARNING_DEDUCTION_ON = "earningDeduction";
	private static final String PAY_HEADS = "payheads";
	private static final String PER_DAY_CALCULATION_BASIS = "perdayCalculationBasis";
	private static final String CALCULATION_PERIOD = "calculationPeriod";
	private static final String PRODUCTION_TYPE = "productionType";

	String[] types = { getMessages().earningsForEmployees(),
			getMessages().deductionsForEmployees(),
			getMessages().employeesStatutoryDeductions(),
			getMessages().employeesStatutoryContributions(),
			getMessages().employeesOtherCharges(), getMessages().bonus(),
			getMessages().loansAndAdvances(),
			getMessages().reimbursmentsToEmployees() };

	String[] calType = { getMessages().attendance(),
			getMessages().asComputedValue(), getMessages().flatRate(),
			getMessages().production() };

	String[] attendanceTypes = { getMessages().otherPayhead(),
			getMessages().onEarningTotal(), getMessages().onSubTotal(),
			getMessages().rate() };

	String[] perDayCalculations = { getMessages().asPerCalendarPeriod(),
			getMessages().days30(), getMessages().userDefinedCalendar() };

	String[] calPeriod = { getMessages().days(), getMessages().weeks(),
			getMessages().months() };

	private ClientPayHead payHead;
	private List<String> payHeadTypesList;
	private List<String> calculationTypesList;
	private List<String> attendanceTypesList;
	private List<String> perDayCalTypesList;
	private List<String> calPeriodTypesList;

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(PAY_HEAD_NAME, getMessages().pleaseEnter(
				getMessages().payhead()), getMessages().payhead(), false, true) {
			@Override
			public void setValue(Object value) {
				if (CreatePayHeadCommand.this.isPayHeadExists((String) value)) {
					addFirstMessage(getMessages().alreadyExist());
					addFirstMessage(getMessages().pleaseEnter(
							getMessages().payhead()));
					return;
				}
				super.setValue(value);
			}
		});

		list.add(new NameRequirement(PAY_HEAD_SLIPNAME, getMessages()
				.pleaseEnter(getMessages().paySlipName()), getMessages()
				.paySlipName(), false, true) {
			@Override
			public void setValue(Object value) {
				super.setValue(value);
			}
		});

		list.add(new StringListRequirement(PAY_HEAD_TYPE, getMessages()
				.pleaseEnter(getMessages().payHeadType()), getMessages()
				.payHeadType(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().payHeadType());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().payHeadType());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getPayHeadTypes();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new BooleanRequirement(AFFECT_NET_SALARY, true) {

			@Override
			protected String getTrueString() {
				return getMessages().affectNetSalary() + " "
						+ getMessages().YES();
			}

			@Override
			protected String getFalseString() {
				return getMessages().affectNetSalary() + " "
						+ getMessages().NO();
			}
		});
		list.add(new AccountRequirement(EXPENSE_ACCOUNT, getMessages()
				.pleaseEnterNameOrNumber(getMessages().expenseAccount()),
				getMessages().expenseAccount(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return null;
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account acc) {
							return acc.getIsActive()
									&& acc.getType() != Account.TYPE_EXPENSE;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(CALCULATION_TYPE, getMessages()
				.pleaseEnter(getMessages().calculationType()), getMessages()
				.calculationType(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().calculationType());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().calculationType());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getCalculationTypes();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(EARNING_DEDUCTION_ON, getMessages()
				.pleaseEnter(getMessages().deductionOn()), getMessages()
				.deductionOn(), true, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String value = get(CALCULATION_TYPE).getValue();
				if (value.equals(getMessages().production())
						|| value.equals(getMessages().attendance())) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}

			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().deductionOn());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().deductionOn());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getEarningDeductionList();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(PAY_HEADS, getMessages()
				.pleaseEnter(getMessages().payhead()), getMessages().payhead(),
				true, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String value = get(EARNING_DEDUCTION_ON).getValue();
				if (value.equals(getMessages().otherPayhead())) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}

			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().deductionOn());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(getMessages().deductionOn());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getpayHeadslist(context);
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(PER_DAY_CALCULATION_BASIS,
				getMessages().pleaseEnter(
						getMessages().perDayCalculationBasis()), getMessages()
						.perDayCalculationBasis(), true, false, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String value = get(CALCULATION_TYPE).getValue();
				if (value.equals(getMessages().attendance())) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().perDayCalculationBasis());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().perDayCalculationBasis());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getPerDayCalculationTypes();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(CALCULATION_PERIOD, getMessages()
				.pleaseEnter(getMessages().calculationPeriod()), getMessages()
				.calculationPeriod(), true, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String value = get(CALCULATION_TYPE).getValue();
				if (value.equals(getMessages().asComputedValue())
						|| value.equals(getMessages().flatRate())) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().calculationPeriod());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().calculationPeriod());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getCalculationPeriodTypes();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

		list.add(new StringListRequirement(PRODUCTION_TYPE, getMessages()
				.pleaseEnter(getMessages().productionType()), getMessages()
				.productionType(), true, true, null) {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				String value = get(CALCULATION_TYPE).getValue();
				if (value.equals(getMessages().production())) {
					return super.run(context, makeResult, list, actions);
				} else {
					return null;
				}
			}

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getMessages().productionType());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().productionType());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getCalculationPeriodTypes();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});

	}

	protected List<String> getCalculationPeriodTypes() {
		calPeriodTypesList = new ArrayList<String>();
		for (String string : calPeriod) {
			calPeriodTypesList.add(string);
		}
		return calPeriodTypesList;
	}

	protected List<String> getPerDayCalculationTypes() {
		perDayCalTypesList = new ArrayList<String>();
		for (String string : perDayCalculations) {
			perDayCalTypesList.add(string);
		}
		return perDayCalTypesList;
	}

	protected List<String> getpayHeadslist(Context context) {
		PaginationList<ClientPayHead> payheadsList = null;
		try {
			payheadsList = new FinanceTool().getPayrollManager()
					.getPayheadsList(0, 0, context.getCompany().getID());
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		List<String> list = new ArrayList<String>();
		for (ClientPayHead clientPayHead : payheadsList) {
			list.add(clientPayHead.getName());
		}
		return list;
	}

	protected List<String> getEarningDeductionList() {
		attendanceTypesList = new ArrayList<String>();
		for (String string : attendanceTypes) {
			attendanceTypesList.add(string);
		}
		return attendanceTypesList;
	}

	protected List<String> getCalculationTypes() {
		calculationTypesList = new ArrayList<String>();
		for (String string : calType) {
			calculationTypesList.add(string);
		}
		return calculationTypesList;
	}

	protected List<String> getPayHeadTypes() {
		payHeadTypesList = new ArrayList<String>();
		for (String string : types) {
			payHeadTypesList.add(string);
		}
		return payHeadTypesList;
	}

	protected boolean isPayHeadExists(String value) {
		return false;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getMessages().payhead());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getMessages().payhead());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(CALCULATION_TYPE).setDefaultValue(getMessages().attendance());
		get(EARNING_DEDUCTION_ON).setDefaultValue(getMessages().otherPayhead());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getMessages().payhead());
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		return null;
	}
}
