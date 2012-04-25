package com.vimukti.accounter.core;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.json.JSONException;

import com.vimukti.accounter.core.change.ChangeTracker;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.exception.AccounterException;

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

		Double total = 0.0;

		for (InventoryAssemblyItem assemblyItem : inventoryAssembly
				.getComponents()) {

			Item inventoryItem = assemblyItem.getInventoryItem();
			TransactionItem transactionItem = new TransactionItem();
			transactionItem.setType(TransactionItem.TYPE_ITEM);
			transactionItem.setTransaction(this);
			Quantity quantity = assemblyItem.getQuantity().copy();
			quantity.setValue(quantity.getValue() * quantityToBuild);
			transactionItem.setQuantity(quantity);
			transactionItem.setUnitPrice(assemblyItem.getUnitPrice());
			total += assemblyItem.getUnitPrice();
			transactionItem.setItem(inventoryItem);
			transactionItem.setDescription(Global.get().messages()
					.buildAssembly());
			transactionItem.setWareHouse(assemblyItem.getWarehouse());
			transactionItem.setLineTotal(assemblyItem.getQuantity()
					.calculatePrice(assemblyItem.getUnitPrice()));
			getTransactionItems().add(transactionItem);
		}
		this.total = total;

		Quantity quantityToBuild = inventoryAssembly.getOnhandQty().copy();
		quantityToBuild.setValue(this.quantityToBuild);

		inventoryAssembly.setOnhandQuantity(inventoryAssembly.getOnhandQty()
				.add(quantityToBuild));

		session.update(inventoryAssembly);
		ChangeTracker.put(inventoryAssembly);
		return super.onSave(session);
	}

	@Override
	public void onEdit(Transaction clonedObject) throws AccounterException {
		super.onEdit(clonedObject);
		if (isDraftOrTemplate()) {
			return;
		}

		if (isBecameVoid()) {
			doReverseEffect();
		}
	}

	@Override
	public boolean onDelete(Session session) throws CallbackException {
		if (!isVoid() && !isDraftOrTemplate()) {
			super.onDelete(session);
			doReverseEffect();
		}
		return false;
	}

	private void doReverseEffect() {
		Session session = HibernateUtil.getCurrentSession();
		Quantity quantityToBuild = getInventoryAssembly().getOnhandQty().copy();
		quantityToBuild.setValue(this.getQuantityToBuild());
		getInventoryAssembly()
				.setOnhandQuantity(
						getInventoryAssembly().getOnhandQty().subtract(
								quantityToBuild));
		session.update(getInventoryAssembly());
		ChangeTracker.put(getInventoryAssembly());
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

	@Override
	public void getEffects(ITransactionEffects e) {
		Quantity quantityToBuild = inventoryAssembly.getOnhandQty().copy();
		quantityToBuild.setValue(this.quantityToBuild);
		e.add(getInventoryAssembly(), quantityToBuild, 0.00D,
				inventoryAssembly.getWarehouse());
		for (TransactionItem tItem : getTransactionItems()) {
			Item item = tItem.getItem();
			e.add(item, tItem.getQuantity().reverse(),
					tItem.getUnitPriceInBaseCurrency(), tItem.getWareHouse());
			double purchaseValue = tItem.getQuantity().calculatePrice(
					tItem.getUnitPriceInBaseCurrency());
			e.add(item.getExpenseAccount(), purchaseValue, 1);
			e.add(getInventoryAssembly().getAssestsAccount(), -purchaseValue, 1);
		}
	}

	@Override
	public void selfValidate() throws AccounterException {
		super.selfValidate();
		if (inventoryAssembly == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().assemblyItem());
		} else if (!inventoryAssembly.isActive) {
			throw new AccounterException(AccounterException.ERROR_ACTIVE_ITEM);
		}
		if (quantityToBuild <= 0) {
			throw new AccounterException(
					AccounterException.ERROR_QUANTITY_ZERO_OR_NEGATIVE, Global
							.get().messages().quantityToBuild());
		}
	}
}
