package com.vimukti.accounter.web.client.ui.company.options;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class DoyouUseOption extends AbstractPreferenceOption {

	private static DoyouUseOptionUiBinder uiBinder = GWT
			.create(DoyouUseOptionUiBinder.class);

	LabelItem doYouLabelItem;

	CheckboxItem useCustomerNo;

	CheckboxItem useVendorNo;

	CheckboxItem useAccountNo;

	StyledPanel mainPanel;

	interface DoyouUseOptionUiBinder extends UiBinder<Widget, DoyouUseOption> {
	}

	public DoyouUseOption() {
		super("");
		createControls();
		initData();
	}

	public void createControls() {
		doYouLabelItem = new LabelItem(messages.Numbers(), "doYouLabelItem");

		useCustomerNo = new CheckboxItem(messages.usePayeeId(Global.get()
				.customer()), "useCustomerNo");
		useVendorNo = new CheckboxItem(messages.usePayeeId(Global.get()
				.vendor()), "useVendorNo");
		useAccountNo = new CheckboxItem(messages.useAccountNos(),
				"useAccountNo");
		mainPanel = new StyledPanel("doyouUseOption");
		mainPanel.add(useCustomerNo);
		mainPanel.add(useVendorNo);
		mainPanel.add(useAccountNo);
		add(mainPanel);

	}

	@Override
	public String getTitle() {
		return messages.Numbers();
	}

	@Override
	public void onSave() {
		getCompanyPreferences().setUseCustomerId(useCustomerNo.getValue());
		getCompanyPreferences().setUseVendorId(useVendorNo.getValue());
		getCompanyPreferences().setUseAccountNumbers(useAccountNo.getValue());
	}

	@Override
	public String getAnchor() {
		return messages.Numbers();
	}

	@Override
	public void initData() {
		useCustomerNo.setValue(getCompanyPreferences().getUseCustomerId());
		useVendorNo.setValue(getCompanyPreferences().getUseVendorId());
		useAccountNo.setValue(getCompanyPreferences().getUseAccountNumbers());

	}

}
