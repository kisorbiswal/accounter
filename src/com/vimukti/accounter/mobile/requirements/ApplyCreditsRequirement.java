package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientTransactionReceivePayment;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public abstract class ApplyCreditsRequirement extends MultiRequirement<Double> {

	private static final String CREDITS_PAYMENTS = "creditsandpayments";
	private static final String AMOUNT_TO_USE = "creditspaymentsamounttouse";
	private static final String AMOUNT_DUE = "creditspaymentsamountDue";

	public ApplyCreditsRequirement(String requirementName, String enterString,
			String recordName) {
		super(requirementName, enterString, recordName, true, true);
		setValue(new ArrayList<ClientTransactionCreditsAndPayments>());
	}

	@Override
	protected void setDefaultValues() {
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		CurrencyAmountRequirement amountDue = new CurrencyAmountRequirement(
				AMOUNT_DUE, getMessages()
						.pleaseEnter(getMessages().amountDue()), getMessages()
						.amountDue(), false, true) {
			@Override
			protected Currency getCurrency() {
				return ApplyCreditsRequirement.this.getCurrency();
			}
		};
		amountDue.setEditable(false);
		list.add(amountDue);

		list.add(new ShowListRequirement<ClientCreditsAndPayments>(
				CREDITS_PAYMENTS, getMessages().pleaseSelect(
						getMessages().creditsPayments()), 40) {

			@Override
			protected String onSelection(ClientCreditsAndPayments value) {
				return null;
			}

			@Override
			protected String getShowMessage() {
				return null;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().creditsPayments());
			}

			@Override
			protected Record createRecord(ClientCreditsAndPayments value) {
				Record record = new Record(value);
				record.add(getMessages().creditAmount(),
						value.getCreditAmount());
				record.add(getMessages().balance(), value.getBalance());
				record.add(getMessages().amountToUse(), value.getAmtTouse());
				return record;
			}

			@Override
			protected void setCreateCommand(CommandList list) {
			}

			@Override
			protected boolean filter(ClientCreditsAndPayments e, String name) {
				return false;
			}

			@Override
			protected List<ClientCreditsAndPayments> getLists(Context context) {
				return ApplyCreditsRequirement.this.getCreditsPayments();
			}
		});

		list.add(new CurrencyAmountRequirement(AMOUNT_TO_USE, getMessages()
				.pleaseEnter(getMessages().amountToUse()), getMessages()
				.amountToUse(), false, true) {

			@Override
			protected Currency getCurrency() {
				return ApplyCreditsRequirement.this.getCurrency();
			}
		});
	}

	protected abstract Currency getCurrency();

	protected abstract List<ClientCreditsAndPayments> getCreditsPayments();

	public List<ClientCreditsAndPayments> getAppliedCredits() {
		List<ClientCreditsAndPayments> clientCreditsAndPayments = new ArrayList<ClientCreditsAndPayments>();
		for (ClientCreditsAndPayments crdPayment : getCreditsPayments()) {
			if (!DecimalUtil.isEquals(crdPayment.getAmtTouse(), 0)) {
				clientCreditsAndPayments.add(crdPayment);
			}
		}
		return clientCreditsAndPayments;
	}

	public void checkBalance(double amount) throws Exception {
		if (DecimalUtil.isEquals(amount, 0))
			throw new Exception(Accounter.messages()
					.youdnthaveBalToApplyCredits());
	}

	@Override
	protected Result onFinish(Context context) {
		setValue(getRequirement(AMOUNT_TO_USE).getValue());
		updateTempCredits();
		return null;
	}

	private void updateTempCredits() {
		// ClientTransactionReceivePayment selectedObject = getSelectedObject();
		// List<ClientCreditsAndPayments> appliedCreditsForThisRec =
		// getAppliedCredits();
		// Map<Integer, Object> appliedCredits = new HashMap<Integer, Object>();
		// TempCredit creditRec = null;
		// List<ClientTransactionReceivePayment> selectedRecords =
		// getSelectedRecords();
		// List<ClientTransactionReceivePayment> allRecords = getAllRecords();
		// for (ClientCreditsAndPayments rec : appliedCreditsForThisRec) {
		// try {
		// checkBalance(rec.getAmtTouse());
		// } catch (Exception e) {
		// Accounter.showError(e.getMessage());
		// return;
		// }
		//
		// Integer recordIndx = allRecords.indexOf(rec);
		// creditRec = new TransactionReceivePaymentTable.TempCredit();
		// for (ClientTransactionReceivePayment rcvp : selectedRecords) {
		// if (rcvp.isCreditsApplied()) {
		// for (Integer idx : rcvp.getTempCredits().keySet()) {
		// if (recordIndx == idx)
		// ((TempCredit) rcvp.getTempCredits().get(idx))
		// .setRemainingBalance(rec.getBalance());
		// }
		// }
		// }
		// creditRec.setRemainingBalance(rec.getBalance());
		// creditRec.setAmountToUse(rec.getAmtTouse());
		// appliedCredits.put(recordIndx, creditRec);
		// }
		// selectedObject.setTempCredits(appliedCredits);
		// selectedObject.setCreditsApplied(true);
		//
		// selectedObject.setAppliedCredits((Double) getValue());
	}

	private List<ClientTransactionReceivePayment> getAllRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<ClientTransactionReceivePayment> getSelectedRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	private ClientTransactionReceivePayment getSelectedObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDisplayValue() {
		return String.valueOf((Double) getValue());
	}

	public List<ClientTransactionCreditsAndPayments> getTransactionCredits(
			IAccounterCore transctn) {
		List<ClientTransactionCreditsAndPayments> clientTransactionCreditsAndPayments = new ArrayList<ClientTransactionCreditsAndPayments>();
		// if (transctn instanceof ClientTransactionPayBill) {
		// ClientTransactionPayBill trPayBill = (ClientTransactionPayBill)
		// transctn;
		// if (trPayBill.getTempCredits() != null) {
		// for (Integer indx : trPayBill.getTempCredits().keySet()) {
		// ClientCreditsAndPayments crdPayment = grid
		// .getRecordByIndex(indx);
		// crdPayment.setBalance(crdPayment.getActualAmt());
		// crdPayment.setRemaoningBalance(crdPayment.getBalance());
		// crdPayment
		// .setAmtTouse(((TransactionPayBillTable.TempCredit) trPayBill
		// .getTempCredits().get(indx))
		// .getAmountToUse());
		// // }
		// // for (IsSerializable obj : grid.getSelectedRecords()) {
		// // ClientCreditsAndPayments crdPayment =
		// // (ClientCreditsAndPayments)
		// // obj;
		// /*
		// * For backnd purpose,they need the original dueamount(the
		// * calculations(decreasing balance by 'amountToUse') are
		// * done at backend side)
		// */
		// // crdPayment.setBalance(crdPayment.getActualAmt());
		// ClientTransactionCreditsAndPayments creditsAndPayments = new
		// ClientTransactionCreditsAndPayments();
		// try {
		// creditsAndPayments.setAmountToUse(crdPayment
		// .getAmtTouse());
		// } catch (Exception e) {
		// }
		// creditsAndPayments.setDate(crdPayment.getTransaction()
		// .getTransactionDate());
		// creditsAndPayments.setMemo(crdPayment.getMemo());
		// creditsAndPayments.setCreditsAndPayments(crdPayment);
		// clientTransactionCreditsAndPayments.add(creditsAndPayments);
		// }
		// }
		// } else {
		// ClientTransactionReceivePayment rcvPaymnt =
		// (ClientTransactionReceivePayment) transctn;
		// for (Integer indx : rcvPaymnt.getTempCredits().keySet()) {
		// ClientCreditsAndPayments crdPayment = grid.getRecords().get(
		// indx);
		// crdPayment.setBalance(crdPayment.getActualAmt());
		// crdPayment.setRemaoningBalance(crdPayment.getBalance());
		// crdPayment
		// .setAmtTouse(((TransactionReceivePaymentTable.TempCredit) (rcvPaymnt
		// .getTempCredits().get(indx))).getAmountToUse());
		// // }
		// // for (IsSerializable obj : grid.getSelectedRecords()) {
		// // ClientCreditsAndPayments crdPayment =
		// // (ClientCreditsAndPayments)
		// // obj;
		// /*
		// * For backnd purpose,they need the original dueamount(the
		// * calculations(decreasing balance by 'amountToUse') are done at
		// * backend side)
		// */
		// // crdPayment.setBalance(crdPayment.getActualAmt());
		// ClientTransactionCreditsAndPayments creditsAndPayments = new
		// ClientTransactionCreditsAndPayments();
		// try {
		// creditsAndPayments.setAmountToUse(crdPayment.getAmtTouse());
		// } catch (Exception e) {
		// }
		// creditsAndPayments.setDate(crdPayment.getTransaction()
		// .getTransactionDate());
		// creditsAndPayments.setMemo(crdPayment.getMemo());
		// creditsAndPayments.setCreditsAndPayments(crdPayment);
		// clientTransactionCreditsAndPayments.add(creditsAndPayments);
		// }
		// }
		return clientTransactionCreditsAndPayments;
	}

}
