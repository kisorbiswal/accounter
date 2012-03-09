package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.AddButton;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid.RecordClickHandler;

public class SalesTaxGroupListView extends BaseView<ClientTAXGroup> {

	protected List<ClientTAXGroup> savedSalesTaxGroup;
	protected GroupDialogButtonsHandler groupDialogButtonHandler;
	protected FocusHandler focusChangedHandler;
	private SalesTaxGroupDialog salesTaxGroupDialog;
	protected TaxGroupGrid grid;
	protected SalesTaxItemsGrid itemsGrid;
	private StyledPanel buttonsLayout;
	private StyledPanel bodyLayout;
	protected AddButton button1;
	private Button button2;
	private Button button3;
	private GroupDialogButtonsHandler dialogButtonsHandler;

	public SalesTaxGroupListView() {
		this.getElement().setId("SalesTaxGroupListView");
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		int errorCode = caught.getErrorCode();
		if (errorCode == AccounterException.ERROR_OBJECT_IN_USE) {
			Accounter.showError(AccounterExceptions.accounterMessages
					.taxGroupInUse());
			return;
		}

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		grid.deleteRecord((ClientTAXGroup) result);
		itemsGrid.removeAllRecords();
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		Label lab = new Label(messages.manageSalesTaxGroup());
		lab.addStyleName("lable-title");

		bodyLayout = new StyledPanel("bodyLayout");

		grid = new TaxGroupGrid(false);
		grid.init();
		// grid.setSize("100%", "250px");
		grid.addRecords(getRecords());
		/**
		 * buttons Layout
		 */
		buttonsLayout = new StyledPanel("buttonsLayout");

		button1 = new AddButton(this);
		button1.setWidth("80px");

		button1.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (dialogButtonsHandler != null)
					firstButtonClick();

			}
		});

		button2 = new Button(messages.edit());
		button2.setEnabled(false);
		button2.setWidth("80px");
		button2.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (dialogButtonsHandler != null)
					secondButtonClick();
			}
		});

		button3 = new Button(messages.remove());
		button3.setEnabled(false);
		button3.setWidth("80px");
		button3.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (dialogButtonsHandler != null)
					thirdButtonClick();
			}
		});

		enableEditRemoveButtons(false);

		buttonsLayout.setStyleName("sales_tax_buttons");
		buttonsLayout.add(button1);
		buttonsLayout.add(button2);
		buttonsLayout.add(button3);

		itemsGrid = new SalesTaxItemsGrid(false);
		itemsGrid.addStyleName("itemGrid");
		itemsGrid.init();
		itemsGrid.setWidth("100%");
		// itemsGrid.setSize("100%", "250px");

		button1.setFocus(true);
		bodyLayout.add(grid);
		// bodyLayout.setCellWidth(grid, "90%");
		if (Accounter.getUser().canDoInvoiceTransactions())
			bodyLayout.add(buttonsLayout);
		StyledPanel mainVlay = new StyledPanel("mainVlay");
		mainVlay.add(lab);
		mainVlay.add(bodyLayout);
		mainVlay.add(itemsGrid);
		this.add(mainVlay);

		grid.addRecordClickHandler(new RecordClickHandler<ClientTAXGroup>() {

			@Override
			public boolean onRecordClick(ClientTAXGroup core, int column) {
				enableEditRemoveButtons(true);
				setTaxItemsToGrid(core.getTaxItems());
				return true;
			}
		});

		groupDialogButtonHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {

			}

			public void onFirstButtonClick() {

				showAddEditTaxGroup(null);

			}

			public void onSecondButtonClick() {

				data = (ClientTAXGroup) grid.getSelection();
				if (data != null && data.getID() != 0) {
					showAddEditTaxGroup(data);
					enableEditRemoveButtons(false);
				} else {
					Accounter.showError(messages.selectATaxGroup());
					new Exception();
				}
			}

			public void onThirdButtonClick() {
				ClientTAXGroup taxGroup = (ClientTAXGroup) grid.getSelection();

				if (taxGroup != null) {
					deleteObject(taxGroup);
					enableEditRemoveButtons(false);
				} else
					Accounter.showError(messages.selectATaxGroup());

			}
		};
		addGroupButtonsHandler(groupDialogButtonHandler);
	}

	public void addGroupButtonsHandler(
			GroupDialogButtonsHandler dialogButtonsHandler) {
		this.dialogButtonsHandler = dialogButtonsHandler;
	}

	public void enableEditRemoveButtons(boolean enable) {
		button2.setEnabled(enable);
		button3.setEnabled(enable);
	}

	public void firstButtonClick() {
		dialogButtonsHandler.onFirstButtonClick();
	}

	public void secondButtonClick() {
		dialogButtonsHandler.onSecondButtonClick();
	}

	public void thirdButtonClick() {
		dialogButtonsHandler.onThirdButtonClick();
	}

	protected void closeWindow() {
		if (dialogButtonsHandler != null)
			dialogButtonsHandler.onCloseButtonClick();

	}

	public void showAddEditTaxGroup(final ClientTAXGroup taxGroup) {

		if (taxGroup != null) {
			salesTaxGroupDialog = new SalesTaxGroupDialog(messages.taxGroup(),
					messages.toAddOrRemoveTaxCode(), taxGroup);
		} else {
			salesTaxGroupDialog = new SalesTaxGroupDialog(messages.taxGroup(),
					messages.toAddOrRemoveTaxCode(), null);
		}

		salesTaxGroupDialog.setCallback(new ActionCallback<ClientTAXGroup>() {

			@Override
			public void actionResult(ClientTAXGroup result) {
				grid.removeAllRecords();
				grid.addRecords(getRecords());
			}
		});
		salesTaxGroupDialog.show();

	}

	protected List<ClientTAXGroup> getRecords() {
		return getCompany().getTaxGroups();
	}

	public void setTaxItemsToGrid(List<ClientTAXItem> taxItems) {
		itemsGrid.removeAllRecords();
		itemsGrid.addRecords(taxItems);

	}

	@Override
	protected String getViewTitle() {
		return messages.vatGroupList();
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
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
		return false;
	}
}
