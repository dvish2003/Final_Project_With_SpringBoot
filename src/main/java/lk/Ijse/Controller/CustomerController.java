package lk.Ijse.Controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.Ijse.Model.Customer;
import lk.Ijse.Model.Employee;
import lk.Ijse.Util.CustomerRegex;
import lk.Ijse.Util.CustomerTextField;
import lk.Ijse.repository.CustomerRepo;
import lk.Ijse.repository.EmployeeRepo;
import lk.Ijse.repository.OrderRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CustomerController {

    @FXML
    private TextField txtSearch;

    @FXML
    private Button SearchBtn;

    @FXML
    private Button btnHome;
    @FXML
    private Button btnClean;

    @FXML
    private Button btnDelete;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnSave;
    @FXML
    private Label lblCustomerID;
    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private AnchorPane CustomerPane;

    @FXML
    private TableColumn<?, ?> colContact;
    @FXML
    private Label lblCustomerID1;

    @FXML
    private TableView<Customer> colCuTel;


    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TextField txtCuAddress;

    @FXML
    private TextField txtCuContact;

    @FXML
    private TextField txtCuEmail;


    @FXML
    private TextField txtCuName;

    public void initialize() {
            setCellValueFactory();
            loadAllCustomers();
            applyButtonAnimations();
            getCurrentCustomerId();


        addHoverHandlers(btnClean);
        addHoverHandlers(btnDelete);
        addHoverHandlers(btnSave);
        addHoverHandlers(btnUpdate);
        addHoverHandlers(btnNext);
        addHoverHandlers(btnHome);



        txtCuName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                txtCuAddress.requestFocus();
            }
        });

        txtCuAddress.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                txtCuContact.requestFocus();
            }
        });
        txtCuContact.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                txtCuEmail.requestFocus();
            }
        });
        txtCuEmail.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnSave.requestFocus();
            }
        });

            colCuTel.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    lblCustomerID.setText(newSelection.getId());
                    txtCuName.setText(newSelection.getName());
                    txtCuAddress.setText(newSelection.getAddress());
                    txtCuContact.setText(newSelection.getContact());
                    txtCuEmail.setText(newSelection.getEmail());
                }
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
    private void applyButtonAnimations() {
        applyAnimation(btnClean);
        applyAnimation(btnDelete);
        applyAnimation(btnSave);
        applyAnimation(btnUpdate);
        applyAnimation(btnNext);
        applyAnimation(btnHome);

    }

    private void getCurrentCustomerId() {
        try {
            String currentId = CustomerRepo.getCustomerCurrentId();

            String nextCustomerId = generateNextCustomerId(currentId);
            lblCustomerID.setText(nextCustomerId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextCustomerId(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("C");
            int idNum = Integer.parseInt(split[1]);
            idNum++;
            return "C" + String.format("%03d", idNum);
        }
        return "C001";
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
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
    }

    private void loadAllCustomers() {
        ObservableList<Customer> obList = FXCollections.observableArrayList();

        try {
            List<Customer> customerList = CustomerRepo.getAll();
            for (Customer customer : customerList) {
                Customer tm = new Customer(
                        customer.getId(),
                        customer.getName(),
                        customer.getAddress(),
                        customer.getContact(),
                        customer.getEmail()
                );

                obList.add(tm);
            }

            colCuTel.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    @FXML
    void btnCleanOnAction(ActionEvent event) {

        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        ObservableList<Customer> selectedCustomers = colCuTel.getSelectionModel().getSelectedItems();
        if (selectedCustomers.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select customer(s) to delete!").show();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected customer(s)?");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.showAndWait();

        if (confirmationAlert.getResult() == ButtonType.OK) {
            try {
                for (Customer customer : selectedCustomers) {
                    boolean isDeleted = CustomerRepo.delete(customer.getId());
                    if (isDeleted) {
                        colCuTel.getItems().remove(customer);
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete customer: " + customer.getName()).show();
                    }
                }
                new Alert(Alert.AlertType.CONFIRMATION, "Customer(s) deleted successfully!").show();
                clearFields();
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Error occurred while deleting customer(s): " + e.getMessage()).show();
            }
        }
    }



    @FXML
    void btnSaveOnAction(ActionEvent event) {

        String id = lblCustomerID.getText();
        String name = txtCuName.getText();
        String address = txtCuAddress.getText();
        String contact = txtCuContact.getText();
        String email = txtCuEmail.getText();

        Customer customer = new Customer(id, name, address, contact, email);

        try {
            if(isValied()){boolean isSaved = CustomerRepo.save(customer);
                if (isSaved) {
                    getCurrentCustomerId();
                    colCuTel.getItems().add(customer);
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer saved successfully!").show();
                    clearFields();}

            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save customer!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error occurred while saving customer: " + e.getMessage()).show();
        }
    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = lblCustomerID.getText();
        String name = txtCuName.getText();
        String address = txtCuAddress.getText();
        String contact = txtCuContact.getText();
        String email = txtCuEmail.getText();

        Customer customer = new Customer(id, name, address, contact, email);
        try {
            if(isValied()){ boolean isUpdated = CustomerRepo.update(customer);
                if (isUpdated) {
                    Customer selectedItem = colCuTel.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        int selectedIndex = colCuTel.getItems().indexOf(selectedItem);
                        colCuTel.getItems().set(selectedIndex, customer);
                        new Alert(Alert.AlertType.CONFIRMATION, "Customer updated successfully!").show();
                        clearFields();
                        getCurrentCustomerId();

                    }}

            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update customer!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error occurred while updating customer: " + e.getMessage()).show();
        }
    }


    private void clearFields() {
        getCurrentCustomerId();
        lblCustomerID1.setText("");
        txtCuName.clear();
        txtCuAddress.clear();
        txtCuContact.clear();
        txtCuEmail.clear();
    }

    public void btnNextOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LocationForm.fxml"));
        Parent rootNode = loader.load();
        CustomerPane.getChildren().clear();
        CustomerPane.getChildren().add(rootNode);
        rootNode.setTranslateX(CustomerPane.getWidth());
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), rootNode);
        transition.setToX(0);
        transition.play();

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
    public boolean isValied(){
        if (!CustomerRegex.setTextColor(CustomerTextField.NAME,txtCuName)) return false;
        if (!CustomerRegex.setTextColor(CustomerTextField.CONTACT,txtCuContact)) return false;
        if (!CustomerRegex.setTextColor(CustomerTextField.EMAIL,txtCuEmail)) return false;
        if (!CustomerRegex.setTextColor(CustomerTextField.ADDRESS,txtCuAddress)) return false;

        return true;
    }

    @FXML
    void SearchBtnOnAction(ActionEvent event) throws SQLException {
String contact = txtSearch.getText();
        try {
            Customer customer = CustomerRepo.searchById(contact);
            if (customer != null) {
                txtCuEmail.setText(customer.getEmail());
                txtCuAddress.setText(customer.getAddress());
                txtCuName.setText(customer.getName());
                txtCuContact.setText(customer.getContact());
                lblCustomerID1.setText(customer.getId());
              //  lblCustomerID.setText(customer.getId());
                            } else {
                showAlert(Alert.AlertType.ERROR, "Customer not found.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error occurred while fetching Customer: " + e.getMessage());
        }

    }
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void txtNameOnKeyReleased(KeyEvent keyEvent) {
       CustomerRegex.setTextColor(CustomerTextField.NAME,txtCuName);

    }

    public void txtAddressOnKeyReleased(KeyEvent keyEvent) {
        CustomerRegex.setTextColor(CustomerTextField.NAME,txtCuAddress);

    }

    public void txtContactOnKeyReleased(KeyEvent keyEvent) {
        CustomerRegex.setTextColor(CustomerTextField.CONTACT,txtCuContact);

    }

    public void txtEmailOnKeyReleased(KeyEvent keyEvent) {
        CustomerRegex.setTextColor(CustomerTextField.EMAIL,txtCuEmail);


    }

    @FXML
    void txtSearchKeyRelse(KeyEvent event) {
        CustomerRegex.setTextColor2(CustomerTextField.CONTACT,txtSearch);

    }
//    public boolean isValied(){
//        if (!CustomerRegex.setTextColor(CustomerTextField.NAME,txtCuName)) return false;
//        if (!CustomerRegex.setTextColor(CustomerTextField.EMAIL,txtCuEmail)) return false;
//        if (!CustomerRegex.setTextColor(CustomerTextField.NAME,txtCuAddress)) return false;
//        if (!CustomerRegex.setTextColor(CustomerTextField.CONTACT,txtCuContact)) return false;
//
//        return true;
    }




