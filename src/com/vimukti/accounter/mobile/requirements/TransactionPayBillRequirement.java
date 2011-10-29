package com.vimukti.accounter.mobile.requirements;

import java.text.SimpleDateFormat;

import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;

public abstract class TransactionPayBillRequirement extends
		ListRequirement<ClientTransactionPayBill> {

	public TransactionPayBillRequirement(String requirementName,
			String enterString, String recordName, boolean isOptional,
			boolean isAllowFromContext,
			ChangeListner<ClientTransactionPayBill> listner) {
		super(requirementName, enterString, recordName, isOptional,
				isAllowFromContext, listner);
	}

	@Override
	protected String getEmptyString() {
		return getMessages().youDontHaveAny(getConstants().billsToPay());
	}

	@Override
	protected Record createRecord(ClientTransactionPayBill value) {
		Record paybillRecord = new Record(value);
		paybillRecord.add("", getConstants().dueDate());
		paybillRecord.add("",
				getDateAsString(new ClientFinanceDate(value.getDueDate())));
		paybillRecord.add("", getConstants().billNo());
		paybillRecord.add("", value.getBillNumber());
		paybillRecord.add("", getConstants().originalAmount());
		paybillRecord.add("", value.getOriginalAmount());
		paybillRecord.add("", getConstants().amountDue());
		paybillRecord.add("", value.getAmountDue());
		paybillRecord.add("", getConstants().discountDate());
		paybillRecord
				.add("",
						getDateAsString(new ClientFinanceDate(value
								.getDiscountDate())));
		paybillRecord.add("", getConstants().cashDiscount());
		paybillRecord.add("", value.getCashDiscount());
		return paybillRecord;
	}

	public String getDateAsString(ClientFinanceDate date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat(getClientCompany()
				.getPreferences().getDateFormat());
		return format.format(date.getDateAsObject());
	}

	@Override
	protected String getDisplayValue(ClientTransactionPayBill value) {
		return value != null ? value.getName() : "";
	}

	@Override
	protected void setCreateCommand(CommandList list) {

	}

	@Override
	protected String getSelectString() {
		return getMessages().pleaseSelect(getConstants().billsToPay());
	}

}
