package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Fondo {
	double x;
	double y;
	double escala;
	Image imagen;
	
	public Fondo() {
		this.x = 3840/2;
		this.y = 883/2-100;
		this.escala = 1;
		this.imagen = Herramientas.cargarImagen("fondo.png");
	}
	public void dibujar(Entorno e) {
		e.dibujarImagen(this.imagen, this.x, this.y, 0, this.escala);
	}
}
