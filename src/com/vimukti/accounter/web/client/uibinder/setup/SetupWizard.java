package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.IAccounterCompanyInitializationServiceAsync;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
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

	private AbstractSetupPage viewList[] = new AbstractSetupPage[] {
			new SetupStartPage(this), new SetupCompanyInfoPage(),
			new SetupOrganisationSelectionPage(),
			new SetupIndustrySelectionPage(), new SetupReferPage(),
			new SetupTrackEmployeesPage(), new SetupSellTypeAndSalesTaxPage(),
			new SetupUsingEstimatesAndStatementsPage(),
			new SetupCurrencyPage(), new SetupTrackBillsAndTimePage(),
			new SetupSelectFiscalYrDatePage(), new SetupSelectAccountsPage(),
			new SetupComplitionPage() };

	private AbstractSetupPage[] skipViewList = new AbstractSetupPage[] {
			new SetupStartPage(this), new SetupCompanyInfoPage(),
			new SetupOrganisationSelectionPage(),
			new SetupIndustrySelectionPage(), new SetupComplitionPage() };

	private Image progressImages[] = new Image[viewList.length - 2];
	private String progressLabels[] = new String[] {
			Accounter.constants().setCompanyInfo(),
			Accounter.constants().companyOrganization(),
			Accounter.constants().selectIndustryType(),
			Accounter.constants().selectReferringNames(),
			Accounter.constants().trackEmployeeExpenses(),
			Accounter.constants().whatDoYouSell(),
			Accounter.constants().setEstimatesAndStatements(),
			Accounter.constants().setCurrency(),
			Accounter.constants().setBillTracking(),
			Accounter.constants().setFiscalYear(),
			Accounter.constants().selectRequiredAccounts() };
	private AbstractSetupPage previousView;
	private AbstractSetupPage viewToShow;
	private int progressImagesIndex;
	private boolean isSkip;

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
			for (int iii = 0; iii < progressImages.length; iii++) {
				HorizontalPanel progressPanel1 = new HorizontalPanel();
				CustomLabel label = new CustomLabel(progressLabels[iii]);
				progressImages[iii] = new Image(Accounter.getFinanceImages()
						.tickMark());
				progressImages[iii].addStyleName("tick_hidden");
				progressPanel1.add(progressImages[iii]);
				progressPanel1.add(label);

				progressPanel.add(progressPanel1);
			}
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

			topPanel.setCellWidth(progressPanel, "25%");
			topPanel.setCellWidth(viewButtonPanel, "75%");
			viewPanel.addStyleName("view_panel");
			viewButtonPanel.setSize("100%", "100%");
			topPanel.setSize("100%", "100%");
			progressPanel.getElement().getParentElement().addClassName(
					"progress_panel");
			topPanel.setCellHorizontalAlignment(progressPanel,
					HasAlignment.ALIGN_RIGHT);

			this.add(topPanel);

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
												callback);
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
					if (currentViewIndex != viewList.length - 1) {
						// if (viewList[currentViewIndex].validate()) {
						currentViewIndex++;
						// }
					}
					showView();
				}
			});

			backButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent arg0) {
					if (currentViewIndex != START_PAGE) {
						currentViewIndex--;
						showView();
					} else {
						viewPanel.remove(viewToShow);
						showStartPage();
					}

				}
			});
			previousView = null;
			showStartPage();
		} catch (Exception e) {
			System.err.println(e);
		}
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
		if (previousView != null) {
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
			progressImagesIndex = currentViewIndex - 2;
			progressImages[progressImagesIndex].addStyleName("tick_show");
		}
	}

	/**
	 * @return
	 */
	private boolean isFirstView() {
		return currentViewIndex != START_PAGE;
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
}
