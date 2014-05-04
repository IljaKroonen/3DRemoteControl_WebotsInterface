package org.black_mesa.webots_remote_control.server;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.black_mesa.webots_remote_control.communication_structures.CameraInstructionQueue;

/**
 * @author Ilja Kroonen
 */
public class Server {
	// This value must only be assigned ; the object never gets modified
	private CameraInstructionQueue mCamera;

	/**
	 * Instantiates a server and its thread.
	 * 
	 * @param port
	 *            Port the server will listen on
	 * @param initialState
	 *            Initial state of the CommunicationStructure used. Will be the first object sent to the client.
	 */
	public Server(final int port, final CameraInstructionQueue initialState) {
		mCamera = initialState;
		new Thread(new Runnable() {
			@Override
			public void run() {
				startServer(port);
			}
		}).start();
	}

	/**
	 * Getter for the received information.
	 * 
	 * @return The most recent CameraInstructionQueue received by the server.
	 */
	public final CameraInstructionQueue getCamera() {
		return mCamera;
	}

	private void startServer(final int port) {
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Server online on port " + serverSocket.getLocalPort());
			while (true) {
				try {
					serverRoutine(serverSocket);
				} catch (EOFException e) {
					System.out.println("Client disconnected");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void serverRoutine(final ServerSocket serverSocket) throws Exception {
		Socket socket = serverSocket.accept();
		System.out.println("Incoming connection");
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(1);
		System.out.println("Sending camera");
		out.writeObject(mCamera);
		System.out.println("Camera sent");
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		while (true) {
			mCamera = (CameraInstructionQueue) ((CameraInstructionQueue) in.readObject()).board(mCamera);
			System.out.println("Received object");
		}
	}
}