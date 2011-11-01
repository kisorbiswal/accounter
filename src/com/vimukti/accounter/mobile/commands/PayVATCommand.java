package com.vimukti.accounter.mobile.commands;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.PayVatTableRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayTAX;
import com.vimukti.accounter.web.client.core.ClientTransactionPayTAX;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;

public class PayVATCommand extends NewAbstractTransactionCommand {

	private static final String VAT_RETURN_END_DATE = "vatReturnEndDate";
	private static final String BILLS_TO_PAY = "billToPay";
	private static final String PAY_FROM = "payFrom";
	private static final String BILLS_DUE_ON_BEFORE = "billsDueOnBefore";

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new AccountRequirement(PAY_FROM, getMessages()
				.pleaseSelectPayFromAccount(getConstants().bankAccount()),
				getConstants().bankAccount(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getConstants().payFrom());
			}

			@Override
			protected List<ClientAccount> getLists(Context context) {

				return Utility.filteredList(new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						if (e.getType() == ClientAccount.TYPE_BANK
								|| e.getType() == ClientAccount.TYPE_OTHER_ASSET) {
							return true;
						}
						return false;
					}
				}, getClientCompany().getAccounts());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Accounts());
			}

			@Override
			protected boolean filter(ClientAccount e, String name) {
				return false;
			}
		});

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseEnterName(getConstants().paymentMethod()),
				getConstants().paymentMethod(), true, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages()
						.hasSelected(getConstants().paymentMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getConstants().paymentMethod());
			}

			@Override
			protected List<String> getLists(Context context) {
				return getPaymentMethods();
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getConstants().paymentMethod());
			}
		});

		list.add(new DateRequirement(BILLS_DUE_ON_BEFORE, getMessages()
				.pleaseEnter(getConstants().billsDueOnOrBefore()),
				getConstants().billsDueOnOrBefore(), true, true));

		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().date()), getConstants().date(), true, true));

		list.add(new NumberRequirement(ORDER_NO, getMessages().pleaseEnter(
				getConstants().number()), getConstants().number(), true, false));

		list.add(new PayVatTableRequirement(BILLS_TO_PAY, getMessages()
				.pleaseSelect(getConstants().billsToPay()), getConstants()
				.billsToPay()) {

			@Override
			protected List<ClientTransactionPayTAX> getList() {
				return getTransactionPayVatBills(getClientCompany());
			}
		});
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		ClientPayTAX payVAT = new ClientPayTAX();

		ClientAccount payFrom = get(PAY_FROM).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		List<ClientTransactionPayTAX> billsToPay = get(BILLS_TO_PAY).getValue();
		ClientFinanceDate vatReturnDate = get(VAT_RETURN_END_DATE).getValue();
		ClientFinanceDate transactionDate = get(DATE).getValue();
		String orderNo = get(ORDER_NO).getValue();

		payVAT.setPayFrom(payFrom.getID());
		payVAT.setPaymentMethod(paymentMethod);
		payVAT.setTransactionPayTax(billsToPay);
		payVAT.setBillsDueOnOrBefore(vatReturnDate.getDate());
		payVAT.setDate(transactionDate.getDate());
		payVAT.setNumber(orderNo);

		create(payVAT, context);

		return null;
	}

	private List<ClientTransactionPayTAX> getTransactionPayVatBills(
			ClientCompany clientCompany) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().creating(getConstants().payVAT());
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().payVAT());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(BILLS_DUE_ON_BEFORE).setDefaultValue(new ClientFinanceDate());
		get(DATE).setDefaultValue(new ClientFinanceDate());
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().payVAT());
	}

	// private Record createTransactionPayVatRecord(TransactionPayTAX
	// payVatBill) {
	// Record record = new Record(payVatBill);
	// TAXAgency taxAgency = payVatBill.getTaxAgency();
	// record.add("Vat Agency", taxAgency != null ? taxAgency.getName() : "");
	// record.add("Tax Due", payVatBill.getTaxDue());
	// record.add("Amount to pay", payVatBill.getAmountToPay());
	// return record;
	// }

}
