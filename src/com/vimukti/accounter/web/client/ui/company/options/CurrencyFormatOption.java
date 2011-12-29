package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class CurrencyFormatOption extends AbstractPreferenceOption {

	private static CurrencyFormatOptionUiBinder uiBinder = GWT
			.create(CurrencyFormatOptionUiBinder.class);

	interface CurrencyFormatOptionUiBinder extends
			UiBinder<Widget, CurrencyFormatOption> {
	}

	public CurrencyFormatOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	@Override
	public String getTitle() {
		return messages.currencyFormat();
	}

	@Override
	public String getAnchor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createControls() {

	}

	@Override
	public void onSave() {
	}

	@Override
	public void initData() {
	}

}