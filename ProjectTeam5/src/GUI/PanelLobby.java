package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import GUIInterfaceSetting.ButtonColumn;
import GUIInterfaceSetting.TableDataSetting;

@SuppressWarnings({"serial", "rawtypes"})
public class PanelLobby extends JPanel {

//	private JList jlistRoom;
	Action enterRoomAction;
	
	private JTable jtableRoom;
	private TableDataSetting dataSetter = new TableDataSetting();
	DefaultTableCellRenderer renderer;
	private DefaultTableModel model;
	
	private JList jlistLobbyUser;
	private JTextArea textAreaChat;
	private JTextField textFieldChat;
	private JScrollPane scrollPaneRoom;
	private JScrollPane scrollPaneChat;
	
	private JButton btnMakeRoom;
	private JButton btnFindRoom;
	private JButton btnChangeUserName;
	private JButton btnExitLobby;
	private JLabel lblRoom;

	public PanelLobby(FrameMain frame) {
		setSize(800, 600);
		setLayout(null);
		
		JScrollPane scrollPaneUserList = new JScrollPane();
		scrollPaneUserList.setBounds(593, 222, 195, 368);
		add(scrollPaneUserList);
		
		jlistLobbyUser = new JList();
		jlistLobbyUser.setDragEnabled(false);
		scrollPaneUserList.setViewportView(jlistLobbyUser);
		
		JLabel lblUserList = new JLabel("대기실 유저");
		lblUserList.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPaneUserList.setColumnHeaderView(lblUserList);
		
		scrollPaneChat = new JScrollPane();
		scrollPaneChat.setBounds(12, 366, 569, 181);
		add(scrollPaneChat);
		
		textAreaChat = new JTextArea();
		textAreaChat.setEditable(false);
		scrollPaneChat.setViewportView(textAreaChat);
		
		textFieldChat = new JTextField();
		textFieldChat.setBounds(12, 557, 569, 33);
		add(textFieldChat);
		textFieldChat.setColumns(10);
		
		jtableRoom = new JTable();
		jtableRoom.setRowHeight(100);
		jtableRoom.setTableHeader(null);
		model = new DefaultTableModel(dataSetter.getEmptyData(), dataSetter.getColumnNames());
		jtableRoom.setModel(model);
		
		try {
			enterRoomAction = new AbstractAction()
			{
				public void actionPerformed(ActionEvent e)
				{
					int modelRow = Integer.valueOf( e.getActionCommand() ); // 누른 버튼의 테이블에서의 인덱스
					String strArr[] = ((String) jtableRoom.getValueAt(modelRow, 0)).split("<br>");
					frame.enterExistRoom(Integer.parseInt(strArr[1].substring(0,3)));
				}
			};
		} catch (Exception e) {
			System.out.println("ER - 06");
		}
		
		ButtonColumn buttonColumn = new ButtonColumn(jtableRoom, enterRoomAction, 2);
		buttonColumn.setMnemonic(KeyEvent.VK_D);

		scrollPaneRoom = new JScrollPane(jtableRoom);
		scrollPaneRoom.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPaneRoom.setBackground(Color.WHITE);
		scrollPaneRoom.setBounds(12, 10, 569, 346);
		add(scrollPaneRoom);
		
		btnMakeRoom = new JButton("방 만들기");
		btnMakeRoom.setBounds(593, 10, 195, 43);
		add(btnMakeRoom);
		
		btnFindRoom = new JButton("방 찾기");
		btnFindRoom.setBounds(593, 63, 195, 43);
		add(btnFindRoom);
		
		btnChangeUserName = new JButton("닉네임 변경");
		btnChangeUserName.setBounds(593, 116, 195, 43);
		add(btnChangeUserName);
		
		btnExitLobby = new JButton("나가기");
		btnExitLobby.setBounds(593, 169, 195, 43);
		add(btnExitLobby);
	}

	public JTable getJtableRoom() {
		return jtableRoom;
	}
	public JList getJlistLobbyUser() {
		return jlistLobbyUser;
	}
	public JTextArea getTextAreaChat() {
		return textAreaChat;
	}
	public JTextField getTextFieldChat() {
		return textFieldChat;
	}
	public JScrollPane getScrollPaneRoom() {
		return scrollPaneRoom;
	}
	public JButton getBtnMakeRoom() {
		return btnMakeRoom;
	}
	public JButton getBtnFindRoom() {
		return btnFindRoom;
	}
	public JButton getBtnExitLobby() {
		return btnExitLobby;
	}
	public JLabel getLblRoom() {
		return lblRoom;
	}
	public JScrollPane getScrollPaneChat() {
		return scrollPaneChat;
	}
	public JButton getBtnChangeUserName() {
		return btnChangeUserName;
	}
	public DefaultTableModel getModel() {
		return model;
	}
	public void tableSet () {
		try {
			jtableRoom.getColumnModel().getColumn(0).setPreferredWidth(60);
			jtableRoom.getColumnModel().getColumn(1).setPreferredWidth(380);
			jtableRoom.getColumnModel().getColumn(2).setPreferredWidth(111);
			jtableRoom.setFont(new Font("굴림", Font.BOLD, 15));
			renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(JLabel.CENTER);
			jtableRoom.getColumnModel().getColumn(0).setCellRenderer(renderer);
			
			ButtonColumn buttonColumn = new ButtonColumn(jtableRoom, enterRoomAction, 2);
			buttonColumn.setMnemonic(KeyEvent.VK_D);
		} catch (Exception e) {
			System.out.println("ER 05");
		}
	}
}
