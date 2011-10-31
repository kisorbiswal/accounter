package com.vimukti.accounter.mobile.requirements;

import java.util.List;

import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;

public abstract class PaybillTableRequirement extends
		AbstractTableRequirement<ClientTransactionPayBill> {

	public PaybillTableRequirement(String requirementName) {
		super(requirementName, "Please Select Bills Due", "Bills Due", false,
				true, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {

		DateRequirement billDueDate = new DateRequirement("BillDueDate",
				"Please Enter Date", "Due Date", true, true);
		billDueDate.setEditable(false);
		list.add(billDueDate);

		NumberRequirement billNo = new NumberRequirement("BillNo", "",
				"Bill No", true, true);
		billNo.setEditable(false);
		list.add(billNo);

		AmountRequirement originalAmount = new AmountRequirement(
				"OriginalAmount", "", "Original Amount", true, true);
		originalAmount.setEditable(false);
		list.add(originalAmount);

		AmountRequirement amountDue = new AmountRequirement("AmountDue", "",
				"Amount Due", true, true);
		amountDue.setEditable(false);
		list.add(amountDue);

		AmountRequirement amount = new AmountRequirement("Amount",
				"Please Enter Amount", "Amount", true, true);
		list.add(amount);

	}

	@Override
	protected void getRequirementsValues(ClientTransactionPayBill obj) {
		obj.setPayment((Double) get("Amount").getValue());
	}

	@Override
	protected void setRequirementsDefaultValues(ClientTransactionPayBill obj) {
		get("BillDueDate").setDefaultValue(
				new ClientFinanceDate(obj.getDueDate()));
		get("BillNo").setDefaultValue(obj.getBillNumber());
		get("OriginalAmount").setDefaultValue(obj.getOriginalAmount());
		get("AmountDue").setDefaultValue(obj.getAmountDue());
		get("Amount").setDefaultValue(obj.getOriginalAmount());
	}

	@Override
	protected ClientTransactionPayBill getNewObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Record createFullRecord(ClientTransactionPayBill t) {
		Record record = new Record(t);
		record.add("", t.getDueDate());
		record.add("", t.getBillNumber());
		record.add("", t.getOriginalAmount());
		record.add("", t.getAmountDue());
		record.add("", t.getPayment());
		return record;
	}

	@Override
	protected Record createRecord(ClientTransactionPayBill t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return "Add more bills";
	}

}
