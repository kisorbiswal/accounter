package com.vimukti.accounter.web.client.ui.setup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.ui.Accounter;

public class SetupWizard extends VerticalPanel {
	private VerticalPanel viewPanel;
	private HorizontalPanel buttonPanel;
	private VerticalPanel progressPanel;
	private Button skipButton, backButton, nextButton;
	private ClientCompanyPreferences preferences;

	private int currentViewIndex = 0;

	private AbstractSetupPage viewList[] = new AbstractSetupPage[] {
			new SetupStartPage(), new SetupCompanyInfoPage(),
			new SetupIndustrySelectionPage(),
			new SetupOrganisationSelectionPage(), new SetupReferPage(),
			new SetupTrackEmployeesPage(), new SetupSellTypeAndSalesTaxPage(),
			new SetupUsingEstimatesAndStatementsPage(),
			new SetupTrackBillsAndTimePage(),
			new SetupSelectFiscalYrDatePage(), new SetupSelectAccountsPage(),
			new SetupComplitionPage() };
	private AbstractSetupPage previousView;
	private AbstractSetupPage viewToShow;

	public SetupWizard() {
		preferences = Accounter.getCompany().getPreferences();
		creteControls();
	}

	public void creteControls() {
		HorizontalPanel topPanel = new HorizontalPanel();
		viewPanel = new VerticalPanel();
		progressPanel = new VerticalPanel();

		buttonPanel = new HorizontalPanel();

		topPanel.add(viewPanel);
		topPanel.add(progressPanel);

		this.add(topPanel);
		this.add(buttonPanel);

		// adding buttons to button panel
		skipButton = new Button(Accounter.constants().skip());
		backButton = new Button(Accounter.constants().back());
		nextButton = new Button(Accounter.constants().next());

		buttonPanel.add(skipButton);
		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);

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
		showView();
	}

	protected void showView() {
		previousView = viewToShow;
		if(previousView != null)
			this.viewPanel.remove(previousView);
		viewToShow = viewList[currentViewIndex];
		viewToShow.setPreferences(preferences);
		while (!viewToShow.doShow()) {
			currentViewIndex++;
			viewToShow = viewList[currentViewIndex];
			viewToShow.setPreferences(preferences);
		}
		this.viewPanel.add(viewToShow);
	}
}
