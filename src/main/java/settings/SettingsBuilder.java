package settings;

import java.io.FileReader;
import java.io.FileWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SettingsBuilder {
	
	static JSONObject json = null;
	
	SettingsBuilder(){
		
		JSONParser jsonParser = new JSONParser();
		
		try (FileReader reader = new FileReader("settings.json")){
			json = (JSONObject) jsonParser.parse(reader);
		} catch (Exception e) {
			System.err.println("Settings file not found!");
		    e.printStackTrace();
	    }
	
	}
	
	private static void writeToFile() {
		try(FileWriter fileWriter = new FileWriter("settings.json")){
			
			fileWriter.write(json.toJSONString());
			fileWriter.flush();
			
		}catch(Exception e) {
			System.err.println("Settings file not found!");			
			e.printStackTrace();
		}
	}
	
	public static void removeSettings(String user) {
		
		if(json == null || "template".equals(user) || json.get(user) == null) return;
		json.remove(user);
		writeToFile();
		
	}
	
	public static Settings getSettings(String user) {
		
		if(json == null || "template".equals(user)) return null;
		JSONObject usersSetting;
		usersSetting = (JSONObject) json.get(user);
		
		if(usersSetting == null) {
			System.out.println("User not found!");
			usersSetting = (JSONObject) json.get("default");
		}
		
		return new Settings(
				(String)usersSetting.get("resolution"),
				(String)usersSetting.get("audio"),
				(String)usersSetting.get("dayTime"),
				(String)usersSetting.get("trail")
				);
		
	}
	
	public static void addSettings(Settings settings, String user) {
		
		if(user.length() < 3)System.out.println("Username too short!");
		
		if(json == null || "template".equals(user))return;
		
		if(json.containsKey(user))json.remove(user);	
	
		JSONObject newSetting = new JSONObject(settings.toMap());
		json.put(user, newSetting);
		
		writeToFile();		
	}
	
}