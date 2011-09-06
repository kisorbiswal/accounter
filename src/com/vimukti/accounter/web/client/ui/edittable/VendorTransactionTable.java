package com.vimukti.accounter.web.client.ui.edittable;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;

public abstract class VendorTransactionTable extends
		EditTable<ClientTransactionItem> {
	public VendorTransactionTable() {
		initTable();
	}

	private void initTable() {
		this.addColumn(new ImageEditColumn<ClientTransactionItem>() {

			@Override
			public ImageResource getResource(ClientTransactionItem row) {
				return VendorTransactionTable.this.getImage(row);
			}

			@Override
			public int getWidth() {
				return 20;
			}

		});

		this.addColumn(new TransactionItemNameColumn() {

			@Override
			public ListFilter<ClientAccount> getAccountsFilter() {
				return new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						if (Accounter.getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK) {
							if (Arrays.asList(
									ClientAccount.TYPE_BANK,
									// ClientAccount.TYPE_CASH,
									ClientAccount.TYPE_CREDIT_CARD,
									ClientAccount.TYPE_OTHER_CURRENT_ASSET,
									ClientAccount.TYPE_FIXED_ASSET).contains(
									account.getType())) {
								return true;
							}
						} else {
							if (Arrays.asList(ClientAccount.TYPE_CREDIT_CARD,
									ClientAccount.TYPE_OTHER_CURRENT_ASSET,
									ClientAccount.TYPE_BANK,
									ClientAccount.TYPE_FIXED_ASSET).contains(
									account.getType())) {

								return true;
							}
						}
						return false;
					}
				};
			}

		});

		this.addColumn(new DescriptionEditColumn());

		this.addColumn(new TransactionQuantityColumn());

		this.addColumn(new TransactionUnitPriceColumn());

		this.addColumn(new TransactionDiscountColumn());

		this.addColumn(new TransactionTotalColumn());

		if (getCompany().getAccountingType() == ClientCompany.ACCOUNTING_TYPE_UK
				&& getCompany().getPreferences().getDoYouPaySalesTax()) {

			this.addColumn(new TransactionVatCodeColumn());

			this.addColumn(new TransactionVatColumn());
		}

		this.addColumn(new DeleteColumn<ClientTransactionItem>() {

			@Override
			protected void onDelete(ClientTransactionItem row) {
				delete(row);
			}
		});
	}

	private ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	private ImageResource getImage(ClientTransactionItem row) {
		switch (row.getType()) {
		case ClientTransactionItem.TYPE_ITEM:
			return Accounter.getFinanceImages().itemsIcon();
		case ClientTransactionItem.TYPE_SERVICE:
			return Accounter.getFinanceImages().salesTaxIcon();
		case ClientTransactionItem.TYPE_ACCOUNT:
			return Accounter.getFinanceImages().AccountsIcon();
		case ClientTransactionItem.TYPE_COMMENT:
			return Accounter.getFinanceImages().CommentsIcon();
		case ClientTransactionItem.TYPE_SALESTAX:
			return Accounter.getFinanceImages().salesTaxIcon();
		default:
			break;
		}
		return Accounter.getFinanceImages().errorImage();
	}

	public void removeAllRecords() {
		clear();
	}

	public void setRecords(List<ClientTransactionItem> transactionItems) {
		setAllRows(transactionItems);
	}

	public void updateTotals() {
		// TODO Auto-generated method stub

	}

	public List<ClientTransactionItem> getRecords() {
		return getAllRows();
	}

	public void setVendorTaxCode(ClientTransactionItem item) {
		// TODO Auto-generated method stub

	}

	public double getTotal() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Double getGrandTotal() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addRecords(List<ClientTransactionItem> transactionItems) {
		for (ClientTransactionItem item : transactionItems) {
			add(item);
		}
	}

	public ValidationResult validateGrid() {
		// TODO Auto-generated method stub
		return null;
	}
}
