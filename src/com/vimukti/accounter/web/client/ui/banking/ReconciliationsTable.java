/**
 * 
 */
package com.vimukti.accounter.web.client.ui.banking;

import java.util.Collections;
import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.core.ClientReconciliation;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.IDeleteCallback;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.grids.columns.ImageActionColumn;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ReconciliationsTable extends CellTable<ClientReconciliation>
		implements IDeleteCallback {

	private ListDataProvider<ClientReconciliation> dataprovider = new ListDataProvider<ClientReconciliation>();

	/**
	 * Creates new Instance
	 */
	public ReconciliationsTable() {
		initColumns();
		dataprovider.addDataDisplay(this);
		HTML emptyMessage = new HTML(Accounter.constants()
				.reconciliationsEmpty());
		emptyMessage.setHeight("150px");
		setEmptyTableWidget(emptyMessage);
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
				return DataUtils.getAmountAsString(object.getOpeningBalance());
			}
		};

		TextColumn<ClientReconciliation> closingBalance = new TextColumn<ClientReconciliation>() {

			@Override
			public String getValue(ClientReconciliation object) {
				return DataUtils.getAmountAsString(object.getClosingBalance());
			}
		};

		ImageActionColumn<ClientReconciliation> show = new ImageActionColumn<ClientReconciliation>() {

			@Override
			protected void onSelect(int index, ClientReconciliation object) {
				openReconciliation(object);
			}

			@Override
			public ImageResource getValue(ClientReconciliation object) {
				return Accounter.getFinanceMenuImages().accounterRegisterIcon();
			}
		};

		ImageActionColumn<ClientReconciliation> delete = new ImageActionColumn<ClientReconciliation>() {

			@Override
			protected void onSelect(int index, ClientReconciliation object) {
				// Accounter.getFinanceMenuImages().accounterRegisterIcon()
				deleteReconciliation(object);
			}

			@Override
			public ImageResource getValue(ClientReconciliation object) {
				return Accounter.getFinanceImages().delete();
			}
		};

		this.addColumn(reconciliationDate, Accounter.constants()
				.ReconciliationDate());
		this.addColumn(reconciliationPeriod, Accounter.constants()
				.ReconciliationPeriod());
		this.addColumn(openingBalance, Accounter.constants().openBalance());
		this.addColumn(closingBalance, Accounter.constants().ClosingBalance());
		this.addColumn(show, Accounter.constants().show());
		this.addColumn(delete);
	}

	private void deleteReconciliation(ClientReconciliation object) {
		Accounter.deleteObject(this, object);
	}

	public void setData(List<ClientReconciliation> data) {
		if (data == null) {
			data = Collections.emptyList();
		}
		this.dataprovider.setList(data);
	}

	public void openReconciliation(ClientReconciliation reconciliation) {
		ActionFactory.getNewReconciliationAction().run(reconciliation, false);
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		Accounter.showError(AccounterExceptions.getErrorString(caught
				.getErrorCode()));
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		dataprovider.getList().remove((ClientReconciliation) result);
	}

}
