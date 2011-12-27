package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

public class DoyouUseShipingsOption extends AbstractPreferenceOption {

	@UiField
	CheckBox useShipMethods;
	@UiField
	Label shippingmedescritionLabel;

	private static DoyouUseShipingsOptionUiBinder uiBinder = GWT
			.create(DoyouUseShipingsOptionUiBinder.class);

	interface DoyouUseShipingsOptionUiBinder extends
			UiBinder<Widget, DoyouUseShipingsOption> {
	}

	public DoyouUseShipingsOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	@Override
	public String getTitle() {
		return Accounter.messages().Doyoudoshipping();
	}

	@Override
	public void onSave() {
		getCompanyPreferences()
				.setDoProductShipMents(useShipMethods.getValue());
	}

	@Override
	public String getAnchor() {
		return Accounter.messages().Doyoudoshipping();
	}

	@Override
	public void createControls() {
		shippingmedescritionLabel
				.setText(Accounter.messages().Thisoptioncanbeusedtoenable());
		shippingmedescritionLabel.setStyleName("organisation_comment");
		useShipMethods.setText(Accounter.messages().Doyoudoshipping());
		useShipMethods.setStyleName("bold");
	}

	@Override
	public void initData() {
		useShipMethods.setValue(getCompanyPreferences().isDoProductShipMents());

	}

}
