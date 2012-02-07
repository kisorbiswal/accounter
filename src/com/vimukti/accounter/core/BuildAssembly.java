package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

public class BuildAssembly extends Transaction {

	/**
	 * 
	 */

	private InventoryAssembly inventoryAssembly;
	private Double quantityToBuild;

	private static final long serialVersionUID = -5038297775075086681L;

	public BuildAssembly() {
		super();
		setType(TYPE_BUILD_ASSEMBLY);
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		Account assemblyAssetsAccount = inventoryAssembly.getAssestsAccount();
		double totalAssemblyCost = 0.00D;
		for (InventoryAssemblyItem item : inventoryAssembly.getComponents()) {
			double itemCost = item.getQuantity().calculatePrice(
					item.getCostByActiveScheme());
			itemCost = itemCost * quantityToBuild;
			totalAssemblyCost += itemCost;
			Account itemAssestsAccount = item.getInventoryItem()
					.getAssestsAccount();
			itemAssestsAccount.updateCurrentBalance(this, itemCost, 1);
		}

		assemblyAssetsAccount.updateCurrentBalance(this, -totalAssemblyCost, 1);

		Quantity onhandQuantity = inventoryAssembly.getOnhandQty();
		onhandQuantity.setValue(onhandQuantity.getValue() + quantityToBuild);

		return super.onSave(session);
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isPositiveTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Account getEffectingAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Payee getPayee() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTransactionCategory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Payee getInvolvedPayee() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void updatePayee(boolean onCreate) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the inventoryAssembly
	 */
	public InventoryAssembly getInventoryAssembly() {
		return inventoryAssembly;
	}

	/**
	 * @param inventoryAssembly
	 *            the inventoryAssembly to set
	 */
	public void setInventoryAssembly(InventoryAssembly inventoryAssembly) {
		this.inventoryAssembly = inventoryAssembly;
	}

	/**
	 * @return the quantityToBuild
	 */
	public Double getQuantityToBuild() {
		return quantityToBuild;
	}

	/**
	 * @param quantityToBuild
	 *            the quantityToBuild to set
	 */
	public void setQuantityToBuild(Double quantityToBuild) {
		this.quantityToBuild = quantityToBuild;
	}

}
