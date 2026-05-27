package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Personaje {
	private double x;
	private double y;
	private boolean direccion;
	private double escala;
	private Image imagen1;
	private Image imagen2;
	private boolean estaTocandoPiso; 
	private boolean saltando;
	private double ancho;
	private double largo;
	private double techo;
	private double piso;
	private double bordeD;
	private double bordeI;
	private int contSaltos;
	private int vidas;
	private Entorno entorno;
	
	public Personaje(Entorno e) {
		// Inicializo todas las variables de instancia.
		this.entorno = e; // Entorno
		// Ubicacion del personaje
		this.x = 200;
		this.y = 100;
		this.direccion = false;
		// Tamaños del personaje
		this.escala = 0.2;
		this.ancho = 320*escala;
		this.largo = 470*escala;
		this.techo = this.y-this.largo/2;
		this.piso = this.y+this.largo/2-1;
		this.bordeD = this.x+this.ancho/2;
		this.bordeI = this.x-this.ancho/2;
		// Imagenes del personaje (visuales)
		this.imagen1 = Herramientas.cargarImagen("blobubu-the-monster-plush-2d-pixel-character-32x32-8-v0-c8b22imgyh8f1.gif");
		this.imagen2 = Herramientas.cargarImagen("descarga.gif");
		// Contadores y booleanos, variables, etc.
		this.estaTocandoPiso = false;
		this.saltando = false;
		this.contSaltos = 0;
		this.vidas = 5;
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
	public boolean isDireccion() {
		return direccion;
	}
	public void setDireccion(boolean direccion) {
		this.direccion = direccion;
	}
	public double getEscala() {
		return escala;
	}
	public void setEscala(double escala) {
		this.escala = escala;
	}
	public Image getImagen1() {
		return imagen1;
	}
	public void setImagen1(Image imagen1) {
		this.imagen1 = imagen1;
	}
	public Image getImagen2() {
		return imagen2;
	}
	public void setImagen2(Image imagen2) {
		this.imagen2 = imagen2;
	}
	public boolean isEstaTocandoPiso() {
		return estaTocandoPiso;
	}
	public void setEstaTocandoPiso(boolean estaTocandoPiso) {
		this.estaTocandoPiso = estaTocandoPiso;
	}
	public boolean isSaltando() {
		return saltando;
	}
	public void setSaltando(boolean saltando) {
		this.saltando = saltando;
	}
	public double getAncho() {
		return ancho;
	}
	public double getLargo() {
		return largo;
	}
	public double getTecho() {
		return techo;
	}
	public double getPiso() {
		return piso;
	}
	public double getBordeD() {
		return bordeD;
	}
	public double getBordeI() {
		return bordeI;
	}
	public int getContSaltos() {
		return contSaltos;
	}
	public void setContSaltos(int contSaltos) {
		this.contSaltos = contSaltos;
	}
	public int getVidas() {
		return vidas;
	}
	public void setVidas(int vidas) {
		this.vidas = vidas;
	}
	public Entorno getEntorno() {
		return entorno;
	}
	public void setEntorno(Entorno entorno) {
		this.entorno = entorno;
	}
	public void moverX(double x) {
		this.x += x;
		this.actualizarBordes();
	}
	public void moverY(double y) {
		this.y += y;
		this.actualizarBordes();
	}
	// Funcion que muestra al personaje (cambia segun la direccion del mismo.)
	public void dibujar(Entorno e) {
		if (!this.direccion) {
			e.dibujarImagen(this.imagen1, this.x, this.y, 0, this.escala);
		}
		else {
			e.dibujarImagen(this.imagen2, this.x, this.y, 0, this.escala);
			
		}
	}
	
	// Funcion que al ser llamada hace que el personaje aumente su variable y (o sea, visualmente cae) SOLAMENTE si no esta tocando piso
	// y no esta saltando. 
	public void caer() {
		if (!this.estaTocandoPiso && !this.saltando) {
			this.y += 10;
			this.actualizarBordes();
		}
		
		// Si el personaje cae mas alla de el entorno, vuelve por arriba. (Hecho para debuggear, con un margen de 60 para PLACER VISUAL)
		if (this.y > this.entorno.alto()+60) {
			this.y = -60;
			this.actualizarBordes();
		}
		
		
		
	}
	
	// Se ejecuta constamente para verificar el salto del usuario.
	public void salto() {
		// Verifica si el usuario está saltando y queda tiempo de salto.
		if (this.saltando == true && this.contSaltos > 0) {
			this.y -=12;			// Se eleva -10.
			this.contSaltos--;	// Resta tiempo de salto.
			this.actualizarBordes();
		}
		else {
			// Si no esta saltando, el tiempo de salto y la variable saltando se reinician por si acaso
			this.saltando = false;
			this.contSaltos = 0;
			this.actualizarBordes();
		}				
	}
	
	// Se llama cuando el usuario toca la flecha arriba.
	public void iniciarSalto() {
		// Si esta tocando piso, se inicia el salto y sus variables
		if (this.estaTocandoPiso && !this.saltando) {
			this.saltando = true;
			this.contSaltos = 17;
			this.y -= 15;
			this.actualizarBordes();
		}
		
		
	}
	
	public void cortarSalto() {
		if (this.saltando) {
			this.contSaltos = 0;
			this.saltando = false;
		}
	}

	public void actualizarBordes() {
		this.techo = (int) (this.y-this.largo/2);
		this.piso = (int) (this.y+this.largo/2);
		this.bordeD = (int) (this.x+this.ancho/2);
		this.bordeI = (int) (this.x-this.ancho/2);
		
		
	}
	
	public void perderVida() {

	    vidas--;

	}
	
	public boolean seApoyaEn(Isla is) {
		if (is == null) {
			return false;
		}
	    return Math.abs(this.piso - is.getTecho()) <= 10 && 
	           this.bordeI < is.getBordeD() && 
	           this.bordeD > is.getBordeI();
	}
	
	public boolean chocaCabezaCon(Isla is) {
		if (is == null) {
			return false;
		}
	    return Math.abs(this.techo - is.getPiso()) <= 10 && 
	           this.bordeI < is.getBordeD() && 
	           this.bordeD > is.getBordeI();
	}
	
	public boolean chocaPorDerechaCon(Isla is) {
		if (is == null) {
			return false;
		}
	    return (Math.abs(this.bordeD - is.getBordeI()) <= 2) && 
	           this.piso > is.getTecho() && 
	           this.techo < is.getPiso();
	}

	public boolean chocaPorIzquierdaCon(Isla is) {
		if (is == null) {
			return false;
		}
	    return (Math.abs(this.bordeI - is.getBordeD()) <= 2) && 
	           this.piso > is.getTecho() && 
	           this.techo < is.getPiso();
	}
}
