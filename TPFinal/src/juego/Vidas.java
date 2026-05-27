package juego;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;


public class Vidas {

    Image imagen1;
    Image imagen2;
    double x;
    double y;
    boolean roto;
    
    public Vidas(double x, double y) {

        this.x = x;
        this.y = y;
        this.roto = false;
        this.imagen1 = Herramientas.cargarImagen("corazon.png");
        this.imagen2 = Herramientas.cargarImagen("corazon-roto.png");

    }

    public void dibujar(Entorno entorno) {
    	if (roto) {
    		entorno.dibujarImagen(this.imagen2, this.x, this.y, 0, 0.085);
    		
    		return;
    	}
    	entorno.dibujarImagen(this.imagen1, this.x, this.y, 0, 0.06);

    }

}