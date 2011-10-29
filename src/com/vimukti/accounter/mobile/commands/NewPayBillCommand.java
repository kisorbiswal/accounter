package com.vimukti.accounter.mobile.commands;

import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.ChangeListner;
import com.vimukti.accounter.mobile.requirements.CurrencyRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.mobile.requirements.VendorRequirement;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.Utility;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class NewPayBillCommand extends NewAbstractTransactionCommand {

	@Override
	protected String getWelcomeMessage() {
		return getMessages().create(getConstants().payBill());
	}

	@Override
	protected String getDetailsMessage() {

		return getMessages().readyToCreate(getConstants().payBill());
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		return getMessages().createSuccessfully(getConstants().payBill());
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
				.vendor(), false, true, new ChangeListner<ClientVendor>() {

			@Override
			public void onSelection(ClientVendor value) {
				// TODO Auto-generated method stub

			}
		})

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

		list.add(new StringListRequirement(PAYMENT_METHOD, getMessages()
				.pleaseSelect(getConstants().paymentMethod()), getConstants()
				.paymentMethod(), false, true, new ChangeListner<String>() {

			@Override
			public void onSelection(String value) {
				// TODO Auto-generated method stub

			}
		}) {

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
						getConstants().directDebit(),
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
		list.add(new CurrencyRequirement(CURRENCY, getMessages().pleaseSelect(
				getConstants().currency()), getConstants().currency(), true,
				true, new ChangeListner<ClientCurrency>() {

					@Override
					public void onSelection(ClientCurrency value) {

					}
				}) {

			@Override
			protected List<ClientCurrency> getLists(Context context) {
				return context.getClientCompany().getCurrencies();
			}
		});
		list.add(new StringRequirement(MEMO, getMessages().pleaseEnter(
				getConstants().memo()), getConstants().memo(), true, true));
	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		// TODO Auto-generated method stub
		return null;
	}

}