package lk.Ijse.repository;

import lk.Ijse.Db.DbConnection;
import lk.Ijse.Model.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocationRepo {

    public static boolean save(Location location) throws SQLException {
        String sql = "INSERT INTO Location (Customer_id, Location_id, Location_Province, Location_City, Location_Address, Location_ZipCode) VALUES (?, ?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, location.getCustomerId());
        pstm.setString(2, location.getId());
        pstm.setString(3, location.getProvince());
        pstm.setString(4, location.getCity());
        pstm.setString(5, location.getAddress());
        pstm.setString(6, location.getZipCode());

        return pstm.executeUpdate() > 0;
    }

    public static  Location searchById(String id) throws SQLException {
        String sql = "SELECT * FROM Location WHERE Location_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            String customerId = resultSet.getString("Customer_id");
            String loId = resultSet.getString("Location_id");
            String loProvince = resultSet.getString("Location_Province");
            String loCity = resultSet.getString("Location_City");
            String loAddress = resultSet.getString("Location_Address");
            String loZipCode = resultSet.getString("Location_ZipCode");



            return new   Location(customerId,loId, loProvince, loCity, loAddress, loZipCode);
        }
        return null;
    }

    public static boolean update(Location location) throws SQLException {
        String sql = "UPDATE Location SET Customer_id = ? ,Location_Province = ?, Location_City = ?, Location_Address = ?, Location_ZipCode = ? WHERE Location_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, location.getCustomerId());
        pstm.setString(2, location.getProvince());
        pstm.setString(3, location.getCity());
        pstm.setString(4, location.getAddress());
        pstm.setString(5, location.getZipCode());
        pstm.setString(6, location.getId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM Location WHERE Location_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static List<Location> getAll() throws SQLException {
        String sql = "SELECT * FROM Location";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        List<Location> locationList = new ArrayList<>();
        while (resultSet.next()) {

            String customerId = resultSet.getString("Customer_id");
            String loId = resultSet.getString("Location_id");
            String loProvince = resultSet.getString("Location_Province");
            String loCity = resultSet.getString("Location_City");
            String loAddress = resultSet.getString("Location_Address");
            String loZipCode = resultSet.getString("Location_ZipCode");


            locationList.add(new Location(customerId,loId,loProvince,loCity,loAddress,loZipCode));
        }
        return locationList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT Location_id FROM Location";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        List<String> idList = new ArrayList<>();
        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            idList.add(id);
        }
        return idList;
    }
    public static String getLocationCurrentId() throws SQLException {
        String sql = "SELECT Location_id FROM Location ORDER BY Location_id DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String Location_id = resultSet.getString(1);
            return Location_id;
        }
        return null;
    }
}