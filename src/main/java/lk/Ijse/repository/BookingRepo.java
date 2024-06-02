package lk.Ijse.repository;

import lk.Ijse.Db.DbConnection;
import lk.Ijse.Model.Booking;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingRepo {
    public static boolean save(Booking booking) throws SQLException {
        String sql = "INSERT INTO Booking (Booking_id, Employee_id, Location_id, Payment_id, Booking_Date, Place_Date, Booking_Description) VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, booking.getBookingId());
        pstm.setString(2, booking.getEmpId());
        pstm.setString(3, booking.getLocId());
        pstm.setString(4, booking.getPaymentId());
        pstm.setDate(5, booking.getBookingDate());
        pstm.setDate(6, booking.getPlaceDate());
        pstm.setString(7, booking.getBookingDescription());

        return pstm.executeUpdate() > 0;
    }

    public static Booking searchById(String id) throws SQLException {
        String sql = "SELECT * FROM Booking WHERE Booking_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()){
            String BookingId = rs.getString(1);
            String empId = rs.getString(2);
            String LocId = rs.getString(3);
            String paymentId = rs.getString(4);
            Date bookingDate = rs.getDate(5);
            Date PlaceDate = rs.getDate(6);
            String bookingDescription = rs.getString(7);
            Booking booking = new Booking(BookingId,empId,LocId,paymentId,bookingDate,PlaceDate,bookingDescription);
return booking;
        }
        return null;

    }

    public static boolean update(Booking booking) throws SQLException {
        String sql = "UPDATE Booking SET Employee_id = ?, Location_id = ?, Payment_id = ?, Booking_Date = ?, Place_Date = ?, Booking_Description = ? WHERE Booking_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, booking.getEmpId());
        pstm.setString(2, booking.getLocId());
        pstm.setString(3, booking.getPaymentId());
        pstm.setDate(4, booking.getBookingDate());
        pstm.setDate(5, booking.getPlaceDate());
        pstm.setString(6, booking.getBookingDescription());
        pstm.setString(7, booking.getBookingId());
        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM Booking WHERE Booking_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, id);

        return pstm.executeUpdate() > 0;
    }
    public static List<Booking> getAll() throws SQLException {
        String sql = "SELECT * FROM Booking";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Booking> bookList = new ArrayList<>();

        while (resultSet.next()) {
            String BookingId = resultSet.getString(1);
            String empId = resultSet.getString(2);
            String LocId = resultSet.getString(3);
            String paymentId = resultSet.getString(4);
            Date bookingDate = resultSet.getDate(5);
            Date PlaceDate = resultSet.getDate(6);
            String bookingDescription = resultSet.getString(7);
            Booking booking = new Booking(BookingId,empId,LocId,paymentId,bookingDate,PlaceDate,bookingDescription);
            bookList.add(booking);
        }
        return bookList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT Booking_id FROM Booking";
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
    public static String getCurrentId() throws SQLException {
        String sql = "SELECT Booking_id FROM Booking ORDER BY Booking_id DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String BookingID = resultSet.getString(1);
            return BookingID;
        }
        return null;
    }
    public static List<Date> getBookedDatesForLocation(String locationId) throws SQLException {
        List<Date> bookedDates = new ArrayList<>();
        String sql = "SELECT Booking_Date FROM Booking WHERE Location_id = ?";

        try (
                Connection connection = DbConnection.getInstance().getConnection();
                PreparedStatement pstm = connection.prepareStatement(sql)
        ) {
            pstm.setString(1, locationId);
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                bookedDates.add(resultSet.getDate("Booking_Date"));
            }
        }

        return bookedDates;
    }
    public static void deleteExpiredBookings() throws SQLException {
        String query = "DELETE FROM Booking WHERE Booking_Date < ?";

        try (Connection connection = DbConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
            int rowsDeleted = preparedStatement.executeUpdate();
            System.out.println("Deleted " + rowsDeleted + " expired bookings.");
        }

}


}
