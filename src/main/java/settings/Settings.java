package settings;

import java.util.Map;
import java.util.TreeMap;

public class Settings {
	String resolution, audio, dayTime, trail;
	
	protected Settings(String resolution, String audio, String dayTime, String trail){
		this.resolution = resolution;// pixels
		this.audio = audio;			 // boolean
		this.dayTime = dayTime; 	 // boolean
		this.trail = trail;			 // boolean
	}

	public String getResolution() {
		return resolution;
	}
	
	public void setResolution() {
		this.resolution = resolution;
	}
	
	public boolean getAudio() {
		return "1".equals(audio) ? true : false;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	public boolean getDayTime() {
		return "1".equals(dayTime) ? true : false;
	}

	public void setDayTime(String dayTime) {
		this.dayTime = dayTime;
	}

	public boolean getTrail() {
		return "1".equals(trail) ? true : false;
	}

	public void setTrail(String trail) {
		this.trail = trail;
	}

	public Map<String, String> toMap(){
		Map<String, String> sol = new TreeMap<>();
		
		sol.put("resolution", resolution);
		sol.put("audio",audio);
		sol.put("dayTime",dayTime);
		sol.put("trail",trail);
		
		return sol;
	}
	
	@Override
	public String toString() {
		return "Settings [resolution= "+resolution+", audio=" + audio + ", dayTime=" + dayTime
				+ ", trail=" + trail + "]";
	}
	
	
}
