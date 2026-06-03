package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Proyectil {
	private double x;
	private double y;

	private double velX;
	private double velY;
	
	private Image imagen;
    private double escala;
    
    private double angulo;
    
    private double radio;             
	
	public Proyectil(double xInicial, double yInicial,
            double mouseX, double mouseY) {
		this.radio = 10;//agrego unos bordes
		this.x = xInicial;
		this.y = yInicial;

		this.escala = 0.07;
        this.imagen = Herramientas.cargarImagen("bala.png");

		double dx = mouseX - xInicial;
		double dy = mouseY - yInicial;
		
		angulo = Math.atan2(dy, dx);                 //*Calcula el ángulo correcto para rotar la imagen hacia donde va el disparo.

		double distancia = Math.sqrt(dx * dx + dy * dy);   //calcula la distancia al mouse

		velX = dx / distancia * 15;                   //Se normaliza el vector (dx/distancia, dy/distancia) ,Luego se multiplica por 15 → velocidad fija constante
		velY = dy / distancia * 15;
}
	public void mover() {          //Actualiza posición cada frame.
	    x += velX;
	    y += velY;
	}
	public double getX() {
	    return x;
	}                                  //Permiten que otras clases (como explosiones o colisiones) accedan a la posición.

	public double getY() {
	    return y;
	}
	
	public void dibujar(Entorno entorno) {
        entorno.dibujarImagen(imagen, x, y, angulo, escala);                        // Dibuja el proyectil en su posición actual Rotado hacia su dirección
    }
	
	public boolean fueraDePantalla(Entorno entorno) {
	    return x < 0 ||
	           x > entorno.ancho() ||
	           y < 0 ||                         //sirve para eliminar proyectiles viejos.
	           y > entorno.alto();
	}
	
	public boolean colisionaCon(Isla isla) {
	    if (isla == null) {
	        return false;
	    }

	    return x + radio > isla.getBordeI()
	        && x - radio < isla.getBordeD()                 //compara límites del proyectil  contra límites de la isla
	        && y + radio > isla.getTecho()
	        && y - radio < isla.getPiso();
	}
	public boolean colisionaCon(Enemigo enemigo) {
		if (enemigo == null) {
			return false;
		}
		
		return x + radio > enemigo.getBordeI()
				&& x - radio < enemigo.getBordeD()                 //compara límites del proyectil  contra límites de la isla
				&& y + radio > enemigo.getTecho()
				&& y - radio < enemigo.getPiso();
	}
	
	
}
