package com.vimukti.accounter.web.client.ui.win8;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.CompanyDetails;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.WebsocketAccounterInitialiser;

/**
 * 
 * @author Devaraju.K
 * 
 * @param <T>
 */
public class CompaniesPanel extends FlowPanel {

	Button createNewCompanyButton;

	Button deleteComapny;

	Button logOut;

	// Button deleteAccount;

	StyledPanel companiesListStyledPanel;

	List<CompanyDetails> comapniesList = new ArrayList<CompanyDetails>();

	WebsocketAccounterInitialiser accounterInitialiser;

	String selected_companyName = "";

	HTML title;

	public CompaniesPanel(List<CompanyDetails> companiesList,
			WebsocketAccounterInitialiser accounterInitialiser) {
		getElement().setId("companiesPanel");
		this.comapniesList = companiesList;
		this.accounterInitialiser = accounterInitialiser;
		createControls();
	}

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void createControls() {

		title = new HTML("<h2>" + Accounter.getMessages().companieslist()
				+ "</h2>");
		createNewCompanyButton = new Button(Accounter.getMessages()
				.createNewCompany());
		createNewCompanyButton.setStyleName("success");
		createNewCompanyButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loadCompany(0);
			}
		});

		companiesListStyledPanel = new StyledPanel("companies-list");

		for (final CompanyDetails com : comapniesList) {
			StyledPanel imgPanel = new StyledPanel("imgPanel");
			Image image = new Image("/images/dfsgsdfg.png");
			imgPanel.add(image);
			Anchor anchor = new Anchor(com.getCompanyName());
			anchor.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					selected_companyName = com.getCompanyName();

					loadCompany(com.getCompanyId());
				}

			});
			StyledPanel anchorPanel = new StyledPanel("companyNameAnchor");
			anchorPanel.add(anchor);
			deleteComapny = new Button(Accounter.getMessages().deletecompany());
			deleteComapny.setStyleName("warning");
			deleteComapny.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					accounterInitialiser.showView(new DeleteCompanyPanel(com,
							accounterInitialiser));
				}
			});
			anchorPanel.add(deleteComapny);
			companiesListStyledPanel.add(imgPanel);
			companiesListStyledPanel.add(anchorPanel);
		}
		logOut = new Button(Accounter.getMessages().logout());
		logOut.setStyleName("cancel");
		final AccounterAsyncCallback<Boolean> logoutCallback = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException exception) {

			}

			@Override
			public void onResultSuccess(Boolean result) {
				CompaniesPanel.this.removeFromParent();
				accounterInitialiser.showView(new LoginPanel(
						accounterInitialiser));

			}

		};
		logOut.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Accounter.createWindowsRPCService().logout(logoutCallback);
			}
		});

		// deleteAccount = new Button(Accounter.getMessages().deleteAccount());
		// deleteAccount.setStyleName("warning");
		// deleteAccount.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		//
		// }
		// });
		StyledPanel mainPanel = new StyledPanel("main-panel");
		mainPanel.add(title);
		mainPanel.add(companiesListStyledPanel);

		StyledPanel buttonsPanel = new StyledPanel("app-bar");
		buttonsPanel.add(logOut);
		buttonsPanel.add(createNewCompanyButton);
		// buttonsPanel.add(deleteAccount);

		mainPanel.add(buttonsPanel);
		add(mainPanel);
	}

	private void loadCompany(final long comId) {
		CompaniesPanel.this.removeFromParent();
		accounterInitialiser.loadCompany(comId);
	}

}
