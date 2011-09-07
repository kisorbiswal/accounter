package com.vimukti.accounter.web.client.ui.edittable.tables;

import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransactionPayBill;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.edittable.AnchorEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public abstract class TransactionPayBillTable extends
		EditTable<ClientTransactionPayBill> {
	private boolean canEdit;
	private ClientVendor vendor;

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
				protected boolean isEnable() {
					return false;
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
			protected boolean isEnable() {
				return false;
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
					return DataUtils.getAmountAsString(row.getOriginalAmount());
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
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return Accounter.constants().originalAmount();
				}
			});

			this.addColumn(new TextEditColumn<ClientTransactionPayBill>() {

				@Override
				protected String getValue(ClientTransactionPayBill row) {
					return DataUtils.getAmountAsString(row.getAmountDue());
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
				protected boolean isEnable() {
					return false;
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
					return DataUtils.getAmountAsString(row.getOriginalAmount());
				}

				@Override
				protected void setValue(ClientTransactionPayBill row,
						String value) {
					// No Need
				}

				@Override
				protected boolean isEnable() {
					return false;
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
			protected boolean isEnable() {
				return false;
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

		this.addColumn(new AnchorEditColumn<ClientTransactionPayBill>() {

			@Override
			protected void onClick(ClientTransactionPayBill row) {
				// TODO Auto-generated method stub

			}

			@Override
			protected String getValue(ClientTransactionPayBill row) {
				return DataUtils.getAmountAsString(row.getCashDiscount());
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

		if (!canEdit) {
			addTdsColumn();
		}
		if (canEdit) {
			this.addColumn(new AnchorEditColumn<ClientTransactionPayBill>() {

				@Override
				protected void onClick(ClientTransactionPayBill row) {
					// TODO Auto-generated method stub

				}

				@Override
				protected String getValue(ClientTransactionPayBill row) {
					return DataUtils.getAmountAsString(row.getAppliedCredits());
				}

				@Override
				protected String getColumnName() {
					return Accounter.constants().credits();
				}
			});

			if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_INDIA) {
				addTdsColumn();
			}

			this.addColumn(new TextEditColumn<ClientTransactionPayBill>() {

				@Override
				protected String getValue(ClientTransactionPayBill row) {
					ClientTAXItem taxItem = Accounter.getCompany().getTAXItem(
							vendor.getTaxItemCode());
					if (row.getPayment() != 0)
						return DataUtils.getAmountAsString(row.getPayment()
								- row.getOriginalAmount()
								* (taxItem.getTaxRate()) / 100);
					else
						return DataUtils.getAmountAsString(0.0);
				}

				@Override
				protected void setValue(ClientTransactionPayBill row,
						String value) {
					double payment = Double.parseDouble(DataUtils
							.getReformatedAmount(value.toString()) + "");
					row.setPayment(payment);
					updateAmountDue(row);
					adjustAmountAndEndingBalance();
					updateFootervalues(row, canEdit);
					update(row);
				}

				@Override
				protected String getColumnName() {
					return Accounter.constants().payments();
				}
			});
		}
		
		// if(!canEdit){
		// vendorConstants.referenceNo(),
		// vendorConstants.amountPaid() };
		// }

	}

	protected abstract void updateFootervalues(ClientTransactionPayBill row,
			boolean canEdit);

	protected abstract void adjustAmountAndEndingBalance();

	private void addTdsColumn() {
		this.addColumn(new TextEditColumn<ClientTransactionPayBill>() {

			@Override
			protected String getValue(ClientTransactionPayBill row) {
				ClientTAXItem taxItem = Accounter.getCompany().getTAXItem(
						vendor.getTaxItemCode());
				if (taxItem != null)
					return DataUtils.getAmountAsString(row.getOriginalAmount()
							* (taxItem.getTaxRate() / 100));
				else
					return DataUtils.getAmountAsString(0.0);
			}

			@Override
			protected void setValue(ClientTransactionPayBill row, String value) {
				// No Need

			}

			@Override
			protected String getColumnName() {
				return Accounter.constants().tds();
			}
		});
	}

	public void updateAmountDue(ClientTransactionPayBill item) {
		double totalValue = item.getCashDiscount() + item.getAppliedCredits()
				+ item.getPayment();

		if (!DecimalUtil.isGreaterThan(totalValue, item.getAmountDue())) {
			item.setDummyDue(item.getAmountDue() - totalValue);
		} else {
			item.setDummyDue(0.0);
		}
	}

	public void setVendor(ClientVendor vendor) {
		this.vendor = vendor;
	}
}
