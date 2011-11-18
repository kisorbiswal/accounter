package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.Accounter;

public class TransactionItemsTable extends EditTable<ClientTransactionItem> {

	public TransactionItemsTable() {
		addStyleName("transactionitemsTable");
	}

	@Override
	protected void initColumns() {
		TextEditColumn<ClientTransactionItem> textEditColumn = new TextEditColumn<ClientTransactionItem>() {

			@Override
			protected String getValue(ClientTransactionItem row) {
				String name = "";
				ClientItem item = Accounter.getCompany().getItem(row.getItem());
				if (item == null) {
					ClientAccount account = Accounter.getCompany().getAccount(
							row.getAccount());
					name = account.getName();
				} else {
					name = item.getName();
				}
				return name;
			}

			@Override
			protected void setValue(ClientTransactionItem row, String value) {

			}

			@Override
			protected String getColumnName() {
				return Accounter.messages().name();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};
		this.addColumn(textEditColumn);

		DescriptionEditColumn textEditColumn2 = new DescriptionEditColumn() {
			@Override
			protected String getValue(ClientTransactionItem row) {
				if (row.getDescription() == null
						|| row.getDescription().isEmpty()) {
					return Accounter.messages().nodescription();
				}
				return row.getDescription();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};
		this.addColumn(textEditColumn2);

		this.addColumn(new TextEditColumn<ClientTransactionItem>() {

			@Override
			protected String getValue(ClientTransactionItem row) {
				String name = "";
				ClientTAXCode taxCode = Accounter.getCompany().getTAXCode(
						row.getTaxCode());
				if (taxCode != null) {
					name = taxCode.getName() + "  " + taxCode.getSalesTaxRate();
				}
				return name;
			}

			@Override
			protected void setValue(ClientTransactionItem row, String value) {

			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			protected String getColumnName() {
				return Accounter.messages().vatCode();
			}
		});

		TextEditColumn<ClientTransactionItem> textEditColumn3 = new TextEditColumn<ClientTransactionItem>() {

			@Override
			protected String getValue(ClientTransactionItem row) {
				return String.valueOf(row.getLineTotal());
			}

			@Override
			protected void setValue(ClientTransactionItem row, String value) {
			}

			@Override
			protected String getColumnName() {
				return Accounter.messages().lineTotal();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		};
		this.addColumn(textEditColumn3);
	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}
}
