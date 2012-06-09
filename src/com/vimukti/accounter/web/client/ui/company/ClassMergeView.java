package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.ClassListCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class ClassMergeView extends BaseView<ClientAccounterClass> {

	private DynamicForm form;
	private DynamicForm form1;

	private ClassListCombo classFromCombo;
	private ClassListCombo classToCombo;
	private TextItem classFromIDTextItem;
	private TextItem classToIDTextItem;

	private ClientAccounterClass clientFromClass;
	private ClientAccounterClass clientToClass;

	public ClassMergeView() {
		this.getElement().setId("ClassMergeView");
		clientToClass = null;
		clientFromClass = null;
	}

	@Override
	public void init() {
		super.init();
		ImageButton meregButton = new ImageButton(messages.merge(), Accounter
				.getFinanceImages().saveAndClose());
		meregButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				validate();
				mergeClass();

			}
		});

		buttonBar.add(meregButton);
		createControls();
	}

	private void createControls() {
		Label lab1 = new Label(messages.mergeClasses());
		lab1.setStyleName("label-title");

		form = new DynamicForm("form");
		form1 = new DynamicForm("form1");
		StyledPanel layout = new StyledPanel("layout");
		StyledPanel layout1 = new StyledPanel("layout1");
		StyledPanel horizontalPanel = new StyledPanel("horizontalPanel");
		classFromCombo = createFromClassCombo();
		classToCombo = createToClassCombo();

		classFromIDTextItem = new TextItem(messages.classId(),
				"classIDTextItem");
		classFromIDTextItem.setEnabled(false);

		classToIDTextItem = new TextItem(messages.classId(), "classIDTextItem1");
		classToIDTextItem.setEnabled(false);

		classFromCombo.setRequired(true);
		classToCombo.setRequired(true);
		form.add(classFromCombo, classFromIDTextItem);
		form1.add(classToCombo, classToIDTextItem);
		layout.add(form);
		layout1.add(form1);
		horizontalPanel.add(lab1);
		horizontalPanel.add(layout);
		horizontalPanel.add(layout1);
		this.add(horizontalPanel);

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

	public ValidationResult validate() {
		ValidationResult result = form.validate();
		if (clientFromClass != null && clientToClass != null) {
			if (clientFromClass.getID() == clientToClass.getID()) {
				result.addError(clientFromClass,
						messages.notMove(messages.classes()));
				return result;
			}
		}
		result.add(form1.validate());
		return result;
	}

	protected void mergeClass() {

		if (clientToClass != null && clientFromClass != null) {
			if (clientToClass.getID() == clientFromClass.getID()) {
			}
		}
		Accounter.createHomeService().mergeClass(clientFromClass,
				clientToClass, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						Accounter.showInformation("Merge Sucessful");
						onClose();
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});
		com.google.gwt.user.client.History.back();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
