package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

public class NewItemDialog extends BaseDialog<ClientItem> {

	public NewItemDialog(String title, String desc) {
		super(title, desc);
		this.getElement().setId("NewItemDialog");
//		setWidth("300px");
		show();
	}

	@Override
	protected boolean onOK() {
		return false;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
