package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
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
			radioButtonHtml, contactDetailsHtml, uploadPictureHtml, titleHtml,
			helpHtml;
	private VerticalPanel mainPanel, titlePanel, subLayPanel, uploadPanel,
			contactDetailsPanel, vPanel;
	private Button newBrandButton, automaticButton;
	private HorizontalPanel buttonPanel, showPanel, allPanel, nameAndMenuPanel;

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
		generalSettingsHTML = new HTML(FinanceApplication.getSettingsMessages()
				.generalSettingsLabel());
		invoiceBrandingHtml = new HTML(FinanceApplication.getSettingsMessages()
				.invoiceBrandingLabel());
		generalSettingsHTML.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getGeneralSettingsAction().run(null,
						false);
			}
		});
		titlePanel.add(generalSettingsHTML);
		titlePanel.add(invoiceBrandingHtml);

		helpHtml = new HTML("<p>What's this?</p>");
		helpHtml.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				helpHtml.getElement().getStyle().setCursor(Cursor.POINTER);
				helpHtml.getElement().getStyle().setTextDecoration(
						TextDecoration.UNDERLINE);
			}
		});
		helpHtml.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				helpHtml.getElement().getStyle().setTextDecoration(
						TextDecoration.NONE);
			}
		});
		helpHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// SettingsActionFactory.getInvoiceBrandingHelpAction().run(null,
				// false);
			}
		});
		buttonPanel = new HorizontalPanel();
		newBrandButton = new Button(FinanceApplication.getSettingsMessages()
				.newBrandingThemeButton());
		newBrandButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getNewBrandThemeAction().run(null, false);
			}
		});
		// newBrandButton.addMouseOverHandler(new MouseOverHandler() {
		//
		// @Override
		// public void onMouseOver(MouseOverEvent event) {
		// PopupPanel newBrandMenuPanel = new PopupPanel();
		// newBrandMenuPanel.add(getNewBrandMenu(newBrandMenuPanel));
		// newBrandMenuPanel.setPopupPosition(newBrandButton
		// .getAbsoluteLeft(), newBrandButton.getAbsoluteTop()
		// + newBrandButton.getOffsetHeight());
		// newBrandMenuPanel.show();
		// newBrandMenuPanel.setAutoHideEnabled(true);
		// }
		// });
		automaticButton = new Button(FinanceApplication.getSettingsMessages()
				.automaticSequencing());
		automaticButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getAutomaticSequenceAction().run(null,
						false);

			}
		});
		automaticButton.setVisible(false);
		buttonPanel.add(newBrandButton);
		buttonPanel.add(automaticButton);
		mainPanel.add(titlePanel);
		mainPanel.add(helpHtml);
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
			pageType = FinanceApplication.getSettingsMessages().a4();
		} else {
			pageType = FinanceApplication.getSettingsMessages().usLetter();
		}

		return pageType;

	}

	private String getLogoType(int type) {
		String logoType = null;
		if (type == ClientBrandingTheme.LOGO_ALIGNMENT_LEFT) {
			logoType = FinanceApplication.getSettingsMessages().left();
		} else {
			logoType = FinanceApplication.getSettingsMessages().right();
		}
		return logoType;

	}

	// private String getTaxesType(int type) {
	// String taxType = null;
	// if (type == ClientBrandingTheme.SHOW_TAXES_AS_EXCLUSIVE) {
	// taxType = FinanceApplication.getSettingsMessages().taxExclusive();
	// } else {
	// taxType = FinanceApplication.getSettingsMessages().taxInclucive();
	// }
	// return taxType;
	//
	// }

	private VerticalPanel addingDefaultTheme(ClientBrandingTheme theme) {
		final Button optionsButton;
		titleHtml = new HTML("<b>" + theme.getThemeName() + "</b>");
		vPanel = new VerticalPanel();

		subLayPanel = new VerticalPanel();
		optionsButton = new Button(FinanceApplication.getSettingsMessages()
				.options());
		allLabelsHtml = new HTML("<p>"
				+ FinanceApplication.getSettingsMessages().pageLabel()
				+ " : <b>" + getPageType(theme.getPageSizeType()) + " : </b>"
				+ FinanceApplication.getSettingsMessages().marginTop() + "<b>"
				+ theme.getTopMargin() + " : </b>"
				+ FinanceApplication.getSettingsMessages().bottom() + " : <b>"
				+ theme.getBottomMargin() + " : </b>"
				+ FinanceApplication.getSettingsMessages().addressPadding()
				+ " : <b>" + theme.getAddressPadding() + " </b>"
				+ FinanceApplication.getSettingsMessages().font() + " : <b>"
				+ theme.getFont() + "</b> , <b>" + theme.getFontSize()
				+ " </b></p>");
		boolean[] showArray = new boolean[] { theme.isShowTaxNumber(),
				theme.isShowTaxColumn(), theme.isShowColumnHeadings(),
				theme.isShowUnitPrice_And_Quantity(),
				// theme.isShowPaymentAdviceCut_Away(),
				theme.isShowRegisteredAddress(), theme.isShowLogo() };
		String[] showDataArray = new String[] {
				FinanceApplication.getSettingsMessages().taxNumber(),
				FinanceApplication.getSettingsMessages().columnHeadings(),
				FinanceApplication.getSettingsMessages().unitPriceAndQuantity(),
				// FinanceApplication.getSettingsMessages().paymentAdviceCutAway(),
				FinanceApplication.getSettingsMessages().taxColumn(),
				FinanceApplication.getSettingsMessages().registeredAddress(),
				FinanceApplication.getSettingsMessages().logo() };
		String showItem = new String();
		for (int i = 0; i < showArray.length; i++) {
			if (showArray[i]) {
				showItem += "<li>" + showDataArray[i];
			}
		}
		ShowHtml = new HTML("<p>"
				+ FinanceApplication.getSettingsMessages().show() + " : </p>");
		checkBoxHtml = new HTML("<ui>" + showItem + "</ui>");
		radioButtonHtml = new HTML("<ui><li>"
		// + getTaxesType(theme.getShowTaxesAsType())
				+ getLogoType(theme.getLogoAlignmentType()) + "<ui>");

		headingsHtml = new HTML("<p>"
				+ FinanceApplication.getSettingsMessages().headings()
				+ " : "
				// + theme.getOpenInvoiceTitle()
				+ theme.getOverDueInvoiceTitle() + " , "
				+ theme.getCreditMemoTitle() + " , "
				+ theme.getStatementTitle() + "</p>");
		paypalEmailHtml = new HTML("<p>"
				+ FinanceApplication.getSettingsMessages().paypalEmail()
				+ " : " + theme.getPayPalEmailID() + "</p>");

		// adding terms.....
		String terms;
		if (theme.getTerms_And_Payment_Advice() == null) {
			terms = FinanceApplication.getSettingsMessages().notAdded();
		} else {
			terms = theme.getTerms_And_Payment_Advice();
		}
		termsHtml = new HTML("<p> "
				+ FinanceApplication.getSettingsMessages().terms() + " : "
				+ terms + "</p>");
		showPanel = new HorizontalPanel();
		showPanel.add(checkBoxHtml);
		showPanel.add(radioButtonHtml);

		optionsButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				optionsMenu(optionsButton);
			}
		});

		contactDetailsHtml = new HTML("<p><b>"
				+ FinanceApplication.getSettingsMessages()
						.contactDetailsLabel() + "</b><br>"
				+ theme.getContactDetails() + "</p>");
		contactDetailsPanel = new VerticalPanel();
		contactDetailsPanel.add(contactDetailsHtml);
		contactDetailsPanel.setStyleName("contact-deatails-panel");

		uploadPictureHtml = new HTML(FinanceApplication.getSettingsMessages()
				.uploadLogo());

		uploadPictureHtml.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				uploadPictureHtml.getElement().getStyle().setCursor(
						Cursor.POINTER);
				uploadPictureHtml.getElement().getStyle().setTextDecoration(
						TextDecoration.UNDERLINE);
			}
		});
		uploadPictureHtml.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				uploadPictureHtml.getElement().getStyle().setTextDecoration(
						TextDecoration.NONE);
			}
		});
		uploadPanel = new VerticalPanel();
		uploadPanel.setStyleName("upload-logo");
		uploadPanel.add(uploadPictureHtml);
		uploadPanel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

		subLayPanel.add(allLabelsHtml);
		subLayPanel.add(ShowHtml);
		subLayPanel.add(showPanel);
		subLayPanel.add(headingsHtml);
		subLayPanel.add(paypalEmailHtml);
		subLayPanel.add(termsHtml);
		// subLayPanel.setWidth("525px");
		subLayPanel.setStyleName("general-setting-invoice");
		contactDetailsPanel.setWidth("200px");
		uploadPanel.setWidth("150px");
		allPanel = new HorizontalPanel();
		allPanel.add(subLayPanel);
		allPanel.add(contactDetailsPanel);
		allPanel.add(uploadPanel);

		nameAndMenuPanel = new HorizontalPanel();
		nameAndMenuPanel.add(titleHtml);
		nameAndMenuPanel.setStyleName("standard-options");
		titleHtml.getElement().getAbsoluteLeft();
		nameAndMenuPanel.add(optionsButton);
		optionsButton.getElement().getAbsoluteRight();

		vPanel.add(nameAndMenuPanel);
		vPanel.add(allPanel);

		return vPanel;

	}

	protected void optionsMenu(Button button) {
		PopupPanel optionsPanel = new PopupPanel();
		optionsPanel.add(getOptionsMenu(optionsPanel));
		optionsPanel.setPopupPosition(button.getAbsoluteLeft(), button
				.getAbsoluteTop()
				+ button.getOffsetHeight());
		optionsPanel.show();
		optionsPanel.setAutoHideEnabled(true);
	}

	private CustomMenuBar getOptionsMenu(PopupPanel panel) {
		CustomMenuBar bar = new CustomMenuBar();
		bar.addItem(FinanceApplication.getSettingsMessages().edit(),
				getOptionsCommand(1, panel));
		bar.addItem(FinanceApplication.getSettingsMessages().copy(),
				getOptionsCommand(2, panel));
		bar.addItem(FinanceApplication.getSettingsMessages().changeLogo(),
				getOptionsCommand(3, panel));
		bar.addItem(FinanceApplication.getSettingsMessages().delete(),
				getOptionsCommand(4, panel));
		bar.addItem(FinanceApplication.getSettingsMessages().removeLogo(),
				getOptionsCommand(5, panel));
		return bar;
	}

	private Command getOptionsCommand(final int type, final PopupPanel panel) {
		final Command command = new Command() {

			@Override
			public void execute() {
				panel.hide();
				switch (type) {
				case 1:
					SettingsActionFactory.getNewBrandThemeAction().run(theme,
							false);
					break;
				case 2:
					SettingsActionFactory.getCopyThemeAction()
							.run(theme, false);
					break;
				case 4:
					SettingsActionFactory.getDeleteThemeAction().run(theme,
							false);
					break;
				default:
				}
			}
		};
		return command;
	}

	private CustomMenuBar getNewBrandMenu(PopupPanel panel) {
		CustomMenuBar menuBar = new CustomMenuBar();
		menuBar.addItem(FinanceApplication.getSettingsMessages()
				.standardTheme(), getNewBrandCommand(1, panel));
		menuBar.addItem(FinanceApplication.getSettingsMessages()
				.customdocxTheme(), getNewBrandCommand(2, panel));
		return menuBar;

	}

	private Command getNewBrandCommand(final int i, final PopupPanel panel) {
		Command command = new Command() {
			@Override
			public void execute() {
				panel.hide();
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

	@Override
	public void saveSuccess(IAccounterCore object) {
		if (object != null) {
			super.saveSuccess(object);
			SettingsActionFactory.getInvoiceBrandingAction().run(null, false);
		} else
			saveFailed(new Exception(FinanceApplication.getCompanyMessages()
					.failed()));
	}

}
