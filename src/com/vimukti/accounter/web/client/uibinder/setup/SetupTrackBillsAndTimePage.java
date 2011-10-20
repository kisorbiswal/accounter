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
import com.vimukti.accounter.web.client.ui.Accounter;

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
	// RadioButton trackingTimeYes;
	// @UiField
	RadioButton managingNo;
	@UiField
	// RadioButton trackingNo;
	// @UiField
	HTML managingInfo;
	@UiField
	HTML trackingTimeDes;
	@UiField
	Label headerLabel;
	@UiField
	Label track_time_head;

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

		// trackOfBillsText.setHTML(accounterConstants.doyouwantTrackTime());
		// trackOfBillsList.setHTML(accounterMessages.trackTimeList());
		managingList.setHTML(accounterMessages.managingList());
		// trackTimeText.setHTML(accounterConstants.doyouwantTrackBills());
		managingYes.setText(accounterConstants.yes());
		// trackingTimeYes.setText(accounterConstants.yes());
		managingNo.setText(accounterConstants.no());
		// trackingNo.setText(accounterConstants.no());
		// trackingTimeDes.setHTML(accounterConstants.timetrackingdescription());
		managingInfo.setHTML(accounterMessages
				.billstrackingdescription(accounterConstants.Accounts()));
		// track_time_head.setText(accounterMessages.trackingtimehead());

	}

	@Override
	public boolean canShow() {
		return true;
	}

	@Override
	public void onLoad() {

		if (preferences.isKeepTrackofBills()) {
			managingYes.setValue(true);
		} else {
			managingNo.setValue(true);
		}
		// if (preferences.isDoYouKeepTrackOfTime()) {
		// trackingTimeYes.setValue(true);
		// } else {
		// trackingNo.setValue(true);
		// }
	}

	@Override
	public void onSave() {
		if (managingYes.getValue()) {
			preferences.setKeepTrackofBills(true);
		} else {
			preferences.setKeepTrackofBills(false);
		}

		// if (trackingTimeYes.getValue()) {
		// preferences.setDoYouKeepTrackOfTime(true);
		// } else {
		// preferences.setDoYouKeepTrackOfTime(false);
		// }
	}

	@Override
	protected boolean validate() {
		if ((!(managingYes.getValue() || managingNo.getValue()))
		/* && (!(trackingTimeYes.getValue() || trackingNo.getValue()) */) {
			Accounter.showError(accounterMessages
					.pleaseEnter(accounterConstants.details()));
			return false;
		} else if (!(managingYes.getValue() || managingNo.getValue())) {
			Accounter.showMessage(accounterConstants.managingBills());
			return false;
			/*
			 * } else if (!(trackingTimeYes.getValue() ||
			 * trackingNo.getValue())) {
			 * Accounter.showMessage(accounterConstants.doyouwantTrackBills());
			 * return false;
			 */
		} else {
			return true;
		}
	}
}
