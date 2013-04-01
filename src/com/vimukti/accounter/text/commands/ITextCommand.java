package com.vimukti.accounter.text.commands;

import com.vimukti.accounter.text.ITextResponse;
import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.web.client.exception.AccounterException;

public interface ITextCommand {

	/**
	 * If given data is not type of this command, then return false otherwise
	 * true
	 * 
	 * @param data
	 * @param respnse
	 * @return
	 */
	public boolean parse(ITextData data, ITextResponse respnse);

	public void process(ITextResponse respnse) throws AccounterException;

}
