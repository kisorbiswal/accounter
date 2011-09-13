package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.ItemCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.WarehouseCombo;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.IntegerField;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LabelItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class StockSettingsView extends BaseView {

	private DateItem adjustDate;
	protected TextItem entryNo, adjustmentQuantity;
	private WarehouseCombo warehouseComboSelect;
	private SelectCombo itemComboSelect;
	private ItemCombo itemGroupComboSelect;
	private AmountField amountText;
	private LabelItem itemIncomeAmoutText, itemExpenseAmountText,
			itemIncomeAmoutTextlabel, itemExpenseAmountTextlabel;
	private TextAreaItem memo;
	private DynamicForm vatform, dateForm, lowerform, middleform;

	@Override
	protected String getViewTitle() {
		return Accounter.constants().stockSettings();
	}

	public void init() {
		super.init();
		// vatAdjustment = (ClientVATAdjustment) this.data;
		createControls();
		setSize("100%", "100%");

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

		@Override
	public List getForms() {
		return null;
	}

	private void createControls() {

		Label infoLabel;
		infoLabel = new Label(Accounter.constants().stockSettings());
		infoLabel.removeStyleName("gwt-Label");

		infoLabel.setStyleName(Accounter.constants().labelTitle());
		adjustDate = new DateItem(null);
		adjustDate.setHelpInformation(true);
		adjustDate.setDatethanFireEvent(new ClientFinanceDate());

		entryNo = new IntegerField(this, Accounter.constants().no());
		entryNo.setHelpInformation(true);
		entryNo.setWidth(100);

		warehouseComboSelect = new WarehouseCombo(Accounter.constants().stockwarehouseCombo());

		itemComboSelect = new SelectCombo(Accounter.constants().stockItemType());
		List<String> combolist = new ArrayList<String>();
		combolist.add("Product");
		combolist.add("Service");
		itemComboSelect.initCombo(combolist);
		itemGroupComboSelect = new ItemCombo(Accounter.constants().stockItemName(), 0);
		adjustmentQuantity = new IntegerField(this,Accounter.constants().stockAdjustmentQuantity());
		entryNo.setWidth(100);
		itemIncomeAmoutText = new LabelItem();
		itemExpenseAmountText = new LabelItem();
		itemIncomeAmoutTextlabel = new LabelItem();
		itemExpenseAmountTextlabel = new LabelItem();
		itemIncomeAmoutTextlabel.setValue(Accounter.constants().stockItemIncomeAccount());
		itemExpenseAmountTextlabel.setValue(Accounter.constants().stockItemExpenseAccount());
		amountText = new AmountField(Accounter.constants().stockItemAmount(),this);
		memo = new TextAreaItem(Accounter.constants().stockMemo());
		memo.setMemo(false,this);
		memo.setWidth(100);

		vatform = new DynamicForm();
		lowerform = new DynamicForm();
		middleform = new DynamicForm();
		vatform.setNumCols(2);
		itemIncomeAmoutText.setValue("6+6646546546");
		itemExpenseAmountText.setValue("25465765416");

		vatform.setFields(warehouseComboSelect, itemComboSelect,
				itemGroupComboSelect, adjustmentQuantity);
		lowerform.setFields(amountText, memo);
		lowerform.setNumCols(4);
		middleform.setNumCols(4);
		middleform.setFields(itemIncomeAmoutTextlabel, itemIncomeAmoutText,
				itemExpenseAmountTextlabel, itemExpenseAmountText);

		dateForm = new DynamicForm();
		dateForm.setNumCols(4);
		dateForm.setStyleName("datenumber-panel");
		dateForm.setFields(adjustDate, entryNo);
		// dateForm.getCellFormatter().setWidth(0, 0, "189");
		HorizontalPanel datepanel = new HorizontalPanel();
		datepanel.setWidth("100%");
		datepanel.add(dateForm);
		datepanel.setCellHorizontalAlignment(dateForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		mainPanel.add(infoLabel);
		mainPanel.add(datepanel);
		// mainPanel.add(topform);
		mainPanel.add(vatform);
		mainPanel.add(middleform);
		mainPanel.add(lowerform);
		// mainPanel.add(memoForm);
		mainPanel.setSpacing(10);
		add(mainPanel);

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
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
