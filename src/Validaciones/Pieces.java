package Validaciones;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import interfaces.ObservadoIF;
import interfaces.ObservadorIF;


/**
 * Esta clase se encarga de almacenar las piezas de cada jugador y avisar a los observadores cuando se mueven las piezas
 */
public class Pieces implements ObservadoIF{

	private int[][] red;
	private int[][] blue;
	private int[][] green;
	private int[][] yellow;
	private Map<Integer, Integer[]> coordinatesDictionary;
	private List<ObservadorIF> lst = new ArrayList<ObservadorIF>();
	
	int[][] GetRedPieces(){return red;}
	int[][] GetBluePieces(){return blue;}
	int[][] GetGreenPieces(){return green;}
	int[][] GetYellowPieces(){return yellow;}
	
	/**
	 * > La función `SetRedPieces` coloca las piezas rojas y notifica a todos los observadores
	 *
	 * @param red Las piezas rojas.
	 */
	void SetRedPieces(int[][] red){
		this.red = red;
		ListIterator<ObservadorIF> li = lst.listIterator();
		while(li.hasNext())
			li.next().notify(this);
	}
	/**
	 * > La función `SetBluePieces` coloca las piezas azules y notifica a todos los observadores
	 *
	 * @param blue Las piezas azules.
	 */
	void SetBluePieces(int[][] blue){
		this.blue = blue; 
		ListIterator<ObservadorIF> li = lst.listIterator();
		while(li.hasNext())
			li.next().notify(this);
	}
	/**
	 * > La función `SetGreenPieces` coloca las piezas verdes y notifica a todos los observadores
	 *
	 * @param green Las piezas verdes.
	 */
	void SetGreenPieces(int[][] green){
		this.green = green; 
		ListIterator<ObservadorIF> li = lst.listIterator();
		while(li.hasNext())
			li.next().notify(this);
	}
	/**
	 * > La función `SetYellowPieces` coloca las piezas amarillas y notifica a todos los observadores
	 *
	 * @param yellow Las piezas amarillas.
	 */
	void SetYellowPieces(int[][] yellow){
		this.yellow = yellow;
		ListIterator<ObservadorIF> li = lst.listIterator();
		while(li.hasNext())
			li.next().notify(this);
	}
	
	private static Pieces instance = null;
	/**
	 * Si la instancia es nula, cree una nueva instancia y devuélvala. De lo contrario, devolver la instancia existente
	 *
	 * @return La instancia de la clase Pieces.
	 */
	public static Pieces GetPieces(){
		if(instance == null)
			instance = new Pieces();
		return instance;
	}
	
    // El constructor de la clase Pieces. Crea un nuevo HashMap y llama a la función SetDictionary.
    Pieces(){
    	coordinatesDictionary = new HashMap<Integer, Integer[]>();
    	SetDictionary();
    }
	
    /**
     * Devuelve una matriz 3D de todas las piezas en el tablero.
     *
     * @return Una matriz de matrices de matrices de enteros.
     */
    int [][][] GetAll(){
    	int[][][] all = new int[4][4][2];
    	all[0] = GetRedPieces();
    	all[1] = GetBluePieces();
    	all[2] = GetGreenPieces();
    	all[3] = GetYellowPieces();
    	return all;
    }
    
    /**
     * Esta función establece las piezas de todos los jugadores a los valores pasados en la matriz
     *
     * @param allpieces una matriz de enteros de 4x4x4.
     */
    public void SetAll(int[][][] allpieces) {
		SetRedPieces(allpieces[0]);
		SetBluePieces(allpieces[1]);
		SetGreenPieces(allpieces[2]);
		SetYellowPieces(allpieces[3]);		
	}
    
    /**
     * Toma una matriz de enteros y devuelve la clave de la entrada del diccionario cuyo valor es el mismo que la matriz
     *
     * @param place las coordenadas del lugar que desea decodificar
     * @return La clave del diccionario.
     */
    int Decode(int[] place){
		 for (Entry<Integer, Integer[]> entry : coordinatesDictionary.entrySet()) 
		 {
			 Integer [] value = entry.getValue();
			 int[] toInt = new int[value.length];
			 int count = 0;
			 for(Integer v : value)
				 toInt[count++] = v.intValue();
		        if ( Arrays.equals(place,toInt)) 
		            return entry.getKey();
		 }
		 return 0;
	}
	
	/**
	 * Esta función toma un lugar y devuelve las coordenadas de ese lugar
	 *
	 * @param place El lugar que desea codificar.
	 * @return El método devuelve una matriz de enteros.
	 */
	int[] Encode(int place) {
		int temp []= new int[2];
		temp[0]=coordinatesDictionary.get(place)[0];
		temp[1]= coordinatesDictionary.get(place)[1];
		return temp;
	}
	
	/**
	 * Toma un color como parámetro y devuelve una matriz de enteros que representan la posición de las piezas de ese color.
	 *
	 * @param color El color de las piezas de las que quieres obtener los lugares.
	 * @return Una matriz de números enteros.
	 */
	int[] GetDecodedPiecesPlacesByColor(String color){
		Game jogo = Game.GetJuego();
		int [] decodedPieces=new int[4];
		int[][] encodedPieces = GetEncodedPiecesFromColor(color);
		int count=0;
		for(int[] piece :encodedPieces ){
			if(jogo.IsInStart(piece, color))
				decodedPieces[count] = -1;
			else if(jogo.IsInEnd(piece, color))
				decodedPieces[count] = 0;
			else
				decodedPieces[count] = Decode(piece);
			count++;
		}		
		return decodedPieces;
	}
	
	/**
	 * Dado un color, devuelve las piezas codificadas para ese color.
	 *
	 * @param color El color de las piezas que quieras conseguir.
	 * @return Una matriz de matrices de enteros.
	 */
	int[][] GetEncodedPiecesFromColor(String color) {
		switch(color){
		case "red":
			return GetRedPieces();
		case "blue":
			return GetBluePieces();
		case "green":
			return GetGreenPieces();
		case "yellow":
			return GetYellowPieces();
		}
		return null;
	}

	/**
	 * Toma una matriz 2D de coordenadas y un color, y luego establece las piezas de ese color en las coordenadas de la
	 * matriz.
	 *
	 * @param newCoordinates Esta es una matriz 2D que contiene las nuevas coordenadas de las piezas.
	 * @param color El color del jugador que se mueve.
	 */
	void MovePieces(int[][] newCoordinates,String color) {
		switch(color){
		case "red":
			SetRedPieces(newCoordinates);
			break;
		case "blue":
			SetBluePieces(newCoordinates);
			break;
		case "green":
			SetGreenPieces(newCoordinates);
			break;
		case "yellow":
			SetYellowPieces(newCoordinates);
			break;
		}
	}
	
	/**
	 * Crea un diccionario de coordenadas para cada una de las 100 casillas del tablero
	 */
	private void SetDictionary(){
		// Crear una matriz 2D de enteros.
		Integer temps[][] = new Integer[][]{
				new  Integer[]{6,0},new Integer[]{7,0}, new Integer[]{8,0}, new Integer[]{8,1}, new Integer[]{8,2} ,
				new  Integer[]{8,3},new Integer[]{8,4}, new Integer[]{8,5}, new Integer[]{9,6}, new Integer[]{10,6},
				new Integer[]{11,6},new Integer[]{12,6},new Integer[]{13,6},new Integer[]{14,6},new Integer[]{14,7},
				new Integer[]{14,8},new Integer[]{13,8},new Integer[]{12,8},new Integer[]{11,8},new Integer[]{10,8},
				new Integer[]{9,8},	new Integer[]{8,9},	new Integer[]{8,10},new Integer[]{8,11},new Integer[]{8,12},
				new Integer[]{8,13},new Integer[]{8,14},new Integer[]{7,14},new Integer[]{6,14},new Integer[]{6,13},
				new Integer[]{6,12},new Integer[]{6,11},new Integer[]{6,10},new Integer[]{6,9} ,new Integer[]{5,8} ,
				new Integer[]{4,8} ,new Integer[]{3,8} ,new Integer[]{2,8} ,new Integer[]{1,8} ,new Integer[]{0,8} ,
				new Integer[]{0,7} ,new Integer[]{0,6} ,new Integer[]{1,6} ,new Integer[]{2,6} ,new Integer[]{3,6} ,
				new Integer[]{4,6} ,new Integer[]{5,6} ,new Integer[]{6,5} ,new Integer[]{6,4} ,new Integer[]{6,3} ,
				new Integer[]{6,2} ,new Integer[]{6,1} 
		};
		// Agregar los valores de la matriz `temps` al diccionario `coordinatesDictionary`.
		int count = 1;
		for(Integer[] temp : temps)
			coordinatesDictionary.put(count++, temp);
		
		// Crear una matriz 2D de enteros.
		Integer finalroute[][] = new Integer[][]{
			new Integer[]{1,7},new Integer[]{2,7},new Integer[]{3,7},new Integer[]{4,7},new Integer[]{5,7},//red
			new Integer[]{7,13},new Integer[]{7,12},new Integer[]{7,11},new Integer[]{7,10},new Integer[]{7,9},//blue
			new Integer[]{7,1},new Integer[]{7,2},new Integer[]{7,3},new Integer[]{7,4},new Integer[]{7,5},//green
			new Integer[]{13,7},new Integer[]{12,7},new Integer[]{11,7},new Integer[]{10,7},new Integer[]{9,7},//yellow
		};
		
		// Agregar la ruta final al diccionario.
		count =100;
		for(Integer[] temp : finalroute)
			coordinatesDictionary.put(count++, temp);
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
	public Object get(int i) {
		// TODO Auto-generated method stub
		return null;
	}
}
