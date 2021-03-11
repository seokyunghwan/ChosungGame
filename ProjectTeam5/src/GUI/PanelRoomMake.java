package GUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import GUIInterfaceSetting.LengthRestrictedDocument;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PanelRoomMake extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	private JPanel pnMainPanel;
	private JTextField tfRoomTitle;
	private JComboBox cbRoomLimit;
	private JButton btnConfirm;
	private JButton btnCancel;
	private JTextField tfPassword;
	private JRadioButton rdbtnPublic;
	private JRadioButton rdbtnPrivate;
	private JLabel lblPassword;
	
	private String[] preparedTitle = new String[] {"게임 한판!", "즐겜 유저분들 다 오세요", "아무나 들어오세요", "다 덤벼랏"};
	private final ButtonGroup buttonGroup = new ButtonGroup();

	public PanelRoomMake(FrameSmallInput frame) {
		setSize(290, 180);
		setLayout(null);
		setBorder(new TitledBorder(new LineBorder(Color.BLACK, 4)));
		
		JLabel lblRoomTitle = new JLabel("방 제목");
		lblRoomTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblRoomTitle.setBounds(24, 25, 57, 15);
		add(lblRoomTitle);
		
		JLabel lblRoomLimit = new JLabel("최대 인원");
		lblRoomLimit.setHorizontalAlignment(SwingConstants.CENTER);
		lblRoomLimit.setBounds(24, 87, 57, 15);
		add(lblRoomLimit);
		
		tfRoomTitle = new JTextField();
		tfRoomTitle.setDocument(new LengthRestrictedDocument(14));
		int randomInt = (int) (Math.random() * preparedTitle.length);
		tfRoomTitle.setText(preparedTitle[randomInt]);
		tfRoomTitle.setBounds(89, 22, 177, 21);
		add(tfRoomTitle);
		tfRoomTitle.setColumns(10);
		
		String[] cbValue = new String[] {"2", "3", "4", "5", "6"};
		cbRoomLimit = new JComboBox(cbValue);
		cbRoomLimit.setSelectedIndex(4);
		cbRoomLimit.setBounds(89, 84, 40, 21);
		add(cbRoomLimit);
		
		btnConfirm = new JButton("방 만들기");
		btnConfirm.setBounds(24, 123, 105, 35);
		add(btnConfirm);
		
		btnCancel = new JButton("취소");
		btnCancel.setBounds(161, 123, 105, 35);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				frame.dispose();
			}
		});
		add(btnCancel);
		
		lblPassword = new JLabel("비밀번호");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setBounds(133, 87, 57, 15);
		lblPassword.setEnabled(false);
		add(lblPassword);
		
		tfPassword = new JTextField();
		tfPassword.setColumns(10);
		tfPassword.setDocument(new LengthRestrictedDocument(6));
		tfPassword.setEnabled(false);
		tfPassword.setEditable(false);
		tfPassword.setBounds(194, 84, 72, 21);
		add(tfPassword);
		
		JLabel lblPasswordSet = new JLabel("공개 설정");
		lblPasswordSet.setHorizontalAlignment(SwingConstants.CENTER);
		lblPasswordSet.setBounds(24, 56, 57, 15);
		add(lblPasswordSet);
		
		rdbtnPublic = new JRadioButton();
		rdbtnPublic.setText("공개");
		rdbtnPublic.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnPublic.setSelected(true);
		rdbtnPublic.setBounds(95, 52, 57, 23);
		rdbtnPublic.setOpaque(false);
		rdbtnPublic.addActionListener(this);
		buttonGroup.add(rdbtnPublic);
		add(rdbtnPublic);
		
		rdbtnPrivate = new JRadioButton();
		rdbtnPrivate.setText("비공개");
		rdbtnPrivate.setHorizontalAlignment(SwingConstants.CENTER);
		rdbtnPrivate.setBounds(161, 52, 65, 23);
		rdbtnPrivate.setOpaque(false);
		rdbtnPrivate.addActionListener(this);
		buttonGroup.add(rdbtnPrivate);
		add(rdbtnPrivate);
	}
	
	public JPanel getPnMainPanel() {
		return pnMainPanel;
	}
	public JTextField getTfRoomTitle() {
		return tfRoomTitle;
	}
	public JComboBox getCbRoomLimit() {
		return cbRoomLimit;
	}
	public JButton getBtnConfirm() {
		return btnConfirm;
	}
	public String[] getPreparedTitle() {
		return preparedTitle;
	}
	public JTextField getTfPassword() {
		return tfPassword;
	}
	public JRadioButton getRdbtnPublic() {
		return rdbtnPublic;
	}
	public JRadioButton getRdbtnPrivate() {
		return rdbtnPrivate;
	}
	public JLabel getLblPassword() {
		return lblPassword;
	}
	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == rdbtnPublic) {
			lblPassword.setEnabled(false);
			tfPassword.setEnabled(false);
			tfPassword.setEditable(false);
		}
		if (e.getSource() == rdbtnPrivate) {
			lblPassword.setEnabled(true);
			tfPassword.setEnabled(true);
			tfPassword.setEditable(true);
		}
	}
}