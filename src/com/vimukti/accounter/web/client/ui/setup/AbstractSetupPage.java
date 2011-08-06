package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class AbstractSetupPage extends AbstractBaseView {
	protected boolean progress;
	protected Label header;
	protected AccounterConstants accounterConstants;
	protected AccounterMessages accounterMessages;
	protected ClientCompanyPreferences preferences;
	// protected static AbstractSetupPage setupPage;
	protected VerticalPanel mainPanel;
	protected HorizontalPanel buttonPanel, setupMainPanel;
	protected Button skipButton, backButton, nextButton;

	public AbstractSetupPage() {
		preferences = Accounter.getCompany().getPreferences();
		accounterConstants = Accounter.constants();
		accounterMessages = Accounter.messages();
		createControls();
	}

	public abstract String getHeader();

	// public abstract String getFooter() {
	// return null;
	// }
	public boolean getProgress() {
		return progress;
	}

	public void setProgress(boolean progress) {
		this.progress = progress;
	}

	public abstract VerticalPanel getPageBody();

	private void createControls() {
		mainPanel = new VerticalPanel();
		setupMainPanel = new HorizontalPanel();
		HorizontalPanel back_nextbuttonPanel = new HorizontalPanel();
		// progressPanel = new SetupProgressPanel(setupPage);

		buttonPanel = new HorizontalPanel();
		skipButton = new Button(Accounter.constants().skip());
		backButton = new Button(Accounter.constants().back());
		nextButton = new Button(Accounter.constants().next());

		buttonPanel.add(skipButton);
		skipButton.getElement().setAttribute("float", "left");
		back_nextbuttonPanel.add(backButton);
		back_nextbuttonPanel.add(nextButton);
		buttonPanel.add(back_nextbuttonPanel);
		back_nextbuttonPanel.getElement().setAttribute("float", "right");
		header = new Label(getHeader());
		mainPanel.add(header);
		header.addStyleName("setup_header_label");
		mainPanel.add(getPageBody());
		if ((this instanceof SetupStartPage)
				|| (this instanceof SetupComplitionPage)) {

		} else {
			mainPanel.add(buttonPanel);
		}

		setupMainPanel.add(mainPanel);
		// setupMainPanel.add(progressPanel);
		setupMainPanel.setSize("100%", "100%");
		add(setupMainPanel);
		setSize("96%", "100%");
		addStyleName("setup_panel");
		skipButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				SetupSkipDialog skipDialog = new SetupSkipDialog();
				skipDialog.show();
				skipDialog.center();
			}
		});

		nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				onSave();
				onNext();
			}
		});

		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				onBack();
			}
		});

	}

	protected abstract void onBack();

	protected abstract void onNext();

	public abstract void onLoad();

	public abstract void onSave();

	public AbstractSetupPage getView() {
		return this;
	}
}
