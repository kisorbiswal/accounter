package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientTransferFund extends ClientTransaction {

	long transferFrom;
	long transferTo;

	/**
	 * @return the version
	 */
	@Override
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the transferFrom
	 */
	public long getTransferFrom() {
		return transferFrom;
	}

	/**
	 * @param transferFrom
	 *            the transferFrom to set
	 */
	public void setTransferFrom(long transferFrom) {
		this.transferFrom = transferFrom;
	}

	/**
	 * @return the transferTo
	 */
	public long getTransferTo() {
		return transferTo;
	}

	/**
	 * @param transferTo
	 *            the transferTo to set
	 */
	public void setTransferTo(long transferTo) {
		this.transferTo = transferTo;
	}

	@Override
	public String toString() {
		return AccounterConstants.TYPE_TRANSFER_FUND;
	}

	@Override
	public String getDisplayName() {

		return getName();
	}

	@Override
	public String getName() {

		return Utility.getTransactionName(getType());
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientTransferFund";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TRANSFERFUND;
	}

	public ClientTAXAdjustment clone() {
		return null;
	}
}
