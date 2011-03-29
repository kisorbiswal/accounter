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
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.FinanceApplication;

@SuppressWarnings("unchecked")
public class GeneralSettingsView extends AbstractBaseView {
	private VerticalPanel mainPanel, conversationPanel, invoiceBrandingPanel,
			userPanel;
	private FlexTable optionsTable;
	private HorizontalPanel titlePanel;
	private HTML conversionHTML, conversationCommentHTML, invoiceBrandingHTML,
			invoiceCommentHtml, titleHtml, userHtml, userCommentHtml;
	private Image conversationImage, usersImage, invoiceImage;

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
		// TODO Auto-generated method stub
		super.initData();
	}

	private void createControls() {
		mainPanel = new VerticalPanel();
		titlePanel = new HorizontalPanel();
		optionsTable = new FlexTable();
		conversationPanel = new VerticalPanel();
		invoiceBrandingPanel = new VerticalPanel();
		userPanel = new VerticalPanel();

		titleHtml = new HTML(FinanceApplication.getSettingsMessages()
				.generalSettingsHeading());

		conversionHTML = new HTML(FinanceApplication.getSettingsMessages()
				.conversionHTML());
		conversationCommentHTML = new HTML(FinanceApplication
				.getSettingsMessages().conversionCommet());
		invoiceBrandingHTML = new HTML(FinanceApplication.getSettingsMessages()
				.invoiceBrandingHTML());
		invoiceCommentHtml = new HTML(FinanceApplication.getSettingsMessages()
				.invoiceComment());

		userHtml = new HTML(FinanceApplication.getSettingsMessages().userHTML());
		userCommentHtml = new HTML(FinanceApplication.getSettingsMessages()
				.usersComment());
		titlePanel.add(titleHtml);
		conversationPanel.add(conversionHTML);
		conversationPanel.add(conversationCommentHTML);
		conversationImage = new Image("images/conversion-Balances.png");
		conversationImage.setStyleName("general-image");
		conversionHTML.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getConversionBalancesAction().run(null,
						false);
			}
		});
		conversionHTML.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				conversionHTML.getElement().getStyle()
						.setCursor(Cursor.POINTER);
				conversionHTML.getElement().getStyle().setTextDecoration(
						TextDecoration.UNDERLINE);
			}
		});
		conversionHTML.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				conversionHTML.getElement().getStyle().setTextDecoration(
						TextDecoration.NONE);

			}
		});

		invoiceBrandingPanel.add(invoiceBrandingHTML);
		invoiceBrandingPanel.add(invoiceCommentHtml);
		invoiceImage = new Image("images/invoice-general.png");
		invoiceBrandingHTML.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getInvoiceBrandingAction().run(null,
						false);
			}
		});

		invoiceBrandingHTML.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				invoiceBrandingHTML.getElement().getStyle().setCursor(
						Cursor.POINTER);
				invoiceBrandingHTML.getElement().getStyle().setTextDecoration(
						TextDecoration.UNDERLINE);
			}
		});
		invoiceBrandingHTML.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				invoiceBrandingHTML.getElement().getStyle().setTextDecoration(
						TextDecoration.NONE);

			}
		});

		userPanel.add(userHtml);
		userPanel.add(userCommentHtml);
		usersImage = new Image("images/users-general.png");
		userHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getUsersAction().run(null, false);
			}
		});
		userHtml.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				userHtml.getElement().getStyle().setCursor(Cursor.POINTER);
				userHtml.getElement().getStyle().setTextDecoration(
						TextDecoration.UNDERLINE);
			}
		});
		userHtml.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				userHtml.getElement().getStyle().setTextDecoration(
						TextDecoration.NONE);

			}
		});

		titlePanel.add(titleHtml);
		optionsTable.setWidget(0, 0, conversationImage);
		optionsTable.setWidget(0, 1, conversationPanel);
		optionsTable.setWidget(1, 0, invoiceImage);
		optionsTable.setWidget(1, 1, invoiceBrandingPanel);
		optionsTable.setWidget(2, 0, usersImage);
		optionsTable.setWidget(2, 1, userPanel);

		mainPanel.add(titlePanel);
		mainPanel.add(optionsTable);

		add(mainPanel);
	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

}
