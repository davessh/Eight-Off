package EightOffJuego;
import Cards.*;
public class Foundation {
 private ListaSimple<Carta> cartas;
 private Palo palo;
 public Foundation(Palo palo) {
  this.palo = palo;
  cartas = new ListaSimple<>();
 }

 public Carta verUltima(){
  return cartas.getFin();
}

 public boolean estaVacio(){
  return cartas.getSize() == 0;
 }

 public Carta sacarCarta(){
  return cartas.eliminaFinal();
 }

 public boolean verificarMovimiento(Carta carta){
  if(carta == null || carta.getPalo() != palo)
   return false;

  Carta cartaFin = cartas.getFin();

  if(cartaFin == null){
   return carta.getValorBajo() == 1; // Debe ser carta.getValorBajo()
  }

  int valorCartaFinal = cartaFin.getValorBajo();
  int valorCarta = carta.getValorBajo();
  return valorCartaFinal + 1 == valorCarta;
 }

 public boolean meterCarta(Carta carta){
 if (!verificarMovimiento(carta)) {
  return false;
 }
 cartas.insertaFinal(carta);
 return true;
}

 public void limpiarFoundation(){
  while(cartas.eliminaFinal() != null){} // != en lugar de ==
 }

 public int getSize(){
  return cartas.getSize();
 }
 public Palo getPalo(){
 return palo;
}

 public ListaSimple<Carta> getFoundation(){
  return cartas;
}
}
