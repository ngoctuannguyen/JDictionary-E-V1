package com.example.jdictionaryev1;
import graphic.DictionaryScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage stg;
    @Override
    public void start(Stage stage) throws IOException {
        stg = stage;
        Parent root = FXMLLoader.load(getClass().getResource("StartDictionary.fxml"));
        stg.setTitle("JDictionary English-Vietnamese");
        stg.getIcons().add(new Image("dictionary_icon.png"));
        Scene scene = new Scene(root,840,540);
        stg.setScene(scene);
        stg.show();

        // Dictionary Scence
        DictionaryScene dictionaryScence = new DictionaryScene();

        // Search bar on click
        searchBar();

        // tạo mục hỏi logout cho nút X trên window
        stage.setOnCloseRequest(event -> {
            event.consume();
            dictionaryScence.logout(stage);
        });
    }

    /* SearchBar */
    public void searchBar() {
        TextField englishSearchBar = new TextField();
        englishSearchBar.setId("textFieldSearch");
        englishSearchBar.setOnMouseClicked(event -> englishSearchBar.clear());
    }

    //change screen
    public void changeScreen(String fxml,int width, int height)throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        stg.setScene(new Scene(root,width ,height));
        stg.show();
    }
    public static void main(String[] args) {
        launch();
    }
}