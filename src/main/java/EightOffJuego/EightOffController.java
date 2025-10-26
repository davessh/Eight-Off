package EightOffJuego;

import Cards.*;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class EightOffController {

    @FXML private HBox reservasContainer;
    @FXML private HBox foundationsContainer;
    @FXML private HBox tableausContainer;
    @FXML private Button btnNuevoJuego;
    @FXML private Button btnUndo;
    @FXML private Button btnPista;
    @FXML private Label lblEstado;
    @FXML private Label lblMovimientos;

    private EightOff juego;
    private List<StackPane> reservasPanes;
    private List<StackPane> foundationsPanes;
    private List<VBox> tableausPanes;

    private String origenSeleccionado = null;
    private StackPane paneSeleccionado = null;
    private int movimientos = 0;

    @FXML
    private void initialize() {
        juego = new EightOff();
        reservasPanes = new ArrayList<>();
        foundationsPanes = new ArrayList<>();
        tableausPanes = new ArrayList<>();

        crearEstructuraVisual();
    }

    private void crearEstructuraVisual() {
        // Crear 8 celdas de reserva
        for (int i = 0; i < 8; i++) {
            StackPane reserva = crearCeldaReserva(i);
            reservasPanes.add(reserva);
            reservasContainer.getChildren().add(reserva);
        }

        // Crear 4 foundations
        Palo[] palos = Palo.values();
        for (int i = 0; i < 4; i++) {
            VBox foundationBox = crearFoundationBox(i, palos[i]);
            foundationsContainer.getChildren().add(foundationBox);
        }

        // Crear 8 tableaus
        for (int i = 0; i < 8; i++) {
            VBox tableau = crearTableauColumn(i);
            tableausPanes.add(tableau);
            tableausContainer.getChildren().add(tableau);
            HBox.setHgrow(tableau, Priority.ALWAYS);
        }
    }

    private StackPane crearCeldaReserva(int index) {
        StackPane stack = new StackPane();
        stack.setPrefSize(90, 120);
        stack.setMinSize(90, 120);
        stack.setMaxSize(90, 120);

        Rectangle fondo = new Rectangle(90, 120);
        fondo.setFill(Color.rgb(26, 92, 26, 0.6));
        fondo.setStroke(Color.rgb(74, 124, 89));
        fondo.setStrokeWidth(3);
        fondo.setArcWidth(12);
        fondo.setArcHeight(12);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setRadius(5);
        shadow.setOffsetX(2);
        shadow.setOffsetY(2);
        fondo.setEffect(shadow);

        stack.getChildren().add(fondo);
        stack.setUserData("R" + index);

        configurarEventosReserva(stack, index);

        return stack;
    }

    private VBox crearFoundationBox(int index, Palo palo) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);

        // Símbolo del palo
        Label simbolo = new Label(palo.getFigura());
        simbolo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        simbolo.setTextFill(palo.getColor().equals("rojo") ? Color.rgb(220, 20, 60) : Color.WHITE);

        DropShadow textShadow = new DropShadow();
        textShadow.setColor(Color.BLACK);
        textShadow.setRadius(2);
        simbolo.setEffect(textShadow);

        // Pane de la foundation
        StackPane foundationPane = new StackPane();
        foundationPane.setPrefSize(90, 120);
        foundationPane.setMinSize(90, 120);
        foundationPane.setMaxSize(90, 120);

        Rectangle fondo = new Rectangle(90, 120);
        fondo.setFill(Color.rgb(80, 45, 22, 0.6));
        fondo.setStroke(Color.rgb(139, 69, 19));
        fondo.setStrokeWidth(3);
        fondo.setArcWidth(12);
        fondo.setArcHeight(12);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setRadius(5);
        shadow.setOffsetX(2);
        shadow.setOffsetY(2);
        fondo.setEffect(shadow);

        foundationPane.getChildren().add(fondo);
        foundationPane.setUserData("F" + index);

        foundationsPanes.add(foundationPane);
        configurarEventosFoundation(foundationPane, index);

        box.getChildren().addAll(simbolo, foundationPane);
        return box;
    }

    private VBox crearTableauColumn(int index) {
        VBox column = new VBox();
        column.setAlignment(Pos.TOP_CENTER);
        column.setMinWidth(95);
        column.setPrefWidth(95);
        column.setSpacing(-70); // Espacio negativo para apilar cartas
        column.setStyle("-fx-background-color: rgba(26, 92, 26, 0.3);" +
                "-fx-border-color: rgba(74, 124, 89, 0.6);" +
                "-fx-border-width: 2;" +
                "-fx-border-style: dashed;" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;" +
                "-fx-padding: 10;");
        column.setMinHeight(400);
        column.setUserData("T" + index);

        // Placeholder para columna vacía
        Label placeholder = new Label("K");
        placeholder.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        placeholder.setTextFill(Color.rgb(255, 255, 255, 0.2));
        placeholder.setUserData("placeholder");
        column.getChildren().add(placeholder);

        configurarEventosTableau(column, index);

        return column;
    }

    @FXML
    private void handleNuevoJuego() {
        juego.iniciarNuevaPartida();
        movimientos = 0;
        limpiarSeleccion();
        actualizarInterfaz();
        lblEstado.setText("Nueva partida iniciada. ¡Buena suerte!");
        lblMovimientos.setText("Movimientos: 0");

        // Animación de inicio
        ScaleTransition st = new ScaleTransition(Duration.millis(300), tableausContainer);
        st.setFromX(0.9);
        st.setFromY(0.9);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    @FXML
    private void handleUndo() {
        if (juego.deshacer()) {
            movimientos = Math.max(0, movimientos - 1);
            actualizarInterfaz();
            lblMovimientos.setText("Movimientos: " + movimientos);
            lblEstado.setText("Movimiento deshecho");
        } else {
            lblEstado.setText("No hay movimientos para deshacer");
        }
        limpiarSeleccion();
    }

    @FXML
    private void handlePista() {
        Movimiento pista = juego.pista();
        if (pista == null) {
            lblEstado.setText("No hay movimientos disponibles");
            return;
        }

        String msg = construirMensajePista(pista);
        lblEstado.setText("Pista: " + msg);

        // Resaltar las áreas involucradas
        resaltarMovimientoPista(pista);
    }

    private String construirMensajePista(Movimiento mov) {
        String origen = mov.getOrigen() == Movimiento.Zona.TABLEAU ? "Tableau " + (mov.getIndiceOrigen() + 1) :
                mov.getOrigen() == Movimiento.Zona.RESERVA ? "Reserva " + (mov.getIndiceOrigen() + 1) :
                        "Foundation";

        String destino = mov.getDestino() == Movimiento.Zona.TABLEAU ? "Tableau " + (mov.getIndiceDestino() + 1) :
                mov.getDestino() == Movimiento.Zona.RESERVA ? "Reserva " + (mov.getIndiceDestino() + 1) :
                        "Foundation";

        return "Mover de " + origen + " a " + destino;
    }

    private void resaltarMovimientoPista(Movimiento mov) {
        // Implementar resaltado temporal de las áreas involucradas
        // Por simplicidad, solo mostramos el mensaje
    }

    private void actualizarInterfaz() {
        actualizarReservas();
        actualizarFoundations();
        actualizarTableaus();
    }

    private void actualizarReservas() {
        Celda[] reservas = juego.getReservas();

        for (int i = 0; i < 8; i++) {
            StackPane pane = reservasPanes.get(i);
            // Limpiar contenido anterior (excepto el fondo)
            while (pane.getChildren().size() > 1) {
                pane.getChildren().remove(1);
            }

            Carta carta = reservas[i].verCarta();
            if (carta != null) {
                StackPane cartaPane = crearCartaVisual(carta, false);
                pane.getChildren().add(cartaPane);
            }
        }
    }

    private void actualizarFoundations() {
        Foundation[] foundations = juego.getFoundations();

        for (int i = 0; i < 4; i++) {
            StackPane pane = foundationsPanes.get(i);
            // Limpiar contenido anterior (excepto el fondo)
            while (pane.getChildren().size() > 1) {
                pane.getChildren().remove(1);
            }

            Carta carta = foundations[i].verUltima();
            if (carta != null) {
                StackPane cartaPane = crearCartaVisual(carta, false);
                pane.getChildren().add(cartaPane);
            }
        }
    }

    private void actualizarTableaus() {
        Tableu[] tableaus = juego.getTableaus();

        for (int i = 0; i < 8; i++) {
            VBox column = tableausPanes.get(i);
            column.getChildren().clear();

            ListaSimple<Carta> cartas = tableaus[i].getTableau();
            List<Carta> listaCartas = cartas.convertirLista();

            if (listaCartas.isEmpty()) {
                // Mostrar placeholder
                Label placeholder = new Label("K");
                placeholder.setFont(Font.font("Arial", FontWeight.BOLD, 24));
                placeholder.setTextFill(Color.rgb(255, 255, 255, 0.2));
                placeholder.setUserData("placeholder");
                column.getChildren().add(placeholder);
            } else {
                for (int j = 0; j < listaCartas.size(); j++) {
                    Carta carta = listaCartas.get(j);
                    boolean esUltima = (j == listaCartas.size() - 1);

                    StackPane cartaPane = crearCartaVisual(carta, esUltima);
                    cartaPane.setUserData("T" + i + "-C" + j);

                    if (esUltima) {
                        configurarDragCarta(cartaPane, i);
                    }

                    column.getChildren().add(cartaPane);
                }
            }
        }
    }

    private StackPane crearCartaVisual(Carta carta, boolean interactiva) {
        StackPane cardContainer = new StackPane();
        cardContainer.setPrefSize(85, 115);
        cardContainer.setMinSize(85, 115);
        cardContainer.setMaxSize(85, 115);

        // Fondo de la carta
        Rectangle cardBg = new Rectangle(85, 115);
        cardBg.setFill(Color.WHITE);
        cardBg.setStroke(Color.rgb(51, 51, 51));
        cardBg.setStrokeWidth(2);
        cardBg.setArcWidth(10);
        cardBg.setArcHeight(10);

        DropShadow cardShadow = new DropShadow();
        cardShadow.setColor(Color.rgb(0, 0, 0, 0.4));
        cardShadow.setRadius(4);
        cardShadow.setOffsetX(2);
        cardShadow.setOffsetY(2);
        cardBg.setEffect(cardShadow);

        cardContainer.getChildren().add(cardBg);

        // Valor de la carta
        String valorStr = obtenerValorString(carta.getValor());
        Color colorCarta = carta.getColor().equals("rojo") ? Color.rgb(220, 20, 60) : Color.BLACK;

        // Valor superior izquierda
        Label valorSup = new Label(valorStr);
        valorSup.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        valorSup.setTextFill(colorCarta);
        StackPane.setAlignment(valorSup, Pos.TOP_LEFT);
        StackPane.setMargin(valorSup, new Insets(5, 0, 0, 8));

        // Palo centro
        Label paloCentro = new Label(carta.getPalo().getFigura());
        paloCentro.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        paloCentro.setTextFill(colorCarta);
        StackPane.setAlignment(paloCentro, Pos.CENTER);

        // Valor inferior derecha (invertido)
        Label valorInf = new Label(valorStr);
        valorInf.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        valorInf.setTextFill(colorCarta);
        valorInf.setRotate(180);
        StackPane.setAlignment(valorInf, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(valorInf, new Insets(0, 8, 5, 0));

        cardContainer.getChildren().addAll(valorSup, paloCentro, valorInf);

        if (interactiva) {
            cardContainer.setOnMouseEntered(e -> {
                Glow glow = new Glow(0.4);
                cardBg.setEffect(glow);
                cardContainer.setCursor(javafx.scene.Cursor.HAND);
            });

            cardContainer.setOnMouseExited(e -> {
                cardBg.setEffect(cardShadow);
                cardContainer.setCursor(javafx.scene.Cursor.DEFAULT);
            });
        }

        return cardContainer;
    }

    private String obtenerValorString(int valor) {
        return switch (valor) {
            case 14 -> "A";
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            default -> String.valueOf(valor);
        };
    }

    private void configurarEventosReserva(StackPane pane, int index) {
        pane.setOnMouseClicked(e -> {
            if (origenSeleccionado == null) {
                // Seleccionar origen
                Carta carta = juego.getTopReservas(index);
                if (carta != null) {
                    origenSeleccionado = "R" + index;
                    paneSeleccionado = pane;
                    resaltarSeleccion(pane, true);
                    lblEstado.setText("Carta seleccionada de Reserva " + (index + 1) + ". Elige destino.");
                }
            } else {
                // Intentar mover aquí
                ejecutarMovimiento("R" + index);
            }
        });

        configurarDragOver(pane);
        configurarDragDrop(pane, "R" + index);
    }

    private void configurarEventosFoundation(StackPane pane, int index) {
        pane.setOnMouseClicked(e -> {
            if (origenSeleccionado != null) {
                ejecutarMovimiento("F" + index);
            }
        });

        configurarDragOver(pane);
        configurarDragDrop(pane, "F" + index);
    }

    private void configurarEventosTableau(VBox column, int index) {
        column.setOnMouseClicked(e -> {
            if (e.getTarget() == column ||
                    (e.getTarget() instanceof Label && "placeholder".equals(((Label)e.getTarget()).getUserData()))) {
                if (origenSeleccionado != null) {
                    ejecutarMovimiento("T" + index);
                }
            }
        });

        configurarDragOver(column);
        configurarDragDrop(column, "T" + index);
    }

    private void configurarDragCarta(StackPane cartaPane, int tableauIndex) {
        cartaPane.setOnDragDetected(e -> {
            Dragboard db = cartaPane.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("T" + tableauIndex);
            db.setContent(content);

            origenSeleccionado = "T" + tableauIndex;
            paneSeleccionado = cartaPane;

            e.consume();
        });
    }

    private void configurarDragOver(Region region) {
        region.setOnDragOver(e -> {
            if (e.getGestureSource() != region && e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });

        region.setOnDragEntered(e -> {
            if (e.getGestureSource() != region && e.getDragboard().hasString()) {
                Glow glow = new Glow(0.6);
                region.setEffect(glow);
                region.setStyle(region.getStyle() + "-fx-border-color: #FFD700; -fx-border-width: 4;");
            }
            e.consume();
        });

        region.setOnDragExited(e -> {
            region.setEffect(null);
            String style = region.getStyle();
            region.setStyle(style.replaceAll("-fx-border-color: #FFD700; -fx-border-width: 4;", ""));
            e.consume();
        });
    }

    private void configurarDragDrop(Region region, String destino) {
        region.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String origen = db.getString();
                origenSeleccionado = origen;
                success = ejecutarMovimientoInterno(destino);
            }

            region.setEffect(null);
            String style = region.getStyle();
            region.setStyle(style.replaceAll("-fx-border-color: #FFD700; -fx-border-width: 4;", ""));

            e.setDropCompleted(success);
            e.consume();

            limpiarSeleccion();
        });
    }

    private void ejecutarMovimiento(String destino) {
        if (origenSeleccionado == null) return;

        boolean exito = ejecutarMovimientoInterno(destino);

        if (!exito) {
            lblEstado.setText("❌ Movimiento inválido");
        }

        limpiarSeleccion();
    }

    private boolean ejecutarMovimientoInterno(String destino) {
        if (origenSeleccionado == null) return false;

        char origenTipo = origenSeleccionado.charAt(0);
        int origenIdx = Integer.parseInt(origenSeleccionado.substring(1));
        char destinoTipo = destino.charAt(0);
        int destinoIdx = Integer.parseInt(destino.substring(1));

        boolean exito = false;

        // Tableau a Tableau
        if (origenTipo == 'T' && destinoTipo == 'T') {
            // Intentar mover múltiples cartas si es posible
            exito = juego.moverTaT(origenIdx, destinoIdx, 1);
        }
        // Tableau a Reserva
        else if (origenTipo == 'T' && destinoTipo == 'R') {
            exito = juego.moverTaR(origenIdx, destinoIdx);
        }
        // Tableau a Foundation
        else if (origenTipo == 'T' && destinoTipo == 'F') {
            exito = juego.moverTaF(origenIdx, destinoIdx);
        }
        // Reserva a Tableau
        else if (origenTipo == 'R' && destinoTipo == 'T') {
            exito = juego.moverRaT(origenIdx, destinoIdx);
        }
        // Reserva a Foundation
        else if (origenTipo == 'R' && destinoTipo == 'F') {
            exito = juego.moverRaF(origenIdx, destinoIdx);
        }

        if (exito) {
            movimientos++;
            actualizarInterfaz();
            lblMovimientos.setText("Movimientos: " + movimientos);
            lblEstado.setText("✓ Movimiento realizado");

            // Verificar victoria
            if (juego.evaluarVictoria()) {
                mostrarVictoria();
            }
        }

        return exito;
    }

    private void resaltarSeleccion(StackPane pane, boolean seleccionar) {
        if (seleccionar) {
            Glow glow = new Glow(0.8);
            pane.setEffect(glow);
            pane.setStyle(pane.getStyle() + "-fx-border-color: yellow; -fx-border-width: 4;");
        } else {
            pane.setEffect(null);
            String style = pane.getStyle();
            pane.setStyle(style.replaceAll("-fx-border-color: yellow; -fx-border-width: 4;", ""));
        }
    }

    private void limpiarSeleccion() {
        if (paneSeleccionado != null) {
            resaltarSeleccion(paneSeleccionado, false);
            paneSeleccionado = null;
        }
        origenSeleccionado = null;
    }

    private void mostrarVictoria() {
        lblEstado.setText("🎉 ¡FELICIDADES! ¡HAS GANADO! 🎉");
        lblEstado.setStyle(lblEstado.getStyle() + "-fx-text-fill: #FFD700; -fx-font-size: 20px;");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("¡Victoria!");
        alert.setHeaderText("🎉 ¡Felicidades! 🎉");
        alert.setContentText("Has completado el juego en " + movimientos + " movimientos.\n\n¡Excelente trabajo!");
        alert.showAndWait();

        // Animación de victoria
        ScaleTransition st = new ScaleTransition(Duration.millis(500), lblEstado);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setCycleCount(4);
        st.setAutoReverse(true);
        st.play();
    }
}
