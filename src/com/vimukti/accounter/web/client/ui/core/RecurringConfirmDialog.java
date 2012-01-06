package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;

public class RecurringConfirmDialog extends CustomDialog {

	protected HorizontalPanel headerLayout;
	protected HorizontalPanel bodyLayout;
	protected HorizontalPanel footerLayout;

	protected Button gotoRecurringBtn;
	protected Button keepWorkingBtn;
	protected VerticalPanel mainPanel, mainVLayPanel;

	public RecurringConfirmDialog() {
		super(true);
		setText(messages.pleaseConfirm());
		setModal(true);
		createControls();
		gotoRecurringBtn.setFocus(true);
		sinkEvents(Event.ONKEYPRESS);
		sinkEvents(Event.ONMOUSEOVER);
	}

	private void createControls() {

		/**
		 * Header Layout
		 */
		headerLayout = new HorizontalPanel();
		headerLayout.setWidth("430px");
		Label label = new Label();
		label.setText(messages.youSuccessfullyCreatedRecurringTemplate());
		headerLayout.add(label);

		/**
		 * Body LayOut
		 */
		bodyLayout = new HorizontalPanel();
		bodyLayout.setWidth("100%");

		/**
		 * Footer Layout
		 */
		footerLayout = new HorizontalPanel();
		footerLayout.setSpacing(3);
		// footerLayout.addStyleName("dialogfooter");

		this.gotoRecurringBtn = new Button(
				messages.gotoRecurringTransactionsList());

		gotoRecurringBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				removeFromParent();
				History.newItem(HistoryTokens.RECURRINGTRANSACTIONS, true);
			}
		});

		keepWorkingBtn = new Button(messages.keepWorkingOnThisTransaction());
		keepWorkingBtn.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				removeFromParent();
			}
		});

		footerLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		footerLayout.add(gotoRecurringBtn);
		footerLayout.add(keepWorkingBtn);

		footerLayout.setCellHorizontalAlignment(gotoRecurringBtn,
				HasHorizontalAlignment.ALIGN_RIGHT);
		footerLayout.setCellHorizontalAlignment(keepWorkingBtn,
				HasHorizontalAlignment.ALIGN_RIGHT);

		/**
		 * adding all Layouts to Window
		 */

		mainPanel = new VerticalPanel();
		mainPanel.setSize("100%", "100%");
		mainPanel.add(headerLayout);
		mainPanel.setCellVerticalAlignment(headerLayout,
				HasVerticalAlignment.ALIGN_TOP);
		mainPanel.add(bodyLayout);
		mainPanel.add(footerLayout);
		mainPanel.setCellHorizontalAlignment(footerLayout,
				HasHorizontalAlignment.ALIGN_RIGHT);

		add(mainPanel);

	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONKEYPRESS:
			int keycode = event.getKeyCode();
			if (KeyCodes.KEY_ESCAPE == keycode) {
				removeFromParent();
			}
			break;
		default:
			break;
		}
		super.onBrowserEvent(event);
	}

	@Override
	protected boolean onCancel() {
		return true;
	}
}
