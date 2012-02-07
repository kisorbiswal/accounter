package com.vimukti.accounter.core;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.Session;

import com.vimukti.accounter.utils.HibernateUtil;

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

	public void buildAssembly(BuildAssembly buildAssembly,
			Double quantityToBuild) {
		Session session = HibernateUtil.getCurrentSession();
		Account assemblyAssetsAccount = getAssestsAccount();
		double totalAssemblyCost = 0.00D;
		for (InventoryAssemblyItem assemblyItem : getComponents()) {
			double itemCost = assemblyItem.getQuantity().calculatePrice(
					assemblyItem.getCostByActiveScheme());
			itemCost = itemCost * quantityToBuild;
			totalAssemblyCost += itemCost;
			Account itemAssestsAccount = assemblyItem.getInventoryItem()
					.getAssestsAccount();
			itemAssestsAccount.updateCurrentBalance(buildAssembly, itemCost, 1);
			session.update(itemAssestsAccount);

			Item inventoryItem = assemblyItem.getInventoryItem();
			inventoryItem.setOnhandQuantity(inventoryItem.getOnhandQty()
					.subtract(assemblyItem.getQuantity()));
			session.update(inventoryItem);
		}

		assemblyAssetsAccount.updateCurrentBalance(buildAssembly,
				-totalAssemblyCost, 1);
		session.update(assemblyAssetsAccount);
		Quantity onhandQuantity = getOnhandQty();
		onhandQuantity.setValue(onhandQuantity.getValue() + quantityToBuild);
		setOnhandQuantity(onhandQuantity);
		session.update(this);
	}
}
