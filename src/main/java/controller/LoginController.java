package controller;

import com.example.jdictionaryev1.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;

public class LoginController {

    public Scene scene;

    public LoginController() {
    }
    @FXML
    private Button login;
    @FXML
    private Label wrongLogin;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML

    public void userLogin(ActionEvent event)throws IOException{
        checkLogin();
    }
    public void checkLogin() throws IOException{
        HelloApplication helloApplication = new HelloApplication();


        //Kiểm tra tài khoản có đúng không    //tk: admin,  mk: admin
        if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){

            wrongLogin.setText("Success");
            //chuyển sang màn hình dictionary
            helloApplication.changeScreen("Dictionary.fxml",840,540);

        }else if(username.getText().isEmpty() && password.getText().isEmpty() )
        {
            wrongLogin.setText("Please enter your username or password");
        }else {
            wrongLogin.setText("Wrong username or password");
        }
    }
}
