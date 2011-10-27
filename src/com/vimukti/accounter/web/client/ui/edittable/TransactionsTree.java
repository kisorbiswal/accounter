package com.vimukti.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;

public class TransactionsTree<T> extends SimplePanel {
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

	private void createColumns() {
		CheckBox checkBox = new CheckBox(Accounter.constants()
				.quotesandsalesOrder());
		checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				Boolean isSelected = event.getValue();
			}
		});
		tree.addItem(checkBox);
		quotesTree = new TreeItem(Accounter.constants().quotesList());
		CheckBox quotesSelection = new CheckBox();
		quotesSelection
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						Boolean isSelected = event.getValue();
					}
				});
		tree.addItem(quotesSelection);
		tree.addItem(quotesTree);
		chargesTree = new TreeItem(Accounter.constants().chargesList());
		CheckBox chargesSelection = new CheckBox();
		chargesSelection
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						Boolean isSelected = event.getValue();
					}
				});
		tree.addItem(chargesSelection);
		tree.addItem(chargesTree);

		creditsTree = new TreeItem(Accounter.constants().creditsList());
		CheckBox creditsSelection = new CheckBox();
		creditsSelection
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						Boolean isSelected = event.getValue();
					}
				});
		tree.addItem(creditsSelection);
		tree.addItem(creditsTree);
		billableTree = new TreeItem(Accounter.constants().billabelList());
		CheckBox billableSelection = new CheckBox();
		billableSelection
				.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						Boolean isSelected = event.getValue();
					}
				});
		tree.addItem(billableSelection);
		tree.addItem(billableTree);
		salesOrderTree = new TreeItem(Accounter.constants().salesOrderList());
		CheckBox salesOrderSelection = new CheckBox();
		checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				Boolean isSelected = event.getValue();
			}
		});
		tree.addItem(salesOrderSelection);
		tree.addItem(salesOrderTree);
	}

	private void addTransactionTree(
			EstimatesAndSalesOrdersList estimatesAndSalesOrdersList) {
		HTML html = new HTML();
		html.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO
			}
		});
		TreeItem transactionTree = new TreeItem(html);
		CheckBox checkBox = new CheckBox();
		checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				Boolean isSelected = event.getValue();
			}
		});
		transactionTree.addItem(checkBox);
		if (estimatesAndSalesOrdersList.getEstimateType() == ClientEstimate.QUOTES) {
			html.setHTML(Accounter.messages().transactionLink(
					(Accounter.constants().quote())));
			quotesTree.addItem(transactionTree);
		} else if (estimatesAndSalesOrdersList.getEstimateType() == ClientEstimate.CHARGES) {
			html.setHTML(Accounter.messages().transactionLink(
					(Accounter.constants().charge())));
			chargesTree.addItem(transactionTree);
		} else if (estimatesAndSalesOrdersList.getEstimateType() == ClientEstimate.CREDITS) {
			html.setHTML(Accounter.messages().transactionLink(
					Accounter.constants().credit()));
			creditsTree.addItem(transactionTree);
		} else if (estimatesAndSalesOrdersList.getEstimateType() == ClientEstimate.BILLABLEEXAPENSES) {
			html.setHTML(Accounter.messages().transactionLink(
					Accounter.constants().billabe()));
			billableTree.addItem(transactionTree);
		} else {
			html.setHTML(Accounter.messages().transactionLink(
					Accounter.constants().salesOrder()));
			salesOrderTree.addItem(transactionTree);
		}
		TreeItem itemsTree = new TreeItem();
		transactionTree.addItem(itemsTree);
		List<ClientTransactionItem> transactionItems = estimatesAndSalesOrdersList
				.getTransactionItems();
		for (ClientTransactionItem clientTransactionItem : transactionItems) {

		}
	}

	public void setAllrows(ArrayList<EstimatesAndSalesOrdersList> result) {
		tree.clear();
		createColumns();
		for (EstimatesAndSalesOrdersList estimatesAndSalesOrdersList : result) {
			addTransactionTree(estimatesAndSalesOrdersList);
		}
	}
}
