package com.vimukti.accounter.web.client.ui.vendors;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientPayBill;
import com.vimukti.accounter.web.client.core.ClientPayTDS;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.BaseListView;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.grids.TDSVendorsListGrid;

/**
 * 
 * @author Sai Prasad N
 * 
 */
public class TDSVendorsListView extends BaseListView<ClientPayTDS> {

	private List<ClientPayBill> listOfPayees;

	Button button;
	private DateItem fromDate;
	private DateItem toDate;

	private boolean isTdsView;

	public TDSVendorsListView(boolean isTdsView) {
		super();
		this.isTdsView = isTdsView;
		createControls1();

	}

	private void createControls1() {

		VerticalPanel mainPanel = new VerticalPanel();
		fromDate = new DateItem(Accounter.constants().from());
		fromDate.setHelpInformation(true);
		fromDate.setWidth(100);
		toDate = new DateItem(Accounter.constants().to());
		toDate.setHelpInformation(true);
		toDate.setWidth(100);

		updateButton = new Button(Accounter.constants().update());
		updateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

			}
		});

		DynamicForm topForm = new DynamicForm();
		topForm.setIsGroup(true);
		topForm.setGroupTitle(Accounter.constants().top());
		topForm.setNumCols(6);
		topForm.setFields(fromDate, toDate);
		topForm.setWidth("100%");

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		if (isTdsView) {
			button = new Button(Accounter.constants().eTDSFiling());
			button.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub

				}
			});
			horizontalPanel.add(button);
		}

		HorizontalPanel topLayout = new HorizontalPanel();
		topLayout.add(topForm);
		topLayout.add(updateButton);

		horizontalPanel.setWidth("100%");

		mainPanel.add(horizontalPanel);
		mainPanel.add(topLayout);
		// mainPanel.add(grid);
		this.add(mainPanel);

	}

	@Override
	public void onFailure(Throwable exception) {

		super.onFailure(exception);
	}

	@Override
	protected void initGrid() {
		grid = new TDSVendorsListGrid(false, isTdsView);
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
		Accounter.createHomeService().getPayBillsByTDS(this);
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

	@Override
	public void updateInGrid(ClientPayTDS objectTobeModified) {
		// TODO Auto-generated method stub
		
	}

}
