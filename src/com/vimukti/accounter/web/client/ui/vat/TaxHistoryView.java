package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientVATReturn;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.company.PaySalesTaxAction;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.SelectionChangedHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class TaxHistoryView extends BaseView<ClientVATReturn>

{
	SelectCombo optionsCombo;
	TaxHistoryTable grid;
	ClientVATReturn clientVATReturn;

	@Override
	public void init() {
		super.init();
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

		this.grid = new TaxHistoryTable(
				new SelectionChangedHandler<ClientVATReturn>() {

					@Override
					public void selectionChanged(ClientVATReturn obj,
							boolean isSelected) {
						clientVATReturn = obj;

					}
				});
		this.grid.setWidth("100%");
		DynamicForm form2 = new DynamicForm();

		form2.setFields(optionsCombo);
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.add(label);
		mainPanel.add(form2);
		mainPanel.add(grid);

		grid.getElement().getParentElement()
				.addClassName("recounciliation_grid");
		setData();
		this.add(mainPanel);
		saveAndCloseButton.setVisible(false);
		saveAndNewButton.setText(Accounter.constants().payTax());
		saveAndNewButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {

				PaySalesTaxAction paySalesTaxAction = ActionFactory
						.getPaySalesTaxAction();
				paySalesTaxAction.run(clientVATReturn, true);

			}
		});

	}

	private void initComboItems() {
		List<String> options = new ArrayList<String>();
		options.add(new String("All"));
		options.add(new String("Paid"));
		options.add(new String("UnPaid"));
		optionsCombo.initCombo(options);
		optionsCombo.setSelectedItem(0);

	}

	private void setData() {
		ArrayList<ClientVATReturn> vatReturns = getCompany().getVatReturns();
		if (!vatReturns.isEmpty()) {
			this.grid.setData(vatReturns);
		}
	}

	private void filterList(String selectItem) {
		// TODO Auto-generated method stub

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
