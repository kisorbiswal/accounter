package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.CustomMenuBar;

public class InvoiceBrandingView extends AbstractBaseView {

	private HTML generalSettingsHTML, invoiceBrandingHtml;
	private VerticalPanel mainPanel, titlePanel;
	private Button newBrandButton, automaticButton;
	private HorizontalPanel buttonPanel;

	private void createControls() {
		mainPanel = new VerticalPanel();
		titlePanel = new VerticalPanel();
		generalSettingsHTML = new HTML(
				"<p><font size='1px'>General Settings</font></p>");
		invoiceBrandingHtml = new HTML(
				"<p><font size='4px'>Innvoice Branding<font></p>");
		generalSettingsHTML.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getGeneralSettingsAction().run(null,
						false);
			}
		});
		titlePanel.add(generalSettingsHTML);
		titlePanel.add(invoiceBrandingHtml);

		buttonPanel = new HorizontalPanel();
		newBrandButton = new Button("New Branding Theme");
		newBrandButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getNewBrandThemeAction().run(null, false);
			}
		});
		newBrandButton.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				DialogBox dialogBox = new DialogBox();
				dialogBox.add(getNewBrandMenu());
				dialogBox.setPopupPosition(newBrandButton.getAbsoluteLeft(),
						newBrandButton.getAbsoluteTop()
								+ newBrandButton.getOffsetHeight());
				dialogBox.show();
				dialogBox.setAutoHideEnabled(true);
			}
		});
		automaticButton = new Button("Automatic Sequencing");

		buttonPanel.add(newBrandButton);
		buttonPanel.add(automaticButton);

		mainPanel.add(titlePanel);
		mainPanel.add(buttonPanel);
		add(mainPanel);
	}

	private CustomMenuBar getNewBrandMenu() {
		CustomMenuBar menuBar = new CustomMenuBar();
		menuBar.addItem("Standard Theme", getNewBrandCommand(1));
		menuBar.addItem("Custom .docx Theme", getNewBrandCommand(2));
		getNewBrandCommand(0);
		return menuBar;

	}

	private Command getNewBrandCommand(final int i) {
		Command command = new Command() {
			@Override
			public void execute() {
				switch (i) {
				case 1:

					break;

				case 2:
					break;
				}
			}
		};
		return command;
		// TODO Auto-generated method stub

	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		super.init();
		try {
			createControls();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

}
