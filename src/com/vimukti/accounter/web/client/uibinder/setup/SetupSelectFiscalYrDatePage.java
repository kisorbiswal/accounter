/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * @author Administrator
 * 
 */
public class SetupSelectFiscalYrDatePage extends AbstractSetupPage {

	private static SetupSelectFiscalYrDatePageUiBinder uiBinder = GWT
			.create(SetupSelectFiscalYrDatePageUiBinder.class);
	@UiField
	VerticalPanel viewPanel;
	@UiField
	Label fiscalStarts;
	@UiField
	HTML fiscalHeader;
	@UiField
	Label startDate;
	@UiField
	RadioButton beginingFiscal;
	@UiField
	HTML beginingFiscalText;
	@UiField
	RadioButton todaysDate;
	@UiField
	HTML todaysDateText;
	@UiField
	DateBox fiscalDate;
	@UiField
	ListBox fiscalStartsList;
	@UiField
	HTML fiscalInfo;
	@UiField
	HTML fiscalHead;

	interface SetupSelectFiscalYrDatePageUiBinder extends
			UiBinder<Widget, SetupSelectFiscalYrDatePage> {
	}

	/**
	 * Because this class has a default constructor, it can be used as a binder
	 * template. In other words, it can be used in other *.ui.xml files as
	 * follows: <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 * xmlns:g="urn:import:**user's package**">
	 * <g:**UserClassName**>Hello!</g:**UserClassName> </ui:UiBinder> Note that
	 * depending on the widget that is used, it may be necessary to implement
	 * HasHTML instead of HasText.
	 */
	public SetupSelectFiscalYrDatePage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createControls() {
		// TODO Auto-generated method stub

		fiscalStarts.setText(accounterConstants.myFiscalYearsStartsIn());
		fiscalHeader.setText(accounterConstants.selectdateToTrackFinance());
		startDate.setText(accounterConstants.yourSelecteddateisStartdate());
		beginingFiscal.setText(accounterConstants.beginingOfthefiscalYear());
		beginingFiscalText.setText(accounterConstants
				.enterTransactionsTocompleteTaxreturns());
		todaysDate.setText(accounterConstants.useTodaysDateasStartdate());
		todaysDateText.setText(accounterConstants
				.enterTransactionsTocompleteTaxreturns());
		String[] monthNames = new String[] { accounterConstants.january(),
				accounterConstants.february(), accounterConstants.march(),
				accounterConstants.april(), accounterConstants.may(),
				accounterConstants.june(), accounterConstants.july(),
				accounterConstants.august(), accounterConstants.september(),
				accounterConstants.october(), accounterConstants.november(),
				accounterConstants.december() };
		// fiscalStartsList = null;
		for (int i = 0; i < monthNames.length; i++) {
			fiscalStartsList.addItem(monthNames[i]);
		}
		fiscalInfo.setText(accounterConstants.fiscalYearsaemasTaxyear());
		fiscalHead.setText(accounterConstants.selectFirstMonthOfFiscalYear());
	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub

	}

}
