package com.vimukti.accounter.web.client.ui.setup;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class SetupSelectFiscalYrDatePage extends AbstractSetupPage {
	VerticalPanel mainPanel;
	Label heading, fiscalYearsameLabel, selectFirstMonth, secondHeading,
			theDateisStartdateLabel, inOrdertoComplete, enterTransaction;
	HTML explainHtml, startDateHtml;
	SelectCombo monthsCombo;
	RadioButton beginingYear, todaysDate;
	DateField datepicker;

	@Override
	public String getHeader() {
		return accounterConstants.selectFirstMonthOfFiscalYear();

	}

	@Override
	public VerticalPanel getPageBody() {
		creatControls();
		return mainPanel;
	}

	private void creatControls() {
		mainPanel = new VerticalPanel();
		selectFirstMonth = new Label(accounterConstants
				.selectFirstMonthOfFiscalYear());
		mainPanel.add(selectFirstMonth);
		fiscalYearsameLabel = new Label(accounterConstants
				.fiscalYearsaemasTaxyear());
		mainPanel.add(fiscalYearsameLabel);
		monthsCombo = new SelectCombo(accounterConstants
				.myFiscalYearsStartsIn());
		monthsCombo.initCombo(getMonthNames());
		DynamicForm dynmicform = new DynamicForm();
		dynmicform.setFields(monthsCombo);
		mainPanel.add(dynmicform);

		// Scond part

		secondHeading = new Label(accounterConstants.selectdateToTrackFinance());
		mainPanel.add(secondHeading);
		theDateisStartdateLabel = new Label(accounterConstants
				.yourSelecteddateisStartdate());
		mainPanel.add(theDateisStartdateLabel);
		beginingYear = new RadioButton(accounterConstants
				.beginingOfthefiscalYear());
		mainPanel.add(beginingYear);
		inOrdertoComplete = new Label(accounterConstants
				.enterTransactionsTocompleteTaxreturns());
		mainPanel.add(inOrdertoComplete);
		todaysDate = new RadioButton(accounterConstants
				.useTodaysDateasStartdate());
		mainPanel.toString();
		enterTransaction = new Label(accounterConstants
				.enterTransactionsTocompleteTaxreturns());
		mainPanel.add(enterTransaction);
		datepicker = new DateField("");
		DynamicForm dynamicform = new DynamicForm();
		dynamicform.setFields(datepicker);
		mainPanel.add(dynamicform);

	}

	private List<String> getMonthNames() {
		String[] monthNames = { accounterConstants.january(),
				accounterConstants.february(), accounterConstants.march(),
				accounterConstants.april(), accounterConstants.may(),
				accounterConstants.june(), accounterConstants.july(),
				accounterConstants.august(), accounterConstants.september(),
				accounterConstants.october(), accounterConstants.november(),
				accounterConstants.december() };
		List<String> monthNam = new ArrayList<String>();
		for (int i = 0; i < monthNames.length; i++) {
			monthNam.add(monthNames[i]);

		}

		return monthNam;
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onBack() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onNext() {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub
		
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
