package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.company.PreferencesAction;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class GeneralSettingsView extends AbstractBaseView {
	private VerticalPanel mainPanel, conversationPanel, invoiceBrandingPanel,
			userPanel, companyPanel;
	private FlexTable optionsTable;
	private HorizontalPanel titlePanel;
	private HTML conversionHTML, conversationCommentHTML, invoiceBrandingHTML,
			invoiceCommentHtml, titleHtml, userHtml, userCommentHtml,
			companySettingsHtml, companyCommentHtml;
	private Image conversationImage, usersImage, invoiceImage, companyImage;
	private AccounterMessages messages = Accounter.messages();

	@Override
	public void fitToSize(int height, int width) {

	}

	@Override
	public void onEdit() {

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	@Override
	public void initData() {
		super.initData();
	}

	private void createControls() {
		mainPanel = new VerticalPanel();
		titlePanel = new HorizontalPanel();
		optionsTable = new FlexTable();
		conversationPanel = new VerticalPanel();
		invoiceBrandingPanel = new VerticalPanel();
		userPanel = new VerticalPanel();

		titleHtml = new HTML(messages.generalSettingsHeading());
		titleHtml.setVisible(false);
		conversionHTML = new HTML(messages.conversionHTML());
		conversationCommentHTML = new HTML(messages.conversionCommet());
		invoiceBrandingHTML = new HTML(messages.invoiceBrandingHTML());
		// invoiceBrandingHTML.setWidth("145px");
		invoiceBrandingHTML.setStyleName("invoice-branding-html");
		invoiceCommentHtml = new HTML(messages.invoiceComment());
		userHtml = new HTML(messages.userHTML());
		userHtml.setWidth("50px");
		userHtml.setStyleName("user-html");
		userCommentHtml = new HTML(messages.usersComment());
		companySettingsHtml = new HTML(messages.companySettingsTitle());
		companySettingsHtml.setStyleName("company-settings-html");
		// companySettingsHtml.setWidth("161px");
		companyCommentHtml = new HTML(messages.companyCommentHtml());

		titlePanel.add(titleHtml);
		conversationPanel.add(conversionHTML);
		conversationPanel.add(conversationCommentHTML);
		conversationPanel.setVisible(false);
		conversationImage = new Image("images/conversion-Balances.png");
		conversationImage.setStyleName("general-image");
		conversationImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getConversionBalancesAction().run(null, false);
			}
		});
		conversationImage.setVisible(false);
		conversionHTML.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getConversionBalancesAction().run(null, false);
			}
		});
		conversionHTML.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				conversionHTML.getElement().getStyle()
						.setCursor(Cursor.POINTER);
				conversionHTML.getElement().getStyle()
						.setTextDecoration(TextDecoration.UNDERLINE);
			}
		});
		conversionHTML.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				conversionHTML.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);

			}
		});

		invoiceBrandingPanel.add(invoiceBrandingHTML);
		invoiceBrandingPanel.add(invoiceCommentHtml);
		invoiceImage = new Image("images/invoice-general.png");
		invoiceImage.setStyleName("general-image");
		invoiceImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getInvoiceBrandingAction().run(null, false);
			}
		});
		invoiceBrandingHTML.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getInvoiceBrandingAction().run(null, false);
				invoiceBrandingHTML.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);

			}
		});

		invoiceBrandingHTML.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				invoiceBrandingHTML.getElement().getStyle()
						.setCursor(Cursor.POINTER);
				invoiceBrandingHTML.getElement().getStyle()
						.setTextDecoration(TextDecoration.UNDERLINE);
			}
		});
		invoiceBrandingHTML.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				invoiceBrandingHTML.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);

			}
		});

		userPanel.add(userHtml);
		userPanel.add(userCommentHtml);
		usersImage = new Image("images/users-general.png");
		usersImage.setStyleName("general-image");
		usersImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getUsersAction().run(null, false);
			}
		});
		userHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getUsersAction().run(null, false);
				userHtml.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);
			}
		});
		userHtml.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				userHtml.getElement().getStyle().setCursor(Cursor.POINTER);
				userHtml.getElement().getStyle()
						.setTextDecoration(TextDecoration.UNDERLINE);
			}
		});
		userHtml.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				userHtml.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);

			}
		});

		companyPanel = new VerticalPanel();
		companyImage = new Image("images/companySettings.png");
		companyImage.setStyleName("general-image");
		companyImage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PreferencesAction.CATEGORY = PreferencesAction.SETTINGS;
				ActionFactory.getPreferencesAction().run(null, false);

			}
		});
		companySettingsHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PreferencesAction.CATEGORY = PreferencesAction.SETTINGS;
				ActionFactory.getPreferencesAction().run(null, false);
				companySettingsHtml.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);

			}
		});
		companySettingsHtml.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				companySettingsHtml.getElement().getStyle()
						.setCursor(Cursor.POINTER);
				companySettingsHtml.getElement().getStyle()
						.setTextDecoration(TextDecoration.UNDERLINE);
			}
		});
		companySettingsHtml.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				companySettingsHtml.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);

			}
		});
		companyPanel.add(companySettingsHtml);
		companyPanel.add(companyCommentHtml);

		optionsTable.setStyleName("general-main-class");
		titlePanel.add(titleHtml);
		optionsTable.setWidget(0, 0, conversationImage);
		optionsTable.setWidget(0, 1, conversationPanel);
		optionsTable.setWidget(1, 0, companyImage);
		optionsTable.setWidget(1, 1, companyPanel);
		optionsTable.setWidget(2, 0, usersImage);
		optionsTable.setWidget(2, 1, userPanel);
		optionsTable.setWidget(3, 0, invoiceImage);
		optionsTable.setWidget(3, 1, invoiceBrandingPanel);

		mainPanel.add(titlePanel);
		mainPanel.add(optionsTable);

		add(mainPanel);
		mainPanel.setStyleName("setting-class-panel");
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result){

	}


	@Override
	protected String getViewTitle() {
		return Accounter.constants().generalSettings();
	}

}
