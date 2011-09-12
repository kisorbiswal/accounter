/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author vimukti36
 * 
 */
public class TaxFormOption extends AbstractPreferenceOption {

	private static TaxFormOptionUiBinder uiBinder = GWT
			.create(TaxFormOptionUiBinder.class);
	@UiField
	Label radioButtonHeaderLabel;
	@UiField
	RadioButton oneOwnerRadioButton;
	@UiField
	RadioButton twoOrmoreownersRadioButton;
	@UiField
	RadioButton oneormoreshareholdersRadioButton;
	@UiField
	RadioButton nonprofitRadioButton;
	@UiField
	RadioButton otherAndNoneRadioButton;
	@UiField
	VerticalPanel radioButtonsPanel;
	@UiField
	HorizontalPanel headerLabelPanel;
	@UiField
	VerticalPanel radioButtonsMainPanel;

	interface TaxFormOptionUiBinder extends UiBinder<Widget, TaxFormOption> {
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
	public TaxFormOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	private void createControls() {
		radioButtonHeaderLabel.setText(Accounter.constants().taxform());

		oneOwnerRadioButton.setName(Accounter.constants().taxFormGroup());
		oneOwnerRadioButton.setHTML(Accounter.constants().OneOwener());
		twoOrmoreownersRadioButton
				.setName(Accounter.constants().taxFormGroup());
		twoOrmoreownersRadioButton.setHTML(Accounter.constants()
				.twoOrMoreOwners());
		oneormoreshareholdersRadioButton.setName(Accounter.constants()
				.taxFormGroup());
		oneormoreshareholdersRadioButton.setHTML(Accounter.constants()
				.oneormoreshareholders());
		nonprofitRadioButton.setName(Accounter.constants().taxFormGroup());
		nonprofitRadioButton.setHTML(Accounter.constants().nonProfit());
		otherAndNoneRadioButton.setName(Accounter.constants().taxFormGroup());
		otherAndNoneRadioButton.setHTML(Accounter.constants().otherorNone());

	}

	private void initData() {
		int taxForm = company.getTaxForm();

	}

	public TaxFormOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	@Override
	public String getTitle() {
		return "Tax Form";
	}

	@Override
	public void onSave() {
	}

	@Override
	public String getAnchor() {
		return constants.company();
	}

}
