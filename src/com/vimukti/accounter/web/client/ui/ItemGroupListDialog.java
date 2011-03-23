package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.company.ItemGroupDialog;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.AccounterErrorType;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

/**
 * 
 * @author V.L.Pavani
 * 
 */
public class ItemGroupListDialog extends GroupDialog<ClientItemGroup> {

	ClientItemGroup itemGroup;

	private GroupDialogButtonsHandler dialogButtonsHandler;

	List<ClientItemGroup> ItemGroups;
	private ItemGroupDialog itemGroupDg;

	public ItemGroupListDialog(String title, String descript) {
		super(title, descript);
		// setSize("400", "330");
		setWidth("400");
		initialise();
		center();
	}

	private void initialise() {
		getGrid().setType(AccounterCoreType.ITEM_GROUP);
		getGrid().addRecordClickHandler(new GridRecordClickHandler() {

			@Override
			public boolean onRecordClick(IsSerializable core, int column) {
				ClientItemGroup clientItemGroup = (ClientItemGroup) core;
				if (clientItemGroup != null)
					enableEditRemoveButtons(true);
				else
					enableEditRemoveButtons(false);

				return false;
			}

		});

		dialogButtonsHandler = new GroupDialogButtonsHandler() {

			public void onCloseButtonClick() {

			}

			public void onFirstButtonClick() {
				showAddEditGroupDialog(null);
			}

			public void onSecondButtonClick() {
				showAddEditGroupDialog((ClientItemGroup) listGridView
						.getSelection());

			}

			public void onThirdButtonClick() {
				deleteObject(getSelectedItemGroup());
				if (ItemGroups == null)
					enableEditRemoveButtons(false);
			}
		};
		addGroupButtonsHandler(dialogButtonsHandler);

	}

	public void createItemSGroups() {
		if (Utility.isObjectExist(FinanceApplication.getCompany()
				.getItemGroups(), itemGroupDg.getItemGroupName())) {
			Accounter
					.showError("A ProductGroup  Already Exists with this name");
		} else {
			createObject(itemGroupDg.createOrEditItemGroup());
		}
	}

	public String getSelectedItemGroupId() {
		return ((ClientItemGroup) listGridView.getSelection()).getStringID();
	}

	public ClientItemGroup getSelectedItemGroup() {
		return (ClientItemGroup) listGridView.getSelection();
	}

	public void showAddEditGroupDialog(ClientItemGroup rec) {
		itemGroup = rec;
		itemGroupDg = new ItemGroupDialog(FinanceApplication
				.getCustomersMessages().productGroup(), "", itemGroup);

		InputDialogHandler dialogHandler = new InputDialogHandler() {

			public void onCancelClick() {
				closeWindow();
			}

			public boolean onOkClick() {
				if (itemGroupDg.validate()) {
					if (itemGroup != null) {
						editItemGroups();
					} else
						createItemSGroups();
				} else {

					return false;
				}
				return true;
			}

		};
		itemGroupDg.addInputDialogHandler(dialogHandler);
		itemGroupDg.show();
	}

	protected void editItemGroups() {

		if (!(itemGroup.getName().equalsIgnoreCase(
				itemGroupDg.getItemGroupName()) ? true : (Utility
				.isObjectExist(company.getItemGroups(), itemGroupDg
						.getItemGroupName())) ? false : true)) {
			Accounter.showError(AccounterErrorType.ALREADYEXIST);
		} else {
			alterObject(itemGroupDg.createOrEditItemGroup());
		}

	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		ClientItemGroup clientItemGroup = (ClientItemGroup) obj;
		if (clientItemGroup != null) {
			switch (index) {
			case 0:
				return clientItemGroup.getName();
			}
		}
		return null;
	}

	@Override
	public String[] setColumns() {
		return new String[] { FinanceApplication.getFinanceUIConstants().name(),
		// FinanceApplication.getFinanceUIConstants().price()
		};
	}

	@Override
	protected List<ClientItemGroup> getRecords() {
		return (List<ClientItemGroup>) FinanceApplication.getCompany()
				.getItemGroups();
	}
}
