package Client;
import GUI.BoardFrame;

public class ClientConnection {
	
	private static ClientConnection instance = null;
	
	// Creando una nueva instancia de la clase BoardFrame.
	public ClientConnection(){
		new BoardFrame();
	}
	
	/**
	 * Si la instancia es nula, cree una nueva instancia de la clase y devuélvala. Si la instancia no es nula, devolver la
	 * instancia
	 *
	 * @return La instancia de la clase ClientConnection.
	 */
	public static ClientConnection getInstance(){
		if(instance == null){
			instance = new ClientConnection();
		}
		return instance;
	}

}
