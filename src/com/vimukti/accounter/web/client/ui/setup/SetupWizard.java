package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.CustomLabel;

public class SetupWizard extends VerticalPanel {
	private static final int START_PAGE = 0;
	private VerticalPanel viewPanel;
	private HorizontalPanel buttonPanel;
	private VerticalPanel progressPanel;
	private Button skipButton, backButton, nextButton;
	private Button gotoButton;
	private ClientCompanyPreferences preferences;

	private int currentViewIndex = START_PAGE;

	private AbstractSetupPage viewList[] = new AbstractSetupPage[] {
			new SetupStartPage(this), new SetupCompanyInfoPage(),
			new SetupIndustrySelectionPage(),
			new SetupOrganisationSelectionPage(), new SetupReferPage(),
			new SetupTrackEmployeesPage(), new SetupSellTypeAndSalesTaxPage(),
			new SetupUsingEstimatesAndStatementsPage(),
			new SetupTrackBillsAndTimePage(),
			new SetupSelectFiscalYrDatePage(), new SetupSelectAccountsPage(),
			new SetupComplitionPage() };

	private Image progressImages[] = new Image[viewList.length - 2];
	private String progressLabels[] = new String[] {
			Accounter.constants().setCompanyInfo(),
			Accounter.constants().selectIndustryType(),
			Accounter.constants().companyOrganization(),
			Accounter.constants().selectReferringNames(),
			Accounter.constants().trackEmployeeExpenses(),
			Accounter.constants().whatDoYouSell(),
			Accounter.constants().setEstimatesAndStatements(),
			Accounter.constants().setBillTracking(),
			Accounter.constants().setFiscalYear(),
			Accounter.constants().selectRequiredAccounts() };
	private AbstractSetupPage previousView;
	private AbstractSetupPage viewToShow;
	private int progressImagesIndex;

	public SetupWizard() {
		preferences = Accounter.getCompany().getPreferences();
		creteControls();
	}

	public void creteControls() {
		HorizontalPanel topPanel = new HorizontalPanel();
		viewPanel = new VerticalPanel();
		progressPanel = new VerticalPanel();

		// add progress steps
		// setting images
		for (int iii = 0; iii < progressImages.length; iii++) {
			HorizontalPanel progressPanel1 = new HorizontalPanel();
			CustomLabel label = new CustomLabel(progressLabels[iii]);
			progressImages[iii] = new Image(Accounter.getFinanceImages()
					.tickMark());
			progressImages[iii].setVisible(false);
			progressPanel1.add(progressImages[iii]);
			progressPanel1.add(label);

			progressPanel.add(progressPanel1);
		}
		buttonPanel = new HorizontalPanel();
		buttonPanel.setVisible(false);

		topPanel.add(viewPanel);
		topPanel.add(progressPanel);

		this.add(topPanel);
		this.add(buttonPanel);

		// adding buttons to button panel
		skipButton = new Button(Accounter.constants().skip());
		backButton = new Button(Accounter.constants().back());
		nextButton = new Button(Accounter.constants().next());
		gotoButton = new Button(Accounter.constants().gotoAccounter());

		// making them invisible at the beginning
		skipButton.setVisible(false);
		backButton.setVisible(false);
		nextButton.setVisible(false);
		gotoButton.setVisible(false);

		buttonPanel.add(skipButton);
		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(gotoButton);

		// adding handlers
		skipButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				currentViewIndex = viewList.length;
				showView();
			}
		});

		nextButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				currentViewIndex++;
				showView();
			}
		});

		backButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				currentViewIndex--;
				showView();
			}
		});
		previousView = null;
		showStartPage();
	}

	private void showStartPage() {
		viewToShow = viewList[START_PAGE];
		this.viewPanel.add(viewToShow);
	}

	protected void showView() {
		currentViewIndex++;
		previousView = viewToShow;
		if (previousView != null){
			previousView.onSave();
			this.viewPanel.remove(previousView);
		}
		viewToShow = viewList[currentViewIndex];
		viewToShow.setPreferences(preferences);
		while (!viewToShow.doShow()) {
			currentViewIndex++;
			viewToShow = viewList[currentViewIndex];
			viewToShow.setPreferences(preferences);
		}
		this.viewPanel.add(viewToShow);

		// checking button display related conditions
		if (currentViewIndex != START_PAGE) {
			buttonPanel.setVisible(true);
			if (currentViewIndex != viewList.length - 1) {
				skipButton.setVisible(true);
				nextButton.setVisible(true);

				gotoButton.setVisible(false);
			} else {
				gotoButton.setVisible(true);
				skipButton.setVisible(false);
				nextButton.setVisible(false);
			}
			backButton.setVisible(true);
		}

		// setting the progress
		if (currentViewIndex > 1) {
			progressImagesIndex = currentViewIndex - 2;
			progressImages[progressImagesIndex].setVisible(true);
		}
	}
}
