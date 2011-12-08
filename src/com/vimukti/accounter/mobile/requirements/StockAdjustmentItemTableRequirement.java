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
import com.vimukti.accounter.web.client.core.ClientStockAdjustmentItem;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public abstract class StockAdjustmentItemTableRequirement extends
		AbstractTableRequirement<ClientStockAdjustmentItem> {

	private static final String ITEM = "inventoryItems";
	private static final String COMMENT = "comment";
	private static final String AVAILABLE_QTY = "availableQty";
	private static final String ADJUSTMENT_QTY = "adjustmentQty";

	public StockAdjustmentItemTableRequirement(String requirementName,
			String enterString, String recordName) {
		super(requirementName, enterString, recordName, true, false, true);
	}

	@Override
	protected void addRequirement(List<Requirement> list) {
		list.add(new ItemRequirement(ITEM, getMessages().pleaseSelect(
				getMessages().item()), getMessages().item(), false, true, null,
				true) {

			@Override
			protected List<Item> getLists(Context context) {
				return getItems();
			}

			@Override
			protected void setCreateCommand(CommandList list) {
				list.add("create inventory item");
			}
		});

		list.add(new StringRequirement(COMMENT, getMessages().pleaseEnter(
				getMessages().comment()), getMessages().comment(), true, true));
		StringRequirement adjustmentQty = new StringRequirement(AVAILABLE_QTY,
				getMessages().pleaseEnter(getMessages().availableQty()),
				getMessages().availableQty(), true, true) {
			@Override
			public String getValue() {
				return getAvailableQuantity((Item) get(ITEM).getValue());
			}
		};
		adjustmentQty.setEditable(false);
		list.add(adjustmentQty);

		list.add(new AmountRequirement(ADJUSTMENT_QTY, getMessages()
				.pleaseEnter(getMessages().adjustmentQty()), getMessages()
				.adjustmentQty(), true, true) {
			@Override
			public void setValue(Object value) {
				Double amount = (Double) value;
				if (amount != null && DecimalUtil.isEquals(amount, 0)) {
					amount = (double) 1;
				}
				super.setValue(amount);
			}
		});
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
		double value = get(ADJUSTMENT_QTY).getValue();
		obj.getAdjustmentQty().setUnit(
				getCompany().getDefaultMeasurement().getDefaultUnit().getID());
		obj.getAdjustmentQty().setValue(value);
	}

	@Override
	protected void setRequirementsDefaultValues(ClientStockAdjustmentItem obj) {
		Item item = (Item) CommandUtils.getServerObjectById(obj.getItem(),
				AccounterCoreType.ITEM);
		get(ITEM).setValue(item);
		get(COMMENT).setValue(obj.getComment());
		get(AVAILABLE_QTY).setValue(getAvailableQuantity(item));
		get(ADJUSTMENT_QTY).setValue(obj.getAdjustmentQty().getValue());
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
		record.add(getMessages().availableQty(), t.getAdjustmentQty()
				.getValue());
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
