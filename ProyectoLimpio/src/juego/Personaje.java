package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Personaje {
	double x;
	double y;
	boolean dir;
	double escala;
	Image imagen1;
	Image imagen2;
	boolean estaTocandoPiso; 
	boolean saltando;
	int ancho;
	int largo;
	int techo;
	int piso;
	int bordeD;
	int bordeI;
	int contSaltos;
	Entorno e;
	
	public Personaje(Entorno e) {
		// Inicializo todas las variables de instancia.
		this.e = e; // Entorno
		// Ubicacion del personaje
		this.x = 200;
		this.y = 100;
		this.dir = false;
		// Tamaños del personaje
		this.escala = 0.2;
		this.ancho = (int) (320*escala);
		this.largo = (int) (470*escala);
		this.techo = (int) (this.y-this.largo/2);
		this.piso = (int) (this.y+this.largo/2)-1;
		this.bordeD = (int) (this.x+this.ancho/2);
		this.bordeI = (int) (this.x-this.ancho/2);
		// Imagenes del personaje (visuales)
		this.imagen1 = Herramientas.cargarImagen("blobubu-the-monster-plush-2d-pixel-character-32x32-8-v0-c8b22imgyh8f1.gif");
		this.imagen2 = Herramientas.cargarImagen("descarga.gif");
		// Contadores y booleanos, variables, etc.
		this.estaTocandoPiso = false;
		this.saltando = false;
		this.contSaltos = 0;
	}
	// Funcion que muestra al personaje (cambia segun la direccion del mismo.)
	public void dibujar(Entorno e) {
		if (!dir) {
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
		}
		
		// Si el personaje cae mas alla de el entorno, vuelve por arriba. (Hecho para debuggear, con un margen de 60 para PLACER VISUAL)
		if (this.y > e.alto()+60) {
			this.y = -60;
		}
		
		
		
	}
	
	// Se ejecuta constamente para verificar el salto del usuario.
	public void salto() {
		// Verifica si el usuario está saltando y queda tiempo de salto.
		if (this.saltando == true && this.contSaltos > 0) {
			this.y -=10;			// Se eleva -10.
			this.contSaltos -=8;	// Resta tiempo de salto.
		}
		else {
			// Si no esta saltando, el tiempo de salto y la variable saltando se reinician por si acaso
			this.saltando = false;
			this.contSaltos = 0;
		}				
	}
	
	// Se llama cuando el usuario toca la flecha arriba.
	public void iniciarSalto() {
		// Si esta tocando piso, se inicia el salto y sus variables
		if (this.estaTocandoPiso && this.saltando == false && this.contSaltos == 0) {
			this.saltando = true;
			this.contSaltos = 100;
			this.y -=20;		// Esto es un boost inicial.
		}
		
		// Si ya esta saltando y su contador de saltos es mayor a 0, el contador de saltos se incrementa un poco
		// para dar la sensacion de que el salto dura mas manteniendo apretado el boton.
		if (this.saltando == true && this.contSaltos > 0) {
			this.contSaltos +=3;
		}
	}

	public void actualizarBordes() {
		this.techo = (int) (this.y-this.largo/2);
		this.piso = (int) (this.y+this.largo/2);
		this.bordeD = (int) (this.x+this.ancho/2);
		this.bordeI = (int) (this.x-this.ancho/2);
		
		
	}
}
