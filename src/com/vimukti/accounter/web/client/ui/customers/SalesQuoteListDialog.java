package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.externalization.FinanceConstants;
import com.vimukti.accounter.web.client.ui.AbstractBaseDialog;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.RecordDoubleClickHandler;

@SuppressWarnings("unchecked")
public class SalesQuoteListDialog extends AbstractBaseDialog {

	private SalesOrderView salesView;
	public DialogGrid grid;
	private List<ClientEstimate> estimates;
	private ClientEstimate selectedEstimate;
	private CustomersMessages customerConstants = GWT
			.create(CustomersMessages.class);
	private FinanceConstants financeConstants = GWT
			.create(FinanceConstants.class);

	public SalesQuoteListDialog(SalesOrderView parentView,
			List<ClientEstimate> estimates) {
		super(parentView);
		salesView = parentView;
		this.estimates = estimates;
		setText(Accounter.getCustomersMessages().quoteList());
		createControl();
		setWidth("600");
		setQuoteList(estimates);
		show();
		center();
	}

	private void createControl() {

		VerticalPanel mainLayout = new VerticalPanel();
		mainLayout.setSize("100%", "100%");
		mainLayout.setSpacing(3);
		Label infoLabel = new Label(Accounter.getCustomersMessages()
				.selectQuote());

		mainLayout.add(infoLabel);

		grid = new DialogGrid(false);
		grid.addColumns(customerConstants.date(), customerConstants.no(),
				customerConstants.type(), customerConstants.customeRName(),
				customerConstants.total());
		// grid.setWidth("100%");
		grid.setCellsWidth(70, 30, 60, -1, 60);
		grid.setView(this);
		grid.init();

		grid
				.addRecordDoubleClickHandler(new RecordDoubleClickHandler<ClientEstimate>() {

					@Override
					public void OnCellDoubleClick(ClientEstimate core,
							int column) {
						try {
							ClientEstimate record = (ClientEstimate) core;

							String estimateId = record.getID();
							selectedEstimate = getEstimate(estimateId);

							if (salesView != null && selectedEstimate != null)
								salesView.selectedQuote(selectedEstimate);

							removeFromParent();

						} catch (Exception e) {
							Accounter
									.showError(Accounter
											.getCustomersMessages()
											.errorLoadingQuote());
						}

					}
				});

		// getGridData();
		setQuoteList(estimates);

		mainLayout.add(grid);

		HorizontalPanel helpButtonLayout = new HorizontalPanel();

		AccounterButton helpButton = new AccounterButton(financeConstants
				.help());
		helpButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Accounter.showError(Accounter.getCustomersMessages()
						.sorryNoHelp());

			}

		});
		helpButtonLayout.add(helpButton);

		helpButton.enabledButton();

		HorizontalPanel okButtonLayout = new HorizontalPanel();
		okButtonLayout.setSpacing(3);
		AccounterButton okButton = new AccounterButton(financeConstants.ok());
		okButton.setWidth("100px");
		okButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				try {
					ClientEstimate selectedEstimate = (ClientEstimate) grid
							.getSelection();
					if (salesView != null && selectedEstimate != null)
						salesView.selectedQuote(selectedEstimate);
					removeFromParent();

				} catch (Exception e) {
					Accounter.showError(Accounter
							.getCustomersMessages().errorLoadingQuote());
				}

			}

		});
		okButtonLayout.add(okButton);

		okButton.enabledButton();
		AccounterButton cancelButton = new AccounterButton(financeConstants
				.cancel());
		cancelButton.setWidth("100px");
		cancelButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				removeFromParent();
			}

		});
		okButtonLayout.add(cancelButton);
		cancelButton.enabledButton();
		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.setWidth("100%");
		// buttonLayout.add(helpButtonLayout);
		buttonLayout.add(okButtonLayout);
		buttonLayout.setCellHorizontalAlignment(okButtonLayout,
				HasHorizontalAlignment.ALIGN_RIGHT);
		mainLayout.add(buttonLayout);
		buttonLayout.setCellHorizontalAlignment(okButtonLayout,
				HasHorizontalAlignment.ALIGN_RIGHT);
		mainLayout.setSize("100%", "100%");

		add(mainLayout);
	}

	protected ClientEstimate getEstimate(String estimateId) {
		for (ClientEstimate estimate : estimates) {
			if (estimate != null) {
				if (estimate.getID().equals(estimateId))
					return estimate;
			}
		}
		return null;
	}

	public void setQuoteList(List<ClientEstimate> result) {
		if (result == null)
			return;
		this.estimates = result;
		grid.removeAllRecords();
		for (ClientEstimate rec : estimates) {
			grid.addData(rec);
		}
	}

	public Object getGridColumnValue(IsSerializable obj, int index) {
		ClientEstimate estimate = (ClientEstimate) obj;
		if (estimate != null) {
			switch (index) {
			case 0:
				return UIUtils.dateFormat(estimate.getDate());
			case 1:
				return estimate.getNumber();
			case 2:
				return Utility.getTransactionName(estimate.getType());
			case 3:
				return company.getCustomer(estimate.getCustomer()).getName();
			case 4:
				return DataUtils.getAmountAsString(estimate.getTotal());
			}
		}
		return null;

	}

	public void setFocus() {
		// cancelBtn.setFocus(true);
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}
	// setTitle(customerConstants.createForm());
}
