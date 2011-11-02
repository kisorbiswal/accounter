package com.vimukti.accounter.mobile.requirements;

import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.Accounter;

public class MakeDepositTableRequirement extends
		AbstractTableRequirement<ClientTransactionMakeDeposit> {

	private static final String ACCOUNT_FROM = "accountFrom";
	private static final String REFERENCE = "reference";
	private static final String RECEIVED_FROM = "receivedFrom";
	private static final String AMOUNT = "amount";

	public MakeDepositTableRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, true, false, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		NameRequirement receivedFrom = new NameRequirement(RECEIVED_FROM, "",
				getConstants().receivedFrom(), true, true);
		receivedFrom.setEditable(false);
		list.add(receivedFrom);

		AccountRequirement account = new AccountRequirement(ACCOUNT_FROM,
				getMessages().pleaseEnter(
						getMessages().accountFrom(Global.get().Account())),
				getMessages().accountFrom(Global.get().Account()), false, true,
				null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().accountFrom(Global.get().Account()));
			}

			@Override
			protected List<ClientAccount> getLists(Context context) {
				return Utility.filteredList(new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						return Arrays.asList(ClientAccount.TYPE_BANK,
								ClientAccount.TYPE_OTHER_CURRENT_ASSET,
								ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
								ClientAccount.TYPE_EQUITY).contains(
								account.getType());
					}
				}, context.getClientCompany().getAccounts());
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(Global.get().Account());
			}
		};
		list.add(account);

		NameRequirement reference = new NameRequirement(REFERENCE,
				getMessages().pleaseEnter(getConstants().reference()),
				getConstants().reference(), true, true);
		list.add(reference);

		AmountRequirement amount = new AmountRequirement(AMOUNT, getMessages()
				.pleaseEnter(getConstants().amount()), getConstants().amount(),
				false, true);
		list.add(amount);

	}

	@Override
	protected String getEmptyString() {
		return "";
	}

	@Override
	protected void getRequirementsValues(ClientTransactionMakeDeposit obj) {
		ClientAccount accountFrom = get(ACCOUNT_FROM).getValue();
		obj.setAccount(accountFrom.getID());

		String reference = get(REFERENCE).getValue();
		obj.setReference(reference);

		Double amount = get(AMOUNT).getValue();
		obj.setAmount(amount);

		obj.setType(ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientTransactionMakeDeposit obj) {
		get(RECEIVED_FROM).setDefaultValue(getConstants().transfer());
	}

	@Override
	protected ClientTransactionMakeDeposit getNewObject() {
		ClientTransactionMakeDeposit clientTransactionMakeDeposit = new ClientTransactionMakeDeposit();
		clientTransactionMakeDeposit.setIsNewEntry(true);
		clientTransactionMakeDeposit.setType(ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT);
		return clientTransactionMakeDeposit;
	}

	@Override
	protected Record createFullRecord(ClientTransactionMakeDeposit t) {
		ClientAccount account = getClientCompany().getAccount(t.getAccount());
		Record record = new Record(t);
		record.add("", getConstants().receivedFrom());
		record.add("", getReceivedFrom(t));
		record.add("", getMessages().accountFrom(Global.get().Account()));
		record.add("", account.getName());
		record.add("", getConstants().reference());
		record.add("", t.getReference());
		record.add("", getConstants().amount());
		record.add("", t.getAmount());
		return record;
	}

	private String getReceivedFrom(ClientTransactionMakeDeposit value) {
		switch (value.getType()) {
		case ClientTransactionMakeDeposit.TYPE_FINANCIAL_ACCOUNT:
			return getConstants().transfer();
		case ClientTransactionMakeDeposit.TYPE_VENDOR:
			return Global.get().vendor();
		case ClientTransactionMakeDeposit.TYPE_CUSTOMER:
			return Global.get().customer();
		default:
			return "";
		}
	}

	@Override
	protected List<ClientTransactionMakeDeposit> getList() {
		return getValue();
	}

	@Override
	protected Record createRecord(ClientTransactionMakeDeposit t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(Global.get().Accounts());
	}

}
