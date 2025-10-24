package interfaz;

import Cards.*;
import EightOffJuego.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class EightOffJuegoController {

    @FXML private Button btnNuevoJuego;
    @FXML private Button btnMenu;
    @FXML private Button btnUndo;
    @FXML private Button btnPista;

    @FXML private Pane foundationSpades;
    @FXML private Pane foundationHearts;
    @FXML private Pane foundationDiamonds;
    @FXML private Pane foundationClubs;

    @FXML private Pane reserva0, reserva1, reserva2, reserva3;
    @FXML private Pane reserva4, reserva5, reserva6, reserva7;

    @FXML private Pane tableau0, tableau1, tableau2, tableau3;
    @FXML private Pane tableau4, tableau5, tableau6, tableau7;

    @FXML private Label lblEstado;
    @FXML private Label lblMovimientos;
    @FXML private Label lblTiempo;

    private EightOff juego;
    private ArrayList<Pane> tableauPanes;
    private ArrayList<Pane> reservaPanes;
    private ArrayList<Pane> foundationPanes;

    private int movimientos = 0;
    private Timeline timer;
    private int segundos = 0;

    // Variables para drag and drop con SuperMove
    private int sourceTableauIndex = -1;
    private int sourceReservaIndex = -1;
    private int cantidadCartasArrastradas = 1;

    @FXML
    private void initialize() {
        juego = new EightOff();

        // Inicializar listas de panes
        tableauPanes = new ArrayList<>();
        tableauPanes.add(tableau0);
        tableauPanes.add(tableau1);
        tableauPanes.add(tableau2);
        tableauPanes.add(tableau3);
        tableauPanes.add(tableau4);
        tableauPanes.add(tableau5);
        tableauPanes.add(tableau6);
        tableauPanes.add(tableau7);

        reservaPanes = new ArrayList<>();
        reservaPanes.add(reserva0);
        reservaPanes.add(reserva1);
        reservaPanes.add(reserva2);
        reservaPanes.add(reserva3);
        reservaPanes.add(reserva4);
        reservaPanes.add(reserva5);
        reservaPanes.add(reserva6);
        reservaPanes.add(reserva7);

        foundationPanes = new ArrayList<>();
        foundationPanes.add(foundationClubs);
        foundationPanes.add(foundationDiamonds);
        foundationPanes.add(foundationHearts);
        foundationPanes.add(foundationSpades);

        juego.iniciarNuevaPartida();
        configurarEventos();
        inicializarTimer();
        actualizarInterfaz();
        actualizarBotonUndo();
        verificarMovimientosDisponibles();
    }

    private void inicializarTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            segundos++;
            int minutos = segundos / 60;
            int segs = segundos % 60;
            lblTiempo.setText(String.format("Tiempo: %02d:%02d", minutos, segs));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    @FXML
    private void handleNuevoJuego() {
        juego = new EightOff();
        juego.iniciarNuevaPartida();
        movimientos = 0;
        segundos = 0;
        if (timer != null) {
            timer.stop();
        }
        inicializarTimer();
        actualizarInterfaz();
        actualizarBotonUndo();
        verificarMovimientosDisponibles();
        lblEstado.setText("Nuevo juego iniciado");
        lblMovimientos.setText("Movimientos: 0");
    }

    @FXML
    private void handleVolverMenu() {
        if (timer != null) {
            timer.stop();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/interfaz/menu-principal.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnMenu.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Eight Off - Menú");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUndo() {
        if (juego.deshacer()) {
            movimientos = Math.max(0, movimientos - 1);
            actualizarInterfaz();
            actualizarBotonUndo();
            lblMovimientos.setText("Movimientos: " + movimientos);
            lblEstado.setText("Movimiento deshecho");
            verificarMovimientosDisponibles();
        } else {
            lblEstado.setText("No hay movimientos para deshacer");
        }
    }

    @FXML
    private void handlePista() {
        Movimiento pista = juego.pista();
        if (pista == null) {
            lblEstado.setText("No hay movimientos disponibles");
            return;
        }

        // Iluminar la jugada recomendada
        limpiarIluminacion();

        Pane origenPane = null;
        Pane destinoPane = null;

        switch (pista.getOrigen()) {
            case TABLEAU:
                origenPane = tableauPanes.get(pista.getIndiceOrigen());
                break;
            case RESERVA:
                origenPane = reservaPanes.get(pista.getIndiceOrigen());
                break;
        }

        switch (pista.getDestino()) {
            case TABLEAU:
                destinoPane = tableauPanes.get(pista.getIndiceDestino());
                break;
            case RESERVA:
                destinoPane = reservaPanes.get(pista.getIndiceDestino());
                break;
            case FUNDACION:
                destinoPane = foundationPanes.get(pista.getIndiceDestino());
                break;
        }

        if (origenPane != null) {
            origenPane.setStyle(origenPane.getStyle() + "-fx-border-color: #FFD700; -fx-border-width: 5;");
            origenPane.setEffect(new Glow(0.8));
        }

        if (destinoPane != null) {
            destinoPane.setStyle(destinoPane.getStyle() + "-fx-border-color: #00FF00; -fx-border-width: 5;");
            destinoPane.setEffect(new Glow(0.8));
        }

        lblEstado.setText("Pista: Mover desde " + pista.getOrigen() + " a " + pista.getDestino());

        // Limpiar iluminación después de 3 segundos
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            limpiarIluminacion();
            actualizarInterfaz();
        }));
        timeline.play();
    }

    private void limpiarIluminacion() {
        for (Pane pane : tableauPanes) {
            pane.setEffect(null);
        }
        for (Pane pane : reservaPanes) {
            pane.setEffect(null);
        }
        for (Pane pane : foundationPanes) {
            pane.setEffect(null);
        }
    }

    private void actualizarBotonUndo() {
        if (btnUndo != null) {
            btnUndo.setDisable(juego.deshacer() == false);
            juego.deshacer(); // Revertir la verificación
        }
    }

    private void verificarMovimientosDisponibles() {
        if (juego.evaluarVictoria()) {
            if (timer != null) {
                timer.stop();
            }
            lblEstado.setText("¡VICTORIA! ¡Felicidades!");
            lblEstado.setStyle(lblEstado.getStyle() + "-fx-text-fill: #00FF00;");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("¡Victoria!");
            alert.setHeaderText("¡Felicidades!");
            alert.setContentText("Has completado el juego en " + movimientos + " movimientos y " +
                    segundos + " segundos.");
            alert.showAndWait();
            return;
        }

        if (juego.sinMovimientos()) {
            if (timer != null) {
                timer.stop();
            }
            lblEstado.setText("No hay más movimientos válidos - Juego terminado");

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Juego Terminado");
            alert.setHeaderText("No hay más movimientos disponibles");
            alert.setContentText("El juego ha terminado. No hay movimientos válidos restantes.");
            alert.showAndWait();
        }
    }

    private void configurarEventos() {
        // Configurar drag and drop para tableaux
        for (int i = 0; i < tableauPanes.size(); i++) {
            configurarTableau(tableauPanes.get(i), i);
        }

        // Configurar drag and drop para reservas
        for (int i = 0; i < reservaPanes.size(); i++) {
            configurarReserva(reservaPanes.get(i), i);
        }

        // Configurar drop para foundations
        for (int i = 0; i < foundationPanes.size(); i++) {
            configurarFoundation(foundationPanes.get(i), i);
        }
    }

    private void configurarTableau(Pane tableau, int index) {
        // Drag detected
        tableau.setOnDragDetected(e -> {
            Carta carta = juego.getTopTableau(index);
            if (carta == null) return;

            // Calcular cuántas cartas se pueden mover (SuperMove)
            int reservasLibres = contarReservasLibres();
            int tableauxVacios = contarTableauxVacios(index);
            cantidadCartasArrastradas = calcularSuperMove(index, reservasLibres, tableauxVacios);

            Dragboard db = tableau.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("tableau-" + index + "-" + cantidadCartasArrastradas);
            db.setContent(content);
            sourceTableauIndex = index;
            sourceReservaIndex = -1;
            e.consume();
        });

        // Drag over
        tableau.setOnDragOver(e -> {
            if (e.getGestureSource() != tableau && e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });

        // Drag entered
        tableau.setOnDragEntered(e -> {
            if (e.getGestureSource() != tableau && e.getDragboard().hasString()) {
                tableau.setEffect(new Glow(0.5));
            }
            e.consume();
        });

        // Drag exited
        tableau.setOnDragExited(e -> {
            tableau.setEffect(null);
            e.consume();
        });

        // Drag dropped
        tableau.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String source = db.getString();

                if (source.startsWith("tableau")) {
                    String[] parts = source.split("-");
                    int sourceIndex = Integer.parseInt(parts[1]);
                    int cantidad = Integer.parseInt(parts[2]);

                    success = juego.moverTaT(sourceIndex, index, cantidad);
                } else if (source.startsWith("reserva")) {
                    int sourceIndex = Integer.parseInt(source.split("-")[1]);
                    success = juego.moverRaT(sourceIndex, index);
                }

                if (success) {
                    movimientos++;
                    actualizarInterfaz();
                    actualizarBotonUndo();
                    lblMovimientos.setText("Movimientos: " + movimientos);
                    verificarMovimientosDisponibles();
                }
            }

            tableau.setEffect(null);
            e.setDropCompleted(success);
            e.consume();
        });
    }

    private void configurarReserva(Pane reserva, int index) {
        // Drag detected
        reserva.setOnDragDetected(e -> {
            Carta carta = juego.getTopReservas(index);
            if (carta == null) return;

            Dragboard db = reserva.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString("reserva-" + index);
            db.setContent(content);
            sourceReservaIndex = index;
            sourceTableauIndex = -1;
            e.consume();
        });

        // Drag over
        reserva.setOnDragOver(e -> {
            if (e.getGestureSource() != reserva && e.getDragboard().hasString()) {
                String source = e.getDragboard().getString();
                if (source.startsWith("tableau")) {
                    e.acceptTransferModes(TransferMode.MOVE);
                }
            }
            e.consume();
        });

        // Drag entered
        reserva.setOnDragEntered(e -> {
            if (e.getGestureSource() != reserva && e.getDragboard().hasString()) {
                reserva.setEffect(new Glow(0.5));
            }
            e.consume();
        });

        // Drag exited
        reserva.setOnDragExited(e -> {
            reserva.setEffect(null);
            e.consume();
        });

        // Drag dropped
        reserva.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String source = db.getString();

                if (source.startsWith("tableau")) {
                    String[] parts = source.split("-");
                    int sourceIndex = Integer.parseInt(parts[1]);
                    success = juego.moverTaR(sourceIndex, index);
                }

                if (success) {
                    movimientos++;
                    actualizarInterfaz();
                    actualizarBotonUndo();
                    lblMovimientos.setText("Movimientos: " + movimientos);
                    verificarMovimientosDisponibles();
                }
            }

            reserva.setEffect(null);
            e.setDropCompleted(success);
            e.consume();
        });
    }

    private void configurarFoundation(Pane foundation, int index) {
        // Drag over
        foundation.setOnDragOver(e -> {
            if (e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.MOVE);
            }
            e.consume();
        });

        // Drag entered
        foundation.setOnDragEntered(e -> {
            if (e.getDragboard().hasString()) {
                foundation.setEffect(new Glow(0.8));
                String currentStyle = foundation.getStyle();
                foundation.setStyle(currentStyle + "-fx-border-color: #FFD700; -fx-border-width: 4;");
            }
            e.consume();
        });

        // Drag exited
        foundation.setOnDragExited(e -> {
            foundation.setEffect(null);
            e.consume();
        });

        // Drag dropped
        foundation.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                String source = db.getString();

                if (source.startsWith("tableau")) {
                    String[] parts = source.split("-");
                    int sourceIndex = Integer.parseInt(parts[1]);
                    success = juego.moverTaF(sourceIndex, index);
                } else if (source.startsWith("reserva")) {
                    int sourceIndex = Integer.parseInt(source.split("-")[1]);
                    success = juego.moverRaF(sourceIndex, index);
                }

                if (success) {
                    movimientos++;
                    actualizarInterfaz();
                    actualizarBotonUndo();
                    lblMovimientos.setText("Movimientos: " + movimientos);
                    verificarMovimientosDisponibles();
                }
            }

            foundation.setEffect(null);
            e.setDropCompleted(success);
            e.consume();
        });
    }

    private int contarReservasLibres() {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            if (juego.getTopReservas(i) == null) {
                count++;
            }
        }
        return count;
    }

    private int contarTableauxVacios(int excluyendo) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            if (i != excluyendo && juego.getTopTableau(i) == null) {
                count++;
            }
        }
        return count;
    }

    private int calcularSuperMove(int tableauIndex, int reservasLibres, int tableauxVacios) {
        // Fórmula SuperMove: (1 + reservasLibres) * 2^tableauxVacios
        int maxMovibles = (1 + reservasLibres) * (int)Math.pow(2, tableauxVacios);

        // Obtener cuántas cartas forman una secuencia válida en el tableau
        Tableu tableau = juego.getTableaus()[tableauIndex];
        ListaSimple<Carta> cartas = tableau.getTableau();
        int tam = cartas.getSize();

        if (tam == 0) return 0;

        int secuenciaValida = 1;
        for (int i = tam - 1; i > 0; i--) {
            Carta actual = cartas.getPosicion(i);
            Carta anterior = cartas.getPosicion(i - 1);

            if (actual.getPalo() == anterior.getPalo() &&
                    actual.getValor() + 1 == anterior.getValor()) {
                secuenciaValida++;
            } else {
                break;
            }
        }

        return Math.min(secuenciaValida, maxMovibles);
    }

    private void actualizarInterfaz() {
        actualizarTableaux();
        actualizarReservas();
        actualizarFoundations();
    }

    private void actualizarTableaux() {
        Tableu[] tableaux = juego.getTableaus();

        for (int i = 0; i < tableaux.length; i++) {
            Pane tableauPane = tableauPanes.get(i);
            tableauPane.getChildren().clear();

            ListaSimple<Carta> cartas = tableaux[i].getTableau();
            int tam = cartas.getSize();

            double yOffset = 10;
            double cardSpacing = 20;
            double xCentered = 5;

            for (int j = 0; j < tam; j++) {
                Carta carta = cartas.getPosicion(j);
                Label cartaLabel = crearLabelCarta(carta);

                cartaLabel.setLayoutX(xCentered);
                cartaLabel.setLayoutY(yOffset);

                yOffset += cardSpacing;

                tableauPane.getChildren().add(cartaLabel);
            }
        }
    }

    private void actualizarReservas() {
        Celda[] reservas = juego.getReservas();

        for (int i = 0; i < reservas.length; i++) {
            Pane reservaPane = reservaPanes.get(i);
            reservaPane.getChildren().clear();

            Carta carta = reservas[i].verCarta();
            if (carta != null) {
                Label cartaLabel = crearLabelCarta(carta);
                cartaLabel.setLayoutX(5);
                cartaLabel.setLayoutY(5);
                reservaPane.getChildren().add(cartaLabel);
            }
        }
    }

    private void actualizarFoundations() {
        Foundation[] foundations = juego.getFoundations();

        for (int i = 0; i < foundations.length; i++) {
            Pane foundationPane = foundationPanes.get(i);
            foundationPane.getChildren().clear();

            Carta ultimaCarta = foundations[i].verUltima();
            if (ultimaCarta != null) {
                Label cartaLabel = crearLabelCarta(ultimaCarta);
                cartaLabel.setLayoutX(5);
                cartaLabel.setLayoutY(5);
                foundationPane.getChildren().add(cartaLabel);
            }
        }
    }

    private Label crearLabelCarta(Carta carta) {
        String valor;
        switch (carta.getValor()) {
            case 14:
                valor = "A";
                break;
            case 11:
                valor = "J";
                break;
            case 12:
                valor = "Q";
                break;
            case 13:
                valor = "K";
                break;
            default:
                valor = String.valueOf(carta.getValor());
        }

        String palo;
        switch (carta.getPalo()) {
            case PICA:
                palo = "♠";
                break;
            case CORAZON:
                palo = "♥";
                break;
            case DIAMANTE:
                palo = "♦";
                break;
            case TREBOL:
                palo = "♣";
                break;
            default:
                palo = "?";
        }

        StackPane stackPane = new StackPane();
        stackPane.setMinSize(60, 85);
        stackPane.setMaxSize(60, 85);
        stackPane.setPrefSize(60, 85);

        String backgroundColor = "white";
        String textColor = carta.getColor().equals("rojo") ? "#DC143C" : "#000000";

        stackPane.setStyle("-fx-background-color: " + backgroundColor + "; " +
                "-fx-border-color: #333333; -fx-border-width: 2; " +
                "-fx-background-radius: 8; -fx-border-radius: 8; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 3, 0.5, 1, 1);");

        Label valorSuperior = new Label(valor);
        valorSuperior.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + textColor + ";");
        StackPane.setAlignment(valorSuperior, javafx.geometry.Pos.TOP_RIGHT);
        javafx.geometry.Insets marginSuperior = new javafx.geometry.Insets(3, 3, 0, 0);
        StackPane.setMargin(valorSuperior, marginSuperior);

        Label paloCentro = new Label(palo);
        paloCentro.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + textColor + ";");
        StackPane.setAlignment(paloCentro, javafx.geometry.Pos.CENTER);

        Label valorInferior = new Label(valor);
        valorInferior.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + textColor + "; -fx-rotate: 180;");
        StackPane.setAlignment(valorInferior, javafx.geometry.Pos.BOTTOM_LEFT);
        javafx.geometry.Insets marginInferior = new javafx.geometry.Insets(0, 0, 3, 3);
        StackPane.setMargin(valorInferior, marginInferior);

        stackPane.getChildren().addAll(valorSuperior, paloCentro, valorInferior);

        Label labelContenedor = new Label();
        labelContenedor.setGraphic(stackPane);
        labelContenedor.setStyle("-fx-padding: 0;");

        return labelContenedor;
    }
}