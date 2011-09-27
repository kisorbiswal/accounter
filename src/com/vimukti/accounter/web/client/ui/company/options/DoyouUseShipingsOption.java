package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

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
		return "Do you do shipping";
	}

	@Override
	public void onSave() {
		getCompanyPreferences()
				.setDoProductShipMents(useShipMethods.getValue());
	}

	@Override
	public String getAnchor() {
		return "Do you do shipping";
	}

	@Override
	public void createControls() {
		shippingmedescritionLabel
				.setText("This option can be used to enable  Shipping Terms ,Shipping Method and Shiping Adress realated to customer and vendor(supplier) transactions");
		shippingmedescritionLabel.setStyleName("organisation_comment");
		useShipMethods.setText("Do you do shipping");
		useShipMethods.setStyleName("bold");
	}

	@Override
	public void initData() {
		useShipMethods.setValue(getCompanyPreferences().isDoProductShipMents());

	}

}
