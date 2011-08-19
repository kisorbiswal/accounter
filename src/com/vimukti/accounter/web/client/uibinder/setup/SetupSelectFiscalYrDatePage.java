/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * @author Administrator
 *
 */
public class SetupSelectFiscalYrDatePage extends AbstractSetupPage {

	private static SetupSelectFiscalYrDatePageUiBinder uiBinder = GWT
			.create(SetupSelectFiscalYrDatePageUiBinder.class);
	@UiField VerticalPanel viewPanel;
	@UiField Label FiscalStarts;
	@UiField HTML FiscalHeader;
	@UiField Label StartDate;
	@UiField RadioButton BeginingFiscal;
	@UiField HTML BeginingFiscalText;
	@UiField RadioButton TodaysDate;
	@UiField HTML TodaysDateText;
	@UiField DateBox FiscalDate;

	interface SetupSelectFiscalYrDatePageUiBinder extends
			UiBinder<Widget, SetupSelectFiscalYrDatePage> {
	}

	/**
	 * Because this class has a default constructor, it can
	 * be used as a binder template. In other words, it can be used in other
	 * *.ui.xml files as follows:
	 * <ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
	 *   xmlns:g="urn:import:**user's package**">
	 *  <g:**UserClassName**>Hello!</g:**UserClassName>
	 * </ui:UiBinder>
	 * Note that depending on the widget that is used, it may be necessary to
	 * implement HasHTML instead of HasText.
	 */
	public SetupSelectFiscalYrDatePage() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	protected VerticalPanel getViewBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getViewHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onSave() {
		// TODO Auto-generated method stub
		
	}

	
}
