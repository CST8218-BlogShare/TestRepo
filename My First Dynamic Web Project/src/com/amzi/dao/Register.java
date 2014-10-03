package com.amzi.dao;  
  
import java.sql.PreparedStatement;  
import java.sql.SQLException;  
import java.sql.ResultSet;
  
public class Register {  
    
	public static String dateRegistered = null;
	public static String errorMessege = null;
	
	public static boolean validate(String name, String pass, String pass2) {          
        boolean status = true;  
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
  
            pst = connectionManager.getConnection().prepareStatement("insert into User values(0, '"+name+"','"+pass+"', curdate() );");  
           
            pst.executeUpdate(); 
            
            //closing the connection to prepare for the next prepared statement.
            pst.close();
            
            pst = connectionManager.getConnection().prepareStatement("select * from user where username=? and password=?"); 
            
            pst.setString(1, name);  
            pst.setString(2, pass);  
  
            rs = pst.executeQuery(); 
            
            rs.first();
            //if the query works,this should never be null. But do we wanna check just because??
            dateRegistered = rs.getString("DateRegistered");
  
            
        // should totally catch this.. com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry 'Derek' for key 'User_Unique_Username'    
        } catch (SQLException sqlE) { 
        	
        	//should have a rollback here
        	
        	try{
        		connectionManager.getConnection().close();
        	}catch(SQLException sqlCloseE){
        		sqlCloseE.printStackTrace();
        	}
        	
        	System.out.println("Error inserting information of new user into registration table, throwing SQLException.");
        	sqlE.printStackTrace();
       	 	errorMessege = "Error completing registration";
       	 	status = false;
       	 	
        } catch (Exception e) { 
        	 e.printStackTrace(); //may not be necessary
            status = false;
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
        return status;  
    }  
}  