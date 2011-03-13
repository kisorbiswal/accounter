package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.CustomMenuBar;
import com.vimukti.accounter.web.client.ui.FinanceApplication;

public class InvoiceBrandingView extends AbstractBaseView<ClientBrandingTheme> {

	private ClientBrandingTheme theme;
	private HTML generalSettingsHTML, invoiceBrandingHtml, allLabelsHtml,
			ShowHtml, checkBoxHtml, headingsHtml, paypalEmailHtml, termsHtml,
			radioButtonHtml, contactDetailsHtml, uploadPictureHtml;
	private VerticalPanel mainPanel, titlePanel, subLayPanel, uploadPanel,
			contactDetailsPanel;
	private Button newBrandButton, automaticButton;
	private HorizontalPanel buttonPanel, showPanel, allPanel;

	@Override
	public void setData(ClientBrandingTheme data) {
		super.setData(data);
		if (data != null) {
			theme = data;
		}
	}

	private void createControls() {
		mainPanel = new VerticalPanel();
		titlePanel = new VerticalPanel();
		generalSettingsHTML = new HTML(
				"<a><font size='1px', color='green'>General Settings</font></a> >");
		invoiceBrandingHtml = new HTML(
				"<p><font size='4px'>Invoice Branding<font></p>");
		generalSettingsHTML.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getGeneralSettingsAction().run(null,
						false);
			}
		});
		titlePanel.add(generalSettingsHTML);
		titlePanel.add(invoiceBrandingHtml);

		buttonPanel = new HorizontalPanel();
		newBrandButton = new Button("New Branding Theme");
		newBrandButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getNewBrandThemeAction().run(null, false);
			}
		});
		newBrandButton.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				DialogBox dialogBox = new DialogBox();
				dialogBox.add(getNewBrandMenu());
				dialogBox.setPopupPosition(newBrandButton.getAbsoluteLeft(),
						newBrandButton.getAbsoluteTop()
								+ newBrandButton.getOffsetHeight());
				dialogBox.show();
				dialogBox.setAutoHideEnabled(true);
			}
		});
		automaticButton = new Button("Automatic Sequencing");
		automaticButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getAutomaticSequenceAction().run(null,
						false);

			}
		});
		buttonPanel.add(newBrandButton);
		buttonPanel.add(automaticButton);
		mainPanel.add(titlePanel);
		mainPanel.add(buttonPanel);
		List<ClientBrandingTheme> brandingThemes = FinanceApplication
				.getCompany().getBrandingTheme();
		for (int i = 0; i < brandingThemes.size(); i++) {
			theme = brandingThemes.get(i);
			mainPanel.add(addingDefaultTheme(theme));
		}
		add(mainPanel);

	}

	private String getPageType(int type) {
		String pageType = null;
		if (type == ClientBrandingTheme.PAGE_SIZE_A4) {
			pageType = "A4";
		} else {
			pageType = "US Letter";
		}

		return pageType;

	}

	private String getLogoType(int type) {
		String logoType = null;
		if (type == ClientBrandingTheme.LOGO_ALIGNMENT_LEFT) {
			logoType = "Left";
		} else {
			logoType = "Right";
		}
		return logoType;

	}

	private String getTaxesType(int type) {
		String taxType = null;
		if (type == ClientBrandingTheme.SHOW_TAXES_AS_EXCLUSIVE) {
			taxType = "Tax exclusive";
		} else {
			taxType = "Tax inclucive";
		}
		return taxType;

	}

	private HorizontalPanel addingDefaultTheme(ClientBrandingTheme theme) {
		subLayPanel = new VerticalPanel();

		allLabelsHtml = new HTML("<p>Page :<b>"
				+ getPageType(theme.getPageSizeType()) + "</b>Margin Top :<b>"
				+ theme.getTopMargin() + "</b>Bottom :<b>"
				+ theme.getBottomMargin() + "</b>Address padding :<b>"
				+ theme.getAddressPadding() + "</b>Font :<b>" + theme.getFont()
				+ "</b>,<b>" + theme.getFontSize() + "</b></p>");
		boolean[] showArray = new boolean[] { theme.isShowTaxNumber(),
				theme.isShowTaxColumn(), theme.isShowColumnHeadings(),
				theme.isShowUnitPrice_And_Quantity(),
				theme.isShowPaymentAdviceCut_Away(),
				theme.isShowRegisteredAddress(), theme.isShowLogo() };
		String[] showDataArray = new String[] { " Tax number",
				"  Column headings ", " Unit price and quantity ",
				" Payment advice cut-away ", " Tax column ",
				" Registered address ", " Logo" };
		String showItem = new String();
		for (int i = 0; i < showArray.length; i++) {
			if (showArray[i]) {
				showItem += "<li>" + showDataArray[i];
			}
		}
		ShowHtml = new HTML("<p>Show: </p>");
		checkBoxHtml = new HTML("<ui>" + showItem + "</ui>");
		radioButtonHtml = new HTML("<ui><li>"
				+ getTaxesType(theme.getShowTaxesAsType()) + "<li>"
				+ getLogoType(theme.getLogoAlignmentType()) + "<ui>");

		headingsHtml = new HTML("<p>Headings:" + theme.getOpenInvoiceTitle()
				+ "," + theme.getOverDueInvoiceTitle() + ","
				+ theme.getCreditMemoTitle() + "," + theme.getStatementTitle()
				+ "</p>");
		paypalEmailHtml = new HTML("<p>Paypal Email:"
				+ theme.getPayPalEmailID() + "</p>");
		// adding terms.....
		String terms;
		if (theme.getTerms_And_Payment_Advice() == null) {
			terms = "(not added)";
		} else {
			terms = theme.getTerms_And_Payment_Advice();
		}
		termsHtml = new HTML("<p>Terms:" + terms + "</p>");
		showPanel = new HorizontalPanel();
		showPanel.add(checkBoxHtml);
		showPanel.add(radioButtonHtml);

		contactDetailsHtml = new HTML("<p><b>Contact Details </b><br>"
				+ theme.getContactDetails() + "</p>");
		contactDetailsPanel = new VerticalPanel();
		contactDetailsPanel.add(contactDetailsHtml);

		uploadPictureHtml = new HTML("<a>Upload logo</a>");
		uploadPanel = new VerticalPanel();
		uploadPanel.add(uploadPictureHtml);

		subLayPanel.add(allLabelsHtml);
		subLayPanel.add(ShowHtml);
		subLayPanel.add(showPanel);
		subLayPanel.add(headingsHtml);
		subLayPanel.add(paypalEmailHtml);
		subLayPanel.add(termsHtml);
		subLayPanel.setWidth("650px");
		contactDetailsPanel.setWidth("200px");
		uploadPanel.setWidth("150px");
		allPanel = new HorizontalPanel();
		allPanel.add(subLayPanel);
		allPanel.add(contactDetailsPanel);
		allPanel.add(uploadPanel);

		return allPanel;

	}

	private CustomMenuBar getNewBrandMenu() {
		CustomMenuBar menuBar = new CustomMenuBar();
		menuBar.addItem("Standard Theme", getNewBrandCommand(1));
		menuBar.addItem("Custom .docx Theme", getNewBrandCommand(2));
		return menuBar;

	}

	private Command getNewBrandCommand(final int i) {
		Command command = new Command() {
			@Override
			public void execute() {
				switch (i) {
				case 1:
					SettingsActionFactory.getNewBrandThemeAction().run(null,
							false);
					break;
				case 2:
					SettingsActionFactory.getCustomThemeAction().run(null,
							false);
					break;
				}
			}
		};
		return command;

	}

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
	public void init() {
		super.init();
		try {
			createControls();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	@Override
	public void initData() {
		super.initData();
	}

	@Override
	public void printPreview() {

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
