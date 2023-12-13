package com.example.jdictionaryev1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import module.SQLite.SQLiteConnection;

import java.io.IOException;
import java.sql.*;
public class AddQuestionController extends SQLiteConnection {

    @FXML
    public Stage stage;
    @FXML
    public Scene scene;

    @FXML
    public void switchToStartOlympiaGame(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("OlympiaStart.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1080, 608);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private TextField answerInsert;

    @FXML
    private TextField questionInsert;

    @FXML
    private TextField choice1Insert;

    @FXML
    private TextField choice2Insert;

    @FXML
    private TextField choice3Insert;

    @FXML
    private TextField choice4Insert;

    public  void addQuestion(ActionEvent event) throws SQLException {
        SQLiteConnection sqLiteConnection1 = new SQLiteConnection();
        sqLiteConnection1.setConnection(dbName);
        String question = questionInsert.getText();
        String answer = answerInsert.getText();
        String choice1 = choice1Insert.getText();
        String choice2 = choice2Insert.getText();
        String choice3 = choice3Insert.getText();
        String choice4 = choice4Insert.getText();
        if (question.isEmpty() || answer.isEmpty() || choice1.isEmpty() || choice2.isEmpty() || choice3.isEmpty() || choice4.isEmpty()) {
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("One of the field is missing,please make sure to type it all");
            alert.showAndWait();
        }
        if (!answer.equalsIgnoreCase(choice1) && !answer.equalsIgnoreCase(choice2)
                && !answer.equalsIgnoreCase(choice3)  && !answer.equalsIgnoreCase(choice4)) {
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("The answer must be the same as one of the 4 choices");
            alert.showAndWait();
        }
        try (Statement statement = connection.createStatement()) {
            String insertQuery = "INSERT INTO " + SQLiteConnection.question + " (DESCRIPTION,ANSWER,Choice1,Choice2,Choice3,Choice4) " +
                    "VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1,question);
            preparedStatement.setString(2,answer);
            preparedStatement.setString(3,choice1);
            preparedStatement.setString(4,choice2);
            preparedStatement.setString(5,choice3);
            preparedStatement.setString(6,choice4);
            preparedStatement.executeUpdate();
            Alert alert =new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Add question successfully");
            alert.show();
            clearField();
        } catch (SQLException E) {
            E.printStackTrace();
        }
    }

    void clearField() {
        questionInsert.clear();
        answerInsert.clear();
        choice1Insert.clear();
        choice2Insert.clear();
        choice3Insert.clear();
        choice4Insert.clear();
    }

}
