package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class AbstractSetupPage extends Composite {

	protected VerticalPanel viewPanel;
	protected AccounterConstants accounterConstants = Accounter.constants();
	protected AccounterMessages accounterMessages = Accounter.messages();

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
