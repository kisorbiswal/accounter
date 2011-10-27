/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Administrator
 * 
 */
public class SetupSellTypeAndSalesTaxPage extends AbstractSetupPage {

	private static SetupSellTypeAndSalesTaxPageUiBinder uiBinder = GWT
			.create(SetupSellTypeAndSalesTaxPageUiBinder.class);
	@UiField
	VerticalPanel viewPanel;
	@UiField
	RadioButton servicesOnly;
	@UiField
	Label servicesOnlyText;
	@UiField
	RadioButton productsOnly;
	@UiField
	Label productsOnlyText;
	@UiField
	RadioButton both;
	@UiField
	Label bothText;
	@UiField
	VerticalPanel sell;

	@UiField
	Label headerLabel;
	@UiField
	RadioButton onepeTransactionRadioButton;
	@UiField
	Label oneperTransactionLabel;
	@UiField
	RadioButton oneperdetaillineRadioButton;
	@UiField
	Label oneperdetaillineLabel;
	@UiField
	CheckBox enableTaxCheckbox;
	@UiField
	Label enableTaxLabel;
	@UiField
	Label taxItemTransactionLabel;
	@UiField
	CheckBox trackCheckbox;
	@UiField
	Label trackLabel;
	@UiField
	VerticalPanel hidePanel;

	@UiField
	CheckBox warehousesCheckBox;
	@UiField
	CheckBox inventoryCheckBox;
	@UiField
	VerticalPanel hiddenPanel;
	@UiField
	VerticalPanel totalPanel;

	interface SetupSellTypeAndSalesTaxPageUiBinder extends
			UiBinder<Widget, SetupSellTypeAndSalesTaxPage> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public SetupSellTypeAndSalesTaxPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(accounterConstants.whatDoYouSell());

		servicesOnly.setText(accounterConstants.services_labelonly());
		servicesOnlyText.setText(accounterConstants.servicesOnly());
		productsOnly.setText(accounterConstants.products_labelonly());
		productsOnlyText.setText(accounterConstants.productsOnly());
		both.setText(accounterConstants.bothservicesandProduct_labelonly());
		bothText.setText(accounterConstants.bothServicesandProducts());

		trackCheckbox.setText(accounterConstants.chargeOrTrackTax());
		trackLabel.setText(accounterConstants.descChrageTrackTax());
		taxItemTransactionLabel.setText(accounterConstants
				.taxtItemTransaction());
		onepeTransactionRadioButton.setText(accounterConstants
				.onepertransaction());
		oneperTransactionLabel.setText(accounterConstants.oneperDescription());
		oneperdetaillineRadioButton.setText(accounterConstants
				.oneperdetailline());
		oneperdetaillineLabel.setText(accounterConstants
				.oneperDetailDescription());
		enableTaxCheckbox.setText(accounterConstants.enableTracking());
		enableTaxLabel.setText(accounterConstants.enableTrackingDescription());
		trackCheckbox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hidePanel.setVisible(trackCheckbox.getValue());

			}
		});
		inventoryCheckBox.setText("Inventory tracking");
		warehousesCheckBox.setText("Is enble Multiple ware houses ? ");

		servicesOnly.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				inventoryCheckBox.setValue(false);
				warehousesCheckBox.setValue(false);
				totalPanel.setVisible(false);

			}
		});
		productsOnly.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				totalPanel.setVisible(productsOnly.getValue());

			}
		});
		both.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				totalPanel.setVisible(both.getValue());

			}
		});

		inventoryCheckBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hiddenPanel.setVisible(inventoryCheckBox.getValue());
			}
		});
	}

	@Override
	public void onLoad() {
		boolean sellServices = preferences.isSellServices();
		if (sellServices)
			servicesOnly.setValue(true);
		boolean sellProducts = preferences.isSellProducts();
		if (sellProducts)
			productsOnly.setValue(true);
		if (sellServices && sellProducts)
			both.setValue(true);

		trackCheckbox.setValue(preferences.isTrackTax());
		hidePanel.setVisible(preferences.isTrackTax());
		enableTaxCheckbox.setValue(preferences.isTrackTax());
		if (preferences.isTaxPerDetailLine())
			oneperdetaillineRadioButton.setValue(true);
		else
			onepeTransactionRadioButton.setValue(true);
		if (preferences.isInventoryEnabled()) {
			inventoryCheckBox.setValue(true);
			hiddenPanel.setVisible(true);
			warehousesCheckBox.setValue(preferences.iswareHouseEnabled());
		} else {
			hiddenPanel.setVisible(false);
		}
	}

	public void onSave() {

		if (servicesOnly.getValue())
			preferences.setSellServices(true);
		if (productsOnly.getValue())
			preferences.setSellProducts(true);
		if (both.getValue()) {
			preferences.setSellServices(true);
			preferences.setSellProducts(true);
		}

		preferences.setTaxTrack(trackCheckbox.getValue());
		preferences.setTaxPerDetailLine(oneperdetaillineRadioButton.getValue());
		preferences.setTrackPaidTax(enableTaxCheckbox.getValue());
		preferences.setInventoryEnabled(inventoryCheckBox.getValue());
		if (inventoryCheckBox.getValue()) {
			preferences.setwareHouseEnabled(true);
		} else {
			preferences.setwareHouseEnabled(false);
		}
	}

	@Override
	public boolean canShow() {
		return true;
	}

	@Override
	protected boolean validate() {
		/*
		 * if ((!(servicesOnly.getValue() || productsOnly.getValue() || both
		 * .getValue()))) { Accounter.showError(accounterMessages
		 * .pleaseEnter(accounterConstants.details())); return false; } else
		 */if (!(servicesOnly.getValue() || productsOnly.getValue() || both
				.getValue())) {
			Accounter.showMessage(accounterConstants.whatDoYouSell());
			return false;
		} else {
			return true;
		}
	}

}
