package com.vimukti.accounter.web.client.ui.vendors;

import java.util.Collections;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.ListDataProvider;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.ClientTDSInfo;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.IDeleteCallback;

public class TDSVendorsTable extends CellTable<ClientTDSInfo> implements
		IDeleteCallback {

	private ListDataProvider<ClientTDSInfo> dataprovider = new ListDataProvider<ClientTDSInfo>();

	TDSVendorsTable() {
		initColumns();
	}

	private void initColumns() {

		dataprovider.addDataDisplay(this);

		TextColumn<ClientTDSInfo> vendorName = new TextColumn<ClientTDSInfo>() {

			@Override
			public String getValue(ClientTDSInfo object) {

				return object.getVendor().getName();
			}
		};

		TextColumn<ClientTDSInfo> date = new TextColumn<ClientTDSInfo>() {

			@Override
			public String getValue(ClientTDSInfo object) {

				return object.getDate().toString();
			}
		};
		TextColumn<ClientTDSInfo> originalBalance = new TextColumn<ClientTDSInfo>() {

			@Override
			public String getValue(ClientTDSInfo object) {

				return DataUtils.getAmountAsString(object.getOrginalBalance());
			}
		};
		TextColumn<ClientTDSInfo> payment = new TextColumn<ClientTDSInfo>() {

			@Override
			public String getValue(ClientTDSInfo object) {

				return DataUtils.getAmountAsString(object.getPayment());
			}
		};

		TextColumn<ClientTDSInfo> tdsPercentage = new TextColumn<ClientTDSInfo>() {

			@Override
			public String getValue(ClientTDSInfo object) {

				return String.valueOf(Accounter.getCompany()
						.getTAXItem(object.getVendor().getTaxItemCode())
						.getTaxRate())
						+ "%";
			}
		};
		TextColumn<ClientTDSInfo> tdsAmount = new TextColumn<ClientTDSInfo>() {

			@Override
			public String getValue(ClientTDSInfo object) {
				return DataUtils.getAmountAsString(object.getTdsAmount());
			}
		};

		this.addColumn(vendorName, Accounter.constants().vendor());
		this.addColumn(date, Accounter.constants().date());
		this.addColumn(originalBalance, Accounter.constants().originalAmount());
		this.addColumn(payment, Accounter.constants().payment());
		this.addColumn(tdsPercentage, Accounter.constants().percentage());
		this.addColumn(tdsAmount, Accounter.constants().tdsAmount());

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	public void setData(List<ClientTDSInfo> data) {
		if (data == null) {
			data = Collections.emptyList();
		}

		this.dataprovider.setList(data);
	}

}
