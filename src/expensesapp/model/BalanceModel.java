/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expensesapp.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 *
 * @author mlinar
 */
public class BalanceModel {
    public static String getBalance(String username, String month, String year){
      Database db = new Database();
      PreparedStatement ps = db.exec("SELECT * FROM balances JOIN users u ON balances.user_id = u.id WHERE month = ? AND year = ? AND username = ? AND user_id = ?");
      String toReturn;
      try{
          ps.setString(1, month);
          ps.setString(2, year);
          ps.setString(3, username);
          ps.setInt(4, LoggedUserModel.user_id);
          ResultSet rs = ps.executeQuery();
          String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
          //If there is some money for current month then return that value
          if(rs.next()){
              DecimalFormat df = new DecimalFormat("#.##");
              toReturn = String.valueOf(df.format(Float.valueOf(rs.getString("amount"))));
          }
          //If there is no money for current month (new month or new user)
          else if(month.equals(monthNames[Calendar.getInstance().get(Calendar.MONTH)]) && year.equals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))){
              //Select amount for month before the current one. So user can add money from past month to the present one, without losing data for past month.
              Calendar cal = Calendar.getInstance();
              int mon = cal.get(Calendar.MONTH)-1;
              int y = Integer.parseInt(year);
              
               
              if(mon == -1){
                    mon = 11;
                    y--;
                }
              PreparedStatement pS = db.exec("SELECT amount FROM balances JOIN users u ON balances.user_id = u.id WHERE month = ? AND year = ? AND username = ? AND user_id = ?");
                
              pS.setString(1, monthNames[mon]);
              pS.setString(2, String.valueOf(y));
              pS.setString(3, username);
              pS.setInt(4, LoggedUserModel.user_id);
              rs = pS.executeQuery();
                
              //If there is some money in the month before, adding it to current month
              if(rs.next()){
                  int pastMonthMoney = Integer.parseInt(rs.getString("amount"));                    
                  setBalance(String.valueOf(pastMonthMoney), username, month, year);
                  toReturn = "0 (" + String.valueOf(pastMonthMoney) + ")";
              }else{
                    toReturn = "0";
                }
            }
          else{
              //In case function is called for some other month that is not current one
              toReturn = "0";
          }
        }catch(SQLException ex) {
            System.out.println("There was a error: "+ex.getMessage());
            toReturn = "0";
        }
      return toReturn;
    }
    public static boolean setBalance(String amount, String username, String month, String year){
        Database db = new Database();
        //First selecting amount for the current month
        PreparedStatement ps = db.exec("SELECT * FROM balances JOIN users u ON balances.user_id = u.id WHERE month = ? AND year = ? AND username = ? AND user_id = ?");
        try{
            ps.setString(1, month);
            ps.setString(2, year);
            ps.setString(3, username);
            ps.setInt(4, LoggedUserModel.user_id);
            ResultSet rs = ps.executeQuery();
            //If there is some money, add it to new amount and send to database
            if(rs.next()){
                float value = Float.parseFloat(rs.getString("amount"));
                float updateValue = value + Float.parseFloat(amount);
                ps = db.exec("UPDATE balances SET amount = ? WHERE month=? AND year=? AND user_id=?");
                try{
                    ps.setString(1, String.valueOf(updateValue));
                    ps.setString(2, month);
                    ps.setString(3, year);
                    ps.setInt(4, LoggedUserModel.user_id);
                    ps.executeUpdate();
                }catch(SQLException ex) {
                    System.out.println("There was a error: "+ex.getMessage());
                    return false;
                }
                return true;
            }
            //If there is no money, then just insert new value into db
            else{
                ps = db.exec("INSERT INTO balances (amount, month, year, user_id) VALUES (?,?,?,?)");
                try{
                    ps.setString(1, amount);
                    ps.setString(2, month);
                    ps.setString(3, year);
                    ps.setInt(4, LoggedUserModel.user_id);
                    ps.execute();
                }catch(SQLException ex) {
                    System.out.println("There was a error: "+ex.getMessage());
                    return false;
                }   
                return true;
            }
        }catch(SQLException ex) {
              System.out.println("There was a error: "+ex.getMessage());
              return false;
          }
    }  
}
