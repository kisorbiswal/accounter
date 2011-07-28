/**
 * 
 */
package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFinanceLogger;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.FinanceLogView;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.InvalidTransactionEntryException;

/**
 * @author Sujana.B This grid displays the log information
 */
public class FinanceLogginGrid extends ListGrid<ClientFinanceLogger> {

	AccounterConstants companyConstants;
	private FinanceLogView financeLogView;

	public FinanceLogginGrid() {
		super(false);
	}

	public FinanceLogginGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
	}

	@Override
	protected int getColumnType(int col) {

		return ListGrid.COLUMN_TYPE_TEXT;

	}

	@Override
	protected Object getColumnValue(ClientFinanceLogger obj, int index) {
		switch (index) {
		case 0:
			return obj.getDescription();
		case 1:
			return obj.getCreatedBy();
		case 2:
			return UIUtils.getDateByCompanyType(new ClientFinanceDate(obj
					.getCreatedDate()));
		default:
			break;
		}
		return null;
	}

	@Override
	protected String[] getSelectValues(ClientFinanceLogger obj, int index) {
		return null;
	}

	@Override
	protected boolean isEditable(ClientFinanceLogger obj, int row, int index) {
		return false;
	}

	@Override
	protected void onClick(ClientFinanceLogger obj, int row, int index) {
		financeLogView.messageTxtPnl.getElement().setInnerHTML(
				obj.getLogMessge().replaceAll("\\n", "<br/>"));
	}

	@Override
	public void onDoubleClick(ClientFinanceLogger obj) {
	}

	@Override
	protected void onValueChange(ClientFinanceLogger obj, int index,
			Object value) {
	}

	@Override
	protected int sort(ClientFinanceLogger obj1, ClientFinanceLogger obj2,
			int index) {
		return 0;
	}

	@Override
	public boolean validateGrid() throws InvalidTransactionEntryException {
		return false;
	}

	@Override
	protected int getCellWidth(int index) {
		return 0;
	}

	@Override
	protected String[] getColumns() {
		AccounterConstants messages =Accounter.constants();
		return new String[] { messages.description(), messages.createdBy(),
				messages.date() };
	}

	public void setView(FinanceLogView financeLogView) {
		this.financeLogView = financeLogView;
	}

}
