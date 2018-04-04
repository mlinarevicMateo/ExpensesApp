/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expensesapp.model;

import static expensesapp.model.ExpensesModel.monthNames;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author mlinar
 */
public class UsersModel {
    SimpleStringProperty username = new SimpleStringProperty();
    SimpleStringProperty userSince = new SimpleStringProperty();
    SimpleStringProperty addedBy = new SimpleStringProperty();
    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty adminSince = new SimpleStringProperty();
    public UsersModel(int id, String username, String userSince, String addedBy) {
        this.id = new SimpleIntegerProperty (id);
        this.username = new SimpleStringProperty (username);
        this.userSince = new SimpleStringProperty (userSince);
        this.addedBy = new SimpleStringProperty(addedBy);
        }
     public UsersModel(int id, String username, String userSince, String adminSince, String addedBy) {
        this.id = new SimpleIntegerProperty (id);
        this.username = new SimpleStringProperty (username);
        this.userSince = new SimpleStringProperty (userSince);
        this.adminSince = new SimpleStringProperty (adminSince);
        this.addedBy = new SimpleStringProperty(addedBy);
        }
   public String getUsername () {
        return username.get();
    }
    public void setUsername(String username) {
        this.username = new SimpleStringProperty(username);
    }
    public String getUserSince () {
        return userSince.get();
    }
    public void setUserSince(String userSince) {
        this.userSince = new SimpleStringProperty(userSince);
    }
    public String getAdminSince () {
        return adminSince.get();
    }
    public void setAdminSince(String adminSince) {
        this.adminSince = new SimpleStringProperty(adminSince);
    }
    public String getAddedBy () {
        return addedBy.get();
    }
    public void setAddedBy(String addedBy) {
        this.addedBy = new SimpleStringProperty(addedBy);
    }
    public Integer getId () {
        return id.get();
    }
        
    public static boolean addNewUser(String username, String password) {
        Database db = new Database();
        String day = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        String month = monthNames[Calendar.getInstance().get(Calendar.MONTH)];
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String date = day + "/" + month + "/" + year;
         //First see if there is already user with the same username, if there is then error
         PreparedStatement ps = db.exec("SELECT COUNT(*) AS count FROM users WHERE username = ?");
         try {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt("count");
            if(count <= 0){
                PreparedStatement pS = db.exec("INSERT INTO users (username, password, user_since, added_by, admin, admin_since) VALUES (?, ?, ?, ?, 0, '')");
                try{
                    pS.setString(1, username);
                    pS.setString(2, password);
                    pS.setString(3, date);
                    pS.setInt(4, LoggedUserModel.user_id);
                    pS.executeUpdate();
                    return true;
                }catch(SQLException ex){
                    System.out.println("There was an error.");
                    return false;
                }
            }
        } catch (SQLException ex) {
            System.out.println("There was a error during iteration: " + ex.getMessage());
            return false;
        }
         return false;
    }

    public static ObservableList<UsersModel> usersList() {
       ObservableList<UsersModel> list = FXCollections.observableArrayList();
       Database db = new Database();
       PreparedStatement ps = db.exec("SELECT u2.id, u2.username, u2.user_since, u1.username FROM users u1 JOIN users u2 ON u2.added_by = u1.id WHERE u2.admin = 0");
       try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new UsersModel(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
            }
        } catch (SQLException ex) {
            System.out.println("There was a error during iteration: " + ex.getMessage());
        }
        return list; 
    }
    
    public static ObservableList<UsersModel> adminList() {
       ObservableList<UsersModel> list = FXCollections.observableArrayList();
       Database db = new Database();
       PreparedStatement ps = db.exec("SELECT u2.id, u2.username, u2.user_since, u2.admin_since, u1.username FROM users u1 JOIN users u2 ON u2.added_by = u1.id WHERE u1.admin = 1 AND u2.admin = 1");
       try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new UsersModel(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }
        } catch (SQLException ex) {
            System.out.println("There was a error during iteration: " + ex.getMessage());
        }
        return list; 
    }
    
    public static boolean addAdmin(String username){
       Database db = new Database();
       String day = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        String month = monthNames[Calendar.getInstance().get(Calendar.MONTH)];
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String date = day + "/" + month + "/" + year;
       PreparedStatement ps = db.exec("UPDATE users SET admin = 1, admin_since = ? WHERE username = ?");
       try{
           ps.setString(1, date);
           ps.setString(2, username);
           ps.executeUpdate();
           return true;
       }catch(SQLException ex){
            System.out.println("There was a error: " + ex.getMessage());
            return false;
       }
    }
    
    public static boolean removeAdmin(String username){
       Database db = new Database();
       PreparedStatement ps =db.exec("SELECT COUNT(*) AS count FROM users WHERE admin = 1");
        try {
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
               if(rs.getInt("count") < 2){
                   return false;
               }else{
                    ps = db.exec("UPDATE users SET admin = 0, admin_since = '' WHERE username = ?");
                    try{
                        ps.setString(1, username);
                        ps.executeUpdate();
                        return true;
                    }catch(SQLException ex){
                         System.out.println("There was a error: " + ex.getMessage());
                         return false;
                    }
               }
            }
        } catch (SQLException ex) {
            System.out.println("There was a error: " + ex.getMessage());
            return false;
        }
        return false;
    }
    public static boolean deleteUser(String username){
        Database db = new Database();
        PreparedStatement ps = db.exec("DELETE FROM users WHERE username = ?");
        
        try {
            ps.setString(1, username);
            ps.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UsersModel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
