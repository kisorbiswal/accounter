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

/**
 * @author Prasanna Kumar G
 * 
 */
public class CompanyNameOption extends AbstractPreferenceOption {

	private static CompanyNameOptionUiBinder uiBinder = GWT
			.create(CompanyNameOptionUiBinder.class);
	@UiField Label companyNameLabel;
	@UiField TextBox companyNameTextBox;

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
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAnchor() {
		// TODO Auto-generated method stub
		return null;
	}

}
