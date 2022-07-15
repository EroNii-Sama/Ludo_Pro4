package Server;

public class MainServer {

	/**
	 * La función principal crea un objeto de servidor y ejecuta el servidor
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Server server = Server.getInstance();
		
		server.runServer();
		
		return;
	}

}