package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class PrimaryCurrencyOption extends AbstractPreferenceOption {

	private static PrimaryCurrencyOptionUiBinder uiBinder = GWT
			.create(PrimaryCurrencyOptionUiBinder.class);

	interface PrimaryCurrencyOptionUiBinder extends
			UiBinder<Widget, PrimaryCurrencyOption> {
	}

	public PrimaryCurrencyOption() {
		initWidget(uiBinder.createAndBindUi(this));

	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAnchor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createControls() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
