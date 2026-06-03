package juego;

import java.awt.Color;

import entorno.Entorno;

public class Puntaje {
	private int valor;
	private Entorno entorno;
	private double x;
	private double y;
	private int tamañoFuente;
	private int multiplicador;
	private int tiempo;
	private int tiempoMuerte;
	private int racha;
	private int mejorRacha;
	private int kills;
	private int modificadorPuntos;
	private int modificadorPuntosNegativo;
	
	public Puntaje(double x, double y, Entorno e) {
		this.valor = 0;
		this.tamañoFuente = 24;
		this.x = x;
		this.y = y;
		this.multiplicador = 0;
		this.tiempo = 0;
		this.tiempoMuerte = 0;
		this.racha = 0;
		this.mejorRacha = 0;
		this.kills = 0;
		this.entorno = e;
		this.modificadorPuntos = 0;
		this.modificadorPuntosNegativo = 0;
	}
	public Puntaje(double x, double y, Entorno e, int tamañoFuente) {
		this.valor = 0;
		this.tamañoFuente = tamañoFuente;
		this.x = x;
		this.y = y;
		this.multiplicador = 0;
		this.tiempo = 0;
		this.tiempoMuerte = 0;
		this.racha = 0;
		this.mejorRacha = 0;
		this.kills = 0;
		this.entorno = e;
		this.modificadorPuntos = 0;
		this.modificadorPuntosNegativo = 0;
	}
	
	
	public void sumar(int puntos) {
		this.valor+=puntos;
	}
	public void restar(int puntos) {
		this.valor-=puntos;
	}
	public void kill() {
		int mod = 50+10*this.multiplicador;
		this.tiempoMuerte = 0;
		this.modificadorPuntosNegativo = 0;
		this.valor+=mod;
		this.multiplicador+=1;
		this.tiempo = 65*2;
		this.racha +=1;
		if (this.racha > this.mejorRacha) {
			this.mejorRacha = this.racha;
		}
		this.kills +=1;
		this.modificadorPuntos+=mod;
	}
	public void pierdeVida() {
		this.tiempo = 0;
		this.valor -= 250;
		this.tiempoMuerte = 65*2;
		this.modificadorPuntosNegativo = 250;
		
	}
	public void resetMultiplicador() {
		
		
		if (this.tiempo <= 0) {
			this.tiempo = 0;
			this.multiplicador = 0;
			if (this.racha > this.mejorRacha) {
				this.mejorRacha = this.racha;
			}
			this.racha = 0;
			this.modificadorPuntos = 0;
			return;
		}
		this.tiempo -=1;
	}
	
	public void mostrarStats(double x, double y, Entorno e) {
		mostrarStats(x, y, "Arial", e);
	}
	public void mostrarStats(double x, double y, String fuente, Entorno e) {
		Color sombra = new Color(0, 0, 0, 150);
		e.cambiarFont(fuente, this.tamañoFuente+4, sombra);
		e.escribirTexto("PUNTOS: " + this.valor, x+2, y+100);
		e.escribirTexto("KILLS: " + this.kills, x+2, y+130);
		e.escribirTexto("MEJOR RACHA: " + this.mejorRacha, x+2, y+160);
		
		e.cambiarFont(fuente, this.tamañoFuente+4, Color.white);
		e.escribirTexto("PUNTOS: " + this.valor, x, y+100);
		e.escribirTexto("KILLS: " + this.kills, x, y+130);
		e.escribirTexto("MEJOR RACHA: " + this.mejorRacha, x, y+160);
	}
	
	public void dibujar(Entorno entorno) {
		
		Color sombra = new Color(0, 0, 0, 125);
		Color verdeClaro = new Color(220, 242, 225, 255);
		Color rojoClaro = new Color(227, 134, 134, 255);
		entorno.cambiarFont("Franklin Gothic Medium", this.tamañoFuente, sombra);
		entorno.escribirTexto("PUNTOS: " + this.valor, x+2, y+1);
		entorno.cambiarFont("Franklin Gothic Medium", this.tamañoFuente, Color.WHITE);
		entorno.escribirTexto("PUNTOS: " + this.valor, x, y);
		if (this.multiplicador != 0) {
			entorno.cambiarFont("Franklin Gothic Medium", this.tamañoFuente, sombra);
			entorno.escribirTexto("+" + this.modificadorPuntos, x+5+2, y+26);
			entorno.escribirTexto("(X" + this.racha + ")", x+80+2, y+26);
			entorno.cambiarFont("Franklin Gothic Medium", this.tamañoFuente, verdeClaro);
			entorno.escribirTexto("+" + this.modificadorPuntos, x+5, y+25);
			entorno.cambiarFont("Franklin Gothic Medium", this.tamañoFuente, Color.WHITE);
			entorno.escribirTexto("(X" + this.racha + ")", x+80, y+25);
		}
		if (this.tiempoMuerte > 0) {
			entorno.cambiarFont("Franklin Gothic Medium", this.tamañoFuente, sombra);
			entorno.escribirTexto("-" + this.modificadorPuntosNegativo, x+5+2, y+26);
			entorno.cambiarFont("Franklin Gothic Medium", this.tamañoFuente, rojoClaro);
			entorno.escribirTexto("-" + this.modificadorPuntosNegativo, x+5, y+25);
			this.tiempoMuerte--;
			if (this.tiempoMuerte <= 0) {
				this.modificadorPuntosNegativo = 0;
			}
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
