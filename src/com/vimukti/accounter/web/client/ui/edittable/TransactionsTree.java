package com.vimukti.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public abstract class TransactionsTree<T> extends SimplePanel {
	Tree tree;

	ICurrencyProvider currencyProvider;
	boolean showTaxCode;
	boolean enableTax;

	private TreeItem salesOrderTree;

	private TreeItem billableTree;

	private TreeItem creditsTree;

	private TreeItem chargesTree;

	private TreeItem quotesTree;

	public TransactionsTree(ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
		tree = new Tree();
		this.add(tree);
	}

	@Override
	protected void onAttach() {
		super.onAttach();
	}

	private void createColumns(boolean isAllrowsSelected) {
		CheckBox checkBox = new CheckBox(Accounter.constants().selectAll());
		checkBox.setValue(isAllrowsSelected);
		checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				for (int i = 0; i < tree.getItemCount(); i++) {
					if (tree.getItem(i) instanceof TreeItem) {
						onSelectionChanged(event.getValue(), tree.getItem(i));
					}
				}
			}
		});
		tree.addItem(checkBox);
	}

	private void addTransactionTree(TreeItem transactionTree,
			ClientTransaction transaction) {
		TransactionItemsTable itemsTable = new TransactionItemsTable();
		transactionTree.addItem(itemsTable);
		List<ClientTransactionItem> transactionItems = transaction
				.getTransactionItems();
		itemsTable.setAllRows(transactionItems);
	}

	public void setAllrows(ArrayList<EstimatesAndSalesOrdersList> result,
			boolean isNew) {
		tree.clear();
		boolean isAllrowsSelected = false;
		if (result.isEmpty() && isNew) {
			return;
		} else if (result.isEmpty() && !isNew) {
			isAllrowsSelected = true;
		}
		createColumns(isAllrowsSelected);
		for (final EstimatesAndSalesOrdersList estimatesAndSalesOrdersList : result) {
			if (estimatesAndSalesOrdersList.getEstimateType() != 0) {
				addQuotesTreeItem(estimatesAndSalesOrdersList);
			} else {
				addSalesOrderTree(estimatesAndSalesOrdersList);
			}
		}
	}

	private void addQuotesTreeItem(
			EstimatesAndSalesOrdersList estimatesAndSalesOrdersList) {
		Accounter.createGETService().getObjectById(AccounterCoreType.ESTIMATE,
				estimatesAndSalesOrdersList.getTransactionId(),
				new AsyncCallback<ClientEstimate>() {

					@Override
					public void onFailure(Throwable caught) {
						Accounter.showError(Accounter.constants()
								.unableToLoadRequiredQuote());
					}

					@Override
					public void onSuccess(ClientEstimate result) {
						addAllQuoteTransactionTreeItem(result, false);
					}
				});
	}

	protected void addAllQuoteTransactionTreeItem(ClientEstimate result,
			boolean isSelected) {
		if (result.getEstimateType() == ClientEstimate.QUOTES) {
			addQuotesTransactionTreeItem(result, isSelected);
		} else if (result.getEstimateType() == ClientEstimate.CHARGES) {
			addChargesTransactionTreeItem(result, isSelected);
		} else if (result.getEstimateType() == ClientEstimate.BILLABLEEXAPENSES) {
			addBillableTransactionTreeItem(result, isSelected);
		} else if (result.getEstimateType() == ClientEstimate.CREDITS) {
			addCreditsTransactionTreeItem(result, isSelected);
		}
	}

	private void addSalesOrderTree(
			EstimatesAndSalesOrdersList estimatesAndSalesOrdersList) {
		Accounter.createGETService().getObjectById(
				AccounterCoreType.SALESORDER,
				estimatesAndSalesOrdersList.getTransactionId(),
				new AsyncCallback<ClientSalesOrder>() {

					@Override
					public void onFailure(Throwable caught) {
						Accounter.showError(Accounter.constants()
								.unableToLoadRequiredQuote());
					}

					@Override
					public void onSuccess(ClientSalesOrder result) {
						addSalesOrderTransactionTreeItem(result, false);
					}
				});
	}

	private void addSalesOrderTransactionTreeItem(
			final ClientSalesOrder salesOrder, boolean isSelected) {
		if (salesOrderTree == null) {
			createSalesOrderTree(isSelected);
		}
		SafeHtml transactionLink = Accounter.messages().transactionLink(
				(Accounter.constants().salesOrder()));
		final TreeItem transactionTree = new TreeItem();
		CheckBox checkBox = new CheckBox();
		checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				onSelectionChanged(event.getValue(), transactionTree);
			}
		});
		checkBox.setValue(isSelected);
		transactionTree.setWidget(checkBox);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.addStyleName("transactionPanel");
		transactionTree.addItem(horizontalPanel);
		HTML transactionLabel = new HTML(transactionLink);
		horizontalPanel.add(transactionLabel);
		transactionLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				ReportsRPC.openTransactionView(salesOrder.getType(),
						salesOrder.getID());
			}
		});
		horizontalPanel.add(new Label(Accounter.messages().total(
				Accounter.constants().salesOrder())
				+ " : " + salesOrder.getTotal()));
		transactionTree.setUserObject(salesOrder);
		salesOrderTree.addItem(transactionTree);
		addTransactionTree(transactionTree, salesOrder);
	}

	private void createSalesOrderTree(boolean isSelected) {
		CheckBox salesOrderSelection = new CheckBox(Accounter.constants()
				.salesOrderList());
		salesOrderSelection.setValue(isSelected);
		salesOrderTree = new TreeItem(salesOrderSelection);
		salesOrderSelection
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						onSelectionChanged(event.getValue(), salesOrderTree);
					}
				});
		tree.addItem(salesOrderTree);
	}

	private void addBillableTransactionTreeItem(final ClientEstimate estimate,
			boolean isSelected) {
		if (billableTree == null) {
			createBillableTree(isSelected);
		}
		SafeHtml transactionLink = Accounter.messages().transactionLink(
				(Accounter.constants().billabe()));
		final TreeItem transactionTree = new TreeItem();
		transactionTree.setUserObject(estimate);
		billableTree.addItem(transactionTree);
		CheckBox checkBox = new CheckBox();
		transactionTree.setWidget(checkBox);
		checkBox.setValue(isSelected);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.addStyleName("transactionPanel");
		transactionTree.addItem(horizontalPanel);
		HTML transactionLabel = new HTML(transactionLink);
		horizontalPanel.add(transactionLabel);
		horizontalPanel.add(new Label(Accounter.messages().total(
				Accounter.constants().billabe())
				+ " : " + estimate.getTotal()));
		checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				onSelectionChanged(event.getValue(), transactionTree);
			}
		});
		transactionLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				ReportsRPC.openTransactionView(
						ClientTransaction.TYPE_ENTER_BILL,
						estimate.getEnterBill());
			}
		});
		addTransactionTree(transactionTree, estimate);
	}

	private void createBillableTree(boolean isSelected) {
		CheckBox billableSelection = new CheckBox(Accounter.constants()
				.billabelList());
		billableSelection.setValue(isSelected);
		billableTree = new TreeItem(billableSelection);
		billableSelection
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						onSelectionChanged(event.getValue(), billableTree);
					}
				});
		tree.addItem(billableTree);
	}

	private void addQuotesTransactionTreeItem(final ClientEstimate estimate,
			boolean isSelected) {
		if (quotesTree == null) {
			createQuoteTree(isSelected);
		}
		SafeHtml transactionLink = Accounter.messages().transactionLink(
				(Accounter.constants().quote()));
		final TreeItem transactionTree = new TreeItem();
		CheckBox checkBox = new CheckBox();
		checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				onSelectionChanged(event.getValue(), transactionTree);
			}
		});
		checkBox.setValue(isSelected);
		transactionTree.setWidget(checkBox);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.addStyleName("transactionPanel");
		transactionTree.addItem(horizontalPanel);
		HTML transactionLabel = new HTML(transactionLink);
		horizontalPanel.add(transactionLabel);
		transactionLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				ReportsRPC.openTransactionView(estimate.getType(),
						estimate.getID());
			}
		});
		horizontalPanel.add(new Label(Accounter.messages().total(
				Accounter.constants().quote())
				+ " : " + estimate.getTotal()));
		transactionTree.setUserObject(estimate);
		quotesTree.addItem(transactionTree);
		addTransactionTree(transactionTree, estimate);
	}

	private void createQuoteTree(boolean isSelected) {
		CheckBox quotesSelection = new CheckBox(Accounter.constants()
				.quotesList());
		quotesSelection.setValue(isSelected);
		quotesTree = new TreeItem(quotesSelection);
		quotesSelection
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						onSelectionChanged(event.getValue(), quotesTree);
					}
				});
		tree.addItem(quotesTree);
	}

	private void addChargesTransactionTreeItem(final ClientEstimate estimate,
			boolean isSelected) {
		if (chargesTree == null) {
			createChargesTree(isSelected);
		}
		SafeHtml transactionLink = Accounter.messages().transactionLink(
				(Accounter.constants().charge()));
		final TreeItem transactionTree = new TreeItem();
		CheckBox checkBox = new CheckBox();
		checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				onSelectionChanged(event.getValue(), transactionTree);
			}
		});
		checkBox.setValue(isSelected);
		transactionTree.setWidget(checkBox);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.addStyleName("transactionPanel");
		transactionTree.addItem(horizontalPanel);
		HTML transactionLabel = new HTML(transactionLink);
		horizontalPanel.add(transactionLabel);
		transactionLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				ReportsRPC.openTransactionView(estimate.getType(),
						estimate.getID());
			}
		});
		horizontalPanel.add(new Label(Accounter.messages().total(
				Accounter.constants().charge())
				+ " : " + estimate.getTotal()));
		transactionTree.setUserObject(estimate);
		chargesTree.addItem(transactionTree);
		addTransactionTree(transactionTree, estimate);
	}

	private void createChargesTree(boolean isSelected) {
		CheckBox chargesSelection = new CheckBox(Accounter.constants()
				.chargesList());
		chargesSelection.setValue(isSelected);
		chargesTree = new TreeItem(chargesSelection);
		chargesSelection
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						onSelectionChanged(event.getValue(), chargesTree);
					}
				});
		tree.addItem(chargesTree);
	}

	private void addCreditsTransactionTreeItem(final ClientEstimate estimate,
			boolean isSelected) {
		if (creditsTree == null) {
			createCreditsTree(isSelected);
		}
		SafeHtml transactionLink = Accounter.messages().transactionLink(
				(Accounter.constants().credit()));
		final TreeItem transactionTree = new TreeItem();
		CheckBox checkBox = new CheckBox();
		checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				onSelectionChanged(event.getValue(), transactionTree);
			}
		});
		checkBox.setValue(isSelected);
		transactionTree.setWidget(checkBox);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.addStyleName("transactionPanel");
		transactionTree.addItem(horizontalPanel);
		HTML transactionLabel = new HTML(transactionLink);
		horizontalPanel.add(transactionLabel);
		transactionLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				ReportsRPC.openTransactionView(estimate.getType(),
						estimate.getID());
			}
		});
		horizontalPanel.add(new Label(Accounter.messages().total(
				Accounter.constants().credit())
				+ " : " + estimate.getTotal()));
		transactionTree.setUserObject(estimate);
		creditsTree.addItem(transactionTree);
		addTransactionTree(transactionTree, estimate);
	}

	private void createCreditsTree(boolean isSelected) {
		CheckBox creditsSelection = new CheckBox(Accounter.constants()
				.creditsList());
		creditsSelection.setValue(isSelected);
		creditsSelection
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						onSelectionChanged(event.getValue(), creditsTree);
					}
				});
		creditsTree = new TreeItem(creditsSelection);
		tree.addItem(creditsTree);
	}

	protected void onSelectionChanged(Boolean value, TreeItem treeItem) {
		Widget treeWidget = treeItem.getWidget();
		if (treeWidget instanceof CheckBox) {
			CheckBox box = (CheckBox) treeWidget;
			box.setValue(value);
			treeItem.setState(value);
		}
		ClientTransaction transaction = (ClientTransaction) treeItem
				.getUserObject();
		if (transaction != null) {
			setTransactionDate(transaction.getDate());
		}
		for (int i = 0; i < treeItem.getChildCount(); i++) {
			TreeItem child = treeItem.getChild(i);
			transaction = (ClientTransaction) child.getUserObject();
			if (transaction != null) {
				setTransactionDate(transaction.getDate());
			}
			child.setState(value);
			Widget widget = child.getWidget();
			if (widget instanceof CheckBox) {
				CheckBox box = (CheckBox) widget;
				box.setValue(value);
			}
		}
		updateTransactionTreeItemTotals();
	}

	public void updateTransactionTreeItemTotals() {
		grandTotal = 0.0;
		lineTotal = 0.0;
		totalTax = 0.0;
		List<ClientTransaction> selectedRecords = getSelectedRecords();
		for (ClientTransaction clientTransaction : selectedRecords) {
			if (clientTransaction instanceof ClientEstimate) {
				ClientEstimate estimate = (ClientEstimate) clientTransaction;
				updateEstimateTotal(estimate);
			} else {
				ClientSalesOrder salesOrder = (ClientSalesOrder) clientTransaction;
				updateSalesOrderTotal(salesOrder);
			}
		}
		updateTransactionTotal();
	}

	private void updateSalesOrderTotal(ClientSalesOrder salesOrder) {
		grandTotal += salesOrder.getTotal();
		lineTotal += salesOrder.getNetAmount();
	}

	public List<ClientTransaction> getSelectedRecords() {
		List<ClientTransaction> selected = new ArrayList<ClientTransaction>();
		for (int i = 1; i < tree.getItemCount(); i++) {
			TreeItem item = tree.getItem(i);
			for (int j = 0; j < item.getChildCount(); j++) {
				TreeItem child = item.getChild(j);
				Widget childWidget = child.getWidget();
				if (childWidget instanceof CheckBox) {
					CheckBox childBox = (CheckBox) childWidget;
					if (childBox.getValue()) {
						ClientTransaction userObject = (ClientTransaction) child
								.getUserObject();
						selected.add(userObject);
					}
				}
			}
		}
		return selected;
	}

	double lineTotal = 0.0;
	double totalTax = 0.0;
	double grandTotal = 0.0;

	private void updateEstimateTotal(ClientEstimate transaction) {
		if (transaction.getEstimateType() == ClientEstimate.CREDITS) {
			grandTotal -= transaction.getTotal();
		} else {
			grandTotal += transaction.getTotal();
		}
		lineTotal += transaction.getNetAmount();
		totalTax += transaction.getTaxTotal();
	}

	public abstract void updateTransactionTotal();

	public Double getTotalTax() {
		return totalTax;
	}

	public Double getLineTotal() {
		return lineTotal;
	}

	public Double getGrandTotal() {
		return grandTotal;
	}

	public void quotesSelected(List<ClientEstimate> estimates) {
		for (ClientEstimate estimate : estimates) {
			addEstimateTreeItemRow(estimate);
		}
	}

	private void addEstimateTreeItemRow(ClientEstimate estimate) {
		addAllQuoteTransactionTreeItem(estimate, true);
	}

	public void salesOrdersSelected(List<ClientSalesOrder> estimates) {
		for (ClientSalesOrder salesOrder : estimates) {
			addSalesOrderTreeItemRow(salesOrder);
		}
	}

	private void addSalesOrderTreeItemRow(ClientSalesOrder salesOrder) {
		addSalesOrderTransactionTreeItem(salesOrder, true);
	}

	public void setEnabled(boolean isEnabled) {
		if (tree.getItem(0) == null) {
			return;
		}
		Widget widget = tree.getItem(0).getWidget();
		if (widget == null) {
			return;
		}
		if (widget instanceof CheckBox) {
			((CheckBox) widget).setEnabled(isEnabled);
		}
		for (int i = 1; i < tree.getItemCount(); i++) {
			TreeItem item = tree.getItem(i);
			item.setState(true, isEnabled);
			widget = item.getWidget();
			if (widget instanceof CheckBox) {
				((CheckBox) widget).setEnabled(isEnabled);
			}
			for (int j = 0; j < item.getChildCount(); j++) {
				TreeItem child = item.getChild(j);
				Widget childWidget = child.getWidget();
				if (childWidget instanceof CheckBox) {
					CheckBox childBox = (CheckBox) childWidget;
					childBox.setEnabled(isEnabled);
				}
			}
		}
	}

	public abstract void setTransactionDate(ClientFinanceDate clientFinanceDate);

	public boolean validateTree() {
		return getSelectedRecords().size() == 0 ? false : true;
	}

}
