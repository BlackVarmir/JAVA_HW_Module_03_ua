package util;

//import static java.lang.System.out;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/";
    private static final String DATABASE_NAME = "taxdb";
//    private static final String USER = "postgres";
//    private static final String PASSWORD = "1";

    private static String username;
    private static String password;

    static {
        try {
            initializeCredentials();
            createDatabaseIfNotExists();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Не вдалося ініціалізувати базу даних.");
        }
    }

    private static void initializeCredentials() {
        username = UserInput.getUsername();
        password = UserInput.getPassword();
    }

    private static void createDatabaseIfNotExists() throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, username, password);
            DatabaseMetaData metaData = connection.getMetaData();
            if (!databaseExists(metaData, DATABASE_NAME)) {
                createDatabase(connection);
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private static boolean databaseExists(DatabaseMetaData metaData, String databaseName) throws SQLException {
        try (var rs = metaData.getCatalogs()) {
            while (rs.next()) {
                String dbName = rs.getString(1);
                if (dbName.equalsIgnoreCase(databaseName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void createDatabase(Connection connection) throws SQLException {
        connection.createStatement().executeUpdate("CREATE DATABASE " + DATABASE_NAME);
    }

    public static Connection getConnection() throws SQLException {
        if (username == null || password == null) {
            initializeCredentials();
        }
        try {
            return DriverManager.getConnection(URL + DATABASE_NAME, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }
}
