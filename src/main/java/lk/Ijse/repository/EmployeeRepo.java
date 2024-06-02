package lk.Ijse.repository;

import lk.Ijse.Db.DbConnection;
import lk.Ijse.Model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepo {
    public static boolean save(Employee employee) throws SQLException {
        String sql = "INSERT INTO Employee VALUES(?,?,?,?,?,?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, employee.getEmpId());
        pstm.setObject(2, employee.getEmpName());
        pstm.setObject(3, employee.getEmpAge());
        pstm.setObject(4, employee.getEmpAddress());
        pstm.setObject(5, employee.getEmpPhone());
        pstm.setObject(6, employee.getEmpEmail());

        return pstm.executeUpdate() > 0;
    }

    public static Employee searchById(String id) throws SQLException {
        String sql = "SELECT * FROM Employee WHERE Employee_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            String empId = rs.getString(1);
            String name = rs.getString(2);
            String age = rs.getString(3);
            String address = rs.getString(4);
            String contact = rs.getString(5);
            String email = rs.getString(6);

           return new Employee(empId,name,age,address,contact,email);
        }
        return null;
    }

    public static boolean update(Employee employee) throws SQLException {
        String sql = "UPDATE Employee SET Employee_Name = ?, Employee_Age = ?, Employee_Address = ?, Employee_Contact = ?,Employee_Email = ?, WHERE Employee_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1, employee.getEmpName());
        pstm.setObject(2, employee.getEmpAge());
        pstm.setObject(3, employee.getEmpAddress());
        pstm.setObject(4, employee.getEmpPhone());
        pstm.setObject(5, employee.getEmpEmail());
        pstm.setObject(6, employee.getEmpId());
        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM Employee WHERE Employee_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);
        return pstm.executeUpdate() > 0;
    }

    public static List<Employee> getAll() throws SQLException {
        String sql = "SELECT * FROM Employee";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();
        List<Employee> empList = new ArrayList<>();
        while (resultSet.next()) {
            String empId = resultSet.getString(1);
            String name = resultSet.getString(2);
            String age = resultSet.getString(3);
            String address = resultSet.getString(4);
            String contact = resultSet.getString(5);
            String email = resultSet.getString(6);
            empList.add(new Employee(empId,name,age,address,contact,email));
        }
        return empList;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT Employee_id FROM Employee";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        List<String> idList = new ArrayList<>();
        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            idList.add(id);
        }
        return idList;
    }
    public static String getEmployeeCurrentId() throws SQLException {
        String sql = "SELECT Employee_id FROM Employee ORDER BY Employee_id DESC LIMIT 1";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            String Employee_id = resultSet.getString(1);
            return Employee_id;
        }
        return null;
    }
}
