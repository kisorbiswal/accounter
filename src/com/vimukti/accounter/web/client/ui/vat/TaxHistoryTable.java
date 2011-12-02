package com.vimukti.accounter.web.client.ui.vat;

import java.util.Collections;
import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXReturn;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.SelectionChangedHandler;
import com.vimukti.accounter.web.client.ui.grids.columns.ImageActionColumn;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class TaxHistoryTable extends CellTable<ClientTAXReturn> {
	private ListDataProvider<ClientTAXReturn> dataprovider = new ListDataProvider<ClientTAXReturn>();
	private SelectionChangedHandler<ClientTAXReturn> callBack;

	TaxHistoryTable(SelectionChangedHandler<ClientTAXReturn> callBack) {
		initColumns();
		this.callBack = callBack;
		HTML emptyMessage = new HTML(Accounter.messages().taxHistoryEmpty());
		emptyMessage.setHeight("150px");
		setEmptyTableWidget(emptyMessage);
	}

	private void initColumns() {

		dataprovider.addDataDisplay(this);

		ImageActionColumn<ClientTAXReturn> status = new ImageActionColumn<ClientTAXReturn>() {

			@Override
			public ImageResource getValue(ClientTAXReturn arg0) {
				return Accounter.getFinanceMenuImages().accounterRegisterIcon();
			}

			@Override
			protected void onSelect(int index, ClientTAXReturn object) {

				ActionFactory.getVATSummaryReportAction().run();

			}
		};

		TextColumn<ClientTAXReturn> periodStartDate = new TextColumn<ClientTAXReturn>() {

			@Override
			public String getValue(ClientTAXReturn object) {

				return new ClientFinanceDate(object.getPeriodStartDate())
						.toString();
			}
		};

		TextColumn<ClientTAXReturn> periodEndDate = new TextColumn<ClientTAXReturn>() {

			@Override
			public String getValue(ClientTAXReturn object) {

				return new ClientFinanceDate(object.getPeriodEndDate())
						.toString();
			}
		};
		TextColumn<ClientTAXReturn> vatFileDate = new TextColumn<ClientTAXReturn>() {

			@Override
			public String getValue(ClientTAXReturn object) {

				return null;
			}
		};
		TextColumn<ClientTAXReturn> netAmountDue = new TextColumn<ClientTAXReturn>() {

			@Override
			public String getValue(ClientTAXReturn object) {

				return String.valueOf(object.getBalance());

			}
		};

		TextColumn<ClientTAXReturn> totalPaymentMade = new TextColumn<ClientTAXReturn>() {

			@Override
			public String getValue(ClientTAXReturn object) {

				return String.valueOf(object.getNetAmount());
			}
		};

		this.addColumn(periodStartDate, Accounter.messages().periodStartDate());
		this.addColumn(periodEndDate, Accounter.messages().periodEndDate());
		this.addColumn(vatFileDate, Accounter.messages().vatFileDate());
		this.addColumn(netAmountDue, Accounter.messages().netAmountDue());
		this.addColumn(totalPaymentMade, Accounter.messages()
				.totalPaymentMade());
		this.addColumn(status, Accounter.messages().select());
	}

	public void setData(List<ClientTAXReturn> data) {
		if (data == null) {
			data = Collections.emptyList();
		}
		this.dataprovider.setList(data);

	}

	public List<ClientTAXReturn> getData() {
		return this.dataprovider.getList();
	}

}
