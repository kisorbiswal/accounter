package com.vimukti.accounter.mobile.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vimukti.accounter.core.InventoryAssemblyItem;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.Result;
import com.vimukti.accounter.mobile.requirements.InventoryAssemblyItemTableRequirement;
import com.vimukti.accounter.web.client.core.ClientInventoryAssembly;
import com.vimukti.accounter.web.client.core.ClientInventoryAssemblyItem;
import com.vimukti.accounter.web.client.core.ClientItem;

public class CreateInventoryAssemblyItemCommand extends
		CreateInventoryItemCommand {
	private static final String INVENTORYITEMS = "inventoryItems";

	public CreateInventoryAssemblyItemCommand() {
		super(ClientItem.TYPE_INVENTORY_ASSEMBLY);
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		super.addRequirements(list);
		list.add(new InventoryAssemblyItemTableRequirement(INVENTORYITEMS,
				getMessages().pleaseEnter(getMessages().name()), getMessages()
						.items(), true, true) {
		});
	}

	@Override
	protected String getWelcomeMessage() {
		return getItem().getID() == 0 ? getMessages().creating(
				getMessages().inventoryItem())
				: "Updating Inventory Assembly Item";
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	protected Result onCompleteProcess(Context context) {
		List<ClientInventoryAssemblyItem> items = get(INVENTORYITEMS)
				.getValue();
		Set<ClientInventoryAssemblyItem> set = new HashSet<ClientInventoryAssemblyItem>();
		set.addAll(items);
		((ClientInventoryAssembly) getItem()).setComponents(set);
		return super.onCompleteProcess(context);
	}

	protected List<InventoryAssemblyItem> getItems() {
		List<InventoryAssemblyItem> returnedItems = new ArrayList<InventoryAssemblyItem>();
		Set<Item> items = getCompany().getItems();
		for (Item item : items) {
			if (item.isActive() && item.getType() == Item.TYPE_INVENTORY_PART) {
				InventoryAssemblyItem assemblyItem = new InventoryAssemblyItem();
				assemblyItem.setInventoryItem(item);
				assemblyItem.setUnitPrice(item.getPurchasePrice());
				assemblyItem.setDiscription(item.getPurchaseDescription());
				assemblyItem.setWarehouse(item.getWarehouse());
				returnedItems.add(assemblyItem);
			}
		}
		return returnedItems;
	}
}
