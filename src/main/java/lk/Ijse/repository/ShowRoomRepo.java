package lk.Ijse.repository;

import lk.Ijse.Db.DbConnection;
import lk.Ijse.Model.ShowRoom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShowRoomRepo {

    public static boolean save(ShowRoom showRoom) throws SQLException {
        String sql = "INSERT INTO ShowRoom (ShowRoom_id, ShowRoom_Location) VALUES (?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, showRoom.getShowRoomId());
        pstm.setString(2, showRoom.getShowRoomLocation());


        return pstm.executeUpdate() > 0;
    }

    public static ShowRoom searchById(String id) throws SQLException {
        String sql = "SELECT * FROM ShowRoom WHERE ShowRoom_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            String showRoomId = rs.getString("ShowRoom_id");
            String location = rs.getString("ShowRoom_Location");


            ShowRoom showRoom = new ShowRoom(showRoomId, location);

            return showRoom;
        }
        return null;
    }

    public static boolean update(ShowRoom showRoom) throws SQLException {
        String sql = "UPDATE ShowRoom SET ShowRoom_Location = ? WHERE ShowRoom_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, showRoom.getShowRoomLocation());
        pstm.setString(2, showRoom.getShowRoomId());
        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM ShowRoom WHERE ShowRoom_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }

    public static List<ShowRoom> getAll() throws SQLException {
        String sql = "SELECT * FROM ShowRoom";

        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<ShowRoom> showRoomList = new ArrayList<>();

        while (resultSet.next()) {
            String showRoomId = resultSet.getString("ShowRoom_id");
            String location = resultSet.getString("ShowRoom_Location");
            ShowRoom showRoom = new ShowRoom(showRoomId, location);
            showRoomList.add(showRoom);
        }
        return showRoomList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT ShowRoom_id FROM ShowRoom";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString("ShowRoom_id");
            idList.add(id);
        }
        return idList;
    }
    public static String getBookingCurrentId() throws SQLException {
        String sql = "SELECT ShowRoom_id FROM ShowRoom ORDER BY ShowRoom_id DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String ShowRoom_id = resultSet.getString(1);
            return ShowRoom_id;
        }
        return null;
    }
}
