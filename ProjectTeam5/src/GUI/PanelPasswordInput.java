package GUI;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import GUIInterfaceSetting.LengthRestrictedDocument;

public class PanelPasswordInput extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JPasswordField pfPassword;
	private JButton btnConfirm;
	
	private String correctPw;
	private int roomNumber;

	public PanelPasswordInput(FrameSmallInput frame) {
		setSize(290, 180);
		setLayout(null);
		setBorder(new TitledBorder(new LineBorder(Color.BLACK, 4)));
		
		JLabel lblExplain1 = new JLabel("비공개 방입니다.");
		lblExplain1.setHorizontalAlignment(SwingConstants.CENTER);
		lblExplain1.setBounds(12, 25, 266, 15);
		add(lblExplain1);
		
		JLabel lblExplain2 = new JLabel("비밀번호를 입력해주세요");
		lblExplain2.setHorizontalAlignment(SwingConstants.CENTER);
		lblExplain2.setBounds(12, 50, 266, 15);
		add(lblExplain2);
		
		pfPassword = new JPasswordField();
		pfPassword.setDocument(new LengthRestrictedDocument(6));
		pfPassword.setHorizontalAlignment(SwingConstants.CENTER);
		pfPassword.setBounds(86, 85, 126, 21);
		add(pfPassword);
		
		btnConfirm = new JButton("확인");
		btnConfirm.setBounds(48, 125, 67, 23);
		add(btnConfirm);
		
		JButton btnCancel = new JButton("취소");
		btnCancel.setBounds(169, 125, 67, 23);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				frame.dispose();
			}
		});
		add(btnCancel);
	}
	
	public JPasswordField getPfPassword() {
		return pfPassword;
	}
	public JButton getBtnConfirm() {
		return btnConfirm;
	}
	public String getCorrectPw() {
		return correctPw;
	}
	public void setCorrectPw(String correctPw) {
		this.correctPw = correctPw;
	}
	public int getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
}
