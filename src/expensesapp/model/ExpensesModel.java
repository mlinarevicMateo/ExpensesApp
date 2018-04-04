/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expensesapp.model;

import expensesapp.controller.ExpensesController;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author mlinar
 */
public class ExpensesModel {
    SimpleStringProperty name = new SimpleStringProperty();
    SimpleStringProperty price= new SimpleStringProperty();
    SimpleStringProperty date= new SimpleStringProperty();
    SimpleStringProperty time= new SimpleStringProperty();
    SimpleIntegerProperty id= new SimpleIntegerProperty();

    public ExpensesModel(int id, String name, String price, String date, String time) {
        this.id = new SimpleIntegerProperty (id);
        this.name = new SimpleStringProperty (name);
        this.price = new SimpleStringProperty (price);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
    }

    public String getName () {
        return name.get();
    }
    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }
    public String getPrice () {
        return price.get();
    }
    public void setPrice(String price) {
        this.price = new SimpleStringProperty(price);
    }
    public String getDate () {
        return date.get();
    }
    public void setDate(String date) {
        this.date = new SimpleStringProperty(date);
    }
    public String getTime () {
        return time.get();
    }
    public void setTime(String time) {
        this.time = new SimpleStringProperty(time);
    }
    public Integer getId () {
        return id.get();
    }
    public static String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static String getTotalExpenses(String month, String year){
        Database db = new Database();
        PreparedStatement ps = db.exec("SELECT price FROM expenses WHERE user_id = ? AND date LIKE ? AND is_visible = 1");
        float totalExpenses = 0;
        try {
            ps.setInt(1, LoggedUserModel.user_id);
            ps.setString(2, "%" + month + "/" + year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                totalExpenses += Float.valueOf(rs.getString("price"));
            }
        } catch (SQLException ex) {
            System.out.println("Nastala je greška prilikom iteriranja: " + ex.getMessage());
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return String.valueOf(df.format(totalExpenses));
    }
    public static boolean setExpense(int user_id, String nameOfExpense, String priceOfExpense){
        Database db = new Database();
        
        
        String day = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        String month = monthNames[Calendar.getInstance().get(Calendar.MONTH)];
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String date = day + "/" + month + "/" + year;
        String time = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) + ":" +
                        String.valueOf(Calendar.getInstance().get(Calendar.MINUTE)) + ":" +
                        String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
        
        PreparedStatement ps = db.exec("INSERT INTO expenses (name, price, date, time, user_id) VALUES (?,?,?,?,?)");
        try{
                    ps.setString(1, nameOfExpense);
                    ps.setString(2, priceOfExpense);
                    ps.setString(3, date);
                    ps.setString(4, time);
                    ps.setInt(5, LoggedUserModel.user_id);
                    ps.executeUpdate();
                }catch(SQLException ex) {
                    System.out.println("There was a error: "+ex.getMessage());
                    return false;
                }
                return true;
    }
    
    public static boolean delete(String name, String price, String date, String time){
        Database db = new Database();
        PreparedStatement ps = db.exec("UPDATE expenses SET is_visible = 0 WHERE name=? AND price=? AND date=? AND time = ? AND user_id = ?");
        
        try{
                    ps.setString(1, name);
                    ps.setString(2, price);
                    ps.setString(3, date);
                    ps.setString(4, time);
                    ps.setInt(5, LoggedUserModel.user_id);
                    ps.executeUpdate();
                }catch(SQLException ex) {
                    System.out.println("There was a error: "+ex.getMessage());
                    return false;
                }
        return true;
    } 
    
    public static ObservableList<ExpensesModel> expensesList (String month, String year) {
        ObservableList<ExpensesModel> list = FXCollections.observableArrayList();
        
        Database db = new Database();
        PreparedStatement ps = db.exec("SELECT * FROM expenses WHERE user_id = ? AND date LIKE ? AND is_visible = 1");
        int totalExpenses = 0;
        try {
            ps.setInt(1, LoggedUserModel.user_id);
            ps.setString(2, "%" + month + "/" + year);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                totalExpenses += Float.valueOf(rs.getString("price"));
                list.add(new ExpensesModel(rs.getInt("id"), rs.getString("name"), rs.getString("price"), rs.getString("date"), rs.getString("time")));
            }
        } catch (SQLException ex) {
            System.out.println("Nastala je greška prilikom iteriranja: " + ex.getMessage());
        }
        return list;
    }
}
