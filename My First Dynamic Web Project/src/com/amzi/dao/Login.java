package com.amzi.dao;  
  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  

  
public class Login { 
	
	public static String dateRegistered = null;
	public static String errorMessege = null;
	
    public static boolean validate(String name, String pass) {          
        boolean status = true;  
        Connection conn = null;  
        PreparedStatement pst = null;  
        ResultSet rs = null;  

        
        String url = "jdbc:mysql://localhost:3306/";  
        String dbName = "blogsharedatatest";  
        String driver = "com.mysql.jdbc.Driver";  
        String dbUserName = "blogshareuser";  
        String dbPassword = "password";
        
        Exception loginError = new Exception();
        
         try {  
            Class.forName(driver).newInstance();  
            
            conn = DriverManager  
                    .getConnection(url + dbName, dbUserName, dbPassword);  
  
            name = name.trim();
            pass = pass.trim();
            
            if(name == ""){
            	System.out.println("Username was not entered, throwing java.lang.Exception.");
            	errorMessege = "Error with previous login attempt. Username was not entered.";
            	throw loginError;
            }
            
            if(pass == ""){
            	System.out.println("Password was not entered, throwing java.lang.Exception.");
            	errorMessege = "Error with previous login attempt. User password was not entered.";
            	throw loginError;
            }
            
            pst = conn.prepareStatement("select * from user where username=? and password=?"); 
            
            pst.setString(1, name);  
            pst.setString(2, pass);  
  
            rs = pst.executeQuery(); 
            
            
            rs.first();
            //if the query works,this should never be null. But do we wanna check just because..yeaah
            dateRegistered = rs.getString("DateRegistered");
            
            //status = rs.next();
        } catch (SQLException sqlE) {  
        	System.out.println("The entered username and password do not match registered users, throwing SQLException");
        	System.out.println(sqlE);
        	errorMessege = "Error with previous login attempt. Incorrect Username and Password.";
        	status = false;
        }catch(Exception e){//might not want to do this and let page server handle error by redirecting to custom page
        	 System.out.println(e);
             status = false;
        }
         finally {  
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