package Cards;

public class Prueba {
    public static void main(String[] args) {
        Mazo mazo = new Mazo();
        mazo.ordenarMazo();
        System.out.println(mazo);
        mazo.mezclarMazo();
        System.out.println(mazo);
//        mazo.getMazoCartas();
//        ListaSimple<Carta> cartas = new ListaSimple<>();
    }
}

