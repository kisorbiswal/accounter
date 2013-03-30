package com.vimukti.accounter.text.commands;

import com.vimukti.accounter.text.ITextData;
import com.vimukti.accounter.text.ITextResponse;

public interface ITextCommand {

	public void parse(ITextData data, ITextResponse respnse);

	public void process(ITextResponse respnse);

}
