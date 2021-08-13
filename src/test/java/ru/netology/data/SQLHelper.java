package ru.netology.data;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import ru.netology.dto.PaymentInfoModel;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    public SQLHelper() {
    }

    public static PaymentInfoModel getPaymentInfo() {
        String dbUrl = System.getProperty("database.url");
        String dbUser = System.getProperty("database.name");
        String dbPassword = System.getProperty("database.password");

        var getInfo = "SELECT * FROM payment_entity ORDER BY created DESC LIMIT 1;";
        var runner = new QueryRunner();

        try (
                var conn = DriverManager.getConnection(
                        dbUrl, dbUser, dbPassword)
        ) {
            return runner.query(conn, getInfo, new BeanHandler<>(PaymentInfoModel.class));

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    public static PaymentInfoModel getPaymentWithCreditInfo() {
        String dbUrl = System.getProperty("database.url");
        String dbUser = System.getProperty("database.name");
        String dbPassword = System.getProperty("database.password");

        var getInfo = "SELECT * FROM credit_request_entity ORDER BY created DESC LIMIT 1;";
        var runner = new QueryRunner();

        try (
                var conn = DriverManager.getConnection(
                        dbUrl, dbUser, dbPassword)
        ) {
            return runner.query(conn, getInfo, new BeanHandler<>(PaymentInfoModel.class));

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    public static void shouldDeleteAfterPayment() {
        String dbUrl = System.getProperty("database.url");
        String dbUser = System.getProperty("database.name");
        String dbPassword = System.getProperty("database.password");

        var clearPayment = "DELETE FROM payment_entity";
        var clearPaymentWithCredit = "DELETE FROM credit_request_entity";
        var clearOrder = "DELETE FROM order_entity";
        var runner = new QueryRunner();

        try (
                var conn = DriverManager.getConnection(
                        dbUrl, dbUser, dbPassword)
        ) {
            runner.update(conn, clearOrder);
            runner.update(conn, clearPayment);
            runner.update(conn, clearPaymentWithCredit);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}