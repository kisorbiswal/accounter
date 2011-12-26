package com.vimukti.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.core.ClientTDSTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.core.IntegerRangeValidator;
import com.vimukti.accounter.web.client.ui.edittable.tables.TdsChalanTransactionItemsTable;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

public class TDSChalanDetailsView extends BaseView<ClientTDSChalanDetail> {

	private IntegerRangeValidator integerRangeValidator;
	private AmountField incomeTaxAmount;
	private AmountField surchargePaidAmount;
	private AmountField eduCessAmount;
	private AmountField interestPaidAmount;
	private AmountField penaltyPaidAmount;
	private AmountField otherAmountPaid;
	private AmountField totalAmountPaid;
	private SelectCombo natureOfPaymentCombo26Q;
	private SelectCombo modeOFPaymentCombo;
	private IntegerField checkNumber;
	private DateField dateItem2;
	private IntegerField bankBsrCode;
	private DynamicForm taxDynamicForm;
	private DynamicForm otherDynamicForm;
	// private TDSTransactionItemGrid grid;
	TdsChalanTransactionItemsTable table;
	private SelectCombo chalanPeriod;
	private IntegerField bankChalanNumber;
	private SelectCombo tdsDepositedBY;
	private SelectCombo selectFormTypeCombo;
	private SelectCombo slectAssecementYear;
	protected ArrayList<ClientTDSTransactionItem> items;

	public static final int Form26Q = 1;
	public static final int Form27Q = 2;
	public static final int Form27EQ = 3;

	int formTypeSeclected = 1;
	String assessmentYear;
	String paymentSectionSelected;
	private SelectCombo natureOfPaymentCombo27Q;
	private SelectCombo natureOfPaymentCombo27EQ;
	int modeOfPayment = 1;
	boolean bookEntry = false;
	private SelectCombo financialYearCombo;
	private DynamicForm belowForm2;
	private DynamicForm belowForm1;
	private PayFromAccountsCombo payFromAccCombo;
	protected ClientAccount selectedPayFromAccount;
	private AmountField endingBalanceText;
	protected boolean financialYearSelected = false;

	public TDSChalanDetailsView() {

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

		selectFormTypeCombo = new SelectCombo("Form Type");
		selectFormTypeCombo.setHelpInformation(true);
		selectFormTypeCombo.initCombo(getFormTypes());
		selectFormTypeCombo.setDisabled(isInViewMode());
		selectFormTypeCombo.setRequired(true);
		selectFormTypeCombo.setSelectedItem(0);
		selectFormTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						if (selectItem.equals(getFormTypes().get(0))) {
							formTypeSeclected = 1;
							natureOfPaymentCombo26Q.show();
							natureOfPaymentCombo27Q.hide();
							natureOfPaymentCombo27EQ.hide();

						} else if (selectItem.equals(getFormTypes().get(1))) {
							formTypeSeclected = 2;
							natureOfPaymentCombo26Q.hide();
							natureOfPaymentCombo27Q.show();
							natureOfPaymentCombo27EQ.hide();
						} else if (selectItem.equals(getFormTypes().get(2))) {
							formTypeSeclected = 3;
							natureOfPaymentCombo26Q.hide();
							natureOfPaymentCombo27Q.hide();
							natureOfPaymentCombo27EQ.show();
						}

					}
				});

		slectAssecementYear = new SelectCombo("Assessment year");
		slectAssecementYear.setHelpInformation(true);
		slectAssecementYear.initCombo(getFinancialYearList());
		slectAssecementYear.setDisabled(true);

		financialYearCombo = new SelectCombo("Financial Year");
		financialYearCombo.setHelpInformation(true);
		financialYearCombo.initCombo(getFinancialYearList());
		financialYearCombo.setSelectedItem(0);
		financialYearCombo.setDisabled(isInViewMode());
		financialYearCombo.setRequired(true);
		financialYearCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						slectAssecementYear.setSelected(getFinancialYearList()
								.get(financialYearCombo.getSelectedIndex() + 1));
						assessmentYear = slectAssecementYear.getSelectedValue();

						financialYearSelected = true;

					}
				});

		incomeTaxAmount = new AmountField("Income Tax", this, getBaseCurrency());
		incomeTaxAmount.setHelpInformation(true);
		incomeTaxAmount.setWidth(100);
		incomeTaxAmount.setValue("0.00");
		incomeTaxAmount.setDisabled(true);
		incomeTaxAmount.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				updateTotalTaxCollected();
			}
		});

		surchargePaidAmount = new AmountField("Surcharge Paid", this,
				getBaseCurrency());
		surchargePaidAmount.setHelpInformation(true);
		surchargePaidAmount.setWidth(100);
		surchargePaidAmount.setValue("0.00");
		surchargePaidAmount.setDisabled(true);
		surchargePaidAmount.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				updateTotalTaxCollected();
			}
		});

		eduCessAmount = new AmountField("Education Cess", this,
				getBaseCurrency());
		eduCessAmount.setHelpInformation(true);
		eduCessAmount.setWidth(100);
		eduCessAmount.setValue("0.00");
		eduCessAmount.setDisabled(true);
		eduCessAmount.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				updateTotalTaxCollected();
			}
		});

		interestPaidAmount = new AmountField("Interest Paid", this,
				getBaseCurrency());
		interestPaidAmount.setHelpInformation(true);
		interestPaidAmount.setWidth(100);
		interestPaidAmount.setValue("0.00");
		interestPaidAmount.setDisabled(isInViewMode());
		interestPaidAmount.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				updateTotalTaxCollected();
			}
		});

		penaltyPaidAmount = new AmountField("Penalty Paid", this,
				getBaseCurrency());
		penaltyPaidAmount.setHelpInformation(true);
		penaltyPaidAmount.setWidth(100);
		penaltyPaidAmount.setValue("0.00");
		penaltyPaidAmount.setDisabled(isInViewMode());
		penaltyPaidAmount.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				updateTotalTaxCollected();
			}
		});

		otherAmountPaid = new AmountField("Other Amount Paid", this,
				getBaseCurrency());
		otherAmountPaid.setHelpInformation(true);
		otherAmountPaid.setWidth(100);
		otherAmountPaid.setValue("0.00");
		otherAmountPaid.setDisabled(isInViewMode());
		otherAmountPaid.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				updateTotalTaxCollected();
			}
		});

		totalAmountPaid = new AmountField("Total Amount Paid", this,
				getBaseCurrency());
		totalAmountPaid.setHelpInformation(true);
		totalAmountPaid.setRequired(true);
		totalAmountPaid.setWidth(100);
		totalAmountPaid.setValue("0.00");
		totalAmountPaid.setDisabled(true);

		natureOfPaymentCombo26Q = new SelectCombo("Nature of Payment(26Q)");
		natureOfPaymentCombo26Q.setHelpInformation(true);
		natureOfPaymentCombo26Q.initCombo(get26QSectionsList());
		natureOfPaymentCombo26Q.setRequired(true);
		natureOfPaymentCombo26Q.setDisabled(isInViewMode());
		natureOfPaymentCombo26Q.setSelectedItem(0);
		natureOfPaymentCombo26Q
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						paymentSectionSelected = selectItem;
					}
				});

		natureOfPaymentCombo27Q = new SelectCombo("Nature of Payment(27Q)");
		natureOfPaymentCombo27Q.setHelpInformation(true);
		natureOfPaymentCombo27Q.initCombo(get27QSectionsList());
		natureOfPaymentCombo27Q.setRequired(true);
		natureOfPaymentCombo27Q.setDisabled(isInViewMode());
		natureOfPaymentCombo27Q.setSelectedItem(0);
		natureOfPaymentCombo27Q
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						paymentSectionSelected = selectItem;
					}
				});

		natureOfPaymentCombo27EQ = new SelectCombo("Nature of Payment(27EQ)");
		natureOfPaymentCombo27EQ.setHelpInformation(true);
		natureOfPaymentCombo27EQ.initCombo(get27EQSectionsList());
		natureOfPaymentCombo27EQ.setRequired(true);
		natureOfPaymentCombo27EQ.setDisabled(isInViewMode());
		natureOfPaymentCombo27EQ.setSelectedItem(0);
		natureOfPaymentCombo27EQ
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						paymentSectionSelected = selectItem;
					}
				});

		modeOFPaymentCombo = new SelectCombo("Payment Method");
		modeOFPaymentCombo.setHelpInformation(true);
		modeOFPaymentCombo.initCombo(getPaymentItems());
		modeOFPaymentCombo.setDisabled(isInViewMode());
		modeOFPaymentCombo.setSelectedItem(0);
		modeOFPaymentCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						if (selectItem.equals(getPaymentItems().get(0))) {
							modeOfPayment = 1;
							checkNumber.show();

						} else if (selectItem.equals(getPaymentItems().get(1))) {
							modeOfPayment = 2;
							checkNumber.hide();
						}
					}
				});

		bankChalanNumber = new IntegerField(this, "Chalan Serial No.");
		bankChalanNumber.setHelpInformation(true);
		bankChalanNumber.setRequired(true);
		bankChalanNumber.setWidth(100);
		bankChalanNumber.setDisabled(isInViewMode());
		bankChalanNumber.setValidators(integerRangeValidator);

		chalanPeriod = new SelectCombo("Chalan Period");
		chalanPeriod.setHelpInformation(true);
		chalanPeriod.initCombo(getFinancialQuatersList());
		chalanPeriod.setRequired(true);
		chalanPeriod.setSelectedItem(0);
		chalanPeriod.setDisabled(isInViewMode());
		chalanPeriod.setPopupWidth("500px");
		chalanPeriod
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						initRPCService();
					}
				});

		checkNumber = new IntegerField(this, "Cheque/Ref. No.");
		checkNumber.setHelpInformation(true);
		checkNumber.setWidth(100);
		checkNumber.setDisabled(isInViewMode());
		checkNumber.setValidators(integerRangeValidator);

		tdsDepositedBY = new SelectCombo("TDS Deposited by book entry");
		tdsDepositedBY.setHelpInformation(true);
		tdsDepositedBY.initCombo(getYESNOList());
		tdsDepositedBY.setRequired(true);
		tdsDepositedBY.setDisabled(isInViewMode());
		tdsDepositedBY.setSelectedItem(1);
		tdsDepositedBY
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {

						if (selectItem.equals(getYESNOList().get(0))) {
							bookEntry = true;

						} else if (selectItem.equals(getYESNOList().get(1))) {
							bookEntry = false;
						}
					}
				});

		dateItem2 = new DateField(messages.date());
		dateItem2.setToolTip(messages.selectDateUntilDue(this.getAction()
				.getViewName()));
		dateItem2.setHelpInformation(true);
		dateItem2.setColSpan(1);
		dateItem2.setEnteredDate(new ClientFinanceDate());
		dateItem2.setTitle("Date on Tax Paid");
		dateItem2.setDisabled(isInViewMode());

		bankBsrCode = new IntegerField(this, "Bank BSR Code");
		bankBsrCode.setHelpInformation(true);
		bankBsrCode.setDisabled(isInViewMode());
		bankBsrCode.setRequired(true);
		bankBsrCode.setWidth(100);
		bankBsrCode.setValidators(integerRangeValidator);

		payFromAccCombo = new PayFromAccountsCombo(messages.payFrom());
		payFromAccCombo.setHelpInformation(true);
		payFromAccCombo.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		payFromAccCombo.setRequired(true);
		payFromAccCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedPayFromAccount = selectItem;
						endingBalanceText.setAmount(selectedPayFromAccount
								.getTotalBalanceInAccountCurrency());
					}

				});

		payFromAccCombo.setDisabled(isInViewMode());
		payFromAccCombo.setPopupWidth("500px");

		endingBalanceText = new AmountField(messages.bankBalance(), this,
				getBaseCurrency());
		endingBalanceText.setHelpInformation(true);
		endingBalanceText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		endingBalanceText.setDisabled(true);

		taxDynamicForm = new DynamicForm();
		taxDynamicForm.setFields(selectFormTypeCombo, financialYearCombo,
				bankChalanNumber, chalanPeriod, modeOFPaymentCombo,
				tdsDepositedBY);

		otherDynamicForm = new DynamicForm();
		otherDynamicForm.setFields(natureOfPaymentCombo26Q,
				natureOfPaymentCombo27Q, natureOfPaymentCombo27EQ,
				slectAssecementYear, checkNumber, dateItem2, bankBsrCode,
				payFromAccCombo, endingBalanceText);

		belowForm1 = new DynamicForm();
		belowForm1.setFields(incomeTaxAmount, surchargePaidAmount,
				eduCessAmount, totalAmountPaid);

		belowForm2 = new DynamicForm();
		belowForm2.setFields(interestPaidAmount, penaltyPaidAmount,
				otherAmountPaid);

		// grid = new TDSTransactionItemGrid(this);
		// grid.setCanEdit(true);
		// grid.init();

		table = new TdsChalanTransactionItemsTable(this);

		HorizontalPanel horizontalPanel1 = new HorizontalPanel();
		horizontalPanel1.setWidth("100%");
		horizontalPanel1.add(taxDynamicForm);
		horizontalPanel1.add(otherDynamicForm);

		HorizontalPanel horizontalPanel2 = new HorizontalPanel();
		horizontalPanel2.setWidth("100%");
		horizontalPanel2.add(belowForm1);
		horizontalPanel2.add(belowForm2);

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setWidth("100%");
		verticalPanel.add(horizontalPanel1);
		verticalPanel.add(table);
		verticalPanel.add(horizontalPanel2);

		this.add(verticalPanel);
		this.setCellHorizontalAlignment(verticalPanel, ALIGN_LEFT);

		natureOfPaymentCombo27Q.hide();
		natureOfPaymentCombo27EQ.hide();

	}

	protected void updateTotalTaxCollected() {
		totalAmountPaid.setAmount(0.00);
		totalAmountPaid.setAmount(incomeTaxAmount.getAmount()
				+ surchargePaidAmount.getAmount() + eduCessAmount.getAmount()
				+ interestPaidAmount.getAmount()
				+ penaltyPaidAmount.getAmount() + otherAmountPaid.getAmount());

	}

	protected void changeTotalAmount() {
		totalAmountPaid.setAmount(0.00);
		totalAmountPaid.setAmount(incomeTaxAmount.getAmount()
				+ surchargePaidAmount.getAmount() + eduCessAmount.getAmount()
				+ interestPaidAmount.getAmount()
				+ penaltyPaidAmount.getAmount() + otherAmountPaid.getAmount());

	}

	private List<String> getFormTypes() {
		ArrayList<String> list = new ArrayList<String>();

		list.add("26Q");
		list.add("27Q");
		list.add("27EQ");

		return list;
	}

	private List<String> get26QSectionsList() {

		ArrayList<String> list = new ArrayList<String>();

		list.add("193" + "-10%");
		list.add("194" + "-10%");
		list.add("194A" + "-10%");
		list.add("194B" + "-30%");
		list.add("194BB" + "-30%");
		list.add("194C" + "-0%");
		list.add("194C" + "-1%");
		list.add("194C" + "-2%");
		list.add("194D" + "-10%");
		list.add("194EE" + "-20%");
		list.add("194F" + "-20%");
		list.add("194G" + "-10%");
		list.add("194H" + "-10%");
		list.add("194I" + "-2%");
		list.add("194I" + "-10%");
		list.add("194J" + "-10%");
		list.add("194LA" + "-10%");

		return list;
	}

	private List<String> get27QSectionsList() {

		ArrayList<String> list = new ArrayList<String>();
		list.add("194E" + "-" + "10%");
		list.add("195(a)" + "-" + "20%");
		list.add("195(b)" + "-" + "10%");
		list.add("195(c)" + "-" + "15%");
		list.add("195(d)" + "-" + "20%");
		list.add("195(e)" + "-" + "20%");
		list.add("195(f)" + "-" + "30%");
		list.add("195(f)" + "-" + "20%");
		list.add("195(f)" + "-" + "10%");
		list.add("195(g)" + "-" + "50%");
		list.add("195(g)" + "-" + "30%");
		list.add("195(g)" + "-" + "20%");
		list.add("195(g)" + "-" + "10%");
		list.add("195(h)" + "-" + "50%");
		list.add("195(h)" + "-" + "30%");
		list.add("195(h)" + "-" + "20%");
		list.add("195(h)" + "-" + "10%");
		list.add("195(i)" + "-" + "40%");
		list.add("195(i)" + "-" + "30%");
		list.add("196A" + "-" + "20%");
		list.add("196B" + "-" + "10%");
		list.add("196C" + "-" + "10%");
		list.add("196D" + "-" + "20%");

		return list;
	}

	private List<String> get27EQSectionsList() {

		ArrayList<String> list = new ArrayList<String>();
		list.add("6CA" + "-" + "1%");
		list.add("6CB" + "-" + "2.5%");
		list.add("6CC" + "-" + "2.5%");
		list.add("6CD" + "-" + "2.5%");
		list.add("6CE" + "-" + "1%");
		list.add("6CF" + "-" + "2%");
		list.add("6CG" + "-" + "2%");
		list.add("6CH" + "-" + "2%");
		list.add("6CI" + "-" + "5%");

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

		List<ClientTDSTransactionItem> records = table.getAllRows();
		double totalTDS = 0;
		for (ClientTDSTransactionItem clientTDSTransactionItem : records) {
			totalTDS = clientTDSTransactionItem.getTdsTotal() + totalTDS;
		}

		double value = incomeTaxAmount.getAmount()
				+ surchargePaidAmount.getAmount() + eduCessAmount.getAmount()
				+ interestPaidAmount.getAmount()
				+ penaltyPaidAmount.getAmount() + otherAmountPaid.getAmount();

		if (totalTDS != value) {
			result.addError(totalAmountPaid, "TDS Amount not matched");
		}

		if (financialYearSelected == false) {
			result.addError(financialYearCombo,
					"Select the financial year for eTDS");
		}

		return result;

	}

	@Override
	public void saveAndUpdateView() {
		updateObject();
		saveOrUpdate(getData());

	}

	private void updateObject() {

		data.setFormType(formTypeSeclected);

		String delims = "-";
		String[] tokens = assessmentYear.split(delims);

		data.setAssesmentYearStart(Integer.parseInt(tokens[0]));
		data.setAssessmentYearEnd(Integer.parseInt(tokens[1]));

		data.setIncomeTaxAmount(incomeTaxAmount.getAmount());
		data.setSurchangePaidAmount(surchargePaidAmount.getAmount());
		data.setEducationCessAmount(eduCessAmount.getAmount());
		data.setInterestPaidAmount(interestPaidAmount.getAmount());
		data.setPenaltyPaidAmount(penaltyPaidAmount.getAmount());
		data.setOtherAmount(otherAmountPaid.getAmount());

		data.setPaymentSection(paymentSectionSelected);
		data.setPaymentMethod(modeOfPayment);

		data.setBankChalanNumber(bankChalanNumber.getNumber());
		data.setChalanPeriod(chalanPeriod.getSelectedIndex() + 1);

		if (checkNumber.getNumber() != null) {
			data.setCheckNumber(checkNumber.getNumber());
		} else {
			data.setCheckNumber(0);
		}
		data.setBookEntry(bookEntry);

		data.setDateTaxPaid(dateItem2.getTime());

		if (bankBsrCode.getNumber() != null) {
			data.setBankBsrCode(bankBsrCode.getNumber());
		} else {
			data.setBankBsrCode(0);
		}

		data.setTdsTransactionItems(table.getSelectedRecords(0));

	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);

		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);

		updateObject();

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
									exception.getStackTrace();

								}

								@Override
								public void onResultSuccess(
										ArrayList<ClientTDSTransactionItem> result) {
									if (result.size() > 0) {
										items = result;
										table.reDraw();
										for (ClientTDSTransactionItem clientTDSTransactionItem : result) {
											clientTDSTransactionItem.setTdsTotal(clientTDSTransactionItem
													.getTaxAmount()
													+ clientTDSTransactionItem
															.getSurchargeAmount()
													+ clientTDSTransactionItem
															.getEduCess());
											table.add(clientTDSTransactionItem);
										}
									} else {
										table.reDraw();
										table.addEmptyMessage(messages
												.noRecordsToShow());
									}

								}
							});
		}
	}

	public void setSurchargeValuesToField(Object value, boolean b) {

		if (b == true) {
			surchargePaidAmount.setAmount(surchargePaidAmount.getAmount()
					+ Double.parseDouble(value.toString()));
		} else {
			surchargePaidAmount.setAmount(surchargePaidAmount.getAmount()
					- Double.parseDouble(value.toString()));
		}
		updateTotalTaxCollected();
	}

	public void setEduCessValuesToField(Object value, boolean b) {
		if (b == true) {
			eduCessAmount.setAmount(eduCessAmount.getAmount()
					+ Double.parseDouble(value.toString()));
		} else {
			eduCessAmount.setAmount(eduCessAmount.getAmount()
					- Double.parseDouble(value.toString()));
		}
		updateTotalTaxCollected();
	}

	public void setTaxAmountValuesToField(double taxAmount, boolean b) {

		if (b == true) {
			incomeTaxAmount.setAmount(incomeTaxAmount.getAmount() + taxAmount);
		} else {
			incomeTaxAmount.setAmount(incomeTaxAmount.getAmount() - taxAmount);
		}
		updateTotalTaxCollected();
	}

}
