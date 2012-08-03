package com.vimukti.accounter.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.Map;

import com.vimukti.accounter.utils.NoCacheAnalyser;
import com.vimukti.accounter.utils.NoCacheAnalyser.Permitation;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;

public class PostCompileMain {

	private static final String MESSAGES_START = "/* ACCOUNTER MESSAGES START */";
	private static final String MESSAGES_END = "/* ACCOUNTER MESSAGES END */";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		File src = new File("war");
		File dest = new File("platforms", "windows 8/Accounter");

		File srcClient = new File(src, "accounter.client");
		File destClient = new File(dest, "accounter.client");
		destClient.mkdirs();

		changeDefaultHtml(dest);
		String deferedID = copyNoCacheJS(srcClient, destClient);

		if (deferedID != null) {
			copyIE10Html(srcClient, destClient, deferedID);
			copyDeferredJS(srcClient, destClient, deferedID);
		}
		copyCss(src, dest);
		changeJsProject(dest);
	}

	private static void changeDefaultHtml(File src) throws Exception {
		File source = new File(src, "default.html");

		String readFile = readFile(source);

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(source)));

		String[] split = readFile.split("\r\n");

		boolean isInMessages = false;
		for (String line : split) {
			if (line.trim().contains(MESSAGES_START)) {
				writer.write(MESSAGES_START);
				writer.newLine();
				writeMessages(writer);
				isInMessages = true;
			} else if (line.trim().contains(MESSAGES_END)) {
				writer.write(MESSAGES_END);
				writer.newLine();
				isInMessages = false;
			} else if (!isInMessages) {
				writer.write(line);
				writer.newLine();
			}

		}

		writer.close();
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
		writer.newLine();
	}

	private static void writeMessages(BufferedReader reader,
			BufferedWriter writer) throws IOException {
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (line.trim().startsWith("#")) {
				continue;
			}
			String[] split = line.split("=");
			String value = split[1];
			if (value.contains("'")) {
				value = value.replace("'", "\\'");
			}
			writer.write("	  				'" + split[0].trim() + "' : '" + value + "',");
			writer.newLine();
		}
	}

	private static String copyNoCacheJS(File src, File dest) throws IOException {
		File noCache = new File(src, "accounter.client.nocache.js");
		File destNoCach = new File(dest, "accounter.client.nocache.js");
		if (!destNoCach.exists()) {
			destNoCach.createNewFile();
		}

		copyFolder(noCache, destNoCach);

		NoCacheAnalyser analyser = new NoCacheAnalyser(readFile(noCache));
		String ie10 = analyser.get(NoCacheAnalyser.IE10);
		if (ie10 == null) {
			Map<Permitation, String> permitations = analyser.permitations();
			if (permitations.size() == 1) {
				ie10 = permitations.values().toArray(new String[] {})[0];
			}
		}
		return ie10;
	}

	private static void copyIE10Html(File src, File dest, String defferedID)
			throws Exception {
		File srcHtml = new File(src, defferedID + ".cache.html");
		File destHtml = new File(dest, defferedID + ".cache.html");
		if (!destHtml.exists()) {
			destHtml.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		try {
			source = new FileInputStream(srcHtml).getChannel();
			destination = new FileOutputStream(destHtml).getChannel();

			long count = 0;
			long size = source.size();
			while ((count += destination.transferFrom(source, count, size
					- count)) < size)
				;
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}

	}

	private static void copyDeferredJS(File src, File dest, String deferedID)
			throws IOException {
		File deferredSrc = new File(src, "deferredjs" + File.separator
				+ deferedID);
		File deferredDest = new File(dest, "deferredjs" + File.separator
				+ deferedID);
		copyFolder(deferredSrc, deferredDest);
	}

	private static void copyCss(File src, File dest) throws IOException {
		File srcCss = new File(src, "css" + File.separator + "default.css");
		File destCss = new File(dest, "css");
		destCss.mkdirs();
		copyFolder(srcCss, new File(destCss, "default.css"));
	}

	private static void changeJsProject(File dest) throws IOException {

		File jsProj = new File(dest, "Accounter.jsproj");

		String fileContent = readFile(jsProj);

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(jsProj), "UTF-8"));

		String[] split = fileContent.split("\r\n");
		boolean isAccounterClient = false;
		for (String str : split) {
			if (isAccounterClient
					&& !str.trim().equals(
							"<Content Include=\"images\\Add.png\" />")) {
				continue;
			}
			writer.write(str);
			writer.newLine();
			if (str.trim().equals("<Content Include=\"default.html\" />")) {
				isAccounterClient = true;
				includeClinet(writer, new File(dest, "accounter.client"));
			} else if (str.trim().equals(
					"<Content Include=\"images\\Add.png\" />")) {
				isAccounterClient = false;
			}

		}
		writer.close();
	}

	private static void includeClinet(BufferedWriter writer, File client)
			throws IOException {
		if (client.isDirectory()) {
			for (File file : client.listFiles()) {
				includeClinet(writer, file);
			}
		} else {
			String path = client.getAbsolutePath();
			writer.write("    <Content Include=\""
					+ path.substring(path.indexOf("accounter.client"
							+ File.separator)) + "\" />");
			writer.newLine();
		}
	}

	public static void copyFolder(File src, File dest) throws IOException {

		if (src.isDirectory()) {

			// if directory not exists, create it
			if (!dest.exists()) {
				dest.mkdirs();
				System.out.println("Directory copied from " + src + "  to "
						+ dest);
			}

			// list all the directory contents
			String files[] = src.list();

			for (String file : files) {
				// construct the src and dest file structure
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// recursive copy
				copyFolder(srcFile, destFile);
			}

		} else {
			if (!dest.exists()) {
				dest.createNewFile();
			}
			// if file, then copy it
			// Use bytes stream to support all file types
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
			System.out.println("File copied from " + src + " to " + dest);
		}
	}

	private static String readFile(File path) throws IOException {
		InputStream in = new FileInputStream(path);
		try {
			byte[] b = new byte[(int) path.length()];
			int len = b.length;
			int total = 0;

			while (total < len) {
				int result = in.read(b, total, len - total);
				if (result == -1) {
					break;
				}
				total += result;
			}

			return new String(b, "UTF-8");
		} finally {
			in.close();
		}

	}

}
