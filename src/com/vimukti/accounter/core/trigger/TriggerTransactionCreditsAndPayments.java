package com.vimukti.accounter.core.trigger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.api.Trigger;

import com.vimukti.accounter.core.Transaction;

/**
 * 
 * @author Devesh Satwani
 * 
 */
public class TriggerTransactionCreditsAndPayments implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		// Required variables declaration and initialization

		Statement stat = conn.createStatement();

		Double newAmoutToUse = (newRow != null) ? ((newRow[4] != null) ? (Double) newRow[4]
				: null)
				: null;
		@SuppressWarnings("unused")
		Double oldAmoutToUse = (oldRow != null) ? ((oldRow[4] != null) ? (Double) oldRow[4]
				: null)
				: null;

		Long newTransactionReceivePaymentId = (newRow != null) ? ((newRow[5] != null) ? (Long) newRow[5]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldTransactionReceivePaymentId = (oldRow != null) ? ((oldRow[5] != null) ? (Long) oldRow[5]
				: null)
				: null;

		Long newTransactionPayBillId = (newRow != null) ? ((newRow[6] != null) ? (Long) newRow[6]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldTransactionPayBillId = (oldRow != null) ? ((oldRow[6] != null) ? (Long) oldRow[6]
				: null)
				: null;

		Long newCreditsAndPaymentsId = (newRow != null) ? ((newRow[7] != null) ? (Long) newRow[7]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldCreditsAndPaymentsId = (oldRow != null) ? ((oldRow[7] != null) ? (Long) oldRow[7]
				: null)
				: null;

		Boolean newIsVoid = (newRow != null) ? ((newRow[8] != null) ? (Boolean) newRow[8]
				: null)
				: null;
		@SuppressWarnings("unused")
		Boolean oldIsVoid = (oldRow != null) ? ((oldRow[8] != null) ? (Boolean) oldRow[8]
				: null)
				: null;

		Integer newIndex = (newRow != null) ? ((newRow[9] != null) ? (Integer) newRow[9]
				: null)
				: null;
		Integer oldIndex = (oldRow != null) ? ((oldRow[9] != null) ? (Integer) oldRow[9]
				: null)
				: null;

		// Condition for checking whether this Trigger call is for new Row
		// Insertion

		if (newRow != null && oldRow == null) {

		}
		// Condition for checking whether this Trigger call is for Row Updation
		else if (newRow != null && oldRow != null) {

			if (oldIndex == null && newIndex != null) {
				doInCreatePart(stat, newAmoutToUse, newCreditsAndPaymentsId);
			}

			else if (!(oldIndex == null && newIndex != null)) {

				if (newTransactionReceivePaymentId != null) {
					ResultSet r = stat
							.executeQuery(new StringBuilder()
									.append("SELECT TRP.IS_VOID FROM TRANSACTION_RECEIVE_PAYMENT TRP WHERE TRP.ID =")
									.append(newTransactionReceivePaymentId)
									.toString());
					r.next();
					if (r.getBoolean(1) && !newIsVoid) {

						// To update the Amount_to_use to 0 in Transaction
						// Credits
						// and
						// Pyaments for the entries to which the
						// TransactionReceivePayment is match.

						stat.execute(new StringBuilder()
								.append("UPDATE TRANSACTION_CREDITS_AND_PAYMENTS TCP SET TCP.AMOUNT_TO_USE = 0.0, TCP.IS_VOID = 'TRUE' WHERE TCP.TRANSACTION_RECEIVE_PAYMENT_ID = ")
								.append(newTransactionReceivePaymentId)
								.toString());

					}
				} else if (newTransactionPayBillId != null) {
					ResultSet r = stat
							.executeQuery(new StringBuilder()
									.append("SELECT TP.IS_VOID FROM TRANSACTION_PAYBILL TP WHERE TP.ID =")
									.append(newTransactionPayBillId).toString());
					r.next();
					if (r.getBoolean(1) && !newIsVoid) {

						// To update the Amount_to_use to 0 in Transaction
						// Credits
						// and
						// Pyaments for the entries to which the
						// TransactionReceivePayment is match.

						stat.execute(new StringBuilder()
								.append("UPDATE TRANSACTION_CREDITS_AND_PAYMENTS TCP SET TCP.AMOUNT_TO_USE = 0.0, TCP.IS_VOID = 'TRUE' WHERE TCP.TRANSACTION_PAYBILL_ID = ")
								.append(newTransactionPayBillId).toString());

					}
				}

				// ResultSet rs = stat
				// .executeQuery(new StringBuilder()
				// .append(
				// "SELECT RP.IS_VOID,RP.UNUSED_PAYMENTS FROM RECEIVE_PAYMENT RP WHERE RP.ID= (SELECT TR.TRANSACTION_ID FROM TRANSACTION_RECEIVE_PAYMENT TR WHERE TR.ID="
				// )
				// .append(newRow[5]).append(")").toString());
				// rs.next();
				// if (rs.getBoolean(1)) {
				// if (rs.getDouble(2) != 0D) {
				// stat
				// .execute(new StringBuilder()
				// .append(
				// "DELETE FROM CREDITS_AND_PAYMENTS CP WHERE CP.MEMO=(SELECT CONCAT((SELECT T.NUMBER FROM TRANSACTION T WHERE T.ID=(SELECT TR.TRANSACTION_ID FROM TRANSACTION_RECEIVE_PAYMENT TR WHERE TR.ID="
				// )
				// .append(newRow[5])
				// .append(")),'Customer Payment') FROM DUAL)")
				// .toString());
				// }
				// stat
				// .execute(new StringBuilder()
				// .append(
				// "UPDATE CREDITS_AND_PAYMENTS CP SET CP.BALANCE= CP.BALANCE + ")
				// .append(newAmoutToUse).append(" WHERE CP.MEMO='")
				// .append(newRow[3]).append("'").toString());
				// }

			}
		}

	}

	private void doInCreatePart(Statement stat, Double newAmoutToUse,
			Long newCreditsAndPaymentsId) throws SQLException {

		// Updating the balance of the corresponding Credit and Payment by the
		// used amount in any Payment.
		stat.execute(new StringBuilder()
				.append("UPDATE CREDITS_AND_PAYMENTS CP SET CP.BALANCE= CP.BALANCE - ")
				.append(newAmoutToUse).append(" WHERE CP.ID =")
				.append(newCreditsAndPaymentsId).toString());

		// Query to retrieve the balance and the credit amount of the credit and
		// payment used in this payment and the type of transaction .
		ResultSet rs = stat
				.executeQuery(new StringBuilder()
						.append("SELECT CP.BALANCE, CP.CREDIT_AMOUNT,T.T_TYPE FROM CREDITS_AND_PAYMENTS CP JOIN TRANSACTION T ON CP.TRANSACTION_ID = T.ID WHERE CP.ID = ")
						.append(newCreditsAndPaymentsId).toString());
		if (rs.next()) {
			Double balance = rs.getDouble(1);
			Double creditAmount = rs.getDouble(2);
			Integer transactionType = rs.getInt(3);
			// To check the transaction type. If it is either Customer Credit
			// Memo(CCM) or Vendor Credit Memo(VCM) then change the status.
			if (transactionType.equals(Transaction.TYPE_CUSTOMER_CREDIT_MEMO)
					|| transactionType
							.equals(Transaction.TYPE_VENDOR_CREDIT_MEMO)) {

				// To change the status of CCM or VCM as Partially Applied
				if (balance > 0D && balance < creditAmount) {
					stat.execute(new StringBuilder()
							.append("UPDATE TRANSACTION T SET T.STATUS = ")
							.append(Transaction.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED)
							.append(" WHERE T.ID = ")
							.append(" SELECT CP.TRANSACTION_ID FROM CREDITS_AND_PAYMENTS CP WHERE CP.ID = ")
							.append(newCreditsAndPaymentsId).toString());
				}
				// To change the status of CCM or VCM as Applied
				else if (balance == 0D) {
					stat.execute(new StringBuilder()
							.append("UPDATE TRANSACTION T SET T.STATUS = ")
							.append(Transaction.STATUS_PAID_OR_APPLIED_OR_ISSUED)
							.append(" WHERE T.ID = ")
							.append(" SELECT CP.TRANSACTION_ID FROM CREDITS_AND_PAYMENTS CP WHERE CP.ID = ")
							.append(newCreditsAndPaymentsId).toString());
				}
			}
		}

	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// currently not using anywhere in the project.

	}

}
