package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientPayTAX;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXReturn;
import com.vimukti.accounter.web.client.core.ClientTransactionPayTAX;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class TaxHistoryView extends BaseView<ClientTAXReturn> {

	SelectCombo optionsCombo;
	TAXHistoryGrid grid;
	ClientTAXReturn clientVATReturn;
	StyledPanel gridLayout;
	List<ClientTAXReturn> clientAbstractTAXReturns;

	@Override
	public void init() {
		setMode(EditMode.EDIT);
		super.init();
		initListGrid();
		createControls();
	}

	private void createControls() {

		Label label = new Label();
		label.removeStyleName("gwt-style");
		label.setWidth("100%");
		label.addStyleName("label-title");
		label.setText(messages.taxHistory());
		this.optionsCombo = new SelectCombo(messages.taxFillings());
		optionsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						filterList(selectItem);
						hideButtons();
					}

				});
		initComboItems();
		// this.grid.setWidth("100%");
		DynamicForm form2 = new DynamicForm("form2");

		form2.add(optionsCombo);
		StyledPanel mainPanel = new StyledPanel("mainPanel");
		mainPanel.add(label);
		mainPanel.add(form2);
		mainPanel.add(gridLayout);

		// grid.getElement().getParentElement()
		// .addClassName("recounciliation_grid");
		setData();
		this.add(mainPanel);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setVisible(false);
		if (saveAndNewButton != null) {
			saveAndNewButton.setVisible(!grid.getRecords().isEmpty());
			saveAndNewButton.setText(messages.payTax());
		}
		deleteButton.setVisible(false);

		deleteButton.addDeleteHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				final ClientTAXReturn selection = grid.getSelection();
				if (selection == null || !canDelete(selection)) {
					return;
				}
				String warning = messages.taxReturnDeleteWarning(
						DateUtills.getDateAsString(selection
								.getPeriodStartDate()),
						DateUtills.getDateAsString(selection.getPeriodEndDate()));
				Accounter.showWarning(warning, AccounterType.WARNING,
						new ErrorDialogHandler() {

							@Override
							public boolean onYesClick() {
								ClientTAXReturn selection2 = selection;
								Accounter.deleteObject(TaxHistoryView.this,
										selection2);
								return true;
							}

							@Override
							public boolean onNoClick() {
								return true;
							}

							@Override
							public boolean onCancelClick() {
								return false;
							}
						});
			}
		});
	}

	protected void hideButtons() {
		if (deleteButton != null) {
			deleteButton.setVisible(false);
		}
		if (saveAndNewButton != null) {
			saveAndNewButton.setVisible(false);
		}

	}

	@Override
	public void onSave(boolean reopen) {

		ClientTAXReturn taxReturn = grid.getSelection();

		List<ClientTransactionPayTAX> payTaxEntriesList = new ArrayList<ClientTransactionPayTAX>();
		ClientTransactionPayTAX payTAXEntry = null;

		payTAXEntry = new ClientTransactionPayTAX();
		if (taxReturn != null) {
			if (taxReturn.getBalance() >= 0) {
				payTAXEntry.setTaxDue(taxReturn.getBalance());
			} else {
				payTAXEntry.setAmountToPay(taxReturn.getBalance());
			}
			payTAXEntry.setTaxAgency(taxReturn.getTAXAgency());
			payTAXEntry.setTAXReturn(taxReturn.getID());
			payTAXEntry.setFiledDate(taxReturn.getDate());
			payTaxEntriesList.add(payTAXEntry);

			ClientPayTAX clientPayTAX = new ClientPayTAX();
			clientPayTAX.setTransactionPayTax(payTaxEntriesList);

			ActionFactory.getpayTAXAction().run(clientPayTAX, true);
		}
	}

	private void initComboItems() {
		List<String> options = new ArrayList<String>();
		options.add(new String(messages.all()));
		options.add(new String(messages.paid()));
		options.add(new String(messages.unPaid()));
		optionsCombo.initCombo(options);
		optionsCombo.setSelectedItem(0);

	}

	private void initListGrid() {

		gridLayout = new StyledPanel("gridLayout");
		grid = new TAXHistoryGrid(this, false);
		grid.setCanEdit(!isInViewMode());
		grid.isEnable = false;
		grid.init();
		grid.setEnabled(!isInViewMode());

		gridLayout.add(grid);

	}

	private void setData() {
		grid.clear();
		grid.addLoadingImagePanel();
		rpcGetService
				.getAllTAXReturns(new AccounterAsyncCallback<List<ClientTAXReturn>>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onResultSuccess(List<ClientTAXReturn> result) {
						grid.removeLoadingImage();
						if (result == null) {

							return;
						}
						clientAbstractTAXReturns = result;
						for (ClientTAXReturn a : result) {
							grid.addData(a);
						}
					}
				});

	}

	private void filterList(String selectItem) {
		grid.showLoadingImage();
		grid.removeAllRecords();
		if (selectItem.equals(messages.paid())) {
			for (ClientTAXReturn a : clientAbstractTAXReturns) {
				if (a.getBalance() <= 0) {
					this.grid.addData(a);
				}
			}
		} else if (selectItem.equals(messages.unPaid())) {
			for (ClientTAXReturn a : clientAbstractTAXReturns) {
				if (a.getBalance() > 0) {
					this.grid.addData(a);
				}
			}
		} else {
			for (ClientTAXReturn a : clientAbstractTAXReturns) {
				this.grid.addData(a);
			}
		}
		grid.removeLoadingImage();
		if (grid.getRecords().isEmpty()) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
		saveAndNewButton.setVisible(!grid.getRecords().isEmpty());
	}

	@Override
	protected String getViewTitle() {
		return messages.taxHistory();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		int errorCode = caught.getErrorCode();
		if (errorCode == AccounterException.ERROR_OBJECT_IN_USE) {
			Accounter.showError(messages.filedTAXReturnHasBeenPaid());
		} else {
			Accounter.showError(AccounterExceptions.getErrorString(errorCode));
		}
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		clientAbstractTAXReturns.remove(result);
		grid.removeRow(grid.getSelectedRecordIndex());
		if (grid.getRowCount() > 0) {
			deleteButton.setVisible(true);
		} else {
			deleteButton.setVisible(false);
		}
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected boolean canDelete() {
		return true;
	}

	public void taxReturnSelected(ClientTAXReturn obj) {
		if (obj == null) {
			deleteButton.setVisible(false);
			return;
		}
		if (obj.getBalance() > 0) {
			saveAndNewButton.setVisible(true);
		} else {
			saveAndNewButton.setVisible(false);
		}
		deleteButton.setVisible(canDelete(obj));
	}

	private boolean canDelete(ClientTAXReturn taxReturn) {
		ClientTAXAgency taxAgency = getCompany().getTaxAgency(
				taxReturn.getTAXAgency());
		return taxAgency.getLastTAXReturnDate() == null
				|| taxReturn.getPeriodEndDate() == taxAgency
						.getLastTAXReturnDate().getDate();
	}
}
