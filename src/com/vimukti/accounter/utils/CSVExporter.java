package com.vimukti.accounter.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.vimukti.accounter.main.ServerConfiguration;

public class CSVExporter<T> {

	private ICSVExportRunner<T> runner;

	public CSVExporter(ICSVExportRunner<T> runner) {
		this.runner = runner;
	}

	public String export(List<T> list) throws IOException {

		// take runner, create file in temp folder with random string,
		// fill data and return file name.
		String createRandomFileName = SecureUtils.createRandomFileName();
		File csvfile = new File(ServerConfiguration.getTmpDir(),
				createRandomFileName);
		FileWriter fstream = new FileWriter(csvfile);
		BufferedWriter out = new BufferedWriter(fstream);
		String[] columns = runner.getColumns();
		// For headers
		for (String columnValue : columns) {
			out.write(columnValue);
			out.write(",");
		}
		out.write("\n");
		// For body
		for (T t : list) {
			for (int i = 0; i < columns.length; i++) {
				String columnValue = runner.getColumnValue(t, i);
				out.write(columnValue);
				out.write(",");
			}
			out.write("\n");
		}
		out.flush();
		out.close();
		return createRandomFileName;
	}
}
