package com.inovatica.javacan.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainNetworkClient {

	static boolean stop;

	public static void main(String[] args) {

		String host = "127.0.0.1";
		
		if (args.length == 1) {
			host = args[0];
		}

		int port = 3005;
		int timeout = 200;

		while (true) {

			try (Socket clientSocket = new Socket(host, port);
					PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

				clientSocket.setSoTimeout(timeout);
				clientSocket.setTcpNoDelay(true);

				System.out.println("Connected");

				stop = false;

				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							while (!stop) {
								out.println("a");

								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
								}
							}
						} catch (Exception e) {
							System.out.println("Exception caught: " + e.getMessage());
							stop = true;
						}
					}
				}).start();

				while (!stop) {
					String inputLine;
//					while (in.ready() && (inputLine = in.readLine()) != null) {
					while (true) {
						inputLine = in.readLine();
						if (inputLine == null) {
							stop = true;
							break;
						}
						System.out.println("Received message: " + inputLine);
					}
				}

			} catch (IOException e) {
				System.out.println("Exception caught: " + e.getMessage());
				stop = true;
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
}
