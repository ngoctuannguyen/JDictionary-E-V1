package com.example.jdictionaryev1;


import dictionary.DictionaryManagement;
import dictionary.Word;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Pair;
import module.GoogleTranslateAPI;
import module.SQLite.SQLiteConnection;
import searchingAlgorithm.Trie;
import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class HelloController extends DictionaryManagement {

    //--------------------------------tạo chuyển cảnh---------------------------
    @FXML
    public Stage stage;
    public Scene scene;

    //----------------------------- chuyển sang từ điển---------------------
    @FXML
    public void switchToDictionary() throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScreen("Dictionary.fxml",840,540);
    }

    // -----------------------------chuyển sang quiz game------------------------
    @FXML
    public void switchToGame() throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScreen("Game.fxml",840,540);

    }
    //-------------------------------Chuyển sang olympia game--------------------------
    @FXML
    public void switchToOlympiaGame() throws IOException {

        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScreen("OlympiaStart.fxml",1080,608);
        try {
            OlympiaController olympiaController = new OlympiaController();
            olympiaController.playAudioClip(new AudioClip(new File("src/main/resources/GameOlympia/OlympiaSound/Giới_thiệu_cuộc_thi_O15.mp3.mpeg").toURI().toString()));
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
        //------------------------------Chuyển sang translate----------
        @FXML
        public void switchToTranslate() throws IOException {
            HelloApplication helloApplication = new HelloApplication();
            helloApplication.changeScreen("translate.fxml",840,540);

        }

    //------------------------------ start game-------------------------
    @FXML
    public void startQuizGame(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("QuizGame.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 700, 700);
        stage.setScene(scene);
        stage.show();
    }


    //--------------------Logout/exit button----------------
    @FXML
    public Button logoutButton;
    @FXML
    public AnchorPane scenePane;
    @FXML
    public void logout() {
        //Tạo hộp xác nhận
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Do you want to save before exiting!: ");
        //System.out.println("im here");
        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) scenePane.getScene().getWindow(); //đang lỗi ở đây help me!!!!!
            System.out.println("You successfully logged out");

            stage.close();
        }
    }


    //-----------------TextField Searching-----------------------
    @FXML
    public Label labelSearch;
    public TextField textFieldSearch;
    public Button buttonSearch;
    public ListView<String> listVocab;
    public WebView view = new WebView();
    public WebEngine e;
    String wordSearch;

    // --wordSearch nhận đầu vào văn bản nhập từ bàn phím
    public void search(ActionEvent event) throws IOException {
        DictionaryManagement dictionaryManagement = new DictionaryManagement();
        SQLiteConnection sqLiteConnection3 = new SQLiteConnection();
        sqLiteConnection3.setConnection(dbName);
        dictionaryManagement.getAllWord(dbName);
        dictionaryManagement.setTrie();
        Trie trie1 = dictionaryManagement.getTrie();
        try {
            wordSearch = textFieldSearch.getText();
        } catch (Exception e) {
            System.out.println(e);  // xử lí ngoại lệ nhập văn bản

        }

        for (char c : textFieldSearch.getText().toCharArray()) {
            if (Character.isDigit(c)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Number is not a word, please try again");
                alert.showAndWait();
            }
        }
        List<String> searchResult = trie1.autoComplete(wordSearch);
        List<String> shorterResult = new ArrayList<>();
        int limit = 0;
        if (searchResult != null) {
        while (limit <= 15 && searchResult.size() > limit) {
            shorterResult.add(searchResult.get(limit));
            limit++;
        }
        ObservableList<String> res = FXCollections.observableArrayList(shorterResult);
        listVocab.setItems(res);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Word not found!");
            alert.show();
        }
        listVocab.setOnMouseClicked(event2 -> {
            view.setVisible(true);
            String selectedItem = listVocab.getSelectionModel().getSelectedItem();
            e = view.getEngine();
            if (selectedItem != null) {
                try {
                    String htmlContent = dictionaryManagement.dictionarySearcher(selectedItem, sqLiteConnection3);
                    e.loadContent(htmlContent);
                    makeSoundButton.setVisible(true); // hiện nút make sound
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    @FXML
    public void deleteTextField(){
        textFieldSearch.clear();
    }

    //-----------------------------table view hiện nghĩa của từ-------------------------


    //---------------------------tạo container button để hiện các nút add,delete,edit---------------
    @FXML
    private Button toggleButton;

    @FXML
    private VBox buttonContainer;

    private boolean buttonsVisible = false;

    @FXML
    void toggleButtonClicked() {
        buttonContainer.setSpacing(20);
        if (buttonsVisible) {
            buttonContainer.getChildren().clear();
            buttonsVisible = false;
        } else {

            Button button1 = createImageButton("EditVocab/add.png", "");
            Button button2 = createImageButton("EditVocab/bin.png", "");
            Button button3 = createImageButton("EditVocab/edit.png", "");

            // Đặt style cho nút
            button1.setStyle("-fx-background-color: #DDDDDD; -fx-background-radius: 50;");
            button2.setStyle("-fx-background-color: #DDDDDD; -fx-background-radius: 50;");
            button3.setStyle("-fx-background-color: #DDDDDD; -fx-background-radius: 50;");

            // Bắt sự kiện hover
            button1.setOnMouseEntered(e -> {
                button1.setStyle("-fx-background-color: #B5B5B5; -fx-background-radius: 50;");
            });

            button1.setOnMouseExited(e -> {
                button1.setStyle("-fx-background-color: #DDDDDD; -fx-background-radius: 50;");
            });

            button2.setOnMouseEntered(e -> {
                button2.setStyle("-fx-background-color: #B5B5B5; -fx-background-radius: 50;");
            });

            button2.setOnMouseExited(e -> {
                button2.setStyle("-fx-background-color: #DDDDDD; -fx-background-radius: 50;");
            });

            button3.setOnMouseEntered(e -> {
                button3.setStyle("-fx-background-color: #B5B5B5; -fx-background-radius: 50;");
            });

            button3.setOnMouseExited(e -> {
                button3.setStyle("-fx-background-color: #DDDDDD; -fx-background-radius: 50;");
            });

            VBox.setMargin(button2, new Insets(0, 0, 0, 20));

            button1.setOnAction(e -> setAddVocab());
            button2.setOnAction(e -> setDeleteVocab());
            button3.setOnAction(e -> setEditVocabVocab());


            buttonContainer.getChildren().addAll(button1, button2, button3);
            buttonsVisible = true;
        }
    }

    private Button createImageButton(String URL, String text) {
        // Tải hình ảnh từ tệp
        Image image = new Image(getClass().getResource("/" + URL).toExternalForm());

        // Tạo một ImageView để hiển thị hình ảnh
        ImageView imageView = new ImageView(image);

        // Tạo một nút và đặt ImageView làm nội dung
        Button button = new Button(text, imageView);
        imageView.setFitWidth(32); // Độ rộng
        imageView.setFitHeight(32); // Độ cao
        button.setMinWidth(50); // Kích thước tối thiểu chiều rộng
        button.setMinHeight(50); // Kích thước tối thiểu chiều cao
        return button;
    }

    //--------------------------------Tạo edit thêm từ----------------------------------

    @FXML
    private void setAddVocab() {
        // Create the custom dialog
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Vocabulary");

        // Set the button types (OK and Cancel)
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the labels and text fields
        Label nameLabel = new Label("New Vocab:");
        nameLabel.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;");
        TextField nameField = new TextField();
        nameField.setStyle("-fx-background-radius: 40;-fx-font-weight: bold;");

        Label descriptionLabel = new Label("Meaning:");
        descriptionLabel.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;");
        TextArea descriptionArea = new TextArea();
        descriptionArea.setStyle("-fx-background-color: #f0f0f0;-fx-text-fill: #333;-fx-font-size: 20p;-fx-border-width: 2px;-fx-border-color: #ccc;-fx-font-weight: bold;");

        // Add the labels and text fields to the dialog pane
        VBox content = new VBox(10, nameLabel, nameField, descriptionLabel, descriptionArea);
        content.setStyle("-fx-background-color: #CC99FF;-fx-padding: 10;-fx-spacing: 10;-fx-pref-width: 450px; -fx-pref-height: 250px;");
        dialog.getDialogPane().setContent(content);

        // Request focus on the name field by default
        Platform.runLater(() -> nameField.requestFocus());

        // Convert the result to a pair of Strings when the OK button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(nameField.getText(), descriptionArea.getText());
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        Optional<Pair<String, String>> result = dialog.showAndWait();

        // Process the result
        result.ifPresent(pair -> {
            System.out.println("Entered new vocab: " + pair.getKey());
            System.out.println("Entered meaning: " + pair.getValue());
            Word newWord = new Word(pair.getKey(), pair.getValue());
            SQLiteConnection sqLiteConnection2 = new SQLiteConnection();
            sqLiteConnection2.setConnection(dbName);
            try {
                if (Objects.equals(dictionarySearcher(pair.getKey(), sqLiteConnection2), "Đần")) {
                    insertWord(newWord);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Add word successfully");
                    alert.show();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Word already exits,please go to Edit if you want to modify it");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Help me ");
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    //--------------------------------Tạo edit xóa----------------------------------

    @FXML
    private void setDeleteVocab() {
        // Create the custom dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Delete Vocabulary");

        // Set the button types (OK and Cancel)
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the labels and text fields
        Label nameLabel = new Label("Vocab:");
        nameLabel.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;");
        TextField nameField = new TextField();
        nameField.setStyle("-fx-background-radius: 40;-fx-pref-width: 250px; -fx-pref-height: 35px;-fx-font-weight: bold;");

        // Add the labels and text fields to the dialog pane
        VBox content = new VBox(30, nameLabel, nameField);

        content.setStyle("-fx-background-color: #CC99FF;-fx-padding: 10;-fx-spacing: 10;-fx-pref-width: 350px; -fx-pref-height: 120px;");

        dialog.getDialogPane().setContent(content);

        // Request focus on the name field by default
        Platform.runLater(() -> nameField.requestFocus());

        // Convert the result to a string when the OK button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return nameField.getText();
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        Optional<String> result = dialog.showAndWait();
        SQLiteConnection sqLiteConnection3 = new SQLiteConnection();
        sqLiteConnection3.setConnection(dbName);
        // Process the result
        result.ifPresent(name -> {
                    System.out.println("Entered delete word: " + name);
                    try {
                        if (!Objects.equals(dictionarySearcher(name, sqLiteConnection3), "Đần")) {
                            listVocab.getItems().clear();
                            view.getEngine().loadContent("");
                            makeSoundButton.setVisible(false);
                            deleteWord(name);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Delete word successfully");
                            alert.show();

                        }
                        else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Word not found, please make sure to check your grammatical error");
                            alert.showAndWait();
                        }
                    } catch (SQLException E) {
                        throw new RuntimeException("");
                    } finally {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }

    //-----------------------------sửa từ ---------------------------

    @FXML
    private void setEditVocabVocab() {
        // Create the custom dialog
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Vocabulary");
        // Set the button types (OK and Cancel)
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the labels and text fields
        Label nameLabel = new Label("Vocabulary:");
        nameLabel.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;");
        TextField nameField = new TextField();
        nameField.setStyle("-fx-background-radius: 40;-fx-font-weight: bold;");


        Label descriptionLabel = new Label("New Meaning:");
        descriptionLabel.setStyle("-fx-font-size: 15px;-fx-font-weight: bold;");
        TextArea descriptionArea = new TextArea();
        descriptionArea.setStyle("-fx-background-color: #f0f0f0;-fx-text-fill: #333;-fx-font-size: 20p;-fx-border-width: 2px;-fx-border-color: #ccc;-fx-font-weight: bold;");

        // Add the labels and text fields to the dialog pane
        VBox content = new VBox(10, nameLabel, nameField, descriptionLabel, descriptionArea);
        content.setStyle("-fx-background-color: #CC99FF;-fx-padding: 10;-fx-spacing: 10;-fx-pref-width: 450px; -fx-pref-height: 250px;");
        dialog.getDialogPane().setContent(content);

        // Request focus on the name field by default
        Platform.runLater(() -> nameField.requestFocus());

        // Convert the result to a pair of Strings when the OK button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(nameField.getText(), descriptionArea.getText());
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        Optional<Pair<String, String>> result = dialog.showAndWait();
        SQLiteConnection sqLiteConnection4 = new SQLiteConnection();
        sqLiteConnection4.setConnection(dbName);
        // Process the result
        result.ifPresent(pair -> {
                    System.out.println("Entered vocab: " + pair.getKey());
                    System.out.println("Entered meaning: " + pair.getValue());
                    try {
                        String search = dictionarySearcher(pair.getKey(), sqLiteConnection4);
                        if (!Objects.equals(search, "Đần")) {
                            Word word = new Word(pair.getKey(), pair.getValue());
                            listVocab.getItems().clear();
                            view.getEngine().loadContent("");
                            makeSoundButton.setVisible(false);
                            editWord(word);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("Edit word successfully");
                            alert.show();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Word not found, please try again or add this word into the dictionary");
                            alert.showAndWait();
                        }
                    } catch (Exception e) {
                        e.printStackTrace(); // Handle the exception appropriately, log or show an error message
                    } finally {
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
            //-----------------Add Question into database-----------------------
    });
    }

    //------------------------------------make spelling---------------------------
    @FXML
    public Button makeSoundButton;

    @FXML
    public void makeSound() {
        String word = listVocab.getSelectionModel().getSelectedItem();
        if (!Objects.equals(word, "")) {
            DictionaryManagement dictionaryManagement = new DictionaryManagement();
            dictionaryManagement.dictionarySpelling(word);
        }
    }

    //-----------------------------translate-----------------------------------
    @FXML
    public Button translateButton;
    private final String VIETNAMESE = "vi";
    private final String ENGLISH = "en";
    private final String JAPANESE = "ja";
    private final String KOREAN = "ko";
    private final String CHINESE = "zh-CN";

    @FXML
    private TextArea sourceDoc, targetDoc;
    @FXML
    private Pane rightFlat, leftFlat;
    private String langTarget = "vi";

    @FXML
    public void translateFunction() throws Exception {
        String source = sourceDoc.getText();
        source = source.replace(". ", ",");
        targetDoc.setText(GoogleTranslateAPI.translate(source,langTarget));
    }

    @FXML
    public SplitMenuButton splitMenuButtonLeft;
    @FXML
    public RadioMenuItem englishLeft;
    public RadioMenuItem vietnameseLeft;
    public RadioMenuItem japaneseLeft;
    public RadioMenuItem koreanLeft;
    public RadioMenuItem chineseLeft;

    @FXML
    public SplitMenuButton splitMenuButtonRight;

    public RadioMenuItem englishRight;
    public RadioMenuItem vietnameseRight;
    public RadioMenuItem japaneseRight;
    public RadioMenuItem koreanRight;
    public RadioMenuItem chineseRight;


    // chuyển ngôn ngữ bên trái
    @FXML
    public void selectEnglishLeft(){
        englishLeft.setSelected(true);
        vietnameseLeft.setSelected(false);
        japaneseLeft.setSelected(false);
        koreanLeft.setSelected(false);
        chineseLeft.setSelected(false);
        splitMenuButtonLeft.setText("English");
    }
    @FXML
    public void selectVietnameseLeft(){
        englishLeft.setSelected(false);
        vietnameseLeft.setSelected(true);
        japaneseLeft.setSelected(false);
        koreanLeft.setSelected(false);
        chineseLeft.setSelected(false);
        splitMenuButtonLeft.setText("Vietnamese");

    }
    @FXML
    public void selectJapaneseLeft(){
        englishLeft.setSelected(false);
        vietnameseLeft.setSelected(false);
        japaneseLeft.setSelected(true);
        koreanLeft.setSelected(false);
        chineseLeft.setSelected(false);
        splitMenuButtonLeft.setText("Japanese");

    }
    @FXML
    public void selectKoreanLeft(){
        englishLeft.setSelected(false);
        vietnameseLeft.setSelected(false);
        japaneseLeft.setSelected(false);
        koreanLeft.setSelected(true);
        chineseLeft.setSelected(false);
        splitMenuButtonLeft.setText("Korean");

    }
    @FXML
    public void selectChineseLeft() {
        englishLeft.setSelected(false);
        vietnameseLeft.setSelected(false);
        japaneseLeft.setSelected(false);
        koreanLeft.setSelected(false);
        chineseLeft.setSelected(true);
        splitMenuButtonLeft.setText("Chinese");
    }

    // Chuyển ngoon ngữ bên phải
    @FXML
    public void selectEnglishRight(){
        englishRight.setSelected(true);
        vietnameseRight.setSelected(false);
        japaneseRight.setSelected(false);
        koreanRight.setSelected(false);
        chineseRight.setSelected(false);
        splitMenuButtonRight.setText("English");
        langTarget = ENGLISH;
    }
    @FXML
    public void selectVietnameseRight(){
        englishRight.setSelected(false);
        vietnameseRight.setSelected(true);
        japaneseRight.setSelected(false);
        koreanRight.setSelected(false);
        chineseRight.setSelected(false);
        splitMenuButtonRight.setText("Vietnamese");
        langTarget = VIETNAMESE;
    }
    @FXML
    public void selectJapaneseRight(){
        englishRight.setSelected(false);
        vietnameseRight.setSelected(false);
        japaneseRight.setSelected(true);
        koreanRight.setSelected(false);
        chineseRight.setSelected(false);
        splitMenuButtonRight.setText("Japanese");
        langTarget = JAPANESE;
    }
    @FXML
    public void selectKoreanRight(){
        englishRight.setSelected(false);
        vietnameseRight.setSelected(false);
        japaneseRight.setSelected(false);
        koreanRight.setSelected(true);
        chineseRight.setSelected(false);
        splitMenuButtonRight.setText("Korean");
        langTarget = KOREAN;
    }
    @FXML
    public void selectChineseRight(){
        englishRight.setSelected(false);
        vietnameseRight.setSelected(false);
        japaneseRight.setSelected(false);
        koreanRight.setSelected(false);
        chineseRight.setSelected(true);
        splitMenuButtonRight.setText("Chinese");
        langTarget = CHINESE;
    }

}