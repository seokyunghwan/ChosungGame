package Client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import GUI.FrameMain;

public class ClientThread implements Runnable {
	
	private FrameMain frame;
	private int methodNumber = 0;
	private Timer timer;
	
	public ClientThread(FrameMain frame) {
		this.frame = frame;
	}
	
	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				if (methodNumber == 1) {
					Thread.sleep(1);
					methodNumber = 0;
					timer = new Timer(1000, new ActionListener() {
						int i = 3;
						@Override
						public void actionPerformed(ActionEvent e) {
							if (i > 0)
								frame.getPnRoom().getLabelQuiz().setText(String.valueOf(i--));
							else {
								frame.getPnRoom().getLabelQuiz().setText(frame.getInitial());
								timer.stop();			
								frame.countdownEnd();
							}
						}
					});
					timer.setRepeats(true);
					timer.setCoalesce(true);
					timer.setInitialDelay(0);
					timer.start();
				}
			}
		} catch (InterruptedException e) {
			System.out.println("isInterrupted");
		} finally {
			System.out.println("FINALLY");
		}
	}

	public int getMethodNumber() {
		return methodNumber;
	}
	public void setMethodNumber(int methodNumber) {
		this.methodNumber = methodNumber;
	}
}
