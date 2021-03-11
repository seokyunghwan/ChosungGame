package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import Client.ClientThread;
import Client.TimerThread;
import GUIInterfaceSetting.TableDataSetting;
import VO.RoomData;
import VO.User;

@SuppressWarnings({"unchecked", "deprecation"})
public class FrameMain extends JFrame implements Runnable, ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	// 서버와의 데이터 통신 관련
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private HashMap<Object, Object> sendMap;
	private HashMap<Object, Object> recieveMap;
	private List<Object> sendList;
	private List<Object> recieveList;
	private List<RoomData> roomDataList;
	private int roomNumber;
	
	//
	private ClientThread thread;
	private Thread th;
	private String initial;
	private TimerThread th2;
	
	// 로비의 방 목록 테이블 관련
	private DefaultTableModel model;
	private TableDataSetting dataSetter = new TableDataSetting();;
	private String[] stringArray;
	private Object[][] tableData;
	
	// 서버와 일부 같은 정보를 갖는 유저 객체
	private User user;
	
	// 프레임 안에 넣을 패널 및 새로운 프레임
	private PanelConnect pnConnect;
	private PanelLobby pnLobby;
	private PanelRoom pnRoom;
	private FrameSmallInput frSmallInput;
	
	// 추가
//	private List<Object> RoomRecieveList;
//	private RoomData roomData;

	public FrameMain() {
		// 프레임 초기 설정
		setTitle("ㅊㅅㅁㅊㄱ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(816, 639); // 패널에서 800, 600 사이즈로 정하려면 프레임은 살짝 커야함
		// 패널, 프레임 객체 미리 생성
		frSmallInput = new FrameSmallInput();
		pnConnect = new PanelConnect(this);
		pnLobby = new PanelLobby(this);
		pnRoom = new PanelRoom(this);
		// 추가
		thread = new ClientThread(this);
		// pnConnect 패널로 전환
		changePanel(pnConnect);
		// ActionListener 추가
		addActionListener();
	} // 생성자 - END
	
	// 패널 전환 METHOD
	public void changePanel(Component c) {
		getContentPane().removeAll();
		getContentPane().add(c);
		revalidate();
		repaint();
	} // METHOD - END
	
	// 각 패널들의 actionListener 추가
	private void addActionListener() {
		// FrameMain의 패널
		pnConnect.getBtnEnter().addActionListener(this);
		pnConnect.getTfUserName().addActionListener(this);
		pnLobby.getTextFieldChat().addActionListener(this);
		pnLobby.getBtnMakeRoom().addActionListener(this);
		pnLobby.getBtnFindRoom().addActionListener(this);
		pnLobby.getBtnChangeUserName().addActionListener(this);
		pnLobby.getBtnExitLobby().addActionListener(this);
		
		// pnRoom
		pnRoom.getTextFieldChat().addActionListener(this);
		pnRoom.getTextFieldGame().addActionListener(this);
		pnRoom.getBtnInputEnter().addActionListener(this);
		pnRoom.getBtnGameStart().addActionListener(this);
		pnRoom.getBtnExit().addActionListener(this);
		
		// 다른 프레임의 패널
		frSmallInput.getPnRoomMake().getBtnConfirm().addActionListener(this);
		frSmallInput.getPnPasswordInput().getPfPassword().addActionListener(this);
		frSmallInput.getPnPasswordInput().getBtnConfirm().addActionListener(this);
		frSmallInput.getPnChangeName().getTfUserName().addActionListener(this);
		frSmallInput.getPnChangeName().getBtnConfirm().addActionListener(this);
		frSmallInput.getPnConfirm().getBtnConfirm().addActionListener(this);
		frSmallInput.getPnFindRoom().getBtnConfirm().addActionListener(this);
		frSmallInput.getPnFindRoom().getTfRoomNumber().addActionListener(this);
	} // METHOD - END
	
	// 서버 접속 METHOD
	private void connectServer(String userName) {
		try {
			Socket socket = new Socket("192.168.7.114", 10000);
			this.outputStream = new ObjectOutputStream(socket.getOutputStream());
			this.inputStream = new ObjectInputStream(socket.getInputStream());
			this.user = new User(userName, socket, outputStream);
			this.sendMap = new HashMap<>();
			sendMap.put("USERNAME", user.getUserName());
			Thread th = new Thread(this);
			th.start();
			user.getOutputStream().writeObject(sendMap);
			changePanel(pnLobby);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // 서버 접속 METHOD - END
	
	// THREAD
	@Override
	public void run() {
		try {
			while (true) {
				
				// HashMap 객체 수신
				recieveMap = (HashMap<Object, Object>) inputStream.readObject();
				
				// 에러 방지
				if (recieveMap.isEmpty()) {
					break;
				}
				
				// 대기실의 유저목록과 방 목록 데이터 수신
				else if (recieveMap.containsKey("LOBBYUSERLIST")) {
					// 유저 목록 업데이트
					recieveList = (List<Object>) recieveMap.get("LOBBYUSERLIST");
					if (recieveList != null) {
						stringArray = new String[recieveList.size()];
						for (int i = 0; i < recieveList.size(); i++) {
							String name = ((String) recieveList.get(i)).split("\\|")[0];
							int number = Integer.parseInt(((String) recieveList.get(i)).split("\\|")[1]);
							if (number == user.getUserNumber()) {
								name += "(나)";
							}
							stringArray[i] = name;
						}
						pnLobby.getJlistLobbyUser().setListData(stringArray);
					}
					// 방 목록 업데이트
					if (recieveMap.containsKey("LOBBYROOMLIST")) {
						try {
							roomDataList = (List<RoomData>) recieveMap.get("LOBBYROOMLIST");
							if (roomDataList != null) {
								tableData = new Object[roomDataList.size()][3];
								int i = 0;
								for (RoomData rd : roomDataList) {
									tableData[i++] = dataSetter.getRowData(rd);
								}
								model = new DefaultTableModel(tableData, new String[] {"", "", ""});
								pnLobby.getJtableRoom().setModel(model);
								pnLobby.tableSet();
								pnLobby.getJtableRoom().updateUI();
							}
						} catch (Exception e) {
							System.out.println("ERROR");
						}
					}
					// 만들어진 방이 없을 때
					else if (recieveMap.containsKey("NOROOM")) {
						model = new DefaultTableModel();
						pnLobby.getJtableRoom().setModel(model);
					}
				} // 대기실의 유저목록과 방 목록 데이터 수신 - END
				
				// 서버로부터 받은 번호를 클라이언트 User 객체에 대입
				else if (recieveMap.containsKey("USERNUMBER")) {
					user.setUserNumber((int) recieveMap.get("USERNUMBER"));
				}
				
				// 서버로부터 받은 채팅내용 표시
				else if (recieveMap.containsKey("LOBBYCHAT")) {
					recieveList = (List<Object>) recieveMap.get("LOBBYCHAT");
					String userName = (String) recieveList.get(0);
					if ((int) recieveList.get(2) == user.getUserNumber()) {
						userName += "(나)";
					}
					pnLobby.getTextAreaChat().append("\n" + userName + " : " + (String) recieveList.get(1));
					pnLobby.getTextAreaChat().setCaretPosition(pnLobby.getTextAreaChat().getText().length());
				}
				
				// 서버로부터 받은 유저 로비 입장 메시지 표시
				else if (recieveMap.containsKey("ENTERLOBBY")) {
					pnLobby.getTextAreaChat().append("\n" + (String) recieveMap.get("ENTERLOBBY") + "님이 입장하셨습니다.");
				}
				
				// 게임 방 화면 전환
				else if (recieveMap.containsKey("ENTERROOM")) {
					frSmallInput.dispose();
					
					RoomData rd = (RoomData) recieveMap.get("ENTERROOM");
					pnRoom.getTextAreaChat().setText("");
					pnRoom.getModel().removeAllElements();
					this.roomNumber = rd.getRoomNumber();

					
					changePanel(pnRoom);
				}
				
				// 비공개방 입장을 위한 비밀번호 수신
				else if (recieveMap.containsKey("ROOMISPRIVATE")) {
					recieveList = (List<Object>) recieveMap.get("ROOMISPRIVATE");
					frSmallInput.changePanel(frSmallInput.getPnPasswordInput());
					frSmallInput.getPnPasswordInput().getPfPassword().setText("");
					frSmallInput.setAlwaysOnTop(true);
					frSmallInput.setVisible(true);
					frSmallInput.getPnPasswordInput().setCorrectPw((String) recieveList.get(0));
					frSmallInput.getPnPasswordInput().setRoomNumber((int) recieveList.get(1));
				}
				
				// 서버에 보낸 들어갈 방 번호에 맞는 방이 존재할 경우
				else if (recieveMap.containsKey("EXISTROOMNUMBER")) {
					int number = (int) recieveMap.get("EXISTROOMNUMBER");
					if (number != -1) {
						frSmallInput.dispose();
						sendMap = new HashMap<>();
						sendMap.put("ENTEREXISTROOM", number);
						outputStream.writeObject(sendMap);
					} else
						JOptionPane.showMessageDialog(frSmallInput, "입력한 방 번호를 찾을 수 없습니다.");
				}
				
				// 들어가려는 방이 가득찼을 경우
				else if (recieveMap.containsKey("ROOMISFULL")) {
					JOptionPane.showMessageDialog(this, "방에 더이상 들어갈 자리가 없습니다.");
				}
				
				// 방 채팅 표시 // 추가 // 표시 수정필
				else if (recieveMap.containsKey("ROOMCHAT")) {
					recieveList = (List<Object>) recieveMap.get("ROOMCHAT");
					String userName = (String) recieveList.get(0);
					String chat = (String) recieveList.get(1);
					int userNumber = (int) recieveList.get(3);

					if (user.getUserNumber() == userNumber)
						pnRoom.getTextAreaChat().append("\n" + userName + " : " + chat);
					else
						pnRoom.getTextAreaChat().append("\n" + userName + " : " + chat);
					pnRoom.getTextAreaChat().setCaretPosition(pnRoom.getTextAreaChat().getText().length());
				}
				
				// 방 정보 수신
				else if (recieveMap.containsKey("SETROOMINFO")) {
					RoomData rd = (RoomData) recieveMap.get("SETROOMINFO");
					if (user.getUserNumber() == rd.getOwnerNumber()) {
						pnRoom.getBtnGameStart().setText("START");
						if (rd.getRoomUserNameList().size() >= 2)
							pnRoom.getBtnGameStart().setEnabled(true);
						else
							pnRoom.getBtnGameStart().setEnabled(false);
					} else {
						pnRoom.getBtnGameStart().setText("WAITING");
						pnRoom.getBtnGameStart().setEnabled(false);
					}
					stringArray = new String[rd.getRoomUserNameList().size()];
					for (int i = 0; i < stringArray.length; i++) {
						stringArray[i] = rd.getRoomUserNameList().get(i);
					}
					pnRoom.getRoomUserList().setListData(stringArray);
				}
				
				// 서버에서 게임 시작하라고 다시 보내옴 초성 / 턴 보내줌
				else if (recieveMap.containsKey("GAMESTART")) {
					pnRoom.getModel().removeAllElements();
					initial = (String) recieveMap.get("INITIAL");
					pnRoom.getBtnGameStart().setEnabled(false);
					pnRoom.getBtnExit().setEnabled(false);
					thread.setMethodNumber(1);
					th = new Thread(thread);
					th.start();
				}
				
				else if (recieveMap.containsKey("TIMER")) {
					sendMap = new HashMap<>();
					sendMap.put("SENDTURN", roomNumber);
					user.getOutputStream().writeObject(sendMap);
				}
				
				else if (recieveMap.containsKey("TURN")) {
					int userNumber = (int) recieveMap.get("TURN");
					int roomIndex = (int) recieveMap.get("ROOMINDEX");
					if (user.getUserNumber() == userNumber) {
						pnRoom.getTextFieldGame().setEditable(true);
						pnRoom.getTextFieldGame().setBackground(Color.WHITE);
						pnRoom.getTextFieldGame().grabFocus();
					} else {
						pnRoom.getTextFieldGame().setEditable(false);
						pnRoom.getTextFieldGame().setBackground(Color.CYAN);
						pnRoom.getTextFieldGame().setText("");
					}
					if (roomIndex != -1)
						pnRoom.getRoomUserList().setSelectedIndex(roomIndex);
					if (th2 == null) {
						th2 = new TimerThread(this);
						th2.start();
					} else {
						if (th2.getState() == Thread.State.TERMINATED) {
							th2 = new TimerThread(this);
							th2.start();
						}
					}
				}
				
				else if (recieveMap.containsKey("STOPTIME")) {
					th2.stop();
					Object obj = recieveMap.get("STOPTIME");
					if (obj != null) {
						String dap = (String) obj;
						pnRoom.getModel().add(0, dap);
						pnRoom.getListUsedWord().updateUI();
					}
					sendMap = new HashMap<>();
					sendMap.put("GETTURN", roomNumber);
					outputStream.writeObject(sendMap);
				}
				
				else if (recieveMap.containsKey("ENDGAME")) {
					th2.stop();
					int userNumber = (int) recieveMap.get("ENDGAME");
					int roomOwner = (int) recieveMap.get("ROOMOWNER");
					if (user.getUserNumber() == userNumber) {
						JOptionPane.showMessageDialog(pnRoom, "승리하셨습니다.");
					} else {
						JOptionPane.showMessageDialog(pnRoom, "게임이 끝났습니다.");
					}
					pnRoom.getTextFieldGame().setBackground(Color.CYAN);
					pnRoom.getTextFieldGame().setEditable(false);
					pnRoom.getTextFieldGame().setText("");
					pnRoom.getBtnExit().setEnabled(true);
					if (user.getUserNumber() == roomOwner) {
						sendMap = new HashMap<>();
						sendMap.put("REGAMEPOSSIBLE", roomNumber);
						user.getOutputStream().writeObject(sendMap);
					}
				}
				
				else if (recieveMap.containsKey("REGAMEPOSSIBLE")) {
					pnRoom.getBtnGameStart().setEnabled(true);
				}
				
				else if (recieveMap.containsKey("ISUSED") || recieveMap.containsKey("NOWORD")) {
					if (recieveMap.containsKey("ISUSED")) {
						pnRoom.getLblGameInfo().setText("이미 사용된 단어입니다.");
					} else {
						pnRoom.getLblGameInfo().setText("없는 단어입니다.");
					}
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						
						@Override
						public void run() {
							pnRoom.getLblGameInfo().setText("");
						}
					}, 1500);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "서버가 종료되었습니다.");
			System.exit(0);
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		try {
			
			// 닉네임 입력 후 첫 접속 (키보드 엔터)
			if (ev.getSource() == pnConnect.getTfUserName()) {
				if (!pnConnect.getTfUserName().getText().equals(""))
					// 서버 접속 METHOD 호출
					connectServer(pnConnect.getTfUserName().getText());
				else
					JOptionPane.showMessageDialog(pnConnect, "닉네임을 입력하세요.");
			} // END
			
			// 닉네임 입력 후 첫 접속 (접속 버튼 클릭)
			else if (ev.getSource() == pnConnect.getBtnEnter()) {
				if (pnConnect.getTfUserName().getText().equals("닉네임을 입력하세요")) {
					JOptionPane.showMessageDialog(pnConnect, "닉네임을 입력하세요.");
				} else if (pnConnect.getTfUserName().getText().equals("")) {
					JOptionPane.showMessageDialog(pnConnect, "닉네임을 입력하세요.");
				} else
					// 서버 접속 METHOD 호출
					connectServer(pnConnect.getTfUserName().getText());
			} // END
			
			// 로비에서 유저가 채팅 입력
			else if (ev.getSource() == pnLobby.getTextFieldChat()) {
				if (!pnLobby.getTextFieldChat().getText().equals("")) {
					sendMap = new HashMap<>();
					sendList = new ArrayList<>();
					sendList.add(user.getUserName());
					sendList.add(pnLobby.getTextFieldChat().getText());
					sendList.add(user.getUserNumber());
					sendMap.put("LOBBYCHAT", sendList);
					outputStream.writeObject(sendMap);
					pnLobby.getTextFieldChat().setText("");
				}
			} // END
			
			// 로비에서 방 만들기 버튼 클릭
			else if (ev.getSource() == pnLobby.getBtnMakeRoom()) {
				int randomInt = (int) (Math.random() * frSmallInput.getPnRoomMake().getPreparedTitle().length);
				frSmallInput.getPnRoomMake().getTfRoomTitle().setText(frSmallInput.getPnRoomMake().getPreparedTitle()[randomInt]);
				frSmallInput.changePanel(frSmallInput.getPnRoomMake());
				frSmallInput.setAlwaysOnTop(true);
				frSmallInput.setVisible(true);
			} // END
			
			// 서버에 방 생성 요청
			else if (ev.getSource() == frSmallInput.getPnRoomMake().getBtnConfirm()) {
				if (!frSmallInput.getPnRoomMake().getTfRoomTitle().getText().equals("")) {
					RoomData rd = new RoomData();
					rd.setRoomTitle(frSmallInput.getPnRoomMake().getTfRoomTitle().getText());
					rd.setRoomLimit(frSmallInput.getPnRoomMake().getCbRoomLimit().getSelectedIndex() + 2);
					rd.setRoomOwner(user.getUserName());
					if (frSmallInput.getPnRoomMake().getRdbtnPrivate().isSelected()) {
						if (!frSmallInput.getPnRoomMake().getTfPassword().getText().equals("")) {
							rd.setPrivate(true);
							rd.setPassword(frSmallInput.getPnRoomMake().getTfPassword().getText());							
						}
					}
					frSmallInput.dispose();
					sendMap = new HashMap<>();
					sendMap.put("ROOMMAKE", rd);
					outputStream.writeObject(sendMap);
				}
			} // END
			
			// 비공개방 입장 비밀번호 입력 시
			else if ((ev.getSource() == frSmallInput.getPnPasswordInput().getPfPassword()) ||
					(ev.getSource() == frSmallInput.getPnPasswordInput().getBtnConfirm())) {
				PanelPasswordInput pnPI = frSmallInput.getPnPasswordInput();
				if (pnPI.getCorrectPw().equals(String.valueOf(pnPI.getPfPassword().getPassword()))) {
					frSmallInput.dispose();
					sendMap = new HashMap<>();
					sendMap.put("ENTERPRIVATEROOM", pnPI.getRoomNumber());
					outputStream.writeObject(sendMap);
				} else
					JOptionPane.showMessageDialog(pnPI, "비밀번호가 틀렸습니다.");
			} // END
			
			// 로비에서 나가기 버튼 클릭 시
			else if (ev.getSource() == pnLobby.getBtnExitLobby()) {
				frSmallInput.changePanel(frSmallInput.getPnConfirm());
				frSmallInput.getPnConfirm().getLblMessage().setText("정말로 나가시겠습니까?");
				frSmallInput.setAlwaysOnTop(true);
				frSmallInput.setVisible(true);
			}
			
			// 나가기 확정
			else if (ev.getSource() == frSmallInput.getPnConfirm().getBtnConfirm()) {
				dispose();
				System.exit(0);
			}
			
			// 로비에서 닉네임 변경 버튼 클릭 시
			else if (ev.getSource() == pnLobby.getBtnChangeUserName()) {
				frSmallInput.changePanel(frSmallInput.getPnChangeName());
				frSmallInput.getPnChangeName().getTfUserName().setText(user.getUserName());
				frSmallInput.setAlwaysOnTop(true);
				frSmallInput.setVisible(true);
			}
			
			// 닉네임 변경 화면에서 확인 시
			else if ((ev.getSource() == frSmallInput.getPnChangeName().getBtnConfirm()) ||
					(ev.getSource() == frSmallInput.getPnChangeName().getTfUserName())) {
				PanelChangeName pnCN = frSmallInput.getPnChangeName();
				String changedName = pnCN.getTfUserName().getText();
				if (!changedName.equals("")) {
					user.setUserName(changedName);
					sendMap = new HashMap<>();
					sendMap.put("CHANGENAME", changedName);
					outputStream.writeObject(sendMap);
					frSmallInput.dispose();
				} else
					JOptionPane.showMessageDialog(pnCN, "사용할 수 없는 이름입니다.");
			} // END
			
			// 로비에서 방 찾기 버튼 클릭
			else if (ev.getSource() == pnLobby.getBtnFindRoom()) {
				frSmallInput.changePanel(frSmallInput.getPnFindRoom());
				frSmallInput.setAlwaysOnTop(true);
				frSmallInput.setVisible(true);
			}
			
			// 방 찾기 화면에서 방 번호 입력 시
			else if ((ev.getSource() == frSmallInput.getPnFindRoom().getBtnConfirm()) ||
					(ev.getSource() == frSmallInput.getPnFindRoom().getTfRoomNumber())) {
				int number;
				try {
					number = Integer.parseInt(frSmallInput.getPnFindRoom().getTfRoomNumber().getText());
				} catch (Exception e) {
					number = -1;
					JOptionPane.showMessageDialog(frSmallInput, "잘못된 값입니다.");
				}
				if (number != -1) {
					sendMap = new HashMap<>();
					sendMap.put("FINDROOMNUMBER", number);
					outputStream.writeObject(sendMap);
				}
			}

			// 방에서 채팅 입력 // 추가
			else if (ev.getSource() == pnRoom.getTextFieldChat()) {
				sendMap = new HashMap<>();
				sendList = new ArrayList<>(); // 채팅에 대한 정보 입력 (이름, 채팅, 유저번호, 방번호)
				sendList.add(user.getUserName());
				sendList.add(pnRoom.getTextFieldChat().getText());
				sendList.add(user.getUserNumber());
				sendList.add(roomNumber);

				sendMap.put("ROOMCHAT", sendList);
				outputStream.writeObject(sendMap);
				pnRoom.getTextFieldChat().setText("");
			}
			
			else if (ev.getSource() == pnRoom.getBtnInputEnter()) {
				sendMap = new HashMap<>();
				sendList = new ArrayList<>(); // 채팅에 대한 정보 입력 (이름, 채팅, 유저번호, 방번호)
				sendList.add(user.getUserName());
				sendList.add(pnRoom.getTextFieldChat().getText());
				sendList.add(user.getUserNumber());
				sendList.add(roomNumber);

				sendMap.put("ROOMCHAT", sendList);
				outputStream.writeObject(sendMap);
				pnRoom.getTextFieldChat().setText("");
			}
			
			// 게임 시작 버튼 // 추가
			else if (ev.getSource() == pnRoom.getBtnGameStart()) {
				sendMap = new HashMap<>();
				sendMap.put("GAMESTART", roomNumber);
				outputStream.writeObject(sendMap);
			}
			
			// 방에서 나가기 버튼
			else if (ev.getSource() == pnRoom.getBtnExit()) {
	            sendMap = new HashMap<>();
	            changePanel(pnLobby);
	            sendMap.put("EXITROOM", roomNumber);
	            outputStream.writeObject(sendMap);
	         }
			
			// 게임 답 입력
			else if (ev.getSource() == pnRoom.getTextFieldGame()) {
				String dap = pnRoom.getTextFieldGame().getText();
				if (!dap.equals("")) {
					sendMap = new HashMap<>();
					sendMap.put("DAP", dap);
					sendMap.put("ROOMNUMBER", roomNumber);
					pnRoom.getTextFieldGame().setText("");
					outputStream.writeObject(sendMap);
				}
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "서버가 응답하지 않습니다.");
		}
	}
	// 서버에 방 입장 요청
	public void enterExistRoom(int roomNumber) {
		sendMap = new HashMap<>();
		sendMap.put("ENTEREXISTROOM", roomNumber);
		try {
			outputStream.writeObject(sendMap);
		} catch (IOException e) {
			System.out.println("ERROR A");
			e.printStackTrace();
		}
	}
	public void countdownEnd() {
		try {
			th.stop();
			sendMap = new HashMap<>();
			sendMap.put("COUNTDOWNEND", roomNumber);
			user.getOutputStream().writeObject(sendMap);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void timeOver() {
		try {
			if (pnRoom.getTextFieldGame().isEditable()) {
				sendMap = new HashMap<>();
				sendMap.put("TIMEOVER", roomNumber);
				user.getOutputStream().writeObject(sendMap);
				System.out.println("TIMEOVER");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PanelRoom getPnRoom() {
		return pnRoom;
	}
	public String getInitial() {
		return initial;
	}
}