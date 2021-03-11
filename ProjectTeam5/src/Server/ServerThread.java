package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import GUI.FrameServer;
import VO.Lobby;
import VO.Room;
import VO.RoomData;
import VO.User;

@SuppressWarnings("unchecked")
public class ServerThread implements Runnable {

	// 현재 클래스 내에서 지속적으로 사용할 객체 미리 생성
	private Socket socket;
	private User user;
	private ObjectInputStream inputStream;
	private List<Socket> allUserSocket;
	private Lobby lobby;
	private List<Object> sendList;
	private List<Object> recieveList;
	private HashMap<Object, Object> sendMap;
	private HashMap<Object, Object> recieveMap;
	private Map<String, List<String>> allWordMap;
	private FrameServer frame;
	private Random random = new Random();
	
	// 추가
//	private Room userRoom;
	private List<String> initialKeys;
	private int time = 100;

	// 생성자
	public ServerThread(Socket socket, User user, ObjectInputStream inputStream, List<Socket> allUserSocket,
			Lobby lobby, Map<String, List<String>> allWordMap, FrameServer frame) {
		this.socket = socket;
		this.user = user;
		this.inputStream = inputStream;
		this.allUserSocket = allUserSocket;
		this.lobby = lobby;
		this.allWordMap = allWordMap;
		this.frame = frame;
	} // 생성자 - END

	// 로비 새로고침 METHOD
	private void refreshLobby() {

		sendMap = new HashMap<>();
		
		// 로비 유저 목록
		sendList = new ArrayList<>();
		if (lobby.getUserList() != null) {
			for (User u : lobby.getUserList()) {
				sendList.add(u.getUserName() + "|" + u.getUserNumber());
			}
			sendMap.put("LOBBYUSERLIST", sendList);			
		}
		// 방 목록
		if (lobby.getRoomList().size() > 0) {
			sendList = new ArrayList<>();
			for (Room r : lobby.getRoomList()) {
				RoomData rd = new RoomData();
				rd.setRoomTitle(r.getRoomTitle());
				rd.setRoomOwner(r.getRoomOwner());
				rd.setRoomNumber(r.getRoomNumber());
				rd.setRoomLimit(r.getRoomLimit());
				rd.setPrivate(r.isPrivate());
				rd.setPlaying(r.isPlaying());
				for (User u : r.getUserList()) {
					rd.getRoomUserNameList().add(u.getUserName());
				}
				sendList.add(rd);
			}
			sendMap.put("LOBBYROOMLIST", sendList);			
		} else {
			sendMap.put("NOROOM", null);
		}
		// 로비 유저목록과 방 목록을 담은 sendMap 전송
		try {
			for (User u : lobby.getUserList()) {
				u.getOutputStream().writeObject(sendMap);
			}
		} catch (IOException e) {
			System.out.println("ERROR C");
			e.printStackTrace();
		}
		frame.recieveRefresh();
	} // 로비 새로고침 METHOD - END
	
	// 방 새로고침
	public void refreshRoom(int roomNumber) {
		try {
			sendMap = new HashMap<>();
			for (Room r : lobby.getRoomList()) {
				if (r.getRoomNumber() == roomNumber) {
					RoomData rd = new RoomData();
					rd.setRoomLimit(r.getRoomLimit());
					rd.setRoomNumber(r.getRoomNumber());
					rd.setRoomOwner(r.getRoomOwner());
					rd.setRoomTitle(r.getRoomTitle());
					rd.setOwnerNumber(r.getOwnerNumber());
					rd.setPlaying(r.isPlaying());
					for (User u : r.getUserList()) {
						rd.getRoomUserNameList().add(u.getUserName());
					}
					sendMap.put("SETROOMINFO", rd);
					for (User u : r.getUserList()) {
						u.getOutputStream().writeObject(sendMap);
					}
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR B");
			e.printStackTrace();
		}
	} // 방 새로고침 - END

	// THREAD
	@Override
	public void run() {
		try {
			while (true) {
				// 보낼 오브젝트 초기화
				sendMap = null;
				sendList = null;
				// 클라이언트로부터 오브젝트 수신
				recieveMap = (HashMap<Object, Object>) inputStream.readObject();
				// 에러 방지
				if (recieveMap == null)
					break;
				// 클라이언트에서 처음 접속 (유저의 닉네임 값 수신)
				else if (recieveMap.containsKey("USERNAME")) {
					// 클라이언트에게 받은 유저의 아이디를 쓰레드의 User 객체에 저장
					user.setUserName((String) recieveMap.get("USERNAME"));
					// 전체 유저 목록에 클라이언트의 소켓 추가
					allUserSocket.add(socket);
					// 로비 유저 목록에 클라이언트의 유저 객체 추가
					lobby.getUserList().add(user);
					// 서버에서 사용되는 유저번호를 클라이언트에게 전송
					sendMap = new HashMap<>();
					sendMap.put("USERNUMBER", user.getUserNumber());
					user.getOutputStream().writeObject(sendMap);
					// K - ENTERLOBBY, V - 새로 접속한 유저의 닉네임을 가지는 sendMap을 로비내의 모든 유저에게 전송
					sendMap = new HashMap<>();
					sendMap.put("ENTERLOBBY", user.getUserName());
					for (User u : lobby.getUserList()) {
						u.getOutputStream().writeObject(sendMap);
					}
					// 로비 새로고침
					refreshLobby();
				} // 클라이언트에서 처음 접속 (유저의 닉네임 값 수신) - END

				// 로비에서 입력받은 채팅을 로비내의 모든 유저에게 송신
				else if (recieveMap.containsKey("LOBBYCHAT")) {
					for (User u : lobby.getUserList()) {
						u.getOutputStream().writeObject(recieveMap);
					}
				} // END
				
				// 방 생성 신호 수신
				else if (recieveMap.containsKey("ROOMMAKE")) {
					// 클라이언트에게 넘어온 RoomData 객체 수신
					RoomData rd = (RoomData) recieveMap.get("ROOMMAKE");
					// 방을 만든 클라이언트에게 받은 데이터로 Room 객체 생성
					Room room = new Room();
					room.setRoomTitle(rd.getRoomTitle());
					room.setRoomOwner(rd.getRoomOwner());
					room.setRoomLimit(rd.getRoomLimit());
					room.setPrivate(rd.isPrivate());
					if (room.isPrivate())
						room.setPassword(rd.getPassword());
					room.setRoomNumber(lobby.getLastUsedNumber());
					room.setOwnerNumber(user.getUserNumber());
					// 방 번호가 999까지 갔다면 다음 방 번호를 1부터 시작하게 수정
					if (room.getRoomNumber() == 999)
						lobby.setLastUsedNumber(0);
					// 방을 만든 클라이언트를 Room 객체의 유저 목록에 추가
					room.getUserList().add(user);
					// lobby 객체의 방 목록 리스트에 새로 만든 Room 객체 추가
					lobby.getRoomList().add(room);
					// 방을 만든 클라이언트를 로비의 유저 목록에서 제거 (방을 만들면 방으로 자동으로 들어가지기 때문에)
					for (User u : lobby.getUserList()) {
						if (user.getSocket().equals(u.getSocket())) {
							lobby.getUserList().remove(u);
							break;
						}
					}
					// 새로 만든 방의 번호를 방을 만든 클라이언트에게 송신
					sendMap = new HashMap<>();
					rd.setRoomNumber(room.getRoomNumber());
					rd.setOwnerNumber(room.getOwnerNumber());
					sendMap.put("ENTERROOM", rd);
					user.getOutputStream().writeObject(sendMap);
					// 로비, 방 새로고침
					refreshLobby();
					refreshRoom(rd.getRoomNumber());
				}
				
				// 방 입장 신호 수신
				else if (recieveMap.containsKey("ENTEREXISTROOM")) {
					// 입장하려는 방의 번호를 정수로 받음
					int roomNumber = (int) recieveMap.get("ENTEREXISTROOM");
					// 방 번호로 방 찾기
					for (Room r : lobby.getRoomList()) {
						if (roomNumber == r.getRoomNumber()) {
							if (!r.isPlaying()) { // 방이 게임중이지 않을 때
								if (r.getUserList().size() < r.getRoomLimit()) { // 들어가려는 방이 유저가 들어갈 자리가 있을 때
									if (r.isPrivate()) { // 들어가려는 방이 비공개방일 경우 클라이언트에게 비공개 방이라는 신호와 함께 방 번호 및 비밀번호 전송
										sendMap = new HashMap<>();
										sendList = new ArrayList<>();
										sendList.add(r.getPassword());
										sendList.add(r.getRoomNumber());
										sendMap.put("ROOMISPRIVATE", sendList);
										user.getOutputStream().writeObject(sendMap);
										// 들어가려는 방이 공개방일 경우
									} else {
										// 방에 입장하려는 클라이언트를 로비 유저목록에서 제거
										for (User u : lobby.getUserList()) {
											if (user.getSocket().equals(u.getSocket())) {
												lobby.getUserList().remove(u);
												break;
											}
										}
										// 데이터 전송용 RoomData 객체 생성 후 sendMap에 추가
										sendMap = new HashMap<>();
										r.getUserList().add(user);
										RoomData rd = new RoomData();
										rd.setRoomLimit(r.getRoomLimit());
										rd.setRoomNumber(r.getRoomNumber());
										rd.setRoomOwner(r.getRoomOwner());
										rd.setRoomTitle(r.getRoomTitle());
										rd.setOwnerNumber(r.getOwnerNumber());
										rd.setPlaying(r.isPlaying());
										for (User u : r.getUserList()) {
											rd.getRoomUserNameList().add(u.getUserName());
										}
										sendMap.put("ENTERROOM", rd);
										// sendMap 전송
										user.getOutputStream().writeObject(sendMap);
										// 로비, 방 새로고침
										refreshLobby();
										refreshRoom(rd.getRoomNumber());
										break;
									} // 들어가려는 방이 공개방일 경우 - END
								} // 들어가려는 방이 유저가 들어갈 자리가 있을 때 - END
								// 들어가려는 방이 자리가 없을 때
								else {
									sendMap = new HashMap<>();
									sendMap.put("ROOMISFULL", "ROOMISFULL");
									user.getOutputStream().writeObject(sendMap);
								}
								break; // 찾은 후 로직 돌렸으니 break
							} // 방이 게임중이지 않을 때
						} // 방 번호에 맞는 방을 찾았을 때 - END
					} // 방 번호로 방 찾기 - END
				} // 방 입장 신호 수신 - END

				// 비공개 상태인 방의 비밀번호를 맞게 입력했을 때 비공개방에 유저 입장
				else if (recieveMap.containsKey("ENTERPRIVATEROOM")) {
					Room room = new Room();
					for (Room r : lobby.getRoomList()) {
						if (r.getRoomNumber() == (int) recieveMap.get("ENTERPRIVATEROOM")) {
							room = r;
							break;
						}
					}
					if (!room.isPlaying()) { // 방이 게임중이지 않을 때
						// 방에 입장하려는 클라이언트를 로비 유저목록에서 제거
						for (User u : lobby.getUserList()) {
							if (user.getSocket().equals(u.getSocket())) {
								lobby.getUserList().remove(u);
								break;
							}
						}
						// 데이터 전송용 RoomData 객체 생성 후 sendMap에 추가
						sendMap = new HashMap<>();
						room.getUserList().add(user);
						RoomData rd = new RoomData();
						rd.setRoomLimit(room.getRoomLimit());
						rd.setRoomNumber(room.getRoomNumber());
						rd.setRoomOwner(room.getRoomOwner());
						rd.setRoomTitle(room.getRoomTitle());
						rd.setOwnerNumber(room.getOwnerNumber());
						for (User u : room.getUserList()) {
							rd.getRoomUserNameList().add(u.getUserName());
						}
						sendMap.put("ENTERROOM", rd);
						// sendMap 전송
						user.getOutputStream().writeObject(sendMap);
						// 로비, 방 새로고침
						refreshLobby();
						refreshRoom(rd.getRoomNumber());
					}
				} // 비공개 상태인 방의 비밀번호를 맞게 입력했을 때 비공개방에 유저 입장 - END
				
				// 로비에서 유저가 닉네임 변경 요청
				else if (recieveMap.containsKey("CHANGENAME")) {
					String name = (String) recieveMap.get("CHANGENAME");
					user.setUserName(name);
					refreshLobby();
				} // END
				
				// 로비에서 유저가 입장하고 싶은 방 번호 입력
				else if (recieveMap.containsKey("FINDROOMNUMBER")) {
					int roomNumber = (int) recieveMap.get("FINDROOMNUMBER");
					boolean swt = true;
					sendMap = new HashMap<>();
					for (Room r : lobby.getRoomList()) {
						if (r.getRoomNumber() == roomNumber) {
							sendMap.put("EXISTROOMNUMBER", roomNumber);
							user.getOutputStream().writeObject(sendMap);
							swt = false;
							break;
						}
					}
					if (swt) {
						sendMap.put("EXISTROOMNUMBER", -1);
						user.getOutputStream().writeObject(sendMap);
					}
				}
				
				// 클라이언트가 보내온 채팅 방에 다른 유저들한테 보내줌 // 추가
				else if (recieveMap.containsKey("ROOMCHAT")) {
					recieveList = (List<Object>) recieveMap.get("ROOMCHAT");
					int roomNumber = (int) recieveList.get(3); // 클라이언트로 부터 받아온 방번호
					for (Room r : lobby.getRoomList()) {
						if (r.getRoomNumber() == roomNumber) { // 전체 방 리스트에서 방번호 비교
							for (User u : r.getUserList()) { // 비교해서 번호 같은 방의 유저들한테
								u.getOutputStream().writeObject(recieveMap); // 채팅 전송
							}
							break;
						}
					}
				} // END
				
				// 방에서 나가기 수신
				else if (recieveMap.containsKey("EXITROOM")) {
					sendMap = new HashMap<>();
					int roomNumber = (int) recieveMap.get("EXITROOM");
					
					for (Room r : lobby.getRoomList()) {
						if (r.getRoomNumber() == roomNumber) {
							r.getUserList().remove(user);
							if (r.getUserList().size() == 0)
								lobby.getRoomList().remove(r);
							else {
								r.setRoomOwner(r.getUserList().get(0).getUserName());
								r.setOwnerNumber(r.getUserList().get(0).getUserNumber());
							}
							break;
						}
					}
					lobby.getUserList().add(user);
					// 로비, 방 새로고침
					refreshLobby();
					refreshRoom(roomNumber);
				} // 방 나가기 - END
				
				// 게임 시작
				else if (recieveMap.containsKey("GAMESTART")) {
					int roomNumber = (int) recieveMap.get("GAMESTART");
					for (Room r : lobby.getRoomList()) {
						if (r.getRoomNumber() == roomNumber) {
							r.setPlaying(true);
							sendMap = new HashMap<>();
							sendMap.put("GAMESTART", null);
							initialKeys = new ArrayList<>(allWordMap.keySet());
							int randomInt = random.nextInt(initialKeys.size());
							sendMap.put("INITIAL", initialKeys.get(randomInt));
							r.getWordList().addAll(allWordMap.get(initialKeys.get(randomInt)));
							for (User u : r.getUserList()) {
								r.getTurnQueue().add(u.getUserNumber());
							}
							for (User u : r.getUserList()) {
								u.getOutputStream().writeObject(sendMap);
							}
							break;
						}
					}
				} // GAMESTART 수신 - END
				
				else if (recieveMap.containsKey("COUNTDOWNEND")) {
					int roomNumber = (int) recieveMap.get("COUNTDOWNEND");
					for (Room r : lobby.getRoomList()) {
						if (r.getRoomNumber() == roomNumber) {
							sendMap = new HashMap<>();
							sendMap.put("TIMER", time);
//							r.getTurnQueue().add(user.getUserNumber());
							user.getOutputStream().writeObject(sendMap);
							break;
						}
					}
				}
				
				else if (recieveMap.containsKey("GETTURN")) {
					int roomNumber = (int) recieveMap.get("GETTURN");
					for (Room r : lobby.getRoomList()) {
						if (r.getRoomNumber() == roomNumber) {
							sendMap = new HashMap<>();
							sendMap.put("TURN", r.getTurnQueue().peek());
							int turnIndex = -1;
							for (User u : r.getUserList()) {
								if (u.getUserNumber() == r.getTurnQueue().peek())
									turnIndex = r.getUserList().indexOf(u);
							}
							sendMap.put("ROOMINDEX", turnIndex);
							user.getOutputStream().writeObject(sendMap);
							break;
						}
					}
				}
				
				else if (recieveMap.containsKey("SENDTURN")) {
					int roomNumber = (int) recieveMap.get("SENDTURN");
					for (Room r : lobby.getRoomList()) {
						if (r.getRoomNumber() == roomNumber) {
							sendMap = new HashMap<>();
							sendMap.put("TURN", r.getTurnQueue().peek());
							int turnIndex = -1;
							for (User u : r.getUserList()) {
								if (u.getUserNumber() == r.getTurnQueue().peek())
									turnIndex = r.getUserList().indexOf(u);
							}
							sendMap.put("ROOMINDEX", turnIndex);
							user.getOutputStream().writeObject(sendMap);
							break;
						}
					}
				}
				
				else if (recieveMap.containsKey("DAP")) {
					String dap = (String) recieveMap.get("DAP");
					int roomNumber = (int) recieveMap.get("ROOMNUMBER");
					for (Room r : lobby.getRoomList()) {
						if (r.getRoomNumber() == roomNumber) {
							if (!r.getUsedWordList().contains(dap) && r.getWordList().contains(dap)) {
								r.getUsedWordList().add(dap);
								r.getTurnQueue().add(r.getTurnQueue().poll());
								sendMap = new HashMap<>();
								sendMap.put("STOPTIME", dap);
								sendMap.put("ROOMOWNER", r.getOwnerNumber());
								for (User u : r.getUserList()) {
									u.getOutputStream().writeObject(sendMap);
								}
								
								// 추가
							} else {
								if (r.getWordList().contains(dap)) {
									sendMap = new HashMap<>();
									sendMap.put("ISUSED", null);
									user.getOutputStream().writeObject(sendMap);									
								} else {
									sendMap = new HashMap<>();
									sendMap.put("NOWORD", null);
									user.getOutputStream().writeObject(sendMap);	
								}
							}
								// 까지
							break;
						}
					}
				}
				
				else if (recieveMap.containsKey("TIMEOVER")) {
					int roomNumber = (int) recieveMap.get("TIMEOVER");
					for (Room r : lobby.getRoomList()) {
						if (r.getRoomNumber() == roomNumber) {
							r.getTurnQueue().poll();
							sendMap = new HashMap<>();
							if (r.getTurnQueue().size() == 1) {
								sendMap.put("ENDGAME", r.getTurnQueue().poll());
								sendMap.put("ROOMOWNER", r.getOwnerNumber());
							}
							else {
								sendMap.put("STOPTIME", null);
								sendMap.put("ROOMOWNER", r.getOwnerNumber());
							}
							for (User u : r.getUserList()) {
								u.getOutputStream().writeObject(sendMap);
							}
							break;
						}
					}
				}
				
				else if (recieveMap.containsKey("REGAMEPOSSIBLE")) {
					int roomNumber = (int) recieveMap.get("REGAMEPOSSIBLE");
					for (Room r : lobby.getRoomList()) {
						if (roomNumber == r.getRoomNumber()) {
							r.setPlaying(false);
							r.getUsedWordList().clear();
							r.getTurnQueue().clear();
							r.getWordList().clear();
							sendMap = new HashMap<>();
							sendMap.put("REGAMEPOSSIBLE", null);
							user.getOutputStream().writeObject(sendMap);
						}
					}
				}
				
				
			} // WHILE - END
		} catch (SocketException e) {
			System.out.println("클라이언트에서 접속 종료");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 전체 유저 목록에서 제거
			for (Socket s : allUserSocket) {
				if (socket.equals(s)) {
					allUserSocket.remove(s);
					break;
				}
			}
			// 로비 목록에서 제거
			for (User u : lobby.getUserList()) {
				if (socket.equals(u.getSocket())) {
					lobby.getUserList().remove(u);
					break;
				}
			}
			// 방 유저목록에서 제거
			for (Room r : lobby.getRoomList()) {
				boolean breakSwitch = false;
				for (User u : r.getUserList()) {
					if (socket.equals(u.getSocket())) {
						r.getUserList().remove(u);
						breakSwitch = true;
						break;
					}
				}
				if (breakSwitch) {
					if (r.getUserList().size() == 0) {
						lobby.getRoomList().remove(r);
					} else {
						r.setRoomOwner(r.getUserList().get(0).getUserName());
						r.setOwnerNumber(r.getUserList().get(0).getUserNumber());
						refreshRoom(r.getRoomNumber());
					}
					break;
				}
			}
			// 로비 새로고침
			refreshLobby();
		}
	}
}