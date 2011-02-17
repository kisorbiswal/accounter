package com.vimukti.accounter.workspace.tool;

import java.util.ArrayList;
import java.util.List;

import com.bizantra.server.workspace.ext.AbstractToolMetaData;
import com.vimukti.accounter.web.client.core.IAccounterCore;

public class FinanceData extends AbstractToolMetaData {

	List<IAccounterCore> objects;

	// need to get all objects and convert as client object and add to this list
	public FinanceData(FinanceTool tool, boolean withData) {
		super(tool, withData);
		if (withData) {
			objects = new ArrayList<IAccounterCore>();
		}
	}

	public FinanceData() {
		super();
	}

}
