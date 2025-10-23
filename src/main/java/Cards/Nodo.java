package Cards;

public class Nodo<T>{
    private T dato;
    private Nodo<T> siguiente;
    public Nodo(T dato) {
        this.dato = dato;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public T getDato() {
        return dato;
    }

    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }
    public void setInfo(T info) {
        this.dato = info;
    }
    public String toString() {
        return dato.toString();
    }
}
