package juego;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;


public class Vidas {

    Image imagen;
    double x;
    double y;

    public Vidas(double x, double y) {

        this.x = x;
        this.y = y;

        imagen = Herramientas.cargarImagen("corazon.png");

    }

    public void dibujar(Entorno entorno) {

    	entorno.dibujarImagen(imagen, x, y, 0, 0.1);

    }

}