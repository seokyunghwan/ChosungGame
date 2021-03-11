package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import GUI.FrameServer;
import JDBC.WordDAO;
import VO.Lobby;
import VO.User;

public class ServerMain {
	
	// 공유하거나 전달할 리스트 및 객체
	private Map<String, List<String>> allWordMap;
	private List<Socket> allUserSocket;
	private Lobby lobby;
	private ServerSocket svSocket;
	private int userNumber = 1;
	private FrameServer frame;
	// JDBC 객체
//	private Connection conn;
	private WordDAO dao;
	
	// 생성자
	public ServerMain(FrameServer frame) throws IOException {
		// 서버 생성
		svSocket = new ServerSocket(10000);
		// 초기 리스트 및 객체 생성
		allWordMap = new HashMap<>();
		allUserSocket = new ArrayList<>();
		lobby = new Lobby();
		// JDBC Connection 객체 받아오기
//		this.conn = frame.getConn();
		this.dao = frame.getDao();
		// 테이블을 만들어서 테이블이 없다면 테이블 생성과 모든 단어를 테이블에 추가
		boolean notExistTable = dao.createTable();
		if (notExistTable)
			dao.insertWord();
		// 문제로 나올 초성이 준비된 리스트 받아오기
		List<String> initialList = frame.getInitialList();
		// allWordMap에 리스트 형태로 각 초성에 대한 모든 단어 추가
		for (String s : initialList) {
			allWordMap.put(s, dao.selectWord(s));
		}
//		// 테스트용 초성의 단어 개수 확인
//		for (String s : initialList) {
//			System.out.print(s + " : " + allWordMap.get(s).size() + " / ");
//		}
//		System.out.println();
		this.frame = frame;
	} // 생성자 - END
	
	// 서버 활동
	public void startServer() {
		try {
			while (true) {
				// 클라이언트 접속
				Socket socket = svSocket.accept();
				// I/O Stream 생성
				ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
				// 유저 객체 생성
				User user = new User();
				user.setSocket(socket);
				user.setOutputStream(outputStream);
				user.setUserNumber(userNumber++);
				// 전체유저리스트와 로비를 공유하고, 현재 접속한 유저의 객체를 갖는 쓰레드 생성
				ServerThread svThread = new ServerThread(socket, user, inputStream, allUserSocket, lobby, allWordMap, frame);
				// 쓰레드 실행
				Thread thread = new Thread(svThread);
				thread.start();
			} // while - END
		} catch (Exception e) {
			
		} finally {
			System.out.println("서버 종료");
		}
	}

	public List<Socket> getAllUserSocket() {
		return allUserSocket;
	}
	public ServerSocket getSvSocket() {
		return svSocket;
	}
}