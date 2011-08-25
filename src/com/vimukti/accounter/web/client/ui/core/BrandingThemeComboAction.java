package com.vimukti.accounter.web.client.ui.core;

import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.customers.BrandingThemeComboDialog;

public class BrandingThemeComboAction extends Action {

	public BrandingThemeComboAction(String text) {
		super(text);
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	public void run() {
		if (listData != null) {
			runAsync(listData);
		} else {
			runAsync(data, isDependent);
		}

	}

	private void runAsync(List<InvoicesList> listData) {

		BrandingThemeComboDialog comboDialog = new BrandingThemeComboDialog(
				Accounter.constants().selectThemes(), "", listData);
		comboDialog.show();
		comboDialog.center();

	}

	private void runAsync(Object data, Boolean isDependent) {
		BrandingThemeComboDialog comboDialog = new BrandingThemeComboDialog(
				Accounter.constants().selectThemes(), "",
				(ClientTransaction) data);
		comboDialog.show();
		comboDialog.center();
	}

	@Override
	public String getHistoryToken() {
		return "BrandingThemeCombo";
	}

	@Override
	public String getHelpToken() {
		return "new-branding-theme";
	}

}
