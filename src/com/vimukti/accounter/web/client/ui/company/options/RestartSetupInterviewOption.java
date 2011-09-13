package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class RestartSetupInterviewOption extends AbstractPreferenceOption {

	private static RestartSetupInterviewOptionUiBinder uiBinder = GWT
			.create(RestartSetupInterviewOptionUiBinder.class);
	@UiField
	Button restartButton;
	@UiField
	Label restartLable;

	interface RestartSetupInterviewOptionUiBinder extends
			UiBinder<Widget, RestartSetupInterviewOption> {

	}

	public RestartSetupInterviewOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	private void createControls() {
		restartLable.setText(constants.restartsetupinterviews());
		restartButton.setText(constants.restart());
		restartButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});
	}

	public RestartSetupInterviewOption(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public String getTitle() {
		return "Setup Interviews";
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
