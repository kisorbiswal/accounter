package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CreateTaxesDialog;
import com.vimukti.accounter.web.client.ui.core.Action;

public class CreateTaxesAction extends Action {

	public CreateTaxesAction(String text) {
		super(text);
		this.catagory = Accounter.constants().vat();
	}

	// @Override
	// public ParentCanvas getView() {
	// // its not using any where
	// return null;
	// }

	@Override
	public void run() {

		new CreateTaxesDialog(null).show();
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().createTaxes();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/Creating_taxes.png";
	// }

	@Override
	public String getHistoryToken() {
		return "CraeteTaxes";
	}

	@Override
	public String getHelpToken() {
		return "new-tax";
	}

}