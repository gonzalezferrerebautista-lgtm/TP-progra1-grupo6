package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Isla {
	private double x;
	private double y;
	private double ancho;
	private double largo;
	private double tamaño;
	private double escala;
	private Color color;
	private double techo;
	private double piso;
	private double bordeD;
	private double bordeI;
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

	public double getLargo() {
		return largo;
	}

	public void setLargo(double largo) {
		this.largo = largo;
	}

	public double getEscala() {
		return escala;
	}

	public void setEscala(double escala) {
		this.escala = escala;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public double getTecho() {
		return techo;
	}

	public void setTecho(double techo) {
		this.techo = techo;
	}

	public double getPiso() {
		return piso;
	}

	public void setPiso(double piso) {
		this.piso = piso;
	}

	public double getBordeD() {
		return bordeD;
	}

	public void setBordeD(double bordeD) {
		this.bordeD = bordeD;
	}

	public double getBordeI() {
		return bordeI;
	}

	public void setBordeI(double bordeI) {
		this.bordeI = bordeI;
	}
	public void moverX(double x) {
		this.x += x;
	}
	public void moverY(double y) {
		this.y += y;
	}

	public Isla(double x, double y, double tamañoIsla) {
		this.x =x;
		this.y =y;
		this.escala= 0.2;
		this.tamaño = tamañoIsla;
		if (this.tamaño == 300) {
			this.imagen = Herramientas.cargarImagen("isla300.png");
		}
		else if (this.tamaño == 220) {
			this.imagen = Herramientas.cargarImagen("isla220.png");
		}
		else if (this.tamaño == 150) {
			this.imagen = Herramientas.cargarImagen("isla150.png");
		}
		else if (this.tamaño == 500) {
			this.imagen = Herramientas.cargarImagen("islafinal.png");
		}
		else {
			this.imagen = Herramientas.cargarImagen("isla150.png");
		}
		this.ancho = this.imagen.getWidth(null);
		this.largo = this.imagen.getHeight(null);
		this.ancho *= escala;
		this.largo *= escala;
		this.color = new Color(64, 30, 9);
		this.techo = this.y-this.largo/2+4;
		this.piso = this.y+this.largo/2-15;
		this.bordeD = this.x+this.ancho/2-5;
		this.bordeI = this.x-this.ancho/2+5;
		
	}
	
	public void dibujar(Entorno e) {
		e.dibujarImagen(imagen, x, y, 0, this.escala);
	}
	
	public void actualizarBordes() {
		this.techo = this.y-this.largo/2+4;
		this.piso = this.y+this.largo/2-15;
		this.bordeD = this.x+this.ancho/2-5;
		this.bordeI = this.x-this.ancho/2+5;
		
		
	}

}
