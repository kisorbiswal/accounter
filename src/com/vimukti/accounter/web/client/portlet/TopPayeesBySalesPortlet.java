package com.vimukti.accounter.web.client.portlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPortletConfiguration;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.PayeesBySalesPortletData;
import com.vimukti.accounter.web.client.ui.Portlet;
import com.vimukti.accounter.web.client.ui.reports.TopPayeesBySalesPortletToolBar;

public class TopPayeesBySalesPortlet extends Portlet {
	public static final int CUSTOMER_PORTLET = 1;
	public static final int VENDOR_PORTLET = 2;
	public static final int ITEM_PORTLET = 3;

	private TopPayeesBySalesPortletToolBar toolBar;
	private VerticalPanel gridPanel;
	private int portletType;

	public TopPayeesBySalesPortlet(ClientPortletConfiguration configuration,
			int portletType) {
		super(configuration, "", "", "100%");
		this.portletType = portletType;
		setPortletTitle();
	}

	private void setPortletTitle() {
		String title = "";
		if (portletType == CUSTOMER_PORTLET) {
			title = messages.topCustomersBySales(Global.get().Customers());
		} else if (portletType == VENDOR_PORTLET) {
			title = messages.topVendorsByExpense(Global.get().vendors());
		} else if (portletType == ITEM_PORTLET) {
			title = messages.topItemsBySales();
		}
		this.setPortletTitle(title);
	}

	@Override
	public void createBody() {
		gridPanel = new VerticalPanel();
		createToolBar();
		this.body.add(gridPanel);
	}

	private void createToolBar() {
		toolBar = new TopPayeesBySalesPortletToolBar() {

			@Override
			protected void refreshPortletData(String selectItem, String limit) {
				TopPayeesBySalesPortlet.this.clearGrid();
				dateRangeItemCombo.setSelected(selectItem);
				limitCombo.setSelected(limit);
				Map<String, String> portletMap = new HashMap<String, String>();
				portletMap.put(DATE_RANGE, selectItem);
				portletMap.put(LIMIT, limit);
				TopPayeesBySalesPortlet.this.getConfiguration().setPortletMap(
						portletMap);
				dateRangeChanged(selectItem);
				TopPayeesBySalesPortlet.this.updateData(toolBar.getStartDate(),
						toolBar.getEndDate(), limit);
			}

			@Override
			protected List<String> getSelectedItem() {
				List<String> data = new ArrayList<String>();
				if (TopPayeesBySalesPortlet.this.getConfiguration()
						.getPortletMap().get(DATE_RANGE) != null) {
					data.add(TopPayeesBySalesPortlet.this.getConfiguration()
							.getPortletMap().get(DATE_RANGE));
				} else {
					data.add(messages.financialYearToDate());
				}
				if (TopPayeesBySalesPortlet.this.getConfiguration()
						.getPortletMap().get(LIMIT) != null) {
					data.add(TopPayeesBySalesPortlet.this.getConfiguration()
							.getPortletMap().get(LIMIT));
				} else {
					data.add("5");
				}

				return data;
			}

			@Override
			public void setDefaultDateRange(String defaultDateRange) {
				dateRangeItemCombo.setSelected(defaultDateRange);
				dateRangeChanged(defaultDateRange);
				TopPayeesBySalesPortlet.this.updateData(getStartDate(),
						getEndDate(), "5");
			}
		};
		this.body.add(toolBar);
	}

	protected void updateData(ClientFinanceDate startDate,
			ClientFinanceDate endDate, String limit) {
		AsyncCallback<ArrayList<PayeesBySalesPortletData>> callback = new AsyncCallback<ArrayList<PayeesBySalesPortletData>>() {

			private PayeeBySalesGrid grid;

			@Override
			public void onSuccess(ArrayList<PayeesBySalesPortletData> result) {
				grid = new PayeeBySalesGrid(portletType);
				grid.init();
				if (result != null && !(result.isEmpty())) {
					grid.setRecords(result);
				} else {
					grid.addEmptyMessage(messages.noTransactionsToshow());
				}
				gridPanel.add(grid);
				completeInitialization();
			}

			@Override
			public void onFailure(Throwable caught) {
				completeInitialization();
			}
		};
		if (portletType == CUSTOMER_PORTLET) {
			Accounter.createHomeService().getTopCustomersBySalesPortletData(
					startDate, endDate, Integer.valueOf(limit), callback);
		} else if (portletType == VENDOR_PORTLET) {
			Accounter.createHomeService().getTopVendorsBySalesPortletData(
					startDate, endDate, Integer.valueOf(limit), callback);
		} else if (portletType == ITEM_PORTLET) {
			Accounter.createHomeService().getItemsBySalesQuantity(startDate,
					endDate, Integer.valueOf(limit), callback);
		}
	}

	protected void clearGrid() {
		gridPanel.clear();
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}
}
