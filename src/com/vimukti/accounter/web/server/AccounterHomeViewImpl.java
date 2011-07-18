/**
 * 
 */
package com.vimukti.accounter.web.server;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.CashPurchase;
import com.vimukti.accounter.core.CashSales;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.CreditCardCharge;
import com.vimukti.accounter.core.CreditsAndPayments;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.CustomerRefund;
import com.vimukti.accounter.core.EnterBill;
import com.vimukti.accounter.core.Entry;
import com.vimukti.accounter.core.Estimate;
import com.vimukti.accounter.core.FinanceLogger;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.JournalEntry;
import com.vimukti.accounter.core.MakeDeposit;
import com.vimukti.accounter.core.PaySalesTaxEntries;
import com.vimukti.accounter.core.PayVATEntries;
import com.vimukti.accounter.core.ReceivePayment;
import com.vimukti.accounter.core.ReceiveVATEntries;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.TransactionMakeDeposit;
import com.vimukti.accounter.core.TransferFund;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.core.WriteCheck;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.IAccounterHomeViewService;
import com.vimukti.accounter.web.client.InvalidOperationException;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientCreditsAndPayments;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientEntry;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientFinanceLogger;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientJournalEntry;
import com.vimukti.accounter.web.client.core.ClientMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientPaySalesTaxEntries;
import com.vimukti.accounter.web.client.core.ClientPayVATEntries;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientReceiveVATEntries;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientVATReturn;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.core.Lists.CustomerRefundsList;
import com.vimukti.accounter.web.client.core.Lists.DepreciableFixedAssetsList;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetLinkedAccountMap;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetSellOrDisposeReviewJournal;
import com.vimukti.accounter.web.client.core.Lists.InvoicesList;
import com.vimukti.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.vimukti.accounter.web.client.core.Lists.OverDueInvoicesList;
import com.vimukti.accounter.web.client.core.Lists.PayBillTransactionList;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.core.Lists.PaymentsList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentTransactionList;
import com.vimukti.accounter.web.client.core.Lists.ReceivePaymentsList;
import com.vimukti.accounter.web.client.core.Lists.SalesOrdersList;
import com.vimukti.accounter.web.client.core.Lists.TempFixedAsset;
import com.vimukti.accounter.web.client.data.InvalidSessionException;
import com.vimukti.accounter.workspace.tool.FinanceTool;
import com.vimukti.accounter.workspace.tool.FinanceTool;

/**
 * @author Fernandez
 * 
 */
public class AccounterHomeViewImpl extends AccounterRPCBaseServiceImpl
		implements IAccounterHomeViewService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccounterHomeViewImpl() {
		super();

	}

	public List<ClientEnterBill> getBillsOwed() {

		List<ClientEnterBill> clientEnterBills = new ArrayList<ClientEnterBill>();
		@SuppressWarnings("unused")
		List<EnterBill> serverEnterBills = null;
		try {

			// serverEnterBills =
			// getFinanceTool().getBillsOwed();
			// for (EnterBill enterBill : serverEnterBills) {
			// clientEnterBills.add((ClientEnterBill) new
			// ClientConvertUtil().toClientObject(
			// serverEnterBills, ClientEnterBill.class));
			// }
			// bill = (List<ClientEnterBill>) manager.merge(bill);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientEnterBills;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.IAccounterHomeViewService#getLatestQuotes
	 * (com.vimukti.accounter.web.client.core.Company)
	 */

	public List<ClientEstimate> getLatestQuotes() {
		List<ClientEstimate> clientEstimates = new ArrayList<ClientEstimate>();
		List<Estimate> serverEstimates = null;
		try {

			serverEstimates = getFinanceTool().getLatestQuotes();
			for (Estimate estimate : serverEstimates) {
				clientEstimates.add(new ClientConvertUtil().toClientObject(
						estimate, ClientEstimate.class));
			}
			// estimate = (List<ClientEstimate>) manager.merge(estimate);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientEstimates;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.IAccounterHomeViewService#getOverDueInvoices
	 * (com.vimukti.accounter.web.client.core.Company)
	 */

	public List<OverDueInvoicesList> getOverDueInvoices() {
		List<OverDueInvoicesList> invoice = null;

		try {

			invoice = getFinanceTool().getOverDueInvoices();

			// invoice = (List<OverDueInvoicesList>) manager.merge(invoice);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return invoice;
	}

	public List<BillsList> getBillsAndItemReceiptList(boolean isExpensesList) {

		List<BillsList> billList = null;

		try {

			billList = getFinanceTool().getBillsList(isExpensesList);

			// billList = (List<BillsList>) manager.merge(billList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return billList;

	}

	public List<PaymentsList> getPaymentsList() {

		List<PaymentsList> paymentsList = null;

		try {

			paymentsList = getFinanceTool().getPaymentsList();

			// paymentsList = (List<PaymentsList>) manager.merge(paymentsList);

		} catch (Exception e) {
			e.printStackTrace();
			// return paymentsList;
		}

		return paymentsList;

	}

	public List<PayBillTransactionList> getTransactionPayBills() {

		List<PayBillTransactionList> paybillTrList = null;

		try {

			paybillTrList = getFinanceTool().getTransactionPayBills();

			// paybillTrList = (List<PayBillTransactionList>)
			// manager.merge(paybillTrList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return paybillTrList;

	}

	public List<ClientPaySalesTaxEntries> getPaySalesTaxEntries(
			long transactionDate) {
		List<ClientPaySalesTaxEntries> clientPaySlesTaxEntries = new ArrayList<ClientPaySalesTaxEntries>();
		try {
			List<PaySalesTaxEntries> paySalesTaxEntriesList = getFinanceTool()
					.getTransactionPaySalesTaxEntriesList(transactionDate);
			for (PaySalesTaxEntries salesTaxEntry : paySalesTaxEntriesList) {
				ClientPaySalesTaxEntries paySalesTxEntry = new ClientPaySalesTaxEntries();
				paySalesTxEntry.setStringID(salesTaxEntry.getStringID());
				paySalesTxEntry.setAmount(salesTaxEntry.getAmount());
				paySalesTxEntry.setBalance(salesTaxEntry.getBalance());
				// paySalesTxEntry.setStatus(salesTaxEntry.getTransaction()
				// .getStatus());
				paySalesTxEntry.setTaxAgency(salesTaxEntry.getTaxAgency()
						.getStringID());
				if (salesTaxEntry.getTaxRateCalculation() != null)
					paySalesTxEntry.setTaxRateCalculation(salesTaxEntry
							.getTaxRateCalculation().getStringID());
				if (salesTaxEntry.getTaxItem() != null)
					paySalesTxEntry.setTaxItem(salesTaxEntry.getTaxItem()
							.getStringID());
				if (salesTaxEntry.getTaxAdjustment() != null)
					paySalesTxEntry.setTaxAdjustment(salesTaxEntry
							.getTaxAdjustment().getStringID());

				paySalesTxEntry.setTransaction(salesTaxEntry.getTransaction()
						.getStringID());
				paySalesTxEntry.setTransactionDate(salesTaxEntry
						.getTransactionDate().getTime());
				clientPaySlesTaxEntries.add(paySalesTxEntry);
				// paySalesTxEntry
				// .setVoid(salesTaxEntry.getTransaction().isVoid());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientPaySlesTaxEntries;

	}

	public List<PayBillTransactionList> getTransactionPayBills(String vendorId) {

		List<PayBillTransactionList> paybillTrList = null;

		try {

			paybillTrList = getFinanceTool().getTransactionPayBills(vendorId);

			// paybillTrList = (List<PayBillTransactionList>) manager
			// .merge(paybillTrList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return paybillTrList;
	}

	public List<ClientCreditsAndPayments> getVendorCreditsAndPayments(
			String vendorId) {
		List<ClientCreditsAndPayments> clientCreditsAndPaymentsList = new ArrayList<ClientCreditsAndPayments>();
		List<CreditsAndPayments> serverCreditsAndPayments = null;
		try {

			serverCreditsAndPayments = getFinanceTool()
					.getVendorCreditsAndPayments(vendorId);
			for (CreditsAndPayments creditsAndPayments : serverCreditsAndPayments) {
				clientCreditsAndPaymentsList.add(new ClientConvertUtil()
						.toClientObject(creditsAndPayments,
								ClientCreditsAndPayments.class));
			}
			// creditsAndPaymentsList = (List<ClientCreditsAndPayments>) manager
			// .merge(creditsAndPaymentsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientCreditsAndPaymentsList;

	}

	public List<PaymentsList> getVendorPaymentsList() {

		List<PaymentsList> vendorPaymentsList = null;

		try {

			vendorPaymentsList = getFinanceTool().getVendorPaymentsList();

			// vendorPaymentsList = (List<PaymentsList>) manager
			// .merge(vendorPaymentsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vendorPaymentsList;
	}

	public List<IssuePaymentTransactionsList> getChecks() {

		List<IssuePaymentTransactionsList> checks = null;

		try {

			checks = getFinanceTool().getChecks();

			// checks = (List<IssuePaymentTransactionsList>)
			// manager.merge(checks);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return checks;

	}

	public List<IssuePaymentTransactionsList> getChecks(String accountId) {

		List<IssuePaymentTransactionsList> checks = null;

		try {

			checks = getFinanceTool().getChecks(accountId);

			// checks = (List<IssuePaymentTransactionsList>)
			// manager.merge(checks);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return checks;

	}

	public List<ClientCreditCardCharge> getCreditCardChargesThisMonth(long date) {
		List<ClientCreditCardCharge> clientCreditCardCharges = new ArrayList<ClientCreditCardCharge>();
		List<CreditCardCharge> serverCreditCardCharges = null;
		try {

			serverCreditCardCharges = getFinanceTool()
					.getCreditCardChargesThisMonth(date);
			for (CreditCardCharge creditCardCharge : serverCreditCardCharges) {
				clientCreditCardCharges.add(new ClientConvertUtil()
						.toClientObject(creditCardCharge,
								ClientCreditCardCharge.class));
			}
			// creditCardCharges = (List<ClientCreditCardCharge>) manager
			// .merge(creditCardCharges);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientCreditCardCharges;
	}

	public List<BillsList> getLatestBills() {
		List<BillsList> bills = null;

		try {

			bills = getFinanceTool().getLatestBills();

			// bills = (List<BillsList>) manager.merge(bills);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bills;
	}

	public List<ClientCashPurchase> getLatestCashPurchases() {
		List<ClientCashPurchase> clientCashPurchase = new ArrayList<ClientCashPurchase>();
		List<CashPurchase> serverCashPurchases = null;
		try {

			serverCashPurchases = getFinanceTool().getLatestCashPurchases();
			for (CashPurchase cashPurchase : serverCashPurchases) {
				clientCashPurchase.add(new ClientConvertUtil().toClientObject(
						cashPurchase, ClientCashPurchase.class));
			}
			// cashPurchase = (List<ClientCashPurchase>)
			// manager.merge(cashPurchase);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientCashPurchase;
	}

	public List<ClientCashSales> getLatestCashSales() {
		List<ClientCashSales> clientCashSales = new ArrayList<ClientCashSales>();
		List<CashSales> serverCashSales = null;
		try {

			serverCashSales = getFinanceTool().getLatestCashSales();
			for (CashSales cashSales : serverCashSales) {
				clientCashSales.add(new ClientConvertUtil().toClientObject(
						cashSales, ClientCashSales.class));
			}
			// cashSales = (List<ClientCashSales>) manager.merge(cashSales);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientCashSales;
	}

	public List<ClientWriteCheck> getLatestChecks() {
		List<ClientWriteCheck> clientWriteChecks = new ArrayList<ClientWriteCheck>();
		List<WriteCheck> serverWriteChecks = null;
		try {

			serverWriteChecks = getFinanceTool().getLatestChecks();
			for (WriteCheck writeCheck : serverWriteChecks) {
				clientWriteChecks.add(new ClientConvertUtil().toClientObject(
						writeCheck, ClientWriteCheck.class));
			}
			// checks = (List<ClientWriteCheck>) manager.merge(checks);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientWriteChecks;
	}

	public List<ClientCustomerRefund> getLatestCustomerRefunds() {
		List<ClientCustomerRefund> clientCustomerRefunds = new ArrayList<ClientCustomerRefund>();
		List<CustomerRefund> serverCustomerRefunds = null;
		try {

			serverCustomerRefunds = getFinanceTool().getLatestCustomerRefunds();
			for (CustomerRefund customerRefund : serverCustomerRefunds) {
				clientCustomerRefunds.add(new ClientConvertUtil()
						.toClientObject(customerRefund,
								ClientCustomerRefund.class));
			}
			// customerRefunds = (List<ClientCustomerRefund>) manager
			// .merge(customerRefunds);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientCustomerRefunds;
	}

	public List<ClientCustomer> getLatestCustomers() {
		List<ClientCustomer> clientCustomers = new ArrayList<ClientCustomer>();
		List<Customer> serverCustomers = null;
		try {

			serverCustomers = getFinanceTool().getLatestCustomers();
			for (Customer customer : serverCustomers) {
				clientCustomers.add(new ClientConvertUtil().toClientObject(
						customer, ClientCustomer.class));
			}
			// customers = (List<ClientCustomer>) manager.merge(customers);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientCustomers;
	}

	public List<ClientMakeDeposit> getLatestDeposits() {
		List<ClientMakeDeposit> clientMakeDeposits = new ArrayList<ClientMakeDeposit>();
		List<MakeDeposit> serverMakeDeposits = null;
		try {

			serverMakeDeposits = getFinanceTool().getLatestDeposits();
			for (MakeDeposit makeDeposit : serverMakeDeposits) {
				clientMakeDeposits.add(new ClientConvertUtil().toClientObject(
						makeDeposit, ClientMakeDeposit.class));
			}
			// deposites = (List<ClientMakeDeposit>) manager.merge(deposites);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientMakeDeposits;
	}

	public List<ClientTransferFund> getLatestFundsTransfer() {
		List<ClientTransferFund> clientTransferFunds = new ArrayList<ClientTransferFund>();
		List<TransferFund> serverTransferFunds = null;
		try {

			serverTransferFunds = getFinanceTool().getLatestFundsTransfer();
			for (TransferFund transferFund : serverTransferFunds) {
				clientTransferFunds.add(new ClientConvertUtil().toClientObject(
						transferFund, ClientTransferFund.class));
			}
			// transferFunds = (List<ClientTransferFund>)
			// manager.merge(transferFunds);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientTransferFunds;
	}

	public List<ClientItem> getLatestItems() {
		List<ClientItem> clientItems = new ArrayList<ClientItem>();
		List<Item> serverItems = null;
		try {

			serverItems = getFinanceTool().getLatestItems();
			for (Item item : serverItems) {
				clientItems.add(new ClientConvertUtil().toClientObject(item,
						ClientItem.class));
			}
			// items = (List<ClientItem>) manager.merge(items);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientItems;
	}

	public List<PaymentsList> getLatestPayments() {
		List<PaymentsList> payments = null;

		try {

			payments = getFinanceTool().getLatestPayments();

			// payments = (List<PaymentsList>) manager.merge(payments);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return payments;
	}

	public List<ClientVendor> getLatestVendors() {
		List<ClientVendor> clientVendors = new ArrayList<ClientVendor>();
		List<Vendor> serverVendors = null;
		try {

			serverVendors = getFinanceTool().getLatestVendors();
			for (Vendor vendor : serverVendors) {
				clientVendors.add(new ClientConvertUtil().toClientObject(
						vendor, ClientVendor.class));
			}
			// vendors = (List<ClientVendor>) manager.merge(vendors);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientVendors;
	}

	public String getNextTransactionNumber(int transactionType) {
		String nextTransactionNumber = "";
		try {

			nextTransactionNumber = getFinanceTool().getNextTransactionNumber(
					transactionType);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextTransactionNumber;

	}

	public boolean canVoidOrEdit(String invoiceOrVendorBillId) {
		boolean isCanVoidOrEdit = false;
		try {

			isCanVoidOrEdit = getFinanceTool().canVoidOrEdit(
					invoiceOrVendorBillId);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isCanVoidOrEdit;
	}

	public List<ClientCreditsAndPayments> getCustomerCreditsAndPayments(
			String customerId) {
		List<ClientCreditsAndPayments> clientCreditsAndPayments = new ArrayList<ClientCreditsAndPayments>();
		List<CreditsAndPayments> serverCreditsAndPayments = null;
		try {

			serverCreditsAndPayments = getFinanceTool()
					.getCustomerCreditsAndPayments(customerId);
			for (CreditsAndPayments creditsAndPayments : serverCreditsAndPayments) {
				clientCreditsAndPayments.add(new ClientConvertUtil()
						.toClientObject(creditsAndPayments,
								ClientCreditsAndPayments.class));
			}
			// creditsAndPayments = (List<ClientCreditsAndPayments>) manager
			// .merge(creditsAndPayments);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientCreditsAndPayments;
	}

	public List<CustomerRefundsList> getCustomerRefundsList() {
		List<CustomerRefundsList> customerRefundsList = null;
		try {

			customerRefundsList = getFinanceTool().getCustomerRefundsList();
			// customerRefundsList = (List<CustomerRefundsList>) manager
			// .merge(customerRefundsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerRefundsList;
	}

	public List<ClientEntry> getEntries(String journalEntryId) {
		List<ClientEntry> clientEntries = new ArrayList<ClientEntry>();
		List<Entry> serverEntries = null;
		try {

			serverEntries = getFinanceTool().getEntries(journalEntryId);
			for (Entry entry : serverEntries) {
				clientEntries.add(new ClientConvertUtil().toClientObject(entry,
						ClientEntry.class));
			}
			// entry = (List<ClientEntry>) manager.merge(entry);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientEntries;
	}

	public List<ClientEstimate> getEstimates() {
		List<ClientEstimate> clientEstimate = new ArrayList<ClientEstimate>();
		List<Estimate> serverEstimates = null;

		try {

			serverEstimates = getFinanceTool().getEstimates();
			for (Estimate estimate : serverEstimates) {
				clientEstimate.add(new ClientConvertUtil().toClientObject(
						estimate, ClientEstimate.class));
			}
			// estimate = (List<ClientEstimate>) manager.merge(estimate);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientEstimate;
	}

	public List<ClientEstimate> getEstimates(String customerId) {
		List<ClientEstimate> clientEstimate = new ArrayList<ClientEstimate>();
		List<Estimate> serverEstimates = null;
		try {

			serverEstimates = getFinanceTool().getEstimates(customerId);
			for (Estimate estimate : serverEstimates) {
				clientEstimate.add(new ClientConvertUtil().toClientObject(
						estimate, ClientEstimate.class));
			}
			// estimate = (List<ClientEstimate>) manager.merge(estimate);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientEstimate;
	}

	public List<InvoicesList> getInvoiceList() {
		List<InvoicesList> invoicesList = null;

		try {

			invoicesList = getFinanceTool().getInvoiceList();

			// invoicesList = (List<InvoicesList>) manager.merge(invoicesList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return invoicesList;
	}

	public List<InvoicesList> getInvoiceList(long fromDate, long toDate) {
		List<InvoicesList> invoicesList = null;
		List<InvoicesList> filteredList = null;
		// getMinimumAndMaximumDates(Utility.dateToString(fromDate), Utility
		// .dateToString(toDate));
		try {

			filteredList = new ArrayList<InvoicesList>();
			invoicesList = getFinanceTool().getInvoiceList();
			for (InvoicesList list : invoicesList) {
				if (!list.getDate().before(new ClientFinanceDate(fromDate))
						&& !list.getDate().after(new ClientFinanceDate(toDate)))

					filteredList.add(list);

			}
			return filteredList;
			// invoicesList = (List<InvoicesList>) manager.merge(invoicesList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return filteredList;
	}

	public List<ClientFinanceDate> getMinimumAndMaximumTransactionDate() {
		List<ClientFinanceDate> transactionDates = new ArrayList<ClientFinanceDate>();
		try {

			ClientFinanceDate[] dates = getFinanceTool()
					.getMinimumAndMaximumTransactionDate();
			transactionDates.add(dates[0]);
			transactionDates.add(dates[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionDates;
	}

	public List<ClientJournalEntry> getJournalEntries() {
		List<ClientJournalEntry> clientJournalEntries = new ArrayList<ClientJournalEntry>();
		List<JournalEntry> serverJournalEntries = null;
		try {

			serverJournalEntries = getFinanceTool().getJournalEntries();
			for (JournalEntry journalEntry : serverJournalEntries) {
				clientJournalEntries
						.add(new ClientConvertUtil().toClientObject(
								journalEntry, ClientJournalEntry.class));
			}
			// journalEntry = (List<ClientJournalEntry>)
			// manager.merge(journalEntry);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientJournalEntries;
	}

	public ClientJournalEntry getJournalEntry(String journalEntryId) {
		ClientJournalEntry clientJournalEntry = null;
		JournalEntry serverJournalEntry = null;
		try {

			serverJournalEntry = getFinanceTool().getJournalEntry(
					journalEntryId);
			clientJournalEntry = new ClientConvertUtil().toClientObject(
					serverJournalEntry, ClientJournalEntry.class);
			// journalEntry = (ClientJournalEntry) manager.merge(journalEntry);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientJournalEntry;
	}

	public Long getNextCheckNumber(String accountId) {
		Long nextCheckNumber = 0l;
		try {

			nextCheckNumber = getFinanceTool().getNextCheckNumber(accountId);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextCheckNumber;
	}

	public Long getNextIssuepaymentCheckNumber(String accountId) {
		Long nextCheckNumber = 0l;
		try {

			nextCheckNumber = getFinanceTool().getNextIssuePaymentCheckNumber(
					accountId);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextCheckNumber;
	}

	public String getNextVoucherNumber() {
		String nextVoucherNumber = "";
		try {

			nextVoucherNumber = getFinanceTool().getNextVoucherNumber();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextVoucherNumber;
	}

	public List<ClientItem> getPurchaseItems() {
		List<ClientItem> clientItems = new ArrayList<ClientItem>();
		List<Item> serverItems = null;
		try {

			serverItems = getFinanceTool().getPurchaseItems();
			for (Item item : serverItems) {
				clientItems.add(new ClientConvertUtil().toClientObject(item,
						ClientItem.class));
			}
			// item = (List<ClientItem>) manager.merge(item);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientItems;
	}

	public List<ClientItem> getSalesItems() {
		List<ClientItem> clientItems = new ArrayList<ClientItem>();
		List<Item> serverItems = null;

		try {

			serverItems = getFinanceTool().getSalesItems();
			for (Item item : serverItems) {
				clientItems.add(new ClientConvertUtil().toClientObject(item,
						ClientItem.class));
			}
			// item = (List<ClientItem>) manager.merge(item);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientItems;
	}

	public List<ClientAccount> getTaxAgencyAccounts() {
		List<ClientAccount> clientAccount = new ArrayList<ClientAccount>();
		List<Account> serverAccounts = null;
		try {

			serverAccounts = getFinanceTool().getTaxAgencyAccounts();
			for (Account account : serverAccounts) {
				clientAccount.add(new ClientConvertUtil().toClientObject(
						account, ClientAccount.class));
			}
			// account = (List<ClientAccount>) manager.merge(account);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientAccount;
	}

	public List<ReceivePaymentTransactionList> getTransactionReceivePayments(
			String customerId, long paymentDate) {
		List<ReceivePaymentTransactionList> receivePaymentTransactionList = null;

		try {

			receivePaymentTransactionList = getFinanceTool()
					.getTransactionReceivePayments(customerId, paymentDate);

			// receivePaymentTransactionList =
			// (List<ReceivePaymentTransactionList>) manager
			// .merge(receivePaymentTransactionList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return receivePaymentTransactionList;
	}

	public boolean isSalesTaxPayableAccount(String accountId) {
		boolean isSalesTaxPayable = false;
		try {

			isSalesTaxPayable = getFinanceTool().isSalesTaxPayableAccount(
					accountId);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isSalesTaxPayable;
	}

	public boolean isSalesTaxPayableAccountByName(String accountName) {
		boolean isSalesTaxPayable = false;
		try {

			isSalesTaxPayable = getFinanceTool().isSalesTaxPayableAccount(
					accountName);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isSalesTaxPayable;
	}

	public boolean isTaxAgencyAccount(String accountId) {
		boolean isTaxAgency = false;
		try {

			isTaxAgency = getFinanceTool().isTaxAgencyAccount(accountId);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isTaxAgency;
	}

	public List<ReceivePaymentsList> getReceivePaymentsList() {
		List<ReceivePaymentsList> receivePaymentList = null;

		try {

			receivePaymentList = getFinanceTool().getReceivePaymentsList();

			// receivePaymentList = (List<ReceivePaymentsList>) manager
			// .merge(receivePaymentList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return receivePaymentList;
	}

	public List<ClientItem> getLatestPurchaseItems() {
		List<ClientItem> clientItems = new ArrayList<ClientItem>();
		List<Item> serverItems = null;
		try {

			serverItems = getFinanceTool().getLatestPurchaseItems();
			for (Item item : serverItems) {
				clientItems.add(new ClientConvertUtil().toClientObject(item,
						ClientItem.class));
			}
			// item = (List<ClientItem>) manager.merge(item);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientItems;
	}

	public List<PaymentsList> getLatestVendorPayments() {
		List<PaymentsList> paymentsList = null;

		try {

			paymentsList = getFinanceTool().getLatestVendorPayments();

			// paymentsList = (List<PaymentsList>) manager.merge(paymentsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return paymentsList;
	}

	public List<ClientReceivePayment> getLatestReceivePayments() {
		List<ClientReceivePayment> clientReceivePaymentList = new ArrayList<ClientReceivePayment>();
		List<ReceivePayment> serverReceivePaymentList = null;
		try {

			serverReceivePaymentList = getFinanceTool()
					.getLatestReceivePayments();
			for (ReceivePayment receivePayment : serverReceivePaymentList) {
				clientReceivePaymentList.add(new ClientConvertUtil()
						.toClientObject(receivePayment,
								ClientReceivePayment.class));
			}
			// receivePaymentList = (List<ClientReceivePayment>) manager
			// .merge(receivePaymentList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientReceivePaymentList;
	}

	public ClientTransactionMakeDeposit getTransactionMakeDeposit(
			String transactionMakeDepositId) {
		ClientTransactionMakeDeposit clientTransactionMakeDeposit = null;
		TransactionMakeDeposit serverTransactionMakeDeposit = null;
		try {

			serverTransactionMakeDeposit = getFinanceTool()
					.getTransactionMakeDeposit(transactionMakeDepositId);
			clientTransactionMakeDeposit = new ClientConvertUtil()
					.toClientObject(serverTransactionMakeDeposit,
							ClientTransactionMakeDeposit.class);

			// transactionMakeDeposit = (ClientTransactionMakeDeposit) manager
			// .merge(transactionMakeDeposit);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientTransactionMakeDeposit;
	}

	public List<ClientTransactionMakeDeposit> getTransactionMakeDeposits() {
		List<ClientTransactionMakeDeposit> makeDepositTransactionsList = null;

		try {

			makeDepositTransactionsList = getFinanceTool()
					.getTransactionMakeDeposits();

			// makeDepositTransactionsList = (List<MakeDepositTransactionsList>)
			// manager
			// .merge(makeDepositTransactionsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return makeDepositTransactionsList;
	}

	@Override
	public List<PurchaseOrdersList> getPurchaseOrders()
			throws InvalidSessionException {

		FinanceTool tool = getFinanceTool();

		try {
			return tool != null ? tool.getPurchaseOrdersList() : null;
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<SalesOrdersList> getPurchaseOrdersForVendor(String vendorID)
			throws InvalidSessionException {

		FinanceTool tool = getFinanceTool();

		return tool != null ? tool.getPurchaseOrdersForVendor(vendorID) : null;
	}

	@Override
	public List<SalesOrdersList> getSalesOrders()
			throws InvalidSessionException {

		FinanceTool tool = getFinanceTool();

		try {
			return tool != null ? tool.getSalesOrdersList() : null;
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<SalesOrdersList> getSalesOrdersForCustomer(String customerID)
			throws InvalidSessionException {

		FinanceTool tool = getFinanceTool();

		return tool != null ? tool.getSalesOrdersForCustomer(customerID) : null;

	}

	@Override
	public List<PurchaseOrdersList> getNotReceivedPurchaseOrdersList(
			String vendorID) throws InvalidSessionException {

		FinanceTool tool = getFinanceTool();

		try {
			return tool != null ? tool
					.getNotReceivedPurchaseOrdersList(vendorID) : null;
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public ClientPurchaseOrder getPurchaseOrderById(String transactionId) {

		return null;
	}

	@Override
	public List<PurchaseOrdersAndItemReceiptsList> getPurchasesAndItemReceiptsList(
			String vendorId) throws InvalidSessionException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool
					.getPurchasesAndItemReceiptsList(vendorId) : null;
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public List<EstimatesAndSalesOrdersList> getEstimatesAndSalesOrdersList(
			String customerId) throws InvalidSessionException {

		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool
					.getEstimatesAndSalesOrdersList(customerId) : null;
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	// @Override
	// public List<ClientDepreciationFixedAsset> getDepreciationFixedAssets(
	// Date startDate, Date toDate) {
	// try {
	// FinanceTool tool = getFinanceTool();
	// return tool != null ? tool.getDepreciationFixedAssets(startDate,
	// toDate) : null;
	// } catch (DAOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return null;
	// }

	@Override
	public ClientVATReturn getTAXReturn(ClientTAXAgency taxAgency,
			long fromDate, long toDate) throws InvalidOperationException,
			InvalidSessionException {

		try {
			FinanceTool tool = getFinanceTool();

			TAXAgency serverVatAgency = new ServerConvertUtil().toServerObject(
					new TAXAgency(), taxAgency, getSession());

			return new ClientConvertUtil().toClientObject(tool
					.getVATReturnDetails(serverVatAgency, fromDate, toDate),
					ClientVATReturn.class);

		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public FixedAssetSellOrDisposeReviewJournal getReviewJournal(
			TempFixedAsset fixedAsset) throws InvalidSessionException {
		try {
			return getFinanceTool().getReviewJournal(fixedAsset);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DepreciableFixedAssetsList getDepreciableFixedAssets(
			long depreciationFrom, long depreciationTo)
			throws InvalidSessionException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool.getDepreciableFixedAssets(
					depreciationFrom, depreciationTo) : null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ClientFinanceDate> getAllDepreciationFromDates()
			throws InvalidSessionException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool.getAllDepreciationFromDates() : null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ClientFinanceDate getDepreciationLastDate()
			throws InvalidSessionException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool.getDepreciationLastDate() : null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<ClientFinanceDate> getFinancialYearStartDates()
			throws InvalidSessionException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool.getFinancialYearStartDates() : null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void rollBackDepreciation(long rollBackDepreciationTo)
			throws InvalidSessionException {
		try {
			FinanceTool tool = getFinanceTool();
			tool.rollBackDepreciation(rollBackDepreciationTo);
		} catch (DAOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void changeDepreciationStartDateTo(long newStartDate)
			throws InvalidSessionException {
		try {
			FinanceTool tool = getFinanceTool();
			tool.changeDepreciationStartDateTo(newStartDate);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public double getAccumulatedDepreciationAmount(int depreciationMethod,
			double depreciationRate, double purchasePrice,
			long depreciationfromDate, long depreciationtoDate)
			throws InvalidSessionException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool.getCalculatedDepreciatedAmount(depreciationMethod,
					depreciationRate, purchasePrice, depreciationfromDate,
					depreciationtoDate);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	public Long getNextNominalCode(int accountType) {
		Long nextTransactionNumber = 0l;
		try {

			nextTransactionNumber = getFinanceTool().getNextNominalCode(
					accountType);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextTransactionNumber;

	}

	public boolean createTaxes(int[] vatReturnType) {
		try {
			getFinanceTool().createTaxes(vatReturnType);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public String getNextFixedAssetNumber() {
		String nextFixedAssestNumber = "";
		try {

			nextFixedAssestNumber = getFinanceTool().getNextFixedAssetNumber();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextFixedAssestNumber;
	}

	@Override
	public boolean changeFiscalYearsStartDateTo(long newStartDate) {
		try {
			getFinanceTool().changeFiscalYearsStartDateTo(newStartDate);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void runDepreciation(long depreciationFrom, long depreciationTo,
			FixedAssetLinkedAccountMap linkedAccounts) {
		try {
			getFinanceTool().runDepreciation(depreciationFrom, depreciationTo,
					linkedAccounts);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<ClientPayVATEntries> getPayVATEntries()
			throws InvalidSessionException {

		List<ClientPayVATEntries> clientEntries = new ArrayList<ClientPayVATEntries>();

		FinanceTool tool = getFinanceTool();
		if (tool == null)
			return clientEntries;
		List<PayVATEntries> entries = tool.getPayVATEntries();
		for (PayVATEntries entry : entries) {
			ClientPayVATEntries clientEntry = new ClientPayVATEntries();
			// VATReturn vatReturn =(VATReturn) entry.getTransaction();
			// ClientVATReturn clientVATReturn = new
			// ClientConvertUtil().toClientObject(vatReturn,ClientVATReturn.class);
			clientEntry.setVatReturn(entry.getTransaction().getStringID());
			clientEntry.setVatAgency(entry.getTaxAgency() != null ? entry
					.getTaxAgency().getStringID() : null);
			clientEntry.setBalance(entry.getBalance());
			clientEntry.setAmount(entry.getAmount());

			clientEntries.add(clientEntry);
		}
		return clientEntries;

	}

	@Override
	public List<ClientFinanceLogger> getLog(long id, boolean isNext)
			throws InvalidSessionException {

		List<ClientFinanceLogger> clientLogs = new ArrayList<ClientFinanceLogger>();

		FinanceTool tool = (FinanceTool) getFinanceTool();
		if (tool == null)
			return clientLogs;
		List<FinanceLogger> logs = tool.getLog(id, isNext);

		if (logs == null) {
			return clientLogs;
		}

		for (FinanceLogger log : logs) {
			ClientFinanceLogger clientLog = new ClientFinanceLogger();

			clientLog.setDescription(log.getDescription());
			clientLog.setLogMessge(log.getLogMessge());
			clientLog.setCreatedBy(log.getCreatedBy());
			clientLog.setCreatedDate(log.getCreatedDate().getTime());
			clientLog.setId(log.getId());

			clientLogs.add(clientLog);
		}

		return clientLogs;

	}

	@Override
	public List<ClientFinanceLogger> getLog(String date, long id, boolean isNext)
			throws InvalidSessionException {

		List<ClientFinanceLogger> clientLogs = new ArrayList<ClientFinanceLogger>();

		FinanceTool tool = (FinanceTool) getFinanceTool();
		if (tool == null)
			return clientLogs;
		List<FinanceLogger> logs = tool.getLog(date, id, isNext);

		for (FinanceLogger log : logs) {
			ClientFinanceLogger clientLog = new ClientFinanceLogger();

			clientLog.setId(log.getId());
			clientLog.setDescription(log.getDescription());
			clientLog.setLogMessge(log.getLogMessge());
			clientLog.setCreatedDate(log.getCreatedDate().getTime());
			clientLog.setCreatedBy(log.getCreatedBy());

			clientLogs.add(clientLog);
		}

		return clientLogs;

	}

	@Override
	public List<PayeeList> getPayeeList(int transactionCategory) {

		List<PayeeList> payeeList = null;

		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool.getPayeeList(transactionCategory) : null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return payeeList;

	}

	@Override
	public String getCustomerNumber() {
		String nextCustomerNumber = "";
		try {

			nextCustomerNumber = getFinanceTool().getNextCustomerNumber();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextCustomerNumber;

	}

	@Override
	public List<ClientReceiveVATEntries> getReceiveVATEntries()
			throws InvalidSessionException {

		List<ClientReceiveVATEntries> clientEntries = new ArrayList<ClientReceiveVATEntries>();

		FinanceTool tool = getFinanceTool();
		if (tool == null)
			return clientEntries;
		List<ReceiveVATEntries> entries = tool.getReceiveVATEntries();
		for (ReceiveVATEntries entry : entries) {
			ClientReceiveVATEntries clientEntry = new ClientReceiveVATEntries();
			// VATReturn vatReturn =(VATReturn) entry.getTransaction();
			// ClientVATReturn clientVATReturn = new
			// ClientConvertUtil().toClientObject(vatReturn,ClientVATReturn.class);
			clientEntry.setVatReturn(entry.getTransaction().getStringID());
			clientEntry.setVatAgency(entry.getTaxAgency() != null ? entry
					.getTaxAgency().getStringID() : null);
			clientEntry.setBalance(entry.getBalance());
			clientEntry.setAmount(entry.getAmount());

			clientEntries.add(clientEntry);
		}
		return clientEntries;

	}

	@Override
	public List<Double> getGraphPointsforAccount(int chartType, long accountNo) {

		List<Double> resultList = null;
		try {

			resultList = getFinanceTool().getGraphPointsforAccount(chartType,
					accountNo);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultList;
	}

	@Override
	public List<BillsList> getEmployeeExpensesByStatus(String userName,
			int status) {
		List<BillsList> resultList = null;
		try {
			resultList = getFinanceTool().getEmployeeExpensesByStatus(userName,
					status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	@Override
	public boolean changePassWord(String emailID, String oldPassword,
			String newPassword) {
		boolean changePassword = false;
		try {
			changePassword = getFinanceTool().changeMyPassword(emailID,
					oldPassword, newPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return changePassword;
	}

	public List<ClientUser> getAllUsers() throws InvalidSessionException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {
			List<ClientUser> allUsers = tool.getAllUsers();
			return allUsers;
		}
		return null;
	}

}