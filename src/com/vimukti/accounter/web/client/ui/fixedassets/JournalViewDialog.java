package com.vimukti.accounter.web.client.ui.fixedassets;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetSellOrDisposeReviewJournal;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.CustomCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.RevenueAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.RevenueAndExpenseAccountCombo;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * This Class is JournalViewDialog for Reporting Values before Selling and
 * Disposing Items
 * 
 * @author B.Srinivasa Rao
 * 
 */
public class JournalViewDialog extends BaseDialog {

	private DynamicForm disposalSummaryForm, disposalJournalForm;
	private FixedAssetSellOrDisposeReviewJournal journalAsset;
	private double creditTotal = 0.0, debitTotal = 0.0, lossorGainAmount,
			totalCapitalGainAmount;
	protected ClientAccount totalCapitalGainAccount,
			LossorGainOnDisposalAccount;
	private RevenueAccountCombo totalGainItem;
	private RevenueAndExpenseAccountCombo lossOnDisposal, gainOnDisposal;
	private ArrayList<DynamicForm> forms = new ArrayList<DynamicForm>();
	private Map<String, Double> disposalJOurnal;
	private Map<String, Double> journalSummary;

	public JournalViewDialog(String title, String desc,
			FixedAssetSellOrDisposeReviewJournal journalObject) {
		super(title, desc);
		this.journalAsset = journalObject;
		init();
	}

	private void init() {
		journalSummary = this.journalAsset.getDisposalSummary();
		disposalJOurnal = this.journalAsset.getDisposalJournal();
		createControls();
	}

	/**
	 * This method for Creating Controls in JournalView Dialog
	 */

	private void createControls() {
		Label disposalSummarylabel = new Label(Accounter.constants()
				.disposalSummary());
		disposalSummarylabel.setStyleName(Accounter.constants().labelTitle());
		disposalSummarylabel.addStyleName("title-color");
		disposalSummaryForm = getDisposalSummaryForm();

		disposalSummaryForm.setWidth("100%");
		Label disposalJOurnallabel = new Label(Accounter.constants()
				.disposalJournal());
		disposalJOurnallabel.setStyleName(Accounter.constants().labelTitle());
		disposalJOurnallabel.addStyleName("title-color");
		totalGainItem = createTotalGainCombo();
		totalGainItem.setRequired(true);
		lossOnDisposal = createLossorGainAccount(Accounter.constants()
				.lossOnDisposal());
		lossOnDisposal.setRequired(true);
		gainOnDisposal = createLossorGainAccount(Accounter.constants()
				.gainOnDisposal());
		gainOnDisposal.setRequired(true);

		disposalJournalForm = getDisposalJournalForm();

		disposalJournalForm.setWidth("100%");

		okbtn = new Button(Accounter.constants().post());
		okbtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				processOK();
			}
		});
		okbtn.setWidth("100px");

		footerLayout.clear();
		footerLayout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		footerLayout.add(okbtn);
		footerLayout.add(cancelBtn);
		// footerLayout.setCellWidth(okbtn, "100%");
		footerLayout.setCellHorizontalAlignment(okbtn,
				HasHorizontalAlignment.ALIGN_RIGHT);

		VerticalPanel mainLayout = new VerticalPanel();
		mainLayout.setWidth("700px");
		mainLayout.setSpacing(15);
		mainLayout.add(disposalSummarylabel);
		mainLayout.add(disposalSummaryForm);
		mainLayout.add(disposalJOurnallabel);
		mainLayout.add(disposalJournalForm);
		setWidth("550px");
		setBodyLayout(mainLayout);
		center();
		show();
	}

	/**
	 * Getting the DynamicForm with DisposalSummary Controls with Data
	 */

	private DynamicForm getDisposalSummaryForm() {
		DynamicForm disposalSummaryForm = new DynamicForm();
		disposalSummaryForm.setStyleName("borders");
		int row = 0;
		double value = 0.0;
		Set<String> summaryString = this.journalAsset.getDisposalSummary()
				.keySet();
		for (String StringValue : summaryString) {
			value = journalSummary.get(StringValue);
			if (!DecimalUtil.isEquals(value, 0.0)) {
				disposalSummaryForm.setText(row, 0, StringValue);
				disposalSummaryForm.setText(row, 1, amountAsString(value));
			}
			disposalSummaryForm.getCellFormatter().setWidth(0, 0, "100%");
			row++;
		}

		return disposalSummaryForm;
	}

	/**
	 * Getting the DynamicForm of DisposalJournal with Data
	 */

	private DynamicForm getDisposalJournalForm() {
		DynamicForm disposalJournalForm = new DynamicForm();
		disposalJournalForm.setStyleName("borders");
		this.forms.clear();

		Set<String> journalKeyset = this.journalAsset.getDisposalJournal()
				.keySet();
		setCreditandDebitForm(disposalJournalForm);
		int row = 1, col = 0;
		double value = 0.0, debitvalue;
		for (String keyValue : journalKeyset) {
			if (keyValue != null) {
				if (!isComboname(keyValue))
					disposalJournalForm.setText(row, col++, keyValue);
				else {

					disposalJournalForm.setWidget(row, col++,
							getComboForm(keyValue));
				}

				disposalJournalForm.getCellFormatter().setWidth(row, col - 1,
						"100%");
				value = disposalJOurnal.get(keyValue);
				if (DecimalUtil.isLessThan(value, 0)) {
					debitvalue = value * (-1);
					disposalJournalForm.setText(row, col++,
							amountAsString(debitvalue));
					disposalJournalForm.setText(row, col++, "" + " ");
					disposalJournalForm.getCellFormatter().setStyleName(row,
							col - 1, "column-seperater");
					disposalJournalForm.setText(row, col++, "" + " ");
					setDebitTotal(debitvalue);

				} else if (DecimalUtil.isGreaterThan(value, 0)) {
					disposalJournalForm.setText(row, col++, "" + " ");
					disposalJournalForm.setText(row, col++, "" + " ");
					disposalJournalForm.getCellFormatter().setStyleName(row,
							col - 1, "column-seperater");
					disposalJournalForm.setText(row, col++,
							amountAsString(value));

					setCreditTotal(value);
				}

				col = 0;
				row++;
			}
		}
		setTotalLabelForm(disposalJournalForm, row);

		return disposalJournalForm;
	}

	/**
	 * This method for getting corresponding Combo to display in DisposalJournal
	 * Form
	 */

	private CustomCombo getCombo(String keyValue) {
		if (keyValue.equals(Accounter.constants().lossOnDisposal())) {
			setLossorGainAmount(this.disposalJOurnal.get(keyValue));
			return lossOnDisposal;
		} else if (keyValue.equals(Accounter.constants().gainOnDisposal())) {
			setLossorGainAmount(this.disposalJOurnal.get(keyValue));
			return gainOnDisposal;
		} else if (keyValue.equals(Accounter.constants().totalCapitalGain())) {
			setTotalCapitalGainAmount(this.disposalJOurnal.get(keyValue));
			return totalGainItem;
		}
		return null;
	}

	/**
	 * This is for TotalLabelForm with credit and Debit Total Values
	 * 
	 * @return
	 */
	private void setTotalLabelForm(DynamicForm form, int row) {
		HTML totalLabel = new HTML();
		totalLabel.setHTML(Accounter.constants().total());
		String debittotal = "<strong>" + amountAsString(this.debitTotal)
				+ "</strong>";
		HTML debitValueLabel = new HTML(debittotal);
		String credittotal = "<strong>" + amountAsString(this.creditTotal)
				+ "</strong>";
		HTML creditValueLabel = new HTML(credittotal);
		form.setWidget(row, 0, totalLabel);
		form.setWidget(row, 1, debitValueLabel);
		form.setText(0, 2, "");
		form.setWidget(row, 3, creditValueLabel);
	}

	/**
	 * This method is setting Debit and Credit labels to the Form
	 */

	private void setCreditandDebitForm(DynamicForm form) {
		HTML debitLabel = new HTML();
		debitLabel.setHTML(Accounter.constants().debit());
		HTML creditLabel = new HTML();
		creditLabel.setHTML(Accounter.constants().credit());
		form.setText(0, 0, "");
		form.setWidth("100%");
		form.setWidget(0, 1, debitLabel);
		form.setText(0, 2, "");
		form.setWidget(0, 3, creditLabel);
	}

	public void setCreditTotal(double creditTotal) {
		this.creditTotal += creditTotal;
	}

	public void setDebitTotal(double debitTotal) {
		this.debitTotal += debitTotal;
	}

	private RevenueAccountCombo createTotalGainCombo() {
		RevenueAccountCombo revenueCombo = new RevenueAccountCombo(Accounter
				.constants().totalCapitalGain(), false);
		revenueCombo.setWidth(80);
		revenueCombo.setRequired(true);
		revenueCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						setTotalCapitalGainAccount(selectItem);
					}
				});
		return revenueCombo;
	}

	private RevenueAndExpenseAccountCombo createLossorGainAccount(String title) {
		RevenueAndExpenseAccountCombo revenueandExpenceCombo = new RevenueAndExpenseAccountCombo(
				title, false);
		revenueandExpenceCombo.setRequired(true);
		revenueandExpenceCombo.setWidth(80);
		revenueandExpenceCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						setLossorGainOnDisposalAccount(selectItem);
					}
				});
		return revenueandExpenceCombo;
	}

	protected void setLossorGainOnDisposalAccount(ClientAccount account) {
		if (account != null)
			this.LossorGainOnDisposalAccount = account;
	}

	protected void setTotalCapitalGainAccount(ClientAccount account) {
		if (account != null)
			this.totalCapitalGainAccount = account;
	}

	public ValidationResult validate() {
		// for (DynamicForm form : this.forms)
		// return AccounterValidator.validateForm(form, true);
		return DynamicForm.validate(this.forms
				.toArray(new DynamicForm[this.forms.size()]));
	}

	/**
	 * This method is for resulting the key value form DisposalJOurnal map is
	 * Combo or not
	 */
	private boolean isComboname(String keyvalue) {
		String[] combonames = getComboNames();
		for (String value : combonames) {
			if (value.equals(keyvalue))
				return true;
		}
		return false;
	}

	/**
	 * This is for getting Correspondig combo names
	 */

	private String[] getComboNames() {
		return new String[] { Accounter.constants().lossOnDisposal(),
				Accounter.constants().gainOnDisposal(),
				Accounter.constants().totalCapitalGain() };

	}

	/**
	 * Each combo is setting as separate DynamicForm.So This is for getting the
	 * ComboForm
	 */
	private DynamicForm getComboForm(String keyvalue) {
		DynamicForm form = new DynamicForm();
		form.setFields(getCombo(keyvalue));
		form.getCellFormatter().setWidth(0, 0, "136");
		return form;

	}

	public void setLossorGainAmount(double lossorGainAmount) {
		this.lossorGainAmount = lossorGainAmount;
	}

	public double getLossorGainAmount() {
		return lossorGainAmount;
	}

	public void setTotalCapitalGainAmount(double totalCapitalGainAmount) {
		this.totalCapitalGainAmount = totalCapitalGainAmount;
	}

	public double getTotalCapitalGainAmount() {
		return totalCapitalGainAmount;
	}

	@Override
	protected boolean onOK() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
