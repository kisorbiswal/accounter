package com.vimukti.accounter.core;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.classic.Lifecycle;

/**
 * Stock adjustment POJO.
 * 
 * @author Srikanth.J
 * 
 */
public class StockAdjustment extends Transaction implements Lifecycle {

	private static final long serialVersionUID = -2706077052390641514L;

	private Set<StockAdjustmentItem> adjustmentItems;

	private boolean completed;
	
	public StockAdjustment() {
		adjustmentItems = new HashSet<StockAdjustmentItem>();
	}

	public void add(StockAdjustmentItem adjustmentItem) {
		adjustmentItems.add(adjustmentItem);
	}

	public Set<StockAdjustmentItem> getAdjustmentItems() {
		return adjustmentItems;
	}

	@Override
	public Account getEffectingAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Payee getInvolvedPayee() {
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

	public boolean isCompleted() {
		return completed;
	}

	@Override
	public boolean isDebitTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPositiveTransaction() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean remove(StockAdjustmentItem item) {
		return adjustmentItems.remove(item);
	}

	public void setAdjustmentItems(Set<StockAdjustmentItem> adjustmentItems) {
		this.adjustmentItems = adjustmentItems;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	@Override
	public String toString() {
		return null;
	}

	/**
	 * Calculates the total adjustment price based on the individual items
	 * adjustment prices.
	 * 
	 * @return sum of individual adjumentPrices.
	 */
	public double totalAdjustmentPrice() {
		double totalPrice = 0;
		for (StockAdjustmentItem item : adjustmentItems) {
			totalPrice += item.getAdjustmentPriceValue();
		}
		return totalPrice;
	}

}
