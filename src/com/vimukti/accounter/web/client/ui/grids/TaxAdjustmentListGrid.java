package com.vimukti.accounter.web.client.ui.grids;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientTAXAdjustment;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;

public class TaxAdjustmentListGrid extends BaseListGrid<ClientTAXAdjustment> {

	public TaxAdjustmentListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("TaxAdjustmentListGrid");

	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { messages.date(), messages.taxAgency(),
				messages.taxItem(), messages.adjustmentAccount(),
				messages.total(), "image-col" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { messages.date(), messages.taxAgency(),
				messages.taxItem(), messages.adjustmentAccount(),
				messages.total(), "image-col" };
	}

	@Override
	protected void onClick(ClientTAXAdjustment obj, int row, int col) {
		switch (col) {
		case 5:
			showWarnDialog(obj);
			break;
		default:
			break;
		}
	}

	@Override
	protected void executeDelete(ClientTAXAdjustment recordToBeDeleted) {
		AccounterAsyncCallback<ClientTAXAdjustment> callback = new AccounterAsyncCallback<ClientTAXAdjustment>() {

			public void onException(AccounterException caught) {
			}

			public void onResultSuccess(ClientTAXAdjustment result) {
				if (result != null) {
					deleteObject(result);

				}
			}

		};
		Accounter.createGETService()
				.getObjectById(AccounterCoreType.TAXADJUSTMENT,
						recordToBeDeleted.id, callback);
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		int errorCode = caught.getErrorCode();
		if (errorCode == AccounterException.ERROR_OBJECT_IN_USE) {
			Accounter.showError(AccounterExceptions.accounterMessages
					.payeeInUse(messages.taxAdjustment()));
			return;
		}
		super.deleteFailed(caught);
	}

	@Override
	protected Object getColumnValue(ClientTAXAdjustment obj, int index) {
		switch (index) {

		case 0:
			return obj.getDate();
		case 1:
			ClientTAXAgency taxAgency = getCompany().getTaxAgency(
					obj.getTaxAgency());
			return taxAgency != null ? taxAgency.getDisplayName() : "";
		case 2:
			ClientTAXItem taxItem = getCompany().getTaxItem(obj.getTaxItem());
			return taxItem != null ? taxItem.getDisplayName() : "";
		case 3:
			ClientAccount account = getCompany().getAccount(
					obj.getAdjustmentAccount());
			return account != null ? account.getDisplayName() : "";
		case 4:
			return obj.getTotal();
		case 5:
			return Accounter.getFinanceMenuImages().delete();
		}
		return "";
	}

	@Override
	public void onDoubleClick(ClientTAXAdjustment obj) {
		if (Utility.isUserHavePermissions(AccounterCoreType.TAX_CODE)) {
			ActionFactory.getAdjustTaxAction().run(obj, false);
		}
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.date(), messages.taxAgency(),
				messages.taxItem(), messages.adjustmentAccount(),
				messages.total(), "" };
	}

}
