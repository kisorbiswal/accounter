package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Sai Prasd N
 * 
 */
public class CreditsOption extends AbstractPreferenceOption {

	private static CreditsOptionUiBinder uiBinder = GWT
			.create(CreditsOptionUiBinder.class);

	interface CreditsOptionUiBinder extends UiBinder<Widget, CreditsOption> {
	}

	public CreditsOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	@UiField
	CheckBox creditsApplyAutomatic;
	@UiField
	Label label;

	public CreditsOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));

	}

	@Override
	public void onSave() {
		getCompanyPreferences().setCreditsApplyAutomatically(
				creditsApplyAutomatic.getValue());

	}

	@Override
	public String getAnchor() {
		return messages.company();
	}

	@Override
	public void createControls() {
		label.setText(messages.creditsOptionLabelTxt());
		creditsApplyAutomatic.setText(messages.automaticallyApplycredits());

	}

	@Override
	public void initData() {
		creditsApplyAutomatic.setValue(getCompanyPreferences()
				.isCreditsApplyAutomaticEnable());

	}

	@Override
	public String getTitle() {
		return messages.creditsApply();
	}

}
