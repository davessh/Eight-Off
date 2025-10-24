package EightOffJuego;

import Cards.*;

public class Tableu {
    private ListaSimple<Carta> tableau = new ListaSimple<>();
    private boolean soloRey = false;

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

    public boolean esMovimientoValido(Carta carta) {
        if (carta == null) {
            return false;
        }

        Carta cartaSuperior = verCarta();
        if (cartaSuperior == null) {
            return !soloRey || carta.getValor() == 13;
        }

        boolean mismoPalo = cartaSuperior.getPalo() == carta.getPalo();
        boolean valorDescendente = cartaSuperior.getValor() - 1 == carta.getValor();

        return mismoPalo && valorDescendente;
    }

    public boolean ponerCarta(Carta carta){
        if (!esMovimientoValido(carta)) {
            return false;
        }
        tableau.insertaFinal(carta);
        return true;
    }

    public ListaSimple<Carta> getTableau(){
        return tableau;
    }

    public ListaSimple<Carta> getNultimos(int n) {
        return tableau.getNultimos(n);
    }

    public ListaSimple<Carta> tomarNultimos(int n) {
        return tableau.tomarNultimos(n);
    }

    public void agregarTodas(ListaSimple<Carta> run) {
        tableau.añadirFinal(run);
    }

    public boolean escaleraValida(ListaSimple<Carta> run) {
        int m = run.getSize();
        if (m == 0) return false;
        for (int i = 1; i < m; i++) {
            Carta abajo  = run.getPosicion(i - 1);
            Carta arriba = run.getPosicion(i);
            if (abajo.getPalo() != arriba.getPalo()) return false;
            if (abajo.getValor() - 1 != arriba.getValor()) return false;
        }
        return true;
    }

    public boolean puedoColocarEscalera(ListaSimple<Carta> run) {
        if (!escaleraValida(run)) return false;
        Carta primera = run.getPosicion(0);
        return esMovimientoValido(primera);
    }

    public boolean ponerEscalera(ListaSimple<Carta> run) {
        if (!puedoColocarEscalera(run)) return false;
        agregarTodas(run);
        return true;
    }

    public void setSoloRey(boolean soloRey) {
        this.soloRey = soloRey;
    }

    public boolean isSoloRey() {
        return soloRey;
    }
}
