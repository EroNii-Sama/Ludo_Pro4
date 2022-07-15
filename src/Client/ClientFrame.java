package Client;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class ClientFrame extends JFrame implements ActionListener, ObserverLobby {
	
	private static ClientFrame instance = null;
	private JLabel l1;
	private JButton b1;
	private JTextField t1;
	private Container screen;
	private String currentNickname;
	private boolean ValidNickname = true;
	
	// El constructor de la clase ClientFrame. Está creando la GUI del cliente.
	public ClientFrame(String s){
		super(s);
		screen = getContentPane();
		screen.setLayout(null);
		
		l1 = new JLabel("Nickname");
		l1.setBounds(20,70,100,30);
		t1 = new JTextField();
		t1.setBounds(130,70,100,30);
		b1 = new JButton("Join");
		b1.setBounds(90,130,80,80);
		
		screen.add(l1);
		screen.add(t1);
		screen.add(b1);
		
		
		t1.addActionListener(this);
		b1.addActionListener(this);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Client.getInstance().addObserver(this);
	}
	
	
	/**
	 * Si la instancia es nula, cree una nueva instancia de ClientFrame; de lo contrario, devuelva la instancia existente
	 *
	 * @return La instancia de la clase ClientFrame.
	 */
	public static ClientFrame getInstance(){
		if(instance == null){
			instance = new ClientFrame("Ludo");
		}
		
		return instance;
	}

	@Override
	// Este método se llama cuando el usuario hace clic en el botón. Obtiene el apodo del campo de texto y lo envía al
	// servidor.
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == b1){
			
			currentNickname = t1.getText();
			Client.getInstance().sendMessage("Nickname " + currentNickname);
			b1.setEnabled(false);
		}
		else{
			System.exit(1);
		}
		
	}

	@Override
	// Ocultar el campo de texto y cambiar la etiqueta para dar la bienvenida al usuario.
	public void receivedNicknameAvaiable() {
		t1.hide();
		l1.setSize(100, 30);
		l1.setText("Bienvenido" + currentNickname);
	}


	@Override
	// Habilitando el botón nuevamente.
	public void receivedNicknameUnavaiable() {
		b1.setEnabled(true);
	}


	@Override
	// Crear una nueva instancia de ClientConnection y luego desechar el marco actual.
	public void receivedGameStart() {
		ClientConnection.getInstance();
		dispose();
	}

}
