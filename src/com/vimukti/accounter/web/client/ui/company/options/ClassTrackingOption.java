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
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author vimukti36
 * 
 */
public class ClassTrackingOption extends AbstractPreferenceOption {

	@UiField
	CheckBox trackClassCheckBox;

	@UiField
	Label classLabel;

	@UiField
	RadioButton onepeTransactionRadioButton;

	@UiField
	Label oneperTransactionLabel;

	@UiField
	RadioButton oneperdetaillineRadioButton;

	@UiField
	Label oneperdetaillineLabel;

	@UiField
	VerticalPanel hidePanel;

	@UiField
	VerticalPanel radioButtonPanel;

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
		trackClassCheckBox.setValue(getCompanyPreferences()
				.isClassTrackingEnabled());
		hidePanel.setVisible(getCompanyPreferences().isClassTrackingEnabled());
		if (getCompanyPreferences().isClassPerDetailLine())
			oneperdetaillineRadioButton.setValue(true);
		else
			onepeTransactionRadioButton.setValue(true);
	}

	public void createControls() {

		trackClassCheckBox.setText(messages.trackclass());
		classLabel.setText(messages.classInAllTransaction());

		onepeTransactionRadioButton.setText(messages.onepertransaction());
		oneperTransactionLabel.setText(messages
				.onePerTransactionclassTrackingDescription());

		oneperdetaillineRadioButton.setText(messages.oneperdetailline());
		oneperdetaillineLabel.setText(messages
				.onePerDetailLineclassTrackingDescription());

		oneperdetaillineRadioButton.setName(messages.className());
		onepeTransactionRadioButton.setName(messages.className());

		oneperTransactionLabel.setStyleName("organisation_comment");
		oneperdetaillineLabel.setStyleName("organisation_comment");

		trackClassCheckBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hidePanel.setVisible(trackClassCheckBox.getValue());
			}
		});

	}

	@Override
	public void onSave() {
		getCompanyPreferences().setClassTrackingEnabled(
				trackClassCheckBox.getValue());
		getCompanyPreferences().setClassPerDetailLine(
				oneperdetaillineRadioButton.getValue());

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
