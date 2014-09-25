package com.amzi.dao;  
  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.SQLException;  
import java.sql.ResultSet;
  
public class Register {  
    
	public static String dateRegistered;
	
	public static boolean validate(String name, String pass, String pass2) {          
        boolean status = true;  
        Connection conn = null;  
        PreparedStatement pst = null; 
        ResultSet rs = null;
        Exception passNonMatch = new Exception();
  
        String url = "jdbc:mysql://localhost:3306/";  
        String dbName = "blogsharedatatest";  
        String driver = "com.mysql.jdbc.Driver";  
        String dbUserName = "blogshareuser";  
        String dbPassword = "password";
        
        try {  
           
        	name = name.trim();
        	pass = pass.trim();
        	pass2 = pass2.trim();
        	
        	if(name.equals("")){
        		System.out.println("No username entered, throwing Java.lang.exception");
        		throw passNonMatch;
        	}
        	
        	if(pass.equals("")){
        		System.out.println("No password entered, throwing Java.lang.exception");
        		throw passNonMatch;
        	}
        	
        	if(pass2.equals("")){
        		System.out.println("Password was not rentered, throwing Java.lang.exception");
        		throw passNonMatch;
        	}
        	
        	if(!pass.equals(pass2)){
        		System.out.println("The passwords that were entered do not match, throwing Java.lang.exception");
        		throw passNonMatch;
        	}
        	
        	Class.forName(driver).newInstance();  
            conn = DriverManager  
                    .getConnection(url + dbName, dbUserName, dbPassword);  
  
            pst = conn  
                    .prepareStatement("insert into User values(0, '"+name+"','"+pass+"', curdate() );");  
           
            pst.executeUpdate(); 
            
            pst.close();
            
            pst = conn.prepareStatement("select * from user where username=? and password=?"); 
            
            pst.setString(1, name);  
            pst.setString(2, pass);  
  
            rs = pst.executeQuery(); 
            
            rs.first();
            //if the query works,this should never be null. But do we wanna check just because??
            dateRegistered = rs.getString("DateRegistered");
  
        } catch (Exception e) {  
            System.out.println(e);  
            status = false;
        } finally {  
            if (conn != null) {  
                try {  
                    conn.close();  
                } catch (SQLException e) {  
                    e.printStackTrace();  
                }  
            }  
            if (pst != null) {  
                try {  
                    pst.close();  
                } catch (SQLException e) {  
                    e.printStackTrace();  
                }  
            }  
            if (rs != null) {  
                try {  
                    rs.close();  
                } catch (SQLException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return status;  
    }  
}  