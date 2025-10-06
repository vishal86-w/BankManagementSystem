package com.bank.dao;

import java.sql.*;

public class ConnectionProvider {

	public static Connection getCon()
    {
        
       try{
          Class.forName("com.mysql.cj.jdbc.Driver");
          Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sprint_bank?useSSL=false","root","root");
          return con;
       } 
       catch(Exception e)
       {
           e.printStackTrace();
           return null;
       }
    }

}
