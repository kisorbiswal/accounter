package com.vimukti.accounter.web.client.ui.vat;

import java.util.List;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXReturn;
import com.vimukti.accounter.web.client.core.ClientTAXReturnEntry;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.grids.FileTAXGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class FileTAXView extends AbstractFileTAXView {

	private FileTAXGrid grid;

	@Override
	protected void reloadGrid() {
		grid.addLoadingImagePanel();
		rpcGetService.getTAXReturnEntries(selectedTaxAgency.getID(), fromDate
				.getDate().getDate(), toDate.getDate().getDate(),
				new AccounterAsyncCallback<List<ClientTAXReturnEntry>>() {

					@Override
					public void onException(AccounterException exception) {
						String errorString = AccounterExceptions
								.getErrorString(exception.getErrorCode());
						Accounter.showError(errorString);
					}

					@Override
					public void onResultSuccess(
							List<ClientTAXReturnEntry> result) {
						grid.removeLoadingImage();
						if (result != null && !result.isEmpty()) {
							grid.setRecords(result);
						} else {
							grid.addEmptyMessage(Accounter.constants()
									.selectTAXAgency());
						}
					}

				});
	}

	@Override
	protected void printTaxReturn() {

	}

	@Override
	protected ListGrid getGrid() {
		if (grid == null) {
			this.grid = new FileTAXGrid(false);
		}
		return grid;
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		saveOrUpdate(getData());
	}

	private void updateTransaction() {
		ClientTAXReturn taxReturn = new ClientTAXReturn();
		taxReturn.setTAXAgency(selectedTaxAgency.getID());
		taxReturn.setTaxEntries(grid.getRecords());
		taxReturn.setTransactionDate(new ClientFinanceDate().getDate());
		taxReturn.setPeriodStartDate(fromDate.getDate().getDate());
		taxReturn.setPeriodEndDate(toDate.getDate().getDate());
		double salesTaxTotal = 0, purchaseTaxTotal = 0;
		for (ClientTAXReturnEntry entry : grid.getRecords()) {
			if (DecimalUtil.isLessThan(entry.getTaxAmount(), 0.00D)) {
				purchaseTaxTotal += -entry.getTaxAmount();
			} else {
				salesTaxTotal += entry.getTaxAmount();
			}
		}
		double totalTax = salesTaxTotal - purchaseTaxTotal;
		taxReturn.setSalesTaxTotal(salesTaxTotal);
		taxReturn.setPurchaseTaxTotal(purchaseTaxTotal);
		taxReturn.setTotalTAXAmount(totalTax);
		taxReturn.setBalance(totalTax);
		setData(taxReturn);
	}

}
