package com.vimukti.accounter.web.client.ui.company;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.ImageButton;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.ItemCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class ItemMergeView extends BaseView<ClientItem> {

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

	public ItemMergeView() {
		this.getElement().setId("ItemMergeView");
	}

	@Override
	public void init() {
		super.init();
		ImageButton meregButton = new ImageButton(messages.merge(), Accounter
				.getFinanceImages().saveAndClose());
		meregButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				validate();
				mergeItem();

			}
		});

		addButton(meregButton);
		createControls();
	}

	private void createControls() {
		Label lab1 = new Label(messages.mergeItems());
		lab1.setStyleName("label-title");

		form = new DynamicForm("form");
		form1 = new DynamicForm("form1");
		StyledPanel layout = new StyledPanel("layout");
		StyledPanel layout1 = new StyledPanel("layout1");
		StyledPanel horizontalPanel = new StyledPanel("horizontalPanel");
		itemCombo = createItemCombo();
		itemCombo1 = createItemCombo1();

		status = new CheckboxItem(messages.active(), "status");
		status.setValue(false);

		status1 = new CheckboxItem(messages.active(), "status1");
		status1.setValue(false);

		itemType = new TextItem(messages.itemType(), "itemType");
		itemType.setEnabled(false);

		itemType1 = new TextItem(messages.itemType(), "itemType1");
		itemType1.setEnabled(false);

		price = new TextItem(messages.salesPrice(), "price");
		price.setEnabled(false);

		price1 = new TextItem(messages.salesPrice(), "price1");
		price1.setEnabled(false);

		form.add(itemCombo, itemType, status, price);
		form1.add(itemCombo1, itemType1, status1, price1);
		// form.setItems(getTextItems());
		layout.add(form);
		layout1.add(form1);
		horizontalPanel.add(lab1);
		horizontalPanel.add(layout);
		horizontalPanel.add(layout1);
		this.add(horizontalPanel);

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

	protected void mergeItem() {
		if (fromClientItem != null && toClientItem != null) {
			if (fromClientItem.getID() == toClientItem.getID()) {
			}
		}
		if (fromClientItem.getType() != toClientItem.getType()) {
			Accounter.showError(messages.bothItemsMustBelongsTheSameType());
		} else {
			Accounter.createHomeService().mergeItem(fromClientItem,
					toClientItem, new AsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							Accounter.showInformation("Merge Sucessful");
							onClose();
						}

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}
					});
			com.google.gwt.user.client.History.back();
		}
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
		return null;
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

}
