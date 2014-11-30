package SpaceGame;

import java.io.*;

import sun.audio.*;

public class MusicPlayer{
	private AudioStream music;
	
	public void play() {
		
		// Music is Fools Rhythm by Two Fingers first heard here:
		// https://vimeo.com/35244188
		String file = "assets/music.wav";
		
		try {
			InputStream fileIn = new FileInputStream(file);
			music = new AudioStream(fileIn);
			AudioPlayer.player.start(music);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop(){
		AudioPlayer.player.stop(music);
	}
}
