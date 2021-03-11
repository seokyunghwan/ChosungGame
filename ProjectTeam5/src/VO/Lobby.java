package VO;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

	private List<User> userList;
	private List<Room> roomList;
	private int lastUsedNumber;
	
	public Lobby() {
		userList = new ArrayList<>();
		roomList = new ArrayList<>();
		lastUsedNumber = 1;
	}
	public List<User> getUserList() {
		return userList;
	}
	public List<Room> getRoomList() {
		return roomList;
	}
	public void setLastUsedNumber(int lastUsedNumber) {
		this.lastUsedNumber = lastUsedNumber;
	}
	public int getLastUsedNumber() {
		return lastUsedNumber++;
	}
}
