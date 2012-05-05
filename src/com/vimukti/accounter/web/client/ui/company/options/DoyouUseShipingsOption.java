package com.vimukti.accounter.web.client.ui.company.options;

import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class DoyouUseShipingsOption extends AbstractPreferenceOption {

	LabelItem shippingmedescritionLabel;
	CheckboxItem useShipMethods;

	StyledPanel mainPanel;

	public DoyouUseShipingsOption() {
		super("");
		createControls();
		initData();
	}

	@Override
	public String getTitle() {
		return messages.Doyoudoshipping();
	}

	@Override
	public void onSave() {
		getCompanyPreferences()
				.setDoProductShipMents(useShipMethods.getValue());
	}

	@Override
	public String getAnchor() {
		return messages.Doyoudoshipping();
	}

	@Override
	public void createControls() {

		useShipMethods = new CheckboxItem(messages.Doyoudoshipping(),
				"useShipMethods");
		shippingmedescritionLabel = new LabelItem(
				messages.Thisoptioncanbeusedtoenable(),
				"shippingmedescritionLabel");
		mainPanel = new StyledPanel("shippingMainPanel");
		mainPanel.add(shippingmedescritionLabel);
		mainPanel.add(useShipMethods);
		add(mainPanel);
	}

	@Override
	public void initData() {
		useShipMethods.setValue(getCompanyPreferences().isDoProductShipMents());

	}

}
