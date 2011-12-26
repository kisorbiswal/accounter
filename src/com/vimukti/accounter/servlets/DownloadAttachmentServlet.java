package com.vimukti.accounter.servlets;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

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
			InputStream attachmentStream = AttachmentFileServer
					.getAttachmentStream(attachmentId);
			if (attachmentStream != null) {
				int length = 0;
				ServletOutputStream op = resp.getOutputStream();
				resp.setHeader("Content-Disposition", "attachment; filename=\""
						+ name + "\"");
				byte[] bbuf = new byte[BUFSIZE];
				DataInputStream in = new DataInputStream(attachmentStream);

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
