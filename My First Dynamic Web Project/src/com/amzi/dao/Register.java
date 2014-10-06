package com.amzi.dao;  
  
import java.sql.PreparedStatement;  
import java.sql.SQLException;  
import java.sql.ResultSet;
  
public class Register {  
    
	private int userId = -1;
	private String dateRegistered = null;
	private String errorMessage = null;
	
	public int getUserId(){
		return userId;
	}
	
	public String getDateRegistered(){
		return dateRegistered;
	}
	
	public String getErrorMessage(){
		return errorMessage;
	}
	
	public boolean validate(String name, String pass, String pass2) {          
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
        		errorMessage = "Error with registration. Username was not entered";
        		throw registrationError;
        	}
        	
        	if(pass.equals("")){
        		System.out.println("Password was not entered, throwing java.lang.Exception.");
        		errorMessage = "Error with registration. Password was not entered";
        		throw registrationError;
        	}
        	
        	if(pass2.equals("")){
        		System.out.println("Password was not rentered, throwing java.lang.Exception.");
        		errorMessage = "Error with registration. Password was not reentered";
        		throw registrationError;
        	}
        	
        	if(!pass.equals(pass2)){
        		System.out.println("The passwords that were entered do not match, throwing java.lang.Exception.");
        		errorMessage = "Error with registration. The passwords that were entered do not match";
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
           
            userId = rs.getInt("UserId");
            dateRegistered = rs.getString("DateRegistered");
  
            
        // should totally catch this.. com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry 'Derek' for key 'User_Unique_Username'    
        } catch (SQLException sqlE) { 
        	
        	//should have a rollback here
        	
        	connectionManager.closeConnection();
        	
        	System.out.println("Error inserting information of new user into registration table, throwing SQLException.");
        	sqlE.printStackTrace();
       	 	errorMessage = "Error completing registration";
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