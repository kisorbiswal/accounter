package com.vimukti.accounter.web.client.uibinder.companypreferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ClassTrackingPage extends AbstractCompanyInfoPanel {

	private static ClassTrackingPageUiBinder uiBinder = GWT
			.create(ClassTrackingPageUiBinder.class);
	@UiField
	VerticalPanel mainPanel;

	interface ClassTrackingPageUiBinder extends
			UiBinder<Widget, ClassTrackingPage> {
	}

	public ClassTrackingPage() {
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
