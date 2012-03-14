package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientJob;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.AccountNameColumn;
import com.vimukti.accounter.web.client.ui.edittable.CustomerColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.DescriptionEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.JobColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionBillableColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionClassColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionDiscountColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionTaxableColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionTotalColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionUnitPriceColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatCodeColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatColumn;

public abstract class VendorAccountTransactionTable extends
		VendorTransactionTable {

	public VendorAccountTransactionTable(boolean enableTax,
			boolean showTaxCode, boolean isTrackClass,
			boolean isClassPerDetailLine, ICurrencyProvider currencyProvider) {
		this(true, enableTax, showTaxCode, isTrackClass, isClassPerDetailLine,
				currencyProvider);
	}

	public VendorAccountTransactionTable(boolean enableTax,
			boolean showTaxCode, boolean enableDisCount, boolean showDiscount,
			boolean isTrackClass, boolean isClassPerDetailLine,
			ICurrencyProvider currencyProvider) {
		super(1, enableDisCount, currencyProvider);
		this.enableTax = enableTax;
		this.showTaxCode = showTaxCode;
		this.enableDisCount = enableDisCount;
		this.showDiscount = showDiscount;
		this.enableClass = isTrackClass;
		this.showClass = isClassPerDetailLine;
		addEmptyRecords();
	}

	public VendorAccountTransactionTable(boolean enableTax,
			boolean showTaxCode, boolean enableDisCount, boolean showDiscount,
			ICurrencyProvider currencyProvider, boolean isCustomerAllowedToAdd,
			boolean isTrackClass, boolean isClassPerDetailLine) {
		super(1, enableDisCount, isCustomerAllowedToAdd, currencyProvider);
		this.enableTax = enableTax;
		this.showTaxCode = showTaxCode;
		this.enableDisCount = enableDisCount;
		this.showDiscount = showDiscount;
		this.enableClass = isTrackClass;
		this.showClass = isClassPerDetailLine;
		addEmptyRecords();
	}

	public VendorAccountTransactionTable(boolean needDiscount,
			boolean enableTax, boolean showTaxCode, boolean isTrackClass,
			boolean isClassPerDetailLine, ICurrencyProvider currencyProvider) {
		super(1, needDiscount, currencyProvider);
		this.enableTax = enableTax;
		this.showTaxCode = showTaxCode;
		this.enableClass = isTrackClass;
		this.showClass = isClassPerDetailLine;
		addEmptyRecords();
	}

	/**
	 * This method will add 4 empty records to the table.
	 */
	@Override
	public void addEmptyRecords() {
		for (int i = 0; i < 4; i++) {
			addEmptyRowAtLast();
		}
	}

	@Override
	public void addEmptyRowAtLast() {
		ClientTransactionItem item = new ClientTransactionItem();
		item.setType(ClientTransactionItem.TYPE_ACCOUNT);
		add(item);
	}

	@Override
	protected void initColumns() {

		AccountNameColumn transactionItemNameColumn = new AccountNameColumn<ClientTransactionItem>() {

			@Override
			public ListFilter<ClientAccount> getAccountsFilter() {
				return new ListFilter<ClientAccount>() {
					@Override
					public boolean filter(ClientAccount account) {

						if (account.getType() != ClientAccount.TYPE_CASH
								&& account.getType() != ClientAccount.TYPE_BANK
								&& account.getType() != ClientAccount.TYPE_CREDIT_CARD
								&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
								&& account.getType() != ClientAccount.TYPE_INCOME
								&& account.getType() != ClientAccount.TYPE_OTHER_INCOME
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
								&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
								&& account.getType() != ClientAccount.TYPE_EQUITY
								&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY) {
							return true;
						} else {
							return false;
						}
					}
				};

			}

			@Override
			protected void setValue(ClientTransactionItem row,
					ClientAccount newValue) {
				row.setAmountIncludeTAX(isShowPriceWithVat());
				updateDiscountValues(row);
				if (newValue != null) {
					row.setAccountable(newValue);
					row.setDescription(newValue.getComment());
					row.setTaxable(true);
					onValueChange(row);
					if (row.getQuantity() == null) {
						ClientQuantity quantity = new ClientQuantity();
						quantity.setValue(1.0);
						row.setQuantity(quantity);
					}
					if (row.getUnitPrice() == null) {
						row.setUnitPrice(new Double(0));
					}
					if (row.getDiscount() == null) {
						row.setDiscount(new Double(0));
					}
					double lt = row.getQuantity().getValue()
							* row.getUnitPrice();
					double disc = row.getDiscount();
					row.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
							* disc / 100))
							: lt);
					getTable().update(row);
				}
				update(row);
			}

			@Override
			public List<Integer> getCanAddedAccountTypes() {
				return Arrays.asList(ClientAccount.TYPE_COST_OF_GOODS_SOLD,
						ClientAccount.TYPE_OTHER_EXPENSE,
						ClientAccount.TYPE_FIXED_ASSET,
						ClientAccount.TYPE_EXPENSE);
			}

			@Override
			protected ClientAccount getValue(ClientTransactionItem row) {
				return (ClientAccount) row.getAccountable();
			}
		};
		this.addColumn(transactionItemNameColumn);

		this.addColumn(new DescriptionEditColumn());

		// this.addColumn(new TransactionQuantityColumn());

		this.addColumn(new TransactionUnitPriceColumn(currencyProvider) {
			@Override
			protected String getColumnName() {
				return getColumnNameWithCurrency(messages.amount());
			}
		});

		if (needDiscount) {
			if (showDiscount) {
				this.addColumn(new TransactionDiscountColumn(currencyProvider));
			}
		}
		if (enableClass) {
			if (showClass) {
				this.addColumn(new TransactionClassColumn<ClientTransactionItem>() {

					@Override
					protected ClientAccounterClass getValue(
							ClientTransactionItem row) {
						return Accounter.getCompany().getAccounterClass(
								row.getAccounterClass());
					}

					@Override
					protected void setValue(ClientTransactionItem row,
							ClientAccounterClass newValue) {
						if (newValue != null) {
							row.setAccounterClass(newValue.getID());
							getTable().update(row);
						}
					}

				});
			}
		}
		this.addColumn(new TransactionTotalColumn(currencyProvider, true));

		if (enableTax) {
			if (showTaxCode) {
				this.addColumn(new TransactionVatCodeColumn() {

					@Override
					protected ListFilter<ClientTAXCode> getTaxCodeFilter() {
						return new ListFilter<ClientTAXCode>() {

							@Override
							public boolean filter(ClientTAXCode e) {
								if (!e.isTaxable()
										|| e.getTAXItemGrpForPurchases() != 0) {
									return true;
								}
								return false;
							}
						};
					}

					@Override
					protected void setValue(ClientTransactionItem row,
							ClientTAXCode newValue) {
						super.setValue(row, newValue);
						update(row);
					}

					@Override
					protected boolean isSales() {
						return false;
					}
				});

				this.addColumn(new TransactionVatColumn(currencyProvider));
			} else {
				this.addColumn(new TransactionTaxableColumn());
			}
		}
		final JobColumn<ClientTransactionItem> jobColumn = new JobColumn<ClientTransactionItem>() {

			@Override
			protected ClientJob getValue(ClientTransactionItem row) {
				return Accounter.getCompany().getjob(row.getJob());
			}

			@Override
			protected void setValue(ClientTransactionItem row,
					ClientJob newValue) {
				if (newValue == null) {
					return;
				}
				row.setJob(newValue.getID());
			}

		};
		if (isCustomerAllowedToAdd) {
			this.addColumn(new CustomerColumn<ClientTransactionItem>() {

				@Override
				protected ClientCustomer getValue(ClientTransactionItem row) {
					return Accounter.getCompany()
							.getCustomer(row.getCustomer());
				}

				@Override
				protected void setValue(ClientTransactionItem row,
						ClientCustomer newValue) {
					if (newValue == null) {
						return;
					}
					row.setCustomer(newValue.getID());
					jobColumn.setcustomerId(newValue.getID());
				}

			});
			if (isTrackJob()) {
				this.addColumn(jobColumn);
			}
			this.addColumn(new TransactionBillableColumn());
		}

		this.addColumn(new DeleteColumn<ClientTransactionItem>());
	}

	protected boolean isTrackJob() {
		return false;
	}

	@Override
	public void updateAmountsFromGUI() {
		super.updateAmountsFromGUI();
	}
}
