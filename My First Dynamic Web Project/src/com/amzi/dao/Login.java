package com.amzi.dao;  
  
 import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  

  
public class Login { 
	
	public static int userId = -1;
	public static String dateRegistered = null;
	public static String errorMessege = null;
	
    public static boolean validate(String name, String pass) {          
        boolean status = true;  
        PreparedStatement pst = null;  
        ResultSet rs = null; 
        DbConnection connectionManager = null;

        Exception loginError = new Exception();
        
         try {  
           
        	name = name.trim();
            pass = pass.trim();
            
            if(name == ""){
            	System.out.println("Username was not entered, throwing java.lang.Exception.\n");
            	errorMessege = "Error with previous login attempt. Username was not entered.";
            	throw loginError;
            }
            
            if(pass == ""){
            	System.out.println("Password was not entered, throwing java.lang.Exception.\n");
            	errorMessege = "Error with previous login attempt. User password was not entered.";
            	throw loginError;
            }
            
             //gaining access to the shared database connection
            connectionManager = DbConnection.getInstance();
            
            pst = connectionManager.getConnection().prepareStatement("select * from user where username=? and password=?"); 
            
            pst.setString(1, name);  
            pst.setString(2, pass);  
  
            rs = pst.executeQuery(); 
            
            
            rs.first();
            //if the query does not return any rows. this will throw an SQLException
            
            userId = rs.getInt("UserId");
            dateRegistered = rs.getString("DateRegistered");
            
            //status = rs.next();
        } catch (SQLException sqlE) {  
        	
        	connectionManager.closeConnection();
        	
        	System.out.println("The entered username and password do not match registered users, throwing SQLException\n");
        	sqlE.printStackTrace();
        	errorMessege = "Error with previous login attempt. Incorrect Username and Password.";
        	
        	status = false;
        }catch(Exception e){
        	 e.printStackTrace(); //may not be necessary
             status = false;
        }
         finally { 
        	//we now have to manage closing the connection a different way...at logout...
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