package module.SQLite;


import java.sql.*;

public class SQLiteConnection {

    public static SQLiteConnection sqLiteConnection = new SQLiteConnection();
    public static Connection connection = null;

    public static String dbName = "jdbc:sqlite:src/main/resources/database/dict_hh.db";

    public static String dataTable = "AV";

    public static String question = "QUESTION";

    public SQLiteConnection getSqLiteConnection() {
        if (sqLiteConnection != null) {
            return sqLiteConnection = new SQLiteConnection();
        }
        return null;
    }

    public void setConnection(String path) {
        try {
            connection = DriverManager.getConnection(path);
            if (connection != null) {
                System.out.println("Connected");
            } else {
                System.out.println("Can't connect to SQLite");
            }
        } catch (SQLException e) {
            System.out.println("Can't connect to SQLite");
            e.printStackTrace();
        }
    }

    // Query
    public ResultSet query(String searchQuery) {

        ResultSet resultSet = null;

        try {
            PreparedStatement preparedStatement;
            if (connection != null) {
                preparedStatement = connection.prepareStatement(searchQuery);
                resultSet = preparedStatement.executeQuery();
            }
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public static void main(String[] args) throws SQLException {}

}
