package lk.Ijse.repository;

import lk.Ijse.Db.DbConnection;
import lk.Ijse.Model.PlaceOrder;

import java.sql.Connection;
import java.sql.SQLException;

public class PlaceOrderRepo {
    public static boolean placeOrder(PlaceOrder po) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            boolean isPayUpdated = PaymentRepo.save(po.getPayment());
            if (isPayUpdated) {
            boolean isOrderSaved = OrderRepo.save(po.getOrder());
            if (isOrderSaved) {
                boolean isQtyUpdated = ProductsRepo.update1(po.getOdList());
                if (isQtyUpdated) {
                    boolean isOrderDetailSaved = OrderDetailRepo.save(po.getOdList());
                    if (isOrderDetailSaved) {

                        connection.commit();
                        return true;
                    }
                    }
                }
            }
            connection.rollback();
            return false;
        } catch (Exception e) {
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
