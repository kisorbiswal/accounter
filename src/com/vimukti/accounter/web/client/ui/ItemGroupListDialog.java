package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ValidationResult;
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
	List<ClientItemGroup> ItemGroups;
	private ItemGroupDialog itemGroupDg;

	public ItemGroupListDialog(String title, String descript) {
		super(title, descript);
		// setSize("400", "330");
		// setWidth("400px");
		initialise();
		center();
	}

	private void initialise() {
		getGrid().setType(AccounterCoreType.ITEM_GROUP);
		getGrid().addRecordClickHandler(
				new GridRecordClickHandler<ClientItemGroup>() {

					@Override
					public boolean onRecordClick(
							ClientItemGroup clientItemGroup, int column) {
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
		this.okbtn.setVisible(false);

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
		itemGroupDg = new ItemGroupDialog(this, messages.itemGroup(), "",
				itemGroup);

		itemGroupDg.show();
	}

	protected void editItemGroups() {

		saveOrUpdate(itemGroupDg.createOrEditItemGroup());

	}

	@Override
	public Object getGridColumnValue(ClientItemGroup clientItemGroup, int index) {
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
		return new String[] { messages.name(),
		// FinanceApplication.constants().price()
		};
	}

	@Override
	public String getHeaderStyle(int index) {
		return "itemgroupname";
	}

	@Override
	public String getRowElementsStyle(int index) {
		return "itemgroupnameValue";
	}

	@Override
	protected List<ClientItemGroup> getRecords() {
		return getCompany().getItemGroups();
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		String name = itemGroupDg.getItemGroupName();
		if (itemGroup != null) {
			String itemGroupName = itemGroup.getName();
			ClientItemGroup groupByName = company.getItemGroupByName(name);
			if (!(itemGroupName.equalsIgnoreCase(name) ? true
					: groupByName == null)) {
				result.addError(this, messages.alreadyExist());
			}
		} else {
			ClientItemGroup itemGroupByName = company.getItemGroupByName(name);
			if (itemGroupByName != null) {
				result.addError(this,
						messages.anItemGroupAlreadyExistswiththisname());
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

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
