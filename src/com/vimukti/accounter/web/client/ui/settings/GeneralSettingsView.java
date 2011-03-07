package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;

@SuppressWarnings("unchecked")
public class GeneralSettingsView extends AbstractBaseView {
	private VerticalPanel mainPanel, conversationPanel, invoiceBrandingPanel,
			userPanel;
	private FlexTable optionsTable;
	private HorizontalPanel titlePanel;
	private HTML conversionHTML, conversationCommentHTML, invoiceBrandingHTML,
			invoiceCommentHtml, titleHtml, userHtml, userCommentHtml;

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

		titleHtml = new HTML(
				"<p><font size='5px', color='green'>General Settings</font></p>");

		conversionHTML = new HTML(
				"<a><font size='3px', color='green'>Conversion Balances</font></a>");
		conversationCommentHTML = new HTML(
				"<p><font size='2px'>Update the balances from your previous accounting system. Be aware this  has an impact on transactions already entered, your conversion date, and  any reports that you may have run already.</font></p>");
		invoiceBrandingHTML = new HTML(
				"<a><font size='3px', color='green'>Invoice Branding</font></a>");
		invoiceCommentHtml = new HTML(
				"<p><font size='2px'>Customise the appearance of invoices, credit notes and statements. Add multiple themes with custom page titles, logos and payment advice. Also, control the automatic numbering of invoices.</font></p>");

		userHtml = new HTML(
				"<a><font size='3px', color='green'>Users</font></a>");
		userCommentHtml = new HTML(
				"<p><font size='2px'>Users are the people who can log in and view your organisation in Xero. You can add, edit and delete other users and determine the role they have.</font></p>");
		titlePanel.add(titleHtml);

		conversationPanel.add(conversionHTML);
		conversationPanel.add(conversationCommentHTML);

		conversionHTML.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getConversionBalancesAction().run(null,
						false);
			}
		});

		invoiceBrandingPanel.add(invoiceBrandingHTML);
		invoiceBrandingPanel.add(invoiceCommentHtml);

		invoiceBrandingHTML.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getInvoiceBrandingAction().run(null,
						false);
			}
		});

		userPanel.add(userHtml);
		userPanel.add(userCommentHtml);

		userHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

			}
		});

		titlePanel.add(titleHtml);
		optionsTable.setWidget(0, 0, conversationPanel);
		optionsTable.setWidget(0, 1, invoiceBrandingPanel);
		optionsTable.setWidget(1, 0, userPanel);

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
