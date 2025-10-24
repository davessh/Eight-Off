package EightOffJuego;

import Cards.*;
public class Tableu {
    private ListaSimple<Carta> tableu = new ListaSimple<>();
    private boolean reyEnVacio = false;

    public Tableu() {

    }

    public int getTam() {
        return tableu.getSize();
    }

    public boolean estaVacio() {
        return tableu.getSize() == 0;
    }

    public Carta verCarta() {
        return tableu.getFin();
    }

    public Carta sacarCarta() {
        return tableu.eliminaFinal();
    }

    public void insertarCarta(Carta carta) {
        tableu.insertaFinal(carta);
    }

    public void limpiarCartas() {
        while (tableu.eliminaFinal() != null) {
        }
    }
    public boolean ponerCarta(Carta carta){
        if (!esMovimientoValido(carta)) {
            return false;
        }
        tableu.insertaFinal(carta);
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
