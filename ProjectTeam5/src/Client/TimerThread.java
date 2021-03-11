package Client;

import GUI.FrameMain;

public class TimerThread extends Thread {
	
	private FrameMain frame;
	private int time = 100;
	
	public TimerThread(FrameMain frame) {
		this.frame = frame;
	}

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				Thread.sleep(50);
				frame.getPnRoom().getProgressBar().setValue(time);
				time--;
				if (time == -1) {
					frame.timeOver();
				}
			}
		} catch (InterruptedException e) {
		}
	}

	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
}
