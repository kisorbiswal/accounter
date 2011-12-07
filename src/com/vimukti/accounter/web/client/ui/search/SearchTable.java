package com.vimukti.accounter.web.client.ui.search;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.SearchInput;
import com.vimukti.accounter.web.client.core.SearchResultlist;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;

public class SearchTable extends CellTable<SearchResultlist> {

	private AsyncDataProvider<SearchResultlist> listDataProvider;
	private AccounterMessages messages = Accounter.messages();
	private SearchInput input;

	public SearchTable(SearchInput input) {
		this.input = input;
		createControls();
	}

	private void createControls() {

		listDataProvider = new AsyncDataProvider<SearchResultlist>() {

			@Override
			protected void onRangeChanged(HasData<SearchResultlist> display) {
				final int start = display.getVisibleRange().getStart();
				int length = display.getVisibleRange().getLength();
				Accounter.createHomeService().getSearchResultByInput(input,
						start, length,
						new AsyncCallback<PaginationList<SearchResultlist>>() {

							@Override
							public void onSuccess(
									PaginationList<SearchResultlist> result) {
								if (result != null || result.size() != 0
										|| !result.isEmpty()) {
									updateRowData(start, result);
									setRowCount(result.getTotalCount());
								} else {
									addEmptyMsg();
								}

							}

							@Override
							public void onFailure(Throwable caught) {

							}
						});

			}
		};
		setPageSize(10);
		listDataProvider.addDataDisplay(this);

		this.setWidth("100%", true);
		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		initTableColumns();

	}

	private void addEmptyMsg() {
		HTML emptyMessage = new HTML(messages.noRecordsToShow());
		setEmptyTableWidget(emptyMessage);
	}

	private void initTableColumns() {

		TextColumn<SearchResultlist> transactionDateColumn = new TextColumn<SearchResultlist>() {

			@Override
			public String getValue(SearchResultlist object) {
				return String.valueOf(object.getDate() != null ? object
						.getDate() : "");
			}
		};

		TextColumn<SearchResultlist> typeColumn = new TextColumn<SearchResultlist>() {

			@Override
			public String getValue(SearchResultlist object) {
				return Utility.getTransactionName(object.getTransactionType());
			}
		};

		TextColumn<SearchResultlist> amountColumn = new TextColumn<SearchResultlist>() {

			@Override
			public String getValue(SearchResultlist object) {
				return DataUtils.getAmountAsString(object.getAmount());
			}
		};

		this.addColumn(transactionDateColumn, messages.date());
		this.addColumn(typeColumn, messages.type());
		this.addColumn(amountColumn, messages.amount());
		this.setColumnWidth(transactionDateColumn, "25px");
		this.setColumnWidth(typeColumn, "25px");
		this.setColumnWidth(amountColumn, "25px");

	}
}
