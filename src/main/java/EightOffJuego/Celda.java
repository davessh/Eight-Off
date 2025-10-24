package EightOffJuego;

import Cards.Carta;

public class Celda {
    private Carta carta;
    public Celda() {
        this.carta = null;
    }

    public boolean estaVacia() {
        return carta == null;
    }
    public boolean esMovimientoValido(Carta carta) {
        return carta!= null && estaVacia();
    }

    public Carta verCarta() {
            return carta;
    }

    public Carta sacarCarta() {
        Carta cartaAux = carta;
        carta = null;
        return cartaAux;
    }

    public boolean ponerCarta(Carta carta){
        if (!esMovimientoValido(carta)){
            return false;
        } else {
            this.carta = carta;
            return true;
        }
    }
    public void setEspacioLibre(){
        carta = null;
    }

}
