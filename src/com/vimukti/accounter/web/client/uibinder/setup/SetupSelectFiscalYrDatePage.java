/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import java.util.Date;

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
	ListBox fiscalStartsList;
	@UiField
	HTML fiscalInfo;
	@UiField
	HTML fiscalHead;
	@UiField
	Label headerLabel;

	String[] monthNames;

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
	protected void createControls() {
		headerLabel.setText(accounterConstants.selectFirstMonthOfFiscalYear());

		fiscalStarts.setText(accounterConstants.myFiscalYearsStartsIn());
		monthNames = new String[] { accounterConstants.january(),
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
	public void onLoad() {
		if (monthNames.length > 0)
			fiscalStartsList.setSelectedIndex(preferences
					.getFiscalYearFirstMonth());

	}

	@Override
	public void onSave() {
		preferences
				.setFiscalYearFirstMonth(fiscalStartsList.getSelectedIndex());
	}

	@Override
	public boolean doShow() {
		return true;
	}

}
