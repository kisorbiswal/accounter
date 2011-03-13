package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.FinanceApplication;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

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
		LabelItem item = new LabelItem();
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
				SettingsActionFactory.getUsersAction().run(null, false);
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
