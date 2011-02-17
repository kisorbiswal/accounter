/**
 * 
 */
package com.vimukti.accounter.web.client;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.FinanceApplication;

/**
 * @author vimukti5
 * 
 */
@SuppressWarnings("serial")
public class InvalidOperationException extends Exception implements
		IAccounterCore {

	public InvalidOperationException(String mesge) {
		this.detailedMessage = mesge;
	}

	public static final int CREATE_FAILED = 777;
	public static final int UPDATE_FAILED = 888;
	public static final int DELETE_FAILED = 999;

	public int status;

	private String detailedMessage;
	private String stringID;

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;

		switch (status) {
		case CREATE_FAILED:
			this.detailedMessage = "Create Failed!";
			break;

		case UPDATE_FAILED:
			this.detailedMessage ="Update Failed";
			break;
		case DELETE_FAILED:
			this.detailedMessage ="Delete Failed";
		default:
			this.detailedMessage = "Failed";
			break;
		}
	}

	/**
	 * @return the detailedMessage
	 */
	public String getDetailedMessage() {
		return detailedMessage;
	}

	/**
	 * @param detailedMessage
	 *            the detailedMessage to set
	 */
	public void setDetailedMessage(String detailedMessage) {
		if (this.detailedMessage == null)
			this.detailedMessage = new String(detailedMessage);
		else
			this.detailedMessage = this.detailedMessage.concat(" "
					+ detailedMessage);

	}

	public InvalidOperationException(int status, String stringID,
			IAccounterCore serverObject) {
		super();

		setStatus(status);

		this.stringID = stringID;

		if (serverObject != null) {
			this.detailedMessage = detailedMessage
					+ serverObject.getObjectType().getServerClassSimpleName();
		}

	}

	public InvalidOperationException() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void printStackTrace() {

		System.out.print(String.valueOf(detailedMessage));
	}

	@Override
	public String getClientClassSimpleName() {

		return "AccounterCRUDServiceFailedException";
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {

		return FinanceApplication.getFinanceMessages().exception();
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
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.ERROR;
	}

	@Override
	public String getMessage() {
		return this.detailedMessage;
	}
}
