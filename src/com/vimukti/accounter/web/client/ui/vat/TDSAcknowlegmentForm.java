package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class TDSAcknowlegmentForm extends BaseDialog {

	private DynamicForm form;
	private DynamicForm form1;

	private TextItem quater1PRN;
	private TextItem quater2PRN;
	private TextItem quater3PRN;
	private TextItem quater4PRN;

	private DateField quater1Date;
	private DateField quater2Date;
	private DateField quater3Date;
	private DateField quater4Date;

	public TDSAcknowlegmentForm() {
		super("TDS Acknowledgement Form",
				"Add the acknowledgement no. you get after e TDS filling.");
		setWidth("650px");
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
		HorizontalPanel layout = new HorizontalPanel();
		HorizontalPanel layout1 = new HorizontalPanel();
		VerticalPanel vPanel = new VerticalPanel();

		quater1PRN = new TextItem("Quarter 1 Org. RRR No. (PRN)");
		quater1PRN.setHelpInformation(true);

		ClientFinanceDate todaysDate = new ClientFinanceDate();
		quater1Date = new DateField(messages.date());
		quater1Date.setHelpInformation(true);
		quater1Date.setColSpan(1);
		quater1Date.setTitle(messages.date());
		quater1Date.setValue(todaysDate);

		quater2PRN = new TextItem("Quarter 2 Org. RRR No. (PRN)");
		quater2PRN.setHelpInformation(true);

		quater2Date = new DateField(messages.date());
		quater2Date.setHelpInformation(true);
		quater2Date.setColSpan(1);
		quater2Date.setTitle(messages.date());
		quater2Date.setValue(todaysDate);

		quater3Date = new DateField(messages.date());
		quater3Date.setHelpInformation(true);
		quater3Date.setColSpan(1);
		quater3Date.setTitle(messages.date());
		quater3Date.setValue(todaysDate);

		quater3PRN = new TextItem("Quarter 3 Org. RRR No. (PRN)");
		quater3PRN.setHelpInformation(true);

		quater4Date = new DateField(messages.date());
		quater4Date.setHelpInformation(true);
		quater4Date.setColSpan(1);
		quater4Date.setTitle(messages.date());
		quater4Date.setValue(todaysDate);

		quater4PRN = new TextItem("Quarter4 Org. RRR No. (PRN)");
		quater4PRN.setHelpInformation(true);

		form.setItems(quater1PRN, quater2PRN, quater3PRN, quater4PRN);
		form1.setItems(quater1Date, quater2Date, quater3Date, quater4Date);

		layout1.add(form);
		layout1.add(form1);
		vPanel.add(layout);
		vPanel.add(layout1);
		setBodyLayout(vPanel);

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
	public void setFocus() {

	}

	@Override
	protected boolean onCancel() {
		return true;
	}

}
