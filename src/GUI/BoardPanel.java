package GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import interfaces.ObservadoIF;
import interfaces.ObservadorIF;
import Validaciones.GameFrame;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class BoardPanel extends JPanel implements ObservadorIF {
	private static final long serialVersionUID = 1L;
	
	private int alto;
	private int ancho;
	// El tamaño de los cuadrados en el tablero.
	private int p;

	private int[][][] pieces = null;
	
	private static BoardPanel instance = null;
	/**
	 * Si la instancia de BoardPanel es nula, cree una nueva instancia de BoardPanel. De lo contrario, devolver la instancia
	 * existente
	 *
	 * @return La instancia de la clase BoardPanel.
	 */
	public static BoardPanel GetBoardPanel(){
		if(instance == null)
			instance = new BoardPanel();
		return instance;
	}
	
	// El constructor de la clase BoardPanel. Es establecer el tamaño del tablero, agregar el tablero a la fachada y
	// establecer las propiedades del tablero.
	private BoardPanel(){
		this.p = Main.puntos;
		this.alto = p*15;
		this.ancho = p*15;
		GameFrame.GetGamef().add(this);
		SetProperties();
		
	}
	
	/**
	 * > Esta función establece el tamaño, el borde, el fondo, el diseño y la visibilidad del panel
	 */
	private void SetProperties(){
		this.setSize(ancho, alto);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setBackground(Color.WHITE);
		this.setLayout(null);
		this.setVisible(true);
		this.addMouseListener(PanelMouseListener.GetMouseListener());
	}
		
	/**
	 * Esta función dibuja el tablero de juego, las piezas del juego de los jugadores y el lugar de llegada.
	 *
	 * @param g Objeto gráfico
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d=(Graphics2D) g;
		
		DrawAllLines(g2d);
		DrawLargeRectangles(g2d);
		DrawSmallRectangles(g2d);
		DrawArrivalPlace(g2d);
		DrawCircles(g2d);
		DrawPlayersGamePieces(g2d);
	}
	
	/**
	 * Dibuja todas las líneas en el tablero.
	 *
	 * @param g2d Objeto Graphics2D
	 */
	private void DrawAllLines(Graphics2D g2d) {
		g2d.setPaint(Color.BLACK);
		for(int i = 0; i <15 ; i++)
			for(int j= 0; j< 15; j ++){
				Rectangle2D rectangle = new Rectangle2D.Double(i*p,j*p,p,p); 				
				g2d.draw(rectangle);
			}
	}

	/**
	 * > Dibuja cuatro rectángulos grandes, dos en cada esquina del lienzo
	 *
	 * @param g2d Objeto Graphics2D
	 */
	private void DrawLargeRectangles(Graphics2D g2d) {
		int tamRet = 6*p, count =0;
		for(int i :new int []{0,  tamRet + 3* p})
			for(int j :new int []{0, tamRet + 3* p}){
				SetPaint(g2d, count++);
				Rectangle2D rectangle = new Rectangle2D.Double(i,j,tamRet, tamRet); 
				FillAndDrawBlackLine(g2d,rectangle);
			}
	}
	

	/**
	 * Esta función dibuja los pequeños rectángulos con flechas y negros
	 *
	 * @param g2d Objeto Graphics2D
	 */
	private void DrawSmallRectangles(Graphics2D g2d) {
		for(int i = 0; i < 4; i++)	{
			DrawSmallRectaglesWithArrowsAndBlackOnes(g2d, i);
			DrawSmallRectanglesFirstFive(g2d, i);
		}
	}

	/**
	 * > Dibuja los pequeños rectángulos con flechas y negros
	 *
	 * @param g2d Objeto Graphics2D
	 * @param i el índice del rectángulo
	 */
	private void DrawSmallRectaglesWithArrowsAndBlackOnes(Graphics2D g2d, int i) {
		for(int j : new int[]{0,2}){
			int[][] coord = new int[][]{
				new int[]{p,6*p+j*p},
				new int[]{6*p+j*p,13*p},
				new int[]{8*p-j*p,p},
				new int[]{13*p,8*p-j*p}
			};
			switch(j){
				case 0: SetPaint(g2d, i);break;
				case 2: g2d.setPaint(Color.BLACK); break;
			}
			Rectangle2D rectangle = new Rectangle2D.Double(coord[i][0],coord[i][1],p,p); 
			FillAndDrawBlackLine(g2d,rectangle);
			if(j==0){
				DrawArrow(g2d,coord[i][0],coord[i][1], i);
			}
		}
	}

	/**
	 * Dibuja una flecha en las coordenadas dadas, con la orientación dada
	 *
	 * @param g2d Objeto Graphics2D
	 * @param x la coordenada x de la esquina superior izquierda del cuadrado
	 * @param y la coordenada y de la esquina superior izquierda del cuadrado
	 * @param i la dirección de la flecha
	 */
	private void DrawArrow(Graphics2D g2d, int x, int y,int i) {
		Polygon pol = new Polygon();
		int a=p/5, b=p/2,c=6*p/8 ;
		switch (i){
		case 0: 
			pol.addPoint(x+a , y+a);
			pol.addPoint(x+a, y+p-a);
			pol.addPoint(x+c, y+b);
			break;
		case 1:
			pol.addPoint(x+p-a , y+p-a);
			pol.addPoint(x+a, y+p-a);
			pol.addPoint(x+b, y+p-c );
			break;
		case 2:
			pol.addPoint(x+p-a, y+a);
			pol.addPoint(x+a, y+a);
			pol.addPoint(x+b, y+c );
			break;
		case 3:
			pol.addPoint(x+p-a, y+a);
			pol.addPoint(x+p-a, y+p-a);
			pol.addPoint(x+p-c, y+b );
			break;
		}
		
		g2d.setPaint(Color.white);
		g2d.fill(pol);
	}

	/**
	 * Dibuja los pequeños rectángulos en las primeras cinco filas y columnas.
	 *
	 * @param g2d Objeto Graphics2D
	 * @param i el índice del rectángulo
	 */
	private void DrawSmallRectanglesFirstFive(Graphics2D g2d, int i) {
		for(int j = 1 ;j < 6;j++){
			int[][] coord = new int[][]{
				new int[]{j*p,7*p},
				new int[]{7*p,(j+8)*p},
				new int[]{7*p,j*p},
				new int[]{(j+8)*p,7*p}
			};
			SetPaint(g2d, i);
			Rectangle2D rectangle = new Rectangle2D.Double(coord[i][0],coord[i][1],p,p); 
			FillAndDrawBlackLine(g2d,rectangle);
		}
	}
	
	/**
	 * Se sortea el lugar de llegada de los cuatro jugadores
	 *
	 * @param g2d el objeto gráfico
	 */
	private void DrawArrivalPlace(Graphics2D g2d){
		int a,b,c,d;
		for(int i=0;i<4;i++){
			switch(i){
				case 0:a=6;b=6;c=6;d=9;break;
				case 1:a=6;b=9;c=9;d=9;break;
				case 2:a=6;b=6;c=9;d=6;break;
				case 3:a=9;b=9;c=9;d=6;break;				
				default: a=0;b=0;c=0;d=0;break;
			}
			Polygon pol = new Polygon();
			pol.addPoint(a*p, b*p);
			pol.addPoint(c*p, d*p);
			pol.addPoint(15*p/2, 15*p/2);
			SetPaint(g2d, i);
			FillAndDrawBlackLine(g2d, pol);
		}
	}
	
	/**
	 * > Dibuja los círculos en la pizarra
	 *
	 * @param g2d Objeto Graphics2D
	 */
	private void DrawCircles(Graphics2D g2d) {
		for(int i : new int[]{1,4,10,13}){
			for(int j : new int[]{1,4,10,13}){
				Ellipse2D ec = new Ellipse2D.Double(i*p-2,j*p-2, p+5,p+5);
				g2d.setPaint(Color.WHITE);	
				FillAndDrawBlackLine(g2d,ec);
			}
		}		
	}	
	
	/**
	 * Esta función dibuja las piezas del juego para cada jugador
	 *
	 * @param g2d El objeto gráfico sobre el que estamos dibujando.
	 */
	private void DrawPlayersGamePieces( Graphics2D g2d){
		if(this.pieces !=null){
			int countColor = 0;
			g2d.setStroke(new BasicStroke(2));
			for(int[][] pieces : this.pieces){
				for(int piece = 0; piece<4;piece++)
					DrawPiece(g2d, countColor, pieces, piece);
				countColor++;
			}	
		}
	}

	/**
	 * Dibuja una pieza del tablero de juego.
	 *
	 * @param g2d Objeto Graphics2D
	 * @param count el número de piezas en el tablero
	 * @param pieces La matriz de piezas.
	 * @param piece la pieza para dibujar
	 */
	private void DrawPiece(Graphics2D g2d, int count, int[][] pieces, int piece) {
		SetPiecesPaint(g2d, count);
		Ellipse2D ec = new Ellipse2D.Double(pieces[piece][0]*p,pieces[piece][1]*p, p,p);
		FillAndDrawBlackLine(g2d,ec);
		SetPaint(g2d, count);
		ec = new Ellipse2D.Double(pieces[piece][0]*p + p/6,pieces[piece][1]*p+ p/6 , p-p/3,p-p/3);
		FillAndDrawBlackLine(g2d,ec);
	}
	

	/**
	 * Toma un objeto Graphics2D y un número entero como parámetros, y establece el color del objeto Graphics2D en uno de
	 * cuatro colores dependiendo del número entero.
	 *
	 * @param g2d El objeto Graphics2D en el que estamos dibujando.
	 * @param i el índice del color actual
	 */
	private void SetPaint(Graphics2D g2d, int i) {
		switch (i){
			case 0: g2d.setPaint(new Color(220,20,60));
			break;
			case 1: g2d.setPaint(new Color(100,149,237));
			break;
			case 2: g2d.setPaint(new Color (60,179,113));
			break;
			case 3: g2d.setPaint(new Color(255,215,0));
			break;
		}
	}	
	
	/**
	 * Establece el color de la pieza a dibujar
	 *
	 * @param g2d Objeto Graphics2D
	 * @param i el índice del jugador
	 */
	private void SetPiecesPaint(Graphics2D g2d, int i) {
		switch (i){
			case 0: g2d.setPaint(new Color(178,34,34));
			break;
			case 1: g2d.setPaint(new Color(65,105,225));
			break;
			case 2: g2d.setPaint(new Color (46,139,87));
			break;
			case 3: g2d.setPaint(new Color(218,165,32));
			break;
		}
	}	
	
	/**
	 * Rellena la forma con la pintura actual, luego establece la pintura en negro y dibuja la forma.
	 *
	 * @param g2d El objeto Graphics2D que se utiliza para dibujar la forma.
	 * @param s La forma a dibujar
	 */
	private void FillAndDrawBlackLine(Graphics2D g2d, Shape s){
		g2d.fill(s);
		g2d.setPaint(Color.BLACK);
		g2d.draw(s);
	}

	@Override
	// Actualización del tablero con las nuevas posiciones de las piezas.
	public void notify(ObservadoIF observado) {
		pieces = (int[][][]) observado.get(6);
		revalidate();
		repaint();
	}
	
}
	
	