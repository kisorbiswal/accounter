/**
 * 
 */
package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ReconciliationDailog;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.SelectionChangedHandler;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ReconciliationView extends BaseView<ClientReconciliation> {

	private VerticalPanel mainPanel;
	private SelectCombo bankAccountsField;
	private DateField startDate;
	private DateField endDate;
	private ReconciliationTransactionsGrid grid;
	private ClientReconciliation reconcilition;
	private Set<ClientTransaction> clearedTransactions = new HashSet<ClientTransaction>();
	private LabelItem bankaccountLabel, startdateLable, enddateLable;
	AmountLabel closebalanceLable, openingBalance, closingBalance,
			clearedAmount, difference;

	/**
	 * Creates new Instance
	 */
	public ReconciliationView(ClientReconciliation data) {
		this.reconcilition = data;
	}

	public void createControls() {
		// Creating Title for View
		Label label = new Label();
		label.removeStyleName("gwt-Label");
		label.addStyleName(Accounter.constants().labelTitle());
		label.setText(constants.Reconciliation());

		bankaccountLabel = new LabelItem();
		bankaccountLabel.setTitle(messages.bankAccount(Global.get().Account()));
		bankaccountLabel.setValue(reconcilition.getAccount().getName());

		closebalanceLable = new AmountLabel(constants.ClosingBalance());
		// closebalanceLable.setTitle(constants.ClosingBalance());
		closebalanceLable.setAmount(reconcilition.getClosingBalance());

		startdateLable = new LabelItem();
		startdateLable.setTitle(constants.startDate());
		startdateLable.setValue(DateUtills.getDateAsString(reconcilition
				.getStartDate().getDateAsObject()));

		enddateLable = new LabelItem();
		enddateLable.setTitle(constants.endDate());
		enddateLable.setValue(DateUtills.getDateAsString(reconcilition
				.getEndDate().getDateAsObject()));

		// // Creating Input Fields to Start Reconciliation of an Account
		// bankAccountsField = new SelectCombo(messages.bankAccount(Global.get()
		// .Account()));
		//
		// bankAccountsField
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<String>() {
		//
		// @Override
		// public void selectedComboBoxItem(String selectItem) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		//
		// startDate = new DateField(constants.startDate());
		// endDate = new DateField(constants.endDate());
		// closingBalance = new TextItem(constants.ClosingBalance());

		DynamicForm form = new DynamicForm();
		form.setFields(bankaccountLabel, closebalanceLable);
		form.setStyleName("recouncilation_value");

		DynamicForm datesForm = new DynamicForm();
		datesForm.setFields(startdateLable, enddateLable);

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setWidth("100%");
		hPanel.add(form);
		hPanel.add(datesForm);
		hPanel.setCellWidth(form, "40%");
		Button startBtn = new Button(constants.change());
		startBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ReconciliationDailog dialog = new ReconciliationDailog(Global
						.get().constants().Reconciliation(),
						(ClientReconciliation) reconcilition);
				dialog.show();
			}
		});

		HorizontalPanel btnPanel = new HorizontalPanel();
		btnPanel.setWidth("100%");
		btnPanel.add(startBtn);
		btnPanel.setStyleName("recoucilation_change");
		btnPanel.setCellHorizontalAlignment(startBtn,
				HasHorizontalAlignment.ALIGN_RIGHT);

		// Creating Amount Fields
		VerticalPanel amountsPanel = new VerticalPanel();
		amountsPanel.setWidth("100%");

		openingBalance = new AmountLabel(constants.openingBalance());
		openingBalance.setHelpInformation(true);
		openingBalance.setDisabled(true);
		openingBalance.setAmount(reconcilition.getOpeningBalance());

		closingBalance = new AmountLabel(constants.ClosingBalance());
		openingBalance.setHelpInformation(true);
		openingBalance.setDisabled(true);
		closingBalance.setAmount(reconcilition.getClosingBalance());

		clearedAmount = new AmountLabel(constants.ClearedAmount());
		openingBalance.setHelpInformation(true);
		openingBalance.setDisabled(true);

		difference = new AmountLabel(constants.Difference());
		openingBalance.setHelpInformation(true);
		openingBalance.setDisabled(true);
		DynamicForm amountsForm = new DynamicForm();
		amountsForm.setWidth("50%");
		amountsForm.setItems(openingBalance, closingBalance, clearedAmount,
				difference);
		amountsPanel.add(amountsForm);
		amountsPanel.setCellHorizontalAlignment(amountsForm,
				HasHorizontalAlignment.ALIGN_RIGHT);

		this.grid = new ReconciliationTransactionsGrid(
				new SelectionChangedHandler<ClientTransaction>() {

					@Override
					public void selectionChanged(ClientTransaction obj,
							boolean isSelected) {
						clearTransaction(obj, isSelected);
					}
				});
		grid.setWidth("100%");
		grid.setHeight("100px");

		this.mainPanel = new VerticalPanel();
		mainPanel.setSize("100%", "300px");
		mainPanel.add(label);
		mainPanel.add(hPanel);
		mainPanel.add(btnPanel);
		mainPanel.add(grid);
		mainPanel.add(amountsPanel);

		this.add(mainPanel);
	}

	/**
	 * @param value
	 */
	protected void clearTransaction(ClientTransaction value, boolean isClear) {
		if (isClear) {
			clearedTransactions.add(value);
		} else {
			clearedTransactions.remove(value);
		}
		double transactionAmount = UIUtils.isMoneyOut(value) ? value.getTotal()
				* -1 : value.getTotal();
		if (!isClear) {
			transactionAmount *= -1;
		}

		updateAmounts(transactionAmount);
	}

	private void updateAmounts(double changedAmount) {
		clearedAmount.setAmount(clearedAmount.getAmount() + changedAmount);
		difference.setAmount(closingBalance.getAmount()
				- clearedAmount.getAmount());
	}

	/**
	 * Gets All the Transaction in between StartDate and EndDate
	 */

	private void getTransactions() {
		rpcGetService.getAllTransactionsOfAccount(reconcilition.getAccount()
				.getID(), reconcilition.getStartDate(), reconcilition
				.getEndDate(),
				new AccounterAsyncCallback<List<ClientTransaction>>() {

					@Override
					public void onException(AccounterException exception) {
						Accounter.showError(messages.unableToGet(constants
								.transaction()));
					}

					@Override
					public void onResultSuccess(List<ClientTransaction> result) {
						List<ClientTransaction> list = new ArrayList<ClientTransaction>();
						for (ClientTransaction clientTransaction : result) {

							if (UIUtils.isMoneyOut(clientTransaction)
									|| UIUtils.isMoneyIn(clientTransaction)) {
								list.add(clientTransaction);
							}
						}
						grid.setData(list);
					}
				});
	}

	@Override
	public void init() {
		super.init();
		setMode(EditMode.CREATE);
		createControls();
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (difference.getAmount() != 0.0D) {
			result.addError(difference, constants.differenceValidate());
		}
		return result;
	}

	@Override
	public void saveAndUpdateView() {
		ClientReconciliation reconciliation = new ClientReconciliation();
		reconciliation.setClosingBalance(reconcilition.getClosingBalance());
		reconciliation.setOpeningBalance(reconciliation.getOpeningBalance());
		reconciliation.setStartDate(reconcilition.getStartDate());
		reconciliation.setEndDate(reconcilition.getEndDate());
		reconciliation.setReconcilationDate(new ClientFinanceDate());
		reconciliation.setAccount(reconcilition.getAccount());
		saveOrUpdate(reconciliation);
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub
		super.onEdit();
	}

	@Override
	public void initData() {
		getTransactions();
		updateAmounts(0);
		super.initData();
	}
}
