package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.CustomLabel;

public class SetupSellTypeAndSalesTaxPage extends AbstractSetupPage {
	private static final String SELL_TYPES = "Sell Type";
	private static final String SALES_TAX = "Sales Tax";
	private RadioButton serviceOnlyRadioButton, productOnlyRadioButton,
			bothserviceandprductRadioButton, yesRadioButton, noRadioButton;
	private static final int SERVICES_ONLY = 1;
	private static final int PRODUCT_ONLY = 2;
	private static final int BOTH_SERVICES_AND_PRODUCTS = 3;
	private static final int YES = 1;
	private static final int NO = 2;
	int slectedsellTYpeValue = 0;
	int selectedSaleTaxvalue = 0;

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
		serviceOnlyRadioButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				slectedsellTYpeValue = 1;
				productOnlyRadioButton.setValue(false);
				bothserviceandprductRadioButton.setValue(false);
			}
		});
		mainVerticalPanel.add(serviceOnlyRadioButton);
		mainVerticalPanel
				.add(new CustomLabel(accounterConstants.servicesOnly()));
		mainVerticalPanel.add(new CustomLabel(""));

		productOnlyRadioButton = new RadioButton(SELL_TYPES,
				accounterConstants.products_labelonly());
		productOnlyRadioButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				slectedsellTYpeValue = 2;
				serviceOnlyRadioButton.setValue(false);
				bothserviceandprductRadioButton.setValue(false);
			}
		});
		mainVerticalPanel.add(productOnlyRadioButton);
		mainVerticalPanel
				.add(new CustomLabel(accounterConstants.productsOnly()));
		mainVerticalPanel.add(new CustomLabel(""));

		bothserviceandprductRadioButton = new RadioButton(SELL_TYPES,
				accounterConstants.bothservicesandProduct_labelonly());
		bothserviceandprductRadioButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				slectedsellTYpeValue = 3;
				serviceOnlyRadioButton.setValue(false);
				productOnlyRadioButton.setValue(false);
			}
		});
		mainVerticalPanel.add(bothserviceandprductRadioButton);
		mainVerticalPanel.add(new CustomLabel(accounterConstants
				.bothServicesandProducts()));
		mainVerticalPanel.add(new CustomLabel(""));

		mainVerticalPanel.add(new CustomLabel(accounterConstants
				.doyouchargesalestax()));
		yesRadioButton = new RadioButton(SALES_TAX, accounterConstants.yes());
		yesRadioButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				selectedSaleTaxvalue = 1;
				noRadioButton.setValue(false);
			}
		});
		mainVerticalPanel.add(yesRadioButton);

		noRadioButton = new RadioButton(SALES_TAX, accounterConstants.no());
		noRadioButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				selectedSaleTaxvalue = 2;
				yesRadioButton.setValue(false);
			}
		});
		mainVerticalPanel.add(noRadioButton);

		return mainVerticalPanel;
	}

	@Override
	public void onLoad() {

		switch (preferences.getSellType()) {
		case SERVICES_ONLY:
			serviceOnlyRadioButton.setValue(true);
			break;
		case PRODUCT_ONLY:
			productOnlyRadioButton.setValue(true);
			break;
		case BOTH_SERVICES_AND_PRODUCTS:
			bothserviceandprductRadioButton.setValue(true);
			break;
		}

		switch (preferences.getSalesTaxs()) {
		case YES:
			yesRadioButton.setValue(true);
			break;
		case NO:
			noRadioButton.setValue(true);
			break;
		}

	}

	@Override
	public void onSave() {

		if (slectedsellTYpeValue != 0) {
			preferences.setSellType(slectedsellTYpeValue);
		}
		if (selectedSaleTaxvalue != 0) {
			preferences.setSalesTaxs(selectedSaleTaxvalue);
		}
	}

	@Override
	public boolean doShow() {
		// TODO Auto-generated method stub
		return false;
	}

}
