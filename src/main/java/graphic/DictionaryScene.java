package graphic;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class DictionaryScene {

    // hàm tạo mục hỏi logout cho nút X trên window
    public void logout(Stage stage){
        //Tạo hộp xác nhận
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Do you want to save before exiting!: ");
        if(alert.showAndWait().get() == ButtonType.OK){
            System.out.println("You successfully logged out");
            stage.close();
        }
    }
}
