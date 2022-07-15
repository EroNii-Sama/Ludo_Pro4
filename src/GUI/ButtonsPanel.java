package GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Validaciones.GameFrame;
import interfaces.*;

public class ButtonsPanel extends JPanel implements ObservadorIF {
	private static final long serialVersionUID = 1L;

	private int puntos;
	// Una variable que se utiliza para establecer la ubicación de los botones y etiquetas.
	private int x0=15* puntos;
	private JButton nueva_partida = new JButton("Nueva Partida");
	private JButton cerrar_juego = new JButton("Cerrar Juego");
	private JButton lanzar_dado = new JButton("Lanzar Dado");
	private JLabel jugadorActual = new JLabel();
	private JLabel dado = new JLabel();
	private JLabel estadoAtual = new JLabel();

	private GameFrame frameJuego;
	
	private static ButtonsPanel instance = null;
	/**
	 * Si la instancia es nula, cree una nueva instancia de la clase. De lo contrario, devolver la instancia existente
	 *
	 * @return La instancia de la clase ButtonsPanel.
	 */
	public static ButtonsPanel GetButtonsPanel(){
	// El constructor de la clase.
		if(instance == null)
			instance = new ButtonsPanel();
		return instance;
	}
	
	private ButtonsPanel(){
		this.puntos = Main.puntos;
		frameJuego = GameFrame.GetGamef();
		frameJuego.add(this);
		SetButtons();
		SetLabels();
		SetDado();
		SetCurrentPlayerLabel();
		StartListeners();
		SetConfigurations();
		
	}
		
	/**
	 * Devuelve la URL de la imagen del dado con el número pasado como parámetro
	 *
	 * @param n El número del dado.
	 * @return La URL de la imagen.
	 */
	private URL GetDadoImageByNumber(int n){
		switch(n){
		case 1:	return getClass().getResource("/images/1.png");
		case 2:	return getClass().getResource("/images/2.png");
		case 3:	return getClass().getResource("/images/3.png");
		case 4:	return getClass().getResource("/images/4.png");
		case 5:	return getClass().getResource("/images/5.png");
		case 6:	return getClass().getResource("/images/6.png");
		case 7:	return getClass().getResource("/images/6.png");
		}
		return null;
	}
	

	/**
	 * Establece el tamaño y la ubicación de los botones.
	 */
	private void SetButtons(){
		int i=0;
		for(JButton jb : new JButton []{nueva_partida, cerrar_juego}){
			jb.setSize(4* puntos, puntos);
			jb.setLocation(x0+ puntos /2, i+ puntos);
			this.add(jb);
			i+=3* puntos /2;
		}		
		lanzar_dado.setSize(4* puntos, puntos);
		lanzar_dado.setLocation(x0+ puntos /2, 19* puntos /2);
		this.add(lanzar_dado);
		
		lanzar_dado.setEnabled(false);
	}
	
	/**
	 * Crea una etiqueta con el texto "Jugar" y la coloca en la esquina superior izquierda de la ventana
	 */
	private void SetLabels(){
		JLabel jl = new JLabel("Jugar:", SwingConstants.CENTER);
		jl.setFont(new Font("Nuevo",Font.BOLD,20) );
		jl.setSize(4* puntos, puntos);
		jl.setLocation(x0+ puntos /2, 11* puntos /2);
		this.add(jl);
		
		estadoAtual.setHorizontalAlignment(JLabel.CENTER);
		estadoAtual.setVerticalAlignment(JLabel.TOP);
		estadoAtual.setFont(new Font("Nuevo",Font.PLAIN,15) );
		estadoAtual.setSize(5* puntos, 3* puntos);
		estadoAtual.setLocation(x0, 22* puntos /2);
		this.add(estadoAtual);
	}

	/**
	 * Esta función establece la ubicación de los dados.
	 */
	private void SetDado(){
		dado.setLocation(x0+2* puntos,16* puntos /2);
		this.add(dado);
	}

	/**
	 * Esta función crea una etiqueta que mostrará el nombre del jugador actual
	 */
	private void SetCurrentPlayerLabel(){
		jugadorActual = new JLabel();
		jugadorActual.setHorizontalAlignment(JLabel.CENTER);
		jugadorActual.setFont(new Font("Nuevo",Font.BOLD,20) );
		jugadorActual.setSize(4* puntos, puntos);
		jugadorActual.setLocation(x0+ puntos /2, 13* puntos /2);
		this.add(jugadorActual);
	}
	
	/**
	 * Inicia los oyentes para los botones.
	 */
	private void StartListeners(){
		
		lanzar_dado.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Thread t = new Thread(new Runnable() { 
				public void run() { 			
					frameJuego.StartNewRound();
					frameJuego.RollDado();
					frameJuego.MovePiece();
				}});
				t.start();
			}
			
		});
		
		nueva_partida.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				frameJuego.StartGame();
				revalidate();
				repaint();
			}
        });

		
		cerrar_juego.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				revalidate();
				repaint();
			}
        });
	}
	
	/**
	 * > Esta función establece el color de fondo, el diseño, el tamaño, la ubicación y la visibilidad del panel
	 */
	private void SetConfigurations() {
		this.setBackground(Color.lightGray);
		this.setLayout(null);
		this.setSize(5* puntos, 15* puntos);
		this.setLocation(15* puntos, 0);
		this.setVisible(true);
	}
	
	/**
	 * Establece la imagen del dado en un número aleatorio entre 1 y 6, y luego espera 90 milisegundos.
	 *
	 * @param diceValue El valor de los dados.
	 */
	private void RefreshDado(int diceValue){
		if(diceValue<=7 && diceValue >0){
			dado.setIcon(new ImageIcon(GetDadoImageByNumber(diceValue)));
			try {
				Thread.sleep(90);
			} catch (InterruptedException e) {
			}
		}
		else
			dado.setIcon(new ImageIcon());
		dado.setSize(dado.getIcon().getIconWidth(), dado.getIcon().getIconHeight());
	}

	@Override
	// La implementación del método `notify` de la interfaz `ObservadorIF`. Es llamado por el objeto `ObservadoIF` para
	// notificar al objeto `ButtonsPanel` que algo ha cambiado.
	public void notify(ObservadoIF observado) {
		int diceValue = (int) observado.get(1);
		RefreshDado(diceValue);
		
		Color foreground = (Color) observado.get(2);
		jugadorActual.setForeground(foreground);
		
		String currentPlayerText = (String) observado.get(3);
		jugadorActual.setText(currentPlayerText);
		
		String currentStateText = (String) observado.get(4);
		estadoAtual.setText(currentStateText);
		
		boolean lancarDadoEnabled = (boolean )observado.get(5);
		lanzar_dado.setEnabled(lancarDadoEnabled);
		
		revalidate();
		repaint();
	}


}
