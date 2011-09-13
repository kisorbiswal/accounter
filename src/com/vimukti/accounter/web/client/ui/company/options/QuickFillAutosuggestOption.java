/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author vimukti36
 * 
 */
public class QuickFillAutosuggestOption extends AbstractPreferenceOption {

	private static QuickFillAutosuggestOptionUiBinder uiBinder = GWT
			.create(QuickFillAutosuggestOptionUiBinder.class);
	@UiField
	CheckBox quickFillAutosuggestCheckBox;

	interface QuickFillAutosuggestOptionUiBinder extends
			UiBinder<Widget, QuickFillAutosuggestOption> {
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
	public QuickFillAutosuggestOption() {
		initWidget(uiBinder.createAndBindUi(this));
		craeteControls();
	}

	private void craeteControls() {
		quickFillAutosuggestCheckBox.setText(constants
				.enableQuickFillAutosuggest());
		quickFillAutosuggestCheckBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});
	}

	public QuickFillAutosuggestOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	@Override
	public String getTitle() {
		return "QuickFill Autosuggest";
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getAnchor() {
		// TODO Auto-generated method stub
		return constants.company();
	}

}
