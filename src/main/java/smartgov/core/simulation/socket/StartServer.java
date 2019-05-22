package smartgov.core.simulation.socket;

import java.io.IOException;

public class StartServer {

	public static void startServer(String file, String command) {
		//Will only work on windows...
		try {
			Runtime.getRuntime().exec("cmd.exe /c start " + command + " " + file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
