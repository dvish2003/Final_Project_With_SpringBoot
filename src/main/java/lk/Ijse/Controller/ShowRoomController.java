package lk.Ijse.Controller;

import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.Ijse.Model.Customer;
import lk.Ijse.Model.ShowRoom;
import lk.Ijse.Util.CustomerRegex;
import lk.Ijse.Util.CustomerTextField;
import lk.Ijse.repository.CustomerRepo;
import lk.Ijse.repository.EmployeeRepo;
import lk.Ijse.repository.ShowRoomRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ShowRoomController {
    public Label lblRegisterId;
    @FXML
    private  Button btnHome;
    @FXML
    private AnchorPane ShowRoomPane;

    @FXML
    private Button btnSrClean;

    @FXML
    private Button btnSrDelete;



    @FXML
    private Button btnSrSave;

    @FXML
    private Button btnSrUpdate;

    @FXML
    private TableColumn<?, ?> colLocation;

    @FXML
    private TableColumn<?, ?> colShowRoom;

    @FXML
    private TableView<ShowRoom> colSrTel;



    @FXML
    private TextField txtSrLocation;



    @FXML
    void btnPrCleanOnAction(ActionEvent event) {
        clearFields();

    }

    private void clearFields() {
        txtSrLocation.clear();
        getCurrentShowRomD();
    }

    @FXML
    void btnPrDeleteOnAction(ActionEvent event) {

        String id = lblRegisterId.getText();
        try {
            boolean isDeleted = ShowRoomRepo.delete(id);
            if (isDeleted) {
                ShowRoom showRoom = (ShowRoom) colSrTel.getSelectionModel().getSelectedItem();
                if (showRoom != null) {
                    colSrTel.getItems().remove(showRoom);
                    new Alert(Alert.AlertType.CONFIRMATION, "ShowRoom deleted successfully!").show();
                    clearFields();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete ShowRoom!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error occurred while deleting ShowRoom: " + e.getMessage()).show();
        }
    }




    @FXML
    void btnPrSaveOnAction(ActionEvent event) {
        String id = lblRegisterId.getText();
        String location = txtSrLocation.getText();


        ShowRoom showRoom = new ShowRoom(id,location);

        try {
            boolean isSaved = ShowRoomRepo.save(showRoom);
            if (isSaved) {

                colSrTel.getItems().add(showRoom);
                new Alert(Alert.AlertType.CONFIRMATION, "ShowRoom saved successfully!").show();
                clearFields();
                getCurrentShowRomD();

            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save ShowRoom!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error occurred while saving ShowRoom: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnPrUpdateOnAction(ActionEvent event) {
        String id = lblRegisterId.getText();
        String location = txtSrLocation.getText();


        ShowRoom showRoom = new ShowRoom(id,location);
        try {
            boolean isUpdated = ShowRoomRepo.update(showRoom);
            if (isUpdated) {
                ShowRoom selectedItem = colSrTel.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    int selectedIndex = colSrTel.getItems().indexOf(selectedItem);
                    colSrTel.getItems().set(selectedIndex, showRoom);
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer updated successfully!").show();
                    clearFields();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update customer!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error occurred while updating customer: " + e.getMessage()).show();
        }

    }
    public void initialize() {
        setCellValueFactory();
        loadAllShowRoom();
        applyButtonAnimations();
        getCurrentShowRomD();
        addHoverHandlers(btnSrClean);
        addHoverHandlers(btnSrUpdate);
        addHoverHandlers(btnSrDelete);
        addHoverHandlers(btnSrSave);
        addHoverHandlers(btnHome);


        txtSrLocation.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    btnSrSave.requestFocus();
                }
            });


        colSrTel.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {

                lblRegisterId.setText(newSelection.getShowRoomId());
                txtSrLocation.setText(newSelection.getShowRoomLocation());

            }
        });
    }
    private void applyButtonAnimations() {

        applyAnimation(btnSrClean);
        applyAnimation(btnSrUpdate);
        applyAnimation(btnSrDelete);
        applyAnimation(btnSrSave);
        applyAnimation(btnHome);


    }

    private void applyAnimation(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
        button.setOnMouseEntered(event -> {
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
        });
        button.setOnMouseExited(event -> {
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();
        });
    }

    private void setCellValueFactory() {
        colShowRoom.setCellValueFactory(new PropertyValueFactory<>("showRoomId"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("showRoomLocation"));
    }

    private void loadAllShowRoom() {
        ObservableList<ShowRoom> obList = FXCollections.observableArrayList();
        try {
            List<ShowRoom> showRoomList = ShowRoomRepo.getAll();
            for (ShowRoom showRoom : showRoomList) {
                ShowRoom tm = new ShowRoom(
                        showRoom.getShowRoomId(),
                        showRoom.getShowRoomLocation()

                );
                obList.add(tm);
            }
            colSrTel.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void btnHomeOnAction(ActionEvent event) throws IOException {
        Button btn = (Button) event.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/DashBoard_from.fxml"));
        Parent rootNode = loader.load();

        Scene scene = new Scene(rootNode);
        stage.setScene(scene);

        stage.show();
    }
    private void addHoverHandlers(Button button) {// button Animation
        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-background-color: Black; -fx-text-fill: white;");
        });
        button.setOnMouseExited(event -> {
            button.setStyle("-fx-background-color:  #1e272e; -fx-text-fill: white;");
        });
    }


    private void getCurrentShowRomD() {
        try {
            String currentId = ShowRoomRepo.getBookingCurrentId();

            String nextShowRoomID = generateNextShowRoom(currentId);
            lblRegisterId.setText(nextShowRoomID);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextShowRoom(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("S");
            int idNum = Integer.parseInt(split[1]);
            idNum++;
            return "S" + String.format("%03d", idNum);
        }
        return "S001";

}


public void LocationK(KeyEvent keyEvent) {
      //  CustomerRegex.setTextColor(CustomerTextField.,txtShowRoomID);

    }
}
