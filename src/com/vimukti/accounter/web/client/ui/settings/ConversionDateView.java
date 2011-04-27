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
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

@SuppressWarnings("unchecked")
public class ConversionDateView extends AbstractBaseView {
	private VerticalPanel mainPanel, headerPanel, bodyPanel;
	private HTML titleHtml, superTitleHtml, bodyHtml, bodyFooterHtml,
			bodycommentHtml;
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
		titleHtml = new HTML(FinanceApplication.getSettingsMessages()
				.conversionDateTitle());
		superTitleHtml = new HTML(FinanceApplication.getSettingsMessages()
				.conversionBalanceTitle());

		bodyHtml = new HTML(
				"<p><font size='2px'>Enter the date that you began processing all your transactions in Accounter. It's easiest when you set your conversion date to be the start of a Sales Tax period.</font> <a><font color='green' size='2px'>Tips for Choosing a Conversion Date</font></a></p>");

		bodycommentHtml = new HTML(FinanceApplication.getSettingsMessages()
				.conversionBodyComment());
		bodycommentHtml.setVisible(false);

		mainPanel = new VerticalPanel();
		headerPanel = new VerticalPanel();
		bodyPanel = new VerticalPanel();
		comboForm = new DynamicForm();
		buttonPanel = new HorizontalPanel();
		saveButton = new Button(FinanceApplication.getSettingsMessages()
				.saveButton());
		cancelButton = new Button(FinanceApplication.getSettingsMessages()
				.cancelButton());

		monthArray = new String[] {
				FinanceApplication.getSettingsMessages().january(),
				FinanceApplication.getSettingsMessages().february(),
				FinanceApplication.getSettingsMessages().march(),
				FinanceApplication.getSettingsMessages().april(),
				FinanceApplication.getSettingsMessages().may(),
				FinanceApplication.getSettingsMessages().june(),
				FinanceApplication.getSettingsMessages().july(),
				FinanceApplication.getSettingsMessages().august(),
				FinanceApplication.getSettingsMessages().september(),
				FinanceApplication.getSettingsMessages().october(),
				FinanceApplication.getSettingsMessages().november(),
				FinanceApplication.getSettingsMessages().december() };
		yearArray = new String[] {
				FinanceApplication.getSettingsMessages().year2011(),
				FinanceApplication.getSettingsMessages().year2012() };
		monthCombo = new SelectCombo(FinanceApplication.getSettingsMessages()
				.month());
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

		yearCombo = new SelectCombo(FinanceApplication.getSettingsMessages()
				.year());
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
		if (saveButton.isEnabled()) {
			saveButton.getElement().getParentElement().setClassName("ibutton");
			ThemesUtil.addDivToButton(saveButton, FinanceApplication
					.getThemeImages().button_right_blue_image(),
					"ibutton-right-image");
		}
		if (cancelButton.isEnabled()) {
			cancelButton.getElement().getParentElement()
					.setClassName("ibutton");
			ThemesUtil.addDivToButton(cancelButton, FinanceApplication
					.getThemeImages().button_right_blue_image(),
					"ibutton-right-image");
		}
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
		} else if (month.equals(FinanceApplication.getSettingsMessages()
				.january())) {
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
		} else if (month.equals(FinanceApplication.getSettingsMessages()
				.january())) {
			returnValue = FinanceApplication.getSettingsMessages().date31()
					+ " " + FinanceApplication.getSettingsMessages().december();
		} else if (month.equals(FinanceApplication.getSettingsMessages()
				.february())) {
			returnValue = FinanceApplication.getSettingsMessages().date31()
					+ " " + FinanceApplication.getSettingsMessages().january();
		} else if (month.equals(FinanceApplication.getSettingsMessages()
				.march())) {
			if (Integer.parseInt(year) % 4 == 0) {
				returnValue = FinanceApplication.getSettingsMessages().date29()
						+ " "
						+ FinanceApplication.getSettingsMessages().february();
			} else {
				returnValue = FinanceApplication.getSettingsMessages().date28()
						+ " "
						+ FinanceApplication.getSettingsMessages().february();
			}

		} else if (month.equals(FinanceApplication.getSettingsMessages()
				.april())) {
			returnValue = FinanceApplication.getSettingsMessages().date31()
					+ " " + FinanceApplication.getSettingsMessages().march();
		} else if (month.equals(FinanceApplication.getSettingsMessages().may())) {
			returnValue = FinanceApplication.getSettingsMessages().date30()
					+ " " + FinanceApplication.getSettingsMessages().april();
		} else if (month
				.equals(FinanceApplication.getSettingsMessages().june())) {
			returnValue = FinanceApplication.getSettingsMessages().april()
					+ " " + FinanceApplication.getSettingsMessages().may();
		} else if (month
				.equals(FinanceApplication.getSettingsMessages().july())) {
			returnValue = FinanceApplication.getSettingsMessages().date30()
					+ " " + FinanceApplication.getSettingsMessages().june();
		} else if (month.equals(FinanceApplication.getSettingsMessages()
				.august())) {
			returnValue = FinanceApplication.getSettingsMessages().date31()
					+ " " + FinanceApplication.getSettingsMessages().july();
		} else if (month.equals(FinanceApplication.getSettingsMessages()
				.september())) {
			returnValue = FinanceApplication.getSettingsMessages().date31()
					+ " " + FinanceApplication.getSettingsMessages().august();
		} else if (month.equals(FinanceApplication.getSettingsMessages()
				.october())) {
			returnValue = FinanceApplication.getSettingsMessages().date30()
					+ " "
					+ FinanceApplication.getSettingsMessages().september();
		} else if (month.equals(FinanceApplication.getSettingsMessages()
				.november())) {
			returnValue = FinanceApplication.getSettingsMessages().date31()
					+ " " + FinanceApplication.getSettingsMessages().october();
		} else if (month.equals(FinanceApplication.getSettingsMessages()
				.december())) {
			returnValue = FinanceApplication.getSettingsMessages().date31()
					+ " " + FinanceApplication.getSettingsMessages().november();
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
