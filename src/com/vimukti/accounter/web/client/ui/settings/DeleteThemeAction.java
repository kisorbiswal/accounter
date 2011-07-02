package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class DeleteThemeAction extends Action {

	public DeleteThemeAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
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

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run(Object data, Boolean isDependent) {
		try {
			DeleteThemeDialog deleteThemeDialog = new DeleteThemeDialog(
					FinanceApplication.getSettingsMessages().deleteThemeLabel(),
					"", (ClientBrandingTheme) data);
			deleteThemeDialog.show();
			deleteThemeDialog.center();
		} catch (Exception e) {
System.out.println(e.toString());		}
	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}
}
