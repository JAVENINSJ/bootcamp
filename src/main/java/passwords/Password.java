package passwords;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Password {

	public static JSONObject json = null;
	
	public static void getLoginFile() {
		try (FileReader reader = new FileReader("pass.json")) {
			JSONParser jsonParser = new JSONParser();
			json = (JSONObject) jsonParser.parse(reader);
		} catch (Exception e) {
			System.err.println("Pass.json file not found!");
			e.printStackTrace();
		}
	}

	public static boolean checkPassword(String user, String password) {
		String encryptedPassword = encrypt(password,(String) ((JSONObject) json.get(user)).get("salt"));
		encryptedPassword = encryptedPassword.substring(encryptedPassword.indexOf(':') + 1);
		
		if (encryptedPassword.equals(((JSONObject) json.get(user)).get("pass"))) {
			return true;
		}
		return false;
	}
	
	
	private static String saltMine() {
		Random rand = new Random();
		int temp1 = rand.nextInt();
		return new String(Integer.toHexString(temp1));
	}

	private static String encrypt(String password, String salt) {
		Random rand = new Random(password.hashCode() + salt.hashCode());
		int temp1 = rand.nextInt();

		String hex = Integer.toHexString(temp1);

		return salt + ":" + hex;
	}

	@SuppressWarnings("unchecked")
	public static void addPassword(String user, String password) {
		if (json == null) return;
		if (json.containsKey(user))	json.remove(user);
		Map<String, String> map = new HashMap<>();
		String salt = saltMine();
		String pass = encrypt(password, salt);
		map.put("salt", salt);
		map.put("pass", pass.substring(pass.indexOf(':') + 1));

		json.put(user, new JSONObject(map));

		writeToFile();
	}

	private static void writeToFile() {
		try (FileWriter fileWriter = new FileWriter("pass.json")) {

			fileWriter.write(json.toJSONString());
			fileWriter.flush();

		} catch (Exception e) {
			System.err.println("pass.json file not found!");
			e.printStackTrace();
		}
	}

	public static void removeAcc(String user) {
		if (json == null || json.get(user) == null)
			return;
		json.remove(user);
		writeToFile();
	}
}
