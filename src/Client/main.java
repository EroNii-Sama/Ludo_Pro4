package Client;

public class main {

	/**
	 * La funci�n principal crea un objeto ClientFrame, establece su tama�o y visibilidad, crea un objeto Cliente e inicia el
	 * cliente
	 */
	public static void main(String[] args) {
		
		ClientFrame screen = ClientFrame.getInstance();
		screen.setSize(300,300);
		screen.setVisible(true);

		Client client = Client.getInstance();
		
		client.startClient();
	
	}

}
