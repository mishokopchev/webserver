package org.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {

	private int port;
	private final ExecutorService handlerService;

	public WebServer(int port) {
		this.port = port;
		this.handlerService = Executors.newCachedThreadPool();

	}

	public void start() {

		try (ServerSocket serverSocket = new ServerSocket(port)) {
			while (true) {
				Socket socket = serverSocket.accept();
				this.handlerService.execute(new ServerWorkerThread(socket));
			}
		} catch (IOException e) {
			System.err.println("Could not listen on port " + port);
			System.exit(-1);
		} finally {

			if (handlerService != null && handlerService.isShutdown() && handlerService.isTerminated()) {
				handlerService.shutdown();
			}
			System.out.println(";The webserver was closed!");
		}
	}

}
