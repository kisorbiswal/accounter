package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTransactionReceiveVAT;

public abstract class ReceiveVatTableRequirement extends
		AbstractTableRequirement<ClientTransactionReceiveVAT> {

	private static final String VAT_AGENCY = "vatAgency";
	private static final String TAX_DUE = "taxDue";
	private static final String AMOUNT = "amount";

	public ReceiveVatTableRequirement(String requirementName,
			String enterString, String recordName, boolean isCreatable,
			boolean isOptional, boolean isAllowFromContext) {
		super(requirementName, enterString, recordName, isCreatable,
				isOptional, isAllowFromContext);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		NameRequirement vatAgency = new NameRequirement(VAT_AGENCY, "",
				getConstants().vatAgency(), true, true);
		vatAgency.setEditable(false);
		list.add(vatAgency);

		AmountRequirement taxDue = new AmountRequirement(TAX_DUE, "",
				getConstants().taxDue(), true, true);
		taxDue.setEditable(false);
		list.add(taxDue);

		AmountRequirement amountReceive = new AmountRequirement(AMOUNT,
				getMessages().pleaseEnter(getConstants().amountToReceive()),
				getConstants().amountToReceive(), false, true);
		list.add(amountReceive);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getConstants().billsToPay());
	}

	@Override
	protected void getRequirementsValues(ClientTransactionReceiveVAT obj) {
		Double amount = get(AMOUNT).getValue();
		obj.setAmountToReceive(amount);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientTransactionReceiveVAT obj) {
		ClientTAXAgency taxAgency = getClientCompany().getTaxAgency(
				obj.getTaxAgency());
		if (taxAgency != null) {
			get(VAT_AGENCY).setDefaultValue(taxAgency.getName());
		}
		get(TAX_DUE).setDefaultValue(obj.getTaxDue());
	}

	@Override
	protected ClientTransactionReceiveVAT getNewObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Record createFullRecord(ClientTransactionReceiveVAT t) {
		ClientTAXAgency taxAgency = getClientCompany().getTaxAgency(
				t.getTaxAgency());

		Record record = new Record(t);
		record.add("", getConstants().vatAgency());
		record.add("", taxAgency.getName());
		record.add("", getConstants().taxDue());
		record.add("", t.getTaxDue());
		record.add("", getConstants().amountToReceive());
		record.add("", t.getAmountToReceive());
		return record;
	}

	@Override
	protected Record createRecord(ClientTransactionReceiveVAT t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getConstants().billsToReceive());
	}

}
