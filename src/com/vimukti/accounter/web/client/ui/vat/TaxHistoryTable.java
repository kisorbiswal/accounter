package com.vimukti.accounter.web.client.ui.vat;

import java.util.Collections;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.core.ClientAbstractTAXReturn;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.SelectionChangedHandler;
import com.vimukti.accounter.web.client.ui.grids.columns.CheckBoxColumn;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class TaxHistoryTable extends CellTable<ClientAbstractTAXReturn> {
	private ListDataProvider<ClientAbstractTAXReturn> dataprovider = new ListDataProvider<ClientAbstractTAXReturn>();
	private SelectionChangedHandler<ClientAbstractTAXReturn> callBack;

	TaxHistoryTable(SelectionChangedHandler<ClientAbstractTAXReturn> callBack) {
		initColumns();
		this.callBack = callBack;
		HTML emptyMessage = new HTML(Accounter.constants().taxHistoryEmpty());
		emptyMessage.setHeight("150px");
		setEmptyTableWidget(emptyMessage);
	}

	private void initColumns() {

		dataprovider.addDataDisplay(this);

		CheckBoxColumn<ClientAbstractTAXReturn> check = new CheckBoxColumn<ClientAbstractTAXReturn>() {

			@Override
			public void update(int arg0, ClientAbstractTAXReturn arg1,
					Boolean arg2) {

				callBack.selectionChanged(arg1, arg2);
			}

			@Override
			public Boolean getValue(ClientAbstractTAXReturn arg0) {
				return false;
			}
		};

		TextColumn<ClientAbstractTAXReturn> periodStartDate = new TextColumn<ClientAbstractTAXReturn>() {

			@Override
			public String getValue(ClientAbstractTAXReturn object) {

				return new ClientFinanceDate(object.getPeriodStartDate())
						.toString();
			}
		};

		TextColumn<ClientAbstractTAXReturn> periodEndDate = new TextColumn<ClientAbstractTAXReturn>() {

			@Override
			public String getValue(ClientAbstractTAXReturn object) {

				return new ClientFinanceDate(object.getPeriodEndDate())
						.toString();
			}
		};
		TextColumn<ClientAbstractTAXReturn> vatFileDate = new TextColumn<ClientAbstractTAXReturn>() {

			@Override
			public String getValue(ClientAbstractTAXReturn object) {

				return null;
			}
		};
		TextColumn<ClientAbstractTAXReturn> netAmountDue = new TextColumn<ClientAbstractTAXReturn>() {

			@Override
			public String getValue(ClientAbstractTAXReturn object) {

				return String.valueOf(object.getBalance());

			}
		};

		TextColumn<ClientAbstractTAXReturn> totalPaymentMade = new TextColumn<ClientAbstractTAXReturn>() {

			@Override
			public String getValue(ClientAbstractTAXReturn object) {

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

	public void setData(List<ClientAbstractTAXReturn> data) {
		if (data == null) {
			data = Collections.emptyList();
		}
		this.dataprovider.setList(data);

	}

	public List<ClientAbstractTAXReturn> getData() {
		return this.dataprovider.getList();
	}

}
