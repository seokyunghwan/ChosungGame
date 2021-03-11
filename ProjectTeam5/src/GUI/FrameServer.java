package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import JDBC.WordDAO;
import Server.ServerMain;
import VO.Initial;

@SuppressWarnings("serial")
public class FrameServer extends JFrame implements ActionListener {
	
	// common
	private static FrameServer frame;
	private ServerSocket svSocket;
	private FrameServerThread thread;
	private ServerMain svMain;
	private List<Socket> allUserSocket;
	private DefaultTableModel model;
	private File file = new File("src/VO/INITIAL_TEXT.txt");
	private BufferedReader br;
	private PrintWriter pw;
	private DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
	private List<String> initialList;
	private WordDAO dao;
	
	// JDBC
	private Connection conn;
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String uid = "hr";
	private String upw = "hr";
	
	// pnServer
	private JPanel pnServer;
	private JLabel lblServerStatus;
	private JButton btnServerStart;
	private JButton btnServerClose;
	private JButton btnExitProgram;
	
	// pnUser
	private JPanel pnUser;
	private JScrollPane spUserTable;
	private JTable tbConnectingUser;
	
	//pnWord
	private JPanel pnWord;
	private JScrollPane spInitialTable;
	private JTable tbInitialTable;
	private JButton btnSearchInitial;
	private JButton btnSubtractInitial;
	private JButton btnApply;
	private JLabel lblInitialList;
	private JList<String> listInitial;
	private JButton btnAddInitial;
	private List<String> iniList;
	private String initial;
	private JLabel lblUserNum;
	
	// Constructor
	public FrameServer() {
		setSize(450, 350);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		// JDBC Connection 객체 생성
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, uid, upw); // DB 연결
		} catch (Exception e) {
			System.out.println("Connection 에러");
			e.printStackTrace();
		}
		// Data Access Object 생성
		try {
			dao = new WordDAO(conn);
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		// 1번탭
		pnServer = new JPanel();
		tabbedPane.setFont(new Font("나눔스퀘어라운드 Bold", Font.PLAIN, 15));
		tabbedPane.addTab("　서버　", null, pnServer, null);
		pnServer.setLayout(null);
		
		lblServerStatus = new JLabel("서버 OFF");
		lblServerStatus.setForeground(Color.RED);
		lblServerStatus.setFont(new Font("나눔스퀘어라운드 Bold", Font.PLAIN, 25));
		lblServerStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblServerStatus.setBounds(12, 10, 417, 114);
		pnServer.add(lblServerStatus);
		
		btnServerStart = new JButton("서버 시작");
		btnServerStart.setFont(new Font("나눔스퀘어라운드 Bold", Font.PLAIN, 15));
		btnServerStart.setBounds(12, 134, 192, 64);
		btnServerStart.addActionListener(this);
		pnServer.add(btnServerStart);
		
		btnServerClose = new JButton("서버 종료");
		btnServerClose.setFont(new Font("나눔스퀘어라운드 Bold", Font.PLAIN, 15));
		btnServerClose.setBounds(237, 134, 192, 64);
		btnServerClose.addActionListener(this);
		pnServer.add(btnServerClose);
		
		btnExitProgram = new JButton("프로그램 종료");
		btnExitProgram.setFont(new Font("나눔스퀘어라운드 Bold", Font.PLAIN, 15));
		btnExitProgram.setBounds(12, 208, 417, 74);
		btnExitProgram.addActionListener(this);
		pnServer.add(btnExitProgram);
		
		// 2번탭
		pnUser = new JPanel();
		tabbedPane.addTab("　접속자　", null, pnUser, null);
		pnUser.setLayout(new BorderLayout(0, 0));
		
		spUserTable = new JScrollPane();
		pnUser.add(spUserTable, BorderLayout.CENTER);
		
		tbConnectingUser = new JTable();
		spUserTable.setViewportView(tbConnectingUser);
		
		// 3번탭
		pnWord = new JPanel();
		tabbedPane.addTab("　단어　", null, pnWord, null);
		pnWord.setLayout(null);
		
		spInitialTable = new JScrollPane();
		spInitialTable.setBounds(12, 10, 140, 272);
		pnWord.add(spInitialTable);
		
		tbInitialTable = new JTable();
		tbInitialTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tbInitialTable.setFillsViewportHeight(true);
		cellRenderer.setHorizontalAlignment( JLabel.CENTER );
		initialList = new ArrayList<>();
		model = new DefaultTableModel(null, new String[] {"초성", "단어 수"}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		try {
			br = new BufferedReader(new FileReader(file));
			String word;
			while ((word = br.readLine()) != null) {
				if (!word.isEmpty()) {
					String[] s = word.split(":");
					initialList.add(s[0]);
					model.addRow(s);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		tbInitialTable.setModel(model);
		tbInitialTable.getColumnModel().getColumn(0).setCellRenderer( cellRenderer );
		tbInitialTable.getColumnModel().getColumn(1).setCellRenderer( cellRenderer );
		spInitialTable.setViewportView(tbInitialTable);
		
		JScrollPane spInitialList = new JScrollPane();
		spInitialList.setBounds(289, 10, 140, 272);
		pnWord.add(spInitialList);
		
		lblInitialList = new JLabel("");
		spInitialList.setColumnHeaderView(lblInitialList);
		
		listInitial = new JList<>();
		spInitialList.setViewportView(listInitial);
		
		btnSearchInitial = new JButton("<html><center>새로운 초성<br>검색</center></html>");
		btnSearchInitial.setFont(new Font("나눔스퀘어라운드 Bold", Font.PLAIN, 15));
		btnSearchInitial.addActionListener(this);
		btnSearchInitial.setBounds(164, 10, 113, 60);
		
		btnAddInitial = new JButton("<html><center>검색한 초성<br>추가</center></html>");
		btnAddInitial.setFont(new Font("나눔스퀘어라운드 Bold", Font.PLAIN, 15));
		btnAddInitial.addActionListener(this);
		btnAddInitial.setBounds(164, 80, 113, 60);
		
		btnSubtractInitial = new JButton("<html><center>선택한 초성<br>제거</center></html>");
		btnSubtractInitial.setFont(new Font("나눔스퀘어라운드 Bold", Font.PLAIN, 15));
		btnSubtractInitial.addActionListener(this);
		btnSubtractInitial.setBounds(164, 150, 113, 60);
		
		btnApply = new JButton("<html><center>수정사항<br>적용하기</center></html>");
		btnApply.setFont(new Font("나눔스퀘어라운드 Bold", Font.PLAIN, 15));
		btnApply.addActionListener(this);
		btnApply.setBounds(164, 220, 113, 60);
		
		pnWord.add(btnSearchInitial);
		pnWord.add(btnAddInitial);
		pnWord.add(btnSubtractInitial);
		pnWord.add(btnApply);
	} // Constructor - END
	
	// Getter / Setter
	public void setAllUserSocket(List<Socket> allUserSocket) {
		this.allUserSocket = allUserSocket;
	}
	public static FrameServer getFrame() {
		return frame;
	}
	public List<String> getInitialList() {
		return initialList;
	}
	public WordDAO getDao() {
		return dao;
	}
	public Connection getConn() {
		return conn;
	} // Getter / Setter - END

	public void recieveRefresh() {
		List<String> ipValue=new ArrayList<String>();
		List<String> portValue=new ArrayList<String>();

		for (Socket s : allUserSocket) {
			ipValue.add(s.getInetAddress().getHostAddress());
			portValue.add(Integer.toString(s.getPort()));
		}

		String[][] rowData3= new String[ipValue.size()][2];

		for(int k=0;k<ipValue.size();k++) {
			rowData3[k][0]=ipValue.get(k);
			rowData3[k][1]=portValue.get(k);   
		}
		String[] coldata2=new String[] {"ip번호","포트번호"};
		model = new DefaultTableModel(rowData3,coldata2) ;
		tbConnectingUser=new JTable(model);
		spUserTable.setViewportView(tbConnectingUser);

		lblUserNum = new JLabel("접속자 수 : " + allUserSocket.size());
		spUserTable.setColumnHeaderView(lblUserNum);
		lblUserNum.setForeground(Color.BLACK);
		lblUserNum.setFont(new Font("굴림", Font.BOLD, 12));
		lblUserNum.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserNum.setBounds(12, 10, 417, 114);

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		/* 서버 탭 */
		// 
		if (e.getSource() == btnServerStart) {
			thread = new FrameServerThread();
			thread.start();
		}
		
		//
		if (e.getSource() == btnServerClose) {
			try {
				thread.interrupt();
				for (Socket s : frame.allUserSocket) {
					s.close();
				}
				svSocket.close();
			} catch (Exception er) {
				er.printStackTrace();
			}
			lblServerStatus.setText("서버 종료");
			lblServerStatus.setForeground(Color.RED);
		}
		
		//
		if (e.getSource() == btnExitProgram) {
			try {
				if (thread != null)
					thread.interrupt();
				if (allUserSocket != null) {
					for (Socket s : frame.allUserSocket) {
						s.close();
					}
					svSocket.close();
				}
			} catch (Exception er) {
				er.printStackTrace();
			}
			frame.dispose();
			System.exit(0);
		}
		
		/* 유저 탭 */
		
		/* 단어 탭 */
		// 초성 검색하기
		if (e.getSource() == btnSearchInitial) {
			initial = JOptionPane.showInputDialog("초성 2개를 입력해주세요");
			// 입력한 단어가 2개인지 확인
			if (initial.length() != 2)
				JOptionPane.showMessageDialog(this, "초성은 2개를 입력해야합니다.");
			// 입력한 단어가 2개라면
			else {
				Initial ini = new Initial();
				// 입력한 2개의 초성이 쌍자음이 없는 2개의 자음이 맞다면
				if (ini.getKoInitial().contains(initial.charAt(0)) && ini.getKoInitial().contains(initial.charAt(1))) {
					iniList = dao.selectWord(initial);
					lblInitialList.setText(initial + "의 검색 결과 : " + iniList.size());
					DefaultListModel<String> listModel = new DefaultListModel<>();
					for (String s : iniList) {
						listModel.addElement(s);						
					}
					listInitial.setModel(listModel);
					listInitial.updateUI();
				} else
					JOptionPane.showMessageDialog(this, "쌍자음을 뺀 자음만 가능합니다.");
			}
		}
		
		// 테이블에 검색한 초성 추가하기
		if (e.getSource() == btnAddInitial) {
			if (initial == null) {
				JOptionPane.showMessageDialog(this, "추가할 초성을 검색한 다음 추가해주세요.");
			} else if (iniList == null) {
				//
			} else {
				model.addRow(new String[] {initial, String.valueOf(iniList.size())});
				tbInitialTable.updateUI();
			}
		}
		
		// 테이블에서 초성 제거하기
		if (e.getSource() == btnSubtractInitial) {
			// 테이블의 값을 선택한 경우
			if (tbInitialTable.getSelectedRow() != -1) {
				((DefaultTableModel)tbInitialTable.getModel()).removeRow(tbInitialTable.getSelectedRow());
				tbInitialTable.updateUI();
			} // if - END
			else
				JOptionPane.showMessageDialog(this, "제거할 초성을 선택해주세요");
		}
		
		// 
		if (e.getSource() == btnApply) {
			try {
				pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
				int row = tbInitialTable.getRowCount();
				int column = tbInitialTable.getColumnCount();
				String s = "";
				for (int i = 0; i < row; i++) {
					StringJoiner sj = new StringJoiner(":");
					for (int j = 0; j < column; j++) {
						sj.add((String) tbInitialTable.getValueAt(i, j));
					}
					s += sj.toString() + "\n";
				}
				pw.write(s);
				pw.close();
				System.out.println(s);
			} catch (Exception er) {
				er.printStackTrace();
			}
		}
	} // ActionPerformed - END
	
	class FrameServerThread extends Thread {
		@Override
		public void run() {
			lblServerStatus.setText("서버 준비중...");
			lblServerStatus.setForeground(Color.YELLOW);
			try {
				svMain = new ServerMain(frame);
				frame.svSocket = svMain.getSvSocket();
				frame.setAllUserSocket(svMain.getAllUserSocket());
				if (frame.allUserSocket != null) {
					lblServerStatus.setText("서버 ON");
					lblServerStatus.setForeground(Color.GREEN);
				}
			} catch (IOException er) {
				er.printStackTrace();
			} finally {
				svMain.startServer();
			} // try-catch - END
		} // run - END
	} // FrameServerThread - END
	
	public static void main(String[] args) {

		frame = new FrameServer();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
