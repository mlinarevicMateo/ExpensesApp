/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expensesapp.model;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author mlinar
 */
public class Connection {
  private String host;
    private String username;
    private String password;
    private String db;

    protected java.sql.Connection connection;

    public Connection () {
        this.host = "localhost";
        this.username = "root";
        this.password = "";
        this.db = "expensesApp";
        this.connect();
    }

    public Connection (String host, String username, String password, String db) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.db = db;
        this.connect();
    }

    public void connect () {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://"+this.host+"/"+this.db+"?"
                    + "user="+this.username+"&password="+this.password);
        } catch (ClassNotFoundException e) {
            System.out.println ("Class for connectin to MYSQL can not be found.");
        } catch (SQLException e) {
            System.out.println ("Couldn't connect to database");
        }
    }

    public void disconnect () {
        try {
            this.connection.close();
        } catch (SQLException e) {
            System.out.println ("Connection couldn't be disconnected.");
        }
    }

}
