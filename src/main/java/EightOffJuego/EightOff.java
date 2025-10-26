
package EightOffJuego;

import Cards.*;
import javafx.scene.control.Alert;

public class EightOff {
    public final Mazo mazo;
    private final Tableu[] tableaus;
    private final Celda[] reservas;
    public final Foundation[] foundations;
    private final ListaSimple<Movimiento> undo;

    public EightOff() {
        mazo = new Mazo();
        System.out.println(mazo);
        tableaus = new Tableu[8];
        reservas = new Celda[8];
        foundations = new Foundation[4];
        undo = new ListaSimple<>();
        iniciarComponentes();
    }

    public void iniciarComponentes() {
        for (int i = 0; i < tableaus.length; i++) {
            tableaus[i] = new Tableu();
            reservas[i] = new Celda();
        }
        Palo[] ordenPalos = Palo.values();
        for (int i = 0; i < 4; i++) {
            foundations[i] = new Foundation(ordenPalos[i]);
        }
    }

    public void iniciarNuevaPartida(){
        limpiarComponentes();
        // Repartir 6 cartas a cada uno de los 8 tableaus
        for (int fila = 0; fila < 6; fila++) {
            for (int col = 0; col < 8; col++) {
                Carta carta = mazo.getMazoCartas().eliminaFin();
                if (carta != null) {
                    tableaus[col].insertarCarta(carta);
                }
            }
        }
        for (Tableu tableau : tableaus) {
            System.out.println(tableau.verCarta());
        }
        // Colocar 4 cartas en las primeras 4 celdas de reserva
        for (int i = 0; i < 4; i++){
            Carta carta = mazo.getMazoCartas().eliminaFin();
            if (carta != null) {
                reservas[i].ponerCarta(carta);
            }
        }
    }

    public void limpiarComponentes() {
        for(Tableu tableau : tableaus) tableau.limpiarCartas();
        for (Celda reserva : reservas) reserva.setEspacioLibre();
        for (Foundation foundation : foundations) foundation.limpiarFoundation();
        while (undo.eliminaFinal() != null){}
    }

    public boolean moverTaR(int from, int to){
        Carta mov = tableaus[from].verCarta();
        if (mov == null) return false;
        if (reservas[to].ponerCarta(mov)){
            tableaus[from].sacarCarta();
            undo.insertaFinal(Movimiento.tablaAReserva(from, to, mov));
            return true;
        }
        return false;
    }

    public boolean moverRaT(int from, int to){
        Carta mov = reservas[from].verCarta();
        if (mov == null) return false;
        if (tableaus[to].ponerCarta(mov)){
            reservas[from].sacarCarta();
            undo.insertaFinal(Movimiento.reservaATabla(from, to, mov));
            return true;
        }
        return false;
    }

    public boolean moverTaF(int from, int to){
        Carta mov = tableaus[from].verCarta();
        if (mov == null) return false;
        int idx = getIdxFoundation(mov.getPalo());
        if (foundations[idx].meterCarta(mov)){
            tableaus[from].sacarCarta();
            undo.insertaFinal(Movimiento.tablaAFundacion(from, idx, mov));
            return true;
        }
        return false;
    }

    public boolean moverRaF(int from, int to){
        Carta mov = reservas[from].verCarta();
        if (mov == null) return false;
        int idx = getIdxFoundation(mov.getPalo());
        if (foundations[idx].meterCarta(mov)){
            reservas[from].sacarCarta();
            undo.insertaFinal(Movimiento.reservaAFundacion(from, idx, mov));
            return true;
        }
        return false;
    }

    private int getIdxFoundation(Palo palo){
        for (int i = 0; i < foundations.length; i++) {
            if (foundations[i].getPalo().equals(palo)) return i;
        }
        return -1;
    }

    public Movimiento pista(){
        // Prioridad 1: Mover de reserva a foundation
        for (int r = 0; r < 8; r++) {
            Carta c = reservas[r].verCarta();
            if (c == null) continue;
            int f = getIdxFoundation(c.getPalo());
            if (foundations[f].verificarMovimiento(c)) return Movimiento.reservaAFundacion(r, f, c);
        }
        // Prioridad 2: Mover de tableau a foundation
        for (int t = 0; t < 8; t++) {
            Carta c = tableaus[t].verCarta();
            if (c == null) continue;
            int f = getIdxFoundation(c.getPalo());
            if (foundations[f].verificarMovimiento(c)) return Movimiento.tablaAFundacion(t, f, c);
        }
        // Prioridad 3: Mover de reserva a tableau
        for (int r = 0; r < 8; r++) {
            Carta c = reservas[r].verCarta();
            if (c == null) continue;
            for (int t = 0; t < 8; t++) {
                if (tableaus[t].esMovimientoValido(c)) return Movimiento.reservaATabla(r, t, c);
            }
        }
        // Prioridad 4: Mover de tableau a tableau
        for (int a = 0; a < 8; a++) {
            Carta c = tableaus[a].verCarta();
            if (c == null) continue;
            for (int b = 0; b < 8; b++) {
                if (a != b && tableaus[b].esMovimientoValido(c)) {
                    return Movimiento.tablaATabla(a, b, c);
                }
            }
        }
        // Prioridad 5: Mover de tableau a reserva vacía
        for (int t = 0; t < 8; t++) {
            if (reservas[t].estaVacia()) {
                for (int z = 0; z < 8; z++) {
                    Carta c = tableaus[z].verCarta();
                    if (c != null) return Movimiento.tablaAReserva(z, t, c);
                }
            }
        }

        // No hay movimientos disponibles
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sin movimientos disponibles");
        alert.setHeaderText(null);
        alert.setContentText("No hay más movimientos posibles.");
        alert.showAndWait();
        limpiarComponentes();
        iniciarComponentes();
        return null;
    }

    public boolean sinMovimientos(){
        for (int i = 0; i < tableaus.length; i++) {
            System.out.println("Tableau " + i + ": " + tableaus[i].getTam() + " cartas\n");
        }

        // Verificar victoria
        if (evaluarVictoria()) {
            victoria();
            return false;
        }

        return pista() == null;
    }

    public boolean evaluarVictoria(){
        for (int i = 0; i < foundations.length; i++) {
            Carta ultimaCarta = foundations[i].verUltima();
            if (ultimaCarta == null || ultimaCarta.getValor() != 13) {
                return false;
            }
        }
        return true;
    }

    public void victoria(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Victoria!");
        alert.setHeaderText(null);
        alert.setContentText("¡Felicidades, ganaste!");
        alert.showAndWait();
    }

    public boolean deshacer(){
        Movimiento ultimoMov = undo.eliminaFinal();
        if (ultimoMov == null) return false;

        switch (ultimoMov.getCategoria()){
            case TT -> {
                int k = Math.max(1, ultimoMov.getCantidadMovida());
                ListaSimple<Carta> pack = tableaus[ultimoMov.getIndiceDestino()].tomarNultimos(k); // Usar tomarNultimos
                for (Carta carta : pack.convertirLista()) {
                    tableaus[ultimoMov.getIndiceOrigen()].insertarCarta(carta);
                }
            }
            case TR -> {
                Carta c = reservas[ultimoMov.getIndiceDestino()].sacarCarta();
                tableaus[ultimoMov.getIndiceOrigen()].insertarCarta(c);
            }
            case RT -> {
                Carta c = tableaus[ultimoMov.getIndiceDestino()].sacarCarta();
                reservas[ultimoMov.getIndiceOrigen()].ponerCarta(c);
            }
            case TF -> {
                Carta c = foundations[ultimoMov.getIndiceDestino()].sacarCarta();
                tableaus[ultimoMov.getIndiceOrigen()].insertarCarta(c);
            }
            case RF -> {
                Carta c = foundations[ultimoMov.getIndiceDestino()].sacarCarta();
                reservas[ultimoMov.getIndiceOrigen()].ponerCarta(c);
            }
        }
        return true;
    }

    public boolean moverTaT(int from, int to, int cantidad) {
        if (from == to || cantidad <= 0) return false;
        if (from < 0 || from >= tableaus.length || to < 0 || to >= tableaus.length) return false;
        if (tableaus[from].getTam() < cantidad) return false;

        ListaSimple<Carta> run = tableaus[from].getNultimos(cantidad);
        if (run == null || run.getSize() == 0) return false;

        // Verificar que sea una escalera válida del mismo palo descendente
        if (!esEscaleraValida(run)) return false;

        // Verificar que se pueda colocar en el destino
        Carta cartaInferior = run.getPosicion(0);
        if (!tableaus[to].esMovimientoValido(cartaInferior)) return false;

        // Realizar el movimiento
        ListaSimple<Carta> movidas = tableaus[from].tomarNultimos(cantidad);
        for (Carta carta : movidas.convertirLista()) {
            if (!tableaus[to].ponerCarta(carta)) {
                // Revertir si falla
                for (Carta c : movidas.convertirLista()) {
                    tableaus[from].insertarCarta(c);
                }
                return false;
            }
        }

        Carta topMovida = movidas.getFin();
        undo.insertaFinal(Movimiento.tablaATabla(from, to, topMovida, cantidad));
        return true;
    }

    private boolean esEscaleraValida(ListaSimple<Carta> cartas) {
        if (cartas.getSize() <= 1) return true;

        Palo paloBase = cartas.getPosicion(0).getPalo();
        for (int i = 0; i < cartas.getSize() - 1; i++) {
            Carta actual = cartas.getPosicion(i);
            Carta siguiente = cartas.getPosicion(i + 1);

            if (actual.getPalo() != paloBase || siguiente.getPalo() != paloBase) {
                return false;
            }
            if (actual.getValor() - 1 != siguiente.getValor()) {
                return false;
            }
        }
        return true;
    }

    public Carta getTopTableau(int col){ return tableaus[col].verCarta(); }
    public Carta getTopReservas(int i){ return reservas[i].verCarta(); }
    public Carta getTopFoundation(int i){ return foundations[i].verUltima(); }
    public ListaSimple<Carta> getTableau(int idx){
        ListaSimple<Carta> resultado = new ListaSimple<>();
        // Nota: Tableu no expone directamente su lista,
        // podrías necesitar agregar un método getter en Tableu
        return resultado;
    }
    public Tableu[] getTableaus() { return tableaus; }
    public Celda[] getReservas() { return reservas; }
    public Foundation[] getFoundations() { return foundations; }
}