package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.customers.CustomersMessages;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid.RecordClickHandler;

public class SalesTaxGroupListView extends BaseView<ClientTAXGroup> {

	protected List<ClientTAXGroup> savedSalesTaxGroup;
	ClientTAXGroup taxGroup;
	protected GroupDialogButtonsHandler groupDialogButtonHandler;
	protected FocusHandler focusChangedHandler;
	private SalesTaxGroupDialog salesTaxGroupDialog;
	protected TaxGroupGrid grid;
	protected SalesTaxItemsGrid itemsGrid;
	private VerticalPanel buttonsLayout;
	private HorizontalPanel bodyLayout;
	protected Button button1;
	private Button button2;
	private Button button3;
	private GroupDialogButtonsHandler dialogButtonsHandler;
	private InputDialogHandler dialogHandler;
	CustomersMessages customersMessages = GWT.create(CustomersMessages.class);

	public SalesTaxGroupListView() {
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		Label lab = new Label(FinanceApplication.getCompanyMessages()
				.manageSalesTaxGroup());
		lab.addStyleName("lable-title");

		bodyLayout = new HorizontalPanel();
		bodyLayout.setSize("100%", "100%");

		grid = new TaxGroupGrid(false);
		grid.init();
		grid.setSize("100%", "250px");
		grid.addRecords(getRecords());
		/**
		 * buttons Layout
		 */
		buttonsLayout = new VerticalPanel();
		buttonsLayout.setWidth("80");
		buttonsLayout.setSpacing(5);

		button1 = new Button(customersMessages.add());
		button1.setWidth("100%");

		button1.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (dialogButtonsHandler != null)
					firstButtonClick();

			}
		});

		button2 = new Button(customersMessages.edit());
		button2.setEnabled(false);
		button2.setWidth("100%");
		button2.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (dialogButtonsHandler != null)
					secondButtonClick();
			}
		});

		button3 = new Button(customersMessages.remove());
		button3.setEnabled(false);
		button3.setWidth("100%");
		button3.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (dialogButtonsHandler != null)
					thirdButtonClick();
			}
		});

		enableEditRemoveButtons(false);

		buttonsLayout.add(button1);
		buttonsLayout.add(button2);
		buttonsLayout.add(button3);

		itemsGrid = new SalesTaxItemsGrid(false);
		itemsGrid.setStyleName("itemGrid");
		itemsGrid.init();
		itemsGrid.setSize("100%", "100%");

		button1.setFocus(true);
		bodyLayout.add(grid);
		bodyLayout.add(buttonsLayout);
		VerticalPanel mainVlay = new VerticalPanel();
		mainVlay.add(lab);
		mainVlay.add(bodyLayout);
		mainVlay.add(itemsGrid);
		canvas.add(mainVlay);

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

				taxGroup = (ClientTAXGroup) grid.getSelection();
				if (taxGroup != null) {
					showAddEditTaxGroup(taxGroup);
				} else {
					Accounter.showError(FinanceApplication
							.getFinanceUIConstants().selectATaxGroup());
					new Exception();
				}
			}

			public void onThirdButtonClick() {
				ClientTAXGroup taxGroup = (ClientTAXGroup) grid.getSelection();

				if (taxGroup != null) {
					deleteObject(taxGroup);
				} else
					Accounter.showError(FinanceApplication
							.getFinanceUIConstants().selectATaxGroup());

			}
		};
		addGroupButtonsHandler(groupDialogButtonHandler);
		saveAndCloseButton.setVisible(false);
		saveAndNewButton.setVisible(false);
		cancelButton.setVisible(false);

	}

	public void addGroupButtonsHandler(
			GroupDialogButtonsHandler dialogButtonsHandler) {
		this.dialogButtonsHandler = dialogButtonsHandler;
	}

	public void deleteObject(IAccounterCore core) {
		ViewManager.getInstance()
				.deleteObject(core, core.getObjectType(), this);
	}

	public void addInputDialogHandler(InputDialogHandler handler) {
		this.dialogHandler = handler;
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
			salesTaxGroupDialog = new SalesTaxGroupDialog(FinanceApplication
					.getFinanceUIConstants().taxGroup(), FinanceApplication
					.getFinanceUIConstants().toAddOrRemoveTaxCode(), taxGroup);
		} else {
			salesTaxGroupDialog = new SalesTaxGroupDialog(FinanceApplication
					.getFinanceUIConstants().taxGroup(), FinanceApplication
					.getFinanceUIConstants().toAddOrRemoveTaxCode(), null);
		}

		salesTaxGroupDialog.addInputDialogHandler(new InputDialogHandler() {

			public void onCancelClick() {

			}

			public boolean onOkClick() {

				if (taxGroup != null) {
					editTaxGroup(taxGroup);

					return true;
				} else {
					if (salesTaxGroupDialog.taxGroupText.getValue() != null) {
						newTaxGroup();
						return true;
					} else {
						Accounter.showError(FinanceApplication
								.getFinanceUIConstants()
								.pleaseEnterTaxGroupName());
						return false;
					}
				}

			}// onOkClick
		});// InputDialogHandler;
		salesTaxGroupDialog.show();

	}

	protected void editTaxGroup(ClientTAXGroup taxGroup) {
		if (!(taxGroup.getName().equalsIgnoreCase(
				UIUtils.toStr(salesTaxGroupDialog.taxGroupText.getValue())) ? true
				: (Utility.isObjectExist(FinanceApplication.getCompany()
						.getTaxGroups(), UIUtils
						.toStr(salesTaxGroupDialog.taxGroupText.getValue())) ? false
						: true))) {
			Accounter.showError(AccounterErrorType.ALREADYEXIST);
		} else {
			taxGroup.setName(UIUtils.toStr(salesTaxGroupDialog.taxGroupText
					.getValue()));
			taxGroup.setTaxItems(getSelectedTaxItems(taxGroup));
			alterObject(taxGroup);
		}
	}

	private List<ClientTAXItem> getSelectedTaxItems(ClientTAXGroup taxGroup) {
		List<ClientTAXItem> taxItems = new ArrayList<ClientTAXItem>();
		List<IsSerializable> records = salesTaxGroupDialog.selectTaxItemsGrid
				.getRecords();
		ClientTAXItem item;
		for (IsSerializable rec : records) {
			ClientTAXItem clientTaxItem = (ClientTAXItem) rec;
			item = getTaxItemByName(clientTaxItem.getName());
			if (item != null) {
				taxItems.add(item);
				taxGroup.setGroupRate(taxGroup.getGroupRate()
						+ item.getTaxRate());

			}// if
		}// for
		return taxItems;
	}

	private ClientTAXItem getTaxItemByName(String attribute) {

		for (ClientTAXItem item : salesTaxGroupDialog.getAllTaxItem()) {
			if (item.getName() != null && item.getName().equals(attribute)) {
				return item;
			}
		}
		return null;
	}

	protected void newTaxGroup() {

		ClientTAXGroup taxGroup = new ClientTAXGroup();
		taxGroup.setName(UIUtils.toStr(salesTaxGroupDialog.taxGroupText
				.getValue()));
		taxGroup.setActive(true);
		taxGroup.setPercentage(true);
		taxGroup.setSalesType(true);
		taxGroup.setTaxItems(getSelectedTaxItems(taxGroup));

		if (Utility.isObjectExist(
				FinanceApplication.getCompany().getTaxItems(), taxGroup
						.getName())
				|| Utility.isObjectExist(FinanceApplication.getCompany()
						.getTaxGroups(), taxGroup.getName())) {

			Accounter.showError(AccounterErrorType.ALREADYEXIST);
		} else
			createObject(taxGroup);

	}

	protected List<ClientTAXGroup> getRecords() {
		return FinanceApplication.getCompany().getTaxGroups();
	}

	public void setTaxItemsToGrid(List<ClientTAXItem> taxItems) {
		itemsGrid.removeAllRecords();
		itemsGrid.addRecords(taxItems);

	}

}
