package com.vimukti.accounter.web.client.core;

@SuppressWarnings("serial")
public class ClientTransferFund extends ClientTransaction {

	String transferFrom;
	String transferTo;

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
	public String getTransferFrom() {
		return transferFrom;
	}

	/**
	 * @param transferFrom
	 *            the transferFrom to set
	 */
	public void setTransferFrom(String transferFrom) {
		this.transferFrom = transferFrom;
	}

	/**
	 * @return the transferTo
	 */
	public String getTransferTo() {
		return transferTo;
	}

	/**
	 * @param transferTo
	 *            the transferTo to set
	 */
	public void setTransferTo(String transferTo) {
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
	public String getStringID() {
		return this.stringID;
	}

	@Override
	public void setStringID(String stringID) {
		this.stringID = stringID;

	}

	@Override
	public String getClientClassSimpleName() {

		return "ClientTransferFund";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TRANSFERFUND;
	}

}
