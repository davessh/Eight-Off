package EightOffJuego;
import Cards.*;
public class Foundation {
 private ListaSimple<Carta> cartas;
 private Palo palo;
 public Foundation(Palo palo) {
  this.palo = palo;
  cartas = new ListaSimple<>();
 }
}
