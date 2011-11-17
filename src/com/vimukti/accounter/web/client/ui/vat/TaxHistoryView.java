package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientPayTAX;
import com.vimukti.accounter.web.client.core.ClientTAXReturn;
import com.vimukti.accounter.web.client.core.ClientTransactionPayTAX;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class TaxHistoryView extends BaseView<ClientTAXReturn> {

	SelectCombo optionsCombo;
	TAXHistoryGrid grid;
	ClientTAXReturn clientVATReturn;
	VerticalPanel gridLayout;
	List<ClientTAXReturn> clientAbstractTAXReturns;

	@Override
	public void init() {
		super.init();
		initListGrid();
		createControls();

	}

	private void createControls() {

		Label label = new Label();
		label.removeStyleName("gwt-style");
		label.setWidth("100%");
		label.addStyleName(Accounter.constants().labelTitle());
		label.setText(Accounter.constants().taxHistory());
		this.optionsCombo = new SelectCombo("Vat Filngs");
		optionsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						filterList(selectItem);

					}

				});
		initComboItems();

		;
		// this.grid.setWidth("100%");
		DynamicForm form2 = new DynamicForm();

		form2.setFields(optionsCombo);
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.add(label);
		mainPanel.add(form2);
		mainPanel.add(gridLayout);

		// grid.getElement().getParentElement()
		// .addClassName("recounciliation_grid");
		setData();
		this.add(mainPanel);
		saveAndCloseButton.setVisible(false);
		saveAndNewButton.setVisible(true);
		saveAndNewButton.setText(Accounter.constants().payTax());
	}

	@Override
	public void onSave(boolean reopen) {

		ClientTAXReturn taxReturn = grid.getSelection();

		List<ClientTransactionPayTAX> payTaxEntriesList = new ArrayList<ClientTransactionPayTAX>();
		ClientTransactionPayTAX payTAXEntry = null;

		payTAXEntry = new ClientTransactionPayTAX();
		if (taxReturn.getBalance() >= 0) {
			payTAXEntry.setTaxDue(taxReturn.getBalance());
		} else {
			payTAXEntry.setAmountToPay(taxReturn.getBalance());
		}
		payTAXEntry.setTaxAgency(taxReturn.getTAXAgency());
		payTAXEntry.setVatReturn(taxReturn.getID());
		payTaxEntriesList.add(payTAXEntry);

		ClientPayTAX clientPayTAX = new ClientPayTAX();
		clientPayTAX.setTransactionPayTax(payTaxEntriesList);

		ActionFactory.getpayTAXAction().run(clientPayTAX, true);
	}

	private void initComboItems() {
		List<String> options = new ArrayList<String>();
		options.add(new String(Accounter.constants().all()));
		options.add(new String(Accounter.constants().paid()));
		options.add(new String(Accounter.constants().unPaid()));
		optionsCombo.initCombo(options);
		optionsCombo.setSelectedItem(0);

	}

	private void initListGrid() {

		gridLayout = new VerticalPanel();
		gridLayout.setWidth("100%");
		grid = new TAXHistoryGrid(false);
		grid.setCanEdit(!isInViewMode());
		grid.isEnable = false;
		grid.init();
		grid.setTaxHistoryView(this);
		grid.setDisabled(isInViewMode());

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
		if (selectItem.equals(constants.paid())) {
			for (ClientTAXReturn a : clientAbstractTAXReturns) {
				if (a.getBalance() <= 0) {
					this.grid.addData(a);
				}
			}
		} else if (selectItem.equals(constants.unPaid())) {
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
			grid.addEmptyMessage(constants.noRecordsToShow());
		}
	}

	@Override
	protected String getViewTitle() {
		return constants.taxHistory();
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
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
