package com.amzi.dao;  
  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  

  
public class Login { 
	
	public static String errorMessege = null;
	public static String errorMessegeFR = null;
	
	//could return a user object instead, and if null then there was an error.
    public static User validate(String name, String pass) {          
        User u = null;
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
            	errorMessegeFR = "Il y a eu une erreur de connection. Veuiller entre un Nom D'utilisateur.";
            	throw loginError;
            }
            
            if(pass == ""){
            	System.out.println();
            	errorMessege = "Error with previous login attempt. User password was not entered.";
            	errorMessegeFR = "Il y a eu une erreur de connection. Veuiller entre un Mot de passe.";
            	throw loginError;
            }
            
             //gaining access to the shared database connection
            connectionManager = DbConnection.getInstance();
            
            if(connectionManager.getConnection() == null){
            	errorMessege = "Error communicating with database. Login cannnot be completed.";
            	errorMessegeFR = "Problem de connection avec la base de données. Veuiller essayer à nouveau.";
            	throw loginError;
            }
            
            pst = connectionManager.getConnection().prepareStatement("select * from user where username=? and password=?"); 
            pst.setString(1, name);  
            pst.setString(2, pass);  
  
            rs = pst.executeQuery(); 
            rs.first();
                       
            //if the query does not return any rows. Calling getInt() and getString() will throw an SQLException.
            u = new User(rs.getInt("UserId"), name, pass, rs.getString("DateRegistered"));
            
        } catch (SQLException sqlE) {  
        	
        	connectionManager.closeConnection();
        	System.out.println("The entered username and password do not match registered users, throwing SQLException\n");
        	sqlE.printStackTrace();
        	errorMessege = "Error with previous login attempt. Incorrect Username and Password.";
        	errorMessegeFR = "Il y a eu une erreur de connection. Verifier votre Nom d'Utilisateur et votre Mot de passe.";
        }catch(Exception e){
        	 e.printStackTrace(); //may not be necessary
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
		return u;  
        
    }  
}  