package juego;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
//prueba

public class Vidas {

	private Image imagen1;
	private  Image imagen2;

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public boolean isRoto() {
		return roto;
	}

	public void setRoto(boolean roto) {
		this.roto = roto;
	}

	private double x;
	private double y;
	private boolean roto;
    
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