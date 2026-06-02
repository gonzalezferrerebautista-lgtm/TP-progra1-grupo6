package juego;

import entorno.Entorno;
import entorno.Herramientas;

import java.awt.Color;
import java.awt.Image;
import java.awt.color.*;

public class Castillo {
	
	// atributos del castillo
	
	private double x,y,ancho,alto;
	private double techo, piso, bordeD, bordeI;
	private double escala;
	private Image imagenCas;
	private Entorno e;
	
	public Castillo (double x, double y, Entorno e) {
		this.e = e;
		this.x = x;
		this.y = y;
		
		this.escala = 0.8;
		this.imagenCas = Herramientas.cargarImagen("imagenCastillo.png");
		
		// El "imagenCas" se usa para saber el tamaño original de la foto
		this.ancho = imagenCas.getWidth(null) * this.escala; 					// tamaño horizontal
		this.alto = imagenCas.getHeight(null) * this.escala; 					// tamaño vertical
		
		this.ancho *= escala;
		this.alto *= escala;

		this.techo = this.y - this.alto / 2;
		this.piso = this.y + this.alto / 2 - 1;
		this.bordeD = this.x + this.ancho / 2;
		this.bordeI = this.x - this.ancho / 2;
		
	}
		
	public void dibujar (Entorno entorno) {
		// dibuja el rectángulo verde de fondo
		// entorno.dibujarRectangulo(this.x, this.y, ancho, alto, 0, Color.green);
	
		// dibuja el castillo (la imagen que le subimos)
		entorno.dibujarImagen(this.imagenCas, this.x, this.y, 0, this.escala);
		
	}
	
	// Esto fue para que el castillo no se mueva cuando se reinicie el personaje
	
	public void moverX (double dx) {
		this.x += dx;
		
	// Como el castillo cambia de posición con la cámara, sus bordes también se desplazan (por lo que los actualizamos)

		this.bordeI = this.x - this.ancho / 2;
		this.bordeD = this.x + this.ancho / 2;
	}

	// Estos métodos le permiten a la clase Juego leer las coordenadas para ganar
	public double getBordeI() {
	    return this.bordeI;
	}
	public double getBordeD() {
	    return this.bordeD;
	}

	public double getTecho() {
	    return this.techo;
	}
	public double getPiso() {
	    return this.piso;
	}
			
	public double getY() {
	    return this.y;
	}

}
	