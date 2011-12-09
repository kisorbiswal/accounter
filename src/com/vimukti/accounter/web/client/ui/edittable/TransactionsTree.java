package com.vimukti.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientSalesOrder;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;

public abstract class TransactionsTree<T> extends SimplePanel {
	Tree tree;

	ICurrencyProvider currencyProvider;
	boolean showTaxCode;
	boolean enableTax;

	private TreeItem billableTree;

	private TreeItem creditsTree;

	private TreeItem chargesTree;

	private TreeItem quotesTree;

	public TransactionsTree(ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
		tree = new Tree() {
		};
		this.add(tree);
	}

	@Override
	protected void onAttach() {
		super.onAttach();
	}

	private void createColumns(boolean isAllrowsSelected) {
		CheckBox checkBox = new CheckBox(Accounter.messages().selectAll());
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

	public void setAllrows(ArrayList<EstimatesAndSalesOrdersList> result,
			boolean isNew) {
		tree.clear();
		quotesTree = null;
		chargesTree = null;
		creditsTree = null;
		billableTree = null;
		boolean isAllrowsSelected = false;
		if (result.isEmpty() && isNew) {
			return;
		} else if (result.isEmpty() && !isNew) {
			isAllrowsSelected = true;
		}
		createColumns(isAllrowsSelected);
		for (final EstimatesAndSalesOrdersList estimatesAndSalesOrdersList : result) {
			addQuotesTreeItem(estimatesAndSalesOrdersList);
		}
	}

	private void addQuotesTreeItem(
			EstimatesAndSalesOrdersList estimatesAndSalesOrdersList) {
		Accounter.createGETService().getObjectById(AccounterCoreType.ESTIMATE,
				estimatesAndSalesOrdersList.getTransactionId(),
				new AsyncCallback<ClientEstimate>() {

					@Override
					public void onFailure(Throwable caught) {
						Accounter.showError(Accounter.messages()
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
		setEnabled(isinViewMode());
	}

	private TreeItem getChildTransactionTree(String transactionLink,
			final ClientEstimate estimate, boolean isSelected) {
		final TreeItem transactionTree = new TreeItem();
		transactionTree.setUserObject(estimate);
		CheckBox checkBox = new CheckBox();
		checkBox.setValue(isSelected);
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(checkBox);
		horizontalPanel.addStyleName("transactionPanel");
		transactionTree.setWidget(horizontalPanel);
		Anchor transactionLabel = new Anchor(transactionLink) {
			@Override
			public void setEnabled(boolean enabled) {
				super.setEnabled(enabled);
				if (isAttached()) {
					onDetach();
					if (enabled) {
						sinkEvents(Event.ONCLICK);
					} else {
						unsinkEvents(Event.ONCLICK);
					}
					onAttach();
				}
			}
		};
		horizontalPanel.add(transactionLabel);
		horizontalPanel.add(new Label(Accounter.messages()
				.totalWithCurrencyName(transactionLink)
				+ " : "
				+ estimate.getTotal()));
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
				if (estimate.getEstimateType() == ClientEstimate.BILLABLEEXAPENSES) {
					openEnterBillView(estimate);
				} else {
					ReportsRPC.openTransactionView(estimate.getType(),
							estimate.getID());
				}
			}
		});
		TransactionItemsTable itemsTable = new TransactionItemsTable();
		transactionTree.addItem(itemsTable);
		List<ClientTransactionItem> transactionItems = estimate
				.getTransactionItems();
		itemsTable.setAllRows(transactionItems);
		return transactionTree;
	}

	protected void openEnterBillView(ClientEstimate estimate) {

		Accounter.createHomeService().getEnterBillByEstimateId(
				estimate.getID(), new AsyncCallback<ClientEnterBill>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(ClientEnterBill result) {
						UIUtils.runAction(result,
								ActionFactory.getEnterBillsAction());
					}
				});
	}

	private void addBillableTransactionTreeItem(final ClientEstimate estimate,
			boolean isSelected) {
		if (billableTree == null) {
			billableTree = new TreeItem();
			createTransactionsTree(isSelected, billableTree, Accounter
					.messages().billabelList());
		}
		String transactionLink = Accounter.messages().billabe();
		billableTree.addItem(getChildTransactionTree(transactionLink, estimate,
				isSelected));
	}

	private void createTransactionsTree(boolean isSelected,
			final TreeItem treeItem, String message) {
		CheckBox billableSelection = new CheckBox(message);
		billableSelection.setEnabled(isinViewMode());
		billableSelection.setValue(isSelected);
		treeItem.setWidget(billableSelection);
		billableSelection
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						onSelectionChanged(event.getValue(), treeItem);
					}
				});
		tree.addItem(treeItem);
	}

	private void addQuotesTransactionTreeItem(final ClientEstimate estimate,
			boolean isSelected) {
		if (quotesTree == null) {
			quotesTree = new TreeItem();
			createTransactionsTree(isSelected, quotesTree, Accounter.messages()
					.quotesList());
		}
		String transactionLink = Accounter.messages().quote();
		quotesTree.addItem(getChildTransactionTree(transactionLink, estimate,
				isSelected));
	}

	public abstract boolean isinViewMode();

	private void addChargesTransactionTreeItem(final ClientEstimate estimate,
			boolean isSelected) {
		if (chargesTree == null) {
			chargesTree = new TreeItem();
			createTransactionsTree(isSelected, chargesTree, Accounter
					.messages().chargesList());
		}
		String transactionLink = Accounter.messages().charge();
		chargesTree.addItem(getChildTransactionTree(transactionLink, estimate,
				isSelected));
	}

	private void addCreditsTransactionTreeItem(final ClientEstimate estimate,
			boolean isSelected) {
		if (creditsTree == null) {
			creditsTree = new TreeItem();
			createTransactionsTree(isSelected, creditsTree, Accounter
					.messages().creditsList());
		}
		creditsTree.addItem(getChildTransactionTree(Accounter.messages()
				.credit(), estimate, isSelected));
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

			if (widget != null && widget instanceof HorizontalPanel) {
				HorizontalPanel hPanel = (HorizontalPanel) widget;
				Widget checkBox = hPanel.getWidget(0);
				if (checkBox instanceof CheckBox) {
					CheckBox childBox = (CheckBox) checkBox;
					childBox.setValue(value);
				}
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
				if (childWidget != null
						&& childWidget instanceof HorizontalPanel) {
					HorizontalPanel hPanel = (HorizontalPanel) childWidget;
					Widget checkBox = hPanel.getWidget(0);
					if (checkBox instanceof CheckBox) {
						CheckBox childBox = (CheckBox) checkBox;
						if (childBox.getValue()) {
							ClientTransaction userObject = (ClientTransaction) child
									.getUserObject();
							selected.add(userObject);
						}
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
			lineTotal -= transaction.getNetAmount();
			totalTax -= transaction.getTaxTotal();
		} else {
			grandTotal += transaction.getTotal();
			lineTotal += transaction.getNetAmount();
			totalTax += transaction.getTaxTotal();
		}
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

	public void setEnabled(boolean isEnabled) {
		tree.setAnimationEnabled(isEnabled);
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
		for (int i = 0; i < tree.getItemCount(); i++) {
			TreeItem item = tree.getItem(i);
			enableTreeItem(item, isEnabled);
		}
	}

	private void enableTreeItem(TreeItem item, boolean isEnabled) {
		Widget widget = item.getWidget();
		if (widget != null && widget instanceof CheckBox) {
			((CheckBox) widget).setEnabled(isEnabled);
		}
		for (int j = 0; j < item.getChildCount(); j++) {
			TreeItem child = item.getChild(j);
			Widget childWidget = child.getWidget();
			if (childWidget != null && childWidget instanceof CheckBox) {
				CheckBox childBox = (CheckBox) childWidget;
				childBox.setEnabled(isEnabled);
			}
			if (childWidget != null && childWidget instanceof HorizontalPanel) {
				HorizontalPanel hPanel = (HorizontalPanel) childWidget;
				Widget checkBox = hPanel.getWidget(0);
				if (checkBox != null && checkBox instanceof CheckBox) {
					CheckBox childBox = (CheckBox) checkBox;
					childBox.setEnabled(isEnabled);
				}
				Widget widget2 = hPanel.getWidget(1);
				if (widget2 != null && widget2 instanceof Anchor) {
					Anchor childAnchor = (Anchor) widget2;
					childAnchor.setEnabled(isEnabled);
					if (isEnabled) {
						childAnchor.removeStyleName("editTable_disable_anchor");
						childAnchor.addStyleName("editTable_enable_anchor");
					} else {
						childAnchor.removeStyleName("editTable_enable_anchor");
						childAnchor.addStyleName("editTable_disable_anchor");
					}
				}
			}
			enableTreeItem(child, isEnabled);
		}
	}

	public abstract void setTransactionDate(ClientFinanceDate clientFinanceDate);

	public boolean validateTree() {
		return getSelectedRecords().size() == 0 ? false : true;
	}

}
