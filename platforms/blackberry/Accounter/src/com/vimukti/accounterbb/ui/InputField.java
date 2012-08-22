package com.vimukti.accounterbb.ui;

import java.util.Calendar;
import java.util.Date;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.picker.DateTimePicker;

import com.vimukti.accounterbb.result.InputType;
import com.vimukti.accounterbb.utils.BlackBerryEditField;

public class InputField extends VerticalFieldManager {

	private InputSelectionListener listener;

	private PasswordField passwordEditField;
	private BlackBerryEditField textField;

	public InputField(InputType inputType, InputSelectionListener listener) {

		this.listener = listener;

		int textInputType = inputType.getType();
		String inputValue = inputType.getValue();

		if (textInputType != 0 && textInputType != 8) {
			add(createTextField(textInputType, inputValue));
		} else if (textInputType == 8) {
			// for pushing the datePickerScreen
			pushDatePicker(inputValue);

		}

	}
	
	private Field createTextField(int inputType, String value) {

		long style = EditField.FILTER_DEFAULT;
		switch (inputType) {
		case 1:
			style = EditField.FILTER_DEFAULT;
			break;
		case 2:
		}

		if (inputType == 1) {
			style = EditField.FILTER_DEFAULT;
		} else if (inputType == 2) {
			style = EditField.FILTER_INTEGER;
		} else if (inputType == 3) {
			style = EditField.FILTER_REAL_NUMERIC;
		} else if (inputType == 5) {
			style = EditField.FILTER_EMAIL;
		} else if (inputType == 6) {
			style = EditField.FILTER_PHONE;
		} else if (inputType == 7) {
			style = EditField.FILTER_URL;
		}

		if (inputType == 4) {
			passwordEditField = new PasswordField();
			passwordEditField.setSelectionListener(listener);
			// TODO add a listener
			return passwordEditField;
		} else {

			textField = new BlackBerryEditField(style);
			textField.setSelectionListener(listener);
			if (value.trim().length() != 0) {
				textField.setText(value);
			}
			return textField;
		}

	}

	public void pushDatePicker(String strLongValue) {

		DateTimePicker dateTimePicker = DateTimePicker.createInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		if (strLongValue.trim().length() > 0) {
			long date = Long.parseLong(strLongValue);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date(date));
			dateTimePicker.setDateTime(calendar);
		}
		dateTimePicker.doModal();
		Calendar cal = dateTimePicker.getDateTime();
		long date = cal.getTime().getTime();
		String dateF = dateFormat.format(new Date(date));
		listener.inputSelected(String.valueOf(dateF));
	}

	public void setSelectionListener(InputSelectionListener listener) {
		this.listener = listener;

	}
}
