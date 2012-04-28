package com.vimukti.accounter.web.client.core;

public class ClientProductionPayHead extends ClientPayHead {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ClientAttendanceOrProductionType productionType;

	public ClientAttendanceOrProductionType getProductionType() {
		return productionType;
	}

	public void setProductionType(
			ClientAttendanceOrProductionType productionType) {
		this.productionType = productionType;
	}

}
