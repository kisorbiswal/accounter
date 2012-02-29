/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author vimukti36
 * 
 */
public class ClassTrackingOption extends AbstractPreferenceOption {

	@UiField
	CheckBox classTrackingCheckBox;
	@UiField
	VerticalPanel hiddenPanel;
	@UiField
	Label classTrackingCheckBoxLabel;
	@UiField
	CheckBox classWarningCheckBox;
	@UiField
	Label classWarningCheckBoxLabel;
	// @UiField
	// Label classesOnSalesLabel;
	/**
	 * NOTE onePerDetailLineRadio for Class Tracking , Currently we are
	 * disabling this feature and allowing the user only to select One Per
	 * Transaction.
	 */
	// @UiField
	// RadioButton onePerTransactionRadio;
	// @UiField
	// RadioButton onePerDetailLineRadio;
	// @UiField
	// VerticalPanel classOnSalesPanel;
	// @UiField
	// VerticalPanel onePerRadioPanel;
	@UiField
	Label classTrackingdescriptionLabel;
	private static ClassTrackingOptionUiBinder uiBinder = GWT
			.create(ClassTrackingOptionUiBinder.class);

	interface ClassTrackingOptionUiBinder extends
			UiBinder<Widget, ClassTrackingOption> {
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
	public ClassTrackingOption() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
		initData();
	}

	public void initData() {
		if (getCompanyPreferences().isClassTrackingEnabled()) {
			classTrackingCheckBox.setValue(true);
			hiddenPanel.setVisible(true);
			// if (getCompanyPreferences().isClassOnePerTransaction()) {
			// onePerTransactionRadio.setValue(true);
			// } else {
			// onePerDetailLineRadio.setValue(true);
			// }

			if (getCompanyPreferences().isWarnOnEmptyClass()) {
				classWarningCheckBox.setValue(true);
			}
		} else {
			hiddenPanel.setVisible(false);
		}

	}

	public void createControls() {

		classTrackingdescriptionLabel.setText(messages
				.classTrackingDescription());
		classTrackingdescriptionLabel.setStyleName("organisation_comment");
		classTrackingCheckBox.setText(messages.classTracking());
		classWarningCheckBox.setText(messages.classWarning());
		// classesOnSalesLabel.setText(messages.classesonsales());
		// onePerTransactionRadio.setName(messages
		// .classunderscoretracking());
		// onePerTransactionRadio.setHTML(messages
		// .onepertransaction());
		// onePerDetailLineRadio.setName(messages
		// .classunderscoretracking());
		// onePerDetailLineRadio.setHTML(messages.oneperdetailline());

		// classOnSalesPanel.getElement().getStyle().setPaddingLeft(15,
		// Unit.PX);
		// onePerRadioPanel.getElement().getStyle().setPaddingLeft(20, Unit.PX);

		classTrackingCheckBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (Accounter.hasPermission(Features.CLASS)) {
					hiddenPanel.setVisible(classTrackingCheckBox.getValue());
				} else {
					classTrackingCheckBox.setValue(false);
					Accounter.showSubscriptionWarning();
				}

			}
		});
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setClassTrackingEnabled(
				classTrackingCheckBox.getValue());
		if (classTrackingCheckBox.getValue()) {
			getCompanyPreferences().setClassOnePerTransaction(true);
		} else {
			getCompanyPreferences().setClassOnePerTransaction(false);
		}
		getCompanyPreferences().setWarnOnEmptyClass(
				classWarningCheckBox.getValue());
	}

	@Override
	public String getTitle() {
		return messages.classTracking();
	}

	@Override
	public String getAnchor() {
		return messages.classTracking();
	}

}
