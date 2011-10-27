package com.vimukti.accounter.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vimukti.accounter.web.client.exception.AccounterException;

/**
 * 
 * A VATReturn object is created when we want to File the VAT that the company
 * is owed to the HMRC.
 * 
 * @author Ravi Chandan
 * 
 */
public class VATReturn extends AbstractTAXReturn {

	/**
	 * 
	 */
	private static final long serialVersionUID = -155771928048866705L;

	//

	List<Box> boxes = new ArrayList<Box>();

	public static final int VAT_RETURN_UK_VAT = 1;
	public static final int VAT_RETURN_IRELAND = 2;
	public static final int VAT_RETURN_NONE = 3;

	/**
	 * @return the boxes
	 */
	public List<Box> getBoxes() {
		return boxes;
	}

	/**
	 * @param boxes
	 *            the boxes to set
	 */
	public void setBoxes(List<Box> boxes) {
		this.boxes = boxes;
	}

	@Override
	public Account getEffectingAccount() {
		// currently not using
		return null;
	}

	@Override
	public Payee getPayee() {
		// currently not using
		return null;
	}

	@Override
	public int getTransactionCategory() {
		// currently not using
		return 0;
	}

	@Override
	public boolean isDebitTransaction() {
		// currently not using
		return false;
	}

	@Override
	public boolean isPositiveTransaction() {
		// currently not using
		return false;
	}

	@Override
	public String toString() {
		// currently not using
		return AccounterServerConstants.TYPE_VAT_RETURN;
	}

	@Override
	public Payee getInvolvedPayee() {

		return this.taxAgency;
	}

	public void updateBalance(double amountToPay) {

		this.balance += amountToPay;

	}

	@Override
	public void onEdit(Transaction clonedObject) {

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject)
			throws AccounterException {
		VATReturn vatReturn = (VATReturn) clientObject;
		List<Box> boxes2 = vatReturn.getBoxes();
		for (Box box : boxes2) {
			box.setCompany(vatReturn.getCompany());
		}
		return true;
	}

	@Override
	public Map<Account, Double> getEffectingAccountsWithAmounts() {
		Map<Account, Double> map = super.getEffectingAccountsWithAmounts();
		map.put(taxAgency.getAccount(), total);
		return map;
	}

}
