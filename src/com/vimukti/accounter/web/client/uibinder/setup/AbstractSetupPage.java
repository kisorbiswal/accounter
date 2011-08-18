package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractSetupPage extends Composite {

	protected VerticalPanel viewPanel;

	private static AbstractSetupPageUiBinder uiBinder = GWT
			.create(AbstractSetupPageUiBinder.class);

	interface AbstractSetupPageUiBinder extends
			UiBinder<Widget, AbstractSetupPage> {
	}

	@UiField
	Label header;
	VerticalPanel mainViewPanel;

	public AbstractSetupPage() {
		initWidget(uiBinder.createAndBindUi(this));
		header.setText(getViewHeader());
		mainViewPanel.add(getViewBody());
	}

	protected abstract String getViewHeader();

	protected abstract VerticalPanel getViewBody();

	protected abstract void onSave();

	protected abstract void onLoad();
}
