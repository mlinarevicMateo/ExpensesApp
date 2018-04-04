/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expensesapp.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author mlinar
 */
public class LoggedUserModel {
    public static int user_id;
    public static String username;
    public static boolean login(String user, String password){
      Database db = new Database();
      PreparedStatement ps = db.exec("SELECT * FROM users WHERE username = ? AND password = ?");
      
      try{
          ps.setString(1, user);
          ps.setString(2, password);
          ResultSet rs = ps.executeQuery();
          
          if(rs.next()){
              user_id = rs.getInt("id");
              username = rs.getString("username");
              return true;
          }
          else{
              return false;
          }
      }catch(SQLException ex) {
            System.out.println("There was a error: "+ex.getMessage());
            return false;
        }
    }
    public static boolean isAdmin(String user){
         Database db = new Database();
      PreparedStatement ps = db.exec("SELECT * FROM users WHERE username = ? AND admin = 1");
      
      try{
          ps.setString(1, user);
          ResultSet rs = ps.executeQuery();
          
          if(rs.next()){
              return true;
          }
          else{
              return false;
          }
      }catch(SQLException ex) {
            System.out.println("There was a error: "+ex.getMessage());
            return false;
        }
    }
}
