package com.vimukti.accounter.web.client.uibinder.companypreferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class LocationTrackingPage extends AbstractCompanyInfoPanel {

	private static LocationTrackingPageUiBinder uiBinder = GWT
			.create(LocationTrackingPageUiBinder.class);

	interface LocationTrackingPageUiBinder extends
			UiBinder<Widget, LocationTrackingPage> {
	}

	public LocationTrackingPage() {
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
