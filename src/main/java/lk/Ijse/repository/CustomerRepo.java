package lk.Ijse.repository;

import lk.Ijse.Db.DbConnection;
import lk.Ijse.Model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepo {
    public static boolean save(Customer customer) throws SQLException {
        String sql = "INSERT INTO Customer VALUES(?,?,?,?,?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,customer.getId());
        pstm.setObject(2,customer.getName());
        pstm.setObject(3, customer.getAddress());
        pstm.setObject(4,customer.getContact());
        pstm.setObject(5,customer.getEmail());

           return pstm.executeUpdate() > 0;
    }
    public static Customer searchById(String Contact) throws SQLException {
 String sql = "SELECT * FROM Customer WHERE Customer_Contact = ?";
 Connection connection = DbConnection.getInstance().getConnection();
 PreparedStatement pstm = connection.prepareStatement(sql);
 pstm.setObject(1,Contact);
 ResultSet rs = pstm.executeQuery();
 if (rs.next()){
     String cus_id = rs.getString(1);
     String Name = rs.getString(2);
     String Address = rs.getString(3);
      String contact = rs.getString(4);
     String Email = rs.getString(5);

     Customer customer = new Customer(cus_id,Name,Address,contact,Email);

      return customer;
////
 }

 return null;

    }

    public static Customer searchById1(String id) throws SQLException {
        String sql = "SELECT * FROM Customer WHERE Customer_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            String cus_id = rs.getString(1);
            String Name = rs.getString(2);
            String Address = rs.getString(3);
            String contact = rs.getString(4);
            String Email = rs.getString(5);

            Customer customer = new Customer(cus_id, Name, Address, contact, Email);

            return customer;

        }

        return null;
    }
    public static boolean update(Customer customer) throws SQLException {
        String sql = "UPDATE Customer SET Customer_Name = ?, Customer_Address = ?, Customer_Contact = ?,Customer_Email = ? WHERE Customer_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,customer.getName());
        pstm.setObject(2,customer.getAddress());
        pstm.setObject(3,customer.getContact());
        pstm.setObject(4,customer.getEmail());
        pstm.setObject(5,customer.getId());
        return pstm.executeUpdate() > 0;



    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM Customer WHERE Customer_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }
    public static List<Customer> getAll() throws SQLException {
        String sql = "SELECT * FROM Customer";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Customer> cusList = new ArrayList<>();

        while (resultSet.next()) {
            String cus_id = resultSet.getString(1);
            String Name = resultSet.getString(2);
            String Address = resultSet.getString(3);
            String Contact = resultSet.getString(4);
            String Email = resultSet.getString(5);
            Customer customer = new Customer(cus_id,Name,Address,Contact,Email);
            cusList.add(customer);
        }
        return cusList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT Customer_id FROM Customer";
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
    public static String getCustomerCurrentId() throws SQLException {
        String sql = "SELECT Customer_id FROM Customer ORDER BY Customer_id DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String Customer_id = resultSet.getString(1);
            return Customer_id;
        }
        return null;
    }
}




