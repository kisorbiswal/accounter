//package com.vimukti.accounter.web.client.ui.customers;
//
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.VerticalPanel;
//import com.vimukti.accounter.web.client.ui.AbstractBaseView;
//import com.vimukti.accounter.web.client.ui.ApplyCreditDialog;
//import com.vimukti.accounter.web.client.ui.CashDiscountDialog;
//import com.vimukti.accounter.web.client.ui.UIUtils;
//import com.vimukti.accounter.web.client.ui.forms.DateItem;
//import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
//import com.vimukti.accounter.web.client.ui.forms.TextItem;
//import com.vimukti.accounter.web.client.ui.grids.ListGrid;
//
//public class CustomerPartialPaymentView extends AbstractBaseView {
//	CustomersMessages messages = GWT.create(CustomersMessages.class);
//
//	public CustomerPartialPaymentView() {
//		createControls();
//	}
//
//	private void createControls() {
//		setTitle("[...]");
//		Label lab1 = new Label(
//				"<div style='font-size: 20px;'>Customer Payment (Partially Unapplied)</div>");
//		// lab1.setWrap(false);
//		// lab1.setAutoFit(true);
//
//		DateItem date = new DateItem();
//		date.setUseTextField(true);
//		date.setWidth(100);
//		date.setShowTitle(false);
//		date.setColSpan(2);
//		TextItem nText = new TextItem();
//		nText.setTitle(messages.no());
//		nText.setWidth(100);
//		nText.setColSpan(1);
//		DynamicForm dateNoForm = new DynamicForm();
//		dateNoForm.setNumCols(4);
//		dateNoForm.setWidth("*");
//		// dateNoForm.setLayoutAlign(Alignment.RIGHT);
//		dateNoForm.setFields(date, nText);
//
//		TextItem recvFrmText = new TextItem();
//		recvFrmText.setTitle(messages.receivedFrom());
//		recvFrmText.setDisabled(true);
//		// recvFrmText.setWidth("*");
//
//		TextItem amtText = new TextItem();
//		amtText.setTitle(messages.amount());
//		amtText.setDisabled(true);
//		// amtText.setWidth("*");
//
//		TextItem payMethText = new TextItem();
//		payMethText.setTitle(messages.paymentMethod());
//		payMethText.setDisabled(true);
//		// payMethText.setWidth("*");
//
//		TextItem refText = new TextItem();
//		refText.setTitle(messages.reference());
//		// refText.setWidth("*");
//
//		TextItem memoText = new TextItem();
//		memoText.setTitle(messages.memo());
//		// memoText.setWidth("*");
//
//		DynamicForm payForm = UIUtils.form(messages.payment());
//		;
//		payForm.setWidth("50%");
//		// payForm.setAutoHeight();
//
//		payForm.setFields(recvFrmText, amtText, payMethText, refText, memoText);
//
//		TextItem depoText = new TextItem();
//		depoText.setTitle(messages.depositIn());
//
//		depoText.setDisabled(true);
//		// depoText.setWidth("*");
//
//		DynamicForm depoForm = UIUtils.form(messages.deposit());
//		depoForm.setWidth("*");
//		// depoForm.setAutoHeight();
//
//		depoForm.setFields(depoText);
//
//		Label lab2 = new Label(messages.dueForPayment());
//		// lab2.setAutoFit(true);
//		// lab2.setWrap(!true);
//
//		final ListGrid grid = new ListGrid();
//		grid.addCellClickHandler(new CellClickHandler() {
//			public void onCellClick(CellClickEvent event) {
//
//				// Record record = (Record) event.getRecord();
//				int colNum = event.getColNum();
//				// ListGridField field = grid.getField(colNum);
//				String fieldName = grid.getFieldName(colNum);
//				// String fieldTitle = field.getTitle();
//				// Accounter.showError("name: " + fieldName + "<br>title: " +
//				// fieldTitle);
//				if (fieldName.equals("cash_disc")) {
//					new CashDiscountDialog();
//				} else if (fieldName.equals("write_off")) {
//					new WriteOffDialog();
//				} else if (fieldName.equals("applied_credit")) {
//					new ApplyCreditDialog();
//				}
//			}
//		});
//
//		ListGridField field1 = new ListGridField("invoice", messages
//				.invoice());
//		ListGridField field2 = new ListGridField("invo_amt", messages
//				.invoiceAmount());
//		ListGridField field3 = new ListGridField("disc_date", messages
//				.discountDate());
//		ListGridField field4 = new ListGridField("cash_disc", messages
//				.cashDiscount());
//		ListGridField field5 = new ListGridField("write_off", messages
//				.writeOff());
//		ListGridField field6 = new ListGridField("applied_credit",
//				messages.appliedCredit());
//		ListGridField field7 = new ListGridField("payment", messages
//				.payment());
//		grid.setFields(field1, field2, field3, field4, field5, field6, field7);
//
//		grid.setData(createDummyRecords(gridRecords));
//
//		TextItem unuseCredText = new TextItem();
//		unuseCredText.setTitle(messages.unusedCredits());
//		unuseCredText.setDisabled(true);
//		unuseCredText.setColSpan(1);
//
//		TextItem unusePayText = new TextItem();
//		unusePayText.setTitle(messages.unusedPayments());
//		unusePayText.setDisabled(true);
//		unusePayText.setColSpan(1);
//
//		DynamicForm unuseForm = new DynamicForm();
//		unuseForm.setWidth("50%");
//		// unuseForm.setLayoutAlign(Alignment.RIGHT);
//		unuseForm.setNumCols(4);
//		unuseForm.setFields(unuseCredText, unusePayText);
//
//		Button saveCloseButt = new Button(messages.saveAndClose());
//		// saveCloseButt.setAutoFit(true);
//		// saveCloseButt.setLayoutAlign(Alignment.LEFT);
//
//		Button saveNewButt = new Button(messages.saveAndNew());
//		// saveNewButt.setAutoFit(true);
//		// saveNewButt.setLayoutAlign(Alignment.RIGHT);
//
//		HorizontalPanel buttHLay = new HorizontalPanel();
//		// buttHLay.setAlign(Alignment.RIGHT);
//		// buttHLay.setMembersMargin(20);
//		// buttHLay.setMargin(10);
//		// buttHLay.setAutoHeight();
//		buttHLay.add(saveCloseButt);
//		buttHLay.add(saveNewButt);
//
//		HorizontalPanel topHLay = new HorizontalPanel();
//		topHLay.setWidth("100%");
//		topHLay.add(payForm);
//		topHLay.add(depoForm);
//
//		VerticalPanel mainVLay = new VerticalPanel();
//		mainVLay.setSize("100%", "100%");
//		mainVLay.add(lab1);
//		mainVLay.add(dateNoForm);
//		mainVLay.add(topHLay);
//		mainVLay.add(lab2);
//		mainVLay.add(grid);
//		mainVLay.add(unuseForm);
//		mainVLay.add(buttHLay);
//
//		add(mainVLay);
//		setSize("100%", "100%");
//	}
//
//	private static final String[] gridRecords = new String[] {
//			"invoice;invo_amt;disc_date;cash_disc;write_off;applied_credit",
//			"1;+UIUtils.getCurrencySymbol() +"5;today;50%;ok;nil", "2;+UIUtils.getCurrencySymbol() +"6;tooday;51%;ook;nill",
//			"3;+UIUtils.getCurrencySymbol() +"7;toooday;53%;oook;nilll", };
//
//	public ListGridRecord[] createDummyRecords(String[] records) {
//		ListGridRecord[] result = new ListGridRecord[records.length - 1];
//		String[] fieldNames = records[0].split(";");
//		for (int recordIndex = 1; recordIndex < records.length; ++recordIndex) {
//			String[] fieldValues = records[recordIndex].split(";");
//			result[recordIndex - 1] = new ListGridRecord();
//			for (int fieldIndex = 0; fieldIndex < fieldValues.length; ++fieldIndex) {
//				result[recordIndex - 1].setAttribute(fieldNames[fieldIndex],
//						fieldValues[fieldIndex]);
//			}
//		}
//		return result;
//	} // createListGridRecords()
//
//	@Override
//	protected void initConstants() {
//
//	}
//
//	@Override
//	public void init() {
//
//	}
//
//	@Override
//	public void initData() {
//
//	}
//
//}
