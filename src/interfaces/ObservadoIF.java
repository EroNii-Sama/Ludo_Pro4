package interfaces;

public interface ObservadoIF {

	/**
	 * Agrega un observador al conjunto de observadores para este objeto, siempre que no sea el mismo que alg�n observador que
	 * ya est� en el conjunto
	 *
	 * @param observador El observador que se agregar� a la lista de observadores.
	 */
	void add(ObservadorIF observador);
	
	/**
	 * Elimina un observador del conjunto de observadores de este objeto.
	 *
	 * @param observador El observador que se eliminar� de la lista de observadores.
	 */
	void remove(ObservadorIF observador);
	
	/**
	 * Devuelve el elemento en la posici�n especificada en esta lista.
	 *
	 * @param i El �ndice del objeto a obtener.
	 * @return El objeto en el �ndice dado.
	 */
	Object get(int i);
}
