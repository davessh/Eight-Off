package EightOffJuego;

import Cards.*;

public class Movimiento {

    public enum Categoria {TT, TR, RT, TF, RF}
    public enum Zona {TABLEAU, RESERVA, FUNDACION}

    private final Categoria categoria;
    private final Zona origen, destino;
    private final int indiceOrigen, indiceDestino;
    private final Carta cartaAsociada;
    private int cantidadMovida;

    public Movimiento(Categoria categoria,
                      Zona origen, int indiceOrigen,
                      Zona destino, int indiceDestino,
                      Carta cartaAsociada) {
        this(categoria, origen, indiceOrigen, destino, indiceDestino, cartaAsociada, 0);
    }

    public Movimiento(Categoria categoria,
                      Zona origen, int indiceOrigen,
                      Zona destino, int indiceDestino,
                      Carta cartaAsociada, int cantidadMovida) {
        this.categoria = categoria;
        this.origen = origen;
        this.indiceOrigen = indiceOrigen;
        this.destino = destino;
        this.indiceDestino = indiceDestino;
        this.cartaAsociada = cartaAsociada;
        this.cantidadMovida = cantidadMovida;
    }

    public static Movimiento tablaATabla(int origen, int destino, Carta carta) {
        return new Movimiento(Categoria.TT, Zona.TABLEAU, origen, Zona.TABLEAU, destino, carta);
    }

    public static Movimiento tablaATabla(int origen, int destino, Carta carta, int cantidad) {
        return new Movimiento(Categoria.TT, Zona.TABLEAU, origen, Zona.TABLEAU, destino, carta, cantidad);
    }

    public static Movimiento tablaAReserva(int origen, int destino, Carta carta) {
        return new Movimiento(Categoria.TR, Zona.TABLEAU, origen, Zona.RESERVA, destino, carta);
    }

    public static Movimiento reservaATabla(int origen, int destino, Carta carta) {
        return new Movimiento(Categoria.RT, Zona.RESERVA, origen, Zona.TABLEAU, destino, carta);
    }

    public static Movimiento tablaAFundacion(int origen, int destino, Carta carta) {
        return new Movimiento(Categoria.TF, Zona.TABLEAU, origen, Zona.FUNDACION, destino, carta);
    }

    public static Movimiento reservaAFundacion(int origen, int destino, Carta carta) {
        return new Movimiento(Categoria.RF, Zona.RESERVA, origen, Zona.FUNDACION, destino, carta);
    }

    public Categoria getCategoria() { return categoria; }
    public Zona getOrigen() { return origen; }
    public Zona getDestino() { return destino; }
    public int getIndiceOrigen() { return indiceOrigen; }
    public int getIndiceDestino() { return indiceDestino; }
    public Carta getCartaAsociada() { return cartaAsociada; }
    public int getCantidadMovida() { return cantidadMovida; }
    public void setCantidadMovida(int cantidad) { this.cantidadMovida = cantidad; }
}
