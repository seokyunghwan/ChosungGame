package GUI;

import java.awt.Color;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

public class PanelRoom extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JLabel labelQuiz; // 문제 제출
	private JProgressBar progressBar; // 게임 시간 bar
	private JTextArea textAreaChat; // 채팅 textArea
	private JScrollPane jscrollPane; // 채팅 scroll
	private JTextField textFieldChat; // 채팅 입력
	private JList<String> roomUserList; // 접속자
	private JTextField textFieldGame; // 답 textField
	private JButton btnInputEnter; // 채팅 엔터
	private	JButton btnGameStart; // 게임 시작
	private JList<String> listUsedWord;		//사용된 단어들
	private JButton btnExit;
	private DefaultListModel<String> model;
	private JLabel lblGameInfo;

	public PanelRoom(FrameMain frame) {
		setSize(800, 600);
		setLayout(null);
		
		UIManager.put("Label.disabledForeground", Color.BLACK);
		
		// 채팅 textArea
		textAreaChat = new JTextArea();
		textAreaChat.setEditable(false);
		textAreaChat.setFont(new Font("굴림", Font.BOLD, 15));

		// 채팅 scroll
		jscrollPane = new JScrollPane(textAreaChat);
		jscrollPane.setBounds(10, 390, 636, 150);
		jscrollPane.setBorder(new LineBorder(Color.BLACK));
		add(jscrollPane);

		// 채팅 textField
		textFieldChat = new JTextField();
		textFieldChat.setBounds(10, 550, 541, 40);
		textFieldChat.setFont(new Font("굴림", Font.BOLD, 15));
		textFieldChat.setBorder(new LineBorder(Color.black));
		add(textFieldChat);

		// 방 접속자
		roomUserList = new JList<>();
		roomUserList.setBorder(new LineBorder(Color.BLACK));
		roomUserList.setBounds(658, 390, 130, 201);
		add(roomUserList);

		// 답 textfield
		textFieldGame = new JTextField();
		textFieldGame.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldGame.setEditable(false);
		textFieldGame.setBackground(Color.CYAN);
		textFieldGame.setFont(new Font("굴림", Font.BOLD, 15));
		textFieldGame.setBorder(new LineBorder(Color.BLUE, 2, true));
		textFieldGame.setBounds(100, 212, 340, 40);
		add(textFieldGame);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setForeground(Color.RED);
		progressBar.setFont(new Font("굴림", Font.PLAIN, 15));
		progressBar.setBounds(100, 176, 340, 32);
		add(progressBar);

		// 채팅 엔터
		btnInputEnter = new JButton("ENTER");
		btnInputEnter.setBounds(563, 550, 83, 40);
		add(btnInputEnter);

		// 게임 시작 버튼
		btnGameStart = new JButton("");
		btnGameStart.setBounds(525, 86, 124, 45);
		add(btnGameStart);


		labelQuiz = new JLabel("");
		labelQuiz.setHorizontalAlignment(SwingConstants.CENTER);
		labelQuiz.setFont(new Font("굴림", Font.PLAIN, 26));
		labelQuiz.setBounds(178, 73, 181, 58);
		add(labelQuiz);
		
		listUsedWord = new JList<>();
		listUsedWord.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listUsedWord.setBorder(new LineBorder(Color.BLACK));
		listUsedWord.setBounds(525, 160, 67, 202);
		model = new DefaultListModel<>();
		listUsedWord.setModel(model);
		add(listUsedWord);
		listUsedWord.setEnabled(false);
		
		btnExit = new JButton("EXIT");
		btnExit.setBounds(658, 86, 124, 45);
		add(btnExit);
		
		lblGameInfo = new JLabel("");
		lblGameInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameInfo.setBounds(100, 264, 340, 40);
		add(lblGameInfo);
	}
	
	public JLabel getLabelQuiz() {
		return labelQuiz;
	}
	public JList<String> getRoomUserList() {
		return roomUserList;
	}
	public JTextArea getTextAreaChat() {
		return textAreaChat;
	}
	public JTextField getTextFieldChat() {
		return textFieldChat;
	}
	public JTextField getTextFieldGame() {
		return textFieldGame;
	}
	public JButton getBtnInputEnter() {
		return btnInputEnter;
	}
	public JButton getBtnGameStart() {
		return btnGameStart;
	}
	public JProgressBar getProgressBar() {
		return progressBar;
	}
	public JList<String> getListUsedWord() {
		return listUsedWord;
	}
	public JButton getBtnExit() {
		return btnExit;
	}
	public DefaultListModel<String> getModel() {
		return model;
	}
	public JLabel getLblGameInfo() {
		return lblGameInfo;
	}
}
