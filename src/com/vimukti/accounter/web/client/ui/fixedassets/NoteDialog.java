package com.vimukti.accounter.web.client.ui.fixedassets;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;

public class NoteDialog extends BaseDialog {

	public TextAreaItem noteArea;

	public NoteDialog(String title, String desc) {
		super(title, desc);
		createControl();
		center();
	}

	private void createControl() {
		mainPanel.setSpacing(3);
		Label noteLbl = new Label(Accounter.constants().note());
		noteArea = new TextAreaItem();
		noteArea.setWidth(100);
		DynamicForm noteForm = new DynamicForm();
		noteForm.setWidth("100%");
		noteForm.setFields(noteArea);
		noteForm.removeCell(0, 0);
		VerticalPanel notePanel = new VerticalPanel();
		notePanel.setWidth("100%");
		notePanel.add(noteLbl);
		notePanel.add(noteForm);
		setBodyLayout(notePanel);
		okbtn.setText(Accounter.constants().save());
		setWidth("350");
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(Throwable exception) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().addNote();
	}

}
