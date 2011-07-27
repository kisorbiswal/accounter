package com.vimukti.accounter.core.trigger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.api.Trigger;

import com.vimukti.accounter.core.Account;
import com.vimukti.accounter.core.AccounterConstants;

/**
 * 
 * @author Devesh Satwani
 * 
 */
public class TriggerAccount implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization

		Long newAccountId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		Long oldAccountId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;

		Integer newAccountType = (newRow != null) ? ((newRow[2] != null) ? (Integer) newRow[2]
				: null)
				: null;
		@SuppressWarnings("unused")
		Integer oldAccountType = (oldRow != null) ? ((oldRow[2] != null) ? (Integer) oldRow[2]
				: null)
				: null;

		Long newParentId = (newRow != null) ? ((newRow[6] != null) ? (Long) newRow[6]
				: null)
				: null;
		Long oldParentId = (oldRow != null) ? ((oldRow[6] != null) ? (Long) oldRow[6]
				: null)
				: null;

		Double newOpeningBalance = (newRow != null) ? ((newRow[8] != null) ? (Double) newRow[8]
				: null)
				: null;
		Double oldOpeningBalance = (oldRow != null) ? ((oldRow[8] != null) ? (Double) oldRow[8]
				: null)
				: null;

		Double newTotalBalance = (newRow != null) ? ((newRow[19] != null) ? (Double) newRow[19]
				: null)
				: null;
		Double oldTotalBalance = (oldRow != null) ? ((oldRow[19] != null) ? (Double) oldRow[19]
				: null)
				: null;

		Boolean newIsOpeningBalanceEditable = (newRow != null) ? ((newRow[20] != null) ? (Boolean) newRow[20]
				: null)
				: null;
		Boolean oldIsOpeningBalanceEditable = (oldRow != null) ? ((oldRow[20] != null) ? (Boolean) oldRow[20]
				: null)
				: null;

		Long companyId = (newRow != null) ? ((newRow[1] != null) ? (Long) newRow[1]
				: null)
				: null;

		Boolean isPositiveAccount = (newRow != null) ? ((newRow[17] != null) ? (Boolean) newRow[17]
				: null)
				: null;

		String accountName = "";
		Statement stat = conn.createStatement();

		// Date newAsOfDate = (newRow != null)?((newRow[10] != null)?(Date)
		// newRow[10]:null):null;
		// String oldAsOfDate = (oldRow != null)?((oldRow[10] != null)?(String)
		// oldRow[10]:null):null;

		// Condition for checking whether this Trigger call is for new Row
		// Insertion
		if (newRow != null && oldRow == null) {
			//
			//
			// // Making this account opening balance as un editable if the
			// account fall under following type
			//
			//
			// if(!newOpeningBalance.equals(0.0) || newAccountType ==
			// Account.TYPE_INVENTORY_ASSET){
			//
			//
			// stat
			// .execute(new StringBuilder()
			// .append(
			// "UPDATE ACCOUNT A SET A.IS_OPENING_BALANCE_EDITABLE = 'FALSE' WHERE A.ID = ").append(newAccountId).toString());
			//
			// }
			//
			//
			//
			//
			//
			// // Query to retrieve the Name of the New Account
			// accountName = getName(stat, accountName, newAccountId);
			//
			// // To check whether this Account is Opening Balances account or
			// not
			// // and opening balance amount of this Account is greater than
			// zero
			// // or not
			//
			// if (newOpeningBalance != 0.0
			// && !accountName
			// .equalsIgnoreCase(AccounterConstants.OPENING_BALANCE)) {
			//
			// // Query to retrieve the type and the company id of this
			// // account.
			// // To check whether the new Account is positive account or not
			//
			// if (!isPositiveAccount) {
			//
			// // Query to Increase the opening balance amount, current
			// // balance amount and the total balance amount of the
			// // Opening Balances Account with the opening balance amount
			// // of the Opening Balances Account
			//
			// updateAllBalancesOfOpeningBalanceAccount(stat, newOpeningBalance,
			// companyId,AccounterConstants.SYMBOL_PLUS);
			//
			// // // inserting the corresponding rows into Account Transaction
			// table
			// // stat
			// // .execute(new StringBuilder()
			// // .append(
			// //
			// "INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) VALUES ")
			// // .append(
			// // "(null,")
			// // .append(AccountTransaction.TYPE_ACCOUNT)
			// // .append(",")
			// // .append(newAccountId)
			// // .append(",null,null,'" )
			// // .append(newRow[10])
			// // .append(" 00:00:00',")
			// // .append(newOpeningBalance)
			// // .append(")").toString());
			//
			// }else{
			//
			//
			// // // inserting the corresponding rows into Account Transaction
			// table
			// // stat
			// // .execute(new StringBuilder()
			// // .append(
			// //
			// "INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) VALUES ")
			// // .append(
			// // "(null,")
			// // .append(AccountTransaction.TYPE_ACCOUNT)
			// // .append(",")
			// // .append(newAccountId)
			// // .append(",null,null,'" )
			// // .append(newRow[10])
			// // .append(" 00:00:00',-1*")
			// // .append(newOpeningBalance)
			// // .append(")").toString());
			//
			// // To check the type of new Account is Equity and the parent
			// // is not null or not
			// if (newParentId != null
			// && newAccountType == Account.TYPE_EQUITY) {
			//
			// // To check whether the Parent of the new Account is
			// // Opening Balances Account or one of the sub level of
			// // the Opening Balances Account
			// if (newParentId == 1
			// || isItAnySubLevelOfOpeningBalance(stat,
			// newParentId)) {
			//
			// // then we will change the opening balance amount
			// // and current balance amount of Opening Balances
			// // account.
			//
			// updateOpeningAndCurrentBalanceOfOpeningBalancesAccount(stat,
			// newOpeningBalance, companyId);
			//
			//
			// // Here we will change the total balance amount of
			// // this new Account if it's parent is not Opening
			// // Balances Account but one of the sub account of
			// // the Opening Banaces Account
			//
			// if (newParentId != 1
			// && isItAnySubLevelOfOpeningBalance(stat,
			// newParentId)) {
			//
			// updateTotalBalanceOfOpeningBalancesAccount(stat,
			// newOpeningBalance, companyId);
			//
			//
			// }
			//
			// }else {
			//
			// // Query to Decrease the opening balance amount, current
			// // balance amount and the total balance amount of the
			// // Opening Balances Account with the opening balance
			// // amount of the New Account
			//
			// updateAllBalancesOfOpeningBalanceAccount(stat, newOpeningBalance,
			// companyId,AccounterConstants.SYMBOL_MINUS);
			//
			//
			// }
			//
			// } else {
			//
			// // Query to Decrease the opening balance amount, current
			// // balance amount and the total balance amount of the
			// // Opening Balances Account with the opening balance
			// // amount of the New Account
			//
			// updateAllBalancesOfOpeningBalanceAccount(stat, newOpeningBalance,
			// companyId,AccounterConstants.SYMBOL_MINUS);
			//
			//
			// }
			// }
			//
			// }
			//
			// // To check if the Parent of the new Account is not null and not
			// the
			// // Opening Balances Account or not
			// if (newParentId != null && newParentId != 1) {
			//
			// // Query to Increase the total balance amount of the parent
			// // Account with the total balance amount of the new Account
			//
			// updateTotalBalanceOfParentAccount(stat,
			// newTotalBalance,newParentId);
			//
			//
			//
			// }
			//
		}

		// Condition for checking whether this Trigger call is for Row Updation
		else if (newRow != null && oldRow != null) {

			if (oldIsOpeningBalanceEditable.equals(newIsOpeningBalanceEditable)) {

				// Making this account opening balance as un editable if the
				// account fall under following type

				if (!(oldOpeningBalance.equals(newOpeningBalance))
						&& oldIsOpeningBalanceEditable.equals(Boolean.TRUE)) {

					stat.execute(new StringBuilder()
							.append("UPDATE ACCOUNT A SET A.IS_OPENING_BALANCE_EDITABLE = 'FALSE' WHERE A.ID = ")
							.append(oldAccountId).toString());

				}

				// Query to retrieve the Name of the updated Account

				accountName = getName(stat, accountName, newAccountId);

				// To check whether this Account is Opening Balances account or
				// not
				// and opening balance amount of old Account and new account are
				// equal or not

				if (!(oldOpeningBalance).equals(newOpeningBalance)
						&& !accountName
								.equalsIgnoreCase(AccounterConstants.OPENING_BALANCE)) {

					// Query to retrieve the type and the company id of this
					// account.
					if (!isPositiveAccount) {

						// Query to Increase the opening balance amount, current
						// balance amount and the total balance amount of the
						// Opening Balances Account with the difference amount
						// of
						// the old and new opening balances amount of the
						// updated
						// Account

						updateAllBalancesOfOpeningBalancesAccountWithDifferenceAmount(
								stat, companyId, newOpeningBalance,
								oldOpeningBalance,
								AccounterConstants.SYMBOL_PLUS);

					} else {

						// To check the type of updated Account is Equity and
						// the
						// parent is not null or not

						if (newParentId != null
								&& newAccountType == Account.TYPE_EQUITY) {

							// To check whether the Parent of the new Account is
							// Opening Balances Account or one of the sub level
							// of
							// the Opening Balances Account
							if (newParentId == 1
									|| isItAnySubLevelOfOpeningBalance(stat,
											newParentId)) {
								// then we will change the opening balance
								// amount
								// and current balance amount of Opening
								// Balances
								// account.
								updateOpeningAndCurrentBalanceOfOpeningBalancesAccount(
										stat, newOpeningBalance, companyId);

								// Here we will change the total balance amount
								// of
								// this updated Account if it's parent is not
								// Opening Balances Account but one of the sub
								// account of the Opening Banaces Account

								if (newParentId != 1
										&& isItAnySubLevelOfOpeningBalance(
												stat, newParentId)) {
									updateTotalBalanceOfOpeningBalancesAccount(
											stat, newOpeningBalance, companyId);

								}

							} else {

								// Query to Decrease the opening balance amount,
								// current
								// balance amount and the total balance amount
								// of the
								// Opening Balances Account with the opening
								// balance
								// amount of the New Account

								updateAllBalancesOfOpeningBalanceAccount(stat,
										newOpeningBalance, companyId,
										AccounterConstants.SYMBOL_MINUS);

							}

						} else {
							// Query to Decrease the opening balance amount,
							// current
							// balance amount and the total balance amount of
							// the
							// Opening Balances Account with the opening balance
							// amount of the updated Account

							updateAllBalancesOfOpeningBalancesAccountWithDifferenceAmount(
									stat, companyId, newOpeningBalance,
									oldOpeningBalance,
									AccounterConstants.SYMBOL_MINUS);

						}
					}

				}

				// To check if the Parent of the Updated Account is not null and
				// not
				// the Opening Balances Account or not

				if (oldParentId != null
						&& !(oldParentId == 1 && oldOpeningBalance
								.equals(newOpeningBalance))) {
					// Query to Increase the total balance amount of the parent
					// Account with the difference of old and new total balance
					// amount of the updated Account

					Double differenceAmount = newTotalBalance - oldTotalBalance;
					updateTotalBalanceOfParentAccount(stat, differenceAmount,
							oldParentId);

				}

			}
		}
	}

	private void updateTotalBalanceOfParentAccount(Statement stat,
			Double amount, Long parentId) throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE ACCOUNT A SET A.TOTAL_BALANCE = A.TOTAL_BALANCE + ")
				.append(amount).append("WHERE A.ID =" + parentId).toString());
	}

	private void updateAllBalancesOfOpeningBalancesAccountWithDifferenceAmount(
			Statement stat, Long companyId, Double newOpeningBalance,
			Double oldOpeningBalance, String symbol) throws SQLException {
		String additionSymbol = null;
		if (symbol.equals(AccounterConstants.SYMBOL_PLUS)) {
			additionSymbol = AccounterConstants.SYMBOL_MINUS;
		} else
			additionSymbol = AccounterConstants.SYMBOL_PLUS;
		stat.execute(new StringBuilder()
				.append("UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(newOpeningBalance)
				.append(additionSymbol).append(oldOpeningBalance)
				.append(",A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(newOpeningBalance).append(additionSymbol)
				.append(oldOpeningBalance).append(" WHERE A.NAME = '")
				.append(AccounterConstants.OPENING_BALANCE).append("'")
				.append(" AND A.COMPANY_ID = ").append(companyId).toString());
	}

	private void updateTotalBalanceOfOpeningBalancesAccount(Statement stat,
			Double newOpeningBalance, Long companyId) throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE ACCOUNT A SET A.TOTAL_BALANCE = A.TOTAL_BALANCE - ")
				.append(newOpeningBalance).append(" WHERE A.NAME = '")
				.append(AccounterConstants.OPENING_BALANCE).append("'")
				.append(" AND A.COMPANY_ID = ").append(companyId).toString());
	}

	private void updateOpeningAndCurrentBalanceOfOpeningBalancesAccount(
			Statement stat, Double newOpeningBalance, Long companyId)
			throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE - ")
				.append(newOpeningBalance).append(" WHERE A.NAME = '")
				.append(AccounterConstants.OPENING_BALANCE).append("'")
				.append(" AND A.COMPANY_ID = ").append(companyId).toString());
	}

	private void updateAllBalancesOfOpeningBalanceAccount(Statement stat,
			Double newOpeningBalance, Long companyId, String symbol)
			throws SQLException {
		stat.execute(new StringBuilder()
				.append("UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(newOpeningBalance)
				.append(",A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(newOpeningBalance)
				.append(" WHERE A.NAME = '")
				.append(AccounterConstants.OPENING_BALANCE).append("'")
				.append(" AND A.COMPANY_ID = ").append(companyId).toString());

	}

	// private void insertRowsInToAccountTransaction(Statement stat, Double
	// newOpeningBalance, Long accountId,String asOfDate) throws SQLException{
	// // inserting the corresponding rows into Account Transaction table
	// stat
	// .execute(new StringBuilder()
	// .append(
	// "INSERT INTO ACCOUNT_TRANSACTION (T_NUMBER,TYPE,USER_ID,T_ID,T_TYPE,T_DATE,AMOUNT) VALUES ")
	// .append(
	// "(null,")
	// .append(AccountTransaction.TYPE_ACCOUNT)
	// .append(",")
	// .append(accountId)
	// .append(",null,null," )
	// .append(asOfDate)
	// .append(newOpeningBalance)
	// .append(")").toString());
	//
	// }

	private String getName(Statement stat, String accountName, Long accountId)
			throws SQLException {
		ResultSet s = stat.executeQuery(new StringBuilder()
				.append("SELECT A.NAME FROM ACCOUNT A WHERE A.ID =")
				.append(accountId).toString());

		if (s.next()) {
			accountName = s.getString(1);
		}
		return accountName;
	}

	@SuppressWarnings("unused")
	private void initializeVariables(Object[] oldRow, Object[] newRow) {

	}

	// Method to check whether a given account is one of Sub account of Opening
	// Balances Account.
	private boolean isItAnySubLevelOfOpeningBalance(Statement stat,
			Long parentId) throws SQLException {

		// Query to get the Parent of the given Account
		Long childParent = null;
		ResultSet rs = stat.executeQuery(new StringBuilder()
				.append("SELECT A.PARENT_ID FROM ACCOUNT A WHERE A.ID = ")
				.append(parentId).toString());

		if (rs.next()) {
			childParent = rs.getLong(1);
		}

		if (childParent != null) {
			if (childParent == 1) {
				return true;
			} else if (childParent == 0) {
				return false;
			} else {

				if (isItAnySubLevelOfOpeningBalance(stat, childParent)) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}

	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// currently not using anywhere in the project.

	}

}
