package Cards;

public class Mazo {
    ListaCircularDoble<Carta> mazoCartas;
    public Mazo() {
    mazoCartas = new ListaCircularDoble<>();
    llenarMazo();
    mezclarMazo();
}

    public void llenarMazo() {
        for (int i = 2; i <= 14; i++) {
            for (Palo palo : Palo.values()) {
                CartaInglesa carta = new CartaInglesa(i, palo, palo.getColor());
                carta.ponerBocaArriba();
                mazoCartas.insertaFin(carta);
            }
        }
    }

    public void ordenarMazo() {
        mazoCartas.ordenarLista();
    }

    public void mezclarMazo(){
        mazoCartas.revolverLista();
    }

    public ListaCircularDoble<Carta> getMazoCartas() {
        return mazoCartas;
    }

    public String toString() {
        return mazoCartas.mostrarLista();
    }
}
