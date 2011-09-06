package com.vimukti.accounter.web.client.uibinder.companypreferences;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompany;
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
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class CompanyInfoPage extends BaseView<ClientCompanyPreferences> {
	private VerticalPanel detailPanel;
	private AccounterConstants constants = Accounter.constants();
	private AccounterMessages messages = Accounter.messages();
	private ClientCompanyPreferences companyPreferences;
	private ClientCompany company;
	private AbstractCompanyInfoPanel companyInfoPanel,
			companyRegisteredeDetailsPanel, companyTradingDetailsPanel,
			companyOtherDetailsPanel, organisationPanel,
			bankingAndOtherFinancialDetailsPanel,
			customerAndvendorSettingsPanel, doYouUseAndHowDoYouReferPanel,
			ageingAndSellingDetailsPanel, employeeSettingsPanel;
	private HTML companyRegisteredeDetailsLink, companyTradingDetailsLink,
			companyOtherDetailsLink, organisationLink,
			bankingAndOtherFinancialDetailsLink, customerAndvendorSettingsLink,
			doYouUseAndHowDoYouReferLink, ageingAndSellingDetailsLink,
			employeeSettingsLink;
	private CompanyInfo[] companyInfos = new CompanyInfo[9];

	@Override
	public void init() {
		super.init();
		createControls();
		addStyleName("fullSizePanel");
	}

	private void createControls() {
		try {
			company = Accounter.getCompany();
			companyPreferences = company.getPreferences();
			HorizontalPanel mainPanel = new HorizontalPanel();
			StackPanel stackPanel = new StackPanel();
			detailPanel = new VerticalPanel();

			// stackPanel.add(getBasicInfoPanel(), constants.basicInfo());
			stackPanel.add(getCompanyInfoPanel(), constants.comapnyInfo());
			stackPanel.add(getBankingAndFinancialInfoPanel(), constants
					.otherDetails());
			stackPanel.add(getOtherDetailsPanel(), constants
					.accounterSettings());

			companyInfoPanel = new CompanyRegisteredDetailsPage(
					companyPreferences, company, this);
			companyRegisteredeDetailsLink.getElement().getParentElement()
					.setClassName("contentSelected");
			companyInfoPanel.addStyleName("fullSizePanel");
			detailPanel.add(companyInfoPanel);

			mainPanel.add(stackPanel);
			mainPanel.add(detailPanel);
			mainPanel.setCellWidth(detailPanel, "70%");
			mainPanel.setCellWidth(stackPanel, "30%");

			mainPanel.setCellHeight(detailPanel, "96%");
			mainPanel.setCellHeight(stackPanel, "100%");

			detailPanel.setSize("100%", "94%");
			stackPanel.setSize("250px", "100%");
			mainPanel.addStyleName("fullSizePanel");
			mainPanel.addStyleName("company_stackpanel_view");
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
		saveAndCloseButton.setText(constants.update());
		cancelButton.setText(constants.close());
		buttonBar.add(saveAndCloseButton);
		buttonBar.add(cancelButton);
		saveAndCloseButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel.onSave();
				Accounter.setCompany(company);
				// Accounter.
			}
		});
	}

	// public VerticalPanel getBasicInfoPanel() {
	// VerticalPanel basicInfoPanel = new VerticalPanel();
	//
	// HTML adminInfoLink = new HTML(messages.adminInfo());
	// basicInfoPanel.add(adminInfoLink);
	//
	// adminInfoLink.addClickHandler(new ClickHandler() {
	// @Override
	// public void onClick(ClickEvent event) {
	// companyInfoPanel = new AdminInfoPanel(companyPreferences,
	// company, CompanyInfoView.this);
	// addDetailsPanel(companyInfoPanel);
	// }
	// });
	//
	// return basicInfoPanel;
	// }

	public VerticalPanel getCompanyInfoPanel() {
		VerticalPanel companyInfo1Panel = new VerticalPanel();

		companyRegisteredeDetailsLink = new HTML(messages.registeredDetails());
		companyTradingDetailsLink = new HTML(messages.tradingDetails());
		companyOtherDetailsLink = new HTML(messages.companyOtherDetails());
		organisationLink = new HTML("<a>" + constants.organisation() + "</a>");

		companyInfo1Panel.add(companyRegisteredeDetailsLink);
		companyInfo1Panel.add(companyTradingDetailsLink);
		companyInfo1Panel.add(companyOtherDetailsLink);
		companyInfo1Panel.add(organisationLink);
		companyRegisteredeDetailsPanel = new CompanyRegisteredDetailsPage(
				companyPreferences, company, CompanyInfoPage.this);
		companyTradingDetailsPanel = new CompanyTradingDetailsPage();
		companyOtherDetailsPanel = new CompanyOtherDetailsPage();
		organisationPanel = new OrganizationDetailsPage();

		companyInfos[0] = new CompanyInfo(companyRegisteredeDetailsLink,
				companyRegisteredeDetailsPanel);
		companyInfos[1] = new CompanyInfo(companyTradingDetailsLink,
				companyTradingDetailsPanel);
		companyInfos[2] = new CompanyInfo(companyOtherDetailsLink,
				companyOtherDetailsPanel);
		companyInfos[3] = new CompanyInfo(organisationLink, organisationPanel);

		companyRegisteredeDetailsLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = companyRegisteredeDetailsPanel;
				addDetailsPanel(companyInfoPanel);

			}
		});
		companyTradingDetailsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = companyTradingDetailsPanel;
				companyInfos[1] = new CompanyInfo(
						companyRegisteredeDetailsLink, companyInfoPanel);
				addDetailsPanel(companyInfoPanel);
			}
		});

		companyOtherDetailsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = companyOtherDetailsPanel;
				addDetailsPanel(companyInfoPanel);
			}
		});

		organisationLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = organisationPanel;
				addDetailsPanel(companyInfoPanel);
			}
		});
		companyInfo1Panel.setWidth("100%");
		return companyInfo1Panel;
	}

	public VerticalPanel getBankingAndFinancialInfoPanel() {

		VerticalPanel bankingAndFinancialInfoPanel = new VerticalPanel();

		bankingAndOtherFinancialDetailsLink = new HTML(messages
				.bankingAndOtherFinancialDetails());

		bankingAndFinancialInfoPanel.add(bankingAndOtherFinancialDetailsLink);
		bankingAndOtherFinancialDetailsPanel = new BankingAndOtherFinancialDetailsPage();
		companyInfos[4] = new CompanyInfo(bankingAndOtherFinancialDetailsLink,
				bankingAndOtherFinancialDetailsPanel);

		bankingAndOtherFinancialDetailsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = bankingAndOtherFinancialDetailsPanel;
				addDetailsPanel(companyInfoPanel);
			}
		});

		bankingAndFinancialInfoPanel.setWidth("100%");
		return bankingAndFinancialInfoPanel;
	}

	public VerticalPanel getOtherDetailsPanel() {

		VerticalPanel otherDetailsPanel = new VerticalPanel();

		customerAndvendorSettingsLink = new HTML(messages
				.customerAndvendorSettings());
		doYouUseAndHowDoYouReferLink = new HTML(messages
				.doYouUseAndHowDoYouRefer());
		ageingAndSellingDetailsLink = new HTML(messages
				.ageingAndSellingDetails());
		employeeSettingsLink = new HTML(messages.employeeSettings());

		otherDetailsPanel.add(customerAndvendorSettingsLink);
		otherDetailsPanel.add(doYouUseAndHowDoYouReferLink);
		otherDetailsPanel.add(ageingAndSellingDetailsLink);
		otherDetailsPanel.add(employeeSettingsLink);

		customerAndvendorSettingsPanel = new CustomerAndVendorsSettingsPage();
		doYouUseAndHowDoYouReferPanel = new DoYouUseAndHowDoYouReferPage();
		ageingAndSellingDetailsPanel = new AgeingAndSellingDetailsPage();
		employeeSettingsPanel = new EmployeeSettingsPage();

		companyInfos[5] = new CompanyInfo(customerAndvendorSettingsLink,
				customerAndvendorSettingsPanel);
		companyInfos[6] = new CompanyInfo(doYouUseAndHowDoYouReferLink,
				doYouUseAndHowDoYouReferPanel);
		companyInfos[7] = new CompanyInfo(ageingAndSellingDetailsLink,
				ageingAndSellingDetailsPanel);
		companyInfos[8] = new CompanyInfo(employeeSettingsLink,
				employeeSettingsPanel);

		customerAndvendorSettingsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = customerAndvendorSettingsPanel;
				addDetailsPanel(companyInfoPanel);
			}
		});
		doYouUseAndHowDoYouReferLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = doYouUseAndHowDoYouReferPanel;
				addDetailsPanel(companyInfoPanel);
			}
		});
		ageingAndSellingDetailsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = ageingAndSellingDetailsPanel;
				addDetailsPanel(companyInfoPanel);
			}
		});
		employeeSettingsLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = employeeSettingsPanel;
				addDetailsPanel(companyInfoPanel);
			}
		});
		otherDetailsPanel.setWidth("100%");
		return otherDetailsPanel;
	}

	private void addDetailsPanel(AbstractCompanyInfoPanel panel) {
		detailPanel.clear();
		try {
			panel.addStyleName("fullSizePanel");
			removeAllLabelsStyle();
			getInstanceOfPanel(panel);
			detailPanel.add(panel);
			panel.onLoad();
		} catch (Exception e) {
			System.err.println(e);
		}

	}

	private void getInstanceOfPanel(AbstractCompanyInfoPanel panel) {
		for (int i = 0; i < companyInfos.length; i++) {
			if (panel instanceof CompanyTradingDetailsPage)
				companyTradingDetailsLink.getElement().getParentElement()
						.addClassName("contentSelected");
			else if (panel instanceof EmployeeSettingsPage)
				employeeSettingsLink.getElement().getParentElement()
						.addClassName("contentSelected_LastChild");
			else if (panel.equals(companyInfos[i].getPanel())) {
				companyTradingDetailsLink.getElement().getParentElement()
						.removeClassName("contentSelected");
				companyInfos[i].getHTML().getElement().getParentElement()
						.addClassName("contentSelected");

			}
		}
	}

	private void removeAllLabelsStyle() {
		for (int i = 0; i < companyInfos.length; i++) {
			companyInfos[i].getHTML().getElement().getParentElement()
					.removeClassName("contentSelected");
			companyInfos[i].getHTML().getElement().getParentElement()
					.removeClassName("contentSelected_LastChild");
		}
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
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

class CompanyInfo {
	private HTML html;
	private AbstractCompanyInfoPanel panel;

	public CompanyInfo(HTML html, AbstractCompanyInfoPanel panel) {
		this.html = html;
		this.panel = panel;
		html.addStyleName("stackPanelLink");
	}

	public HTML getHTML() {
		return html;
	}

	public AbstractCompanyInfoPanel getPanel() {
		return panel;
	}
}