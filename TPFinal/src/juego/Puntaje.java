package juego;

import java.awt.Color;

import entorno.Entorno;

public class Puntaje {
	private int valor;
	private double x;
	private double y;
	private int tamañoFuente;
	private double multiplicador;
	private int tiempo;
	private int racha;
	private int mejorRacha;
	private int kills;
	
	public Puntaje(double x, double y) {
		this.valor = 0;
		this.tamañoFuente = 24;
		this.x = x;
		this.y = y;
		this.multiplicador = 0;
		this.tiempo = 0;
		this.racha = 0;
		this.mejorRacha = 0;
		this.kills = 0;
		
	}
	public Puntaje(double x, double y, int tamañoFuente) {
		this.valor = 0;
		this.tamañoFuente = tamañoFuente;
		this.x = x;
		this.y = y;
		this.multiplicador = 0;
		this.tiempo = 0;
		this.racha = 0;
		this.mejorRacha = 0;
		this.kills = 0;
	}
	
	
	public void sumar(int puntos) {
		this.valor+=puntos;
	}
	public void restar(int puntos) {
		this.valor-=puntos;
	}
	public void kill() {
		this.valor+=50+10*this.multiplicador;
		this.multiplicador+=1;
		this.tiempo = 65*2;
		this.racha +=1;
		if (this.racha > this.mejorRacha) {
			this.mejorRacha = this.racha;
		}
		this.kills +=1;
	}
	public void pierdeVida() {
		this.tiempo = 0;
		this.valor -= 250;
		
	}
	public void resetMultiplicador() {
		
		
		if (this.tiempo <= 0) {
			this.tiempo = 0;
			this.multiplicador = 0;
			if (this.racha > this.mejorRacha) {
				this.mejorRacha = this.racha;
			}
			this.racha = 0;
			
			return;
		}
		this.tiempo -=1;
	}
	
	public void mostrarStats(double x, double y, Entorno e) {
		mostrarStats(x, y, "Arial", e);
	}
	public void mostrarStats(double x, double y, String fuente, Entorno e) {
		e.cambiarFont(fuente, this.tamañoFuente, Color.white);
		y += 100;
		e.escribirTexto("PUNTOS: " + this.valor, x, y);
		y += 30;
		e.escribirTexto("KILLS: " + this.kills, x, y);
		y += 30;
		e.escribirTexto("MEJOR RACHA: " + this.mejorRacha, x, y);
	}
	
	public void dibujar(Entorno entorno) {
		
		Color sombra = new Color(0, 0, 0, 125);
		entorno.cambiarFont("Franklin Gothic Medium", this.tamañoFuente, sombra);
		entorno.escribirTexto("PUNTOS: " + this.valor, x+2, y+1);
		entorno.cambiarFont("Franklin Gothic Medium", this.tamañoFuente, Color.WHITE);
		entorno.escribirTexto("PUNTOS: " + this.valor, x, y);
		if (this.multiplicador != 0) {
			entorno.cambiarFont("Franklin Gothic Medium", this.tamañoFuente, sombra);
			entorno.escribirTexto("MULTIPLICADOR X" + this.racha, x-34+2, y+26);
			entorno.cambiarFont("Franklin Gothic Medium", this.tamañoFuente, Color.WHITE);
			entorno.escribirTexto("MULTIPLICADOR X" + this.racha, x-34, y+25);
		}
	}
	
	public double getX() {
		return this.x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return this.y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public int getValor() {
		return this.valor;
	}
	public void setValor(int valor) {
		this.valor = valor;
	}
}
