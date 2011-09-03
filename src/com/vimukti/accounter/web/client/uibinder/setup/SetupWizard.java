package com.vimukti.accounter.web.client.uibinder.setup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccountsTemplate;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.TemplateAccount;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.Accounter.AccounterType;
import com.vimukti.accounter.web.client.ui.CustomLabel;
import com.vimukti.accounter.web.client.ui.core.AccounterDialog;
import com.vimukti.accounter.web.client.ui.core.ErrorDialogHandler;

public class SetupWizard extends VerticalPanel {
	private static final int START_PAGE = 0;
	private VerticalPanel viewPanel;
	private HorizontalPanel buttonPanel, backNextButtonPanel;
	private VerticalPanel progressPanel, viewButtonPanel;
	private Button backButton, nextButton, cancelBtn;
	private Button gotoButton;
	private ClientCompanyPreferences preferences;
	private Label progressHeader;
	private AsyncCallback<Boolean> callback;
	private int currentViewIndex = START_PAGE;
	private List<TemplateAccount> selectedAccounts = new ArrayList<TemplateAccount>();
	private FlexTable progressTable;

	private AbstractSetupPage viewList[] = new AbstractSetupPage[] {
			new SetupStartPage(this),
			new SetupCompanyInfoPage(),
			new SetupIndustrySelectionPage(this),
			new SetupOrganisationSelectionPage(),
			new SetupReferPage(),
			// Employee Expanse Will be Added in Next Version
			/* new SetupTrackEmployeesPage(), */new SetupSellTypeAndSalesTaxPage(),

			new SetupUsingEstimatesAndStatementsPage(),
			new SetupCurrencyPage(), new SetupTrackBillsAndTimePage(),
			new SetupSelectFiscalYrDatePage(),
			new SetupSelectAccountsPage(this), new SetupComplitionPage() };

	private AbstractSetupPage[] skipViewList = new AbstractSetupPage[] {
			new SetupStartPage(this), new SetupCompanyInfoPage(),
			new SetupOrganisationSelectionPage(),
			new SetupSelectFiscalYrDatePage(),
			new SetupIndustrySelectionWithAccountsPage(this),
			new SetupComplitionPage() };

	private Image startProgressImages[] = new Image[viewList.length - 2];
	private String startProgressLabels[] = new String[] {
			Accounter.constants().setCompanyInfo(),
			Accounter.constants().selectIndustryType(),
			Accounter.constants().companyOrganization(),
			Accounter.constants().selectReferringNames(),
			/* Accounter.constants().trackEmployeeExpenses(), */
			Accounter.constants().whatDoYouSell(),
			Accounter.constants().setEstimatesAndStatements(),
			Accounter.constants().setCurrency(),
			Accounter.constants().setBillTracking(),
			Accounter.constants().setFiscalYear(),
			Accounter.constants().Accounts() };

	private Image skipProgressImages[] = new Image[skipViewList.length - 2];
	private String skipProgressLabels[] = new String[] {
			Accounter.constants().setCompanyInfo(),
			Accounter.constants().companyOrganization(),
			Accounter.constants().setFiscalYear(),
			Accounter.constants().selectIndustryType() };

	private AbstractSetupPage previousView;
	private AbstractSetupPage viewToShow;
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
			buttonPanel.setStyleName("back_next_buttons");

			viewButtonPanel.setCellHeight(viewPanel, "90%");
			viewButtonPanel.setCellHeight(buttonPanel, "10%");

			topPanel.add(progressPanel);
			topPanel.add(viewButtonPanel);

			// topPanel.setCellWidth(progressPanel, "25%");
			// topPanel.setCellWidth(viewButtonPanel, "75%");

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
			cancelBtn = new Button(Accounter.constants().cancel());

			// making them invisible at the beginning
			// skipButton.setVisible(false);
			backButton.setVisible(false);
			nextButton.setVisible(false);
			gotoButton.setVisible(false);
			// cancelBtn.setVisible(false);

			// buttonPanel.add(skipButton);
			backNextButtonPanel.add(backButton);
			backNextButtonPanel.add(nextButton);
			backNextButtonPanel.add(gotoButton);
			buttonPanel.add(cancelBtn);
			buttonPanel.setCellHorizontalAlignment(cancelBtn,
					HasAlignment.ALIGN_LEFT);
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

					Accounter.createCompanyInitializationService()
							.initalizeCompany(preferences, selectedAccounts,
									callback);

				}
			});
			nextButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					showView(true);
				}
			});

			backButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					if (currentViewIndex != START_PAGE) {
						showView(false);
						if (currentViewIndex == 0) {
							removeProgressPanel();
						}
					} else {
						viewPanel.remove(viewToShow);
						removeProgressPanel();
					}
				}
			});

			cancelBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					cancel();
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

	private void showStartPage() {
		try {
			viewToShow = viewList[START_PAGE];
			this.viewPanel.add(viewToShow);
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	protected void showView(boolean isNext) {
		previousView = viewToShow;
		if (previousView == null) {
			return;
		}
		if (isNext) {
			if (!previousView.validate()) {
				return;
			}
			previousView.onSave();
			if (currentViewIndex > 0) {
				getProgressImages()[currentViewIndex - 1]
						.addStyleName("tick_show");
			}
			if (!isLastView()) {
				currentViewIndex++;
			}

		} else {
			if (currentViewIndex > 1) {
				getProgressImages()[currentViewIndex - 2]
						.removeStyleName("tick_show");
			}
			currentViewIndex--;
		}

		this.viewPanel.remove(previousView);
		viewToShow = getNextView();

		if (Accounter.getCompany().getAccountingType() != ClientCompany.ACCOUNTING_TYPE_US
				&& isOrganizationView()) {
			showView(isNext);
		}

		if (viewToShow == null) {
			return;
		}

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

	}

	/**
	 * @return
	 */
	private boolean isOrganizationView() {
		return viewToShow instanceof SetupOrganisationSelectionPage;
	}

	public void initProgessPanel() {
		progressTable = new FlexTable();
		Image[] progressImages = getProgressImages();
		String[] progressLabels = getProgressLabels();
		for (int iii = 0; iii < progressImages.length; iii++) {
			CustomLabel label = new CustomLabel(progressLabels[iii]);
			progressImages[iii] = new Image(Accounter.getFinanceImages()
					.tickMark());
			progressImages[iii].addStyleName("tick_hidden");
			progressTable.setWidget(iii, 0, progressImages[iii]);
			progressTable.setWidget(iii, 1, label);
		}

		progressPanel.add(progressTable);
		progressTable.addStyleName("progress_panel_data");
		progressPanel.getElement().getParentElement()
				.setClassName("progress_panel_show");
	}

	private String[] getProgressLabels() {
		if (isSkip) {
			return skipProgressLabels;
		}
		return startProgressLabels;
	}

	private Image[] getProgressImages() {
		if (isSkip) {
			return skipProgressImages;
		}
		return startProgressImages;
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

	/**
	 * Cancels the Setup
	 */
	public void cancel() {
		new AccounterDialog(Accounter.messages().setupCancelMessgae(),
				AccounterType.WARNING, new ErrorDialogHandler() {

					@Override
					public boolean onYesClick() {
						try {
							redirectToCompaniesPage();
						} catch (Exception e) {
							return false;
						}
						return true;
					}

					@Override
					public boolean onNoClick() {
						return true;
					}

					@Override
					public boolean onCancelClick() {
						return true;
					}
				});
	}

	/**
	 * @throws RequestException
	 * 
	 */
	protected void redirectToCompaniesPage() throws RequestException {
		Window.Location.assign("/main/login");
	}

	public void initInterview(boolean isSkip) {
		this.isSkip = isSkip;
		initProgessPanel();
		showView(true);
	}

}
