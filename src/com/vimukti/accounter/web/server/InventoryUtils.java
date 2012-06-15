package com.vimukti.accounter.web.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.hibernate.Query;
import org.hibernate.Session;

import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.CompanyPreferences;
import com.vimukti.accounter.core.Item;
import com.vimukti.accounter.core.Quantity;
import com.vimukti.accounter.core.TransactionItem;
import com.vimukti.accounter.core.Unit;
import com.vimukti.accounter.utils.HibernateUtil;
import com.vimukti.accounter.web.client.exception.AccounterException;

public class InventoryUtils {

	public static void remapSalesPurchases(List<Item> items)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		for (Item item : items) {
			long itemID = item.getID();
			List<TransactionItem> sales = getSales(itemID);
			int inventoryScheme = item.getActiveInventoryScheme();
			List<InventoryDetails> purchases = getPurchases(itemID,
					inventoryScheme);
			item.setAverageCost(getAverageCost(item));
			adjustSales(
					item,
					inventoryScheme == CompanyPreferences.INVENTORY_SCHME_AVERAGE,
					sales, purchases);
			item.onUpdate(session);
			session.saveOrUpdate(item);
		}
	}

	private static void adjustSales(Item item, boolean isSchemeAvarage,
			List<TransactionItem> sales, List<InventoryDetails> purchases)
			throws AccounterException {
		if (sales.isEmpty()) {
			return;
		}
		InventoryEffects iEffects = new InventoryEffects();

		for (TransactionItem inventorySale : sales) {
			Quantity salesQty = inventorySale.getQuantityCopy();
			Map<Double, Quantity> purchaseForThisSale = new HashMap<Double, Quantity>();
			Iterator<InventoryDetails> purchaseIterator = purchases.iterator();
			while (purchaseIterator.hasNext()) {
				InventoryDetails next = purchaseIterator.next();
				Quantity purchaseQty = next.quantity;
				int compareTo = purchaseQty.compareTo(salesQty);
				if (compareTo < 0) {
					Quantity quantity = purchaseForThisSale.get(next.cost);
					if (quantity != null) {
						purchaseQty = purchaseQty.add(quantity);
					}
					purchaseForThisSale.put(next.cost, purchaseQty);
					purchaseIterator.remove();
					salesQty = salesQty.subtract(purchaseQty);
					continue;
				} else if (compareTo > 0) {
					purchaseQty = purchaseQty.subtract(salesQty);
					Quantity qtyToPut = salesQty.copy();
					Quantity quantity = purchaseForThisSale.get(next.cost);
					if (quantity != null) {
						qtyToPut = qtyToPut.add(quantity);
					}
					purchaseForThisSale.put(next.cost, qtyToPut);
					next.quantity = purchaseQty;
					salesQty.setValue(0.00D);
					break;
				} else {
					Quantity quantity = purchaseForThisSale.get(next.cost);
					if (quantity != null) {
						purchaseQty = purchaseQty.add(quantity);
					}
					purchaseForThisSale.put(next.cost, purchaseQty);
					purchaseIterator.remove();
					salesQty.setValue(0.00D);
					break;
				}
			}
			iEffects.addPurchase(inventorySale, purchaseForThisSale,
					isSchemeAvarage, isSchemeAvarage ? item.getAverageCost()
							: 0);

			// inventorySale.modifyPurchases(purchaseForThisSale,
			// isSchemeAvarage,
			// isSchemeAvarage ? item.getAverageCost() : 0);
		}
		iEffects.doEffect();
	}

	private static List<TransactionItem> getSales(long itemID) {
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		try {
			session.setFlushMode(FlushMode.COMMIT);
			Query query = session.getNamedQuery("list.InventorySales")
					.setParameter("itemId", itemID);

			return query.list();
		} finally {
			session.setFlushMode(flushMode);
		}
	}

	private static List<InventoryDetails> getPurchases(long itemID,
			int activeInventoryScheme) {
		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		List<InventoryDetails> details = new ArrayList<InventoryDetails>();
		try {
			session.setFlushMode(FlushMode.COMMIT);
			Query query = null;
			if (activeInventoryScheme == CompanyPreferences.INVENTORY_SCHME_LIFO) {
				query = session.getNamedQuery("getPurchasesOfItem.for.LIFO")
						.setParameter("inventoryId", itemID);
			} else {
				query = session.getNamedQuery("getPurchasesOfItem")
						.setParameter("inventoryId", itemID);
			}

			Iterator<Object[]> result = query.list().iterator();
			while (result.hasNext()) {
				Object[] next = result.next();
				Quantity quantity = new Quantity();
				quantity.setValue((Double) next[1]);

				Unit unit = (Unit) session.get(Unit.class, (Long) next[2]);
				quantity.setUnit(unit);
				details.add(new InventoryDetails(quantity, (Double) next[3]));
			}
		} finally {
			session.setFlushMode(flushMode);
		}
		return details;
	}

	public static double getAverageCost(Item item) {
		long itemID = item.getID();

		double unitFactor = item.getOnhandQty().getUnit().getFactor();

		double defaultUnitFactor = item.getMeasurement().getDefaultUnit()
				.getFactor();

		Session session = HibernateUtil.getCurrentSession();
		FlushMode flushMode = session.getFlushMode();
		try {
			session.setFlushMode(FlushMode.COMMIT);
			Object result = session.getNamedQuery("getAverageCost.of.Item")
					.setParameter("inventoryId", itemID)
					.setParameter("unitFactor", unitFactor)
					.setParameter("defaultUnitFactor", defaultUnitFactor)
					.uniqueResult();
			return result != null ? (Double) result : 0.00D;
		} finally {
			session.setFlushMode(flushMode);
		}
	}

	public static double getAverageCost(Item item, Quantity qty, double newCost) {
		Quantity newQty = qty.copy();
		double averageCost = item.getAverageCost();
		Quantity onhandQty = item.getOnhandQty();
		double onHandCost = onhandQty.calculatePrice(averageCost);
		newCost = newQty.calculate(newCost);

		double totalAssetValue = onHandCost + newCost;
		Quantity totalQuantity = onhandQty.add(newQty.convertToDefaultUnit());
		if (!totalQuantity.isEmpty()) {
			return Math.abs(totalAssetValue / totalQuantity.getValue());
		}
		return averageCost;
	}

	static class InventoryDetails {
		Quantity quantity;
		double cost;

		InventoryDetails(Quantity quantity, double cost) {
			this.quantity = quantity;
			this.cost = cost;
		}
	}

	public static void remapAllInventory(Company company)
			throws AccounterException {
		Session session = HibernateUtil.getCurrentSession();
		Query query = session.getNamedQuery("get.all.Items").setEntity(
				"company", company);
		List<Item> items = query.list();

		InventoryUtils.remapSalesPurchases(items);
	}

}
