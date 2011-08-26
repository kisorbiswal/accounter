package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AddButton;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.AbstractTransactionGrid.RecordClickHandler;

public class SalesTaxGroupListView extends BaseView<ClientTAXGroup> {

	protected List<ClientTAXGroup> savedSalesTaxGroup;
	protected GroupDialogButtonsHandler groupDialogButtonHandler;
	protected FocusHandler focusChangedHandler;
	private SalesTaxGroupDialog salesTaxGroupDialog;
	protected TaxGroupGrid grid;
	protected SalesTaxItemsGrid itemsGrid;
	private VerticalPanel buttonsLayout;
	private HorizontalPanel bodyLayout;
	protected AddButton button1;
	private Button button2;
	private Button button3;
	private GroupDialogButtonsHandler dialogButtonsHandler;
	private InputDialogHandler dialogHandler;

	public SalesTaxGroupListView() {
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
			Accounter.showError(AccounterExceptions.accounterErrors
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
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		Label lab = new Label(Accounter.constants().manageSalesTaxGroup());
		lab.addStyleName("lable-title");

		bodyLayout = new HorizontalPanel();
		bodyLayout.setSize("100%", "100%");

		grid = new TaxGroupGrid(false);
		grid.init();
		grid.setWidth("100%");
		// grid.setSize("100%", "250px");
		grid.addRecords(getRecords());
		/**
		 * buttons Layout
		 */
		buttonsLayout = new VerticalPanel();
		buttonsLayout.setWidth("100px");
		buttonsLayout.setSpacing(5);

		button1 = new AddButton(this);
		button1.setWidth("80px");

		button1.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (dialogButtonsHandler != null)
					firstButtonClick();

			}
		});

		button2 = new Button(Accounter.constants().edit());
		button2.setEnabled(false);
		button2.setWidth("80px");
		button2.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (dialogButtonsHandler != null)
					secondButtonClick();
			}
		});

		button3 = new Button(Accounter.constants().remove());
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
		bodyLayout.setCellWidth(grid, "90%");
		if (Accounter.getUser().canDoInvoiceTransactions())
			bodyLayout.add(buttonsLayout);
		VerticalPanel mainVlay = new VerticalPanel();
		mainVlay.add(lab);
		mainVlay.add(bodyLayout);
		mainVlay.add(itemsGrid);
		mainVlay.setWidth("100%");
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
				} else {
					Accounter
							.showError(Accounter.constants().selectATaxGroup());
					new Exception();
				}
			}

			public void onThirdButtonClick() {
				ClientTAXGroup taxGroup = (ClientTAXGroup) grid.getSelection();

				if (taxGroup != null) {
					deleteObject(taxGroup);
				} else
					Accounter
							.showError(Accounter.constants().selectATaxGroup());

			}
		};
		addGroupButtonsHandler(groupDialogButtonHandler);
	}

	public void addGroupButtonsHandler(
			GroupDialogButtonsHandler dialogButtonsHandler) {
		this.dialogButtonsHandler = dialogButtonsHandler;
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
			salesTaxGroupDialog = new SalesTaxGroupDialog(Accounter.constants()
					.taxGroup(), Accounter.constants().toAddOrRemoveTaxCode(),
					taxGroup);
		} else {
			salesTaxGroupDialog = new SalesTaxGroupDialog(Accounter.constants()
					.taxGroup(), Accounter.constants().toAddOrRemoveTaxCode(),
					null);
		}

		salesTaxGroupDialog.addInputDialogHandler(new InputDialogHandler() {

			public void onCancel() {

			}

			public boolean onOK() {

				if (taxGroup != null) {
					editTaxGroup(taxGroup);
				} else {
					if (salesTaxGroupDialog.taxGroupText.getValue() != null
							&& !salesTaxGroupDialog.taxGroupText.getValue()
									.toString().isEmpty()) {
						newTaxGroup();
					}

				}
				return true;
			}// onOkClick
		});// InputDialogHandler;
		salesTaxGroupDialog.show();

	}

	protected void editTaxGroup(ClientTAXGroup taxGroup) {
		String groupName = salesTaxGroupDialog.taxGroupText.getValue();
		ClientTAXGroup taxGroupByName = getCompany().getTaxGroupByName(
				groupName);
		if (!(taxGroup.getName().equalsIgnoreCase(groupName) ? true
				: taxGroupByName == null)) {
			Accounter.showError(Accounter.constants().alreadyExist());
		} else {
			taxGroup.setName(groupName);
			taxGroup.setTaxItems(getSelectedTaxItems(taxGroup));
			saveOrUpdate(taxGroup);
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
		ClientCompany company = getCompany();
		ClientTAXItem itemByName = company.getTaxItemByName(taxGroup.getName());
		ClientTAXGroup taxGroupByName = company.getTaxGroupByName(taxGroup
				.getName());
		if (itemByName != null || taxGroupByName != null) {
			Accounter.showError(Accounter.constants().alreadyExist());
		} else {
			saveOrUpdate(taxGroup);
		}
	}

	protected <P extends IAccounterCore> void saveOrUpdate(final P core) {
		AsyncCallback<Long> callback2 = new AsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				if (core.getID() == 0) {
					core.setID(result);
				}
				getCompany().processUpdateOrCreateObject(core);
				grid.removeAllRecords();
				grid.addRecords(getRecords());

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		};
		if (core.getID() == 0) {
			Accounter.createCRUDService().create(core, callback2);
		} else {
			Accounter.createCRUDService().update(core, callback2);
		}

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
		return Accounter.constants().vatGroupList();
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
	}

}
