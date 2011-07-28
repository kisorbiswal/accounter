package com.vimukti.accounter.core.trigger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.h2.api.Trigger;

import com.vimukti.accounter.core.FiscalYear;

/**
 * 
 * @author Devesh Satwani
 * 
 */
public class TriggerFiscalYear implements Trigger {

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow)
			throws SQLException {

		
		Long newFiscalYearId = (newRow != null) ? ((newRow[0] != null) ? (Long) newRow[0]
				: null)
				: null;
		
		Long oldFiscalYearId = (oldRow != null) ? ((oldRow[0] != null) ? (Long) oldRow[0]
				: null)
				: null;

		
		Long newCompanyId = (newRow != null) ? ((newRow[1] != null) ? (Long) newRow[1]
				: null)
				: null;
		
		Long oldCompanyId = (oldRow != null) ? ((oldRow[1] != null) ? (Long) oldRow[1]
				: null)
				: null;

		
		Date newStartDate = (newRow != null) ? ((newRow[2] != null) ? (Date) newRow[2]
				: null)
				: null;
		
		Date oldStartDate = (oldRow != null) ? ((oldRow[2] != null) ? (Date) oldRow[2]
				: null)
				: null;

		
		Date newEndDate = (newRow != null) ? ((newRow[3] != null) ? (Date) newRow[3]
				: null)
				: null;
		
		Date oldEndDate = (oldRow != null) ? ((oldRow[3] != null) ? (Date) oldRow[3]
				: null)
				: null;

		Integer newStatus = (newRow != null) ? ((newRow[4] != null) ? (Integer) newRow[4]
				: null)
				: null;
		Integer oldStatus = (oldRow != null) ? ((oldRow[4] != null) ? (Integer) oldRow[4]
				: null)
				: null;

		
		Boolean newIsCurrentFiscalYear = (newRow != null) ? ((newRow[5] != null) ? (Boolean) newRow[5]
				: null)
				: null;
		
		Boolean oldIsCurrentFiscalYear = (oldRow != null) ? ((oldRow[5] != null) ? (Boolean) oldRow[5]
				: null)
				: null;

		
		Date newPreviousStartDate = (newRow != null) ? ((newRow[6] != null) ? (Date) newRow[6]
				: null)
				: null;
		
		Date oldPreviousStartDate = (oldRow != null) ? ((oldRow[6] != null) ? (Date) oldRow[6]
				: null)
				: null;

		
		Statement stat = conn.createStatement();
		// Condition for checking whether this Trigger call is for new Row
		// Insertion
		if (newRow != null && oldRow == null) {

		}
		// Condition for checking whether this Trigger call is for Row Updation
		else if (newRow != null && oldRow != null) {
			if (oldStatus == FiscalYear.STATUS_OPEN
					&& newStatus == FiscalYear.STATUS_CLOSE) {

				// stat.execute(new
				// StringBuilder().append("UPDATE FISCAL_YEAR SET  IS_CURRENT_FISCAL_YEAR = 'TRUE' WHERE ID = SELECT F.ID FROM FISCAL_YEAR F ").append(newFiscalYearId).toString());
				//
				//
				// stat.execute(new
				// StringBuilder().append("UPDATE FISCAL_YEAR SET  IS_CURRENT_FISCAL_YEAR = 'FALSE' WHERE ID = ").append(newFiscalYearId).toString());
				//

			} else if (oldStatus == FiscalYear.STATUS_CLOSE
					&& newStatus == FiscalYear.STATUS_OPEN) {

			}
		}

	}

	@Override
	public void init(Connection arg0, String arg1, String arg2, String arg3,
			boolean arg4, int arg5) throws SQLException {
		// currently not using anywhere in the project.

	}

}
