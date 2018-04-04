/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expensesapp.controller;

import expensesapp.model.LoggedUserModel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceBoxBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mlinar
 */
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    TextField login_username;
    
    @FXML
    Label login_status_bar;
    
    @FXML
    PasswordField login_password;
    
    @FXML 
    ChoiceBox choiceBox;
    
    @FXML
    public void logIn(ActionEvent e){
        String username = login_username.getText();
        String password = login_password.getText();
        
        
        if(username.equals("") || password.equals("")){
            login_status_bar.setText("Please fill in all fields.");
        }
        else{
            if (LoggedUserModel.login(username, password)) {
                if(LoggedUserModel.isAdmin(username)){
                    try {
                    login_status_bar.setTextFill(Color.GREEN);
                    login_status_bar.setText("Successfully logged in.");
                    Parent root;
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("expensesapp/view/expenses_administrator.fxml"));
                    Stage stage = new Stage();
                    
                    stage.setTitle("ExpensesApp - Administrator");
                    stage.setScene(new Scene(root, 590, 490));
                    stage.resizableProperty().setValue(Boolean.FALSE);
                    stage.show();
                    login_status_bar.getScene().getWindow().hide();
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
                else{
                    try {
                    login_status_bar.setTextFill(Color.GREEN);
                    login_status_bar.setText("Successfully logged in.");
                    Parent root;
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("expensesapp/view/expenses.fxml"));
                    Stage stage = new Stage();
                    
                    stage.setTitle("ExpensesApp");
                    stage.setScene(new Scene(root, 600, 500));
                    stage.show();
                    login_status_bar.getScene().getWindow().hide();
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
            } else {
                login_status_bar.setText("Your credentials are invalid");
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
