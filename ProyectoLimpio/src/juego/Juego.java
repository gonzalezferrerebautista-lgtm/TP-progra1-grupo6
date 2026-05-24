package juego;

import java.awt.Color;
import java.util.Random;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;

	// Variables y métodos propios de cada grupo
	// ...
	Personaje per;
	Fondo fon;
	Isla[][] islas;

	Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 1280, 720);

		// Inicializar lo que haga falta para el juego
		// ...
		this.per = new Personaje(entorno); // Inicializamos el personaje
		this.fon = new Fondo(); // Inicializamos el fondo
		/*
		 * this.is = new Isla( 300, 700, 200, 50); // Inicializamos la isla / las islas
		 * this.is2 = new Isla(300, 600, 300, 50); // Inicializamos la isla / las islas
		 * this.is3 = new Isla(300, 500, 400, 50); // Inicializamos la isla / las islas
		 * this.is4 = new Isla(300, 400, 500, 50); // Inicializamos la isla / las islas
		 */

		this.islas = new Isla[4][5];

		int[] tamañoIsla = { 300, 370, 440, 550 };

		for (int i = 0; i < islas.length; i++) { // Recorre los niveles
			int y = (i + 1) * 170;

			for (int j = 0; j < islas[i].length; j++) {

				double r = new Random().nextInt(54);
				r/=10;
				System.out.println(r);
				int x = (int) (300 + 700 * r); // (300, 3840) 300+700*5.2

				int a = new Random().nextInt(4);

				islas[i][j] = new Isla(x, y, tamañoIsla[a], 50);

			}
		}

		// Inicia el juego!
		this.entorno.iniciar();

	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y por lo
	 * tanto es el método más importante de esta clase. Aquí se debe actualizar el
	 * estado interno del juego para simular el paso del tiempo (ver el enunciado
	 * del TP para mayor detalle).
	 */

	public void tick() {
		// Procesamiento de un instante de tiempo

		fon.dibujar(entorno); // Dibuja el fondo
		per.dibujar(entorno); // Dibuja el personaje
		for (int i = 0; i < islas.length; i++) { // Recorre los niveles
			for (int j = 0; j < islas[i].length; j++) {
				islas[i][j].dibujar(entorno);
			}
		}
		per.caer(); // Llama a la funcion caer() del personaje que lo hace caer cuando no esta
					// tocando el piso.
		
		if (!tocoTecho(per, islas)) {
			
			per.salto(); // Llama a la funcion salto() del personaje que verifica si esta en un salto y
			// hace que se eleve.
		}
		else {
			per.saltando = false;
			per.contSaltos = 0;
		}
		per.actualizarBordes(); // Llama a la funcion actualizarBordes() del personaje que actualiza sus bordes
								// constantemente.
		tocoPiso(per, islas); // Llama a la funcion tocoPiso(per, is) que detecta si el personaje esta tocando
								// alguna isla y si es asi cambia su variable de instancia.
		arregloVisual(per, islas);
		limite(per, islas, fon);
		// Si se presiona la tecla derecha Y el personaje no toca la isla del lado
		// izquierdo (tocaIsla NO retorne 2) ===> Se mueve a la derecha +4
		if (entorno.estaPresionada(entorno.TECLA_DERECHA) && tocaIsla(per, islas) != 2 && tocaIsla(per, islas) != 3) {
			per.x += 4;
			per.dir = false; // Es falso cuando se mueve a la derecha
		}
		// Si se presiona la tecla izquierda Y el personaje no toca la isla del lado
		// derecho (tocaIsla NO retorne 1) ===> Se mueve a la izquierda -4
		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && tocaIsla(per, islas) !=1 && tocaIsla(per, islas) != 3) {
			per.x -= 4;
			per.dir = true; // Es verdadero cuando se mueve a la izquierda
		}

		// Si el personaje presiona la tecla arriba comienza el salto
		// Si el usuario mantiene el boton apretado, no se volvera a ejecutar el salto,
		// pero si este durara mas tiempo y se eleva un poco mas (intencionalmente)
		if (entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
			per.iniciarSalto(); // Llama a la funcion saltar del personaje que inicia el salto unicamente si
								// está
		}

	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}

	// La funcion tocoPiso esta hecha con el objetivo de detectar si el personaje
	// esta apoyado sobre una isla.
	// Detecta sus bordes y en el caso de que se cumplan las condiciones dadas se
	// puede decir que el personaje esta
	// tocando el piso y por ende cambia su variable de instancia estaTocandoPiso a
	// true.
	public static void tocoPiso(Personaje p, Isla[][] islas) {
		// bordes personaje
		int perAbajo = p.piso;
		int perArriba = p.techo;
		int perDerecha = p.bordeD;
		int perIzquierda = p.bordeI;

		boolean resultado = false;

		for (int i = 0; i < islas.length; i++) { // Recorre los niveles
			for (int j = 0; j < islas[i].length; j++) {
				Isla is = islas[i][j];
				// bordes isla
				int islaArriba = is.techo;
				int islaAbajo = is.piso;
				int islaIzquierda = is.bordeI;
				int islaDerecha = is.bordeD;

				// | bordeinferiorpersonaje - bordesuperiorisla | <= 1
				// && perIzquierda < islaDerecha && perDerecha > islaIzquierda

				if (Math.abs(perAbajo - islaArriba) <= 10 && perIzquierda < islaDerecha && perDerecha > islaIzquierda) {
					resultado = true;
				}
			}
		}

		p.estaTocandoPiso = resultado;

	}

	public static boolean estaTocandoIsla(Personaje p, Isla is) {
		// bordes personaje
		int perAbajo = p.piso;
		int perArriba = p.techo;
		int perDerecha = p.bordeD;
		int perIzquierda = p.bordeI;
		// bordes isla
		int islaArriba = is.techo;
		int islaAbajo = is.piso;
		int islaIzquierda = is.bordeI;
		int islaDerecha = is.bordeD;

		// | bordeinferiorpersonaje - bordesuperiorisla | <= 1
		// && perIzquierda < islaDerecha && perDerecha > islaIzquierda

		if (Math.abs(perAbajo - islaArriba) <= 10 && perIzquierda < islaDerecha && perDerecha > islaIzquierda) {
			return true;
		} else {
			return false;
		}

	}

	public static void arregloVisual(Personaje p, Isla[][] islas) {
		for (int i = 0; i < islas.length; i++) { // Recorre los niveles
			for (int j = 0; j < islas[i].length; j++) {
				// Esto evalua si el personaje esta tocando el piso y si la distancia de pixeles
				// es mayor a 2 lo acomoda mas cerca del piso para
				// evitar errores visuales
				Isla is = islas[i][j];
				if (estaTocandoIsla(p, is)) {
					// bordes personaje
					int perAbajo = p.piso;
					int perArriba = p.techo;
					int perDerecha = p.bordeD;
					int perIzquierda = p.bordeI;
					// bordes isla
					int islaArriba = is.techo;
					int islaAbajo = is.piso;
					int islaIzquierda = is.bordeI;
					int islaDerecha = is.bordeD;
					if (p.estaTocandoPiso && Math.abs(perAbajo - islaArriba) > 5) {
						p.y += 3;
					}

				
					
					// Esto evalua si el personaje esta tocando el piso y si la distancia de pixeles
					// es mayor o igual a 1 lo ubica exactamente en el piso.
					// Esto se separa del anterior bloque para que el acercamiento sea mas preciso.
					if (p.estaTocandoPiso && Math.abs(perAbajo - islaArriba) >= 2) {
						p.y += 1;
					}
				}

			}
		}
	}

	// Esta funcion devuelve un numero entre 0 y 2 dependiendo el caso.
	// La idea sería que cuando la funcion detecta que el personaje esta tocando la
	// isla por el lado derecho de la misma, retorne 1.
	// En cambio, si detecta que el personaje esta tocando la isla por el lado
	// izquierod de la misma, retorne 2.
	// Si no está tocando la isla ni por la derecha ni por la izquierda, devuelve 0.
	public static int tocaIsla(Personaje p, Isla i) {

		// bordes personaje
		int perAbajo = p.piso;
		int perArriba = p.techo;
		int perDerecha = p.bordeD;
		int perIzquierda = p.bordeI;
		// bordes isla
		int islaArriba = i.techo;
		int islaAbajo = i.piso;
		int islaIzquierda = i.bordeI;
		int islaDerecha = i.bordeD;

		if ((Math.abs(perIzquierda - islaDerecha) <= 2) && perAbajo > islaArriba && perArriba < islaAbajo) {
			return 1; // Toca la isla por el lado derecho.
		} else if (Math.abs(perDerecha - islaIzquierda) <= 2 && perAbajo > islaArriba && perArriba < islaAbajo) {
			return 2; // Toca la isla por el lado izquierdo.
		} else {
			return 0; // No toca la isla POR SUS LADOS LATERALES. (LA FUNCION NO DETECTA SI TOCA LA
						// ISLA ABAJO O ARRIBA)
		}
	}
	
	public void limite(Personaje p, Isla[][] islas, Fondo f) {
		if (p.x > this.entorno.ancho()*2/3) {
			p.x -=4;
			f.x -=3;
			p.actualizarBordes();
			for (int i = 0; i < islas.length; i++) { // Recorre los niveles
				for (int j = 0; j < islas[i].length; j++) {
					islas[i][j].x -=4;
					islas[i][j].actualizarBordes();
				}
			}
		}

	}

	public static int tocaIsla(Personaje p, Isla[][] islas) {
		// bordes personaje
		int perAbajo = p.piso;
		int perArriba = p.techo;
		int perDerecha = p.bordeD;
		int perIzquierda = p.bordeI;
		int resultado = 0;

		for (int i = 0; i < islas.length; i++) { // Recorre los niveles
			for (int j = 0; j < islas[i].length; j++) {
				Isla is = islas[i][j];
				// bordes isla
				int islaArriba = is.techo;
				int islaAbajo = is.piso;
				int islaIzquierda = is.bordeI;
				int islaDerecha = is.bordeD;

				if ((Math.abs(perIzquierda - islaDerecha) <= 2) && perAbajo > islaArriba && perArriba < islaAbajo) {
					resultado += 1; // Toca la isla por el lado derecho.
				}

				if (Math.abs(perDerecha - islaIzquierda) <= 2 && perAbajo > islaArriba && perArriba < islaAbajo) {
					resultado += 2; // Toca la isla por el lado izquierdo.
				}

				if (Math.abs(perDerecha - islaIzquierda) <= 2 && perAbajo > islaArriba && perArriba < islaAbajo) {
					resultado += 3; // Toca la isla por el lado izquierdo.
				}
			}
		}

		if (resultado >= 3) {
			return 3;
		}
		return resultado;

	}
	
	
	public static boolean tocoTecho(Personaje p, Isla[][] islas) {
		// bordes personaje
		int perAbajo = p.piso;
		int perArriba = p.techo;
		int perDerecha = p.bordeD;
		int perIzquierda = p.bordeI;

		boolean resultado = false;

		for (int i = 0; i < islas.length; i++) { // Recorre los niveles
			for (int j = 0; j < islas[i].length; j++) {
				Isla is = islas[i][j];
				// bordes isla
				int islaArriba = is.techo;
				int islaAbajo = is.piso;
				int islaIzquierda = is.bordeI;
				int islaDerecha = is.bordeD;

				// | bordeinferiorpersonaje - bordesuperiorisla | <= 1
				// && perIzquierda < islaDerecha && perDerecha > islaIzquierda

				if (Math.abs(perArriba - islaAbajo) <= 10 && perIzquierda < islaDerecha && perDerecha > islaIzquierda) {
					resultado = true;
				}
			}
		}

		return resultado;

	}

}
