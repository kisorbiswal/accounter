package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class TdsForm16ACreationDialogue extends BaseDialog {

	private DynamicForm form;
	private TextItem location;
	private TextItem tdsCertificateNumber;
	private SelectCombo financialYearCombo;
	private DynamicForm form0;
	private DateField form16AprintDate;
	private VendorCombo vendorCombo;

	private long vendorID;
	private String datesRange;
	private int place;
	private String printDate;

	private SelectCombo chalanQuarterPeriod;
	private Button emailBUtton;
	private Button coveringLetter;
	private SelectCombo monthlyCombo;
	private DateField fromDate;
	private DateField toDate;

	public TdsForm16ACreationDialogue() {
		super("TDS Acknowledgement Form",
				"Add the details you get from the TIN Website and press create 16A form");
		setWidth("650px");

		okbtn.setText("Generate 16A form");
		okbtn.setWidth("150px");
		emailBUtton = new Button(messages.email());
		emailBUtton.setWidth("80px");
		emailBUtton.setFocus(true);

		emailBUtton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				sendEmail();
			}
		});

		coveringLetter = new Button("Generate covering letter");
		coveringLetter.setFocus(true);

		coveringLetter.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				generateCoveringLetter();
			}
		});

		footerLayout.add(emailBUtton);
		footerLayout.add(coveringLetter);
		createControls();
		center();
	}

	protected void generateCoveringLetter() {
		// TODO Auto-generated method stub

	}

	private void createControls() {
		form = new DynamicForm();
		form.setWidth("100%");
		form.setHeight("100%");

		HorizontalPanel layout = new HorizontalPanel();
		HorizontalPanel layout1 = new HorizontalPanel();
		VerticalPanel vPanel = new VerticalPanel();

		List<ClientVendor> vendors = getCompany().getActiveVendors();
		vendorCombo = new VendorCombo("Select Deductee");
		vendorCombo.initCombo(vendors);
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						vendorID = selectItem.getID();
					}
				});

		chalanQuarterPeriod = new SelectCombo("Select Quarter");
		chalanQuarterPeriod.setHelpInformation(true);
		chalanQuarterPeriod.initCombo(getFinancialQuatersList());
		chalanQuarterPeriod.setSelectedItem(0);
		chalanQuarterPeriod.setPopupWidth("500px");
		chalanQuarterPeriod
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (selectItem.equals(getFinancialQuatersList().get(
								chalanQuarterPeriod.getSelectedIndex()))) {
							place = chalanQuarterPeriod.getSelectedIndex();
						}
					}
				});

		monthlyCombo = new SelectCombo("Select Months");
		monthlyCombo.setHelpInformation(true);
		monthlyCombo.initCombo(getMonthsList());
		monthlyCombo.setPopupWidth("500px");
		monthlyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		location = new TextItem("Place");
		location.setHelpInformation(true);

		ClientFinanceDate todaysDate = new ClientFinanceDate();

		form16AprintDate = new DateField(messages.date());
		form16AprintDate.setHelpInformation(true);
		form16AprintDate.setColSpan(1);
		form16AprintDate.setTitle(messages.date());
		form16AprintDate.setValue(todaysDate);

		fromDate = new DateField("From Date");
		fromDate.setHelpInformation(true);
		fromDate.setColSpan(1);
		fromDate.setTitle("From Date");
		fromDate.setValue(todaysDate);

		toDate = new DateField("To Date");
		toDate.setHelpInformation(true);
		toDate.setColSpan(1);
		toDate.setTitle("To Date");
		toDate.setValue(todaysDate);

		tdsCertificateNumber = new TextItem("TDS Certificate No.");
		tdsCertificateNumber.setHelpInformation(true);

		financialYearCombo = new SelectCombo("Financial Year");
		financialYearCombo.setHelpInformation(true);
		financialYearCombo.initCombo(getFinancialYearList());
		financialYearCombo.setRequired(true);
		financialYearCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						datesRange = selectItem;
					}
				});

		form0 = new DynamicForm();
		form0.setWidth("100%");

		form.setItems(tdsCertificateNumber, location, form16AprintDate);
		form0.setItems(vendorCombo, chalanQuarterPeriod, financialYearCombo,
				monthlyCombo, fromDate, toDate);

		HorizontalPanel radioButtonPanel = new HorizontalPanel();

		String[] sports = { "Quarterly", "Monthly", "Between Dates" };

		final RadioButton button1 = new RadioButton("group", sports[0]);
		button1.setValue(true);
		final RadioButton button2 = new RadioButton("group", sports[1]);
		button2.setValue(false);
		final RadioButton button3 = new RadioButton("group", sports[2]);
		button3.setValue(false);

		ClickHandler handler = new ClickHandler() {
			public void onClick(ClickEvent e) {
				if (e.getSource() == button1) {
					financialYearCombo.show();
					chalanQuarterPeriod.show();
					monthlyCombo.hide();
					fromDate.hide();
					toDate.hide();
				} else if (e.getSource() == button2) {
					financialYearCombo.show();
					chalanQuarterPeriod.hide();
					monthlyCombo.show();
					fromDate.hide();
					toDate.hide();
				} else if (e.getSource() == button3) {
					financialYearCombo.hide();
					chalanQuarterPeriod.hide();
					monthlyCombo.hide();
					fromDate.show();
					toDate.show();
				}
			}
		};

		financialYearCombo.show();
		chalanQuarterPeriod.show();
		monthlyCombo.hide();
		fromDate.hide();
		toDate.hide();

		button1.addClickHandler(handler);
		button2.addClickHandler(handler);
		button3.addClickHandler(handler);

		radioButtonPanel.add(button1);
		radioButtonPanel.add(button2);
		radioButtonPanel.add(button3);

		layout.add(form0);
		layout1.add(form);
		vPanel.add(radioButtonPanel);
		vPanel.add(layout);
		vPanel.add(layout1);

		setBodyLayout(vPanel);

	}

	private List<String> getMonthsList() {
		// TODO Auto-generated method stub
		return null;
	}

	protected void sendEmail() {

	}

	@Override
	protected ValidationResult validate() {

		return null;
	}

	@Override
	protected boolean onOK() {

		UIUtils.generateForm16A(vendorID, datesRange, place, printDate);
		return false;

	}

	@Override
	public void setFocus() {

	}

	private List<String> getFormTypes() {
		ArrayList<String> list = new ArrayList<String>();

		list.add("26Q");
		list.add("27Q");
		list.add("27EQ");

		return list;
	}

}
