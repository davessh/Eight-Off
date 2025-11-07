package EightOffJuego;

import Cards.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EstadoJuego implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Carta>[] tableaus;
    private Carta[] reservas;
    private List<Carta>[] foundations;
    private String descripcionMovimiento;
    private int numeroMovimiento;

    @SuppressWarnings("unchecked")
    public EstadoJuego(Tableu[] tableusActuales, Celda[] reservasActuales,
                       Foundation[] foundationsActuales, String descripcion, int numMov) {
        this.numeroMovimiento = numMov;
        this.descripcionMovimiento = descripcion;

        tableaus = new ArrayList[8];
        for (int i = 0; i < 8; i++) {
            tableaus[i] = new ArrayList<>();
            ListaSimple<Carta> cartas = tableusActuales[i].getTableau();
            for (Carta c : cartas.convertirLista()) {
                tableaus[i].add(c);
            }
        }

        reservas = new Carta[8];
        for (int i = 0; i < 8; i++) {
            reservas[i] = reservasActuales[i].verCarta();
        }

        foundations = new ArrayList[4];
        for (int i = 0; i < 4; i++) {
            foundations[i] = new ArrayList<>();
            ListaSimple<Carta> cartas = foundationsActuales[i].getFoundation();
            for (Carta c : cartas.convertirLista()) {
                foundations[i].add(c);
            }
        }
    }

    public List<Carta>[] getTableaus() { return tableaus; }
    public Carta[] getReservas() { return reservas; }
    public List<Carta>[] getFoundations() { return foundations; }
    public String getDescripcion() { return descripcionMovimiento; }
    public int getNumeroMovimiento() { return numeroMovimiento; }
}