package com.vimukti.accounter.servlets;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bizantra.server.main.ServerConfiguration;
import com.bizantra.server.utils.SecureUtils;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class UploadFileServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3451003399754758827L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			MultipartRequest multi = new MultipartRequest(request,
					ServerConfiguration.getTmpDir(), 50 * 1024 * 1024,
					"ISO-8859-1", new DefaultFileRenamePolicy());

			// response.getWriter().print("<html><body>");
			// JSONObject obj=new JSONObject();
			Enumeration<?> files = multi.getFileNames();
			String parentID = (String) request.getParameter("parentId");
			if (parentID == null) {
				return;
			}

			String fileID = (String) files.nextElement();
			File file = multi.getFile(fileID);
			if (file != null) {
				// String fileName = file.getName();
				// int size = (int) file.length();
				// String filepath = file.getPath();
				if (fileID == null || fileID == "") {
					fileID = SecureUtils.createID();
				}

				file.renameTo(new File(ServerConfiguration.getTmpDir()
						+ File.separator + parentID + fileID));

			}
			response.setContentType("text/html");
			response.getWriter().print(parentID + fileID);
			// jsonObj.put("fileIDS", fileArray);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
