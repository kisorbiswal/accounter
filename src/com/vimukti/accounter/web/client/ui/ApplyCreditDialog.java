package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.IGenericCallback;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

/**
 * 
 * @author venki.p
 * 
 */

@SuppressWarnings("unchecked")
public class ApplyCreditDialog extends BaseDialog {
	@SuppressWarnings("unused")
	private static final String ATTR_CREDIT_AMOUNT = Accounter
			.messages().credit();
	@SuppressWarnings("unused")
	private final String ATTR_DATE = Accounter.messages()
			.date();
	@SuppressWarnings("unused")
	private final String ATTR_MEMO = Accounter.messages()
			.memo();
	@SuppressWarnings("unused")
	private final String ATTR_BALANCE = Accounter.messages()
			.balance();
	@SuppressWarnings("unused")
	private final String ATTR_AMOUNT_TO_USE = Accounter
			.messages().amounttouse();
	@SuppressWarnings("unused")
	private final String ATTR_ID = Accounter.messages().Id();
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
	@SuppressWarnings("unused")
	private List<ClientCreditsAndPayments> creditsAndPayments;
	@SuppressWarnings("unused")
	private LinkedHashMap<String, List<ClientTransactionCreditsAndPayments>> creditsAndPaymentsMap;
	@SuppressWarnings("unused")
	private int key;
	private IGenericCallback<String> callback;

	public ApplyCreditDialog() {
		super(
				Accounter.messages()
						.applyCreditsandPayments(), "");
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
		super(
				Accounter.messages()
						.applyCreditsandPayments(), "");
		this.key = key;
		this.creditsAndPaymentsMap = creditsAndPaymentsMap;
		this.amountDue = amountDue;
		this.cashDiscount = cashDiscount;
		this.creditsAndPayments = creditsAndPayments;
		this.callback = callback;
		createControls();

	}

	private void createControls() {
		Label lab1 = new Label(Accounter.constants()
				.applyCreditAndPayments());
		lab1.setWidth("100%");
		// lab1.setAutoHeight();

		amtDueText = new AmountField(Accounter.constants()
				.amtDue());
		amtDueText.setColSpan(1);
		amtDueText.setValue(amountDue);
		amtDueText.setDisabled(true);

		totCredAmtText = new AmountField(Accounter
				.constants().totalCreditAmount());
		totCredAmtText.setColSpan(1);
		totCredAmtText.setDisabled(true);

		cashDiscText = new AmountField(Accounter
				.constants().cashDiscount());
		cashDiscText.setColSpan(1);
		cashDiscText.setValue(cashDiscount);
		cashDiscText.setDisabled(true);

		totBalText = new AmountField(Accounter.constants()
				.totalBal());
		totBalText.setColSpan(1);
		totBalText.setDisabled(true);

		adjPayText = new AmountField(Accounter.constants()
				.adjustedPayment());
		adjPayText.setColSpan(1);
		adjPayText.setDisabled(true);
		adjPayText.setValue(DataUtils.getAmountAsString(DataUtils
				.getBalance(amountDue)
				- DataUtils.getBalance(cashDiscount)));

		totAmtUseText = new AmountField(Accounter
				.constants().totalAmountToUse());
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
		okbtn.setTitle("Adjust");
		addInputDialogHandler(new InputDialogHandler() {

			public void onCancelClick() {

			}

			public boolean onOkClick() {
				@SuppressWarnings("unused")
				ClientCreditsAndPayments selectedRecords = (ClientCreditsAndPayments) grid
						.getSelection();
				@SuppressWarnings("unused")
				List<ClientTransactionCreditsAndPayments> creditsAndPaymentsSet = new ArrayList<ClientTransactionCreditsAndPayments>();
				// for (ListGridRecord rec : selectedRecords) {
				// ClientTransactionCreditsAndPayments customerPaymentCredits11
				// = new ClientTransactionCreditsAndPayments();
				// customerPaymentCredits11.setMemo(rec
				// .getAttribute(ATTR_MEMO));
				//
				// customerPaymentCredits11.setAmountToUse(DataUtils
				// .getBalance(rec.getAttribute(ATTR_AMOUNT_TO_USE))
				// .doubleValue());
				// System.out.println("Getting ID is "
				// + rec.getAttribute(ATTR_ID));
				// long key = Long.parseLong(rec.getAttribute(ATTR_ID));
				// for (ClientCreditsAndPayments tempcrditandpayment :
				// creditsAndPayments) {
				// if (tempcrditandpayment.getID() == key) {
				// customerPaymentCredits11
				// .setCreditsAndPayments(tempcrditandpayment);
				// break;
				// }
				// }
				// creditsAndPaymentsSet.add(customerPaymentCredits11);
				// }
				// creditsAndPaymentsMap.put(key + "", creditsAndPaymentsSet);?
				callback.called(totAmtUseText.getValue().toString());
				return true;
			}

		});

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

		setSize("600", "500");
		show();
	}

	protected void setGridFields() {
		grid.addColumn(ListGrid.COLUMN_TYPE_CHECK, "");
		grid.addColumns(new String[] { "Date",
				Accounter.constants().memo(),
				Accounter.constants().creditAmount(),
				Accounter.constants().balance(),
				Accounter.constants().amountToUse() });
	}

	@Override
	public Object getGridColumnValue(IsSerializable accounterCore, int col) {
		@SuppressWarnings("unused")
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
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(Throwable exception) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().applyCreditsandPayments();
	}

}
