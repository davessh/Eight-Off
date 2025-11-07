package EightOffJuego;

import javafx.fxml.FXML;
import Cards.*;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import java.util.List;

public class HistorialController {
    @FXML
    private ListView<String> historialList;
    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnCancelar;

    private ListaHistorial listaHistorial;
    private NodoHistorial estadoSeleccionado;
    private NodoHistorial estadoActualAnterior;
    private boolean confirmado = false;
    private EightOff juego;
    private int indiceSeleccionadoTemporal = -1;

    @FXML
    public void initialize() {
        historialList.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    HBox container = new HBox(10);
                    container.setAlignment(Pos.CENTER_LEFT);

                    javafx.scene.control.Label label = new javafx.scene.control.Label(item);
                    label.setFont(Font.font("Arial", FontWeight.NORMAL, 13));

                    if (getIndex() == listaHistorial.getIndiceActual()) {
                        label.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
                        label.setTextFill(Color.web("#4CAF50"));
                    } else {
                        label.setStyle("-fx-text-fill: white;");
                        label.setTextFill(Color.WHITE);
                    }

                    HBox.setHgrow(label, Priority.ALWAYS);
                    container.getChildren().add(label);

                    setGraphic(container);
                    setText(null);
                    setStyle("-fx-padding: 8; -fx-background-color: transparent;");
                }
            }
        });

        historialList.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.intValue() >= 0) {
                indiceSeleccionadoTemporal = newVal.intValue();
                mostrarVistaPrevia(newVal.intValue());
            }
        });
    }

    public void setHistorial(ListaHistorial historial, EightOff juegoActual) {
        this.listaHistorial = historial;
        this.juego = juegoActual;
        this.estadoActualAnterior = historial.getActual();
        this.estadoSeleccionado = historial.getActual();

        List<String> descripciones = historial.obtenerDescripciones();
        historialList.getItems().addAll(descripciones);

        int indiceActual = historial.getIndiceActual();
        historialList.getSelectionModel().select(indiceActual);
        indiceSeleccionadoTemporal = indiceActual;
    }

    private void mostrarVistaPrevia(int indice) {
        if (juego == null || indice < 0) return;

        List<NodoHistorial> nodos = listaHistorial.obtenerNodos();
        if (indice >= nodos.size()) return;

        NodoHistorial nodo = nodos.get(indice);
        EstadoJuego estado = nodo.getEstado();

        juego.restaurarEstado(estado);
    }

    @FXML
    private void handleAceptar() {
        confirmado = true;

        if (indiceSeleccionadoTemporal >= 0) {
            List<NodoHistorial> nodos = listaHistorial.obtenerNodos();
            estadoSeleccionado = nodos.get(indiceSeleccionadoTemporal);
            eliminarEstadosPosteriores(estadoSeleccionado);
            listaHistorial.setActual(estadoSeleccionado);
        }

        cerrarVentana();
    }

    @FXML
    private void handleCancelar() {
        confirmado = false;
        estadoSeleccionado = null;
        if (estadoActualAnterior != null && juego != null) {
            juego.restaurarEstado(estadoActualAnterior.getEstado());
        }

        listaHistorial.setActual(estadoActualAnterior);
        cerrarVentana();
    }

    private void eliminarEstadosPosteriores(NodoHistorial nodoSeleccionado) {
        if (nodoSeleccionado == null) return;
        nodoSeleccionado.setSiguiente(null);
        NodoHistorial actual = listaHistorial.getInicio();
        while (actual != null && actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }
    }

    private void cerrarVentana() {
        btnCancelar.getScene().getWindow().hide();
    }

    public NodoHistorial getEstadoSeleccionado() {
        return estadoSeleccionado;
    }

    public boolean isConfirmado() {
        return confirmado;
    }
}