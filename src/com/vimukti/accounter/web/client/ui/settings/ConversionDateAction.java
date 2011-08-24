package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ConversionDateAction extends Action {
	private ConversionDateView view;

	public ConversionDateAction(String text) {
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

	@Override
	public void run() {
		try {
			view = new ConversionDateView();
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, ConversionDateAction.this);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}

	}

	@Override
	public String getHistoryToken() {

		return "ConversionDate";
	}

	@Override
	public String getHelpToken() {
		return "conversion-date";
	}

}
