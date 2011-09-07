package com.vimukti.accounter.web.client.ui.edittable.tables;

import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public class TransactionPayBillTable extends
		EditTable<ClientTransactionPayBill> {
	private boolean canEdit;

	public TransactionPayBillTable() {
		initColumn();
	}

	private void initColumn() {
		if (canEdit) {
			TextEditColumn<ClientTransactionPayBill> dueDate = new TextEditColumn<ClientTransactionPayBill>() {

				@Override
				protected String getValue(ClientTransactionPayBill row) {
					return "";// new FinanceDate(row.getDueDate()).toString();
				}

				@Override
				protected void setValue(ClientTransactionPayBill row,
						String value) {
					// No need
				}

				@Override
				public int getWidth() {
					return 40;
				}

				@Override
				protected String getColumnName() {
					return Accounter.constants().dueDate();
				}
			};
			this.addColumn(dueDate);
		}

		TextEditColumn<ClientTransactionPayBill> billNo = new TextEditColumn<ClientTransactionPayBill>() {

			@Override
			protected String getValue(ClientTransactionPayBill row) {
				return row.getBillNumber();
			}

			@Override
			protected void setValue(ClientTransactionPayBill row, String value) {
				// No Need
			}

			@Override
			public int getWidth() {
				return 40;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().billNo();
			}
		};
		this.addColumn(billNo);

		if (canEdit) {
			this.addColumn(new TextEditColumn<ClientTransactionPayBill>() {

				@Override
				protected String getValue(ClientTransactionPayBill row) {
					return String.valueOf(row.getOriginalAmount());
				}

				@Override
				protected void setValue(ClientTransactionPayBill row,
						String value) {
					// No Need
				}

				@Override
				public int getWidth() {
					return 40;
				}

				@Override
				protected String getColumnName() {
					return Accounter.constants().originalAmount();
				}
			});

			this.addColumn(new TextEditColumn<ClientTransactionPayBill>() {

				@Override
				protected String getValue(ClientTransactionPayBill row) {
					return String.valueOf(row.getAmountDue());
				}

				@Override
				protected void setValue(ClientTransactionPayBill row,
						String value) {
					// No Need
				}

				@Override
				public int getWidth() {
					return 40;
				}

				@Override
				protected String getColumnName() {
					return Accounter.constants().amountDue();
				}
			});
		} else {
			this.addColumn(new TextEditColumn<ClientTransactionPayBill>() {

				@Override
				protected String getValue(ClientTransactionPayBill row) {
					return String.valueOf(row.getOriginalAmount());
				}

				@Override
				protected void setValue(ClientTransactionPayBill row,
						String value) {
					// No Need
				}

				@Override
				public int getWidth() {
					return 40;
				}

				@Override
				protected String getColumnName() {
					return Accounter.constants().billAmount();
				}
			});
		}

		this.addColumn(new TextEditColumn<ClientTransactionPayBill>() {

			@Override
			protected String getValue(ClientTransactionPayBill row) {
				return "";// new FinanceDate(row.getDiscountDate()).toString();
			}

			@Override
			protected void setValue(ClientTransactionPayBill row, String value) {
				// No Need

			}

			@Override
			public int getWidth() {
				return 40;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().discountDate();
			}
		});

		this.addColumn(new TextEditColumn<ClientTransactionPayBill>() {

			@Override
			protected String getValue(ClientTransactionPayBill row) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected void setValue(ClientTransactionPayBill row, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			public int getWidth() {
				return 40;
			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().cashDiscount();
			}
		});
		// vendorConstants.cashDiscount(),
		// if(!canEdit){
		// vendorConstants.tds(),
		// }
		// if(canEdit){
		// vendorConstants.credits(),
		// if(INDIA){
		// vendorConstants.tds(),
		// }
		// vendorConstants.payments()
		// }
		// if(!canEdit){
		// vendorConstants.referenceNo(),
		// vendorConstants.amountPaid() };
		// }

	}
}
