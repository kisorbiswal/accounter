package com.vimukti.accounter.core;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

public class InventoryAssembly extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4556028000041106619L;

	private Set<InventoryAssemblyItem> components = new HashSet<InventoryAssemblyItem>();

	private boolean isPurhasedThisItem;

	public InventoryAssembly() {
		// TODO Auto-generated constructor stub
	}

	public Set<InventoryAssemblyItem> getComponents() {
		return components;
	}

	public void setComponents(Set<InventoryAssemblyItem> components) {
		this.components = components;
	}

	public boolean isPurhasedThisItem() {
		return isPurhasedThisItem;
	}

	public void setPurhasedThisItem(boolean isPurhasedThisItem) {
		this.isPurhasedThisItem = isPurhasedThisItem;
	}
}
