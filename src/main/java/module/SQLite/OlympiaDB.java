package module.SQLite;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class OlympiaDB extends SQLiteConnection {
    public static SQLiteConnection sqLiteConnection1 = new SQLiteConnection();
    public static int getRandomIdR2() {
        Random rand = new Random();
        int ans = rand.nextInt(1, 3);
        return ans;
    }
    public static int randomIdR2 = getRandomIdR2();
    public void setConnectionOlympia() {
        sqLiteConnection1.setConnection("jdbc:sqlite:src/main/resources/database/Olympia.db");
    }

    public ResultSet executeStartUp(int counter ) throws SQLException {
        String dataTable = "KD";
        String searchQuery = "SELECT Question FROM " + dataTable + " WHERE ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);
        preparedStatement.setInt(1,counter +1);
        return preparedStatement.executeQuery();
    }

    public ResultSet getAnswerR1(int counter ) throws SQLException {
        String dataTable = "KD";
        String searchQuery = "SELECT Answer FROM " + dataTable + " WHERE ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);
        preparedStatement.setInt(1,counter +1);
        return preparedStatement.executeQuery();
    }
    public ResultSet executeObstacle(String question) throws SQLException {
        String dataTable = "VCNV";
        String searchQuery = "SELECT " + question +  " FROM " + dataTable;
        PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);
        return preparedStatement.executeQuery();
    }

    public static ResultSet addImageR2() throws SQLException {
        String dataTable = "VCNV";
        String searchQuery = "SELECT Image FROM " + dataTable + " WHERE ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);
        preparedStatement.setInt(1,randomIdR2);
        return preparedStatement.executeQuery();
    }

    public static ArrayList<String> getQuestionR2() throws SQLException {
        String dataTable = "VCNV";
        String searchQuery = "SELECT Ques1,Ques2,Ques3,Ques4,Ques5 FROM " + dataTable + " WHERE ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);
        preparedStatement.setInt(1,randomIdR2);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<String> ans = new ArrayList<>();
        if (resultSet.next()) {
            for (int i = 1;i<=5;i++) {
                ans.add(resultSet.getString("Ques" + i));
            }
            ans.add("The vocabulary we want to talk about here is?");
        }
        return ans;
    }

    public static ArrayList<String> getAnswerR2() throws SQLException {
        String dataTable = "VCNV";
        String searchQuery = "SELECT Ans1,Ans2,Ans3,Ans4,Ans5,AnsFinal FROM " + dataTable + " WHERE ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);
        preparedStatement.setInt(1,randomIdR2);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<String> ans = new ArrayList<>();
        if (resultSet.next()) {
            for (int i = 1;i<=5;i++) {
                ans.add(resultSet.getString("Ans" + i));
            }
            ans.add(resultSet.getString("AnsFinal"));
        }
        return ans;
    }
    public ResultSet executeSpeedUp() throws SQLException {
        String dataTable = "TT";
        String searchQuery = "SELECT Question FROM " + dataTable;
        PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);
        return preparedStatement.executeQuery();
    }

    public ResultSet executeFinish() throws SQLException {
        String dataTable = "VD";
        String searchQuery = "SELECT Question FROM " + dataTable;
        PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);
        return preparedStatement.executeQuery();
    }

    public static void main(String[] args) throws SQLException {}
}
