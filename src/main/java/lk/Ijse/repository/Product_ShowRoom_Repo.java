package lk.Ijse.repository;

import lk.Ijse.Db.DbConnection;
import lk.Ijse.Model.Product_ShowRoom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Product_ShowRoom_Repo {

    public static boolean save(Product_ShowRoom ps) throws SQLException {
        String sql = "INSERT INTO Product_ShowRoom  VALUES(?,?)";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1, ps.getProductID());
            pstm.setString(2, ps.getShowRoomId());

            return pstm.executeUpdate() > 0;

        }
    public static List<Product_ShowRoom> getAll() throws SQLException {
        String sql = "SELECT * FROM Product_ShowRoom";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Product_ShowRoom> PSList = new ArrayList<>();

        while (resultSet.next()) {
            String P_ID = resultSet.getString(2);
            String S_Id = resultSet.getString(1);

            Product_ShowRoom ps = new Product_ShowRoom(P_ID,S_Id);
            PSList.add(ps);
        }
        return PSList;
    }
    }

