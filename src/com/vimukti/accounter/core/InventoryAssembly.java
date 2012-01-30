package com.vimukti.accounter.core;

import java.util.HashSet;
import java.util.Set;

public class InventoryAssembly extends Item {

	private Set<InventoryAssemblyItem> components = new HashSet<InventoryAssemblyItem>();

	private boolean isPurhasedThisItem;

	private Vendor vendor;
}
