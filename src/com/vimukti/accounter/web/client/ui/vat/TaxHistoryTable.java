package com.vimukti.accounter.web.client.ui.vat;

import java.util.Collections;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientVATReturn;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.SelectionChangedHandler;
import com.vimukti.accounter.web.client.ui.grids.columns.CheckBoxColumn;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class TaxHistoryTable extends CellTable<ClientVATReturn> {
	private ListDataProvider<ClientVATReturn> dataprovider = new ListDataProvider<ClientVATReturn>();
	private SelectionChangedHandler<ClientVATReturn> callBack;

	TaxHistoryTable(SelectionChangedHandler<ClientVATReturn> callBack) {
		initColumns();
		this.callBack = callBack;
		HTML emptyMessage = new HTML(Accounter.constants().taxHistoryEmpty());
		emptyMessage.setHeight("150px");
		setEmptyTableWidget(emptyMessage);
	}

	private void initColumns() {

		dataprovider.addDataDisplay(this);

		CheckBoxColumn<ClientVATReturn> check = new CheckBoxColumn<ClientVATReturn>() {

			@Override
			public void update(int arg0, ClientVATReturn arg1, Boolean arg2) {

				callBack.selectionChanged(arg1, arg2);
			}

			@Override
			public Boolean getValue(ClientVATReturn arg0) {
				return false;
			}
		};

		TextColumn<ClientVATReturn> periodStartDate = new TextColumn<ClientVATReturn>() {

			@Override
			public String getValue(ClientVATReturn object) {

				return new ClientFinanceDate(object.getVATperiodStartDate())
						.toString();
			}
		};

		TextColumn<ClientVATReturn> periodEndDate = new TextColumn<ClientVATReturn>() {

			@Override
			public String getValue(ClientVATReturn object) {

				return new ClientFinanceDate(object.getVATperiodEndDate())
						.toString();
			}
		};
		TextColumn<ClientVATReturn> vatFileDate = new TextColumn<ClientVATReturn>() {

			@Override
			public String getValue(ClientVATReturn object) {

				return null;
			}
		};
		TextColumn<ClientVATReturn> netAmountDue = new TextColumn<ClientVATReturn>() {

			@Override
			public String getValue(ClientVATReturn object) {

				return String.valueOf(object.getBalance());

			}
		};

		TextColumn<ClientVATReturn> totalPaymentMade = new TextColumn<ClientVATReturn>() {

			@Override
			public String getValue(ClientVATReturn object) {

				return String.valueOf(object.getNetAmount());
			}
		};

		this.addColumn(check, Accounter.constants().select());
		this.addColumn(periodStartDate, Accounter.constants().periodStartDate());
		this.addColumn(periodEndDate, Accounter.constants().periodEndDate());
		this.addColumn(vatFileDate, Accounter.constants().vatFileDate());
		this.addColumn(netAmountDue, Accounter.constants().netAmountDue());
		this.addColumn(totalPaymentMade, Accounter.constants()
				.totalPaymentMade());

	}

	public void setData(List<ClientVATReturn> data) {
		if (data == null) {
			data = Collections.emptyList();
		}
		this.dataprovider.setList(data);

	}

	public List<ClientVATReturn> getData() {
		return this.dataprovider.getList();
	}

}
