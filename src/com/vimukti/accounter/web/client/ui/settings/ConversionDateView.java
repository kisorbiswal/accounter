package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

@SuppressWarnings("unchecked")
public class ConversionDateView extends AbstractBaseView {
	private VerticalPanel mainPanel, headerPanel, bodyPanel;
	private HTML titleHtml, superTitleHtml, bodyHtml, bodyHeaderHtml,
			bodyFooterHtml, bodycommentHtml;
	private SelectCombo monthCombo, yearCombo;
	private HorizontalPanel buttonPanel;
	private String[] monthArray;
	private String[] yearArray;
	private DynamicForm comboForm;
	private List<String> monthList;
	private List<String> yearList;
	private Button saveButton, cancelButton;

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		titleHtml = new HTML(
				"<p><font size='4px',color='green'>Conversion Date</font></p>");
		superTitleHtml = new HTML(
				"<a><font size='2px', color='green'>Conversion Balances</font></a> > ");

		bodyHeaderHtml = new HTML(
				"<p><font size='2px'>Conversion Date</font></p>");
		bodyHtml = new HTML(
				"<p><font size='2px'>Enter the date that you began processing all your transactions in Accounter. It's easiest when you set your conversion date to be the start of a Sales Tax period.</font> <a><font color='green' size='2px'>Tips for Choosing a Conversion Date</font></a></p>");

		bodycommentHtml = new HTML(
				"<p><font color='990000',size='2px'><b>WARNING:</b> If you change your conversion date you will need to confirm your balances and any related invoices.</font></p>");
		bodycommentHtml.setVisible(false);

		mainPanel = new VerticalPanel();
		headerPanel = new VerticalPanel();
		bodyPanel = new VerticalPanel();
		comboForm = new DynamicForm();
		buttonPanel = new HorizontalPanel();
		saveButton = new Button("Save");
		cancelButton = new Button("Cancel");

		monthArray = new String[] { "January", "February", "March", "April",
				"May", "June", "July", "August", "September", "October",
				"November", "December" };
		yearArray = new String[] { "2011", "2012" };
		monthCombo = new SelectCombo("Month");
		monthCombo.setHelpInformation(true);
		monthList = new ArrayList<String>();
		for (int i = 0; i < monthArray.length; i++) {
			monthList.add(monthArray[i]);
		}
		bodyFooterHtml = new HTML();
		final String bodyFooter = "<p><font size='2px',color='993300'>For this conversion date you need to enter conversion balances (also known as opening balances) as at:";
		monthCombo.initCombo(monthList);
		monthCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						monthCombo.setSelected(monthCombo.getSelectedValue());
						bodycommentHtml.setVisible(true);
						bodyFooterHtml.setHTML(bodyFooter
								+ "<b>"
								+ getPreviousMonth(monthCombo
										.getSelectedValue(), yearCombo
										.getSelectedValue())
								+ getYear(monthCombo.getSelectedValue(),
										yearCombo.getSelectedValue())
								+ "</b> </font></p>");
					}
				});

		yearCombo = new SelectCombo("Year");
		yearCombo.setHelpInformation(true);
		yearList = new ArrayList<String>();
		for (int i = 0; i < yearArray.length; i++) {
			yearList.add(yearArray[i]);
		}
		yearCombo.initCombo(yearList);
		yearCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						yearCombo.setSelected(yearCombo.getSelectedValue());
						bodycommentHtml.setVisible(true);
						bodyFooterHtml.setHTML(bodyFooter
								+ "<b>"
								+ getPreviousMonth(monthCombo
										.getSelectedValue(), yearCombo
										.getSelectedValue())
								+ getYear(monthCombo.getSelectedValue(),
										yearCombo.getSelectedValue())
								+ "</b>  </font></p>");
					}

				});

		superTitleHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getConversionBalancesAction().run(null,
						false);
			}
		});

		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String endindDate = getPreviousMonth(monthCombo
						.getSelectedValue(), yearCombo.getSelectedValue())
						+ getYear(monthCombo.getSelectedValue(), yearCombo
								.getSelectedValue());
				SettingsActionFactory.getConversionBalancesAction().run(null,
						false
				/*
				 * ,endindDate, getYear(monthCombo.getSelectedValue(), yearCombo
				 * .getSelectedValue())
				 */);
			}
		});

		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getConversionBalancesAction().run(null,
						false);
			}
		});
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		headerPanel.add(superTitleHtml);
		headerPanel.add(titleHtml);

		comboForm.setNumCols(2);

		comboForm.setFields(monthCombo, yearCombo);

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);

		bodyPanel.add(bodyHeaderHtml);
		bodyPanel.add(bodyHtml);
		bodyPanel.add(comboForm);
		bodyPanel.add(bodyFooterHtml);
		bodyPanel.add(bodycommentHtml);
		bodyPanel.add(buttonPanel);

		mainPanel.add(headerPanel);
		mainPanel.add(bodyPanel);

		add(mainPanel);
	}

	private String getYear(String month, String year) {
		String returnValue = null;
		if (month == null || year == null) {
			returnValue = "";
		} else if (month.equals("January")) {
			returnValue = String.valueOf(Integer.parseInt(year) - 1);
		} else {
			returnValue = year;
		}
		return returnValue;
	}

	private String getPreviousMonth(String month, String year) {
		String returnValue = null;
		if (month == null || year == null) {
			returnValue = "";
		} else if (month.equals("January")) {
			returnValue = "31 December ";
		} else if (month.equals("February")) {
			returnValue = "31 January ";
		} else if (month.equals("March")) {
			if (Integer.parseInt(year) % 4 == 0) {
				returnValue = "29 February ";
			} else {
				returnValue = "28 February ";
			}

		} else if (month.equals("April")) {
			returnValue = "31 March ";
		} else if (month.equals("May")) {
			returnValue = "30 April ";
		} else if (month.equals("June")) {
			returnValue = "31 May ";
		} else if (month.equals("July")) {
			returnValue = "30 June ";
		} else if (month.equals("August")) {
			returnValue = "31 July ";
		} else if (month.equals("September")) {
			returnValue = "31 August ";
		} else if (month.equals("October")) {
			returnValue = "30 September ";
		} else if (month.equals("November")) {
			returnValue = "31 October ";
		} else if (month.equals("December")) {
			returnValue = "31 November ";
		}

		return returnValue;

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

}
