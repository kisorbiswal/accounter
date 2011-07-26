package com.vimukti.accounter.core.trigger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.api.Trigger;

/**
 * 
 * @author Devesh Satwani
 * 
 */
public class TriggerEntry implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		@SuppressWarnings("unused")
		Statement stat = conn.createStatement();

		@SuppressWarnings("unused")
		Long newEntryId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldEntryId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;

		@SuppressWarnings("unused")
		Integer newType = (newRow != null) ? ((newRow[3] != null) ? (Integer) newRow[3]
				: null)
				: null;
		@SuppressWarnings("unused")
		Integer oldType = (oldRow != null) ? ((oldRow[3] != null) ? (Integer) oldRow[3]
				: null)
				: null;

		@SuppressWarnings("unused")
		Long newAccountId = (newRow != null) ? ((newRow[4] != null) ? (Long) newRow[4]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldAccountId = (oldRow != null) ? ((oldRow[4] != null) ? (Long) oldRow[4]
				: null)
				: null;

		// Clob newMemo = (newRow != null)?((newRow[11] != null)?(Clob)
		// newRow[11]:null):null;
		// Clob oldMemo = (oldRow != null)?((oldRow[11] != null)?(Clob)
		// oldRow[11]:null):null;

		@SuppressWarnings("unused")
		Double newDebitAmount = (newRow != null) ? ((newRow[9] != null) ? (Double) newRow[9]
				: null)
				: null;
		@SuppressWarnings("unused")
		Double oldDebitAmount = (oldRow != null) ? ((oldRow[9] != null) ? (Double) oldRow[9]
				: null)
				: null;

		@SuppressWarnings("unused")
		Double newCreditAmount = (newRow != null) ? ((newRow[10] != null) ? (Double) newRow[10]
				: null)
				: null;
		@SuppressWarnings("unused")
		Double oldCreditAmount = (oldRow != null) ? ((oldRow[10] != null) ? (Double) oldRow[10]
				: null)
				: null;

		@SuppressWarnings("unused")
		Long newTransactionId = (newRow != null) ? ((newRow[12] != null) ? (Long) newRow[12]
				: null)
				: null;
		@SuppressWarnings("unused")
		Long oldTransactionId = (oldRow != null) ? ((oldRow[12] != null) ? (Long) oldRow[12]
				: null)
				: null;

		Integer newIndex = (newRow != null) ? ((newRow[13] != null) ? (Integer) newRow[13]
				: null)
				: null;
		Integer oldIndex = (oldRow != null) ? ((oldRow[13] != null) ? (Integer) oldRow[13]
				: null)
				: null;

		// String newMemo = null;
		// String oldMemo = null;
		// try {
		// if(newRow[11] != null){
		// BufferedReader br = (BufferedReader)newRow[11];
		// char c[] = new char[1024];
		// int i=0;
		// while((i=br.read()) > 0 ){
		// newMemo += c.toString();
		// }
		// }
		// } catch (IOException e) {
		// // Auto-generated catch block
		// e.printStackTrace();
		// }

		if (newRow != null && oldRow == null) {
			// if(newTransactionId != null){
			// doInCreatePart(stat, newAccountId, newTransactionId,
			// newDebitAmount, newCreditAmount, newMemo, newType);
			// }

		} else if (newRow != null && oldRow != null) {

			// This Trigger Updation call is for Updation TransactionID and
			// Index. Hence do the create part
			if (oldIndex == null && newIndex != null) {
				// doInCreatePart(stat, newAccountId, newTransactionId,
				// newDebitAmount, newCreditAmount, newRow[11], newType);
			}
		}

	}

	// Its not using any where
	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
	}

}
