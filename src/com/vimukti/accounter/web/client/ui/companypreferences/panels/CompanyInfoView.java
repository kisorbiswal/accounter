package com.vimukti.accounter.web.client.ui.companypreferences.panels;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
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
import com.vimukti.accounter.web.client.ui.core.CancelButton;
import com.vimukti.accounter.web.client.ui.core.SaveAndCloseButton;

public class CompanyInfoView extends BaseView<ClientCompanyPreferences> {
	private VerticalPanel detailPanel;
	private AccounterConstants constants = Accounter.constants();
	private AccounterMessages messages = Accounter.messages();
	private ClientCompanyPreferences companyPreferences;
	private AbstractCompanyInfoPanel companyInfoPanel;

	@Override
	public void init() {
		super.init();
		createControls();
		// setSize(width, height)
	}

	private void createControls() {
		try {
			HorizontalPanel mainPanel = new HorizontalPanel();
			StackPanel stackPanel = new StackPanel();
			detailPanel = new VerticalPanel();

			stackPanel.add(getBasicInfoPanel(), constants.basicInfo());
			stackPanel.add(getCompanyInfoPanel(), constants.comapnyInfo());
			stackPanel.add(getBankingAndFinancialInfoPanel(), constants
					.bankingAndOtherFinancialDetails());
			stackPanel.add(getOtherDetailsPanel(), constants.otherDetails());

			companyInfoPanel = new AdminInfoPanel(companyPreferences, this);
			detailPanel.add(companyInfoPanel);

			mainPanel.add(stackPanel);
			mainPanel.add(detailPanel);

			add(mainPanel);
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		this.saveAndCloseButton = new SaveAndCloseButton(this);
		this.cancelButton = new CancelButton(this);
		saveAndCloseButton.setText(constants.save());
		buttonBar.add(saveAndCloseButton);
		buttonBar.add(cancelButton);
	}

	public VerticalPanel getBasicInfoPanel() {
		VerticalPanel basicInfoPanel = new VerticalPanel();

		HTML adminInfoLink = new HTML(messages.adminInfo());
		basicInfoPanel.add(adminInfoLink);

		adminInfoLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = new AdminInfoPanel(companyPreferences,
						CompanyInfoView.this);
				addDetailsPanel(companyInfoPanel);
			}
		});

		return basicInfoPanel;
	}

	private void addDetailsPanel(AbstractCompanyInfoPanel panel) {
		detailPanel.clear();
		panel.onLoad();
		detailPanel.add(panel);
	}

	public VerticalPanel getCompanyInfoPanel() {
		VerticalPanel companyInfo1Panel = new VerticalPanel();

		HTML companyRegisteredeDetailsLink = new HTML(messages
				.registeredDetails());
		HTML companyTradingDetailsLink = new HTML(messages.tradingDetails());
		HTML companyOtherDetailsLink = new HTML(messages.companyOtherDetails());

		companyInfo1Panel.add(companyRegisteredeDetailsLink);
		companyInfo1Panel.add(companyTradingDetailsLink);
		companyInfo1Panel.add(companyOtherDetailsLink);

		companyRegisteredeDetailsLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = new CompanyRegisteredeDetailsPanel(
						companyPreferences);
				addDetailsPanel(companyInfoPanel);

			}
		});
		companyTradingDetailsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = new CompanyTradingDetailsPanel(
						companyPreferences);
				addDetailsPanel(companyInfoPanel);
			}
		});

		companyOtherDetailsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = new CompanyOtherDetailsPanel(
						companyPreferences, CompanyInfoView.this);
				addDetailsPanel(companyInfoPanel);
			}
		});
		return companyInfo1Panel;
	}

	public VerticalPanel getBankingAndFinancialInfoPanel() {
		VerticalPanel bankingAndFinancialInfoPanel = new VerticalPanel();

		HTML bankingAndOtherFinancialDetailsLink = new HTML(messages
				.bankingAndOtherFinancialDetails());

		bankingAndFinancialInfoPanel.add(bankingAndOtherFinancialDetailsLink);

		bankingAndOtherFinancialDetailsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = new BankingAndOtherFinancialDetailsPanel(
						companyPreferences);
				addDetailsPanel(companyInfoPanel);
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
				companyInfoPanel = new CustomerAndvendorSettingsPanel(
						companyPreferences);
				addDetailsPanel(companyInfoPanel);
			}
		});
		doYouUseAndHowDoYouReferLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = new DoYouUseAndHowDoYouReferPanel(
						companyPreferences);
				addDetailsPanel(companyInfoPanel);
			}
		});
		ageingAndSellingDetailsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = new AgeingAndSellingDetailsPanel(
						companyPreferences);
				addDetailsPanel(companyInfoPanel);
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
