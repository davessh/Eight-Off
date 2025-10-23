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
  return estaVacio();
 }

 public Carta sacarCarta(){
  return cartas.eliminaFinal();
 }
 public boolean verificarMovimiento(Carta carta){
  if(carta == null || carta.getPalo() != palo )
   return false;
  Carta cartaFin = cartas.getFin();
  int valorCarta = cartaFin.getValorBajo();
  if(cartaFin == null){
   return valorCarta == 1;
  }
  int valorCartaFinal = cartaFin.getValorBajo();
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
  while(cartas.eliminaFinal() == null){
  }
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
