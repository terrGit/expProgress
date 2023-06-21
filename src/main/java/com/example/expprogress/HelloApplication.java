package com.example.expprogress;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class HelloApplication extends Application {
    final static String driver = "com.mysql.cj.jdbc.Driver";
    final static String databaseName = "laundry";
    final static String url = "jdbc:mysql://localhost/" + databaseName ;
    final static String user = "root";
    final static String password = "";



    public static Connection createDatabaseConnection() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, user, password);
        return con;
    }




    @Override
    public void start(Stage stage) throws IOException {




        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 804, 606);

        HelloController helloController = (HelloController) fxmlLoader.getController();

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        helloController.Initialize();
    }

    public static void main(String[] args) {
        launch();
    }
}