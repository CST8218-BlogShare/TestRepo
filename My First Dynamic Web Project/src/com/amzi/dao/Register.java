package com.amzi.dao;  
  
import java.sql.PreparedStatement;  
import java.sql.SQLException;  
import java.sql.ResultSet;
  
public class Register {  
	
	public static String errorMessege = null;
	
	//could return a user object instead and if null an error occured.
	public static User validate(String name, String pass, String pass2) {          
        User u = null;
        PreparedStatement pst = null; 
        ResultSet rs = null;
        DbConnection connectionManager = null;
        Exception registrationError = new Exception();
 
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
        	
        	
        	//gaining access to the shared database connection.
        	connectionManager = DbConnection.getInstance();
  
        	if(connectionManager.getConnection() == null){
            	errorMessege = "Error communicating with database. Registration cannnot be completed.";
            	throw registrationError;
            }
        	
            pst = connectionManager.getConnection().prepareStatement("insert into User values(0, '"+name+"','"+pass+"', curdate() );");  
           
            pst.executeUpdate(); 
            
            //closing the connection to prepare for the next prepared statement.
            pst.close();
            
            pst = connectionManager.getConnection().prepareStatement("select * from user where username=? and password=?"); 
            
            pst.setString(1, name);  
            pst.setString(2, pass);  
  
            rs = pst.executeQuery(); 
            rs.first();
           
            //if the query does not return any rows. Calling getInt() and getString() will throw an SQLException.
            u = new User(rs.getInt("UserId"), name, pass, rs.getString("DateRegistered"));
            
        // should totally catch this.. com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry 'Derek' for key 'User_Unique_Username'    
        } catch (SQLException sqlE) { 
        	
        	//should have a rollback here
        	
        	connectionManager.closeConnection();
        	
        	System.out.println("Error inserting information of new user into registration table, throwing SQLException.");
        	sqlE.printStackTrace();
       	 	errorMessege = "Error completing registration";
       	 	
        } catch (Exception e) { 
        	 e.printStackTrace(); 
        } finally {  
            if (pst != null) {  
                try {  
                    pst.close();  
                } catch (SQLException sqlE) {  
                    sqlE.printStackTrace();  
                }  
            }  
            if (rs != null) {  
                try {  
                    rs.close();  
                } catch (SQLException sqlE) {  
                    sqlE.printStackTrace();  
                }  
            }  
        }  
        return u;  
    }  
}  