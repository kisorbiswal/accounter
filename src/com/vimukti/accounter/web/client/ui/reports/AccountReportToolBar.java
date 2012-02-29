package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.forms.DateItem;

public class AccountReportToolBar extends ReportToolbar {

	private DateItem fromItem;
	private DateItem toItem;
	private AccountCombo accountCombo;
	private Button updateButton;
	private ClientAccount selectAccount = null;

	public AccountReportToolBar() {
		createControls();
	}

	public void createControls() {
		accountCombo = new AccountCombo(messages.Account(), false) {

			@Override
			protected List<ClientAccount> getAccounts() {
				List<ClientAccount> list = new ArrayList<ClientAccount>();
				list.addAll(getCompany().getAccounts(ClientAccount.TYPE_BANK));
				list.addAll(getCompany().getAccounts(
						ClientAccount.TYPE_ACCOUNT_RECEIVABLE));
				list.addAll(getCompany().getAccounts(
						ClientAccount.TYPE_FIXED_ASSET));
				list.addAll(getCompany().getAccounts(
						ClientAccount.TYPE_OTHER_CURRENT_ASSET));
				list.addAll(getCompany().getAccounts(
						ClientAccount.TYPE_OTHER_ASSET));
				return list;
			}
		};
		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						setAccId(selectItem.getID());
					}
				});

		fromItem = new DateItem();
		fromItem.setTitle(messages.from());

		toItem = new DateItem();
		toItem.setTitle(messages.to());
		toItem.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				startDate = fromItem.getValue();
				endDate = toItem.getValue();
			}
		});
		updateButton = new Button(messages.update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (accountCombo.getSelectedValue() == null) {
					Accounter.showError("Please select account");
				} else {
					setStartDate(fromItem.getDate());
					setEndDate(toItem.getDate());
					setSelectedDateRange(messages.custom());
					reportview.makeReportRequest(fromItem.getDate(),
							toItem.getDate());
				}
			}
		});

		addItems(accountCombo, fromItem, toItem);
		add(updateButton);
		this.setCellVerticalAlignment(updateButton,
				HasVerticalAlignment.ALIGN_MIDDLE);
	}

	@Override
	public void changeDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		fromItem.setValue(startDate);
		toItem.setValue(endDate);
		if (selectAccount != null) {
			reportview.makeReportRequest(selectAccount.getID(), startDate,
					endDate);
		} else {
			reportview.addEmptyMessage(messages.noRecordsToShow());
		}
	}

	@Override
	public void setStartAndEndDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		if (startDate != null && endDate != null) {
			fromItem.setEnteredDate(startDate);
			toItem.setEnteredDate(endDate);
			setStartDate(startDate);
			setEndDate(endDate);
		}
	}

	@Override
	public void setDefaultDateRange(String defaultDateRange) {
		dateRangeChanged(defaultDateRange);
	}

	@Override
	protected void accountData() {
		if (getAccId() != 0) {
			accData(Accounter.getCompany().getAccount(getAccId()));
		}
	}

	protected void accData(ClientAccount selectItem) {
		if (selectItem != null) {
			selectAccount = selectItem;
			ClientFinanceDate startDate = fromItem.getDate();
			ClientFinanceDate endDate = toItem.getDate();
			reportview.makeReportRequest(selectAccount.getID(), startDate,
					endDate);
			reportview.removeEmptyStyle();
			accountCombo.setSelected(selectAccount.getName());
		}
	}
}
