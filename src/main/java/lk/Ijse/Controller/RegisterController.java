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
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.Ijse.Model.Employee;
import lk.Ijse.Model.Register;
import lk.Ijse.Util.CustomerRegex;
import lk.Ijse.Util.CustomerTextField;
import lk.Ijse.repository.EmployeeRepo;
import lk.Ijse.repository.RegisterRepo;
import lk.Ijse.repository.ShowRoomRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RegisterController {
    @FXML
    private Label lblRegisterId;
    @FXML
    private Label lblRegisterId1;
    @FXML
    private Button SearchBtn;
    @FXML
    private TextField txtSearch;
    @FXML
    private Button btnHome;

    @FXML
    private Button btnClean;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<?, ?> colPassword;

    @FXML
    private TableColumn<?, ?> colPosition;

    @FXML
    private TableView<Register> colRegiTel;

    @FXML
    private TableColumn<?, ?> colUserId;

    @FXML
    private TableColumn<?, ?> colUserName;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtPost;



    public void initialize() {
        setCellValueFactory();
        loadAllUsers();
        applyButtonAnimations();
        getCurrentRegister();

        addHoverHandlers(btnClean);
        addHoverHandlers(btnDelete);
        addHoverHandlers(btnSave);
        addHoverHandlers(btnUpdate);
        addHoverHandlers(btnHome);



        txtName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                txtPost.requestFocus();
            }
        });

        txtPost.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                txtPassword.requestFocus();
            }
        });

        colRegiTel.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                lblRegisterId1.setText(newSelection.getRegisterId());
                txtName.setText(newSelection.getRegisterName());
                txtPost.setText(newSelection.getPost());
                txtPassword.setText(newSelection.getRegisterPassword());
            }
        });
    }

    private void applyButtonAnimations() {
        applyAnimation(btnClean);
        applyAnimation(btnDelete);
        applyAnimation(btnSave);
        applyAnimation(btnUpdate);
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
    private void addHoverHandlers(Button button) {// button Animation
        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-background-color: Black; -fx-text-fill: white;");
        });
        button.setOnMouseExited(event -> {
            button.setStyle("-fx-background-color:  #1e272e; -fx-text-fill: white;");
        });
    }

    private void setCellValueFactory() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("registerId"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("registerName"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("Post"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("registerPassword"));
    }

    private void loadAllUsers() {
        ObservableList<Register> obList = FXCollections.observableArrayList();

        try {
            List<Register> userList = RegisterRepo.getAll();
            obList.addAll(userList);
            colRegiTel.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnCleanOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Register selectedRegister = colRegiTel.getSelectionModel().getSelectedItem();
        if (selectedRegister != null) {
            try {
                boolean isDeleted = RegisterRepo.delete(selectedRegister.getRegisterId());
                if (isDeleted) {
                    colRegiTel.getItems().remove(selectedRegister);
                    new Alert(Alert.AlertType.CONFIRMATION, "User deleted successfully!").show();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to delete user!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Error occurred while deleting user: " + e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a user to delete!").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = lblRegisterId1.getText();
        String name = txtName.getText();
        String post = txtPost.getText();
        String password = txtPassword.getText();

        Register register = new Register(id, name, post, password);

        try {
            boolean isSaved = RegisterRepo.save(register);
            if (isSaved) {
                colRegiTel.getItems().add(register);
                new Alert(Alert.AlertType.CONFIRMATION, "User saved successfully!").show();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save user!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error occurred while saving user: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Register selectedRegister = colRegiTel.getSelectionModel().getSelectedItem();
        if (selectedRegister != null) {
            String id = lblRegisterId1.getText();
            String name = txtName.getText();
            String post = txtPost.getText();
            String password = txtPassword.getText();

            Register updatedRegister = new Register(id, name, post, password);

            try {
                boolean isUpdated = RegisterRepo.update(updatedRegister);
                if (isUpdated) {
                    int selectedIndex = colRegiTel.getSelectionModel().getSelectedIndex();
                    colRegiTel.getItems().set(selectedIndex, updatedRegister);
                    new Alert(Alert.AlertType.CONFIRMATION, "User updated successfully!").show();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to update user!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Error occurred while updating user: " + e.getMessage()).show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a user to update!").show();
        }
    }

    private void clearFields() {
        getCurrentRegister();

        lblRegisterId1.setText("");
        txtName.clear();
        txtPost.clear();
        txtPassword.clear();
    }

    @FXML
    private void btnHomeOnAction(ActionEvent event) throws IOException {
        Button btn = (Button) event.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/DashBoard_from.fxml"));
        Parent rootNode = loader.load();

        Scene scene = new Scene(rootNode);
        stage.setScene(scene);

        stage.show();    }



    public void NameK(KeyEvent keyEvent) {
        CustomerRegex.setTextColor(CustomerTextField.NAME,txtName);

    }

    public void PositionK(KeyEvent keyEvent) {
    }

    public void PasswordK(KeyEvent keyEvent) {
    }

    public void txtSearchKeyRelse(KeyEvent keyEvent) {
    }

    public void SearchBtnOnAction(ActionEvent event) {
        String UserID = txtSearch.getText();

        try {
            Register register = RegisterRepo.searchById(UserID);
            if (register != null) {

                lblRegisterId.setText(register.getRegisterId());
              //  lblRegisterId1.setText(register.getRegisterId());
                txtPassword.setText(register.getRegisterPassword());
                  txtName.setText(register.getRegisterName());
                    txtPost.setText(register.getPost());


            } else {
                showAlert(Alert.AlertType.ERROR, "Employee not found.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error occurred while fetching Employee: " + e.getMessage());
        }
    }
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void getCurrentRegister() {
        try {
            String currentId = RegisterRepo.getCustomerCurrentId();

            String nextRegisterID = generateNexRegisterID(currentId);
            lblRegisterId.setText(nextRegisterID);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNexRegisterID(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("R");
            int idNum = Integer.parseInt(split[1]);
            idNum++;
            return "R" + String.format("%03d", idNum);
        }
        return "R001";

    }
}

