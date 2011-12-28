package com.vimukti.accounter.web.client.ui.grids;

import java.util.List;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTDSChalanDetail;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.reports.ReportsRPC;
import com.vimukti.accounter.web.client.util.DayAndMonthUtil;

public class ChalanDetailsListGrid extends BaseListGrid<ClientTDSChalanDetail> {

	public ChalanDetailsListGrid() {
		super(false, true);
	}

	public void initWithItems(List<ClientTDSChalanDetail> allItems) {
		this.setRecords(allItems);

	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected void executeDelete(ClientTDSChalanDetail object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(ClientTDSChalanDetail obj, int index) {
		switch (index) {
		case 0: {
			if (obj.getFormType() == 1) {
				return "Form26Q";
			} else if (obj.getFormType() == 2) {
				return "Form27Q";
			} else if (obj.getFormType() == 3) {
				return "Form27EQ";
			} else {
				return "Form26Q";
			}
		}
		case 1: {
			String assessmentYear = Integer.toString(obj
					.getAssesmentYearStart())
					+ "-"
					+ Integer.toString(obj.getAssessmentYearEnd());
			return assessmentYear;
		}
		case 2:
			return obj.getIncomeTaxAmount() + obj.getSurchangePaidAmount()
					+ obj.getEducationCessAmount()
					+ obj.getInterestPaidAmount() + obj.getPenaltyPaidAmount()
					+ obj.getOtherAmount();
		case 3: {

			if (obj.getChalanPeriod() == 1) {
				return "Q1" + " " + DayAndMonthUtil.apr() + " - "
						+ DayAndMonthUtil.jun();
			} else if (obj.getChalanPeriod() == 2) {
				return "Q2" + " " + DayAndMonthUtil.jul() + " - "
						+ DayAndMonthUtil.sep();
			} else if (obj.getChalanPeriod() == 3) {
				return "Q3" + " " + DayAndMonthUtil.oct() + " - "
						+ DayAndMonthUtil.dec();
			} else if (obj.getChalanPeriod() == 4) {
				return "Q4" + " " + DayAndMonthUtil.jan() + " - "
						+ DayAndMonthUtil.mar();
			} else {
				return "Q4" + " " + DayAndMonthUtil.jan() + " - "
						+ DayAndMonthUtil.mar();
			}
		}
		case 4:
			return new ClientFinanceDate(obj.getDateTaxPaid());
		case 5:
			return obj.getBsrCode();
		default:
			break;
		}

		return null;
	}

	@Override
	public void onDoubleClick(ClientTDSChalanDetail obj) {
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(obj.getType(), obj.getID());
	}

	@Override
	protected String[] getColumns() {
		String[] colArray = new String[6];
		for (int index = 0; index < colArray.length; index++) {
			switch (index) {
			case 0:
				colArray[index] = "Form Type";
				break;
			case 1:
				colArray[index] = "Assessment Year";
				break;
			case 2:
				colArray[index] = messages.totalAmount();
				break;
			case 3:
				colArray[index] = "Chalan Period";
				break;
			case 4:
				colArray[index] = "Date of Payment";
				break;
			case 5:
				colArray[index] = "Bank BSR Code";
				break;
			default:
				break;
			}
		}
		return colArray;
	}

}
