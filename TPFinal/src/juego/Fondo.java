package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Fondo {
	double x;
	double y;
	double ancho;
	double alto;
	double escala;
	double bordeD;
	Image imagen;
	
	public Fondo(Entorno e) {
		this.imagen = Herramientas.cargarImagen("fondo.png");
		this.ancho = imagen.getWidth(null);
		this.alto = imagen.getHeight(null);
		this.x = this.ancho/2;
		this.y = this.alto/2-100;
		
		System.out.println(e.alto());
		System.out.println(this.alto);
		System.out.println(e.alto()/this.alto);
		
		this.escala = e.alto()/this.alto*1.2;
		
		this.ancho = imagen.getWidth(null)*escala;
		this.alto = imagen.getHeight(null)*escala;
		this.x = this.ancho/2;
		this.y = this.alto/2-100;
		
		
	}
	public void dibujar(Entorno e) {
		e.dibujarImagen(this.imagen, this.x, this.y, 0, this.escala);
	}
}
