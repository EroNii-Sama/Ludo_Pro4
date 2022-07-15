package GUI;
import java.awt.*;
import javax.swing.*;

import Client.ObserverGame;
import Client.Client;

public class BoardFrame extends JFrame implements ObserverGame {
	private static final long serialVersionUID = 1L;

	private int alto;
	private int ancho;
	private Graphics2D g;
	public BoardPanel boardPanel;
	public ButtonsPanel buttonsPanel;
	
	// El constructor de la clase BoardFrame. Se llama cuando se instancia la clase.
	public BoardFrame(){
		Client.getInstance().addObserver(this);
		SetPanelTablero();
		SetPanelBotones();
		SetFrameC();
	}
	
	/**
	 * Crea un nuevo objeto BoardPanel y lo agrega al JFrame.
	 */
	private void SetPanelTablero() {
		boardPanel = BoardPanel.GetBoardPanel();
		boardPanel.paintComponents(g);
		this.getContentPane().add(boardPanel);
	}
	
	/**
	 * > Esta funci�n crea un nuevo objeto ButtonsPanel y lo agrega al JFrame
	 */
	private void SetPanelBotones(){
		buttonsPanel = ButtonsPanel.GetButtonsPanel();
		this.getContentPane().add(buttonsPanel);
	}
	
	/**
	 * > Establezca los tama�os predeterminados de los componentes, establezca los l�mites y el dise�o del marco, establezca
	 * el t�tulo y el tama�o del marco, establezca la operaci�n de cierre predeterminada del marco y haga que el marco sea
	 * visible
	 */
	private void SetFrameC() {
		SetDefaultSizes();
		SetBoundsAndLayout();		
		SetTitleAndSize();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	/**
	 * Establece los tama�os predeterminados de la ventana.
	 */
	private void SetDefaultSizes(){
		this.alto = Main.alto;
		this.ancho = Main.ancho;
	}
	
	/**
	 * Establece el tama�o y la posici�n de la ventana.
	 */
	private void SetBoundsAndLayout(){
		Toolkit tk=Toolkit.getDefaultToolkit();
		Dimension screenSize=tk.getScreenSize();
		int sl=screenSize.width, sa=screenSize.height;
		int x=sl/2- ancho /2, y=sa/2- alto /2;
		this.setBounds(x,y, ancho, alto);
		this.setLayout(null);
	}
	
	/**
	 * Esta funci�n establece el tama�o y el t�tulo de la ventana.
	 */
	private void SetTitleAndSize(){
		this.setSize(ancho, alto);
		this.setTitle("Ludo");
		this.setResizable(false);
	}

	@Override
	// Un m�todo que se llama cuando el cliente recibe una jugada del servidor.
	public void receivedPlay(String play) {
		System.out.println("Se a recibido una jugada " + play);
	}
	
}
