package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.core.ClientTDSTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.core.IntegerRangeValidator;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.TDSTransactionItemGrid;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class TDSChalanDetailsView extends BaseView<ClientTDSChalanDetail> {

	private IntegerRangeValidator integerRangeValidator;
	private AmountField incomeTaxAmount;
	private AmountField surchargePaidAmount;
	private AmountField eduCessAmount;
	private AmountField interestPaidAmount;
	private FormItem<String> penaltyPaidAmount;
	private TextItem otherAmountPaid;
	private TextItem totalAmountPaid;
	private SelectCombo natureOfPaymentCombo;
	private SelectCombo modeOFPaymentCombo;
	private IntegerField checkNumber;
	private DateField dateItem2;
	private SelectCombo bankCodeCombo;
	private DynamicForm taxDynamicForm;
	private DynamicForm otherDynamicForm;
	private TDSTransactionItemGrid grid;
	private SelectCombo chalanPeriod;
	private IntegerField bankChalanNumber;
	private SelectCombo tdsDepositedBY;

	// private IntegerField receiptNumber;

	public TDSChalanDetailsView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		super.init();
		createControls();
		setSize("100%", "100%");
	}

	public void initData() {
		super.initData();
		if (data == null) {
			ClientTDSChalanDetail tdsChalanDetailsData = new ClientTDSChalanDetail();
			setData(tdsChalanDetailsData);
		}

	}

	private void createControls() {

		integerRangeValidator = new IntegerRangeValidator();
		integerRangeValidator.setMin(0);

		incomeTaxAmount = new AmountField("Income Tax", this, getBaseCurrency());
		incomeTaxAmount.setHelpInformation(true);
		incomeTaxAmount.setWidth(100);
		incomeTaxAmount.setValue("0.00");
		incomeTaxAmount.setDisabled(isInViewMode());

		surchargePaidAmount = new AmountField("Surcharge Paid", this,
				getBaseCurrency());
		surchargePaidAmount.setHelpInformation(true);
		surchargePaidAmount.setWidth(100);
		surchargePaidAmount.setValue("0.00");
		surchargePaidAmount.setDisabled(isInViewMode());

		eduCessAmount = new AmountField("Education Cess", this,
				getBaseCurrency());
		eduCessAmount.setHelpInformation(true);
		eduCessAmount.setWidth(100);
		eduCessAmount.setValue("0.00");
		eduCessAmount.setDisabled(isInViewMode());

		interestPaidAmount = new AmountField("Interest Paid", this,
				getBaseCurrency());
		interestPaidAmount.setHelpInformation(true);
		interestPaidAmount.setWidth(100);
		interestPaidAmount.setValue("0.00");
		interestPaidAmount.setDisabled(isInViewMode());

		penaltyPaidAmount = new AmountField("Penalty Paid", this,
				getBaseCurrency());
		penaltyPaidAmount.setHelpInformation(true);
		penaltyPaidAmount.setWidth(100);
		penaltyPaidAmount.setValue("0.00");
		penaltyPaidAmount.setDisabled(isInViewMode());

		otherAmountPaid = new AmountField("Other Amount Paid", this,
				getBaseCurrency());
		otherAmountPaid.setHelpInformation(true);
		otherAmountPaid.setWidth(100);
		otherAmountPaid.setValue("0.00");
		otherAmountPaid.setDisabled(isInViewMode());

		totalAmountPaid = new AmountField("Total Amount Paid", this,
				getBaseCurrency());
		totalAmountPaid.setHelpInformation(true);
		totalAmountPaid.setRequired(true);
		totalAmountPaid.setWidth(100);
		totalAmountPaid.setValue("0.00");
		totalAmountPaid.setDisabled(isInViewMode());

		taxDynamicForm = new DynamicForm();
		taxDynamicForm.setFields(incomeTaxAmount, surchargePaidAmount,
				eduCessAmount, interestPaidAmount, penaltyPaidAmount,
				otherAmountPaid, totalAmountPaid);

		natureOfPaymentCombo = new SelectCombo("Nature of Payment");
		natureOfPaymentCombo.setHelpInformation(true);
		natureOfPaymentCombo.initCombo(getSectionsList());
		natureOfPaymentCombo.setRequired(true);
		natureOfPaymentCombo.setDisabled(isInViewMode());
		natureOfPaymentCombo.setPopupWidth("500px");

		modeOFPaymentCombo = new SelectCombo("Payment Method");
		modeOFPaymentCombo.setHelpInformation(true);
		modeOFPaymentCombo.initCombo(getPaymentItems());
		modeOFPaymentCombo.setDisabled(isInViewMode());
		modeOFPaymentCombo.setPopupWidth("500px");

		bankChalanNumber = new IntegerField(this, "Chalan Serial No.");
		bankChalanNumber.setHelpInformation(true);
		bankChalanNumber.setRequired(true);
		bankChalanNumber.setWidth(100);
		bankChalanNumber.setDisabled(isInViewMode());
		bankChalanNumber.setValidators(integerRangeValidator);

		chalanPeriod = new SelectCombo("Chalan Period");
		chalanPeriod.setHelpInformation(true);
		chalanPeriod.initCombo(getChalanPeriodList());
		chalanPeriod.setRequired(true);
		chalanPeriod.setDisabled(isInViewMode());
		chalanPeriod.setPopupWidth("500px");

		checkNumber = new IntegerField(this, "Cheque/Ref. No.");
		checkNumber.setHelpInformation(true);
		checkNumber.setWidth(100);
		checkNumber.setDisabled(isInViewMode());
		checkNumber.setValidators(integerRangeValidator);

		tdsDepositedBY = new SelectCombo("TDS Deposited by book entry");
		tdsDepositedBY.setHelpInformation(true);
		tdsDepositedBY.initCombo(getTDSList());
		tdsDepositedBY.setRequired(true);
		tdsDepositedBY.setDisabled(isInViewMode());
		tdsDepositedBY.setPopupWidth("500px");

		dateItem2 = new DateField(messages.date());
		dateItem2.setToolTip(messages.selectDateUntilDue(this.getAction()
				.getViewName()));
		dateItem2.setHelpInformation(true);
		dateItem2.setColSpan(1);
		dateItem2.setTitle("Date on Tax Paid");
		dateItem2.setDisabled(isInViewMode());

		bankCodeCombo = new SelectCombo("Bank BSR Code");
		bankCodeCombo.setHelpInformation(true);
		bankCodeCombo.setDisabled(isInViewMode());
		bankCodeCombo.setRequired(true);
		bankCodeCombo.setPopupWidth("500px");

		otherDynamicForm = new DynamicForm();
		otherDynamicForm.setFields(natureOfPaymentCombo, bankChalanNumber,
				chalanPeriod, checkNumber, dateItem2, bankCodeCombo,
				tdsDepositedBY, modeOFPaymentCombo);

		grid = new TDSTransactionItemGrid();
		grid.init();

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.add(taxDynamicForm);
		horizontalPanel.add(otherDynamicForm);

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(horizontalPanel);
		verticalPanel.add(grid);

		this.add(verticalPanel);

	}

	private List<String> getTDSList() {
		ArrayList<String> list = new ArrayList<String>();

		list.add("YES");
		list.add("NO");
		return list;
	}

	private List<String> getSectionsList() {

		ArrayList<String> list = new ArrayList<String>();

		list.add("193" + " Interest on Debentures & Securities" + " - 10%");
		list.add("194" + "  Deemed Dividend" + " - 10%");
		list.add("194A"
				+ " Aggregate sum exceeding Rs. 10,000 for Banking Co's , etc.per person during the financial year."
				+ " - 10%");
		list.add("194B" + "  Lottery/Crossword Puzzle > Rs.10,000" + " - 30%");
		list.add("194BB" + " Winnings from Horse Race > Rs. 5,000" + " - 30%");
		list.add("194C"
				+ "  Contracts to Transporter, who has provided a valid PAN"
				+ " - 0%");
		list.add("194C" + " Contracts to Individuals/HUF" + " - 1%");
		list.add("194C" + " Contracts to others" + " - 2%");
		list.add("194D" + "  Insurance Commission > Rs. 20,000" + " - 10%");
		list.add("194EE" + "  Withdrawl from NSS > Rs.2,500" + " - 20%");
		list.add("194F" + "  Repurchase of Units by MF/UTI" + " - 20%");
		list.add("194G" + "  Commission on Sale of Lottery Tickets > Rs.1,000"
				+ " - 10%");
		list.add("194H" + "  Commission or Brokerage > Rs.5,000" + " - 10%");
		list.add("194I"
				+ "  Rent > Rs.1,80,000 p. a. / Rent of Plant & Machinery"
				+ " - 2%");
		list.add("194I" + " Rent of Land, Building, Furniture, etc" + " - 10%");
		list.add("194J" + "  Professional or Technical Fess > Rs.30,000"
				+ " - 10%");
		list.add("194LA"
				+ " Compensation on Compulsory Acquisition of immovable property >Rs.1,00,000 during the financial year"
				+ " - 10%");

		return list;
	}

	private List<String> getChalanPeriodList() {
		ArrayList<String> list = new ArrayList<String>();

		list.add("Q1" + " " + DayAndMonthUtil.jan() + " - " + DayAndMonthUtil.mar());
		list.add("Q2" + " " + DayAndMonthUtil.apr() + " - " + DayAndMonthUtil.jun());
		list.add("Q3" + " " + DayAndMonthUtil.jul() + " - " + DayAndMonthUtil.sep());
		list.add("Q4" + " " + DayAndMonthUtil.oct() + " - " + DayAndMonthUtil.dec());

		return list;
	}

	private List<String> getPaymentItems() {
		ArrayList<String> list = new ArrayList<String>();

		list.add("Cheque");
		list.add("Cash/ePayment");
		return list;
	}

	@Override
	public boolean isInViewMode() {
		// TODO Auto-generated method stub
		return super.isInViewMode();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return "Chalan Details";
	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		result.add(taxDynamicForm.validate());
		result.add(otherDynamicForm.validate());

		return result;

	}

	@Override
	public void saveAndUpdateView() {
		updateObject();
		saveOrUpdate(getData());

	}

	private void updateObject() {

	}

	@Override
	protected void initRPCService() {
		super.initRPCService();

		if (chalanPeriod != null) {
			int chalanPer = chalanPeriod.getSelectedIndex();

			Accounter
					.createHomeService()
					.getTDSTransactionItemsList(
							chalanPer,
							new AccounterAsyncCallback<ArrayList<ClientTDSTransactionItem>>() {

								@Override
								public void onException(
										AccounterException exception) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onResultSuccess(
										ArrayList<ClientTDSTransactionItem> result) {
									// TODO Auto-generated method stub

								}
							});
		}
	}

}
