package com.vimukti.accounter.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import au.com.bytecode.opencsv.CSVParser;

public class TextCommandParser {

	public static ArrayList<ITextData> parse(String text) throws IOException {
		ArrayList<ITextData> commands = new ArrayList<ITextData>();

		Scanner scanner = new Scanner(text);

		// Get First command
		String command = scanner.nextLine();
		while (scanner.hasNextLine()) {
			// Get Next Command
			String nextLine = scanner.nextLine();
			if (nextLine == null || nextLine.isEmpty()) {
				continue;
			}
			// Add Previous command, if current line is a command
			String first = getFirst(nextLine);
			if (isCommand(first)) {
				// Add command to List
				commands.add(new TextDataImpl(getFirst(command), command,
						parseCSV(command)));
				// set current command to previous command
				command = nextLine;
			} else {
				// If current line is not a command, then just append it to
				// previous
				if (!command.endsWith(",") && !nextLine.startsWith(",")) {
					// If Not ends with comma(,), then append it
					nextLine = "," + nextLine;
				}
				command = command + nextLine;
			}
		}

		// Add Last command here
		String first = getFirst(command);
		if (isCommand(first)) {
			commands.add(new TextDataImpl(first, command, parseCSV(command)));
		}

		return commands;
	}

	private static String[] parseCSV(String nextLine) throws IOException {
		CSVParser parser = new CSVParser(',');
		String[] parseLine = parser.parseLine(nextLine);
		return parseLine;
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
