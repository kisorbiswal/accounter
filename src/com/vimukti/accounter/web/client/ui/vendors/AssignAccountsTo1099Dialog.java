package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author Umasree V
 * 
 */
public class AssignAccountsTo1099Dialog extends BaseDialog {

	private FlexTable flexTable;
	private String[] strings;
	private int[] boxNums = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 13, 14 };
	private int rowCount = 0;

	public AssignAccountsTo1099Dialog(String title, String desc) {
		super(title, desc);
		AccounterConstants c = Accounter.constants();
		strings = new String[] { c.box1Label(), c.box2Label(), c.box3Label(),
				c.box4Label(), c.box5Label(), c.box6Label(), c.box7Label(),
				c.box8Label(), c.box9Label(), c.box10Label(), c.box13Label(),
				c.box14Label() };
		createControls();
	}

	private void createControls() {
		VerticalPanel verticalPanel = new VerticalPanel();
		setWidth("570px");
		// Label label = new Label(
		// "Certain payments made to vendors must be assigned to IRS-defined boxes. To do so, assign to each box the accounts in which you track these payments.");
		// label.addStyleName("centre");

		flexTable = new FlexTable();
		flexTable.insertRow(rowCount);
		flexTable
				.setWidget(rowCount, 0, new Label(Accounter.constants().use()));
		flexTable.setWidget(rowCount, 1, new Label(Accounter.constants()
				.get1099Information()));
		flexTable.setWidget(rowCount, 2, new Label(Accounter.messages()
				.account(Global.get().Account())));
		rowCount++;
		for (String string : strings) {
			addRow(string, boxNums[rowCount - 1]);
		}

		// verticalPanel.add(label);
		verticalPanel.add(flexTable);

		setBodyLayout(verticalPanel);
		center();
	}

	private void addRow(String string, int boxNo) {
		final ArrayList<ClientAccount> activeAccounts = getCompany()
				.getActiveAccounts();// getAccounts();
		final CheckBox checkBox = new CheckBox();
		Label label = new Label(string);
		final AccountCombo accountCombo = new AccountCombo("", true) {

			@Override
			protected List<ClientAccount> getAccounts() {
				return activeAccounts;
			}
		};
		accountCombo.setDisabled(true);
		checkBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (checkBox.isChecked()) {
					accountCombo.setDisabled(false);
				} else {
					accountCombo.setDisabled(true);
					accountCombo.setValue(null);
				}
			}
		});
		ClientAccount clientAccount = getAccountByBoxNum(boxNo);
		if (clientAccount != null) {
			checkBox.setChecked(true);
			accountCombo.setComboItem(clientAccount);
			accountCombo.setDisabled(false);
		}
		DynamicForm accountsform = new DynamicForm();
		accountCombo.setName("AccountsCombo");
		accountsform.setFields(accountCombo);

		flexTable.insertRow(rowCount);
		flexTable.setWidget(rowCount, 0, checkBox);
		flexTable.setWidget(rowCount, 1, label);
		flexTable.setWidget(rowCount, 2, accountsform);
		rowCount++;
	}

	private ClientAccount getAccountByBoxNum(int i) {
		ArrayList<ClientAccount> activeAccounts = getCompany()
				.getActiveAccounts();
		for (ClientAccount clientAccount : activeAccounts) {
			if (clientAccount.getBoxNumber() == i) {
				return clientAccount;
			}
		}
		return null;
	}

	private ArrayList<ClientAccount> getAccounts() {
		ArrayList<ClientAccount> gridAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (account.getType() != ClientAccount.TYPE_CASH
					&& account.getType() != ClientAccount.TYPE_BANK
					&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE)
				gridAccounts.add(account);
		}
		return gridAccounts;
	}

	@Override
	protected boolean onOK() {
		for (int boxNum : boxNums) {
			ClientAccount account = getAccountByBoxNum(boxNum);
			if (account != null) {
				account.setBoxNumber(0);
				saveOrUpdate(account);
			}
		}
		for (int i = 1; i <= boxNums.length; i++) {
			CheckBox checkBox = (CheckBox) flexTable.getWidget(i, 0);
			DynamicForm accountsForm = (DynamicForm) flexTable.getWidget(i, 2);
			AccountCombo accountsCombo = (AccountCombo) accountsForm
					.getField("AccountsCombo");
			ClientAccount selectedValue = accountsCombo.getSelectedValue();
			if (selectedValue.getBoxNumber() == 0) {
				if (checkBox.isChecked() && selectedValue != null) {

					selectedValue.setBoxNumber(boxNums[i - 1]);
					saveOrUpdate(selectedValue);
				}
			} else {
				Accounter
						.showError("Please Select One Account For One Box Only..");
				return false;
			}

		}

		return true;
	}

}
