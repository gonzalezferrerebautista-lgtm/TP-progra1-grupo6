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
	Vidas[] corazones;
	Fondo fon;
	Isla[][] islas;
	Enemigo[][] enemigos;
	

	Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 1280, 720);

		// Inicializar lo que haga falta para el juego
		this.per = new Personaje(entorno); 					// Inicializamos el personaje
		this.fon = new Fondo(entorno); 						// Inicializamos el fondo
		this.islas = new Isla[4][6];
		int[] tamañoIsla = { 200, 300, 400, 500 };
		for (int i = 0; i < islas.length; i++) { 			// Recorre los niveles
			double y = (i + 1) * 170;
			for (int j = 0; j < islas[i].length; j++) { 	// Recorre la cantidad de islas
				
				double base = 300;							// Limite menor de la coordenada x.
				double separacion = 700;					// Separacion promedio entre las islas
	
				// Genera un numero aleatorio entre 0 y la cantidad de islas que entran en el fondo * 10.
				// Ejemplo: Si el fondo es de 3000-300 y la separacion de 700, entran 3,85 islas. *100 = 385.
				// entonces genera un numero entre 0 y 385.
				double r = new Random().nextInt((int) ((this.fon.ancho-base)/separacion*100));
				
				// Divide el numero entre 100. La idea es que el numero aleatorio este entre 0 y 3,85 siguiendo el caso anterior.
				r/=100;
				
				// Determina la coordenada x de la isla, que va a estar entre 300 y el ancho del fondo.
				// La base es +300, y la variable es separacion * r que puede ser un numero entre 0 y el ancho
				// del fondo, de manera aleatoria.
				// NO SE PUEDE PASAR DEL ANCHO DE LA ISLA porque el calculo de r lo limita. En el caso mas extremo, 
				// estará en el borde
				double x = (300 + separacion * r); // [300, 3000] 300+700*x con  0 < x < 3,85 .  300+700*3,85 = 2995
				
				int a = new Random().nextInt(4);	// Numero aleatorio que determina el tamaño de la isla, 0 mas chico, 3 mas grande.

				// Si la distancia entre el personaje y la isla es menor a 300, y la isla es de las capas superiores,
				// la isla es movida automaticamente por la separacion. Esto se debe a que ya hay una isla
				// ubicada en ese lugar creada intencionalmente.
				if (Math.abs(x-this.per.x) < 300 && (i == 0||i == 1)) {
					x+=separacion;
				}
				
				islas[i][j] =  new Isla(x, y, tamañoIsla[a], 50);	// Se invoca a la isla.
				
				// Si la isla es la primera en la segunda capa, se genera justo en la coordenada x del personaje.
				if (j == 0 && i == 1) {
					islas[i][j] =  new Isla(this.per.x, y, tamañoIsla[1], 50);
				}
				

			}
		}
		
		// Se inicializan los corazones segun la cantidad de vidas del personaje.
		this.corazones = new Vidas[per.vidas];
		for(int i = 0; i < corazones.length; i++) {
			corazones[i] = new Vidas(40 + i * 50, 40);
		}
		
		/*this.enemigos = new Enemigo[4][6];
		for (int i = 0; i < enemigos.length; i++) { // Recorre los niveles		
			for (int j = 0; j < enemigos[i].length; j++) {
				enemigos[i][j] = new Enemigo(entorno);
			}
		}*/

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
		if (perdio(per)) {
			return;
		}
		
		fon.dibujar(entorno); // Dibuja el fondo
		per.dibujar(entorno); // Dibuja el personaje
		dibujarIslas(entorno, islas);
		/*for (int i = 0; i < enemigos.length; i++) { // Recorre los niveles
			for (int j = 0; j < enemigos[i].length; j++) {
				enemigos[i][j].dibujar(entorno);
				enemigos[i][j].mover();
			}
		}*/
		dibujarCorazones(entorno, corazones);
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
		caidaAlVacio(entorno, per); // Verifica si el personaje cayo al vacio y si es asi le resta una vida.
		
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
		double perAbajo = p.piso;
		double perArriba = p.techo;
		double perDerecha = p.bordeD;
		double perIzquierda = p.bordeI;

		boolean resultado = false;

		for (int i = 0; i < islas.length; i++) { // Recorre los niveles
			for (int j = 0; j < islas[i].length; j++) {
				Isla is = islas[i][j];
				// bordes isla
				double islaArriba = is.techo;
				double islaAbajo = is.piso;
				double islaIzquierda = is.bordeI;
				double islaDerecha = is.bordeD;

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
		double perAbajo = p.piso;
		double perArriba = p.techo;
		double perDerecha = p.bordeD;
		double perIzquierda = p.bordeI;
		// bordes isla
		double islaArriba = is.techo;
		double islaAbajo = is.piso;
		double islaIzquierda = is.bordeI;
		double islaDerecha = is.bordeD;

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
					double perAbajo = p.piso;
					double perArriba = p.techo;
					double perDerecha = p.bordeD;
					double perIzquierda = p.bordeI;
					// bordes isla
					double islaArriba = is.techo;
					double islaAbajo = is.piso;
					double islaIzquierda = is.bordeI;
					double islaDerecha = is.bordeD;
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
		double perAbajo = p.piso;
		double perArriba = p.techo;
		double perDerecha = p.bordeD;
		double perIzquierda = p.bordeI;
		// bordes isla
		double islaArriba = i.techo;
		double islaAbajo = i.piso;
		double islaIzquierda = i.bordeI;
		double islaDerecha = i.bordeD;
		
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
		double bordeDFondo = f.x+f.ancho/2;
		
		if (p.x > this.entorno.ancho()*2/3 && bordeDFondo > this.entorno.ancho()) {
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
		
		if (p.x < 25) {
			p.x +=4;
		}

	}

	public static int tocaIsla(Personaje p, Isla[][] islas) {
		// bordes personaje
		double perAbajo = p.piso;
		double perArriba = p.techo;
		double perDerecha = p.bordeD;
		double perIzquierda = p.bordeI;
		int resultado = 0;

		for (int i = 0; i < islas.length; i++) { // Recorre los niveles
			for (int j = 0; j < islas[i].length; j++) {
				Isla is = islas[i][j];
				// bordes isla
				double islaArriba = is.techo;
				double islaAbajo = is.piso;
				double islaIzquierda = is.bordeI;
				double islaDerecha = is.bordeD;

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
		double perAbajo = p.piso;
		double perArriba = p.techo;
		double perDerecha = p.bordeD;
		double perIzquierda = p.bordeI;

		boolean resultado = false;

		for (int i = 0; i < islas.length; i++) { // Recorre los niveles
			for (int j = 0; j < islas[i].length; j++) {
				Isla is = islas[i][j];
				// bordes isla
				double islaArriba = is.techo;
				double islaAbajo = is.piso;
				double islaIzquierda = is.bordeI;
				double islaDerecha = is.bordeD;

				// | bordeinferiorpersonaje - bordesuperiorisla | <= 1
				// && perIzquierda < islaDerecha && perDerecha > islaIzquierda

				if (Math.abs(perArriba - islaAbajo) <= 10 && perIzquierda < islaDerecha && perDerecha > islaIzquierda) {
					resultado = true;
				}
			}
		}

		return resultado;

	}

	// Funcion que verifica si el jugador perdio.
	public boolean perdio(Personaje p) {
		if(p.vidas <= 0) {
			int tamañoFuente = 70;
			entorno.cambiarFont("Arial", tamañoFuente, Color.white);
			entorno.escribirTexto("PERDISTE", entorno.ancho()/2-tamañoFuente*2.8, entorno.alto()/2);
			return true;
		}
		return false;
	}
	
	public void dibujarIslas(Entorno entorno, Isla[][] islas) {
		for (int i = 0; i < islas.length; i++) { // Recorre los niveles
			for (int j = 0; j < islas[i].length; j++) { // Recorre la cantidad de islas por nivel
				islas[i][j].dibujar(entorno);
			}
		}
	}
	public void dibujarCorazones(Entorno entorno, Vidas[] corazones) {
		for(int i = 0; i < corazones.length; i++) {
			corazones[i].dibujar(entorno);
		}
	}
	
	public void caidaAlVacio(Entorno entorno, Personaje per) {
		if(per.piso > entorno.alto()) {

		    per.vidas--;
		    boolean terminar = false;
		    for(int i = this.corazones.length-1; i >= 0 && terminar == false; i--) {
		    	if (!this.corazones[i].roto) {
		    		this.corazones[i].roto = true;
		    		terminar = true;
		    	}
		    }
		    reinicio();
		    

		}
	}
	
	public void reinicio() {
		per.x = 200;
	    per.y = 100;

		double xFondoOriginal = this.fon.ancho/2;
		double diferencia = Math.abs(xFondoOriginal-this.fon.x);
		this.fon.x = xFondoOriginal;
		
		diferencia=diferencia*4/3;
		
		per.actualizarBordes();
		for (int i = 0; i < this.islas.length; i++) { // Recorre los niveles
			for (int j = 0; j < this.islas[i].length; j++) {
				this.islas[i][j].x +=diferencia;
				this.islas[i][j].actualizarBordes();
			}
		}
	}
}
