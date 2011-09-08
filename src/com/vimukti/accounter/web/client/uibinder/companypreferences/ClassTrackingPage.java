package com.vimukti.accounter.web.client.uibinder.companypreferences;

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

public class ClassTrackingPage extends AbstractCompanyInfoPanel {

	private static ClassTrackingPageUiBinder uiBinder = GWT
			.create(ClassTrackingPageUiBinder.class);
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

	interface ClassTrackingPageUiBinder extends
			UiBinder<Widget, ClassTrackingPage> {
	}

	public ClassTrackingPage() {
		initWidget(uiBinder.createAndBindUi(this));
		createControls();
	}

	private void createControls() {
		classTrackingCheckBox.setText("Class Tracking");
		classWarningCheckBox.setText("Class Warning");
		classesOnSalesLabel.setText("Classes on sales");
		onePerTransactionRadio.setName("class_tracking");
		onePerTransactionRadio.setHTML("One per transaction");
		onePerDetailLineRadio.setName("class_tracking");
		onePerDetailLineRadio.setHTML("One per detail line");
		classTrackingCheckBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hiddenPanel.setVisible(classTrackingCheckBox.getValue());

			}
		});
	}

	@Override
	public void onLoad() {
		
	}

	@Override
	public void onSave() {
		// TODO Auto-generated method stub

	}

}
