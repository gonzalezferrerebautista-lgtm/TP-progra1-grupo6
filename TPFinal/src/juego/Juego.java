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
		this.islas = new Isla[4][25];
		
		generarMapa();
		
		// Se inicializan los corazones segun la cantidad de vidas del personaje.
		this.corazones = new Vidas[per.getVidas()];
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

		
		boolean golpeoTecho = false;
		for (int i = 0; i < islas.length; i++) {
		    for (int j = 0; j < islas[i].length; j++) {
		        if (islas[i][j] != null && per.chocaCabezaCon(islas[i][j])) {
		            golpeoTecho = true;
		        }
		    }
		}
		
		if (!golpeoTecho) {			
			per.salto(); // Llama a la funcion salto() del personaje que verifica si esta en un salto y
			// hace que se eleve.
		}
		else {
			per.setSaltando(false);
			per.setContSaltos(0);
		}
		per.actualizarBordes(); // Llama a la funcion actualizarBordes() del personaje que actualiza sus bordes
								// constantemente.
		caidaAlVacio(entorno, per); // Verifica si el personaje cayo al vacio y si es asi le resta una vida.
		
		per.setEstaTocandoPiso(false); // Asumimos que cae por defecto
		for (int i = 0; i < islas.length; i++) {
			for (int j = 0; j < islas[i].length; j++) {
				Isla islaActual = islas[i][j];

				if (islaActual != null && per.seApoyaEn(islaActual)) {
					per.setEstaTocandoPiso(true);
				
					per.setY(islaActual.getTecho()-per.getLargo() /2);
					per.actualizarBordes();
					
				}
		        
		    }
		}

		limite(per, islas, fon);
		
		
		boolean puedeMoverDerecha = true;
		boolean puedeMoverIzquierda = true;
		for (int i = 0; i < islas.length; i++) {
		    for (int j = 0; j < islas[i].length; j++) {
		        if (islas[i][j] != null) {
		            if (per.chocaPorDerechaCon(islas[i][j])) {
		            	puedeMoverDerecha = false;
		            }
		            if (per.chocaPorIzquierdaCon(islas[i][j])) {
		            	puedeMoverIzquierda = false;
		            }
		        }
		    }
		}
		
		if (entorno.estaPresionada(entorno.TECLA_DERECHA) && puedeMoverDerecha) {
			per.moverX(4);
			per.setDireccion(false); // Es falso cuando se mueve a la derecha
		}
		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && puedeMoverIzquierda) {
			per.moverX(-4);
			per.setDireccion(true); // Es verdadero cuando se mueve a la izquierda
		}

		// Si el personaje presiona la tecla arriba comienza el salto
		// Si el usuario mantiene el boton apretado, no se volvera a ejecutar el salto,
		// pero si este durara mas tiempo y se eleva un poco mas (intencionalmente)
		if (entorno.sePresiono(entorno.TECLA_ARRIBA)) {
			per.iniciarSalto(); // Llama a la funcion saltar del personaje que inicia el salto unicamente si
								// está
		}
		
		if (!entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
			per.cortarSalto();
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

	public static boolean estaTocandoIsla(Personaje p, Isla is) {
		// bordes personaje
		double perAbajo = p.getPiso();
		double perArriba = p.getTecho();
		double perDerecha = p.getBordeD();
		double perIzquierda = p.getBordeI();
		// bordes isla
		double islaArriba = is.getTecho();
		double islaAbajo = is.getPiso();
		double islaIzquierda = is.getBordeI();
		double islaDerecha = is.getBordeD();

		// | bordeinferiorpersonaje - bordesuperiorisla | <= 1
		// && perIzquierda < islaDerecha && perDerecha > islaIzquierda

		if (Math.abs(perAbajo - islaArriba) <= 10 && perIzquierda < islaDerecha && perDerecha > islaIzquierda) {
			return true;
		} else {
			return false;
		}

	}

	
	public void limite(Personaje p, Isla[][] islas, Fondo f) {
		double bordeDFondo = f.getX()+f.getAncho()/2;
		
		if (p.getX() > this.entorno.ancho()*2/3 && bordeDFondo > this.entorno.ancho()) {
			p.moverX(-4);
			f.moverX(-3);
			p.actualizarBordes();
			for (int i = 0; i < islas.length; i++) { // Recorre los niveles
				for (int j = 0; j < islas[i].length; j++) {
					if (islas[i][j] != null) {
						islas[i][j].moverX(-4);
						islas[i][j].actualizarBordes();
					}
				}
			}
		}
		
		if (p.getX() < 25) {
			p.moverX(4);
		}

	}
	
	// Funcion que verifica si el jugador perdio.
	public boolean perdio(Personaje p) {
		if(p.getVidas() <= 0) {
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
				if (islas[i][j] != null) {
					islas[i][j].dibujar(entorno);
				}
			}
		}
	}
	public void dibujarCorazones(Entorno entorno, Vidas[] corazones) {
		for(int i = 0; i < corazones.length; i++) {
			corazones[i].dibujar(entorno);
		}
	}
	
	public void caidaAlVacio(Entorno entorno, Personaje per) {
		if(per.getPiso() > entorno.alto()) {

		    per.perderVida();
		    boolean terminar = false;
		    for(int i = this.corazones.length-1; i >= 0 && terminar == false; i--) {
		    	if (!this.corazones[i].isRoto()) {
		    		this.corazones[i].setRoto(true);
		    		terminar = true;
		    	}
		    }
		    reinicio();
		    

		}
	}
	
	public void reinicio() {
		per.setX(200);
	    per.setY(100);

		double xFondoOriginal = this.fon.getAncho()/2;
		double diferencia = Math.abs(xFondoOriginal-this.fon.getX());
		this.fon.setX(xFondoOriginal);
		
		diferencia=diferencia*4/3;
		
		per.actualizarBordes();
		for (int i = 0; i < this.islas.length; i++) { // Recorre los niveles
			for (int j = 0; j < this.islas[i].length; j++) {
				if (islas[i][j] != null) {
					this.islas[i][j].moverX(diferencia);
					this.islas[i][j].actualizarBordes();
				}
				
			}
		}
	}
	
	public void generarMapa() {
		double grosorIsla = 45;
		// Primer piso (con separaciones fijas, fila index 3)
		double tamañoIsla = 300; // El tamaño de las islas del piso es fijo en 300
		double yPiso = 700;		// Esta en la altura 650 casi abajo de la pantalla
		double separacionPiso = 160;	// La separacion del piso es 
		double x = 200;	// La primera isla aparece en las coordenadas x del jugador
		
		for (int i = 0; i < islas[islas.length-1].length; i++) {
			islas[islas.length-1][i] = new Isla(x, yPiso, tamañoIsla, grosorIsla);
			x+= tamañoIsla+separacionPiso;
		}
		
		// Generacion de las islas flotantes
		double[] alturas = {160, 340, 520};
		int[] tamaños = {150, 220, 300};
		
		for (int i = 0; i < islas.length-1; i++) {
			double y = alturas[i];
			x = 400;
			
			for (int j = 0; j < islas[i].length; j++) {
				
				if (Math.random() > 0.3) {
					int r = new Random().nextInt(tamaños.length);
					int tamaño = tamaños[r];
					islas[i][j] = new Isla(x, y, tamaño, 45);
					
					int separacion = new Random().nextInt(100)+tamaño+110;
					x += separacion;
					
				}
				else {
					islas[i][j] = null;
					x += 250;
				}
			}
			
		}
		
		
		
	}
}
