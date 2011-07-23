package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ParentCanvas;

public class CompanyInfoAction extends Action {

	public CompanyInfoAction(String text) {
		super(text);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	public CompanyInfoAction(String text, String iconString) {
		super(text, iconString);
		this.catagory = Accounter.getCompanyMessages().company();
	}

	@Override
	public ParentCanvas<?> getView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run(Object data, Boolean isDependent) {
		new CompanyInfoDialog(Accounter.getCompanyMessages()
				.companyInformation(),"").show();
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return  Accounter.getFinanceImages().companyInformation();
	}
@Override
public String getImageUrl() {
	return "/images/information-icon-new.png";
}

@Override
public String getHistoryToken() {
	// TODO Auto-generated method stub
	return null;
}
}
