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
import lk.Ijse.Model.*;
import lk.Ijse.Util.CustomerRegex;
import lk.Ijse.Util.CustomerTextField;
import lk.Ijse.repository.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ProductController {

    @FXML
    private Button btnHome;


    @FXML
    private Button btnPrClean;

    @FXML
    private Button btnPrDelete;



    @FXML
    private Button btnPrSave;

    @FXML
    private Button btnPrUpdate;

    @FXML
    private ComboBox<String> cmbShowRoom;

    @FXML
    private TableColumn<Products, String> colPrDesc;

    @FXML
    private TableColumn<Products, String> colPrID;

    @FXML
    private TableView<Products> colPrTel;

    @FXML
    private TableColumn<Products, Integer> colPrUnitPrice;

    @FXML
    private TableColumn<Products, Integer> colQtyOnHand;

    @FXML
    private TableColumn<Product_ShowRoom, String> coljoinPrDesc;

    @FXML
    private TableColumn<Product_ShowRoom, String> coljoinPrID;

    @FXML
    private TableColumn<Product_ShowRoom, String> coljoinShowRoom;

    @FXML
    private TableView<ProductShowRoomJoin> colPrJoinTel;

    @FXML
    private TableColumn<Product_ShowRoom, Integer> coljoinQtyOnHand;

    @FXML
    private TableColumn<Product_ShowRoom, Integer> coljoinPrUnitPrice;

    @FXML
    private TextField txtPrDesc;
    @FXML
    private Label lblProductID;


    @FXML
    private TextField txtPrUnitPrice;

    @FXML
    private TextField txtQty;

    private ObservableList<ProductShowRoomJoin> productShowRoomList;

    @FXML
    void initialize() {
        setCellValueFactory();
        setCellValueFactory2();
        loadAllProductShowRoom();
        applyComboBoxStyles();
        loadAllProduct();
        setListeners();
        getShowRoomIds();
        applyButtonAnimations();
        addHoverHandlers(btnHome);
        addHoverHandlers(btnPrClean);
        addHoverHandlers(btnPrUpdate);
        addHoverHandlers(btnPrSave);
        addHoverHandlers(btnPrDelete);
        getProductId();

    }
    private void applyButtonAnimations() {
        applyAnimation(btnHome);
        applyAnimation(btnPrClean);
        applyAnimation(btnPrUpdate);
        applyAnimation(btnPrSave);
        applyAnimation(btnPrDelete);


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


    private void loadAllProduct() {
        ObservableList<Products> obList = FXCollections.observableArrayList();

        try {
            List<Products> productsList = ProductsRepo.getAll();
            obList.addAll(productsList);
            colPrTel.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException("Error loading products: " + e.getMessage(), e);
        }
    }

    private void loadAllProductShowRoom() {
        productShowRoomList = FXCollections.observableArrayList();

        try {
            List<ProductShowRoomJoin> productsShowRoomList = ProductShowRoomJoinRepo.getAll();
            productShowRoomList.addAll(productsShowRoomList);
            colPrJoinTel.setItems(productShowRoomList);
        } catch (SQLException e) {
            throw new RuntimeException("Error loading products: " + e.getMessage(), e);
        }
    }

    private void setCellValueFactory() {
        coljoinShowRoom.setCellValueFactory(new PropertyValueFactory<>("showRoomId"));
        coljoinPrID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        coljoinPrDesc.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        coljoinQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("productQuantityOnHand"));
        coljoinPrUnitPrice.setCellValueFactory(new PropertyValueFactory<>("productUnitPrice"));
    }
    private void setCellValueFactory2() {
        colPrID.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        colPrDesc.setCellValueFactory(new PropertyValueFactory<>("product_description"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("showRoom_qtyOnHand"));
        colPrUnitPrice.setCellValueFactory(new PropertyValueFactory<>("product_unitPrice"));
    }

    private void setListeners() {
        setTextFieldFocusTraversal();
        setTableSelectionListener();
    }

    private void setTextFieldFocusTraversal() {


        txtPrDesc.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                txtPrUnitPrice.requestFocus();
            }
        });

        txtPrUnitPrice.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                txtQty.requestFocus();
            }
        });
    }

    private void setTableSelectionListener() {
        colPrTel.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                lblProductID.setText(newSelection.getProduct_id());
                txtPrDesc.setText(newSelection.getProduct_description());
                txtPrUnitPrice.setText(String.valueOf(newSelection.getProduct_unitPrice()));
                txtQty.setText(String.valueOf(newSelection.getShowRoom_qtyOnHand()));
            }
        });
    }

    @FXML
    void cmbShowRoomOnAction(ActionEvent event) {
        String id = cmbShowRoom.getValue();
        try {
            ShowRoom showRoom = ShowRoomRepo.searchById(id);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error occurred while searching for showroom: " + e.getMessage());
        }
    }

    private void getShowRoomIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> idList = ShowRoomRepo.getIds();
            obList.addAll(idList);
            cmbShowRoom.setItems(obList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error occurred while fetching showroom IDs: " + e.getMessage());
        }
    }
    private void getProductId() {
        try {
            String currentId = ProductsRepo.getProductCurrentId();

            String nextProductId = generateNextProductID(currentId);
            lblProductID.setText(nextProductId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextProductID(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("PRD");
            int idNum = Integer.parseInt(split[1]);
            idNum++;
            return "PRD" + String.format("%03d", idNum);
        }
        return "PRD001";
    }

    @FXML
    void btnHomeOnAction(ActionEvent event) throws IOException {
        Button btn = (Button) event.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/DashBoard_from.fxml"));
        Parent rootNode = loader.load();
        Scene scene = new Scene(rootNode);
        stage.setScene(scene);

        stage.show();
    }

    public void applyComboBoxStyles() {
        cmbShowRoom.setStyle(" -fx-text-fill: white;");

    }

    @FXML
    void btnPrCleanOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnPrDeleteOnAction(ActionEvent event) {
        String id = lblProductID.getText();
        try {
            boolean isDeleted = ProductsRepo.delete(id);
            if (isDeleted) {
                Products selectedItem = colPrTel.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    colPrTel.getItems().remove(selectedItem);
                    productShowRoomList.removeIf(item -> item.getProductId().equals(id));
                    new Alert(Alert.AlertType.CONFIRMATION, "Product deleted successfully!").show();
                    clearFields();
                }

            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to delete product!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error occurred while deleting product: " + e.getMessage()).show();
        }
    }



    @FXML
    void btnPrSaveOnAction(ActionEvent event) {
        String showRoomId = cmbShowRoom.getValue();
        String productId = lblProductID.getText();
        String productDescription = txtPrDesc.getText();
        int productUnitPrice = Integer.parseInt(txtPrUnitPrice.getText());
        int productQty = Integer.parseInt(txtQty.getText());

        Products products = new Products(productId, productDescription,productQty,productUnitPrice);
        Product_ShowRoom ps = new Product_ShowRoom(productId, showRoomId);

        try {
            if(isValied()){ boolean isProductSaved = ProductsRepo.save(products);
                if (isProductSaved) {
                    colPrTel.getItems().add(products);
                    boolean isProductShowRoomSaved = Product_ShowRoom_Repo.save(ps);
                    if (isProductShowRoomSaved) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Product and ShowRoom saved successfully!").show();
                        productShowRoomList.add(new ProductShowRoomJoin(productId, showRoomId, productDescription, productQty, productUnitPrice));
                        clearFields();
                    }

                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to save Product and ShowRoom!").show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save Product!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error occurred while saving data: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnPrUpdateOnAction(ActionEvent event) {
        String showRoomId = cmbShowRoom.getValue();
        String productId = lblProductID.getText();
        String productDescription = txtPrDesc.getText();
        int productUnitPrice = Integer.parseInt(txtPrUnitPrice.getText());
        int productQty = Integer.parseInt(txtQty.getText());

        Products products = new Products(productId, productDescription, productUnitPrice, productQty);
        Product_ShowRoom ps = new Product_ShowRoom(productId, showRoomId);
        try {
            if(isValied()){ boolean isProductUpdate = ProductsRepo.update(products, productQty);
                if (isProductUpdate) {
                    Products selectedProduct = colPrTel.getSelectionModel().getSelectedItem();
                    selectedProduct.setProduct_description(productDescription);
                    selectedProduct.setProduct_unitPrice(productUnitPrice);
                    selectedProduct.setShowRoom_qtyOnHand(productQty);
                    colPrTel.refresh();

                    boolean isProductShowRoomSaved = Product_ShowRoom_Repo.save(ps);
                    if (isProductShowRoomSaved) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Product and ShowRoom updated successfully!").show();
                        clearFields();
                        loadAllProductShowRoom();}

                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to update Product and ShowRoom!").show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to update Product!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error occurred while updating data: " + e.getMessage()).show();
        }
    }


    private void clearFields() {
        cmbShowRoom.getSelectionModel().clearSelection();
        txtQty.clear();
        txtPrDesc.clear();
        txtPrUnitPrice.clear();
        getProductId();

    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean isValied(){
        if (!CustomerRegex.setTextColor(CustomerTextField.NUMBER,txtPrUnitPrice)) return false;
        if (!CustomerRegex.setTextColor(CustomerTextField.NUMBER,txtQty)) return false;



        return true;
    }

    public void DescK(KeyEvent keyEvent) {
    }

    public void UnitK(KeyEvent keyEvent) {
        CustomerRegex.setTextColor(CustomerTextField.NUMBER,txtPrUnitPrice);

    }

    public void QtyK(KeyEvent keyEvent) {
        CustomerRegex.setTextColor(CustomerTextField.NUMBER,txtQty);

    }
    private void addHoverHandlers(Button button) {// button Animation
        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-background-color: Black; -fx-text-fill: white;");
        });
        button.setOnMouseExited(event -> {
            button.setStyle("-fx-background-color:  #1e272e; -fx-text-fill: white;");
        });
    }

}
