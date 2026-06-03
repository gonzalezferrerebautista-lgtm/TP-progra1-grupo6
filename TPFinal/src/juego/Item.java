package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Item {
	private double x, y, escala;
	private String tipo;
	private Image image;
	private double radio; 
	
	public Item(double x, double y, String tipo) {
		this.radio = 10;
		this.x = x;
		this.y = y;
		if (tipo.equalsIgnoreCase("corazon")) {			
			this.tipo = "corazon";
			this.image = Herramientas.cargarImagen("corazon-obj.png");
			this.escala = 0.2;
		}
		if (tipo.equalsIgnoreCase("hongo")) {			
			this.tipo = "hongo";
			this.image = Herramientas.cargarImagen("honjo-obj.png");
			this.escala = 0.1;
		}
		
		
	}
	
	public double getX() {
		return x;
	}
	public void moverX(int x) {
		this.x += x;
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

	public double getEscala() {
		return escala;
	}

	public void setEscala(double escala) {
		this.escala = escala;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public double getRadio() {
		return radio;
	}

	public void setRadio(double radio) {
		this.radio = radio;
	}

	public void dibujar(Entorno e) {	
		e.dibujarImagen(this.image, this.x, this.y, 0, this.escala);	
	}

}
