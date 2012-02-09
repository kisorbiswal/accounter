package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class CreateIRASInformationFileDialog extends BaseDialog {

	private VerticalPanel verticalPanel;
	private DateField startDateFiled, endDateField;
	private DynamicForm mainForm;
	private Button downloadXml, downloadTxt;

	public CreateIRASInformationFileDialog() {
		super(messages.createGST(), null);
		createControls();
		center();
	}

	private void createControls() {
		setText(messages.generateIrasAuditFile());

		verticalPanel = new VerticalPanel();

		mainForm = new DynamicForm();

		Label label = new Label(messages.selectTheDateRange());

		startDateFiled = new DateField(messages.startDate());
		startDateFiled.setValue(new ClientFinanceDate());

		endDateField = new DateField(messages.endDate());
		endDateField.setValue(new ClientFinanceDate());

		mainForm.setFields(startDateFiled, endDateField);

		verticalPanel.add(label);
		verticalPanel.add(mainForm);

		downloadXml = new Button(messages.downloadXmlFile());
		downloadXml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createFile(true);
			}
		});

		downloadTxt = new Button(messages.downloadTxtFile());
		downloadTxt.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				createFile(false);
			}
		});

		footerLayout.remove(okbtn);
		footerLayout.insert(downloadXml, getAbsoluteLeft());
		footerLayout.insert(downloadTxt, getAbsoluteLeft());

		setBodyLayout(verticalPanel);
		setWidth("370px");
	}

	protected void createFile(final boolean isXml) {
		ClientFinanceDate startDate = startDateFiled.getValue();
		ClientFinanceDate endDate = endDateField.getValue();

		AsyncCallback<String> callback = new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(String result) {
				String name = isXml ? "GstXmlFile.xml" : "GstTxtFile.txt";
				UIUtils.downloadFileFromTemp(name, result);
				closeView();
			}
		};

		Accounter.createHomeService().getIRASFileInformation(startDate,
				endDate, isXml, callback);
	}

	private void closeView() {
		com.google.gwt.user.client.History.back();
		removeFromParent();
	}

	@Override
	protected boolean onOK() {
		MainFinanceWindow.getViewManager().viewDataHistory.put(
				"createGSTIncormationFile", null);
		return true;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
