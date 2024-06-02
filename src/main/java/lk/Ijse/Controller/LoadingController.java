package lk.Ijse.Controller;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class LoadingController {

    @FXML
    private Label LblLoading;

    @FXML
    private Label LblProgessCount;

    @FXML
    private AnchorPane LoadAncorPane2;

    @FXML
    private AnchorPane LoadingAncorPane;

    @FXML
    private Label lblTopic;

    private String fullTitle ="KD.AIRCON INDUSTRIES";
    private int currentIndex = 0;
    @FXML
    private ProgressBar progressBar;

    private double progress = 0.0;

    public void initialize() {
        //animateLabel();
        progressBar.setStyle("-fx-accent: #000000;");

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(progressBar.progressProperty(), 0.5)),
                new KeyFrame(Duration.seconds(4), new KeyValue(progressBar.progressProperty(), 1))
        );

        // Update label with animation
        timeline.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            double progressValue = progressBar.getProgress() * 100;
            LblProgessCount.setText(String.format("%.2f", progressValue) + "%");
        });

        timeline.play();

        timeline.setOnFinished(event -> {
            loadLoginPage();
        });
    }

    private void loadLoginPage() {
        try {
            Stage stage = (Stage) LoadingAncorPane.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LognForm.fxml"));
            Parent rootNode = loader.load();
            Scene scene = new Scene(rootNode);

            stage.setScene(scene);
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), rootNode);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   /* private void animateLabel() {

        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(0.15),
                event -> {
                    if (currentIndex <= fullTitle.length()) {
                        lblTopic.setText(fullTitle.substring(0, currentIndex));
                        currentIndex++;
                    }
                }
        );

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(fullTitle.length() + 1);


        timeline.play();

    }*/
}
