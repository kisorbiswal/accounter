package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientItem;

public class ItemCombo extends CustomCombo<ClientItem> {
	// Type For Checking Request is from Customer View Or Vendor View
	private int type;

	// private final int TYPE_CUSTOMER = 1;
	// private final int TYPE_VENDOR = 2;

	public ItemCombo(String title, int type) {
		super(title);
		this.type = type;
		initCombo(getCompany().getItems());
	}

	public ItemCombo(String title, int type, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 3);
		this.type = type;
		initCombo(getCompany().getItems());
	}

	public ItemCombo(String title, int type, boolean isAddNewRequired,
			boolean isService) {
		super(title, isAddNewRequired, 3);
		this.type = type;
		initCombo(isService);

	}

	public void initCombo(boolean isService) {
		List<ClientItem> items = getCompany().getActiveItems();
		List<ClientItem> serviceitems = new ArrayList<ClientItem>();
		List<ClientItem> productitems = new ArrayList<ClientItem>();
		for (ClientItem item : items) {
			if (item.getType() == ClientItem.TYPE_SERVICE)
				serviceitems.add(item);
			else
				productitems.add(item);
		}
		if (isService)
			initCombo(serviceitems);
		else
			initCombo(productitems);
	}

	@Override
	public String getDefaultAddNewCaption() {
		return comboMessages.newItem();
	}

	@Override
	protected String getDisplayName(ClientItem object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		// Action action;
		// if (type == 1) {
		// action = getNewItemAction();
		// } else {
		// action = VendorsActionFactory.getNewItemAction();
		// }
		// action.setActionSource(this);
		// HistoryTokenUtils.setPresentToken(action, null);
		// action.run(null, true);
	}

	@Override
	protected String getColumnData(ClientItem object,  int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return "";
	}

//	 @Override
//	 protected String getColumnData(ClientItem object, int row, int col) {
//	 switch (col) {
//	 case 0:
//	 return object.getName();
//	 case 1:
//	 return object.getType() == ClientItem.TYPE_SERVICE ?
//	 FinanceApplication.getCustomersMessages().service()
//	 : .getCustomersMessages().PRoduct();
//	 case 2:
//	 return DataUtils.getAmountAsString(object.getSalesPrice());
//	 default:
//	 break;
//	 }
//	// return null;
//	// }

}
