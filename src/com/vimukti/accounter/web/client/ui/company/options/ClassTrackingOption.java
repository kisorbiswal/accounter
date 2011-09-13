/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
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
	@UiField
	Label classesOnSalesLabel;
	@UiField
	RadioButton onePerTransactionRadio;
	@UiField
	RadioButton onePerDetailLineRadio;
	@UiField
	VerticalPanel classOnSalesPanel;
	@UiField
	VerticalPanel onePerRadioPanel;

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

	private void initData() {
		if (companyPreferences.isClassTrackingEnabled()) {
			classTrackingCheckBox.setValue(true);
			hiddenPanel.setVisible(true);
			if (companyPreferences.isClassOnePerTransaction()) {
				onePerTransactionRadio.setValue(true);
			} else {
				onePerDetailLineRadio.setValue(true);
			}

			if (companyPreferences.isWarnOnEmptyClass()) {
				classWarningCheckBox.setValue(true);
			}
		} else {
			hiddenPanel.setVisible(false);
		}

	}

	private void createControls() {
		classTrackingCheckBox.setText(Accounter.constants().classTracking());
		classWarningCheckBox.setText(Accounter.constants().classWarning());
		classesOnSalesLabel.setText(Accounter.constants().classesonsales());
		onePerTransactionRadio.setName(Accounter.constants()
				.classunderscoretracking());
		onePerTransactionRadio.setHTML(Accounter.constants()
				.onepertransaction());
		onePerDetailLineRadio.setName(Accounter.constants()
				.classunderscoretracking());
		onePerDetailLineRadio.setHTML(Accounter.constants().oneperdetailline());

		classOnSalesPanel.getElement().getStyle().setPaddingLeft(15, Unit.PX);
		onePerRadioPanel.getElement().getStyle().setPaddingLeft(20, Unit.PX);

		classTrackingCheckBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hiddenPanel.setVisible(classTrackingCheckBox.getValue());

			}
		});
	}

	@Override
	public void onSave() {
		companyPreferences.setClassTrackingEnabled(classTrackingCheckBox
				.getValue());
		companyPreferences.setClassOnePerTransaction(onePerTransactionRadio
				.getValue());
		companyPreferences.setWarnOnEmptyClass(classWarningCheckBox.getValue());
	}

	@Override
	public String getTitle() {
		return Accounter.constants().classTracking();
	}

	@Override
	public String getAnchor() {
		return Accounter.constants().classTracking();
	}

}
