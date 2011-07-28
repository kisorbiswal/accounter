package com.vimukti.accounter.web.client.ui.vendors;

import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;

public class NewVendorItemAction extends NewItemAction {

	public NewVendorItemAction(String text, String iconString) {
		super(text, iconString, false);
		this.catagory = UIUtils.getVendorString(Accounter.constants()
				.supplier(), Accounter.constants().vendor());
	}

	@Override
	public String getHistoryToken() {
		return UIUtils.getVendorString("newSupplierItem", "newVendorItem");
	}

}
