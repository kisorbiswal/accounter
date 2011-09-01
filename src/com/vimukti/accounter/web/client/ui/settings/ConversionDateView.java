package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

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
	private AccounterConstants messages = Accounter.constants();

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		titleHtml = new HTML(messages.conversionDateTitle());
		superTitleHtml = new HTML(messages.conversionBalanceTitle());

		bodyHtml = new HTML(Accounter.messages()
				.conversationDateSelectionHTML());

		bodycommentHtml = new HTML(messages.conversionBodyComment());
		bodycommentHtml.setVisible(false);

		mainPanel = new VerticalPanel();
		headerPanel = new VerticalPanel();
		bodyPanel = new VerticalPanel();
		comboForm = new DynamicForm();
		buttonPanel = new HorizontalPanel();
		saveButton = new Button(messages.saveButton());
		cancelButton = new Button(messages.cancelButton());

		monthArray = new String[] { messages.january(), messages.february(),
				messages.march(), messages.april(), messages.may(),
				messages.june(), messages.july(), messages.august(),
				messages.september(), messages.october(), messages.november(),
				messages.december() };
		yearArray = new String[] { messages.year2011(), messages.year2012() };
		monthCombo = new SelectCombo(messages.month());
		monthCombo.setHelpInformation(true);
		monthList = new ArrayList<String>();
		for (int i = 0; i < monthArray.length; i++) {
			monthList.add(monthArray[i]);
		}
		bodyFooterHtml = new HTML();
		final SafeHtml bodyFooter = Accounter.messages().bodyFooter();
		monthCombo.initCombo(monthList);
		monthCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						monthCombo.setSelected(monthCombo.getSelectedValue());
						bodycommentHtml.setVisible(true);
						bodyFooterHtml.setHTML(bodyFooter
								+ "<b>"
								+ getPreviousMonth(
										monthCombo.getSelectedValue(),
										yearCombo.getSelectedValue())
								+ getYear(monthCombo.getSelectedValue(),
										yearCombo.getSelectedValue())
								+ "</b> </font></p>");
					}
				});

		yearCombo = new SelectCombo(messages.year());
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
								+ getPreviousMonth(
										monthCombo.getSelectedValue(),
										yearCombo.getSelectedValue())
								+ getYear(monthCombo.getSelectedValue(),
										yearCombo.getSelectedValue())
								+ "</b>  </font></p>");
					}

				});

		superTitleHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getConversionBalancesAction().run(null, false);
			}
		});

		saveButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String endindDate = getPreviousMonth(
						monthCombo.getSelectedValue(),
						yearCombo.getSelectedValue())
						+ getYear(monthCombo.getSelectedValue(),
								yearCombo.getSelectedValue());
				ActionFactory.getConversionBalancesAction().run(null, false
				/*
				 * ,endindDate, getYear(monthCombo.getSelectedValue(), yearCombo
				 * .getSelectedValue())
				 */);
			}
		});

		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getConversionBalancesAction().run(null, false);
			}
		});
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		headerPanel.add(superTitleHtml);
		headerPanel.add(titleHtml);

		comboForm.setNumCols(2);

		comboForm.setFields(monthCombo, yearCombo);

		buttonPanel.add(saveButton);
		buttonPanel.add(cancelButton);
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
		} else if (month.equals(messages.january())) {
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
		} else if (month.equals(messages.january())) {
			returnValue = messages.date31() + " " + messages.december();
		} else if (month.equals(messages.february())) {
			returnValue = messages.date31() + " " + messages.january();
		} else if (month.equals(messages.march())) {
			if (Integer.parseInt(year) % 4 == 0) {
				returnValue = messages.date29() + " " + messages.february();
			} else {
				returnValue = messages.date28() + " " + messages.february();
			}

		} else if (month.equals(messages.april())) {
			returnValue = messages.date31() + " " + messages.march();
		} else if (month.equals(messages.may())) {
			returnValue = messages.date30() + " " + messages.april();
		} else if (month.equals(messages.june())) {
			returnValue = messages.april() + " " + messages.may();
		} else if (month.equals(messages.july())) {
			returnValue = messages.date30() + " " + messages.june();
		} else if (month.equals(messages.august())) {
			returnValue = messages.date31() + " " + messages.july();
		} else if (month.equals(messages.september())) {
			returnValue = messages.date31() + " " + messages.august();
		} else if (month.equals(messages.october())) {
			returnValue = messages.date30() + " " + messages.september();
		} else if (month.equals(messages.november())) {
			returnValue = messages.date31() + " " + messages.october();
		} else if (month.equals(messages.december())) {
			returnValue = messages.date31() + " " + messages.november();
		}

		return returnValue;

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {
		// it is not using any where
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result){

	}


	@Override
	protected String getViewTitle() {
		return Accounter.constants().conversionDate();
	}

}
