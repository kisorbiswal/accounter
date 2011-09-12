package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

public class QuantitiyAndRateOption extends AbstractPreferenceOption {

	private static QuantitiyAndRateOptionUiBinder uiBinder = GWT
			.create(QuantitiyAndRateOptionUiBinder.class);
	@UiField
	CheckBox quantityAndRateCheckBox;

	interface QuantitiyAndRateOptionUiBinder extends
			UiBinder<Widget, QuantitiyAndRateOption> {
	}

	public QuantitiyAndRateOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	private void createControls() {
		quantityAndRateCheckBox.setText(constants.quantityandPriceandRate());

	}

	public QuantitiyAndRateOption(String firstName) {
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

}
