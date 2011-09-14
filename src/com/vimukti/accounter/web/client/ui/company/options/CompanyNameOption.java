/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Prasanna Kumar G
 * 
 */
public class CompanyNameOption extends AbstractPreferenceOption {

	private static CompanyNameOptionUiBinder uiBinder = GWT
			.create(CompanyNameOptionUiBinder.class);
	@UiField
	Label companyNameLabel;
	@UiField
	TextBox companyNameTextBox;

	// @UiField
	// CheckBox legalNmaeCheckBox;
	// @UiField
	// Label companyLegalNameLabel;
	// @UiField
	// TextBox comapnyLegalNameTextBox;
	// @UiField
	// HorizontalPanel comapnyLegalNameHeaderPanel;

	interface CompanyNameOptionUiBinder extends
			UiBinder<Widget, CompanyNameOption> {
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
	public CompanyNameOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	private void createControls() {
		companyNameLabel.setText(Accounter.constants().companyName());
		// companyLegalNameLabel.setText(Accounter.constants().legalName());
		// legalNmaeCheckBox
		// .setText(Accounter.constants().getDifferentLegalName());
		// legalNmaeCheckBox.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// comapnyLegalNameHeaderPanel.setVisible(legalNmaeCheckBox
		// .getValue());
		// }
		// });
	}

	private void initData() {
		companyNameTextBox.setText(company.getDisplayName());
		// comapnyLegalNameTextBox.setText(company.getDisplayName());
	}

	@Override
	public void onSave() {
		company.setName(companyNameTextBox.getValue());
		// company.setTradingName(comapnyLegalNameTextBox.getValue());
	}

	@Override
	public String getTitle() {
		return Accounter.constants().name();
	}

	@Override
	public String getAnchor() {
		return Accounter.constants().name();
	}

}
