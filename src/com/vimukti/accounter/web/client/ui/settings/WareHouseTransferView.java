package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;

public class WareHouseTransferView extends BaseView<ClientWarehouse> {

	private VerticalPanel mainPanel;
	private WareHouseTransferGrid grid;
	private OtherAccountsCombo fromCombo, toCombo;
	private TextAreaItem commentArea;
	private DynamicForm form;

	@Override
	protected String getViewTitle() {
		return Accounter.constants().wareHouseTransfer();
	}

	@Override
	public void init() {
		super.init();
		createControls();
	}

	private void createControls() {
		try {
			mainPanel = new VerticalPanel();
			fromCombo = new OtherAccountsCombo(Accounter.constants().from());
			toCombo = new OtherAccountsCombo(Accounter.constants().to());
			commentArea = new TextAreaItem();
			form = new DynamicForm();
			form.setNumCols(2);
			form.setFields(fromCombo, toCombo, commentArea);
			mainPanel.add(form);
			initGrid();
			mainPanel.add(grid);

			this.add(mainPanel);
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	private void initGrid() {

		grid = new WareHouseTransferGrid(false);
		grid.isEnable = false;
		grid.init();
		grid.setView(this);
		grid.setHeight("600px");
		grid.setSize("100%", "100%");

	}

	@Override
	public void fitToSize(int height, int width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEdit() {
		setMode(EditMode.EDIT);
	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {
		// currently not using

	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// currently not using

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// currently not using

	}

	@Override
	public List getForms() {
		// currently not using
		return null;
	}

	@Override
	public void setFocus() {
		this.fromCombo.setFocus();

	}

}
