package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CreateTaxesDialog;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class CreateTaxesAction extends Action {

	public CreateTaxesAction(String text) {
		super(text);
	}

	public CreateTaxesAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.constants().VAT();
	}

	
	@Override
	public ParentCanvas getView() {
		// its not using any where
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {

		new CreateTaxesDialog(null).show();
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().createTaxes();
	}

	@Override
	public String getImageUrl() {
		return "/images/Creating_taxes.png";
	}

	@Override
	public String getHistoryToken() {
		return "CraeteTaxes";
	}

}