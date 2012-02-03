package com.vimukti.accounter.web.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

/**
 * for exporting the the list as CSV file
 * 
 * @author Lingarao.R
 * 
 */
public interface IAccounterExportCSVServiceAsync {

	public void getPayeeListExportCsv(int transactionCategory,
			boolean isActive, AsyncCallback<String> callBack);

	public void getInvoiceListExportCsv(long fromDate, long toDate,
			int invoicesType, int viewType, AsyncCallback<String> callback);

	public void getEstimatesExportCsv(int type, int status, long fromDate,
			long toDate, AsyncCallback<String> callback);

	public void getReceivePaymentsListExportCsv(long fromDate, long toDate,
			int transactionType, int viewType, AsyncCallback<String> callback);

	public void getCustomerRefundsListExportCsv(long fromDate, long toDate,
			AsyncCallback<String> callback);

	public void getBillsAndItemReceiptListExportCsv(boolean isExpensesList,
			int transactionType, long fromDate, long toDate, int viewType,
			AsyncCallback<String> callback);

	public void getVendorPaymentsListExportCsv(long fromDate, long toDate,
			int viewType, AsyncCallback<String> callBack);

	public void getPaymentsListExportCsv(long fromDate, long toDate,
			int viewType, AsyncCallback<String> callBack);

	public void getPayeeChecksExportCsv(boolean isCustomerChecks,
			long fromDate, long toDate, int viewType,
			AsyncCallback<String> callBack);

	void getFixedAssetListExportCsv(int status, AsyncCallback<String> callback);

	void getAccountsExportCsv(int typeOfAccount, boolean isActiveAccount,
			AsyncCallback<String> callBack);

	public void getJournalEntriesExportCsv(long fromDate, long toDate,
			AsyncCallback<String> callback);

	public void getUsersActivityLogExportCsv(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long value,
			AsyncCallback<String> callback);

	public void getRecurringsListExportCsv(long fromDate, long toDate,
			AsyncCallback<String> callBack);

	public void getRemindersListExportCsv(AsyncCallback<String> callBack);

	public void getWarehousesExportCsv(AsyncCallback<String> callBack);

	void getWarehouseTransfersListExportCsv(AsyncCallback<String> callback);

	void getStockAdjustmentsExportCsv(AsyncCallback<String> callback);

	public void getAllUnitsExportCsv(AsyncCallback<String> callBack);

	public void getItemsExportCsv(boolean isPurchaseType, boolean isSaleType,
			String viewType, int itemType, AsyncCallback<String> callBack);

	public void getTaxItemsListExportCsv(String viewType,
			AsyncCallback<String> callBack);

	public void getTaxCodesListExportCsv(String selectedValue,
			AsyncCallback<String> callBack);

	public void getSalesPersonsListExportCsv(String selectedValue,
			AsyncCallback<String> exportCSVCallback);

}
