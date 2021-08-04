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

	protected String getAudio() {
		return audio;
	}

	protected void setAudio(String audio) {
		this.audio = audio;
	}

	protected String getDayTime() {
		return dayTime;
	}

	protected void setDayTime(String dayTime) {
		this.dayTime = dayTime;
	}

	protected String getTrail() {
		return trail;
	}

	protected void setTrail(String trail) {
		this.trail = trail;
	}

	public Map<String, String> toMap(){
		Map<String, String> sol = new TreeMap<>();
		
		sol.put("resolution", resolution);
		sol.put("audio",audio);
		sol.put("dayTime",audio);
		sol.put("trail",audio);
		
		return sol;
	}
	
	@Override
	public String toString() {
		return "Settings [resolution= "+resolution+", audio=" + audio + ", dayTime=" + dayTime
				+ ", trail=" + trail + "]";
	}
	
	
}
