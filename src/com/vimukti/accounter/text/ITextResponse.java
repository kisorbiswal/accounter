package com.vimukti.accounter.text;

import java.io.File;

public interface ITextResponse {

	public void addError(String error, Throwable t);

	public boolean hasErrors();

	public void addMessage(String msg);

	public void addFile(String path);

	public void addFile(File file);

}
