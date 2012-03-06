package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class AccountMergeDialog extends BaseView<ClientAccount> {

	private DynamicForm firstForm;
	private DynamicForm secondForm;

	private OtherAccountsCombo accountCombo;
	private OtherAccountsCombo accountCombo1;
	private TextItem accountNumberTextItem;
	private TextItem accountNumberTextItem1;
	private TextItem name;
	private TextItem name1;
	private TextItem balanceTextItem1;
	private TextItem balanceTextItem;
	private ClientAccount fromAccount;
	private ClientAccount toAccount;

	public AccountMergeDialog() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("accountMergeDialog");
		createControls();
		saveAndNewButton.setVisible(false);
		saveAndCloseButton.setText(messages.merge());
		setSize("100%", "100%");
	}

	private void createControls() {
		firstForm = new DynamicForm("firstForm");
		secondForm = new DynamicForm("secondForm");
		StyledPanel layout = new StyledPanel("layout");
		StyledPanel layout1 = new StyledPanel("layout1");
		accountCombo = createAccountCombo();
		accountCombo1 = createAccountCombo1();

		accountNumberTextItem = new TextItem(messages.payeeNumber(messages
				.Account()), "accountNumberTextItem");

		accountNumberTextItem1 = new TextItem(messages.payeeNumber(messages
				.Account()), "accountNumberTextItem");

		name = new TextItem(messages.accountName(), "name");
		name1 = new TextItem(messages.accountName(), "name");

		balanceTextItem = new TextItem(messages.balance(), "balanceTextItem");

		balanceTextItem1 = new TextItem(messages.balance(), "balanceTextItem");

		firstForm.add(accountCombo, accountNumberTextItem, name,
				balanceTextItem);

		secondForm.add(accountCombo1, accountNumberTextItem1, name1,
				balanceTextItem1);
		// form.setItems(getTextItems());
		layout.add(firstForm);
		layout1.add(secondForm);
		add(layout);
		add(layout1);

	}

	private OtherAccountsCombo createAccountCombo1() {
		accountCombo1 = new OtherAccountsCombo(messages.payeeTo(messages
				.Account()), false);
		accountCombo1.setRequired(true);
		accountCombo1
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						toAccount = selectItem;
						customerSelected1(selectItem);

					}

				});

		return accountCombo1;
	}

	private OtherAccountsCombo createAccountCombo() {
		accountCombo = new OtherAccountsCombo(messages.payeeFrom(messages
				.Account()), false);
		accountCombo.setRequired(true);

		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						fromAccount = selectItem;
						customerSelected(selectItem);

					}

				});

		return accountCombo;
	}

	private void customerSelected(ClientAccount selectItem) {

		accountNumberTextItem.setValue(String.valueOf(selectItem.getNumber()));
		balanceTextItem.setValue(DataUtils
				.getAmountAsStringInPrimaryCurrency(selectItem
						.getOpeningBalance()));

		name.setValue(selectItem.getName());

	}

	private void customerSelected1(ClientAccount selectItem) {
		accountNumberTextItem1.setValue(String.valueOf(selectItem.getNumber()));
		balanceTextItem1.setValue(DataUtils
				.getAmountAsStringInPrimaryCurrency(selectItem
						.getOpeningBalance()));

		name1.setValue(selectItem.getName());

	}

	public ValidationResult validate() {
		ValidationResult result = firstForm.validate();
		if (toAccount != null && fromAccount != null) {
			if ((toAccount.getID() == fromAccount.getID())) {
				result.addError(fromAccount, messages.notMoveAccount());
				return result;
			}
			if (toAccount.getType() != fromAccount.getType()) {
				result.addError(fromAccount, messages.notMoveDiffTypeAccount());
				return result;
			}
			if (fromAccount.getID() == toAccount.getID()) {
				result.addError(fromAccount, "Accounts must be different");
				return result;
			}

			if (fromAccount.getCurrency() != toAccount.getCurrency()) {
				result.addError(fromAccount,
						"Currencies of the both Accounts must be same ");
				return result;
			}
			result = firstForm.validate();

			result = secondForm.validate();

			return result;

		}
		return result;
	}

	@Override
	public void saveAndUpdateView() {

		validate();

		Accounter.createHomeService().mergeAccount(fromAccount, toAccount,
				new AccounterAsyncCallback<ClientAccount>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(ClientAccount result) {
						getCompany().processUpdateOrCreateObject(result);
						com.google.gwt.user.client.History.back();
					}

				});
		return true;
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	}

	public void onSuccess(Void result) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getViewTitle() {
		return messages.mergeAccounts();
	}

	@Override
	public List getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
