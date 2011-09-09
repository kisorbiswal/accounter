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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;

public class ConversionBalancesView extends BaseView {
	private HTML superHeaderHtml, headerHtml, footerCommentHtml;
	private VerticalPanel headerPanel, bodyPanel, mainPanel, tabBodyPanel,
			footerPanel;
	private HorizontalPanel headerButtonPanel, footerButtonPanel,
			addNewButtonPanel;
	private Button addComparativeBalancesButton, conversionDateButton,
			saveButton, cancelButton, addNewButton;
	private LabelItem removeZeroBalance, showAllAccounts;
	private DynamicForm debit_creditForm, labelsForm, adjustmentsForm;
	private AmountLabel creditLabel, debitLabel, adjustmentLabel;
	private DecoratedTabPanel tabPanel;
	// private ListGrid grid;
	private Label account, debit, credit;
	private FlexTable tabFlexTable;
	private AccounterMessages messages = Accounter.messages();

	public ConversionBalancesView(String endingDate, String year) {
		if (endingDate != null && year != null) {
			addNewTab(endingDate, year);
		}
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	public void addNewTab(String endingDate, String year) {
		createControls();
		tabPanel.add(getBodyControls(), Accounter.constants().january01()
				+ year + " _ " + endingDate);
	}

	private void createControls() {
		initControls();

		superHeaderHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getGeneralSettingsAction().run(null, false);
			}
		});

		conversionDateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				try {
					ActionFactory.getConversionDateAction().run(null, false);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		bodyPanel.add(headerButtonPanel);
		try {
			tabPanel = new DecoratedTabPanel();
			tabPanel.add(getBodyControls(), Accounter.getCompany()
					.getCurrentFiscalYearStartDate().toString()
					+ " _ "
					+ Accounter.getCompany().getCurrentFiscalYearEndDate()
							.toString());

			bodyPanel.add(tabPanel);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		mainPanel.add(headerPanel);
		mainPanel.add(bodyPanel);

		add(mainPanel);
	}

	private void initControls() {
		headerPanel = new VerticalPanel();
		bodyPanel = new VerticalPanel();
		mainPanel = new VerticalPanel();

		headerButtonPanel = new HorizontalPanel();
		// grid=new
		superHeaderHtml = new HTML(Accounter.constants().generalSettingsLabel());
		superHeaderHtml.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				superHeaderHtml.getElement().getStyle()
						.setCursor(Cursor.POINTER);
				superHeaderHtml.getElement().getStyle()
						.setTextDecoration(TextDecoration.UNDERLINE);
			}
		});
		superHeaderHtml.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				superHeaderHtml.getElement().getStyle()
						.setTextDecoration(TextDecoration.NONE);
			}
		});
		headerHtml = new HTML(messages.conversionBalanaceHeader());

		addComparativeBalancesButton = new Button(
				messages.addComparativeButton());
		conversionDateButton = new Button(messages.conversionDateButton());

		headerButtonPanel.add(addComparativeBalancesButton);
		headerButtonPanel.add(conversionDateButton);
		headerPanel.add(superHeaderHtml);
		headerPanel.add(headerHtml);

	}

	private VerticalPanel getBodyControls() {
		tabBodyPanel = new VerticalPanel();
		addNewButtonPanel = new HorizontalPanel();
		footerButtonPanel = new HorizontalPanel();
		addNewButton = new Button(messages.addNewLine());
		saveButton = new Button(Accounter.constants().saveButton());
		cancelButton = new Button(Accounter.constants().cancelButton());
		footerPanel = new VerticalPanel();
		debit_creditForm = new DynamicForm();
		debitLabel = new AmountLabel(Accounter.constants().totalDebits());
		creditLabel = new AmountLabel(Accounter.constants().totalCredits());
		adjustmentLabel = new AmountLabel(Accounter.constants().adjustments());
		tabFlexTable = new FlexTable();
		account = new Label(Accounter.messages().conversionAccount(
				Global.get().Account()));
		credit = new Label(Accounter.constants().credit());
		debit = new Label(Accounter.constants().debit());

		tabFlexTable.setWidget(0, 0, account);
		tabFlexTable.setWidget(0, 1, credit);
		tabFlexTable.setWidget(0, 2, debit);

		adjustmentLabel.setAmount(debitLabel.getAmount()
				- creditLabel.getAmount());
		removeZeroBalance = new LabelItem();
		removeZeroBalance.setValue(Accounter.constants().removeZeroBalances());
		removeZeroBalance.addStyleName("falseHyperlink");
		removeZeroBalance.setShowTitle(false);
		removeZeroBalance.setDisabled(isInViewMode());
		showAllAccounts = new LabelItem();
		showAllAccounts.setValue(Accounter.messages().showAllAccounts(
				Global.get().Account()));
		showAllAccounts.addStyleName("falseHyperlink");
		showAllAccounts.setShowTitle(false);
		showAllAccounts.setDisabled(isInViewMode());
		footerCommentHtml = new HTML(messages.footerComment());

		labelsForm = new DynamicForm();
		labelsForm.setNumCols(4);
		labelsForm.setCellSpacing(6);
		labelsForm.setFields(removeZeroBalance, showAllAccounts);
		addNewButtonPanel.add(addNewButton);
		addNewButtonPanel.add(labelsForm);

		debit_creditForm.setNumCols(4);
		debit_creditForm.setCellSpacing(6);
		debit_creditForm.setWidth("350px");
		debit_creditForm.setFields(debitLabel, creditLabel);

		adjustmentsForm = new DynamicForm();
		adjustmentsForm.setNumCols(2);
		adjustmentsForm.setCellSpacing(6);
		adjustmentsForm.setWidth("375px");
		adjustmentsForm.setFields(adjustmentLabel);

		footerButtonPanel.add(saveButton);
		footerButtonPanel.add(cancelButton);

		footerPanel.add(addNewButtonPanel);
		footerPanel.add(debit_creditForm);
		footerPanel.add(adjustmentsForm);
		footerPanel.add(footerCommentHtml);

		tabBodyPanel.add(tabFlexTable);
		tabBodyPanel.add(footerPanel);
		tabBodyPanel.add(footerButtonPanel);

		return tabBodyPanel;

	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().conversionBalance();
	}

	@Override
	public void fitToSize(int height, int width) {

	}

	@Override
	public List getForms() {
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
