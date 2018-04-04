/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expensesapp.controller;

import expensesapp.model.ExpensesModel;
import expensesapp.model.LoggedUserModel;
import expensesapp.model.UsersModel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mlinar
 */
public class UsersController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button backToExpensesButton;
    @FXML
    private Button addNewUserButton;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML 
    private Label addUserLabel;
    @FXML
    TableView usersTableView;
    @FXML
    TableColumn usernameColumn;
    @FXML
    TableColumn userSinceColumn;
    @FXML
    TableColumn addedByColumn;
    @FXML
    TableView adminsTableView;
    @FXML
    TableColumn usernameColumnAdmin;
    @FXML
    TableColumn userSinceColumnAdmin;
    @FXML
    TableColumn addedByColumnAdmin;
    @FXML
    TableColumn adminSinceColumn;
    public void backToExpenses(){
        try {
                Parent root;
                root = FXMLLoader.load(getClass().getClassLoader().getResource("expensesapp/view/expenses_administrator.fxml"));
                Stage stage = new Stage();
                    
                stage.setTitle("All users");
                stage.setScene(new Scene(root, 590, 490));
                stage.resizableProperty().setValue(Boolean.FALSE);
                stage.show();
                backToExpensesButton.getScene().getWindow().hide();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    public void addNewUser(){
        String userName = username.getText();
        String Password = password.getText();
        
        if(userName.equals("") || Password.equals("")){
            addUserLabel.setTextFill(Color.RED);
            addUserLabel.setText("Invalid data inserted");
        }
        else{
            if(UsersModel.addNewUser(userName, Password)){
                addUserLabel.setTextFill(Color.GREEN);
                addUserLabel.setText("User inserted.");
                username.setText("");
                password.setText("");
                listUsers();
            }
            else{
                addUserLabel.setTextFill(Color.RED);
                addUserLabel.setText("User exists");
            }
        }
    }
    public void listUsers(){
        ObservableList<UsersModel> data = UsersModel.usersList();
        usernameColumn.setCellValueFactory(new PropertyValueFactory<UsersModel, String>("username"));
        userSinceColumn.setCellValueFactory(new PropertyValueFactory<UsersModel, String>("userSince"));
        addedByColumn.setCellValueFactory(new PropertyValueFactory<UsersModel, String>("addedBy"));
        usersTableView.setItems(data);
    } 
    public void listAdmins(){
        ObservableList<UsersModel> data = UsersModel.adminList();
        usernameColumnAdmin.setCellValueFactory(new PropertyValueFactory<UsersModel, String>("username"));
        userSinceColumnAdmin.setCellValueFactory(new PropertyValueFactory<UsersModel, String>("userSince"));
        adminSinceColumn.setCellValueFactory(new PropertyValueFactory<UsersModel, String>("adminSince"));
        addedByColumnAdmin.setCellValueFactory(new PropertyValueFactory<UsersModel, String>("addedBy"));
        adminsTableView.setItems(data);
    }
    public void addAdmin(){
        usersTableView.setEditable(true);
        int selected = usersTableView.getSelectionModel().getSelectedIndex();
        if(selected >= 0){
            UsersModel toAdd = (UsersModel) usersTableView.getSelectionModel().getSelectedItem();
            if(UsersModel.addAdmin(toAdd.getUsername())){
                listAdmins();
                listUsers();
            }
        }
    }
    public void removeAdmin(){
        adminsTableView.setEditable(true);
        int selected = adminsTableView.getSelectionModel().getSelectedIndex();
        if(selected >= 0){
            UsersModel toRemove = (UsersModel) adminsTableView.getSelectionModel().getSelectedItem();
            if(LoggedUserModel.username.equals(toRemove.getUsername())){
                addUserLabel.setTextFill(Color.RED);
                addUserLabel.setText("Can't remove current user");
            }
            else if(UsersModel.removeAdmin(toRemove.getUsername())){
                listAdmins();
                listUsers();
                addUserLabel.setText("");
            }else{
                addUserLabel.setTextFill(Color.RED);
                addUserLabel.setText("Min. 1 admin.");
            }
        }
    }
    public void deleteUser(){
        usersTableView.setEditable(true);
        int selected = usersTableView.getSelectionModel().getSelectedIndex();
        if(selected >= 0){
            UsersModel toDelete = (UsersModel) usersTableView.getSelectionModel().getSelectedItem();
            if(LoggedUserModel.username.equals(toDelete.getUsername())){
                addUserLabel.setTextFill(Color.RED);
                addUserLabel.setText("Can't remove current user");
            }
            else if(UsersModel.deleteUser(toDelete.getUsername())){
                listUsers();
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listUsers();
        listAdmins();
    }    
    
}
