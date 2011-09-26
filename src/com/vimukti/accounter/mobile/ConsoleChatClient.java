/**
 * 
 */
package com.vimukti.accounter.mobile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ConsoleChatClient {
	MobileMessageHandler handler = new MobileMessageHandler();

	public void start() {
		try {
			Socket client = new Socket("localhost", 8080);
			ObjectOutputStream out = new ObjectOutputStream(
					client.getOutputStream());
			out.flush();
			ObjectInputStream in = new ObjectInputStream(
					client.getInputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));
			// String readLine = reader.readLine();
			// readLine = readLine.substring(1);
			// out.write(readLine.getBytes());
			while (true) {
				String str = (String) in.readObject();
				System.out.println("Server:" + str);
				System.out.print('>');
				String readLine = reader.readLine();
				out.writeObject(readLine);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ConsoleChatClient console = new ConsoleChatClient();
		console.start();
	}
}
