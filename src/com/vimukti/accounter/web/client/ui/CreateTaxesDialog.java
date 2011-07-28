package com.vimukti.accounter.web.client.ui;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.InputDialogHandler;
import com.vimukti.accounter.web.client.ui.grids.DialogGrid;

@SuppressWarnings("unchecked")
public class CreateTaxesDialog extends BaseDialog {

	DialogGrid listGridView;;
	@SuppressWarnings("unused")
	private HorizontalPanel bodyLayout;
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
		listGridView.addColumns("Name");
		listGridView.setColumnTypes(getColunmTypes());
		listGridView.init();
		listGridView.setWidth("100%");
		CreateTax c = new CreateTax();
		CreateTax c1 = new CreateTax();

		c.setName("Ireland");
		c1.setName("UK");

		listGridView.addData(c);
		listGridView.addData(c1);

		addInputDialogHandler(new InputDialogHandler() {

			@Override
			public boolean onOkClick() {

				final int[] index = new int[2];
				List list = listGridView.getSelectedRecords();
				for (Object o : list) {
					CreateTax c = (CreateTax) o;
					if (c.getIsChecheked() && c.getName().equals("Ireland"))
						index[i] = 2;
					if (c.getIsChecheked() && c.getName().equals("UK"))
						index[i] = 1;
					i++;
				}

				AsyncCallback<Long> createTaxesCallback = new AsyncCallback<Long>() {

					public void onFailure(Throwable caught) {

					}

					public void onSuccess(Long result) {
						okClicked();
					}

				};
				// Accounter.createHomeService().createTaxes(index,
				// createTaxesCallback);
				return true;
			}

			@Override
			public void onCancelClick() {

			}
		});

		VerticalPanel mainVLay = new VerticalPanel();
		mainVLay.setSize("100%", "100%");
		mainVLay.add(listGridView);

		setBodyLayout(mainVLay);
		setSize("300", "170");
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

	@SuppressWarnings("serial")
	class CreateTax implements IAccounterCore {

		boolean isChecked;
		String name;
		private long id;

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

	}

	@Override
	public void processupdateView(IAccounterCore core, int command) {

	}

	@Override
	protected String getViewTitle() {
		return Accounter.constants().createTaxes();
	}

}