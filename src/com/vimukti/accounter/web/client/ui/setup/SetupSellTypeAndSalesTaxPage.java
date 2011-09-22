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
	private CustomLabel servicesOnlyLabel, productsOnlyLabel,
			bothServicesandProductsLabel, emptyLabel, doyouchargesalestaxLabel;

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
		servicesOnlyLabel = new CustomLabel(accounterConstants.servicesOnly());
		productsOnlyLabel = new CustomLabel(accounterConstants.productsOnly());
		emptyLabel = new CustomLabel("");
		bothServicesandProductsLabel = new CustomLabel(
				accounterConstants.bothServicesandProducts());
		doyouchargesalestaxLabel = new CustomLabel(
				accounterConstants.doyouchargesalestax());

		mainVerticalPanel.add(serviceOnlyRadioButton);

		mainVerticalPanel.add(servicesOnlyLabel);
		mainVerticalPanel.add(emptyLabel);

		productOnlyRadioButton = new RadioButton(SELL_TYPES,
				accounterConstants.products_labelonly());

		mainVerticalPanel.add(productOnlyRadioButton);
		mainVerticalPanel.add(productsOnlyLabel);
		mainVerticalPanel.add(emptyLabel);

		bothserviceandprductRadioButton = new RadioButton(SELL_TYPES,
				accounterConstants.bothservicesandProduct_labelonly());

		mainVerticalPanel.add(bothserviceandprductRadioButton);
		mainVerticalPanel.add(bothServicesandProductsLabel);
		mainVerticalPanel.add(emptyLabel);

		mainVerticalPanel.add(doyouchargesalestaxLabel);
		doyouchargesalestaxLabel.addStyleName("setup_header_label");
		yesRadioButton = new RadioButton(SALES_TAX, accounterConstants.yes());

		mainVerticalPanel.add(yesRadioButton);

		noRadioButton = new RadioButton(SALES_TAX, accounterConstants.no());

		mainVerticalPanel.add(noRadioButton);

		mainVerticalPanel.addStyleName("setuppage_body");
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
			preferences.setChargeSalesTax(true);
		} else {
			preferences.setChargeSalesTax(false);
		}

	}

	@Override
	public boolean doShow() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

}
