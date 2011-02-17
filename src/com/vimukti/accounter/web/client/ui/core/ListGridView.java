//package com.vimukti.accounter.web.client.ui.core;
//
//import java.awt.Menu;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.i18n.client.NumberFormat;
//import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.VerticalPanel;
//import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
//import com.vimukti.accounter.web.client.core.RecordDoubleClickHandler;
//import com.vimukti.accounter.web.client.ui.RecordDeleteHandler;
//import com.vimukti.accounter.web.client.ui.grids.EditCompleteHandler;
//import com.vimukti.accounter.web.client.ui.widgets.ListGrid;
//
//
///**
// * This class is for create ListGrid with editable cells,if records can be
// * different types in listgrid, than this can use
// * 
// * @author kumar kasimala
// * 
// */
//public class ListGridView extends VerticalPanel {
//
//	private Menu popupMenu;
//
//	public ListGridField imageField;
//	private ListGridRecord emptyRecord;
//
//	private ListGrid listgrid;
//
//	private List<ListGridRecord> records = new ArrayList<ListGridRecord>();
//	private List<Label> labels = new ArrayList<Label>();
//	private List<ListGridField> fields = new ArrayList<ListGridField>();
//
//	private HorizontalPanel HorizontalPanel;
//
////	private IntegerRangeValidator integerValidator;
//
//	private ListGridField deleteField;
//	private boolean isMenuEnable = true;
//
//	protected boolean isEdit;
//
//	private String imageName;
//
//	private RecordDoubleClickHandler doubleClickHandler;
//
//	private Map<String, String> defaultValues = new HashMap<String, String>();
//
//	private boolean isShowDefaultValues;
//	private boolean showRecordImage = true;
//
////	private SelectionAppearance selectionAppearance;
//
//	private RecordDeleteHandler recordDeleteHandler;
//	private RecordAddhandler recordAddhandler;
//	private RecordAddCompleteHandler addCompleteHandler;
//
//	private boolean isDeleteEnable = true;
//	protected int transactionDomain;
//
//	public static final int NON_TRANSACTIONAL = 0;
//	public static final int CUSTOMER_TRANSACTION = 1;
//	public static final int VENDOR_TRANSACTION = 2;
//	public static final int BANKING_TRANSACTION = 3;
//
//	public ListGridView(int transactionDomain) {
//		this.transactionDomain = transactionDomain;
//		listgrid = new ListGrid();
//		initGrid();
//		initMenu();
//	}
//
//	public ListGridView(SelectionAppearance appearance) {
//		listgrid = new ListGrid();
////		this.selectionAppearance = appearance;
//		initGrid();
//		initMenu();
//	}
//
//	public void initGrid() {
//		setWidth("100%");
////		setHeight(200);
//
//		listgrid.setWidth("100%");
////		listgrid.setSelectionType(SelectionStyle.SINGLE);
////		if (this.selectionAppearance != null)
////			listgrid.setSelectionAppearance(selectionAppearance);
//		listgrid.setHeight(200);
//		listgrid.setAlternateRecordStyles(true);
//		listgrid.setShowAllRecords(true);
//		listgrid.setCellHeight(20);
//		listgrid.setWrapCells(true);
//		listgrid.setAutoFetchData(true);
//		listgrid.setCanEdit(true);
//		listgrid.setCanSort(false);
//		listgrid.setCanGroupBy(false);
//		listgrid.setCanFreezeFields(false);
//		listgrid.setCanResizeFields(false);
//
//		listgrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
//
//		listgrid.addEditFailedHandler(new EditFailedHandler() {
//
//			public void onEditFailed(EditFailedEvent event) {
//				listgrid.discardAllEdits();
//			}
//		});
//
//		imageField = new ListGridField("", " ");
//		imageField.setWidth("20");
//		imageField.setName("image");
////		imageField.setAlign(Alignment.LEFT);
//		imageField.setType(ListGridFieldType.IMAGE);
//		imageField.setImageURLPrefix("/images/icons/");
//		imageField.setImageURLSuffix(".png");
//		imageField.setCanEdit(false);
//		imageField.addRecordClickHandler(new RecordClickHandler() {
//
//			public void onRecordClick(RecordClickEvent event) {
//				if (isMenuEnable)
//					popupMenu.showContextMenu();
//				else if (!isEdit) {
//					addEmptyRecord("");
//				}
//
//			}
//		});
//
//		deleteField = new ListGridField("", " ");
//		deleteField.setWidth("20");
//		deleteField.setName("delete");
////		deleteField.setAlign(Alignment.LEFT);
//		deleteField.setType(ListGridFieldType.IMAGE);
//		deleteField.setImageURLPrefix("/images/icons/");
//		deleteField.setImageURLSuffix(".png");
//		deleteField.setCanEdit(false);
//		deleteField.addRecordClickHandler(new RecordClickHandler() {
//
//			public void onRecordClick(RecordClickEvent event) {
//				if (isDeleteEnable) {
//					if (!(recordDeleteHandler == null || recordDeleteHandler
//							.onRecordDelete((ListGridRecord) event.getRecord())))
//						return;
//					deleteRecord((ListGridRecord) event.getRecord());
//
//				}
//
//			}
//
//		});
//		HorizontalPanel = new HorizontalPanel();
//		HorizontalPanel.setStyleName("listgridViewBottom");
//		HorizontalPanel.setWidth("100%");
//
//		recordAddhandler = new RecordAddhandler() {
//
//			public boolean onRecordAdd(ListGridRecord record) {
//				return true;
//			}
//		};
//		listgrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
//
//			public void onCellDoubleClick(CellDoubleClickEvent event) {
//				canEditCurrentRecord();
//			}
//		});
//
//		addEmptyRecord("none");
//		add(listgrid);
//		add(HorizontalPanel);
//	}
//
//	public void initMenu() {
//		this.popupMenu = new Menu();
//	}
//
//	/**
//	 * This method is for create empty Record
//	 * 
//	 * @param type
//	 *            is for type of record need to create
//	 */
//	public void addEmptyRecord(String type) {
//
//		boolean isaddContione = recordAddhandler
//				.onRecordAdd(getpreviousRecord());
//		if (isaddContione) {
//			if (emptyRecord != null) {
//				// if record already exist , than change its properties like
//				// type of
//				// record, set delete image & record appropriate images
//
//				emptyRecord.setAttribute("type", type);
//				if (isDeleteEnable)
//					emptyRecord.setAttribute("delete", "delete");
//				emptyRecord.setAttribute("image", imageName);
//				if (isShowDefaultValues)
//					updateDefaultValues();
//				if (addCompleteHandler != null)
//					addCompleteHandler.onRecordAddComplete(emptyRecord);
//			}
//
//			if (transactionDomain == NON_TRANSACTIONAL)
//				emptyRecord = new ListGridRecord();
//			else {
//				emptyRecord = new TransactionItemRecord(transactionDomain);
//				((TransactionItemRecord) emptyRecord).setGrid(getGrid());
//				((TransactionItemRecord) emptyRecord).setType(type);
//			}
//			emptyRecord.setAttribute("image", "add");
//			emptyRecord.setAttribute("type", "none");
//			records.add(emptyRecord);
//
//			listgrid.setRecords(records.toArray(new ListGridRecord[1]));
//		}
//		// change the created record state to editable
//		listgrid.startEditing(listgrid.getRecords().length - 2, 1, true);
//		isShowDefaultValues = true;
//
//	}
//
//	private ListGridRecord getpreviousRecord() {
//		if (listgrid.getRecords().length > 1)
//			return listgrid.getRecord(listgrid.getRecords().length - 2);
//		return null;
//	}
//
//	private void updateDefaultValues() {
//		String[] keys = this.defaultValues.keySet().toArray(
//				new String[this.defaultValues.keySet().size()]);
//		for (int i = 0; i < defaultValues.keySet().size(); i++) {
//			emptyRecord.setAttribute(keys[i], this.defaultValues.get(keys[i]));
//		}
//	}
//
//	public void setSelectionAppearance() {
////		listgrid.setSelectionType(SelectionStyle.SINGLE);
//		listgrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
//	}
//
//	/**
//	 * Disable all Records if it canEdit attribute set true, called when double
//	 * click on Record
//	 */
//	public void canEditCurrentRecord() {
//		if (records.indexOf(listgrid.getSelectedRecord()) == records.size() - 1) {
//			editLastRecord(false);
//		} else
//			editLastRecord(true);
//
//		if (getSelectedRecord().getAttribute("canEdit") != null) {
//			if (!getSelectedRecord().getAttributeAsBoolean("canEdit"))
//				setEditDisableRecord();
//		}
//		if (doubleClickHandler != null)
//			doubleClickHandler.OnCellDoubleClick(getSelectedRecord());
//
//	}
//
//	/**
//	 * This method is for add fields to listgrid
//	 * 
//	 * @param fieldsarry
//	 */
//	public void addFields(ListGridField... fieldsarry) {
//		if (showRecordImage)
//			fields.add(imageField);
//		for (ListGridField field : fieldsarry) {
//			field.setWidth("100%");
//			fields.add(field);
//		}
//		fields.add(deleteField);
//		listgrid.setFields(fields.toArray(fieldsarry));
//		addBottomLabels();
//	}
//
//	private void addBottomLabels() {
//		for (int i = 0; i < listgrid.getFields().length; i++) {
//			Label lab = new Label("");
//			lab.setWidth100();
//			lab.setStyleName("boldcell");
//			labels.add(lab);
//			HorizontalPanel.add(lab);
//		}
//	}
//
//	/**
//	 * This method is for set image name of first colounm
//	 * 
//	 * @param path
//	 */
//	public void setSelectedRecordImagePath(String path) {
//		getSelectedRecord().setAttribute("image", path);
//	}
//
//	public void setImagePath(String path) {
//		this.imageName = path;
//	}
//
//	/**
//	 * This method is change records cells state to editable
//	 * 
//	 * @param canEdit
//	 *            if it is false , than all cells editable false
//	 */
//	public void editLastRecord(boolean canEdit) {
//		for (ListGridField field : this.listgrid.getFields()) {
//			field.setCanEdit(canEdit);
//		}
//		imageField.setCanEdit(false);
//		deleteField.setCanEdit(false);
//	}
//
//	/**
//	 * This method is to make currentRecord editable
//	 */
//	public void makeCurrentRecordEditable() {
//		listgrid.startEditing(listgrid.getRecordIndex(listgrid
//				.getSelectedRecord()), 1, true);
//	}
//
//	/**
//	 * This method is to enable array of cells and disable others
//	 * 
//	 */
//	public void setEditDiableCellsExecpt(ListGridField... fields) {
//		editLastRecord(false);
//		for (ListGridField field : fields) {
//			field.setCanEdit(true);
//		}
//	}
//
//	public void setEditDisableCells(ListGridField... fields) {
//		editLastRecord(false);
//		for (ListGridField field : fields) {
//			field.setCanEdit(false);
//		}
//	}
//
//	public void setEditDisableRecord() {
//		editLastRecord(false);
//		for (ListGridField field : fields) {
//			field.setCanEdit(false);
//		}
//	}
//
//	public ListGridRecord[] getRecords() {
//		List<ListGridRecord> list = new ArrayList<ListGridRecord>();
//		list.addAll(records);
//		list.remove(records.size() - 1);
//		return list.toArray(new ListGridRecord[list.size()]);
//	}
//
//	public void addRecordDoubleClickHandler(
//			RecordDoubleClickHandler doubleClickHandler) {
//		this.doubleClickHandler = doubleClickHandler;
//	}
//
//	public ListGridRecord getSelectedRecord() {
//		return listgrid.getSelectedRecord();
//	}
//
//	/**
//	 * This method is get ListGrid refered variable
//	 * 
//	 * @return
//	 */
//	public ListGrid getGrid() {
//		return listgrid;
//	}
//
//	public void addCellSavedHandler(CellSavedHandler cellSavedHandler) {
//		listgrid.addCellSavedHandler(cellSavedHandler);
//	}
//
//	/**
//	 * This method is set title for label at bottom of particular field or
//	 * coloum.
//	 * 
//	 * @param title
//	 * @param colom
//	 */
//	public void setBottomLabelTitle(String title, int colom) {
//		Label lal = labels.get(colom);
//		lal.setText(title);
//		//FIXME
////		HorizontalPanel.reflowNow();
//	}
//
//	/**
//	 * This method is for enable or disable menu.
//	 * 
//	 * @param enable
//	 *            bydefault this value is true, it means menu enable
//	 */
//	public void setEnableMenu(boolean enable) {
//		this.isMenuEnable = enable;
//	}
//
//	/**
//	 * Add a editComplete handler.
//	 * <p>
//	 * Callback fired when inline edits have been successfully saved.
//	 * <P>
//	 * No default implementation.
//	 * 
//	 * @param handler
//	 *            the editComplete handler
//	 */
//	public void addEditCompleteHandler(EditCompleteHandler editCompleteHandler) {
//		listgrid.addEditCompleteHandler(editCompleteHandler);
//	}
//
//	/**
//	 * This method is validate given field on saving its data
//	 * 
//	 * @param fields
//	 */
//	public void addToIntegerValidator(ListGridField fields) {
//		fields.setType(ListGridFieldType.INTEGER);
//		fields.setCellFormatter(new CellFormatter() {
//			public String format(Object value, ListGridRecord record,
//					int rowNum, int colNum) {
//				if (value == null)
//					return null;
//				NumberFormat nf = NumberFormat.getFormat("0,000");
//				try {
//					return nf.format(((Number) value).longValue());
//				} catch (Exception e) {
//					return value.toString();
//				}
//			}
//		});
//		integerValidator = new IntegerRangeValidator();
//		integerValidator.setMin(1);
//		fields.setValidators(integerValidator);
//	}
//
//	/**
//	 * This method is validate given fields on saving its data
//	 * 
//	 * @param fields
//	 */
//	public void addToIntegerValidator(ListGridField... fields) {
//		for (ListGridField field : fields)
//			addToIntegerValidator(field);
//	}
//
//	/**
//	 * This method is to create menu item.
//	 * 
//	 * @param title
//	 *            is name of menu item
//	 * @param clickHandler
//	 *            is the click handler for menu item
//	 */
//	public void addMenuItem(String title, String image,
//			ClickHandler clickHandler) {
//		//FIXME
////		MenuItem menuitem = new MenuItem(title);
////		menuitem.setIcon(image);
////		menuitem.addClickHandler(clickHandler);
////		this.popupMenu.addItem(menuitem);
//	}
//
//	public void setCanEdit(boolean canEdit) {
//		isEdit = true;
//		listgrid.setCanEdit(canEdit);
//	}
//
//	/**
//	 * add listener on record delete,implement listener if you want to do on
//	 * record delete
//	 * 
//	 * @param deleteHandler
//	 *            ;
//	 */
//	public void addRecordDeleteHandler(RecordDeleteHandler deleteHandler) {
//		this.recordDeleteHandler = deleteHandler;
//	}
//
//	public void deleteRecord(final ListGridRecord record) {
//		if (listgrid.getRecords().length != listgrid.getRecordIndex(record) + 1) {
//			records.remove(record);
//			 listgrid.removeData(record);
//			// listgrid.removeData(record, new DSCallback() {
//			//
//			// public void execute(DSResponse response, Object rawData,
//			// DSRequest request) {
//			//
//			// if (record instanceof TransactionItemRecord) {
//			//
//			// transactionItemRecordDeleted();
//			//
//			// }
//			// }
//			//
//			// });
//		}
//	}
//
//	protected void transactionItemRecordDeleted() {
//		// Implemented in SubClasses
//	}
//
//	public void deleteRecord(int index) {
//		if (listgrid.getRecords().length != index + 1) {
//			records.remove(index);
//			deleteRecord(listgrid.getRecord(index));
//		}
//	}
//
//	/**
//	 * This method is to clear present selected record Data,call when you want
//	 * fresh data.
//	 */
//	public void clearRecord(ListGridRecord gridRecord) {
//		for (String att : gridRecord.getAttributes()) {
//			if (!att.equals("image") && !att.equals("delete")
//					&& !att.contains("_")) {
//				listgrid.getSelectedRecord().setAttribute(att, "");
//			}
//		}
//	}
//
//	public void addRecords(ListGridRecord... records) {
//		ListGridRecord emRecord = this.records.remove(0);
//		this.listgrid.removeData(emRecord);
//		for (ListGridRecord record : records) {
//			if (record != null) {
//				if (isDeleteEnable)
//					record.setAttribute("delete", "delete");
//				if (imageName != null)
//					record.setAttribute("image", imageName);
//				this.records.add(record);
//			}
//		}
//		this.records.add(emRecord);
//		this.listgrid.setRecords(this.records.toArray(records));
//	}
//
//	public void selectRecord(int index) {
//		this.listgrid.selectRecord(index);
//	}
//
//	public void selectRecord(ListGridRecord record) {
//		this.listgrid.selectRecord(record);
//	}
//
//	/**
//	 * check the whether listgrid contains records or not
//	 * 
//	 * @return
//	 */
//	public boolean isEmpty() {
//		return this.listgrid.getRecords().length == 1;
//	}
//
//	/**
//	 * Method is to setDefalut values to each field
//	 * 
//	 * @param fieldName
//	 *            is the name of field
//	 * @param value
//	 *            is the default value
//	 */
//	public void setDefaultValue(String fieldName, String value) {
//		this.defaultValues.put(fieldName, value);
//	}
//
//	public void setDefaultValues(Map<String, String> values) {
//		this.defaultValues = values;
//	}
//
//	/**
//	 * This method is to tell whether need add default values to record or not
//	 * on onCreating Record, by default showDefaultValus is true.
//	 * 
//	 * @param show
//	 */
//	public void isShowDefaultValues(boolean show) {
//		this.isShowDefaultValues = show;
//	}
//
//	public void showImageForRecord(boolean b) {
//		this.showRecordImage = b;
//	}
//
//	public void setSelectionAppearance(SelectionAppearance appearance) {
//		this.selectionAppearance = appearance;
//	}
//
//	public void deleteAllRecords() {
//		for (ListGridRecord record : getRecords())
//			deleteRecord(record);
//	}
//
//	/**
//	 * call
//	 * 
//	 * @param addhandler
//	 */
//	public void addRecordAddHandler(RecordAddhandler addhandler) {
//		this.recordAddhandler = addhandler;
//	}
//
//	public void setStopOnErrors(Boolean stopOnErrors) {
//		this.listgrid.setStopOnErrors(stopOnErrors);
//	}
//
//	public void setSaveByCell(Boolean saveByCell) {
//		this.listgrid.setSaveByCell(saveByCell);
//	}
//
//	public void addRecordAddCompletedHandler(
//			RecordAddCompleteHandler completeHandler) {
//		this.addCompleteHandler = completeHandler;
//	}
//
//	/**
//	 * set Enable or disable delete button on each record, by default
//	 * isDeleteEnable is true
//	 * 
//	 * @param enable
//	 */
//
//	public void setIsDeleteEnable(boolean isDeleteEnable) {
//		this.isDeleteEnable = isDeleteEnable;
//	}
//
//	public void setEditEvent(ListGridEditEvent gridEditEvent) {
//		this.listgrid.setEditEvent(gridEditEvent);
//	}
//
//}
