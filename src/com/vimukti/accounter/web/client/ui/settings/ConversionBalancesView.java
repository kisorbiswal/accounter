package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

@SuppressWarnings("unchecked")
public class ConversionBalancesView extends AbstractBaseView {
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

	public ConversionBalancesView(String endingDate, String year) {
		if (endingDate != null && year != null) {
			addNewTab(endingDate, year);
		}
	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	@SuppressWarnings("deprecation")
	public void addNewTab(String endingDate, String year) {
		createControls();
		tabPanel.add(getBodyControls(), "01 January" + year + " _ "
				+ endingDate);
	}

	@SuppressWarnings("deprecation")
	private void createControls() {
		initControls();

		superHeaderHtml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SettingsActionFactory.getGeneralSettingsAction().run(null,
						false);
			}
		});

		conversionDateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				try {
					SettingsActionFactory.getConversionDateAction().run(null,
							false);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});

		bodyPanel.add(headerButtonPanel);
		try {
			tabPanel = new DecoratedTabPanel();
			tabPanel.add(getBodyControls(), Utility
					.getCurrentFiscalYearStartDate().toString()
					+ " _ " + Utility.getCurrentFiscalYearEndDate().toString());

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
		superHeaderHtml = new HTML(
				"<a><font size='2px', color='green'>General Settings</font></a> > ");
		headerHtml = new HTML(
				"<p><font size='4px',color='green'>Conversion Balances</font></p>");

		addComparativeBalancesButton = new Button("Add Comparative Balances");
		conversionDateButton = new Button("Conversion Date");

		headerButtonPanel.add(addComparativeBalancesButton);
		headerButtonPanel.add(conversionDateButton);

		headerPanel.add(superHeaderHtml);
		headerPanel.add(headerHtml);

	}

	private VerticalPanel getBodyControls() {
		tabBodyPanel = new VerticalPanel();
		addNewButtonPanel = new HorizontalPanel();
		footerButtonPanel = new HorizontalPanel();
		addNewButton = new Button("Add New Line");
		saveButton = new Button("Save");
		cancelButton = new Button("Cancel");
		footerPanel = new VerticalPanel();
		debit_creditForm = new DynamicForm();
		debitLabel = new AmountLabel("Total Debits");
		creditLabel = new AmountLabel("Total Credits");
		adjustmentLabel = new AmountLabel("Adjustments");
		tabFlexTable = new FlexTable();
		account = new Label("Account");
		credit = new Label("Credit");
		debit = new Label("Debit");

		tabFlexTable.setWidget(0, 0, account);
		tabFlexTable.setWidget(0, 1, credit);
		tabFlexTable.setWidget(0, 2, debit);

		adjustmentLabel.setAmount(debitLabel.getAmount()
				- creditLabel.getAmount());
		removeZeroBalance = new LabelItem();
		removeZeroBalance.setValue("remove zero balances");
		removeZeroBalance.addStyleName("falseHyperlink");
		removeZeroBalance.setShowTitle(false);
		removeZeroBalance.setDisabled(isEdit);
		showAllAccounts = new LabelItem();
		showAllAccounts.setValue("show all accounts");
		showAllAccounts.addStyleName("falseHyperlink");
		showAllAccounts.setShowTitle(false);
		showAllAccounts.setDisabled(isEdit);
		footerCommentHtml = new HTML(
				"<p><font size='2px',color='gray'>This accounts for the difference between debits and credits and for FX gains and losses</font></p>");

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
	public void initData() {
		super.initData();
	}

	@Override
	public void onEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFailed(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(Boolean result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

}
