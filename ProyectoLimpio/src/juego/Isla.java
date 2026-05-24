package juego;

import java.awt.Color;

import entorno.Entorno;

public class Isla {
	int x;
	int y;
	int ancho;
	int largo;
	int escala;
	Color color;
	int techo;
	int piso;
	int bordeD;
	int bordeI;
	
	public Isla(int x, int y, int ancho, int largo) {
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
