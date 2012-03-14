package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Button;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXGroup;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTAXItemGroup;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
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

public class SalesTaxGroupDialog extends BaseDialog<ClientTAXGroup> {

	protected DialogGrid<ClientTAXItem> availTaxItemsGrid;
	protected DialogGrid<ClientTAXItem> selectTaxItemsGrid;
	protected Button addButton, removeButton;
	ClientTAXGroup taxGroup;

	private ArrayList<ClientTAXItem> tempAvailTaxItemList;
	private ArrayList<ClientTAXItem> tempSelectedTaxItemList;
	public TextItem taxGroupText;
	public DynamicForm form1;
	private static boolean flag = false;

	public SalesTaxGroupDialog(String title, String desc,
			ClientTAXGroup taxGroup) {

		super(title, desc);
		this.getElement().setId("SalesTaxGroupDialog");
		this.taxGroup = taxGroup;
		createControls(taxGroup);
		if (taxGroup != null)
			fillSelectedTaxItems(taxGroup);

		fillAvailableTaxItems();
		// mainPanel.setSpacing(3);
		center();
	}

	// Filling Available tax Codes in availTaxCodesGrid
	private void fillAvailableTaxItems() {
		for (ClientTAXItem codeInternal : getAvailableTaxItems())
			availTaxItemsGrid.addData(codeInternal);
	}

	// Filling Available tax Codes in selectTaxCodesGrid
	private void fillSelectedTaxItems(ClientTAXGroup taxGroup) {
		if (getTaxItemsForTaxGroup(taxGroup) != null)
			for (ClientTAXItem codeInternal : getTaxItemsForTaxGroup(taxGroup)) {
				selectTaxItemsGrid.addData(codeInternal);
			}
	}

	// getting all Tax Codes from Company Object & converting these to temporary
	// TaxCodeInternal object for inserting data in Grid...
	public List<ClientTAXItem> getAllTaxItem() {
		List<ClientTAXItem> taxItems = new ArrayList<ClientTAXItem>();
		List<ClientTAXItem> savedTaxItems = getCompany().getActiveTaxItems();
		for (ClientTAXItem clientTAXItem : savedTaxItems) {
			ClientTAXAgency taxAgency = Accounter.getCompany().getTaxAgency(
					clientTAXItem.getTaxAgency());
			if (taxAgency.getTaxType() != ClientTAXAgency.TAX_TYPE_TDS) {
				taxItems.add(clientTAXItem);
			}
		}
		return taxItems;
	}

	// getting all Available Tax Codes which are not available in Selected Tax
	// Code grid
	private ArrayList<ClientTAXItem> getAvailableTaxItems() {
		boolean isFound = false;
		tempSelectedTaxItemList = new ArrayList<ClientTAXItem>();
		for (ClientTAXItem codeInternal : getAllTaxItem()) {
			if (tempAvailTaxItemList != null)
				for (ClientTAXItem internal : tempAvailTaxItemList) {
					if (codeInternal.getID() == internal.getID()) {
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
	private ArrayList<ClientTAXItem> getTaxItemsForTaxGroup(
			ClientTAXGroup taxGroup) {

		List<ClientTAXItem> items = taxGroup.getTaxItems();
		if (items != null)
			tempAvailTaxItemList = new ArrayList<ClientTAXItem>(items);
		// tempAvailTaxCodeList = (ArrayList) Arrays.asList(codes.toArray());

		return tempAvailTaxItemList;

	}

	private void setAvailTaxItemsGridFields() {
		availTaxItemsGrid.addColumns(new String[] { messages.name(),
				messages.currentRate() });
	}

	public void setAvalableTCGridCellWidths() {
		availTaxItemsGrid.setCellsWidth(new Integer[] { 80, 50 });
	}

	private void setSelectedTaxItemsGridFields() {
		selectTaxItemsGrid.addColumns(new String[] { messages.name(),
				messages.currentRate() });

	}

	public void setSelectedTCGridCellWidths() {
		selectTaxItemsGrid.setCellsWidth(new Integer[] { 80, 50 });
	}

	public Object getAvailTaxItemsGridColumnValue(
			ClientTAXItemGroup accounterCore, int col) {
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

		return getCompany().getTaxGroups();
	}

	private void createControls(final ClientTAXGroup taxGroup) {

		// setPageTop(10);
		StyledPanel bodyLayout = new StyledPanel("bodyLayout");

		form1 = new DynamicForm("form1");
		taxGroupText = new TextItem(messages.selectedTaxGroupItem(),
				"taxGroupText");
		taxGroupText.setRequired(true);

		if (taxGroup != null)
			taxGroupText.setValue(taxGroup.getName());

		form1.add(taxGroupText);

		StyledPanel horizontalPanel = new StyledPanel("horizontalPanel");

		addButton = new Button(messages.add());
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
					if (availTaxItemsGrid.getRecords() == null
							|| availTaxItemsGrid.getRecords().size() == 0
							|| availTaxItemsGrid.getSelection() == null) {
						addButton.setEnabled(false);
					}

				} else {

					UIUtils.say(messages
							.selectTaxItemFromAvailableListofTaxItems());
					new Exception();
				}

			}
		});
		addButton.setEnabled(false);

		removeButton = new Button(messages.remove());
//		removeButton.setWidth("80px");
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (selectTaxItemsGrid.getSelection() != null) {
					ClientTAXItem gridRecord = (ClientTAXItem) selectTaxItemsGrid
							.getSelection();
					selectTaxItemsGrid.deleteRecord(gridRecord);
					tempSelectedTaxItemList.remove(gridRecord);
					int selectedIndex = availTaxItemsGrid
							.getSelectedRecordIndex();
					availTaxItemsGrid.addData(gridRecord);
					availTaxItemsGrid.selectRecord(selectedIndex);
					if (tempAvailTaxItemList != null)
						tempAvailTaxItemList.add(gridRecord);
					if (selectTaxItemsGrid.getRecords() == null
							|| selectTaxItemsGrid.getRecords().size() == 0
							|| selectTaxItemsGrid.getSelection() == null) {
						removeButton.setEnabled(false);
					}
				} else {

					Accounter.showError(messages
							.selectTaxItemFromSelectedListofTaxItems());
					new Exception();
				}

			}
		});
		removeButton.setEnabled(false);

		availTaxItemsGrid = new DialogGrid<ClientTAXItem>(false) {
			@Override
			protected Object getColumnValue(ClientTAXItem obj, int index) {
				return getAvailTaxItemsGridColumnValue(obj, index);
			}
		};
		// availTaxCodesGrid.setCellsWidth(cellsWidth)

		availTaxItemsGrid.setName(messages.available());
		setAvailTaxItemsGridFields();
		setAvalableTCGridCellWidths();
		availTaxItemsGrid.setView(SalesTaxGroupDialog.this);
		// getAvailableRecords();
		availTaxItemsGrid
				.addRecordClickHandler(new GridRecordClickHandler<ClientTAXItem>() {

					@Override
					public boolean onRecordClick(ClientTAXItem core, int column) {
						addButton.setEnabled(true);
						return true;
					}
				});
		availTaxItemsGrid.init();
		// availTaxCodesGrid.setWidth("80%");
		// availTaxCodesGrid.setHeight("100%");

		// Buttons Layout
		StyledPanel buttonsLayout = new StyledPanel("buttonsLayout");
		// buttonsLayout.setMembersMargin(10);
		// buttonsLayout.setLayoutMargin(10);

		buttonsLayout.add(addButton);
		buttonsLayout.add(removeButton);
		// Selected Tax Codes Layout
		// DynamicForm selectForm = new DynamicForm();

		selectTaxItemsGrid = new DialogGrid<ClientTAXItem>(false) {
			@Override
			protected Object getColumnValue(ClientTAXItem obj, int index) {
				return getAvailTaxItemsGridColumnValue(obj, index);
			}
		};
		selectTaxItemsGrid.setName(messages.selected());
		setSelectedTaxItemsGridFields();
		setSelectedTCGridCellWidths();
		selectTaxItemsGrid.setView(SalesTaxGroupDialog.this);
		if (taxGroup != null)
			getTaxItemsForTaxGroup(taxGroup);
		selectTaxItemsGrid
				.addRecordClickHandler(new GridRecordClickHandler<ClientTAXItem>() {

					@Override
					public boolean onRecordClick(ClientTAXItem core, int column) {
						removeButton.setEnabled(true);
						return true;
					}
				});
		selectTaxItemsGrid.init();
		// selectTaxCodesGrid.setWidth("80%");
		// selectTaxCodesGrid.setHeight("100%");

		// selectForm.add(selectTaxCodesGrid);

		// horizontalPanel.setCellHorizontalAlignment(availTaxItemsGrid,
		// HasHorizontalAlignment.ALIGN_LEFT);
		horizontalPanel.add(availTaxItemsGrid);
		// horizontalPanel.setCellHorizontalAlignment(buttonsLayout,
		// HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel.add(buttonsLayout);
		// horizontalPanel.setCellWidth(buttonsLayout, "75px");
		// horizontalPanel.setCellHorizontalAlignment(selectTaxItemsGrid,
		// HasHorizontalAlignment.ALIGN_RIGHT);
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
	public Object getGridColumnValue(ClientTAXGroup obj, int index) {

		if (!flag) {
			flag = true;
			return getAvailTaxItemsGridColumnValue(obj, index);

		} else {
			flag = false;
			return getSelectedTaxItemsColumnValue(obj, index);

		}

	}

	@Override
	protected ValidationResult validate() {

		ValidationResult result = new ValidationResult();
		result.add(form1.validate());
		return result;
	}

	@Override
	protected boolean onOK() {
		if (taxGroup != null) {
			editTaxGroup(taxGroup);
		} else {
			if (taxGroupText.getValue() != null
					&& !taxGroupText.getValue().toString().isEmpty()) {
				newTaxGroup();
			}

		}
		return false;
	}

	protected void saveOrUpdate(final ClientTAXGroup core) {
		AsyncCallback<Long> callback2 = new AsyncCallback<Long>() {

			@Override
			public void onSuccess(Long result) {
				removeFromParent();
				if (core.getID() != 0) {
					core.setVersion(core.getVersion() + 1);
				}
				core.setID(result);
				getCompany().processUpdateOrCreateObject(core);
				if (getCallback() != null) {
					getCallback().actionResult(core);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		};
		if (core.getID() == 0) {
			Accounter.createCRUDService().create(core, callback2);
		} else {
			Accounter.createCRUDService().update(core, callback2);
		}

	}

	protected void newTaxGroup() {

		ClientTAXGroup taxGroup = new ClientTAXGroup();
		taxGroup.setName(UIUtils.toStr(taxGroupText.getValue()));
		taxGroup.setActive(true);
		taxGroup.setPercentage(true);
		taxGroup.setSalesType(true);
		taxGroup.setTaxItems(getSelectedTaxItems(taxGroup));
		ClientCompany company = getCompany();
		ClientTAXItem itemByName = company.getTaxItemByName(taxGroup.getName());
		ClientTAXGroup taxGroupByName = company.getTaxGroupByName(taxGroup
				.getName());
		if (itemByName != null || taxGroupByName != null) {
			Accounter.showError(messages.alreadyExist());
		} else {
			saveOrUpdate(taxGroup);
		}
	}

	protected void editTaxGroup(ClientTAXGroup taxGroup) {
		String groupName = taxGroupText.getValue();
		ClientTAXGroup taxGroupByName = getCompany().getTaxGroupByName(
				groupName);
		if (!(taxGroup.getName().equalsIgnoreCase(groupName) ? true
				: taxGroupByName == null)) {
			Accounter.showError(messages.alreadyExist());
		} else {
			taxGroup.setName(groupName);
			taxGroup.setTaxItems(getSelectedTaxItems(taxGroup));
			saveOrUpdate(taxGroup);
		}
	}

	private List<ClientTAXItem> getSelectedTaxItems(ClientTAXGroup taxGroup) {
		List<ClientTAXItem> taxItems = new ArrayList<ClientTAXItem>();
		List<ClientTAXItem> records = selectTaxItemsGrid.getRecords();
		ClientTAXItem item;
		double groupRate = 0.00D;
		for (ClientTAXItem clientTaxItem : records) {
			item = getTaxItemByName(clientTaxItem.getName());
			if (item != null) {
				taxItems.add(item);
				groupRate = item.getTaxRate() + groupRate;

			}// if
		}// for
		taxGroup.setGroupRate(groupRate);
		return taxItems;
	}

	private ClientTAXItem getTaxItemByName(String attribute) {

		for (ClientTAXItem item : getAllTaxItem()) {
			if (item.getName() != null && item.getName().equals(attribute)) {
				return item;
			}
		}
		return null;
	}

	protected List<ClientTAXGroup> getRecords() {
		return getCompany().getTaxGroups();
	}

	@Override
	public void setFocus() {
		taxGroupText.setFocus();

	}

	@Override
	protected boolean onCancel() {
		return true;
	}
	// @Override
	// public Object getGridColumnValue(IsSerializable obj, int index) {
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
