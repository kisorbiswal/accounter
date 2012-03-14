package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemGroup;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.ItemGroupListDialog;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;

public class ItemGroupDialog extends BaseDialog<ClientItem> {
	ClientItemGroup itemgroup;
	private DialogGrid dialoggrid;
	// private ItemDialogGrid dialoggrid;
	private TextItem itemGtext;
	private DynamicForm dform;
	private final ItemGroupListDialog parent;

	// private Button menuButton;

	public ItemGroupDialog(ItemGroupListDialog parent, String title,
			String desc, ClientItemGroup itemGroup) {
		super(title, desc);
		this.getElement().setId("ItemGroupDialog");
//		setWidth("300px");
		// setHeight("300");
		this.itemgroup = itemGroup;

		initdialog();
		this.parent = parent;
		// initGridData();
	}

	private void initGridData() {
		List<ClientItem> clientItems = getCompany().getActiveItems();
		if (this.itemgroup != null) {
			for (ClientItem clientItem : clientItems) {
				if (clientItem.getItemGroup() != 0
						&& clientItem.getItemGroup() == (this.itemgroup.getID()))
					dialoggrid.addData(clientItem);
			}
		} else {
			List<ClientItemGroup> itemGroups = getCompany().getItemGroups();
			for (ClientItem clientItem : clientItems) {
				if (clientItem.getItemGroup() == 0) {
					dialoggrid.addData(clientItem);
					continue;
				}
			}
		}
	}

	public void initdialog() {

		StyledPanel panel = new StyledPanel("panel");

		dform = new DynamicForm("dform");
		itemGtext = new TextItem(messages.itemGroup(), "itemGtext");
		itemGtext.setRequired(true);
		dform.add(itemGtext);
		if (this.itemgroup != null) {
			itemGtext.setValue(itemgroup.getName());
		}

		// dialoggrid = new ItemDialogGrid(false, itemgroup);
		// dialoggrid.init();
		// dialoggrid.setCanEdit(true);
		// dialoggrid.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
		// ItemDialogGrid itemdialoggrid=new ItemDialogGrid(true);
		// itemdialoggrid.init();
		// dialoggrid.add(itemdialoggrid);
		panel.add(dform);
		// panel.add(createAddNewButton());
		// panel.add(dialoggrid);

		setBodyLayout(panel);

		this.center();

		show();

	}

	public ClientItemGroup createOrEditItemGroup() {
		// List<ClientItem> clientItems = (ArrayList) dialoggrid
		// .getSelectedRecords();
		// List<ClientItem> clientItems = (ArrayList) dialoggrid.getRecords();
		if (itemgroup == null) {
			itemgroup = new ClientItemGroup();
			// this.itemgroup.setItems(clientItems);
			this.itemgroup.setName(itemGtext.getValue().toString());
		} else {
			// for (ClientItem clientItem : itemgroup.getItems()) {
			// if (clientItems.contains(clientItem)) {
			// clientItem.setItemGroup(null);
			// }
			// }
			this.itemgroup.setName(itemGtext.getValue().toString());
		}

		return this.itemgroup;
	}

	@Override
	public Object getGridColumnValue(ClientItem clientItem, int index) {
		switch (index) {
		case 0:
			return clientItem.getName();
			// case 1:
			// return clientItem.getSalesPrice();

		default:
			return "";
		}
	}

	// public List<ClientItem> getItems() {
	// return this.dialoggrid.getRecords();
	// }

	public String getItemGroupName() {
		return this.itemGtext.getValue().toString();
	}

	public ValidationResult validate() {
		ValidationResult result = dform.validate();
		result.add(parent.validate());
		return result;
	}

	@Override
	protected boolean onOK() {
		return parent.onOK();
	}

	@Override
	public void setFocus() {
		itemGtext.setFocus();

	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
