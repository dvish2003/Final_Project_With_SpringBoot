package lk.Ijse.repository;

import lk.Ijse.Db.DbConnection;
import lk.Ijse.Model.Order;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRepo {
    public static String getCurrentId() throws SQLException {
        String sql = "SELECT Order_id FROM `Order` ORDER BY Order_id DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String orderId = resultSet.getString(1);
            return orderId;
        }
        return null;
    }
    public static String getPayCurrentId() throws SQLException {
        String sql = "SELECT Payment_id FROM Payment ORDER BY Payment_id DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String PayId = resultSet.getString(1);
            return PayId;
        }
        return null;
    }
    public static boolean save(Order order) throws SQLException {
        String sql = "INSERT INTO `Order` VALUES(?, ?, ?,?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setString(1, order.getOrderId());
        pstm.setString(2, order.getCustomerId());
        pstm.setString(3, order.getPayment_Id());
        pstm.setDate(4, order.getOrderPlaceDate());

        return pstm.executeUpdate() > 0;
    }

}