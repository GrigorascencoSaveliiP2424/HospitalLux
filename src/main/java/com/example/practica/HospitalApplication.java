package com.example.practica;
import javafx.stage.StageStyle;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HospitalApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader splashLoader =
                new FXMLLoader(getClass().getResource("splash-view.fxml"));
        Scene splashScene = new Scene(splashLoader.load());

        stage.getIcons().add(
                new Image(getClass().getResourceAsStream("/com/example/practica/icons/hospitallux.png"))
        );

        stage.setTitle("HospitalLux");

        stage.setScene(splashScene);

        stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());

        stage.setMaximized(true);

        stage.show();

        PauseTransition pause =
                new PauseTransition(Duration.seconds(2.5));

        pause.setOnFinished(event -> {

            try {

                FXMLLoader mainLoader =
                        new FXMLLoader(getClass().getResource("main-view.fxml"));

                Scene mainScene =
                        new Scene(mainLoader.load());

                stage.setScene(mainScene);

                stage.setWidth(
                        Screen.getPrimary().getVisualBounds().getWidth()
                );

                stage.setHeight(
                        Screen.getPrimary().getVisualBounds().getHeight()
                );

                stage.setMaximized(true);

                stage.centerOnScreen();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        pause.play();
    }

    public static void main(String[] args) {
        launch();
    }
}