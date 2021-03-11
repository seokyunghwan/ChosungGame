package GUI;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

public class PanelConfirm extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JLabel lblMessage;
	private JButton btnConfirm;
	
	public PanelConfirm(FrameSmallInput frame) {
		setSize(290, 180);
		setBorder(new TitledBorder(new LineBorder(Color.BLACK, 4)));
		setLayout(null);
		
		lblMessage = new JLabel("");
		lblMessage.setFont(new Font("굴림", Font.BOLD, 14));
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblMessage.setBounds(12, 25, 266, 70);
		add(lblMessage);
		
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
	}

	public JButton getBtnConfirm() {
		return btnConfirm;
	}
	public JLabel getLblMessage() {
		return lblMessage;
	}
}
