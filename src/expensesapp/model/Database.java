/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expensesapp.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author mlinar
 */
public class Database extends Connection {
   private Statement query;
    private PreparedStatement execQuery;
    
    public static final Database DB = new Database();

    public Database () {
        super();
        try {
            this.query = this.connection.createStatement();
            this.query.execute("SET NAMES utf8");
        } catch (SQLException ex) {
            System.out.println ("Error " + ex.getMessage());
        }
    }

    public ResultSet select (String sql) {
        try {
            this.query = this.connection.createStatement();
            return this.query.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println ("Error" + e.getMessage());
            return null;
        }
    }

    public PreparedStatement exec (String sql) {
        try {
            
            this.execQuery = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            return this.execQuery;
        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }
        return null;
    } 
}
