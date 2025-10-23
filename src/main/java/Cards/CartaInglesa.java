package Cards;

public class  CartaInglesa extends Carta{
    public CartaInglesa(int valor, Palo figura, String color) {
        super(valor, figura, color);
    }
    @Override
    public int compareTo(Carta o) {
        if (getValor() == o.getValor() ) {
            if (getColor().equals(o.getColor())) {
                return 0;
            } else {
                return palo.getPeso() - o.palo.getPeso();
            }
        }
        return getValor() - o.getValor();
    }
}
