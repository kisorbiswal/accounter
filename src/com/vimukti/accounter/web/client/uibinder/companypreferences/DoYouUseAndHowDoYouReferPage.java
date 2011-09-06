package com.vimukti.accounter.web.client.uibinder.companypreferences;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class DoYouUseAndHowDoYouReferPage extends AbstractCompanyInfoPanel {

	private static DoYouUseAndHowDoYouReferPageUiBinder uiBinder = GWT
			.create(DoYouUseAndHowDoYouReferPageUiBinder.class);

	interface DoYouUseAndHowDoYouReferPageUiBinder extends
			UiBinder<Widget, DoYouUseAndHowDoYouReferPage> {
	}

	public DoYouUseAndHowDoYouReferPage() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

}
