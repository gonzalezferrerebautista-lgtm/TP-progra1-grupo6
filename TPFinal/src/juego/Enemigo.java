package juego;

import java.awt.Color;
import java.awt.Image;
import java.util.Random;

import entorno.Entorno;
import entorno.Herramientas;

public class Enemigo {
	private double x, y, ancho, largo, escala, velocidad; 
	private double techo, piso, bordeD, bordeI;
	private Image imageI, imageD, imageIdañado, imageDdañado;
	private Entorno e;
	private boolean direccion, muerto, muriendo;
	private double tiempoMuerte;
	public Enemigo(double x, double y, Entorno e) {
		this.e = e;
		
		this.x = x;
		this.y = y;
		
		this.escala = 0.6;
		this.imageD = Herramientas.cargarImagen("dragon.gif");
		this.imageI = Herramientas.cargarImagen("dragon-rotate.gif");
		this.imageDdañado = Herramientas.cargarImagen("dragon-dañado.gif");
		this.imageIdañado = Herramientas.cargarImagen("dragon-dañado-rotate.gif");
	
		this.ancho = imageD.getWidth(null);
		this.largo = imageD.getHeight(null);
		this.ancho *= escala;
		this.largo *= escala;

		this.techo = this.y-this.largo/2;
		this.piso = this.y+this.largo/2-1;
		this.bordeD = this.x+this.ancho/2;
		this.bordeI = this.x-this.ancho/2;
		this.direccion = true;
		this.muriendo = false;
		this.muerto = false;
	
	}
	public Enemigo(double x, double y, Entorno e, boolean dir) {
		this.e = e;
		
		this.x = x;
		this.y = y;
		
		this.escala = 0.6;
		this.imageD = Herramientas.cargarImagen("dragon.gif");
		this.imageI = Herramientas.cargarImagen("dragon-rotate.gif");
		this.imageDdañado = Herramientas.cargarImagen("dragon-dañado.gif");
		this.imageIdañado = Herramientas.cargarImagen("dragon-dañado-rotate.gif");
		
		this.ancho = imageD.getWidth(null);
		this.largo = imageD.getHeight(null);
		this.ancho *= escala;
		this.largo *= escala;

		this.techo = this.y-this.largo/2;
		this.piso = this.y+this.largo/2-1;
		this.bordeD = this.x+this.ancho/2;
		this.bordeI = this.x-this.ancho/2;
		this.direccion = dir;
		this.muriendo = false;
		this.muerto = false;
		
	
	}


	public void dibujar(Entorno e) {
		
		if (!this.isMuriendo()) {
			if (!this.direccion) {	
				this.e.dibujarImagen(this.imageI, this.x, this.y, 0, this.escala);
			}
			else {	
				this.e.dibujarImagen(this.imageD, this.x, this.y, 0, this.escala);
			}

		} 
		else {
			if (!this.direccion) {	
				this.e.dibujarImagen(this.imageIdañado, this.x, this.y, Math.toRadians(this.tiempoMuerte*-9+180), this.escala);
			}
			else {	
				this.e.dibujarImagen(this.imageDdañado, this.x, this.y, Math.toRadians(this.tiempoMuerte*-9+180), this.escala);
			}

		}
		
		
	}
	public void mover() {
		if (this.direccion) {
			this.x += 2;			
		}
		else {
			this.x -=2;
		}
		actualizarBordes();
			
	}
	public boolean fueraDePantalla(Entorno entorno) {
	    return x < 0-80 ||
	           x > entorno.ancho()+120 ||
	           y < 0-120 ||                       
	           y > entorno.alto()+120;
	}
	
	public void morir() {
		if (!muriendo) {
			this.muriendo = true;
			this.tiempoMuerte = 20;
		}
		else {
			this.muriendo = false;
			this.tiempoMuerte = 0;
			this.muerto = true;
		}
	}
	
	public void estaMuriendo() {
		if (this.muriendo && this.tiempoMuerte > 0) {							
			this.tiempoMuerte-=1;
			moverY(30);
			return;
		}
		if (this.muriendo && this.tiempoMuerte < 1) {
			this.muriendo = false;
			this.muerto = true;
			this.tiempoMuerte = 0;
			return;
		}
		
	}
	public boolean colisionaCon(Isla isla) {
	    if (isla == null) {
	        return false;
	    }

	    return this.bordeD > isla.getBordeI()
	        && this.bordeI < isla.getBordeD()                 
	        && this.piso > isla.getTecho()
	        && this.techo < isla.getPiso();
	}
	
	public void actualizarBordes() {
		this.techo = (int) (this.y-this.largo/2);
		this.piso = (int) (this.y+this.largo/2);
		this.bordeD = (int) (this.x+this.ancho/2);
		this.bordeI = (int) (this.x-this.ancho/2);
		
		
	}
	

	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
		this.actualizarBordes();
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
		this.actualizarBordes();
	}
	public void moverX(double x) {
		this.x += x;
		this.actualizarBordes();
	}
	public void moverY(double y) {
		this.y += y;
		this.actualizarBordes();
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
	public double getVelocidad() {
		return velocidad;
	}
	public void setVelocidad(double velocidad) {
		this.velocidad = velocidad;
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
	public Image getImageI() {
		return imageI;
	}
	public void setImageI(Image imageI) {
		this.imageI = imageI;
	}
	public Image getImageD() {
		return imageD;
	}
	public void setImageD(Image imageD) {
		this.imageD = imageD;
	}
	public Entorno getE() {
		return e;
	}
	public void setE(Entorno e) {
		this.e = e;
	}
	public boolean isDireccion() {
		return direccion;
	}
	public void setDireccion(boolean direccion) {
		this.direccion = direccion;
	}
	public boolean isMuerto() {
		return muerto;
	}
	public void setMuerto(boolean muerto) {
		this.muerto = muerto;
	}
	public boolean isMuriendo() {
		return muriendo;
	}
	public void setMuriendo(boolean muriendo) {
		this.muriendo = muriendo;
	}
	public double getTiempoMuerte() {
		return tiempoMuerte;
	}
	public void setTiempoMuerte(double tiempoMuerte) {
		this.tiempoMuerte = tiempoMuerte;
	}
	public void subTiempoMuerte(double tiempoMuerte) {
		this.tiempoMuerte -= tiempoMuerte;
	}
	
}
