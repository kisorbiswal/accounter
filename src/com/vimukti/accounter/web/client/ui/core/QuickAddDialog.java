package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.QuickAddListener;
import com.vimukti.accounter.web.client.ui.forms.CustomDialog;
import com.vimukti.accounter.web.client.ui.forms.TextBoxItem;

/**
 * @author Srikanth Jessu
 * 
 */
public class QuickAddDialog extends CustomDialog {

	private TextBoxItem textBox;
	private Button quickAddBtn;

	private QuickAddListener<? extends IAccounterCore> listener;

	public QuickAddDialog(String header) {
		super();
		setText(header);
		createControls();
	}

	private void createControls() {
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		Label nameLabel = new Label();
		textBox = new TextBoxItem();

		nameLabel.setText(Accounter.constants().name());
		horizontalPanel.getElement().getStyle().setMargin(5, Unit.PX);
		horizontalPanel.setSpacing(6);

		horizontalPanel.add(nameLabel);
		horizontalPanel.add(textBox);

		quickAddBtn = new Button("Quick Add");
		Button addAllInfoBtn = new Button("Add All Info");
		Button cancelBtn = new Button("Cancel");

		quickAddBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (listener != null) {
					quickAdd();
				}
				hide();
			}
		});

		addAllInfoBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (listener != null) {
					listener.onAddAllInfo(textBox.getText());
				}
				hide();
			}
		});

		cancelBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// Doubt
				if (listener != null) {
					listener.onCancel();
				}
				hide();

			}
		});

		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.add(quickAddBtn);
		buttonsPanel.add(addAllInfoBtn);
		buttonsPanel.add(cancelBtn);

		buttonsPanel.getElement().getStyle().setMargin(5, Unit.PX);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(horizontalPanel);
		mainPanel.add(buttonsPanel);

		add(mainPanel);
		center();

	}

	private void quickAdd() {
		IAccounterCore data = listener.getData(textBox.getText());
		if (data == null) {
			return;
		}
		Accounter.createOrUpdate(listener, data);
	}

	public void setListener(QuickAddListener<? extends IAccounterCore> listener) {
		this.listener = listener;
	}

	public void setDefaultText(String text) {
		textBox.setValue(text);
	}

	public void setFocus() {
		this.quickAddBtn.setFocus(true);
	}

	@Override
	protected void onAttach() {

		super.onAttach();
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				setFocus();
			}
		});

	}
}
