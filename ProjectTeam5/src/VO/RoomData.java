package VO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoomData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String roomTitle;
	private String roomOwner;
	private int roomNumber;
	private int roomLimit;
	private boolean isPrivate = false;
	private String password;
	private List<String> roomUserNameList;
	private boolean isPlaying;
	private int ownerNumber;
	
	// 추가 사항
	private List<String> usedWordList;
	
	public RoomData() {
		this.roomUserNameList = new ArrayList<>();
		this.usedWordList = new ArrayList<>();
	}
	public RoomData(String roomTitle, String roomOwner, int roomNumber, boolean isPrivate, boolean isPlaying, int ownerNumber) {
		this.roomUserNameList = new ArrayList<>();
		this.usedWordList = new ArrayList<>();
		this.roomTitle = roomTitle;
		this.roomOwner = roomOwner;
		this.roomNumber = roomNumber;
		this.isPrivate = isPrivate;
		this.isPlaying = isPlaying;
		this.ownerNumber = ownerNumber;
	}
	public String getRoomTitle() {
		return roomTitle;
	}
	public void setRoomTitle(String roomTitle) {
		this.roomTitle = roomTitle;
	}
	public String getRoomOwner() {
		return roomOwner;
	}
	public void setRoomOwner(String roomOwner) {
		this.roomOwner = roomOwner;
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
	public List<String> getRoomUserNameList() {
		return roomUserNameList;
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
	public List<String> getUsedWordList() {
		return usedWordList;
	}
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
}
