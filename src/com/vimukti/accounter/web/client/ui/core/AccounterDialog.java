package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.core.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;

public class AccounterDialog extends CustomDialog {
	public ErrorDialogHandler dialogHandler;
	public AccounterType type;
	public String message;
	private boolean isError;
	private AccounterButton okButton;
	private AccounterButton cancelButton;

	public AccounterDialog(String mesg, AccounterType type) {

		super(true);
		this.message = mesg;
		this.type = type;
		createControls();
		show();
		center();
		// if (type == AccounterType.INFORMATION) {
		// Timer timer = new Timer() {
		//
		// @Override
		// public void run() {
		// okClicked();
		// Accounter.stopExecution();
		// }
		// };
		// timer.schedule(4000);
		// }
		sinkEvents(Event.ONKEYPRESS);
		sinkEvents(Event.ONMOUSEOVER);
	}

	public AccounterDialog(String mesg, AccounterType type,
			ErrorDialogHandler handler) {

		super(true);
		this.message = mesg;
		this.type = type;
		this.dialogHandler = handler;
		createControls();
		show();
		center();
	}

	public void createControls() {

		VerticalPanel bodyLayout = new VerticalPanel();

		HorizontalPanel headerLayout = new HorizontalPanel();

		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		buttonLayout.setSpacing(5);
		// buttonLayout.setAutoHeight();
		// buttonLayout.setHeight("20%");

		AccounterButton yesButton;
		AccounterButton noButton;

		String imageUrl;

		if ((isError = this.type.equals(AccounterType.ERROR))
				|| this.type.equals(AccounterType.INFORMATION)) {
			// set imagePath for error type

			if (isError) {

				imageUrl = "/images/error-icon.png";
				setText("ERROR");

			} else {

				imageUrl = "/images/information-icon.png";
				setText("INFORMATION");
			}

			okButton = new AccounterButton(FinanceApplication
					.getCompanyMessages().ok());
			okButton.setWidth("80px");

			// buttonLayout.setAlign(Alignment.RIGHT);
			buttonLayout.add(okButton);
			okButton.enabledButton();
			okButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					okClicked();
					Accounter.stopExecution();
				}

			});

		} else {

			imageUrl = "/images/warn-icon.png";
			setText("WARNING");
			yesButton = new AccounterButton(FinanceApplication
					.getCompanyMessages().yes());
			yesButton.setWidth("60");
			noButton = new AccounterButton(FinanceApplication
					.getCompanyMessages().no());
			noButton.setWidth("60");
			yesButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					try {
						yesClicked();
					} catch (InvalidEntryException e) {
						e.printStackTrace();
					}
				}

			});
			noButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					try {
						noClicked();
					} catch (InvalidEntryException e) {
					}
				}

			});
			// buttonLayout.setAlign(Alignment.RIGHT);
			if (this.type.equals(AccounterType.WARNINGWITHCANCEL)) {
				cancelButton = new AccounterButton(FinanceApplication
						.getCompanyMessages().cancel());
				buttonLayout.add(yesButton);
				buttonLayout.add(noButton);
				buttonLayout.add(cancelButton);
				yesButton.enabledButton();
				noButton.enabledButton();
				cancelButton.enabledButton();
				cancelButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						removeFromParent();
					}

				});
			} else {
				buttonLayout.add(yesButton);
				buttonLayout.add(noButton);
				yesButton.enabledButton();
				noButton.enabledButton();
			}

		}

		Image image = new Image(imageUrl);
		HorizontalPanel imageLayout = new HorizontalPanel();
		imageLayout.add(image);
		Label msgLabel = new Label(this.message);

		headerLayout.add(imageLayout);
		headerLayout.add(msgLabel);
		headerLayout.setCellVerticalAlignment(msgLabel,
				HasVerticalAlignment.ALIGN_MIDDLE);
		headerLayout.setSpacing(5);
		headerLayout.setHeight("10%");
		buttonLayout.setHeight("10%");

		bodyLayout.add(headerLayout);
		bodyLayout.add(buttonLayout);
		bodyLayout.setCellHorizontalAlignment(buttonLayout,
				HasHorizontalAlignment.ALIGN_RIGHT);
		bodyLayout.setSpacing(1);

		bodyLayout.setSize("305", "5");

		setModal(true);
		add(bodyLayout);
	}

	protected void noClicked() throws InvalidEntryException {
		if (dialogHandler != null)
			if (dialogHandler.onNoClick()) {
				removeFromParent();
			}
	}

	protected void yesClicked() throws InvalidEntryException {
		if (dialogHandler != null) {
			if (dialogHandler.onYesClick())
				removeFromParent();
		}

	}

	protected void okClicked() {
		removeFromParent();
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONKEYPRESS:
			int keycode = event.getKeyCode();
			if (KeyCodes.KEY_ESCAPE == keycode) {
				this.okClicked();
			}
			break;
		case Event.ONMOUSEOVER:
			if (cancelButton != null)
				cancelButton.setFocus(true);
			else if (okButton != null) {
				okButton.setFocus(true);
			}

		default:
			break;
		}
		super.onBrowserEvent(event);
	}

	@Override
	protected void onLoad() {
		if (cancelButton != null)
			cancelButton.setFocus(true);
		else if (okButton != null) {
			okButton.setFocus(true);
		}

		super.onLoad();
	}
}
