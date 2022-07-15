package interfaces;

public interface ObservadoIF {

	/**
	 * Agrega un observador al conjunto de observadores para este objeto, siempre que no sea el mismo que algún observador que
	 * ya esté en el conjunto
	 *
	 * @param observador El observador que se agregará a la lista de observadores.
	 */
	void add(ObservadorIF observador);
	
	/**
	 * Elimina un observador del conjunto de observadores de este objeto.
	 *
	 * @param observador El observador que se eliminará de la lista de observadores.
	 */
	void remove(ObservadorIF observador);
	
	/**
	 * Devuelve el elemento en la posición especificada en esta lista.
	 *
	 * @param i El índice del objeto a obtener.
	 * @return El objeto en el índice dado.
	 */
	Object get(int i);
}
