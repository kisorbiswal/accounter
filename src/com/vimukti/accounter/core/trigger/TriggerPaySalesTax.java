package com.vimukti.accounter.core.trigger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.h2.api.Trigger;

import com.vimukti.accounter.core.AccounterConstants;
import com.vimukti.accounter.core.Transaction;

public class TriggerPaySalesTax implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization
		Statement stat = conn.createStatement();

		Long newPaySalesTaxId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;

		
		Long oldPaySalesTaxId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;

		
		Long newPaymentMethodId = (newRow != null) ? ((newRow[2] != null) ? (Long) newRow[2]
				: null)
				: null;

		
		Long oldPaymentMethodId = (oldRow != null) ? ((oldRow[2] != null) ? (Long) oldRow[2]
				: null)
				: null;

		
		Long newPayFromAccountId = (newRow != null) ? ((newRow[3] != null) ? (Long) newRow[3]
				: null)
				: null;

		Long oldPayFromAccountId = (oldRow != null) ? ((oldRow[3] != null) ? (Long) oldRow[3]
				: null)
				: null;

		
		Long newTaxAgencyId = (newRow != null) ? ((newRow[4] != null) ? (Long) newRow[4]
				: null)
				: null;

		
		Long oldTaxAgencyId = (oldRow != null) ? ((oldRow[4] != null) ? (Long) oldRow[4]
				: null)
				: null;

		
		Date newBillsDueOnOrBeforeDate = (newRow != null) ? ((newRow[1] != null) ? (Date) newRow[1]
				: null)
				: null;

		
		Date oldBillsDueOnOrBeforeDate = (oldRow != null) ? ((oldRow[1] != null) ? (Date) oldRow[1]
				: null)
				: null;

		
		Double newTotal = (newRow != null) ? ((newRow[7] != null) ? (Double) newRow[7]
				: null)
				: null;

		Double oldTotal = (oldRow != null) ? ((oldRow[7] != null) ? (Double) oldRow[7]
				: null)
				: null;

		
		Double newAmount = (newRow != null) ? ((newRow[5] != null) ? (Double) newRow[5]
				: null)
				: null;

		
		Double oldAmount = (oldRow != null) ? ((oldRow[5] != null) ? (Double) oldRow[5]
				: null)
				: null;

		
		Double newEndingbalance = (newRow != null) ? ((newRow[6] != null) ? (Double) newRow[6]
				: null)
				: null;

		
		Double oldEndingbalance = (oldRow != null) ? ((oldRow[6] != null) ? (Double) oldRow[6]
				: null)
				: null;

		Boolean newIsVoid = (newRow != null) ? ((newRow[8] != null) ? (Boolean) newRow[8]
				: null)
				: null;

		Boolean oldIsVoid = (oldRow != null) ? ((oldRow[8] != null) ? (Boolean) oldRow[8]
				: null)
				: null;

		
		Boolean newIsEdited = (newRow != null) ? ((newRow[9] != null) ? (Boolean) newRow[9]
				: null)
				: null;

		
		Boolean oldIsEdited = (oldRow != null) ? ((oldRow[9] != null) ? (Boolean) oldRow[9]
				: null)
				: null;

		boolean isIncrease;
		if (newRow != null && oldRow == null) {
		}
		// Condition for checking whether this Trigger call is for Row Updation
		else if (newRow != null && oldRow != null) {

			// To check whether this updation is caused by Voiding any Cash Sale
			// or not.

			if (oldIsVoid != newIsVoid) {

				// Query for deleting corresponding transaction rows from
				// AccountTransaction table
				stat.execute(new StringBuilder()
						.append("DELETE FROM ACCOUNT_TRANSACTION WHERE T_ID =")
						.append(newPaySalesTaxId).toString());

				// Query to retrieve the type of the Deposit In Account
				isIncrease = getAccountIsIncrease(stat, oldPayFromAccountId);

				if (isIncrease) {

					// Decreasing the Current and the total balances of the
					// corresponding Payfrom account
					updateCurrentAndTotalBalancesOfPayFromAccount(stat,
							oldPayFromAccountId, oldTotal,
							AccounterConstants.SYMBOL_MINUS);

				} else {
					// Increasing the Current and the total balances of the
					// corresponding Payfrom account
					updateCurrentAndTotalBalancesOfPayFromAccount(stat,
							oldPayFromAccountId, oldTotal,
							AccounterConstants.SYMBOL_PLUS);

				}

			}

		}

	}

	
	private void updateStatusOfNewPaySalesTax(Statement stat,
			Long newPaymentMethodId, Long newPaySalesTaxId) throws SQLException {

		Integer paymentMethodType = 0;
		ResultSet rs = stat.executeQuery(new StringBuilder()
				.append("SELECT PM.TYPE FROM PAYMENTMETHOD PM WHERE PM.ID = ")
				.append(newPaymentMethodId).toString());
		if (rs.next()) {
			paymentMethodType = rs.getInt(1);
		}
		if ((paymentMethodType).equals(AccounterConstants.PAYMENT_METHOD_CHECK)) {
			stat.execute(new StringBuilder()
					.append("UPDATE TRANSACTION T SET T.STATUS = ")
					.append(Transaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
					.append(" WHERE T.ID = ").append(newPaySalesTaxId)
					.toString());
		} else {
			stat.execute(new StringBuilder()
					.append("UPDATE TRANSACTION T SET T.STATUS = ")
					.append(Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED)
					.append(" WHERE T.ID = ").append(newPaySalesTaxId)
					.toString());

		}
	}

	private void updateCurrentAndTotalBalancesOfPayFromAccount(Statement stat,
			Long payFromAccountId, Double total, String symbol)
			throws SQLException {

		stat.execute(new StringBuilder()
				.append("UPDATE ACCOUNT A SET A.CURRENT_BALANCE = A.CURRENT_BALANCE ")
				.append(symbol).append(" ").append(total)
				.append(", A.TOTAL_BALANCE = A.TOTAL_BALANCE ").append(symbol)
				.append(" ").append(total).append(" WHERE A.ID =")
				.append(payFromAccountId).toString());
	}

	private boolean getAccountIsIncrease(Statement stat, Long depositInAccountId)
			throws SQLException {
		boolean accountType = false;

		ResultSet r = stat.executeQuery(new StringBuilder()
				.append("SELECT A.IS_INCREASE FROM ACCOUNT A WHERE A.ID =")
				.append(depositInAccountId).toString());

		if (r.next()) {
			accountType = r.getBoolean(1);
		}
		return accountType;
	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// currently not using anywhere in the project.

	}

}
