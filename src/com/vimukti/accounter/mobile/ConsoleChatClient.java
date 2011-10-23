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
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			System.out.print("Enter user Email:");
			String email = "lingarao.r@vimukti.com";

			Socket client = new Socket("localhost", 8080);
			ObjectOutputStream out = new ObjectOutputStream(
					client.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(
					client.getInputStream());
			out.writeObject(email);
			out.writeObject("Hi");
			String str = (String) in.readObject();
			System.out.println("Server:" + str);
			System.out.print('>');
			while (true) {
				str = (String) in.readObject();
				System.out.println("Server:" + str);
				System.out.print('>');
				String readLine = br.readLine();
				out.writeObject(email);
				out.writeObject(readLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ConsoleChatClient console = new ConsoleChatClient();
		console.start();
	}
}
