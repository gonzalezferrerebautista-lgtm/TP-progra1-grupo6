package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Fondo {
	private double x;
	private double y;
	private double ancho;
	private double alto;
	private double escala;
	private double bordeD;
	private Image imagen;
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getAncho() {
		return ancho;
	}
	public void setAncho(double ancho) {
		this.ancho = ancho;
	}
	public double getAlto() {
		return alto;
	}
	public void setAlto(double alto) {
		this.alto = alto;
	}
	public double getEscala() {
		return escala;
	}
	public void setEscala(double escala) {
		this.escala = escala;
	}
	public double getBordeD() {
		return bordeD;
	}
	public void setBordeD(double bordeD) {
		this.bordeD = bordeD;
	}

	public void moverX(double x) {
		this.x += x;
	}
	public void moverY(double y) {
		this.y += y;
	}
	public Fondo(Entorno e) {
		this.imagen = Herramientas.cargarImagen("fondo.png");
		this.ancho = imagen.getWidth(null);
		this.alto = imagen.getHeight(null);
		this.x = this.ancho/2;
		this.y = this.alto/2-100;
		
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
