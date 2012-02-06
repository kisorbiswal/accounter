package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientBuildAssembly;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientInventoryAssembly;
import com.vimukti.accounter.web.client.core.ClientInventoryAssemblyItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.InventoryAssemblyItemCombo;
import com.vimukti.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.ButtonBar;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class BuildAssemblyView extends
		AbstractTransactionBaseView<ClientBuildAssembly> {

	public BuildAssemblyView() {
		super(ClientTransaction.BUILD_ASSEMBLY);
	}

	private InventoryAssemblyItemCombo itemCombo;
	private AmountLabel quantityOnHand, buildPoint, maximumBuildsLabel;
	private InventoryAssemblyItemTable itemTable;
	private AmountField quantityToBuild;
	private DateField dateField;
	private TextAreaItem memoItem;
	private ValidationResult result;

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected void createControls() {
		result = new ValidationResult();
		itemCombo = new InventoryAssemblyItemCombo(messages.assemblyItem());
		itemCombo.setRequired(true);

		quantityOnHand = new AmountLabel(messages.quantityOnHand());
		buildPoint = new AmountLabel(messages.buildPoint());
		itemTable = new InventoryAssemblyItemTable(
				InventoryAssemblyItemTable.BUILD_ASSEMBLY,
				new ICurrencyProvider() {

					@Override
					public ClientCurrency getTransactionCurrency() {
						return getCompany().getPrimaryCurrency();
					}

					@Override
					public Double getCurrencyFactor() {
						return 1.0;
					}

					@Override
					public Double getAmountInBaseCurrency(Double amount) {
						return amount;
					}
				}) {

			@Override
			protected void updateNonEditableItems() {

			}

			@Override
			protected boolean isInViewMode() {
				return true;
			}

			@Override
			protected void addEmptyRecords() {

			}

		};

		itemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientInventoryAssembly>() {

					@Override
					public void selectedComboBoxItem(
							ClientInventoryAssembly selectItem) {
						inventoryAssemblySelected(selectItem);
					}
				});
		itemTable.setDisabled(true);
		quantityToBuild = new AmountField(messages.quantityToBuild(), this);
		quantityToBuild.setRequired(true);
		dateField = new DateField(messages.date());
		dateField.setEnteredDate(new ClientFinanceDate());
		memoItem = new TextAreaItem(messages.memo());
		memoItem.setMemo(true, this);
		maximumBuildsLabel = new AmountLabel(
				messages.maximumNumberYouCanBuildFrom());
		quantityToBuild.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (Integer.parseInt(maximumBuildsLabel.getValue()) < Integer
						.parseInt(quantityToBuild.getValue())) {
					result.addError(quantityToBuild, messages
							.dntHaveEnoughComponents(maximumBuildsLabel
									.getValue()));
				}
			}
		});

		transactionNumber = new TextItem(messages.refNo());
		DynamicForm headerForm = new DynamicForm();
		headerForm.setNumCols(4);
		headerForm.setFields(dateField, transactionNumber);

		DynamicForm comboForm = new DynamicForm();
		comboForm.setNumCols(6);
		comboForm.setFields(itemCombo, quantityOnHand, buildPoint);

		VerticalPanel topPanel = new VerticalPanel();
		topPanel.add(headerForm);
		topPanel.add(comboForm);
		topPanel.setCellHorizontalAlignment(headerForm,
				HasHorizontalAlignment.ALIGN_RIGHT);
		comboForm.setWidth("100%");

		DynamicForm buildToForm = new DynamicForm();
		buildToForm.setFields(quantityToBuild);

		DynamicForm memoForm = new DynamicForm();
		memoForm.setFields(memoItem);

		DynamicForm maximumBuildsForm = new DynamicForm();
		maximumBuildsForm.setFields(maximumBuildsLabel);

		DynamicForm quantityToBuildForm = new DynamicForm();
		quantityToBuildForm.setFields(quantityToBuild);

		VerticalPanel panel = new VerticalPanel();
		panel.add(maximumBuildsForm);
		panel.add(quantityToBuildForm);

		HorizontalPanel bottomPanel = new HorizontalPanel();
		bottomPanel.add(memoForm);
		bottomPanel.add(panel);
		quantityToBuildForm.getElement().getParentElement()
				.setAttribute("align", "right");

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(topPanel);
		mainPanel.add(itemTable);
		mainPanel.add(bottomPanel);

		this.add(mainPanel);
		mainPanel.setWidth("100%");
		topPanel.setWidth("100%");
		bottomPanel.setWidth("100%");

		enableFormItems();

	}

	private void enableFormItems() {
		itemCombo.setDisabled(isInViewMode());
		dateField.setDisabled(isInViewMode());
		transactionNumber.setRequired(isInViewMode());
		quantityToBuild.setDisabled(isInViewMode());
		memoItem.setDisabled(isInViewMode());
	}

	@Override
	public void onEdit() {
		super.onEdit();
		enableFormItems();
	}

	protected void inventoryAssemblySelected(ClientInventoryAssembly selectItem) {
		if (selectItem != null) {
			buildPoint.setValue(Integer.toString(selectItem.getReorderPoint()));
			quantityOnHand.setAmount((double) selectItem.getOnhandQuantity());
			Set<ClientInventoryAssemblyItem> components = selectItem
					.getComponents();
			List<ClientInventoryAssemblyItem> assemblyItems = new ArrayList<ClientInventoryAssemblyItem>();
			for (ClientInventoryAssemblyItem clientInventoryAssemblyItem : components) {
				assemblyItems.add(clientInventoryAssemblyItem);
			}
			itemTable.setRecords(assemblyItems);
			maximumBuildsLabel.setValue(Integer
					.toString(calculateNumberOfBuilds(assemblyItems)));

		}

	}

	@Override
	protected void createButtons(ButtonBar buttonBar) {
		super.createButtons(buttonBar);
		saveAndCloseButton.setText(messages.buildAndClose());
		saveAndNewButton.setText(messages.buildAndNew());
	}

	@Override
	public void initData() {
		if (data == null) {
			setData(new ClientBuildAssembly());
		} else {
			setData(data);
			if (data.getInventoryAssembly() != null) {
				buildPoint.setValue(Integer.toString(data
						.getInventoryAssembly().getReorderPoint()));
				quantityOnHand.setAmount((double) data.getInventoryAssembly()
						.getOnhandQuantity());
				Set<ClientInventoryAssemblyItem> components = data
						.getInventoryAssembly().getComponents();
				List<ClientInventoryAssemblyItem> assemblyItems = new ArrayList<ClientInventoryAssemblyItem>();
				for (ClientInventoryAssemblyItem clientInventoryAssemblyItem : components) {
					assemblyItems.add(clientInventoryAssemblyItem);
				}
				itemTable.setRecords(assemblyItems);
			}

			if (data.getInventoryAssembly() != null) {
				itemCombo.setComboItem(data.getInventoryAssembly());
			}
			if (data.getDate() != null) {
				dateField.setDefaultValue(data.getDate());
			}
			if (data.getNumber() != null) {
				transactionNumber.setValue(data.getNumber());
			}
			if (data.getQuantityToBuild() != null) {
				quantityToBuild.setAmount(data.getQuantityToBuild());
			}
			super.initData();
		}
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void setFocus() {

	}

	private int calculateNumberOfBuilds(
			List<ClientInventoryAssemblyItem> assemblyItems) {
		int buildNumber = 0;
		int tempBuildNumber = 0;
		for (ClientInventoryAssemblyItem clientInventoryAssemblyItem : assemblyItems) {
			long onhandQuantity = clientInventoryAssemblyItem
					.getInventoryItem().getOnhandQuantity();
			double value = clientInventoryAssemblyItem.getQuantity().getValue();
			tempBuildNumber = (int) (onhandQuantity / value);
			if (buildNumber > tempBuildNumber) {
				buildNumber = tempBuildNumber;
			}
		}
		return buildNumber;
	}

	@Override
	protected void updateTransaction() {
		if (transaction == null) {
			transaction = new ClientBuildAssembly();
		}
		if (itemCombo.getSelectedValue() != null) {
			transaction.setInventoryAssembly(itemCombo.getSelectedValue());
		}
		if (dateField.getTime() <= 0) {
			transaction.setDate(dateField.getTime());
			transactionDate = dateField.getDate();
		}
		if (transactionNumber.getValue() != null) {
			transaction.setNumber(transactionNumber.getValue());
		}
		if (quantityToBuild.getAmount() != null) {
			transaction.setQuantityToBuild(quantityToBuild.getAmount());
		}
		super.updateTransaction();
	}

	@Override
	public ValidationResult validate() {
		if (quantityToBuild.getAmount() < 0) {
			result.addError(quantityToBuild,
					messages.pleaseEnter(quantityToBuild.getName()));
		}
		if (itemCombo.getSelectedValue() == null) {
			result.addError(itemCombo,
					messages.pleaseSelect(itemCombo.getName()));
		}
		return result;
	}

	@Override
	public void saveAndUpdateView() {
		saveOrUpdate(transaction);
	}

	@Override
	protected void initTransactionViewData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNonEditableItems() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void refreshTransactionGrid() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateDiscountValues() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAmountsFromGUI() {
		// TODO Auto-generated method stub

	}

	@Override
	public ClientBuildAssembly saveView() {
		ClientBuildAssembly assembly = new ClientBuildAssembly();
		assembly.setInventoryAssembly(itemCombo.getSelectedValue());
		return assembly;
	}

	@Override
	public void restoreView(ClientBuildAssembly assembly) {
		itemCombo.setComboItem(assembly.getInventoryAssembly());
		inventoryAssemblySelected(assembly.getInventoryAssembly());
	}
}
