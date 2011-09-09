/**
 * 
 */
package com.vimukti.accounter.web.client.ui.banking;

import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.ui.Accounter;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ReconciliationsTable extends CellTable<ClientReconciliation> {

	private ListDataProvider<ClientReconciliation> dataprovider = new ListDataProvider<ClientReconciliation>();

	/**
	 * Creates new Instance
	 */
	public ReconciliationsTable() {
		initColumns();
		dataprovider.addDataDisplay(this);
	}

	/**
	 * 
	 */
	private void initColumns() {
		TextColumn<ClientReconciliation> reconciliationDate = new TextColumn<ClientReconciliation>() {

			@Override
			public String getValue(ClientReconciliation object) {
				return object.getReconcilationDate().toString();
			}
		};

		TextColumn<ClientReconciliation> reconciliationPeriod = new TextColumn<ClientReconciliation>() {

			@Override
			public String getValue(ClientReconciliation object) {
				return Accounter.messages().to(
						object.getStartDate().toString(),
						object.getEndDate().toString());
			}
		};

		TextColumn<ClientReconciliation> openingBalance = new TextColumn<ClientReconciliation>() {

			@Override
			public String getValue(ClientReconciliation object) {
				return String.valueOf(object.getOpeningBalance());
			}
		};

		TextColumn<ClientReconciliation> closingBalance = new TextColumn<ClientReconciliation>() {

			@Override
			public String getValue(ClientReconciliation object) {
				return String.valueOf(object.getClosingBalance());
			}
		};

		this.addColumn(reconciliationDate, Accounter.constants()
				.ReconciliationDate());
		this.addColumn(reconciliationPeriod, Accounter.constants()
				.ReconciliationPeriod());
		this.addColumn(openingBalance, Accounter.constants().openBalance());
		this.addColumn(closingBalance, Accounter.constants().ClosingBalance());
	}

	public void setData(List<ClientReconciliation> data) {
		this.dataprovider.setList(data);
	}
}
