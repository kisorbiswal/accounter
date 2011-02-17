package com.vimukti.accounter.web.client.ui.core;

public interface ErrorDialogHandler {

	public boolean onYesClick() throws InvalidEntryException;

	public boolean onNoClick() throws InvalidEntryException;

	public boolean onCancelClick() throws InvalidEntryException;

}
