package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.company.ItemGroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialog;
import com.vimukti.accounter.web.client.ui.core.GroupDialogButtonsHandler;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

/**
 * 
 * @author V.L.Pavani
 * 
 */
public class ItemGroupListDialog extends GroupDialog<ClientItemGroup> {

	ClientItemGroup itemGroup;

	private GroupDialogButtonsHandler dialogButtonsHandler;
	AccounterConstants accounterConstants = Accounter.constants();
	List<ClientItemGroup> ItemGroups;
	private ItemGroupDialog itemGroupDg;

	public ItemGroupListDialog(String title, String descript) {
		super(title, descript);
		// setSize("400", "330");
		setWidth("400px");
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
		saveOrUpdate(itemGroupDg.createOrEditItemGroup());
	}

	public long getSelectedItemGroupId() {
		return ((ClientItemGroup) listGridView.getSelection()).getID();
	}

	public ClientItemGroup getSelectedItemGroup() {
		return (ClientItemGroup) listGridView.getSelection();
	}

	public void showAddEditGroupDialog(ClientItemGroup rec) {
		itemGroup = rec;
		itemGroupDg = new ItemGroupDialog(this, Accounter.constants()
				.itemGroup(), "", itemGroup);

		itemGroupDg.show();
	}

	protected void editItemGroups() {

		saveOrUpdate(itemGroupDg.createOrEditItemGroup());

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
		return new String[] { Accounter.constants().name(),
		// FinanceApplication.constants().price()
		};
	}

	@Override
	protected List<ClientItemGroup> getRecords() {
		return (List<ClientItemGroup>) getCompany().getItemGroups();
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (itemGroupDg != null) {
			if (itemGroup != null) {
				if (!(itemGroup.getName().equalsIgnoreCase(
						itemGroupDg.getItemGroupName()) ? true : (Utility
						.isObjectExist(company.getItemGroups(), itemGroupDg
								.getItemGroupName())) ? false : true)) {
					result.addError(this, accounterConstants.alreadyExist());
				}
			} else {
				if (Utility.isObjectExist(getCompany().getItemGroups(),
						itemGroupDg.getItemGroupName())) {
					result.addError(this, Accounter.constants()
							.aItemGroupAlreadyExistswiththisname());
				}
			}
		}

		return result;
	}

	@Override
	public boolean onOK() {
		if (itemGroupDg != null) {
			if (itemGroup != null) {
				editItemGroups();
				itemGroupDg = null;
			} else {
				createItemSGroups();
				itemGroupDg = null;
			}

		}
		return true;
	}

}
