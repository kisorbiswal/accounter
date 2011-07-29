package com.vimukti.accounter.web.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.Util;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.IAccounterReportService;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.Lists.DummyDebitor;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.core.Lists.PayeeStatementsList;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.client.core.reports.AmountsDueToVendor;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.core.reports.DepositDetail;
import com.vimukti.accounter.web.client.core.reports.ECSalesList;
import com.vimukti.accounter.web.client.core.reports.ECSalesListDetail;
import com.vimukti.accounter.web.client.core.reports.ExpenseList;
import com.vimukti.accounter.web.client.core.reports.MostProfitableCustomers;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeList;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeListDetail;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.core.reports.SalesTaxLiability;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.core.reports.UncategorisedAmountsReport;
import com.vimukti.accounter.web.client.core.reports.VATDetail;
import com.vimukti.accounter.web.client.core.reports.VATItemDetail;
import com.vimukti.accounter.web.client.core.reports.VATItemSummary;
import com.vimukti.accounter.web.client.core.reports.VATSummary;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.reports.CheckDetailReport;

@SuppressWarnings("serial")
public class AccounterReportServiceImpl extends AccounterRPCBaseServiceImpl
		implements IAccounterReportService {

	private FinanceDate transtartDate = new FinanceDate();
	private FinanceDate tranendDate = new FinanceDate();

	/**
	 * 
	 */

	public AccounterReportServiceImpl() {
		super();

	}

	private BaseReport setStartEndDates(BaseReport obj) {
		// if (Utility.stringToDate(new
		// ClientFinanceDate(transtartDate).toString()) != null)
		obj.setStartDate(transtartDate.toClientFinanceDate());
		// if (Utility.stringToDate(new
		// ClientFinanceDate(transtartDate).toString()) != null)
		obj.setEndDate(tranendDate.toClientFinanceDate());
		return obj;
	}

	@Override
	public List<SalesByCustomerDetail> getSalesByCustomerSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<SalesByCustomerDetail> salesByCustomerDetailList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			salesByCustomerDetailList = getFinanceTool()
					.getSalesByCustomerSummary(transtartDate, tranendDate);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	// @Override
	// public List<AccountBalance> getAccountBalances() {
	// List<AccountBalance> accountBalanceList = null;
	//
	// try {
	//
	// IAccounterReportDAOService dao =
	// dbService.getAccounterReportDAOService();
	//
	// accountBalanceList =
	// getFinanceTool().getAccountBalances(getThreadLocalCompanyId());
	//
	// accountBalanceList = (List<AccountBalance>)
	// manager.merge(accountBalanceList);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return accountBalanceList;
	// }

	@Override
	public List<AccountRegister> getAccountRegister(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long accountId) {
		List<AccountRegister> accountRegisterList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			accountRegisterList = getFinanceTool().getAccountRegister(
					transtartDate, tranendDate, accountId);

			AccountRegister obj = new AccountRegister();
			if (accountRegisterList != null)
				accountRegisterList
						.add((AccountRegister) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return accountRegisterList;
	}

	@Override
	public List<AgedDebtors> getAgedCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		List<AgedDebtors> agedDebtorsList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			agedDebtorsList = getFinanceTool().getAgedCreditors(transtartDate,
					tranendDate);
			List<AgedDebtors> debtors = new ArrayList<AgedDebtors>();
			for (AgedDebtors debtor : agedDebtorsList) {
				debtors = updateDebtorListByName(debtors, debtor);
			}

			Collections.sort(debtors, new Comparator<AgedDebtors>() {

				@Override
				public int compare(AgedDebtors o1, AgedDebtors o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			agedDebtorsList.addAll(debtors);

			AgedDebtors obj = new AgedDebtors();
			if (agedDebtorsList != null)
				agedDebtorsList.add((AgedDebtors) setStartEndDates(obj));

			// agedDebtorsList = (List<AgedDebtors>) manager
			// .merge(agedDebtorsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return agedDebtorsList;
	}

	@Override
	public List<AgedDebtors> getAgedDebtors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		List<AgedDebtors> agedDebtorsList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			agedDebtorsList = getFinanceTool().getAgedDebtors(transtartDate,
					tranendDate);
			List<AgedDebtors> debtors = new ArrayList<AgedDebtors>();
			for (AgedDebtors debtor : agedDebtorsList) {
				debtors = updateDebtorListByName(debtors, debtor);
			}

			Collections.sort(debtors, new Comparator<AgedDebtors>() {

				@Override
				public int compare(AgedDebtors o1, AgedDebtors o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			agedDebtorsList.addAll(debtors);

			AgedDebtors obj = new AgedDebtors();
			if (agedDebtorsList != null)
				agedDebtorsList.add((AgedDebtors) (setStartEndDates(obj)));

			// agedDebtorsList = (List<AgedDebtors>) manager
			// .merge(agedDebtorsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return agedDebtorsList;
	}

	private AgedDebtors createdAgedDebitor(AgedDebtors debtor) {

		AgedDebtors agedDebtors = new AgedDebtors();

		agedDebtors.setName(debtor.getName());
		agedDebtors.setContact(debtor.getContact());
		// agedDebtors.setPhone(debtor.getPhone());
		// agedDebtors.setType(debtor.getType());
		// agedDebtors.setDate(debtor.getDate());
		// agedDebtors.setNumber(debtor.getNumber());
		// agedDebtors.setReference(debtor.getReference());
		// agedDebtors.setDueDate(debtor.getDueDate());
		// agedDebtors.setPaymentTermName(debtor.getPaymentTermName());
		agedDebtors.setAmount(debtor.getAmount());
		agedDebtors.setTotal(debtor.getTotal());
		// agedDebtors.setIsVoid(debtor.getIsVoid());
		agedDebtors.setTransactionId(debtor.getTransactionId());
		return agedDebtors;
	}

	public List<AgedDebtors> updateDebtorListByName(List<AgedDebtors> debtors,
			AgedDebtors agedDebtors) {
		if (debtors == null) {
			debtors = new ArrayList<AgedDebtors>();
		}

		boolean isExist = false;
		for (AgedDebtors debtor : debtors) {
			if (debtor.getName().equals(agedDebtors.getName())) {
				debtor.setTotal(debtor.getTotal() + agedDebtors.getTotal());
				isExist = true;
				break;
			}
		}
		if (debtors.isEmpty() || !isExist) {
			AgedDebtors debtor = createdAgedDebitor(agedDebtors);
			debtor.setCategory(5);
			debtors.add(debtor);
		}
		return debtors;

	}

	@Override
	public List<AmountsDueToVendor> getAmountsDueToVendor(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<AmountsDueToVendor> amountsDueToVendorList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			amountsDueToVendorList = getFinanceTool().getAmountsDueToVendor(
					transtartDate, tranendDate);

			AmountsDueToVendor obj = new AmountsDueToVendor();
			if (amountsDueToVendorList != null)
				amountsDueToVendorList
						.add((AmountsDueToVendor) setStartEndDates(obj));

			// amountsDueToVendorList = (List<AmountsDueToVendor>) manager
			// .merge(amountsDueToVendorList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return amountsDueToVendorList;
	}

	@Override
	public List<TransactionHistory> getCustomerTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<TransactionHistory> transactionHistoryList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			transactionHistoryList = getFinanceTool()
					.getCustomerTransactionHistory(transtartDate, tranendDate);

			TransactionHistory obj = new TransactionHistory();
			if (transactionHistoryList != null)
				transactionHistoryList
						.add((TransactionHistory) setStartEndDates(obj));

			// transactionHistoryList = (List<TransactionHistory>) manager
			// .merge(transactionHistoryList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transactionHistoryList;
	}

	@Override
	public List<MostProfitableCustomers> getMostProfitableCustomers(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<MostProfitableCustomers> mostProfitableCustomersList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			mostProfitableCustomersList = getFinanceTool()
					.getMostProfitableCustomers(transtartDate, tranendDate);

			MostProfitableCustomers obj = new MostProfitableCustomers();
			if (mostProfitableCustomersList != null)
				mostProfitableCustomersList
						.add((MostProfitableCustomers) setStartEndDates(obj));

			// mostProfitableCustomersList = (List<MostProfitableCustomers>)
			// manager
			// .merge(mostProfitableCustomersList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mostProfitableCustomersList;
	}

	@Override
	public List<SalesByCustomerDetail> getPurchasesByItemDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<SalesByCustomerDetail> salesByCustomerDetailList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			salesByCustomerDetailList = getFinanceTool()
					.getPurchasesByItemDetail(transtartDate, tranendDate);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	@Override
	public List<SalesByCustomerDetail> getPurchasesByItemSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<SalesByCustomerDetail> salesByCustomerDetailList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			salesByCustomerDetailList = getFinanceTool()
					.getPurchasesByItemSummary(transtartDate, tranendDate);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	@Override
	public List<SalesByCustomerDetail> getPurchasesByVendorDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<SalesByCustomerDetail> salesByCustomerDetailList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			salesByCustomerDetailList = getFinanceTool()
					.getPurchasesByVendorDetail(transtartDate, tranendDate);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	@Override
	public List<SalesByCustomerDetail> getPurchasesByVendorSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<SalesByCustomerDetail> salesByCustomerDetailList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			salesByCustomerDetailList = getFinanceTool()
					.getPurchasesByVendorSummary(transtartDate, tranendDate);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			salesByCustomerDetailList
					.add((SalesByCustomerDetail) setStartEndDates(obj));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	@Override
	public List<ClientTransaction> getRegister(long accountId) {
		List<ClientTransaction> clientTransactionList = new ArrayList<ClientTransaction>();
		
		List<Transaction> serverTransactionList = null;

		try {

			// serverTransactionList =
			// getFinanceTool().getRegister(getFinanceTool()
			// .getAccounterDAOService().getAccount(
			// accountId));
			// for (Transaction transaction : serverTransactionList) {
			// clientTransactionList.add(Util
			// .toClientObject(transaction, ClientTransaction.class));
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientTransactionList;
	}

	@Override
	public List<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<SalesByCustomerDetail> salesByCustomerDetailList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			salesByCustomerDetailList = getFinanceTool()
					.getSalesByCustomerDetailReport(transtartDate, tranendDate);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	@Override
	public List<SalesByCustomerDetail> getSalesByItemDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<SalesByCustomerDetail> salesByCustomerDetailList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			salesByCustomerDetailList = getFinanceTool().getSalesByItemDetail(
					transtartDate, tranendDate);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	@Override
	public List<SalesByCustomerDetail> getSalesByItemSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<SalesByCustomerDetail> salesByCustomerDetailList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			salesByCustomerDetailList = getFinanceTool().getSalesByItemSummary(
					transtartDate, tranendDate);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	@Override
	public List<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<TransactionDetailByTaxItem> transactionDetailByTaxItemList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			transactionDetailByTaxItemList = getFinanceTool()
					.getTransactionDetailByTaxItem(transtartDate, tranendDate);

			TransactionDetailByTaxItem obj = new TransactionDetailByTaxItem();
			if (transactionDetailByTaxItemList != null)
				transactionDetailByTaxItemList
						.add((TransactionDetailByTaxItem) setStartEndDates(obj));

			// transactionDetailByTaxcodeList =
			// (List<TransactionDetailByTaxcode>) manager
			// .merge(transactionDetailByTaxcodeList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transactionDetailByTaxItemList;
	}

	@Override
	public List<TrialBalance> getTrialBalance(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		List<TrialBalance> trialBalanceList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			trialBalanceList = getFinanceTool().getTrialBalance(transtartDate,
					tranendDate);

			TrialBalance obj = new TrialBalance();
			if (trialBalanceList != null)
				trialBalanceList.add((TrialBalance) setStartEndDates(obj));

			// trialBalanceList = (List<TrialBalance>) manager
			// .merge(trialBalanceList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return trialBalanceList;
	}

	@Override
	public List<TransactionHistory> getVendorTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<TransactionHistory> transactionHistoryList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			transactionHistoryList = getFinanceTool()
					.getVendorTransactionHistory(transtartDate, tranendDate);

			TransactionHistory obj = new TransactionHistory();
			if (transactionHistoryList != null)
				transactionHistoryList
						.add((TransactionHistory) setStartEndDates(obj));

			// transactionHistoryList = (List<TransactionHistory>) manager
			// .merge(transactionHistoryList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transactionHistoryList;
	}

	@Override
	public List<ClientItem> getPurchaseReportItems(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		List<ClientItem> clientItemList = new ArrayList<ClientItem>();
		List<Item> serverItemsList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			serverItemsList = getFinanceTool().getPurchaseReportItems(
					transtartDate, tranendDate);

			ClientItem obj = new ClientItem();
			if (clientItemList != null)
				clientItemList.add((ClientItem) setStartEndDates(obj));

			for (Item item : serverItemsList) {
				clientItemList.add(new ClientConvertUtil().toClientObject(item,
						ClientItem.class));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientItemList;
	}

	@Override
	public List<ClientItem> getSalesReportItems(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		List<ClientItem> clientItemList = new ArrayList<ClientItem>();
		List<Item> serverItemsList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			serverItemsList = getFinanceTool().getSalesReportItems(
					transtartDate, tranendDate);

			ClientItem obj = new ClientItem();
			if (clientItemList != null)
				clientItemList.add((ClientItem) setStartEndDates(obj));

			for (Item item : serverItemsList) {
				clientItemList.add(new ClientConvertUtil().toClientObject(item,
						ClientItem.class));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientItemList;
	}

	@Override
	public List<ClientCustomer> getTransactionHistoryCustomers(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<ClientCustomer> clientCustomerList = new ArrayList<ClientCustomer>();
		List<Customer> serverCustomerList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			serverCustomerList = getFinanceTool()
					.getTransactionHistoryCustomers(transtartDate, tranendDate);

			for (Customer customer : serverCustomerList) {
				clientCustomerList.add(new ClientConvertUtil().toClientObject(
						customer, ClientCustomer.class));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientCustomerList;
	}

	@Override
	public List<ClientVendor> getTransactionHistoryVendors(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<ClientVendor> clientVendorList = new ArrayList<ClientVendor>();
		List<Vendor> serverVendorList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			serverVendorList = getFinanceTool().getTransactionHistoryVendors(
					transtartDate, tranendDate);
			for (Vendor vendor : serverVendorList) {
				clientVendorList.add(new ClientConvertUtil().toClientObject(
						vendor, ClientVendor.class));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clientVendorList;
	}

	@Override
	public List<SalesTaxLiability> getSalesTaxLiabilityReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {

		List<SalesTaxLiability> salesTaxLiabilityList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			salesTaxLiabilityList = getFinanceTool()
					.getSalesTaxLiabilityReport(transtartDate, tranendDate);

			SalesTaxLiability obj = new SalesTaxLiability();
			if (salesTaxLiabilityList != null)
				salesTaxLiabilityList
						.add((SalesTaxLiability) setStartEndDates(obj));
			// salesTaxLiabilityList = (List<SalesTaxLiability>) manager
			// .merge(salesTaxLiabilityList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesTaxLiabilityList;
	}

	@Override
	public List<TransactionDetailByAccount> getTransactionDetailByAccount(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<TransactionDetailByAccount> transDetailByAccountList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			transDetailByAccountList = getFinanceTool()
					.getTransactionDetailByAccount(transtartDate, tranendDate);

			TransactionDetailByAccount obj = new TransactionDetailByAccount();
			if (transDetailByAccountList != null)
				transDetailByAccountList
						.add((TransactionDetailByAccount) setStartEndDates(obj));

			// transDetailByAccountList = (List<TransactionDetailByAccount>)
			// manager
			// .merge(transDetailByAccountList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transDetailByAccountList;

	}

	@Override
	public List<SalesByCustomerDetail> getPurchasesByItemDetail(
			String itemName, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		List<SalesByCustomerDetail> salesByCustomertList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			salesByCustomertList = getFinanceTool().getPurchasesByItemDetail(
					itemName, transtartDate, tranendDate);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomertList != null)
				salesByCustomertList
						.add((SalesByCustomerDetail) setStartEndDates(obj));

			// salesByCustomertList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomertList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomertList;
	}

	@Override
	public List<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String vendorName, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		List<SalesByCustomerDetail> salesByCustomertList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			salesByCustomertList = getFinanceTool().getPurchasesByVendorDetail(
					vendorName, transtartDate, tranendDate);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomertList != null)
				salesByCustomertList
						.add((SalesByCustomerDetail) setStartEndDates(obj));

			// salesByCustomertList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomertList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomertList;
	}

	@Override
	public List<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String customerName, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		List<SalesByCustomerDetail> salesByCustomertList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			salesByCustomertList = getFinanceTool()
					.getSalesByCustomerDetailReport(customerName,
							transtartDate, tranendDate);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomertList != null)
				salesByCustomertList
						.add((SalesByCustomerDetail) setStartEndDates(obj));

			// salesByCustomertList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomertList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomertList;
	}

	@Override
	public List<SalesByCustomerDetail> getSalesByItemDetail(String itemName,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<SalesByCustomerDetail> salesByCustomertList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			salesByCustomertList = getFinanceTool().getSalesByItemDetail(
					itemName, transtartDate, tranendDate);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomertList != null)
				salesByCustomertList
						.add((SalesByCustomerDetail) setStartEndDates(obj));

			// salesByCustomertList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomertList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomertList;
	}

	@Override
	public List<AgedDebtors> getAgedDebtors(String Name,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<AgedDebtors> agedDebtorsList = null;
		List<AgedDebtors> agedDebtorsListForCustomer = new ArrayList<AgedDebtors>();

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			agedDebtorsList = getFinanceTool().getAgedDebtors(transtartDate,
					tranendDate);
			for (AgedDebtors agdDebitor : agedDebtorsList) {
				if (Name.equals(agdDebitor.getName()))
					agedDebtorsListForCustomer.add(agdDebitor);
			}
			AgedDebtors obj = new AgedDebtors();
			if (agedDebtorsListForCustomer != null)
				agedDebtorsListForCustomer
						.add((AgedDebtors) (setStartEndDates(obj)));

			// agedDebtorsList = (List<AgedDebtors>) manager
			// .merge(agedDebtorsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return agedDebtorsListForCustomer;
	}

	@Override
	public List<AgedDebtors> getAgedCreditors(String Name,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<AgedDebtors> agedDebtorsList = null;
		List<AgedDebtors> agedCreditorsListForCustomer = new ArrayList<AgedDebtors>();

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			agedDebtorsList = getFinanceTool().getAgedCreditors(transtartDate,
					tranendDate);
			for (AgedDebtors agdDebitor : agedDebtorsList) {
				if (Name.equals(agdDebitor.getName()))
					agedCreditorsListForCustomer.add(agdDebitor);
			}
			AgedDebtors obj = new AgedDebtors();
			if (agedCreditorsListForCustomer != null)
				agedCreditorsListForCustomer
						.add((AgedDebtors) (setStartEndDates(obj)));

			// agedDebtorsList = (List<AgedDebtors>) manager
			// .merge(agedDebtorsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return agedCreditorsListForCustomer;
	}

	@Override
	public List<TransactionDetailByAccount> getTransactionDetailByAccount(
			String accountName, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		List<TransactionDetailByAccount> transDetailByAccountList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			transDetailByAccountList = getFinanceTool()
					.getTransactionDetailByAccount(accountName, transtartDate,
							tranendDate);

			TransactionDetailByAccount obj = new TransactionDetailByAccount();
			if (transDetailByAccountList != null)
				transDetailByAccountList
						.add((TransactionDetailByAccount) setStartEndDates(obj));

			// transDetailByAccountList = (List<TransactionDetailByAccount>)
			// manager
			// .merge(transDetailByAccountList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transDetailByAccountList;
	}

	@Override
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

	@Override
	public List<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			final String taxItemName, final ClientFinanceDate startDate,
			final ClientFinanceDate endDate) {
		List<TransactionDetailByTaxItem> transactionDetailByTaxItemList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			transactionDetailByTaxItemList = getFinanceTool()
					.getTransactionDetailByTaxItem(taxItemName, transtartDate,
							tranendDate);

			TransactionDetailByTaxItem obj = new TransactionDetailByTaxItem();
			if (transactionDetailByTaxItemList != null)
				transactionDetailByTaxItemList
						.add((TransactionDetailByTaxItem) setStartEndDates(obj));

			// transactionDetailByTaxcodeList =
			// (List<TransactionDetailByTaxcode>) manager
			// .merge(transactionDetailByTaxcodeList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transactionDetailByTaxItemList;
	}

	@Override
	public List<TrialBalance> getBalanceSheetReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<TrialBalance> trialbalanceList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			trialbalanceList = getFinanceTool().getBalanceSheetReport(
					transtartDate, tranendDate);

			TrialBalance obj = new TrialBalance();
			if (trialbalanceList != null)
				trialbalanceList.add((TrialBalance) setStartEndDates(obj));

			if (trialbalanceList.size() == 1) {
				if (trialbalanceList.get(0).getAccountName()
						.equals("Net Income")
						&& DecimalUtil.isEquals(trialbalanceList.get(0)
								.getAmount(), 0)) {
					trialbalanceList.clear();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return trialbalanceList;
	}

	@Override
	public List<TrialBalance> getCashFlowReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		List<TrialBalance> trialbalanceList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			trialbalanceList = getFinanceTool().getCashFlowReport(
					transtartDate, tranendDate);

			TrialBalance obj = new TrialBalance();
			if (trialbalanceList != null)
				trialbalanceList.add((TrialBalance) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return trialbalanceList;
	}

	@Override
	public List<TrialBalance> getProfitAndLossReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<TrialBalance> trialbalanceList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			trialbalanceList = getFinanceTool().getProfitAndLossReport(
					transtartDate, tranendDate);

			TrialBalance obj = new TrialBalance();
			if (trialbalanceList != null)
				trialbalanceList.add((TrialBalance) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return trialbalanceList;
	}

	@Override
	public List<OpenAndClosedOrders> getPurchaseOpenOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<OpenAndClosedOrders> purchaseOrders = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {
			purchaseOrders = getFinanceTool().getOpenPurchaseOrders(
					transtartDate, tranendDate);

			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (purchaseOrders != null)
				purchaseOrders.add((OpenAndClosedOrders) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrders;
	}

	@Override
	public List<OpenAndClosedOrders> getPurchaseCompletedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<OpenAndClosedOrders> purchaseOrders = null;
		getMinimumAndMaximumDates(startDate, endDate);
		try {
			purchaseOrders = getFinanceTool().getCompletedPurchaseOrders(
					transtartDate, tranendDate);
			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (purchaseOrders != null)
				purchaseOrders.add((OpenAndClosedOrders) setStartEndDates(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrders;
	}

	@Override
	public List<OpenAndClosedOrders> getPurchaseCancelledOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<OpenAndClosedOrders> purchaseOrders = null;
		getMinimumAndMaximumDates(startDate, endDate);
		try {
			purchaseOrders = getFinanceTool().getCanceledPurchaseOrders(
					transtartDate, tranendDate);
			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (purchaseOrders != null)
				purchaseOrders.add((OpenAndClosedOrders) setStartEndDates(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrders;
	}

	@Override
	public List<OpenAndClosedOrders> getPurchaseOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<OpenAndClosedOrders> purchaseOrders = null;
		getMinimumAndMaximumDates(startDate, endDate);
		try {
			purchaseOrders = getFinanceTool().getPurchaseOrders(transtartDate,
					tranendDate);
			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (purchaseOrders != null)
				purchaseOrders.add((OpenAndClosedOrders) setStartEndDates(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrders;
	}

	@Override
	public List<OpenAndClosedOrders> getPurchaseClosedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<OpenAndClosedOrders> purchaseOrders = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {
			purchaseOrders = getFinanceTool().getClosedPurchaseOrders(
					transtartDate, tranendDate);

			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (purchaseOrders != null)
				purchaseOrders.add((OpenAndClosedOrders) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrders;
	}

	@Override
	public List<OpenAndClosedOrders> getSalesOpenOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<OpenAndClosedOrders> salesOrders = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {
			salesOrders = getFinanceTool().getOpenSalesOrders(transtartDate,
					tranendDate);

			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (salesOrders != null)
				salesOrders.add((OpenAndClosedOrders) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesOrders;
	}

	@Override
	public List<OpenAndClosedOrders> getSalesCompletedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<OpenAndClosedOrders> salesOrders = null;
		getMinimumAndMaximumDates(startDate, endDate);
		try {
			salesOrders = getFinanceTool().getCompletedSalesOrders(
					transtartDate, tranendDate);
			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (salesOrders != null)
				salesOrders.add((OpenAndClosedOrders) setStartEndDates(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesOrders;
	}

	@Override
	public List<OpenAndClosedOrders> getSalesOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<OpenAndClosedOrders> salesOrders = null;
		getMinimumAndMaximumDates(startDate, endDate);
		try {
			salesOrders = getFinanceTool().getSalesOrders(transtartDate,
					tranendDate);
			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (salesOrders != null)
				salesOrders.add((OpenAndClosedOrders) setStartEndDates(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesOrders;
	}

	@Override
	public List<OpenAndClosedOrders> getSalesCancelledOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<OpenAndClosedOrders> salesOrders = null;
		getMinimumAndMaximumDates(startDate, endDate);
		try {
			salesOrders = getFinanceTool().getCanceledSalesOrders(
					transtartDate, tranendDate);
			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (salesOrders != null)
				salesOrders.add((OpenAndClosedOrders) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesOrders;
	}

	@Override
	public List<OpenAndClosedOrders> getSalesClosedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<OpenAndClosedOrders> salesOrders = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {
			salesOrders = getFinanceTool().getClosedSalesOrders(transtartDate,
					tranendDate);

			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (salesOrders != null)
				salesOrders.add((OpenAndClosedOrders) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesOrders;
	}

	//
	// public List<OpenAndClosedOrders> getSalesOpenOrderReportByStatus(
	// int status) {
	// List<OpenAndClosedOrders> salesOpenOrders = null;
	// try {
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return salesOpenOrders;
	// }

	@Override
	public List<VATDetail> getPriorVATReturnVATDetailReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		List<VATDetail> vatDetailReport = null;

		getMinimumAndMaximumDates(startDate, endDate);
		// transtartDate = "";
		// tranendDate = endDate;

		try {
			vatDetailReport = getFinanceTool().getVATDetailReport(
					transtartDate, tranendDate);

			VATDetail obj = new VATDetail();
			if (vatDetailReport != null)
				vatDetailReport.add((VATDetail) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatDetailReport;
	}

	@Override
	public List<VATDetail> getPriorVATReturnReport(long taxAgency,
			ClientFinanceDate endDate) {
		List<VATDetail> vatDetailReport = null;

		// getMinimumAndMaximumDates("", endDate);

		transtartDate.clear();
		tranendDate = new FinanceDate(endDate);

		try {
			TAXAgency vatAgncy = (TAXAgency) Util.loadObjectByid(
					HibernateUtil.getCurrentSession(), "TAXAgency", taxAgency);
			;
			vatDetailReport = getFinanceTool()
					.getPriorVATReturnVATDetailReport(vatAgncy, tranendDate);

			VATDetail obj = new VATDetail();
			if (vatDetailReport != null)
				vatDetailReport.add((VATDetail) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatDetailReport;
	}

	public List<VATSummary> getPriorReturnVATSummary(long taxAgncy,
			ClientFinanceDate endDate) {
		List<VATSummary> vatSummaryList = new ArrayList<VATSummary>();

		tranendDate = new FinanceDate(endDate);

		try {
			TAXAgency vatAgency = (TAXAgency) Util.loadObjectByid(
					HibernateUtil.getCurrentSession(), "TAXAgency", taxAgncy);
			vatSummaryList = getFinanceTool().getPriorReturnVATSummary(
					vatAgency, new FinanceDate(endDate));

			VATSummary obj = new VATSummary();
			if (vatSummaryList != null)
				vatSummaryList.add((VATSummary) setStartEndDates(obj));

			/*
			 * Removing Box 3 and Box 5 from list, as the calculations for box 3
			 * and box 5 are done in gui
			 */
			vatSummaryList.remove(2);
			vatSummaryList.remove(3);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatSummaryList;
	}

	public List<VATSummary> getVAT100Report(long taxAgency,
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {
		List<VATSummary> vatSummaryList = new ArrayList<VATSummary>();

		getMinimumAndMaximumDates(fromDate, toDate);

		try {
			TAXAgency vatAgency = (TAXAgency) Util.loadObjectByid(
					HibernateUtil.getCurrentSession(), "TAXAgency", taxAgency);
			vatSummaryList = getFinanceTool().getVAT100Report(vatAgency,
					transtartDate, tranendDate);

			VATSummary obj = new VATSummary();
			if (vatSummaryList != null)
				vatSummaryList.add((VATSummary) setStartEndDates(obj));

			/*
			 * //* Removing Box 3 and Box 5 from list, as the calculations for
			 * box 3 and box 5 are done in gui
			 *///
			vatSummaryList.remove(2);
			vatSummaryList.remove(3);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatSummaryList;
	}

	@Override
	public List<UncategorisedAmountsReport> getUncategorisedAmountsReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {
		List<UncategorisedAmountsReport> uncategories = new ArrayList<UncategorisedAmountsReport>();

		getMinimumAndMaximumDates(fromDate, toDate);

		try {
			uncategories = getFinanceTool().getUncategorisedAmountsReport(
					transtartDate, tranendDate);

			UncategorisedAmountsReport obj = new UncategorisedAmountsReport();
			if (uncategories != null)
				uncategories
						.add((UncategorisedAmountsReport) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return uncategories;
	}

	@Override
	public List<VATItemSummary> getVATItemSummaryReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {
		List<VATItemSummary> vatItems = new ArrayList<VATItemSummary>();

		getMinimumAndMaximumDates(fromDate, toDate);

		try {
			vatItems = getFinanceTool().getVATItemSummaryReport(transtartDate,
					tranendDate);

			VATItemSummary obj = new VATItemSummary();
			if (vatItems != null)
				vatItems.add((VATItemSummary) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatItems;
	}

	@Override
	public List<VATItemDetail> getVATItemDetailReport(String vatItemName,
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {

		List<VATItemDetail> itemsList = new ArrayList<VATItemDetail>();

		getMinimumAndMaximumDates(fromDate, toDate);

		try {
			itemsList = getFinanceTool().getVATItemDetailReport(vatItemName,
					transtartDate, tranendDate);

			VATItemDetail obj = new VATItemDetail();
			if (itemsList != null)
				itemsList.add((VATItemDetail) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return itemsList;

	}

	@Override
	public List<ECSalesList> getECSalesListReport(ClientFinanceDate fromDate,
			ClientFinanceDate toDate) {

		List<ECSalesList> salesList = new ArrayList<ECSalesList>();

		getMinimumAndMaximumDates(fromDate, toDate);

		try {
			salesList = getFinanceTool().getECSalesListReport(transtartDate,
					tranendDate);

			ECSalesList obj = new ECSalesList();
			if (salesList != null)
				salesList.add((ECSalesList) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesList;

	}

	@Override
	public List<ECSalesListDetail> getECSalesListDetailReport(String payeeName,
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {

		List<ECSalesListDetail> salesList = new ArrayList<ECSalesListDetail>();

		getMinimumAndMaximumDates(fromDate, toDate);

		try {
			salesList = getFinanceTool().getECSalesListDetailReport(payeeName,
					transtartDate, tranendDate);

			ECSalesListDetail obj = new ECSalesListDetail();
			if (salesList != null)
				salesList.add((ECSalesListDetail) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesList;

	}

	@Override
	public List<ReverseChargeListDetail> getReverseChargeListDetailReport(
			String payeeName, ClientFinanceDate fromDate,
			ClientFinanceDate toDate) {

		List<ReverseChargeListDetail> salesList = new ArrayList<ReverseChargeListDetail>();

		getMinimumAndMaximumDates(fromDate, toDate);

		try {
			salesList = getFinanceTool().getReverseChargeListDetailReport(
					payeeName, transtartDate, tranendDate);

			ReverseChargeListDetail obj = new ReverseChargeListDetail();
			if (salesList != null)
				salesList.add((ReverseChargeListDetail) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesList;

	}

	@Override
	public List<ReverseChargeList> getReverseChargeListReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {

		List<ReverseChargeList> salesList = new ArrayList<ReverseChargeList>();

		getMinimumAndMaximumDates(fromDate, toDate);

		try {
			salesList = getFinanceTool().getReverseChargeListReport(
					transtartDate, tranendDate);

			ReverseChargeList obj = new ReverseChargeList();
			if (salesList != null)
				salesList.add((ReverseChargeList) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesList;

	}

	private void getMinimumAndMaximumDates(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {

		// if ((startDate.equals("") || startDate == null)
		// || (endDate.equals("") || endDate == null)) {

		List<ClientFinanceDate> dates = getMinimumAndMaximumTransactionDate();
		ClientFinanceDate startDate1 = dates.get(0) == null ? new ClientFinanceDate()
				: dates.get(0);
		ClientFinanceDate endDate2 = dates.get(1) == null ? new ClientFinanceDate()
				: dates.get(1);

		if (startDate.isEmpty())
			transtartDate = new FinanceDate(startDate1);
		else
			transtartDate = new FinanceDate(startDate);

		if (endDate.isEmpty())
			tranendDate = new FinanceDate(endDate2);
		else
			tranendDate = new FinanceDate(endDate);

	}

	@Override
	public List<DummyDebitor> getDebitors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) throws AccounterException {

		getMinimumAndMaximumDates(startDate, endDate);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		List<DummyDebitor> debitors = new ArrayList<DummyDebitor>();
		Calendar calender = Calendar.getInstance();
		// calender.add(Calendar.MONTH, 1);
		// Date oneMonthPreviousDate = calender.getTime();
		// calender.add(Calendar.MONTH, 1);
		// Date twoMonthPreviousDate = calender.getTime();
		// calender.add(Calendar.MONTH, 1);
		// Date threeMonthPreviousDate = calender.getTime();
		try {
			List<AgedDebtors> agedDebtors = getFinanceTool().getAgedDebtors(
					transtartDate, tranendDate);

			debitors = getDebtorsWidSameName(agedDebtors, transtartDate,
					tranendDate);

			DummyDebitor obj = new DummyDebitor();
			if (debitors != null)
				debitors.add((DummyDebitor) setStartEndDates(obj));

		} catch (DAOException e) {

			e.printStackTrace();
		}

		return debitors;
	}

	/*
	 * This method calculates the totals for individual debtors and prepares
	 * single obj for those debtors who has same name
	 */
	private List<DummyDebitor> getDebtorsWidSameName(
			List<AgedDebtors> agedDebtors, FinanceDate startdate,
			FinanceDate enddate) {
		List<DummyDebitor> listDebtors = new ArrayList<DummyDebitor>();
		Map<Integer, List<AgedDebtors>> sameDebtorsMap = new HashMap<Integer, List<AgedDebtors>>();
		List<AgedDebtors> sameDebtors;
		List<AgedDebtors> tempDtrs = new ArrayList<AgedDebtors>();
		String name;
		int listSize = agedDebtors.size();

		/*
		 * seperating(made a list and it to a map wid a key) debtors who has
		 * same name
		 */
		for (int i = 0; !agedDebtors.isEmpty(); i++) {

			sameDebtors = new ArrayList<AgedDebtors>();

			// if (agedDebtors.get(i).getCategory() == 5) {
			// agedDebtors.remove(i);
			// break;
			// }

			/* Initialising list with atleast one obj */
			if (!tempDtrs.contains(agedDebtors.get(0)))
				sameDebtors.add(agedDebtors.get(0));
			name = agedDebtors.get(0).getName();
			agedDebtors.remove(0);

			/* Iterating from next available obj in the list */
			for (AgedDebtors debitorAgedDebtors : agedDebtors) {
				if (name.equals(debitorAgedDebtors.getName())
						&& !tempDtrs.contains(debitorAgedDebtors)) {
					sameDebtors.add(debitorAgedDebtors);
					tempDtrs.add(debitorAgedDebtors);
				}
			}
			/*
			 * changing decision var. with modifed list size.And intialize name
			 * with 1st available obj in the list
			 */
			agedDebtors.removeAll(tempDtrs);
			listSize = agedDebtors.size();
			if (listSize != 0)
				name = agedDebtors.get(0).getName();

			/* add the list to map */
			sameDebtorsMap.put(i, sameDebtors);
		}

		/*
		 * Iterating the map for calculating the totals for the debtors who has
		 * same name
		 */
		for (Map.Entry<Integer, List<AgedDebtors>> sameDtr : sameDebtorsMap
				.entrySet()) {
			List<AgedDebtors> SimilarDtrs = sameDtr.getValue();
			double dayscurrentTotal = 0.0;
			double days30Total = 0.0;
			double days60Total = 0.0;
			double days90Total = 0.0;
			double daysOlderTotal = 0.0;
			long days = 0;
			for (AgedDebtors agdDebtor : SimilarDtrs) {
				// // if (!agdDebtor.getDate().after(enddate)) {
				// days = UIUtils.getDays_between(agdDebtor.getDate()
				// .getDateAsObject(), enddate.getDateAsObject());
				// }

				days = agdDebtor.getAgeing();

				if (days <= 0)
					dayscurrentTotal += agdDebtor.getTotal();
				else if (days > 0 && days <= 30)
					days30Total += agdDebtor.getTotal();
				else if (days > 30 && days <= 60)
					days60Total += agdDebtor.getTotal();
				else if (days > 60 && days <= 90)
					days90Total += agdDebtor.getTotal();
				else if (days > 90)
					daysOlderTotal += agdDebtor.getTotal();
			}
			AgedDebtors dtr = SimilarDtrs.get(0);

			DummyDebitor debitor = new DummyDebitor();
			debitor.setDebitdays_incurrent(dayscurrentTotal);
			debitor.setDebitdays_in30(days30Total);
			debitor.setDebitdays_in60(days60Total);
			debitor.setDebitdays_in90(days90Total);
			debitor.setDebitdays_inolder(daysOlderTotal);
			debitor.setDebitorName(dtr.getName());
			debitor.setDueDate(dtr.getDueDate());
			debitor.setTransactionId(dtr.getTransactionId());
			listDebtors.add(debitor);
		}

		return listDebtors;
	}

	@Override
	public List<DummyDebitor> getCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) throws AccounterException {

		getMinimumAndMaximumDates(startDate, endDate);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		List<DummyDebitor> Creditors = new ArrayList<DummyDebitor>();
		// Calendar calender = Calendar.getInstance();
		// calender.add(Calendar.MONTH, 1);
		// Date oneMonthPreviousDate = calender.getTime();
		// calender.add(Calendar.MONTH, 1);
		// Date twoMonthPreviousDate = calender.getTime();
		// calender.add(Calendar.MONTH, 1);
		// Date threeMonthPreviousDate = calender.getTime();
		try {
			List<AgedDebtors> agedCreditors = getFinanceTool()
					.getAgedCreditors(transtartDate, tranendDate);

			Creditors = getDebtorsWidSameName(agedCreditors, transtartDate,
					tranendDate);

			DummyDebitor obj = new DummyDebitor();
			if (Creditors != null)
				Creditors.add((DummyDebitor) setStartEndDates(obj));

		} catch (DAOException e) {

			e.printStackTrace();
		}

		return Creditors;
	}

	@Override
	public List<ExpenseList> getExpenseReportByType(int status,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {

		List<ExpenseList> expenseList = new ArrayList<ExpenseList>();

		getMinimumAndMaximumDates(startDate, endDate);

		try {
			expenseList = getFinanceTool().getExpenseReportByType(status,
					transtartDate, tranendDate);

			ExpenseList obj = new ExpenseList();

			if (expenseList != null)
				expenseList.add((ExpenseList) setStartEndDates(obj));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return expenseList;
	}

	@Override
	public List<DepositDetail> getDepositDetail(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		List<DepositDetail> transDetailByAccountList = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			transDetailByAccountList = getFinanceTool().getDepositDetail(
					transtartDate, tranendDate);

			DepositDetail obj = new DepositDetail();
			if (transDetailByAccountList != null)
				transDetailByAccountList
						.add((DepositDetail) setStartEndDates(obj));

			// transDetailByAccountList = (List<TransactionDetailByAccount>)
			// manager
			// .merge(transDetailByAccountList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transDetailByAccountList;
	}

	@Override
	public List<CheckDetailReport> getCheckDetailReport(long paymentmethod,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {

		List<CheckDetailReport> checkDetailReports = null;

		getMinimumAndMaximumDates(startDate, endDate);

		try {

			checkDetailReports = getFinanceTool().getCheckDetailReport(
					paymentmethod, transtartDate, tranendDate);

			CheckDetailReport obj = new CheckDetailReport();
			if (checkDetailReports != null)
				checkDetailReports
						.add((CheckDetailReport) setStartEndDates(obj));

			// transDetailByAccountList = (List<TransactionDetailByAccount>)
			// manager
			// .merge(transDetailByAccountList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return checkDetailReports;
	}

	@Override
	public List<PayeeStatementsList> getStatements(long id,
			long transactionDate, ClientFinanceDate fromDate,
			ClientFinanceDate toDate, int noOfDays,
			boolean isEnabledOfZeroBalBox,
			boolean isEnabledOfLessthanZeroBalBox,
			double lessThanZeroBalanceValue,
			boolean isEnabledOfNoAccountActivity,
			boolean isEnabledOfInactiveCustomer) {

		List<PayeeStatementsList> resultList = null;
		getMinimumAndMaximumDates(fromDate, toDate);
		try {

			resultList = getFinanceTool().getPayeeStatementsList(id,
					transactionDate, transtartDate, tranendDate, noOfDays,
					isEnabledOfZeroBalBox, isEnabledOfLessthanZeroBalBox,
					lessThanZeroBalanceValue, isEnabledOfNoAccountActivity,
					isEnabledOfInactiveCustomer);

			PayeeStatementsList obj = new PayeeStatementsList();
			if (resultList != null)
				resultList.add((PayeeStatementsList) setStartEndDates(obj));
			// for (PayeeStatementsList obj : list) {
			//
			// resultList.add((PayeeStatementsList) setStartEndDates(obj));
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultList;
	}

}