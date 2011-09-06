package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SetupTDSSelectionPage extends AbstractSetupPage {

	private static SetupTDSSelectionPageUiBinder uiBinder = GWT
			.create(SetupTDSSelectionPageUiBinder.class);

	interface SetupTDSSelectionPageUiBinder extends
			UiBinder<Widget, SetupTDSSelectionPage> {
	}

	public SetupTDSSelectionPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@UiField
	VerticalPanel viewPanel;
	@UiField
	RadioButton tdsYes;
	@UiField
	RadioButton tdsNo;
	@UiField
	Label headerLabel;

	public SetupTDSSelectionPage(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(accounterConstants.doYouWantTDS());
		tdsYes.setText(accounterConstants.yes());
		tdsNo.setText(accounterConstants.no());
	}

	@Override
	protected void onSave() {
		this.preferences.setTDSEnabled(tdsYes.getValue());

	}

	@Override
	protected void onLoad() {
		tdsNo.setValue(true);

	}

	@Override
	protected boolean validate() {

		return true;
	}

	@Override
	public boolean canShow() {

		return true;
	}

}
