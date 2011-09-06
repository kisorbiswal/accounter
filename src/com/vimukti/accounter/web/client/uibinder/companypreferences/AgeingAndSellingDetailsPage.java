package com.vimukti.accounter.web.client.uibinder.companypreferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class AgeingAndSellingDetailsPage extends AbstractCompanyInfoPanel {

	private static AgeingAndSellingDetailsPageUiBinder uiBinder = GWT
			.create(AgeingAndSellingDetailsPageUiBinder.class);

	interface AgeingAndSellingDetailsPageUiBinder extends
			UiBinder<Widget, AgeingAndSellingDetailsPage> {
	}

	public AgeingAndSellingDetailsPage() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

}
