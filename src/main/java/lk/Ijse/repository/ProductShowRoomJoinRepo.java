package lk.Ijse.repository;

import lk.Ijse.Db.DbConnection;
import lk.Ijse.Model.ProductShowRoomJoin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductShowRoomJoinRepo {
    public static List<ProductShowRoomJoin> getAll() throws SQLException {
        String sql = "SELECT \n" +
                "    Product.Product_id,\n" +
                "    Product.Product_Description,\n" +
                "    Product.ShowRoom_QtyOnHand,\n" +
                "    Product.Product_UnitPrice,\n" +
                "    Product_ShowRoom.ShowRoom_id\n" +
                "FROM \n" +
                "    Product\n" +
                "JOIN \n" +
                "    Product_ShowRoom ON Product.Product_id = Product_ShowRoom.Product_id;\n";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        List<ProductShowRoomJoin> productShowRoomJoinList = new ArrayList<>();
        while (resultSet.next()) {
            String showRoomId = resultSet.getString("ShowRoom_id");
            String productId = resultSet.getString("Product_id");
            String productDescription = resultSet.getString("Product_Description");
            int showRoomQtyOnHand = resultSet.getInt("ShowRoom_QtyOnHand");
            int productUnitPrice = resultSet.getInt("Product_UnitPrice");


            productShowRoomJoinList.add(new ProductShowRoomJoin(showRoomId,productId, productDescription, showRoomQtyOnHand, productUnitPrice));
        }
        return productShowRoomJoinList;
    }
}
