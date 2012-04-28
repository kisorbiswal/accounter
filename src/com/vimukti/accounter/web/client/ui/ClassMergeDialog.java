package com.vimukti.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.combo.ClassListCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class ClassMergeDialog extends BaseDialog<ClientAccounterClass>
		implements AsyncCallback<Void> {

	private DynamicForm form;
	private DynamicForm form1;

	private ClassListCombo classFromCombo;
	private ClassListCombo classToCombo;
	private TextItem classFromIDTextItem;
	private TextItem classToIDTextItem;

	private ClientAccounterClass clientFromClass;
	private ClientAccounterClass clientToClass;

	public ClassMergeDialog(String title, String descript) {
		super(title, descript);
		this.getElement().setId("ClassMergeDialog");
		okbtn.setText(messages.merge());
		createControls();
		center();
		clientToClass = null;
		clientFromClass = null;
	}

	private void createControls() {
		form = new DynamicForm("form");
		form1 = new DynamicForm("form1");
		StyledPanel layout = new StyledPanel("layout");
		StyledPanel layout1 = new StyledPanel("layout1");
		StyledPanel horizontalPanel = new StyledPanel("horizontalPanel");
		classFromCombo = createFromClassCombo();
		classToCombo = createToClassCombo();

		classFromIDTextItem = new TextItem(
				messages.classId(), "classIDTextItem");
		classFromIDTextItem.setEnabled(false);

		classToIDTextItem = new TextItem(messages.classId(), "classIDTextItem1");
		classToIDTextItem.setEnabled(false);

		classFromCombo.setRequired(true);
		classToCombo.setRequired(true);
		form.add(classFromCombo, classFromIDTextItem);
		form1.add(classToCombo, classToIDTextItem);
		layout.add(form);
		layout1.add(form1);
		horizontalPanel.add(layout);
		horizontalPanel.add(layout1);
		setBodyLayout(horizontalPanel);

	}

	private ClassListCombo createToClassCombo() {

		classToCombo = new ClassListCombo(messages.mergeTo(), false);
		classToCombo.setRequired(true);
		classToCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccounterClass>() {
					@Override
					public void selectedComboBoxItem(
							ClientAccounterClass selectItem) {
						clientToClass = selectItem;
						classToSelected(selectItem);
					}
				});
		return classToCombo;
	}

	private ClassListCombo createFromClassCombo() {
		classFromCombo = new ClassListCombo(messages.mergeFrom(), false);
		classFromCombo.setRequired(true);
		classFromCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccounterClass>() {
					@Override
					public void selectedComboBoxItem(
							ClientAccounterClass selectItem) {
						clientFromClass = selectItem;
						classFromSelected(selectItem);
					}
				});
		return classFromCombo;
	}

	private void classFromSelected(ClientAccounterClass selectItem) {
		classFromIDTextItem.setValue(String.valueOf(selectItem.getID()));
	}

	private void classToSelected(ClientAccounterClass selectItem) {
		classToIDTextItem.setValue(String.valueOf(selectItem.getID()));
	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();
		if ( clientFromClass != null && clientToClass != null) {
			if (clientFromClass.getID() == clientToClass.getID()) {
				result.addError(clientFromClass,
						messages.notMove(messages.classes()));
				return result;
			}
		}
		result.add(form1.validate());
		return result;
	}

	@Override
	protected boolean onOK() {

		if (clientToClass != null && clientFromClass != null) {
			if (clientToClass.getID() == clientFromClass.getID()) {
				return false;
			}
		}
		Accounter.createHomeService().mergeClass(clientFromClass,
				clientToClass, this);
		com.google.gwt.user.client.History.back();
		return true;
	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Void result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		classFromCombo.setFocus();
	}

}
