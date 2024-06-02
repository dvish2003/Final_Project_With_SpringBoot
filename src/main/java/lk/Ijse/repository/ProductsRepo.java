package lk.Ijse.repository;

import lk.Ijse.Db.DbConnection;
import lk.Ijse.Model.OrderDetail;
import lk.Ijse.Model.Products;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductsRepo {

    public static boolean save(Products product) throws SQLException {
        String sql = "INSERT INTO Product VALUES(?,?,?,?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, product.getProduct_id());
        pstm.setString(2, product.getProduct_description());
        pstm.setInt(3, product.getShowRoom_qtyOnHand());
        pstm.setInt(4, product.getProduct_unitPrice());


        return pstm.executeUpdate() > 0;
    }

    public static Products searchById(String id) throws SQLException {
        String sql = "SELECT * FROM Product WHERE Product_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            String productId = rs.getString("Product_id");
            String description = rs.getString("Product_Description");

            int QtyOnHand = rs.getInt("ShowRoom_QtyOnHand");
            int unitPrice = rs.getInt("Product_UnitPrice");
            Products product = new Products(productId, description,QtyOnHand,unitPrice);

            return product;
        }
        return null;
    }
    public static Products searchByName(String Description) throws SQLException {
        String sql = "SELECT * FROM Product WHERE Product_Description = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, Description);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            String productId = rs.getString("Product_id");
            String description = rs.getString("Product_Description");

            int QtyOnHand = rs.getInt("ShowRoom_QtyOnHand");
            int unitPrice = rs.getInt("Product_UnitPrice");
            Products product = new Products(productId, description,QtyOnHand,unitPrice);

            return product;
        }
        return null;
    }

    public static boolean update(Products product, int qtyChange) throws SQLException {
        String sql = "UPDATE Product SET Product_Description = ?, ShowRoom_QtyOnHand = ShowRoom_QtyOnHand + ?, Product_UnitPrice = ? WHERE Product_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, product.getProduct_description());
        pstm.setInt(2, product.getShowRoom_qtyOnHand());
        pstm.setInt(3, product.getProduct_unitPrice());
        pstm.setString(4, product.getProduct_id());
        return pstm.executeUpdate() > 0;
    }


    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM Product WHERE Product_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }

  public static List<Products> getAll() throws SQLException {
        String sql = "SELECT * FROM Product";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Products> productList = new ArrayList<>();

        while (resultSet.next()) {
            String productId = resultSet.getString("Product_id");
            String description = resultSet.getString("Product_Description");
            int QtyOnHand = resultSet.getInt("ShowRoom_QtyOnHand");
            int unitPrice = resultSet.getInt("Product_UnitPrice");
            Products product = new Products(productId, description,QtyOnHand,unitPrice);
            productList.add(product);
        }
        return productList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT Product_id FROM Product";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString("Product_id");
            idList.add(id);
        }
        return idList;
    }
    public static List<String> getNames() throws SQLException {
        String sql = "SELECT Product_Description FROM Product";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String Name = resultSet.getString("product_description");
            idList.add(Name);
        }
        return idList;
    }
    public static boolean update1(List<OrderDetail> odList) throws SQLException {
        for (OrderDetail od : odList) {
            boolean isUpdateQty = updateQty(od.getProductId(), od.getQuantity());
            if(!isUpdateQty) {
                return false;
            }
        }
        return true;
    }

    private static boolean updateQty(String Id, int qty) throws SQLException {
        String sql = "UPDATE Product SET ShowRoom_QtyOnHand = ShowRoom_QtyOnHand - ? WHERE Product_id = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setInt(1, qty);
        pstm.setString(2, Id);

        return pstm.executeUpdate() > 0;
    }
    public static String getProductCurrentId() throws SQLException {
        String sql = "SELECT Product_id FROM Product ORDER BY Product_id DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String Product_id = resultSet.getString(1);
            return Product_id;
        }
        return null;
    }

}
