package lk.Ijse.Controller;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.Ijse.Db.DbConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private static final int PROGRESS_DURATION = 5; // in seconds

    @FXML
    private Label LbLCheck;

    @FXML
    private AnchorPane MainLogAncorPane;

    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserId;

 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtUserId.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                txtPassword.requestFocus();
            }
        });

        txtPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnLogin.fire();
            }
        });
    }

    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {
        String userID = txtUserId.getText();
        String password = txtPassword.getText();

        animateLabelText();
        try {
            checkCredential(userID, password, event);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();

        }
    }

    private void animateLabelText() {
        LbLCheck.setText("");
        String labelText = "Please Enter Valid Detail.....";
        Timeline labelTimeline = new Timeline();
        for (int i = 0; i <= labelText.length(); i++) {
            int finalI = i;
            KeyFrame frame = new KeyFrame(Duration.millis(100 * i), e -> LbLCheck.setText(labelText.substring(0, finalI)));
            labelTimeline.getKeyFrames().add(frame);
        }
        labelTimeline.play();
    }

    private void checkCredential(String userId, String password, ActionEvent event) throws SQLException, IOException {
        String sql = "SELECT Register_id, Register_Password FROM Register WHERE Register_id = ?  ";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, userId);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String dbpw = resultSet.getString("Register_Password");
            if (password.equals(dbpw)) {
                navigateToDashBoard(event,userId);
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid Password").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Invalid UserID").show();
        }
    }

    private void navigateToDashBoard(ActionEvent event,String userId) throws IOException {
        Button btn = (Button) event.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/DashBoard_from.fxml"));
        Parent rootNode = loader.load();DashBoardController dashBoardController = loader.getController();
        dashBoardController.setUserId(userId);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), rootNode);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        Scene scene = new Scene(rootNode);
        stage.setScene(scene);
    }
}
