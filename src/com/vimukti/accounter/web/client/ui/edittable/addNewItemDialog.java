package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;

public class addNewItemDialog extends BaseDialog<ClientItem> {

	public addNewItemDialog(String title, String desc) {
		super(title, desc);
		setWidth("300px");
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

}
