package com.example.jdictionaryev1;

import com.almasb.fxgl.audio.Audio;
import com.sun.javafx.scene.control.skin.Utils;
import game.Olympia.Media.MediaInit;
import game.Olympia.Round.Starting;
import game.Olympia.UserInfo.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import module.SQLite.OlympiaDB;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.*;
import javax.swing.*;

import java.io.*;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class OlympiaController {
    @FXML
    Button tutorialButton;
    @FXML
    public Stage stage;
    @FXML
    public Scene scene;

    public OlympiaController() throws LineUnavailableException {
    }

    public void playAudioClip(AudioClip audioClip) {
        audioClip.play();
    }

    @FXML
    public void backToDictionary(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Game.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 840, 540);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void switchToTutorial(@NotNull ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Tutorial.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1080, 608);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToOlympiaGame(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("OlympiaStart.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1080, 608);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToSetUp(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Addq.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1080, 608);
        stage.setScene(scene);
        stage.show();
    }

    //--------------------------tạo count down -------------------
    @FXML
    public Label countdownLabel1;
    public Label countdownLabel2;
    public Label countdownLabel3;
    public Label countdownLabel4;
    public Button countdownButton;
    public Label countdownNextRound;

    @FXML
    public void Countdown(int seconds, Label labelCountDown) {
        Timer timer = new Timer();
        int de_lay = 0;

        timer.scheduleAtFixedRate(new TimerTask() {
            int remainingSeconds = seconds;
            @Override
            public void run() {
                if (remainingSeconds > 0) {
                    Platform.runLater(() -> {
                        labelCountDown.setText(String.valueOf(remainingSeconds));
                    });//                    if (remainingSeconds > 5) {
//                        System.out.println("Thời gian còn lại: " + remainingSeconds + " giây");
//                    } else if (remainingSeconds >= 0 && remainingSeconds <= 5) {
//                        // You can remove this part if not needed
//                    }
                    if (endR1) {
                        remainingSeconds = 0;
                    } else {
                        remainingSeconds--;
                    }
                } else {

                    Platform.runLater(() -> {
                        if (labelCountDown == countdownLabel1) {
                                playAudioClip(clipSU[6]);
                                Question.setText("Go to next round.");
                                showQuestion();
                                stopAudioClip(clipSU[2]);
                                playAudioClip(clipSU[6]);
                                endR1 = true;
                                counter = 0;
                                submit.setDisable(true);
                                myAnswer.setDisable(true);
                                showNextButton(nextToRound2Review);
                            labelCountDown.setText("Time's up!");
                        }else if(labelCountDown == countdownLabel2){
                            labelCountDown.setText("Bump!");
                            try {
                                submitAnswerRound2();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }else if(labelCountDown == countdownLabel3){
                            labelCountDown.setText("Bump!!!");
                            try {
                                submitAnswerRound3();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }else if(labelCountDown == countdownLabel4) {
                            labelCountDown.setText("Bump!!!");
                            try {
                                submitAnswerRound4();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else if (labelCountDown == countdownNextRound) {
                            labelCountDown.setText("Go....");
                        } else {
                            labelCountDown.setText("Start");
                        }
                    });
                    timer.cancel();
                }
            }
        }, de_lay, 1000);

    }
    //------------------------tạo delay----------------------------------
    public static void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //--------------------------set up thông số ---------------------------
    public static int counter = 0;
//    public static int correct = 0;
//    public static int wrong = 0;
    public static int Mark = 0;
    // ------------------------set up button, label, textfield, image ---------------------
    @FXML
    public Label myAnswerLabel;
    public Label myanswerIs;
    public Label correctAnswerIs;
    public Label countdownLabelReady;
    public ImageView imageReady;
    public Button readyButton;
    public Label annouceNextRound;
    public Label alertAnswer;
    @FXML
    public Label Question;
    public TextField myAnswer;
    public Label Answer;
    public Label AnswerWrong;

    public Label score;
    public Button submit;
    @FXML
    public ImageView correctImage;
    public ImageView wrongImage;

    //------------------------------------các hàm show chỉ số-------------------

    public void showQuestion() {
        Question.setVisible(true);
    }

    public void showScore(int mark) {
        score.setText(String.valueOf(mark));
        score.setVisible(true);
    }

    @FXML
    public void showImageCorrect() {
        correctImage.setVisible(true);   // hàm hiện ảnh biểu thị correct
        wrongImage.setVisible(false);
    }

    @FXML
    public void showImageWrong() {
        wrongImage.setVisible(true);    // hàm hiện ảnh biểu thị wrong
        correctImage.setVisible(false);
    }

    public void hideImageCheck() {
        wrongImage.setVisible(false);
        correctImage.setVisible(false);
    }
    public void hideImageWrong(){
        wrongImage.setVisible(false);
    }
    public void hideImageCorrect(){
        correctImage.setVisible(false);
    }

    public void showAnswer() {
        Answer.setVisible(true);
    }
    public void showAnswerWrong(){
        AnswerWrong.setVisible(true);
    }

    public void hideAnswer() {
        Answer.setVisible(false);
    }
    public void hideAnswerWrong(){
        AnswerWrong.setVisible(false);
    }

    public void showNextButton(Button nextButton) {
        nextButton.setVisible(true);
    }

    public void endSound(AudioClip[] audioClips) {
        for (AudioClip audioClip : audioClips) {
            if (audioClip != null) {
                audioClip.stop();
            }
        }
    }

    public static AudioClip[] clipSU = new AudioClip[10];

    public static int i = 0;

    public void SoundR1() {
        String[] path = {
                "src/main/resources/GameOlympia/OlympiaSound/StartUp/KĐ_bắt_đầu_O15.wav",
                "src/main/resources/GameOlympia/OlympiaSound/StartUp/KĐ_chuẩn_bị_O15.wav",
                "src/main/resources/GameOlympia/OlympiaSound/StartUp/KĐ_60s_O15.wav",
                "src/main/resources/GameOlympia/OlympiaSound/StartUp/KĐ_mở_câu_hỏi_O15.mp3.mpeg",
                "src/main/resources/GameOlympia/OlympiaSound/StartUp/KĐ_đúng_O15.wav",
                "src/main/resources/GameOlympia/OlympiaSound/StartUp/KĐ_sai_O15.wav",
                "src/main/resources/GameOlympia/OlympiaSound/StartUp/KĐ_hoàn_thành_O15.wav"
        };
        for (String path1 : path) {
            clipSU[i] = new AudioClip(new File(path1).toURI().toString());
            i++;
        }
    }

    public void stopAudioClip(AudioClip audioClip) {
        audioClip.stop();
    }

    //    //----------------------------Round 1--------------------------
    @FXML
    public Button clickToContinue;
    public Starting starting = new Starting(0);

    public OlympiaDB olympiaDB = new OlympiaDB();

    @FXML
    public void gotoRound1Review() throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScreen("round1Review.fxml", 1080, 608);
        olympiaDB.setConnectionOlympia();
        SoundR1();
        playAudioClip(clipSU[0]);
    }

    public boolean endR1 = false;

    public void loadQuestion1() throws SQLException {
        ResultSet resultSet = olympiaDB.executeStartUp(counter);

        if (counter <3) { //Question 1
//            Question.setText("1." + "What is the opposite of \"big\"?");
          if (resultSet.next()) {
              Question.setText(counter + 1 + "." + resultSet.getString("Question"));
            }
            showQuestion();
        }
        else  if (counter == 3) {
            Question.setText("Go to next round.");
            showQuestion();
            stopAudioClip(clipSU[2]);
            playAudioClip(clipSU[6]);
            endR1 = true;
            counter = 0;
            submit.setDisable(true);
            myAnswer.setDisable(true);
            showNextButton(nextToRound2Review);
        }
    }

    public boolean checkAlertAnswer(String ans) {
        return ans.isEmpty();
    }

    public boolean checkAnswerRound1(String ans) throws SQLException {
        ans = ans.toLowerCase();
        ResultSet resultSet = olympiaDB.getAnswerR1(counter);
        if (counter <3) {
            if (resultSet.next()) {
                String correctAns = resultSet.getString("Answer");
                if (checkAlertAnswer(ans)) {
                    alertAnswer.setText("Invalid answer");
                    alertAnswer.setVisible(true);
                    AnswerWrong.setText(correctAns);
                    return false;
                }
                if (ans.equals(correctAns)) {
                    Answer.setText(correctAns);
                    alertAnswer.setVisible(false);
                    return true;
                } else {
                    AnswerWrong.setText(correctAns);
                    alertAnswer.setVisible(false);
                    return false;
                }
            }
        }
        return false;
    }

    @FXML
    public void submitAnswerRound1() throws SQLException {
        String ans = myAnswer.getText().trim();
        if (checkAnswerRound1(ans)) {
            showAnswer();
            hideAnswerWrong();
            playAudioClip(clipSU[4]);
            showImageCorrect();
            Mark = starting.correctAnsPlus(Mark);
            showScore(Mark);
        } else {
            showAnswerWrong();
            hideAnswer();
            playAudioClip(clipSU[5]);
            Mark = starting.incorrectAnsMinus(Mark);
            showImageWrong();
            showScore(Mark);
        }
        myAnswer.clear();
        counter++;
        loadQuestion1();
    }

    @FXML
    public void gotoRound1() throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScreen("round1.fxml", 1080, 608);
    }

    public static boolean start = true;

    @FXML
    public void startCountDownRound1() {
        int seconds = 21;
        if (start) {
            Countdown(seconds, countdownLabel1);
            //System.out.println(seconds);
            start = false;
        }
    }

    public static boolean readyR1 = true;

    @FXML
    public void readyRound1() {
        readyButton.setDisable(true);
        countdownLabelReady.setVisible(true);
        if (readyR1) {
            Countdown(6, countdownLabelReady);
            readyR1 = false;
        }
        // Tạo một Timeline với một KeyFrame để thực hiện việc ẩn các thành phần sau khi hàm Countdown kết thúc
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(6), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imageReady.setVisible(false);
                readyButton.setVisible(false);
                countdownLabelReady.setVisible(false);
                correctImage.setVisible(false);
                wrongImage.setVisible(false);
//                nextToRound2Review.setVisible(false);
                try {
                    startRound1();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                //submit.setDisable(true);
                start = true;
            }
        }));

        //if (readyR1) {
        // Bắt đầu Timeline
        timeline.play();
        readyR1 = true;
        playAudioClip(clipSU[3]);
        playAudioClip(clipSU[1]);
    }

    @FXML
    public void startRound1() throws SQLException {
        // tạo 2 ảnh correct, wrong ở chế độ visible(false)
        //sau khi submit hiện ảnh đúng sai và hiện đáp án, chú ý khi chuyển sang câu hỏi mới phải xóa đáp án câu cũ và ảnh correct/wrong
        // tao điều kiện khi hoàn thành câu cuối cùng sẽ hiện ra chữ + nút để chuyển round
        // nên viết hết vào lớp Starting trong game.Olympia để khời chạy
        hideImageCheck();
        hideAnswer();
        startCountDownRound1();
        playAudioClip(clipSU[2]);
        showScore(Mark);
        loadQuestion1();
    }


    //----------------------------------------round 2---------------------------
    public static int countQuestionR2 = 0;
    @FXML
    public Button nextToRound2Review;
    public boolean endR2 = false;
    public static boolean readyR2 = true;

    public static AudioClip[] clipOb = new AudioClip[15];

    public void SoundR2() {
        i = 0;
        String[] path = {
                "src/main/resources/GameOlympia/OlympiaSound/Obstacle/VCNV_bắt_đầu_O15.mp3.mpeg",
                "src/main/resources/GameOlympia/OlympiaSound/Obstacle/VCNV_15s_O15.mp3.mpeg",
                "src/main/resources/GameOlympia/OlympiaSound/Obstacle/VCNV_chọn_ô_chữ_O15.mp3",
                "src/main/resources/GameOlympia/OlympiaSound/Obstacle/VCNV_mở_câu_hỏi_O15.mp3",
                "src/main/resources/GameOlympia/OlympiaSound/Obstacle/VCNV_mở_hình_ảnh_O15.mp3",
                "src/main/resources/GameOlympia/OlympiaSound/Obstacle/VCNV_mở_ô_chữ_O15.mp3.mpeg",
                "src/main/resources/GameOlympia/OlympiaSound/Obstacle/VCNV_tín_hiệu_trả_lời_O15.mp3.mpeg",
                "src/main/resources/GameOlympia/OlympiaSound/Obstacle/VCNV_ô_chữ_được_mở_O15.mp3.mpeg",
                "src/main/resources/GameOlympia/OlympiaSound/Obstacle/VCNV_đúng_chướng_ngại_vật_O15.mp3.mpeg",
                "src/main/resources/GameOlympia/OlympiaSound/Obstacle/VCNV_ẩn_số_O11.mp3",
                "src/main/resources/GameOlympia/OlympiaSound/Obstacle/KĐ_sai_O7.mp3.mpeg"
        };
        for (String path1 : path) {
            clipOb[i] = new AudioClip(new File(path1).toURI().toString());
            i++;
        }
    }

    @FXML
    public void gotoRound2Review() throws IOException, SQLException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScreen("round2Review.fxml", 1080, 608);
        endSound(clipSU);
        SoundR2();
        playAudioClip(clipOb[0]);
//        olympiaDB.executeObstacle();
    }

    @FXML
    private ImageView mainImage;
    @FXML
    public void gotoRound2() throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScreen("round2.fxml", 1080, 608);
    }

    @FXML
    public ImageView pieceImage1;
    public ImageView pieceImage2;
    public ImageView pieceImage3;
    public ImageView pieceImage4;
    public ImageView pieceImage5;

    public Button openImageButton1;
    public Button openImageButton2;
    public Button openImageButton3;
    public Button openImageButton4;
    public Button openImageButton5;

    public static int Line = -1;
    @FXML
    public AnchorPane containerCircle = new AnchorPane();
    public Circle[] circles = new Circle[40];
    public List<String>listAns;
    public List<String>listQuestionRound2;
    public Text[] textAns = new Text[40];

    public void loadQuesAndAnsRound2() throws SQLException {
        listAns = OlympiaDB.getAnswerR2();
        listQuestionRound2 = OlympiaDB.getQuestionR2();
//        ResultSet resultSet = olympiaDB.executeObstacle("Ques1");
    }//


    @FXML
    public void loadCircleAndText(){
        int j = 0;
        for(int i = 0;i<40;i++){
            if(i<10) {
                if(i<listAns.get((int)i/10).length()) {
                    circles[i] = new Circle();
                    circles[i].setRadius(13);
                    circles[i].setCenterX(30 * j);
                    circles[i].setCenterY(20);
                    circles[i].setFill(Color.web("#0099FF"));

                    textAns[i] = new Text();
                    textAns[i].setText(String.valueOf(listAns.get((int)i/10).charAt(i)));
                    textAns[i].setX(30*j-4);
                    textAns[i].setY(23);
                    textAns[i].setVisible(false);
                }else{
                    circles[i] = new Circle();
                    circles[i].setRadius(13);
                    circles[i].setCenterX(30 * j);
                    circles[i].setCenterY(20);
                    circles[i].setFill(Color.web("#0099FF"));
                    circles[i].setVisible(false);

                    textAns[i] = new Text();
                    textAns[i].setText("");
                    textAns[i].setX(30*j);
                    textAns[i].setY(23);

                }
                j++;
                if(i==9){
                    j=0;
                }
            }else if(i>=10 && i < 20){
                if((i%10)<listAns.get((int)i/10).length()){
                    circles[i] = new Circle();
                    circles[i].setRadius(13);
                    circles[i].setCenterX(30 * j);
                    circles[i].setCenterY(60);
                    circles[i].setFill(Color.web("#0099FF"));

                    textAns[i] = new Text();
                    textAns[i].setText(String.valueOf(listAns.get((int)i/10).charAt(i%10)));
                    textAns[i].setX(30*j-4);
                    textAns[i].setY(63);
                    textAns[i].setVisible(false);
                }else{
                    circles[i] = new Circle();
                    circles[i].setRadius(13);
                    circles[i].setCenterX(30 * j);
                    circles[i].setCenterY(60);
                    circles[i].setFill(Color.web("#0099FF"));
                    circles[i].setVisible(false);

                    textAns[i] = new Text();
                    textAns[i].setText("");
                    textAns[i].setX(30*j);
                    textAns[i].setY(63);
                }
                j++;
                if(i==19){
                    j=0;
                }
            }else if(i >= 20 && i<30){
                if((i%10)<listAns.get((int)i/10).length()) {
                    circles[i] = new Circle();
                    circles[i].setRadius(13);
                    circles[i].setCenterX(30 * j);
                    circles[i].setCenterY(100);
                    circles[i].setFill(Color.web("#0099FF"));

                    textAns[i] = new Text();
                    textAns[i].setText(String.valueOf(listAns.get((int)i/10).charAt(i%10)));
                    textAns[i].setX(30*j-4);
                    textAns[i].setY(103);
                    textAns[i].setVisible(false);
                }else{
                    circles[i] = new Circle();
                    circles[i].setRadius(13);
                    circles[i].setCenterX(30 * j);
                    circles[i].setCenterY(100);
                    circles[i].setFill(Color.web("#0099FF"));
                    circles[i].setVisible(false);

                    textAns[i] = new Text();
                    textAns[i].setText("");
                    textAns[i].setX(30*j);
                    textAns[i].setY(63);
                }
                j++;
                if(i==29){
                    j=0;
                }
            }else{
                if((i%10)<listAns.get((int)i/10).length()) {
                    circles[i] = new Circle();
                    circles[i].setRadius(13);
                    circles[i].setCenterX(30 * j);
                    circles[i].setCenterY(140);
                    circles[i].setFill(Color.web("#0099FF"));

                    textAns[i] = new Text();
                    textAns[i].setText(String.valueOf(listAns.get((int)i/10).charAt(i%10)));
                    textAns[i].setX(30*j-4);
                    textAns[i].setY(143);
                    textAns[i].setVisible(false);
                }else{
                    circles[i] = new Circle();
                    circles[i].setRadius(13);
                    circles[i].setCenterX(30 * j);
                    circles[i].setCenterY(140);
                    circles[i].setFill(Color.web("#0099FF"));
                    circles[i].setVisible(false);

                    textAns[i] = new Text();
                    textAns[i].setText("");
                    textAns[i].setX(30*j);
                    textAns[i].setY(63);
                }
                j++;
                if(i==39){
                    j=0;//
                }
            }
        }
        containerCircle.getChildren().addAll(circles);
        containerCircle.getChildren().addAll(textAns);
    }

    public void loadQuestionRound2(int line){
        countQuestionR2++;
        String ques = listQuestionRound2.get(line);
        String cmt = String.valueOf(line+1);
        Question.setText(cmt +"." + ques);
        showQuestion();//
    }
    public void loadQuestion5R2(){
        hideImageCheck();
        myanswerIs.setVisible(false);
        myAnswerLabel.setVisible(false);
        countQuestionR2++;
        String ques = listQuestionRound2.get(4).trim();
        Question.setText("5."+ques);
        showQuestion();
        startCountDownRound2();
    }

    public boolean checkAnswerRound2(String ans) {
        if(countQuestionR2 == 5){
            String correctAns = listAns.get(4).toLowerCase();
            if (ans.equals(correctAns)) {
                showImageCorrect();
                hideImageWrong();
                openImageButton5.setVisible(false);
                pieceImage5.setVisible(false);
                return true;
            } else {
                showImageWrong();
                hideImageCorrect();
                openImageButton5.setVisible(true);
                pieceImage5.setVisible(true);
            }
        }else {
            String correctAns = listAns.get(Line).toLowerCase();
            if (ans.equals(correctAns)) {
                showImageCorrect();
                hideImageWrong();
                if (Line == 0) {
                    openImageButton1.setVisible(false);
                    pieceImage1.setVisible(false);
                } else if (Line == 1) {
                    openImageButton2.setVisible(false);
                    pieceImage2.setVisible(false);
                } else if (Line == 2) {
                    openImageButton3.setVisible(false);
                    pieceImage3.setVisible(false);
                } else if (Line == 3) {
                    openImageButton4.setVisible(false);
                    pieceImage4.setVisible(false);
                }
                return true;
            } else {
                showImageWrong();
                hideImageCorrect();
                if (Line == 0) {
                    openImageButton1.setDisable(true);
                } else if (Line == 1) {
                    openImageButton2.setDisable(true);
                } else if (Line == 2) {
                    openImageButton3.setDisable(true);
                } else if (Line == 3) {
                    openImageButton4.setDisable(true);
                }
            }
        }
        return false;//
    }

    @FXML
    public void submitAnswerRound2() throws IOException {
        String ans = myAnswerLabel.getText().toLowerCase().trim();
        if(countQuestionR2 == 5){
            if(checkAnswerRound2(ans)){
                showImageCorrect();
                Mark += 10;
                showScore(Mark);
            }else{
                showImageWrong();
                showScore(Mark);
            }
        } else {
            if (checkAnswerRound2(ans)) {
                correctAnsRound2();
                playAudioClip(clipOb[7]);
//                Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(7), new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        showImageCorrect();
//                    }
//                }));
//                timeline1.play();
                //showImageCorrect();
                Mark += 10;
                showScore(Mark);
            } else {
                playAudioClip(clipOb[10]);
                wrongAnsRound2();
//                showImageWrong();
//                Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(7), new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        showImageWrong();
//                    }
//                }));
                showScore(Mark);//
            }
            if (countQuestionR2 == 4) {
                openImageButton5.setDisable(false);
            }
//            timeline1.play();
        }
    }

    @FXML
    public void showMyAnsRound2(){
        String myAns = myAnswer.getText().toLowerCase().trim();
        myAnswerLabel.setText(myAns);
        myAnswerLabel.setVisible(true);
        myanswerIs.setVisible(true);//
        myAnswer.clear();
    }
    @FXML
    public void startCountDownRound2() {
        int seconds = 16;
        Countdown(seconds, countdownLabel2);

        Thread countdownThread = new Thread(() -> {
            try {
                Thread.sleep(6000);
                playAudioClip(clipOb[1]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        countdownThread.start();
    }


    @FXML
    public void correctAnsRound2(){
        showTextLine(Line);
        changeColorGreen(Line);
    }
    public void wrongAnsRound2(){
        changeColorRed(Line);//
    }

    @FXML
    public synchronized void chooseLine0() throws InterruptedException {
        //xoa du lieu cau cu
        hideImageCheck();
        myAnswerLabel.setVisible(false);
        myanswerIs.setVisible(false);
        //------------
        Line = 0;
        changeColorYellow(0);
        playAudioClip(clipOb[2]);
        wait(2000);
        playAudioClip(clipOb[3]);
        loadQuestionRound2(Line);
        countdownLabel2.setText("15");
        startCountDownRound2();
    }
    public void chooseLine1(){
        //xoa du lieu cau cu
        hideImageCheck();
        myAnswerLabel.setVisible(false);
        myanswerIs.setVisible(false);
        //------------
        Line = 1;
        changeColorYellow(1);
        playAudioClip(clipOb[2]);
        playAudioClip(clipOb[3]);
        loadQuestionRound2(Line);
        countdownLabel2.setText("15");
        startCountDownRound2();//
    }
    public void chooseLine2(){
        //xoa du lieu cau cu
        hideImageCheck();
        myAnswerLabel.setVisible(false);
        myanswerIs.setVisible(false);
        //------------
        Line = 2;
        changeColorYellow(2);
        playAudioClip(clipOb[2]);
        playAudioClip(clipOb[3]);
        loadQuestionRound2(Line);
        countdownLabel2.setText("15");
        startCountDownRound2();//

    }
    public void chooseLine3(){
        //xoa du lieu cau cu
        hideImageCheck();
        myAnswerLabel.setVisible(false);
        myanswerIs.setVisible(false);
        //------------
        Line = 3;
        changeColorYellow(3);
        playAudioClip(clipOb[2]);
        playAudioClip(clipOb[3]);
        loadQuestionRound2(Line);
        countdownLabel2.setText("15");//
        startCountDownRound2();
    }


    @FXML
    public void changeColorRed(int line){
        for(int i = 10*line;i<10*line+10;i++){
            circles[i].setFill(Color.web("#FF6666"));//
        }
    }
    public void changeColorGreen(int line){
        for(int i = 10*line;i<10*line+10;i++){
            circles[i].setFill(Color.web("#00CC00"));//
        }
    }
    public void changeColorYellow(int line){
        for(int i = 10*line;i<10*line+10;i++){
            circles[i].setFill(Color.web("#FFCC33"));//
        }
    }

    @FXML
    public void showTextLine(int line){
        for(int i = line*10;i<line*10+10;i++){
            textAns[i].setVisible(true);//
        }
    }

    public void setImageR2() throws SQLException {
        ResultSet resultSet = OlympiaDB.addImageR2();
        String initialPath = "E:/";
        if (resultSet.next()) {
            Image image= new Image("file:"+ initialPath + resultSet.getString("Image"));
            mainImage.setImage(image);
        }
    }

    @FXML
    public AnchorPane anchorPaneReady;
    @FXML
    public void readyRound2(){
        readyButton.setDisable(true);
        countdownLabelReady.setVisible(true);
        if (readyR1) {
            Countdown(6, countdownLabelReady);
            readyR2 = false;//
        }
        // Tạo một Timeline với một KeyFrame để thực hiện việc ẩn các thành phần sau khi hàm Countdown kết thúc
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(7), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showScore(Mark);
                Question.setVisible(true);
                myAnswer.setDisable(false);
                submit.setDisable(false);
                Line = -1;
                openImageButton1.setDisable(false);
                openImageButton2.setDisable(false);
                openImageButton3.setDisable(false);
                openImageButton4.setDisable(false);
                try {
                    setImageR2();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    loadQuesAndAnsRound2();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                loadCircleAndText();
                startRound2();
                start = true;
            }
        }));
        // Bắt đầu Timeline
        timeline.play();//
    }

    @FXML
    public AnchorPane AnchorPaneRound2Submit;
    public Button quitRound2Submit;
    public Button Round2SubmitButton;
    public TextField myAnsRound2;

    @FXML
    public AnchorPane anchorPaneWinRound2;
    public AnchorPane anchorPaneLoseRound2;

    @FXML
    public void showAnchorPaneSubmitRound2(){
        myanswerIs.setVisible(false);
        myAnswerLabel.setVisible(false);
        submit.setDisable(true);
        myAnswer.setDisable(true);
        playAudioClip(clipOb[6]);
        AnchorPaneRound2Submit.setVisible(true);
    }

    @FXML
    public void submitRound2(){
        showFullImageQuestion();
        String ans = myAnsRound2.getText().toLowerCase().trim();
        String correctAns = listAns.get(5).toLowerCase();
        if(ans.equals(correctAns)){
            correctAnswerIs.setVisible(true);
            Answer.setText(correctAns);
            playAudioClip(clipOb[8]);
            Answer.setVisible(true);
            Mark += 50;
            showScore(Mark);
            anchorPaneWinRound2.setVisible(true);

        }else{
            correctAnswerIs.setVisible(true);
            AnswerWrong.setText(correctAns);
            AnswerWrong.setVisible(true);
            Mark -= 15;
            showScore(Mark);
            anchorPaneLoseRound2.setVisible(true);

        }
        AnchorPaneRound2Submit.setVisible(false);//
    }
    public void cancelRound2(){
        showFullImageQuestion();
        String correctAns = listAns.get(5).toLowerCase();
        AnchorPaneRound2Submit.setVisible(false);
        anchorPaneLoseRound2.setVisible(true);
        correctAnswerIs.setVisible(true);
        AnswerWrong.setText(correctAns);
        AnswerWrong.setVisible(true);
        Mark -= 15;
        showScore(Mark);//
    }
    public void showFullImageQuestion(){
        openImageButton1.setVisible(false);
        openImageButton2.setVisible(false);
        openImageButton3.setVisible(false);
        openImageButton4.setVisible(false);
        pieceImage1.setVisible(false);
        pieceImage2.setVisible(false);
        pieceImage3.setVisible(false);
        pieceImage4.setVisible(false);
        pieceImage5.setVisible(false);//
    }

    @FXML
    public void startRound2() {
        anchorPaneReady.setVisible(false);//
        playAudioClip(clipOb[9]);
    }

    //----------------------------------Round 3--------------------------

    @FXML
    public Button nextToRound3Review;

    public static AudioClip[] clipSp = new AudioClip[10];

    public void SoundR3() {
        i = 0;
        String[] path = {
                "src/main/resources/GameOlympia/OlympiaSound/SpeedUp/TT_bắt_đầu_O15.mp3.mpeg",
                "src/main/resources/GameOlympia/OlympiaSound/SpeedUp/TT_30s_O15.mp3",
                "src/main/resources/GameOlympia/OlympiaSound/SpeedUp/TT_mở_đáp_án_O15.mp3",
                "src/main/resources/GameOlympia/OlympiaSound/SpeedUp/VCNV_ô_chữ_được_mở_O15.mp3.mpeg",
                "src/main/resources/GameOlympia/OlympiaSound/SpeedUp/KĐ_sai_O7.mp3.mpeg",
                "src/main/resources/GameOlympia/OlympiaSound/SpeedUp/TT_mở_câu_hỏi_O15.mp3.mpeg"
        };// TT is not finished.

        for (String path1 : path) {
            clipSp[i] = new AudioClip(new File(path1).toURI().toString());
            i++;
        }
    }


    @FXML
    public void gotoRound3Review() throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScreen("round3Review.fxml", 1080, 608);
        endSound(clipOb);
        SoundR3();
        playAudioClip(clipSp[0]);
    }

    public boolean endR3 = false;
    //Tạo và show các hình ảnh câu hỏi
    @FXML
    public ImageView imageViewQuestion;
    private List<List<Image>> listImages = new ArrayList<>();
    private int currentIndex;
    Timeline timeline;

    // Need to modify
    @FXML
    private void loadImageQuestion() {
        // Tạo list 3 ảnh 1
        // List 1
        //delay(5000);
        List<Image> images1 = new ArrayList<>();
        images1.add(new Image(Objects.requireNonNull(getClass().getResource("/GameOlympia/QuestionRound3/question1/1.png")).toExternalForm()));
        images1.add(new Image(Objects.requireNonNull(getClass().getResource("/GameOlympia/QuestionRound3/question1/2.png")).toExternalForm()));
        images1.add(new Image(Objects.requireNonNull(getClass().getResource("/GameOlympia/QuestionRound3/question1/3.png")).toExternalForm()));
        images1.add(new Image(Objects.requireNonNull(getClass().getResource("/GameOlympia/QuestionRound3/question1/4.png")).toExternalForm()));

        // List 2
        List<Image> images2 = new ArrayList<>();
        images2.add(new Image(Objects.requireNonNull(getClass().getResource("/GameOlympia/QuestionRound3/question2/1.png")).toExternalForm()));
        images2.add(new Image(Objects.requireNonNull(getClass().getResource("/GameOlympia/QuestionRound3/question2/2.png")).toExternalForm()));
        images2.add(new Image(Objects.requireNonNull(getClass().getResource("/GameOlympia/QuestionRound3/question2/3.png")).toExternalForm()));
        images2.add(new Image(Objects.requireNonNull(getClass().getResource("/GameOlympia/QuestionRound3/question2/4.png")).toExternalForm()));
        // List 3
        List<Image> images3 = new ArrayList<>();
        images3.add(new Image(Objects.requireNonNull(getClass().getResource("/GameOlympia/QuestionRound3/question3/1.png")).toExternalForm()));
        images3.add(new Image(Objects.requireNonNull(getClass().getResource("/GameOlympia/QuestionRound3/question3/2.png")).toExternalForm()));
        images3.add(new Image(Objects.requireNonNull(getClass().getResource("/GameOlympia/QuestionRound3/question3/3.png")).toExternalForm()));
        images3.add(new Image(Objects.requireNonNull(getClass().getResource("/GameOlympia/QuestionRound3/question3/4.png")).toExternalForm()));

        //Thêm các list vào 1 list chung
        listImages.add(images1);
        listImages.add(images2);
        listImages.add(images3);

        // Initialize currentIndex
        currentIndex = 0;

        // Display the first image
        imageViewQuestion.setImage(listImages.get(counter).get(currentIndex));
        // Set up a Timeline to switch images every 5 seconds
        timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(30 / listImages.size()),
                        this::nextImage
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        //timeline.setDelay(Duration.millis(6000));
        timeline.play();
    }

    @FXML
    public void nextQuestionImage() {
        // check counter == 3 thì show nút next to round 4
        if (counter == 3) {
            // chuyển màn
            counter = 0;
            endR3 = true;
            submit.setDisable(true);
            myAnswer.setDisable(true);
            showNextButton(nextToRound4Review);
            imageViewQuestion.setVisible(false);
            return;
        }
        playAudioClip(clipSp[5]);
        currentIndex = 0;
        // Display the first image
        imageViewQuestion.setImage(listImages.get(counter).get(currentIndex));

//        System.out.println(listImages.size());
        startCountDownRound3();
        // Set up a Timeline to switch images every 5 seconds
        timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(6),
                        this::nextImage
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    public void nextImage(ActionEvent event) {
        currentIndex = (currentIndex + 1) % listImages.get(counter).size();
        imageViewQuestion.setImage(listImages.get(counter).get(currentIndex));

        // Kiểm tra nếu đã đến hình ảnh cuối cùng, thì dừng Timeline
        if (currentIndex == listImages.get(counter).size() - 1) {
            // khi đến ảnh cuối cùng sẽ bắt đầu đếm thời gian
            //startCountDownRound3();
            timeline.stop();
        }
    }

    // Hard-code
    public boolean checkAnswerRound3(String ans) {
        ans = ans.toLowerCase();
        if (counter == 0) {
            if (checkAlertAnswer(ans)) {
                alertAnswer.setText("Invalid answer");
                alertAnswer.setVisible(true);
                AnswerWrong.setText("whale");
                playAudioClip(clipSp[4]);
                return false;
            }
            if (ans.equals("whale")) {
                Answer.setText("whale");
                playAudioClip(clipSp[3]);
                alertAnswer.setVisible(false);
                return true;
            } else {
                AnswerWrong.setText("whale");
                alertAnswer.setVisible(false);
                playAudioClip(clipSp[4]);
                return false;
            }
        }
        if (counter == 1) {
            if (checkAlertAnswer(ans)) {
                alertAnswer.setText("Invalid answer");
                alertAnswer.setVisible(true);
                AnswerWrong.setText("football / soccer");
                return false;
            }
            if (ans.equals("football") || ans.equals("soccer")) {
                Answer.setText("football / soccer");
                alertAnswer.setVisible(false);
                return true;
            } else {
                AnswerWrong.setText("football / soccer");
                alertAnswer.setVisible(false);
                return false;
            }
        }
        if (counter == 2) {
            if (checkAlertAnswer(ans)) {
                alertAnswer.setText("Invalid answer");
                alertAnswer.setVisible(true);
                AnswerWrong.setText("hospital");
                return false;
            }
            if (ans.equals("hospital")) {
                Answer.setText("hospital");
                alertAnswer.setVisible(false);
                return true;
            } else {
                AnswerWrong.setText("hospital");
                alertAnswer.setVisible(false);
                return false;
            }
        }
        return false;
    }
    @FXML
    public void showMyAnsRound3(){
        hideImageCheck();
        alertAnswer.setVisible(false);
        correctAnswerIs.setVisible(false);
        Answer.setVisible(false);
        AnswerWrong.setVisible(false);
        myanswerIs.setVisible(true);
        String ans = myAnswer.getText();
        myAnswerLabel.setText(ans);
        myAnswerLabel.setVisible(true);
        myAnswer.clear();
    }

    @FXML
    public void submitAnswerRound3() throws IOException {
        myAnswerLabel.setVisible(false);
        myanswerIs.setVisible(false);
        String ans = myAnswerLabel.getText().trim();
        if (checkAnswerRound3(ans)) {
            showAnswer();
            correctAnswerIs.setVisible(true);
            showImageCorrect();
            Mark += 20;
            showScore(Mark);
        } else {
            showAnswerWrong();
            correctAnswerIs.setVisible(true);
            Mark -= 5;
            showImageWrong();
            showScore(Mark);
        }
        timeline.stop();
        myAnswer.clear();
        counter++;
        countdownLabel3.setText("30");
//        delay(2000);
//        correctAnswerIs.setVisible(false);
//        Answer.setVisible(false);
        nextQuestionImage();
    }

//    public static boolean start = true;

    @FXML
    public void startCountDownRound3() {
        int seconds = 31;
        playAudioClip(clipSp[1]);
//        if (start) {
//            Countdown(seconds, countdownLabel3);
//            //System.out.println(seconds);
//            start = false;
//        }
        Countdown(seconds,countdownLabel3);
    }

    @FXML
    public void gotoRound3(ActionEvent event) throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScreen("round3.fxml", 1080, 608);
    }

    public static boolean readyR3 = true;

    @FXML
    public void readyRound3() {
        readyButton.setDisable(true);
        countdownLabelReady.setVisible(true);
        if (readyR3) {
            Countdown(6, countdownLabelReady);
            readyR3 = false;
        }
        // Tạo một Timeline với một KeyFrame để thực hiện việc ẩn các thành phần sau khi hàm Countdown kết thúc
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(7), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imageReady.setVisible(false);
                readyButton.setVisible(false);
                countdownLabelReady.setVisible(false);
                correctImage.setVisible(false);
                wrongImage.setVisible(false);
                startRound3();
                start = true;
            }
        }));

        // Bắt đầu Timeline
        timeline.play();
        readyR3 = true;
    }

    @FXML
    public void startRound3() {
        myAnswer.setDisable(false);
        submit.setDisable(false);
        countdownLabel3.setVisible(true);
        hideImageCheck();
        hideAnswer();
        showScore(Mark);
        loadImageQuestion();
        startCountDownRound3();
    }

    //----------------------------------Round 4--------------------------
    @FXML
    public Button nextToRound4Review;

    public Label QSscore;

    public static AudioClip[] clipF = new AudioClip[15];

    public void SoundR4() {
        i = 0;
        String[] path = {
               "src/main/resources/GameOlympia/OlympiaSound/ReachToFinishLine/VĐ_bắt_đầu_O15.mp3.mpeg",
                "src/main/resources/GameOlympia/OlympiaSound/ReachToFinishLine/VĐ_chọn_gói_điểm_O11.wav",
                "src/main/resources/GameOlympia/OlympiaSound/ReachToFinishLine/VĐ_lên_sân_khấu_O15.mp3.mpeg",
                "src/main/resources/GameOlympia/OlympiaSound/ReachToFinishLine/VĐ_ngôi_sao_O15.mp3.mpeg",
                "src/main/resources/GameOlympia/OlympiaSound/ReachToFinishLine/VĐ_sai_O21.mp3.mpeg",
                "src/main/resources/GameOlympia/OlympiaSound/ReachToFinishLine/VĐ_đúng_O15.mp3.mpeg",
                "src/main/resources/GameOlympia/OlympiaSound/ReachToFinishLine/VĐ_10s_O15_1.mp3",
                "src/main/resources/GameOlympia/OlympiaSound/ReachToFinishLine/VĐ_15s_O15_1.mp3",
                "src/main/resources/GameOlympia/OlympiaSound/ReachToFinishLine/VĐ_20s_O15.mp3",
                "src/main/resources/GameOlympia/OlympiaSound/ReachToFinishLine/TT_mở_đáp_án_thí_sinh_O14.mp3",
                "src/main/resources/GameOlympia/OlympiaSound/ReachToFinishLine/VĐ_mở_câu_hỏi_O15.mp3.mpeg",
                "src/main/resources/GameOlympia/OlympiaSound/ReachToFinishLine/VĐ_về_vị_trí_O15.mp3.mpeg"
        }; // wrong and correct

        for (String path1 : path) {
            clipF[i] = new AudioClip(new File(path1).toURI().toString());
            i++;
        }
    }


    @FXML
    public void gotoRound4Review() throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScreen("round4Review.fxml", 1080, 608);
        stopAudioClip(clipSU[6]);
        endSound(clipSp);
        SoundR4();
        playAudioClip(clipF[0]);
    }

    @FXML
    public void gotoPackageQuestion() throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScreen("packageQuestionRound4.fxml", 1080, 608);
    }

    @FXML
    private CheckBox checkBox1;

    @FXML
    private CheckBox checkBox2;

    @FXML
    private CheckBox checkBox3;

    @FXML
    private CheckBox checkBox4;

    @FXML
    private CheckBox checkBox5;

    @FXML
    private CheckBox checkBox6;

    @FXML
    private CheckBox checkBox7;

    @FXML
    private CheckBox checkBox8;

    @FXML
    private CheckBox checkBox9;

    @FXML
    private Button playRound4;

    public static int counterR4 = 0;

    public static List<String> listQuestion = new ArrayList<>() ;
    public CheckBox[] checkBoxes = new CheckBox[11];
    @FXML
    public void loadCheckBox() {
        counterR4 = 0;
        playAudioClip(clipF[1]);
//        listQuestion = new ArrayList<>();
        // checkbox
        checkBoxes[1] = checkBox1;
        checkBoxes[2] = checkBox2;
        checkBoxes[3] = checkBox3;
        checkBoxes[4] = checkBox4;
        checkBoxes[5] = checkBox5;
        checkBoxes[6] = checkBox6;
        checkBoxes[7] = checkBox7;
        checkBoxes[8] = checkBox8;
        checkBoxes[9] = checkBox9;
        //counterR4();
        checkBox1.setOnAction(event -> handleCheckBoxAction(checkBox1));
        checkBox2.setOnAction(event -> handleCheckBoxAction(checkBox2));
        checkBox3.setOnAction(event -> handleCheckBoxAction(checkBox3));
        checkBox4.setOnAction(event -> handleCheckBoxAction(checkBox4));
        checkBox5.setOnAction(event -> handleCheckBoxAction(checkBox5));
        checkBox6.setOnAction(event -> handleCheckBoxAction(checkBox6));
        checkBox7.setOnAction(event -> handleCheckBoxAction(checkBox7));
        checkBox8.setOnAction(event -> handleCheckBoxAction(checkBox8));
        checkBox9.setOnAction(event -> handleCheckBoxAction(checkBox9));
        counterR4();
        playRound4.setOnAction(event -> {
            try {
                playAudioClip(clipF[10]);
                gotoRound4();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void handleCheckBoxAction(CheckBox checkBox) {
        if (checkBox.isSelected()) {
            playAudioClip(clipF[9]);
            // Nếu ô checkbox được chọn, thêm giá trị của ô đó vào danh sách
            listQuestion.add(checkBox.getText());
            setSwitchOnOffOtherBox(checkBox, true);
            if (listQuestion.size() == 3) {
                checkBox1.setDisable(true);
                checkBox2.setDisable(true);
                checkBox3.setDisable(true);
                checkBox4.setDisable(true);
                checkBox5.setDisable(true);
                checkBox6.setDisable(true);
                checkBox7.setDisable(true);
                checkBox8.setDisable(true);
                checkBox9.setDisable(true);
                playRound4.setVisible(true);
            } else {
                counterR4();
            }
        }
    }

    public void counterR4() {
        counterR4++;
        if (counterR4 == 3) {
            setSwitchOnOffOtherBox(checkBox3, false);
            checkBox3.setDisable(false);
        } else {
            for (int i = 1; i <= 9; ++i) {
                setSwitchOnOffOtherBox(checkBoxes[i], i % 3 != counterR4);
            }
        }
    }

    private int getIndex(CheckBox checkBox) {
        if (checkBox.equals(checkBox1)) {
            return 1;
        } else if (checkBox.equals(checkBox2)) {
            return 2;
        } else if (checkBox.equals(checkBox3)) {
            return 3;
        } else if (checkBox.equals(checkBox4)) {
            return 4;
        } else if (checkBox.equals(checkBox5)) {
            return 5;
        } else if (checkBox.equals(checkBox6)) {
            return 6;
        } else if (checkBox.equals(checkBox7)) {
            return 7;
        } else if (checkBox.equals(checkBox8)) {
            return 8;
        } else {
            return 9;
        }
    }

    public void setSwitchOnOffOtherBox(CheckBox checkBox, boolean state) {

        int i = getIndex(checkBox);
        checkBoxes[i].setDisable(state);
        if (i - 3 > 0) {
            checkBoxes[i - 3].setDisable(state);
        }
        if (i - 6 > 0) {
            checkBoxes[i - 6].setDisable(state);
        }
        if (i + 3 <= 9) {
            checkBoxes[i + 3].setDisable(state);
        }
        if (i + 6 <= 9) {
            checkBoxes[i + 6].setDisable(state);
        }
    }

    public static boolean readyR4 = true;

    @FXML
    public void readyRound4() {
        readyButton.setDisable(true);
        countdownLabelReady.setVisible(true);
        if (readyR4) {
            Countdown(6, countdownLabelReady);
            readyR4 = false;
        }
        playAudioClip(clipF[2]);
        // Tạo một Timeline với một KeyFrame để thực hiện việc ẩn các thành phần sau khi hàm Countdown kết thúc
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(7), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                imageReady.setVisible(false);
                readyButton.setVisible(false);
                countdownLabelReady.setVisible(false);
                loadCheckBox();
            }
        }));
        // Bắt đầu Timeline
        timeline.play();
        readyR4 = true;
    }

    @FXML
    public void gotoRound4() throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScreen("round4.fxml", 1080, 608);
    }

    public boolean endR4 = false;

    List<String> package10 ;
    List<String> ansPackage10;
    List<String> package20 ;
    List<String> ansPackage20;
    List<String> package30 ;
    List<String> ansPackage30;

    public void loadPackageQuesion() {
        System.out.println("counter: " + counter);
        System.out.println("go to loadPackageQuestion");

        package10 = new ArrayList<>();
        package20 = new ArrayList<>();
        package30 = new ArrayList<>();
        ansPackage10 = new ArrayList<>();
        ansPackage20 = new ArrayList<>();
        ansPackage30 = new ArrayList<>();

        //cau hoi package 10
        package10.add("What is the opposite of \"fast\"");
        ansPackage10.add("Slow");
        package10.add("Provide a synonym for the word \"happy.\"");
        ansPackage10.add("Joyful");
        package10.add("What is the opposite of \"dark\"");
        ansPackage10.add("Light");

        //cau hoi package 20
        package20.add(" Identify the antonym for the word \"expand.\"");
        ansPackage20.add("Contract");
        package20.add(" Identify the antonym for the word \"generous.\"");
        ansPackage20.add("Stingy");
        package20.add("Give a one-word synonym for \"enthusiastic.\"");
        ansPackage20.add("Eager");
        //cau hoi package 30
        package30.add("Provide a word that means \"the ability to speak or write several languages.\"");
        ansPackage30.add("Multilingualism");
        package30.add("Provide a word that means \"the simultaneous occurrence of events that appear significantly related but have no discernible causal connection.\"");
        ansPackage30.add("Synchronicity");
        package30.add("What is the term for the study of the origins and historical development of words?");
        ansPackage30.add("Etymology");
    }

    public int idQuestion10 = -1;
    public int idQuestion20 = -1;
    public int idQuestion30 = -1;

    public int randomQuestion(List mylist) {
        if (mylist == null || mylist.isEmpty()) {
            throw new IllegalArgumentException("List is null or empty");
        }
        Random random = new Random();
        int index = random.nextInt(mylist.size());
        return index;

    }

    public void loadQuestion4() {
//        System.out.println("go to load question");
//        alertAnswer.setVisible(false);
        System.out.println(counter);
        if (counter == 3) {
            Question.setText("Congratulate!");
            showQuestion();
            endR4 = true;
            counter = 0;
            submit.setDisable(true);
            myAnswer.setDisable(true);
            Answer.setDisable(true);
            countdownLabel4.setVisible(false);
            //Question.setVisible(false);
            starHopeButton.setDisable(true);
            showNextButton(resultButton);
            //endSound(clipF);
            playAudioClip(clipF[11]);
            // hiện nút endgame dẫn ra bảng kết quả
        } else {
            if (listQuestion.get(counter).equals("10")) {
                countdownLabel4.setText("10");
            } else if (listQuestion.get(counter).equals("20")) {
                countdownLabel4.setText("15");
            } else {
                countdownLabel4.setText("20");
            }
            if (counter == 0) { //Question 1

                if (listQuestion.get(counter).equals("10")) {
                    if (idQuestion10 == -1) {
                        idQuestion10 = randomQuestion(package10);
                    } else {
                        int oldID = idQuestion10;
                        idQuestion10 = randomQuestion(package10);
                        while (idQuestion10 == oldID) {
                            idQuestion10 = randomQuestion(package10);
                        }
                    }
                    QSscore.setText("10");
                    String ques = package10.get(idQuestion10);
                    Question.setText("1." + ques);
                    showQuestion();
                } else if (listQuestion.get(counter).equals("20")) {
                    if (idQuestion20 == -1) {
                        idQuestion20 = randomQuestion(package20);
                    } else {
                        int oldID = idQuestion20;
                        idQuestion20 = randomQuestion(package20);
                        while (idQuestion20 == oldID) {
                            idQuestion20 = randomQuestion(package20);
                        }
                    }
                    QSscore.setText("20");
                    String ques = package20.get(idQuestion20);
                    Question.setText("1." + ques);
                    showQuestion();
                } else if (listQuestion.get(counter).equals("30")) {
                    if (idQuestion30 == -1) {
                        idQuestion30 = randomQuestion(package30);
                    } else {
                        int oldID = idQuestion30;
                        idQuestion30 = randomQuestion(package30);
                        while (idQuestion30 == oldID) {
                            idQuestion30 = randomQuestion(package30);
                        }
                    }
                    QSscore.setText("30");
                    String ques = package30.get(idQuestion30);
                    Question.setText("1." + ques);
                    showQuestion();
                }
                startCountDownRound4();
            }
            if (counter == 1) {
                if (listQuestion.get(counter).equals("10")) {
                    if (idQuestion10 == -1) {
                        idQuestion10 = randomQuestion(package10);
                    } else {
                        int oldID = idQuestion10;
                        idQuestion10 = randomQuestion(package10);
                        while (idQuestion10 == oldID) {
                            idQuestion10 = randomQuestion(package10);
                        }
                    }
                    QSscore.setText("10");
                    String ques = package10.get(idQuestion10);
                    Question.setText("2." + ques);
                    showQuestion();
                } else if (listQuestion.get(counter).equals("20")) {
                    if (idQuestion20 == -1) {
                        idQuestion20 = randomQuestion(package20);
                    } else {
                        int oldID = idQuestion20;
                        idQuestion20 = randomQuestion(package20);
                        while (idQuestion20 == oldID) {
                            idQuestion20 = randomQuestion(package20);
                        }
                    }
                    QSscore.setText("20");
                    String ques = package20.get(idQuestion20);
                    Question.setText("2." + ques);
                    showQuestion();
                } else if (listQuestion.get(counter).equals("30")) {
                    if (idQuestion30 == -1) {
                        idQuestion30 = randomQuestion(package30);
                    } else {
                        int oldID = idQuestion30;
                        idQuestion30 = randomQuestion(package30);
                        while (idQuestion30 == oldID) {
                            idQuestion30 = randomQuestion(package30);
                        }
                    }
                    QSscore.setText("30");
                    String ques = package30.get(idQuestion30);
                    Question.setText("2." + ques);
                    showQuestion();
                }
            }
            if (counter == 2) {
                if (listQuestion.get(counter).equals("10")) {
                    if (idQuestion10 == -1) {
                        idQuestion10 = randomQuestion(package10);
                    } else {
                        int oldID = idQuestion10;
                        idQuestion10 = randomQuestion(package10);
                        while (idQuestion10 == oldID) {
                            idQuestion10 = randomQuestion(package10);
                        }
                    }
                    QSscore.setText("10");
                    String ques = package10.get(idQuestion10);
                    Question.setText("3." + ques);
                    showQuestion();
                } else if (listQuestion.get(counter).equals("20")) {
                    if (idQuestion20 == -1) {
                        idQuestion20 = randomQuestion(package20);
                    } else {
                        int oldID = idQuestion20;
                        idQuestion20 = randomQuestion(package20);
                        while (idQuestion20 == oldID) {
                            idQuestion20 = randomQuestion(package20);
                        }
                    }
                    QSscore.setText("20");
                    String ques = package20.get(idQuestion20);
                    Question.setText("3." + ques);
                    showQuestion();
                } else if (listQuestion.get(counter).equals("30")) {
                    if (idQuestion30 == -1) {
                        idQuestion30 = randomQuestion(package30);
                    } else {
                        int oldID = idQuestion30;
                        idQuestion30 = randomQuestion(package30);
                        while (idQuestion30 == oldID) {
                            idQuestion30 = randomQuestion(package30);
                        }
                    }
                    QSscore.setText("30");
                    String ques = package30.get(idQuestion30);
                    Question.setText("3." + ques);
                    showQuestion();
                }
            }
        }
    }


    public boolean checkAnswerRound4(String ans) {
        ans = ans.toLowerCase();
        if (counter == 0) {
            if (checkAlertAnswer(ans)) {
                alertAnswer.setText("Invalid answer");
                alertAnswer.setVisible(true);
                return false;
            }
            alertAnswer.setVisible(false);
            if (listQuestion.get(counter).equals("10")) {
                String ansCorrect = ansPackage10.get(idQuestion10).toLowerCase();
                if (ans.equals(ansCorrect)) {
                    Answer.setText(ansCorrect);
                    return true;
                } else {
                    AnswerWrong.setText(ansCorrect);
                    return false;
                }
            } else if (listQuestion.get(counter).equals("20")) {
                String ansCorrect = ansPackage20.get(idQuestion20).toLowerCase();
                if (ans.equals(ansCorrect)) {
                    Answer.setText(ansCorrect);
                    return true;
                } else {
                    AnswerWrong.setText(ansCorrect);
                    return false;
                }
            } else if (listQuestion.get(counter).equals("30")) {
                String ansCorrect = ansPackage30.get(idQuestion30).toLowerCase();
                if (ans.equals(ansCorrect)) {
                    Answer.setText(ansCorrect);
                    return true;
                } else {
                    AnswerWrong.setText(ansCorrect);
                    return false;
                }
            }

        }
        if (counter == 1) {
            if (checkAlertAnswer(ans)) {
                alertAnswer.setText("Invalid answer");
                alertAnswer.setVisible(true);
                return false;
            }
            alertAnswer.setVisible(false);
            if (listQuestion.get(counter).equals("10")) {
                String ansCorrect = ansPackage10.get(idQuestion10).toLowerCase();
                if (ans.equals(ansCorrect)) {
                    Answer.setText(ansCorrect);
                    return true;
                } else {
                    AnswerWrong.setText(ansCorrect);
                    return false;
                }
            } else if (listQuestion.get(counter).equals("20")) {
                String ansCorrect = ansPackage20.get(idQuestion20).toLowerCase();
                if (ans.equals(ansCorrect)) {
                    Answer.setText(ansCorrect);
                    return true;
                } else {
                    AnswerWrong.setText(ansCorrect);
                    return false;
                }
            } else if (listQuestion.get(counter).equals("30")) {
                String ansCorrect = ansPackage30.get(idQuestion30).toLowerCase();
                if (ans.equals(ansCorrect)) {
                    Answer.setText(ansCorrect);
                    return true;
                } else {
                    AnswerWrong.setText(ansCorrect);
                    return false;
                }
            }
        }
        if (counter == 2) {
            if (checkAlertAnswer(ans)) {
                alertAnswer.setText("Invalid answer");
                alertAnswer.setVisible(true);
                return false;
            }
            alertAnswer.setVisible(false);
            if (listQuestion.get(counter).equals("10")) {
                String ansCorrect = ansPackage10.get(idQuestion10).toLowerCase();
                if (ans.equals(ansCorrect)) {
                    Answer.setText(ansCorrect);
                    return true;
                } else {
                    AnswerWrong.setText(ansCorrect);
                    return false;
                }
            } else if (listQuestion.get(counter).equals("20")) {
                String ansCorrect = ansPackage20.get(idQuestion20).toLowerCase();
                if (ans.equals(ansCorrect)) {
                    Answer.setText(ansCorrect);
                    return true;
                } else {
                    AnswerWrong.setText(ansCorrect);
                    return false;
                }
            } else if (listQuestion.get(counter).equals("30")) {
                String ansCorrect = ansPackage30.get(idQuestion30).toLowerCase();
                if (ans.equals(ansCorrect)) {
                    Answer.setText(ansCorrect);
                    return true;
                } else {
                    AnswerWrong.setText(ansCorrect);
                    return false;
                }
            }
        }
        return false;
    }
    @FXML
    public void showMyAnsRound4(){
        hideImageCheck();
        alertAnswer.setVisible(false);
        correctAnswerIs.setVisible(false);
        Answer.setVisible(false);
        AnswerWrong.setVisible(false);
        myanswerIs.setVisible(true);
        String ans = myAnswer.getText();
        myAnswerLabel.setText(ans);
        myAnswerLabel.setVisible(true);
        myAnswer.clear();
    }
    public boolean checkStar = false;
    @FXML
    public void submitAnswerRound4() throws IOException {
        myAnswerLabel.setVisible(false);
        myanswerIs.setVisible(false);
        starHopeImage.setVisible(false);
        String ans = myAnswerLabel.getText().trim();
        if (checkAnswerRound4(ans)) {
            showAnswer();
            playAudioClip(clipF[5]);
            correctAnswerIs.setVisible(true);
            hideAnswerWrong();
            showImageCorrect();
            hideImageWrong();
            int add = 0;
            if(listQuestion.get(counter).equals("10")){
                if(checkStar){
                    add = 20;
                    checkStar = false;
                }else{
                    add = 10;
                }
            }else if(listQuestion.get(counter).equals("20")){
                if(checkStar){
                    add = 40;
                    checkStar = false;
                }else{
                    add = 20;
                }
            }else if(listQuestion.get(counter).equals("30")){
                if(checkStar){
                    add = 60;
                    checkStar = false;
                }else{
                    add = 30;
                }
            }
            Mark += add;
            showScore(Mark);
        } else {
            playAudioClip(clipF[4]);
            correctAnswerIs.setVisible(true);
            showAnswerWrong();
            hideAnswer();
            showImageWrong();
            hideImageCorrect();
            int subtract = 0;
            if(listQuestion.get(counter).equals("10")){
                if(checkStar){
                    subtract = 10;
                    checkStar = false;
                }
            }else if(listQuestion.get(counter).equals("20")){
                if(checkStar){
                    subtract = 20;
                    checkStar = false;
                }
            }else if(listQuestion.get(counter).equals("30")){
                if(checkStar){
                    subtract = 30;
                    checkStar = false;
                }
            }
            Mark -= subtract;
            showScore(Mark);
        }
        myAnswer.clear();
        counter++;
        loadQuestion4();
        if (!checkStar) {
            starHopeButton.setDisable(false);
        }
        //delay(5000);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startCountDownRound4();
                if (!checkStar) {
                    starHopeButton.setDisable(true);
                }
            }
        }));

        if (!endR4) {
            timeline.play();
        }
    }

    @FXML
    public Button startRound4Button;
    public Button starHopeButton;
    public ImageView starHopeImage;
    public Label labelPackage1;
    public Label labelPackage2;
    public Label labelPackage3;

    public void startCountDownRound4(int time) {
        Countdown(time,countdownLabel4);
    }

    @FXML
    public void starHopeFunction(){
        playAudioClip(clipF[3]);
        starHopeImage.setVisible(true);
        checkStar = true;
        starHopeButton.setDisable(true);
    }
    @FXML
    public void startRound4() {
        // show các câu hỏi đã lựa chọn vào ô tròn
        labelPackage1.setVisible(true);
        labelPackage2.setVisible(true);
        labelPackage3.setVisible(true);
        labelPackage1.setText(listQuestion.get(0));
        labelPackage2.setText(listQuestion.get(1));
        labelPackage3.setText(listQuestion.get(2));
        // load các hàm nạp dữ liệu câu hỏi và các nút
        loadPackageQuesion();
        startRound4Button.setVisible(false);
        Question.setVisible(true);
        submit.setDisable(false);
        myAnswer.setDisable(false);
        countdownLabel4.setVisible(true);
        showScore(Mark);
        // hiện nút ngôi sao hy vọng
        starHopeButton.setVisible(true);
        //playAudioClip(clipF[10]);
        //count down
//        startCountDownRound4(16);
        // load câu hỏi ra màn hình
//        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                startCountDownRound4();
//            }
//        }));
        loadQuestion4();
//        timeline.play();
    }

    //--------------------------------------bảng điểm-------------
    @FXML
    public Button resultButton;

    @FXML
    public void gotoResult() throws IOException {
        HelloApplication helloApplication = new HelloApplication();
        helloApplication.changeScreen("resultOlympia.fxml", 1080, 608);
        playAudioClip(new AudioClip(new File("src/main/resources/GameOlympia/OlympiaSound/FinalResult/Trao_giải_thưởng_O15.mp3").
                toURI().toString()));
    }

    @FXML
    public void startCountDownRound4() {
        if (listQuestion.get(counter).equals("10")) {
            Countdown(11, countdownLabel4);
            playAudioClip(clipF[6]);
        } else if (listQuestion.get(counter).equals("20")) {
            Countdown(16, countdownLabel4);
            playAudioClip(clipF[7]);
        } else {
            Countdown(21, countdownLabel4);
            playAudioClip(clipF[8]);
        }
    }

    //------------------------- tạo cảnh báo thoát game ---------------------
    @FXML
    public Button endGameButton;

    @FXML
    public void endGame(ActionEvent event) throws IOException {
        //Tạo hộp xác nhận
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("End Game");
        alert.setHeaderText("You're about to end game!");
        alert.setContentText("If you out game, you'll lose this Game. Click CANCEL to continue!");
        //System.out.println("im here");
        if (alert.showAndWait().get() == ButtonType.OK) {
            HelloApplication helloApplication = new HelloApplication();
            helloApplication.changeScreen("OlympiaStart.fxml", 1080, 608);
            endSound(clipSU);
            endSound(clipOb);
            endSound(clipSp);
            endSound(clipF);
            playAudioClip(new AudioClip(new File("src/main/resources/GameOlympia/OlympiaSound/Giới_thiệu_cuộc_thi_O15.mp3.mpeg").toURI().toString()));
        }
    }
 }
