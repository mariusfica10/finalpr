import controller.LoginMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import objectprotocol.proxy.ServiceProxyInClient;
import service.IService;

import java.io.IOException;
import java.util.Properties;

public class Main extends Application {

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(Main.class.getResourceAsStream("/chatclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("chat.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("chat.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IService service = new ServiceProxyInClient(serverIP, serverPort);

        //stage e fereastra
        //scena e ce pun in fereastra
        //controller = handler de evenimente (ex: apasare pe buton)
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/loginMenu.fxml"));
        AnchorPane root = loader.load();


        LoginMenuController loginMenuController = loader.getController();
        loginMenuController.setParameters(service);

        primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.setTitle("Login menu");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}