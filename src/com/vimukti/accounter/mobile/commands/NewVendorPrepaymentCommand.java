package com.vimukti.accounter.mobile.commands;

import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.NumberUtils;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.AddressRequirement;
import com.vimukti.accounter.mobile.requirements.AmountRequirement;
import com.vimukti.accounter.mobile.requirements.BooleanRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewVendorPrepaymentCommand extends NewAbstractTransactionCommand {

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return getMessages().create(
				getMessages().vendorPaymentsList(Global.get().Vendor()));
	}

	@Override
	protected String getDetailsMessage() {
		return getMessages().readyToCreate(
				getMessages().vendorPrePayment(Global.get().Vendor()));
	}

	@Override
	protected void setDefaultValues(Context context) {
		get(DATE).setDefaultValue(new ClientFinanceDate());
		get(MEMO).setDefaultValue("");
		get(NUMBER).setDefaultValue(
				NumberUtils.getNextTransactionNumber(
						ClientTransaction.TYPE_VENDOR_PAYMENT,
						context.getCompany()));

	}

	@Override
	public String getSuccessMessage() {

		return getMessages().createSuccessfully(
				getMessages().vendorPrePayment(Global.get().Vendor()));
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new VendorRequirement(VENDOR, getMessages()
				.pleaseSelectVendor(getConstants().Vendor()), getConstants()
				.vendor(), false, true, null)

		{

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected List<ClientVendor> getLists(Context context) {
				return context.getClientCompany().getVendors();
			}

			@Override
			protected String getEmptyString() {
				return null;
			}

			@Override
			protected boolean filter(ClientVendor e, String name) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		list.add(new NumberRequirement(NUMBER, getMessages().pleaseEnter(
				getConstants().billNo()), getConstants().billNo(), true, true));
		list.add(new DateRequirement(DATE, getMessages().pleaseEnter(
				getConstants().transactionDate()), getConstants()
				.transactionDate(), true, true));
		list.add(new AccountRequirement(PAY_FROM, getMessages()
				.pleaseSelectPayFromAccount(getConstants().bankAccount()),
				getConstants().bankAccount(), false, false,
				new ChangeListner<ClientAccount>() {

					@Override
					public void onSelection(ClientAccount value) {
						// TODO Auto-generated method stub

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
		list.add(new AddressRequirement(BILL_TO, getMessages().pleaseEnter(
				getConstants().billTo()), getConstants().billTo(), true, true));

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseSelect(getConstants().paymentMethod()), getConstants()
				.paymentMethod(), false, true, null) {

			@Override
			protected String getSetMessage() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected String getSelectString() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected List<String> getLists(Context context) {

				/*
				 * Map<String, String> paymentMethods =
				 * context.getClientCompany() .getPaymentMethods(); List<String>
				 * paymentMethod = new ArrayList<String>(
				 * paymentMethods.values());
				 */
				String payVatMethodArray[] = new String[] {
						getConstants().cash(), getConstants().creditCard(),
						getConstants().check(), getConstants().directDebit(),
						getConstants().masterCard(),
						getConstants().onlineBanking(),
						getConstants().standingOrder(),
						getConstants().switchMaestro() };
				List<String> wordList = Arrays.asList(payVatMethodArray);
				return wordList;
			}

			@Override
			protected String getEmptyString() {
				return "Empty List";
			}
		});
		list.add(new AmountRequirement(AMOUNT, getMessages().pleaseEnter(
				getConstants().amount()), getConstants().amount(), false, true));

		list.add(new BooleanRequirement(TO_BE_PRINTED, true) {

			@Override
			protected String getTrueString() {
				return getConstants().toBePrinted();
			}

			@Override
			protected String getFalseString() {
				return "Not Printed ";
			}
		});
		list.add(new StringRequirement(CHEQUE_NO, getMessages().pleaseEnter(
				getConstants().checkNo()), getConstants().checkNo(), true, true) {
			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				if ((Boolean) get(TO_BE_PRINTED).getValue()) {
					return super.run(context, makeResult, list, actions);
				}
				return null;

			}
		});
		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));

	}

	@Override
	protected Result onCompleteProcess(Context context) {

		ClientPayBill paybill = new ClientPayBill();
		ClientVendor vendor = (ClientVendor) get(VENDOR).getValue();
		ClientAddress billTo = (ClientAddress) get(BILL_TO).getValue();
		ClientAccount pay = (ClientAccount) get(PAY_FROM).getValue();

		double amount = get(AMOUNT).getValue();
		String paymentMethod = get(PAYMENT_METHOD).getValue();
		Boolean toBePrinted = (Boolean) get(TO_BE_PRINTED).getValue();
		String memo = get(MEMO).getValue();
		String chequeNumber = get(CHEQUE_NO).getValue();

		ClientFinanceDate transactionDate = get(DATE).getValue();
		paybill.setDate(transactionDate.getDate());
		paybill.setType(ClientTransaction.TYPE_PAY_BILL);
		paybill.setVendor(vendor);
		paybill.setAddress(billTo);
		paybill.setPayFrom(pay);
		paybill.setTotal(amount);
		paybill.setStatus(ClientPayBill.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED);
		paybill.setPaymentMethod(paymentMethod);
		paybill.setMemo(memo);
		paybill.setToBePrinted(toBePrinted);
		paybill.setCheckNumber(chequeNumber);
		create(paybill, context);
		return null;
	}

}