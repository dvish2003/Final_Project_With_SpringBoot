package lk.Ijse.Controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.sql.*;
import java.time.LocalTime;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.Ijse.Db.DbConnection;
import lk.Ijse.Model.Booking;
import lk.Ijse.Model.Employee;
import lk.Ijse.Util.CustomerRegex;
import lk.Ijse.Util.CustomerTextField;
import lk.Ijse.repository.BookingRepo;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lk.Ijse.repository.EmployeeRepo;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashBoardController {

   // public static final AnchorPane U1 = ;
   @FXML
  private Label LblEc;
    @FXML
  private Label LblBc;
    @FXML
  private Label lblUc;
    @FXML
    private AnchorPane B1;
    @FXML
    private AnchorPane U1;

    @FXML
    private AnchorPane DashBoardAncorPane;

    @FXML
    private AnchorPane E1;

    @FXML
    private Label LblBookingCustomerName;

    @FXML
    private Label LblLocationBooking;

    @FXML
    private AnchorPane MenuAncorPane;

    @FXML
    private Button MenuBtn;

    @FXML
    private Button ModeBtn;

    @FXML
    private AnchorPane O1;
    @FXML
    private TableView<Booking> ColBookTel;

    @FXML
    private AnchorPane TitleBar;

    @FXML
    private PieChart PieChart;

    @FXML
    private Button SearchBtn;

    @FXML
    private AnchorPane SpecialDataPane;

    @FXML
    private Button btnBooking;

    @FXML
    private Button btnCustomer;

    @FXML
    private Button btnEmployye;

    @FXML
    private Button btnLocation;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnOrder;

    @FXML
    private Button btnProduct;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnShowRoom;

    @FXML
    private TableColumn<?, ?> colBookDate;

    @FXML
    private TableColumn<?, ?> colBookID;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colEmpID;

    @FXML
    private TableColumn<?, ?> colLocID;

    @FXML
    private TableColumn<?, ?> colPayID;

    @FXML
    private TableColumn<?, ?> colPlaceDate;

    @FXML
    private Label lblBookingDate;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblEContatc;

    @FXML
    private Label lblEName;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblEmployeeCount;

    @FXML
    private Label lblOc;

    @FXML
    private Label lblGreeting;

    @FXML
    private Label lblOrderCount;

    @FXML
    private Label lblProductCount;

    @FXML
    private Line lblL1;

    @FXML
    private Line lblL2;


    @FXML
    private Label lblClick;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblUser;

    @FXML
    private Label lblUserCount;

    @FXML
    private TextField txtSearch;
    private String fullTitle ="KD.Aircon Industries (Pvt) Ltd";

    private int currentIndex = 0;

    private int EmployeeCount;

    private int UserCount;

    private int BookingCount;

    private int OrderCount;

    private boolean darkMode = false;
    private boolean isMenuVisible = true;



    public void initialize(){

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> { // set current Time
            LocalTime currentTime = LocalTime.now();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = currentTime.format(formatter);

            lblTime.setText(formattedTime);

            int currentHour = currentTime.getHour();
            String greeting = currentHour < 12 ? "Good Morning" : "Good Afternoon";

            lblGreeting.setText(greeting);
            PieChart.setPrefWidth(600);
            PieChart.setPrefHeight(400);

        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

//        txtSearch.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.ENTER) {
//                SearchBtn.requestFocus();
//            }
//        });

        //loadOrderData();
        displayTodayBookings();
        animateLabel();
        setDate();
        setCellValueFactory();
        loadAllBooking();
        addHoverHandlers(btnBooking);
        addHoverHandlers(btnCustomer);
        addHoverHandlers(btnEmployye);
        addHoverHandlers(btnLocation);
        addHoverHandlers(btnLogOut);
        addHoverHandlers(btnOrder);
        addHoverHandlers(btnProduct);
        addHoverHandlers(btnRegister);
        addHoverHandlers(btnShowRoom);

        setupOrderPieChart();
        try {
            EmployeeCount = getEmployeeCount();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        try {
            OrderCount = getOrderCount();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        try {
            UserCount = getUserCount();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

        try {
            BookingCount = getBookingCount();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        setBookingCount(BookingCount);
        setEmployeeCount(EmployeeCount);
        setUserCount(UserCount);
        setOrderCount(OrderCount);

    }


    private void setupOrderPieChart() {
        try {
            ObservableList<PieChart.Data> pieChartData = getOrderDataForPieChart();
            PieChart.setData(pieChartData);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private ObservableList<PieChart.Data> getOrderDataForPieChart() throws SQLException {
        String sql = "SELECT C.Customer_Name, COUNT(O.Order_id) AS Order_Count " +
                "FROM `Order` O " +
                "INNER JOIN Customer C ON O.Customer_id = C.Customer_id " +
                "GROUP BY C.Customer_Name";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        while (resultSet.next()) {
            String customerName = resultSet.getString("Customer_Name");
            int orderCount = resultSet.getInt("Order_Count");
            pieChartData.add(new PieChart.Data(customerName, orderCount));
        }

        return pieChartData;
    }
    private void addHoverHandlers(Button button) {// button Animation
        button.setOnMouseEntered(event -> {
            button.setStyle("-fx-background-color: Black; -fx-text-fill: white; ");
        });
        button.setOnMouseExited(event -> {
            button.setStyle("-fx-background-color:  #1e272e; -fx-text-fill: white;");
        });
    }






    private void setOrderCount(int UserCount) {
        lblOrderCount.setText(String.valueOf(UserCount));

    }
    private int getOrderCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS Order_count FROM `Order`";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("Order_count");
        }
        return 0;
    }


    private void setUserCount(int UserCount) {
        lblUserCount.setText(String.valueOf(UserCount));

    }

    private int getUserCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS User_count FROM Register";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("User_count");
        }
        return 0;
    }

    private void setEmployeeCount(int EmployeeCount) {
        lblEmployeeCount.setText(String.valueOf(EmployeeCount));
    }

    private int getEmployeeCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS Employee_count FROM Employee";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("Employee_count");
        }
        return 0;
    }


    private void setBookingCount(int BookingCount) {
        lblProductCount.setText(String.valueOf(BookingCount));
    }

    private int getBookingCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS Booking_count FROM Booking";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("Booking_count");
        }
        return 0;
    }

    private void loadAllBooking() {
        ObservableList<Booking> obList = FXCollections.observableArrayList();

        try {
            List<Booking> BookList = BookingRepo.getAll();
            for (Booking booking : BookList) {
                Booking tm = new Booking(
                        booking.getBookingId(),
                        booking.getLocId(),
                        booking.getEmpId(),
                        booking.getPaymentId(),
                        booking.getBookingDate(),
                        booking.getPlaceDate(),
                        booking.getBookingDescription()
                );

                obList.add(tm);
            }

            ColBookTel.setItems(obList);

            ColBookTel.setRowFactory(tv -> new TableRow<Booking>() {   // already expire date colour
                @Override
                protected void updateItem(Booking item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setStyle("-fx-background-color: white; ");
                    } else if (item.getBookingDate().toLocalDate().isBefore(LocalDate.now())) {
                        setStyle("-fx-background-color: #70a1ff;");
                    } else {
                        setStyle("");
                    }
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCellValueFactory() {
        colBookID.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        colEmpID.setCellValueFactory(new PropertyValueFactory<>("LocId"));
        colLocID.setCellValueFactory(new PropertyValueFactory<>("empId"));
        colPayID.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colPlaceDate.setCellValueFactory(new PropertyValueFactory<>("PlaceDate"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("bookingDescription"));
        colBookDate.setCellValueFactory(new PropertyValueFactory<>("bookingDate"));

    }

    private void setDate() {
        LocalDate now = LocalDate.now();
        lblDate.setText(String.valueOf(now));

    }

    private void animateLabel() {

        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(0.2),
                event -> {
                    if (currentIndex <= fullTitle.length()) {
                        lblTitle.setText(fullTitle.substring(0, currentIndex));
                        currentIndex++;
                    }
                }
        );

        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(fullTitle.length() + 1);


        timeline.play();

    }

    public void setUserId(String userId) {
        lblUser.setText(userId);
    }




    @FXML
    void ModeBtnOnAction(ActionEvent event) {   // Dark Mode Light Mode
        if (darkMode) {
            DashBoardAncorPane.setStyle("-fx-background-color: white;");
            B1.setStyle("-fx-background-color: Black;");
            MenuAncorPane.setStyle("-fx-background-color: Black;");
            TitleBar.setStyle("-fx-background-color: Black;");
            O1.setStyle("-fx-background-color: Black;");
            E1.setStyle("-fx-background-color: Black;");
            O1.setStyle("-fx-background-color: Black;");
            U1.setStyle("-fx-background-color: Black;");

            lblGreeting.setStyle("-fx-text-fill: White;");
            lblDate.setStyle("-fx-text-fill: White;");
            lblProductCount.setStyle("-fx-text-fill: White;");
            lblTitle.setStyle("-fx-text-fill: White;");
            lblUc.setStyle("-fx-text-fill: White;");
            lblUser.setStyle("-fx-text-fill: White;");
            lblTime.setStyle("-fx-text-fill: White;");
            lblOrderCount.setStyle("-fx-text-fill: White;");
            lblUserCount.setStyle("-fx-text-fill: White;");
            lblEmployeeCount.setStyle("-fx-text-fill: White;");
            LblBc.setStyle("-fx-text-fill: White;");
            LblEc.setStyle("-fx-text-fill: White;");
            lblOc.setStyle("-fx-text-fill: White;");
            lblL1.setStyle("-fx-text-fill: White;");
            lblL2.setStyle("-fx-text-fill: White;");
            lblClick.setStyle("-fx-text-fill: White;");
            MenuBtn.setStyle("-fx-text-fill: White;-fx-background-color: Transparent");
            ModeBtn.setStyle("-fx-text-fill: White;-fx-background-color: Transparent");
        } else {
            DashBoardAncorPane.setStyle("-fx-background-color: #2f3542;");
            B1.setStyle("-fx-background-color: White;");
            MenuAncorPane.setStyle("-fx-background-color: White;");
            TitleBar.setStyle("-fx-background-color: White;");
            O1.setStyle("-fx-background-color: White;");
            E1.setStyle("-fx-background-color: White;");
            O1.setStyle("-fx-background-color: White;");
            U1.setStyle("-fx-background-color: White;");



            lblGreeting.setStyle("-fx-text-fill: black;");
            lblDate.setStyle("-fx-text-fill: black;");
            lblProductCount.setStyle("-fx-text-fill: black;");
            lblTitle.setStyle("-fx-text-fill: black;");
            lblUc.setStyle("-fx-text-fill: black;");
            lblUser.setStyle("-fx-text-fill: black;");
            lblTime.setStyle("-fx-text-fill: black;");
            lblOrderCount.setStyle("-fx-text-fill: black;");
            lblUserCount.setStyle("-fx-text-fill: black;");
            lblEmployeeCount.setStyle("-fx-text-fill: black;");
            LblBc.setStyle("-fx-text-fill: black;");
            LblEc.setStyle("-fx-text-fill: black;");
            lblOc.setStyle("-fx-text-fill: black;");
            lblL1.setStyle("-fx-text-fill: black;");
            lblL2.setStyle("-fx-text-fill: black;");
            lblClick.setStyle("-fx-text-fill: black;");
            MenuBtn.setStyle("-fx-text-fill: black; -fx-background-color: Transparent");
            ModeBtn.setStyle("-fx-text-fill: black; -fx-background-color: Transparent");


        }
        darkMode = !darkMode;
    }

    private void displayTodayBookings() {
        LocalDate today = LocalDate.now();

        try {
            Connection connection = DbConnection.getInstance().getConnection();

            String sql = "SELECT C.Customer_Name, L.Location_Address, B.Booking_Date " +
                    "FROM Booking B " +
                    "INNER JOIN Location L ON B.Location_id = L.Location_id " +
                    "INNER JOIN Customer C ON L.Customer_id = C.Customer_id " +
                    "WHERE B.Booking_Date = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(today));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                LblBookingCustomerName.setText(rs.getString("Customer_Name"));
                LblLocationBooking.setText(rs.getString("Location_Address"));
                lblBookingDate.setText(rs.getDate("Booking_Date").toLocalDate().toString());
            } else {
                // No bookings for today
                LblBookingCustomerName.setText("No bookings for today");
                LblLocationBooking.setText("");
                lblBookingDate.setText("");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void MenuBtnOnAction(ActionEvent event) {  // hide menu Bar
        if (isMenuVisible) {
            hideMenu();
        } else {
            showMenu();
        }
        isMenuVisible = !isMenuVisible;
    }

    private void hideMenu() {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), MenuAncorPane);
        transition.setToX(-MenuAncorPane.getWidth());
        transition.play();
    }

    private void showMenu() {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), MenuAncorPane);
        transition.setToX(0);
        transition.play();
    }
    @FXML
    void SearchBtnOnAction(ActionEvent event) {
        String EmployeeID = txtSearch.getText();

        try {
            Employee employee = EmployeeRepo.searchById(EmployeeID);
            if (employee != null) {
                lblEName.setText(employee.getEmpName());
                lblEContatc.setText(employee.getEmpPhone());
                lblEmail.setText(employee.getEmpEmail());
            } else {
                showAlert(Alert.AlertType.ERROR, "Employee not found.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error occurred while fetching Employee: " + e.getMessage());
        }
    }


    @FXML
    void btnBookingOnAction(ActionEvent event) throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Booking.fxml"));
            Parent rootNode = loader.load();
        rootNode.setTranslateX(SpecialDataPane.getWidth());
        SpecialDataPane.getChildren().clear();
        SpecialDataPane.getChildren().add(rootNode);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), rootNode);
        transition.setToX(0);
        transition.play();


    }

    @FXML
    void btnCustomerOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/CustomerForm.fxml"));
        Parent rootNode = loader.load();
        rootNode.setTranslateX(SpecialDataPane.getWidth());
        SpecialDataPane.getChildren().clear();
        SpecialDataPane.getChildren().add(rootNode);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), rootNode);
        transition.setToX(0);
        transition.play();
    }

    @FXML
    void btnEmployyeOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/EmployeeForm.fxml"));
        Parent rootNode = loader.load();
        rootNode.setTranslateX(SpecialDataPane.getWidth());
        SpecialDataPane.getChildren().clear();
        SpecialDataPane.getChildren().add(rootNode);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), rootNode);
        transition.setToX(0);
        transition.play();
    }



    @FXML
    void btnLocationOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LocationForm.fxml"));
        Parent rootNode = loader.load();
        rootNode.setTranslateX(SpecialDataPane.getWidth());
        SpecialDataPane.getChildren().clear();
        SpecialDataPane.getChildren().add(rootNode);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), rootNode);
        transition.setToX(0);
        transition.play();
    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LognForm.fxml"));
        Parent rootNode = loader.load();

        Stage newStage = new Stage();
        newStage.setScene(new Scene(rootNode));

        newStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void btnOrderOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Order.fxml"));
        Parent rootNode = loader.load();
        SpecialDataPane.getChildren().clear();
        SpecialDataPane.getChildren().add(rootNode);
        rootNode.setTranslateX(SpecialDataPane.getWidth());
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), rootNode);
        transition.setToX(0);
        transition.play();
    }

    @FXML
    void btnProductOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Product.fxml"));
        Parent rootNode = loader.load();
        rootNode.setTranslateX(SpecialDataPane.getWidth());
        SpecialDataPane.getChildren().clear();
        SpecialDataPane.getChildren().add(rootNode);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), rootNode);
        transition.setToX(0);
        transition.play();
    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Register_form.fxml"));
        Parent rootNode = loader.load();
        SpecialDataPane.getChildren().clear();
        SpecialDataPane.getChildren().add(rootNode);
        rootNode.setTranslateX(SpecialDataPane.getWidth());
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), rootNode);
        transition.setToX(0);
        transition.play();
    }

    @FXML
    void btnShowRoomOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/ShowRoom.fxml"));
        Parent rootNode = loader.load();
        rootNode.setTranslateX(SpecialDataPane.getWidth());
        SpecialDataPane.getChildren().clear();
        SpecialDataPane.getChildren().add(rootNode);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), rootNode);
        transition.setToX(0);
        transition.play();
    }
    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void txtSearchKeyReleased(KeyEvent event) {
        CustomerRegex.setTextColor2(CustomerTextField.ID,txtSearch);
//        try {
//            String searchText = txtSearch.getText();
//            List<String> suggestedIDs = EmployeeRepo.getIds();
//
//            txtSearch.clear();
//
//            for (String id : suggestedIDs) {
//                if (id.startsWith(searchText)) {
//                    txtSearch.appendText(id);
//                    txtSearch.requestFocus(); // Set focus back to the text field
//                    txtSearch.end(); // Move the caret to the end of the text
//                    break; // Stop after adding the first suggestion
//                }
//            }
//        } catch (SQLException e) {
//            showAlert(Alert.AlertType.ERROR, "Error occurred while fetching suggestions: " + e.getMessage());
//        }
    }

}
