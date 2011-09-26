package com.vimukti.accounter.core;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.CallbackException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.services.DAOException;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.core.AccounterCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.FixedAssetSellOrDisposeReviewJournal;
import com.vimukti.accounter.web.client.core.Lists.TempFixedAsset;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

/**
 * @author Suresh Garikapati
 * 
 * 
 *         A long-term, tangible asset held for business use and not expected to
 *         be converted to cash in the current or upcoming fiscal year, such as
 *         manufacturing equipment, real estate, and furniture.
 * 
 *         We can run Depreciation on registered Fixed Asset and as well as we
 *         can Sell or Dispose any Fixed Asset.
 */
public class FixedAsset extends CreatableObject implements
		IAccounterServerCore, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int STATUS_PENDING = 1;
	public static final int STATUS_REGISTERED = 2;
	public static final int STATUS_SOLD_OR_DISPOSED = 3;

	/**
	 * Unique Item ID, for which the
	 */
	private String name;

	private String assetNumber;

	/**
	 * Asset Account to which the Sold amount of this FixedAsset will go.
	 * 
	 * @see Account
	 */
	@ReffereredObject
	private Account assetAccount;

	/**
	 * Date of Purchase
	 */
	private FinanceDate purchaseDate;

	/**
	 * Purchase Price
	 */
	private double purchasePrice;

	/**
	 * Fixed Asset Description
	 */
	private String description;

	/**
	 * Asset Type
	 */
	private String assetType;

	/**
	 * Depreciation will be calculated per month depending on this depreciation
	 * rate. But here depreciation rate indicate the rate per Year.
	 */
	private double depreciationRate;

	/**
	 * Depreciation can be calculated with different type of methods. We have 2
	 * types of Depreciatio Methods. 1) Strait Line -> Depreciation will be
	 * calculated on Purchase Price. 2) Declining Balances. -> Depreciation will
	 * be calculated on Book value. This Book value is equal to Purchase Price
	 * for 1 Financial Year, but later it will decrease by the depreciation
	 * amount
	 */
	private int depreciationMethod;

	/**
	 * Depreciation Expense Account to which the depreciation amount will go.
	 * 
	 * @see Account
	 */
	@ReffereredObject
	private Account depreciationExpenseAccount;

	/**
	 * Accumulated Depreciation Account is the depreciated amount for the Fixed
	 * Assets whose purchase date is prior to the Depreciation Start Date.
	 * 
	 * @see Account
	 */
	// private Account accumulatedDepreciationAccount;
	private double accumulatedDepreciationAmount = 0.0;

	/**
	 * Fixed Asset status may be any of Pending , Registered and SellOrDispose
	 */
	private int status;

	/**
	 * This book value initially consists of the value as Purchase price minus
	 * Accumulated Depreciated Amount. Later onwards this value will be updated
	 * according to the Depreciation Method.
	 */
	private double bookValue;

	/**
	 * To hold the Initial Book value for this Fixed Asset.
	 */
	private double openingBalanceForFiscalYear;

	/**
	 * To know whether this FixedAsset is SoldOrDisposed or not
	 */
	private boolean isSoldOrDisposed;

	/**
	 * Each Fixed Asset Account must be linked with any another FixedAsset
	 * Account
	 */
	@ReffereredObject
	private Account linkedAccumulatedDepreciationAccount;
	@ReffereredObject
	transient private Account oldLinkedAccumulatedDepreciationAccount;

	transient private boolean isOnSaveProccessed;

	boolean isDefault;

	/**
	 * Selling OR Disposing Fixed Asset
	 * 
	 * @see SellingOrDisposingFixedAsset
	 */

	// private SellingOrDisposingFixedAsset sellingOrDisposingFixedAsset;
	private DecimalFormat decimalFormat = new DecimalFormat("##.##");

	@ReffereredObject
	private List<Transaction> transactions = new ArrayList<Transaction>();
	private List<FixedAssetNote> fixedAssetNotes = new ArrayList<FixedAssetNote>();
	private List<FixedAssetHistory> fixedAssetsHistory = new ArrayList<FixedAssetHistory>();

	transient private List<FixedAssetNote> oldFixedAssetNotes;
	/**
	 * Date on which this FixedAsset is Sold or Disposed.
	 */
	private FinanceDate soldOrDisposedDate;

	/**
	 * Account to which the Selling amount will go.
	 */
	@ReffereredObject
	private Account accountForSale;

	/**
	 * The amount for which this Fixed Asset is Sold.
	 */
	private double salePrice = 0.0;

	/**
	 * To specify whether the user required or not the Depreciation for this
	 * Fixed Asset in the Financial Year in which he sold.
	 */
	private boolean noDepreciation;

	/**
	 * If the user wants the Depreciation for this Fixed Asset while sold then
	 * the following is the Date till the Depreciation should be run.
	 */
	private FinanceDate depreciationTillDate;

	/**
	 * To save the Note for this Fixed Asset.
	 */
	private String notes;

	/**
	 * This is the account to hold the calculated loss or gain on Sold or
	 * Disposal.
	 */
	@ReffereredObject
	private Account lossOrGainOnDisposalAccount;

	/**
	 * This is the account that hold the excess amount of Sold on the purchase
	 * price.
	 */
	@ReffereredObject
	private Account totalCapitalGain;

	/**
	 * This is the amount that represent the calculated loss or gain on Sold or
	 * Disposal.
	 */
	double lossOrGain;

	/**
	 * This is the excess amount of Sold on the purchase price.
	 */
	double totalCapitalGainAmount;

	transient int oldStatus;

	/**
	 * @return the transactions
	 */
	public List<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * @param transactions
	 *            the transactions to set
	 */
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Account getLinkedAccumulatedDepreciationAccount() {
		return linkedAccumulatedDepreciationAccount;
	}

	public void setLinkedAccumulatedDepreciationAccount(
			Account linkedAccumulatedDepreciationAccount) {
		this.linkedAccumulatedDepreciationAccount = linkedAccumulatedDepreciationAccount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the assetNumber
	 */
	public String getAssetNumber() {
		return assetNumber;
	}

	/**
	 * @return the assetAccount
	 */
	public Account getAssetAccount() {
		return assetAccount;
	}

	/**
	 * @return the purchaseDate
	 */
	public FinanceDate getPurchaseDate() {
		return purchaseDate;
	}

	/**
	 * @return the purchasePrice
	 */
	public double getPurchasePrice() {
		return purchasePrice;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the assetType
	 */
	public String getAssetType() {
		return assetType;
	}

	/**
	 * @return the depreciationRate
	 */
	public double getDepreciationRate() {
		return depreciationRate;
	}

	/**
	 * @return the depreciationMethod
	 */
	public int getDepreciationMethod() {
		return depreciationMethod;
	}

	/**
	 * @return the depreciationExpenseAccount
	 */
	public Account getDepreciationExpenseAccount() {
		return depreciationExpenseAccount;
	}

	// /**
	// * @return the accumulatedDepreciationAccount
	// */
	// public Account getAccumulatedDepreciationAccount() {
	// return accumulatedDepreciationAccount;
	// }

	/**
	 * @return the accumulatedDepreciationAmount
	 */
	public double getAccumulatedDepreciationAmount() {
		return accumulatedDepreciationAmount;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @return the bookValue
	 */
	public double getBookValue() {
		return bookValue;
	}

	public double getOpeningBalanceForFiscalYear() {
		return openingBalanceForFiscalYear;
	}

	public void setOpeningBalanceForFiscalYear(
			double openingBalanceForFiscalYear) {
		this.openingBalanceForFiscalYear = openingBalanceForFiscalYear;
	}

	/**
	 * @return the isSoldOrDisposed
	 */
	public boolean isSoldOrDisposed() {
		return isSoldOrDisposed;
	}

	// /**
	// * @return the sellingOrDisposingFixedAsset
	// */
	// public SellingOrDisposingFixedAsset getSellingOrDisposingFixedAsset() {
	// return sellingOrDisposingFixedAsset;
	// }

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id) {
		this.id = id;
	}

	/**
	 * @param assetNumber
	 *            the assetNumber to set
	 */
	public void setAssetNumber(String assetNumber) {
		this.assetNumber = assetNumber;
	}

	/**
	 * @param assetAccount
	 *            the assetAccount to set
	 */
	public void setAssetAccount(Account assetAccount) {
		this.assetAccount = assetAccount;
	}

	/**
	 * @param purchaseDate
	 *            the purchaseDate to set
	 */
	public void setPurchaseDate(FinanceDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	/**
	 * @param purchasePrice
	 *            the purchasePrice to set
	 */
	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param assetType
	 *            the assetType to set
	 */
	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	/**
	 * @param depreciationRate
	 *            the depreciationRate to set
	 */
	public void setDepreciationRate(double depreciationRate) {
		this.depreciationRate = depreciationRate;
	}

	/**
	 * @param depreciationMethod
	 *            the depreciationMethod to set
	 */
	public void setDepreciationMethod(int depreciationMethod) {
		this.depreciationMethod = depreciationMethod;
	}

	/**
	 * @param depreciationExpenseAccount
	 *            the depreciationExpenseAccount to set
	 */
	public void setDepreciationExpenseAccount(Account depreciationExpenseAccount) {
		this.depreciationExpenseAccount = depreciationExpenseAccount;
	}

	// /**
	// * @param accumulatedDepreciationAccount
	// * the accumulatedDepreciationAccount to set
	// */
	// public void setAccumulatedDepreciationAccount(
	// Account accumulatedDepreciationAccount) {
	// this.accumulatedDepreciationAccount = accumulatedDepreciationAccount;
	// }

	/**
	 * @param accumulatedDepreciationAmount
	 *            the accumulatedDepreciationAmount to set
	 */
	public void setAccumulatedDepreciationAmount(
			double accumulatedDepreciationAmount) {
		this.accumulatedDepreciationAmount = accumulatedDepreciationAmount;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @param bookValue
	 *            the bookValue to set
	 */
	public void setBookValue(double bookValue) {
		this.bookValue = bookValue;
	}

	/**
	 * @param isSoldOrDisposed
	 *            the isSoldOrDisposed to set
	 */
	public void setSoldOrDisposed(boolean isSoldOrDisposed) {
		this.isSoldOrDisposed = isSoldOrDisposed;
	}

	// /**
	// * @param sellingOrDisposingFixedAsset
	// * the sellingOrDisposingFixedAsset to set
	// */
	// public void setSellingOrDisposingFixedAsset(
	// SellingOrDisposingFixedAsset sellingOrDisposingFixedAsset) {
	// this.sellingOrDisposingFixedAsset = sellingOrDisposingFixedAsset;
	// }
	//

	/**
	 * @return the soldOrDisposedDate
	 */
	public FinanceDate getSoldOrDisposedDate() {
		return soldOrDisposedDate;
	}

	/**
	 * @return the accountForSale
	 */
	public Account getAccountForSale() {
		return accountForSale;
	}

	/**
	 * @return the salePrice
	 */
	public double getSalePrice() {
		return salePrice;
	}

	/**
	 * @return the noDepreciation
	 */
	public boolean isNoDepreciation() {
		return noDepreciation;
	}

	/**
	 * @return the depreciationTillDate
	 */
	public FinanceDate getDepreciationTillDate() {
		return depreciationTillDate;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @return the lossOrGainOnDisposalAccount
	 */
	public Account getLossOrGainOnDisposalAccount() {
		return lossOrGainOnDisposalAccount;
	}

	/**
	 * @param soldOrDisposedDate
	 *            the soldOrDisposedDate to set
	 */
	public void setSoldOrDisposedDate(FinanceDate soldOrDisposedDate) {
		this.soldOrDisposedDate = soldOrDisposedDate;
	}

	/**
	 * @param accountForSale
	 *            the accountForSale to set
	 */
	public void setAccountForSale(Account accountForSale) {
		this.accountForSale = accountForSale;
	}

	/**
	 * @param salePrice
	 *            the salePrice to set
	 */
	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	/**
	 * @param noDepreciation
	 *            the noDepreciation to set
	 */
	public void setNoDepreciation(boolean noDepreciation) {
		this.noDepreciation = noDepreciation;
	}

	/**
	 * @param depreciationTillDate
	 *            the depreciationTillDate to set
	 */
	public void setDepreciationTillDate(FinanceDate depreciationTillDate) {
		this.depreciationTillDate = depreciationTillDate;
	}

	/**
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @param lossOrGainOnDisposalAccount
	 *            the lossOrGainOnDisposalAccount to set
	 */
	public void setLossOrGainOnDisposalAccount(
			Account lossOrGainOnDisposalAccount) {
		this.lossOrGainOnDisposalAccount = lossOrGainOnDisposalAccount;
	}

	/**
	 * @return the totalCapitalGain
	 */
	public Account getTotalCapitalGain() {
		return totalCapitalGain;
	}

	/**
	 * @param totalCapitalGain
	 *            the totalCapitalGain to set
	 */
	public void setTotalCapitalGain(Account totalCapitalGain) {
		this.totalCapitalGain = totalCapitalGain;
	}

	/**
	 * @return the lossOrGain
	 */
	public double getLossOrGain() {
		return lossOrGain;
	}

	/**
	 * @param lossOrGain
	 *            the lossOrGain to set
	 */
	public void setLossOrGain(double lossOrGain) {
		this.lossOrGain = lossOrGain;
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {

		AccounterCommand accounterCore = new AccounterCommand();
		accounterCore.setCommand(AccounterCommand.DELETION_SUCCESS);
		accounterCore.setID(id);
		accounterCore.setObjectType(AccounterCoreType.FIXEDASSET);

		ChangeTracker.put(accounterCore);
		return false;
	}

	@Override
	public void onLoad(Session session, Serializable id) {
		this.oldLinkedAccumulatedDepreciationAccount = linkedAccumulatedDepreciationAccount;
		this.oldFixedAssetNotes = fixedAssetNotes;
		this.oldStatus = status;
	}

	public double getTotalCapitalGainAmount() {
		return totalCapitalGainAmount;
	}

	public void setTotalCapitalGainAmount(double totalCapitalGainAmount) {
		this.totalCapitalGainAmount = totalCapitalGainAmount;
	}

	/**
	 * This static method will be called when either user approved depreciation
	 * for specific duration or roll back depreciation or change start date
	 **/

	@Override
	public boolean onSave(Session session) throws CallbackException {
		if (this.isOnSaveProccessed)
			return true;
		super.onSave(session);
		this.isOnSaveProccessed = true;
		/**
		 * Adjusting the book value with the result amount of deducting
		 * accumulated Depreciation amount from the Purchase Price.
		 */

		this.bookValue = this.purchasePrice
				- this.accumulatedDepreciationAmount;
		/*
		 * Initial set up of the opening balance for this Fixed Asset.
		 */
		this.openingBalanceForFiscalYear = this.bookValue;

		// if (this.assetAccount.linkedAccount == null) {
		// this.assetAccount.linkedAccount = this.accumulatedDepreciationAccount
		// .getName();
		// }

		if (this.linkedAccumulatedDepreciationAccount != null) {
			this.assetAccount.linkedAccumulatedDepreciationAccount = this.linkedAccumulatedDepreciationAccount;
		}

		/**
		 * Saving the action into History
		 */
		if (this.status == STATUS_PENDING) {
			FixedAssetHistory fixedAssetHistory = new FixedAssetHistory();
			fixedAssetHistory
					.setActionType(FixedAssetHistory.ACTION_TYPE_CREATED);
			fixedAssetHistory.setActionDate(new FinanceDate());
			fixedAssetHistory.setDetails("");
			this.fixedAssetsHistory.add(fixedAssetHistory);
		}

		if (this.status == STATUS_REGISTERED) {
			/**
			 * Saving the action into History
			 */

			FixedAssetHistory fixedAssetHistory = new FixedAssetHistory();
			fixedAssetHistory
					.setActionType(FixedAssetHistory.ACTION_TYPE_CREATED);
			fixedAssetHistory.setActionDate(new FinanceDate());
			fixedAssetHistory.setDetails("");
			this.fixedAssetsHistory.add(fixedAssetHistory);

			/**
			 * Saving the action into History
			 */

			FixedAssetHistory fixedAssetHistory2 = new FixedAssetHistory();
			fixedAssetHistory2
					.setActionType(FixedAssetHistory.ACTION_TYPE_REGISTERED);
			fixedAssetHistory2.setActionDate(new FinanceDate());
			fixedAssetHistory2.setDetails("");
			this.fixedAssetsHistory.add(fixedAssetHistory2);

		}

		/**
		 * If this Fixed Asset is purchased before the Last Depreciation Date
		 * then we must run Depreciation for this Fixed Asset from the Purchase
		 * Date till the Last Depreciation Date.
		 */
		FinanceDate lastDepreciationDate = Depreciation
				.getDepreciationLastDate(getCompany());
		if (lastDepreciationDate != null && this.status == STATUS_REGISTERED
				&& this.purchaseDate.before(lastDepreciationDate)) {

			/**
			 * Run the required depreciation till the Last Depreciation Date.
			 */
			Depreciation
					.runDepreciationFromPurchaseDateToLastDepreciationDate(this);

		}

		return false;
	}

	@Override
	public boolean onUpdate(Session session) throws CallbackException {
		super.onUpdate(session);
		/**
		 * Check whether any note is added, and Save the Action into History.
		 */
		if (this.fixedAssetNotes.size() > 0
				&& this.fixedAssetNotes.size() >= this.oldFixedAssetNotes
						.size()) {
			FixedAssetHistory fixedAssetHistory = new FixedAssetHistory();
			fixedAssetHistory.setActionType(FixedAssetHistory.ACTION_TYPE_NOTE);
			fixedAssetHistory.setActionDate(new FinanceDate());
			FixedAssetNote recentNote = this.fixedAssetNotes
					.get(this.fixedAssetNotes.size() - 1);
			fixedAssetHistory.setDetails(recentNote.getNote());
			this.getFixedAssetsHistory().add(fixedAssetHistory);
		}

		if (this.linkedAccumulatedDepreciationAccount != null
				&& this.oldLinkedAccumulatedDepreciationAccount != null
				&& this.linkedAccumulatedDepreciationAccount.getID() != this.oldLinkedAccumulatedDepreciationAccount
						.getID()) {
			Account account = this.getAssetAccount();
			account.setLinkedAccumulatedDepreciationAccount(linkedAccumulatedDepreciationAccount);
			session.saveOrUpdate(account);
		}
		if (this.oldStatus == STATUS_PENDING
				&& this.status == STATUS_REGISTERED && !this.isSoldOrDisposed) {

			/**
			 * 
			 * Save the Action into History
			 */
			FixedAssetHistory fixedAssetHistory = new FixedAssetHistory();
			fixedAssetHistory
					.setActionType(FixedAssetHistory.ACTION_TYPE_REGISTERED);
			fixedAssetHistory.setActionDate(new FinanceDate());
			fixedAssetHistory.setDetails("");
			this.getFixedAssetsHistory().add(fixedAssetHistory);
		}
		/**
		 * If this Fixed Asset is purchased before the Last Depreciation Date
		 * then we must run Depreciation for this Fixed Asset from the Purchase
		 * Date till the Last Depreciation Date.
		 */
		FinanceDate lastDepreciationDate = Depreciation
				.getDepreciationLastDate(getCompany());
		if (lastDepreciationDate != null
				&& (this.oldStatus == STATUS_PENDING || this.oldStatus == STATUS_REGISTERED)
				&& this.status == STATUS_REGISTERED
				&& this.purchaseDate.before(lastDepreciationDate)) {

			/**
			 * Run the required depreciation till the Last Depreciation Date.
			 */
			// Rakesh
			this.bookValue = purchasePrice - accumulatedDepreciationAmount;
			Depreciation
					.runDepreciationFromPurchaseDateToLastDepreciationDate(this);

		}
		/**
		 * Checking whether this Updating call is for Selling or Disposing an
		 * existing Fixed Asset.
		 */
		if (this.oldStatus != STATUS_SOLD_OR_DISPOSED
				&& this.status == STATUS_SOLD_OR_DISPOSED) {
			this.isSoldOrDisposed = true;
			// Query query = session.getNamedQuery("getNextTransactionNumber");
			// query.setLong("type", Transaction.TYPE_JOURNAL_ENTRY);
			// List list = query.list();
			// long nextVoucherNumber = 1;
			// if (list != null && list.size() > 0) {
			// nextVoucherNumber = ((Long) list.get(0)).longValue() + 1;
			// }
			String nextVoucherNumber = NumberUtils.getNextTransactionNumber(
					Transaction.TYPE_JOURNAL_ENTRY, getCompany());
			/**
			 * Preparing the journal entry for this Sell or Dispose.
			 */
			JournalEntry journalEntry = new JournalEntry(this,
					nextVoucherNumber);
			transactions.add(journalEntry);

			/**
			 * 
			 * Save the Action into History
			 */
			FixedAssetHistory fixedAssetHistory = new FixedAssetHistory();
			fixedAssetHistory
					.setActionType(FixedAssetHistory.ACTION_TYPE_DISPOSED);
			fixedAssetHistory.setActionDate(new FinanceDate());
			fixedAssetHistory.setDetails("View disposal journal");
			fixedAssetHistory.setPostedJournalEntry(journalEntry);
			this.getFixedAssetsHistory().add(fixedAssetHistory);

			// Date lastDepreciationDate =
			// Depreciation.getDepreciationLastDate();
			if (this.isNoDepreciation()) {
				/**
				 * If the last depreciation date is greater than the start date
				 * of the Financial year in which the SellOrDespose date will
				 * fall then we will roll-back all the depreciation for this
				 * FixedAsset till the start date of the Financial year in which
				 * the SellOrDespose date will fall.
				 */

				Calendar soldOrDisposedDateCal = new GregorianCalendar();
				soldOrDisposedDateCal.setTime(this.getSoldOrDisposedDate()
						.getAsDateObject());
				int year = soldOrDisposedDateCal.get(Calendar.YEAR);

				Calendar startDateCal = new GregorianCalendar();
				startDateCal.setTime(getCompany().getPreferences()
						.getDepreciationStartDate().getAsDateObject());

				int month = startDateCal.get(Calendar.MONTH);

				Calendar requiredStartDateCal = new GregorianCalendar();
				requiredStartDateCal.set(Calendar.YEAR, year);
				requiredStartDateCal.set(Calendar.MONTH, month);
				requiredStartDateCal.set(Calendar.DAY_OF_MONTH, 1);

				if (Utility.compareTo(lastDepreciationDate.getAsDateObject(),
						requiredStartDateCal.getTime()) > 0) {
					try {
						Depreciation
								.rollBackDepreciation(
										this.getID(),
										new FinanceDate(requiredStartDateCal
												.getTime()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				/**
				 * If the last depreciation date is prior to the start date of
				 * the Financial year in which the SellOrDespose date will fall
				 * then we will run the depreciation from last Depreciation date
				 * to the start date of the Financial year in which the
				 * SellOrDespose date will fall.
				 */
				if (Utility.compareTo(lastDepreciationDate.getAsDateObject(),
						requiredStartDateCal.getTime()) < 0) {

					Calendar lastDepreciationDateCal = new GregorianCalendar();
					lastDepreciationDateCal.setTime(lastDepreciationDate
							.getAsDateObject());
					lastDepreciationDateCal
							.set(Calendar.DAY_OF_MONTH, lastDepreciationDateCal
									.get(Calendar.DAY_OF_MONTH) + 1);

					Depreciation depreciation = new Depreciation();
					depreciation.setStatus(Depreciation.APPROVE);
					// List<FixedAsset> fixedAssets = new
					// ArrayList<FixedAsset>();
					// fixedAssets.add(this);
					// depreciation.setFixedAssets(fixedAssets);
					depreciation.setFixedAsset(this);
					depreciation
							.setDepreciationFor(Depreciation.DEPRECIATION_FOR_SINGLE_FIXEDASSET);
					depreciation
							.setDepreciateFrom(this
									.getPurchaseDate()
									.getAsDateObject()
									.compareTo(
											lastDepreciationDate
													.getAsDateObject()) <= 0 ? new FinanceDate(
									lastDepreciationDateCal.getTime()) : this
									.getPurchaseDate());
					depreciation.setDepreciateTo(new FinanceDate(
							requiredStartDateCal.getTime()));
					session.save(depreciation);

				}
			} else {

				FinanceDate depreciationTillDate = this
						.getDepreciationTillDate();
				/**
				 * If the last depreciation date is greater than the
				 * depreciationTill date of this FixedAsset then we will
				 * roll-back all the depreciation for this FixedAsset till the
				 * depreciationTill date of this FixedAsset.
				 */

				if (Utility.compareTo(lastDepreciationDate.getAsDateObject(),
						depreciationTillDate.getAsDateObject()) > 0) {
					try {
						Depreciation.rollBackDepreciation(this.getID(),
								depreciationTillDate);
					} catch (Exception e) {

						e.printStackTrace();
					}
				}

				/**
				 * If the last depreciation date is prior to the
				 * depreciationTill date of this FixedAsset then we will run the
				 * depreciation from last Depreciation date to the
				 * depreciationTill date of this FixedAsset.
				 */

				if (Utility.compareTo(lastDepreciationDate.getAsDateObject(),
						depreciationTillDate.getAsDateObject()) < 0) {

					Calendar lastDepreciationDateCal = new GregorianCalendar();
					lastDepreciationDateCal.setTime(lastDepreciationDate
							.getAsDateObject());
					lastDepreciationDateCal.set(Calendar.DAY_OF_MONTH,
							lastDepreciationDateCal.get(Calendar.DAY_OF_MONTH));

					Depreciation depreciation = new Depreciation();
					depreciation.setStatus(Depreciation.APPROVE);
					// List<FixedAsset> fixedAssets = new
					// ArrayList<FixedAsset>();
					// fixedAssets.add(this);
					// depreciation.setFixedAssets(fixedAssets);
					depreciation.setFixedAsset(this);
					depreciation
							.setDepreciationFor(Depreciation.DEPRECIATION_FOR_SINGLE_FIXEDASSET);
					depreciation
							.setDepreciateFrom(this.getPurchaseDate()
									.compareTo(lastDepreciationDate) <= 0 ? (new FinanceDate(
									lastDepreciationDateCal.getTime())) : this
									.getPurchaseDate());
					depreciation.setDepreciateTo(depreciationTillDate);
					session.save(depreciation);

				}

			}

		}

		ChangeTracker.put(this);

		return false;
	}

	/**
	 * The purpose of this method is to run the depreciation for every month
	 * from the given start date till the end date.
	 */

	public void createJournalEntriesForDepreciationAtTheEndOfEveryMonthFromStartDateToEndDate(
			Calendar fromCal1, Calendar toCal, Session session) {

		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		/**
		 * Adjusting the from date to the last day of that month.
		 */
		Calendar fromCal = new GregorianCalendar();
		fromCal.setTime(fromCal1.getTime());

		int maxDay = fromCal.getActualMaximum(Calendar.DAY_OF_MONTH);

		fromCal.set(fromCal.get(Calendar.YEAR), fromCal.get(Calendar.MONTH),
				maxDay);

		FinanceDate startDate = getCompany().getPreferences()
				.getDepreciationStartDate();
		Calendar startDateCal = new GregorianCalendar();
		startDateCal.setTime(startDate.getAsDateObject());

		while (fromCal.getTime().compareTo(toCal.getTime()) <= 0) {

			/**
			 * Adjusting the opening balance of this Fixed Asset each year after
			 * the start date.
			 */
			if (fromCal.get(Calendar.MONTH) == startDateCal.get(Calendar.MONTH)) {
				this.openingBalanceForFiscalYear = this.bookValue;
			}
			/**
			 * We should create one one Journal Entry for each Depreciation.
			 */
			// Query query = session.getNamedQuery("getNextTransactionNumber");
			// query.setLong("type", Transaction.TYPE_JOURNAL_ENTRY);
			// List list = query.list();
			// long nextVoucherNumber = 1;
			// if (list != null && list.size() > 0) {
			// nextVoucherNumber = ((Long) list.get(0)).longValue() + 1;
			// }
			String nextVoucherNumber = NumberUtils.getNextTransactionNumber(
					Transaction.TYPE_JOURNAL_ENTRY, getCompany());

			double amount = depreciationMethod == Depreciation.METHOD_STRAIGHT_LINE ? this.purchasePrice
					: this.openingBalanceForFiscalYear;

			double depreciationAmount = Double.parseDouble(decimalFormat
					.format(amount * depreciationRate / 1200));

			JournalEntry journalEntry = new JournalEntry(this, new FinanceDate(
					fromCal.getTime()), nextVoucherNumber, depreciationAmount);
			transactions.add(journalEntry);

			/**
			 * 
			 * Save the Action into History
			 */
			FixedAssetHistory fixedAssetHistory = new FixedAssetHistory();
			fixedAssetHistory
					.setActionType(FixedAssetHistory.ACTION_TYPE_DEPRECIATED);
			fixedAssetHistory.setActionDate(new FinanceDate());
			fixedAssetHistory.setDetails("Depreciation of "
					+ depreciationAmount + " on "
					+ format.format(fromCal.getTime()));
			this.fixedAssetsHistory.add(fixedAssetHistory);

			/**
			 * update the book value with the calculated depreciation amount
			 */
			this.bookValue -= depreciationAmount;
			/**
			 * Update the accumulated Depreciation amount with the calculated
			 * Depreciation amount.
			 */
			// this.accumulatedDepreciationAmount += depreciationAmount;

			// if (fromCal.get(Calendar.YEAR) > toCal.get(Calendar.YEAR)
			// && fromCal.get(Calendar.MONTH) == 11) {
			//
			// fromCal.set(fromCal.get(Calendar.YEAR) + 1, 0, 31);
			//
			// } else

			{
				/**
				 * Adjusting the from date so that it will hold the next month
				 * last date.
				 */
				int year = fromCal.get(Calendar.YEAR);
				int month = fromCal.get(Calendar.MONTH) + 1;

				fromCal.clear();
				fromCal.set(Calendar.YEAR, year);
				fromCal.set(Calendar.MONTH, month);
				fromCal.set(Calendar.DATE,
						fromCal.getActualMaximum(Calendar.DAY_OF_MONTH));

			}

		}

	}

	/**
	 * 
	 * The purpose of this method is to pre-calculate the depreciation amount
	 * between to dates, with our run the Depreciation.
	 * 
	 * @param depreciationFrom
	 * @param depreciationTo
	 * @return calculated Depreciated amount between Depreciation From and
	 *         Depreciation to dates.
	 * 
	 * 
	 */
	public double getCalculatedDepreciatedAmount(FinanceDate depreciationFrom,
			FinanceDate depreciationTo) {

		/**
		 * In this process of calculating Depreciation amount, the actual values
		 * of the Fixed Asset will change. But we don't need this effect on the
		 * Fixed Asset. So we have to clone this Fixed Asset and make the
		 * operations on the cloned one but on the Actual Fixed Asset.
		 */
		FixedAsset fixedAsset = null;
		try {
			fixedAsset = this.clone();
		} catch (CloneNotSupportedException e) {

			e.printStackTrace();
		}

		DecimalFormat decimalFormat = new DecimalFormat("##.##");

		double amountToBeDepreciatedforThisFixedAsset = 0.0;

		/**
		 * In order to calculate the Depreciation amount between two given
		 * dates, the depreciation should be calculated on monthly basis at the
		 * end of each month in the given date range. So to get the end date at
		 * each month, we need to create Calendar Instance on the given from and
		 * to dates.
		 */
		Calendar fromCal = new GregorianCalendar();
		fromCal.setTime(depreciationFrom.getAsDateObject());

		Calendar toCal = new GregorianCalendar();
		toCal.setTime(depreciationTo.getAsDateObject());

		int maxDay = fromCal.getActualMaximum(Calendar.DAY_OF_MONTH);

		fromCal.set(fromCal.get(Calendar.YEAR), fromCal.get(Calendar.MONTH),
				maxDay);

		/**
		 * This is to get the depreciation start date. Here the purpose of this
		 * depreciation start date is, if this Fixed Asset depreciation method
		 * is Reducing Balances then, we need to reduce the bookvalue of this
		 * Fixed Asset at the end of Each Financial year by the depreciation
		 * amount calculated for this fiscal year.
		 */
		FinanceDate startDate = getCompany().getPreferences()
				.getDepreciationStartDate();
		Calendar startDateCal = new GregorianCalendar();
		startDateCal.setTime(startDate.getAsDateObject());

		if (startDate.equals(depreciationFrom)) {
			fixedAsset.setOpeningBalanceForFiscalYear(fixedAsset
					.getPurchasePrice()
					- fixedAsset.accumulatedDepreciationAmount);
		}
		/**
		 * The following loop should iterate once for each month between the
		 * from date and end date.
		 */

		while (fromCal.getTime().compareTo(toCal.getTime()) <= 0) {

			/**
			 * Adjusting the opening balance of this Fixed Asset each year after
			 * the start date by the actual bookvalue which represents the
			 * updated bookvalue at the end of each month.
			 */
			if (fromCal.get(Calendar.MONTH) == startDateCal.get(Calendar.MONTH)) {
				fixedAsset.setOpeningBalanceForFiscalYear(fixedAsset
						.getBookValue());
			}

			/**
			 * If the depreciation method is Straight Line then The depreciation
			 * should calculate on the Purchase Price other wise the
			 * Depreciation should calculate on the Book value in that year.
			 */
			double amount = fixedAsset.getDepreciationMethod() == Depreciation.METHOD_STRAIGHT_LINE ? fixedAsset
					.getPurchasePrice() : fixedAsset
					.getOpeningBalanceForFiscalYear();

			/**
			 * To calculate the depreciation amount for this month based on the
			 * depreciation rate.
			 */
			double depreciationAmount = Double.parseDouble(decimalFormat
					.format(amount * fixedAsset.getDepreciationRate() / 1200));

			amountToBeDepreciatedforThisFixedAsset += depreciationAmount;

			/**
			 * Decreasing the Book Value of this Fixed Asset by the calculated
			 * depreciation amount for this month.
			 */
			fixedAsset.setBookValue(fixedAsset.getBookValue()
					- depreciationAmount);

			/**
			 * Updating this Fixed Asset's accumulated depreciation amount by
			 * this calculated depreciation amount for this month.
			 */
			fixedAsset.setAccumulatedDepreciationAmount(fixedAsset
					.getAccumulatedDepreciationAmount() + depreciationAmount);

			/**
			 * Adjusting the from date so that it will hold the next month last
			 * date.
			 */
			int year = fromCal.get(Calendar.YEAR);
			int month = fromCal.get(Calendar.MONTH) + 1;

			fromCal.clear();
			fromCal.set(Calendar.YEAR, year);
			fromCal.set(Calendar.MONTH, month);
			fromCal.set(Calendar.DATE,
					fromCal.getActualMaximum(Calendar.DAY_OF_MONTH));

		}

		return amountToBeDepreciatedforThisFixedAsset;
	}

	/**
	 * 
	 * This method will give us the total effect of Selling or Disposing a Fixed
	 * Asset, before Sell or Dispose this Fixed Asset.
	 * 
	 * @return
	 * @throws Exception
	 */
	public FixedAssetSellOrDisposeReviewJournal getReviewJournal()
			throws Exception {

		TempFixedAsset fixedAsset = new TempFixedAsset();
		fixedAsset.setFixedAssetID(this.getID());
		fixedAsset.setPurchaseDate(new ClientFinanceDate(this.getPurchaseDate()
				.getDate()));
		fixedAsset.setNoDepreciation(this.isNoDepreciation());
		fixedAsset.setSoldOrDisposedDate(new ClientFinanceDate(
				this.soldOrDisposedDate.getDate()));
		fixedAsset.setDepreciationTillDate(new ClientFinanceDate(this
				.getDepreciationTillDate().getDate()));
		fixedAsset.setPurchasePrice(this.getPurchasePrice());
		fixedAsset.setSalesPrice(this.getSalePrice());
		fixedAsset.setBookValue(this.getBookValue());
		fixedAsset.setAssetAccountName(this.getAssetAccount().getName());
		fixedAsset.setExpenseAccountName(this.getDepreciationExpenseAccount()
				.getName());
		fixedAsset.setSalesAccountName(this.getAccountForSale() != null ? (this
				.getAccountForSale().getName()) : "");
		fixedAsset.setDepreciationMethod(this.getDepreciationMethod());
		fixedAsset.setDepreciationRate(this.getDepreciationRate());

		/**
		 * In this method we should prepare two maps named disposalSummary and
		 * disposalJournal. Each Map contains several Strings as keys and
		 * corresponing calculated values for them.
		 */

		DecimalFormat decimalFormat = new DecimalFormat("##.##");
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		FixedAssetSellOrDisposeReviewJournal fixedAssetSellOrDisposeReviewJournal = new FixedAssetSellOrDisposeReviewJournal();

		/**
		 * Preparing the keys and values for disposalSummary Map.
		 */
		String purchasedDate = "Purchase "
				+ format.format(fixedAsset.getPurchaseDate());
		String currentAccumulatedDepreciation = "Current accumulated depreciation";
		String depreciationTobePosted = "Depreciation to be posted (";
		String rollBackDepreciation = "rollback deprecaition till ";
		FinanceDate date = Depreciation.getDepreciationLastDate(getCompany());
		FinanceDate depreciationTillDate = null;
		double depreciationToBePostedAmount = 0.0;
		double rollBackDepreciatinAmount = 0.0;

		FinanceDate lastDepreciationDate = date;
		// Below code is not required
		// if (fixedAsset.getPurchaseDate().compareTo(lastDepreciationDate) > 0)
		// {
		// lastDepreciationDate = fixedAsset.getPurchaseDate();
		// }
		Calendar lastDepreciationDateCal = new GregorianCalendar();
		lastDepreciationDateCal.setTime(lastDepreciationDate.getAsDateObject());
		lastDepreciationDateCal.set(Calendar.DAY_OF_MONTH, 01);
		lastDepreciationDateCal.set(Calendar.MONTH,
				lastDepreciationDateCal.get(Calendar.MONTH) + 1);

		if (fixedAsset.isNoDepreciation()) {

			Calendar soldOrDisposedDateCal = new GregorianCalendar();
			soldOrDisposedDateCal.setTime(fixedAsset.getSoldOrDisposedDate()
					.getDateAsObject());

			FinanceDate startDate = getCompany().getPreferences()
					.getDepreciationStartDate();
			Calendar startDateCal = new GregorianCalendar();
			startDateCal.setTime(startDate.getAsDateObject());

			Calendar soldYearStartDateCal = new GregorianCalendar(
					soldOrDisposedDateCal.get(Calendar.YEAR),
					startDateCal.get(Calendar.MONTH),
					startDateCal.get(Calendar.DAY_OF_MONTH));
			soldYearStartDateCal.set(Calendar.MONTH,
					soldYearStartDateCal.get(Calendar.MONTH) - 1);
			soldYearStartDateCal.set(Calendar.DAY_OF_MONTH,
					soldYearStartDateCal
							.getActualMaximum(Calendar.DAY_OF_MONTH));

			// if (fixedAsset.getPurchaseDate().compareTo(lastDepreciationDate)
			// <= 0) {

			if (lastDepreciationDate.compareTo(new FinanceDate(
					soldYearStartDateCal.getTime())) < 0) {

				// Run depreciation from last depreciation date to sold year
				// start date.
				ClientFinanceDate depFrom = fixedAsset.getPurchaseDate()
						.compareTo(
								new ClientFinanceDate(lastDepreciationDate
										.getDate())) <= 0 ? (new ClientFinanceDate(
						lastDepreciationDateCal.getTime())) : (fixedAsset
						.getPurchaseDate());
				depreciationTobePosted += format.format(depFrom);
				depreciationTobePosted += " to";
				depreciationTobePosted += format.format(soldYearStartDateCal
						.getTime());
				depreciationTobePosted += ")";

				depreciationToBePostedAmount = getCalculatedDepreciatedAmount(
						fixedAsset.getDepreciationMethod(),
						fixedAsset.getDepreciationRate(),
						fixedAsset.getPurchasePrice(),
						new FinanceDate(depFrom.getDate()), new FinanceDate(
								soldYearStartDateCal.getTime()));
				depreciationToBePostedAmount = Double.parseDouble(decimalFormat
						.format(depreciationToBePostedAmount));

			} else if (lastDepreciationDate.compareTo(new FinanceDate(
					soldYearStartDateCal.getTime())) > 0) {

				// Roll back the Depreciation till the sold year start date.
				rollBackDepreciation += format.format(soldYearStartDateCal
						.getTime());
				rollBackDepreciatinAmount = getCalculatedRollBackDepreciationAmount(
						fixedAsset.getFixedAssetID(), (new FinanceDate(
								soldYearStartDateCal.getTime())));
				rollBackDepreciatinAmount = Double.parseDouble(decimalFormat
						.format(rollBackDepreciatinAmount));

			}
			// }

			depreciationTillDate = new FinanceDate(
					soldYearStartDateCal.getTime());

		} else {

			depreciationTillDate = new FinanceDate(fixedAsset
					.getDepreciationTillDate().getDate());

			if (fixedAsset.getPurchaseDate().compareTo(
					new ClientFinanceDate(depreciationTillDate.getDate())) <= 0) {

				if (lastDepreciationDate.compareTo(depreciationTillDate) < 0) {
					FinanceDate depFrom = fixedAsset.getPurchaseDate()
							.compareTo(
									new ClientFinanceDate(lastDepreciationDate
											.getDate())) <= 0 ? (new FinanceDate(
							lastDepreciationDateCal.getTime()))
							: new FinanceDate(fixedAsset.getPurchaseDate()
									.getDate());

					depreciationTobePosted += format.format(depFrom);
					depreciationTobePosted += " to";
					depreciationTobePosted += format
							.format(depreciationTillDate);
					depreciationTobePosted += ")";
					depreciationToBePostedAmount = getCalculatedDepreciatedAmount(
							fixedAsset.getDepreciationMethod(),
							fixedAsset.getDepreciationRate(),
							fixedAsset.getPurchasePrice(), depFrom,
							depreciationTillDate);
					depreciationToBePostedAmount = Double
							.parseDouble(decimalFormat
									.format(depreciationToBePostedAmount));
				} else if (lastDepreciationDate.compareTo(depreciationTillDate) > 0) {

					// Roll back the Depreciation upto given Depreciation till
					// date.
					rollBackDepreciation += format.format(depreciationTillDate);
					rollBackDepreciatinAmount = getCalculatedRollBackDepreciationAmount(
							fixedAsset.getFixedAssetID(), depreciationTillDate);
					rollBackDepreciatinAmount = Double
							.parseDouble(decimalFormat
									.format(rollBackDepreciatinAmount));
				}

			}

		}

		String soldDate = "Sold "
				+ format.format(fixedAsset.getSoldOrDisposedDate());

		/**
		 * Preparing the disposalSummary Map with the above calculated Keys and
		 * corresponding amounts for those keys.
		 */
		Map<String, Double> disposalSummary = new LinkedHashMap<String, Double>();
		disposalSummary.put(purchasedDate, fixedAsset.getPurchasePrice());
		if (!DecimalUtil.isEquals(fixedAsset.getPurchasePrice(),
				fixedAsset.getBookValue())) {
			// disposalSummary.put(currentAccumulatedDepreciation, fixedAsset
			// .getAccumulatedDepreciationAmount());
			disposalSummary
					.put(currentAccumulatedDepreciation, (fixedAsset
							.getPurchasePrice() - fixedAsset.getBookValue()));
		}
		if (!DecimalUtil.isEquals(depreciationToBePostedAmount, 0.0)) {
			disposalSummary.put(depreciationTobePosted,
					depreciationToBePostedAmount);
		}
		if (!DecimalUtil.isEquals(rollBackDepreciatinAmount, 0.0)) {
			disposalSummary
					.put(rollBackDepreciation, rollBackDepreciatinAmount);
		}

		disposalSummary.put(soldDate, fixedAsset.getSalesPrice());

		/**
		 * Preparing the keys and values for disposalJournal Map.
		 */
		String assetAccount = fixedAsset.getAssetAccountName();
		String lessAccoummulatedDepreciation = this.getAssetAccount()
				.getLinkedAccumulatedDepreciationAccount().getName();
		String salesAccount = fixedAsset.getSalesAccountName();
		String totalCapitalGainString = "Total Capital Gain";
		String gainOnDisposal = "Gain on disposal";
		String lossOnDisposal = "Loss on disposal";

		double salesPrice = fixedAsset.getSalesPrice();
		double purchasePrice = fixedAsset.getPurchasePrice();
		double lessAccumulatedDepreciationAmount = fixedAsset
				.getPurchasePrice() - fixedAsset.getBookValue();

		/**
		 * calculating whether there will be any capital gain or not for this
		 * Sell or Dispose of Fixed Asset.
		 */
		double totalCapitalGain = Double
				.parseDouble(decimalFormat.format((DecimalUtil.isGreaterThan(
						salesPrice, purchasePrice)) ? (salesPrice - purchasePrice)
						: 0.0));

		double calculatedLessAccDep = (fixedAsset.getPurchasePrice() - fixedAsset
				.getBookValue())
				+ depreciationToBePostedAmount
				- rollBackDepreciatinAmount;

		/**
		 * calculating gain/loss for this Sell or Dispose of Fixed Asset. If the
		 * calculated amount is +ve then it represents gain or if the calculated
		 * amount is -ve then it represents loss.
		 */
		double lossOrGainOnDisposal = Double.parseDouble(decimalFormat
				.format((salesPrice + calculatedLessAccDep) - purchasePrice
						- totalCapitalGain));

		/**
		 * Preparing the disposalJournal Map with the above calculated Keys and
		 * corresponding amounts for those keys.
		 */
		Map<String, Double> disposalJournal = new LinkedHashMap<String, Double>();
		disposalJournal.put(assetAccount, fixedAsset.getPurchasePrice());

		if (!DecimalUtil.isEquals(calculatedLessAccDep, 0.0)) {
			disposalJournal.put(lessAccoummulatedDepreciation, -1
					* (calculatedLessAccDep));
		}

		if (salesAccount != null) {
			disposalJournal.put(salesAccount, -1 * salesPrice);
		}

		if (DecimalUtil.isGreaterThan(totalCapitalGain, 0.0)) {
			disposalJournal.put(totalCapitalGainString, totalCapitalGain);
		}
		if (!DecimalUtil.isEquals(lossOrGainOnDisposal, 0.0)) {
			if (DecimalUtil.isGreaterThan(lossOrGainOnDisposal, 0.0)) {
				disposalJournal.put(gainOnDisposal, lossOrGainOnDisposal);
			} else {
				disposalJournal.put(lossOnDisposal, lossOrGainOnDisposal);
			}
		}

		fixedAssetSellOrDisposeReviewJournal
				.setDisposalSummary(disposalSummary);
		fixedAssetSellOrDisposeReviewJournal
				.setDisposalJournal(disposalJournal);
		return fixedAssetSellOrDisposeReviewJournal;

	}

	public double getCalculatedRollBackDepreciationAmount(long fixedAssetID,
			FinanceDate rollBackDepreciationTo) throws DAOException {

		Session session = HibernateUtil.getCurrentSession() == null ? Utility
				.getCurrentSession() : HibernateUtil.getCurrentSession();

		Query query = session
				.getNamedQuery("getDepreciation.byFixedAsset.andWithDetails")
				.setLong("date", rollBackDepreciationTo.getDate())
				.setInteger("status", Depreciation.APPROVE)
				.setLong("id", fixedAssetID).setEntity("company", getCompany());

		List<Depreciation> list = query.list();
		double rollBackDepAmt = 0.0;
		for (Depreciation dep : list) {
			// for (FixedAsset fixedAsset : dep.getFixedAssets()) {
			if (dep.getFixedAsset() != null)
				for (Transaction trans : dep.getFixedAsset().getTransactions()) {
					if (!trans.isVoid()) {
						rollBackDepAmt += ((JournalEntry) trans)
								.getDebitTotal();
					}
				}
			// }
		}
		return rollBackDepAmt;
	}

	public double getCalculatedDepreciatedAmount(int depreciationMethod,
			double depreciationRate, double purchasePrice,
			FinanceDate depreciationFrom, FinanceDate depreciationTo)
			throws DAOException {

		/**
		 * Just Preparing the Dummy fixed asset object with the following fixed
		 * asset information.
		 */
		FixedAsset fixedAsset = new FixedAsset();
		fixedAsset.setPurchaseDate(depreciationTo);
		fixedAsset.setPurchasePrice(purchasePrice);
		fixedAsset.setDepreciationMethod(depreciationMethod);
		fixedAsset.setDepreciationRate(depreciationRate);

		// try {
		// fixedAsset = fixedAsset2.clone();
		// } catch (CloneNotSupportedException e) {
		//
		// e.printStackTrace();
		// }

		DecimalFormat decimalFormat = new DecimalFormat("##.##");

		double amountToBeDepreciatedforThisFixedAsset = 0.0;

		/**
		 * In order to calculate the Depreciation amount between two given
		 * dates, the depreciation should be calculated on monthly basis at the
		 * end of each month in the given date range. So to get the end date at
		 * each month, we need to create Calendar Instance on the given from and
		 * to dates.
		 */
		Calendar fromCal = new GregorianCalendar();
		fromCal.setTime(depreciationFrom.getAsDateObject());

		Calendar toCal = new GregorianCalendar();
		toCal.setTime(depreciationTo.getAsDateObject());

		int maxDay = fromCal.getActualMaximum(Calendar.DAY_OF_MONTH);

		fromCal.set(fromCal.get(Calendar.YEAR), fromCal.get(Calendar.MONTH),
				maxDay);

		/**
		 * This is to get the depreciation start date. Here the purpose of this
		 * depreciation start date is, if this Fixed Asset depreciation method
		 * is Reducing Balances then, we need to reduce the bookvalue of this
		 * Fixed Asset at the end of Each Financial year by the depreciation
		 * amount calculated for this fiscal year.
		 */
		FinanceDate startDate = getCompany().getPreferences()
				.getDepreciationStartDate();
		Calendar startDateCal = new GregorianCalendar();
		startDateCal.setTime(startDate.getAsDateObject());

		fixedAsset
				.setOpeningBalanceForFiscalYear(fixedAsset.getPurchasePrice());

		/**
		 * The following loop should iterate once for each month between the
		 * from date and end date.
		 */

		while (fromCal.getTime().compareTo(toCal.getTime()) <= 0) {

			/**
			 * If the depreciation method is Straight Line then The depreciation
			 * should calculate on the Purchase Price other wise the
			 * Depreciation should calculate on the Book value in that year.
			 */
			double amount = fixedAsset.getDepreciationMethod() == Depreciation.METHOD_STRAIGHT_LINE ? fixedAsset
					.getPurchasePrice() : fixedAsset
					.getOpeningBalanceForFiscalYear();

			/**
			 * To calculate the depreciation amount for this month based on the
			 * depreciation rate.
			 */
			double depreciationAmount = Double.parseDouble(decimalFormat
					.format(amount * fixedAsset.getDepreciationRate() / 1200));

			amountToBeDepreciatedforThisFixedAsset += depreciationAmount;

			/**
			 * Decreasing the Book Value of this Fixed Asset by the calculated
			 * depreciation amount for this month.
			 */
			fixedAsset.setBookValue(fixedAsset.getBookValue()
					- depreciationAmount);

			/**
			 * Updating this Fixed Asset's accumulated depreciation amount by
			 * this calculated depreciation amount for this month.
			 */
			fixedAsset.setAccumulatedDepreciationAmount(fixedAsset
					.getAccumulatedDepreciationAmount() + depreciationAmount);

			/**
			 * Adjusting the from date so that it will hold the next month last
			 * date.
			 */
			int year = fromCal.get(Calendar.YEAR);
			int month = fromCal.get(Calendar.MONTH) + 1;

			fromCal.clear();
			fromCal.set(Calendar.YEAR, year);
			fromCal.set(Calendar.MONTH, month);
			fromCal.set(Calendar.DATE,
					fromCal.getActualMaximum(Calendar.DAY_OF_MONTH));

			/**
			 * Adjusting the opening balance of this Fixed Asset each year after
			 * the start date by the actual bookvalue which represents the
			 * updated bookvalue at the end of each month.
			 */
			if (fromCal.get(Calendar.MONTH) == startDateCal.get(Calendar.MONTH)) {
				fixedAsset.setOpeningBalanceForFiscalYear(fixedAsset
						.getBookValue());
			}

		}

		return amountToBeDepreciatedforThisFixedAsset;
	}

	public List<FixedAssetNote> getFixedAssetNotes() {
		return fixedAssetNotes;
	}

	public void setFixedAssetNotes(List<FixedAssetNote> fixedAssetNotes) {
		this.fixedAssetNotes = fixedAssetNotes;
	}

	public List<FixedAssetHistory> getFixedAssetsHistory() {
		return fixedAssetsHistory;
	}

	public void setFixedAssetsHistory(List<FixedAssetHistory> fixedAssetsHistory) {
		this.fixedAssetsHistory = fixedAssetsHistory;
	}

	@Override
	public FixedAsset clone() throws CloneNotSupportedException {
		return (FixedAsset) super.clone();
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		// TODO Auto-generated method stub
		return true;
	}
}
