package com.vimukti.accounter.web.client.ui.banking;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.IncomeAndExpensesAccountCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.InvalidEntryException;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Mandeep Singh
 * 
 */
@SuppressWarnings("unchecked")
public class TransferFundsDialog extends BaseDialog {

	private DateField transferDate;
	private DynamicForm transferForm, transferFromForm, transferToForm;
	protected IncomeAndExpensesAccountCombo accountComboFrom, accountComboTo;
	private TextItem memoText;
	AmountField amountText;
	AmountField balanceToText, balanceFromText;
	private CheckboxItem transferOnlineCheck;
	protected boolean isClose;
	protected ClientAccount accountFrom, accountTo;
	BankingMessages bankingConstants = GWT.create(BankingMessages.class);
	public boolean isValidatedTransferAmount = false;
	ClientTransferFund transferFund;

	private Double transferAmount = 0D;
	private AccounterButton editButton;

	public TransferFundsDialog(Object data) {
		super(Accounter.getBankingsMessages().transferFunds(), Accounter
				.getBankingsMessages().toTransferFunds());
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
		balanceFromText.setValue(DataUtils
				.getAmountAsString(fromAccount != null ? fromAccount
						.getTotalBalance() : 0.0));
		ClientAccount toAccount = getCompany().getAccount(data.getTransferTo());
		accountTo = toAccount;
		accountComboTo.setSelected(toAccount != null ? accountComboTo
				.getDisplayName(toAccount) : "");
		accountComboTo.setDisabled(true);
		balanceToText.setValue(DataUtils
				.getAmountAsString(toAccount != null ? toAccount
						.getTotalBalance() : 0.0));
		memoText.setValue(data.getMemo());
		memoText.setDisabled(true);
		amountText.setValue(DataUtils.getAmountAsString(data.getTotal()));
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

		accountComboFrom = new IncomeAndExpensesAccountCombo(
				bankingConstants.fromAccount());

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

		balanceFromText = new AmountField(bankingConstants.balance());
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

		accountComboTo = new IncomeAndExpensesAccountCombo(
				bankingConstants.toAccount());
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
		balanceToText = new AmountField(bankingConstants.balance());
		balanceToText.setDisabled(Boolean.TRUE);

		amountText = new AmountField(bankingConstants.amount());
		amountText.setRequired(true);
		amountText.setAmount(0.00);
		amountText.addBlurHandler(new BlurHandler() {

			public void onBlur(BlurEvent event) {
				if (amountText.getAmount() != null) {
					try {
						transferAmount = amountText.getAmount();
						if (DecimalUtil.isLessThan(transferAmount, 0D)) {
							transferAmount = 0D;
							Accounter.showError(Accounter.getBankingsMessages()
									.amountShouldNotBeNegative());
						}

					} catch (Exception e) {
						Accounter.showError(Accounter.getBankingsMessages()
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
		okbtn.setTitle(Accounter.getBankingsMessages().transfer());
		cancelBtn.setTitle(Accounter.getBankingsMessages().cancel());

		HorizontalPanel hlay = new HorizontalPanel();
		hlay.setWidth("100%");
		// hlay.setMembersMargin(10);
		hlay.add(transferFromForm);
		hlay.add(transferToForm);
		editButton = new AccounterButton();
		editButton.setText("Edit");
		editButton.setVisible(false);

		editButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				onEdit();
			}
		});

		addInputDialogHandler(new InputDialogHandler() {

			public void onCancelClick() {
				removeFromParent();
			}

			public boolean onOkClick() {
				try {
					if (validateTransaction())
						createTransferFundsDialog();
				} catch (InvalidEntryException e) {
					Accounter.showError(AccounterErrorType.REQUIRED_FIELDS);
					e.printStackTrace();
				}

				return false;
			}

		});
		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");

		// footerLayout.setCellWidth(okbtn, "85%");

		footerLayout.insert(editButton, getAbsoluteLeft());
		editButton.enabledButton();
		mainVLay.add(transferForm);
		mainVLay.add(hlay);
		mainVLay.add(footerLayout);
		// mainVLay.add(editButton);
		setBodyLayout(mainVLay);
		setWidth("600");
		show();
	}

	protected boolean validateTransaction() throws InvalidEntryException {

		if (AccounterValidator.validateForm(transferFromForm, true)
				&& AccounterValidator.validateForm(transferToForm, true)) {
			if (!AccounterValidator.validate_TransferFunds(accountFrom,
					accountTo)) {
				return false;

			} else if (isValidatedTransferAmount == false) {
				return AccounterValidator.validate_TransferFromAccount(
						accountFrom, amountText.getAmount(), this);

			} else
				return true;

		}

		return true;
	}

	protected void updateToBalance(ClientAccount accountTo) {
		balanceToText.setValue(DataUtils.getAmountAsString(accountTo
				.getTotalBalance()));

	}

	protected void updateFromBalance(ClientAccount accountFrom) {

		balanceFromText.setValue(DataUtils.getAmountAsString(accountFrom
				.getTotalBalance()));

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
		ClientTransferFund transferFund1 = getTransferFundsObject();
		if (transferFund == null) {
			ViewManager.getInstance().createObject(transferFund1, this);
		} else {
			transferFund1.setID(transferFund.id);
			ViewManager.getInstance().alterObject(transferFund1, this);
		}

	}

	private ClientTransferFund getTransferFundsObject() {
		ClientTransferFund transferFund = new ClientTransferFund();
		transferFund.setType(ClientTransaction.TYPE_TRANSFER_FUND);
		if (transferDate != null)

			transferFund.setDate(((ClientFinanceDate) transferDate.getValue())
					.getTime());
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
	public Object getGridColumnValue(IsSerializable obj, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		removeFromParent();
	}

	@Override
	public void saveFailed(Throwable exception) {
		Accounter.showError(AccounterErrorType.FAILEDREQUEST);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// currently not using anywhere in the project.

	}

	public void onEdit() {
		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter
							.showMessage("Your session expired, Please login again to continue");
				} else {
					Accounter.showError(((InvalidOperationException) (caught))
							.getDetailedMessage());
				}
			}

			@Override
			public void onSuccess(Boolean result) {
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
	protected String getViewTitle() {
		return Accounter.getBankingsMessages().transferFunds();
	}

}
