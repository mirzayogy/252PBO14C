package com.mycompany.pbo14c.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    private final String URL = "jdbc:mysql://localhost:3306/db_perpus";
    private final String USER = "root";
    private final String PASS = "";
    
    public Connection getConnection(){
        Connection con = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Koneksi berhasil");
            return con;
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println("Error");
            System.err.println(ex.toString());
            return null;
        }
    }
    
    public static void main(String[] args) {
        Koneksi koneksi = new Koneksi();
        koneksi.getConnection();
    }
}
