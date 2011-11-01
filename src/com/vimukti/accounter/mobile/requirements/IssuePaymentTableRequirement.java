package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransactionIssuePayment;

public abstract class IssuePaymentTableRequirement extends
		AbstractTableRequirement<ClientTransactionIssuePayment> {

	private static final String DATE = "date";
	private static final String NUMBER = "number";
	private static final String NAME = "name";
	private static final String MEMO = "memo";
	private static final String AMOUNT = "amount";
	private static final String PAYMENT_METHOD = "paymentMethod";

	public IssuePaymentTableRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, false, false, true);
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
	protected void getRequirementsValues(ClientTransactionIssuePayment obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setRequirementsDefaultValues(
			ClientTransactionIssuePayment obj) {

		get(DATE).setDefaultValue(new ClientFinanceDate(obj.getDate()));
		get(NUMBER).setDefaultValue(obj.getNumber());
		get(NAME).setDefaultValue(obj.getName());
		get(MEMO).setDefaultValue(obj.getMemo());
		get(AMOUNT).setDefaultValue(obj.getAmount());
		get(PAYMENT_METHOD).setDefaultValue(obj.getPaymentMethod());
	}

	@Override
	protected ClientTransactionIssuePayment getNewObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Record createFullRecord(ClientTransactionIssuePayment t) {
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
	protected Record createRecord(ClientTransactionIssuePayment t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getConstants().payments());
	}

}
