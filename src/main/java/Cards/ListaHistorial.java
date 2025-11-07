package Cards;

import EightOffJuego.*;
import java.util.ArrayList;
import java.util.List;

public class ListaHistorial {
    private NodoHistorial inicio;
    private NodoHistorial fin;
    private NodoHistorial actual;

    public ListaHistorial() {
        inicio = null;
        fin = null;
        actual = null;
    }

    public void agregarEstado(EstadoJuego estado) {
        NodoHistorial nuevoNodo = new NodoHistorial(estado);

        if (inicio == null) {
            inicio = fin = actual = nuevoNodo;
        } else {
            // Si actual no es fin, eliminar todo lo que viene después (branch)
            if (actual != fin) {
                actual.setSiguiente(null);
                fin = actual;
            }

            // Agregar nuevo nodo al final
            nuevoNodo.setAnterior(fin);
            fin.setSiguiente(nuevoNodo);
            fin = nuevoNodo;
            actual = nuevoNodo;
        }
    }

    public NodoHistorial getActual() {
        return actual;
    }

    public void setActual(NodoHistorial nodo) {
        actual = nodo;
    }

    public NodoHistorial getInicio() {
        return inicio;
    }

    public NodoHistorial getFin() {
        return fin;
    }

    public List<String> obtenerDescripciones() {
        List<String> descripciones = new ArrayList<>();
        NodoHistorial nodo = inicio;
        while (nodo != null) {
            String desc = String.format("Mov %d: %s",
                    nodo.getEstado().getNumeroMovimiento(),
                    nodo.getEstado().getDescripcion());
            descripciones.add(desc);
            nodo = nodo.getSiguiente();
        }
        return descripciones;
    }

    public List<NodoHistorial> obtenerNodos() {
        List<NodoHistorial> nodos = new ArrayList<>();
        NodoHistorial nodo = inicio;
        while (nodo != null) {
            nodos.add(nodo);
            nodo = nodo.getSiguiente();
        }
        return nodos;
    }

    public int getSize() {
        int count = 0;
        NodoHistorial nodo = inicio;
        while (nodo != null) {
            count++;
            nodo = nodo.getSiguiente();
        }
        return count;
    }

    public int getIndiceActual() {
        int indice = 0;
        NodoHistorial nodo = inicio;
        while (nodo != null) {
            if (nodo == actual) return indice;
            nodo = nodo.getSiguiente();
            indice++;
        }
        return -1;
    }
}