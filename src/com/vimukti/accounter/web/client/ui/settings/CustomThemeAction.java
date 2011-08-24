package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;

public class CustomThemeAction extends Action {

	public CustomThemeAction(String text) {
		super(text);
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// NOTHING TO DO
		return null;
	}

//	@Override
//	public ParentCanvas getView() {
//		// NOTHING TO DO
//		return null;
//	}

	@Override
	public void run() {
		try {
			CustomThemeDialog customThemeDialog = new CustomThemeDialog(
					Accounter.constants().newBrandThemeLabel(), "");
			customThemeDialog.show();
			customThemeDialog.center();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelpToken() {
		return null;
	}
}
