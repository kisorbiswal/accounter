package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class SetupSkipDialog extends AbstractSetupPage {

	private static SetupSkipDialogUiBinder uiBinder = GWT
			.create(SetupSkipDialogUiBinder.class);

	interface SetupSkipDialogUiBinder extends UiBinder<Widget, SetupSkipDialog> {
	}

	public SetupSkipDialog() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createControls() {
		// TODO Auto-generated method stub

	}

}
