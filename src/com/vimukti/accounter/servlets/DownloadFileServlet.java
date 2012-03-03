package com.vimukti.accounter.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.main.ServerConfiguration;

public class DownloadFileServlet extends BaseServlet {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	class FileInfo {
		boolean inline;

		public FileInfo(String filename2, int size, InputStream in2,
				boolean inline) {
			this.fileName = filename2;
			this.fileSize = size;
			this.in = in2;
			this.inline = inline;
		}

		String fileName;
		long fileSize;
		InputStream in;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException {
		try {
			Long companyId = (Long) req.getSession().getAttribute(COMPANY_ID);
			if (companyId == null) {
				return;
			}
			String fileName = req.getParameter("fileName");

			if (fileName == null
					|| req.getSession().getAttribute("identityID") == null) {

			}

			File file = new File(ServerConfiguration.getAttachmentsDir()
					+ File.separator + companyId + File.separator + fileName);
			response.setContentLength((int) file.length());

			// Open the file and output streams
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();

			// Copy the contents of the file to the output stream
			byte[] buf = new byte[1024];
			int count = 0;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			in.close();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		} finally {
		}
	}

}
