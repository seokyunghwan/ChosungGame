package VO;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Room {

	private List<User> userList;
	private String roomTitle;
	private String roomOwner;
	private int roomNumber;
	private int roomLimit;
	private boolean isPrivate = false;
	private String password;
	private boolean isPlaying = false;
	private int ownerNumber;
	
	// 
	private Queue<Integer> turnQueue;
	
	// 추가 사항
	private List<String> wordList;
	private List<String> usedWordList;
//	private Map<Integer, Integer> turn;
	// 추가 사항 중 의문
//	private List<Integer> turnList;
	
	public Room() {
		this.userList = new ArrayList<>();
		this.wordList = new ArrayList<>();
		this.usedWordList = new ArrayList<>();
//		this.turn = new HashMap<>();
//		this.turnList = new ArrayList<>();
		this.turnQueue = new LinkedList<>();
	}
	public Room(String roomTitle, String roomOwner, int roomNumber, int roomLimit, int ownerNumber) {
		this.userList = new ArrayList<>();
		this.wordList = new ArrayList<>();
		this.usedWordList = new ArrayList<>();
//		this.turn = new HashMap<>();
//		this.turnList = new ArrayList<>();
		this.turnQueue = new LinkedList<>();
		this.roomTitle = roomTitle;
		this.roomOwner = roomOwner;
		this.roomNumber = roomNumber;
		this.roomLimit = roomLimit;
		this.ownerNumber = ownerNumber;
	}
	
	public List<User> getUserList() {
		return userList;
	}
	public String getRoomTitle() {
		return roomTitle;
	}
	public void setRoomTitle(String roomTitle) {
		this.roomTitle = roomTitle;
	}
	public int getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	public int getRoomLimit() {
		return roomLimit;
	}
	public void setRoomLimit(int roomLimit) {
		this.roomLimit = roomLimit;
	}
	public String getRoomOwner() {
		return roomOwner;
	}
	public void setRoomOwner(String roomOwner) {
		this.roomOwner = roomOwner;
	}
	public boolean isPrivate() {
		return isPrivate;
	}
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getWordList() {
		return wordList;
	}
	public List<String> getUsedWordList() {
		return usedWordList;
	}
//	public Map<Integer, Integer> getTurn() {
//		return turn;
//	}
//	public List<Integer> getTurnList() {
//		return turnList;
//	}
	public boolean isPlaying() {
		return isPlaying;
	}
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	public int getOwnerNumber() {
		return ownerNumber;
	}
	public void setOwnerNumber(int ownerNumber) {
		this.ownerNumber = ownerNumber;
	}
	public Queue<Integer> getTurnQueue() {
		return turnQueue;
	}
}
