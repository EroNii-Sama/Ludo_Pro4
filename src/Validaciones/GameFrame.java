package Validaciones;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.JOptionPane;

import interfaces.*;

public class GameFrame implements ObservadoIF, ObservadorIF{

	Game game;
	Pieces pieces;
	private List<ObservadorIF> lst = new ArrayList<ObservadorIF>();
	boolean lanzarDadoEnabled;
	
	private static GameFrame instance = null;
	/**
	 * Si la instancia de la clase GameFrame es nula, cree una nueva instancia de la clase GameFrame. Si la instancia de la
	 * clase GameFrame no es nula, devolver la instancia de la clase GameFrame
	 *
	 * @return La instancia de la clase GameFrame.
	 */
	public static GameFrame GetGamef(){
		if(instance == null)
			instance = new GameFrame();
		return instance;
	}	
	
	// Creando una nueva instancia de la clase GameFrame.
	GameFrame(){
		game = Game.GetJuego();
		pieces = Pieces.GetPieces();
		
		game.add(this);
		pieces.add(this);
	}
	
	/**
	 * > La función `StartNewRound` se llama cuando el usuario hace clic en el botón `LancarDado` (`RollDice`). Deshabilita el
	 * botón `LancarDado` y llama a la función `StartNewRound` de la clase `Game`
	 */
	public void StartNewRound(){
		SetLancarDadoEnabled(false);
		game.StartNewRound();
	}
	
	/**
	 * > Esta función establece el valor de la variable lanzarDadoEnabled en verdadero o falso, y luego notifica a todos los
	 * observadores que el valor de la variable ha cambiado
	 *
	 * @param trueorfalse verdadero si el botón debe estar habilitado, falso si debe estar deshabilitado.
	 */
	public void SetLancarDadoEnabled(boolean trueorfalse){
		lanzarDadoEnabled = trueorfalse;
		ListIterator<ObservadorIF> li = lst.listIterator();
		while(li.hasNext())
		li.next().notify(this);
	}
	
	/**
	 * > La función `StartGame()` habilita el botón `LancarDado` y llama a la función `StartGame()` de la clase `Game`
	 */
	public void StartGame(){
		SetLancarDadoEnabled(true);
		game.StartGame();
	}
	
	public void RollDado(){
		game.RollDado();
	}
	
	public void MovePiece(){
		game.MovePiece();
	}
	
	public int GetDadoValue(){
		return game.GetDadoValue();
	}
	
	public void MouseClicked(MouseEvent e){
		game.MouseClicked(e);
	}
	
	/**
	 * Devuelve un color basado en el jugador actual.
	 *
	 * @return El color del jugador actual.
	 */
	public Color GetCurrentPlayerForeground(){
		switch (game.GetCurrentPlayer()){
			case 0:	return new Color(220,20,60);
			case 1:	return new Color (60,179,113);
			case 2:	return new Color(255,215,0);
			case 3:	return new Color(100,149,237);
		}return null;
	}
	
	/**
	 * > Esta función devuelve una cadena que representa al jugador actual
	 *
	 * @return Una cadena con el nombre del jugador actual.
	 */
	public String GetCurrentPlayerText(){
		switch (game.GetCurrentPlayer()){
			case 0:	return "Rojo";
			case 1: return "Verde";
			case 2: return "Amarillo";
			case 3:	return "Azul";
		}	return null;
	}
	
	/**
	 * Devuelve una cadena que describe el estado actual del juego.
	 *
	 * @return El texto que se mostrará en la barra de estado.
	 */
	public String GetCurrentStateText(){
		switch (game.GetCurrentState()){
			case 6: return "";
			case 0:	return "<html>Esperando<br> lanzamento.</html>";
			case 1:	return "<html>Escoja la jugada<br>.</html>";
			case 2:	return "<html>No hay<br> ninguna jugada<br> posible.</html>";
			case 3:	return "<html> esta jugada<br> no es<br> posible<br>.";
			case 4:	return "<html> Se mueve 7 espacios<br> (Dado = 6 e<br> no existen<br> piezas en el tablero<br> inicial).</html>";
			case 5:	return "<html> <br> avanza<br> 20 casillas.</html>";
		}return null;
	}

	

	@Override
	public void add(ObservadorIF observador) {
		lst.add(observador);
	}

	@Override
	public void remove(ObservadorIF observador) {
		lst.remove(observador);
	}

	@Override
	// Devolviendo el valor de los dados, el color del jugador actual, el nombre del jugador actual, el estado actual del
	// juego, el valor de lanzarDadoEnabled y las piezas del juego.
	public Object get(int i) {
		if(i == 1)
			return game.GetDadoValue();
		if(i == 2)
			return GetCurrentPlayerForeground();
		if(i == 3)
			return GetCurrentPlayerText();
		if(i == 4)
			return GetCurrentStateText();
		if(i == 5)
			return lanzarDadoEnabled;
		if(i == 6)
			return pieces.GetAll();
		return 0;
	}
	
	/**
	 * Muestra un diálogo de mensaje con el ganador y los puntos de cada jugador.
	 */
	void EndGame(){
		JOptionPane.showMessageDialog(null, "Fin del juego\n El ganador es: " + GetCurrentPlayerText()+
				"\n" + game.GetPoints());
		System.exit(1);
	}


	@Override
	// Notificar a todos los observadores que el valor de la variable ha cambiado.
	public void notify(ObservadoIF observado) {
		ListIterator<ObservadorIF> li = lst.listIterator();
		while(li.hasNext())
			li.next().notify(this);
	}
}
