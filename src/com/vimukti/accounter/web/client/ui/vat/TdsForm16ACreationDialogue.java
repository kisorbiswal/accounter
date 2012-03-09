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
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class TdsForm16ACreationDialogue extends BaseDialog {

	private DynamicForm form;
	private TextItem location;
	private TextItem tdsCertificateNumber;
	private DynamicForm form0;
	private DateField form16AprintDate;
	private VendorCombo vendorCombo;

	private long vendorID;
	private String datesRange;
	private String place;
	private String printDate;

	int dateRAngeType = 1;

	private SelectCombo chalanQuarterPeriod;
	private Button emailBUtton;
	private Button coveringLetter;
	private SelectCombo monthlyCombo;
	private DateField fromDate;
	private DateField toDate;
	protected boolean isCoveringLetter;

	public TdsForm16ACreationDialogue() {
		super(messages.tdsForm16A(), messages
				.addTheDetailsYouGetFromTheTINWebsiteAndPressCreate16AForm());
		setWidth("650px");

		okbtn.setText(messages.generate16Aform());
		okbtn.setWidth("150px");

		emailBUtton = new Button(messages.email());
		emailBUtton.setWidth("80px");
		emailBUtton.setFocus(true);
		emailBUtton.setVisible(false);
		emailBUtton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {

				sendEmail();
			}
		});

		coveringLetter = new Button(messages.generateCoveringLetter());
		coveringLetter.setFocus(true);
		coveringLetter.setVisible(false);
		coveringLetter.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				isCoveringLetter = true;
				onOK();
			}
		});

		footerLayout.add(emailBUtton);
		footerLayout.add(coveringLetter);
		createControls();
		center();
	}

	@SuppressWarnings({ "unchecked" })
	private void createControls() {
		form = new DynamicForm();
		form.setWidth("100%");
		form.setHeight("100%");

		HorizontalPanel layout = new HorizontalPanel();
		HorizontalPanel layout1 = new HorizontalPanel();
		VerticalPanel vPanel = new VerticalPanel();

		List<ClientVendor> vendors = getCompany().getActiveVendors();
		List<ClientVendor> tdsVendors = new ArrayList<ClientVendor>();
		for (ClientVendor clientVendor : vendors) {
			if (clientVendor.isTdsApplicable()) {
				tdsVendors.add(clientVendor);
			}
		}

		vendorCombo = new VendorCombo(messages.deductee(), false);
		vendorCombo.setRequired(true);
		vendorCombo.initCombo(tdsVendors);
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						vendorID = selectItem.getID();
					}
				});

		chalanQuarterPeriod = new SelectCombo(messages.selectQuarter());
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
						}
					}
				});

		monthlyCombo = new SelectCombo(messages.selectMonths());
		monthlyCombo.setHelpInformation(true);
		monthlyCombo.setComboItem(DayAndMonthUtil.january());
		monthlyCombo.initCombo(getMonthsList());
		monthlyCombo.setPopupWidth("500px");
		monthlyCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

					}
				});

		location = new TextItem(messages.place());
		location.setHelpInformation(true);

		ClientFinanceDate todaysDate = new ClientFinanceDate();

		form16AprintDate = new DateField(messages.date());
		form16AprintDate.setHelpInformation(true);
		form16AprintDate.setColSpan(1);
		form16AprintDate.setTitle(messages.date());
		form16AprintDate.setValue(todaysDate);

		fromDate = new DateField(messages.fromDate());
		fromDate.setHelpInformation(true);
		fromDate.setColSpan(1);
		fromDate.setTitle(messages.fromDate());
		fromDate.setValue(todaysDate);

		toDate = new DateField(messages.toDate());
		toDate.setHelpInformation(true);
		toDate.setColSpan(1);
		toDate.setTitle(messages.toDate());
		toDate.setValue(todaysDate);

		tdsCertificateNumber = new TextItem(messages.tdsCertificateNumber());
		tdsCertificateNumber.setHelpInformation(true);
		tdsCertificateNumber.setRequired(true);
		form0 = new DynamicForm();
		form0.setWidth("100%");

		form.setItems(tdsCertificateNumber, location, form16AprintDate);
		form0.setItems(vendorCombo, chalanQuarterPeriod, monthlyCombo,
				fromDate, toDate);

		HorizontalPanel radioButtonPanel = new HorizontalPanel();

		String[] sports = { messages.quarterly(), messages.monthly(),
				messages.betweenDates(), messages.yearly() };

		final RadioButton button1 = new RadioButton("group", sports[0]);
		button1.setValue(true);
		final RadioButton button2 = new RadioButton("group", sports[1]);
		button2.setValue(false);
		final RadioButton button3 = new RadioButton("group", sports[2]);
		button3.setValue(false);

		final RadioButton button4 = new RadioButton("group", sports[3]);
		button4.setValue(false);

		ClickHandler handler = new ClickHandler() {
			public void onClick(ClickEvent e) {
				if (e.getSource() == button1) {
					dateRAngeType = 1;
					chalanQuarterPeriod.show();
					monthlyCombo.hide();
					fromDate.hide();
					toDate.hide();
				} else if (e.getSource() == button2) {
					dateRAngeType = 2;
					chalanQuarterPeriod.hide();
					monthlyCombo.show();
					fromDate.hide();
					toDate.hide();
				} else if (e.getSource() == button3) {
					dateRAngeType = 3;
					chalanQuarterPeriod.hide();
					monthlyCombo.hide();
					fromDate.show();
					toDate.show();
				} else if (e.getSource() == button4) {
					dateRAngeType = 4;
					chalanQuarterPeriod.hide();
					monthlyCombo.hide();
					fromDate.hide();
					toDate.hide();
				}
			}
		};

		chalanQuarterPeriod.show();
		monthlyCombo.hide();
		fromDate.hide();
		toDate.hide();

		button1.addClickHandler(handler);
		button2.addClickHandler(handler);
		button3.addClickHandler(handler);
		button4.addClickHandler(handler);

		radioButtonPanel.add(button1);
		radioButtonPanel.add(button2);
		radioButtonPanel.add(button3);
		radioButtonPanel.add(button4);

		layout.add(form0);
		layout1.add(form);
		vPanel.add(radioButtonPanel);
		vPanel.add(layout);
		vPanel.add(layout1);

		setBodyLayout(vPanel);

	}

	private List<String> getMonthsList() {

		ArrayList<String> list = new ArrayList<String>();

		list.add(DayAndMonthUtil.january());
		list.add(DayAndMonthUtil.february());
		list.add(DayAndMonthUtil.march());
		list.add(DayAndMonthUtil.april());
		list.add(DayAndMonthUtil.may_full());
		list.add(DayAndMonthUtil.june());
		list.add(DayAndMonthUtil.july());
		list.add(DayAndMonthUtil.august());
		list.add(DayAndMonthUtil.september());
		list.add(DayAndMonthUtil.october());
		list.add(DayAndMonthUtil.november());
		list.add(DayAndMonthUtil.december());

		return list;
	}

	protected void sendEmail() {

	}

	@Override
	protected ValidationResult validate() {
		ValidationResult result = new ValidationResult();
		if (vendorCombo.getSelectedValue() == null) {
			result.addError(vendorCombo,
					messages.pleaseSelect(vendorCombo.getName()));
		}

		if (tdsCertificateNumber.getValue() == null
				|| tdsCertificateNumber.getValue().isEmpty()) {
			result.addError(tdsCertificateNumber,
					messages.pleaseEnter(tdsCertificateNumber.getTitle()));
		}
		return result;
	}

	@Override
	protected boolean onOK() {
		if (location.getValue() != null) {
			place = location.getValue();
		} else {
			place = "";
		}

		printDate = form16AprintDate.getDate().toString();
		String frmDt = null, toDt = null;
		int finYear = new ClientFinanceDate().getYear();
		if (dateRAngeType == 1) {
			int qtrSelected = chalanQuarterPeriod.getSelectedIndex();
			switch (qtrSelected) {
			case 0:
				frmDt = "04/01/" + Integer.toString(finYear);
				toDt = "06/30/" + Integer.toString(finYear);
				break;
			case 1:
				frmDt = "07/01/" + Integer.toString(finYear);
				toDt = "09/30/" + Integer.toString(finYear);
				break;
			case 2:
				frmDt = "10/01/" + Integer.toString(finYear);
				toDt = "12/31/" + Integer.toString(finYear);
				break;
			case 3:
				frmDt = "01/01/" + Integer.toString(finYear);
				toDt = "31/03/" + Integer.toString(finYear);
				break;
			default:
				break;

			}

		} else if (dateRAngeType == 2) {
			int mnSelected = monthlyCombo.getSelectedIndex() + 1;
			String month = Integer.toString(mnSelected);
			if (mnSelected < 10) {
				month = "0" + month;
			}
			frmDt = "01/" + month + "/" + Integer.toString(finYear);
			toDt = "30/" + month + "/" + Integer.toString(finYear);

		} else if (dateRAngeType == 3) {
			frmDt = fromDate.getDate().toString();
			toDt = toDate.getDate().toString();
		} else if (dateRAngeType == 4) {
			frmDt = "01/01/" + Integer.toString(finYear);
			toDt = "12/31/" + Integer.toString(finYear);
		}
		datesRange = frmDt + "-" + toDt;
		if (isCoveringLetter) {
			UIUtils.generateForm16A(vendorID, datesRange, place, printDate,
					tdsCertificateNumber.getValue(), 1);
		} else {
			UIUtils.generateForm16A(vendorID, datesRange, place, printDate,
					tdsCertificateNumber.getValue(), 0);
		}
		isCoveringLetter = false;
		processCancel();
		return true;

	}

	@Override
	public void setFocus() {
	}

}
