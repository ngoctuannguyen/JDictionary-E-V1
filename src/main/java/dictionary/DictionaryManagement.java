package dictionary;


import module.SQLite.SQLiteConnection;
import module.TextToSpeech;
import searchingAlgorithm.Trie;
import utils.Utils;
import javax.speech.AudioException;
import javax.speech.EngineException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DictionaryManagement extends SQLiteConnection {

    public Trie trie = new Trie();
    public List<String> wordList = new ArrayList<>();
    public Utils utils = new Utils();
    public DictionaryManagement() {

    }

    public String dictionarySearcher(String word, SQLiteConnection sqLiteConnection) throws SQLException {
        String searchQuery = "SELECT html FROM " + dataTable + " WHERE word LIKE '" + word + "'";

        try (ResultSet resultSet = sqLiteConnection.query(searchQuery)) {
            if (resultSet.next()) {
                return resultSet.getString(1);
            } else {
                return "Đần";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception to signal the failure
        }
    }

    public void dictionarySpelling(String word) {
        TextToSpeech textToSpeech = new TextToSpeech();
        try {
            textToSpeech.Spelling(word);
        } catch (EngineException e) {
            throw new RuntimeException(e);
        } catch (AudioException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteWord(String word) {
        SQLiteConnection sqLiteConnection1 = new SQLiteConnection();
        sqLiteConnection1.setConnection(dbName);
        String deleteQuery = "DELETE FROM " + dataTable + " WHERE word = " + '"' + word +'"';
        System.out.println(deleteQuery);
        try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void insertWord(Word word) throws SQLException {
        SQLiteConnection sqLiteConnection1 = new SQLiteConnection();
        sqLiteConnection1.setConnection(dbName);
        String insertQuery = "INSERT INTO " + dataTable + "(id, word, html, pronounce)" + "VALUES(?,?,?, 0)";
        String getMaxID = "SELECT id FROM " + dataTable + " WHERE id = (SELECT MAX(id) FROM " + dataTable + ")";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(getMaxID);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getMaxID);
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, resultSet.getInt("id")+ 1);
            preparedStatement.setString(2, Word.wordFound);
            preparedStatement.setString(3, utils.generateHtmlForWord(word));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
        }

    }

    public void editWord(Word word) throws SQLException {
        String editQuery = "UPDATE " + dataTable + " SET html = ?" + " WHERE word = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(editQuery);
            preparedStatement.setString(1, utils.generateHtmlForWord(word));
            preparedStatement.setString(2, word.getWordFound());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    public void getAllWord(String dbName) {
        String query = "SELECT WORD FROM AV";
        int count = 0;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String word = resultSet.getString("WORD");
                wordList.add(word);
                count++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Trie getTrie() {
        return trie;
    }

    public void setTrie() {
        try {
            for (String word : wordList) {
                trie.insert(word);
            }
        } catch (NullPointerException e) {
            System.out.println("Something went wrong: " + e);
        }
    }

    public static void main(String[] args) throws SQLException {
    }
}
