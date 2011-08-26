package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;

public class CreateTaxesDialog extends BaseDialog {

	DialogGrid listGridView;;

	// private HorizontalPanel bodyLayout;
	int i = 0;

	public CreateTaxesDialog(AbstractBaseView<?> parent) {
		super(Accounter.constants().createTaxes(), "");
		setText(Accounter.constants().createTaxes());
		createControls();
		center();
	}

	public void createControls() {

		listGridView = new DialogGrid(true);
		listGridView.setView(this);
		listGridView.addColumns(Accounter.constants().name());
		listGridView.setColumnTypes(getColunmTypes());
		listGridView.init();
		listGridView.setWidth("100%");
		CreateTax c = new CreateTax();
		CreateTax c1 = new CreateTax();

		c.setName(Accounter.constants().ireland());
		c1.setName(Accounter.constants().uk());

		listGridView.addData(c);
		listGridView.addData(c1);

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(listGridView);

		setBodyLayout(mainVLay);
		setSize("300px", "170px");
	}

	public Integer[] getColunmTypes() {

		return null;
	}

	@Override
	public Object getGridColumnValue(IsSerializable obj, int index) {
		CreateTax tt = (CreateTax) obj;
		if (index < 0)
			return tt.isChecked;

		System.out.println(tt.getName());
		return tt.getName();

	}

	class CreateTax implements IAccounterCore {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		boolean isChecked;
		String name;
		private long id;

		private int version;

		@Override
		public String getClientClassSimpleName() {
			// its not using any where
			return null;
		}

		public void setIsChecked(boolean isChecked) {
			this.isChecked = isChecked;
		}

		public boolean getIsChecheked() {
			return isChecked;
		}

		@Override
		public String getDisplayName() {
			return this.name;
		}

		@Override
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;

		}

		@Override
		public AccounterCoreType getObjectType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getID() {
			return this.id;
		}

		@Override
		public void setID(long id) {
			this.id = id;
		}

		public CreateTax clone() {
			return null;
		}

		@Override
		public int getVersion() {
			return version;
		}

		@Override
		public void setVersion(int version) {
			this.version=version;
		}

	}

	@Override
	protected boolean onOK() {

		final int[] index = new int[2];
		List<?> list = listGridView.getSelectedRecords();
		for (Object o : list) {
			CreateTax c = (CreateTax) o;
			if (c.getIsChecheked()
					&& c.getName().equals(Accounter.constants().ireland()))
				index[i] = 2;
			if (c.getIsChecheked()
					&& c.getName().equals(Accounter.constants().uk()))
				index[i] = 1;
			i++;
		}

		AccounterAsyncCallback<Long> createTaxesCallback = new AccounterAsyncCallback<Long>() {

			public void onException(AccounterException caught) {

			}

			public void onResultSuccess(Long result) {
				super.onSuccess(result);
				processOK();
			}

		};
		// FIXME
		// Accounter.createHomeService().createTaxes(index,
		// createTaxesCallback);
		return true;
	}

}