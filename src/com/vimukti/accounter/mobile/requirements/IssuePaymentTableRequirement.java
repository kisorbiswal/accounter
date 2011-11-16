package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.vimukti.accounter.web.server.FinanceTool;

public abstract class IssuePaymentTableRequirement extends
		AbstractTableRequirement<IssuePaymentTransactionsList> {

	private static final String DATE = "date";
	private static final String NUMBER = "number";
	private static final String NAME = "name";
	private static final String MEMO = "memo";
	private static final String AMOUNT = "amount";
	private static final String PAYMENT_METHOD = "paymentMethod";

	public IssuePaymentTableRequirement(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, false, isOptional,
				isAllowFromContext);
		setDefaultValue(new ArrayList<IssuePaymentTransactionsList>());
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		DateRequirement billDueDate = new DateRequirement(DATE, getMessages()
				.pleaseEnter(getConstants().date()), getConstants().date(),
				true, true);
		billDueDate.setEditable(false);
		list.add(billDueDate);

		NumberRequirement billNo = new NumberRequirement(NUMBER, "",
				getConstants().billNo(), true, true);
		billNo.setEditable(false);
		list.add(billNo);

		NameRequirement name = new NameRequirement(NAME, "", getConstants()
				.name(), true, true);
		name.setEditable(false);
		list.add(name);

		NameRequirement memo = new NameRequirement(MEMO, "", getConstants()
				.memo(), true, true);
		name.setEditable(false);
		list.add(memo);

		AmountRequirement amount = new AmountRequirement(AMOUNT, getMessages()
				.pleaseEnter(getConstants().amount()), getConstants().amount(),
				true, true);
		amount.setEditable(false);
		list.add(amount);

		NameRequirement paymentMethod = new NameRequirement(PAYMENT_METHOD,
				getMessages().pleaseEnter(getConstants().paymentMethod()),
				getConstants().paymentMethod(), true, true);
		paymentMethod.setEditable(false);
		list.add(paymentMethod);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getConstants().paymentsToIssue());
	}

	@Override
	protected void getRequirementsValues(IssuePaymentTransactionsList obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setRequirementsDefaultValues(IssuePaymentTransactionsList obj) {

		get(DATE).setDefaultValue((obj.getDate()));
		get(NUMBER).setDefaultValue(obj.getNumber());
		get(NAME).setDefaultValue(obj.getName());
		get(MEMO).setDefaultValue(obj.getMemo());
		get(AMOUNT).setDefaultValue(obj.getAmount());
		get(PAYMENT_METHOD).setDefaultValue(obj.getPaymentMethod());
	}

	@Override
	protected IssuePaymentTransactionsList getNewObject() {
		return new IssuePaymentTransactionsList();
	}

	protected abstract Account getAccount();

	@Override
	protected Record createFullRecord(IssuePaymentTransactionsList t) {
		Record record = new Record(t);
		record.add("", getConstants().date());
		record.add("", t.getDate());
		record.add("", getConstants().number());
		record.add("", t.getNumber());
		record.add("", getConstants().name());
		record.add("", t.getName());
		record.add("", getConstants().memo());
		record.add("", t.getMemo());
		record.add("", getConstants().amount());
		record.add("", t.getAmount());
		record.add("", getConstants().paymentMethod());
		record.add("", t.getPaymentMethod());
		return record;
	}

	@Override
	protected List<IssuePaymentTransactionsList> getList() {
		try {
			return new FinanceTool().getVendorManager().getChecks(
					getAccount().getID(), getCompanyId());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected Record createRecord(IssuePaymentTransactionsList t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return "Add More PayMent Issues";
	}
}
