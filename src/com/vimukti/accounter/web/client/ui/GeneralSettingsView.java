package com.vimukti.accounter.web.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;

@SuppressWarnings("unchecked")
public class GeneralSettingsView extends AbstractBaseView {
	private VerticalPanel mainPanel, conversationPanel, invoiceBrandingPanel;
	private FlexTable optionsTable;
	private HorizontalPanel titlePanel;
	private HTML conversionHTML, conversationCommentHTML, invoiceBrandingHTML,
			invoiceCommentHtml, titleHtml;

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
		
		titleHtml = new HTML(
		"<a><font size='5px', color='green'>General Settings</p></a>");

		conversionHTML = new HTML(
				"<a><font size='3px', color='green'>Conversion Balances</font></a>");
		conversationCommentHTML = new HTML(
				"<p><font size='1px'>Update the balances from your previous accounting system. Be aware this  has an impact on transactions already entered, your conversion date, and  any reports that you may have run already.</font></p>");
		invoiceBrandingHTML = new HTML(
				"<a><font size='3px', color='green'>Invoice Branding</font></a>");
		invoiceCommentHtml = new HTML(
				"<p><font size='1px'>Customise the appearance of invoices, credit notes and statements. Add multiple themes with custom page titles, logos and payment advice. Also, control the automatic numbering of invoices.</font></p>");

		titlePanel.add(titleHtml);

		conversationPanel.add(conversionHTML);
		conversationPanel.add(conversationCommentHTML);

		conversionHTML.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

			}
		});

		invoiceBrandingPanel.add(invoiceBrandingHTML);
		invoiceBrandingPanel.add(invoiceCommentHtml);

		invoiceBrandingHTML.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});

		titlePanel.add(titleHtml);
		optionsTable.setWidget(0, 0, conversationPanel);
		optionsTable.setWidget(0, 1, invoiceBrandingPanel);

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
