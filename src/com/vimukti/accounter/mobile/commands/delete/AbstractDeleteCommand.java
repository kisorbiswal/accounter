package com.vimukti.accounter.mobile.commands.delete;

import java.util.List;

import com.vimukti.accounter.mobile.Context;
import com.vimukti.accounter.mobile.Requirement;
import com.vimukti.accounter.mobile.commands.NewAbstractCommand;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.server.FinanceTool;
import com.vimukti.accounter.web.server.OperationContext;

/**
 * 
 * @author Lingarao.R
 * 
 */
public abstract class AbstractDeleteCommand extends NewAbstractCommand {

	@Override
	protected String getWelcomeMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDetailsMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setDefaultValues(Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getSuccessMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addRequirements(List<Requirement> list) {
		// TODO Auto-generated method stub

	}

	/**
	 * delete
	 * 
	 * @param type
	 * @param id
	 * @param context
	 * @return {@link Boolean}
	 * @throws AccounterException
	 */
	public boolean delete(AccounterCoreType type, long id, Context context)
			throws AccounterException {
		FinanceTool tool = new FinanceTool();
		OperationContext opContext = new OperationContext(getCompanyId(), type,
				context.getEmailId());
		opContext.setArg1(String.valueOf(id));
		opContext.setArg2(type.getClientClassSimpleName());
		return tool.delete(opContext);

	}
}
