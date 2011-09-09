package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;

public class SetupTrackBillsAndTimePage extends AbstractSetupPage {

	private static final String TRACK_BIllS = "Track bills";
	private static final String TRACKING_TIME = "Tracling Time";
	private static final int YES = 1;
	private static final int NO = 2;
	private VerticalPanel mainPanel;
	private HTML billsdescription, billsmanageCashflowHtml1,
			billsmanageCashflowHtml2, billsmanageCashflowHtml3,
			billssubtitleHtml, timedescription, timemanageCashflowHtml1,
			timemanageCashflowHtml2, timemanageCashflowHtml3, timesubtitleHtml;
	private RadioButton billsyesRadioButton, billsnoRadioButton,
			timeyesRadioButton, timenoRadioButton;

	@Override
	public String getHeader() {
		return this.accounterConstants.managingBills();
	}

	@Override
	public VerticalPanel getPageBody() {
		createControls();
		mainPanel.addStyleName("setuppage_body");
		return mainPanel;
	}

	/**
	 * Create all Controls
	 */
	public void createControls() {
		mainPanel = new VerticalPanel();

		billsdescription = new HTML(Accounter.messages()
				.billstrackingdescription(Global.get().account()));
		billsmanageCashflowHtml1 = new HTML(Accounter.messages()
				.billstrackingmanageCashflowStep1(Global.get().vendor()));
		billsmanageCashflowHtml2 = new HTML(
				accounterConstants.billstrackingmanageCashflowStep2());
		billsmanageCashflowHtml3 = new HTML(
				accounterConstants.billstrackingmanageCashflowStep3());
		mainPanel.add(billsdescription);
		mainPanel.add(billsmanageCashflowHtml1);
		mainPanel.add(billsmanageCashflowHtml2);
		mainPanel.add(billsmanageCashflowHtml3);

		billssubtitleHtml = new HTML(accounterConstants.doyouwantTrackBills());
		billssubtitleHtml.setStyleName("BOLD");
		mainPanel.add(billssubtitleHtml);
		billsyesRadioButton = new RadioButton(TRACK_BIllS,
				accounterConstants.yes());

		mainPanel.add(billsyesRadioButton);

		billsnoRadioButton = new RadioButton(TRACK_BIllS,
				accounterConstants.no());

		mainPanel.add(billsnoRadioButton);

		timedescription = new HTML(accounterConstants.timetrackingdescription());
		timedescription.setStyleName("BOLD");
		timemanageCashflowHtml1 = new HTML(Accounter.messages()
				.timetrackingflowStep1(Global.get().customer()));
		timemanageCashflowHtml2 = new HTML(
				accounterConstants.timetrackingflowStep2());
		timemanageCashflowHtml3 = new HTML(
				accounterConstants.timetrackingflowStep3());
		mainPanel.add(timedescription);
		mainPanel.add(timemanageCashflowHtml1);
		mainPanel.add(timemanageCashflowHtml2);
		mainPanel.add(timemanageCashflowHtml3);

		timesubtitleHtml = new HTML(accounterConstants.doyouwantTrackTime());
		billssubtitleHtml.setStyleName("BOLD");
		mainPanel.add(timesubtitleHtml);
		timeyesRadioButton = new RadioButton(TRACKING_TIME,
				accounterConstants.yes());

		mainPanel.add(timeyesRadioButton);

		timenoRadioButton = new RadioButton(TRACKING_TIME,
				accounterConstants.no());

		mainPanel.add(timenoRadioButton);

	}

	@Override
	public void onLoad() {

		if (preferences.isKeepTrackofBills()) {
			billsyesRadioButton.setValue(true);
		} else {
			billsnoRadioButton.setValue(true);
		}
		if (preferences.isKeepTrackOfTime()) {
			timeyesRadioButton.setValue(true);
		} else {
			timenoRadioButton.setValue(true);
		}
	}

	@Override
	public void onSave() {
		if (billsyesRadioButton.getValue()) {
			preferences.setKeepTrackofBills(true);
		} else {
			preferences.setKeepTrackofBills(false);
		}

		if (timeyesRadioButton.getValue()) {
			preferences.setKeepTrackOfTime(true);
		} else {
			preferences.setKeepTrackOfTime(false);
		}
	}

	@Override
	public boolean doShow() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

}
