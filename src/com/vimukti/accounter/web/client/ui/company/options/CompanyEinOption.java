/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author vimukti36
 * 
 */
public class CompanyEinOption extends AbstractPreferenceOption {

	private static CompanyEinOptionUiBinder uiBinder = GWT
			.create(CompanyEinOptionUiBinder.class);
	@UiField
	TextBox EINTextBox;

	interface CompanyEinOptionUiBinder extends
			UiBinder<Widget, CompanyEinOption> {
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
	public CompanyEinOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	private void initData() {

	}

	private void createControls() {
		EINTextBox
				.setText(Accounter.constants().employerIdentificationNumber());
	}

	public CompanyEinOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	@Override
	public String getTitle() {
		return "EIN";
	}

	@Override
	public void onSave() {
		String EINvalue = EINTextBox.getValue();

	}

	@Override
	public String getAnchor() {
		return constants.company();
	}
}
