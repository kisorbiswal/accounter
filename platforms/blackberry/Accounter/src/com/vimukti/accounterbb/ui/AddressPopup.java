package com.vimukti.accounterbb.ui;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.vimukti.accounterbb.core.AddressListener;
import com.vimukti.accounterbb.utils.BlackBerryEditField;

public class AddressPopup extends PopupScreen {
	
	private BlackBerryEditField editField ;
	private AddressListener addressListener;
	

	public AddressPopup( AddressListener addressListener) {
		super(new VerticalFieldManager(USE_ALL_WIDTH));
		this.addressListener = addressListener;
		createGUI();
	}

	private void createGUI() {
		 VerticalFieldManager manager = new VerticalFieldManager(
					VERTICAL_SCROLL);
		 
		LabelField labelField = new LabelField(
				"Enter IP Address ", FIELD_HCENTER);
		  editField = new BlackBerryEditField();

		ButtonField buttonField = new ButtonField("Connect",
				ButtonField.CONSUME_CLICK | ButtonField.FOCUSABLE
						| ButtonField.FIELD_HCENTER);

		manager.add(labelField);
		manager.add(editField);
		manager.add(buttonField);

		add(manager);

		buttonField.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
			addressListener.setAddress(editField.getText());
			UiApplication.getUiApplication().popScreen();
			
			}
		});

	}


	public boolean onClose() {
		System.exit(1);
		close();
		return true;
	}

}
