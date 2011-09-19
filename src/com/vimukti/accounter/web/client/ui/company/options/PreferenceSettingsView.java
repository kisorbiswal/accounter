package com.vimukti.accounter.web.client.ui.company.options;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.company.PreferencePage;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.CancelButton;
import com.vimukti.accounter.web.client.ui.core.SaveAndCloseButton;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class PreferenceSettingsView extends BaseView<ClientCompanyPreferences> {

	private ScrollPanel pageDetailsPane;
	private AccounterConstants constants = Accounter.constants();
	private List<PreferencePage> preferencePages;
	private List<HTML> optionLinks = new ArrayList<HTML>();

	@Override
	public void init() {
		super.init();
		createControls();
		addStyleName("fullSizePanel");
	}

	private void createControls() {
		HorizontalPanel mainPanel = new HorizontalPanel();
		final StackPanel stackPanel = new StackPanel();
		pageDetailsPane = new ScrollPanel();
		pageDetailsPane.addStyleName("pre_scroll_table");
		preferencePages = getPreferencePages();
		for (PreferencePage page : preferencePages) {
			VerticalPanel pageView = createPageView(page);
			stackPanel.add(pageView, page.getTitle());
			pageView.getElement().getParentElement()
					.setAttribute("height", "230px");
		}
		stackPanel.addHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int selectedIndex = stackPanel.getSelectedIndex();
				PreferencePage selectedPage = preferencePages
						.get(selectedIndex);
				pageDetailsPane.clear();
				pageDetailsPane.add(selectedPage);

			}
		}, ClickEvent.getType());

		pageDetailsPane.clear();
		pageDetailsPane.add(preferencePages.get(0));
		mainPanel.add(stackPanel);
		mainPanel.add(pageDetailsPane);
		mainPanel.setCellWidth(pageDetailsPane, "70%");
		mainPanel.setCellWidth(stackPanel, "30%");

		mainPanel.setCellHeight(pageDetailsPane, "96%");
		mainPanel.setCellHeight(stackPanel, "100%");

		pageDetailsPane.setSize("100%", "400px");
		mainPanel.setSize("100%", "100%");
		stackPanel.setSize("250px", "100%");
		mainPanel.addStyleName("fullSizePanel");
		mainPanel.addStyleName("company_stackpanel_view");
		this.add(mainPanel);
		setSize("100%", "100%");
	}

	private List<PreferencePage> getPreferencePages() {
		List<PreferencePage> preferenceList = new ArrayList<PreferencePage>();
		preferenceList.add(getCompanyContactInfoPage());
		preferenceList.add(getCompanyInfoPage());
		// preferenceList.add(getProductAndServicePage());
		preferenceList.add(getCatogiriesInfoPage());
		preferenceList.add(getCustomerAndVendorPage());
		preferenceList.add(getAgningDetailsPage());
		preferenceList.add(getTerminoligies());
		return preferenceList;
	}

	private PreferencePage getAgningDetailsPage() {
		PreferencePage agningDetailsPage = new PreferencePage(
				constants.productAndServices());
		AgeingAndSellingDetailsOption ageingAndSellingDetailsOption = new AgeingAndSellingDetailsOption();
		ProductAndServicesOption productAndServicesOption = new ProductAndServicesOption();

		agningDetailsPage.addPreferenceOption(productAndServicesOption);
		agningDetailsPage.addPreferenceOption(ageingAndSellingDetailsOption);
		return agningDetailsPage;
	}

	private PreferencePage getCustomerAndVendorPage() {
		PreferencePage customerAndVendorPage = new PreferencePage(
				constants.vendorAndPurchases());
		CustomerAndVendorsSettingsOption customerAndVendorsSettingsPage = new CustomerAndVendorsSettingsOption();
		ManageBillsOption manageBillsOption = new ManageBillsOption();
		DoyouUseOption doyouUseOption = new DoyouUseOption();
		customerAndVendorPage.addPreferenceOption(manageBillsOption);
		customerAndVendorPage
				.addPreferenceOption(customerAndVendorsSettingsPage);
		customerAndVendorPage.addPreferenceOption(doyouUseOption);
		return customerAndVendorPage;
	}

	private PreferencePage getProductAndServicePage() {
		PreferencePage productAndServicePage = new PreferencePage(
				constants.productAndServices());

		return productAndServicePage;
	}

	private PreferencePage getCatogiriesInfoPage() {
		PreferencePage catogiriesInfoPage = new PreferencePage(Accounter
				.constants().Categories());
		LocationTrackingOption locationTrackingOption = new LocationTrackingOption();
		ClassTrackingOption classTrackingPage = new ClassTrackingOption();
		catogiriesInfoPage.addPreferenceOption(locationTrackingOption);
		catogiriesInfoPage.addPreferenceOption(classTrackingPage);
		return catogiriesInfoPage;
	}

	private PreferencePage getCompanyInfoPage() {
		PreferencePage companyInfoPage = new PreferencePage(Accounter
				.constants().company());
		// CustomerTerminologyOption terminologyOption = new
		// CustomerTerminologyOption();
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

		// companyInfoPage.addPreferenceOption(terminologyOption);
		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_US) {
			companyInfoPage.addPreferenceOption(formOption);
		}
		// companyPage.addPreferenceOption(numbersOption);
		companyInfoPage.addPreferenceOption(einOption);
		companyInfoPage.addPreferenceOption(fiscalYearOption);
		// companyPage.addPreferenceOption(booksOption);
		// companyPage.addPreferenceOption(autosuggestOption);
		// companyPage.addPreferenceOption(alertsOption);
		// companyPage.addPreferenceOption(autoRecallOption);
		// companyPage.addPreferenceOption(interviewOption);
		// companyPage.addPreferenceOption(afterInactivityOption);
		return companyInfoPage;
	}

	private PreferencePage getCompanyContactInfoPage() {
		PreferencePage companyContactInfoPage = new PreferencePage(Accounter
				.constants().comapnyInfo());
		CompanyNameOption name = new CompanyNameOption();
		CompanyAddressOption address = new CompanyAddressOption();
		CompanyEmailOption email = new CompanyEmailOption();
		ComapnyWebsiteOption website = new ComapnyWebsiteOption();
		CompanyPhoneNumberOption phone = new CompanyPhoneNumberOption();
		companyContactInfoPage.addPreferenceOption(name);
		companyContactInfoPage.addPreferenceOption(address);
		companyContactInfoPage.addPreferenceOption(email);
		companyContactInfoPage.addPreferenceOption(website);
		companyContactInfoPage.addPreferenceOption(phone);
		return companyContactInfoPage;
	}

	private PreferencePage getTerminoligies() {
		PreferencePage teriminalogyPreferencePage = new PreferencePage(
				constants.accounterTerminologies());
		CustomerTerminologyOption productAndServicesOption = new CustomerTerminologyOption();
		VendorTerninalogyOption terminalogyOption = new VendorTerninalogyOption();
		AccountTerminalogyOption accountTerminalogyOption = new AccountTerminalogyOption();
		teriminalogyPreferencePage
				.addPreferenceOption(productAndServicesOption);
		teriminalogyPreferencePage.addPreferenceOption(terminalogyOption);
		teriminalogyPreferencePage
				.addPreferenceOption(accountTerminalogyOption);
		return teriminalogyPreferencePage;
	}

	private VerticalPanel createPageView(final PreferencePage page) {
		final VerticalPanel pageView = new VerticalPanel();
		pageView.setWidth("100%");
		List<AbstractPreferenceOption> options = page.getOptions();
		for (int index = 0; index < options.size(); index++) {
			final AbstractPreferenceOption option = options.get(index);
			final HTML optionLink = new HTML("<a class='stackPanelLink'>"
					+ option.getTitle() + "</a>");
			pageView.add(optionLink);
			// PreferenceOptionLinks.addLink(optionLink);
			if (index == 0) {
				optionLink.getElement().getParentElement()
						.addClassName("contentSelected");
			}
			optionLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					for (int index = 0; index < pageView.getWidgetCount(); index++) {
						Widget widget = pageView.getWidget(index);
						widget.getElement().getParentElement()
								.removeClassName("contentSelected");
						System.out.println(widget);
					}
					optionLink.getElement().getParentElement()
							.addClassName("contentSelected");
					pageDetailsPane.ensureVisible(option);

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
				if (preferencePages == null) {
					return;
				}
				for (PreferencePage page : preferencePages) {
					page.onSave();
					
				}

							Accounter.updateCompany(PreferenceSettingsView.this, Accounter.getCompany());
				Accounter.reset();
			}
		});
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
