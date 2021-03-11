package GUI;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import GUIInterfaceSetting.LengthRestrictedDocument;

public class PanelChangeName extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JButton btnConfirm;
	private JTextField tfUserName;

	public PanelChangeName(FrameSmallInput frame) {
		setSize(290, 180);
		setBorder(new TitledBorder(new LineBorder(Color.BLACK, 4)));
		setLayout(null);
		
		tfUserName = new JTextField();
		tfUserName.setDocument(new LengthRestrictedDocument(12));
		tfUserName.setHorizontalAlignment(SwingConstants.CENTER);
		tfUserName.setBounds(52, 75, 186, 30);
		add(tfUserName);
		tfUserName.setColumns(10);
		
		btnConfirm = new JButton("확인");
		btnConfirm.setBounds(52, 125, 67, 23);
		add(btnConfirm);
		
		JButton btnCancel = new JButton("취소");
		btnCancel.setBounds(171, 125, 67, 23);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				tfUserName.setText("");
				frame.dispose();
			}
		});
		add(btnCancel);
		
		JLabel lblExplain1 = new JLabel("변경하고 싶은");
		lblExplain1.setHorizontalAlignment(SwingConstants.CENTER);
		lblExplain1.setBounds(12, 10, 266, 23);
		add(lblExplain1);
		
		JLabel lblExplain2 = new JLabel("닉네임을 적으세요");
		lblExplain2.setHorizontalAlignment(SwingConstants.CENTER);
		lblExplain2.setBounds(12, 32, 266, 23);
		add(lblExplain2);
	}

	public JButton getBtnConfirm() {
		return btnConfirm;
	}
	public JTextField getTfUserName() {
		return tfUserName;
	}
}
