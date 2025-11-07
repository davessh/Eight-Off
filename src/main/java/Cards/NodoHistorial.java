package Cards;

import EightOffJuego.*;

public class NodoHistorial {
    private EstadoJuego estado;
    private NodoHistorial siguiente;
    private NodoHistorial anterior;

    public NodoHistorial(EstadoJuego estado) {
        this.estado = estado;
        this.siguiente = null;
        this.anterior = null;
    }

    public EstadoJuego getEstado() {
        return estado;
    }

    public NodoHistorial getSiguiente() {
        return siguiente;
    }

    public NodoHistorial getAnterior() {
        return anterior;
    }

    public void setSiguiente(NodoHistorial siguiente) {
        this.siguiente = siguiente;
    }

    public void setAnterior(NodoHistorial anterior) {
        this.anterior = anterior;
    }
}