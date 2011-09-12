/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author vimukti36
 * 
 */
public class ClosingBooksOption extends AbstractPreferenceOption {

	private static ClosingBooksOptionUiBinder uiBinder = GWT
			.create(ClosingBooksOptionUiBinder.class);
	@UiField
	CheckBox closingBooksCheckBox;

	interface ClosingBooksOptionUiBinder extends
			UiBinder<Widget, ClosingBooksOption> {
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
	public ClosingBooksOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	private void createControls() {
		closingBooksCheckBox.setText(constants.closingthebooks());
	}

	public ClosingBooksOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	@Override
	public String getTitle() {
		return "Closing the Books";
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAnchor() {
		return constants.company();
	}

}
