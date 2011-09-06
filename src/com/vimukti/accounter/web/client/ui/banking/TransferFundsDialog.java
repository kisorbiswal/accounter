package com.vimukti.accounter.web.client.ui.banking;

import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.IncomeAndExpensesAccountCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AccounterWarningType;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Mandeep Singh
 * 
 */

public class TransferFundsDialog extends BaseDialog<ClientTransferFund> {

	private DateField transferDate;
	private DynamicForm transferForm, transferFromForm, transferToForm;
	protected IncomeAndExpensesAccountCombo accountComboFrom, accountComboTo;
	private TextItem memoText;
	AmountField amountText;
	AmountField balanceToText, balanceFromText;
	private CheckboxItem transferOnlineCheck;
	protected boolean isClose;
	protected ClientAccount accountFrom, accountTo;
	AccounterConstants bankingConstants = Accounter.constants();
	ClientTransferFund transferFund;

	private Double transferAmount = 0D;
	private Button editButton;
	public boolean isValidatedTransferAmount = false;

	public TransferFundsDialog(Object data) {
		super(Accounter.constants().transferFunds(), Accounter.messages()
				.toTransferFunds(Global.get().account()));
		createControls();
		addAccountsNameToList();
		addAccountsName2ToList();
		if (data != null) {
			editButton.setVisible(true);

			transferFund = (ClientTransferFund) data;
			initData((ClientTransferFund) data);

		}
		center();

	}

	private void initData(ClientTransferFund data) {
		transferDate.setEnteredDate(data.getDate());
		transferDate.setDisabled(true);
		ClientAccount fromAccount = getCompany().getAccount(
				data.getTransferFrom());
		accountFrom = fromAccount;

		accountComboFrom.setSelected(fromAccount != null ? accountComboFrom
				.getDisplayName(fromAccount) : "");
		accountComboFrom.setDisabled(true);
		balanceFromText
				.setValue(amountAsString(fromAccount != null ? fromAccount
						.getTotalBalance() : 0.0));
		ClientAccount toAccount = getCompany().getAccount(data.getTransferTo());
		accountTo = toAccount;
		accountComboTo.setSelected(toAccount != null ? accountComboTo
				.getDisplayName(toAccount) : "");
		accountComboTo.setDisabled(true);
		balanceToText.setValue(amountAsString(toAccount != null ? toAccount
				.getTotalBalance() : 0.0));
		memoText.setValue(data.getMemo());
		memoText.setDisabled(true);
		amountText.setValue(amountAsString(data.getTotal()));
		amountText.setDisabled(true);
		okbtn.setEnabled(false);
	}

	private void createControls() {
		mainPanel.setSpacing(3);

		// Transfer
		transferDate = new DateField(bankingConstants.date());
		transferDate.setValue(new ClientFinanceDate());
		// transferDate.setWidth("*");

		transferForm = new DynamicForm();
		transferForm.setIsGroup(true);
		transferForm.setGroupTitle(bankingConstants.transfer());
		// transferForm.setPadding(8);
		transferForm.setFields(transferDate);

		// Transfer From

		accountComboFrom = new IncomeAndExpensesAccountCombo(Accounter
				.messages().fromAccount(Global.get().Account()));

		accountComboFrom.setRequired(true);
		// accountComboFrom.setWidth("*");

		accountComboFrom
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {

						if (selectItem instanceof ClientAccount) {

							// selectedBalance = (Account) selectItem;
							accountFrom = selectItem;
							updateFromBalance(accountFrom);

						}

					}

				});

		balanceFromText = new AmountField(bankingConstants.balance(), this);
		balanceFromText.setDisabled(Boolean.TRUE);
		memoText = new TextItem(bankingConstants.memo());

		// transferOnlineCheck = new CheckboxItem(bankingConstants
		// .transferFundsOnline());
		// transferOnlineCheck.setDisabled(true);

		transferFromForm = new DynamicForm();
		transferFromForm.setWidth("50%");
		transferFromForm.setIsGroup(true);
		transferFromForm.setGroupTitle(bankingConstants.transferFrom());

		transferFromForm.setFields(accountComboFrom, balanceFromText, memoText);

		// Transfer To

		accountComboTo = new IncomeAndExpensesAccountCombo(Accounter.messages()
				.toAccount(Global.get().Account()));
		// accountComboTo.setAddNewCaptionTitle(CustomCombo.addNewBankAccount);

		accountComboTo.setRequired(true);
		accountComboTo.setWidth(95);

		accountComboTo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					public void selectedComboBoxItem(ClientAccount selectItem) {
						// accountTo = (Account) selectItem;

						if (selectItem instanceof ClientAccount) {

							accountTo = selectItem;
							updateToBalance(accountTo);
							/*
							 * selectedBalances = (Account) selectItem;
							 * updateBalances(selectedBalances);
							 */
						}

					}

				});
		balanceToText = new AmountField(bankingConstants.balance(), this);
		balanceToText.setDisabled(Boolean.TRUE);

		amountText = new AmountField(bankingConstants.amount(), this);
		amountText.setRequired(true);
		amountText.setAmount(0.00);
		amountText.addBlurHandler(new BlurHandler() {

			public void onBlur(BlurEvent event) {
				if (amountText.getAmount() != null) {
					try {
						transferAmount = amountText.getAmount();
						if (DecimalUtil.isLessThan(transferAmount, 0D)) {
							transferAmount = 0D;
							Accounter.showError(Accounter.constants()
									.amountShouldNotBeNegative());
						}

					} catch (Exception e) {
						Accounter.showError(Accounter.constants()
								.invalidAmount());
						transferAmount = 0D;
					} finally {
						amountText.setAmount(transferAmount);
					}
				}

			}

		});

		transferToForm = new DynamicForm();
		// transferToForm.setWidth("*");
		// transferToForm.setWidth("*");
		transferToForm.setIsGroup(true);
		transferToForm.setGroupTitle(bankingConstants.transferTo());

		transferToForm.setFields(accountComboTo, balanceToText, amountText);
		okbtn.setTitle(Accounter.constants().transfer());
		cancelBtn.setTitle(Accounter.constants().cancel());

		HorizontalPanel hlay = new HorizontalPanel();
		hlay.setWidth("100%");
		// hlay.setMembersMargin(10);
		hlay.add(transferFromForm);
		hlay.add(transferToForm);
		editButton = new Button();
		editButton.setText(Accounter.constants().edit());
		editButton.setVisible(false);

		editButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				onEdit();
			}
		});

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");

		// footerLayout.setCellWidth(okbtn, "85%");

		footerLayout.insert(editButton, getAbsoluteLeft());
		mainVLay.add(transferForm);
		mainVLay.add(hlay);
		mainVLay.add(footerLayout);
		// mainVLay.add(editButton);
		setBodyLayout(mainVLay);
		setWidth("600px");
		show();
	}

	protected ValidationResult validateTransaction() {

		ValidationResult result = transferFromForm.validate();
		result.add(transferToForm.validate());

		if (!AccounterValidator.validate_TransferFunds(accountFrom, accountTo)) {
			result.addError(accountFrom, Accounter.constants().transferFunds());

		} else if (isValidatedTransferAmount == false) {
			if (AccounterValidator.validate_TransferFromAccount(accountFrom,
					amountText.getAmount(), this)) {
				result.addWarning(amountText,
						AccounterWarningType.transferFromAccount);
			}

		}
		return result;
	}

	protected void updateToBalance(ClientAccount accountTo) {
		balanceToText.setValue(amountAsString(accountTo.getTotalBalance()));

	}

	protected void updateFromBalance(ClientAccount accountFrom) {

		balanceFromText.setValue(amountAsString(accountFrom.getTotalBalance()));

	}

	public void addAccountsNameToList() {
		List<ClientAccount> result = accountComboFrom.getAccounts();
		if (result != null) {
			accountComboFrom.initCombo(result);
		}

	}

	public void addAccountsName2ToList() {
		List<ClientAccount> result = accountComboTo.getAccounts();
		if (result != null) {
			accountComboTo.initCombo(result);
		}

	}

	private void createTransferFundsDialog() {
		saveOrUpdate(transferFund);

	}

	private ClientTransferFund getTransferFundsObject() {
		ClientTransferFund transferFund = new ClientTransferFund();
		transferFund.setType(ClientTransaction.TYPE_TRANSFER_FUND);
		if (transferDate != null)

			transferFund.setDate(((ClientFinanceDate) transferDate.getValue())
					.getDate());
		transferFund.setMemo(UIUtils.toStr(memoText.getValue()));
		transferFund.setTransferFrom(accountFrom.getID());
		transferFund.setTransferTo(accountTo.getID());
		transferFund.setTotal(transferAmount);

		return transferFund;
	}

	protected void clearFields() {
		// FIXME--the fields should be cleared
		// transferFromForm.reset();
		// transferToForm.reset();
	}

	@Override
	public Object getGridColumnValue(ClientTransferFund obj, int index) {
		// currently not using anywhere in the project.
		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		removeFromParent();
	}

	@Override
	public void saveFailed(AccounterException exception) {
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(caught.getMessage());
			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transferFund
				.getType());
		this.rpcDoSerivce.canEdit(type, transferFund.id, editCallBack);
	}

	protected void enableFormItems() {
		okbtn.setEnabled(true);
		transferDate.setDisabled(false);
		accountComboFrom.setDisabled(false);
		accountComboTo.setDisabled(false);
		amountText.setDisabled(false);
		memoText.setDisabled(false);
		;

	}

	@Override
	protected ValidationResult validate() {
		return validateTransaction();
	}

	@Override
	protected boolean onOK() {
		createTransferFundsDialog();
		return false;
	}

}
