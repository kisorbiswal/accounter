package com.vimukti.accounter.web.client.ui.company;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.IAccounterGETService;
import com.vimukti.accounter.web.client.IAccounterGETServiceAsync;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.AbstractBaseDialog;
import com.vimukti.accounter.web.client.ui.AbstractBaseView;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterButton;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.RecordDoubleClickHandler;

public class CompanyListDialog extends AbstractBaseDialog<ClientCompany> {
	DialogGrid companyGrid;

	// String[] fieldNames = { "id;ID", "name;Name", "legal_name;Legal Name" };

	public CompanyListDialog(AbstractBaseView<ClientCompany> parent) {
		super(parent);
		createControls();
		getCompanyList();
	}

	private void createControls() {
		companyGrid = new DialogGrid(false);
		companyGrid.addColumn(ListGrid.COLUMN_TYPE_TEXTBOX, Accounter
				.constants().id());

		companyGrid.addColumn(ListGrid.COLUMN_TYPE_TEXTBOX, Accounter
				.constants().name());
		companyGrid.addColumn(ListGrid.COLUMN_TYPE_TEXTBOX, Accounter
				.constants().legalName());

		AccounterButton closeButt = new AccounterButton(Accounter.constants()
				.close());
		// closeButt.setLayoutAlign(Alignment.RIGHT);

		closeButt.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// XXX THIS CLASS IS NOT USED
				// cancelClick();
			}
		});

		companyGrid
				.addRecordDoubleClickHandler(new RecordDoubleClickHandler<ClientCompany>() {

					@Override
					public void OnCellDoubleClick(ClientCompany core, int column) {
						ClientCompany company = core;
						UIUtils.say(Accounter.constants().fetchingCompany()
								+ company.getID() + "\n"
								+ Accounter.constants().name()
								+ company.getName() + "\n"
								+ Accounter.constants().legalName()
								+ company.getTradingName());

					}

				});
		companyGrid
				.addRecordDoubleClickHandler(new RecordDoubleClickHandler<ClientCompany>() {

					@Override
					public void OnCellDoubleClick(ClientCompany core, int column) {
						ClientCompany company = core;
						UIUtils.say(Accounter.constants().fetchingCompany()
								+ company.getID() + "\n"
								+ Accounter.constants().name()
								+ company.getName() + "\n"
								+ Accounter.constants().legalName()
								+ company.getTradingName());

					}

				});

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.add(companyGrid);
		mainVLay.add(closeButt);

		closeButt.enabledButton();

		add(mainVLay);
		setSize("400", "400");

		show();
	}

	private void getCompanyList() {
		IAccounterGETServiceAsync getService = (IAccounterGETServiceAsync) GWT
				.create(IAccounterGETService.class);
		((ServiceDefTarget) getService)
				.setServiceEntryPoint(Accounter.GET_SERVICE_ENTRY_POINT);
		AsyncCallback<List<ClientCompany>> getCompanyListCallback = new AsyncCallback<List<ClientCompany>>() {

			public void onFailure(Throwable caught) {

			}

			public void onSuccess(List<ClientCompany> result) {
				if (result != null) {
					fillGrid(result);
				} else {
				}
			}

		};

		getService
				.getObjects(AccounterCoreType.COMPANY, getCompanyListCallback);
	}

	
	private void fillGrid(List<ClientCompany> result) {
		// ListGridRecord[] records = new ListGridRecord[result.size()];
		// ClientCompany c;
		// for (int recordIndex = 0; recordIndex < records.length;
		// ++recordIndex) {
		// c = result.get(recordIndex);
		// records[recordIndex] = new ListGridRecord();
		// records[recordIndex].setAttribute("comp_id", c.getID() + "");
		// records[recordIndex].setAttribute("name", c.getName());
		// // records[recordIndex].setAttribute("legal_name",
		// // c.getLegalName());
		// }
		List<IsSerializable> list = (ArrayList) result;
		companyGrid.setRecords(list);
		// companyGrid.fetchData();
	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}
}
