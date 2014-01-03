package net.fe.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import net.fe.FEMultiplayer;
import net.fe.network.message.ClientInit;
import net.fe.network.message.JoinLobby;
import net.fe.network.message.QuitMessage;

public class Client {
	
	private Socket serverSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Thread serverIn;
	private boolean open = true;
	private boolean closeRequested = false;
	byte id;
	public volatile ArrayList<Message> messages;
	
	public Client() {
		messages = new ArrayList<Message>();
		try {
			System.out.println("CLIENT: Connecting to server at port 21255...");
			serverSocket = new Socket(
					java.net.InetAddress.getLocalHost().getHostName(), 
					21255);
			System.out.println("CLIENT: Successfully connected!");
			out = new ObjectOutputStream(serverSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(serverSocket.getInputStream());
			System.out.println("CLIENT: I/O streams initialized");
			serverIn = new Thread() {
				public void run() {
					try {
						Message message;
						while((message = (Message)in.readObject()) != null) {
							processInput(message);
						}
						in.close();
						out.close();
						serverSocket.close();
					} catch (IOException e) {
						System.out.println("CLIENT: EXIT");
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			};
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		serverIn.start();
	}
	
	private void processInput(Message message) {
		if(message instanceof ClientInit) {
			id = ((ClientInit)message).clientID;
			FEMultiplayer.getLocalPlayer().setClientID(id);
			System.out.println("CLIENT: Recieved ID "+id+" from server");
			// Send a join lobby request
			sendMessage(new JoinLobby(id, FEMultiplayer.getLocalPlayer()));
		} else if (message instanceof QuitMessage) {
			if(message.origin == id && closeRequested) {
				close();
			}
		}
		messages.add(message);
	}
	
	public ArrayList<Message> getMessages() {
		return messages;
	}
	
	private void close() {
		try {
			in.close();
			out.close();
			serverSocket.close();
			open = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void quit() {
		sendMessage(new QuitMessage(id));
		// simple security to prevent clients closing other clients
		closeRequested = true;
	}
	
	public void sendMessage(Message message) {
		try {
			out.writeObject(message);
			System.out.println("CLIENT: Sent message ["+message.toString()+"]");
		} catch (IOException e) {
			System.err.println("CLIENT Unable to send message!");
		}
	}
	
	public boolean isOpen() {
		return open;
	}

	public byte getID() {
		return id;
	}
}