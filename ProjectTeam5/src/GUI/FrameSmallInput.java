package GUI;

import java.awt.Component;

import javax.swing.JFrame;


public class FrameSmallInput extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private PanelPasswordInput pnPasswordInput;
	private PanelRoomMake pnRoomMake;
	private PanelChangeName pnChangeName;
	private PanelConfirm pnConfirm;
	private PanelFindRoom pnFindRoom;
	
	public FrameSmallInput() {
		
		setSize(290, 180);
		setLocationRelativeTo(null);
		setResizable(false);
		setUndecorated(true);
		
		this.pnPasswordInput = new PanelPasswordInput(this);
		this.pnRoomMake = new PanelRoomMake(this);
		this.pnChangeName = new PanelChangeName(this);
		this.pnConfirm = new PanelConfirm(this);
		this.pnFindRoom = new PanelFindRoom(this);
	}
	// 패널 전환 METHOD
	public void changePanel(Component c) {
		getContentPane().removeAll();
		getContentPane().add(c);
		revalidate();
		repaint();
	} // METHOD - END
	
	public PanelPasswordInput getPnPasswordInput() {
		return pnPasswordInput;
	}
	public PanelRoomMake getPnRoomMake() {
		return pnRoomMake;
	}
	public PanelChangeName getPnChangeName() {
		return pnChangeName;
	}
	public PanelConfirm getPnConfirm() {
		return pnConfirm;
	}
	public PanelFindRoom getPnFindRoom() {
		return pnFindRoom;
	}
}
