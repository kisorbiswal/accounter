/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author vimukti36
 * 
 */
public class ProductAndServicesOption extends AbstractPreferenceOption {

	private static ProductAndServicesOptionUiBinder uiBinder = GWT
			.create(ProductAndServicesOptionUiBinder.class);
	@UiField
	VerticalPanel viewPanel;
	@UiField
	RadioButton servicesOnly;
	@UiField
	Label servicesOnlyText;
	@UiField
	Label headerLabel;
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

	interface ProductAndServicesOptionUiBinder extends
			UiBinder<Widget, ProductAndServicesOption> {
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
	public ProductAndServicesOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public ProductAndServicesOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	public void createControls() {

		headerLabel.setText(constants.whatDoYouSell());
		// servicesOnlyText.setText(constants.whatDoYouSell());
		servicesOnly.setText(constants.services_labelonly());
		servicesOnlyText.setText(constants.servicesOnly());
		productsOnly.setText(constants.products_labelonly());
		productsOnlyText.setText(constants.productsOnly());
		both.setText(constants.bothservicesandProduct_labelonly());
		bothText.setText(constants.bothServicesandProducts());
	}

	@Override
	public String getTitle() {
		return constants.productAndServices();
	}

	@Override
	public void onSave() {

		if (servicesOnly.getValue())
			companyPreferences.setSellServices(true);
		if (productsOnly.getValue())
			companyPreferences.setSellProducts(true);
		if (both.getValue()) {
			companyPreferences.setSellServices(true);
			companyPreferences.setSellProducts(true);
		}
	}

	@Override
	public String getAnchor() {
		return constants.productAndServices();
	}

	@Override
	public void initData() {

		boolean sellServices = companyPreferences.isSellServices();
		if (sellServices)
			servicesOnly.setValue(true);
		boolean sellProducts = companyPreferences.isSellProducts();
		if (sellProducts)
			productsOnly.setValue(true);
		if (sellServices && sellProducts)
			both.setValue(true);
	}
}
