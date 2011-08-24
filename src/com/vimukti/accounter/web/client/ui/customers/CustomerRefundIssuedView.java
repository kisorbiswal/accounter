package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

// // its not using any where
public class CustomerRefundIssuedView extends AbstractBaseView {
	AccounterConstants customerConstants = Accounter.constants();

	public CustomerRefundIssuedView() {
		createControls();
	}

	private void createControls() {
		// setCanDragReposition(true);
		// setShowCloseButton(true);

		Label lab1 = new Label(Accounter.messages().customerRefundIssued(
				Global.get().Customer()));
		// lab1.setWrap(false);

		TextItem pay2Text = new TextItem();
		pay2Text.setTitle(customerConstants.payTo());
		// pay2Text.setWidth("*");
		pay2Text.setDisabled(true);

		TextAreaItem addrArea = new TextAreaItem();
		addrArea.setTitle(customerConstants.address());
		// addrArea.setWidth("*");
		addrArea.setDisabled(true);

		DynamicForm custForm = new DynamicForm();
		custForm.setIsGroup(true);
		custForm.setGroupTitle(Global.get().customer());
		custForm.setFields(pay2Text, addrArea);

		TextItem payFromText = new TextItem();
		payFromText.setTitle(customerConstants.payFrom());
		// payFromText.setWidth("*");
		payFromText.setDisabled(true);

		AmountField amtText = new AmountField(customerConstants.amount(), this);

		// amtText.setWidth("*");
		amtText.setDisabled(true);

		TextItem payMethText = new TextItem();
		payMethText.setTitle(customerConstants.paymentMethod());
		// payMethText.setWidth("*");
		payMethText.setDisabled(true);

		CheckboxItem printCheck = new CheckboxItem(
				customerConstants.toBePrinted());

		TextItem checkNoText = new TextItem();
		checkNoText.setTitle(customerConstants.checkNo());
		// checkNoText.setWidth("*");
		checkNoText.setDisabled(true);

		TextItem memoText = new TextItem();
		memoText.setTitle(customerConstants.memo());
		// memoText.setWidth("*");

		TextItem refText = new TextItem();
		refText.setTitle(customerConstants.reference());
		// refText.setWidth("*");

		DynamicForm payForm = new DynamicForm();
		payForm.setIsGroup(true);
		payForm.setGroupTitle(customerConstants.paymentInformation());
		// payForm.setWrapItemTitles(false);
		payForm.setFields(payFromText, amtText, payMethText, printCheck,
				checkNoText, memoText, refText);

		VerticalPanel leftVLay = new VerticalPanel();
		leftVLay.setWidth("50%");
		leftVLay.add(custForm);
		leftVLay.add(payForm);

		AmountField endBalText = new AmountField(
				customerConstants.endingBalance(), this);
		// endBalText.setWidth("*");
		endBalText.setDisabled(true);

		DynamicForm balForm = new DynamicForm();
		balForm.setWidth("50%");
		// balForm.setAutoHeight();
		balForm.setIsGroup(true);
		balForm.setGroupTitle(customerConstants.balances());
		// balForm.setWrapItemTitles(false);
		balForm.setFields(endBalText);

		HorizontalPanel topHLay = new HorizontalPanel();
		topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.add(balForm);

		Button saveCloseButt = new Button(customerConstants.saveAndClose());
		// saveCloseButt.setAutoFit(true);
		// saveCloseButt.setLayoutAlign(Alignment.LEFT);

		Button saveNewButt = new Button(customerConstants.saveAndNew());
		// saveNewButt.setAutoFit(true);
		// saveNewButt.setLayoutAlign(Alignment.RIGHT);

		HorizontalPanel buttHLay = new HorizontalPanel();
		// buttHLay.setMembersMargin(150);
		// buttHLay.setMargin(10);
		// buttHLay.setAutoHeight();
		buttHLay.add(saveCloseButt);
		buttHLay.add(saveNewButt);
		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		// mainVLay.setTop(10);
		mainVLay.add(lab1);
		mainVLay.add(topHLay);
		mainVLay.add(buttHLay);

		// addChild(mainVLay);
		add(mainVLay);
		setSize("100%", "100%");
	}

	@Override
	public void init() {

	}

	@Override
	public void initData() {

	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

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
	public void printPreview() {

	}

	// setTitle(FinanceApplication.constants().title());

	@Override
	protected String getViewTitle() {
		return Accounter.messages().customerRefundIssued(
				Global.get().Customer());
	}

}
