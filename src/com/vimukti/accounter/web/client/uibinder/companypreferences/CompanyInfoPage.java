package com.vimukti.accounter.web.client.uibinder.companypreferences;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.company.PreferencePage;
import com.vimukti.accounter.web.client.ui.company.options.AbstractPreferenceOption;
import com.vimukti.accounter.web.client.ui.company.options.AccountNumbersOption;
import com.vimukti.accounter.web.client.ui.company.options.AccountTerminalogyOption;
import com.vimukti.accounter.web.client.ui.company.options.AgeingAndSellingDetailsOption;
import com.vimukti.accounter.web.client.ui.company.options.AutoRecallOption;
import com.vimukti.accounter.web.client.ui.company.options.ClosingBooksOption;
import com.vimukti.accounter.web.client.ui.company.options.CompanyEinOption;
import com.vimukti.accounter.web.client.ui.company.options.CompanyFiscalYearOption;
import com.vimukti.accounter.web.client.ui.company.options.CustomerAndVendorsSettingsOption;
import com.vimukti.accounter.web.client.ui.company.options.CustomerTerminologyOption;
import com.vimukti.accounter.web.client.ui.company.options.EmailAlertsOption;
import com.vimukti.accounter.web.client.ui.company.options.LogOutAfterInactivityOption;
import com.vimukti.accounter.web.client.ui.company.options.ManageBillsOption;
import com.vimukti.accounter.web.client.ui.company.options.QuickFillAutosuggestOption;
import com.vimukti.accounter.web.client.ui.company.options.RestartSetupInterviewOption;
import com.vimukti.accounter.web.client.ui.company.options.OrganisationTypeOption;
import com.vimukti.accounter.web.client.ui.company.options.VendorTerninalogyOption;
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
			ageingAndSellingDetailsPanel, employeeSettingsPanel,
			categoriesPanel, locationTrackingPanel, classTrackingPanel;
	private HTML companyRegisteredeDetailsLink, companyTradingDetailsLink,
			companyOtherDetailsLink, organisationLink,
			bankingAndOtherFinancialDetailsLink, customerAndvendorSettingsLink,
			doYouUseAndHowDoYouReferLink, ageingAndSellingDetailsLink,
			employeeSettingsLink, locationTrackingLink, classTrackingLink;
	private CompanyInfo[] companyInfos = new CompanyInfo[11];
	private PreferencePage[] preferencePages = new PreferencePage[4];

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
			final StackPanel stackPanel = new StackPanel();
			detailPanel = new VerticalPanel();

			// stackPanel.add(getBasicInfoPanel(), constants.basicInfo());
			// stackPanel.add(getCompanyInfoPanel(), constants.comapnyInfo());
			// stackPanel.add(getcompanyPanel(), constants.company());
			stackPanel.add(getProductAndServicesPanel(),
					constants.productAndServices());
			// stackPanel.add(getCustomerAndVendorSettings(),
			// "Vendors & Purchases");
			stackPanel.add(getBankingAndFinancialInfoPanel(),
					constants.otherDetails());
			stackPanel.add(getOtherDetailsPanel(),
					constants.accounterSettings());
			stackPanel.add(getCategoriesPanel(), constants.Categories());
			stackPanel.addHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					int selectedIndex = stackPanel.getSelectedIndex();

				}
			}, ClickEvent.getType());
			// companyInfoPanel = new CompanyRegisteredDetailsPage(
			// companyPreferences, company, this);
			// companyRegisteredeDetailsLink.getElement().getParentElement()
			// .setClassName("contentSelected");
			// companyInfoPanel.addStyleName("fullSizePanel");
			// detailPanel.add(companyInfoPanel);
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

	private VerticalPanel getAgeingAndSellingDetaials() {
		PreferencePage preferencePage = new PreferencePage(
				constants.productAndServices());
		AgeingAndSellingDetailsOption ageingAndSellingDetailsOption = new AgeingAndSellingDetailsOption();
		preferencePage.addPreferenceOption(ageingAndSellingDetailsOption);
		detailPanel.add(preferencePage);
		return createPageView(preferencePage);
	}

	private Widget preferencePages() {
		PreferencePage preferencePage = new PreferencePage(
				constants.productAndServices());
		// AgeingAndSellingDetailsOption customerAndVendorsSettingsPage = new
		// AgeingAndSellingDetailsOption();
		// preferencePage.addPreferenceOption(customerAndVendorsSettingsPage);
		detailPanel.add(preferencePage);
		return createPageView(preferencePage);
	}

	private VerticalPanel getProductAndServicesPanel() {
		preferencePages[0] = new PreferencePage(
				constants.customerAndvendorSettings());
		CustomerAndVendorsSettingsOption productAndServicesOption = new CustomerAndVendorsSettingsOption();
		ManageBillsOption manageBillsOption = new ManageBillsOption();

		VendorTerninalogyOption terminalogyOption = new VendorTerninalogyOption();
		AccountTerminalogyOption accountTerminalogyOption = new AccountTerminalogyOption();
		preferencePages[0].addPreferenceOption(productAndServicesOption);
		preferencePages[0].addPreferenceOption(terminalogyOption);
		preferencePages[0].addPreferenceOption(accountTerminalogyOption);
		detailPanel.add(preferencePages[0]);
		return createPageView(preferencePages[0]);
	}

	private VerticalPanel getcompanyPanel() {

		preferencePages[2] = new PreferencePage(Accounter.constants().company());

		CustomerTerminologyOption terminologyOption = new CustomerTerminologyOption();
		OrganisationTypeOption formOption = new OrganisationTypeOption();
		// AccountNumbersOption numbersOption = new AccountNumbersOption();
		CompanyEinOption einOption = new CompanyEinOption();
		CompanyFiscalYearOption fiscalYearOption = new CompanyFiscalYearOption();
		// ClosingBooksOption booksOption = new ClosingBooksOption();
		// QuickFillAutosuggestOption autosuggestOption = new
		// QuickFillAutosuggestOption();
		// EmailAlertsOption alertsOption = new EmailAlertsOption();
		// AutoRecallOption autoRecallOption = new AutoRecallOption();
		// RestartSetupInterviewOption interviewOption = new
		// RestartSetupInterviewOption();
		// LogOutAfterInactivityOption afterInactivityOption = new
		// LogOutAfterInactivityOption();

		preferencePages[2].addPreferenceOption(terminologyOption);
		preferencePages[2].addPreferenceOption(formOption);
		// companyPage.addPreferenceOption(numbersOption);
		preferencePages[2].addPreferenceOption(einOption);
		preferencePages[2].addPreferenceOption(fiscalYearOption);
		// companyPage.addPreferenceOption(booksOption);
		// companyPage.addPreferenceOption(autosuggestOption);
		// companyPage.addPreferenceOption(alertsOption);
		// companyPage.addPreferenceOption(autoRecallOption);
		// companyPage.addPreferenceOption(interviewOption);
		// companyPage.addPreferenceOption(afterInactivityOption);

		detailPanel.add(preferencePages[2]);
		return createPageView(preferencePages[2]);

	}

	private VerticalPanel createPageView(final PreferencePage page) {
		VerticalPanel pageView = new VerticalPanel();
		List<AbstractPreferenceOption> options = page.getOptions();
		for (final AbstractPreferenceOption option : options) {
			HTML optionLink = new HTML("<a>" + option.getTitle() + "</a>");
			pageView.add(optionLink);
			optionLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					page.show(option);
				}
			});
		}
		return pageView;
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
				for (CompanyInfo infoPanel : companyInfos) {
					infoPanel.getPanel().onSave();
				}
				company.setPreferences(companyPreferences);
				Accounter.setCompany(company);
				Accounter.updateCompany(CompanyInfoPage.this, company);
				Window.Location.reload();
			}
		});
	}

	public VerticalPanel getCategoriesPanel() {
		VerticalPanel categories1Panel = new VerticalPanel();
		locationTrackingLink = new HTML("<a>"
				+ messages.locationTracking(Global.get().Location()) + "</a>");
		classTrackingLink = new HTML("<a>" + "Class Tracking" + "</a>");
		categories1Panel.add(locationTrackingLink);
		categories1Panel.add(classTrackingLink);
		locationTrackingPanel = new LocationTrackingPage();
		classTrackingPanel = new ClassTrackingPage();
		companyInfos[9] = new CompanyInfo(locationTrackingLink,
				locationTrackingPanel);
		companyInfos[10] = new CompanyInfo(classTrackingLink,
				classTrackingPanel);
		locationTrackingLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = locationTrackingPanel;
				addDetailsPanel(companyInfoPanel);

			}
		});
		classTrackingLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				companyInfoPanel = classTrackingPanel;
				addDetailsPanel(companyInfoPanel);

			}
		});
		categories1Panel.setWidth("100%");
		return categories1Panel;

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
		PreferencePage companyInfoPage = new PreferencePage(Accounter
				.constants().company());

		CustomerTerminologyOption terminologyOption = new CustomerTerminologyOption();
		OrganisationTypeOption formOption = new OrganisationTypeOption();
		AccountNumbersOption numbersOption = new AccountNumbersOption();
		CompanyEinOption einOption = new CompanyEinOption();
		CompanyFiscalYearOption fiscalYearOption = new CompanyFiscalYearOption();
		ClosingBooksOption booksOption = new ClosingBooksOption();
		QuickFillAutosuggestOption autosuggestOption = new QuickFillAutosuggestOption();
		EmailAlertsOption alertsOption = new EmailAlertsOption();
		AutoRecallOption autoRecallOption = new AutoRecallOption();
		RestartSetupInterviewOption interviewOption = new RestartSetupInterviewOption();
		LogOutAfterInactivityOption afterInactivityOption = new LogOutAfterInactivityOption();

		companyInfoPage.addPreferenceOption(terminologyOption);
		companyInfoPage.addPreferenceOption(formOption);
		companyInfoPage.addPreferenceOption(numbersOption);
		companyInfoPage.addPreferenceOption(einOption);
		companyInfoPage.addPreferenceOption(fiscalYearOption);
		companyInfoPage.addPreferenceOption(booksOption);
		companyInfoPage.addPreferenceOption(autosuggestOption);
		companyInfoPage.addPreferenceOption(alertsOption);
		companyInfoPage.addPreferenceOption(autoRecallOption);
		companyInfoPage.addPreferenceOption(interviewOption);
		companyInfoPage.addPreferenceOption(afterInactivityOption);

		detailPanel.add(companyInfoPage);
		return createPageView(companyInfoPage);
		// companyRegisteredeDetailsLink = new
		// HTML(messages.registeredDetails());
		// companyTradingDetailsLink = new HTML(messages.tradingDetails());
		// companyOtherDetailsLink = new HTML(messages.companyOtherDetails());
		// organisationLink = new HTML("<a>" + constants.organisation() +
		// "</a>");
		//
		// companyInfo1Panel.add(companyRegisteredeDetailsLink);
		// companyInfo1Panel.add(companyTradingDetailsLink);
		// companyInfo1Panel.add(companyOtherDetailsLink);
		// companyInfo1Panel.add(organisationLink);
		// companyRegisteredeDetailsPanel = new CompanyRegisteredDetailsPage(
		// companyPreferences, company, CompanyInfoPage.this);
		// companyTradingDetailsPanel = new CompanyTradingDetailsPage();
		// companyOtherDetailsPanel = new CompanyOtherDetailsPage();
		// organisationPanel = new OrganizationDetailsPage();
		//
		// companyInfos[0] = new CompanyInfo(companyRegisteredeDetailsLink,
		// companyRegisteredeDetailsPanel);
		// companyInfos[1] = new CompanyInfo(companyTradingDetailsLink,
		// companyTradingDetailsPanel);
		// companyInfos[2] = new CompanyInfo(companyOtherDetailsLink,
		// companyOtherDetailsPanel);
		// companyInfos[3] = new CompanyInfo(organisationLink,
		// organisationPanel);
		//
		// companyRegisteredeDetailsLink.addClickHandler(new ClickHandler() {
		// @Override
		// public void onClick(ClickEvent event) {
		// companyInfoPanel = companyRegisteredeDetailsPanel;
		// addDetailsPanel(companyInfoPanel);
		//
		// }
		// });
		// companyTradingDetailsLink.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// companyInfoPanel = companyTradingDetailsPanel;
		// addDetailsPanel(companyInfoPanel);
		// }
		// });
		//
		// companyOtherDetailsLink.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// companyInfoPanel = companyOtherDetailsPanel;
		// addDetailsPanel(companyInfoPanel);
		// }
		// });
		//
		// organisationLink.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// companyInfoPanel = organisationPanel;
		// addDetailsPanel(companyInfoPanel);
		// }
		// });
		// companyInfo1Panel.setWidth("100%");
	}

	public VerticalPanel getBankingAndFinancialInfoPanel() {

		VerticalPanel bankingAndFinancialInfoPanel = new VerticalPanel();

		bankingAndOtherFinancialDetailsLink = new HTML(
				messages.bankingAndOtherFinancialDetails());

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

		customerAndvendorSettingsLink = new HTML(
				messages.customerAndvendorSettings());
		doYouUseAndHowDoYouReferLink = new HTML(
				messages.doYouUseAndHowDoYouRefer());
		ageingAndSellingDetailsLink = new HTML(
				messages.ageingAndSellingDetails());
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
		} catch (Exception e) {
			System.err.println(e);
		}

	}

	private void getInstanceOfPanel(AbstractCompanyInfoPanel panel) {
		for (int i = 0; i < companyInfos.length; i++) {
			if (panel instanceof CompanyTradingDetailsPage)
				companyTradingDetailsLink.getElement().getParentElement()
						.addClassName("contentSelected");
			else if (panel instanceof CompanyRegisteredDetailsPage)
				companyRegisteredeDetailsLink.getElement().getParentElement()
						.addClassName("contentSelected");
			else if (panel instanceof CompanyOtherDetailsPage)
				companyOtherDetailsLink.getElement().getParentElement()
						.addClassName("contentSelected");
			else if (panel instanceof OrganizationDetailsPage)
				organisationLink.getElement().getParentElement()
						.addClassName("contentSelected");
			else if (panel instanceof BankingAndOtherFinancialDetailsPage)
				bankingAndOtherFinancialDetailsLink.getElement()
						.getParentElement().addClassName("contentSelected");
			else if (panel instanceof CustomerAndVendorsSettingsPage)
				customerAndvendorSettingsLink.getElement().getParentElement()
						.addClassName("contentSelected");
			else if (panel instanceof DoYouUseAndHowDoYouReferPage)
				doYouUseAndHowDoYouReferLink.getElement().getParentElement()
						.addClassName("contentSelected");
			else if (panel instanceof AgeingAndSellingDetailsPage)
				ageingAndSellingDetailsLink.getElement().getParentElement()
						.addClassName("contentSelected");
			else if (panel instanceof EmployeeSettingsPage)
				employeeSettingsLink.getElement().getParentElement()
						.addClassName("contentSelected_LastChild");
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

	@Override
	public void setFocus() {
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