package GUI;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import GUIInterfaceSetting.GhostText;
import GUIInterfaceSetting.LengthRestrictedDocument;
import GUIInterfaceSetting.RequestFocusListener;
import java.awt.Font;

@SuppressWarnings("serial")
public class PanelConnect extends JPanel {
	
	private JTextField tfUserName;
	private JButton btnEnter;

	public PanelConnect(FrameMain frame) {
		setSize(800, 600);
		setLayout(null);
		
		JLabel lblTitle = new JLabel("ㅊㅅㅁㅊㄱ");
		lblTitle.setFont(new Font("굴림", Font.PLAIN, 50));
		
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(12, 50, 776, 180);
		add(lblTitle);
		
		btnEnter = new JButton("입장");
		btnEnter.setBounds(262, 420, 276, 74);
		// 첫 포커스를 버튼으로 세팅
		btnEnter.addAncestorListener(new RequestFocusListener(false));
		add(btnEnter);
		
		tfUserName = new JTextField();
		tfUserName.setHorizontalAlignment(SwingConstants.CENTER);
		tfUserName.setBounds(212, 300, 376, 85);
		// 글자 제한
		tfUserName.setDocument(new LengthRestrictedDocument(12));
		tfUserName.setCaretPosition(tfUserName.getText().length());
		// 텍스트 필드를 포커싱하면 회색 글자 제거
		new GhostText(tfUserName, "닉네임을 입력하세요");
		add(tfUserName);
	}

	public JTextField getTfUserName() {
		return tfUserName;
	}
	public JButton getBtnEnter() {
		return btnEnter;
	}
}