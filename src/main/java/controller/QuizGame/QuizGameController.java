package controller.QuizGame;

import com.example.jdictionaryev1.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import module.SQLite.SQLiteConnection;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class QuizGameController extends SQLiteConnection {
    @FXML
    public Scene scene;
    @FXML
    public Label question;

    @FXML
    public Button opt1;
    public Button opt2;
    public Button opt3;
    public Button opt4;
   public Button outGame;


    static int counter = 0;
    static int correct = 0;
    static int wrong = 0;
    static Boolean[] asked = new Boolean[11];

    int getRandom() {
        Random rand = new Random();
        int ans = rand.nextInt(1,11);
        while (asked[ans] && Arrays.asList(asked).contains(false)) {
            ans = rand.nextInt(1,11);
        }
        return ans;
    }

    int randomId;
    @FXML
    private void initialize() {
        Arrays.fill(asked,false);
        loadQuestions();
    }



    private void loadQuestions() {
        SQLiteConnection sqLiteConnection1 = new SQLiteConnection();
        sqLiteConnection1.setConnection(dbName);
        try {
            String query = "SELECT id, description, choice1, choice2, choice3, choice4 FROM question WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            randomId = getRandom();
            asked[randomId] = true;
            preparedStatement.setInt(1, randomId); // Assuming your questions start from 1
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                question.setText(String.valueOf(counter+1)+"."+resultSet.getString("description"));
                opt1.setText(resultSet.getString("choice1"));
                opt2.setText(resultSet.getString("choice2"));
                opt3.setText(resultSet.getString("choice3"));
                opt4.setText(resultSet.getString("choice4"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkAnswer(String answer) {
        try {
            SQLiteConnection sqLiteConnection2 = new SQLiteConnection();
            sqLiteConnection2.setConnection(dbName);
            String query = "SELECT answer FROM question WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, randomId); // Assuming your questions start from 1
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String correctAnswer = resultSet.getString("answer");
                return answer.equals(correctAnswer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Default to false in case of any error
    }

    @FXML
    public void opt1clicked(ActionEvent event) {
        checkAnswer(opt1.getText().toString());
        if (checkAnswer(opt1.getText().toString())) {
            correct = correct + 1;
        } else {
            wrong = wrong + 1;
        }
        if (counter == 9) {
            try {
                counter = 0; // reset game
                HelloApplication helloApplication = new HelloApplication();
                helloApplication.changeScreen("Result.fxml",700,700);
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            counter++;
            loadQuestions();
        }

    }
    @FXML
    public void opt2clicked(ActionEvent event) {
        checkAnswer(opt2.getText().toString());
        if (checkAnswer(opt2.getText().toString())) {
            correct = correct + 1;
        } else {
            wrong = wrong + 1;
        }
        if (counter == 9) {
            try {
                counter = 0; // reset game
                HelloApplication helloApplication = new HelloApplication();
                helloApplication.changeScreen("Result.fxml",700,700);
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            counter++;
            loadQuestions();
        }
    }

    @FXML
    public void opt3clicked(ActionEvent event) {
        checkAnswer(opt3.getText().toString());
        if (checkAnswer(opt3.getText().toString())) {
            correct = correct + 1;
        } else {
            wrong = wrong + 1;
        }
        if (counter == 9) {
            try {
                counter = 0; // reset game
                HelloApplication helloApplication = new HelloApplication();
                helloApplication.changeScreen("Result.fxml",700,700);
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            counter++;
            loadQuestions();
        }
    }

    @FXML
    public void opt4clicked(ActionEvent event) {
        checkAnswer(opt4.getText().toString());
        if (checkAnswer(opt4.getText().toString())) {
            correct = correct + 1;
        } else {
            wrong = wrong + 1;
        }
        if (counter == 9) {
            try {
                counter = 0; // reset game
                HelloApplication helloApplication = new HelloApplication();
                helloApplication.changeScreen("Result.fxml",700,700);
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            counter++;
            loadQuestions();
        }
    }

    //-------------out game------------------
    public void returnApp(ActionEvent event) throws IOException {
        //xóa dữ liệu game
        counter = 0;
        correct = 0;
        wrong = 0;
        randomId = 0;
        Arrays.fill(asked,false);
        //chuyển về màn hình app
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScreen("Game.fxml",840,540);
    }

}
