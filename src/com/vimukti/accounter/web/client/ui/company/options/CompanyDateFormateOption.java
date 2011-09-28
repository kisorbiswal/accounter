package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.widgets.DateUtills;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class CompanyDateFormateOption extends AbstractPreferenceOption {
	private static CompanyDateFormateOptionUiBinder uiBinder = GWT
			.create(CompanyDateFormateOptionUiBinder.class);
	@UiField
	Label comboBoxLabel;
	@UiField
	ListBox dateFormateComboBox;
	@UiField
	Label exampleDateFomateLabel;
	@UiField
	Label dateFormateDescriptionlabel;
	String[] dateFormates;

	interface CompanyDateFormateOptionUiBinder extends
			UiBinder<Widget, CompanyDateFormateOption> {
	}

	public CompanyDateFormateOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	@Override
	public String getTitle() {
		return "Date Format";
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setDateFormat(
				dateFormateComboBox.getItemText(dateFormateComboBox
						.getSelectedIndex()));

	}

	@Override
	public String getAnchor() {
		return "Date Format";
	}

	@Override
	public void createControls() {

		comboBoxLabel.setText("Date Formate");

		dateFormateDescriptionlabel.setText("Date Formats");
		dateFormateDescriptionlabel.setStyleName("organisation_comment");
		dateFormates = new String[] { "ddMMyy", "MM/dd/yy", "dd/MM/yy",
				"ddMMyyyy", "MMddyyyy", "MMM-dd-yy", "MMMddyyyy", "dd/MM/yyyy",
				"MM/dd/yyyy", "dd/MMMM/yyyy", "MMMMddyyyy", "dd-MM-yyyy",
				"MM-dd-yyyy", "dd/MMM/yyyy", "MMM/dd/yyyy", };

		for (int i = 0; i < dateFormates.length; i++) {
			dateFormateComboBox.addItem(dateFormates[i]);
		}
		dateFormateComboBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				getExampleDateFormat(dateFormateComboBox.getSelectedIndex());
			}
		});

	}

	@Override
	public void initData() {
		dateFormateComboBox.setSelectedIndex(getindex(getCompanyPreferences()
				.getDateFormat()));

	}

	/**
	 * 
	 * @param dateFormat
	 * @return
	 */
	private int getindex(String dateFormat) {
		for (int i = 0; i < dateFormates.length; i++) {
			if (dateFormat.equals(dateFormates[i])) {
				getExampleDateFormat(i);
				return i;
			}
		}
		return 0;
	}

	/**
	 * get example Label similarly to combo date formate
	 * 
	 * @param index
	 *            of combo Box
	 */
	private void getExampleDateFormat(int i) {
		switch (i) {
		case 0:
			exampleDateFomateLabel.setText("  *Example   : "
					+ DateUtills.getCurrentDateAsString("ddMMyy"));
			break;
		case 1:
			exampleDateFomateLabel.setText("  *Example   : "
					+ DateUtills.getCurrentDateAsString("MM/dd/yy"));
			break;
		case 2:
			exampleDateFomateLabel.setText("  *Example   : "
					+ DateUtills.getCurrentDateAsString("dd/MM/yy"));
			break;
		case 3:
			exampleDateFomateLabel.setText("  *Example   : "
					+ DateUtills.getCurrentDateAsString("ddMMyyyy"));
			break;
		case 4:
			exampleDateFomateLabel.setText("  *Example   : "
					+ DateUtills.getCurrentDateAsString("MMddyyyy"));
			break;
		case 5:
			exampleDateFomateLabel.setText("  *Example   : "
					+ DateUtills.getCurrentDateAsString("MMM-dd-yy"));
			break;
		case 6:
			exampleDateFomateLabel.setText("  *Example   : "
					+ DateUtills.getCurrentDateAsString("MMMddyyyy"));
			break;
		case 7:
			exampleDateFomateLabel.setText("  *Example   : "
					+ DateUtills.getCurrentDateAsString("dd/MM/yyyy"));
			break;
		case 8:
			exampleDateFomateLabel.setText("  *Example   : "
					+ DateUtills.getCurrentDateAsString("MM/dd/yyyy"));
			break;
		case 9:
			exampleDateFomateLabel.setText("  *Example   : "
					+ DateUtills.getCurrentDateAsString("dd/MMMM/yyyy"));
			break;
		case 10:
			exampleDateFomateLabel.setText("  *Example   : "
					+ DateUtills.getCurrentDateAsString("MMMMddyyyy"));
			break;
		case 11:
			exampleDateFomateLabel.setText("  *Example   : "
					+ DateUtills.getCurrentDateAsString("dd-MM-yyyy"));
			break;
		case 12:
			exampleDateFomateLabel.setText("  *Example   : "
					+ DateUtills.getCurrentDateAsString("MM-dd-yyyy"));
			break;
		case 13:
			exampleDateFomateLabel.setText("  *Example   : "
					+ DateUtills.getCurrentDateAsString("dd/MMM/yyyy"));
			break;
		case 14:
			exampleDateFomateLabel.setText("  *Example   : "
					+ DateUtills.getCurrentDateAsString("MMM/dd/yyyy"));
			break;
		}
	}
}
