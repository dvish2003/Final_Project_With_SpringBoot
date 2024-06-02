
package lk.Ijse.repository;

import lk.Ijse.Db.DbConnection;
import lk.Ijse.Model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepo {
    public static boolean save(Payment payment) throws SQLException {
        String sql = "INSERT INTO Payment VALUES(?,?,?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,payment.getPaymentId());
        pstm.setObject(2,payment.getPaymentAmount());
        pstm.setObject(3, payment.getPaymentDate());


        return pstm.executeUpdate() > 0;
    }
    public static Payment searchById(String id) throws SQLException {
        String sql = "SELECT * FROM Payment WHERE Payment_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,id);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()){
            String Pay_ID = rs.getString(1);
            int Amount = Integer.parseInt(rs.getString(2));
            Date date = Date.valueOf(rs.getString(3));


Payment payment = new Payment(Pay_ID,Amount,date);
            return payment;

        }
        return null;

    }

    public static List<Payment> getAll() throws SQLException {
        String sql = "SELECT * FROM Payment";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Payment> paymentList = new ArrayList<>();

        while (resultSet.next()) {
            String Pay_ID = resultSet.getString(1);
            int Amount = Integer.parseInt(resultSet.getString(2));
            Date date = Date.valueOf(resultSet.getString(3));

            Payment payment = new Payment(Pay_ID,Amount,date);
            paymentList.add(payment);
        }
        return paymentList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT Payment_id FROM Payment";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            idList.add(id);
        }
        return idList;
    }
}




