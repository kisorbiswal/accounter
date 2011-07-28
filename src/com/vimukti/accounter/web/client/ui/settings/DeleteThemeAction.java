package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class DeleteThemeAction extends Action {

	public DeleteThemeAction(String text) {
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

	@SuppressWarnings("unchecked")
	@Override
	public ParentCanvas getView() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run(Object data, Boolean isDependent) {
		try {
			DeleteThemeDialog deleteThemeDialog = new DeleteThemeDialog(
					Accounter.constants().deleteThemeLabel(), "",
					(ClientBrandingTheme) data);
			deleteThemeDialog.show();
			deleteThemeDialog.center();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	@Override
	public String getHistoryToken() {
		return null;
	}
}
