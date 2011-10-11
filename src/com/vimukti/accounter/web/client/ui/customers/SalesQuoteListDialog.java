package com.vimukti.accounter.web.client.ui.customers;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.RecordDoubleClickHandler;

public class SalesQuoteListDialog extends BaseDialog {

	private SalesOrderView salesView;
	public DialogGrid grid;
	private List<ClientEstimate> estimates;
	private ClientEstimate selectedEstimate;
	private AccounterConstants financeConstants = GWT
			.create(AccounterConstants.class);

	public SalesQuoteListDialog(SalesOrderView parentView,
			List<ClientEstimate> estimates) {
		super(Accounter.constants().quoteList());
		salesView = parentView;
		this.estimates = estimates;
		createControl();
		// setWidth("600px");
		setQuoteList(estimates);
		show();
		center();
	}

	private void createControl() {

		VerticalPanel mainLayout = new VerticalPanel();
		mainLayout.setSize("100%", "100%");
		mainLayout.setSpacing(3);
		Label infoLabel = new Label(Accounter.constants().selectQuote());

		mainLayout.add(infoLabel);

		grid = new DialogGrid(false);
		grid.addColumns(constants.date(), constants.no(), constants.type(),
				messages.customerName(Global.get().Customer()), constants
						.total());
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

							long estimateId = record.getID();
							selectedEstimate = getEstimate(estimateId);

							if (salesView != null && selectedEstimate != null)
								salesView.selectedQuote(selectedEstimate);

							removeFromParent();

						} catch (Exception e) {
							Accounter.showError(Accounter.constants()
									.errorLoadingQuote());
						}

					}
				});

		// getGridData();
		setQuoteList(estimates);

		mainLayout.add(grid);

		HorizontalPanel helpButtonLayout = new HorizontalPanel();

		Button helpButton = new Button(financeConstants.help());
		helpButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Accounter.showError(Accounter.constants().sorryNoHelp());

			}

		});
		helpButtonLayout.add(helpButton);

		HorizontalPanel okButtonLayout = new HorizontalPanel();
		okButtonLayout.setSpacing(3);
		Button okButton = new Button(financeConstants.ok());
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
					Accounter.showError(Accounter.constants()
							.errorLoadingQuote());
				}

			}

		});
		okButtonLayout.add(okButton);

		Button cancelButton = new Button(financeConstants.cancel());
		cancelButton.setWidth("100px");
		cancelButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				removeFromParent();
			}

		});
		okButtonLayout.add(cancelButton);
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

	protected ClientEstimate getEstimate(long estimateId) {
		for (ClientEstimate estimate : estimates) {
			if (estimate != null) {
				if (estimate.getID() == estimateId)
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

	public Object getGridColumnValue(IAccounterCore obj, int index) {
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
				return amountAsString(estimate.getTotal());
			}
		}
		return null;

	}

	public void setFocus() {

	}

	// setTitle(customerConstants.createForm());

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return false;
	}
}
