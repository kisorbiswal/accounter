package com.vimukti.accounter.web.client.ui;

import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.IGenericCallback;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

/**
 * 
 * @author venki.p
 * 
 */

public class ApplyCreditDialog extends BaseDialog {

	private static final String ATTR_CREDIT_AMOUNT = Accounter.constants()
			.credit();

	private final String ATTR_DATE = Accounter.constants().date();

	private final String ATTR_MEMO = Accounter.constants().memo();

	private final String ATTR_BALANCE = Accounter.constants().balance();

	private final String ATTR_AMOUNT_TO_USE = Accounter.constants()
			.amountToUse();

	private final String ATTR_ID = Accounter.constants().id();
	AmountField amtDueText, totCredAmtText, cashDiscText, totBalText,
			adjPayText, totAmtUseText;
	DialogGrid grid;
	private String amountDue;
	private String cashDiscount;
	// private ListGridField dateField;
	// private ListGridField memoField;
	// private ListGridField creditAmountField;
	// private ListGridField balanceField;
	// private ListGridField amountToUseField;

	private List<ClientCreditsAndPayments> creditsAndPayments;

	private LinkedHashMap<String, List<ClientTransactionCreditsAndPayments>> creditsAndPaymentsMap;

	private int key;
	private IGenericCallback<String> callback;

	public ApplyCreditDialog() {
		super(Accounter.constants().applyCreditsandPayments(), "");
		createControls();
		center();
	}

	public ApplyCreditDialog(
			String amountDue,
			String cashDiscount,
			List<ClientCreditsAndPayments> creditsAndPayments,
			int key,
			LinkedHashMap<String, List<ClientTransactionCreditsAndPayments>> creditsAndPaymentsMap,
			IGenericCallback<String> callback) {
		super(Accounter.constants().applyCreditsandPayments(), "");
		this.key = key;
		this.creditsAndPaymentsMap = creditsAndPaymentsMap;
		this.amountDue = amountDue;
		this.cashDiscount = cashDiscount;
		this.creditsAndPayments = creditsAndPayments;
		this.callback = callback;
		createControls();

	}

	private void createControls() {
		Label lab1 = new Label(Accounter.constants().applyCreditsandPayments());
		lab1.setWidth("100%");
		// lab1.setAutoHeight();

		amtDueText = new AmountField(Accounter.constants().amountDue(), this);
		amtDueText.setColSpan(1);
		amtDueText.setValue(amountDue);
		amtDueText.setDisabled(true);

		totCredAmtText = new AmountField(Accounter.constants()
				.totalCreditAmount(), this);
		totCredAmtText.setColSpan(1);
		totCredAmtText.setDisabled(true);

		cashDiscText = new AmountField(Accounter.constants().cashDiscount(),
				this);
		cashDiscText.setColSpan(1);
		cashDiscText.setValue(cashDiscount);
		cashDiscText.setDisabled(true);

		totBalText = new AmountField(Accounter.constants().totalBalance(), this);
		totBalText.setColSpan(1);
		totBalText.setDisabled(true);

		adjPayText = new AmountField(Accounter.constants().adjustedPayment(),
				this);
		adjPayText.setColSpan(1);
		adjPayText.setDisabled(true);
		adjPayText.setValue(amountAsString(DataUtils
				.getBalance(amountDue)
				- DataUtils.getBalance(cashDiscount)));

		totAmtUseText = new AmountField(Accounter.constants()
				.totalAmountToUse(), this);
		totAmtUseText.setColSpan(1);
		totAmtUseText.setDisabled(true);
		totAmtUseText.setValue("" + UIUtils.getCurrencySymbol() + "0.00");

		DynamicForm form = new DynamicForm();
		form.setNumCols(4);
		// form.setWrapItemTitles(false);
		form.setWidth("100%");
		form.setFields(amtDueText, totCredAmtText, cashDiscText, totBalText,
				adjPayText, totAmtUseText);

		grid = new DialogGrid(false);
		grid.setWidth("100%");
		// grid.setSelectionType(SelectionStyle.SIMPLE);
		// grid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		// grid.setEditByCell(true);

		// ListGridField dateField = new ListGridField(ATTR_DATE, "Date");
		// dateField.setCanEdit(false);
		// ListGridField memoField = new ListGridField(ATTR_MEMO, "Memo");
		// memoField.setCanEdit(false);
		// ListGridField creditAmountField = new
		// ListGridField(ATTR_CREDIT_AMOUNT,
		// "Credit Amount");
		// creditAmountField.setCanEdit(false);
		// ListGridField balanceField = new ListGridField(ATTR_BALANCE,
		// "Balance");
		// balanceField.setCanEdit(false);
		// amountToUseField = new ListGridField(ATTR_AMOUNT_TO_USE,
		// "Amount to use");
		// amountToUseField.addEditorExitHandler(new EditorExitHandler() {
		//
		// public void onEditorExit(EditorExitEvent event) {
		// ListGridRecord rec = (ListGridRecord) event.getRecord();
		// double pastValue = DataUtils.getBalance(
		// rec.getAttribute(ATTR_AMOUNT_TO_USE)).doubleValue();
		// double newEnteredValue = DataUtils.getBalance(
		// event.getNewValue().toString()).doubleValue();
		// double remainedBalance = pastValue - newEnteredValue;
		// if (pastValue >= newEnteredValue) {
		// rec.setAttribute(ATTR_BALANCE, DataUtils
		// .getAmountAsString(new Double(remainedBalance)));
		// double pastTotalAmtUseValue = DataUtils.getBalance(
		// totAmtUseText.getValue().toString()).doubleValue();
		// double toBeSetTotalAmtUseValue = pastTotalAmtUseValue
		// - pastValue + newEnteredValue;
		// totAmtUseText.setValue(DataUtils
		// .getAmountAsString(new Double(
		// toBeSetTotalAmtUseValue)));
		// totBalText.setValue(DataUtils.getAmountAsString(DataUtils
		// .getBalance(totCredAmtText.getValue().toString())
		// - DataUtils.getBalance(totAmtUseText.getValue()
		// .toString())));
		// } else {
		// newEnteredValue = pastValue;
		// }
		// rec.setAttribute(ATTR_AMOUNT_TO_USE, DataUtils
		// .getAmountAsString(new Double(newEnteredValue)));
		// grid.refreshRow(grid.getRecordIndex(rec));
		// }
		//
		// });
		//
		// grid.setFields(dateField, memoField, creditAmountField, balanceField,
		// amountToUseField);
		// addGridRecordsToListGrid();
		//
		// grid.addSelectionChangedHandler(new SelectionChangedHandler() {
		//
		// public void onSelectionChanged(SelectionEvent event) {
		// ListGridRecord selectedRecords[] = grid.getSelection();
		// ListGridRecord allRecords[] = grid.getRecords();
		//
		// double totalAmountToUse = 0.0;
		// for (ListGridRecord allRecord : allRecords) {
		// boolean flag = true;
		// for (ListGridRecord rec : selectedRecords) {
		// if (allRecord.equals(rec)) {
		// System.out.println("No's are"
		// + grid.getRecordIndex(rec));
		// amountToUseField.setCanEdit(true);
		// if (rec.getAttribute(ATTR_AMOUNT_TO_USE).equals(
		// "$0.00")) {
		// rec.setAttribute(ATTR_AMOUNT_TO_USE, rec
		// .getAttribute(ATTR_BALANCE));
		// rec.setAttribute(ATTR_BALANCE, "$0.00");
		// }
		// grid.refreshRow(grid.getRecordIndex(rec));
		// totalAmountToUse += DataUtils.getBalance(
		// rec.getAttribute(ATTR_AMOUNT_TO_USE))
		// .doubleValue();
		// flag = false;
		// }
		// }
		// if (flag) {
		// double fromAmountTouse = DataUtils.getBalance(
		// allRecord.getAttribute(ATTR_AMOUNT_TO_USE))
		// .doubleValue();
		// double toBesetBalance = DataUtils.getBalance(
		// allRecord.getAttribute(ATTR_BALANCE))
		// .doubleValue()
		// + fromAmountTouse;
		// allRecord.setAttribute(ATTR_BALANCE, DataUtils
		// .getAmountAsString(new Double(toBesetBalance)));
		// allRecord.setAttribute(ATTR_AMOUNT_TO_USE, "$0.00");
		// }
		// grid.refreshRow(grid.getRecordIndex(allRecord));
		//
		// }
		//
		// totAmtUseText.setValue(DataUtils
		// .getAmountAsString(totalAmountToUse));
		// totBalText.setValue(DataUtils.getAmountAsString(DataUtils
		// .getBalance(totCredAmtText.getValue().toString())
		// - DataUtils.getBalance(totAmtUseText.getValue()
		// .toString())));
		// }
		//
		// });
		// okbtn.setAutoFit(true);
		okbtn.setTitle(Accounter.constants().adjust());
		// addInputDialogHandler(new InputDialogHandler() {
		//
		// public void onCancel() {
		//
		// }
		//
		// public boolean onOK() {
		//
		// ClientCreditsAndPayments selectedRecords = (ClientCreditsAndPayments)
		// grid
		// .getSelection();
		//
		// List<ClientTransactionCreditsAndPayments> creditsAndPaymentsSet = new
		// ArrayList<ClientTransactionCreditsAndPayments>();
		// // for (ListGridRecord rec : selectedRecords) {
		// // ClientTransactionCreditsAndPayments customerPaymentCredits11
		// // = new ClientTransactionCreditsAndPayments();
		// // customerPaymentCredits11.setMemo(rec
		// // .getAttribute(ATTR_MEMO));
		// //
		// // customerPaymentCredits11.setAmountToUse(DataUtils
		// // .getBalance(rec.getAttribute(ATTR_AMOUNT_TO_USE))
		// // .doubleValue());
		// // System.out.println("Getting ID is "
		// // + rec.getAttribute(ATTR_ID));
		// // long key = Long.parseLong(rec.getAttribute(ATTR_ID));
		// // for (ClientCreditsAndPayments tempcrditandpayment :
		// // creditsAndPayments) {
		// // if (tempcrditandpayment.getID() == key) {
		// // customerPaymentCredits11
		// // .setCreditsAndPayments(tempcrditandpayment);
		// // break;
		// // }
		// // }
		// // creditsAndPaymentsSet.add(customerPaymentCredits11);
		// // }
		// // creditsAndPaymentsMap.put(key + "", creditsAndPaymentsSet);?
		// callback.called(totAmtUseText.getValue().toString());
		// return true;
		// }
		//
		// });

		// Button helpButt = new Button("Help");
		// helpButt.setAutoFit(true);
		// Button okButt = new Button("Adjust");

		// Button canButt = new Button("Cancel");
		// canButt.setAutoFit(true);
		//
		// HorizontalPanel helpHLay = new HorizontalPanel();
		// helpHLay.setWidth("50%");
		// helpHLay.add(helpButt);
		//
		// HorizontalPanel buttHLay = new HorizontalPanel();
		// buttHLay.setWidth("100%");
		// buttHLay.setMembersMargin(20);
		// buttHLay.setAutoHeight();
		// buttHLay.setLayoutBottomMargin(10);
		// buttHLay.setLayoutTopMargin(35);
		// buttHLay.setLayoutLeftMargin(5);
		// buttHLay.setLayoutRightMargin(5);
		// buttHLay.setMembers(helpHLay, okButt, canButt);
		headerLayout.setHeight("50px");
		VerticalPanel mainVLay = new VerticalPanel();
		// mainVLay.setTop(30);
		mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(form);
		mainVLay.add(grid);

		setBodyLayout(mainVLay);

		setSize("600px", "500px");
		show();
	}

	protected void setGridFields() {
		grid.addColumn(ListGrid.COLUMN_TYPE_CHECK, "");
		grid.addColumns(new String[] { Accounter.constants().date(),
				Accounter.constants().memo(),
				Accounter.constants().creditAmount(),
				Accounter.constants().balance(),
				Accounter.constants().amountToUse() });
	}

	@Override
	public Object getGridColumnValue(IsSerializable accounterCore, int col) {

		ClientCustomer customer = (ClientCustomer) accounterCore;
		switch (col) {
		case 0:
			return "";
		case 1:
			return null;
		case 2:
			return null;
		case 3:
			return null;
		case 4:
			return null;
		case 5:
			return null;

		default:
			break;
		}
		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result){

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(AccounterException exception) {

	}


	@Override
	protected boolean onOK() {
		callback.called(totAmtUseText.getValue().toString());
		return true;
	}

}
