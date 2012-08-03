package com.vimukti.accounter.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;

public class PostCompileMain {

	private static final String MESSAGES_START = "<!-- ACCOUNTER MESSAGES START -->";
	private static final String MESSAGES_END = "<!-- ACCOUNTER MESSAGES END -->";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Path src = Paths.get("war");
		Path dest = Paths.get("platforms", "windows 8", "Accounter");
		generateDefaultHtml(src, dest);
		copyNoCacheJS(src, dest);
		copyIE10Html(src, dest);
		copyDefferredJS(src, dest);
	}

	private static void generateDefaultHtml(Path src, Path dest)
			throws Exception {
		File destination = dest.resolve("default.html").toFile();
		File source = src.resolve("default.html").toFile();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(source)));

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(destination)));

		String line = null;
		boolean isInMessages = false;

		while ((line = reader.readLine()) != null) {
			if (line.trim().contains(MESSAGES_START)) {
				isInMessages = true;
				writeMessages(writer);
			} else if (line.trim().contains(MESSAGES_END)) {
				isInMessages = false;
			} else if (!isInMessages) {
				writer.write(line);
			}
		}

	}

	private static void writeMessages(BufferedWriter writer) throws Exception {
		String messages1 = AccounterMessages.class.getName().replace('.', '/')
				+ ".properties";

		ClassLoader classLoader = AccounterMessages.class.getClassLoader();

		String messages2 = AccounterMessages2.class.getName().replace('.', '/')
				+ ".properties";

		InputStream is = classLoader.getResourceAsStream(messages1);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		writer.write(" var AccounterMessages = {");
		writeMessages(reader, writer);

		is = classLoader.getResourceAsStream(messages2);
		reader = new BufferedReader(new InputStreamReader(is));
		writeMessages(reader, writer);

		writer.write("};");

	}

	private static void writeMessages(BufferedReader reader,
			BufferedWriter writer) throws IOException {
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] split = line.split("=");
			writer.write("'" + split[0] + "' : '" + split[1] + "'");
		}
	}

	private static void copyNoCacheJS(Path src, Path dest) {
		// TODO Auto-generated method stub

	}

	private static void copyIE10Html(Path src, Path dest) {
		// TODO Auto-generated method stub

	}

	private static void copyDefferredJS(Path src, Path dest) {
		// TODO Auto-generated method stub

	}

}
