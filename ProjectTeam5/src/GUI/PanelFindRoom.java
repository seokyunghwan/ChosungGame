package GUI;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;

public class PanelFindRoom extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JButton btnConfirm;
	private JTextField tfRoomNumber;

	public PanelFindRoom(FrameSmallInput frame) {
		setSize(290, 180);
		setBorder(new TitledBorder(new LineBorder(Color.BLACK, 4)));
		setLayout(null);
		
		btnConfirm = new JButton("확인");
		btnConfirm.setBounds(52, 125, 67, 23);
		add(btnConfirm);
		
		JButton btnCancel = new JButton("취소");
		btnCancel.setBounds(171, 125, 67, 23);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				frame.dispose();
			}
		});
		add(btnCancel);
		
		JLabel lblRoomNumber = new JLabel("방 번호");
		lblRoomNumber.setFont(new Font("굴림", Font.BOLD, 14));
		lblRoomNumber.setHorizontalAlignment(SwingConstants.CENTER);
		lblRoomNumber.setBounds(69, 48, 57, 15);
		add(lblRoomNumber);
		
		tfRoomNumber = new JTextField();
		tfRoomNumber.setBounds(149, 46, 74, 21);
		add(tfRoomNumber);
		tfRoomNumber.setColumns(10);
	}

	public JButton getBtnConfirm() {
		return btnConfirm;
	}
	public JTextField getTfRoomNumber() {
		return tfRoomNumber;
	}
}
