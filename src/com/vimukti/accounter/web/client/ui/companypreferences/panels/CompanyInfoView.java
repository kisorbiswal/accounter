package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
	private ClientCompanyPreferences companyPreferences;

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
	}

	public VerticalPanel getBasicInfoPanel() {
		VerticalPanel basicInfoPanel = new VerticalPanel();

		HTML adminInfoLink = new HTML(messages.adminInfo());
		basicInfoPanel.add(adminInfoLink);

		adminInfoLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				AdminInfoPanel panel = new AdminInfoPanel(companyPreferences);
				addDetailsPanel(panel);
			}
		});

		return basicInfoPanel;
	}

	private void addDetailsPanel(Panel panel) {
		detailPanel.clear();
		detailPanel.add(panel);
	}

	public VerticalPanel getCompanyInfoPanel() {
		VerticalPanel companyInfoPanel = new VerticalPanel();

		HTML companyRegisteredeDetailsLink = new HTML(messages
				.registeredDetails());
		HTML companyTradingDetailsLink = new HTML(messages.tradingDetails());
		HTML companyOtherDetailsLink = new HTML(messages.companyOtherDetails());

		companyInfoPanel.add(companyRegisteredeDetailsLink);
		companyInfoPanel.add(companyTradingDetailsLink);
		companyInfoPanel.add(companyOtherDetailsLink);

		companyRegisteredeDetailsLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				CompanyRegisteredeDetailsPanel registeredeDetailsPanel = new CompanyRegisteredeDetailsPanel(
						companyPreferences);
				addDetailsPanel(registeredeDetailsPanel);

			}
		});
		companyTradingDetailsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CompanyTradingDetailsPanel tradingDetailsPanel = new CompanyTradingDetailsPanel(
						companyPreferences);
				addDetailsPanel(tradingDetailsPanel);
			}
		});

		companyOtherDetailsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CompanyOtherDetailsPanel otherDetailsPanel = new CompanyOtherDetailsPanel(
						companyPreferences);
				addDetailsPanel(otherDetailsPanel);
			}
		});
		return companyInfoPanel;
	}

	public VerticalPanel getBankingAndFinancialInfoPanel() {
		VerticalPanel bankingAndFinancialInfoPanel = new VerticalPanel();

		HTML bankingAndOtherFinancialDetailsLink = new HTML(messages
				.bankingAndOtherFinancialDetails());

		bankingAndFinancialInfoPanel.add(bankingAndOtherFinancialDetailsLink);

		bankingAndOtherFinancialDetailsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				BankingAndOtherFinancialDetailsPanel bankingAndOtherFinancialDetailsPanel = new BankingAndOtherFinancialDetailsPanel(
						companyPreferences);
				addDetailsPanel(bankingAndOtherFinancialDetailsPanel);
			}
		});

		return bankingAndFinancialInfoPanel;
	}

	public VerticalPanel getOtherDetailsPanel() {
		VerticalPanel otherDetailsPanel = new VerticalPanel();

		HTML customerAndvendorSettingsLink = new HTML(messages
				.customerAndvendorSettings());
		HTML doYouUseAndHowDoYouReferLink = new HTML(messages
				.doYouUseAndHowDoYouRefer());
		HTML ageingAndSellingDetailsLink = new HTML(messages
				.ageingAndSellingDetails());

		otherDetailsPanel.add(customerAndvendorSettingsLink);
		otherDetailsPanel.add(doYouUseAndHowDoYouReferLink);
		otherDetailsPanel.add(ageingAndSellingDetailsLink);
		customerAndvendorSettingsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CustomerAndvendorSettingsPanel customerAndvendorSettingsPanel = new CustomerAndvendorSettingsPanel(
						companyPreferences);
				addDetailsPanel(customerAndvendorSettingsPanel);
			}
		});
		doYouUseAndHowDoYouReferLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DoYouUseAndHowDoYouReferPanel doYouReferPanel = new DoYouUseAndHowDoYouReferPanel(
						companyPreferences);
				addDetailsPanel(doYouReferPanel);
			}
		});
		ageingAndSellingDetailsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				AgeingAndSellingDetailsPanel ageingAndSellingDetailsPanel = new AgeingAndSellingDetailsPanel(
						companyPreferences);
				addDetailsPanel(ageingAndSellingDetailsPanel);
			}
		});

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
