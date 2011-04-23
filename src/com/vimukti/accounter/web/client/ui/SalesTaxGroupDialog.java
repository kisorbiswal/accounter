package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.theme.ThemesUtil;
import com.vimukti.accounter.web.client.ui.core.Accounter;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid.GridRecordClickHandler;

/**
 * Re-implemented Class to get Correct Flow
 * 
 * @author Raj Vimal
 * @author Rajesh Alupula(Initial Implementation)
 * 
 */

@SuppressWarnings("unchecked")
public class SalesTaxGroupDialog extends BaseDialog {

	protected InputDialogHandler addInputDialogHandler;
	protected DialogGrid availTaxItemsGrid;
	protected DialogGrid selectTaxItemsGrid;
	protected Button addButton, removeButton;

	private ArrayList<ClientTAXItem> tempAvailTaxItemList;
	private ArrayList<ClientTAXItem> tempSelectedTaxItemList;
	public TextItem taxGroupText;
	private static boolean flag = false;

	public SalesTaxGroupDialog(String title, String desc,
			ClientTAXGroup taxGroup) {

		super(title, desc);
		createControls(taxGroup);
		if (taxGroup != null)
			fillSelectedTaxItems(taxGroup);

		fillAvailableTaxItems();
		mainPanel.setSpacing(3);
		center();
	}

	// Filling Available tax Codes in availTaxCodesGrid
	private void fillAvailableTaxItems() {
		for (ClientTAXItem codeInternal : getAvailableTaxItems())
			availTaxItemsGrid.addData(codeInternal);
	}

	// Filling Available tax Codes in selectTaxCodesGrid
	private void fillSelectedTaxItems(ClientTAXGroup taxGroup) {
		for (ClientTAXItem codeInternal : getSelectedTaxItems(taxGroup)) {
			selectTaxItemsGrid.addData(codeInternal);
		}
	}

	// getting all Tax Codes from Company Object & converting these to temporary
	// TaxCodeInternal object for inserting data in Grid...
	public List<ClientTAXItem> getAllTaxItem() {
		List<ClientTAXItem> savedTaxItems = FinanceApplication.getCompany()
				.getActiveTaxItems();

		return savedTaxItems;
	}

	// getting all Available Tax Codes which are not available in Selected Tax
	// Code grid
	private ArrayList<ClientTAXItem> getAvailableTaxItems() {
		boolean isFound = false;
		tempSelectedTaxItemList = new ArrayList<ClientTAXItem>();
		for (ClientTAXItem codeInternal : getAllTaxItem()) {
			if (tempAvailTaxItemList != null)
				for (ClientTAXItem internal : tempAvailTaxItemList) {
					if (codeInternal.getStringID() == internal.getStringID()) {
						isFound = true;
						break;
					} else
						isFound = false;
				}
			if (!isFound)
				tempSelectedTaxItemList.add(codeInternal);
		}
		return tempSelectedTaxItemList;

	}

	// getting all available Tax Codes in Selected Tax Code grid
	private ArrayList<ClientTAXItem> getSelectedTaxItems(ClientTAXGroup taxGroup) {

		List<ClientTAXItem> items = taxGroup.getTaxItems();
		tempAvailTaxItemList = new ArrayList<ClientTAXItem>(items);
		// tempAvailTaxCodeList = (ArrayList) Arrays.asList(codes.toArray());

		return tempAvailTaxItemList;

	}

	private void setAvailTaxItemsGridFields() {
		availTaxItemsGrid.addColumns(new String[] {
				FinanceApplication.getFinanceUIConstants().Name(),
				FinanceApplication.getFinanceUIConstants().currentRate() });
	}

	public void setAvalableTCGridCellWidths() {
		availTaxItemsGrid.setCellsWidth(new Integer[] { 109, 100 });
	}

	private void setSelectedTaxItemsGridFields() {
		selectTaxItemsGrid.addColumns(new String[] {
				FinanceApplication.getFinanceUIConstants().Name(),
				FinanceApplication.getFinanceUIConstants().currentRate() });

	}

	public void setSelectedTCGridCellWidths() {
		selectTaxItemsGrid.setCellsWidth(new Integer[] { 109, 100 });
	}

	public Object getAvailTaxItemsGridColumnValue(IsSerializable accounterCore,
			int col) {
		ClientTAXItem taxItem = (ClientTAXItem) accounterCore;
		switch (col) {
		case 0:
			return taxItem.getName() != null ? taxItem.getName() : "";
		case 1:
			return taxItem.getTaxRate() + " %";
		default:
			break;
		}
		return null;
	}

	public Object getSelectedTaxItemsColumnValue(IsSerializable accounterCore,
			int col) {
		ClientTAXItem taxItem = (ClientTAXItem) accounterCore;
		switch (col) {
		case 0:
			return taxItem.getName();
		case 1:
			return taxItem.getTaxRate() + " %";
		default:
			break;
		}
		return null;
	}

	public List<ClientTAXGroup> getAvailableRecords() {

		return FinanceApplication.getCompany().getTaxGroups();
	}

	private void createControls(final ClientTAXGroup taxGroup) {

		setWidth("570");
		// setPageTop(10);
		VerticalPanel bodyLayout = new VerticalPanel();

		DynamicForm form1 = new DynamicForm();
		// form1.setHeight("100px");
		taxGroupText = new TextItem();
		taxGroupText.setTitle(FinanceApplication.getFinanceUIConstants()
				.selectedTaxGroupItem());
		taxGroupText.setRequired(true);

		if (taxGroup != null)
			taxGroupText.setValue(taxGroup.getName());

		form1.setFields(taxGroupText);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		// horizontalPanel.setHeight("200px");
		horizontalPanel.setWidth("100%");
		// Available Tax Codes Layout
		// DynamicForm availForm = new DynamicForm();

		availTaxItemsGrid = new DialogGrid(false);
		// availTaxCodesGrid.setCellsWidth(cellsWidth)

		availTaxItemsGrid.setName(FinanceApplication.getFinanceUIConstants()
				.available());
		setAvailTaxItemsGridFields();
		setAvalableTCGridCellWidths();
		availTaxItemsGrid.setView(SalesTaxGroupDialog.this);
		// getAvailableRecords();
		availTaxItemsGrid.addRecordClickHandler(new GridRecordClickHandler() {

			@Override
			public boolean onRecordClick(IsSerializable core, int column) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		availTaxItemsGrid.init();
		// availTaxCodesGrid.setWidth("80%");
		// availTaxCodesGrid.setHeight("100%");

		// Buttons Layout
		VerticalPanel buttonsLayout = new VerticalPanel();
		buttonsLayout.setWidth("100px");
		buttonsLayout.setSpacing(3);
		// buttonsLayout.setMembersMargin(10);
		// buttonsLayout.setLayoutMargin(10);
		Button addButton = new Button(FinanceApplication.getVATMessages().Add());
		addButton.setWidth("80px");
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				if (availTaxItemsGrid.getSelection() != null) {
					ClientTAXItem gridRecord = (ClientTAXItem) availTaxItemsGrid
							.getSelection();
					selectTaxItemsGrid.addData(gridRecord);
					tempSelectedTaxItemList.add(gridRecord);

					availTaxItemsGrid.deleteRecord(gridRecord);
					if (tempAvailTaxItemList != null)
						tempAvailTaxItemList.remove(gridRecord);

				} else {

					UIUtils.say(FinanceApplication.getFinanceUIConstants()
							.selectTaxItemFromAvailableListofTaxItems());
					new Exception();
				}

			}
		});

		Button removeButton = new Button(FinanceApplication
				.getFinanceUIConstants().remove());
		removeButton.setWidth("80px");
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (selectTaxItemsGrid.getSelection() != null) {
					ClientTAXItem gridRecord = (ClientTAXItem) selectTaxItemsGrid
							.getSelection();
					selectTaxItemsGrid.deleteRecord(gridRecord);
					tempSelectedTaxItemList.remove(gridRecord);
					availTaxItemsGrid.addData(gridRecord);
					if (tempAvailTaxItemList != null)
						tempAvailTaxItemList.add(gridRecord);
				} else {

					Accounter.showError(FinanceApplication
							.getFinanceUIConstants()
							.selectTaxItemFromSelectedListofTaxItems());
					new Exception();
				}

			}
		});

		buttonsLayout.add(addButton);
		buttonsLayout.add(removeButton);

		addButton.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(addButton, FinanceApplication
				.getThemeImages().button_right_blue_image(),
				"ibutton-right-image");
		removeButton.getElement().getParentElement().setClassName("ibutton");
		ThemesUtil.addDivToButton(removeButton, FinanceApplication
				.getThemeImages().button_right_blue_image(),
				"ibutton-right-image");
		// Selected Tax Codes Layout
		// DynamicForm selectForm = new DynamicForm();

		selectTaxItemsGrid = new DialogGrid(false);
		selectTaxItemsGrid.setName(FinanceApplication.getFinanceUIConstants()
				.selected());
		setSelectedTaxItemsGridFields();
		setSelectedTCGridCellWidths();
		selectTaxItemsGrid.setView(SalesTaxGroupDialog.this);
		if (taxGroup != null)
			getSelectedTaxItems(taxGroup);
		selectTaxItemsGrid.addRecordClickHandler(new GridRecordClickHandler() {

			@Override
			public boolean onRecordClick(IsSerializable core, int column) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		selectTaxItemsGrid.init();
		// selectTaxCodesGrid.setWidth("80%");
		// selectTaxCodesGrid.setHeight("100%");

		// selectForm.add(selectTaxCodesGrid);

		horizontalPanel.setCellHorizontalAlignment(availTaxItemsGrid,
				HasHorizontalAlignment.ALIGN_LEFT);
		horizontalPanel.add(availTaxItemsGrid);
		horizontalPanel.setCellHorizontalAlignment(buttonsLayout,
				HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.add(buttonsLayout);
		horizontalPanel.setCellHorizontalAlignment(selectTaxItemsGrid,
				HasHorizontalAlignment.ALIGN_RIGHT);
		horizontalPanel.add(selectTaxItemsGrid);

		bodyLayout.add(form1);
		bodyLayout.add(horizontalPanel);

		setBodyLayout(bodyLayout);
		center();

	}

	public DialogGrid getSelectTaxItemsGrid() {
		return selectTaxItemsGrid;
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {

		if (!flag) {
			flag = true;
			return getAvailTaxItemsGridColumnValue(obj, index);

		} else {
			flag = false;
			return getSelectedTaxItemsColumnValue(obj, index);

		}

	}

	@Override
	public void deleteFailed(Throwable caught) {

	}

	@Override
	public void deleteSuccess(Boolean result) {

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
	}

	@Override
	public void saveFailed(Throwable exception) {

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {
		// TODO Auto-generated method stub

	}

	// @Override
	// public Object getGridColumnValue(IsSerializable obj, int index) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	// public void initGrid(List<ClientTaxGroup> resultrecords) {
	// // grid.setRecords((List<IsSerializable>) (ArrayList) resultrecords);
	// if (resultrecords != null) {
	//
	// List<ClientTaxGroup> records = resultrecords;
	//
	// if (records != null) {
	// for (ClientTaxGroup t : records) {
	// addToGrid(t);
	// }
	//
	// }
	// }
	// }
	//	
	// public void addToGrid(ClientTaxGroup objectToBeAdded) {
	// availTaxCodesGrid.addData((IsSerializable) objectToBeAdded);
	// }

}
