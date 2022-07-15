package Validaciones;

public class GetByColor {

	/**
	 * Las posiciones iniciales de las piezas rojas son las cuatro esquinas del tablero.
	 *
	 * @return Una matriz 2D de enteros.
	 */
	int[][] getStartRed(){return new int [][]{ new int[]{1,4}, new int[]{4,1},	new int[]{1,1},	new int[]{4,4}};}
	/**
	 * Devuelve una matriz de matrices de enteros, donde cada matriz de enteros es un par de enteros que representan la fila y
	 * la columna de una posición inicial para una pieza azul.
	 *
	 * @return Una matriz de matrices de enteros.
	 */
	int[][] getStartBlue(){return new int [][]{ new int[]{1,10}, new int[]{1,13}, new int[]{4,10}, new int[]{4,13}};}
	/**
	 * Devuelve una matriz de matrices de enteros, donde cada matriz de enteros es un par de enteros que representan la fila y
	 * la columna de una celda verde inicial.
	 *
	 * @return Una matriz de matrices de enteros.
	 */
	int[][] getStartGreen(){return new int [][]{ new int[]{10,1}, new int[]{13,1}, new int[]{10,4}, new int[]{13,4}};}
	/**
	 * Devuelve una matriz de matrices de enteros.
	 *
	 * @return Una matriz 2D de enteros.
	 */
	int[][] getStartYellow(){return new int [][]{ new int[]{13,10}, new int[]{10,13}, new int[]{10,10}, new int[]{13,13}};	}
		
	/**
	 * Esta función devuelve una matriz de dos enteros, siendo el primero 6 y el segundo 7.
	 *
	 * @return Una matriz de números enteros.
	 */
	private int[] getEndRed(){return new int[]{6,7};}
	/**
	 * Esta función devuelve una matriz de dos enteros, siendo el primero el final del rango azul y el segundo el final del
	 * rango verde.
	 *
	 * @return El método devuelve una matriz de enteros.
	 */
	private int[] getEndBlue(){return new int[]{7,8};}
	/**
	 * La función getEndGreen devuelve una matriz de dos enteros, siendo el primero 7 y el segundo 6.
	 *
	 * @return El método devuelve una matriz de enteros.
	 */
	private int[] getEndGreen(){return new int[]{7,6};}
	/**
	 * Esta función devuelve una matriz de dos enteros, el primero es la coordenada x del final del camino amarillo y el
	 * segundo es la coordenada y del final del camino amarillo.
	 *
	 * @return El método devuelve una matriz de enteros.
	 */
	private int[] getEndYellow(){return new int[]{8,7};}
		
	private int LastRedPieceMoved;
	private int LastBluePieceMoved;
	private int LastGreenPieceMoved;
	private int LastYellowPieceMoved;
	
	private static GetByColor instance = null;
	/**
	 * Si la instancia es nula, cree una nueva instancia; de lo contrario, devuelva la instancia existente.
	 *
	 * @return La instancia de la clase GetByColor.
	 */
	public static GetByColor GetGetByColor(){
		if(instance == null)
			instance = new GetByColor();
		return instance;
	}	
	
	/**
	 * Obtenga los lugares de inicio para el color dado.
	 *
	 * @param color El color del jugador para el que desea obtener los lugares de inicio.
	 * @return Los lugares de inicio para el color que se pasa.
	 */
	int[][] GetStartPlacesByColor(String color) {
		switch(color){
		case "red":
			return getStartRed();
		case "blue":
			return getStartBlue();
		case "green":
			return getStartGreen();
	/**
	 * > Esta función devuelve las coordenadas del lugar final de un color dado
	 *
	 * @param color El color del jugador.
	 * @return Las coordenadas del lugar final del color dado.
	 */
		case "yellow":
			return getStartYellow();
		}
		return null;
	}
	
	int[] GetCoordinatesOfEndPlaceByColor(String color) {
		switch(color){
		case "red":
			return this.getEndRed();
		case "blue":
			return this.getEndBlue();
		case "green":
			return this.getEndGreen();
		case "yellow":
		return this.getEndYellow();
		}
		return null;
	}
	
	/**
	 * > Esta función toma un color como parámetro y devuelve la casa inicial para ese color
	 *
	 * @param color El color del jugador.
	 * @return La casa inicial para el color del jugador.
	 */
	int GetStartHouseByColor(String color) {
		 int redStartPlace = 43;
		 int blueStartPlace = 30;
		 int greenStartPlace = 4;
		 int yellowStartPlace = 17;
		switch(color){
		case "red":
			return redStartPlace;
		case "blue":
			return blueStartPlace;
		case "green":
			return greenStartPlace;
		case  "yellow":
			return yellowStartPlace;
		}
		return 0;
	}
	
	/**
	 * > Dado un color, devuelve la casa final para ese color
	 *
	 * @param color El color del jugador.
	 * @return La casa final del color en el que se pasa.
	 */
	int GetFinalHouseByColor(String color) {
		 int redFinalPlace = 41;
		 int blueFinalPlace = 28;
		 int greenFinalPlace = 2;
		 int yellowFinalPlace = 15;
		switch(color){
		case "red":
			return redFinalPlace;
		case "blue":
			return blueFinalPlace;
		case "green":
			return greenFinalPlace;
		case  "yellow":
			return yellowFinalPlace;
		}
		return 0;
	}
	
	/**
	 * > Esta función devuelve la primera casa de la ruta final de un jugador en función del color del jugador
	 *
	 * @param color El color del jugador.
	 * @return La primera casa de la ruta final del jugador con el color que se pasa.
	 */
	int GetFirstHouseOfFinalRouteByColor(String color){
		int redFirstPlaceofFinalRoute = 100;
		 int blueFirstPlaceofFinalRoute = 105;
		 int greenFirstPlaceofFinalRoute = 110;
		 int yellowFirstPlaceofFinalRoute = 115;
		switch(color){
		case "red":
			return redFirstPlaceofFinalRoute;
		case "blue":
			return blueFirstPlaceofFinalRoute;
		case "green":
			return greenFirstPlaceofFinalRoute;
		case  "yellow":
			return yellowFirstPlaceofFinalRoute;
		}
		return 0;
	}
	
	/**
	 * > Esta función devuelve la última pieza movida por el color pasado en
	 *
	 * @param color El color del jugador por el que quieres mover la última pieza.
	 * @return La última pieza movida por el color especificado.
	 */
	int GetLastPieceMovedByColor(String color){
		switch(color){
		case "red":
			return LastRedPieceMoved;
		case "blue":
			return LastBluePieceMoved;
		case "green":
			return LastGreenPieceMoved;
		case "yellow":
			return LastYellowPieceMoved;
		}
		return 0;
	}
	
	/**
	 * Esta función toma un color y un nuevo lugar, y luego coloca la última pieza movida en el nuevo lugar
	 *
	 * @param color El color de la pieza que se movió.
	 * @param newPlace El nuevo lugar de la pieza que se movió.
	 */
	void StoreLastPieceMoved(String color, int newPlace) {
		switch(color){
		case "red":
			LastRedPieceMoved = newPlace;
			break;
		case "blue":
			LastBluePieceMoved = newPlace;
			break;
		case "green":
			LastGreenPieceMoved = newPlace;
			break;
		case "yellow":
			LastYellowPieceMoved = newPlace;
			break;
		}
	}
	
}
