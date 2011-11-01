package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.IssuePaymentTableRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientIssuePayment;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionIssuePayment;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.vimukti.accounter.web.server.FinanceTool;

public class NewIssuePaymentCommand extends NewAbstractTransactionCommand {
	private static final String PAYMENTS_TO_ISSUED = "paymentstoissued";
	private static final String CHEQUE_NO = "checknum";
	private static final String PAYMENT_METHOD = "paymentMethod";
	private static final String ACCOUNT = "account";

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new StringListRequirement(PAYMENT_METHOD,
				"Please select payment method", PAYMENT_METHOD, false, true,
				null) {

			@Override
			protected String getSetMessage() {
				return "Payment method selected";
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getConstants().paymentMethod());
			}

			@Override
			protected List<String> getLists(Context context) {
				String payVatMethodArray[] = new String[] {
						getConstants().cash(), getConstants().check() };
				List<String> wordList = Arrays.asList(payVatMethodArray);
				return wordList;
			}

			@Override
			protected String getEmptyString() {
				return null;
			}
		});
		list.add(new AccountRequirement(ACCOUNT, getMessages()
				.pleaseSelectPayFromAccount(getConstants().bankAccount()),
				getConstants().bankAccount(), false, false,
				new ChangeListner<ClientAccount>() {

					@Override
					public void onSelection(ClientAccount value) {
						resetIssuedPayments(value);
					}
				}) {

			@Override
			protected String getSetMessage() {
				return "";
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
				return "No bank acounts available";
			}

			@Override
			protected boolean filter(ClientAccount e, String name) {
				return false;
			}
		});
		list.add(new StringRequirement(CHEQUE_NO, getMessages().pleaseEnter(
				getConstants().checkNo()), getConstants().checkNo(), true, true));
		list.add(new IssuePaymentTableRequirement(PAYMENTS_TO_ISSUED,
				"Please Select Payment", "payments list", false, false, true) {

			@Override
			protected List<ClientTransactionIssuePayment> getList() {
				return getclientTransactionIssuePayments(getClientCompany());
			}
		});
	}

	protected void resetIssuedPayments(ClientAccount value) {
		get(PAYMENTS_TO_ISSUED).setValue(
				new ArrayList<ClientTransactionIssuePayment>());
	}

	private String getNextCheckNumber(Context context) {
		ClientAccount account = get(ACCOUNT).getValue();
		if (account == null) {
			return "";
		}
		String checknumber = "";
		try {
			String nextIssuePaymentCheckNumber = new FinanceTool()
					.getNextIssuePaymentCheckNumber(account.getID(), context
							.getClientCompany().getID());
			if (nextIssuePaymentCheckNumber != null) {
				checknumber = nextIssuePaymentCheckNumber;
			}
		} catch (Exception e) {
			checknumber = "";
		}
		return checknumber;
	}

	private String getNextTransactionNumber(Context context) {
		String nextTransactionNumber = new FinanceTool()
				.getNextTransactionNumber(ClientTransaction.TYPE_ISSUE_PAYMENT,
						context.getClientCompany().getID());
		return nextTransactionNumber;
	}

	private List<ClientTransactionIssuePayment> getclientTransactionIssuePayments(
			ClientCompany clientCompany) {
		ClientAccount account = get(ACCOUNT).getValue();
		return getchecks(clientCompany, account.getID());
	}

	protected ArrayList<ClientTransactionIssuePayment> getchecks(
			ClientCompany clientCompany, long accountId) {
		ArrayList<IssuePaymentTransactionsList> checks;
		ArrayList<ClientTransactionIssuePayment> issuepayments = new ArrayList<ClientTransactionIssuePayment>();
		try {
			checks = new FinanceTool().getVendorManager().getChecks(accountId,
					clientCompany.getID());

			for (IssuePaymentTransactionsList entry : checks) {
				ClientTransactionIssuePayment record = new ClientTransactionIssuePayment();
				if (entry.getDate() != null)
					record.setDate(entry.getDate().getDate());
				if (entry.getNumber() != null)
					record.setNumber(entry.getNumber());
				record.setName(entry.getName() != null ? entry.getName() : "");
				record.setMemo(entry.getMemo() != null ? entry.getMemo() : "");
				if (entry.getAmount() != null)
					record.setAmount(entry.getAmount());
				if (entry.getPaymentMethod() != null)
					record.setPaymentMethod(entry.getPaymentMethod());
				record.setRecordType(entry.getType());
				if (record.getRecordType() == ClientTransaction.TYPE_WRITE_CHECK)
					record.setWriteCheck(entry.getTransactionId());
				else if (record.getRecordType() == ClientTransaction.TYPE_CUSTOMER_REFUNDS)
					record.setCustomerRefund(entry.getTransactionId());
				record.setID(entry.getTransactionId());
				issuepayments.add(record);
			}

		} catch (DAOException e) {
			e.printStackTrace();
			issuepayments = new ArrayList<ClientTransactionIssuePayment>();
		}
		return issuepayments;
	}

	private void completeProcess(Context context) {
		ClientIssuePayment issuePayment = new ClientIssuePayment();
		issuePayment.setType(ClientTransaction.TYPE_ISSUE_PAYMENT);
		issuePayment.setNumber(getNextTransactionNumber(context));
		issuePayment.setDate(new ClientFinanceDate().getDate());
		String paymentmethod = get(PAYMENT_METHOD).getValue();
		issuePayment.setPaymentMethod(paymentmethod);
		ClientAccount account = get(ACCOUNT).getValue();
		issuePayment.setAccount(account.getID());
		String chequenum = get(CHEQUE_NO).getValue();
		if (chequenum.isEmpty()) {
			chequenum = "1";
		}
		issuePayment.setCheckNumber(chequenum);
		ArrayList<ClientTransactionIssuePayment> issuepayments = get(
				PAYMENTS_TO_ISSUED).getValue();
		issuePayment.setTransactionIssuePayment(issuepayments);
		setTransactionTotal(issuePayment);
		create(issuePayment, context);
	}

	private void setTransactionTotal(ClientIssuePayment issuePayment) {
		double total = 0.0;
		for (ClientTransactionIssuePayment rec : issuePayment
				.getTransactionIssuePayment()) {
			total += rec.getAmount();
		}
		issuePayment.setTotal(total);

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return "Creating Issue Payment... ";
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(getConstants().issuePayment());
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(CHEQUE_NO).setValue(getNextCheckNumber(context));
	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().issuePayment());
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		completeProcess(context);
		return null;
	}
}
