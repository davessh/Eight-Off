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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EightOffJuego/eight-off-game.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 1200, 650);
            primaryStage.setTitle("Eight Off Solitaire - Juego de Cartas");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(1200);
            primaryStage.setMinHeight(720);
            primaryStage.setResizable(true);
            primaryStage.setFullScreen(true);
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
