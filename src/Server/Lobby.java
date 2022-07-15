package Server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Logger;

public class Lobby {

	private Player players[];
	private int numberOfPlayers;
	private boolean isFull;
	private int turn = 0;
	Logger logger = Logger.getLogger(Lobby.class.getName());
	
	// El constructor de la clase Lobby.
	public Lobby(){
		players = new Player[4];
		numberOfPlayers = 0;
		isFull = false;
	}
	
	/**
	 * Esta funci�n devuelve el valor de la variable isFull
	 *
	 * @return El valor de la variable isFull.
	 */
	public boolean getIsFull() {
		return isFull;
	}
	
	/**
	 * Esta funci�n devuelve el n�mero de jugadores en el juego.
	 *
	 * @return El n�mero de jugadores en el juego.
	 */
	public int getNumberOfPlayers(){
		return numberOfPlayers;
	}
	
	/**
	 * Esta funci�n agrega un jugador al juego.
	 *
	 * @param nickname El apodo del jugador que se agregar�.
	 * @param socket El z�calo del jugador que se est� agregando al juego.
	 * @return Un valor booleano.
	 */
	public boolean addPlayer(String nickname, Socket socket) {
		
		if (numberOfPlayers == 3) {
			isFull = true;
		}
		
		if (numberOfPlayers != 0) {
			
			for (int i = 0; i < numberOfPlayers; i++) {
				
				if (players[i].getNickname().compareTo(nickname) == 0) {
					
					return false;
				}
			}
		}
		
		players[numberOfPlayers] = new Player(nickname, socket);
		numberOfPlayers += 1;
		
		return true;
	}
	
	/**
	 * Env�a un mensaje a todos los jugadores en el juego para iniciar el juego
	 */
	public void startGame() {
		
		for (int i = 0; i<numberOfPlayers; i++){
			
			try {

				PrintStream output = new PrintStream(players[i].getSocket().getOutputStream());
				String message = "Game Start";
				String fullMessage;
				
				if (turn == i) {
					fullMessage = "YourTurn " + message;
				} else {
					fullMessage = message;
				}

				output.println(fullMessage);
			} catch (IOException e) {
				logger.severe("Error en enviar mensaje al cliente");
				e.printStackTrace();
			}
		}
		
		turn += 1;
	}
	
	/**
	 * Env�a un mensaje a todos los jugadores excepto al que envi� el mensaje.
	 *
	 * @param nickname El apodo del jugador que envi� el mensaje.
	 * @param message El mensaje que se enviar� a todos los jugadores.
	 */
	public void sendToAllPlayers(String nickname, String message) {

		for(int i = 0; i < numberOfPlayers; i++) {
			if(players[i].getNickname().compareTo(nickname) != 0) {
				try {

					PrintStream output = new PrintStream(players[i].getSocket().getOutputStream());
					String fullMessage;
					
					if (turn == i) {
						fullMessage = "YourTurn " + message;
					} else {
						fullMessage = message;
					}

					output.println(fullMessage);
				} catch (IOException e) {
					logger.severe("Error en enviar mensaje al cliente");
					e.printStackTrace();
				}
			}
		}
		
		turn += 1;
		
		if (turn == 4) {
			turn = 0;
		}
	}
}
