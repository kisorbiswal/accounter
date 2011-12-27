package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class TDSAcknowledgementFormView extends BaseDialog implements
		AsyncCallback<Void> {

	private DynamicForm form;
	private DynamicForm form1;

	private TextItem quater1PRN;
	private TextItem quater1Date;
	private TextItem quater2PRN;
	private TextItem quater2Date;
	private TextItem quater3PRN;
	private TextItem quater3Date;

	private TextItem quater4PRN;
	private TextItem quater4Date;

	public TDSAcknowledgementFormView() {
		super("TDS Acknowledgement Form",
				"Add the details you get from the TIN Website and press create 16A form");
		setWidth("650px");
		// okbtn.setText(Accounter.messages().merge());
		createControls();
		center();
	}

	private void createControls() {
		form = new DynamicForm();
		form1 = new DynamicForm();
		form.setWidth("100%");
		form.setHeight("100%");
		form1.setHeight("100%");
		form1.setWidth("100%");
		VerticalPanel layout = new VerticalPanel();
		VerticalPanel layout1 = new VerticalPanel();
		HorizontalPanel horizontalPanel = new HorizontalPanel();

		quater1PRN = new TextItem("Quarter 1 Org. RRR No. (PRN)");
		quater1PRN.setHelpInformation(true);

		quater1Date = new TextItem(messages.date());
		quater1Date.setHelpInformation(true);

		quater2PRN = new TextItem("Quarter 2 Org. RRR No. (PRN)");
		quater2PRN.setHelpInformation(true);
		quater2Date = new TextItem(messages.date());
		quater2Date.setHelpInformation(true);

		quater3Date = new TextItem("Quarter 3 Org. RRR No. (PRN)");
		quater3Date.setHelpInformation(true);

		quater3PRN = new TextItem(messages.date());
		quater3PRN.setHelpInformation(true);

		quater4Date = new TextItem("Quarter 4 Org. RRR No. (PRN)");
		quater4Date.setHelpInformation(true);

		quater4PRN = new TextItem(messages.date());
		quater4PRN.setHelpInformation(true);

		form.setItems(quater1PRN, quater2PRN, quater3Date, quater4Date);
		form1.setItems(quater1Date, quater2Date, quater3PRN, quater4PRN);

		layout.add(form);
		layout1.add(form1);
		horizontalPanel.add(layout);
		horizontalPanel.add(layout1);
		setBodyLayout(horizontalPanel);

	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = form.validate();

		return result;
	}

	@Override
	protected boolean onOK() {

		return false;

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

	}

}
