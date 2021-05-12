package controller;

import domain.Account;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.IService;
import service.ServiceException;

import java.io.IOException;
import java.util.Optional;

public class LoginMenuController {

    private IService service;

    @FXML
    TextField usernameFieldFX;
    @FXML
    PasswordField passwordFieldFX;

    @FXML
    public void initialize() {

    }

    public void setParameters(IService service) {
        this.service = service;
    }

    public void loginButtonReleased(MouseEvent mouseEvent) throws ServiceException {
        Optional<Account> account = service.findEmployee(usernameFieldFX.getText(), passwordFieldFX.getText());
        if(account.isEmpty())
        {
            System.out.println("nu e bun");
        }
        else
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/showData.fxml"));
                AnchorPane root = null;
                root = (AnchorPane) loader.load();
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Main menu");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                //dialogStage.initOwner(primaryStage);
                Scene scene = new Scene(root, 1024, 800);
                dialogStage.setScene(scene);

                MainMenuController mainMenuController = loader.getController();
                mainMenuController.setParameters(service, dialogStage);
                dialogStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
