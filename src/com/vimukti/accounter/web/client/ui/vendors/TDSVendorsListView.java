package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.Lists.PayeeList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.grids.TDSVendorsListGrid;

/**
 * 
 * @author Sai Prasad N
 *
 */
public class TDSVendorsListView extends BaseListView<PayeeList> {

	private List<PayeeList> listOfPayees;

	Button button;

	public TDSVendorsListView() {
		super();
		createControls1();

	}

	private void createControls1() {

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		button = new Button("TDS Filing");
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});
	

		horizontalPanel.add(button);
	
		horizontalPanel.setHorizontalAlignment(ALIGN_CENTER);
		this.add(horizontalPanel);

	}

	@Override
	public void onFailure(Throwable exception) {

		super.onFailure(exception);
	}

	@Override
	public void onSuccess(ArrayList<PayeeList> result) {
		listOfPayees = result;
		super.onSuccess(result);
	}

	@Override
	public void updateInGrid(PayeeList objectTobeModified) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initGrid() {
		grid = new TDSVendorsListGrid(false);
		grid.init();

	}

	@Override
	protected SelectCombo getSelectItem() {
		// TODO Auto-generated method stub
		return super.getSelectItem();
	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub
		super.fitToSize(height, width);
	}

	@Override
	protected String getListViewHeading() {
		return messages.vendorList(Global.get().Vendor());
	}

	@Override
	protected Action getAddNewAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getAddNewLabelString() {

		
			return "";
	}

	@Override
	public void initListCallback() {
		super.initListCallback();
		Accounter.createHomeService().getPayeeList(
				ClientTransaction.CATEGORY_VENDOR, this);
	}

	@Override
	public void customManage() {
		// TODO Auto-generated method stub
		super.customManage();
	}

	@Override
	protected String getViewTitle() {
		return messages.vendors(Global.get().Vendor());
	}

}
