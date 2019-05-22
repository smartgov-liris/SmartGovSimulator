package smartgov.core.simulation.socket;

public class Server {

	private static Server instance = null;
	private Process process;
	
	public static synchronized Server getInstance(){
		if(instance == null){
			instance = new Server();
		}
		return instance;
	}

	public Process getProcess() {
		return process;
	}
	
	public Server() {
		if(process != null) {
			System.out.println("destroy process");
			this.process.destroy();
		}
	}
	
	public void setProcess(Process process) {
		this.process = process;
	}
	
	public static void resetSingleton(){
		if(instance != null){
			System.out.println("reset server");
			instance = null;
		}
	}
	
}
