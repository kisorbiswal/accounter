package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.CustomLabel;

public class SetupSellTypeAndSalesTaxPage extends AbstractSetupPage {
	private static final String SELL_TYPES = "Sell Type";
	private static final String SALES_TAX = "Sales Tax";
	private RadioButton serviceOnlyRadioButton, productOnlyRadioButton,
			bothserviceandprductRadioButton, yesRadioButton, noRadioButton;
	boolean servicvalue;

	public SetupSellTypeAndSalesTaxPage() {
		super();
	}

	@Override
	public String getHeader() {
		return this.accounterConstants.whatDoYouSell();
	}

	@Override
	public VerticalPanel getPageBody() {

		VerticalPanel mainVerticalPanel = new VerticalPanel();

		serviceOnlyRadioButton = new RadioButton(SELL_TYPES,
				accounterConstants.services_labelonly());

		mainVerticalPanel.add(serviceOnlyRadioButton);
		mainVerticalPanel
				.add(new CustomLabel(accounterConstants.servicesOnly()));
		mainVerticalPanel.add(new CustomLabel(""));

		productOnlyRadioButton = new RadioButton(SELL_TYPES,
				accounterConstants.products_labelonly());

		mainVerticalPanel.add(productOnlyRadioButton);
		mainVerticalPanel
				.add(new CustomLabel(accounterConstants.productsOnly()));
		mainVerticalPanel.add(new CustomLabel(""));

		bothserviceandprductRadioButton = new RadioButton(SELL_TYPES,
				accounterConstants.bothservicesandProduct_labelonly());

		mainVerticalPanel.add(bothserviceandprductRadioButton);
		mainVerticalPanel.add(new CustomLabel(accounterConstants
				.bothServicesandProducts()));
		mainVerticalPanel.add(new CustomLabel(""));

		mainVerticalPanel.add(new CustomLabel(accounterConstants
				.doyouchargesalestax()));
		yesRadioButton = new RadioButton(SALES_TAX, accounterConstants.yes());

		mainVerticalPanel.add(yesRadioButton);

		noRadioButton = new RadioButton(SALES_TAX, accounterConstants.no());

		mainVerticalPanel.add(noRadioButton);

		return mainVerticalPanel;
	}

	@Override
	public void onLoad() {
		boolean sellServices = preferences.isSellServices();
		if (sellServices)
			serviceOnlyRadioButton.setValue(true);
		boolean sellProducts = preferences.isSellProducts();
		if (sellProducts)
			productOnlyRadioButton.setValue(true);
		if (sellServices && sellProducts)
			bothserviceandprductRadioButton.setValue(true);

		if (preferences.getDoYouPaySalesTax()) {
			yesRadioButton.setValue(true);
		} else {
			noRadioButton.setValue(true);
		}
		if (preferences.isDoYouChargesalesTax()) {
			yesRadioButton.setValue(true);
		} else {
			noRadioButton.setValue(true);
		}

	}

	@Override
	public void onSave() {

		if (serviceOnlyRadioButton.getValue())
			preferences.setSellServices(true);
		if (productOnlyRadioButton.getValue())
			preferences.setSellProducts(true);
		if (bothserviceandprductRadioButton.getValue()) {
			preferences.setSellServices(true);
			preferences.setSellProducts(true);
		}

		if (yesRadioButton.getValue()) {
			preferences.setDoYouChargesalesTax(true);
		} else {
			preferences.setDoYouChargesalesTax(false);
		}

	}

	@Override
	public boolean doShow() {
		// TODO Auto-generated method stub
		return true;
	}

}
