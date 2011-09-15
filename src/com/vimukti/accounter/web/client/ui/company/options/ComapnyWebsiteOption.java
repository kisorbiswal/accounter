package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.Accounter;

public class ComapnyWebsiteOption extends AbstractPreferenceOption {

	@UiField
	Label companyWebsiteHeaderLabel;

	@UiField
	TextBox companyWebsiteTextBox;

	private static ComapnyWebsiteOptionUiBinder uiBinder = GWT
			.create(ComapnyWebsiteOptionUiBinder.class);

	interface ComapnyWebsiteOptionUiBinder extends
			UiBinder<Widget, ComapnyWebsiteOption> {
	}

	public ComapnyWebsiteOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public void initData() {
		companyWebsiteTextBox.setText(company.getWebSite());
	}

	public void createControls() {
		companyWebsiteHeaderLabel.setText(Accounter.constants().webSite());
	}

	@Override
	public String getTitle() {
		return Accounter.constants().webSite();
	}

	@Override
	public void onSave() {
		company.setWebSite(companyWebsiteTextBox.getValue());

	}

	@Override
	public String getAnchor() {
		return Accounter.constants().webSite();
	}

}
