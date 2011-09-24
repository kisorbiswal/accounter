/**
 * 
 */
package com.vimukti.accounter.web.server;

import java.io.IOException;
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
import com.vimukti.accounter.core.FinanceDate;
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
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientActivity;
import com.vimukti.accounter.web.client.core.ClientBudget;
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
import com.vimukti.accounter.web.client.core.ClientReceivePayment;
import com.vimukti.accounter.web.client.core.ClientReceiveVATEntries;
import com.vimukti.accounter.web.client.core.ClientRecurringTransaction;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTransactionMakeDeposit;
import com.vimukti.accounter.web.client.core.ClientTransferFund;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.core.ClientVATReturn;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.PaginationList;
import com.vimukti.accounter.web.client.core.Lists.BillsList;
import com.vimukti.accounter.web.client.core.Lists.ClientTDSInfo;
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
import com.vimukti.accounter.web.client.exception.AccounterException;

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

	public ArrayList<ClientEnterBill> getBillsOwed() {

		List<ClientEnterBill> clientEnterBills = new ArrayList<ClientEnterBill>();

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

		return new ArrayList<ClientEnterBill>(clientEnterBills);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.IAccounterHomeViewService#getLatestQuotes
	 * (com.vimukti.accounter.web.client.core.Company)
	 */

	public ArrayList<ClientEstimate> getLatestQuotes() {
		List<ClientEstimate> clientEstimates = new ArrayList<ClientEstimate>();
		List<Estimate> serverEstimates = null;
		try {

			serverEstimates = getFinanceTool().getLatestQuotes(getCompanyId());
			for (Estimate estimate : serverEstimates) {
				clientEstimates.add(new ClientConvertUtil().toClientObject(
						estimate, ClientEstimate.class));
			}
			// estimate = (List<ClientEstimate>) manager.merge(estimate);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientEstimate>(clientEstimates);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.IAccounterHomeViewService#getOverDueInvoices
	 * (com.vimukti.accounter.web.client.core.Company)
	 */

	public ArrayList<OverDueInvoicesList> getOverDueInvoices() {
		List<OverDueInvoicesList> invoice = null;

		try {

			invoice = getFinanceTool().getOverDueInvoices(getCompanyId());

			// invoice = (List<OverDueInvoicesList>) manager.merge(invoice);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<OverDueInvoicesList>(invoice);
	}

	public ArrayList<BillsList> getBillsAndItemReceiptList(
			boolean isExpensesList) {

		List<BillsList> billList = null;

		try {

			billList = getFinanceTool().getBillsList(isExpensesList,
					getCompanyId());

			// billList = (List<BillsList>) manager.merge(billList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<BillsList>(billList);

	}

	public ArrayList<PaymentsList> getPaymentsList() {

		List<PaymentsList> paymentsList = null;

		try {

			paymentsList = getFinanceTool().getPaymentsList(getCompanyId());

			// paymentsList = (List<PaymentsList>) manager.merge(paymentsList);

		} catch (Exception e) {
			e.printStackTrace();
			// return paymentsList;
		}

		return new ArrayList<PaymentsList>(paymentsList);

	}

	public ArrayList<PayBillTransactionList> getTransactionPayBills() {

		List<PayBillTransactionList> paybillTrList = null;

		try {

			paybillTrList = getFinanceTool().getTransactionPayBills(
					getCompanyId());

			// paybillTrList = (List<PayBillTransactionList>)
			// manager.merge(paybillTrList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<PayBillTransactionList>(paybillTrList);

	}

	public ArrayList<ClientPaySalesTaxEntries> getPaySalesTaxEntries(
			long transactionDate) {
		List<ClientPaySalesTaxEntries> clientPaySlesTaxEntries = new ArrayList<ClientPaySalesTaxEntries>();
		try {
			List<PaySalesTaxEntries> paySalesTaxEntriesList = getFinanceTool()
					.getTransactionPaySalesTaxEntriesList(transactionDate,
							getCompanyId());
			for (PaySalesTaxEntries salesTaxEntry : paySalesTaxEntriesList) {
				ClientPaySalesTaxEntries paySalesTxEntry = new ClientPaySalesTaxEntries();
				paySalesTxEntry.setID(salesTaxEntry.getID());
				paySalesTxEntry.setAmount(salesTaxEntry.getAmount());
				paySalesTxEntry.setBalance(salesTaxEntry.getBalance());
				// paySalesTxEntry.setStatus(salesTaxEntry.getTransaction()
				// .getStatus());
				paySalesTxEntry.setTaxAgency(salesTaxEntry.getTaxAgency()
						.getID());
				if (salesTaxEntry.getTaxRateCalculation() != null)
					paySalesTxEntry.setTaxRateCalculation(salesTaxEntry
							.getTaxRateCalculation().getID());
				if (salesTaxEntry.getTaxItem() != null)
					paySalesTxEntry.setTaxItem(salesTaxEntry.getTaxItem()
							.getID());
				if (salesTaxEntry.getTaxAdjustment() != null)
					paySalesTxEntry.setTaxAdjustment(salesTaxEntry
							.getTaxAdjustment().getID());

				// paySalesTxEntry.setTransaction(salesTaxEntry.getTransaction()
				// .getID());
				paySalesTxEntry.setTransactionDate(salesTaxEntry
						.getTransactionDate().getDate());
				clientPaySlesTaxEntries.add(paySalesTxEntry);
				// paySalesTxEntry
				// .setVoid(salesTaxEntry.getTransaction().isVoid());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientPaySalesTaxEntries>(clientPaySlesTaxEntries);

	}

	public ArrayList<PayBillTransactionList> getTransactionPayBills(
			long vendorId) {

		List<PayBillTransactionList> paybillTrList = null;

		try {

			paybillTrList = getFinanceTool().getTransactionPayBills(vendorId);

			// paybillTrList = (List<PayBillTransactionList>) manager
			// .merge(paybillTrList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<PayBillTransactionList>(paybillTrList);
	}

	public ArrayList<ClientCreditsAndPayments> getVendorCreditsAndPayments(
			long vendorId) {
		List<ClientCreditsAndPayments> clientCreditsAndPaymentsList = new ArrayList<ClientCreditsAndPayments>();
		List<CreditsAndPayments> serverCreditsAndPayments = null;
		try {

			serverCreditsAndPayments = getFinanceTool()
					.getVendorCreditsAndPayments(vendorId, getCompanyId());
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

		return new ArrayList<ClientCreditsAndPayments>(
				clientCreditsAndPaymentsList);

	}

	public ArrayList<PaymentsList> getVendorPaymentsList() {

		ArrayList<PaymentsList> vendorPaymentsList = new ArrayList<PaymentsList>();
		try {

			vendorPaymentsList = getFinanceTool().getVendorPaymentsList(
					getCompanyId());

			// vendorPaymentsList = (List<PaymentsList>) manager
			// .merge(vendorPaymentsList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vendorPaymentsList;

	}

	public ArrayList<IssuePaymentTransactionsList> getChecks() {

		List<IssuePaymentTransactionsList> checks = null;

		try {

			checks = getFinanceTool().getChecks(getCompanyId());

			// checks = (List<IssuePaymentTransactionsList>)
			// manager.merge(checks);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<IssuePaymentTransactionsList>(checks);

	}

	public ArrayList<IssuePaymentTransactionsList> getChecks(long accountId) {

		List<IssuePaymentTransactionsList> checks = null;

		try {

			checks = getFinanceTool().getChecks(accountId, getCompanyId());

			// checks = (List<IssuePaymentTransactionsList>)
			// manager.merge(checks);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<IssuePaymentTransactionsList>(checks);

	}

	public ArrayList<ClientCreditCardCharge> getCreditCardChargesThisMonth(
			long date) {
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

		return new ArrayList<ClientCreditCardCharge>(clientCreditCardCharges);
	}

	public ArrayList<BillsList> getLatestBills() {
		List<BillsList> bills = null;

		try {

			bills = getFinanceTool().getLatestBills(getCompanyId());

			// bills = (List<BillsList>) manager.merge(bills);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<BillsList>(bills);
	}

	public ArrayList<ClientCashPurchase> getLatestCashPurchases() {
		List<ClientCashPurchase> clientCashPurchase = new ArrayList<ClientCashPurchase>();
		List<CashPurchase> serverCashPurchases = null;
		try {

			serverCashPurchases = getFinanceTool().getLatestCashPurchases(
					getCompanyId());
			for (CashPurchase cashPurchase : serverCashPurchases) {
				clientCashPurchase.add(new ClientConvertUtil().toClientObject(
						cashPurchase, ClientCashPurchase.class));
			}
			// cashPurchase = (List<ClientCashPurchase>)
			// manager.merge(cashPurchase);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientCashPurchase>(clientCashPurchase);
	}

	public ArrayList<ClientBudget> getBudgetList() {

		List<ClientBudget> budgetList = null;
		try {

			budgetList = getFinanceTool().getBudgetList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ClientBudget>(budgetList);
	}

	public ArrayList<ClientCashSales> getLatestCashSales() {
		List<ClientCashSales> clientCashSales = new ArrayList<ClientCashSales>();
		List<CashSales> serverCashSales = null;
		try {

			serverCashSales = getFinanceTool().getLatestCashSales(
					getCompanyId());
			for (CashSales cashSales : serverCashSales) {
				clientCashSales.add(new ClientConvertUtil().toClientObject(
						cashSales, ClientCashSales.class));
			}
			// cashSales = (List<ClientCashSales>) manager.merge(cashSales);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientCashSales>(clientCashSales);
	}

	public ArrayList<ClientWriteCheck> getLatestChecks() {
		List<ClientWriteCheck> clientWriteChecks = new ArrayList<ClientWriteCheck>();
		List<WriteCheck> serverWriteChecks = null;
		try {

			serverWriteChecks = getFinanceTool()
					.getLatestChecks(getCompanyId());
			for (WriteCheck writeCheck : serverWriteChecks) {
				clientWriteChecks.add(new ClientConvertUtil().toClientObject(
						writeCheck, ClientWriteCheck.class));
			}
			// checks = (List<ClientWriteCheck>) manager.merge(checks);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientWriteCheck>(clientWriteChecks);
	}

	public ArrayList<ClientCustomerRefund> getLatestCustomerRefunds() {
		List<ClientCustomerRefund> clientCustomerRefunds = new ArrayList<ClientCustomerRefund>();
		List<CustomerRefund> serverCustomerRefunds = null;
		try {

			serverCustomerRefunds = getFinanceTool().getLatestCustomerRefunds(
					getCompanyId());
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

		return new ArrayList<ClientCustomerRefund>(clientCustomerRefunds);
	}

	public ArrayList<ClientCustomer> getLatestCustomers() {
		List<ClientCustomer> clientCustomers = new ArrayList<ClientCustomer>();
		List<Customer> serverCustomers = null;
		try {

			serverCustomers = getFinanceTool().getLatestCustomers(
					getCompanyId());
			for (Customer customer : serverCustomers) {
				clientCustomers.add(new ClientConvertUtil().toClientObject(
						customer, ClientCustomer.class));
			}
			// customers = (List<ClientCustomer>) manager.merge(customers);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientCustomer>(clientCustomers);
	}

	public ArrayList<ClientMakeDeposit> getLatestDeposits() {
		List<ClientMakeDeposit> clientMakeDeposits = new ArrayList<ClientMakeDeposit>();
		List<MakeDeposit> serverMakeDeposits = null;
		try {

			serverMakeDeposits = getFinanceTool().getLatestDeposits(
					getCompanyId());
			for (MakeDeposit makeDeposit : serverMakeDeposits) {
				clientMakeDeposits.add(new ClientConvertUtil().toClientObject(
						makeDeposit, ClientMakeDeposit.class));
			}
			// deposites = (List<ClientMakeDeposit>) manager.merge(deposites);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientMakeDeposit>(clientMakeDeposits);
	}

	public ArrayList<ClientTransferFund> getLatestFundsTransfer() {
		List<ClientTransferFund> clientTransferFunds = new ArrayList<ClientTransferFund>();
		List<TransferFund> serverTransferFunds = null;
		try {

			serverTransferFunds = getFinanceTool().getLatestFundsTransfer(
					getCompanyId());
			for (TransferFund transferFund : serverTransferFunds) {
				clientTransferFunds.add(new ClientConvertUtil().toClientObject(
						transferFund, ClientTransferFund.class));
			}
			// transferFunds = (List<ClientTransferFund>)
			// manager.merge(transferFunds);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientTransferFund>(clientTransferFunds);
	}

	public ArrayList<ClientItem> getLatestItems() {
		List<ClientItem> clientItems = new ArrayList<ClientItem>();
		List<Item> serverItems = null;
		try {

			serverItems = getFinanceTool().getLatestItems(getCompanyId());
			for (Item item : serverItems) {
				clientItems.add(new ClientConvertUtil().toClientObject(item,
						ClientItem.class));
			}
			// items = (List<ClientItem>) manager.merge(items);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientItem>(clientItems);
	}

	public ArrayList<PaymentsList> getLatestPayments() {
		List<PaymentsList> payments = null;

		try {

			payments = getFinanceTool().getLatestPayments(getCompanyId());

			// payments = (List<PaymentsList>) manager.merge(payments);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<PaymentsList>(payments);
	}

	public ArrayList<ClientVendor> getLatestVendors() {
		List<ClientVendor> clientVendors = new ArrayList<ClientVendor>();
		List<Vendor> serverVendors = null;
		try {

			serverVendors = getFinanceTool().getLatestVendors(getCompanyId());
			for (Vendor vendor : serverVendors) {
				clientVendors.add(new ClientConvertUtil().toClientObject(
						vendor, ClientVendor.class));
			}
			// vendors = (List<ClientVendor>) manager.merge(vendors);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientVendor>(clientVendors);
	}

	public String getNextTransactionNumber(int transactionType) {
		String nextTransactionNumber = "";
		try {

			nextTransactionNumber = getFinanceTool().getNextTransactionNumber(
					transactionType, getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextTransactionNumber;

	}

	public boolean canVoidOrEdit(long invoiceOrVendorBillId) {
		boolean isCanVoidOrEdit = false;
		try {

			isCanVoidOrEdit = getFinanceTool().canVoidOrEdit(
					invoiceOrVendorBillId, getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isCanVoidOrEdit;
	}

	public ArrayList<ClientCreditsAndPayments> getCustomerCreditsAndPayments(
			long customerId) {
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

		return new ArrayList<ClientCreditsAndPayments>(clientCreditsAndPayments);
	}

	public ArrayList<CustomerRefundsList> getCustomerRefundsList() {
		List<CustomerRefundsList> customerRefundsList = null;
		try {

			customerRefundsList = getFinanceTool().getCustomerRefundsList(
					getCompanyId());
			// customerRefundsList = (List<CustomerRefundsList>) manager
			// .merge(customerRefundsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<CustomerRefundsList>(customerRefundsList);
	}

	public ArrayList<ClientEntry> getEntries(long journalEntryId) {
		List<ClientEntry> clientEntries = new ArrayList<ClientEntry>();
		List<Entry> serverEntries = null;
		try {

			serverEntries = getFinanceTool().getEntries(journalEntryId,
					getCompanyId());
			for (Entry entry : serverEntries) {
				clientEntries.add(new ClientConvertUtil().toClientObject(entry,
						ClientEntry.class));
			}
			// entry = (List<ClientEntry>) manager.merge(entry);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientEntry>(clientEntries);
	}

	public ArrayList<ClientEstimate> getEstimates() {
		List<ClientEstimate> clientEstimate = new ArrayList<ClientEstimate>();
		List<Estimate> serverEstimates = null;

		try {

			serverEstimates = getFinanceTool().getEstimates(getCompanyId());
			for (Estimate estimate : serverEstimates) {
				clientEstimate.add(new ClientConvertUtil().toClientObject(
						estimate, ClientEstimate.class));
			}
			// estimate = (List<ClientEstimate>) manager.merge(estimate);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientEstimate>(clientEstimate);
	}

	public ArrayList<ClientEstimate> getEstimates(long customerId) {
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

		return new ArrayList<ClientEstimate>(clientEstimate);
	}

	public ArrayList<InvoicesList> getInvoiceList() {
		List<InvoicesList> invoicesList = null;

		try {

			invoicesList = getFinanceTool().getInvoiceList(getCompanyId());

			// invoicesList = (List<InvoicesList>) manager.merge(invoicesList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<InvoicesList>(invoicesList);
	}

	public ArrayList<InvoicesList> getInvoiceList(long fromDate, long toDate) {
		List<InvoicesList> invoicesList = null;
		List<InvoicesList> filteredList = null;
		// getMinimumAndMaximumDates(Utility.dateToString(fromDate), Utility
		// .dateToString(toDate));
		try {

			filteredList = new ArrayList<InvoicesList>();
			invoicesList = getFinanceTool().getInvoiceList(getCompanyId());
			for (InvoicesList list : invoicesList) {
				if (!list.getDate().before(new ClientFinanceDate(fromDate))
						&& !list.getDate().after(new ClientFinanceDate(toDate)))

					filteredList.add(list);

			}
			return new ArrayList<InvoicesList>(filteredList);
			// invoicesList = (List<InvoicesList>) manager.merge(invoicesList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<InvoicesList>(filteredList);
	}

	public ArrayList<ClientFinanceDate> getMinimumAndMaximumTransactionDate() {
		List<ClientFinanceDate> transactionDates = new ArrayList<ClientFinanceDate>();
		try {

			ClientFinanceDate[] dates = getFinanceTool()
					.getMinimumAndMaximumTransactionDate(getCompanyId());
			transactionDates.add(dates[0]);
			transactionDates.add(dates[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<ClientFinanceDate>(transactionDates);
	}

	public ArrayList<ClientJournalEntry> getJournalEntries() {
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

		return new ArrayList<ClientJournalEntry>(clientJournalEntries);
	}

	public ClientJournalEntry getJournalEntry(long journalEntryId) {
		ClientJournalEntry clientJournalEntry = null;
		JournalEntry serverJournalEntry = null;
		try {

			serverJournalEntry = getFinanceTool().getJournalEntry(
					journalEntryId, getCompanyId());
			clientJournalEntry = new ClientConvertUtil().toClientObject(
					serverJournalEntry, ClientJournalEntry.class);
			// journalEntry = (ClientJournalEntry) manager.merge(journalEntry);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientJournalEntry;
	}

	public Long getNextCheckNumber(long accountId) {
		Long nextCheckNumber = 0l;
		try {

			nextCheckNumber = getFinanceTool().getNextCheckNumber(accountId);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextCheckNumber;
	}

	public Long getNextIssuepaymentCheckNumber(long accountId) {
		Long nextCheckNumber = 0l;
		try {

			nextCheckNumber = getFinanceTool().getNextIssuePaymentCheckNumber(
					accountId, getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextCheckNumber;
	}

	public String getNextVoucherNumber() {
		String nextVoucherNumber = "";
		try {

			nextVoucherNumber = getFinanceTool().getNextVoucherNumber(
					getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextVoucherNumber;
	}

	public ArrayList<ClientItem> getPurchaseItems() {
		List<ClientItem> clientItems = new ArrayList<ClientItem>();
		List<Item> serverItems = null;
		try {

			serverItems = getFinanceTool().getPurchaseItems(getCompanyId());
			for (Item item : serverItems) {
				clientItems.add(new ClientConvertUtil().toClientObject(item,
						ClientItem.class));
			}
			// item = (List<ClientItem>) manager.merge(item);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientItem>(clientItems);
	}

	public ArrayList<ClientItem> getSalesItems() {
		List<ClientItem> clientItems = new ArrayList<ClientItem>();
		List<Item> serverItems = null;

		try {

			serverItems = getFinanceTool().getSalesItems(getCompanyId());
			for (Item item : serverItems) {
				clientItems.add(new ClientConvertUtil().toClientObject(item,
						ClientItem.class));
			}
			// item = (List<ClientItem>) manager.merge(item);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientItem>(clientItems);
	}

	public ArrayList<ClientAccount> getTaxAgencyAccounts() {
		List<ClientAccount> clientAccount = new ArrayList<ClientAccount>();
		List<Account> serverAccounts = null;
		try {

			serverAccounts = getFinanceTool().getTaxAgencyAccounts(
					getCompanyId());
			for (Account account : serverAccounts) {
				clientAccount.add(new ClientConvertUtil().toClientObject(
						account, ClientAccount.class));
			}
			// account = (List<ClientAccount>) manager.merge(account);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ClientAccount>(clientAccount);
	}

	public ArrayList<ReceivePaymentTransactionList> getTransactionReceivePayments(
			long customerId, long paymentDate) throws AccounterException {
		List<ReceivePaymentTransactionList> receivePaymentTransactionList = null;

		receivePaymentTransactionList = getFinanceTool()
				.getTransactionReceivePayments(customerId, paymentDate,
						getCompanyId());

		// receivePaymentTransactionList =
		// (List<ReceivePaymentTransactionList>) manager
		// .merge(receivePaymentTransactionList);

		return new ArrayList<ReceivePaymentTransactionList>(
				receivePaymentTransactionList);
	}

	public boolean isSalesTaxPayableAccount(long accountId) {
		boolean isSalesTaxPayable = false;
		try {
			FinanceTool tool = getFinanceTool();
			isSalesTaxPayable = tool.isSalesTaxPayableAccount(accountId,
					getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isSalesTaxPayable;
	}

	public boolean isSalesTaxPayableAccountByName(String accountName) {
		boolean isSalesTaxPayable = false;
		try {

			isSalesTaxPayable = getFinanceTool()
					.isSalesTaxPayableAccountByName(accountName, getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isSalesTaxPayable;
	}

	public boolean isTaxAgencyAccount(long accountId) {
		boolean isTaxAgency = false;
		try {

			isTaxAgency = getFinanceTool().isTaxAgencyAccount(accountId);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isTaxAgency;
	}

	public ArrayList<ReceivePaymentsList> getReceivePaymentsList() {

		try {

			return getFinanceTool().getReceivePaymentsList(getCompanyId());

			// receivePaymentList = (List<ReceivePaymentsList>) manager
			// .merge(receivePaymentList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ReceivePaymentsList>();
	}

	public ArrayList<ClientItem> getLatestPurchaseItems() {
		ArrayList<ClientItem> clientItems = new ArrayList<ClientItem>();
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

	public ArrayList<PaymentsList> getLatestVendorPayments() {
		ArrayList<PaymentsList> paymentsList = null;

		try {

			paymentsList = getFinanceTool().getLatestVendorPayments(
					getCompanyId());

			// paymentsList = (List<PaymentsList>) manager.merge(paymentsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return paymentsList;
	}

	public ArrayList<ClientReceivePayment> getLatestReceivePayments() {
		ArrayList<ClientReceivePayment> clientReceivePaymentList = new ArrayList<ClientReceivePayment>();
		ArrayList<ReceivePayment> serverReceivePaymentList = null;
		try {

			serverReceivePaymentList = getFinanceTool()
					.getLatestReceivePayments(getCompanyId());
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
			long transactionMakeDepositId) {
		ClientTransactionMakeDeposit clientTransactionMakeDeposit = null;
		TransactionMakeDeposit serverTransactionMakeDeposit = null;
		try {

			serverTransactionMakeDeposit = getFinanceTool()
					.getTransactionMakeDeposit(transactionMakeDepositId,
							getCompanyId());
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

	public ArrayList<ClientTransactionMakeDeposit> getTransactionMakeDeposits() {
		ArrayList<ClientTransactionMakeDeposit> makeDepositTransactionsList = null;

		try {

			makeDepositTransactionsList = getFinanceTool()
					.getTransactionMakeDeposits(getCompanyId());

			// makeDepositTransactionsList = (List<MakeDepositTransactionsList>)
			// manager
			// .merge(makeDepositTransactionsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return makeDepositTransactionsList;
	}

	@Override
	public ArrayList<PurchaseOrdersList> getPurchaseOrders()
			throws AccounterException {

		FinanceTool tool = getFinanceTool();

		try {
			return tool != null ? tool.getPurchaseOrdersList(getCompanyId())
					: null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * @Override public List<SalesOrdersList> getPurchaseOrdersForVendor(long
	 * vendorID) throws AccounterException {
	 * 
	 * FinanceTool tool = getFinanceTool();
	 * 
	 * return tool != null ? tool.getPurchaseOrdersForVendor(vendorID) : null; }
	 */
	@Override
	public ArrayList<SalesOrdersList> getSalesOrders()
			throws AccounterException {

		FinanceTool tool = getFinanceTool();

		try {
			return tool != null ? tool.getSalesOrdersList(getCompanyId())
					: null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * @Override public List<SalesOrdersList> getSalesOrdersForCustomer(long
	 * customerID) throws AccounterException {
	 * 
	 * FinanceTool tool = getFinanceTool();
	 * 
	 * return tool != null ? tool.getSalesOrdersForCustomer(customerID) : null;
	 * 
	 * }
	 */

	@Override
	public ArrayList<PurchaseOrdersList> getNotReceivedPurchaseOrdersList(
			long vendorID) throws AccounterException {

		FinanceTool tool = getFinanceTool();

		try {
			return tool != null ? tool.getNotReceivedPurchaseOrdersList(
					vendorID, getCompanyId()) : null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<PurchaseOrdersAndItemReceiptsList> getPurchasesAndItemReceiptsList(
			long vendorId) throws AccounterException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool.getPurchasesAndItemReceiptsList(
					vendorId, getCompanyId()) : null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public ArrayList<EstimatesAndSalesOrdersList> getEstimatesAndSalesOrdersList(
			long customerId) throws AccounterException {

		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool.getEstimatesAndSalesOrdersList(
					customerId, getCompanyId()) : null;
		} catch (DAOException e) {

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
	//
	// e.printStackTrace();
	// }
	// return null;
	// }

	@Override
	public ClientVATReturn getTAXReturn(ClientTAXAgency taxAgency,
			ClientFinanceDate fromDate, ClientFinanceDate toDate)
			throws AccounterException {

		try {
			FinanceTool tool = getFinanceTool();

			TAXAgency serverVatAgency = new ServerConvertUtil().toServerObject(
					new TAXAgency(), taxAgency, getSession());

			return new ClientConvertUtil()
					.toClientObject(tool.getVATReturnDetails(serverVatAgency,
							new FinanceDate(fromDate), new FinanceDate(toDate),
							getCompanyId()), ClientVATReturn.class);

		} catch (Exception e) {
			throw new AccounterException(e);
		}
	}

	@Override
	public FixedAssetSellOrDisposeReviewJournal getReviewJournal(
			TempFixedAsset fixedAsset) throws AccounterException {
		try {
			return getFinanceTool()
					.getReviewJournal(fixedAsset, getCompanyId());
		} catch (DAOException e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	public DepreciableFixedAssetsList getDepreciableFixedAssets(
			long depreciationFrom, long depreciationTo)
			throws AccounterException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool.getDepreciableFixedAssets(
					depreciationFrom, depreciationTo, getCompanyId()) : null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<ClientFinanceDate> getAllDepreciationFromDates()
			throws AccounterException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool
					.getAllDepreciationFromDates(getCompanyId()) : null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ClientFinanceDate getDepreciationLastDate()
			throws AccounterException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool.getDepreciationLastDate(getCompanyId())
					: null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<ClientFinanceDate> getFinancialYearStartDates()
			throws AccounterException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool
					.getFinancialYearStartDates(getCompanyId()) : null;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void rollBackDepreciation(long rollBackDepreciationTo)
			throws AccounterException {
		FinanceTool tool = getFinanceTool();
		tool.rollBackDepreciation(rollBackDepreciationTo, getCompanyId());

	}

	@Override
	public void changeDepreciationStartDateTo(long newStartDate)
			throws AccounterException {
		FinanceTool tool = getFinanceTool();
		OperationContext opContext = new OperationContext(newStartDate);

		tool.updateDeprecationStartDate(opContext);
	}

	@Override
	public double getAccumulatedDepreciationAmount(int depreciationMethod,
			double depreciationRate, double purchasePrice,
			long depreciationfromDate, long depreciationtoDate)
			throws AccounterException {
		try {
			FinanceTool tool = getFinanceTool();
			return tool.getCalculatedDepreciatedAmount(depreciationMethod,
					depreciationRate, purchasePrice, depreciationfromDate,
					depreciationtoDate, getCompanyId());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	public Long getNextNominalCode(int accountType) {
		Long nextTransactionNumber = 0l;
		try {

			nextTransactionNumber = getFinanceTool().getNextNominalCode(
					accountType, getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextTransactionNumber;

	}

	/*
	 * public boolean createTaxes(int[] vatReturnType) { try {
	 * getFinanceTool().createTaxes(vatReturnType); return true; } catch
	 * (Exception e) { e.printStackTrace(); return false; }
	 * 
	 * }
	 */

	public String getNextFixedAssetNumber() {
		String nextFixedAssestNumber = "";
		try {

			nextFixedAssestNumber = getFinanceTool().getNextFixedAssetNumber(
					getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextFixedAssestNumber;
	}

	@Override
	public boolean changeFiscalYearsStartDateTo(long newStartDate) {
		try {
			OperationContext opContext = new OperationContext(newStartDate);
			getFinanceTool().updateCompanyStartDate(opContext);
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
					linkedAccounts, getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<ClientPayVATEntries> getPayVATEntries()
			throws AccounterException {

		ArrayList<ClientPayVATEntries> clientEntries = new ArrayList<ClientPayVATEntries>();

		FinanceTool tool = getFinanceTool();
		if (tool == null)
			return clientEntries;
		List<PayVATEntries> entries = tool.getPayVATEntries(getCompanyId());
		for (PayVATEntries entry : entries) {
			ClientPayVATEntries clientEntry = new ClientPayVATEntries();
			// VATReturn vatReturn =(VATReturn) entry.getTransaction();
			// ClientVATReturn clientVATReturn = new
			// ClientConvertUtil().toClientObject(vatReturn,ClientVATReturn.class);
			clientEntry.setVatReturn(entry.getTransaction().getID());
			clientEntry.setVatAgency(entry.getTaxAgency() != null ? entry
					.getTaxAgency().getID() : null);
			clientEntry.setBalance(entry.getBalance());
			clientEntry.setAmount(entry.getAmount());

			clientEntries.add(clientEntry);
		}
		return clientEntries;

	}

	@Override
	public ArrayList<ClientFinanceLogger> getLog(long id, boolean isNext)
			throws AccounterException {

		ArrayList<ClientFinanceLogger> clientLogs = new ArrayList<ClientFinanceLogger>();

		FinanceTool tool = (FinanceTool) getFinanceTool();
		if (tool == null)
			return clientLogs;
		List<FinanceLogger> logs = tool.getLog(id, isNext, getCompanyId());

		if (logs == null) {
			return clientLogs;
		}

		for (FinanceLogger log : logs) {
			ClientFinanceLogger clientLog = new ClientFinanceLogger();

			clientLog.setDescription(log.getDescription());
			clientLog.setLogMessge(log.getLogMessge());
			// clientLog.setCreatedBy(log.getCreatedBy());
			// clientLog.setCreatedDate(log.getCreatedDate().getDate());
			clientLog.setID(log.getID());

			clientLogs.add(clientLog);
		}

		return clientLogs;

	}

	@Override
	public ArrayList<ClientFinanceLogger> getLog(String date, long id,
			boolean isNext) throws AccounterException {

		ArrayList<ClientFinanceLogger> clientLogs = new ArrayList<ClientFinanceLogger>();

		FinanceTool tool = (FinanceTool) getFinanceTool();
		if (tool == null)
			return clientLogs;
		List<FinanceLogger> logs = tool.getLog(date, id, isNext);

		for (FinanceLogger log : logs) {
			ClientFinanceLogger clientLog = new ClientFinanceLogger();

			clientLog.setID(log.getID());
			clientLog.setDescription(log.getDescription());
			clientLog.setLogMessge(log.getLogMessge());
			// clientLog.setCreatedDate(log.getCreatedDate().getDate());
			// clientLog.setCreatedBy(log.getCreatedBy());

			clientLogs.add(clientLog);
		}

		return clientLogs;

	}

	@Override
	public ArrayList<PayeeList> getPayeeList(int transactionCategory) {

		ArrayList<PayeeList> payeeList = null;

		try {
			FinanceTool tool = getFinanceTool();
			return tool != null ? tool.getPayeeList(transactionCategory,
					getCompanyId()) : null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return payeeList;

	}

	@Override
	public String getCustomerNumber() {
		String nextCustomerNumber = "";
		try {

			nextCustomerNumber = getFinanceTool().getNextCustomerNumber(
					getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextCustomerNumber;

	}

	@Override
	public String getVendorNumber() {
		String nextCustomerNumber = "";
		try {

			nextCustomerNumber = getFinanceTool().getNextVendorNumber(
					getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nextCustomerNumber;
	}

	@Override
	public ArrayList<ClientReceiveVATEntries> getReceiveVATEntries()
			throws AccounterException {

		ArrayList<ClientReceiveVATEntries> clientEntries = new ArrayList<ClientReceiveVATEntries>();

		FinanceTool tool = getFinanceTool();
		if (tool == null)
			return clientEntries;
		List<ReceiveVATEntries> entries = tool
				.getReceiveVATEntries(getCompanyId());
		for (ReceiveVATEntries entry : entries) {
			ClientReceiveVATEntries clientEntry = new ClientReceiveVATEntries();
			// VATReturn vatReturn =(VATReturn) entry.getTransaction();
			// ClientVATReturn clientVATReturn = new
			// ClientConvertUtil().toClientObject(vatReturn,ClientVATReturn.class);
			clientEntry.setVatReturn(entry.getTransaction().getID());
			clientEntry.setVatAgency(entry.getTaxAgency() != null ? entry
					.getTaxAgency().getID() : null);
			clientEntry.setBalance(entry.getBalance());
			clientEntry.setAmount(entry.getAmount());

			clientEntries.add(clientEntry);
		}
		return new ArrayList<ClientReceiveVATEntries>(clientEntries);

	}

	@Override
	public ArrayList<Double> getGraphPointsforAccount(int chartType,
			long accountNo) {

		List<Double> resultList = null;
		try {

			resultList = getFinanceTool().getGraphPointsforAccount(chartType,
					accountNo, getCompanyId());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<Double>(resultList);
	}

	@Override
	public ArrayList<BillsList> getEmployeeExpensesByStatus(String userName,
			int status) {
		List<BillsList> resultList = null;
		try {
			resultList = getFinanceTool().getEmployeeExpensesByStatus(userName,
					status, getCompanyId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<BillsList>(resultList);
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

	public ArrayList<ClientUserInfo> getAllUsers() throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {
			return tool.getAllUsers(getCompanyId());
		}
		return null;
	}

	@Override
	public ArrayList<ClientRecurringTransaction> getRecurringsList()
			throws AccounterException {

		FinanceTool tool = getFinanceTool();
		if (tool != null) {
			return tool.getAllRecurringTransactions();
		}
		return null;
	}

	@Override
	public void mergeCustomer(ClientCustomer fromClientCustomer,
			ClientCustomer toClientCustomer) throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {

			try {
				tool.mergeCustomer(fromClientCustomer, toClientCustomer,
						getCompanyId());
			} catch (DAOException e) {

				e.printStackTrace();
			}
		}

	}

	@Override
	public void mergeVendor(ClientVendor fromClientVendor,
			ClientVendor toClientVendor) throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {

			tool.mergeVendor(fromClientVendor, toClientVendor, getCompanyId());
		}

	}

	@Override
	public void mergeItem(ClientItem froClientItem, ClientItem toClientItem)
			throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {

			tool.mergeItem(froClientItem, toClientItem, getCompanyId());
		}

	}

	@Override
	public void mergeAccount(ClientAccount fromClientAccount,
			ClientAccount toClientAccount) throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {

			tool.mergeAcoount(fromClientAccount, toClientAccount,
					getCompanyId());
		}

	}

	@Override
	public PaginationList<ClientActivity> getUsersActivityLog(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			int startIndex, int length) throws AccounterException {

		FinanceTool tool = getFinanceTool();
		if (tool != null) {
			return tool.getUsersActivityLog(startDate, endDate, startIndex,
					length, getCompanyId());
		}
		return null;

	}

	// public ArrayList<ClientEmployee> getAllEmployees()
	// throws AccounterException {
	// FinanceTool tool = getFinanceTool();
	// if (tool != null) {
	// return tool.getAllEmployees();
	// }
	// return null;
	// }
	// this method is used to send Pdf as an attachment in email
	@Override
	public void sendPdfInMail(long objectID, int type, long brandingThemeId,
			String mimeType, String subject, String content,
			String senderEmail, String recipientEmail, String ccEmail)
			throws Exception, IOException, AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {
			tool.sendPdfInMail(objectID, type, brandingThemeId, mimeType,
					subject, content, senderEmail, recipientEmail, ccEmail,
					getCompanyId());
		}

	}

	@Override
	public ArrayList<ClientTDSInfo> getPayBillsByTDS()
			throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {
			return tool.getPayBillsByTDS(getCompanyId());
		}
		return null;
	}

}