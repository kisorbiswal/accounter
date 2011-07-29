package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ViewSalesTaxLiabilityAction extends Action {

	public ViewSalesTaxLiabilityAction(String text) {
		super(text);

	}

	// @Override
	// public ParentCanvas<?> getView() {
	//
	// return null;
	// }

	@Override
	public void run(Object data, Boolean isDependent) {

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/reports.png";
	// }

	@Override
	public String getHistoryToken() {

		return null;
	}
}
