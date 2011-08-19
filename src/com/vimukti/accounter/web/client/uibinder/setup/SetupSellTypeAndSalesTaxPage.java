/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

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
	RadioButton ServicesOnly;
	@UiField
	Label ServicesOnlyText;
	@UiField
	RadioButton ProductsOnly;
	@UiField
	Label ProductsOnlyText;
	@UiField
	RadioButton Both;
	@UiField
	Label BothText;
	@UiField
	VerticalPanel Sell;
	@UiField
	VerticalPanel SalesTax;
	@UiField
	Label SalesTaxNoText;
	@UiField
	RadioButton SalesTaxNo;
	@UiField
	RadioButton SalesTaxYes;
	@UiField
	HTML SalesTaxHead;

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
	protected void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createControls() {
		// TODO Auto-generated method stub

	}

}
