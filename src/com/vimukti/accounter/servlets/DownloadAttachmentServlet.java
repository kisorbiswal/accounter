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

import com.vimukti.accounter.main.upload.AttachmentFileServer;

public class DownloadAttachmentServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int BUFSIZE = 1024;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String attachmentId = req.getParameter("attachmentId");
		String name = req.getParameter("name");
		try {
			File attachmentFile = AttachmentFileServer
					.getAttachmentFile(attachmentId);
			if (attachmentFile.exists()) {
				int length = 0;
				ServletOutputStream op = resp.getOutputStream();
				resp.setContentLength((int) attachmentFile.length());
				resp.setHeader("Content-Disposition", "attachment; filename=\""
						+ name + "\"");
				byte[] bbuf = new byte[BUFSIZE];
				DataInputStream in = new DataInputStream(new FileInputStream(
						attachmentFile));

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
