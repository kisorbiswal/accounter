package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class NewBrandThemeAction extends Action {
	public NewBrandThemeDialog newBrandThemeDialog;

	public NewBrandThemeAction(String text) {
		super(text);
		this.catagory = Accounter.constants().settings();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	@Override
	public void run(Object data, Boolean isDependent) {
		try {
			if (data != null) {
				newBrandThemeDialog = new NewBrandThemeDialog(Accounter
						.constants().editBrandThemeLabel(), "",
						(ClientBrandingTheme) data);
				newBrandThemeDialog.show();
				newBrandThemeDialog.center();
			} else {
				newBrandThemeDialog = new NewBrandThemeDialog(Accounter
						.constants().newBrandThemeLabel(), "");
				newBrandThemeDialog.show();
				newBrandThemeDialog.center();
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	@Override
	public String getHistoryToken() {
		return "newBrandingTheme";
	}
}
