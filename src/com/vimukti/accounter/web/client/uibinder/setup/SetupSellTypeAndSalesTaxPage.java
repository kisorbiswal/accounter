/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientCompany;
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
	VerticalPanel salesTax;
	@UiField
	RadioButton salesTaxNo;
	@UiField
	RadioButton salesTaxYes;
	@UiField
	HTML salesTaxHead;
	@UiField
	Label headerLabel;

	@UiField
	VerticalPanel vat;
	@UiField
	RadioButton vatNo;
	@UiField
	RadioButton vatYes;
	@UiField
	HTML vatHeader;
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

		salesTaxHead.setHTML(accounterConstants.doyouchargesalestax());
		salesTaxNo.setText(accounterConstants.no());
		salesTaxYes.setText(accounterConstants.yes());

		vatHeader.setHTML(Accounter.constants().doyouchargeVat());
		vatNo.setText(accounterConstants.no());
		vatYes.setText(accounterConstants.yes());

		trackCheckbox.setText(accounterConstants.chargeOrTrackTax());
		trackLabel.setText(accounterConstants.descChrageTrackTax());
		trackLabel.setStyleName("organisation_comment");
		taxItemTransactionLabel.setText(accounterConstants
				.taxtItemTransaction());
		onepeTransactionRadioButton.setText(accounterConstants
				.onepertransaction());
		oneperTransactionLabel.setText(accounterConstants.oneperDescription());
		oneperTransactionLabel.setStyleName("organisation_comment");
		oneperdetaillineRadioButton.setText(accounterConstants
				.oneperdetailline());
		oneperdetaillineLabel.setText(accounterConstants
				.oneperDetailDescription());
		oneperTransactionLabel.setStyleName("organisation_comment");
		enableTaxCheckbox.setText(accounterConstants.enableTracking());
		enableTaxLabel.setText(accounterConstants.enableTrackingDescription());
		enableTaxLabel.setStyleName("organisation_comment");

		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
			salesTax.setVisible(false);
		}

		if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			vat.setVisible(false);
		}
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

		if (preferences.isRegisteredForVAT()) {
			vatYes.setValue(true);
		} else {
			vatNo.setValue(true);
		}
		if (preferences.isChargeSalesTax()) {
			salesTaxYes.setValue(true);
		} else {
			salesTaxNo.setValue(true);
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
		if (trackCheckbox.getValue()) {
			preferences.setTaxTrack(true);
			preferences.setTaxPerDetailLine(oneperdetaillineRadioButton
					.getValue());

			if (enableTaxCheckbox.getValue()) {
				preferences.setTaxTrack(true);
			}
		}

	}

	@Override
	public boolean canShow() {
		return true;
	}

	@Override
	protected boolean validate() {
		if ((!(servicesOnly.getValue() || productsOnly.getValue() || both
				.getValue()))
				&& (!(salesTaxYes.getValue() || salesTaxNo.getValue()))) {
			Accounter.showError(accounterMessages
					.pleaseEnter(accounterConstants.details()));
			return false;
		} else if (!(servicesOnly.getValue() || productsOnly.getValue() || both
				.getValue())) {
			Accounter.showMessage(accounterConstants.whatDoYouSell());
			return false;
		} else if (!(salesTaxYes.getValue() || salesTaxNo.getValue())) {
			Accounter.showMessage(salesTaxHead.getText());
			return false;
		} else {
			return true;
		}
	}

}
