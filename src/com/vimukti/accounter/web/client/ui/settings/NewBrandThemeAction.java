package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class NewBrandThemeAction extends Action<ClientBrandingTheme> {

	private NewBrandingThemeView view;

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
	public void run() {
		runAysnc(data, isDependent);
	}

	private void runAysnc(Object data, Boolean isDependent) {
		try {
			if (data == null) {
				view = new NewBrandingThemeView(Accounter.constants()
						.brandingTheme(), "");
			} else {
				view = new NewBrandingThemeView(Accounter.constants()
						.editBrandThemeLabel(), "", (ClientBrandingTheme) data);

			}
			MainFinanceWindow.getViewManager()
					.showView(view, data, false, this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getHistoryToken() {
		return "newBrandingTheme";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}
}
