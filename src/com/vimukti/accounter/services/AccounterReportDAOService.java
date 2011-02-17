package com.vimukti.accounter.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.support.TransactionTemplate;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.Customer;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.core.TAXAgency;
import com.vimukti.accounter.core.VATReturn;
import com.vimukti.accounter.core.Vendor;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.reports.AccountBalance;
import com.vimukti.accounter.web.client.core.reports.AccountRegister;
import com.vimukti.accounter.web.client.core.reports.AgedDebtors;
import com.vimukti.accounter.web.client.core.reports.AmountsDueToVendor;
import com.vimukti.accounter.web.client.core.reports.DepositDetail;
import com.vimukti.accounter.web.client.core.reports.MostProfitableCustomers;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.core.reports.SalesTaxLiability;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByTaxItem;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.core.reports.VATDetailReport;
import com.vimukti.accounter.web.client.core.reports.VATItemDetail;
import com.vimukti.accounter.web.client.core.reports.VATSummary;

public class AccounterReportDAOService extends HibernateDaoSupport implements
		IAccounterReportDAOService {

	TransactionTemplate transactionTemplate;
	IAccounterDAOService accounterDao;

	public void setTransactionTemplate(TransactionTemplate template) {
		this.transactionTemplate = template;
	}

	public void setAccounterDao(IAccounterDAOService accounterDao) {
		this.accounterDao = accounterDao;
	}

	@SuppressWarnings("unchecked")
	private long getLongIdForGivenStringId(AccounterCoreType entity,
			String account) {

		Session session = Utility.getCurrentSession();
		String hqlQuery = "select entity.id from "
				+ entity.getServerClassSimpleName()
				+ " entity where entity.stringID=?";
		Query query = session.createQuery(hqlQuery).setString(0, account);
		List l = query.list();
		if (l != null && l.get(0) != null) {
			return (Long) l.get(0);
		} else
			return 0;

	}

	@Override
	public void createTaxes(int... vatReturnType) throws DAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<AccountBalance> getAccountBalances() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AccountRegister> getAccountRegister(String startDate,
			String endDate, String accountId) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AgedDebtors> getAgedCreditors(String startDate, String endDate)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AgedDebtors> getAgedDebtors(String startDate, String endDate)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AmountsDueToVendor> getAmountsDueToVendor(String startDate,
			String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TrialBalance> getBalanceSheetReport(String startDate,
			String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TrialBalance> getCashFlowReport(String startDate, String endDate)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionHistory> getCustomerTransactionHistory(
			String startDate, String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date[] getMinimumAndMaximumTransactionDate() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MostProfitableCustomers> getMostProfitableCustomers(
			String startDate, String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long getNextNominalCode(int accountType) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VATSummary getPriorReturnVATSummary(TAXAgency vatAgency, Date endDate)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VATDetailReport getPriorVATReturnVATDetailReport(
			TAXAgency vatAgency, Date endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TrialBalance> getProfitAndLossReport(String startDate,
			String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MostProfitableCustomers> getProfitabilityByCustomerDetail(
			String customer, String startDate, String endDate)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Item> getPurchaseReportItems(String startDate, String endDate)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalesByCustomerDetail> getPurchasesByItemDetail(
			String startDate, String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalesByCustomerDetail> getPurchasesByItemDetail(
			String itemName, String startDate, String endDate)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalesByCustomerDetail> getPurchasesByItemSummary(
			String startDate, String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String startDate, String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalesByCustomerDetail> getPurchasesByVendorDetail(
			String vendorName, String startDate, String endDate)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalesByCustomerDetail> getPurchasesByVendorSummary(
			String startDate, String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Transaction> getRegister(Account account) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String startDate, String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalesByCustomerDetail> getSalesByCustomerDetailReport(
			String customerName, String startDate, String endDate)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalesByCustomerDetail> getSalesByCustomerSummary(
			String startDate, String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalesByCustomerDetail> getSalesByItemDetail(String startDate,
			String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalesByCustomerDetail> getSalesByItemDetail(String itemName,
			String startDate, String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalesByCustomerDetail> getSalesByItemSummary(String startDate,
			String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Item> getSalesReportItems(String startDate, String endDate)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SalesTaxLiability> getSalesTaxLiabilityReport(String startDate,
			String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionDetailByAccount> getTransactionDetailByAccount(
			String startDate, String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionDetailByAccount> getTransactionDetailByAccount(
			String accountName, String startDate, String endDate)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			String startDate, String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionDetailByTaxItem> getTransactionDetailByTaxItem(
			String taxItemName, String startDate, String endDate)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Customer> getTransactionHistoryCustomers(String startDate,
			String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Vendor> getTransactionHistoryVendors(String startDate,
			String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TrialBalance> getTrialBalance(String startDate, String endDate)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VATItemDetail> getVATItemDetailReport(String fromDate,
			String toDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Double> getVATReturnBoxes(String startDate,
			String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VATReturn getVATReturnDetails(TAXAgency vatAgency, Date fromDate,
			Date toDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransactionHistory> getVendorTransactionHistory(
			String startDate, String endDate) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DepositDetail> getDepositDetail(String startDate, String endDate)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	
}
