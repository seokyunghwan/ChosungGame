package Client;

import GUI.FrameMain;

public class ClientMain {

	// 생성자
	public ClientMain() {
		// 메인 프레임 생성 후 setVisible
		FrameMain frame = new FrameMain();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		
		new ClientMain();
	}
}