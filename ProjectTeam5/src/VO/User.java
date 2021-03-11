package VO;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class User {

	private String userName;
	private Socket socket;
	private ObjectOutputStream outputStream;
	private int userNumber;
	
	public User() {}
	public User(String userName) {
		this.userName = userName;
	}
	public User(String userName, Socket socket, ObjectOutputStream outputStream) {
		super();
		this.userName = userName;
		this.socket = socket;
		this.outputStream = outputStream;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}
	public void setOutputStream(ObjectOutputStream outputStream) {
		this.outputStream = outputStream;
	}
	public int getUserNumber() {
		return userNumber;
	}
	public void setUserNumber(int userNumber) {
		this.userNumber = userNumber;
	}
}
