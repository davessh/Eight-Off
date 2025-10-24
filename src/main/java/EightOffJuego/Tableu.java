package EightOffJuego;

import Cards.*;
public class Tableu {
    private ListaSimple<Carta> tableau = new ListaSimple<>();
    private boolean reyEnVacio = false;

    public Tableu() {

    }

    public int getTam() {
        return tableau.getSize();
    }

    public boolean estaVacio() {
        return tableau.getSize() == 0;
    }

    public Carta verCarta() {
        return tableau.getFin();
    }

    public Carta sacarCarta() {
        return tableau.eliminaFinal();
    }

    public void insertarCarta(Carta carta) {
        tableau.insertaFinal(carta);
    }

    public void limpiarCartas() {
        while (tableau.eliminaFinal() != null) {
        }
    }
    public boolean ponerCarta(Carta carta){
        if (!esMovimientoValido(carta)) {
            return false;
        }
        tableau.insertaFinal(carta);
        return true;
    }

    public boolean esMovimientoValido(Carta carta) {
        if (carta == null) {
            return false;
        }

        Carta cartaSuperior = verCarta();
        if (cartaSuperior == null) {
            return !reyEnVacio || carta.getValor() == 13;
        }

        boolean mismoPalo = cartaSuperior.getPalo() == carta.getPalo();
        boolean valorDescendente = cartaSuperior.getValor() - 1 == carta.getValor();

        return mismoPalo && valorDescendente;
    }
}
