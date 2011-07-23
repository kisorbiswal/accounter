package com.vimukti.accounter.web.client.ui.vendors;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;

public class NewVendorItemAction extends NewItemAction {

	public NewVendorItemAction(String text, String iconString) {
		super(text, iconString, false);
		this.catagory = UIUtils.getVendorString(Accounter
				.getVendorsMessages().supplier(), Accounter
				.getVendorsMessages().vendor());
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return UIUtils.getVendorString("newSupplierItem", "newVendorItem");
	}

}
