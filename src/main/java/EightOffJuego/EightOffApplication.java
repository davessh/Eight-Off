package EightOffJuego;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EightOffApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EightOffJuego/eight-off-game.fxml"));
            Parent root = loader.load();

            // Configurar la escena
            Scene scene = new Scene(root, 1400, 850);

            // Configurar el stage
            primaryStage.setTitle("Eight Off Solitaire - Juego de Cartas");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1200);
            primaryStage.setMinHeight(750);
            primaryStage.setResizable(true);

            // Mostrar la ventana
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al cargar la interfaz: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
