package SpaceGame;

import java.io.*;

import sun.audio.*;

public class MusicPlayer extends Thread {

	@Override
	public void run() {
		String file = "assets/music.wav";
		try {
			InputStream fileIn = new FileInputStream(file);
			AudioStream music = new AudioStream(fileIn);
			AudioPlayer.player.start(music);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
