package Server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Logger;

public class Server{

	private static Server instance = null;

	private Lobby currentLobby;
	private boolean serverIsRunning = true;
	
	/**
	 * Si la instancia es nula, cree una nueva instancia y devuélvala. De lo contrario, devolver la instancia existente
	 *
	 * @return La instancia de la clase Server.
	 */
	public static Server getInstance(){
		if(instance == null){
			instance = new Server();
		}
		return instance;
	}
	Logger logger = Logger.getLogger(Server.class.getName());
	/**
	 * Crea un nuevo socket de servidor y luego llama a la función `threadAcceptClients` con el socket del servidor como
	 * parámetro
	 */
	public void runServer() {
		ServerSocket server;
		try {
			server = new ServerSocket(12345);
		} catch (IOException e) {
			System.out.println("No se pudo abrir el puerto 12345");
			e.printStackTrace();
			return;
		} 
		System.out.println("Puerto 12345 abierto!");

		currentLobby = new Lobby();

		threadAcceptClients(server);
	}
	
	/**
	 * Crea un nuevo hilo que escucha nuevos clientes y cuando se conecta un nuevo cliente, crea un nuevo hilo para escuchar a
	 * ese cliente.
	 *
	 * @param server El socket del servidor que escuchará nuevos clientes.
	 */
	private void threadAcceptClients(ServerSocket server) {
		
		Thread acceptClients = new Thread(){
			
			@Override
			public void run() {

				while (serverIsRunning){
					
					try {


						Socket newClient = server.accept();

						System.out.println("Nueva conexion con el cliente " + newClient.getInetAddress().getHostAddress());

						threadListenNewClient(newClient);
							
					} catch (IOException e) {
						System.out.println("El servidor se cerró");
						e.printStackTrace();
					}
				}
			}
		};
		
		acceptClients.run();
	}
	
	/**
	 * Crea un nuevo hilo que escucha a un nuevo cliente y lo agrega al lobby actual
	 *
	 * @param newClient El socket que representa al cliente que acaba de conectarse al servidor.
	 */
	private void threadListenNewClient(Socket newClient){
		
		Thread listenClient = new Thread(){
			
			@Override
			public void run(){
				
				Scanner scanner;
				
				if (currentLobby.getIsFull()) {
					System.out.println("Creando nuevo lobby");
					currentLobby = new Lobby();
				}
				
				Lobby lobby = currentLobby;

				while(serverIsRunning){
					String nickname = null;
					
					try {
						scanner = new Scanner(newClient.getInputStream());

						while (scanner.hasNextLine()) {
							String message = scanner.nextLine();
							
							logger.info("Mensaje recibido: " + message);

							if (message.equals("###")){

							} else if  (message.startsWith("Nickname ")){

								nickname = message.substring(9);

								PrintStream output = new PrintStream(newClient.getOutputStream());
								
								if (lobby.addPlayer(nickname, newClient)) {
									output.println("Nickname Valido");
									
									if (lobby.getIsFull()) {
										System.out.println("Comenzando la partida");
										lobby.startGame();
									}
								} else {
									output.println("Nickname Invalido");
								}

							} else {
								lobby.sendToAllPlayers(nickname, message);
							}
						}
					} catch (IOException e) {
						logger.info("El cliente se desconecto");
						e.printStackTrace();
					}
				}
			}
		};
		
		listenClient.start();
	}
}