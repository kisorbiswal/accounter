package com.vimukti.accounter.web.client.uibinder.setup;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.TemplateAccount;
import com.vimukti.accounter.web.client.ui.Accounter;

public class SetupSelectAccountsPage extends AbstractSetupPage {

	private static SetupReviewExpenseAccountsUiBinder uiBinder = GWT
			.create(SetupReviewExpenseAccountsUiBinder.class);

	interface SetupReviewExpenseAccountsUiBinder extends
			UiBinder<Widget, SetupSelectAccountsPage> {
	}

	private SetupWizard setupWizard;

	public SetupSelectAccountsPage(SetupWizard setupWizard) {
		initWidget(uiBinder.createAndBindUi(this));
		this.setupWizard = setupWizard;
		createControls();
	}

	@UiField
	VerticalPanel viewPanel;
	@UiField
	Label expensesInfo;
	@UiField
	HTML recommendedInfo;
	@UiField
	Button restoreButton;
	@UiField
	HTML expensesLink;
	@UiField
	HTML expensesNote;
	@UiField
	Label headerLabel;
	@UiField
	FlexTable accountsTable;

	private List<TemplateAccount> selectedAccounts = new ArrayList<TemplateAccount>();
	private int selectedIndusty;

	public SetupSelectAccountsPage(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	@Override
	protected void createControls() {
		headerLabel.setText(Accounter.messages()
				.reviewIncomeAndExpensesAccounts(Global.get().account()));

		expensesInfo.setText(accounterConstants.doyouWantToUseStatements());
		recommendedInfo.setHTML(accounterMessages.recommendedAccounts());
		restoreButton.setText(accounterConstants.restoreRecommendations());
		expensesLink.setHTML(accounterMessages.whyshoudIUseRecommended());
		expensesNote.setHTML(accounterMessages.recommendedNote());

		restoreButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// onLoad();
				List<TemplateAccount> accounts = setupWizard
						.getIndustryDefaultAccounts();
				selectedAccounts.clear();
				for (int index = 0; index < accounts.size(); index++) {
					TemplateAccount account = accounts.get(index);
					CheckBox checkBox = (CheckBox) accountsTable.getWidget(
							index, 0);
					checkBox.setValue(account.getDefaultValue());
					if (account.getDefaultValue()) {
						selectedAccounts.add(account);
					}
				}
			}
		});

		accountsTable.setCellSpacing(5);
		CellTable<TemplateAccount> table = new CellTable<TemplateAccount>();

		Column<TemplateAccount, Boolean> checkBoxColumn = new Column<TemplateAccount, Boolean>(
				new CheckboxCell()) {

			@Override
			public Boolean getValue(TemplateAccount object) {
				return object.getDefaultValue();
			}
		};

		TextColumn<TemplateAccount> accountNameColumn = new TextColumn<TemplateAccount>() {

			@Override
			public String getValue(TemplateAccount object) {
				return object.getName();
			}
		};

		TextColumn<TemplateAccount> accountTypeColumn = new TextColumn<TemplateAccount>() {

			@Override
			public String getValue(TemplateAccount object) {
				return object.getType();
			}
		};

		table.addColumn(checkBoxColumn);
		// table.addColumn(accountNameColumn, accounterConstants.accountName());
		// table.addColumn(accountTypeColumn, accounterConstants.accountType());

		// this.accountsTable.add(table);

	}

	/**
	 * @param account
	 * @return
	 */
	private CheckBox createCheckBox(final TemplateAccount account) {
		CheckBox checkBox = new CheckBox();
		checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				Boolean isChecked = event.getValue();
				if (isChecked) {
					selectedAccounts.add(account);
				} else {
					selectedAccounts.remove(account);
				}
			}
		});
		// checkBox.setEnabled(!account.isSystemOnly());
		return checkBox;
	}

	@Override
	protected void onLoad() {

		if (this.selectedIndusty != preferences.getIndustryType()) {

			List<TemplateAccount> accounts = setupWizard
					.getIndustryDefaultAccounts();
			accountsTable.clear();
			accountsTable.removeAllRows();
			selectedAccounts.clear();
			for (int index = 0; index < accounts.size(); index++) {
				TemplateAccount account = accounts.get(index);
				CheckBox checkBox = createCheckBox(account);
				accountsTable.setWidget(index, 0, checkBox);
				accountsTable.setText(index, 1, account.getName());
				accountsTable.setText(index, 2, account.getType());
				if (account.getDefaultValue()) {
					checkBox.setValue(true);
					selectedAccounts.add(account);
				}
			}
		}
		this.selectedIndusty = preferences.getIndustryType();

	}

	@Override
	protected void onSave() {
		this.setupWizard.setSelectedAccountsList(this.selectedAccounts);
	}

	@Override
	public boolean canShow() {
		return true;
	}

	@Override
	protected boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}

}
