package com.vimukti.accounter.web.client.ui.settings;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.externalization.AccounterConstants;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.ActionFactory;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.EditMode;

public class MesurementListView extends BaseView {

	private UnitsListGird grid;
	private VerticalPanel mesurementPanel;
	private AccounterConstants settingsMessages = Accounter.constants();

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result){
		// TODO Auto-generated method stub

	}


	@Override
	protected String getViewTitle() {
		// TODO Auto-generated method stub
		return null;
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
	public void printPreview() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		super.init();
		mesurementPanel = new VerticalPanel();
		mesurementPanel.setSpacing(20);
		setSize("100%", "100%");
		createMeasurementListView();

	}

	private void createMeasurementListView() {
		HorizontalPanel hPanel = new HorizontalPanel();
		Button addMeasurementButton = new Button();
		addMeasurementButton.setText(settingsMessages.addMeasurementButton());
		mesurementPanel = new VerticalPanel();
		mesurementPanel.add(addMeasurementButton);
		addMeasurementButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ActionFactory.getAddMeasurementAction().run(null, false);
				;
			}
		});
		initGrid();
		mesurementPanel.add(grid);
		this.add(mesurementPanel);
	}

	private void initGrid() {
		grid = new UnitsListGird(false);
		grid.isEnable = false;
		grid.init();
		grid.setView(this);
		grid.setHeight("600px");
		grid.setSize("100%", "100%");
		setExistingListToView();
	}

	@Override
	public List getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	private void setExistingListToView() {
		// TODO Auto-generated method stub

	}

}
