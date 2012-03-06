package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.ItemCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class ItemMergeDialog extends BaseView implements AsyncCallback<Void> {

	private DynamicForm form;
	private DynamicForm form1;

	private ItemCombo itemCombo;
	private ItemCombo itemCombo1;
	private CheckboxItem status;
	private CheckboxItem status1;

	private TextItem itemType;
	private TextItem itemType1;
	private TextItem price1;
	private TextItem price;

	private ClientItem fromClientItem;
	private ClientItem toClientItem;

	public ItemMergeDialog(String title, String descript) {

	}

	public ItemMergeDialog() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("accountMergeDialog");
		createControls();
		saveAndNewButton.setVisible(false);
		saveAndCloseButton.setText(messages.merge());
	}

	private void createControls() {
		form = new DynamicForm("firstForm");
		form1 = new DynamicForm("seondForm");
		StyledPanel styledPanel = new StyledPanel("mainPanel");
		itemCombo = createItemCombo();
		itemCombo1 = createItemCombo1();

		status = new CheckboxItem(messages.active(), "status");
		status.setValue(false);

		status1 = new CheckboxItem(messages.active(), "status");
		status1.setValue(false);

		itemType = new TextItem(messages.itemType(), "itemType");
		itemType.setEnabled(false);

		itemType1 = new TextItem(messages.itemType(), "itemType");
		itemType1.setEnabled(false);

		price = new TextItem(messages.salesPrice(), "price");
		price.setEnabled(false);

		price1 = new TextItem(messages.salesPrice(), "price");
		price1.setEnabled(false);
		form.add(itemCombo);
		form.add(itemType);
		form.add(status);
		form.add(price);
		form1.add(itemCombo1);
		form1.add(itemType1);
		form1.add(status1);
		form1.add(price1);
		// form.setItems(getTextItems());
		styledPanel.add(form);
		styledPanel.add(form1);
		this.add(styledPanel);

	}

	private ItemCombo createItemCombo1() {
		itemCombo1 = new ItemCombo(messages.itemTo(), 2, false);
		itemCombo1.setRequired(true);
		itemCombo1
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItem>() {

					@Override
					public void selectedComboBoxItem(ClientItem selectItem) {
						toClientItem = selectItem;
						customerSelected1(selectItem);

					}

				});

		return itemCombo1;
	}

	private ItemCombo createItemCombo() {
		itemCombo = new ItemCombo(messages.itemFrom(), 2, false);
		itemCombo.setRequired(true);
		itemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientItem>() {

					@Override
					public void selectedComboBoxItem(ClientItem selectItem) {
						fromClientItem = selectItem;
						customerSelected(selectItem);

					}

				});

		return itemCombo;
	}

	private void customerSelected(ClientItem selectItem) {

		status.setValue(selectItem.isActive());
		if (selectItem.getType() == 1) {
			itemType.setValue(messages.service());
		} else if (selectItem.getType() == 3) {
			itemType.setValue(messages.product());
		}
		price.setValue(String.valueOf(selectItem.getSalesPrice()));

	}

	private void customerSelected1(ClientItem selectItem) {
		status1.setValue(selectItem.isActive());
		if (selectItem.getType() == 1) {
			itemType1.setValue(messages.service());
		} else if (selectItem.getType() == 3) {
			itemType1.setValue(messages.product());
		}
		price1.setValue(String.valueOf(selectItem.getSalesPrice()));
	}

	public ValidationResult validate() {
		ValidationResult result = form.validate();
		result.add(form1.validate());
		if (fromClientItem != null && toClientItem != null) {
			if (fromClientItem.getID() == toClientItem.getID()) {
				result.addError(fromClientItem,
						messages.notMove(messages.items()));
			}
			if (fromClientItem.getType() != toClientItem.getType()) {
				result.addError(fromClientItem,
						messages.typesMustbeSame(messages.items()));
			}
			return result;
		}
		return result;
	}

	@Override
	public void saveAndUpdateView() {

		validate();

		if (fromClientItem != null && toClientItem != null) {
			if (fromClientItem.getID() == toClientItem.getID()) {
			}
		}
		if (fromClientItem.getType() != toClientItem.getType()) {
			Accounter.showError("Both Items must belong to the same type");
		} else {
			Accounter.createHomeService().mergeItem(fromClientItem,
					toClientItem, this);
		}
	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Void result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFocus() {
		itemCombo.setFocus();

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
	public String getViewTitle() {
		return messages.mergeItems();
	}

	@Override
	public List getForms() {
		// TODO Auto-generated method stub
		return null;
	}

}
