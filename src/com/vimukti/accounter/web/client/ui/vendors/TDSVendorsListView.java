package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Lists.ClientTDSInfo;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class TDSVendorsListView extends BaseView<ClientTDSInfo> implements
		AsyncCallback<ArrayList<ClientTDSInfo>> {

	private DateItem fromDate;
	private DateItem toDate;

	Label label;
	ArrayList<ClientTDSInfo> clientTDSInfos;

	private VendorCombo vendorCombo;
	private Button updateButton;
	private TDSVendorsTable grid;

	private boolean isTdsView;

	@Override
	public void init() {
		super.init();
		createControls1();
	}

	public TDSVendorsListView(boolean isTdsView) {

		this.isTdsView = isTdsView;
	}

	private void createControls1() {

		this.saveAndCloseButton.setVisible(false);
		this.saveAndNewButton.setText("TDS Filing");
		label = new Label();
		label.removeStyleName("gwt-style");
		label.setWidth("100%");
		label.addStyleName(Accounter.constants().labelTitle());
		label.setText(Accounter.constants().tdsVendorsList());
		this.fromDate = new DateItem(Accounter.constants().from());
		this.fromDate.setHelpInformation(true);

		this.toDate = new DateItem(Accounter.constants().to());
		this.toDate.setHelpInformation(true);
		this.toDate.setEnteredDate(new ClientFinanceDate());

		this.fromDate.setEnteredDate(new ClientFinanceDate());
		this.vendorCombo = new VendorCombo(Global.get().messages()
				.vendorName(Global.get().Vendor()), true);
		vendorCombo.setValue(new String((Accounter.constants().all())));

		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						filterListByVendor(selectItem);

					}

				});

		this.updateButton = new Button(Accounter.constants().update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				filterListByDate();

			}

		});

		this.grid = new TDSVendorsTable();
		grid.setWidth("100%");
		DynamicForm topForm = new DynamicForm();
		topForm.setIsGroup(true);
		topForm.setGroupTitle(Accounter.constants().top());
		topForm.setNumCols(6);
		topForm.setItems(fromDate, toDate);
		// topForm.setWidth("100%");
		DynamicForm form2 = new DynamicForm();

		form2.setFields(vendorCombo);

		Accounter.createHomeService().getPayBillsByTDS(this);

		HorizontalPanel topLayout = new HorizontalPanel();
		topLayout.setWidth("100%");
		topLayout.add(topForm);
		topLayout.add(updateButton);
		topLayout.setCellHorizontalAlignment(topForm, ALIGN_LEFT);
		topLayout.setCellHorizontalAlignment(updateButton, ALIGN_LEFT);

		// topLayout.setCellHorizontalAlignment(updateButton, ALIGN_RIGHT);
		HorizontalPanel h1 = new HorizontalPanel();

		h1.add(label);
		h1.setWidth("100%");

		h1.setHorizontalAlignment(ALIGN_RIGHT);
		h1.add(form2);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setWidth("100%");
		// mainPanel.add(topForm);
		mainPanel.add(topLayout);
		mainPanel.add(h1);
		// mainPanel.add(label);
		mainPanel.add(grid);
		grid.getElement().getParentElement()
				.addClassName("recounciliation_grid");
		mainPanel.setCellHeight(grid, "200px");

		this.add(mainPanel);

	}

	private void filterListByVendor(ClientVendor selectItem) {

		List<ClientTDSInfo> cl = new ArrayList<ClientTDSInfo>();
		for (ClientTDSInfo clientTDSInfo : clientTDSInfos) {
			if (selectItem.getID() == clientTDSInfo.getVendor().getID()) {
				cl.add(clientTDSInfo);
			}

		}

		grid.setData(cl);

	}

	@Override
	protected String getViewTitle() {
		return messages.vendors(Global.get().Vendor());
	}

	private void filterListByDate() {

		List<ClientTDSInfo> cl = new ArrayList<ClientTDSInfo>();
		for (ClientTDSInfo clientTDSInfo : clientTDSInfos) {
			if ((clientTDSInfo.getDate().after(fromDate.getDate()))
					&& (clientTDSInfo.getDate().before(toDate.getDate()))) {
				cl.add(clientTDSInfo);
			}

		}
		grid.setData(cl);

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(ArrayList<ClientTDSInfo> result) {

		if (result != null) {
			clientTDSInfos = result;
			grid.setData(result);
		}

	}

}
