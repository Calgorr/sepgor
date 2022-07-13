package com.example.sepgorfront01;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pkg.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class HelloApplication extends Application {
    public static Socket fileSocket;

    static {
        try {
            fileSocket = new Socket("localhost", 1212);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws IOException, InterruptedException, NoSuchAlgorithmException {
        try {
            new Thread(new FileServerGetter(new ServerSocket(10001))).start();
        } catch (Exception e) {
            System.out.println("couldnt create server");
        }
        HelloController controller = Main.controller;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loginSignUp.fxml"));
        fxmlLoader.setController(controller);

        Scene scene = new Scene(fxmlLoader.load(), 1310, 900);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();


        Main.client = new Client();
        Main.client.connect();
        new RSAKeyPairGenerator();
        Socket socket = Main.client.socket;
        Main.content = new Content("", "", "", "", "", "");
        Main.content.validKey2(Main.client.socket, Main.client.inputClient);


    }

    public static void setUsername(String userName) {
        // com.example.sepgorfront01.HelloController.class.ge
    }

    public static void main(String[] args) {
        launch();
    }
}