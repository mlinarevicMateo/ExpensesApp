/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expensesapp.controller;

import expensesapp.model.BalanceModel;
import expensesapp.model.ExpensesModel;
import expensesapp.model.LoggedUserModel;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mlinar
 */
public class ExpensesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ChoiceBox choiceBoxMonth;
    @FXML
    private ChoiceBox choiceBoxYear;
    @FXML
    private Text balance;
    @FXML 
    private TextField amountToAdd;
    @FXML
    private Label addBalanceLabel;
    @FXML
    private Button logout_button;
    @FXML 
    private TextField expenseName;
    @FXML 
    private TextField expensePrice;
    @FXML
    private Label addExpenseLabel;
    @FXML
    TableView expensesTableView;
    @FXML
    TableColumn numbTableColumn;
    @FXML
    TableColumn nameTableColumn;
    @FXML
    TableColumn priceTableColumn;
    @FXML
    TableColumn dateTableColumn;
    @FXML
    TableColumn timeTableColumn;
    @FXML
    private Text expenses;
    @FXML
    private Text remainingBalance;
    @FXML
    private Text aproximate;

    public void listMonths(){
        List<String> mon = new ArrayList<String>();
        mon.add("January"); mon.add("February"); mon.add("March"); mon.add("April");
        mon.add("May"); mon.add("June"); mon.add("July"); mon.add("August"); mon.add("September");
        mon.add("October"); mon.add("November"); mon.add("December");
        ObservableList months = FXCollections.observableArrayList(mon);
        choiceBoxMonth.setItems(months);
        choiceBoxMonth.getSelectionModel().select(Calendar.getInstance().get(Calendar.MONTH));
    }
    
    public void listYears(){
        List<String> year = new ArrayList<String>();
        year.add("2017"); year.add("2018"); year.add("2019"); 
        year.add("2020"); year.add("2021");
        ObservableList years = FXCollections.observableArrayList(year);
        choiceBoxYear.setItems(years);
        choiceBoxYear.setValue(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
    }
    
    public void getBalance(String username){
        String month = choiceBoxMonth.getValue().toString();
        String year = choiceBoxYear.getValue().toString();
        balance.setText(BalanceModel.getBalance(username, month, year)+" €");
    }
    public void getBalance(String username, String month, String year){
        balance.setText(BalanceModel.getBalance(username, month, year)+" €");
    }
   
    public void addBalance(){
        String amount = amountToAdd.getText();
        //if field is empty or doesn't have alphanumeric values(if user entered just whitespaces
        if(amount.equals("") || amount.contains(" ")){
            addBalanceLabel.setTextFill(Color.RED);
            addBalanceLabel.setText("Field is empty or invalid!");
            return;
        }
        else if(amount.matches("-?\\d+(\\.\\d+)?") && Float.parseFloat(amount) < 0){
            addBalanceLabel.setTextFill(Color.RED);
            addBalanceLabel.setText("No negative numbers!");
            return;
        }
        else if(amount.matches("-?\\d+(\\.\\d+)?")){
            String month = choiceBoxMonth.getValue().toString();
            String year = choiceBoxYear.getValue().toString();
            String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            if(month.equals(monthNames[Calendar.getInstance().get(Calendar.MONTH)]) && year.equals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))){
                if(BalanceModel.setBalance(amount, LoggedUserModel.username, month, year)){
                    addBalanceLabel.setTextFill(Color.GREEN);
                    addBalanceLabel.setText("Amount successfully added.");
                    getBalance(LoggedUserModel.username);
                    String totalExpenses = expenses.getText();
                    String totalBalance = balance.getText();
                    DecimalFormat df = new DecimalFormat("#.##");
                    remainingBalance.setText(String.valueOf(df.format(Float.valueOf(totalBalance.substring(0, totalBalance.length() - 2)) - Float.valueOf(totalExpenses.substring(0, totalExpenses.length() - 2)))) + " €");
                    amountToAdd.setText("");
                    aproximateExpenses(choiceBoxMonth.getValue().toString(), choiceBoxYear.getValue().toString());
                }
                else{
                    addBalanceLabel.setTextFill(Color.RED);
                    addBalanceLabel.setText("There was a problem (Database).");
                    return;
                }
            }
            else{
                addBalanceLabel.setTextFill(Color.RED);
                addBalanceLabel.setText("Use current month/year");
                return;
            }
        }else{
            addBalanceLabel.setTextFill(Color.RED);
            addBalanceLabel.setText("Use only numeric values!");
            return;
        }
    }
    public void addExpense(){
        String expName = expenseName.getText();
        String expPrice = expensePrice.getText();
        String remainingMoney = remainingBalance.getText();
        if(expName.equals("") || expPrice.equals("") || expPrice.contains(" ")){
            addExpenseLabel.setTextFill(Color.RED);
            addExpenseLabel.setText("Field is empty or invalid!");
            return;
        }
        else if(Float.parseFloat(expPrice) > Float.parseFloat(remainingMoney.substring(0,remainingMoney.length()-2))){
            addExpenseLabel.setTextFill(Color.RED);
            addExpenseLabel.setText("Not enough money");
            return;
        }
        else if(expPrice.matches("-?\\d+(\\.\\d+)?") && Float.parseFloat(expPrice) < 0){
            addExpenseLabel.setTextFill(Color.RED);
            addExpenseLabel.setText("No negative numbers!");
            return;
        }
        else if(expPrice.matches("-?\\d+(\\.\\d+)?")){
            String month = choiceBoxMonth.getValue().toString();
            String year = choiceBoxYear.getValue().toString();
            String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            if(month.equals(monthNames[Calendar.getInstance().get(Calendar.MONTH)]) && year.equals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))){
                if(ExpensesModel.setExpense(LoggedUserModel.user_id, expName, expPrice)){
                    addExpenseLabel.setTextFill(Color.GREEN);
                    addExpenseLabel.setText("Amount successfully added.");
                    getBalance(LoggedUserModel.username);
                    listExpenses(choiceBoxMonth.getValue().toString(), choiceBoxYear.getValue().toString());
                    showTotalExpenses(choiceBoxMonth.getValue().toString(), choiceBoxYear.getValue().toString());
                    expenseName.setText("");
                    expensePrice.setText("");
                    aproximateExpenses(choiceBoxMonth.getValue().toString(), choiceBoxYear.getValue().toString());
                }
                else{
                    addBalanceLabel.setTextFill(Color.RED);
                    addBalanceLabel.setText("There was a problem (Database).");
                    return;
                }
            }
            else{
                addExpenseLabel.setTextFill(Color.RED);
                addExpenseLabel.setText("Use current month/year");
                return;
            }
        }
        else{
            addExpenseLabel.setTextFill(Color.RED);
            addExpenseLabel.setText("Use only numeric values!");
            return;
        }
    }
    
    public void setListeners(){
        choiceBoxMonth.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
              getBalance(LoggedUserModel.username, (String) choiceBoxMonth.getItems().get((Integer) number2), choiceBoxYear.getValue().toString());
              showTotalExpenses((String) choiceBoxMonth.getItems().get((Integer) number2), choiceBoxYear.getValue().toString());
              listExpenses((String) choiceBoxMonth.getItems().get((Integer) number2), choiceBoxYear.getValue().toString());
              aproximateExpenses((String) choiceBoxMonth.getItems().get((Integer) number2), choiceBoxYear.getValue().toString());
            }
          });
        choiceBoxYear.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
              getBalance(LoggedUserModel.username, choiceBoxMonth.getValue().toString(), (String) choiceBoxYear.getItems().get((Integer) number2));
              showTotalExpenses(choiceBoxMonth.getValue().toString(), (String) choiceBoxYear.getItems().get((Integer) number2));
              listExpenses(choiceBoxMonth.getValue().toString(), (String) choiceBoxYear.getItems().get((Integer) number2));
              aproximateExpenses(choiceBoxMonth.getValue().toString(), (String) choiceBoxYear.getItems().get((Integer) number2));
            }
          });
        logout_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    logOut();
                } catch (IOException ex) {
                    Logger.getLogger(ExpensesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
     public void logOut() throws IOException{
        try {
                    Parent root;
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("expensesapp/view/login.fxml"));
                    Stage stage = new Stage();
                    
                    stage.setTitle("Login");
                    stage.setScene(new Scene(root, 590, 310));
                    stage.resizableProperty().setValue(Boolean.FALSE);
                    stage.show();
                    logout_button.getScene().getWindow().hide();
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
    }
    public void showUsers(){
        try {
                    Parent root;
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("expensesapp/view/users.fxml"));
                    Stage stage = new Stage();
                    
                    stage.setTitle("All users");
                    stage.setScene(new Scene(root, 590, 386));
                    stage.resizableProperty().setValue(Boolean.FALSE);
                    stage.show();
                    logout_button.getScene().getWindow().hide();
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
    }
    public void listExpenses(String month, String year){
        ObservableList<ExpensesModel> data = ExpensesModel.expensesList(month, year);
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<ExpensesModel, String>("Name"));
        priceTableColumn.setCellValueFactory(new PropertyValueFactory<ExpensesModel, String>("Price"));
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<ExpensesModel, String>("Date"));
        timeTableColumn.setCellValueFactory(new PropertyValueFactory<ExpensesModel, String>("Time"));
        expensesTableView.setItems(data);
    } 
    public void showTotalExpenses(String month, String year){
        String totalExpenses = ExpensesModel.getTotalExpenses(month, year)+" €";
        expenses.setText(totalExpenses);
        String totalBalance = balance.getText();
        DecimalFormat df = new DecimalFormat("#.##");
        remainingBalance.setText(String.valueOf(df.format(Float.valueOf(totalBalance.substring(0, totalBalance.length() - 2)) - Float.valueOf(totalExpenses.substring(0, totalExpenses.length() - 2)))) + " €");
    }
    
    public void deleteExpense(){
        expensesTableView.setEditable(true);
        int selected = expensesTableView.getSelectionModel().getSelectedIndex();
        String month = choiceBoxMonth.getValue().toString();
        String year = choiceBoxYear.getValue().toString();
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        
        if(selected >= 0 && month.equals(monthNames[Calendar.getInstance().get(Calendar.MONTH)]) && year.equals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))){
            ExpensesModel toDelete = (ExpensesModel) expensesTableView.getSelectionModel().getSelectedItem();
            if(ExpensesModel.delete(toDelete.getName(), toDelete.getPrice(), toDelete.getDate(), toDelete.getTime())){
                expensesTableView.getItems().remove(selected);
                showTotalExpenses(choiceBoxMonth.getValue().toString(), choiceBoxYear.getValue().toString());
                aproximateExpenses(choiceBoxMonth.getValue().toString(), choiceBoxYear.getValue().toString());
            }
        }
    }
    public void aproximateExpenses(String month, String year){
        float remaining = Float.valueOf(remainingBalance.getText().substring(0, remainingBalance.getText().length() - 2));
        int daysMax = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int remainingDays = daysMax - day;
        DecimalFormat df = new DecimalFormat("#.##");
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        if(month.equals(monthNames[Calendar.getInstance().get(Calendar.MONTH)]) && year.equals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))){
            aproximate.setFill(Color.BLACK);
            aproximate.setText(String.valueOf(df.format(remaining / remainingDays)));
        }
        else{
            aproximate.setFill(Color.RED);
            aproximate.setText("0");
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listMonths();
        listYears();
        getBalance(LoggedUserModel.username);
        setListeners();
        listExpenses(choiceBoxMonth.getValue().toString(), choiceBoxYear.getValue().toString());
        showTotalExpenses(choiceBoxMonth.getValue().toString(), choiceBoxYear.getValue().toString());
        aproximateExpenses(choiceBoxMonth.getValue().toString(), choiceBoxYear.getValue().toString());
    }
    
}
