package com.vimukti.accounter.web.client.uibinder.setup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.IAccounterCompanyInitializationServiceAsync;
import com.vimukti.accounter.web.client.core.AccountsTemplate;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.TemplateAccount;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CustomLabel;

public class SetupWizard extends VerticalPanel {
	private static final int START_PAGE = 0;
	private VerticalPanel viewPanel;
	private HorizontalPanel buttonPanel, backNextButtonPanel;
	private VerticalPanel progressPanel, viewButtonPanel;
	private Button backButton, nextButton;
	private Button gotoButton;
	private ClientCompanyPreferences preferences;
	private Label progressHeader;
	private AsyncCallback<Boolean> callback;
	public int currentViewIndex = START_PAGE;
	private List<TemplateAccount> selectedAccounts = new ArrayList<TemplateAccount>();
	private FlexTable progressTable;

	private AbstractSetupPage viewList[] = new AbstractSetupPage[] {
			new SetupStartPage(this), new SetupCompanyInfoPage(),
			new SetupOrganisationSelectionPage(),
			new SetupIndustrySelectionPage(), new SetupReferPage(),
			new SetupTrackEmployeesPage(), new SetupSellTypeAndSalesTaxPage(),
			new SetupUsingEstimatesAndStatementsPage(),
			new SetupCurrencyPage(), new SetupTrackBillsAndTimePage(),
			new SetupSelectFiscalYrDatePage(),
			new SetupSelectAccountsPage(this), new SetupComplitionPage() };

	private AbstractSetupPage[] skipViewList = new AbstractSetupPage[] {
			new SetupStartPage(this), new SetupCompanyInfoPage(),
			new SetupIndustrySelectionWithAccountsPage(this),
			new SetupComplitionPage() };

	private Image startProgressImages[] = new Image[viewList.length - 2];
	private String startProgressLabels[] = new String[] {
			Accounter.constants().setCompanyInfo(),
			Accounter.constants().selectIndustryType(),
			Accounter.constants().companyOrganization(),
			Accounter.constants().selectReferringNames(),
			Accounter.constants().trackEmployeeExpenses(),
			Accounter.constants().whatDoYouSell(),
			Accounter.constants().setEstimatesAndStatements(),
			Accounter.constants().setCurrency(),
			Accounter.constants().setBillTracking(),
			Accounter.constants().setFiscalYear(),
			Accounter.messages().selectRequiredAccounts(Global.get().account()) };

	private Image skipProgressImages[] = new Image[skipViewList.length - 2];
	private String skipProgressLabels[] = new String[] {
			Accounter.constants().setCompanyInfo(),
			Accounter.constants().selectIndustryType(),
			Accounter.constants().companyOrganization() };
	private int skipProgressImagesIndex;

	private AbstractSetupPage previousView;
	private AbstractSetupPage viewToShow;
	private int startProgressImagesIndex;
	private boolean isSkip;
	private Map<Integer, AccountsTemplate> accountsTemplates = new HashMap<Integer, AccountsTemplate>();

	public SetupWizard(AsyncCallback<Boolean> callback) {
		preferences = Accounter.getCompany().getPreferences();
		creteControls();
		this.addStyleName("setup_panel");
		this.callback = callback;
	}

	public void creteControls() {
		try {
			HorizontalPanel topPanel = new HorizontalPanel();
			viewPanel = new VerticalPanel();
			progressPanel = new VerticalPanel();
			viewButtonPanel = new VerticalPanel();
			backNextButtonPanel = new HorizontalPanel();
			progressHeader = new Label(Accounter.constants().setupProgress());

			progressPanel.add(progressHeader);
			progressHeader.addStyleName("progress_header");

			// add progress steps
			// setting images

			buttonPanel = new HorizontalPanel();
			buttonPanel.setVisible(false);

			viewButtonPanel.add(viewPanel);
			viewButtonPanel.add(buttonPanel);
			viewButtonPanel.setCellVerticalAlignment(buttonPanel,
					HasAlignment.ALIGN_MIDDLE);

			viewButtonPanel.setCellHeight(viewPanel, "93%");
			viewButtonPanel.setCellHeight(buttonPanel, "7%");

			topPanel.add(progressPanel);
			topPanel.add(viewButtonPanel);

			topPanel.setCellWidth(progressPanel, "28%");
			topPanel.setCellWidth(viewButtonPanel, "72%");

			viewPanel.addStyleName("view_panel");
			viewButtonPanel.setSize("100%", "100%");
			topPanel.setSize("100%", "100%");
			topPanel.setCellHorizontalAlignment(progressPanel,
					HasAlignment.ALIGN_RIGHT);

			this.add(topPanel);

			progressPanel.getElement().getParentElement()
					.setClassName("progress_panel_hide");
			// adding buttons to button panel
			// skipButton = new Button(Accounter.constants().skip());
			backButton = new Button(Accounter.constants().back());
			nextButton = new Button(Accounter.constants().next());
			gotoButton = new Button(Accounter.constants().gotoAccounter());

			// making them invisible at the beginning
			// skipButton.setVisible(false);
			backButton.setVisible(false);
			nextButton.setVisible(false);
			gotoButton.setVisible(false);

			// buttonPanel.add(skipButton);
			backNextButtonPanel.add(backButton);
			backNextButtonPanel.add(nextButton);
			backNextButtonPanel.add(gotoButton);
			buttonPanel.add(backNextButtonPanel);
			buttonPanel.setCellHorizontalAlignment(backNextButtonPanel,
					HasAlignment.ALIGN_RIGHT);
			buttonPanel.setWidth("100%");

			// adding handlers
			// skipButton.addClickHandler(new ClickHandler() {
			// @Override
			// public void onClick(ClickEvent arg0) {
			// gotoLastPage();
			// }
			// });
			gotoButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					Accounter.createCRUDService().updateCompanyPreferences(
							preferences, new AsyncCallback<Boolean>() {

								@Override
								public void onSuccess(Boolean result) {
									if (result) {
										IAccounterCompanyInitializationServiceAsync cIService = Accounter
												.createCompanyInitializationService();
										cIService.initalizeCompany(preferences,
												selectedAccounts, callback);
									}
								}

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub

								}
							});
				}
			});
			nextButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					if (getViewsList()[currentViewIndex].validate()) {
						if (currentViewIndex != viewList.length - 1) {
							currentViewIndex++;
						}
						showView();
					}
				}
			});

			backButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					if (getViewsList()[currentViewIndex].validate()) {
						if (currentViewIndex != START_PAGE) {
							currentViewIndex--;
							showView();
							if (currentViewIndex == 0) {
								removeProgressPanel();

							}
						} else {
							viewPanel.remove(viewToShow);
							removeProgressPanel();
						}

					}
				}
			});
			previousView = null;
			showStartPage();
		} catch (Exception e) {
			System.err.println(e);
		}

		loadIndustriesDefaultAccounts();
	}

	/**
	 * 
	 */
	private void loadIndustriesDefaultAccounts() {
		Accounter.createGETService().getAccountsTemplate(
				new AccounterAsyncCallback<List<AccountsTemplate>>() {

					@Override
					public void onException(AccounterException exception) {
						// TODO
					}

					@Override
					public void onResultSuccess(List<AccountsTemplate> result) {
						setIndustryDefaultAccounts(result);
					}

				});

	}

	protected void gotoLastPage() {
		currentViewIndex = viewList.length;
		showView();
	}

	private void showStartPage() {
		try {
			viewToShow = viewList[START_PAGE];
			this.viewPanel.add(viewToShow);
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	protected void showView() {

		previousView = viewToShow;
		if (previousView != null && previousView.validate()) {
			previousView.onSave();
			this.viewPanel.remove(previousView);
		}

		// while (!viewToShow.doShow()) {
		// currentViewIndex++;
		// viewToShow = viewList[currentViewIndex];
		// viewToShow.setPreferences(preferences);
		// }
		viewToShow = getNextView();

		if (viewToShow == null) {
			return;
		}

		// if (viewToShow.doShow()) {
		// viewToShow = viewList[currentViewIndex];
		// viewToShow.setPreferences(preferences);
		// }
		if (isLastView()) {
			backNextButtonPanel.addStyleName("back_GoToPanel");
		} else {
			backNextButtonPanel.addStyleName("back_NextPanel");
		}
		this.viewPanel.add(viewToShow);

		// checking button display related conditions
		if (isFirstView()) {
			buttonPanel.setVisible(true);
			if (!isLastView()) {
				// skipButton.setVisible(true);
				nextButton.setVisible(true);

				gotoButton.setVisible(false);
			} else {
				gotoButton.setVisible(true);
				// skipButton.setVisible(false);
				nextButton.setVisible(false);
			}
			backButton.setVisible(true);
		} else {
			buttonPanel.setVisible(false);
		}

		// setting the progress
		if (currentViewIndex > 1) {
			if (isSkip) {
				skipProgressImagesIndex = currentViewIndex - 2;
				skipProgressImages[skipProgressImagesIndex]
						.addStyleName("tick_show");
			} else {
				startProgressImagesIndex = currentViewIndex - 2;
				startProgressImages[startProgressImagesIndex]
						.addStyleName("tick_show");
			}

		}
	}

	public void getProgessPanel() {
		progressTable = new FlexTable();
		if (isSkip) {
			for (int iii = 0; iii < skipProgressImages.length; iii++) {
				CustomLabel label = new CustomLabel(skipProgressLabels[iii]);
				skipProgressImages[iii] = new Image(Accounter
						.getFinanceImages().tickMark());
				skipProgressImages[iii].addStyleName("tick_hidden");
				progressTable.setWidget(iii, 0, skipProgressImages[iii]);
				progressTable.setWidget(iii, 1, label);
			}
		} else {
			for (int iii = 0; iii < startProgressImages.length; iii++) {
				CustomLabel label = new CustomLabel(startProgressLabels[iii]);
				startProgressImages[iii] = new Image(Accounter
						.getFinanceImages().tickMark());
				startProgressImages[iii].addStyleName("tick_hidden");
				progressTable.setWidget(iii, 0, startProgressImages[iii]);
				progressTable.setWidget(iii, 1, label);
			}

		}

		progressPanel.add(progressTable);
		progressTable.addStyleName("progress_panel_data");
		progressPanel.getElement().getParentElement()
				.setClassName("progress_panel_show");
	}

	/**
	 * @return
	 */
	private boolean isFirstView() {
		return currentViewIndex != START_PAGE;
	}

	private void removeProgressPanel() {
		progressPanel.remove(progressTable);
		progressPanel.getElement().getParentElement()
				.setClassName("progress_panel_hide");
	}

	/**
	 * @return
	 */
	private boolean isLastView() {
		return currentViewIndex == getViewsList().length - 1;
	}

	private AbstractSetupPage getNextView() {
		AbstractSetupPage nextView = getViewsList()[currentViewIndex];
		nextView.setPreferences(preferences);
		while (!nextView.canShow()) {
			currentViewIndex++;
			if (currentViewIndex > getViewsList().length - 1) {
				return null;
			}
			nextView = getViewsList()[currentViewIndex];
		}
		return nextView;
	}

	/**
	 * @param b
	 */
	public void setSkip(boolean value) {
		this.isSkip = value;
	}

	private AbstractSetupPage[] getViewsList() {
		if (isSkip) {
			return skipViewList;
		} else {
			return viewList;
		}
	}

	/**
	 * @return
	 */
	public List<TemplateAccount> getIndustryDefaultAccounts() {
		int industryType = preferences.getIndustryType();
		AccountsTemplate accountsTemplate = this.accountsTemplates
				.get(industryType);
		List<TemplateAccount> accounts = accountsTemplate.getAccounts();
		if (accounts != null) {
			return accounts;
		}
		return Collections.emptyList();
	}

	public Map<Integer, AccountsTemplate> getAllIndustiesWithAccounts() {
		return accountsTemplates;
	}

	/**
	 * @return
	 */
	private void setIndustryDefaultAccounts(
			List<AccountsTemplate> accountesTemplates) {
		for (AccountsTemplate accountTemplate : accountesTemplates) {
			this.accountsTemplates.put(accountTemplate.getType(),
					accountTemplate);
		}
	}

	/**
	 * @param account
	 * @param value
	 */
	public void setSelectedAccountsList(List<TemplateAccount> selectedAccounts) {
		this.selectedAccounts = selectedAccounts;
	}

	/**
	 * @param account
	 * @param value
	 */
	public List<TemplateAccount> getSelectedAccountsList() {
		return this.selectedAccounts;
	}

}
