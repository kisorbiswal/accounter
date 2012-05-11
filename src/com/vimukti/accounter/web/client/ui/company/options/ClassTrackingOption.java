/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.RadioGroupItem;

/**
 * @author vimukti36
 * 
 */
public class ClassTrackingOption extends AbstractPreferenceOption {

	CheckboxItem trackClassCheckBox;

	LabelItem classLabel;

	RadioGroupItem transactionRadioButton;

	StyledPanel hidePanel;

	LabelItem oneperTransactionLabel, oneperdetaillineLabel;

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
		super("");
		createControls();
		initData();
	}

	public void initData() {
		trackClassCheckBox.setValue(getCompanyPreferences()
				.isClassTrackingEnabled());
		hidePanel.setVisible(getCompanyPreferences().isClassTrackingEnabled());
		if (getCompanyPreferences().isClassPerDetailLine())
			transactionRadioButton.setValue(messages.onepertransaction());
		else
			transactionRadioButton.setValue(messages.oneperdetailline());
	}

	public void createControls() {

		hidePanel = new StyledPanel("classTrackingHidepanel");

		trackClassCheckBox = new CheckboxItem(messages.classTracking(),
				"header");

		classLabel = new LabelItem(messages.classInAllTransaction(),
				"classLabel");

		oneperTransactionLabel = new LabelItem(messages.onepertransaction()
				+ " : " + messages.onePerTransactionclassTrackingDescription(),
				"organisation_comment");

		oneperdetaillineLabel = new LabelItem(messages.oneperdetailline()
				+ " : " + messages.onePerDetailLineclassTrackingDescription(),
				"organisation_comment");

		transactionRadioButton = new RadioGroupItem();
		transactionRadioButton.setDefaultValue("transactionRadioButton");
		transactionRadioButton.setShowTitle(false);

		transactionRadioButton.setValueMap(messages.onepertransaction(),
				messages.oneperdetailline());
		transactionRadioButton.setDefaultValue(messages.onepertransaction());

		trackClassCheckBox.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				hidePanel.setVisible(trackClassCheckBox.getValue());

			}
		});

		hidePanel.add(oneperTransactionLabel);
		hidePanel.add(oneperdetaillineLabel);
		hidePanel.add(transactionRadioButton);
		add(trackClassCheckBox);
		add(classLabel);
		add(hidePanel);
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setClassTrackingEnabled(
				trackClassCheckBox.getValue());
		if (transactionRadioButton.getValue().equals(
				messages.onepertransaction())) {
			getCompanyPreferences().setClassPerDetailLine(true);
		} else {
			getCompanyPreferences().setClassPerDetailLine(false);
		}

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
