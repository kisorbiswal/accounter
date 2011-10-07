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
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.web.client.IAccounterReportService;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientBudget;
import com.vimukti.accounter.web.client.core.ClientBudgetItem;
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
import com.vimukti.accounter.web.client.core.reports.ClientBudgetList;
import com.vimukti.accounter.web.client.core.reports.DepositDetail;
import com.vimukti.accounter.web.client.core.reports.ECSalesList;
import com.vimukti.accounter.web.client.core.reports.ECSalesListDetail;
import com.vimukti.accounter.web.client.core.reports.ExpenseList;
import com.vimukti.accounter.web.client.core.reports.MISC1099TransactionDetail;
import com.vimukti.accounter.web.client.core.reports.MostProfitableCustomers;
import com.vimukti.accounter.web.client.core.reports.ProfitAndLossByLocation;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeList;
import com.vimukti.accounter.web.client.core.reports.ReverseChargeListDetail;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationDetails;
import com.vimukti.accounter.web.client.core.reports.SalesByLocationSummary;
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

public class AccounterReportServiceImpl extends AccounterRPCBaseServiceImpl
		implements IAccounterReportService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	public AccounterReportServiceImpl() {
		super();

	}

	private BaseReport setStartEndDates(BaseReport obj,
			FinanceDate[] financeDates) {
		// if (Utility.stringToDate(new
		// ClientFinanceDate(transtartDate).toString()) != null)
		obj.setStartDate(financeDates[0].toClientFinanceDate());
		// if (Utility.stringToDate(new
		// ClientFinanceDate(transtartDate).toString()) != null)
		obj.setEndDate(financeDates[1].toClientFinanceDate());
		return obj;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getSalesByCustomerSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return salesByCustomerSummary(startDate, endDate, getCompanyId());
	}

	private ArrayList<SalesByCustomerDetail> salesByCustomerSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetailList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);
		try {

			salesByCustomerDetailList = getFinanceTool().getSalesManager()
					.getSalesByCustomerSummary(financeDates[0],
							financeDates[1], companyId);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

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
	public ArrayList<AccountRegister> getAccountRegister(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long accountId) {
		ArrayList<AccountRegister> accountRegisterList = new ArrayList<AccountRegister>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			accountRegisterList = getFinanceTool()
					.getAccountRegister(financeDates[0], financeDates[1],
							accountId, getCompanyId());

			// if (accountRegisterList != null)
			// accountRegisterList.add((AccountRegister) setStartEndDates(obj,
			// financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return accountRegisterList;
	}

	@Override
	public ArrayList<AgedDebtors> getAgedCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		return agedCreditorsReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<AgedDebtors> agedCreditorsReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		ArrayList<AgedDebtors> agedDebtorsList = new ArrayList<AgedDebtors>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			agedDebtorsList = getFinanceTool().getReportManager()
					.getAgedCreditors(financeDates[0], financeDates[1],
							companyId);
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
				agedDebtorsList.add((AgedDebtors) setStartEndDates(obj,
						financeDates));

			// agedDebtorsList = (List<AgedDebtors>) manager
			// .merge(agedDebtorsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return agedDebtorsList;
	}

	@Override
	public ArrayList<AgedDebtors> getAgedDebtors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		return agedDebtorsReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<AgedDebtors> agedDebtorsReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		ArrayList<AgedDebtors> agedDebtorsList = new ArrayList<AgedDebtors>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			agedDebtorsList = getFinanceTool()
					.getReportManager()
					.getAgedDebtors(financeDates[0], financeDates[1], companyId);
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
				agedDebtorsList.add((AgedDebtors) (setStartEndDates(obj,
						financeDates)));

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

	private List<AgedDebtors> updateDebtorListByName(List<AgedDebtors> debtors,
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
	public ArrayList<AmountsDueToVendor> getAmountsDueToVendor(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<AmountsDueToVendor> amountsDueToVendorList = new ArrayList<AmountsDueToVendor>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			amountsDueToVendorList = getFinanceTool().getVendorManager()
					.getAmountsDueToVendor(financeDates[0], financeDates[1],
							getCompanyId());

			AmountsDueToVendor obj = new AmountsDueToVendor();
			if (amountsDueToVendorList != null)
				amountsDueToVendorList
						.add((AmountsDueToVendor) setStartEndDates(obj,
								financeDates));

			// amountsDueToVendorList = (List<AmountsDueToVendor>) manager
			// .merge(amountsDueToVendorList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return amountsDueToVendorList;
	}

	@Override
	public ArrayList<TransactionHistory> getCustomerTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return customerTransactionHistory(startDate, endDate, getCompanyId());
	}

	private ArrayList<TransactionHistory> customerTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<TransactionHistory> transactionHistoryList = new ArrayList<TransactionHistory>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			transactionHistoryList = getFinanceTool().getCustomerManager()
					.getCustomerTransactionHistory(financeDates[0],
							financeDates[1], companyId);

			TransactionHistory obj = new TransactionHistory();
			if (transactionHistoryList != null)
				transactionHistoryList
						.add((TransactionHistory) setStartEndDates(obj,
								financeDates));

			// transactionHistoryList = (List<TransactionHistory>) manager
			// .merge(transactionHistoryList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transactionHistoryList;

	}

	@Override
	public ArrayList<MostProfitableCustomers> getMostProfitableCustomers(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<MostProfitableCustomers> mostProfitableCustomersList = new ArrayList<MostProfitableCustomers>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			mostProfitableCustomersList = getFinanceTool().getCustomerManager()
					.getMostProfitableCustomers(financeDates[0],
							financeDates[1], getCompanyId());

			MostProfitableCustomers obj = new MostProfitableCustomers();
			if (mostProfitableCustomersList != null)
				mostProfitableCustomersList
						.add((MostProfitableCustomers) setStartEndDates(obj,
								financeDates));

			// mostProfitableCustomersList = (List<MostProfitableCustomers>)
			// manager
			// .merge(mostProfitableCustomersList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mostProfitableCustomersList;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByItemDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetailList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			salesByCustomerDetailList = getFinanceTool().getPurchageManager()
					.getPurchasesByItemDetail(financeDates[0], financeDates[1],
							getCompanyId());

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByItemSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetailList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			salesByCustomerDetailList = getFinanceTool().getPurchageManager()
					.getPurchasesByItemSummary(financeDates[0],
							financeDates[1], getCompanyId());

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return purchasesByVendorDetail(startDate, endDate, getCompanyId());
	}

	private ArrayList<SalesByCustomerDetail> purchasesByVendorDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<SalesByCustomerDetail> salesByCustomerDetailList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			salesByCustomerDetailList = getFinanceTool().getVendorManager()
					.getPurchasesByVendorDetail(financeDates[0],
							financeDates[1], companyId);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;

	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetailList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			salesByCustomerDetailList = getFinanceTool().getVendorManager()
					.getPurchasesByVendorSummary(financeDates[0],
							financeDates[1], getCompanyId());

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			salesByCustomerDetailList
					.add((SalesByCustomerDetail) setStartEndDates(obj,
							financeDates));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	@Override
	public ArrayList<ClientTransaction> getRegister(long accountId) {
		ArrayList<ClientTransaction> clientTransactionList = new ArrayList<ClientTransaction>();

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
	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return salesByCustomerDetailReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<SalesByCustomerDetail> salesByCustomerDetailReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<SalesByCustomerDetail> salesByCustomerDetailList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			salesByCustomerDetailList = getFinanceTool().getReportManager()
					.getSalesByCustomerDetailReport(financeDates[0],
							financeDates[1], companyId);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;

	}

	@Override
	public ArrayList<SalesByCustomerDetail> getSalesByItemDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetailList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			salesByCustomerDetailList = getFinanceTool().getSalesManager()
					.getSalesByItemDetail(financeDates[0], financeDates[1],
							getCompanyId());

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getSalesByItemSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return salesByItemSummary(startDate, endDate, getCompanyId());
	}

	private ArrayList<SalesByCustomerDetail> salesByItemSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		ArrayList<SalesByCustomerDetail> salesByCustomerDetailList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			salesByCustomerDetailList = getFinanceTool().getSalesManager()
					.getSalesByItemSummary(financeDates[0], financeDates[1],
							companyId);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomerDetailList != null)
				salesByCustomerDetailList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomerDetailList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomerDetailList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomerDetailList;
	}

	@Override
	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return transactionDetailByTaxItem(startDate, endDate, getCompanyId());
	}

	private ArrayList<TransactionDetailByTaxItem> transactionDetailByTaxItem(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<TransactionDetailByTaxItem> transactionDetailByTaxItemList = new ArrayList<TransactionDetailByTaxItem>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			transactionDetailByTaxItemList = getFinanceTool()
					.getReportManager().getTransactionDetailByTaxItem(
							financeDates[0], financeDates[1], companyId);

			TransactionDetailByTaxItem obj = new TransactionDetailByTaxItem();
			if (transactionDetailByTaxItemList != null)
				transactionDetailByTaxItemList
						.add((TransactionDetailByTaxItem) setStartEndDates(obj,
								financeDates));

			// transactionDetailByTaxcodeList =
			// (List<TransactionDetailByTaxcode>) manager
			// .merge(transactionDetailByTaxcodeList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transactionDetailByTaxItemList;

	}

	@Override
	public ArrayList<TrialBalance> getTrialBalance(ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		return trialBalanceReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<TrialBalance> trialBalanceReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<TrialBalance> trialBalanceList = new ArrayList<TrialBalance>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			trialBalanceList = getFinanceTool().getReportManager()
					.getTrialBalance(financeDates[0], financeDates[1],
							companyId);

			TrialBalance obj = new TrialBalance();
			if (trialBalanceList != null)
				trialBalanceList.add((TrialBalance) setStartEndDates(obj,
						financeDates));

			// trialBalanceList = (List<TrialBalance>) manager
			// .merge(trialBalanceList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return trialBalanceList;

	}

	@Override
	public ArrayList<TransactionHistory> getVendorTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return vendorTransactionHistory(startDate, endDate, getCompanyId());
	}

	private ArrayList<TransactionHistory> vendorTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<TransactionHistory> transactionHistoryList = new ArrayList<TransactionHistory>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			transactionHistoryList = getFinanceTool().getVendorManager()
					.getVendorTransactionHistory(financeDates[0],
							financeDates[1], companyId);

			TransactionHistory obj = new TransactionHistory();
			if (transactionHistoryList != null)
				transactionHistoryList
						.add((TransactionHistory) setStartEndDates(obj,
								financeDates));

			// transactionHistoryList = (List<TransactionHistory>) manager
			// .merge(transactionHistoryList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transactionHistoryList;

	}

	@Override
	public ArrayList<ClientItem> getPurchaseReportItems(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<ClientItem> clientItemList = new ArrayList<ClientItem>();
		List<Item> serverItemsList = new ArrayList<Item>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			serverItemsList = getFinanceTool().getPurchageManager()
					.getPurchaseReportItems(financeDates[0], financeDates[1],
							getCompanyId());

			ClientItem obj = new ClientItem();
			if (clientItemList != null)
				clientItemList.add((ClientItem) setStartEndDates(obj,
						financeDates));

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
	public ArrayList<ClientItem> getSalesReportItems(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<ClientItem> clientItemList = new ArrayList<ClientItem>();
		List<Item> serverItemsList = new ArrayList<Item>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			serverItemsList = getFinanceTool().getSalesManager()
					.getSalesReportItems(financeDates[0], financeDates[1],
							getCompanyId());

			ClientItem obj = new ClientItem();
			if (clientItemList != null)
				clientItemList.add((ClientItem) setStartEndDates(obj,
						financeDates));

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
	public ArrayList<ClientCustomer> getTransactionHistoryCustomers(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<ClientCustomer> clientCustomerList = new ArrayList<ClientCustomer>();
		List<Customer> serverCustomerList = new ArrayList<Customer>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			serverCustomerList = getFinanceTool().getCustomerManager()
					.getTransactionHistoryCustomers(financeDates[0],
							financeDates[1], getCompanyId());

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
	public ArrayList<ClientVendor> getTransactionHistoryVendors(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<ClientVendor> clientVendorList = new ArrayList<ClientVendor>();
		List<Vendor> serverVendorList = new ArrayList<Vendor>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			serverVendorList = getFinanceTool().getVendorManager()
					.getTransactionHistoryVendors(financeDates[0],
							financeDates[1], getCompanyId());
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
	public ArrayList<SalesTaxLiability> getSalesTaxLiabilityReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {

		ArrayList<SalesTaxLiability> salesTaxLiabilityList = new ArrayList<SalesTaxLiability>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			salesTaxLiabilityList = getFinanceTool().getReportManager()
					.getSalesTaxLiabilityReport(financeDates[0],
							financeDates[1], getCompanyId());

			SalesTaxLiability obj = new SalesTaxLiability();
			if (salesTaxLiabilityList != null)
				salesTaxLiabilityList.add((SalesTaxLiability) setStartEndDates(
						obj, financeDates));
			// salesTaxLiabilityList = (List<SalesTaxLiability>) manager
			// .merge(salesTaxLiabilityList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesTaxLiabilityList;
	}

	@Override
	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return transactionDetailByAccount(startDate, endDate, getCompanyId());
	}

	private ArrayList<TransactionDetailByAccount> transactionDetailByAccount(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		ArrayList<TransactionDetailByAccount> transDetailByAccountList = new ArrayList<TransactionDetailByAccount>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			transDetailByAccountList = getFinanceTool().getReportManager()
					.getTransactionDetailByAccount(financeDates[0],
							financeDates[1], companyId);

			TransactionDetailByAccount obj = new TransactionDetailByAccount();
			if (transDetailByAccountList != null)
				transDetailByAccountList
						.add((TransactionDetailByAccount) setStartEndDates(obj,
								financeDates));

			// transDetailByAccountList = (List<TransactionDetailByAccount>)
			// manager
			// .merge(transDetailByAccountList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transDetailByAccountList;

	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByItemDetail(
			String itemName, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		ArrayList<SalesByCustomerDetail> salesByCustomertList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			salesByCustomertList = getFinanceTool().getPurchageManager()
					.getPurchasesByItemDetail(itemName, financeDates[0],
							financeDates[1], getCompanyId());

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomertList != null)
				salesByCustomertList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomertList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomertList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomertList;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String vendorName, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		ArrayList<SalesByCustomerDetail> salesByCustomertList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			salesByCustomertList = getFinanceTool().getVendorManager()
					.getPurchasesByVendorDetail(vendorName, financeDates[0],
							financeDates[1], getCompanyId());

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomertList != null)
				salesByCustomertList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomertList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomertList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomertList;
	}

	@Override
	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String customerName, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		return salesByCustomerDetailReport(customerName, startDate, endDate,
				getCompanyId());
	}

	private ArrayList<SalesByCustomerDetail> salesByCustomerDetailReport(
			String customerName, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {

		ArrayList<SalesByCustomerDetail> salesByCustomertList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			salesByCustomertList = getFinanceTool().getReportManager()
					.getSalesByCustomerDetailReport(customerName,
							financeDates[0], financeDates[1], companyId);

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomertList != null)
				salesByCustomertList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomertList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomertList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomertList;

	}

	@Override
	public ArrayList<SalesByCustomerDetail> getSalesByItemDetail(
			String itemName, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		ArrayList<SalesByCustomerDetail> salesByCustomertList = new ArrayList<SalesByCustomerDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			salesByCustomertList = getFinanceTool().getSalesManager()
					.getSalesByItemDetail(itemName, financeDates[0],
							financeDates[1], getCompanyId());

			SalesByCustomerDetail obj = new SalesByCustomerDetail();
			if (salesByCustomertList != null)
				salesByCustomertList
						.add((SalesByCustomerDetail) setStartEndDates(obj,
								financeDates));

			// salesByCustomertList = (List<SalesByCustomerDetail>) manager
			// .merge(salesByCustomertList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByCustomertList;
	}

	@Override
	public ArrayList<AgedDebtors> getAgedDebtors(String Name,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return agedDebtorsReport(Name, startDate, endDate, getCompanyId());
	}

	private ArrayList<AgedDebtors> agedDebtorsReport(String name,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<AgedDebtors> agedDebtorsList = new ArrayList<AgedDebtors>();
		ArrayList<AgedDebtors> agedDebtorsListForCustomer = new ArrayList<AgedDebtors>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			agedDebtorsList = getFinanceTool()
					.getReportManager()
					.getAgedDebtors(financeDates[0], financeDates[1], companyId);
			for (AgedDebtors agdDebitor : agedDebtorsList) {
				if (name.equals(agdDebitor.getName()))
					agedDebtorsListForCustomer.add(agdDebitor);
			}
			AgedDebtors obj = new AgedDebtors();
			if (agedDebtorsListForCustomer != null)
				agedDebtorsListForCustomer.add((AgedDebtors) (setStartEndDates(
						obj, financeDates)));

			// agedDebtorsList = (List<AgedDebtors>) manager
			// .merge(agedDebtorsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return agedDebtorsListForCustomer;

	}

	@Override
	public ArrayList<AgedDebtors> getAgedCreditors(String Name,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return agedCreditorsReport(Name, startDate, endDate, getCompanyId());
	}

	private ArrayList<AgedDebtors> agedCreditorsReport(String Name,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<AgedDebtors> agedDebtorsList = new ArrayList<AgedDebtors>();
		ArrayList<AgedDebtors> agedCreditorsListForCustomer = new ArrayList<AgedDebtors>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			agedDebtorsList = getFinanceTool().getReportManager()
					.getAgedCreditors(financeDates[0], financeDates[1],
							companyId);
			for (AgedDebtors agdDebitor : agedDebtorsList) {
				if (Name.equals(agdDebitor.getName()))
					agedCreditorsListForCustomer.add(agdDebitor);
			}
			AgedDebtors obj = new AgedDebtors();
			if (agedCreditorsListForCustomer != null)
				agedCreditorsListForCustomer
						.add((AgedDebtors) (setStartEndDates(obj, financeDates)));

			// agedDebtorsList = (List<AgedDebtors>) manager
			// .merge(agedDebtorsList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return agedCreditorsListForCustomer;

	}

	@Override
	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			String accountName, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		return transactionDetailByAccount(accountName, startDate, endDate,
				getCompanyId());
	}

	private ArrayList<TransactionDetailByAccount> transactionDetailByAccount(
			String accountName, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		ArrayList<TransactionDetailByAccount> transDetailByAccountList = new ArrayList<TransactionDetailByAccount>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			transDetailByAccountList = getFinanceTool().getReportManager()
					.getTransactionDetailByAccount(accountName,
							financeDates[0], financeDates[1], companyId);

			TransactionDetailByAccount obj = new TransactionDetailByAccount();
			if (transDetailByAccountList != null)
				transDetailByAccountList
						.add((TransactionDetailByAccount) setStartEndDates(obj,
								financeDates));

			// transDetailByAccountList = (List<TransactionDetailByAccount>)
			// manager
			// .merge(transDetailByAccountList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transDetailByAccountList;
	}

	private ArrayList<ClientFinanceDate> getMinimumAndMaximumTransactionDate(long companyId) {
		ArrayList<ClientFinanceDate> transactionDates = new ArrayList<ClientFinanceDate>();
		try {

			ClientFinanceDate[] dates = getFinanceTool().getManager()
					.getMinimumAndMaximumTransactionDate(companyId);
			transactionDates.add(dates[0]);
			transactionDates.add(dates[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionDates;
	}
	@Override
	public ArrayList<ClientFinanceDate> getMinimumAndMaximumTransactionDate(
			) {
		return getMinimumAndMaximumTransactionDate(getCompanyId());
	}

	@Override
	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			final String taxItemName, final ClientFinanceDate startDate,
			final ClientFinanceDate endDate) {
		return transactionDetailByTaxItem(taxItemName, startDate, endDate,
				getCompanyId());
	}

	private ArrayList<TransactionDetailByTaxItem> transactionDetailByTaxItem(
			String taxItemName, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		ArrayList<TransactionDetailByTaxItem> transactionDetailByTaxItemList = new ArrayList<TransactionDetailByTaxItem>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			transactionDetailByTaxItemList = getFinanceTool()
					.getReportManager().getTransactionDetailByTaxItem(
							taxItemName, financeDates[0], financeDates[1],
							companyId);

			TransactionDetailByTaxItem obj = new TransactionDetailByTaxItem();
			if (transactionDetailByTaxItemList != null)
				transactionDetailByTaxItemList
						.add((TransactionDetailByTaxItem) setStartEndDates(obj,
								financeDates));

			// transactionDetailByTaxcodeList =
			// (List<TransactionDetailByTaxcode>) manager
			// .merge(transactionDetailByTaxcodeList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transactionDetailByTaxItemList;
	}

	@Override
	public ArrayList<TrialBalance> getBalanceSheetReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return balanceSheetReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<TrialBalance> balanceSheetReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		ArrayList<TrialBalance> trialbalanceList = new ArrayList<TrialBalance>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			trialbalanceList = getFinanceTool().getReportManager()
					.getBalanceSheetReport(financeDates[0], financeDates[1],
							companyId);

			TrialBalance obj = new TrialBalance();
			if (trialbalanceList != null)
				trialbalanceList.add((TrialBalance) setStartEndDates(obj,
						financeDates));

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
	public ArrayList<TrialBalance> getCashFlowReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<TrialBalance> trialbalanceList = new ArrayList<TrialBalance>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			trialbalanceList = getFinanceTool().getReportManager()
					.getCashFlowReport(financeDates[0], financeDates[1],
							getCompanyId());

			TrialBalance obj = new TrialBalance();
			if (trialbalanceList != null)
				trialbalanceList.add((TrialBalance) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return trialbalanceList;
	}

	public ArrayList<ProfitAndLossByLocation> getProfitAndLossByLocationReport(
			boolean isLocation, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		ArrayList<ProfitAndLossByLocation> profitAndLossByLocationList = new ArrayList<ProfitAndLossByLocation>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {
			profitAndLossByLocationList = getFinanceTool().getReportManager()
					.getProfitAndLossByLocation(isLocation, financeDates[0],
							financeDates[1], getCompanyId());
			ProfitAndLossByLocation obj = new ProfitAndLossByLocation();
			if (profitAndLossByLocationList != null)
				profitAndLossByLocationList
						.add((ProfitAndLossByLocation) setStartEndDates(obj,
								financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return profitAndLossByLocationList;
	}

	@Override
	public ArrayList<SalesByLocationDetails> getSalesByLocationDetailsReport(
			boolean isLocation, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		return salesByLocationDetailsReport(isLocation, startDate, endDate,
				getCompanyId());
	}

	private ArrayList<SalesByLocationDetails> salesByLocationDetailsReport(
			boolean isLocation, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {

		ArrayList<SalesByLocationDetails> salesByLocationDetailList = new ArrayList<SalesByLocationDetails>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {
			salesByLocationDetailList = getFinanceTool().getSalesManager()
					.getSalesByLocationDetail(isLocation, financeDates[0],
							financeDates[1], companyId);
			SalesByLocationDetails obj = new SalesByLocationDetails();
			if (salesByLocationDetailList != null)
				salesByLocationDetailList
						.add((SalesByLocationDetails) setStartEndDates(obj,
								financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByLocationDetailList;

	}

	@Override
	public ArrayList<SalesByLocationDetails> getSalesByLocationDetailsForLocation(
			boolean isLocation, String locationName,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<SalesByLocationDetails> salesByLocationDetailList = new ArrayList<SalesByLocationDetails>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {
			salesByLocationDetailList = getFinanceTool().getSalesManager()
					.getSalesByLocationDetailForLocation(isLocation,
							locationName, financeDates[0], financeDates[1],
							getCompanyId());
			SalesByLocationDetails obj = new SalesByLocationDetails();
			if (salesByLocationDetailList != null)
				salesByLocationDetailList
						.add((SalesByLocationDetails) setStartEndDates(obj,
								financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByLocationDetailList;
	}

	@Override
	public ArrayList<SalesByLocationSummary> getSalesByLocationSummaryReport(
			boolean isLocation, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		return salesByLocationSummary(isLocation, startDate, endDate,
				getCompanyId());
	}

	private ArrayList<SalesByLocationSummary> salesByLocationSummary(
			boolean isLocation, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {

		ArrayList<SalesByLocationSummary> salesByLocationDetailList = new ArrayList<SalesByLocationSummary>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {
			salesByLocationDetailList = getFinanceTool().getSalesManager()
					.getSalesByLocationSummary(isLocation, financeDates[0],
							financeDates[1], companyId);
			SalesByLocationSummary obj = new SalesByLocationSummary();
			if (salesByLocationDetailList != null)
				salesByLocationDetailList
						.add((SalesByLocationSummary) setStartEndDates(obj,
								financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesByLocationDetailList;

	}

	@Override
	public ArrayList<TrialBalance> getProfitAndLossReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return profitAndLossReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<TrialBalance> profitAndLossReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		ArrayList<TrialBalance> trialbalanceList = new ArrayList<TrialBalance>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {

			trialbalanceList = getFinanceTool().getReportManager()
					.getProfitAndLossReport(financeDates[0], financeDates[1],
							companyId);

			TrialBalance obj = new TrialBalance();
			if (trialbalanceList != null)
				trialbalanceList.add((TrialBalance) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return trialbalanceList;
	}

	@Override
	public ArrayList<OpenAndClosedOrders> getPurchaseOpenOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return purchaseOpenOrderReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<OpenAndClosedOrders> purchaseOpenOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<OpenAndClosedOrders> purchaseOrders = new ArrayList<OpenAndClosedOrders>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {
			purchaseOrders = getFinanceTool().getPurchageManager()
					.getOpenPurchaseOrders(financeDates[0], financeDates[1],
							companyId);

			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (purchaseOrders != null)
				purchaseOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrders;

	}

	@Override
	public ArrayList<OpenAndClosedOrders> getPurchaseCompletedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return purchaseCompletedOrderReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<OpenAndClosedOrders> purchaseCompletedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<OpenAndClosedOrders> purchaseOrders = new ArrayList<OpenAndClosedOrders>();
		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);
		try {
			purchaseOrders = getFinanceTool().getPurchageManager()
					.getCompletedPurchaseOrders(financeDates[0],
							financeDates[1], companyId);
			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (purchaseOrders != null)
				purchaseOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
						financeDates));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrders;

	}

	@Override
	public ArrayList<OpenAndClosedOrders> getPurchaseCancelledOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return purchaseCancelledOrderReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<OpenAndClosedOrders> purchaseCancelledOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		ArrayList<OpenAndClosedOrders> purchaseOrders = new ArrayList<OpenAndClosedOrders>();
		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);
		try {
			purchaseOrders = getFinanceTool().getPurchageManager()
					.getCanceledPurchaseOrders(financeDates[0],
							financeDates[1], companyId);
			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (purchaseOrders != null)
				purchaseOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
						financeDates));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrders;
	}

	@Override
	public ArrayList<OpenAndClosedOrders> getPurchaseOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return purchaseOrderReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<OpenAndClosedOrders> purchaseOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<OpenAndClosedOrders> purchaseOrders = new ArrayList<OpenAndClosedOrders>();
		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);
		try {
			purchaseOrders = getFinanceTool().getPurchageManager()
					.getPurchaseOrders(financeDates[0], financeDates[1],
							companyId);
			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (purchaseOrders != null)
				purchaseOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
						financeDates));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrders;

	}

	@Override
	public ArrayList<OpenAndClosedOrders> getPurchaseClosedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<OpenAndClosedOrders> purchaseOrders = new ArrayList<OpenAndClosedOrders>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {
			purchaseOrders = getFinanceTool().getPurchageManager()
					.getClosedPurchaseOrders(financeDates[0], financeDates[1],
							getCompanyId());

			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (purchaseOrders != null)
				purchaseOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseOrders;
	}

	@Override
	public ArrayList<OpenAndClosedOrders> getSalesOpenOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return salesOpenOrderReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<OpenAndClosedOrders> salesOpenOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<OpenAndClosedOrders> salesOrders = new ArrayList<OpenAndClosedOrders>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {
			salesOrders = getFinanceTool().getSalesManager()
					.getOpenSalesOrders(financeDates[0], financeDates[1],
							companyId);

			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (salesOrders != null)
				salesOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesOrders;

	}

	@Override
	public ArrayList<OpenAndClosedOrders> getSalesCompletedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return salesCompletedOrderReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<OpenAndClosedOrders> salesCompletedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<OpenAndClosedOrders> salesOrders = new ArrayList<OpenAndClosedOrders>();
		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);
		try {
			salesOrders = getFinanceTool().getSalesManager()
					.getCompletedSalesOrders(financeDates[0], financeDates[1],
							companyId);
			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (salesOrders != null)
				salesOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
						financeDates));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesOrders;

	}

	@Override
	public ArrayList<OpenAndClosedOrders> getSalesOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return salesOrderReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<OpenAndClosedOrders> salesOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<OpenAndClosedOrders> salesOrders = new ArrayList<OpenAndClosedOrders>();
		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);
		try {
			salesOrders = getFinanceTool().getSalesManager().getSalesOrders(
					financeDates[0], financeDates[1], companyId);
			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (salesOrders != null)
				salesOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
						financeDates));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesOrders;
	}

	@Override
	public ArrayList<OpenAndClosedOrders> getSalesCancelledOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return salesCancelledReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<OpenAndClosedOrders> salesCancelledReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		ArrayList<OpenAndClosedOrders> salesOrders = new ArrayList<OpenAndClosedOrders>();
		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);
		try {
			salesOrders = getFinanceTool().getSalesManager()
					.getCanceledSalesOrders(financeDates[0], financeDates[1],
							companyId);
			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (salesOrders != null)
				salesOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesOrders;
	}

	@Override
	public ArrayList<OpenAndClosedOrders> getSalesClosedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<OpenAndClosedOrders> salesOrders = new ArrayList<OpenAndClosedOrders>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {
			salesOrders = getFinanceTool().getSalesManager()
					.getClosedSalesOrders(financeDates[0], financeDates[1],
							getCompanyId());

			OpenAndClosedOrders obj = new OpenAndClosedOrders();
			if (salesOrders != null)
				salesOrders.add((OpenAndClosedOrders) setStartEndDates(obj,
						financeDates));

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
	public ArrayList<VATDetail> getPriorVATReturnVATDetailReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<VATDetail> vatDetailReport = new ArrayList<VATDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());
		// financeDates[0] = "";
		// financeDates[1] = endDate;

		try {
			vatDetailReport = getFinanceTool().getReportManager()
					.getVATDetailReport(financeDates[0], financeDates[1],
							getCompanyId());

			VATDetail obj = new VATDetail();
			if (vatDetailReport != null)
				vatDetailReport.add((VATDetail) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatDetailReport;
	}

	@Override
	public ArrayList<VATDetail> getPriorVATReturnReport(long taxAgency,
			ClientFinanceDate endDate) {
		ArrayList<VATDetail> vatDetailReport = new ArrayList<VATDetail>();

		// FinanceDate[] financeDates = getMinimumAndMaximumDates("", endDate);
		FinanceDate transtartDate = new FinanceDate();
		FinanceDate tranendDate = new FinanceDate();
		transtartDate.clear();
		tranendDate = new FinanceDate(endDate);

		try {
			TAXAgency vatAgncy = (TAXAgency) loadObjectById("TAXAgency",
					taxAgency);
			;
			vatDetailReport = getFinanceTool().getReportManager()
					.getPriorVATReturnVATDetailReport(vatAgncy, tranendDate,
							getCompanyId());

			VATDetail obj = new VATDetail();
			if (vatDetailReport != null)
				vatDetailReport.add((VATDetail) setStartEndDates(obj,
						new FinanceDate[] { transtartDate, tranendDate }));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatDetailReport;
	}

	public ArrayList<VATSummary> getPriorReturnVATSummary(long taxAgncy,
			ClientFinanceDate endDate) {
		return priorReturnVATSummary(taxAgncy, endDate, getCompanyId());
	}

	private ArrayList<VATSummary> priorReturnVATSummary(long taxAgncy,
			ClientFinanceDate endDate, long companyId) {

		ArrayList<VATSummary> vatSummaryList = new ArrayList<VATSummary>();

		try {
			TAXAgency vatAgency = (TAXAgency) loadObjectById(
					AccounterCoreType.TAXAGENCY
							.getServerClassFullyQualifiedName(),
					taxAgncy);
			vatSummaryList = getFinanceTool().getTaxManager()
					.getPriorReturnVATSummary(vatAgency,
							new FinanceDate(endDate), companyId);

			VATSummary obj = new VATSummary();
			if (vatSummaryList != null)
				vatSummaryList.add((VATSummary) setStartEndDates(obj,
						new FinanceDate[] { new FinanceDate(),
								new FinanceDate() }));

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

	public ArrayList<VATSummary> getVAT100Report(long taxAgency,
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {
		return vat100Report(taxAgency, fromDate, toDate, getCompanyId());
	}

	private ArrayList<VATSummary> vat100Report(long taxAgency,
			ClientFinanceDate fromDate, ClientFinanceDate toDate, long companyId) {

		ArrayList<VATSummary> vatSummaryList = new ArrayList<VATSummary>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, companyId);

		try {
			TAXAgency vatAgency = (TAXAgency) loadObjectById(
					AccounterCoreType.TAXAGENCY
							.getServerClassFullyQualifiedName(),
					taxAgency);
			vatSummaryList = getFinanceTool().getReportManager()
					.getVAT100Report(vatAgency, financeDates[0],
							financeDates[1], companyId);

			VATSummary obj = new VATSummary();
			if (vatSummaryList != null)
				vatSummaryList.add((VATSummary) setStartEndDates(obj,
						financeDates));

			/*
			 * //* Removing Box 3 and Box 5 from list, as the calculations for
			 * box 3 and box 5 are done in gui
			 *///
				// vatSummaryList.remove(2);
				// vatSummaryList.remove(3);
			double box1 = vatSummaryList.get(0).getValue();
			double box2 = vatSummaryList.get(1).getValue();
			vatSummaryList.get(2).setValue(box1 + box2);

			double box3 = vatSummaryList.get(2).getValue();
			double box4 = vatSummaryList.get(3).getValue();
			vatSummaryList.get(4).setValue(box3 - box4);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatSummaryList;

	}

	@Override
	public ArrayList<UncategorisedAmountsReport> getUncategorisedAmountsReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {
		ArrayList<UncategorisedAmountsReport> uncategories = new ArrayList<UncategorisedAmountsReport>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());

		try {
			uncategories = getFinanceTool().getReportManager()
					.getUncategorisedAmountsReport(financeDates[0],
							financeDates[1], getCompanyId());

			UncategorisedAmountsReport obj = new UncategorisedAmountsReport();
			if (uncategories != null)
				uncategories.add((UncategorisedAmountsReport) setStartEndDates(
						obj, financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return uncategories;
	}

	@Override
	public ArrayList<VATItemSummary> getVATItemSummaryReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {
		ArrayList<VATItemSummary> vatItems = new ArrayList<VATItemSummary>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());

		try {
			vatItems = getFinanceTool().getReportManager()
					.getVATItemSummaryReport(financeDates[0], financeDates[1],
							getCompanyId());

			VATItemSummary obj = new VATItemSummary();
			if (vatItems != null)
				vatItems.add((VATItemSummary) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatItems;
	}

	@Override
	public ArrayList<VATItemDetail> getVATItemDetailReport(String vatItemName,
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {

		ArrayList<VATItemDetail> itemsList = new ArrayList<VATItemDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());

		try {
			itemsList = getFinanceTool().getReportManager()
					.getVATItemDetailReport(vatItemName, financeDates[0],
							financeDates[1], getCompanyId());

			VATItemDetail obj = new VATItemDetail();
			if (itemsList != null)
				itemsList.add((VATItemDetail) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return itemsList;

	}

	@Override
	public ArrayList<ECSalesList> getECSalesListReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {

		ArrayList<ECSalesList> salesList = new ArrayList<ECSalesList>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());

		try {
			FinanceTool tool = getFinanceTool();
			salesList = tool.getReportManager().getECSalesListReport(
					financeDates[0], financeDates[1],
					tool.getCompany(getCompanyId()));

			ECSalesList obj = new ECSalesList();
			if (salesList != null)
				salesList
						.add((ECSalesList) setStartEndDates(obj, financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesList;

	}

	@Override
	public ArrayList<ECSalesListDetail> getECSalesListDetailReport(
			String payeeName, ClientFinanceDate fromDate,
			ClientFinanceDate toDate) {

		ArrayList<ECSalesListDetail> salesList = new ArrayList<ECSalesListDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());

		try {
			FinanceTool tool = getFinanceTool();
			salesList = tool.getReportManager().getECSalesListDetailReport(
					payeeName, financeDates[0], financeDates[1],
					tool.getCompany(getCompanyId()));

			ECSalesListDetail obj = new ECSalesListDetail();
			if (salesList != null)
				salesList.add((ECSalesListDetail) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesList;

	}

	@Override
	public ArrayList<ReverseChargeListDetail> getReverseChargeListDetailReport(
			String payeeName, ClientFinanceDate fromDate,
			ClientFinanceDate toDate) {

		ArrayList<ReverseChargeListDetail> salesList = new ArrayList<ReverseChargeListDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());

		try {
			salesList = getFinanceTool().getReportManager()
					.getReverseChargeListDetailReport(payeeName,
							financeDates[0], financeDates[1], getCompanyId());

			ReverseChargeListDetail obj = new ReverseChargeListDetail();
			if (salesList != null)
				salesList.add((ReverseChargeListDetail) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesList;

	}

	@Override
	public ArrayList<ReverseChargeList> getReverseChargeListReport(
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {

		ArrayList<ReverseChargeList> salesList = new ArrayList<ReverseChargeList>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());

		try {
			salesList = getFinanceTool().getReportManager()
					.getReverseChargeListReport(financeDates[0],
							financeDates[1], getCompanyId());

			ReverseChargeList obj = new ReverseChargeList();
			if (salesList != null)
				salesList.add((ReverseChargeList) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return salesList;

	}

	/**
	 * It returns an Array of dates. array[0] is start date and array[1] is end
	 * date
	 * 
	 * @param startDate
	 * @param endDate
	 */
	private FinanceDate[] getMinimumAndMaximumDates(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		// if ((startDate.equals("") || startDate == null)
		// || (endDate.equals("") || endDate == null)) {

		List<ClientFinanceDate> dates = getMinimumAndMaximumTransactionDate(companyId);
		ClientFinanceDate startDate1 = dates.get(0) == null ? new ClientFinanceDate()
				: dates.get(0);
		ClientFinanceDate endDate2 = dates.get(1) == null ? new ClientFinanceDate()
				: dates.get(1);

		FinanceDate transtartDate;
		if (startDate == null || startDate.isEmpty())
			transtartDate = new FinanceDate(startDate1);
		else
			transtartDate = new FinanceDate(startDate.getDate());
		FinanceDate tranendDate;
		if (endDate == null || endDate.isEmpty())
			tranendDate = new FinanceDate(endDate2);
		else
			tranendDate = new FinanceDate(endDate.getDate());

		return new FinanceDate[] { transtartDate, tranendDate };
	}

	@Override
	public ArrayList<DummyDebitor> getDebitors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) throws AccounterException {
		return debtorsReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<DummyDebitor> debtorsReport(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId)
			throws AccounterException {

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		ArrayList<DummyDebitor> debitors = new ArrayList<DummyDebitor>();
		Calendar calender = Calendar.getInstance();
		// calender.add(Calendar.MONTH, 1);
		// Date oneMonthPreviousDate = calender.getTime();
		// calender.add(Calendar.MONTH, 1);
		// Date twoMonthPreviousDate = calender.getTime();
		// calender.add(Calendar.MONTH, 1);
		// Date threeMonthPreviousDate = calender.getTime();
		try {
			List<AgedDebtors> agedDebtors = getFinanceTool()
					.getReportManager()
					.getAgedDebtors(financeDates[0], financeDates[1], companyId);

			debitors = getDebtorsWidSameName(agedDebtors, financeDates[0],
					financeDates[1]);

			DummyDebitor obj = new DummyDebitor();
			if (debitors != null)
				debitors.add((DummyDebitor) setStartEndDates(obj, financeDates));

		} catch (DAOException e) {

			e.printStackTrace();
		}

		return debitors;

	}

	/*
	 * This method calculates the totals for individual debtors and prepares
	 * single obj for those debtors who has same name
	 */
	private ArrayList<DummyDebitor> getDebtorsWidSameName(
			List<AgedDebtors> agedDebtors, FinanceDate startdate,
			FinanceDate enddate) {
		ArrayList<DummyDebitor> listDebtors = new ArrayList<DummyDebitor>();
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
			if (sameDebtors.size() > 0) {
				sameDebtorsMap.put(i, sameDebtors);
			}
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
	public ArrayList<DummyDebitor> getCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate) throws AccounterException {
		return creditorsReport(startDate, endDate, getCompanyId());
	}

	private ArrayList<DummyDebitor> creditorsReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) throws AccounterException {

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		ArrayList<DummyDebitor> Creditors = new ArrayList<DummyDebitor>();
		// Calendar calender = Calendar.getInstance();
		// calender.add(Calendar.MONTH, 1);
		// Date oneMonthPreviousDate = calender.getTime();
		// calender.add(Calendar.MONTH, 1);
		// Date twoMonthPreviousDate = calender.getTime();
		// calender.add(Calendar.MONTH, 1);
		// Date threeMonthPreviousDate = calender.getTime();
		try {
			List<AgedDebtors> agedCreditors = getFinanceTool()
					.getReportManager().getAgedCreditors(financeDates[0],
							financeDates[1], companyId);

			Creditors = getDebtorsWidSameName(agedCreditors, financeDates[0],
					financeDates[1]);

			DummyDebitor obj = new DummyDebitor();
			if (Creditors != null)
				Creditors
						.add((DummyDebitor) setStartEndDates(obj, financeDates));

		} catch (DAOException e) {

			e.printStackTrace();
		}

		return Creditors;
	}

	@Override
	public ArrayList<ExpenseList> getExpenseReportByType(int status,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		return expenseReportByType(status, startDate, endDate, getCompanyId());
	}

	private ArrayList<ExpenseList> expenseReportByType(int status,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {

		ArrayList<ExpenseList> expenseList = new ArrayList<ExpenseList>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, companyId);

		try {
			expenseList = getFinanceTool().getReportManager()
					.getExpenseReportByType(status, financeDates[0],
							financeDates[1], companyId);

			ExpenseList obj = new ExpenseList();

			if (expenseList != null)
				expenseList.add((ExpenseList) setStartEndDates(obj,
						financeDates));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return expenseList;
	}

	public ArrayList<ClientBudgetList> getBudgetItemsList(long id,
			ClientFinanceDate startDate, ClientFinanceDate endDate, int month) {
		return budgetItemsList(id, startDate, endDate, month, getCompanyId());
	}

	private ArrayList<ClientBudgetList> budgetItemsList(long id,
			ClientFinanceDate startDate, ClientFinanceDate endDate, int month,
			long companyId) {

		ArrayList<ClientBudgetList> budgetList = new ArrayList<ClientBudgetList>();

		// FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
		// endDate);

		try {
			ArrayList<ClientBudget> budgets = new ArrayList<ClientBudget>();
			budgets = (ArrayList<ClientBudget>) getFinanceTool().getBudgetList(
					companyId);

			// ClientBudgetList obj = new ClientBudgetList();

			for (ClientBudget budget : budgets) {
				ArrayList<ClientBudgetItem> budgetItems = new ArrayList<ClientBudgetItem>();

				if (budget.getID() == id) {
					budgetItems = (ArrayList<ClientBudgetItem>) budget
							.getBudgetItem();

					for (ClientBudgetItem item : budgetItems) {

						if (item.getTotalAmount() > 0.0) {
							ClientBudgetList e = new ClientBudgetList();
							e.setAccount(item.getAccount());
							e.setJanuaryAmount(item.getJanuaryAmount());
							e.setFebrauaryAmount(item.getFebruaryAmount());
							e.setMarchAmount(item.getMarchAmount());
							e.setAprilAmount(item.getAprilAmount());
							e.setMayAmount(item.getMayAmount());
							e.setJuneAmount(item.getJuneAmount());
							e.setJulyAmount(item.getJulyAmount());
							e.setAugustAmount(item.getAugustAmount());
							e.setSeptemberAmount(item.getSpetemberAmount());
							e.setOctoberAmount(item.getOctoberAmount());
							e.setNovemberAmount(item.getNovemberAmount());
							e.setDecemberAmount(item.getDecemberAmount());
							e.setTotalAmount(item.getTotalAmount());
							budgetList.add(e);
						}

					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return budgetList;

	}

	@Override
	public ArrayList<DepositDetail> getDepositDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		ArrayList<DepositDetail> transDetailByAccountList = new ArrayList<DepositDetail>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			transDetailByAccountList = getFinanceTool().getDepositDetail(
					financeDates[0], financeDates[1], getCompanyId());

			DepositDetail obj = new DepositDetail();
			if (transDetailByAccountList != null)
				transDetailByAccountList.add((DepositDetail) setStartEndDates(
						obj, financeDates));

			// transDetailByAccountList = (List<TransactionDetailByAccount>)
			// manager
			// .merge(transDetailByAccountList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return transDetailByAccountList;
	}

	@Override
	public ArrayList<CheckDetailReport> getCheckDetailReport(
			long paymentmethod, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {

		ArrayList<CheckDetailReport> checkDetailReports = new ArrayList<CheckDetailReport>();

		FinanceDate[] financeDates = getMinimumAndMaximumDates(startDate,
				endDate, getCompanyId());

		try {

			checkDetailReports = getFinanceTool().getReportManager()
					.getCheckDetailReport(paymentmethod, financeDates[0],
							financeDates[1], getCompanyId());

			CheckDetailReport obj = new CheckDetailReport();
			if (checkDetailReports != null)
				checkDetailReports.add((CheckDetailReport) setStartEndDates(
						obj, financeDates));

			// transDetailByAccountList = (List<TransactionDetailByAccount>)
			// manager
			// .merge(transDetailByAccountList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return checkDetailReports;
	}

	@Override
	public ArrayList<PayeeStatementsList> getStatements(long id,
			ClientFinanceDate fromDate, ClientFinanceDate toDate) {

		ArrayList<PayeeStatementsList> resultList = new ArrayList<PayeeStatementsList>();
		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());
		try {

			resultList = getFinanceTool().getReportManager()
					.getPayeeStatementsList(id, financeDates[0],
							financeDates[1], getCompanyId());

			PayeeStatementsList obj = new PayeeStatementsList();
			if (resultList != null)
				resultList.add((PayeeStatementsList) setStartEndDates(obj,
						financeDates));
			// for (PayeeStatementsList obj : list) {
			//
			// resultList.add((PayeeStatementsList)
			// setStartEndDates(obj,financeDates));
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultList;
	}

	@Override
	public ArrayList<PayeeStatementsList> getCustomerStatement(long customer,
			long fromDate, long toDate) throws AccounterException {
		FinanceTool tool = getFinanceTool();
		if (tool != null) {
			return tool.getCustomerManager().getCustomerStatement(customer,
					fromDate, toDate, getCompanyId());
		}
		return null;
	}

	@Override
	public ArrayList<MISC1099TransactionDetail> getMISC1099TransactionDetailReport(
			long vendorId, int boxNo, ClientFinanceDate fromDate,
			ClientFinanceDate toDate) {
		FinanceDate[] financeDates = getMinimumAndMaximumDates(fromDate,
				toDate, getCompanyId());
		ArrayList<MISC1099TransactionDetail> result = new ArrayList<MISC1099TransactionDetail>();
		try {
			result = getFinanceTool().getVendorManager()
					.getPaybillsByVendorAndBoxNumber(financeDates[0],
							financeDates[1], vendorId, boxNo, getCompanyId());
			MISC1099TransactionDetail obj = new MISC1099TransactionDetail();
			if (result != null)
				result.add((MISC1099TransactionDetail) setStartEndDates(obj,
						financeDates));
		} catch (AccounterException e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<DummyDebitor> getDebitors(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId)
			throws AccounterException {
		return debtorsReport(startDate, endDate, companyId);
	}

	public ArrayList<AgedDebtors> getAgedDebtors(String name,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return agedDebtorsReport(name, startDate, endDate, companyId);
	}

	public ArrayList<OpenAndClosedOrders> getSalesOpenOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return salesOpenOrderReport(startDate, endDate, companyId);
	}

	public ArrayList<OpenAndClosedOrders> getSalesCompletedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return salesCompletedOrderReport(startDate, endDate, companyId);
	}

	public ArrayList<OpenAndClosedOrders> getSalesCancelledOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return salesCancelledReport(startDate, endDate, companyId);
	}

	public ArrayList<OpenAndClosedOrders> getSalesOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return salesOrderReport(startDate, endDate, companyId);
	}

	public ArrayList<DummyDebitor> getCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId)
			throws AccounterException {
		return creditorsReport(startDate, endDate, companyId);
	}

	public ArrayList<AgedDebtors> getAgedCreditors(String name,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return agedCreditorsReport(name, startDate, endDate, companyId);
	}

	public ArrayList<OpenAndClosedOrders> getPurchaseOpenOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return purchaseOpenOrderReport(startDate, endDate, companyId);
	}

	public ArrayList<OpenAndClosedOrders> getPurchaseCompletedOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return purchaseCompletedOrderReport(startDate, endDate, companyId);
	}

	public ArrayList<OpenAndClosedOrders> getPurchaseCancelledOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return purchaseCancelledOrderReport(startDate, endDate, companyId);
	}

	public ArrayList<OpenAndClosedOrders> getPurchaseOrderReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return purchaseOrderReport(startDate, endDate, companyId);
	}

	public ArrayList<VATSummary> getPriorReturnVATSummary(long taxAgncy,
			ClientFinanceDate endDate, long companyId) {
		return priorReturnVATSummary(taxAgncy, endDate, companyId);
	}

	public ArrayList<VATSummary> getVAT100Report(long taxAgency,
			ClientFinanceDate fromDate, ClientFinanceDate toDate, long companyId) {
		return vat100Report(taxAgency, fromDate, toDate, companyId);
	}

	public ArrayList<ClientBudgetList> getBudgetItemsList(int id,
			ClientFinanceDate startDate, ClientFinanceDate endDate, int month,
			long companyId) {
		return budgetItemsList(id, startDate, endDate, month, companyId);
	}

	public ArrayList<SalesByLocationDetails> getSalesByLocationDetailsReport(
			boolean isLocation, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		return salesByLocationDetailsReport(isLocation, startDate, endDate,
				companyId);
	}

	public ArrayList<SalesByLocationSummary> getSalesByLocationSummaryReport(
			boolean isLocation, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		return salesByLocationSummary(isLocation, startDate, endDate, companyId);
	}

	public ArrayList<AgedDebtors> getAgedCreditors(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		return agedCreditorsReport(startDate, endDate, companyId);
	}

	public ArrayList<AgedDebtors> getAgedDebtors(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		return agedDebtorsReport(startDate, endDate, companyId);
	}

	public ArrayList<ExpenseList> getExpenseReportByType(int status,
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return expenseReportByType(status, startDate, endDate, companyId);
	}

	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return transactionDetailByTaxItem(startDate, endDate, companyId);
	}

	public ArrayList<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			String status, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		return transactionDetailByTaxItem(status, startDate, endDate, companyId);
	}

	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return transactionDetailByAccount(startDate, endDate, companyId);
	}

	public ArrayList<TransactionDetailByAccount> getTransactionDetailByAccount(
			String accountName, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		return transactionDetailByAccount(accountName, startDate, endDate,
				companyId);
	}

	public ArrayList<TransactionHistory> getCustomerTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return customerTransactionHistory(startDate, endDate, companyId);
	}

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return salesByCustomerSummary(startDate, endDate, companyId);
	}

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return salesByCustomerDetailReport(startDate, endDate, companyId);
	}

	public ArrayList<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String status, ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		return salesByCustomerDetailReport(status, startDate, endDate,
				companyId);
	}

	public ArrayList<SalesByCustomerDetail> getSalesByItemSummary(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return salesByItemSummary(startDate, endDate, companyId);
	}

	public ArrayList<TrialBalance> getProfitAndLossReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return profitAndLossReport(startDate, endDate, companyId);
	}

	public ArrayList<TrialBalance> getBalanceSheetReport(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return balanceSheetReport(startDate, endDate, companyId);
	}

	public ArrayList<TrialBalance> getTrialBalance(ClientFinanceDate startDate,
			ClientFinanceDate endDate, long companyId) {
		return trialBalanceReport(startDate, endDate, companyId);
	}

	public ArrayList<TransactionHistory> getVendorTransactionHistory(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return vendorTransactionHistory(startDate, endDate, companyId);
	}

	public ArrayList<SalesByCustomerDetail> getPurchasesByVendorDetail(
			ClientFinanceDate startDate, ClientFinanceDate endDate,
			long companyId) {
		return purchasesByVendorDetail(startDate, endDate, companyId);
	}

}