package juego;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.util.Random;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;

	// Variables y métodos propios de cada grupo
	// ...
	Personaje per;
	Vidas[] corazones;
	Proyectil proyectil;
	Explosion explosion;
	Fondo fon;
	Isla[][] islas;
	Enemigo[][] enemigos;
	Castillo castillo;	
	Puntaje puntaje;
	Item[] items;

	Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 1280, 720);

		// Inicializar lo que haga falta para el juego
		this.per = new Personaje(entorno, 5); 					// Inicializamos el personaje
		this.fon = new Fondo(entorno); 						// Inicializamos el fondo
		int niveles = 4;
		int cantIslas = 24;
		this.islas = new Isla[niveles][cantIslas];						// ...
		this.enemigos = new Enemigo[4][3];					// ...
		this.puntaje = new Puntaje(entorno.ancho() - 200, 40, this.entorno);
		this.items = new Item[6];


		
		generarMapa();
		
		// Obtengo la posicion de esa ultima isla y guardamos las coordenadas
		
		
		double x;
		if (islas[niveles-1].length > 21) {
			x = islas[niveles-1][20].getX();
		} else {
			
			x = islas[niveles-1][islas[niveles-1].length-1].getX();
		}
		double y = islas[niveles-1][0].getY();
		
		// Creo/inicializo el objeto castillo

		this.castillo = new Castillo(x, y-250, entorno);		// ese "- algo" sirve para subir el objeto
		
		
		// Se inicializan los corazones segun la cantidad de vidas del personaje.
		this.corazones = new Vidas[per.getVidas()];
		for(int i = 0; i < corazones.length; i++) {
			corazones[i] = new Vidas(40 + i * 50, 40);
			
			
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
		
		// -------------------------- Verificacion de victoria ----------------------------
		if (gano(per, castillo)) {
			if (entorno.sePresiono('r') || entorno.sePresiono('R')) {
				reiniciarJuego(); // Reinicia el juego si presiona R
			}
		}
		//---------------------------- Verificacion de derrota ----------------------------
		else if (perdio(per)) {
			if (entorno.sePresiono('r') || entorno.sePresiono('R')) {
				reiniciarJuego(); // Reinicia el juego si presiona R
			}
		}
		// Si no perdio ni gano, se ejecuta el desarrollo del juego.
		else {
			fon.dibujar(entorno); // Dibuja el fondo
			per.dibujar(entorno); // Dibuja el personaje
			dibujarIslas(entorno, islas); // Llama a la funcion que dibuja las islas
			castillo.dibujar(entorno);	// Dibuja el castillo
			
			// Bloque de codigo que dibuja los items del piso.
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null) {
					items[i].dibujar(entorno);
					if (per.colisionCon(items[i])) {
						if (items[i].getTipo().equalsIgnoreCase("corazon")) {
							items[i] = null;
							revivirCorazon(); // Llama a la funcion que recupera el corazon
						}
						else if (items[i].getTipo().equalsIgnoreCase("hongo")) {
							items[i] = null;
							per.iniciarInvulnerabilidad(65*5); // Inicia invulnerabilidad por 65*5 ticks (65 ticks = 1 seg aprox)
						}
					}
				}
			}
			
			// Bloque de codigo que dibuja los enemigos y actualiza su logica (movimiento, bordes, animacion de muerte)
			int contEnemigos = 0;
			for (int i = 0; i < enemigos.length; i++) { 
				for (int j = 0; j < enemigos[i].length; j++) {
					if (enemigos[i][j] != null) {							
						enemigos[i][j].dibujar(entorno); // Dibuja enemigos
						enemigos[i][j].mover();			// Mueve enemigos para el lado contrario
						enemigos[i][j].actualizarBordes(); // Actualiza bordes del enemigo
						enemigos[i][j].estaMuriendo();		// Detecta si está muriendo y si es asi activa la animacion
						if (enemigos[i][j].fueraDePantalla(entorno)) { 
							enemigos[i][j] = null; // Si el enemigo esta fuera de la pantalla lo vuelve null
						}
						if (enemigos[i][j] != null && enemigos[i][j].isMuerto()) {
							enemigos[i][j] = null; // Si el enemigo está muerto pero no lo eliminó, lo vuelve null.
						}
						contEnemigos+=1; // Cuenta enemigos.
					}
					else if (Math.random() < 0.003) { // 0.003 porque es funcional.
						spawnearEnemigo(i, j); // Si el lugar de la matriz es null, hay un % aleatorio de generacion del enemigo.
					
					}
				}
			}
			if (contEnemigos == 0) {
				Random r = new Random();
				int i = r.nextInt(enemigos.length);
				int j = r.nextInt(enemigos[i].length);
				spawnearEnemigo(i,j); // Si la cantidad de enemigos en pantalla es 0, genera uno en un lugar aleatorio.
			}
			
			// --------------------------------- Logica del jugador. ---------------------------------------
			// ---------------------------- Bloque del salto del jugador. ----------------------------
			
			// Verificacion de si el jugador esta chocando la cabeza contra el techo
			boolean golpeoTecho = false;
			for (int i = 0; i < islas.length; i++) { 
				for (int j = 0; j < islas[i].length; j++) {
					if (islas[i][j] != null && per.chocaCabezaCon(islas[i][j])) {
						golpeoTecho = true;
					}
				}
			}
			
			// Si el jugador no esta golpeando el techo....
			if (!golpeoTecho) {			
				per.salto(); // Se llama a la funcion salto() del personaje que verifica si esta en un salto y hace que se eleve.
			}
			// Si el jugador esta golpeando el techo
			else {
				// Se para el salto.
				per.setSaltando(false);
				per.setContSaltos(0);
			}
			
			// ---------------------------- Actualizaciones constantes del jugador ----------------------------
			per.restarTiempoInvulnerabilidad(); // 
			per.actualizarBordes(); // Llama a la funcion actualizarBordes() del personaje que actualiza sus bordes constantemente.
			
			// ---------------------------- Gravedad del jugador ----------------------------
			per.caer(); // Llama a la funcion caer() del personaje que lo hace caer cuando no esta tocando el piso.
			caidaAlVacio(entorno, per); // Verifica si el personaje cayo al vacio y si es asi le resta una vida.
			per.setEstaTocandoPiso(false); // Asumimos que cae por defecto
			for (int i = 0; i < islas.length; i++) {
				for (int j = 0; j < islas[i].length; j++) {
					Isla islaActual = islas[i][j];
					
					if (islaActual != null && per.seApoyaEn(islaActual)) { // Busca alguna isla en la que el jugador este apoyado para cortar la caida
						per.setEstaTocandoPiso(true);
						
						per.setY(islaActual.getTecho()-per.getLargo() /2);
						per.actualizarBordes();
						
					}
					
				}
			}
			// ---------------------------- Movimiento del jugador del jugador ----------------------------
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
			
			// ---------------------------- Si el personaje colisiona con el enemigo... ----------------------------
			for (int i = 0; i < enemigos.length; i++) {
				for (int j = 0; j < enemigos[i].length; j++) {
					if (enemigos[i][j] != null) {
						
						if (per.colisionCon(enemigos[i][j]) && !enemigos[i][j].isMuerto() && !enemigos[i][j].isMuriendo() && !per.isInvulnerable()) {
							eliminarCorazon();
							enemigos[i][j].morir();
						}
					}
				}
			}
			
			// ---------------------------- Actualizaciones constantes del juego ----------------------------
			limite(per, islas, fon); // Revisa 
			puntaje.resetMultiplicador();
			
			// ---------------------------- Disparo del proyectil ----------------------------
			if (entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)                    //Solo dispara cuando se presiona el click izquierdo y no hay otro proyectil activo
					&& proyectil == null) {
				
				proyectil = new Proyectil(                        //Crea la bala,sale del jugador y apunta al mouse
						per.getX(), 
						per.getY(),
						entorno.mouseX(),
						entorno.mouseY()
						);
			}
			movimientoProyectil();
			
			
			// ---------------------------- Animacion explosion del proyectil ----------------------------
			if (explosion != null) {
				
				explosion.dibujar(entorno);        //mientras exista la explosion se muestra y se reduce su tiempo de vida
				explosion.actualizar();
				
				if (explosion.terminada()) {
					explosion = null;
				}
			}
			
			// ---------------------------- Animacion puntaje y corazones ----------------------------
			dibujarCorazones(entorno, corazones);
			puntaje.dibujar(entorno);
			
		}
	}
	

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
	
	
	// Funcion del movimiento del proyectil.
	private void movimientoProyectil() {
		if (proyectil != null) {
			
			if (proyectil.fueraDePantalla(entorno)) {		
				proyectil = null;
			}
			else {
				proyectil.mover();
				proyectil.dibujar(entorno);
				
				boolean impacto = false;
				
				for (int i = 0; i < islas.length && !impacto; i++) {
					for (int j = 0; j < islas[i].length && !impacto; j++) {
						
						if (islas[i][j] != null &&                             // chequea que exista isla y que haya choque
								proyectil.colisionaCon(islas[i][j])) {                   
							
							explosion = new Explosion(            //crea explosión EXACTAMENTE donde estaba la bala
									proyectil.getX(),
									proyectil.getY()
									);
							
							proyectil = null;           //elimina la bala y corta el loop
							impacto = true;
						}
					}
				}
				for (int i = 0; i < enemigos.length && !impacto; i++) {
					for (int j = 0; j < enemigos[i].length && !impacto; j++) {
						
						if (enemigos[i][j] != null &&                             // chequea que el enemigo exista y que haya choque
								proyectil.colisionaCon(enemigos[i][j])) {                   
							
							explosion = new Explosion(            //crea explosión EXACTAMENTE donde estaba la bala
									proyectil.getX(),
									proyectil.getY()
									);
							
							enemigos[i][j].morir();
							this.puntaje.kill();
							
							int z = 0;
							while (z < items.length-1 && items[z] != null) {
								z++;
							}
							if (items[z] == null) {
								Random random = new Random();
								int a = random.nextInt(islas.length);
								int b = random.nextInt(islas[a].length);
								
								if (islas[a][b] != null && Math.random() < 0.1) {
									if (Math.random() > 0.5) {						
										items[z] = new Item(islas[a][b].getX(), islas[a][b].getY()-35, "corazon");
									}
									else {
										items[z] = new Item(islas[a][b].getX(), islas[a][b].getY()-35, "hongo");	
									}
									
								}
							}
							
							proyectil = null;           //elimina la bala y corta el loop
							impacto = true;
						}
					}
				}
				
			}
			
		}
		
	}
	
	
	// Funcion que spawnea un enemigo en un lugar vacio de la matriz
	private void spawnearEnemigo(int i, int j) {
		boolean posicionInicioValida = true;		
		boolean posicionFinalValida = true;		
		
		int k = 0;
		while (k < enemigos[i].length) {
			if (enemigos[i][k] != null) {
				double xExistente = enemigos[i][k].getX();
				
				// Si la distancia absoluta es menor al mínimo, no sirve
				if (Math.abs(0 - xExistente) < enemigos[i][k].getAncho()+30 && enemigos[i][k].isDireccion() == true) {
					posicionInicioValida = false; 
				}
				if (Math.abs(this.entorno.ancho() - xExistente) < enemigos[i][k].getAncho()+30 && enemigos[i][k].isDireccion() == false) {
					posicionFinalValida = false; 
				}
			}
			k++;
		}
		
		double x;
		boolean dir;
		int y = 90+180*i;
		if (posicionInicioValida && posicionFinalValida) {
			
			if (Math.random() > 0.7) {
				x = 0;
				dir = true;
			}
			else {
				x = this.entorno.ancho();
				dir = false;
			}
			this.enemigos[i][j] = new Enemigo(x, y, this.entorno, dir);
			
		}
		else if (posicionInicioValida) {
			x = 0;
			dir = true;
			this.enemigos[i][j] = new Enemigo(x, y, this.entorno, dir);
		}
		else if (posicionFinalValida) {
			x = this.entorno.ancho();
			dir = false;
			this.enemigos[i][j] = new Enemigo(x, y, this.entorno, dir);
		}					
		
		
	}
	
	// Funcion que reinicia el juego
	private void reiniciarJuego() {
		vaciarVariables();
		this.per = new Personaje(entorno, 5); 					
		this.fon = new Fondo(entorno); 						
		int niveles = 4;
		int cantIslas = 24;
		this.islas = new Isla[niveles][cantIslas];					
		this.enemigos = new Enemigo[4][3];				
		this.puntaje = new Puntaje(entorno.ancho() - 200, 40, this.entorno);	
		this.items = new Item[6];
		generarMapa();
		double x;
		if (islas[niveles-1].length > 21) {
			x = islas[niveles-1][20].getX();
		} else {
			
			x = islas[niveles-1][islas[niveles-1].length-1].getX();
		}
		double y = islas[niveles-1][0].getY();
		this.castillo = new Castillo(x, y-250, entorno);	
		this.corazones = new Vidas[per.getVidas()];
		for(int i = 0; i < corazones.length; i++) {
			corazones[i] = new Vidas(40 + i * 50, 40);
			
			
		}
	}

	// Funcion que verifica donde está el jugador y si paso la pantalla mueve los elementos a su par
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
			castillo.moverX(-4);
			
			for (int i = 0; i < enemigos.length; i++) { // Recorre los niveles
				for (int j = 0; j < enemigos[i].length; j++) {
					if (enemigos[i][j] != null) {
						enemigos[i][j].moverX(-4);
						enemigos[i][j].actualizarBordes();
					}
				}
			}
			
			if (this.explosion != null) {
				explosion.moverX(-4);
			}
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null) {
					items[i].moverX(-4);
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
			Image fondodif = Herramientas.cargarImagen("fondo-muerte.png");
	    	entorno.dibujarImagen(fondodif, entorno.ancho()/2, entorno.alto()/2, 0);
	    	
	    	
	    	Color sombra = new Color(0, 0, 0, 180);
			int tamañoFuente = 70;
			double y = entorno.alto()/2-100;
			double x = centrarTextoX("Lucida Console", tamañoFuente, "GAME OVER");
			entorno.cambiarFont("Lucida Console", tamañoFuente, sombra);
			entorno.escribirTexto("GAME OVER", x+4, y);
			entorno.cambiarFont("Lucida Console", tamañoFuente, Color.white);
			entorno.escribirTexto("GAME OVER", x, y);
			puntaje.mostrarStats(x, y, "Lucida Console", this.entorno);
			double x2 = centrarTextoX("Lucida Console", tamañoFuente-40, "Presiona R para volver a intentar.");
			entorno.cambiarFont("Lucida Console", tamañoFuente-40, sombra);
			entorno.escribirTexto("Presiona R para volver a intentar.", x2+3, y+300);
			entorno.cambiarFont("Lucida Console", tamañoFuente-40, Color.white);
			entorno.escribirTexto("Presiona R para volver a intentar.", x2, y+300);
			vaciarVariables();
			return true;
		}
		return false;
	}
	
	// Funcion que verifica si el jugador ganó
	public boolean gano (Personaje p, Castillo c) {
	    if (p.getX() > c.getBordeI()+70 && p.getY() > c.getTecho()+120 && p.getX() < c.getBordeD()-50 && p.getY() < c.getPiso()) {
	        Image fondodif = Herramientas.cargarImagen("fondo-difuminado.png");
	    	entorno.dibujarImagen(fondodif, entorno.ancho()/2, entorno.alto()/2, 0);
	    	
	    	Color sombra = new Color(0, 0, 0, 150);
	    	
	    	int tamañoFuente = 70;
	        double y = entorno.alto()/2-100;
	        double x = centrarTextoX("Lucida Console", tamañoFuente, "¡GANASTE!");
	        entorno.cambiarFont("Lucida Console", tamañoFuente, sombra); 
	        entorno.escribirTexto("¡GANASTE!", x+3, y);
	        entorno.cambiarFont("Lucida Console", tamañoFuente, Color.white); 
	        entorno.escribirTexto("¡GANASTE!", x, y);
	        puntaje.mostrarStats(x, y, "Lucida Console", this.entorno);
	        double x2 = centrarTextoX("Lucida Console", tamañoFuente-40, "Presiona R para volver a jugar.");
			entorno.cambiarFont("Lucida Console", tamañoFuente-40, sombra);
			entorno.escribirTexto("Presiona R para volver a jugar.", x2+3, y+300);
			entorno.cambiarFont("Lucida Console", tamañoFuente-40, Color.LIGHT_GRAY);
			entorno.escribirTexto("Presiona R para volver a jugar.", x2, y+300);
	        vaciarVariables();
	        return true;
	    }
	    return false;
	}
	
	// Vacía las variables
	public void vaciarVariables() {
	
		this.islas = null;
		this.enemigos = null;
		this.corazones = null;
		this.explosion = null;
		this.proyectil = null;
		this.items = null;
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
		if (per.getY() > this.entorno.alto()+60) {
			per.setY(-60);
			eliminarCorazon();
			per.actualizarBordes();
		}
		
	}
	
	public void eliminarCorazon() {			
		per.perderVida();
		per.iniciarInvulnerabilidad();
		puntaje.pierdeVida();
		
		boolean terminar = false;
		for(int i = this.corazones.length-1; i >= 0 && terminar == false; i--) {
			if (!this.corazones[i].isRoto()) {
				this.corazones[i].setRoto(true);
				terminar = true;
			}
			
		}
		
		
		
	}
	public void revivirCorazon() {	
		if (per.getVidas() < corazones.length) {
				
			per.sumarVida();
			
			boolean terminar = false;
			for(int i = 0; i < this.corazones.length && terminar == false; i++) {
				if (this.corazones[i].isRoto()) {
					this.corazones[i].setRoto(false);
					terminar = true;
				}
			}    
		}
		
	}
	
	public void generarMapa() {
		// Primer piso (con separaciones fijas, fila index 3)
		double tamañoIsla = 300; // El tamaño de las islas del piso es fijo en 300
		double yPiso = 700;		// Esta en la altura 650 casi abajo de la pantalla
		double separacionPiso = 160;	// La separacion del piso es 
		double x = 200;	// La primera isla aparece en las coordenadas x del jugador
		
		for (int i = 0; i < islas[islas.length-1].length && i < 21; i++) {
			if (i == islas[islas.length-1].length-1 || i == 20) {
				tamañoIsla = 500;
				
			}
			if (Math.random() < 0.04) {
				int l = 0;
				while (l < items.length-1 && items[l] != null) {
					l++;
				}
				
				if (items[l] == null) {
					if (Math.random() > 0.5) {						
						items[l] = new Item(x, yPiso-35, "corazon");
					}
					else {
						items[l] = new Item(x, yPiso-35, "hongo");	
					}
					
				}
			}
			islas[islas.length-1][i] = new Isla(x, yPiso, tamañoIsla);
			x+= tamañoIsla+separacionPiso;
		}
		tamañoIsla = 300;
		
		
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
					if (Math.random() < 0.04) {
						int l = 0;
						while (l < items.length-1 && items[l] != null) {
							l++;
						}
						if (items[l] == null) {
							if (Math.random() > 0.5) {						
								items[l] = new Item(x, y-35, "corazon");
							}
							else {
								items[l] = new Item(x, y-35, "hongo");	
							}
							
						}
					}
					islas[i][j] = new Isla(x, y, tamaño);
					
					int separacion = new Random().nextInt(75)+tamaño+135;
					x += separacion;
					
				}
				else {
					islas[i][j] = null;
					x += 250;
				}
			}
			
		}
		
	}

	
	// Funcion auxiliar que ayuda a centrar el texto en la pantalla (AYUDA CON IA).
	public double centrarTextoX(String fuente, int tamañoFuente, String texto) {
		Font font = new Font(fuente, Font.PLAIN, tamañoFuente); // Creamos un objeto Font idéntico con Java AWT nativo
        Canvas auxiliar = new Canvas(); // Usamos un Canvas auxiliar para obtener las métricas reales de la fuente (sacado de internet)
        FontMetrics metrics = auxiliar.getFontMetrics(font);
        int anchoTexto = metrics.stringWidth(texto); // Calculamos el ancho exacto que el texto va a ocupar en pantalla (en píxeles)
        return (this.entorno.ancho() - anchoTexto) / 2.0;  // Calculamos la coordenada X para que quede perfectamente centrado
	}

}

