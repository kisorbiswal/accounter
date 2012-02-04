package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.QuickAddListener;
import com.vimukti.accounter.web.client.ui.forms.TextBoxItem;

/**
 * @author Srikanth Jessu
 * 
 */
public class QuickAddDialog extends BaseDialog<IAccounterCore> {

	private TextBoxItem textBox;

	private QuickAddListener<? extends IAccounterCore> listener;

	public QuickAddDialog(String header) {
		super(header);
		createControls();
	}

	private void createControls() {
		Label nameLabel = new Label();
		textBox = new TextBoxItem();

		nameLabel.setText(messages.name());
		// horizontalPanel.getElement().getStyle().setMargin(5, Unit.PX);

		bodyLayout.add(nameLabel);
		bodyLayout.add(textBox);

		okbtn.setText(messages.QuickAdd());
		Button addAllInfoBtn = new Button(messages.AddAllInfo());

		addAllInfoBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (listener != null) {
					listener.onAddAllInfo(textBox.getText());
				}
				hide();

			}
		});
		footerLayout.add(addAllInfoBtn);
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

	}

	@Override
	protected boolean onCancel() {
		if (listener != null) {
			listener.onCancel();
		}
		return true;
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

	@Override
	protected ValidationResult validate() {
		IAccounterCore data = listener.getData(textBox.getText());
		ValidationResult validate = super.validate();
		if (data == null) {
			validate.addError(textBox, messages.alreadyExist());
		}
		return validate;
	}

	@Override
	protected boolean onOK() {
		quickAdd();
		return true;
	}

}
