package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Explosion {
	private double x;
	private double y;
	private Image imagen;
	private int tiempo;                                          //Contador de vida de la explosión (en ticks del juego)
	
	public Explosion(double x, double y) {
	    this.x = x;
	    this.y = y;

	    imagen = Herramientas.cargarImagen("explosion.png");

	    tiempo = 15;
	}
	
	public void dibujar(Entorno entorno) {
	    entorno.dibujarImagen(imagen, x, y, 0, 0.1);
	}
	
	public void actualizar() {           //nos sirve para contar los ticks  (Cada tick) reduce el contador
	    tiempo--;
	}
	
	public boolean terminada() {            //nos sirve para saber cuando desaparece la explosion, Cuando el contador llega a 0, el juego debe eliminarla
	    return tiempo <= 0;
	}
}
