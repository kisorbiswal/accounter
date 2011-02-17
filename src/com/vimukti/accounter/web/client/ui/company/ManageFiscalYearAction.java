package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class ManageFiscalYearAction extends Action {

	public ManageFiscalYearAction(String text) {
		super(text);
	}

	public ManageFiscalYearAction(String text, String iconString) {
		super(text, iconString);
	}

	@Override
	public ParentCanvas<?> getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		ManageFiscalYearDialog dialog = new ManageFiscalYearDialog(
				FinanceApplication.getCompanyMessages().manageFiscalYear(), "");
		dialog.show();
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return FinanceApplication.getFinanceMenuImages().manageFiscalYear();
	}
@Override
public String getImageUrl() {
	// TODO Auto-generated method stub
	return "/images/manage_fiscal_year.png";
}
}
