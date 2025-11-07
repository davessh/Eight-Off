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

        // Actualizar 'fin' si es necesario
        if (nodo != null) {
            NodoHistorial temp = nodo;
            while (temp.getSiguiente() != null) {
                temp = temp.getSiguiente();
            }
            fin = temp;
        }
    }

    public NodoHistorial getInicio() {
        return inicio;
    }

    public NodoHistorial getFin() {
        return fin;
    }

    /**
     * Obtiene las descripciones de todos los estados en el historial
     */
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

    /**
     * Obtiene todos los nodos del historial como lista
     */
    public List<NodoHistorial> obtenerNodos() {
        List<NodoHistorial> nodos = new ArrayList<>();
        NodoHistorial nodo = inicio;
        while (nodo != null) {
            nodos.add(nodo);
            nodo = nodo.getSiguiente();
        }
        return nodos;
    }

    /**
     * Obtiene el tamaño actual del historial
     */
    public int getSize() {
        int count = 0;
        NodoHistorial nodo = inicio;
        while (nodo != null) {
            count++;
            nodo = nodo.getSiguiente();
        }
        return count;
    }

    /**
     * Obtiene el índice del nodo actual
     */
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

    /**
     * Elimina todos los nodos posteriores a un nodo dado
     * Útil para crear un nuevo branch desde un punto anterior
     */
    public void eliminarPosteriores(NodoHistorial nodo) {
        if (nodo == null) return;

        // Cortar la cadena
        nodo.setSiguiente(null);

        // Actualizar fin
        fin = nodo;

        // Si el actual estaba después del nodo, actualizarlo
        boolean actualEliminado = false;
        NodoHistorial temp = inicio;
        while (temp != null && temp != actual) {
            if (temp == nodo) {
                actualEliminado = true;
                break;
            }
            temp = temp.getSiguiente();
        }

        if (actualEliminado) {
            actual = nodo;
        }
    }

    /**
     * Limpia todo el historial
     */
    public void limpiar() {
        inicio = null;
        fin = null;
        actual = null;
    }
}