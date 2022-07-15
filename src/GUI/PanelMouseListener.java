package GUI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import Validaciones.GameFrame;

public class PanelMouseListener	implements MouseListener {

	private static PanelMouseListener instance = null;
	private GameFrame frameJuego;
	
	// Creando una nueva instancia de la clase GameFrame.
	public PanelMouseListener(){
		frameJuego = GameFrame.GetGamef();
	}
	
	/**
	 * Si la instancia es nula, cree una nueva instancia; de lo contrario, devuelva la instancia existente.
	 *
	 * @return La instancia de la clase.
	 */
	public static PanelMouseListener GetMouseListener(){
		if(instance == null)
			instance = new PanelMouseListener();
		return instance;
	}
	
	@Override
	// Llamar al método MouseClicked en la clase GameFrame.
	public void mouseClicked(MouseEvent e) {
		frameJuego.MouseClicked(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
  
}