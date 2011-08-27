package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import java.util.List;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;

public class CompanyInfoView extends BaseView<ClientCompanyPreferences> {
	private VerticalPanel detailPanel;
	private AccounterConstants constants = Accounter.constants();
	private AccounterMessages messages = Accounter.messages();

	@Override
	public void init() {
		super.init();
		createControls();
		// setSize(width, height)
	}

	private void createControls() {
		HorizontalPanel mainPanel = new HorizontalPanel();
		StackPanel stackPanel = new StackPanel();
		detailPanel = new VerticalPanel();

		stackPanel.add(getBasicInfoPanel(), constants.basicInfo());
		stackPanel.add(getCompanyInfoPanel(), constants.comapnyInfo());
		stackPanel.add(getBankingAndFinancialInfoPanel(), constants
				.bankingAndOtherFinancialDetails());
		stackPanel.add(getOtherDetailsPanel(), constants.otherDetails());

		mainPanel.add(stackPanel);
		mainPanel.add(detailPanel);

		add(mainPanel);
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		// TODO Auto-generated method stub
		super.createButtons(buttonBar);
	}

	public VerticalPanel getBasicInfoPanel() {
		VerticalPanel basicInfoPanel = new VerticalPanel();

		HTML adminInfoLabel = new HTML(messages.adminInfo());

		basicInfoPanel.add(adminInfoLabel);

		return basicInfoPanel;
	}

	private void addDetailsPanel(Panel panel) {
		detailPanel.clear();
		detailPanel.add(panel);
	}

	public VerticalPanel getCompanyInfoPanel() {
		VerticalPanel companyInfoPanel = new VerticalPanel();

		HTML companyRegisteredeDetailsLabel = new HTML(messages
				.registeredDetails());
		HTML companyTradingDetailsLabel = new HTML(messages.tradingDetails());
		HTML companyOtherDetailsLabel = new HTML(messages
				.companyOtherDetails());

		companyInfoPanel.add(companyRegisteredeDetailsLabel);
		companyInfoPanel.add(companyTradingDetailsLabel);
		companyInfoPanel.add(companyOtherDetailsLabel);

		return companyInfoPanel;
	}

	public VerticalPanel getBankingAndFinancialInfoPanel() {
		VerticalPanel bankingAndFinancialInfoPanel = new VerticalPanel();

		HTML bankingAndOtherFinancialDetailsLabel = new HTML(messages
				.bankingAndOtherFinancialDetails());

		bankingAndFinancialInfoPanel.add(bankingAndOtherFinancialDetailsLabel);

		return bankingAndFinancialInfoPanel;
	}

	public VerticalPanel getOtherDetailsPanel() {
		VerticalPanel otherDetailsPanel = new VerticalPanel();

		HTML customerAndvendorSettingsLabel = new HTML(messages
				.customerAndvendorSettings());
		HTML doYouUseAndHowDoYouReferLabel = new HTML(messages
				.doYouUseAndHowDoYouRefer());
		HTML ageingAndSellingDetailsLabel = new HTML(messages
				.ageingAndSellingDetails());

		otherDetailsPanel.add(customerAndvendorSettingsLabel);
		otherDetailsPanel.add(doYouUseAndHowDoYouReferLabel);
		otherDetailsPanel.add(ageingAndSellingDetailsLabel);

		return otherDetailsPanel;
	}

	@Override
	public List getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().companyPrefeTitle();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}	

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

}
