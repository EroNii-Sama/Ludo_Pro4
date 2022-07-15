package Client;
import javax.swing.JPanel;

public class ClientPanel extends JPanel {
	
	private static ClientPanel instance = null;
	
	public ClientPanel(){
		
	}
	
	/**
	 * Si la instancia es nula, cree una nueva instancia de ClientPanel. De lo contrario, devolver la instancia existente
	 *
	 * @return La instancia de la clase ClientPanel.
	 */
	public static ClientPanel getInstance(){
		if(instance == null){
			instance = new ClientPanel();
		}
		
		return instance;
	}

}
