package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.externalization.ActionsConstants;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.customers.BrandingThemeComboDialog;

public class BrandingThemeComboAction extends Action {

	public BrandingThemeComboAction(String text) {
		super(text);
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParentCanvas getView() {
		return null;
	}

	public void run(Object data, Boolean isDependent) {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		BrandingThemeComboDialog comboDialog = new BrandingThemeComboDialog(
				FinanceApplication.getSettingsMessages().selectThemes(),
				"", (ClientTransaction) data);
		comboDialog.show();
		comboDialog.center();
	}

}
