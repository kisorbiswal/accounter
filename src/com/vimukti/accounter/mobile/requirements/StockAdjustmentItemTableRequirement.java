package com.vimukti.accounter.mobile.requirements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.ItemStatus;
import com.vimukti.accounter.core.Measurement;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.core.Warehouse;
import com.vimukti.accounter.mobile.CommandList;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Record;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.utils.CommandUtils;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientStockAdjustmentItem;

public abstract class StockAdjustmentItemTableRequirement extends
		AbstractTableRequirement<ClientStockAdjustmentItem> {

	private static final String ITEM = "inventoryItems";
	private static final String COMMENT = "comment";
	private static final String ADJUSTMENT_QTY = "adjustmentQty";

	public StockAdjustmentItemTableRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, true, false, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new ItemRequirement(ITEM, getMessages().pleaseSelect(
				getMessages().item()), getMessages().item(), false, true,
				new ChangeListner<Item>() {

					@Override
					public void onSelection(Item item) {
						ClientQuantity value2 = get(ADJUSTMENT_QTY).getValue();
						value2.setUnit(item.getMeasurement().getDefaultUnit()
								.getID());

						QuantityRequirement requirement = (QuantityRequirement) get(ADJUSTMENT_QTY);
						requirement.setDefaultUnit(item.getMeasurement()
								.getDefaultUnit());
					}

				}, true) {

			@Override
			protected List<Item> getLists(Context context) {
				return getItems();
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("createInventoryItem");
			}
		});

		list.add(new StringRequirement(COMMENT, getMessages().pleaseEnter(
				getMessages().comment()), getMessages().comment(), true, true));

		list.add(new QuantityRequirement(ADJUSTMENT_QTY, getMessages()
				.pleaseEnter(getMessages().adjustedQty()), getMessages()
				.adjustedQty(), false) {

			@Override
			protected List<Unit> getLists(Context context) {
				return StockAdjustmentItemTableRequirement.this.getUnits();
			}
		});
	}

	protected List<Unit> getUnits() {
		Item item = get(ITEM).getValue();
		Set<Unit> units = item.getMeasurement().getUnits();
		return new ArrayList<Unit>(units);
	}

	protected List<Item> getItems() {
		List<Item> returnedItems = new ArrayList<Item>();
		Set<Item> items = getCompany().getItems();
		for (Item item : items) {
			if (item.isActive() && item.getType() == Item.TYPE_INVENTORY_PART) {
				returnedItems.add(item);
			}
		}
		return returnedItems;
	}

	@Override
	protected String getEmptyString() {
		return getMessages().noRecordsToShow();
	}

	@Override
	protected void getRequirementsValues(ClientStockAdjustmentItem obj) {
		Item item = get(ITEM).getValue();
		obj.setItem(item.getID());
		String comment = get(COMMENT).getValue();
		obj.setComment(comment);
		ClientQuantity clientQuantity = get(ADJUSTMENT_QTY).getValue();
		obj.setAdjustedQty(clientQuantity);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientStockAdjustmentItem obj) {
		Item item = (Item) CommandUtils.getServerObjectById(obj.getItem(),
				AccounterCoreType.ITEM);
		get(ITEM).setValue(item);
		get(COMMENT).setValue(obj.getComment());
		get(ADJUSTMENT_QTY).setValue(obj.getAdjustedQty());
	}

	@Override
	protected ClientStockAdjustmentItem getNewObject() {
		ClientStockAdjustmentItem adjustmentItem = new ClientStockAdjustmentItem();
		return adjustmentItem;
	}

	@Override
	protected Record createFullRecord(ClientStockAdjustmentItem t) {
		Record record = new Record(t);
		Item item = (Item) CommandUtils.getServerObjectById(t.getItem(),
				AccounterCoreType.ITEM);
		record.add(getMessages().itemName(), item != null ? item.getName() : "");
		record.add(getMessages().comment(), t.getComment());
		record.add(getMessages().availableQty(), getAvailableQuantity(item));

		Unit clientUnit = (Unit) CommandUtils.getServerObjectById(t
				.getAdjustedQty().getUnit(), AccounterCoreType.UNIT);
		if (clientUnit != null) {
			record.add(getMessages().adjustedQty(), t.getAdjustedQty()
					.getValue() + " " + clientUnit.getType());
		} else {
			record.add(getMessages().adjustedQty(), t.getAdjustedQty()
					.getValue());
		}
		return record;
	}

	protected String getAvailableQuantity(Item item) {
		StringBuffer result = new StringBuffer();
		if (item != null) {
			Warehouse warehouse = getWareHouse();
			if (warehouse != null) {
				ItemStatus itemStatus = null;
				for (ItemStatus is : warehouse.getItemStatuses()) {
					if (is.getItem().getID() == item.getID()) {
						itemStatus = is;
						break;
					}
				}
				if (itemStatus != null) {
					Unit unit = itemStatus.getQuantity().getUnit();
					result.append(itemStatus.getQuantity().getValue());
					result.append(" ");
					result.append(unit.getType());
				} else {
					Measurement measurement = item.getMeasurement();
					result.append(0.0);
					result.append(" ");
					if (measurement != null) {
						result.append(measurement.getDefaultUnit().getType());
					}
				}
			}
		}
		return result.toString();
	}

	protected abstract Warehouse getWareHouse();

	@Override
	protected List<ClientStockAdjustmentItem> getList() {
		return null;
	}

	@Override
	protected Record createRecord(ClientStockAdjustmentItem t) {
		return createFullRecord(t);
	}

	@Override
	protected String getAddMoreString() {
		return getMessages().addMore(getMessages().items());
	}

}
