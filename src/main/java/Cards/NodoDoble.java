package Cards;

public class NodoDoble<T> {
    private T info;
    private NodoDoble<T> siguiente;
    private NodoDoble<T> anterior;
    public NodoDoble(T info) {
        this.info = info;
    }
    public T getInfo() {
        return info;
    }
    public NodoDoble<T> getSiguiente() {
        return siguiente;
    }
    public NodoDoble<T> getAnterior() {
        return anterior;
    }
    public void setSiguiente(NodoDoble<T> siguiente) {
        this.siguiente = siguiente;
    }
    public void setAnterior(NodoDoble<T> anterior) {
        this.anterior = anterior;
    }
    public void setInfo(T info) {
        this.info = info;
    }
    public String toString() {
        return info.toString();
    }
}
