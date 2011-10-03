package com.vimukti.accounter.web.server.managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.vimukti.accounter.core.Activity;
import com.vimukti.accounter.core.ActivityType;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.Depreciation;
import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.core.FiscalYear;
import com.vimukti.accounter.core.FixedAsset;
import com.vimukti.accounter.core.FixedAssetHistory;
import com.vimukti.accounter.core.RecurringTransaction;
import com.vimukti.accounter.core.ServerConvertUtil;
import com.vimukti.accounter.core.Transaction;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.core.User;
import com.vimukti.accounter.core.Utility;
import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.main.ServerConfiguration;
import com.vimukti.accounter.services.IS2SService;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientUserInfo;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.OperationContext;

public class CompanyManager extends Manager {

	public void updateDeprecationStartDate(OperationContext context)
			throws AccounterException {
		String arg1 = context.getArg1();

		if (arg1 == null || arg1.isEmpty()) {
			throw new AccounterException(
					"Cann't Update Deprecation Strart Date with Null or Empty");
		}
		FinanceDate newStartDate = new FinanceDate(Long.parseLong(arg1));
		Company company = getCompany(context.getCompanyId());

		CompanyPreferences serverCompanyPreferences1 = company.getPreferences();

		changeDepreciationStartDateTo(newStartDate, company);

		company.setPreferences(serverCompanyPreferences1);
		serverCompanyPreferences1.setDepreciationStartDate(newStartDate);
		// CompanyPreferences serverObject = serverCompanyPreferences1;

		String userID = context.getUserEmail();
		User user1 = company.getUserByUserEmail(userID);

		Activity activity = new Activity(company, user1, ActivityType.EDIT,
				company);
		HibernateUtil.getCurrentSession().save(activity);

		HibernateUtil.getCurrentSession().saveOrUpdate(company);
		ChangeTracker.put(serverCompanyPreferences1);
	}

	private void changeDepreciationStartDateTo(FinanceDate newStartDate,
			Company company) throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		// Catching the current Start Long
		FinanceDate startDate = company.getPreferences()
				.getDepreciationStartDate();
		FinanceDate depreciationFirstDate = null;
		Query query = session.getNamedQuery("getDepreciation").setEntity(
				"company", company);
		List<Depreciation> list = query.list();
		if (list != null && list.size() > 0 && list.get(0) != null) {
			Depreciation dep = list.get(0);
			depreciationFirstDate = dep.getDepreciateFrom();
		}

		// Roll-back all the existing depreciation
		if (depreciationFirstDate != null) {
			rollBackDepreciation(depreciationFirstDate, company);
		}

		query = session.getNamedQuery("getFixedAsset.by.purchaseDate")
				.setParameter("purchaseDate", (newStartDate))
				.setEntity("company", company);
		List<FixedAsset> assetsList = query.list();
		for (FixedAsset fixedAsset : assetsList) {

			fixedAsset.setAccumulatedDepreciationAmount(0.0);
			fixedAsset.setSoldOrDisposed(false);
			// fixedAsset.setSellingOrDisposingFixedAsset(null);
			fixedAsset.setBookValue(fixedAsset.getPurchasePrice());
			fixedAsset.setSoldOrDisposedDate(null);
			fixedAsset.setAccountForSale(null);
			fixedAsset.setSalePrice(0.0);
			fixedAsset.setNoDepreciation(false);
			fixedAsset.setDepreciationTillDate(null);
			fixedAsset.setNotes(null);
			fixedAsset.setLossOrGainOnDisposalAccount(null);
			fixedAsset.setTotalCapitalGain(null);
			fixedAsset.setLossOrGain(0.0);

			/**
			 * Saving the action into History
			 */
			FixedAssetHistory fixedAssetHistory = new FixedAssetHistory();
			if (fixedAsset.getPurchaseDate().before(newStartDate)) {
				fixedAsset.setStatus(FixedAsset.STATUS_PENDING);

				fixedAssetHistory
						.setActionType(FixedAssetHistory.ACTION_TYPE_ROLLBACK);
				fixedAssetHistory.setActionDate(new FinanceDate());
				fixedAssetHistory
						.setDetails("Fixed Asset Start Long altered from "
								+ format.format(startDate.getAsDateObject())
								+ " to "
								+ format.format(newStartDate.getAsDateObject())
								+ ". All fixed assets journals reversed.");

			} else {

				fixedAssetHistory
						.setActionType(FixedAssetHistory.ACTION_TYPE_NONE);
				fixedAssetHistory.setActionDate(new FinanceDate());
				fixedAssetHistory
						.setDetails("Fixed Asset Start Long altered from "
								+ format.format(startDate.getAsDateObject())
								+ " to "
								+ format.format(newStartDate.getAsDateObject())
								+ ". All fixed assets book values are set");

			}
			fixedAsset.getFixedAssetsHistory().add(fixedAssetHistory);
			session.saveOrUpdate(fixedAsset);
			ChangeTracker.put(fixedAsset);

		}

	}

	// SURESH
	public void rollBackDepreciation(long rollBackDepreciationTo, long companyId)
			throws AccounterException {
		rollBackDepreciation(new FinanceDate(rollBackDepreciationTo),
				getCompany(companyId));
	}

	public void rollBackDepreciation(FinanceDate rollBackDepreciationTo,
			Company company) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("getDepreciation.by.ToandStatus")
				.setParameter("deprediationTo", (rollBackDepreciationTo))
				.setParameter("status", Depreciation.APPROVE)
				.setEntity("company", company);
		List<Depreciation> list = query.list();
		for (Depreciation dep : list) {
			// if (dep.getFixedAsset().getStatus() ==
			// FixedAsset.STATUS_REGISTERED) {
			dep.setStatus(Depreciation.ROLLBACK);
			// Rakesh
			dep.setRollBackDepreciationDate(new FinanceDate(
					rollBackDepreciationTo.getDate()));

			org.hibernate.Transaction t = session.beginTransaction();
			session.saveOrUpdate(dep);
			if (dep instanceof Lifecycle) {
				Lifecycle lifecycle = (Lifecycle) dep;
				lifecycle.onUpdate(session);
			}
			t.commit();
			// }
		}

		// for (Depreciation dep : list) {
		// for (FixedAsset fixedAsset : dep.getFixedAsset()) {
		// if (fixedAsset != null)
		// fixedAsset.getTransactions().clear();
		// }
		// org.hibernate.Transaction t = session.beginTransaction();
		// session.saveOrUpdate(dep);
		//
		// t.commit();
		// }

	}

	// RAKESH

	//
	// public void rollBackDepreciation(Long rollBackDepreciationTo)
	// throws DAOException {
	// Session session = HibernateUtil.getCurrentSession();
	// Query query = session
	// .createQuery(
	// "from com.vimukti.accounter.core.Transaction T wehre t.transactionDate=?")
	// .setParameter(0, rollBackDepreciationTo);
	//
	// List<Transaction> list = query.list();
	// for (Transaction tran : list) {
	// tran.setVoid(true);
	// org.hibernate.Transaction t = session.beginTransaction();
	// session.saveOrUpdate(tran);
	// if (tran instanceof Lifecycle) {
	// Lifecycle lifecycle = (Lifecycle) tran;
	// lifecycle.onUpdate(session);
	// }
	// t.commit();
	//
	// }
	// }

	public void updateCompanyPreferences(OperationContext context)
			throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();

		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			IAccounterCore data = context.getData();

			if (data == null) {
				throw new AccounterException(
						AccounterException.ERROR_ILLEGAL_ARGUMENT,
						"Update Company Preferences, as the Source Object could not be Found....");
			}

			Company company = getCompany(context.getCompanyId());
			// String IdentiName =
			// this.getSpace().getIDentity().getDisplayName();

			CompanyPreferences serverCompanyPreferences = company
					.getPreferences();

			serverCompanyPreferences = new ServerConvertUtil().toServerObject(
					serverCompanyPreferences, (ClientCompanyPreferences) data,
					session);

			company.setPreferences(serverCompanyPreferences);

			String userID = context.getUserEmail();
			User user1 = getCompany(context.getCompanyId()).getUserByUserEmail(
					userID);

			Activity activity = new Activity(company, user1, ActivityType.EDIT,
					serverCompanyPreferences);
			session.save(activity);
			session.update(company);
			transaction.commit();
			// CompanyPreferences serverObject = serverCompanyPreferences;
			ChangeTracker.put(serverCompanyPreferences);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			transaction.rollback();
			throw new AccounterException(AccounterException.ERROR_INTERNAL);
		}
	}

	public Long updateCompany(OperationContext context)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			IAccounterCore data = context.getData();
			if (data == null) {
				throw new AccounterException(
						AccounterException.ERROR_PERMISSION_DENIED,
						"Update Company , as the Source Object could not be Found....");
			}

			Company cmp = getCompany(context.getCompanyId());
			cmp.updatePreferences((ClientCompany) data);

			String userID = context.getUserEmail();
			User user1 = cmp.getUserByUserEmail(userID);

			Activity activity = new Activity(cmp, user1,
					ActivityType.UPDATE_PREFERENCE, cmp);
			session.save(activity);
			session.update(cmp);

			// Updating ServerCompany
			IS2SService s2sSyncProxy = getS2sSyncProxy(ServerConfiguration
					.getMainServerDomain());
			s2sSyncProxy.updateServerCompany(context.getCompanyId(), cmp
					.getPreferences().getFullName());

			transaction.commit();
			ChangeTracker.put(cmp.toClientCompany());
			return cmp.getID();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			transaction.rollback();
			throw new AccounterException(AccounterException.ERROR_INTERNAL);
		}

	}

	private IS2SService getS2sSyncProxy(String domainName) {
		String url = "http://" + domainName + ":"
				+ ServerConfiguration.getMainServerPort()
				+ "/company/stosservice";
		return (IS2SService) SyncProxy.newProxyInstance(IS2SService.class, url,
				"");
	}

	public void updateCompanyStartDate(OperationContext context)
			throws AccounterException {
		String arg1 = context.getArg1();
		if (arg1 == null || arg1.isEmpty()) {
			throw new AccounterException(
					AccounterException.ERROR_PERMISSION_DENIED,
					"Cann't Update the Compamy StartData with Null or Empty");
		}
		FinanceDate modifiedStartDate = new FinanceDate(Long.parseLong(arg1));

		changeFiscalYearsStartDate(modifiedStartDate, context.getCompanyId());

		Company company = getCompany(context.getCompanyId());

		CompanyPreferences serverCompanyPreferences = company.getPreferences();

		company.setPreferences(serverCompanyPreferences);

		serverCompanyPreferences.setStartDate(modifiedStartDate);
		serverCompanyPreferences.setPreventPostingBeforeDate(modifiedStartDate);
		serverCompanyPreferences.setStartOfFiscalYear(modifiedStartDate);
		// CompanyPreferences serverObject = serverCompanyPreferences;
		String userID = context.getUserEmail();
		User user1 = getCompany(context.getCompanyId()).getUserByUserEmail(
				userID);

		Activity activity = new Activity(company, user1, ActivityType.EDIT);
		HibernateUtil.getCurrentSession().save(activity);
		HibernateUtil.getCurrentSession().update(company);
		ChangeTracker.put(serverCompanyPreferences);
	}

	private void changeFiscalYearsStartDate(FinanceDate modifiedStartDate,
			long companyId) {

		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		// List<FiscalYear> list = new ArrayList<FiscalYear>();

		// FinanceDate existingLeastStartDate = modifiedStartDate;
		// FinanceDate existingHighestEndDate = modifiedStartDate;
		// list = session
		// .createQuery(
		// "from com.vimukti.accounter.core.FiscalYear f order by f.startDate")
		// .list();

		Query query = session.getNamedQuery("getMinStartDateAndMaxEndDate")
				.setParameter("companyId", companyId);
		List list = query.list();
		Object[] object = (Object[]) list.get(0);

		FinanceDate existingLeastStartDate = new FinanceDate((Long) object[0]);
		FinanceDate existingHighestEndDate = new FinanceDate((Long) object[1]);

		// if (list.size() > 0) {
		// Iterator<FiscalYear> i = list.iterator();
		// FiscalYear firstFiscalYear = list.get(0) != null ? list.get(0)
		// : null;
		// FiscalYear lastFiscalYear = null;
		// while (i.hasNext()) {
		// lastFiscalYear = (FiscalYear) i.next();
		// }
		// existingLeastStartDate = firstFiscalYear.getPreviousStartDate();
		//
		// existingHighestEndDate = lastFiscalYear.getEndDate();

		// if ((modifiedStartDate.before(existingLeastStartDate) ||
		// modifiedStartDate
		// .after(existingHighestEndDate))) {

		Calendar leastStartDateCal = Calendar.getInstance();
		leastStartDateCal.setTime(existingLeastStartDate.getAsDateObject());

		Calendar highestEndDateCal = Calendar.getInstance();
		highestEndDateCal.setTime(existingHighestEndDate.getAsDateObject());

		Calendar modifiedDateCal = Calendar.getInstance();
		modifiedDateCal.setTime(modifiedStartDate.getAsDateObject());

		if (modifiedStartDate.getDate() >= existingLeastStartDate.getDate()
				&& modifiedStartDate.getDate() <= existingHighestEndDate
						.getDate()) {
			return;
		} else if (modifiedStartDate.getDate() < existingLeastStartDate
				.getDate()) {

			// if (modifiedStartDate.before(existingLeastStartDate)) {

			Calendar tempCal = Calendar.getInstance();
			tempCal.setTime(leastStartDateCal.getTime());

			FinanceDate tempDate = existingLeastStartDate;

			// while (tempCal.compareTo(modifiedDateCal) > 0) {
			while (tempDate.getDate() > modifiedStartDate.getDate()) {

				Calendar cal = Calendar.getInstance();

				cal.setTime(tempCal.getTime());
				cal.set(Calendar.MONTH, tempCal.get(Calendar.MONTH) - 12);

				FinanceDate startDate = new FinanceDate(cal.getTime());

				cal.clear();
				cal.setTime(tempCal.getTime());
				cal.set(Calendar.DAY_OF_MONTH,
						tempCal.get(Calendar.DAY_OF_MONTH) - 1);

				FinanceDate endDate = new FinanceDate(cal.getTime());

				FiscalYear fs = new FiscalYear(startDate, endDate,
						FiscalYear.STATUS_OPEN, Boolean.FALSE);
				fs.checkIsCurrentFY(fs);
				session.save(fs);
				ChangeTracker.put(fs);

				tempDate = startDate;

				tempCal.setTime(startDate.getAsDateObject());

			}
			// } else if (modifiedStartDate.after(existingHighestEndDate)) {

		} else if (modifiedStartDate.getDate() > existingHighestEndDate
				.getDate()) {

			Calendar tempCal = Calendar.getInstance();
			tempCal.setTime(highestEndDateCal.getTime());

			FinanceDate tempDate = existingHighestEndDate;

			// while (tempCal.compareTo(modifiedDateCal) < 0) {

			while (tempDate.getDate() < modifiedStartDate.getDate()) {

				Calendar cal = Calendar.getInstance();

				cal.setTime(tempCal.getTime());
				cal.set(Calendar.DAY_OF_MONTH,
						tempCal.get(Calendar.DAY_OF_MONTH) + 1);

				FinanceDate startDate = new FinanceDate(cal.getTime());

				cal.clear();
				cal.setTime(tempCal.getTime());
				cal.set(Calendar.MONTH, tempCal.get(Calendar.MONTH) + 12);

				FinanceDate endDate = new FinanceDate(cal.getTime());
				FiscalYear fs = new FiscalYear(startDate, endDate,
						FiscalYear.STATUS_OPEN, Boolean.FALSE);
				fs.checkIsCurrentFY(fs);
				session.save(fs);
				ChangeTracker.put(fs);

				tempDate = endDate;

				tempCal.setTime(endDate.getAsDateObject());
			}
		}

		// }
		Utility.updateCurrentFiscalYear();
		transaction.commit();

	}

	/**
	 * @param companyName
	 * @param clientDateAtServer
	 *            see
	 *            {@link FinanceDate#clientTimeAtServer(java.util.Date, long)}
	 */
	public void performRecurringAction(String companyName,
			FinanceDate clientDateAtServer, long companyId) {
		Session session = HibernateUtil.openSession();
		try {
			// TODO need to write query
			Query namedQuery = session
					.getNamedQuery("list.currentRecTransactions");
			namedQuery.setLong("name", clientDateAtServer.getDate());
			namedQuery.setEntity("company", getCompany(companyId));
			List<RecurringTransaction> list = session
					.getNamedQuery("list.currentRecTransactions")
					.setLong("name", clientDateAtServer.getDate())
					.setEntity("company", getCompany(companyId)).list();

			for (RecurringTransaction recurringTransaction : list) {

				Transaction duplicateTransaction = FinanceTool
						.createDuplicateTransaction(recurringTransaction);

				session.save(duplicateTransaction);
				// TODO notify user and save this duplicate transaction
				recurringTransaction.scheduleAgain();
				session.saveOrUpdate(recurringTransaction);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public ClientCompanyPreferences getClientCompanyPreferences(Company company)
			throws AccounterException {
		ClientCompanyPreferences clientCompanyPreferences = new ClientConvertUtil()
				.toClientObject(company.getPreferences(),
						ClientCompanyPreferences.class);
		return clientCompanyPreferences;
	}

	public ClientCompany getClientCompany(String logInUserEmail, long companyId)
			throws AccounterException {

		Session session = HibernateUtil.getCurrentSession();

		Company company = getCompany(companyId);
		User logInUser = company.getUserByUserEmail(logInUserEmail);
		if (logInUser == null) {
			throw new AccounterException(
					AccounterException.ERROR_PERMISSION_DENIED);
		}
		if (!logInUser.isActive()) {
			activateUser(logInUser);
		}

		Hibernate.initialize(company);

		// company.setAccounts(getAccountsListBySorted());
		//
		// company.setFiscalYears(new
		// ArrayList<FiscalYear>(session.getNamedQuery(
		// "list.FiscalYear").list()));
		//
		// company.setPayees(new ArrayList<Payee>(session.getNamedQuery(
		// "list.Payee").list()));
		//
		// company.setItems(new
		// ArrayList<Item>(session.getNamedQuery("list.Item")
		// .list()));
		//
		// company.setCustomerGroups(new ArrayList<CustomerGroup>(session
		// .getNamedQuery("list.CustomerGroup").list()));
		//
		// company.setVendorGroups(new ArrayList<VendorGroup>(session
		// .getNamedQuery("list.VendorGroup").list()));
		//
		// company.setShippingTerms(new ArrayList<ShippingTerms>(session
		// .getNamedQuery("list.ShippingTerms").list()));
		//
		// company.setShippingMethods(new ArrayList<ShippingMethod>(session
		// .getNamedQuery("list.ShippingMethod").list()));
		//
		// company.setPriceLevels(new
		// ArrayList<PriceLevel>(session.getNamedQuery(
		// "list.PriceLevel").list()));
		//
		// company.setItemGroups(new ArrayList<ItemGroup>(session.getNamedQuery(
		// "list.ItemGroup").list()));
		//
		// company.setTaxGroups(new ArrayList<TAXGroup>(session.getNamedQuery(
		// "list.TAXGroup").list()));
		//
		// company.setPaymentTerms(new ArrayList<PaymentTerms>(session
		// .getNamedQuery("list.PaymentTerms").list()));
		//
		// company.setCreditRatings(new ArrayList<CreditRating>(session
		// .getNamedQuery("list.CreditRating").list()));
		//
		// company.setSalesPersons(new ArrayList<SalesPerson>(session
		// .getNamedQuery("list.SalesPerson").list()));
		//
		// company.setTaxCodes(new ArrayList<TAXCode>(session.getNamedQuery(
		// "list.TAXCode").list()));
		//
		// company.setTaxItems(new ArrayList<TAXItem>(session.getNamedQuery(
		// "list.TAXItem").list()));
		//
		// company.setTaxItemGroups(new ArrayList<TAXItemGroup>(session
		// .getNamedQuery("list.TAXItemGroups").list()));
		//
		// company.setBanks(new
		// ArrayList<Bank>(session.getNamedQuery("list.Bank")
		// .list()));
		//
		// company.setTaxrates(new ArrayList<TaxRates>(session.getNamedQuery(
		// "list.TaxRates").list()));
		//
		// company.setFixedAssets(new
		// ArrayList<FixedAsset>(session.getNamedQuery(
		// "list.FixedAsset").list()));
		// // company
		// // .setSellingDisposingFixedAssets(new
		// // HashSet<SellingOrDisposingFixedAsset>(
		// // session.getNamedQuery(
		// // "list.SellingOrDisposingFixedAsset").list()));
		//
		// company.setVatReturns(new ArrayList<VATReturn>(session.getNamedQuery(
		// "list.VATReturn").list()));
		// company.setCurrencies(new HashSet<Currency>(session.getNamedQuery(
		// "list.currency").list()));
		//
		// company.setTaxAdjustments(new ArrayList<TAXAdjustment>(session
		// .getNamedQuery("list.TAXAdjustment").list()));
		//
		// // company.setVatCodes(new ArrayList<TAXCode>(session.getNamedQuery(
		// // "list.VATCode").list()));
		//
		// // company.setVatItemGroups(new ArrayList<TAXItemGroup>(session
		// // .getNamedQuery("list.VATItemGroup").list()));
		//
		// company.setTaxAgencies(new
		// ArrayList<TAXAgency>(session.getNamedQuery(
		// "list.TAXAgency").list()));
		//
		// company.setVatBoxes(new
		// HashSet<Box>(session.getNamedQuery("list.Box")
		// .list()));
		// company.setVatReturnBoxes(new HashSet<VATReturnBox>(session
		// .getNamedQuery("list.VATReturnBox").list()));

		// company.setAccounterClasses(new HashSet<AccounterClass>(session
		// .getNamedQuery("list.TrackClass").list()));
		company = company.toCompany(company);
		ClientConvertUtil clientConvertUtil = new ClientConvertUtil();
		ClientCompany clientCompany = clientConvertUtil.toClientObject(company,
				ClientCompany.class);

		ClientFinanceDate[] dates = getMinimumAndMaximumTransactionDate(companyId);

		clientCompany.setTransactionStartDate(dates[0]);
		clientCompany.setTransactionEndDate(dates[1]);

		clientCompany.setUsersList(getAllEmployees(company));

		// User logInUSer = (User) session.getNamedQuery("getuser.by.email")
		// .setParameter("email", logInUserEmail).uniqueResult();
		clientCompany.setLoggedInUser(logInUser.getClientUser());

		List list = session.getNamedQuery("get.All.Units")
				.setEntity("company", company).list();

		ArrayList<ClientUnit> units = new ArrayList<ClientUnit>();
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Unit unit = (Unit) iterator.next();
			units.add(clientConvertUtil.toClientObject(unit, ClientUnit.class));
		}

		clientCompany.setUnits(units);
		clientCompany.setConfigured(company.isConfigured());

		return clientCompany;
	}

	private void activateUser(User user) throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		org.hibernate.Transaction transaction = session.beginTransaction();
		try {
			user.setActive(true);
			session.saveOrUpdate(user);
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			throw new AccounterException(AccounterException.ERROR_INTERNAL);
		}

	}

	/**
	 * @param company
	 * @return
	 * @throws AccounterException
	 */
	public ArrayList<ClientUserInfo> getAllEmployees(Company company)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Set<User> financeUsers = company.getUsers();
		ArrayList<ClientUserInfo> employees = new ArrayList<ClientUserInfo>();
		for (User user : financeUsers) {
			if (!user.isDeleted()) {
				ClientUser clientUser = new ClientConvertUtil().toClientObject(
						user, ClientUser.class);
				ClientUserInfo userInfo = clientUser.toUserInfo();
				employees.add(userInfo);
			}
		}
		return employees;
	}
}
