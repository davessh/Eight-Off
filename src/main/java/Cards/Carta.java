package Cards;

public abstract class Carta implements Comparable<Carta> {
    private int valor;
    protected Palo palo;
    private String color;
    private int valorBajo;
    private boolean faceup;

    public Carta(int valor, Palo palo, String color) {
        this.valor = valor;
        this.palo = palo;
        this.color = color;
        if (valor == 14) {
            valorBajo = 1;
        } else {
            valorBajo = valor;
        }

        faceup = false;
    }

    public boolean isFaceup() {
        return faceup;
    }

    public String toString() {
        if (!faceup) {
            return "back";
        }
        return switch (valor) {
            case 14 -> "A" + palo.getFigura();
            case 11 -> "J"+ palo.getFigura();
            case 12 -> "Q" + palo.getFigura();
            case 13 -> "K" + palo.getFigura();
            case 15-> "Joker";
            default -> valor + palo.getFigura();
        };
    }

    public boolean tieneElMismoValor(Carta carta) {
        return valor == carta.valor;
    }
    public boolean tieneElMismoPalo(Palo palo) {
        return this.palo.equals(palo);
    }
    public boolean esLaSiguiente(Carta carta) {
        if (valor+1 == carta.valor) {
            return true;
        }
        // verificar si es un As y un 2
        if (valor == 14 && carta.valor == 2) {
            return true;
        }
        return false;
    }
    public int getValor() {
        return valor;
    }
    public Palo getPalo() {
        return palo;
    }
    public String getColor() {
        return color;
    }
    public int getValorBajo() {
        return valorBajo;
    }
    public void ponerBocaArriba(){
        faceup = true;
    }
    public boolean estaCaraArriba(){
        return faceup;
    }

    public void ponerBocaAbajo(){
        faceup = false;
    }
    public boolean estaCaraAbajo(){
        return !faceup;
    }
}
