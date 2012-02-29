package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.core.change.ChangeTracker;

public class BuildAssembly extends Transaction {

	private InventoryAssembly inventoryAssembly;
	private Double quantityToBuild;

	private static final long serialVersionUID = -5038297775075086681L;

	public BuildAssembly() {
		super();
		setType(TYPE_BUILD_ASSEMBLY);
	}

	@Override
	public boolean onSave(Session session) throws CallbackException {

		if (number == null) {
			String number = NumberUtils.getNextTransactionNumber(
					Transaction.TYPE_BUILD_ASSEMBLY, getCompany());
			setNumber(number);
		}

		double totalAssemblyCost = 0.00D;

		Quantity quantityToBuild = inventoryAssembly.getOnhandQty().copy();
		quantityToBuild.setValue(this.quantityToBuild);

		for (InventoryAssemblyItem assemblyItem : inventoryAssembly
				.getComponents()) {

			Item inventoryItem = assemblyItem.getInventoryItem();
			TransactionItem transactionItem = new TransactionItem();
			transactionItem.setType(TransactionItem.TYPE_ITEM);
			transactionItem.setTransaction(this);
			transactionItem.setQuantity(assemblyItem.getQuantity().multiply(
					quantityToBuild));
			transactionItem.setUnitPrice(assemblyItem.getUnitPrice());

			transactionItem.setItem(inventoryItem);
			transactionItem.setDescription("Build Assembly");
			transactionItem.setWareHouse(assemblyItem.getWarehouse());
			transactionItem.setLineTotal(assemblyItem.getQuantity()
					.calculatePrice(assemblyItem.getUnitPrice()));
			getTransactionItems().add(transactionItem);
			totalAssemblyCost += transactionItem.getQuantity().calculatePrice(
					transactionItem.getUnitPrice());
		}

		inventoryAssembly.setOnhandQuantity(inventoryAssembly.getOnhandQty()
				.add(quantityToBuild));

		session.update(inventoryAssembly);
		ChangeTracker.put(inventoryAssembly);
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
		return Transaction.CATEGORY_CUSTOMER;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_BUILD_ASSEMBLY;
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
