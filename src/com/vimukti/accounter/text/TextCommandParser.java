package com.vimukti.accounter.text;

import java.util.ArrayList;
import java.util.Scanner;

public class TextCommandParser {

	public static ArrayList<ITextData> parse(String text) {
		ArrayList<ITextData> commands = new ArrayList<ITextData>();

		Scanner scanner = new Scanner(text);

		// Get First command
		String command = scanner.nextLine();
		while (scanner.hasNextLine()) {
			// Get Next Command
			String nextLine = scanner.nextLine();

			// Add Previous command, if current line is a command
			String first = getFirst(nextLine);
			if (isCommand(first)) {
				// Add command to List
				commands.add(new TextData(first, nextLine));
				// set current command to previous command
				command = nextLine;
			} else {
				// If current line is not a command, then just append it to
				// previous
				if (!command.endsWith(",")) {
					// If Not ends with comma(,), then append it
					command += ",";
				}
				command = command + nextLine;
			}
		}

		// Add Last command here
		String first = getFirst(command);
		if (isCommand(first)) {
			commands.add(new TextData(first, command));
		}

		return commands;
	}

	private static boolean isCommand(String first) {
		return CommandsFactory.getCommand(first) != null;
	}

	private static String getFirst(String line) {
		if (line == null || line.isEmpty()) {
			return null;
		}
		return line.split(",")[0];
	}
}
