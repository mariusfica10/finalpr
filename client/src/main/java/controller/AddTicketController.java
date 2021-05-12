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

import java.io.IOException;
import java.util.Optional;

public class AddTicketController {

    private IService service;
    private Stage stage;


    public void setParametersTicket(IService service, Stage stage)
    {
        this.service = service;
        this.stage = stage;
    }

}

