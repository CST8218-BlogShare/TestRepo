package com.amzi.dao;  
  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.SQLException;  
import java.sql.ResultSet;
  
public class Register {  
    
	public static String dateRegistered = null;
	public static String errorMessege = null;
	
	public static boolean validate(String name, String pass, String pass2) {          
        boolean status = true;  
        Connection conn = null;  
        PreparedStatement pst = null; 
        ResultSet rs = null;
        Exception registrationError = new Exception();
  
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
        		System.out.println("Username was not entered, throwing java.lang.Exception.");
        		errorMessege = "Error with registration. Username was not entered";
        		throw registrationError;
        	}
        	
        	if(pass.equals("")){
        		System.out.println("Password was not entered, throwing java.lang.Exception.");
        		errorMessege = "Error with registration. Password was not entered";
        		throw registrationError;
        	}
        	
        	if(pass2.equals("")){
        		System.out.println("Password was not rentered, throwing java.lang.Exception.");
        		errorMessege = "Error with registration. Password was not reentered";
        		throw registrationError;
        	}
        	
        	if(!pass.equals(pass2)){
        		System.out.println("The passwords that were entered do not match, throwing java.lang.Exception.");
        		errorMessege = "Error with registration. The passwords that were entered do not match";
        		throw registrationError;
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
  
            
        // should totally catch this.. com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry 'Derek' for key 'User_Unique_Username'    
        } catch (SQLException sqlE) { 
        	System.out.println("Error inserting information of new user into registration table, throwing SQLException.");
        	System.out.println(sqlE);
       	 	errorMessege = "Error completing registration";
       	 	status = false;
        } catch (Exception e) { //might not want to do this and let page server handle error by redirecting to custom page 
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