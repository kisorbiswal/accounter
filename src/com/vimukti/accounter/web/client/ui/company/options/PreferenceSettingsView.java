package com.vimukti.accounter.web.client.ui.company.options;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.company.PreferencePage;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.CancelButton;
import com.vimukti.accounter.web.client.ui.core.SaveAndCloseButton;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class PreferenceSettingsView extends BaseView<ClientCompanyPreferences> {

	private ScrollPanel pageDetailsPanel;
	private List<PreferencePage> preferencePages;

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		HorizontalPanel mainPanel = new HorizontalPanel();
		VerticalPanel titlesPanel = new VerticalPanel();
		pageDetailsPanel = new ScrollPanel();
		pageDetailsPanel.addStyleName("pre_scroll_table");
		preferencePages = getPreferencePages();
		for (final PreferencePage page : preferencePages) {
			VerticalPanel pageView = createPageView(page);
			Label title = new Label(page.getTitle());
			title.addStyleName("preferences_option_title");
			titlesPanel.add(title);
			titlesPanel.add(pageView);
			/**
			 * title.addClickHandler(new ClickHandler() {
			 * 
			 * @Override public void onClick(ClickEvent event) {
			 *           pageDetailsPanel.clear(); pageDetailsPanel.add(page); }
			 *           });
			 **/
			pageDetailsPanel.clear();
			pageDetailsPanel.add(preferencePages.get(0));
		}
		mainPanel.add(titlesPanel);
		mainPanel.add(pageDetailsPanel);
		titlesPanel.getElement().getParentElement()
				.addClassName("titles_panel_td");
		pageDetailsPanel.getElement().getParentElement()
				.addClassName("page_details_panel_td");
		mainPanel.addStyleName("company_settings_panel");
		this.add(mainPanel);
		setSize("100%", "100%");
	}

	private List<PreferencePage> getPreferencePages() {
		List<PreferencePage> preferenceList = new ArrayList<PreferencePage>();
		preferenceList.add(getCompanyInfoPage());
		if (hasPermission(Features.LOCATION) || hasPermission(Features.CLASS)
				|| hasPermission(Features.JOB_COSTING)) {
			preferenceList.add(getCatogiriesInfoPage());
		}
		preferenceList.add(getVendorAndPurchasesPage());
		preferenceList.add(getCustomerAndSalesPage());
		return preferenceList;
	}

	private PreferencePage getCustomerAndSalesPage() {
		PreferencePage customerAndSalesPage = new PreferencePage(
				messages.customersAndSales(Global.get().Customer()));
		AgeingAndSellingDetailsOption ageingAndSellingDetailsOption = new AgeingAndSellingDetailsOption();
		ProductAndServicesOption productAndServicesOption = new ProductAndServicesOption();
		TrackDiscountsOption discountsOption = new TrackDiscountsOption();
		TrackEstimatesOption estimatesOption = new TrackEstimatesOption();
		DoyouUseShipingsOption shipingsOption = new DoyouUseShipingsOption();

		customerAndSalesPage.addPreferenceOption(productAndServicesOption);
		customerAndSalesPage.addPreferenceOption(ageingAndSellingDetailsOption);
		customerAndSalesPage.addPreferenceOption(discountsOption);
		customerAndSalesPage.addPreferenceOption(estimatesOption);
		customerAndSalesPage.addPreferenceOption(shipingsOption);

		return customerAndSalesPage;
	}

	private PreferencePage getVendorAndPurchasesPage() {
		PreferencePage vendorsAndPurchasesPage = new PreferencePage(
				messages.vendorAndPurchases(Global.get().Vendor()));

		ManageBillsOption manageBillsOption = new ManageBillsOption();
		ExpensesByCustomerOption expensesByCustomerOption = new ExpensesByCustomerOption();

		vendorsAndPurchasesPage.addPreferenceOption(manageBillsOption);
		if (hasPermission(Features.BILLABLE_EXPENSE)) {
			vendorsAndPurchasesPage
					.addPreferenceOption(expensesByCustomerOption);
		}
		return vendorsAndPurchasesPage;
	}

	private PreferencePage getCatogiriesInfoPage() {
		PreferencePage catogiriesInfoPage = new PreferencePage(
				messages.categories());
		LocationTrackingOption locationTrackingOption = new LocationTrackingOption();
		ClassTrackingOption classTrackingPage = new ClassTrackingOption();
		JobTrackingOption jobTrackingOption = new JobTrackingOption();
		if (hasPermission(Features.LOCATION)) {
			catogiriesInfoPage.addPreferenceOption(locationTrackingOption);
		}
		if (hasPermission(Features.CLASS)) {
			catogiriesInfoPage.addPreferenceOption(classTrackingPage);
		}
		if (hasPermission(Features.JOB_COSTING)) {
			catogiriesInfoPage.addPreferenceOption(jobTrackingOption);
		}
		return catogiriesInfoPage;
	}

	public boolean hasPermission(String feature) {
		return Accounter.hasPermission(feature);
	}

	private PreferencePage getCompanyInfoPage() {
		PreferencePage companyInfoPage = new PreferencePage(messages.company());

		CompanyInfoOption companyInfoOption = new CompanyInfoOption();
		CompanyDateFormateOption formateOption = new CompanyDateFormateOption();
		CompanyEinOption einOption = new CompanyEinOption();
		// CompanyFiscalYearOption fiscalYearOption = new
		// CompanyFiscalYearOption();
		DoyouUseOption doyouUseOption = new DoyouUseOption();
		AccountNumberRangeOption accountNumberRangeOption = new AccountNumberRangeOption();
		CompanyCurrencyOption currencyOption = new CompanyCurrencyOption();
		CreditsOption creditsOption = new CreditsOption();
		CompanyTimeZoneOption timeZoneOption = new CompanyTimeZoneOption();
		TerminologyOption terminologyOption = new TerminologyOption();
		TrackOrChargeTaxOption taxOption = new TrackOrChargeTaxOption();
		CurrencyFormatOption currencyFormatOption = new CurrencyFormatOption();

		companyInfoPage.addPreferenceOption(companyInfoOption);
		companyInfoPage.addPreferenceOption(formateOption);
		companyInfoPage.addPreferenceOption(einOption);
		// companyInfoPage.addPreferenceOption(fiscalYearOption);
		companyInfoPage.addPreferenceOption(doyouUseOption);
		companyInfoPage.addPreferenceOption(accountNumberRangeOption);
		companyInfoPage.addPreferenceOption(currencyOption);
		companyInfoPage.addPreferenceOption(creditsOption);
		companyInfoPage.addPreferenceOption(timeZoneOption);
		companyInfoPage.addPreferenceOption(terminologyOption);
		companyInfoPage.addPreferenceOption(taxOption);
		companyInfoPage.addPreferenceOption(currencyFormatOption);

		return companyInfoPage;
	}

	private VerticalPanel createPageView(final PreferencePage page) {
		final VerticalPanel pageView = new VerticalPanel();
		pageView.setWidth("100%");
		List<AbstractPreferenceOption> options = page.getOptions();
		for (int index = 0; index < options.size(); index++) {
			final AbstractPreferenceOption option = options.get(index);
			final Anchor optionLink = new Anchor(option.getTitle());
			pageView.add(optionLink);
			pageView.addStyleName("options_panel");
			optionLink.getElement().getParentElement()
					.addClassName("preferences_option");

			optionLink.addMouseOverHandler(new MouseOverHandler() {

				@Override
				public void onMouseOver(MouseOverEvent event) {
					optionLink.getElement().getParentElement()
							.addClassName("optionFocused");
				}
			});
			optionLink.addMouseOutHandler(new MouseOutHandler() {

				@Override
				public void onMouseOut(MouseOutEvent event) {
					optionLink.getElement().getParentElement()
							.removeClassName("optionFocused");
				}
			});
			optionLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					for (int superIndex = 0; superIndex < ((ComplexPanel) pageView
							.getParent()).getWidgetCount(); superIndex++) {
						if (((ComplexPanel) pageView.getParent())
								.getWidget(superIndex) instanceof VerticalPanel) {
							Widget pageview = ((ComplexPanel) pageView
									.getParent()).getWidget(superIndex);
							for (int index = 0; index < ((ComplexPanel) pageview)
									.getWidgetCount(); index++) {
								Widget widget = ((ComplexPanel) pageview)
										.getWidget(index);
								widget.getElement().getParentElement()
										.removeClassName("optionClicked");
							}
						}
					}

					optionLink.getElement().getParentElement()
							.addClassName("optionClicked");
					if (!page.isAttached()) {
						pageDetailsPanel.clear();
						pageDetailsPanel.add(page);
					}
					pageDetailsPanel.ensureVisible(option);
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
		saveAndCloseButton.setText(messages.update());
		cancelButton.setText(messages.close());
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
					if (!page.canSave) {
						return;
					}
				}
				Accounter.updateCompany(PreferenceSettingsView.this,
						Accounter.getCompany());
			}
		});
	}

	@Override
	protected void changeButtonBarMode(boolean disable) {

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

	@Override
	public void saveSuccess(IAccounterCore object) {
		Accounter.reset();
		super.saveSuccess(object);
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		String errorString = AccounterExceptions.getErrorString(exception
				.getErrorCode());
		Accounter.showError(errorString);
	}

	@Override
	protected boolean canVoid() {
		return false;
	}

	@Override
	protected boolean canDelete() {
		return false;
	}
}
