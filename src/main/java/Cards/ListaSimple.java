package Cards;

import java.util.*;

public class ListaSimple<T> {
    private Nodo<T> inicio;
    public ListaSimple() {
        this.inicio = null;
    }
    public void insertaInicio(T dato){
        Nodo<T> nodo = new Nodo(dato);
        nodo.setSiguiente(inicio);
        inicio = nodo;
    }
    public void insertaFinal(T dato){
        Nodo<T> nodo = new Nodo(dato);
        if (inicio == null){
            nodo.setSiguiente(inicio);
            inicio = nodo;
        }else{
            Nodo<T> iter = inicio;
            while (iter.getSiguiente() != null){
                iter = iter.getSiguiente();
            }
            iter.setSiguiente(nodo);
            nodo.setSiguiente(null);
        }
    }
    public T eliminaInicio(){
        Nodo<T> nodoEliminado = inicio;
        if (inicio == null){
            return null;
        }
        inicio = inicio.getSiguiente();
        return nodoEliminado.getDato();
    }
    public T eliminaFinal(){
        if (inicio == null){
            return null;
        }
        if (inicio.getSiguiente() == null){
            T dato = inicio.getDato();
            inicio = null;
            return dato;
        }
        Nodo<T> ref = inicio;
        Nodo<T> anterior = ref;
        while (ref.getSiguiente() != null){
            anterior =ref;
            ref = ref.getSiguiente();
        }
        anterior .setSiguiente(null);
        return ref.getDato();
    }
    public String mostrarLista(){
        String cadena = "";
        if (inicio == null){
            return "La lista se encuentra vacia";
        }
        Nodo<T> iter = inicio;
        while (iter.getSiguiente() != null){
            cadena += iter.getDato().toString() + "\n";
            iter = iter.getSiguiente();
        }
        cadena += iter.getDato().toString();
        return cadena;
    }

    public int getSize(){
        Nodo<T> ref = inicio;
        int size = 1;
        if (ref == null){
            return 0;
        }
        while (ref.getSiguiente() != null){
            ref = ref.getSiguiente();
            size++;
        }
        return size;
    }

    public void ordenarLista(){
        Comparator<T> cmp = (a, b) -> ((Comparable<? super T>) a).compareTo(b);
        boolean huboCambios;
        do {
            huboCambios = false;
            Nodo<T> actual = inicio;
            while (actual != null && actual.getSiguiente() != null) {
                Nodo<T> sig = actual.getSiguiente();
                if (cmp.compare(actual.getDato(), sig.getDato()) > 0) {
                    T tmp = actual.getDato();
                    actual.setInfo(sig.getDato());
                    sig.setInfo(tmp);
                    huboCambios = true;
                }
                actual = sig;
            }
        } while (huboCambios);
    }

    //Pasa los datos de mi lista a un arrayList vacio
    public List<T> convertirLista() {
        ArrayList<T> lista = new ArrayList<>();
        Nodo<T> actual = inicio;
        while (actual != null) {
            lista.add(actual.getDato());
            actual = actual.getSiguiente();
        }
        return lista;
    }


    public T getFin(){
        if (inicio == null){
            return null;
        }
        Nodo<T> iter = inicio;
        while (iter.getSiguiente() != null){
            iter = iter.getSiguiente();
        }
        return iter.getDato();
    }
    public T getPosicion(int posicion){
        if (posicion < 0) return null;
        Nodo<T> iter = inicio;
        int i = 0;
        while (iter != null){
            if (i == posicion) return iter.getDato();
            iter = iter.getSiguiente();
            i++;
        }
        return null;
    }
    public void añadirFinal(ListaSimple<T> lista) {
        for (T obj : lista.convertirLista()) {
            insertaFinal(obj);
        }
    }

    //Se extraen y eliminan los N elementos de la lista original, se devuelven en una nueva lista manteniendo el orden original
    public ListaSimple<T> tomarNultimos(int cantidad) {
        ListaSimple<T> listaResultado = new ListaSimple<>();
        int tamaño = getSize();
        if (cantidad <= 0 || tamaño == 0) return listaResultado;
        if (cantidad > tamaño) cantidad = tamaño;
        for (int i = 0; i < cantidad; i++) {
            T elemento = eliminaFinal();
            if (elemento == null) break;
            listaResultado.insertaInicio(elemento); //Valor eliminado se inserta al inicio de la lista para no invertir el orden
        }
        return listaResultado;
    }
    //Devuelve una lista contiene los N ultimos elementos de la lista original
    public ListaSimple<T> getNultimos(int n) {
        ListaSimple<T> listaResultado = new ListaSimple<>();
        int tamañoLista = getSize();
        if (n <= 0 || n > tamañoLista) return listaResultado;
        for (int i = tamañoLista - n; i < tamañoLista; i++) {
            listaResultado.insertaFinal(getPosicion(i));
        }
        return listaResultado;
    }

    public void addAllFinal(ListaSimple<T> lista) {
        for (T x : lista.convertirLista()) {
            insertaFinal(x);
        }
    }
    }


