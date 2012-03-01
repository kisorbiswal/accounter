package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Currency;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.ResultList;
import com.vimukti.accounter.mobile.requirements.AccountRequirement;
import com.vimukti.accounter.mobile.requirements.CurrencyAmountRequirement;
import com.vimukti.accounter.mobile.requirements.DateRequirement;
import com.vimukti.accounter.mobile.requirements.EmptyRquirement;
import com.vimukti.accounter.mobile.requirements.NameRequirement;
import com.vimukti.accounter.mobile.requirements.NumberRequirement;
import com.vimukti.accounter.mobile.requirements.StringListRequirement;
import com.vimukti.accounter.mobile.requirements.StringRequirement;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.server.FinanceTool;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class CreateFixedAssetCommand extends AbstractTransactionCommand {

	private static final String NEW_ITEM = "newItem";
	private static final String ASSET_NUMBER = "assetNumber";
	private static final String ACCOUNT = "account";
	private static final String PURCAHSE_DATE = "purchaseDate";
	private static final String PURCHASE_PRICE = "purchasePrice";
	private static final String DESCRIPTION = "description";
	private static final String ASSET_TYPE = "assetType";
	private static final String DEPRICATION_METHOD = "depricationMethod";
	private static final String DEPRECATION_RATE = "depreciationRate";
	private static final String DEPRECATION_ACCOUNT = "deprecationAccount";
	private static final String ACCUMULATED_DEPRECATION_ACCOUNT = "accumulatedDepreciationAccount";
	private boolean isAssetAccumulated;
	private double depAmount;
	private double accmulatdDepreciationAmount;

	@Override
	protected void addRequirements(List<Requirement> list) {
		list.add(new NameRequirement(NEW_ITEM, getMessages().pleaseEnter(
				getMessages().itemName()), getMessages().newItem(), false, true));
		list.add(new NumberRequirement(ASSET_NUMBER, getMessages().pleaseEnter(
				getMessages().assetNumber()), getMessages().assetNumber(),
				false, true));
		list.add(new AccountRequirement(ACCOUNT, getMessages().pleaseSelect(
				getMessages().account()), getMessages().account(), false, true,
				null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(getMessages().account());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return e.getType() == ClientAccount.TYPE_FIXED_ASSET;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(getMessages().account());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().contains(name);
			}
		});
		list.add(new DateRequirement(PURCAHSE_DATE, getMessages().pleaseEnter(
				getMessages().purchaseDate()), getMessages().purchaseDate(),
				true, true));
		list.add(new CurrencyAmountRequirement(PURCHASE_PRICE, getMessages()
				.pleaseEnter(getMessages().purchasePrice()), getMessages()
				.purchasePrice(), true, true) {

			@Override
			protected Currency getCurrency() {
				return getCompany().getPrimaryCurrency();

			}
		});
		list.add(new StringRequirement(DESCRIPTION, getMessages().pleaseEnter(
				getMessages().description()), getMessages().description(),
				true, true));
		list.add(new StringRequirement(ASSET_TYPE, getMessages().pleaseEnter(
				getMessages().assetType()), getMessages().assetType(), false,
				true));
		list.add(new StringListRequirement(DEPRICATION_METHOD, getMessages()
				.pleaseSelect(getMessages().depreciationMethod()),
				getMessages().depreciationMethod(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().depreciationMethod());
			}

			@Override
			protected String getSelectString() {
				return getMessages().pleaseSelect(
						getMessages().depreciationMethod());
			}

			@Override
			protected List<String> getLists(Context context) {
				String payVatMethodArray[] = new String[] {
						getMessages().straightLine(),
						getMessages().reducingBalance() };
				List<String> wordList = Arrays.asList(payVatMethodArray);
				return wordList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().depreciationMethod());
			}
		});
		list.add(new StringRequirement(DEPRECATION_RATE, getMessages()
				.pleaseEnter(getMessages().depreciationRate()), getMessages()
				.depreciationRate(), false, true));
		list.add(new AccountRequirement(DEPRECATION_ACCOUNT, getMessages()
				.pleaseSelect(getMessages().depreciationAccount()),
				getMessages().depreciationAccount(), false, true, null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().depreciationAccount());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return e.getType() == ClientAccount.TYPE_EXPENSE;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().depreciationAccount());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().contains(name);
			}
		});

		list.add(new EmptyRquirement() {

			@Override
			public Result run(Context context, Result makeResult,
					ResultList list, ResultList actions) {
				makeResult
						.add(getMessages()
								.assetAccountYouHaveSelectedNeedsLinkedAccumulatedDepreciationAccount());
				return null;
			}
		});

		list.add(new AccountRequirement(ACCUMULATED_DEPRECATION_ACCOUNT,
				getMessages().pleaseSelect(
						getMessages().accumulatedDepreciationAccount()),
				getMessages().accumulatedDepreciationAccount(), true, true,
				null) {

			@Override
			protected String getSetMessage() {
				return getMessages().hasSelected(
						getMessages().accumulatedDepreciationAccount());
			}

			@Override
			protected List<Account> getLists(Context context) {
				List<Account> filteredList = new ArrayList<Account>();
				for (Account obj : context.getCompany().getAccounts()) {
					if (new ListFilter<Account>() {

						@Override
						public boolean filter(Account e) {
							return e.getType() == ClientAccount.TYPE_FIXED_ASSET;
						}
					}.filter(obj)) {
						filteredList.add(obj);
					}
				}
				return filteredList;
			}

			@Override
			protected String getEmptyString() {
				return getMessages().youDontHaveAny(
						getMessages().accumulatedDepreciationAccount());
			}

			@Override
			protected boolean filter(Account e, String name) {
				return e.getName().contains(name);
			}
		});

	}

	@Override
	protected String initObject(Context context, boolean isUpdate) {
		return null;
	}

	@Override
	protected String getWelcomeMessage() {
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		try {
			String nextFixedAssetNumber = new FinanceTool()
					.getNextFixedAssetNumber(getCompanyId());
			get(ASSET_NUMBER).setValue(nextFixedAssetNumber);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getSuccessMessage() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {

		ClientFixedAsset fixedAsset = new ClientFixedAsset();

		fixedAsset.setName((String) get(NEW_ITEM).getValue());
		fixedAsset.setAssetNumber((String) get(ASSET_NUMBER).getValue());
		Account fixedAccount = get(ACCOUNT).getValue();
		Account accumulatedAccount = get(ACCUMULATED_DEPRECATION_ACCOUNT)
				.getValue();
		if (fixedAccount != null) {

			if (accumulatedAccount != null) {
				fixedAsset
						.setLinkedAccumulatedDepreciationAccount(accumulatedAccount
								.getID());
				fixedAccount
						.setLinkedAccumulatedDepreciationAccount(accumulatedAccount);
			}
			fixedAsset.setAssetAccount(fixedAccount.getID());

		}
		ClientFinanceDate purchaseDate = get(PURCAHSE_DATE).getValue();
		fixedAsset.setPurchaseDate(purchaseDate.getDate());
		showAccumultdDepAmountForm(purchaseDate);
		fixedAsset.setPurchasePrice((Double) get(PURCHASE_PRICE).getValue());
		fixedAsset.setDescription((String) get(DESCRIPTION).getValue());
		fixedAsset.setAssetType((String) get(ASSET_TYPE).getValue());

		fixedAsset.setDepreciationRate((Double) get(DEPRECATION_RATE)
				.getValue());
		fixedAsset.setDepreciationMethod(((Integer) get(DEPRICATION_METHOD)
				.getValue()).intValue());

		fixedAsset.setDepreciationExpenseAccount(((Account) get(
				DEPRECATION_ACCOUNT).getValue()).getID());
		fixedAsset.setBookValue((Double) get(PURCHASE_PRICE).getValue());

		if (isAssetAccumulated)
			fixedAsset
					.setAccumulatedDepreciationAmount(accmulatdDepreciationAmount);
		else
			fixedAsset.setAccumulatedDepreciationAmount(0.0);

		/*
		 * while registering the data from viewmode or updating a registeritem
		 */
		if ((false && fixedAsset != null)
				|| (fixedAsset != null && fixedAsset.getStatus() == ClientFixedAsset.STATUS_REGISTERED)) {
			fixedAsset.setStatus(ClientFixedAsset.STATUS_REGISTERED);
		} else if (false) {
			/* while creating a registeritem */
			fixedAsset.setStatus(ClientFixedAsset.STATUS_REGISTERED);
		} else {
			/* while updating/creating a pending item */
			fixedAsset.setStatus(ClientFixedAsset.STATUS_PENDING);
		}

		return null;
	}

	/**
	 * 
	 * @return
	 */
	public ClientFinanceDate getDepreciationStartDate() {
		return new ClientFinanceDate(getPreferences()
				.getDepreciationStartDate());
	}

	/*
	 * The AccumulatedDepreciationAmount field need to be added when given date
	 * is before the depreciation startdate
	 */

	private void showAccumultdDepAmountForm(ClientFinanceDate enteredDate) {
		if (getDepreciationStartDate() != null) {
			if (!enteredDate.equals(getDepreciationStartDate())
					&& enteredDate.before(getDepreciationStartDate())) {
				isAssetAccumulated = true;
				accmulatdDepreciationAmount = getDepreciationAmount();
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public double getDepreciationAmount() {
		ClientFinanceDate purchaseDate = get(PURCAHSE_DATE).getValue();
		String string = get(DEPRICATION_METHOD).getValue();
		int depMethod = 0;
		if (string.equals(getMessages().straightLine())) {
			depMethod = 1;
		} else if (string.equals(getMessages().reducingBalance())) {
			depMethod = 2;
		}
		double depRate = get(DEPRECATION_RATE).getValue();
		double purchasePrice = get(PURCHASE_PRICE).getValue();
		ClientFinanceDate depStartDate = getDepreciationStartDate();
		depAmount = 0.0;
		if (depMethod != 0 && !DecimalUtil.isEquals(depRate, 0)
				&& !DecimalUtil.isEquals(purchasePrice, 0)
				&& purchaseDate.before(depStartDate)) {
			FinanceTool tool = new FinanceTool();
			try {
				return tool.getFixedAssetManager()
						.getCalculatedDepreciatedAmount(depMethod, depRate,
								purchasePrice, purchaseDate.getDate(),
								depStartDate.getDate(), getCompanyId());
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}
		return 0.0;

	}

	@Override
	protected Currency getCurrency() {
		// TODO Auto-generated method stub
		return null;
	}

}