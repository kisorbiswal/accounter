package com.vimukti.accounter.servlets;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vimukti.accounter.main.ServerConfiguration;

public class DownloadTempFileServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int BUFSIZE = 1024;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String filename = req.getParameter("filename");
		try {

			if (filename != null) {
				File file = new File(ServerConfiguration.getTmpDir(), filename);
				if (!file.exists()) {
					return;
				}
				int length = 0;
				ServletOutputStream op = resp.getOutputStream();
				resp.setHeader("Content-Disposition",
						"attachment; filename=\"printcheque\"");
				byte[] bbuf = new byte[BUFSIZE];
				DataInputStream in = new DataInputStream(new FileInputStream(
						file));
				while ((in != null) && ((length = in.read(bbuf)) != -1)) {
					op.write(bbuf, 0, length);
				}
				in.close();
				op.flush();
				op.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
