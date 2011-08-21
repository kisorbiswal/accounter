/**
 * 
 */
package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Administrator
 * 
 */
public class SetupTrackBillsAndTimePage extends AbstractSetupPage {

	private static SetupTrackBillsAndTimePageUiBinder uiBinder = GWT
			.create(SetupTrackBillsAndTimePageUiBinder.class);
	@UiField
	VerticalPanel viewPanel;
	@UiField
	VerticalPanel trackingBillsPanel;
	@UiField
	VerticalPanel managing;
	@UiField
	HTML trackOfBillsText;
	@UiField
	HTML trackOfBillsList;
	@UiField
	HTML managingList;
	@UiField
	HTML trackTimeText;
	@UiField
	RadioButton managingYes;
	@UiField
	RadioButton trackingTimeYes;
	@UiField
	RadioButton managingNo;
	@UiField
	RadioButton trackingNo;
	@UiField
	HTML managingInfo;
	@UiField
	HTML trackingTimeDes;
	@UiField
	Label headerLabel;

	interface SetupTrackBillsAndTimePageUiBinder extends
			UiBinder<Widget, SetupTrackBillsAndTimePage> {
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
	public SetupTrackBillsAndTimePage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	@Override
	protected void createControls() {
		headerLabel.setText(accounterConstants.managingBills());

		trackOfBillsText.setText(accounterConstants.doyouwantTrackTime());
		trackOfBillsList.setText(accounterMessages.trackTimeList());
		managingList.setText(accounterMessages.managingList());
		trackTimeText.setText(accounterConstants.doyouwantTrackBills());
		managingYes.setText(accounterConstants.yes());
		trackingTimeYes.setText(accounterConstants.yes());
		managingNo.setText(accounterConstants.no());
		trackingNo.setText(accounterConstants.no());
		trackingTimeDes.setText(accounterConstants.timetrackingdescription());
		managingInfo.setText(accounterConstants.billstrackingdescription());

	}

	@Override
	public boolean doShow() {
		return true;
	}

	@Override
	public void onLoad() {

		if (preferences.isDoyouKeepTrackofBills()) {
			managingYes.setValue(true);
		} else {
			managingNo.setValue(true);
		}
		if (preferences.isDoYouKeepTrackOfTime()) {
			trackingTimeYes.setValue(true);
		} else {
			trackingNo.setValue(true);
		}
	}

	@Override
	public void onSave() {
		if (managingYes.getValue()) {
			preferences.setDoyouKeepTrackofBills(true);
		} else {
			preferences.setDoyouKeepTrackofBills(false);
		}

		if (trackingTimeYes.getValue()) {
			preferences.setDoYouKeepTrackOfTime(true);
		} else {
			preferences.setDoYouKeepTrackOfTime(false);
		}
	}

}
