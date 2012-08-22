package com.vimukti.accounterbb.ui;

import java.util.Vector;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.vimukti.accounterbb.result.Command;
import com.vimukti.accounterbb.result.CommandList;

public class CommandListField extends VerticalFieldManager {

	private CommandSelectionListener listener;
	private CommandList commandList;

	public CommandListField(CommandList commandList) {
		this.commandList = commandList;

		Vector commandsList = commandList.getCommandNames();
		for (int j = 0; j < commandsList.size(); j++) {
			final Command command = (Command) commandsList.elementAt(j);
			final ButtonField buttonField = new ButtonField(command.getName(),
					ButtonField.CONSUME_CLICK | ButtonField.FOCUSABLE|USE_ALL_WIDTH) {

				// public int getPreferredHeight() {
				// return 25;
				// }
				//
				// protected void paint(Graphics graphics) {
				//
				// graphics.drawText(command.getName(), 5, 5);
				//
				// }

				 public int getPreferredWidth() {
				 return Display.getWidth() - 42;
				 }
				
				 protected void layout(int width, int height) {
				 super.layout(Display.getWidth() - 42, 25);
				 }

			};
			buttonField.setChangeListener(new FieldChangeListener() {
				public void fieldChanged(Field field, int context) {
					fireCommandSelection(command);

				}
			});
			this.add(buttonField);
		}
	}

	protected void fireCommandSelection(Command command) {
		listener.commandSelected(commandList, command);

	}

	public void setSelectionListener(CommandSelectionListener listener) {
		this.listener = listener;
	}

}
