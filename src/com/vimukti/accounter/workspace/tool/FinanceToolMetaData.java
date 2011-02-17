package com.vimukti.accounter.workspace.tool;

import com.bizantra.server.workspace.ext.AbstractToolMetaData;
import com.vimukti.accounter.core.ClientConvertUtil;
import com.vimukti.accounter.core.Company;
import com.vimukti.accounter.core.Util;
import com.vimukti.accounter.web.client.core.ClientCompany;

public class FinanceToolMetaData extends AbstractToolMetaData {

	/**
  * 
  */
	private static final long serialVersionUID = 1L;

	public ClientCompany company;

	public FinanceToolMetaData(FinanceTool tool, boolean withData) {
		super(tool, withData);
		if (withData) {
			Company compy = tool.getCompany();
			this.company = (ClientCompany) new ClientConvertUtil().toClientObject(compy, Util
					.getClientEqualentClass(compy.getClass()));
		}
	}

	public FinanceToolMetaData() {
		super();
	}

}