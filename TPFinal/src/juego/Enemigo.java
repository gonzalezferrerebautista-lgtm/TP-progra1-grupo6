package juego;

import java.awt.Color;
import java.awt.Image;
import java.util.Random;

import entorno.Entorno;
import entorno.Herramientas;

public class Enemigo {
	private double x, y, escala;
	private Image imageI, imageD;
	private Entorno e;
	private boolean dir;
	private int[] niveles = {220*1,220*2,220*3,220*4};
	public Enemigo(Entorno e) {
		
		this.e = e;
		if (Math.random() > 0.5) {
			this.x = this.e.ancho() * 0.1;
			this.dir = true;
		}
		else {
			this.x = 1280;
			this.dir = false;
					
		}
		
		int r = new Random().nextInt(4);
		
		this.y = niveles[r];
		this.escala = 0.3;
		this.imageD = Herramientas.cargarImagen("");
		this.imageI = Herramientas.cargarImagen("");
	
	
	}
	
	public void dibujar(Entorno e) {
		/*if (this.dir) {
			//this.e.dibujarImagen(this.imageI, this.x, this.y, this.escala);
		}*/
		e.dibujarRectangulo(x, y, 75, 25, 0, Color.red);
	}
	public void mover() {
		if (this.dir) {
			this.x += 2;			
		}
		else {
			this.x -=2;
		}
			
	}
}
