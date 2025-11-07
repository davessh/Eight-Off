package EightOffJuego;

import javafx.fxml.FXML;
import Cards.*;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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

    @FXML
    public void initialize() {
        historialList.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setFont(Font.font("Arial", FontWeight.NORMAL, 13));

                    // Resaltar el estado actual
                    if (getIndex() == listaHistorial.getIndiceActual()) {
                        setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold; -fx-padding: 8;");
                        setTextFill(Color.web("#4CAF50"));
                    } else {
                        setStyle("-fx-text-fill: white; -fx-padding: 8;");
                        setTextFill(Color.WHITE);
                    }
                }
            }
        });
    }

    public void setHistorial(ListaHistorial historial) {
        this.listaHistorial = historial;
        this.estadoActualAnterior = historial.getActual();
        this.estadoSeleccionado = historial.getActual();

        // Cargar los movimientos
        List<String> descripciones = historial.obtenerDescripciones();
        historialList.getItems().addAll(descripciones);

        // Seleccionar el estado actual
        int indiceActual = historial.getIndiceActual();
        historialList.getSelectionModel().select(indiceActual);
    }

    @FXML
    private void handleAceptar() {
        confirmado = true;
        int indiceSeleccionado = historialList.getSelectionModel().getSelectedIndex();
        if (indiceSeleccionado >= 0) {
            List<NodoHistorial> nodos = listaHistorial.obtenerNodos();
            estadoSeleccionado = nodos.get(indiceSeleccionado);
            listaHistorial.setActual(estadoSeleccionado);
        }
        cerrarVentana();
    }

    @FXML
    private void handleCancelar() {
        confirmado = false;
        estadoSeleccionado = estadoActualAnterior;
        listaHistorial.setActual(estadoActualAnterior);
        cerrarVentana();
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