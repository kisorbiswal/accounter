package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class NewBrandThemeAction extends Action {
	public NewBrandThemeDialog newBrandThemeDialog;

	public NewBrandThemeAction(String text) {
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

	@Override
	public ParentCanvas getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		try {
			newBrandThemeDialog = new NewBrandThemeDialog(FinanceApplication
					.getSettingsMessages().newBrandThemeLabel(), "");
			newBrandThemeDialog.show();
			newBrandThemeDialog.center();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}
}
