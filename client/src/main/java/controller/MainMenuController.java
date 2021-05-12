package controller;

import domain.Account;
import domain.Game;
import domain.Ticket;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import observer.IObserver;
import service.IService;
import service.ServiceException;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

public class MainMenuController implements IObserver {

    @FXML
    public TextField _name;
    @FXML
    public TextField _seats;
    @FXML
    public TextField _gameID;
    @FXML
    public TextField _homeTeam;
    @FXML
    public TextField _awayTeam;
    @FXML
    public TextField _date;
    @FXML
    public TextField _ticketPrice;
    @FXML
    public TextField _seatsAvailable;

    ObservableList<Game> gameList = FXCollections.observableArrayList();
    ObservableList<Ticket> ticketList = FXCollections.observableArrayList();

    @FXML
    TableView<Game> tableGame;
    @FXML
    TableColumn<Game, Integer> idg;
    @FXML
    TableColumn<Game, String> homeTeam;
    @FXML
    TableColumn<Game, String> awayTeam;
    @FXML
    TableColumn<Game, Date> date;
    @FXML
    TableColumn<Game, Double> ticketPrice;
    @FXML
    TableColumn<Game, Integer> seatsAvailable;

    @FXML
    TableView<Ticket> tableTicket;
    @FXML
    TableColumn<Ticket, Integer> idt;
    @FXML
    TableColumn<Ticket, String> name;
    @FXML
    TableColumn<Ticket, Integer> seats;
    @FXML
    TableColumn<Ticket, Integer> gameID;

    private IService service;
    private Stage stage;

    @FXML
    public void initialize() {

    }

    public void setParameters(IService service, Stage dialogstage) throws ServiceException {
        this.service = service;
        this.stage = dialogstage;

        service.addObserver(this);


        tableTicket.setItems(ticketList);
        //idt.setCellValueFactory(new PropertyValueFactory<Ticket, Integer>("idt"));
        name.setCellValueFactory(new PropertyValueFactory<Ticket, String>("name"));
        seats.setCellValueFactory(new PropertyValueFactory<Ticket, Integer>("seats"));
        gameID.setCellValueFactory(new PropertyValueFactory<Ticket, Integer>("gameID"));


        tableGame.setItems(gameList);
        //idg.setCellValueFactory(new PropertyValueFactory<Game, Integer>("idg"));
        homeTeam.setCellValueFactory(new PropertyValueFactory<Game, String>("homeTeam"));
        awayTeam.setCellValueFactory(new PropertyValueFactory<Game, String>("awayTeam"));
        date.setCellValueFactory(new PropertyValueFactory<Game, Date>("date"));
        ticketPrice.setCellValueFactory(new PropertyValueFactory<Game, Double>("ticketPrice"));
        seatsAvailable.setCellValueFactory(new PropertyValueFactory<Game, Integer>("seatsAvailable"));

        /*
        ticketList = FXCollections.observableArrayList(service.getAllTickets());
        tableTicket.setItems(ticketList);
        */

        gameList = FXCollections.observableArrayList(service.getAllGames());
        tableGame.setItems(gameList);
    }

    public void refresh(MouseEvent mouseEvent) throws ServiceException {
        ticketList = FXCollections.observableArrayList(service.getAllTickets());
        tableTicket.setItems(ticketList);

        gameList = FXCollections.observableArrayList(service.getAllGames());
        tableGame.setItems(gameList);
    }

    public void addTicket(MouseEvent mouseEvent) throws ServiceException {
        String var1 = _name.getText();
        Integer var2 = Integer.parseInt(_seats.getText());
        Integer var3 = Integer.parseInt(_gameID.getText());
        int var0 = new Random().nextInt();

        service.addTicket(var0, var1, var2, var3);

    }

    public void addGame(MouseEvent mouseEvent) {
        String var1 = _homeTeam.getText();
        String var2 = _awayTeam.getText();
        String var3 = _date.getText();
        String[] tokens = var3.split(",");

        Date var4 = new Date(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
        Double var5 = Double.parseDouble(_ticketPrice.getText());
        Integer var6 = Integer.parseInt(_seatsAvailable.getText());

        int var0 = new Random().nextInt();
        service.addGame(var0, var1, var2, var4, var5, var6);

    }

    @Override
    public void seatsReserved(Game game) throws IOException {
        Platform.runLater(() -> {
            for (var x : gameList) {
                if (x.getID().equals(game.getID())) {
                    x.setSeats(game.getSeatsAvailable());
                    tableGame.refresh();
                    return;
                }
            }

        });
    }
}
