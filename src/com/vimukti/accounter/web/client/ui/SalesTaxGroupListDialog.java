package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.FocusHandler;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

/**
 * 
 * @author Rajesh A
 * 
 */

public class SalesTaxGroupListDialog extends GroupDialog<ClientTAXGroup> {

	protected List<ClientTAXGroup> savedSalesTaxGroup;
	ClientTAXGroup taxGroup;
	protected GroupDialogButtonsHandler groupDialogButtonHandler;
	protected FocusHandler focusChangedHandler;
	private SalesTaxGroupDialog salesTaxGroupDialog;

	// private ClientTaxGroup newTaxGroup;
	// private ClientTaxGroup takenTaxGroup;
	// private int selectedTaxGroupIndex;

	public SalesTaxGroupListDialog(String title, String desc) {
		super(title, desc);
		this.getElement().setId("SalesTaxGroupListDialog");
		// setWidth("400px");
		initialise();
		// mainPanel.setSpacing(3);
		center();
	}

	// public long getSelectedTaxGroupMethodId() {
	//
	// return ((ClientTaxGroup) listGridView.getSelection()).getID();
	// }

	public void initialise() {
		getGrid().setType(AccounterCoreType.TAX_GROUP);
		getGrid().addRecordClickHandler(
				new GridRecordClickHandler<ClientTAXGroup>() {

					@Override
					public boolean onRecordClick(ClientTAXGroup core, int column) {
						enableEditRemoveButtons(true);
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

				taxGroup = (ClientTAXGroup) listGridView.getSelection();
				if (taxGroup != null) {
					showAddEditTaxGroup(taxGroup);
				} else {
					Accounter.showError(messages.selectATaxGroup());
					new Exception();
				}
			}

			public void onThirdButtonClick() {
				ClientTAXGroup taxGroup = (ClientTAXGroup) listGridView
						.getSelection();

				if (taxGroup != null) {
					deleteObject(taxGroup);
				} else
					Accounter.showError(messages.selectATaxGroup());

			}
		};
		addGroupButtonsHandler(groupDialogButtonHandler);

	}

	public void showAddEditTaxGroup(final ClientTAXGroup taxGroup) {

		if (taxGroup != null) {
			salesTaxGroupDialog = new SalesTaxGroupDialog(messages.taxGroup(),
					messages.toAddOrRemoveTaxCode(), taxGroup);
		} else {
			salesTaxGroupDialog = new SalesTaxGroupDialog(messages.taxGroup(),
					messages.toAddOrRemoveTaxCode(), null);
		}

		salesTaxGroupDialog.addInputDialogHandler(new InputDialogHandler() {

			public void onCancel() {

			}

			public boolean onOK() {

				if (taxGroup != null) {
					editTaxGroup(taxGroup);
				} else {
					newTaxGroup();
				}
				return false;

			}// onOkClick
		});// InputDialogHandler;
		salesTaxGroupDialog.show();

	}

	protected void editTaxGroup(ClientTAXGroup taxGroup) {
		String groupName = salesTaxGroupDialog.taxGroupText.getValue();
		ClientTAXGroup clientTAXGroup = company.getTaxGroupByName(groupName);
		if (!(taxGroup.getName().equalsIgnoreCase(groupName) ? true
				: clientTAXGroup == null)) {
			Accounter.showError(messages.alreadyExist());
		} else {
			taxGroup.setName(groupName);
			taxGroup.setTaxItems(getSelectedTaxItems(taxGroup));
			saveOrUpdate(taxGroup);
		}
	}

	private List<ClientTAXItem> getSelectedTaxItems(ClientTAXGroup taxGroup) {
		List<ClientTAXItem> taxItems = new ArrayList<ClientTAXItem>();
		List<ClientTAXItem> records = salesTaxGroupDialog.selectTaxItemsGrid
				.getRecords();
		ClientTAXItem item;
		for (ClientTAXItem clientTaxItem : records) {
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
		ClientTAXItem itemByName = company.getTaxItemByName(taxGroup.getName());
		ClientTAXGroup taxGroupByName = company.getTaxGroupByName(taxGroup
				.getName());
		if (itemByName != null || taxGroupByName != null) {
			Accounter.showError(messages.alreadyExist());
		} else
			saveOrUpdate(taxGroup);

	}

	@Override
	public Object getGridColumnValue(ClientTAXGroup rec, int index) {
		switch (index) {
		case 0:
			if (rec.getName() != null)
				return rec.getName();
			else
				return "";
		default:
			return "";
		}
	}

	@Override
	public String[] setColumns() {

		return new String[] { messages.name() };
	}

	@Override
	public String getHeaderStyle(int index) {
		return "name";
	}

	@Override
	public String getRowElementsStyle(int index) {
		return "nameValue";
	}

	@Override
	protected List<ClientTAXGroup> getRecords() {
		return getCompany().getTaxGroups();
	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
