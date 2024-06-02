package lk.Ijse.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.Ijse.Db.DbConnection;
import lk.Ijse.Model.*;
import lk.Ijse.Util.CustomerRegex;
import lk.Ijse.Util.CustomerTextField;
import lk.Ijse.repository.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class OrderController {
    @FXML
    private Button btnPrintBill;

    @FXML
    private Label LblOrderDate;

    @FXML
    private Button btnAddToCart;

    @FXML
    private Button btnNewCus;

    @FXML
    private Button btnPlaceOrder;

    @FXML
    private Button btnPrBack;


    @FXML
    private AnchorPane orderPane;

    @FXML
    private ComboBox<String> cmbCustomerID;

    @FXML
    private ComboBox<String> cmbProductID;

    @FXML
    private ComboBox<String> cmbShowRoomID;

    @FXML
    private TableColumn<?, ?> colOrAction;

    @FXML
    private TableColumn<?, ?> colOrPrDesc;

    @FXML
    private TableColumn<?, ?> colOrPrID;

    @FXML
    private TableColumn<?, ?> colOrPrQty;

    @FXML
    private TableColumn<?, ?> colOrPrTotal;
    @FXML
    private TableView<Product_ShowRoom> colPSTable;

    @FXML
    private TableColumn<?, ?> colJoinPR;

    @FXML
    private TableColumn<?, ?> colJoinSW;

    @FXML
    private TableColumn<?, ?> colOrPrUnitPrice;
    @FXML
    private TableView<CartTm> colOrderTel;
    @FXML
    private Label lblLocationShowRoom;

    @FXML
    private Label lblCustomerName;
    @FXML
    private Label lblQtyOnHand;


    @FXML
    private Label lblOrderID;

    @FXML
    private Label lblPayDate;

    @FXML
    private Label lblProductID;

    @FXML
    private Label lblUnitPrice;

    @FXML
    private Label lblPaymentAmount;

    @FXML
    private Label lblPaymentID;
    private ObservableList<CartTm> obList = FXCollections.observableArrayList();


    @FXML
    private TextField txtQty;

    public void initialize() {
        setDate();
        getCurrentOrderId();
        getCustomerIds();
        getCurrentPayId();
        applyButtonAnimations();
        applyLabelAnimations();
        getShowRoomIds();
        getProductIds();
        setCellValueFactory();
        loadAllPS();
        applyComboBoxStyles();
        addHoverHandlers(btnAddToCart);
        addHoverHandlers(btnNewCus);
        addHoverHandlers(btnPlaceOrder);
        addHoverHandlers(btnPrBack);
        addHoverHandlers(btnPrBack);
        addHoverHandlers(btnPrintBill);


        cmbCustomerID.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                cmbShowRoomID.requestFocus();
            }
        });

        cmbShowRoomID.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                cmbProductID.requestFocus();
            }
        });

        cmbProductID.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                txtQty.requestFocus();
            }
        });



        colOrderTel.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtQty.requestFocus();
            }
        });

    }
    private void setCellValueFactory() {
        colOrPrID.setCellValueFactory(new PropertyValueFactory<>("P_ID"));
        colOrPrDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colOrPrUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colOrPrQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colOrPrTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colOrAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));
        colJoinPR.setCellValueFactory(new PropertyValueFactory<>("productID"));
        colJoinSW.setCellValueFactory(new PropertyValueFactory<>("showRoomId"));


    }

    private void loadAllPS() {
        ObservableList<Product_ShowRoom> obList = FXCollections.observableArrayList();

        try {
            List<Product_ShowRoom> ps = Product_ShowRoom_Repo.getAll();
            for (Product_ShowRoom psList : ps) {
                Product_ShowRoom tm = new Product_ShowRoom(

                        psList.getShowRoomId(),
                        psList.getProductID()

                );

                obList.add(tm);
            }

            colPSTable.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void applyButtonAnimations() {
        applyAnimation(btnAddToCart);
        applyAnimation(btnNewCus);
        applyAnimation(btnPlaceOrder);
        applyAnimation(btnPrBack);
        applyAnimation(btnPrBack);
        applyAnimation(btnPrintBill);

    }

    private void applyLabelAnimations() {
        applyAnimation(lblCustomerName);
        applyAnimation(lblQtyOnHand);
        applyAnimation(lblOrderID);
        applyAnimation(lblPayDate);
        applyAnimation(LblOrderDate);
        applyAnimation(lblUnitPrice);
        applyAnimation(lblPaymentAmount);
        applyAnimation(lblPaymentID);
        applyAnimation(lblLocationShowRoom);
        applyAnimation(lblProductID);



    }
    private void addHoverHandlers(Button button) {// button Animation
        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-background-color: Black; -fx-text-fill: white;");
        });
        button.setOnMouseExited(event -> {
            button.setStyle("-fx-background-color:  #1e272e; -fx-text-fill: white;");
        });
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

    private void applyAnimation(Label label) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), label);
        label.setOnMouseEntered(event -> {
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0.5);
            fadeTransition.play();
        });
        label.setOnMouseExited(event -> {
            fadeTransition.setFromValue(0.5);
            fadeTransition.setToValue(1);
            fadeTransition.play();
        });
    }

    private void getCustomerIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<String> idList = CustomerRepo.getIds();

            for(String id : idList) {
                obList.add(id);
            }

            cmbCustomerID.setItems(obList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getCurrentOrderId() {
        try {
            String currentId = OrderRepo.getCurrentId();

            String nextOrderId = generateNextOrderId(currentId);
            lblOrderID.setText(nextOrderId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextOrderId(String currentId) {
        if (currentId != null) {
            String[] split = currentId.split("O");
            int idNum = Integer.parseInt(split[1]);
            idNum++;
            return "O" + String.format("%03d", idNum);
        }
        return "O001";
    }


    private void setDate() {
        LocalDate now = LocalDate.now();
        lblPayDate.setText(String.valueOf(now));
        LblOrderDate.setText(String.valueOf(now));
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent event) {
    String P_Id = lblProductID.getText();
    String Description =cmbProductID.getValue();
    int Unit_Price = Integer.parseInt(lblUnitPrice.getText());
    int Qty = Integer.parseInt(txtQty.getText());
    int Total_Price = Unit_Price * Qty;

        JFXButton btnRemove = new JFXButton("remove");
        btnRemove.setCursor(Cursor.HAND);
        btnRemove.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if(type.orElse(no) == yes) {
                CartTm selectedIndex = colOrderTel.getSelectionModel().getSelectedItem();
                obList.remove(selectedIndex);

                colOrderTel.refresh();
                calculateNetTotal();
            }
        });
        for (int i = 0; i < colOrderTel.getItems().size(); i++) {
            if(P_Id.equals(colOrPrID.getCellData(i))){
                CartTm tm = obList.get(i);
                Qty += tm.getQty();
                Total_Price = Qty*Unit_Price;
                 tm.setQty(Qty);
                 tm.setTotal(Total_Price);

                 colOrderTel.refresh();
                 calculateNetTotal();
                 return;

            }
        }
        CartTm tm = new CartTm(P_Id,Description,Qty,Unit_Price,Total_Price,btnRemove);
        obList.add(tm);

        colOrderTel.setItems(obList);
        calculateNetTotal();
        txtQty.setText("");
    }

    private void calculateNetTotal() {
        int netTotal = 0;
        for (int i = 0; i < colOrderTel.getItems().size(); i++) {
            netTotal += (int) colOrPrTotal.getCellData(i);
        }
        lblPaymentAmount.setText(String.valueOf(netTotal));
    }

    @FXML
    void btnNewCusOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/CustomerForm.fxml"));
        Parent rootNode = loader.load();
        orderPane.getChildren().clear();
        orderPane.getChildren().add(rootNode);
        rootNode.setTranslateX(orderPane.getWidth());
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), rootNode);
        transition.setToX(0);
        transition.play();
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) throws SQLException, JRException {
        String orderID = lblOrderID.getText();
        String customerID = cmbCustomerID.getValue();
        String paymentID = lblPaymentID.getText();
        Date date = Date.valueOf(LocalDate.now());
        int Amount = Integer.parseInt(lblPaymentAmount.getText());

        var order = new Order(orderID, customerID, paymentID, date);
        List<OrderDetail> odList = new ArrayList<>();

        for (int i = 0; i < colOrderTel.getItems().size(); i++) {
            CartTm tm = obList.get(i);
            OrderDetail od = new OrderDetail(
                    tm.getP_ID(),
                    orderID,
                    tm.getQty(),
                    tm.getUnitPrice()

            );

            odList.add(od);

        }

        Payment payment = new Payment(paymentID, Amount, date);
        PlaceOrder po = new PlaceOrder(order, odList, payment);

        boolean isPlaced = PlaceOrderRepo.placeOrder(po);

        if (isPlaced) {
            obList.clear();
            colOrderTel.setItems(obList);
            cmbProductID.getSelectionModel().clearSelection();
            cmbCustomerID.getSelectionModel().clearSelection();
            cmbShowRoomID.getSelectionModel().clearSelection();
            lblLocationShowRoom.setText("");
            lblCustomerName.setText("");
            lblPaymentAmount.setText("");
            lblProductID.setText("");
           lblUnitPrice.setText("");
           lblQtyOnHand.setText("");
            txtQty.clear();

            getCurrentOrderId();
            getCurrentPayId();

            new Alert(Alert.AlertType.CONFIRMATION, "Order Placed!").show();
            btnPrintBillOnAction(null);

        } else {
            new Alert(Alert.AlertType.WARNING, "Order Placed Unsuccessfully!").show();
        }
    }

    @FXML
    void btnPrBackOnAction(ActionEvent event) throws IOException {
        Button btn = (Button) event.getSource();
        Stage stage = (Stage) btn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/DashBoard_from.fxml"));
        Parent rootNode = loader.load();

        Scene scene = new Scene(rootNode);
        stage.setScene(scene);

        stage.show();
    }



    @FXML
    void cmbCustomerIDOnAction(ActionEvent event) {
        String id = cmbCustomerID.getValue();
        try {
            Customer customer = CustomerRepo.searchById1(id);
if (customer != null){
    lblCustomerName.setText(customer.getName());
} else {
    lblCustomerName.setText("Customer Not Found ");

}

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void cmbProductIDOnAction(ActionEvent event) {
        String Desc = String.valueOf(cmbProductID.getValue());
        try {
            Products products = ProductsRepo.searchByName(Desc);
            if(products != null) {lblProductID.setText(products.getProduct_id());
                lblUnitPrice.setText(String.valueOf(products.getProduct_unitPrice()));
                lblQtyOnHand.setText(String.valueOf(products.getShowRoom_qtyOnHand()));
            }else{
            lblProductID.setText("not Found");
            lblUnitPrice.setText("not Found");
            lblQtyOnHand.setText("not Found");
}

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    private void getProductIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> idList = ProductsRepo.getNames();
            obList.addAll(idList);
            cmbProductID.setItems(obList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error occurred while fetching Products IDs: " + e.getMessage());
        }

    }

    @FXML
    void cmbShowRoomIDOnAction(ActionEvent event) {
        String id = String.valueOf(cmbShowRoomID.getValue());
        try {
            ShowRoom showRoom = ShowRoomRepo.searchById(id);
            if (showRoom != null){
                lblLocationShowRoom.setText(showRoom.getShowRoomLocation());

            } else {
                lblLocationShowRoom.setText("ShowRoom Not Found");


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void getShowRoomIds() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> idList = ShowRoomRepo.getIds();
            obList.addAll(idList);
            cmbShowRoomID.setItems(obList);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error occurred while fetching showroom IDs: " + e.getMessage());
        }
    }
    private void getCurrentPayId() {
        try {
            String currentId = OrderRepo.getPayCurrentId();

            String nextPayId = generateNextPay(currentId);
            lblPaymentID.setText(nextPayId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateNextPay(String currentId) {
        if (currentId != null && currentId.startsWith("P")) {

                int idNum = Integer.parseInt(currentId.substring(1)) + 1;
                return "P" + String.format("%03d", idNum);

        }
        return "P001";
    }
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void QtyK(KeyEvent keyEvent) {
        CustomerRegex.setTextColor(CustomerTextField.NUMBER,txtQty);

    }
    @FXML
    private void btnPrintBillOnAction(ActionEvent event) throws JRException, SQLException {
           JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/Reports/OrderBillReport.jrxml");
        JRDesignQuery jrDesignQuery = new JRDesignQuery();
        jrDesignQuery.setText("SELECT o.Order_id, c.Customer_Name, p.Product_Description, od.Qty, od.Product_UnitPrice, py.Payment_Amount\n" +
                "FROM `Order` o\n" +
                "JOIN Customer c ON o.Customer_id = c.Customer_id\n" +
                "JOIN OrderDetails od ON o.Order_id = od.Order_id\n" +
                "JOIN Product p ON od.Product_id = p.Product_id\n" +
                "JOIN Payment py ON o.Payment_id = py.Payment_id\n" +
                "WHERE o.Order_id = (\n" +
                "    SELECT MAX(Order_id)\n" +
                "    FROM `Order`\n" +
                ");\n");
        jasperDesign.setQuery(jrDesignQuery);
           JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);



        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, null,DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);



//        // SQL query to fetch details of the latest order
//        String sqlQuery = "SELECT o.Order_id, c.Customer_Name, p.Product_Description, od.Qty, od.Product_UnitPrice, py.Payment_Amount " +
//                "FROM `Order` o " +
//                "JOIN Customer c ON o.Customer_id = c.Customer_id " +
//                "JOIN OrderDetails od ON o.Order_id = od.Order_id " +
//                "JOIN Product p ON od.Product_id = p.Product_id " +
//                "JOIN Payment py ON o.Payment_id = py.Payment_id " +
//                "WHERE o.Order_id = ( " +
//                "    SELECT MAX(Order_id) " +
//                "    FROM `Order` " +
//                ")";
//
//        // Prepare the statement
//        PreparedStatement preparedStatement = DbConnection.getInstance().getConnection().prepareStatement(sqlQuery);
//
//        // Execute the query
//        boolean querySuccess = preparedStatement.execute();
//
//        // Check if the query execution was successful
//        if (querySuccess) {
//            // Retrieve the result set
//            ResultSet resultSet = preparedStatement.getResultSet();
//
//            // Check if the result set has data
//            if (resultSet.next()) {
//                // Create a map to hold the data for the report
//                Map<String, Object> data = new HashMap<>();
//                // Populate the map with data from the result set
//                data.put("Order_id", resultSet.getString("Order_id"));
//                data.put("Customer_Name", resultSet.getString("Customer_Name"));
//                data.put("Product_Description", resultSet.getString("Product_Description"));
//                data.put("Qty", resultSet.getInt("Qty"));
//                data.put("Product_UnitPrice", resultSet.getInt("Product_UnitPrice"));
//                data.put("Payment_Amount", resultSet.getInt("Payment_Amount"));
//
//                // Load the Jasper report
//                JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/Reports/OrderBillReport.jrxml");
//                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
//
//                // Fill the report with data and view it
//                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, data, new JREmptyDataSource());
//                JasperViewer.viewReport(jasperPrint, false);
//            } else {
//                // If no data found, show an alert
//                new Alert(Alert.AlertType.ERROR, "No data found for the latest order.").show();
//            }
//
//            // Close the result set
//            resultSet.close();
//        } else {
//            // If the query execution failed, show an error alert
//            new Alert(Alert.AlertType.ERROR, "Failed to retrieve data for the latest order.").show();
//        }
//
//        // Close the prepared statement
//        preparedStatement.close();
    }

    public void applyComboBoxStyles() {
        cmbProductID.setStyle(" -fx-text-fill: white;");
        cmbShowRoomID.setStyle(" -fx-text-fill: white;");
        cmbCustomerID.setStyle(" -fx-text-fill: white;");


    }
}
