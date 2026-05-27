package juego;

import java.awt.Color;

import entorno.Entorno;

public class Isla {
	private double x;
	private double y;
	private double ancho;
	private double largo;
	private double escala;
	private Color color;
	private double techo;
	private double piso;
	private double bordeD;
	private double bordeI;
	
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

	public Isla(double x, double y, double ancho, double largo) {
		this.x =x;
		this.y =y;
		this.ancho= ancho;
		this.largo = largo;
		this.escala= 1;
		this.color = new Color(64, 30, 9);
		this.techo = this.y-this.largo/2;
		this.piso = this.y+this.largo/2;
		this.bordeD = this.x+this.ancho/2;
		this.bordeI = this.x-this.ancho/2;
		
	}
	
	public void dibujar(Entorno e) {
		e.dibujarRectangulo(x, y, ancho, largo, 0, color);
	}
	
	public void actualizarBordes() {
		this.techo = (int) (this.y-this.largo/2);
		this.piso = (int) (this.y+this.largo/2);
		this.bordeD = (int) (this.x+this.ancho/2);
		this.bordeI = (int) (this.x-this.ancho/2);
		
		
	}

}
